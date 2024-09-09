package com.backend.clinicaOdontologica.dto.entrada;

import com.backend.clinicaOdontologica.entity.Odontologo;
import com.backend.clinicaOdontologica.entity.Paciente;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.Valid;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

public class TurnoEntradaDto {
    //El dato para agendar al paciente será el dni, me parece mas realista que el nombre (ROMI)
    @Positive(message = "El dni del paciente no puede ser nulo o menor a cero")
    private int dniPacienteEntradaDto;

    //@NotNull(message = "El odontologo no puede ser nulo")
    @NotNull(message = "Debe especificarse el apellido del odontologo")
    @Valid
    private String apellidoOdontologoEntradaDto;

    //@NotNull(message = "El odontologo no puede ser nulo")
    @NotNull(message = "Debe especificarse el nombre del odontologo")
    @Valid
    private String nombreOdontologoEntradaDto;

    //FutureOrPresent toma en cuenta solo fecha u hora tambien?
    @FutureOrPresent(message = "La fecha no puede ser anterior al día de hoy")
    @NotNull(message = "Debe especificarse la fecha del turno")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaHora;

    public TurnoEntradaDto(){

    }

    public TurnoEntradaDto(int dniPacienteEntradaDto, String apellidoOdontologoEntradaDto, String nombreOdontologoEntradaDto, LocalDateTime fechaHoraEntradaDto) {
        this.dniPacienteEntradaDto = dniPacienteEntradaDto;
        this.apellidoOdontologoEntradaDto = apellidoOdontologoEntradaDto;
        this.nombreOdontologoEntradaDto = nombreOdontologoEntradaDto;
        this.fechaHora = fechaHora;
    }

    @Positive(message = "El dni del paciente no puede ser nulo o menor a cero")
    public int getDniPacienteEntradaDto() {
        return dniPacienteEntradaDto;
    }

    public void setDniPacienteEntradaDto(int dniPacienteEntradaDto) {
        this.dniPacienteEntradaDto = dniPacienteEntradaDto;
    }

    public String getApellidoOdontologoEntradaDto() {
        return apellidoOdontologoEntradaDto;
    }

    public void setApellidoOdontologoEntradaDto(String apellidoOdontologoEntradaDto) {
        this.apellidoOdontologoEntradaDto = apellidoOdontologoEntradaDto;
    }

    public String getNombreOdontologoEntradaDto() {
        return nombreOdontologoEntradaDto;
    }

    public void setNombreOdontologoEntradaDto(String nombreOdontologoEntradaDto) {
        this.nombreOdontologoEntradaDto = nombreOdontologoEntradaDto;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora( LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }
}

