package com.backend.clinicaOdontologica.service.impl;

import com.backend.clinicaOdontologica.dto.entrada.TurnoEntradaDto;
import com.backend.clinicaOdontologica.dto.salida.OdontologoSalidaDto;
import com.backend.clinicaOdontologica.dto.salida.PacienteSalidaDto;
import com.backend.clinicaOdontologica.dto.salida.TurnoSalidaDto;
import com.backend.clinicaOdontologica.entity.Odontologo;
import com.backend.clinicaOdontologica.entity.Paciente;
import com.backend.clinicaOdontologica.entity.Turno;
import com.backend.clinicaOdontologica.exceptions.BadRequestException;
import com.backend.clinicaOdontologica.exceptions.ResourceNotFoundException;
import com.backend.clinicaOdontologica.repository.TurnoRepository;
import com.backend.clinicaOdontologica.service.ITurnoService;
import com.backend.clinicaOdontologica.utils.JsonPrinter;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import java.util.List;


@Service
public class TurnoService implements ITurnoService {

    private final Logger LOGGER = LoggerFactory.getLogger(TurnoService.class);
    private final TurnoRepository turnoRepository;
    private final PacienteService pacienteService;
    private final OdontologoService odontologoService;
    private final ModelMapper modelMapper;


    public TurnoService(TurnoRepository turnoRepository, PacienteService pacienteService, OdontologoService odontologoService, ModelMapper modelMapper) {
        this.turnoRepository = turnoRepository;
        this.pacienteService = pacienteService;
        this.odontologoService = odontologoService;
        this.modelMapper = modelMapper;
        configureMapping();

    }


    @Override
    public TurnoSalidaDto registrarTurno(TurnoEntradaDto turno) throws BadRequestException {
        LOGGER.info("TurnoEntradaDto: {}", JsonPrinter.toString(turno));

        PacienteSalidaDto pacienteSalidaDto = pacienteService.buscarPacientePorDNI(turno.getDniPaciente());

        OdontologoSalidaDto odontologoSalidaDto = odontologoService.buscarOdontologoPorNumeroMatricula(turno.getNumeroMatriculaOdontologo());

        if (pacienteSalidaDto != null) {
            if (odontologoSalidaDto != null) {

                Turno entidadTurno = modelMapper.map(turno, Turno.class);

                Paciente paciente = modelMapper.map(pacienteSalidaDto, Paciente.class);
                Odontologo odontologo = modelMapper.map(odontologoSalidaDto, Odontologo.class);
                entidadTurno.setPaciente(paciente);
                entidadTurno.setOdontologo(odontologo);
                LOGGER.info("EntidadTurno: {}", JsonPrinter.toString(entidadTurno));

                Turno turnoRegistrado = turnoRepository.save(entidadTurno);
                LOGGER.info("TurnoRegistrado: {}", JsonPrinter.toString(turnoRegistrado));

                TurnoSalidaDto turnoSalidaDto = modelMapper.map(turnoRegistrado, TurnoSalidaDto.class);
                LOGGER.info("TurnoSalidaDto: {}", JsonPrinter.toString(turnoSalidaDto));

                return turnoSalidaDto;

            } else {
                LOGGER.error("Odontólogo con número de matrícula {} no encontrado", turno.getNumeroMatriculaOdontologo());
                throw new BadRequestException("El Odontólogo no fue encontrado");
            }

        } else {
            if (odontologoSalidaDto == null) {
                LOGGER.error("Ni paciente ni odontólogo fueron encontrados");
                throw new BadRequestException("Ni odontólogo ni paciente fueron encontrados");

            } else {
                LOGGER.error("Paciente con DNI {} no encontrado", turno.getDniPaciente());
                throw new BadRequestException("El paciente no fue encontrado");
            }
        }
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

        LOGGER.info("Listado de todos los turnos: {}", JsonPrinter.toString(turnoSalidaDtos));
        return turnoSalidaDtos;
    }

    @Override
    public void eliminarTurno(Long id) throws ResourceNotFoundException{
        if (buscarTurnoPorId(id)!=null){

            turnoRepository.deleteById(id);
            LOGGER.warn("Se ha eliminado el turno con id {}", id);
        } else{

            throw new ResourceNotFoundException("No existe el turno con id " + id);
        }
    }

    
   @Override
   public TurnoSalidaDto actualizarTurno(TurnoEntradaDto turnoEntradaDto, Long id) throws BadRequestException {

       Turno turnoAActualizar = turnoRepository.findById(id).orElse(null);

       if (turnoAActualizar == null) {
           LOGGER.error("No fue posible actualizar el turno porque no se encuentra en nuestra base de datos");

       }

       PacienteSalidaDto pacienteSalidaDto = pacienteService.buscarPacientePorDNI(turnoEntradaDto.getDniPaciente());
       OdontologoSalidaDto odontologoSalidaDto = odontologoService.buscarOdontologoPorNumeroMatricula(turnoEntradaDto.getNumeroMatriculaOdontologo());


       if (pacienteSalidaDto == null || odontologoSalidaDto == null) {
           LOGGER.error("No se encontraron el paciente u odontólogo existentes. Paciente: {}, Odontólogo: {}",
                   pacienteSalidaDto != null ? pacienteSalidaDto.getDni() : "No encontrado",
                   odontologoSalidaDto != null ? odontologoSalidaDto.getNumeroMatricula(): "No encontrado");

           throw new BadRequestException("Paciente u odontólogo no encontrados");
       }

       Paciente paciente = modelMapper.map(pacienteSalidaDto, Paciente.class);
       Odontologo odontologo = modelMapper.map(odontologoSalidaDto, Odontologo.class);


       turnoAActualizar.setPaciente(paciente);
       turnoAActualizar.setOdontologo(odontologo);
       turnoAActualizar.setFechaHora(turnoEntradaDto.getFechaHora());


       Turno turnoActualizado = turnoRepository.save(turnoAActualizar);
       LOGGER.info("Turno actualizado: {}", JsonPrinter.toString(turnoActualizado));


       TurnoSalidaDto turnoSalidaDto = modelMapper.map(turnoActualizado, TurnoSalidaDto.class);
       LOGGER.info("TurnoSalidaDto actualizado: {}", JsonPrinter.toString(turnoSalidaDto));

       return turnoSalidaDto;
   }


    private void configureMapping() {
        modelMapper.typeMap(TurnoEntradaDto.class, Turno.class)
                .addMappings(mapper -> {
                    mapper.map(TurnoEntradaDto::getFechaHora, Turno::setFechaHora);

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



