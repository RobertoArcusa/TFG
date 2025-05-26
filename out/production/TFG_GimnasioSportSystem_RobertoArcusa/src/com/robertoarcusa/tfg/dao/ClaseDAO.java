package com.robertoarcusa.tfg.dao;

import com.robertoarcusa.tfg.clases.Clase;
import com.robertoarcusa.tfg.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ClaseDAO {

    public void agregarClase(Clase clase) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(clase);
            transaction.commit();
        } catch (Exception e) {
            // Rollback deshace los cambios hechos si ocurre algún error
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void actualizarClase(Clase clase) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(clase);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public boolean eliminarClase(Clase clase) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.delete(clase);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            return false;
        } finally {
            session.close();
        }
    }

    public Clase obtenerClasePorId(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.get(Clase.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            session.close();
        }
    }

    public static List<Clase> obtenerTodasLasClases() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            // Obtenemos todas las clases
            return session.createQuery("from Clase", Clase.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            session.close();
        }
    }

}