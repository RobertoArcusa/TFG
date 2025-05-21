package com.robertoarcusa.tfg.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;

public class DatabaseInitializer {

    private static final String DB_NAME = "bbdd_gimnasio";
    private static final String SQL_SCRIPT_PATH = "/sql/bbdd_gimnasio.sql";

    private static final String JDBC_URL_WITHOUT_DB = "jdbc:mysql://localhost:3306/?useSSL=false&allowMultiQueries=true";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "";

    public static void initializeDatabase() {
        if (databaseExists()) {
            System.out.println("La base de datos ya existe. No se hace nada.");
            return;
        }

        System.out.println("La base de datos no existe. Ejecutando script de inicialización...");

        try (Connection conn = DriverManager.getConnection(JDBC_URL_WITHOUT_DB, DB_USERNAME, DB_PASSWORD)) {
            String sqlScript = loadSqlScript(SQL_SCRIPT_PATH);
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(sqlScript);
                System.out.println("Base de datos creada y poblada correctamente.");
            }
        } catch (Exception e) {
            System.err.println("Error al inicializar la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static boolean databaseExists() {
        try (Connection conn = DriverManager.getConnection(JDBC_URL_WITHOUT_DB, DB_USERNAME, DB_PASSWORD);
             ResultSet rs = conn.getMetaData().getCatalogs()) {

            while (rs.next()) {
                String dbName = rs.getString(1);
                if (DB_NAME.equalsIgnoreCase(dbName)) {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar existencia de la base de datos: " + e.getMessage());
        }
        return false;
    }

    private static String loadSqlScript(String path) throws Exception {
        InputStream input = DatabaseInitializer.class.getResourceAsStream(path);
        if (input == null) {
            throw new RuntimeException("Archivo SQL no encontrado en: " + path);
        }

        StringBuilder script = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
            String line;
            while ((line = reader.readLine()) != null) {
                script.append(line).append("\n");
            }
        }

        return script.toString();
    }
}