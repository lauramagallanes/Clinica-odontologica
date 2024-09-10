package com.backend.clinicaOdontologica.service.impl;

import com.backend.clinicaOdontologica.dto.entrada.TurnoEntradaDto;
import com.backend.clinicaOdontologica.dto.salida.PacienteSalidaDto;
import com.backend.clinicaOdontologica.dto.salida.TurnoSalidaDto;
import com.backend.clinicaOdontologica.entity.Odontologo;
import com.backend.clinicaOdontologica.entity.Paciente;
import com.backend.clinicaOdontologica.entity.Turno;
import com.backend.clinicaOdontologica.exceptions.ResourceNotFoundException;
import com.backend.clinicaOdontologica.repository.OdontologoRepository;
import com.backend.clinicaOdontologica.repository.PacienteRepository;
import com.backend.clinicaOdontologica.repository.TurnoRepository;
import com.backend.clinicaOdontologica.service.ITurnoService;
import com.backend.clinicaOdontologica.utils.JsonPrinter;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class TurnoService implements ITurnoService {
    //Usamos slf4j como libreria de logging
    private final Logger LOGGER = LoggerFactory.getLogger(TurnoService.class);
    private final TurnoRepository turnoRepository;
    private final PacienteRepository pacienteRepository;
    private final OdontologoRepository odontologoRepository;
    private final ModelMapper modelMapper;

    //cuando defino algo como final, le debo asignar un valor, para eso creamos el constructor--> inyectamos el model mapper en el servicio a través del constructor:
    public TurnoService(TurnoRepository turnoRepository, PacienteRepository pacienteRepository, OdontologoRepository odontologoRepository, ModelMapper modelMapper) {
        this.turnoRepository = turnoRepository;
        this.pacienteRepository = pacienteRepository;
        this.odontologoRepository = odontologoRepository;
        this.modelMapper = modelMapper;
        // llamamos al metodo configureMapping que creamos abajo:
        configureMapping();

    }


    @Override
    public TurnoSalidaDto registrarTurno(TurnoEntradaDto turno) {
        LOGGER.info("TurnoEntradaDto: {}", JsonPrinter.toString(turno));

        // Buscar paciente por DNI
        Optional<Paciente> optionalPaciente = pacienteRepository.findByDni(turno.getDniPacienteEntradaDto());
        Paciente paciente = optionalPaciente.orElse(null);

        // Buscar odontólogo por nombre y apellido
        Optional<Odontologo> optionalOdontologo = odontologoRepository.findByNombreOdontologoAndApellidoOdontologo(
                turno.getNombreOdontologoEntradaDto(),
                turno.getApellidoOdontologoEntradaDto());
        Odontologo odontologo = optionalOdontologo.orElse(null);

        if (paciente == null) {
            if (odontologo != null) {
                LOGGER.error("Paciente con DNI {} no encontrado", turno.getDniPacienteEntradaDto());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Paciente no encontrado");
            } else {
                LOGGER.error("ni paciente ni odontólogo fueron encontrados");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ni odontólogo ni paciente fueron  encontrados");
            }
        }

        if (odontologo == null) {
            LOGGER.error("Odontólogo con nombre {} y apellido {} no encontrado",
                    turno.getNombreOdontologoEntradaDto(),
                    turno.getApellidoOdontologoEntradaDto());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Odontólogo no encontrado");
        }


        // Crear la entidad Turno a partir del TurnoEntradaDto
        Turno entidadTurno = modelMapper.map(turno, Turno.class);
        // Asignar el paciente y odontólogo encontrados a la entidad Turno
        entidadTurno.setPaciente(paciente);
        entidadTurno.setOdontologo(odontologo);
        LOGGER.info("EntidadTurno: {}", JsonPrinter.toString(entidadTurno));

        Turno turnoRegistrado = turnoRepository.save(entidadTurno);
        LOGGER.info("TurnoRegistrado: {}", JsonPrinter.toString(turnoRegistrado));


        // Mapear el turno registrado a DTO de salida
        TurnoSalidaDto turnoSalidaDto = modelMapper.map(turnoRegistrado, TurnoSalidaDto.class);
        LOGGER.info("TurnoSalidaDto: {}", JsonPrinter.toString(turnoSalidaDto));

        return turnoSalidaDto;

    }


    @Override
    public TurnoSalidaDto buscarTurnoPorId(Long id) {
        Turno turnoBuscado = turnoRepository.findById(id).orElse(null);
        LOGGER.info("Turno buscado: {}", JsonPrinter.toString(turnoBuscado));
        TurnoSalidaDto turnoEncontrado = null;
        if (turnoBuscado != null) {
            turnoEncontrado = modelMapper.map(turnoBuscado, TurnoSalidaDto.class);
            LOGGER.info("Turno encontrado: {}", JsonPrinter.toString(turnoEncontrado));
        } else LOGGER.error("No se ha encontrado el turno con id {}", id);
        return turnoEncontrado;
    }

    @Override
    public List<TurnoSalidaDto> listarTurnos() {
        List<TurnoSalidaDto> turnoSalidaDtos = turnoRepository.findAll()
                .stream()
                .map(turno -> modelMapper.map(turno, TurnoSalidaDto.class))
                .toList();
        //ACÁ NO SE MY BIEN COMO SE PONDRÍA (ROMI): //Ahi lo arreglé, va con JsonPrinter.toString
        LOGGER.info("Listado de todos los turnos: {}", JsonPrinter.toString(turnoSalidaDtos));
        return turnoSalidaDtos;
    }

    @Override
    public void eliminarTurno(Long id) throws ResourceNotFoundException{
        if (buscarTurnoPorId(id)!=null){
            //llamada a la capa repositorio para eliminar
            turnoRepository.deleteById(id);
            LOGGER.warn("Se ha eliminado el turno con id {}", id);
        } else{

            throw new ResourceNotFoundException("No existe el turno con id " + id); //El msje de la excepcion es para el cliente, y el logger es para nosotros los dev
        }
    }

   /* @Override
    public TurnoSalidaDto actualizarTurno(TurnoEntradaDto turnoEntradaDto, Long id) {
        //busco el turno a actualizar en la base de datos
        Turno turnoAActualizar = turnoRepository.findById(id).orElse(null); //podria usar el metodo de buscar por id que creamos antes ya que tiene los loggs
        //mapeo el turnoEntradaDto que es lo que me llega por parametro para castearlo a una entity (Turno)
        Turno turnoRecibido = modelMapper.map(turnoEntradaDto, Turno.class);

        TurnoSalidaDto turnoSalidaDto = null;
        if (turnoAActualizar != null) {
            // seteo la id de turnoRecibido usando la id de turnoAActualizar
            turnoRecibido.setId(turnoAActualizar.getId());
            Paciente paciente = pacienteRepository.findByDni(turnoAActualizar.getPaciente().getDni())
                    .orElse(null);
            Odontologo odontologo = odontologoRepository.findByNombreOdontologoAndApellidoOdontologo(
                            turnoAActualizar.getOdontologo().getNombreOdontologo(),
                            turnoAActualizar.getOdontologo().getApellidoOdontologo())
                    .orElse(null);
//VER SI CORRESPONDE PONER ALGO DE ODONTOLOGO O PACIENTE (ROMI)
            turnoRecibido.getPaciente().setDni(turnoAActualizar.getPaciente().getDni());
            turnoRecibido.getOdontologo().setNombreOdontologo(turnoAActualizar.getOdontologo().getNombreOdontologo());
            turnoRecibido.getOdontologo().setApellidoOdontologo(turnoAActualizar.getOdontologo().getApellidoOdontologo());
            turnoRecibido.setFechaHora(turnoAActualizar.getFechaHora());
            turnoAActualizar = turnoRecibido; //asi me evisto hacer los setters para cada atributo
            turnoRepository.save(turnoAActualizar);
        } else {
            LOGGER.error("No fue posible actualizar el turno por que no se encuentra en nuestra base de datos");
        }

        return turnoSalidaDto; //da siemrpe nulo, lo tengo que vichar (romi)
    } */
   @Override
   public TurnoSalidaDto actualizarTurno(TurnoEntradaDto turnoEntradaDto, Long id) {
       // Buscar el turno existente por ID
       Turno turnoAActualizar = turnoRepository.findById(id).orElse(null);

       if (turnoAActualizar == null) {
           LOGGER.error("No fue posible actualizar el turno porque no se encuentra en nuestra base de datos");

       }

       // Buscar paciente por DNI
       Optional<Paciente> optionalPaciente = pacienteRepository.findByDni(turnoEntradaDto.getDniPacienteEntradaDto());
       Paciente paciente = optionalPaciente.orElse(null);

       // Buscar odontólogo por nombre y apellido
       Optional<Odontologo> optionalOdontologo = odontologoRepository.findByNombreOdontologoAndApellidoOdontologo(
               turnoEntradaDto.getNombreOdontologoEntradaDto(),
               turnoEntradaDto.getApellidoOdontologoEntradaDto());
       Odontologo odontologo = optionalOdontologo.orElse(null);

       if (paciente == null || odontologo == null) {
           LOGGER.error("No se encontraron el paciente u odontólogo existentes. Paciente: {}, Odontólogo: {}",
                   paciente != null ? paciente.getDni() : "No encontrado",
                   odontologo != null ? odontologo.getNombreOdontologo() + " " + odontologo.getApellidoOdontologo() : "No encontrado");
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Paciente u odontólogo no encontrados");
       }

       // Actualizar las propiedades del turno existente
       turnoAActualizar.setPaciente(paciente);
       turnoAActualizar.setOdontologo(odontologo);
       turnoAActualizar.setFechaHora(turnoEntradaDto.getFechaHora());

       // Guardar el turno actualizado
       Turno turnoActualizado = turnoRepository.save(turnoAActualizar);
       LOGGER.info("Turno actualizado: {}", JsonPrinter.toString(turnoActualizado));

       // Mapear a DTO de salida
       TurnoSalidaDto turnoSalidaDto = modelMapper.map(turnoActualizado, TurnoSalidaDto.class);
       LOGGER.info("TurnoSalidaDto actualizado: {}", JsonPrinter.toString(turnoSalidaDto));

       return turnoSalidaDto;
   }


    private void configureMapping() {
        modelMapper.typeMap(TurnoEntradaDto.class, Turno.class)
                .addMappings(mapper -> {
                    mapper.map(TurnoEntradaDto::getFechaHora, Turno::setFechaHora);
                    // No intentes mapear las entidades directamente aquí (xq no sé aun si existen) (ROMI)
                });

        modelMapper.typeMap(Turno.class, TurnoSalidaDto.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getPaciente().getNombrePaciente(), TurnoSalidaDto::setNombrePacienteSalidaDto);
                    mapper.map(src -> src.getPaciente().getApellidoPaciente(), TurnoSalidaDto::setApellidoPacienteSalidaDto);
                    mapper.map(src -> src.getOdontologo().getNombreOdontologo(), TurnoSalidaDto::setNombreOdontologoSalidaDto);
                    mapper.map(src -> src.getOdontologo().getApellidoOdontologo(), TurnoSalidaDto::setApellidoOdontologoSalidaDto);
                    mapper.map(Turno::getFechaHora, TurnoSalidaDto::setFechaHora);
                });
    }

}



