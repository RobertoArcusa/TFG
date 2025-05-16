package com.robertoarcusa.tfg.util;

import com.robertoarcusa.tfg.clases.Socio;
import com.robertoarcusa.tfg.enums.TipoUsuario;

public class Sesion {

    private static Socio usuarioActual;

    public static void setUsuarioActual(Socio socio) {
        usuarioActual = socio;
    }

    public static Socio getUsuarioActual() {
        return usuarioActual;
    }

    public static TipoUsuario getTipoUsuarioActual() {
        return usuarioActual != null ? usuarioActual.getTipoUsuario() : null;
    }

    public static boolean esAdmin() {
        return getTipoUsuarioActual() == TipoUsuario.ADMIN;
    }

    public static boolean esEditor() {
        return getTipoUsuarioActual() == TipoUsuario.EDITOR;
    }

    public static boolean esBasic() {
        return getTipoUsuarioActual() == TipoUsuario.BASIC;
    }
}