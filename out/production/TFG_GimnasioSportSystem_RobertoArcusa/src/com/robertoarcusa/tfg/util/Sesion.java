package com.robertoarcusa.tfg.util;

import com.robertoarcusa.tfg.clases.Socio;
import com.robertoarcusa.tfg.enums.TipoUsuario;

/**
 * Clase utilitaria para la gestión de la sesión del usuario actual en la aplicación.
 * <p>
 * Esta clase permite almacenar y consultar información sobre el {@link Socio} que ha iniciado sesión,
 * así como determinar su tipo de usuario.
 *
 * @author Roberto Arcusa
 * @version 1.0
 * @since 2025
 */

public class Sesion {

    /**
     * Referencia estática al socio actualmente autenticado en la sesión.
     */
    private static Socio usuarioActual;

    /**
     * Establece el usuario actual de la sesión.
     *
     * @param socio El objeto {@link Socio} correspondiente al usuario autenticado.
     */
    public static void setUsuarioActual(Socio socio) {
        usuarioActual = socio;
    }

    /**
     * Devuelve el usuario actual de la sesión.
     *
     * @return El objeto {@link Socio} correspondiente al usuario autenticado, o {@code null} si no hay sesión activa.
     */
    public static Socio getUsuarioActual() {
        return usuarioActual;
    }

    /**
     * Obtiene el tipo de usuario del socio autenticado.
     *
     * @return Un valor de {@link TipoUsuario} (ADMIN, EDITOR, BASIC) o {@code null} si no hay usuario activo.
     */
    public static TipoUsuario getTipoUsuarioActual() {
        return usuarioActual != null ? usuarioActual.getTipoUsuario() : null;
    }

    /**
     * Verifica si el usuario actual tiene rol de administrador.
     *
     * @return {@code true} si el tipo de usuario es {@link TipoUsuario#ADMIN}, {@code false} en caso contrario.
     */
    public static boolean esAdmin() {
        return getTipoUsuarioActual() == TipoUsuario.ADMIN;
    }

    /**
     * Verifica si el usuario actual tiene rol de editor.
     *
     * @return {@code true} si el tipo de usuario es {@link TipoUsuario#EDITOR}, {@code false} en caso contrario.
     */
    public static boolean esEditor() {
        return getTipoUsuarioActual() == TipoUsuario.EDITOR;
    }

    /**
     * Verifica si el usuario actual tiene rol básico.
     *
     * @return {@code true} si el tipo de usuario es {@link TipoUsuario#BASIC}, {@code false} en caso contrario.
     */
    public static boolean esBasic() {
        return getTipoUsuarioActual() == TipoUsuario.BASIC;
    }
}