package com.robertoarcusa.tfg.dao;

import com.robertoarcusa.tfg.clases.Socio;
import com.robertoarcusa.tfg.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class SocioDAO {

    public List<Socio> obtenerTodosLosSocios() {
        List<Socio> socios = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            Query<Socio> query = session.createQuery("FROM Socio", Socio.class);
            socios = query.getResultList();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return socios;
    }

    public void agregarSocio(Socio socio) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.save(socio);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminarSocio(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            Socio socio = session.get(Socio.class, id);

            if (socio != null) {
                session.delete(socio);
                session.getTransaction().commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al eliminar el socio");
        }
    }

    public boolean existeDni(String dni) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            String hql = "SELECT COUNT(*) FROM Socio WHERE dni = :dni";
            Long count = (Long) session.createQuery(hql)
                    .setParameter("dni", dni)
                    .uniqueResult();
            return count != null && count > 0;
        } finally {
            session.close();
        }
    }

    public Socio obtenerSocioPorId(int id) {
        Socio socio = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            socio = session.get(Socio.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return socio;
    }

    public void actualizarSocio(Socio socio) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(socio);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();  //rollback deshace los cambios hechos si ocurre algún error
            e.printStackTrace();
        }
    }

}