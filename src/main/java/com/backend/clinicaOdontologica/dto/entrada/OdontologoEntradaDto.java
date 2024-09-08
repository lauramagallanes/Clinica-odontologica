package com.backend.clinicaOdontologica.dto.entrada;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class OdontologoEntradaDto {
    @Positive(message = "El número de matricula del odontólogo no puede ser nulo ni menor a cero")
    @Digits(integer = 10, fraction = 0, message = "El numero debe tener como maximo 10 digitos")
    private int numeroMatricula;
    @NotBlank(message = "Debe especificarse el nombre del odontólogo")
    @Size(max=50, message = "El nombre debe tener hasta 50 caracteres")
    private String nombreOdontologo;
    @NotBlank(message = "Debe especificarse el apellido del odontólogo")
    @Size(max=50, message = "El apellido debe tener hasta 50 caracteres")
    private String apellidoOdontologo;

    public OdontologoEntradaDto() {
    }

    public OdontologoEntradaDto(int numeroMatricula, String nombreOdontologo, String apellidoOdontologo) {
        this.numeroMatricula = numeroMatricula;
        this.nombreOdontologo = nombreOdontologo;
        this.apellidoOdontologo = apellidoOdontologo;
    }

    public int getNumeroMatricula() {
        return numeroMatricula;
    }

    public void setNumeroMatricula(int numeroMatricula) {
        this.numeroMatricula = numeroMatricula;
    }

    public String getNombreOdontologo() {
        return nombreOdontologo;
    }

    public void setNombreOdontologo(String nombreOdontologo) {
        this.nombreOdontologo = nombreOdontologo;
    }

    public String getApellidoOdontologo() {
        return apellidoOdontologo;
    }

    public void setApellidoOdontologo(String apellidoOdontologo) {
        this.apellidoOdontologo = apellidoOdontologo;
    }
}
