package com.backend.clinicaOdontologica.dto.entrada;

import com.backend.clinicaOdontologica.entity.Odontologo;
import com.backend.clinicaOdontologica.entity.Paciente;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

public class TurnoEntradaDto {
    //El dato para agendar al paciente será el dni, me parece mas realista que el nombre (ROMI)
    @Positive(message = "El dni del paciente no puede ser nulo o menor a cero")
    private int dniPaciente;

    //@NotNull(message = "El odontologo no puede ser nulo")
    @Positive(message = "El número de matricula del odontólogo no puede ser nulo ni menor a cero")
    private int numeroMatriculaOdontologo;


    //FutureOrPresent toma en cuenta solo fecha u hora tambien?
    @FutureOrPresent(message = "La fecha no puede ser anterior al día de hoy")
    @NotNull(message = "Debe especificarse la fecha del turno")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaHora;

    public TurnoEntradaDto(){

    }

    public TurnoEntradaDto(int dniPaciente, int numeroMatriculaOdontologo,  LocalDateTime fechaHora) {
        this.dniPaciente = dniPaciente;
        this.numeroMatriculaOdontologo = numeroMatriculaOdontologo;
        this.fechaHora = fechaHora;
    }

    @Positive(message = "El dni del paciente no puede ser nulo o menor a cero")
    public int getDniPaciente() {
        return dniPaciente;
    }

    public void setDniPacienteEntradaDto(int dniPaciente) {
        this.dniPaciente = dniPaciente;
    }

    public int getNumeroMatriculaOdontologo() {
        return numeroMatriculaOdontologo;
    }

    public void setNumeroMatriculaOdontologo(int numeroMatriculaOdontologo) {
        this.numeroMatriculaOdontologo = numeroMatriculaOdontologo;
    }


    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora( LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }
}

