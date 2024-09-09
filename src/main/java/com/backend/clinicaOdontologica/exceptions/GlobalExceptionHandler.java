package com.backend.clinicaOdontologica.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ResourceNotFoundException.class}) //si quiero agregar mas excepciones dentro del ExceptionHandler, pongo una coma dentro de las llaves y la siguiente excepcion a manejar
    @ResponseStatus(HttpStatus.NOT_FOUND) //Le indicamos que codigo de estado va a lanzar esta situación
    public Map<String, String> manejarResourceNotFoundException(ResourceNotFoundException resourceNotFoundException){
        Map<String, String> mensaje = new HashMap<>();
        mensaje.put("mensaje", "Recurso no encontrado: " + resourceNotFoundException.getMessage());
        //el mensaje de la exepcion va a ser el que setee cuando instancio la excepcion en el service correspondiente
        return mensaje;
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    //Esta es la excepción que se lanza cuando en el dto nos llega algo que no cumple con los requerimientos de la validacion que el dto tiene
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> manejarValidationException(MethodArgumentNotValidException methodArgumentNotValidException){
        Map<String, String> mensaje = new HashMap<>();

        methodArgumentNotValidException.getBindingResult().getAllErrors().forEach(e -> {
            String nombreCampo = ((FieldError) e).getField();
            String mensajeError = e.getDefaultMessage();
            mensaje.put(nombreCampo, mensajeError);
        });
        return mensaje;
    }




}
