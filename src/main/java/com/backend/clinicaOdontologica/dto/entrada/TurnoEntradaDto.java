package com.backend.clinicaOdontologica.dto.entrada;


import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

public class TurnoEntradaDto {

    @Positive(message = "El dni del paciente no puede ser nulo o menor a cero")
    private int dniPaciente;


    @Positive(message = "El número de matricula del odontólogo no puede ser nulo ni menor a cero")
    private int numeroMatriculaOdontologo;



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

