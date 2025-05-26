package com.robertoarcusa.tfg.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JFormattedTextField.AbstractFormatter;

/**
 * Clase que extiende {@link AbstractFormatter} para proporcionar
 * un formateador de fechas con el formato {@code "yyyy-MM-dd"}.
 * <p>
 * Esta clase convierte cadenas de texto en objetos {@link Calendar}
 * y viceversa, facilitando la entrada y presentación de fechas
 * en componentes Swing como JFormattedTextField.
 *
 * @author Roberto Arcusa
 * @version 1.0
 * @since 2025
 */

public class FormateadorFecha extends AbstractFormatter {

    private final String formatoFecha = "yyyy-MM-dd";
    private final SimpleDateFormat formateadorFecha = new SimpleDateFormat(formatoFecha);

    /**
     * Convierte una cadena de texto en un objeto {@link java.util.Date}.
     * <p>
     * El texto debe estar en el formato "yyyy-MM-dd".
     *
     * @param text Cadena de texto que representa una fecha.
     * @return Objeto {@link java.util.Date} resultante del parseo.
     * @throws ParseException si el texto no tiene el formato correcto.
     */
    @Override
    public Object stringToValue(String text) throws ParseException {
        return formateadorFecha.parse(text);
    }

    /**
     * Convierte un objeto {@link Calendar} en una cadena de texto
     * con el formato "yyyy-MM-dd".
     *
     * @param value Objeto que debe ser una instancia de {@link Calendar}.
     * @return Cadena formateada que representa la fecha, o cadena vacía
     *         si el valor es {@code null}.
     * @throws ParseException si ocurre un error durante el formateo.
     */
    @Override
    public String valueToString(Object value) throws ParseException {
        if (value != null) {
            Calendar cal = (Calendar) value;
            return formateadorFecha.format(cal.getTime());
        }
        return "";
    }
}

