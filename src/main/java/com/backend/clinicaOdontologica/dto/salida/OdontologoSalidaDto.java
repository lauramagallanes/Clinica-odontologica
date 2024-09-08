package com.backend.clinicaOdontologica.dto.salida;

public class OdontologoSalidaDto {
    private Long id;
    private int numeroMatricula;
    private String nombreOdontologo;
    private String apellidoOdontologo;

    public OdontologoSalidaDto() {
    }

    public OdontologoSalidaDto(Long id, int numeroMatricula, String nombreOdontologo, String apellidoOdontologo) {
        this.id = id;
        this.numeroMatricula = numeroMatricula;
        this.nombreOdontologo = nombreOdontologo;
        this.apellidoOdontologo = apellidoOdontologo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
