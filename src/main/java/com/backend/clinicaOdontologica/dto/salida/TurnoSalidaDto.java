package com.backend.clinicaOdontologica.dto.salida;

import com.backend.clinicaOdontologica.entity.Odontologo;
import com.backend.clinicaOdontologica.entity.Paciente;

import java.time.LocalDateTime;

public class TurnoSalidaDto {
    // a la salida le voy a mostrar al paciente su nombre y apellido, ya que es mas visual (romi)
    private Long id;
    private String nombrePacienteSalidaDto;
    private String apellidoPacienteSalidaDto;
    private String nombreOdontologoSalidaDto;
    private String apellidoOdontologoSalidaDto;
    private LocalDateTime fechaHora;

    public TurnoSalidaDto() {
    }

    public TurnoSalidaDto(Long id, String nombrePacienteSalidaDto, String apellidoPacienteSalidaDto, String nombreOdontologoSalidaDto, String apellidoOdontologoSalidaDto, LocalDateTime fechaHora) {
        this.id = id;
        this.nombrePacienteSalidaDto = nombrePacienteSalidaDto;
        this.apellidoPacienteSalidaDto = apellidoPacienteSalidaDto;
        this.nombreOdontologoSalidaDto = nombreOdontologoSalidaDto;
        this.apellidoOdontologoSalidaDto = apellidoOdontologoSalidaDto;
        this.fechaHora = fechaHora;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombrePacienteSalidaDto() {
        return nombrePacienteSalidaDto;
    }

    public void setNombrePacienteSalidaDto(String nombrePacienteSalidaDto) {
        this.nombrePacienteSalidaDto = nombrePacienteSalidaDto;
    }

    public String getApellidoPacienteSalidaDto() {
        return apellidoPacienteSalidaDto;
    }

    public void setApellidoPacienteSalidaDto(String apellidoPacienteSalidaDto) {
        this.apellidoPacienteSalidaDto = apellidoPacienteSalidaDto;
    }

    public String getNombreOdontologoSalidaDto() {
        return nombreOdontologoSalidaDto;
    }

    public void setNombreOdontologoSalidaDto(String nombreOdontologoSalidaDto) {
        this.nombreOdontologoSalidaDto = nombreOdontologoSalidaDto;
    }

    public String getApellidoOdontologoSalidaDto() {
        return apellidoOdontologoSalidaDto;
    }

    public void setApellidoOdontologoSalidaDto(String apellidoOdontologoSalidaDto) {
        this.apellidoOdontologoSalidaDto = apellidoOdontologoSalidaDto;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }
}