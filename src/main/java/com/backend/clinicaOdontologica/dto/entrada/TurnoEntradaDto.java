package com.backend.clinicaOdontologica.dto.entrada;

import com.backend.clinicaOdontologica.entity.Odontologo;
import com.backend.clinicaOdontologica.entity.Paciente;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.Valid;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class TurnoEntradaDto {

    //@NotNull(message = "El paciente no puede ser nulo")
    @NotNull(message = "Debe especificarse el paciente")
    @Valid
    private PacienteEntradaDto pacienteEntradaDto;

    //@NotNull(message = "El odontologo no puede ser nulo")
    @NotNull(message = "Debe especificarse el odontologo")
    @Valid
    private OdontologoEntradaDto odontologoEntradaDto;
    //FutureOrPresent toma en cuenta solo fecha u hora tambien?
    @FutureOrPresent(message = "La fecha no puede ser anterior al día de hoy")
    @NotNull(message = "Debe especificarse la fecha del turno")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaHora;

    //Tienen que ir todos los datos de paciente y odontologo? Hacer nuevo DTO con solo nombres
    public TurnoEntradaDto(PacienteEntradaDto pacienteEntradaDto, OdontologoEntradaDto odontologoEntradaDto, LocalDateTime fechaHora) {
        this.pacienteEntradaDto = pacienteEntradaDto;
        this.odontologoEntradaDto = odontologoEntradaDto;
        this.fechaHora = fechaHora;
    }

    public @NotNull(message = "El paciente no puede ser nulo") @NotBlank(message = "Debe especificarse el paciente") @Valid PacienteEntradaDto getPacienteEntradaDto() {
        return pacienteEntradaDto;
    }

    public void setPacienteEntradaDto(@NotNull(message = "El paciente no puede ser nulo") @NotBlank(message = "Debe especificarse el paciente") @Valid PacienteEntradaDto pacienteEntradaDto) {
        this.pacienteEntradaDto = pacienteEntradaDto;
    }

    public @NotNull(message = "El odontologo no puede ser nulo") @NotBlank(message = "Debe especificarse el odontologo") @Valid OdontologoEntradaDto getOdontologoEntradaDto() {
        return odontologoEntradaDto;
    }

    public void setOdontologoEntradaDto(@NotNull(message = "El odontologo no puede ser nulo") @NotBlank(message = "Debe especificarse el odontologo") @Valid OdontologoEntradaDto odontologoEntradaDto) {
        this.odontologoEntradaDto = odontologoEntradaDto;
    }

    public @FutureOrPresent(message = "La fecha no puede ser anterior al día de hoy") @NotNull(message = "Debe especificarse la fecha del turno") LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(@FutureOrPresent(message = "La fecha no puede ser anterior al día de hoy") @NotNull(message = "Debe especificarse la fecha del turno") LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }
}
