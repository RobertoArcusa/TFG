package com.robertoarcusa.tfg.dao;

import com.robertoarcusa.tfg.clases.Entrenador;
import com.robertoarcusa.tfg.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class EntrenadorDAO {

    public void agregarEntrenador(Entrenador entrenador) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(entrenador);
            transaction.commit();
        } catch (Exception e) {
            // Rollback deshace los cambios hechos si ocurre algún error
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void actualizarEntrenador(Entrenador entrenador) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(entrenador);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public boolean eliminarEntrenador(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            Entrenador entrenador = session.get(Entrenador.class, id);

            if (entrenador != null) {
                session.delete(entrenador);
                session.getTransaction().commit();
                return true;
            }
            return false;

        } catch (Exception e) {
            return false;
        }
    }

    public Entrenador obtenerEntrenadorPorId(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.get(Entrenador.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            session.close();
        }
    }

    public static List<Entrenador> obtenerTodosLosEntrenadores() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.createQuery("from Entrenador", Entrenador.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            session.close();
        }
    }

    public boolean existeTelefono(String telefono) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            String hql = "select count(e) from Entrenador e where e.telefono = :telefono";
            Long count = (Long) session.createQuery(hql)
                    .setParameter("telefono", telefono)
                    .uniqueResult();
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }
}
