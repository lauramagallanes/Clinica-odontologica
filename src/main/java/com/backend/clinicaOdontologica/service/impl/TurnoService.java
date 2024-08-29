package com.backend.clinicaOdontologica.service.impl;

import com.backend.clinicaOdontologica.dto.entrada.TurnoEntradaDto;
import com.backend.clinicaOdontologica.dto.salida.TurnoSalidaDto;
import com.backend.clinicaOdontologica.entity.Odontologo;
import com.backend.clinicaOdontologica.entity.Turno;
import com.backend.clinicaOdontologica.utils.LocalDateTimeAdapter;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TurnoService {
    //Usamos slf4j como libreria de logging
    private final Logger LOGGER = LoggerFactory.getLogger(TurnoService.class);
    private final IDao<Turno> turnoIDao;
    private final ModelMapper modelMapper;

    //cuando defino algo como final, le debo asignar un valor, para eso creamos el constructor--> inyectamos el model mapper en el servicio a través del constructor:
    public TurnoService(IDao<Turno> turnoIDao, ModelMapper modelMapper) {
        this.turnoIDao = turnoIDao;
        this.modelMapper = modelMapper;
        // llamamos al metodo configureMapping que creamos abajo:
        configureMapping();

    }

    @Override
    public List<TurnoSalidaDto> listarTurnos() {
        List<TurnoSalidaDto> turnoSalidaDtos = turnoIDao.listarTodos()
                .stream()
                .map(turno -> modelMapper.map(turno, TurnoSalidaDto.class))
                .toList();
        //ACÁ NO SE MY BIEN COMO SE PONDRÍA (ROMI):
        LOGGER.info("Listado de todos los turnos: {}", LocalDateTimeAdapter.deserialize(turnoSalidaDtos));
        return turnoSalidaDtos;
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
