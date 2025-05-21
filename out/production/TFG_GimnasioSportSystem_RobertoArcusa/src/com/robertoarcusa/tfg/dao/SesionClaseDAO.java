package com.robertoarcusa.tfg.dao;

import com.robertoarcusa.tfg.clases.SesionClase;
import com.robertoarcusa.tfg.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

public class SesionClaseDAO {

    public void agregarSesion(SesionClase sesionClase) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(sesionClase);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback(); //rollback deshace los cambios hechos si ocurre algún error
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void actualizarSesion(SesionClase sesionClase) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(sesionClase);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public boolean eliminarSesion(SesionClase sesionClase) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.delete(sesionClase);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            return false;
        } finally {
            session.close();
        }
    }

    public boolean existeSesion(Timestamp fechaHora) {
        fechaHora.setNanos(0);

        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            String hql = "SELECT count(*) FROM SesionClase s WHERE FUNCTION('DATE_FORMAT', s.fechaHora, '%Y-%m-%d %H:%i') = :fechaHoraStr";
            Long count = session.createQuery(hql, Long.class)
                    .setParameter("fechaHoraStr", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(fechaHora))
                    .uniqueResult();

            return count != null && count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

    public static List<SesionClase> obtenerTodasLasSesiones() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.createQuery("from SesionClase", SesionClase.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            session.close();
        }
    }

    public SesionClase obtenerSesionPorId(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.get(SesionClase.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            session.close();
        }
    }

}
