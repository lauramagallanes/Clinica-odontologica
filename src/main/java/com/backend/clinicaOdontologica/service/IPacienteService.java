package com.backend.clinicaOdontologica.service;

import com.backend.clinicaOdontologica.dto.entrada.PacienteEntradaDto;
import com.backend.clinicaOdontologica.dto.salida.PacienteSalidaDto;
import com.backend.clinicaOdontologica.exceptions.ResourceNotFoundException;


import java.util.List;

public interface IPacienteService {

    PacienteSalidaDto registrarPaciente(PacienteEntradaDto paciente);
    PacienteSalidaDto buscarPacientePorId(Long id);
    PacienteSalidaDto buscarPacientePorDNI(int dni);

    List<PacienteSalidaDto> listarPacientes();

    void eliminarPaciente(Long id) throws ResourceNotFoundException;
    PacienteSalidaDto actualizarPaciente(PacienteEntradaDto pacienteEntradaDto, Long id);
}
