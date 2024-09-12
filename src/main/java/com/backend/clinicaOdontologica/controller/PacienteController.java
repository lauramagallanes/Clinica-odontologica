package com.backend.clinicaOdontologica.controller;

import com.backend.clinicaOdontologica.dto.entrada.PacienteEntradaDto;
import com.backend.clinicaOdontologica.dto.salida.PacienteSalidaDto;
import com.backend.clinicaOdontologica.exceptions.BadRequestException;
import com.backend.clinicaOdontologica.exceptions.ResourceNotFoundException;
import com.backend.clinicaOdontologica.service.IPacienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("pacientes")
@CrossOrigin
public class PacienteController {

    private IPacienteService pacienteService;

    // Hacemos inyección de dependencia con el constructor, pasando pcienteService como parametro:
    public PacienteController(IPacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    //Asi funciona el sistema: Me llega un JSON y el @RequestBody lo transforma en el DTO de entrada. Este va al controller, del controller va al service, del service al mapper y lo transforma en una entidad, la entidad va al repositorio(DAO), del repositorio se consulta a la base de datos, la base de datos devuelve una entidad que va al repositorio, el repositorio ejecuta el service, y la info es mapeada nuevamente, generando el DTO de salida. El DTO de salida va al controller, y el @ResponseBody lo trnsforma en JSON, que es lo que le llega al cliente

    //POST
    @PostMapping("/registrar")
    public ResponseEntity<PacienteSalidaDto> registrarPaciente(@RequestBody @Valid PacienteEntradaDto pacienteEntradaDto){
        PacienteSalidaDto pacienteSalidaDto = pacienteService.registrarPaciente(pacienteEntradaDto);
        return new ResponseEntity<>(pacienteSalidaDto, HttpStatus.CREATED);
    }

    //GET
    @GetMapping("/listar")
    public ResponseEntity<List<PacienteSalidaDto>>  listarPacientes(){
        return new ResponseEntity<>(pacienteService.listarPacientes(), HttpStatus.OK);
    }

    @GetMapping("/{id}")//localhost:8080/pacientes/x
    public ResponseEntity<PacienteSalidaDto> buscarPacientePorId(@PathVariable Long id){
        return new ResponseEntity<>(pacienteService.buscarPacientePorId(id), HttpStatus.OK);
    }

    //PUT -- actualización completa del objeto

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<PacienteSalidaDto> actualizarPaciente(@RequestBody @Valid PacienteEntradaDto paciente, @PathVariable Long id) throws ResourceNotFoundException{
        return new ResponseEntity<>(pacienteService.actualizarPaciente(paciente, id), HttpStatus.OK);
    }

    //DELETE
    @DeleteMapping("/eliminar")//localhost:8080/pacientes/eliminar?id=x
    public ResponseEntity<String> eliminarPaciente(@RequestParam Long id) throws ResourceNotFoundException, BadRequestException {
            pacienteService.eliminarPaciente(id);
           return ResponseEntity.status(HttpStatus.ACCEPTED).body("Paciente eliminado correctamente");}

}
