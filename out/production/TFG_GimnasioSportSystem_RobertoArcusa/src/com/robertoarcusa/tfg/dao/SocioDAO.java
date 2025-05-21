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

    public boolean eliminarSocio(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            Socio socio = session.get(Socio.class, id);

            if (socio != null) {
                // Comprueba datos relacionados
                Long pagosCount = (Long) session.createQuery("SELECT COUNT(p) FROM Pago p WHERE p.socio.idSocio = :id")
                        .setParameter("id", id).uniqueResult();

                Long inscripcionesCount = (Long) session.createQuery("SELECT COUNT(i) FROM Inscripcion i WHERE i.socio.idSocio = :id")
                        .setParameter("id", id).uniqueResult();

                if (pagosCount > 0 || inscripcionesCount > 0) {
                    return false;  // No se elimina porque hay datos relacionados
                }

                session.delete(socio);
                session.getTransaction().commit();
                return true;  // Eliminado con éxito
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al eliminar el socio");
        }
        return false;  // No encontrado o no eliminado
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