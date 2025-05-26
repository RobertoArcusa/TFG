package com.robertoarcusa.tfg.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Clase utilitaria para manejar la configuración y acceso a la {@link SessionFactory}
 * de Hibernate, que es la fábrica de sesiones para la gestión de la conexión a la base de datos.
 * <p>
 * Esta clase inicializa la {@code SessionFactory} de forma estática y única,
 * asegurando que solo exista una instancia durante toda la ejecución de la aplicación.
 * Proporciona un método estático para obtener dicha instancia.
 *
 * @author Roberto Arcusa
 * @version 1.0
 * @since 2025
 */

public class HibernateUtil {

    /**
     * Creamos una única instancia de {@link SessionFactory}.
     * Se crea al cargar la clase y permanece durante toda la vida útil de la aplicación.
     */
    private static final SessionFactory sessionFactory;

    // Bloque estático que inicializa la SessionFactory una única vez
    static {
        try {
            // Cargamos la configuración desde el archivo hibernate.cfg.xml y construimos la SessionFactory
            sessionFactory = new Configuration()
                    .configure("hibernate.cfg.xml")
                    .buildSessionFactory();
        } catch (Throwable ex) {
            // Si hay algún error, lo mostramos por consola
            System.err.println("Inicialización fallida de la SessionFactory: " + ex);
            // Y lanzamos una excepción para evitar que la aplicación siga
            throw new ExceptionInInitializerError(ex);
        }
    }

    // Método para acceder desde cualquier parte de la app a la SessionFactory
    /**
     * Devuelve la instancia única de {@link SessionFactory} para la gestión de sesiones.
     *
     * @return la {@code SessionFactory} inicializada.
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
