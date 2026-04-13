package org.alopsalv.novabank.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnectionManager {
    private static DatabaseConnectionManager instance;
    private static final Properties properties = new Properties();

    private DatabaseConnectionManager() {
        //Cargamos el archivo de configuración al iniciar
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("database.properties")) {
            if (input == null) {
                throw new RuntimeException("No se pudo encontrar el archivo database.properties");
            }
            properties.load(input);
        } catch (IOException ex) {
            throw new RuntimeException("Error al cargar la configuración de la base de datos", ex);
        }
    }

    public static DatabaseConnectionManager getInstance() {
        if (instance == null) {
            instance = new DatabaseConnectionManager();
        }
        return instance;
    }

    public static Connection getConnection() throws SQLException {
        //Forzamos la carga inicial
        getInstance();
        return DriverManager.getConnection(
                properties.getProperty("db.url"),
                properties.getProperty("db.user"),
                properties.getProperty("db.password")
        );
    }
}