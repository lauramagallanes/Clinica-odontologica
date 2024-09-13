package com.backend.clinicaOdontologica.controller;


import com.backend.clinicaOdontologica.dto.entrada.TurnoEntradaDto;
import com.backend.clinicaOdontologica.dto.salida.TurnoSalidaDto;
import com.backend.clinicaOdontologica.exceptions.BadRequestException;
import com.backend.clinicaOdontologica.exceptions.ResourceNotFoundException;
import com.backend.clinicaOdontologica.service.ITurnoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

    @RestController
    @RequestMapping("turnos")
    @CrossOrigin

    public class TurnoController {

        private ITurnoService turnoService;

        public TurnoController(ITurnoService turnoService) {
            this.turnoService = turnoService;
        }


        @PostMapping("/registrar")
        public ResponseEntity<?> registrarTurno(@RequestBody @Valid TurnoEntradaDto turnoEntradaDto)throws BadRequestException {
                TurnoSalidaDto turnoSalidaDto = turnoService.registrarTurno(turnoEntradaDto);
                return new ResponseEntity<>(turnoSalidaDto, HttpStatus.CREATED);
            }


        @GetMapping("/listar")
        public ResponseEntity<List<TurnoSalidaDto>>  listarPacientes(){
            return new ResponseEntity<>(turnoService.listarTurnos(), HttpStatus.OK);
        }

         @GetMapping("/{id}")
         public ResponseEntity<TurnoSalidaDto> buscarTurnoPorId(@PathVariable Long id){
            return new ResponseEntity<>(turnoService.buscarTurnoPorId(id), HttpStatus.OK);
        }


        @PutMapping("/actualizar/{id}")
        public ResponseEntity<?> actualizarTurno(@RequestBody @Valid TurnoEntradaDto turno, @PathVariable Long id) throws BadRequestException{
            try {
                return new ResponseEntity<>(turnoService.actualizarTurno(turno, id), HttpStatus.OK);
            } catch(BadRequestException exception){
                return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
            }


        }

        @DeleteMapping("/eliminar")
        public ResponseEntity<String> eliminarTurno(@RequestParam Long id) throws ResourceNotFoundException {
            turnoService.eliminarTurno(id);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Turno eliminado correctamente");
        }
    }
