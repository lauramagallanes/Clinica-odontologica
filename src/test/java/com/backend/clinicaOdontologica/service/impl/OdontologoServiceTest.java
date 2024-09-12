package com.backend.clinicaOdontologica.service.impl;

import com.backend.clinicaOdontologica.dto.entrada.OdontologoEntradaDto;
import com.backend.clinicaOdontologica.dto.salida.OdontologoSalidaDto;
import com.backend.clinicaOdontologica.entity.Odontologo;
import com.backend.clinicaOdontologica.exceptions.ResourceNotFoundException;
import com.backend.clinicaOdontologica.repository.OdontologoRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.configuration.IMockitoConfiguration;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class OdontologoServiceTest {

    private final OdontologoRepository odontologoRepositoryMock = mock(OdontologoRepository.class);
    private final ModelMapper modelMapper = new ModelMapper();
    private final OdontologoService odontologoService = new OdontologoService(odontologoRepositoryMock, modelMapper);

    private static OdontologoEntradaDto odontologoEntradaDto;
    private static Odontologo odontologo;

    @BeforeAll
    static void setUp(){
        odontologo = new Odontologo(1L, 1234, "Mario", "Moreno");
        odontologoEntradaDto =  new OdontologoEntradaDto(1234, "Mario", "Moreno");
    }

    @Test //No pasa, me dice que el id es null
    void deberiaMandarAlRepositorioUnOdontologoDeApellidoMoreno_YRetornarUnaSalidaConSuId (){
        when(odontologoRepositoryMock.save(any(Odontologo.class))).thenReturn(odontologo);

        OdontologoSalidaDto odontologoSalidaDto = odontologoService.registrarOdontologo((odontologoEntradaDto));

        assertNotNull(odontologoSalidaDto);
        assertNotNull(odontologoSalidaDto.getId());
        assertEquals("Moreno", odontologoSalidaDto.getApellidoOdontologo());
        verify(odontologoRepositoryMock, times(1)).save(any(Odontologo.class));
    }
    @Test //Este si pasa
    void deberiaBuscarOdontologoPorNumeroMatricula() {
        when(odontologoRepositoryMock.findByNumeroMatricula(1234)).thenReturn(Optional.of(odontologo));

        OdontologoSalidaDto odontologoSalidaDto = odontologoService.buscarOdontologoPorNumeroMatricula(1234);

        assertNotNull(odontologoSalidaDto);
        assertEquals(1234, odontologoSalidaDto.getNumeroMatricula());
        verify(odontologoRepositoryMock, times(1)).findByNumeroMatricula(1234);
    }

    @Test // Si pasa
    void deberiaEliminarOdontologo() {
        when(odontologoRepositoryMock.findById(1L)).thenReturn(Optional.of(odontologo));
        doNothing().when(odontologoRepositoryMock).deleteById(1L);

        assertDoesNotThrow(() -> odontologoService.eliminarOdontologo(1L));
        verify(odontologoRepositoryMock, times(1)).deleteById(1L);
    }

    @Test// si pasa
    void deberiaActualizarOdontologoExistente() throws ResourceNotFoundException {
        when(odontologoRepositoryMock.findById(1L)).thenReturn(Optional.of(odontologo));
        when(odontologoRepositoryMock.save(any(Odontologo.class))).thenReturn(odontologo);

        odontologoEntradaDto.setNombreOdontologo("Carlos");
        OdontologoSalidaDto odontologoSalidaDto = odontologoService.actualizarOdontologo(odontologoEntradaDto, 1L);

        assertNotNull(odontologoSalidaDto);
        assertEquals("Carlos", odontologoSalidaDto.getNombreOdontologo());
        verify(odontologoRepositoryMock, times(1)).findById(1L);
        verify(odontologoRepositoryMock, times(1)).save(any(Odontologo.class));
    }
}