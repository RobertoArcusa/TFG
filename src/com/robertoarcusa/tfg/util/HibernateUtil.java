package com.robertoarcusa.tfg.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    // Creamos una única instancia de SessionFactory (la fábrica de sesiones de Hibernate)
    private static final SessionFactory sessionFactory;

    // Este bloque se ejecuta una única vez al usar la clase
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
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
