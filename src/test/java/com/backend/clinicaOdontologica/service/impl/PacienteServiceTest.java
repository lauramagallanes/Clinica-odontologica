package com.backend.clinicaOdontologica.service.impl;

import com.backend.clinicaOdontologica.dto.entrada.DomicilioEntradaDto;
import com.backend.clinicaOdontologica.dto.entrada.PacienteEntradaDto;
import com.backend.clinicaOdontologica.dto.salida.PacienteSalidaDto;
import com.backend.clinicaOdontologica.entity.Domicilio;
import com.backend.clinicaOdontologica.entity.Paciente;
import com.backend.clinicaOdontologica.exceptions.ResourceNotFoundException;
import com.backend.clinicaOdontologica.repository.PacienteRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class PacienteServiceTest {

    //creo el mock de PacienteRepository
    private final PacienteRepository pacienteRepositoryMock = mock(PacienteRepository.class); // nos permite referenciar a los metodos del repository pero sin invocarlos realmente

    private final ModelMapper modelMapper = new ModelMapper();

    private final PacienteService pacienteService = new PacienteService(pacienteRepositoryMock, modelMapper);

    //Declaro las variables que voy a usar en diferentes metodos, y luego las inicialiso dentro de setUp dentro de BeforeEach
    private static PacienteEntradaDto pacienteEntradaDto;
    private static Paciente paciente;
    @BeforeAll
    //Before requiere que el metodo asociado sea estático, a diferencia de BeforeEach
    static void setUp(){

        paciente = new Paciente(1L, "Jose", "Paez", 4563789, LocalDate.of(2024, 8, 5), new Domicilio(1L, "Av. Brasil", 3576, "Pocitos", "Montevideo"));

        pacienteEntradaDto = new PacienteEntradaDto("Jose", "Paez", 4563789, LocalDate.of(2024, 8, 5), new DomicilioEntradaDto("Av. Brasil", 3576, "Pocitos", "Montevideo"));

    }

    @Test
    void deberiaMandarAlRepositorioUnPacienteDeApellidoPaez_yRetornarUnSalidaDtoConSuId(){
        when(pacienteRepositoryMock.save(any(Paciente.class))).thenReturn(paciente);
        //con el any(Paciente.class) le estoy indicando a mockito que cuanto reciba algo del tipo paciente, retorne el objeto paciente. Lo que recibe en el save no es la entidad paciente tal cual la tenemos instanciada, ya que lo que recibe es una entidad similar, pero sin el id

        PacienteSalidaDto pacienteSalidaDto = pacienteService.registrarPaciente((pacienteEntradaDto));

        assertNotNull(pacienteSalidaDto);
        assertNotNull(pacienteSalidaDto.getId());
        assertEquals("Paez", pacienteSalidaDto.getApellidoPaciente());
        verify(pacienteRepositoryMock, times(1)).save(any(Paciente.class));
    }

    @Test
    void deberiaDevolverUnListadoNoVacioDePacientes(){

        List<Paciente> pacientes = new ArrayList<>(List.of(paciente));
        when(pacienteRepositoryMock.findAll()).thenReturn(pacientes);

        List<PacienteSalidaDto> listadoDePacientes = pacienteService.listarPacientes();
        assertFalse(listadoDePacientes.isEmpty());
    }

    @Test
    void deberiaEliminarElPacienteConId1(){

        when(pacienteRepositoryMock.findById(1L)).thenReturn(Optional.of(paciente));
        doNothing().when(pacienteRepositoryMock).deleteById(1L);

        assertDoesNotThrow(() -> pacienteService.eliminarPaciente(1L));
        verify(pacienteRepositoryMock, times(1)).deleteById(1L);


    }

    @Test
    void deberiaDevolverUnaListaVaciaDePacientes(){
        when(pacienteRepositoryMock.findAll()).thenReturn(new ArrayList<>());

        List<PacienteSalidaDto> pacientes = pacienteService.listarPacientes();
        assertTrue(pacientes.isEmpty());

        verify(pacienteRepositoryMock, times(1)).findAll();
    }

    @Test
    void deberiaLanzarExcepcionCuandoElPacienteAActualizarNoSeaEncontrado(){

        when(pacienteRepositoryMock.findById(3L)).thenReturn(Optional.empty());
        pacienteEntradaDto.setNombrePaciente("Maria");
        assertThrows(ResourceNotFoundException.class, ()-> pacienteService.actualizarPaciente(pacienteEntradaDto, 3L));


    }
    @Test
    void deberiaBuscarPacientePorIdExistente() {
        when(pacienteRepositoryMock.findById(1L)).thenReturn(Optional.of(paciente));

        PacienteSalidaDto pacienteSalidaDto = pacienteService.buscarPacientePorId(1L);

        assertNotNull(pacienteSalidaDto);
        assertEquals("Paez", pacienteSalidaDto.getApellidoPaciente());
        verify(pacienteRepositoryMock, times(1)).findById(1L);
    }
    @Test
    void deberiaDevolverNullCuandoPacientePorIdNoExistente() {
        when(pacienteRepositoryMock.findById(2L)).thenReturn(Optional.empty());

        PacienteSalidaDto pacienteSalidaDto = pacienteService.buscarPacientePorId(2L);

        assertNull(pacienteSalidaDto);
        verify(pacienteRepositoryMock, times(1)).findById(2L);
    }
    @Test
    void deberiaBuscarPacientePorDNIExistente() {
        when(pacienteRepositoryMock.findByDni(4563789)).thenReturn(Optional.of(paciente));

        PacienteSalidaDto pacienteSalidaDto = pacienteService.buscarPacientePorDNI(4563789);

        assertNotNull(pacienteSalidaDto);
        assertEquals("Paez", pacienteSalidaDto.getApellidoPaciente());
        verify(pacienteRepositoryMock, times(1)).findByDni(4563789);
    }
    @Test
    void deberiaActualizarPacienteExistente() throws ResourceNotFoundException {
        when(pacienteRepositoryMock.findById(1L)).thenReturn(Optional.of(paciente));
        when(pacienteRepositoryMock.save(any(Paciente.class))).thenReturn(paciente);

        pacienteEntradaDto.setNombrePaciente("Maria");
        PacienteSalidaDto pacienteSalidaDto = pacienteService.actualizarPaciente(pacienteEntradaDto, 1L);

        assertNotNull(pacienteSalidaDto);
        assertEquals("Maria", pacienteSalidaDto.getNombrePaciente());
        verify(pacienteRepositoryMock, times(1)).findById(1L);
        verify(pacienteRepositoryMock, times(1)).save(any(Paciente.class));
    }

  
}