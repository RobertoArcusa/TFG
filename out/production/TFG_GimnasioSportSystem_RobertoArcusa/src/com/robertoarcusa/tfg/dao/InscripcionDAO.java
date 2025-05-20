package com.robertoarcusa.tfg.dao;

import com.robertoarcusa.tfg.clases.Inscripcion;
import com.robertoarcusa.tfg.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class InscripcionDAO {

    public void agregarInscripcion(Inscripcion inscripcion) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(inscripcion);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback(); //rollback deshace los cambios hechos si ocurre algún error
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void eliminarInscripcion(Inscripcion inscripcion) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.delete(inscripcion);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback(); // Rollback si error
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public Inscripcion obtenerInscripcionPorId(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.get(Inscripcion.class, id);  // Buscamos la inscripción por su ID
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            session.close();
        }
    }

    public static List<Inscripcion> obtenerTodasLasInscripciones() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.createQuery("from Inscripcion", Inscripcion.class).list();  // Traemos todas las inscripciones
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            session.close();
        }
    }

    public List<Inscripcion> obtenerInscripcionesPorSocio(int idSocio) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.createQuery(
                    "FROM Inscripcion i WHERE i.socio.idSocio = :idSocio", Inscripcion.class)
                    .setParameter("idSocio", idSocio)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            session.close();
        }
    }

    public void modificarInscripcion(Inscripcion inscripcion) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(inscripcion);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();  // Rollback si ocurre un error
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}
