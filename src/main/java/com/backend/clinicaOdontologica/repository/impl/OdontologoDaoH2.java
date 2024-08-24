package com.backend.clinicaOdontologica.repository.impl;

import com.backend.clinicaOdontologica.dbconnection.H2Connection;
import com.backend.clinicaOdontologica.entity.Odontologo;
import com.backend.clinicaOdontologica.repository.IDao;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OdontologoDaoH2 implements IDao<Odontologo> {

    private final Logger LOGGER = LoggerFactory.getLogger(OdontologoDaoH2.class);
    @Override
    public Odontologo registrar(Odontologo odontologo) {
        LOGGER.info("Odontólogo a registrar: " + odontologo);

        Odontologo odontologoRegistrado = null;
        Connection connection = null;

        try{
            connection = H2Connection.getConnection();
            connection.setAutoCommit(false);

            String insert = "INSERT INTO ODONTOLOGOS(NUMERO_MATRICULA, NOMBRE, APELLIDO) VALUES(?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insert, PreparedStatement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, odontologo.getNumero_matricula());
            preparedStatement.setString(2, odontologo.getNombre());
            preparedStatement.setString(3, odontologo.getApellido());
            preparedStatement.execute();

            connection.commit();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            while (resultSet.next()) {
                odontologoRegistrado = new Odontologo(resultSet.getLong(1), odontologo.getNumero_matricula(), odontologo.getNombre(), odontologo.getApellido());
            }
            LOGGER.info("Odontologo registrado: " + odontologoRegistrado);

        }
        catch(Exception exception){
            LOGGER.error(exception.getMessage());
            exception.printStackTrace();
            if(connection != null){
                try{
                    connection.rollback();
                    LOGGER.error("Tuvimos un problema. " + exception.getMessage());
                    exception.printStackTrace();
                } catch (SQLException sqlException){
                    LOGGER.error(sqlException.getMessage());
                    sqlException.printStackTrace();

                }
            }

        } finally {
            try{
                connection.close();
            }catch (Exception ex){
                LOGGER.error("No se pudo cerrar la conexión: " + ex.getMessage());
            }

        }


        return odontologoRegistrado;
    }

    @Override
    public Odontologo buscarPorId(Long id) {
        return null; //TODO implementar este metodo
    }

    @Override
    public List<Odontologo> listarTodos() {
        Connection connection = null;
        List<Odontologo> odontologos = new ArrayList<>();


        try{
            connection = H2Connection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM ODONTOLOGOS");
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Odontologo odontologo = new Odontologo(resultSet.getLong(1), resultSet.getInt(2), resultSet.getString(3), resultSet.getString(4));
                odontologos.add(odontologo);

            }
            LOGGER.info("Tenemos " + odontologos.size() + " Odontologos.");
            LOGGER.info("Lista de odontologos: " + odontologos);
        }
        catch (Exception exception){
            LOGGER.error(exception.getMessage());
            exception.printStackTrace();
        } finally {
            try{
                connection.close(); // siempre debo intentar cerrar la conexión en el bloque finally
            }catch (Exception ex){
                LOGGER.error("No se pudo cerrar la conexión: " + ex.getMessage());
            }
        }
        return odontologos;
    }
}
