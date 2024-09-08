package com.backend.clinicaOdontologica.dto.salida;

import com.backend.clinicaOdontologica.entity.Domicilio;

import java.time.LocalDate;

public class PacienteSalidaDto {
    private Long id;
    private String nombrePaciente;
    private String apellidoPaciente;
    private int dni;
    private LocalDate fechaIngreso;
    private DomicilioSalidaDto domicilioSalidaDto;

    public PacienteSalidaDto() {
    }

    public PacienteSalidaDto(Long id, String nombrePaciente, String apellidoPaciente, int dni, LocalDate fechaIngreso, DomicilioSalidaDto domicilioSalidaDto) {
        this.id = id;
        this.nombrePaciente = nombrePaciente;
        this.apellidoPaciente = apellidoPaciente;
        this.dni = dni;
        this.fechaIngreso = fechaIngreso;
        this.domicilioSalidaDto = domicilioSalidaDto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombrePaciente() {
        return nombrePaciente;
    }

    public void setNombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
    }

    public String getApellidoPaciente() {
        return apellidoPaciente;
    }

    public void setApellidoPaciente(String apellidoPaciente) {
        this.apellidoPaciente = apellidoPaciente;
    }

    public int getDni() {
        return dni;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public DomicilioSalidaDto getDomicilioSalidaDto() {
        return domicilioSalidaDto;
    }

    public void setDomicilioSalidaDto(DomicilioSalidaDto domicilioSalidaDto) {
        this.domicilioSalidaDto = domicilioSalidaDto;
    }
}
