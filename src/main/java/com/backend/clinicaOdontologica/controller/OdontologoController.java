package com.backend.clinicaOdontologica.controller;


import com.backend.clinicaOdontologica.dto.entrada.OdontologoEntradaDto;
import com.backend.clinicaOdontologica.dto.salida.OdontologoSalidaDto;
import com.backend.clinicaOdontologica.service.IOdontologoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("odontologos")
public class OdontologoController {

    private IOdontologoService odontologoService;

    // Hacemos inyección de dependencia con el constructor, pasando odontologo Service como parametro:
    public OdontologoController(IOdontologoService odontologoService) {
        this.odontologoService = odontologoService;
    }

    //Asi funciona el sistema: Me llega un JSON y el @RequestBody lo transforma en el DTO de entrada. Este va al controller, del controller va al service, del service al mapper y lo transforma en una entidad, la entidad va al repositorio(DAO), del repositorio se consulta a la base de datos, la base de datos devuelve una entidad que va al repositorio, el repositorio ejecuta el service, y la info es mapeada nuevamente, generando el DTO de salida. El DTO de salida va al controller, y el @ResponseBody lo trnsforma en JSON, que es lo que le llega al cliente

    //POST
    @PostMapping("/registrar")
    public ResponseEntity<OdontologoSalidaDto> registrarOdontologo(@RequestBody @Valid OdontologoEntradaDto odontologoEntradaDto){
        OdontologoSalidaDto odontologoSalidaDto = odontologoService.registrarOdontologo(odontologoEntradaDto);
        return new ResponseEntity<>(odontologoSalidaDto, HttpStatus.CREATED);
    }

    //GET
    @GetMapping("/listar")
    public ResponseEntity<List<OdontologoSalidaDto>>  listarOdontologos(){
        return new ResponseEntity<>(odontologoService.listarOdontologos(), HttpStatus.OK);
    }
}
