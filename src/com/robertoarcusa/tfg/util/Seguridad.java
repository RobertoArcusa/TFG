package com.robertoarcusa.tfg.util;

import com.robertoarcusa.tfg.clases.Socio;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.mindrot.jbcrypt.BCrypt;

public class Seguridad {

    // Método para verificar la contraseña
    public static boolean verificar(String contrasenaIntroducida, String contrasenaHash, String dni) {
        try {
            // Verificamos si la contraseña introducida coincide con el hash almacenado
            if (BCrypt.checkpw(contrasenaIntroducida, contrasenaHash)) {
                // Contraseña válida, ya que el hash coincide
                return true;
            } else {
                // Si la contraseña no coincide, intentar re-hashear
                System.out.println("Hash no válido, actualizando la contraseña...");
                String nuevoHash = BCrypt.hashpw(contrasenaIntroducida, BCrypt.gensalt());
                // Actualizamos la contraseña con el nuevo hash en la base de datos
                actualizarContrasena(nuevoHash, dni);
                return false;  // No coincide, pero hemos actualizado el hash
            }
        } catch (IllegalArgumentException e) {
            // Si se produce un error al verificar el hash
            System.err.println("Error al verificar la contraseña: " + e.getMessage());
            return false;
        }
    }

    // Método para actualizar la contraseña con el nuevo hash en la base de datos
    private static void actualizarContrasena(String nuevoHash, String dni) {
        // Iniciar sesión con Hibernate
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            // Buscar el socio en la base de datos por su DNI
            Socio socio = session.createQuery("FROM Socio WHERE dni = :dni", Socio.class)
                    .setParameter("dni", dni)
                    .uniqueResult();

            if (socio != null) {
                // Actualizamos el hash de la contraseña
                socio.setContrasena(nuevoHash);
                session.update(socio);  // Actualizamos el socio en la base de datos
                transaction.commit();  // Confirmamos la transacción
                System.out.println("Contraseña actualizada exitosamente.");
            } else {
                System.out.println("No se encontró el socio con DNI: " + dni);
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();  // Si algo sale mal, hacemos rollback
            }
            e.printStackTrace();
        } finally {
            session.close();  // Cerramos la sesión
        }
    }

    // Método para encriptar una nueva contraseña (para el registro)
    public static String hashear(String contrasena) {
        return BCrypt.hashpw(contrasena, BCrypt.gensalt());
    }
}
