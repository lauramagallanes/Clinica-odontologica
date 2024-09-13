package com.backend.clinicaOdontologica.service.impl;

import com.backend.clinicaOdontologica.dto.salida.DomicilioSalidaDto;
import com.backend.clinicaOdontologica.dto.salida.OdontologoSalidaDto;
import com.backend.clinicaOdontologica.dto.salida.PacienteSalidaDto;
import com.backend.clinicaOdontologica.dto.salida.TurnoSalidaDto;
import com.backend.clinicaOdontologica.entity.Domicilio;
import com.backend.clinicaOdontologica.entity.Odontologo;
import com.backend.clinicaOdontologica.entity.Paciente;
import com.backend.clinicaOdontologica.exceptions.BadRequestException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.backend.clinicaOdontologica.dto.entrada.TurnoEntradaDto;
import com.backend.clinicaOdontologica.entity.Turno;
import com.backend.clinicaOdontologica.repository.TurnoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class TurnoServiceTest {

    private final TurnoRepository turnoRepositoryMock = mock(TurnoRepository.class);
    private final PacienteService pacienteServiceMock = mock(PacienteService.class);
    private final OdontologoService odontologoServiceMock = mock(OdontologoService.class);
    private final ModelMapper modelMapper = new ModelMapper();

    private final TurnoService turnoService = new TurnoService(turnoRepositoryMock, pacienteServiceMock, odontologoServiceMock, modelMapper);


    private static TurnoEntradaDto turnoEntradaDto;
    private static PacienteSalidaDto pacienteSalidaDto;
    private static OdontologoSalidaDto odontologoSalidaDto;
    private static TurnoSalidaDto turnoSalidaDto;
    private static Turno turno;

    @BeforeAll

    static void setUp() {

        turnoEntradaDto = new TurnoEntradaDto(123, 13, LocalDateTime.of(2024, 9, 29, 12, 0, 30));

        turnoSalidaDto = new TurnoSalidaDto(2L, "Andrea", "Perez", "Javier", "Molina", LocalDateTime.of(2024, 9, 29, 12, 0, 30));
        pacienteSalidaDto = new PacienteSalidaDto(1L, "Andrea", "Perez", 123, LocalDate.of(2024, 1, 23), new DomicilioSalidaDto(1L, "Av_Italia", 2345, "montevideo", "montevideo"));
        odontologoSalidaDto = new OdontologoSalidaDto(1L, 13, "Javier", "Molina");

        turno = new Turno(2L, new Paciente(1L, "Andrea", "Perez", 123, LocalDate.of(2024, 1, 23), new Domicilio(1L, "Av_Italia", 2345, "montevideo", "montevideo")), new Odontologo(1L, 13, "Javier", "Molina"), LocalDateTime.of(2024, 9, 29, 12, 0, 30));

    }


    @Test
    void deberiaAlRegistrarTurnoDevolverNombreYApellidoDeOdontologoCuandoIngresoCamposExistentes() throws BadRequestException {
        when(pacienteServiceMock.buscarPacientePorDNI(turnoEntradaDto.getDniPaciente())).thenReturn(pacienteSalidaDto);
        when(odontologoServiceMock.buscarOdontologoPorNumeroMatricula(turnoEntradaDto.getNumeroMatriculaOdontologo())).thenReturn(odontologoSalidaDto);
        when(turnoRepositoryMock.save(any(Turno.class))).thenReturn(turno);

        TurnoSalidaDto turnoSalidaDto = turnoService.registrarTurno((turnoEntradaDto));


        assertNotNull(turnoSalidaDto);
        assertNotNull(turnoSalidaDto.getId());
        assertEquals("Javier", turnoSalidaDto.getNombreOdontologoSalidaDto());
        assertEquals("Molina", turnoSalidaDto.getApellidoOdontologoSalidaDto());
        verify(turnoRepositoryMock, times(1)).save(any(Turno.class));
    }


    @Test
    void deberiaLargarExcepcionAlIntentarRegistrarTurnoCuandoNoExisteUnPaciente() {
        when(pacienteServiceMock.buscarPacientePorDNI(turnoEntradaDto.getDniPaciente())).thenReturn(null);
        when(odontologoServiceMock.buscarOdontologoPorNumeroMatricula(turnoEntradaDto.getNumeroMatriculaOdontologo())).thenReturn(odontologoSalidaDto);

        assertThrows(BadRequestException.class,()->turnoService.registrarTurno(turnoEntradaDto));
    }


    @Test
    void deberiaActualizarTurnoPorIdAlCambiarLaFechaHora() throws BadRequestException {

        when(turnoRepositoryMock.findById(2L)).thenReturn(Optional.of(turno));
        when(turnoRepositoryMock.save(any(Turno.class))).thenReturn(turno);
        when(pacienteServiceMock.buscarPacientePorDNI(turnoEntradaDto.getDniPaciente())).thenReturn(pacienteSalidaDto);
        when(odontologoServiceMock.buscarOdontologoPorNumeroMatricula(turnoEntradaDto.getNumeroMatriculaOdontologo())).thenReturn(odontologoSalidaDto);

        turnoEntradaDto.setFechaHora(LocalDateTime.of(2024,10,23,13,23,34));
        TurnoSalidaDto turnoSalidaDto = turnoService.actualizarTurno(turnoEntradaDto, 2L);

        assertNotNull(turnoSalidaDto);
        assertEquals(LocalDateTime.of(2024,10,23,13,23,34), turnoSalidaDto.getFechaHora());
        verify(turnoRepositoryMock, times(1)).findById(2L);
        verify(turnoRepositoryMock, times(1)).save(any(Turno.class));
    }
}