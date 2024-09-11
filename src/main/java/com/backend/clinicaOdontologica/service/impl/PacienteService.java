package com.backend.clinicaOdontologica.service.impl;

import com.backend.clinicaOdontologica.dto.entrada.PacienteEntradaDto;
import com.backend.clinicaOdontologica.dto.salida.PacienteSalidaDto;
import com.backend.clinicaOdontologica.entity.Paciente;
import com.backend.clinicaOdontologica.exceptions.BadRequestException;
import com.backend.clinicaOdontologica.exceptions.ResourceNotFoundException;
import com.backend.clinicaOdontologica.repository.PacienteRepository;
import com.backend.clinicaOdontologica.service.IPacienteService;
import com.backend.clinicaOdontologica.utils.JsonPrinter;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PacienteService implements IPacienteService {

    //Usamos slf4j como libreria de logging
    private final Logger LOGGER = LoggerFactory.getLogger(PacienteService.class);
    private final PacienteRepository pacienteRepository;
    private final ModelMapper modelMapper;
    //cuando defino algo como final, le debo asignar un valor, para eso creamos el constructor--> inyectamos el model mapper en el servicio a través del constructor:
    public PacienteService(PacienteRepository pacienteRepository,  ModelMapper modelMapper) {
        this.pacienteRepository = pacienteRepository;
        this.modelMapper = modelMapper;
        // llamamos al metodo configureMapping que creamos abajo:
        configureMapping();

    }


    @Override
    public PacienteSalidaDto registrarPaciente(PacienteEntradaDto paciente) {
        LOGGER.info("PacienteEntradaDto: {}", JsonPrinter.toString(paciente));
        // voy a transformar el Dto PacienteEntradaDto que recibo en un objeto Paciente a través del mapper:
        Paciente entidadPaciente = modelMapper.map(paciente, Paciente.class);
        LOGGER.info("EntidadPaciente: {}", JsonPrinter.toString(entidadPaciente));
        Paciente pacienteRegistrado = pacienteRepository.save(entidadPaciente);
        LOGGER.info("PacienteRegistrado: {}", JsonPrinter.toString(pacienteRegistrado));
        PacienteSalidaDto pacienteSalidaDto = modelMapper.map(pacienteRegistrado, PacienteSalidaDto.class);
        LOGGER.info("PacienteSalidaDto: {}", JsonPrinter.toString(pacienteSalidaDto));

        return pacienteSalidaDto;
    }

    @Override
    public PacienteSalidaDto buscarPacientePorId(Long id) {
        Paciente pacienteBuscado = pacienteRepository.findById(id).orElse(null);
        LOGGER.info("Paciente buscado: {}", JsonPrinter.toString(pacienteBuscado));
        PacienteSalidaDto pacienteEncontrado = null;
        if (pacienteBuscado !=null){
            pacienteEncontrado= modelMapper.map(pacienteBuscado, PacienteSalidaDto.class);
            LOGGER.info("Paciente encontroado: {}", JsonPrinter.toString(pacienteEncontrado));
        } else LOGGER.error("No se ha encontrado el paciente con id {}", id);
        return pacienteEncontrado;
    }

    @Override
    public PacienteSalidaDto buscarPacientePorDNI(int dni) {
        Paciente pacienteBuscado = pacienteRepository.findByDni(dni).orElse(null);
        LOGGER.info("Paciente buscado: {}", JsonPrinter.toString(pacienteBuscado));
        PacienteSalidaDto pacienteEncontrado = null;
        if (pacienteBuscado !=null){
            pacienteEncontrado= modelMapper.map(pacienteBuscado, PacienteSalidaDto.class);
            LOGGER.info("Paciente encontrado: {}", JsonPrinter.toString(pacienteEncontrado));
        } else LOGGER.error("No se ha encontrado el paciente con dni {}", dni);
        return pacienteEncontrado;
    }

    @Override
    public List<PacienteSalidaDto> listarPacientes() {
        List<PacienteSalidaDto> pacienteSalidaDtos = pacienteRepository.findAll()
                .stream()
                .map(paciente -> modelMapper.map(paciente, PacienteSalidaDto.class))
                .toList();
        LOGGER.info("Listado de todos los pacientes: {}", JsonPrinter.toString(pacienteSalidaDtos));
        return pacienteSalidaDtos;
    }

    @Override
    public void eliminarPaciente(Long id) throws ResourceNotFoundException{
        if (buscarPacientePorId(id)!=null){
            try{
                //llamada a la capa repositorio para eliminar
                pacienteRepository.deleteById(id);
                LOGGER.warn("Se ha eliminado el paciente con id {}", id);
            } catch (DataIntegrityViolationException exception) {
                LOGGER.error("No se puede eliminar el paciente con id {} porque está asociado a un turno", id);
                throw new BadRequestException("No se puede eliminar el paciente porque está asociado a un turno.");
            }


        } else{

            throw new ResourceNotFoundException("No existe el paciente con id " + id); //El msje de la excepcion es para el cliente, y el logger es para nosotros los dev
        }
    }

    @Override
    public PacienteSalidaDto actualizarPaciente(PacienteEntradaDto pacienteEntradaDto, Long id) throws ResourceNotFoundException {
        //busco al paciente a actualizar en la base de datos
        Paciente pacienteAActualizar = pacienteRepository.findById(id).orElse(null); //podria usar el metodo de buscar por id que creamos antes ya que tiene los loggs
        //mapeo el pacienteEntradaDto que es lo que me llega por parametro para castearlo a una entity (Paciente)
        Paciente pacienteRecibido = modelMapper.map(pacienteEntradaDto, Paciente.class);

        PacienteSalidaDto pacienteSalidaDto = null;
        if (pacienteAActualizar != null){
            // seteo la id de pacienteRecibido usando la id de pacienteAActualizar
            pacienteRecibido.setId(pacienteAActualizar.getId());
            pacienteRecibido.getDomicilio().setId(pacienteAActualizar.getDomicilio().getId());
            pacienteAActualizar = pacienteRecibido; //asi me evito hacer los setters para cada atributo
            pacienteRepository.save(pacienteAActualizar);
            LOGGER.info("PacienteActualizado: {}", JsonPrinter.toString(pacienteAActualizar));
        }else {
            LOGGER.error("No fue posible actualizar el paciente por que no se encuentra en nuestra base de datos");
            throw new ResourceNotFoundException("No fue posible actualizar el paciente por que no se encuentra en nuestra base de datos");
        }
        return pacienteSalidaDto;
    }

    private void configureMapping(){
        // Le indico que cuando le llega algo del tipo PacienteEntradaDto en este servicio, tinee que transformarlo en un objeto del tipo Paciente:
        modelMapper.typeMap(PacienteEntradaDto.class, Paciente.class)
                //invoco un metodo a traves de una clase con :: (referencia al metodo de una clase)
                .addMappings(mapper -> mapper.map(PacienteEntradaDto::getDomicilioEntradaDto, Paciente::setDomicilio));
        modelMapper.typeMap(Paciente.class, PacienteSalidaDto.class)
                .addMappings(mapper -> mapper.map(Paciente::getDomicilio, PacienteSalidaDto::setDomicilioSalidaDto));
    }
}
