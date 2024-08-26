package com.backend.clinicaOdontologica.controller;

import com.backend.clinicaOdontologica.dto.entrada.PacienteEntradaDto;
import com.backend.clinicaOdontologica.dto.entrada.TurnoEntradaDto;
import com.backend.clinicaOdontologica.dto.salida.PacienteSalidaDto;
import com.backend.clinicaOdontologica.dto.salida.TurnoSalidaDto;
import com.backend.clinicaOdontologica.entity.Turno;
import com.backend.clinicaOdontologica.service.IPacienteService;
import com.backend.clinicaOdontologica.service.ITurnoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

    @RestController
    @RequestMapping("turnos")

    public class TurnoController {

        private ITurnoService turnoService;

        // Hacemos inyección de dependencia con el constructor, pasando TurnoService como parametro:
        public TurnoController(ITurnoService turnoService) {
            this.turnoService = turnoService;
        }

        //Asi funciona el sistema: Me llega un JSON y el @RequestBody lo transforma en el DTO de entrada. Este va al controller, del controller va al service, del service al mapper y lo transforma en una entidad, la entidad va al repositorio(DAO), del repositorio se consulta a la base de datos, la base de datos devuelve una entidad que va al repositorio, el repositorio ejecuta el service, y la info es mapeada nuevamente, generando el DTO de salida. El DTO de salida va al controller, y el @ResponseBody lo trnsforma en JSON, que es lo que le llega al cliente

        //POST
        @PostMapping("/registrar")
        public ResponseEntity<TurnoSalidaDto> registrarTurno(@RequestBody @Valid TurnoEntradaDto turnoEntradaDto){
            TurnoSalidaDto turnoSalidaDto = turnoService.registrarTurno(turnoEntradaDto);
            return new ResponseEntity<>(turnoSalidaDto, HttpStatus.CREATED);
        }

        //GET
        @GetMapping("/listar")
        public ResponseEntity<List<TurnoSalidaDto>>  listarPacientes(){
            return new ResponseEntity<>(turnoService.listarTurnos(), HttpStatus.OK);
        }
}
