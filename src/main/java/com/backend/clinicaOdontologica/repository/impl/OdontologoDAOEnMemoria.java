package com.backend.clinicaOdontologica.repository.impl;

import com.backend.clinicaOdontologica.entity.Odontologo;
import com.backend.clinicaOdontologica.repository.IDao;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class OdontologoDAOEnMemoria implements IDao<Odontologo> {
    //private Logger LOGGER = LogManager.getLogger(DomicilioDaoH2.class)
    private Logger LOGGER = LoggerFactory.getLogger(OdontologoDAOEnMemoria.class);
    private final Map<Long, Odontologo> odontologoMap = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();

    @Override
    public Odontologo registrar(Odontologo odontologo) {
        LOGGER.info("Odontologo a ser guardado/registrado: " + odontologo);

        long id = idGenerator.incrementAndGet();
        odontologo.setId(id);
        odontologoMap.put(id, odontologo);

        LOGGER.info("Ha sido registrado/guardado el siguiente odontologo: " + odontologo);
        return odontologo;
    }

    @Override
    public Odontologo buscarPorId(Long id) {
        return null; //TODO hay que implementarlo aun
    }

    @Override
    public List<Odontologo> listarTodos() {
        LOGGER.info("Listando todos los odontologos solicitados");

        LOGGER.info("Los odontologos en la lista son" + odontologoMap.values());
        return new ArrayList<>(odontologoMap.values());
    }
}
