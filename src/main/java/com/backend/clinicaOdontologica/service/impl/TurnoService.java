package com.backend.clinicaOdontologica.service.impl;

import com.backend.clinicaOdontologica.dto.entrada.TurnoEntradaDto;
import com.backend.clinicaOdontologica.dto.salida.PacienteSalidaDto;
import com.backend.clinicaOdontologica.dto.salida.TurnoSalidaDto;
import com.backend.clinicaOdontologica.entity.Odontologo;
import com.backend.clinicaOdontologica.entity.Paciente;
import com.backend.clinicaOdontologica.entity.Turno;
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
    //Usamos slf4j como libreria de logging
    private final Logger LOGGER = LoggerFactory.getLogger(TurnoService.class);
    private final TurnoRepository turnoRepository;
    private final ModelMapper modelMapper;

    //cuando defino algo como final, le debo asignar un valor, para eso creamos el constructor--> inyectamos el model mapper en el servicio a través del constructor:
    public TurnoService(TurnoRepository turnoRepository, ModelMapper modelMapper) {
        this.turnoRepository = turnoRepository;
        this.modelMapper = modelMapper;
        // llamamos al metodo configureMapping que creamos abajo:
        configureMapping();

    }

    @Override
    public TurnoSalidaDto registrarTurno(TurnoEntradaDto turno) {
        //Hay que terminar de armarlo ESTO LO DEJÉ ASÍ PORQ NO ESTABA INDICADO HACER (ROMI)
        return null;
    }

    @Override
    public TurnoSalidaDto buscarTurnoPorId(Long id) {
        Turno turnoBuscado = turnoRepository.findById(id).orElse(null);
        LOGGER.info("Turno buscado: {}", JsonPrinter.toString(turnoBuscado));
        TurnoSalidaDto turnoEncontrado = null;
        if (turnoBuscado !=null){
            turnoEncontrado= modelMapper.map(turnoBuscado, TurnoSalidaDto.class);
            LOGGER.info("Turno encontroado: {}", JsonPrinter.toString(turnoEncontrado));
        }
        else LOGGER.error("No se ha encontrado el turno con id {}", id);
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
    public void eliminarTurno(Long id) {
        if (buscarTurnoPorId(id) != null) {
            //llamada a la capa repositorio para eliminar
            turnoRepository.deleteById(id);
            LOGGER.warn("Se ha eliminado el turno con id {}", id);
        } else {
            //Excepcion
        }
    }

    @Override
    public TurnoSalidaDto actualizarTurno(TurnoEntradaDto turnoEntradaDto, Long id) {
        //busco el turno a actualizar en la base de datos
        Turno turnoAActualizar = turnoRepository.findById(id).orElse(null); //podria usar el metodo de buscar por id que creamos antes ya que tiene los loggs
        //mapeo el turnoEntradaDto que es lo que me llega por parametro para castearlo a una entity (Turno)
        Turno turnoRecibido = modelMapper.map(turnoEntradaDto, Turno.class);

        TurnoSalidaDto turnoSalidaDto = null;
        if (turnoAActualizar != null) {
            // seteo la id de turnoRecibido usando la id de turnoAActualizar
            turnoRecibido.setId(turnoAActualizar.getId());
//VER SI CORRESPONDE PONER ALGO DE ODONTOLOGO O PACIENTE (ROMI)
            turnoAActualizar = turnoRecibido; //asi me evisto hacer los setters para cada atributo
            turnoRepository.save(turnoAActualizar);
        } else {
            LOGGER.error("No fue posible actualizar el turno por que no se encuentra en nuestra base de datos");
        }
        return turnoSalidaDto;

    }


    private void configureMapping() {
        // Le indico que cuando le llega algo del tipo TurnoEntradaDto en este servicio, tiene que transformarlo en un objeto del tipo Turno:
        modelMapper.typeMap(TurnoEntradaDto.class, Turno.class)
                //Primera Lambda (Nombre):
                //
                //turnoEntradaDto -> (String) turnoEntradaDto.getPacienteEntradaDto().getNombre(): Aquí, turnoEntradaDto es el objeto TurnoEntradaDto. Desde este objeto, accedes al objeto PacienteEntradaDto y obtienes el nombre con getNombre(). El cast (String) asegura que el valor obtenido se trate como un String.
                //(turno, nombre) -> turno.getPaciente().setNombre((String) nombre): Aquí, turno es el objeto de destino, es decir, la entidad Turno. El valor nombre obtenido se asigna al método setNombre del objeto Paciente dentro del Turno.
                .addMappings(mapper ->
                        mapper.map(
                                turnoEntradaDto -> turnoEntradaDto.getPacienteEntradaDto().getNombre(),
                                (turno, nombre) -> turno.getPaciente().setNombre((String) nombre)
                        )
                )
                .addMappings(mapper ->
                        mapper.map(
                                TurnoEntradaDto::getOdontologoEntradaDto,
                                (turno, odontologo) -> turno.setOdontologo((Odontologo) odontologo)
                        )
                );
        modelMapper.typeMap(Turno.class, TurnoSalidaDto.class)
                .addMappings(mapper -> mapper.map(Turno::getPaciente, TurnoSalidaDto::setPacienteSalidaDto))
                .addMappings(mapper -> mapper.map(Turno::getOdontologo, TurnoSalidaDto::setOdontologoSalidaDto));
    }
}
