package app.config;

import app.exceptions.MyException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConectorMySQL {

    private Connection conexion;

    public ConectorMySQL() throws MyException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Properties props = new Properties();

            InputStream entrada = Thread.currentThread()
                    .getContextClassLoader()
                    .getResourceAsStream("db.properties");

            if (entrada == null) {
                entrada = new FileInputStream(
                        "C:/Users/Usuario/Documents/DayneiraApp/src/main/resources/db.properties"
                );
            }

            if (entrada == null) {
                throw new MyException("No se encontro db.properties");
            }

            props.load(entrada);

            String url      = props.getProperty("db.url");
            String usuario  = props.getProperty("db.usuario");
            String password = props.getProperty("db.password");

            this.conexion = DriverManager.getConnection(url, usuario, password);

        } catch (ClassNotFoundException e) {
            throw new MyException("Driver MySQL no encontrado: " + e.getMessage());
        } catch (IOException e) {
            throw new MyException("Error leyendo db.properties: " + e.getMessage());
        } catch (SQLException e) {
            throw new MyException("Error conectando a MySQL: " + e.getMessage());
        }
    }

    public Connection getConexion() {
        return conexion;
    }
}