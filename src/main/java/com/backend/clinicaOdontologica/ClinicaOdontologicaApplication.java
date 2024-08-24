package com.backend.clinicaOdontologica;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ClinicaOdontologicaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClinicaOdontologicaApplication.class, args);
	}
	//Creo el bean del mapper que usaremos para mapear entidades a DTOs y DTOs a entidades
	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}

}
