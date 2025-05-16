package com.robertoarcusa.tfg.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JFormattedTextField.AbstractFormatter;

public class FormateadorFecha extends AbstractFormatter {

    private final String formatoFecha = "yyyy-MM-dd";
    private final SimpleDateFormat formateadorFecha = new SimpleDateFormat(formatoFecha);

    @Override
    public Object stringToValue(String text) throws ParseException {
        return formateadorFecha.parse(text);
    }

    @Override
    public String valueToString(Object value) throws ParseException {
        if (value != null) {
            Calendar cal = (Calendar) value;
            return formateadorFecha.format(cal.getTime());
        }
        return "";
    }
}

