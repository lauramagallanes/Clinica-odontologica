package com.backend.clinicaOdontologica.service;

import com.backend.clinicaOdontologica.dto.entrada.OdontologoEntradaDto;
import com.backend.clinicaOdontologica.dto.salida.OdontologoSalidaDto;
import com.backend.clinicaOdontologica.exceptions.BadRequestException;
import com.backend.clinicaOdontologica.exceptions.ResourceNotFoundException;


import java.util.List;

public interface IOdontologoService {
    OdontologoSalidaDto registrarOdontologo(OdontologoEntradaDto odontologo);
    OdontologoSalidaDto buscarOdontologoPorId(Long id);
    OdontologoSalidaDto buscarOdontologoPorNumeroMatricula(int numeroMatricula);
    List<OdontologoSalidaDto> listarOdontologos();
    void eliminarOdontologo(Long id) throws ResourceNotFoundException, BadRequestException;
    OdontologoSalidaDto actualizarOdontologo(OdontologoEntradaDto odontologoEntradaDto, Long id) throws ResourceNotFoundException;

}
