package com.robertoarcusa.tfg.util;

/**
 * Clase utilitaria para la validación de números de Documento Nacional de Identidad (DNI) españoles.
 * <p>
 * Proporciona métodos para verificar que un DNI tiene el formato correcto y que la letra
 * corresponde correctamente al número según el algoritmo oficial.
 *
 * @author Roberto Arcusa
 * @version 1.0
 * @since 2025
 */

public class DNIUtils {

    // Lista de letras válidas para el DNI
    private static final char[] LETRAS_DNI = {'T', 'R', 'W', 'A', 'G', 'M', 'Y', 'F', 'P', 'D', 'X', 'B', 'N', 'J', 'Z', 'S', 'Q', 'V', 'H', 'L', 'C', 'K', 'E'};

    /**
     * Valida si un DNI es correcto.
     * <p>
     * Comprueba que la cadena tenga 9 caracteres, que los primeros 8 sean dígitos y
     * que el último sea una letra válida según el algoritmo oficial.
     * También verifica que la letra corresponda al número calculado.
     *
     * @param dni Número de DNI a validar (formato esperado: 8 dígitos seguidos de una letra).
     * @return {@code true} si el DNI es válido; {@code false} en caso contrario.
     */
    public static boolean esDNIValido(String dni) {
        if (dni == null || dni.length() != 9) {
            return false; // Hacemos que el DNI deba tener 9 caracteres (8 números y 1 letra)
        }

        dni = dni.toUpperCase(); // Aseguramos que la letra esté en mayúsculas

        String numero = dni.substring(0, 8); // Sacamos los primeros 8 caracteres
        char letra = dni.charAt(8); // Hacemos que el último carácter sea una letra

        // Validar formato general: 8 dígitos y una letra válida
        if (!dni.matches("\\d{8}[A-HJ-NP-TV-Z]")) {
            return false; // Si el formato es incorrecto o la letra es inválida (I, Ñ, O, U excluidas) retornamos false
        }

        // Calcular letra correspondiente
        int numeroDNI = Integer.parseInt(numero);
        char letraCalculada = LETRAS_DNI[numeroDNI % 23]; // Resto de la división entre 23

        // Comparar con la letra introducida
        return letra == letraCalculada;
    }
}
