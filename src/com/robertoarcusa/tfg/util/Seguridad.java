package com.robertoarcusa.tfg.util;

import com.robertoarcusa.tfg.clases.Socio;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Clase utilitaria para la gestión de contraseñas utilizando el algoritmo BCrypt.
 * <p>
 * Ofrece métodos para verificar contraseñas, hashearlas y actualizar su valor
 * en la base de datos si es necesario.
 *
 * @author Roberto Arcusa
 * @version 1.0
 * @since 2025
 */

public class Seguridad {

    /**
     * Verifica si la contraseña introducida coincide con el hash almacenado.
     * <p>
     * Si la contraseña es incorrecta pero el hash parece desactualizado o inválido,
     * se genera un nuevo hash y se actualiza automáticamente en la base de datos
     * del socio correspondiente.
     *
     * @param contrasenaIntroducida Contraseña introducida por el usuario.
     * @param contrasenaHash        Hash de la contraseña almacenada en la base de datos.
     * @param dni                   DNI del socio al que pertenece la contraseña.
     * @return {@code true} si la contraseña es válida; {@code false} en caso contrario.
     */
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

    /**
     * Actualiza el hash de la contraseña del socio en la base de datos.
     * <p>
     * Utiliza Hibernate para buscar el socio por DNI y actualizar su contraseña.
     *
     * @param nuevoHash Nuevo hash de la contraseña generado.
     * @param dni       DNI del socio cuya contraseña se actualizará.
     */
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

    /**
     * Genera un hash seguro de una contraseña utilizando BCrypt.
     * <p>
     * Este método debe usarse durante el registro o creación de usuarios.
     *
     * @param contrasena Contraseña en texto plano.
     * @return Hash de la contraseña.
     */
    public static String hashear(String contrasena) {
        return BCrypt.hashpw(contrasena, BCrypt.gensalt());
    }
}
