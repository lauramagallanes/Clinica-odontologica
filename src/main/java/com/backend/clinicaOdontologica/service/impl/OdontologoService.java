package com.backend.clinicaOdontologica.service.impl;
import com.backend.clinicaOdontologica.dto.entrada.OdontologoEntradaDto;
import com.backend.clinicaOdontologica.dto.salida.OdontologoSalidaDto;
import com.backend.clinicaOdontologica.entity.Odontologo;
import com.backend.clinicaOdontologica.exceptions.BadRequestException;
import com.backend.clinicaOdontologica.exceptions.ResourceNotFoundException;
import com.backend.clinicaOdontologica.repository.OdontologoRepository;
import com.backend.clinicaOdontologica.service.IOdontologoService;
import com.backend.clinicaOdontologica.utils.JsonPrinter;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OdontologoService implements IOdontologoService {

    private final Logger LOGGER = LoggerFactory.getLogger(OdontologoService.class);
    private OdontologoRepository odontologoRepository;
    private final ModelMapper modelMapper;

    public OdontologoService(OdontologoRepository odontologoRepository, ModelMapper modelMapper) {
        this.odontologoRepository = odontologoRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public OdontologoSalidaDto registrarOdontologo(OdontologoEntradaDto odontologo) {
        LOGGER.info("OdontologoEntradaDto: {}", JsonPrinter.toString(odontologo));
        Odontologo entidadOdontologo = modelMapper.map(odontologo, Odontologo.class);
        LOGGER.info("EntidadOdontologo: {}", JsonPrinter.toString(entidadOdontologo));
        Odontologo odontologoRegistrado = odontologoRepository.save(entidadOdontologo);
        LOGGER.info("OdontologoRegistrado: {}", JsonPrinter.toString(odontologoRegistrado));
        OdontologoSalidaDto odontologoSalidaDto = modelMapper.map(odontologoRegistrado, OdontologoSalidaDto.class);
        LOGGER.info("OdontologoSalidaDto: {}", JsonPrinter.toString(odontologoSalidaDto));

        return odontologoSalidaDto;
    }

    @Override
    public OdontologoSalidaDto buscarOdontologoPorId(Long id) {
        Odontologo odontologoBuscado = odontologoRepository.findById(id).orElse(null);
        LOGGER.info("Odontologo buscado: {}", JsonPrinter.toString(odontologoBuscado));
        OdontologoSalidaDto odontologoEncontrado= null;
        if(odontologoBuscado != null){
            odontologoEncontrado = modelMapper.map(odontologoBuscado, OdontologoSalidaDto.class);
            LOGGER.info("Odontologo encontroado: {}", JsonPrinter.toString(odontologoEncontrado));
        } else LOGGER.error("No se ha encontrado el odontologo con id {}", id);

        return odontologoEncontrado;
    }

    @Override
    public OdontologoSalidaDto buscarOdontologoPorNumeroMatricula(int numeroMatricula) {
        Odontologo odontologoBuscado = odontologoRepository.findByNumeroMatricula(numeroMatricula).orElse(null);
        LOGGER.info("Odontologo buscado: {}", JsonPrinter.toString(odontologoBuscado));
        OdontologoSalidaDto odontologoEncontrado= null;
        if(odontologoBuscado != null){
            odontologoEncontrado = modelMapper.map(odontologoBuscado, OdontologoSalidaDto.class);
            LOGGER.info("Odontologo encontroado: {}", JsonPrinter.toString(odontologoEncontrado));
        } else LOGGER.error("No se ha encontrado el odontologo con número de matrícula {}", numeroMatricula);

        return odontologoEncontrado;
    }

    @Override
    public List<OdontologoSalidaDto> listarOdontologos() {
        List<OdontologoSalidaDto> odontologoSalidaDtos = odontologoRepository.findAll()
                .stream()
                .map(odontologo -> modelMapper.map(odontologo, OdontologoSalidaDto.class))
                .toList();
        LOGGER.info("Listado de todos los odontologos: {}", JsonPrinter.toString(odontologoSalidaDtos));
        return odontologoSalidaDtos;
    }

    @Override
    public void eliminarOdontologo(Long id) throws ResourceNotFoundException, BadRequestException{
        if(buscarOdontologoPorId(id) != null){
            try {
                odontologoRepository.deleteById(id);
                LOGGER.warn("Se ha eliminado el odontólogo con id {}", id);
            } catch (DataIntegrityViolationException exception){
                LOGGER.error("No se puede eliminar el odontólogo con id {} porque está asociado a un turno", id);
                throw new BadRequestException("No se puede eliminar el odontólogo porque está asociado a un turno.");
            }


        } else {
            throw new ResourceNotFoundException("No existe el paciente con id " + id);
        }
    }

    @Override
    public OdontologoSalidaDto actualizarOdontologo(OdontologoEntradaDto odontologoEntradaDto, Long id) throws ResourceNotFoundException {
        Odontologo odontologoAActualizar = odontologoRepository.findById(id).orElse(null);
        Odontologo odontologoRecibido = modelMapper.map(odontologoEntradaDto, Odontologo.class);

        OdontologoSalidaDto odontologoSalidaDto = null;
        if (odontologoAActualizar != null){
            odontologoRecibido.setId(odontologoAActualizar.getId());
            odontologoAActualizar = odontologoRecibido;
            odontologoRepository.save(odontologoAActualizar);
            odontologoSalidaDto = modelMapper.map(odontologoAActualizar, OdontologoSalidaDto.class);
            LOGGER.warn("OdontologoActualizado: {}", JsonPrinter.toString(odontologoAActualizar));
        } else {
            LOGGER.error("No fue posible actualizar el odontólogo por que no se encuentra en nuestra base de datos");
            throw new ResourceNotFoundException("No fue posible actualizar el paciente por que no se encuentra en nuestra base de datos");
        }
        return odontologoSalidaDto;
    }


}
