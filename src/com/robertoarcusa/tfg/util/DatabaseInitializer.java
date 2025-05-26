package com.robertoarcusa.tfg.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;

/**
 * Clase utilitaria para inicializar la base de datos del gimnasio.
 * <p>
 * Comprueba si la base de datos "bbdd_gimnasio" existe en el servidor MySQL.
 * Si no existe, ejecuta un script SQL para crear y poblar la base de datos.
 * <p>
 * La conexión se realiza sin especificar una base de datos para permitir
 * la creación en caso de que no exista.
 *
 * @author Roberto Arcusa
 * @version 1.0
 * @since 2025
 */

public class DatabaseInitializer {

    // Nombre de la base de datos que vamos a comprobar o crear
    private static final String DB_NAME = "bbdd_gimnasio";

    // Ruta del script SQL dentro de la carpeta resources (src/main/resources/sql)
    private static final String SQL_SCRIPT_PATH = "/sql/bbdd_gimnasio.sql";

    // URL de conexión a MySQL SIN especificar la base de datos (para poder crearla si no existe)
    private static final String JDBC_URL_WITHOUT_DB = "jdbc:mysql://localhost:3306/?useSSL=false&allowMultiQueries=true";

    // Usuario y contraseña de MySQL (ajusta estos datos si usas otra configuración)
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "";

    /**
     * Método principal que se llama desde Main.
     * Comprueba si la base de datos existe. Si no existe, ejecuta el script SQL para crearla.
     */
    public static void initializeDatabase() {
        if (databaseExists()) {
            System.out.println("La base de datos ya existe.");
            return;
        }

        System.out.println("La base de datos no existe. Ejecutando script de inicialización...");

        // Intentamos conectar a MySQL sin especificar ninguna base de datos
        try (Connection conn = DriverManager.getConnection(JDBC_URL_WITHOUT_DB, DB_USERNAME, DB_PASSWORD)) {

            // Cargamos el contenido del archivo SQL como un texto largo
            String sqlScript = loadSqlScript(SQL_SCRIPT_PATH);

            // Ejecutamos el script usando un Statement (puede contener múltiples consultas)
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(sqlScript);
                System.out.println("Base de datos creada y poblada correctamente.");
            }
        } catch (Exception e) {
            System.err.println("Error al inicializar la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Este método verifica si ya existe una base de datos con el nombre "bbdd_gimnasio".
     * Lo hace consultando el catálogo (lista de bases de datos) del servidor MySQL.
     */
    private static boolean databaseExists() {
        try (
                // Conectamos al servidor MySQL (sin especificar base de datos)
                Connection conn = DriverManager.getConnection(JDBC_URL_WITHOUT_DB, DB_USERNAME, DB_PASSWORD);
                // Obtenemos el catálogo de bases de datos disponibles
                ResultSet rs = conn.getMetaData().getCatalogs()
        ) {
            // Recorremos la lista de bases de datos
            while (rs.next()) {
                String dbName = rs.getString(1);
                // Comparamos con el nombre que queremos
                if (DB_NAME.equalsIgnoreCase(dbName)) {
                    return true; // Existe
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar existencia de la base de datos: " + e.getMessage());
        }
        return false; // No existe
    }

    /**
     * Este método lee el contenido de un archivo SQL que está dentro de resources
     * y lo convierte en un único texto (String) para poder ejecutarlo luego.
     */
    private static String loadSqlScript(String path) throws Exception {
        // Abrimos el archivo usando el ClassLoader para que funcione bien con el jar y resources
        InputStream input = DatabaseInitializer.class.getResourceAsStream(path);

        // Si no se encuentra el archivo, lanzamos un error
        if (input == null) {
            throw new RuntimeException("Archivo SQL no encontrado en: " + path);
        }

        // Leemos línea por línea y vamos construyendo el script entero en un StringBuilder
        StringBuilder script = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
            String line;
            while ((line = reader.readLine()) != null) {
                script.append(line).append("\n"); // Añadimos salto de línea
            }
        }

        return script.toString(); // Devolvemos el contenido completo del script
    }
}