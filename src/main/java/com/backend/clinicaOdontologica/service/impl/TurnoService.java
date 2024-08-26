package com.backend.clinicaOdontologica.service.impl;

import com.backend.clinicaOdontologica.dto.entrada.TurnoEntradaDto;
import com.backend.clinicaOdontologica.dto.salida.PacienteSalidaDto;
import com.backend.clinicaOdontologica.dto.salida.TurnoSalidaDto;
import com.backend.clinicaOdontologica.entity.Paciente;
import com.backend.clinicaOdontologica.entity.Turno;
import com.backend.clinicaOdontologica.repository.IDao;
import com.backend.clinicaOdontologica.utils.JsonPrinter;
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
                //invoco un metodo a traves de una clase con :: (referencia al metodo de una clase)

                //INVESTIGAR!! ACÁ SERÍA ALGO DE BUSCAR SOLO LOS ATRIBUTOS QUE ME INTERESA MAPPEAR ENTRADA SALIDA: NOMBRE Y APELLIDO (ROMI)
                .addMappings(mapper -> mapper.map(TurnoEntradaDto::getPacienteEntradaDto.getNombre, Turno::setPaciente.setNombre))
                .addMappings(mapper -> mapper.map(TurnoEntradaDto::getOdontologoEntradaDto, Turno::setOdontologo));
        modelMapper.typeMap(Turno.class, TurnoSalidaDto.class)
                .addMappings(mapper -> mapper.map(Turno::getPaciente, TurnoSalidaDto::setPacienteSalidaDto))
                .addMappings(mapper -> mapper.map(Turno::getOdontologo, TurnoSalidaDto::setOdontologoSalidaDto));
    }
}
