package com.backend.clinicaOdontologica.repository;

import com.backend.clinicaOdontologica.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

//Long es el tipo de dato de la id de la entidad Paciente:
public interface PacienteRepository extends JpaRepository<Paciente, Long>  {
    // ya trae el CRUD basico
    //Escribimos las consultas especificas



}
