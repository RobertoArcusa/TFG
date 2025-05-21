package com.robertoarcusa.tfg.dao;

import com.robertoarcusa.tfg.clases.Pago;
import com.robertoarcusa.tfg.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class PagoDAO {

    public void actualizarPago(Pago pago) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(pago);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback(); //rollback deshace los cambios hechos si ocurre algún error
            e.printStackTrace();
        }
    }

    public void agregarPago(Pago pago) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(pago);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public boolean eliminarPago(int idPago) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            Pago pago = session.get(Pago.class, idPago);

            if (pago != null) {
                session.delete(pago);
                transaction.commit();
                return true;
            } else {
                System.out.println("Pago no encontrado");
                return false;
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace(); // opcional para depurar
            return false;
        } finally {
            session.close();
        }
    }

    public Pago obtenerPagoPorId(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Pago.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Pago> obtenerTodosLosPagos() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Pago> query = session.createQuery("FROM Pago", Pago.class);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}