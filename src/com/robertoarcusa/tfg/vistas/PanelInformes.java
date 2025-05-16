package com.robertoarcusa.tfg.vistas;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class PanelInformes extends JPanel {
    private JButton btnInformeSocios;
    private JButton btnInformeClases;
    private JButton btnInformeEntrenadores;
    private JButton btnInformeTotalEntrenadoresEspecialidad;

    public PanelInformes() {
        setLayout(new GridBagLayout());

        btnInformeSocios = new JButton("GENERAR INFORME • LISTADO DE SOCIOS");
        btnInformeEntrenadores = new JButton("GENERAR INFORME • ENTRENADORES POR ESPECIALIDAD");
        btnInformeClases = new JButton("GENERAR INFORME • MEDIA CAPACIDAD MÁXIMA CLASES");
        btnInformeTotalEntrenadoresEspecialidad = new JButton("GENERAR INFORME  • TOTAL ENTRENADORES POR ESPECIALIDAD");

        // Estilo común para todos los botones
        Dimension buttonSize = new Dimension(500, 120); // Ancho x Alto
        Font buttonFont = new Font("Arial", Font.PLAIN, 16); // Fuente más grande

        JButton[] botones = {
                btnInformeSocios,
                btnInformeEntrenadores,
                btnInformeClases,
                btnInformeTotalEntrenadoresEspecialidad
        };

        for (JButton boton : botones) {
            boton.setPreferredSize(buttonSize);
            boton.setFont(buttonFont);
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(15, 0, 15, 0); // Espaciado vertical

        gbc.gridy = 0;
        add(btnInformeSocios, gbc);

        gbc.gridy = 1;
        add(btnInformeEntrenadores, gbc);

        gbc.gridy = 2;
        add(btnInformeClases, gbc);

        gbc.gridy = 3;
        add(btnInformeTotalEntrenadoresEspecialidad, gbc);

        btnInformeSocios.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Llamamos al método que genera el informe
                generarInformeSocios();
            }
        });

        btnInformeEntrenadores.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String especialidad = JOptionPane.showInputDialog(null, "Introduce la especialidad:");
                if (especialidad != null && !especialidad.trim().isEmpty()) {
                    generarInformeEntrenadoresPorEspecialidad(especialidad.trim());
                }
            }
        });

        btnInformeClases.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Llamamos al método que genera el informe
                generarInformeCapacidadMaximaClases();
            }
        });

        btnInformeTotalEntrenadoresEspecialidad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Llamamos al método que genera el informe
                generarInformeTotEntrenadoresEspecialidad();
            }
        });

    }

    public void generarInformeSocios() {
        try {
            InputStream inputt = getClass().getResourceAsStream("/com/robertoarcusa/tfg/informes/informeSocios.jasper");
            if (inputt == null) {
                System.out.println("No se encontró el archivo .jasper");
            } else {
                System.out.println("Archivo .jasper encontrado correctamente");
            }

            // Cargar el controlador de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Conectar a la base de datos (cambia los valores a los de tu configuración)
            Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/bbdd_gimnasio", "root", "");

            // Cargar el archivo .jasper (el informe ya compilado)
            InputStream input = getClass().getResourceAsStream("/com/robertoarcusa/tfg/informes/informeSocios.jasper");
            JasperReport informe = (JasperReport) JRLoader.loadObject(input);

            // Crear los parámetros para el informe (si necesitas algunos)
            Map<String, Object> parametros = new HashMap<>();
            // Por ejemplo, si tienes parámetros en tu informe (no los tienes ahora mismo, pero si fuera el caso):
            // parametros.put("parametro1", valor);

            // Llenar el informe con los datos
            JasperPrint jasperPrint = JasperFillManager.fillReport(informe, parametros, conexion);

            // Visualizar el informe
            JasperViewer.viewReport(jasperPrint, false);

            // Cerrar la conexión a la base de datos
            conexion.close();

        } catch (ClassNotFoundException | SQLException | JRException e) {
            e.printStackTrace();
        }
    }

    public void generarInformeEntrenadoresPorEspecialidad(String especialidad) {
        try {
            // Ruta del informe
            InputStream input = getClass().getResourceAsStream("/com/robertoarcusa/tfg/informes/informeEspecialidadEntrenadores.jasper");

            if (input == null) {
                System.out.println("No se encontró el archivo .jasper");
                return;
            } else {
                System.out.println("Archivo .jasper encontrado correctamente");
            }

            // Cargar el driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Conexión a la base de datos
            Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/bbdd_gimnasio", "root", "");

            // Cargar el informe
            JasperReport informe = (JasperReport) JRLoader.loadObject(input);

            // Crear y establecer parámetros
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("especialidadFiltro", especialidad); // Este nombre debe coincidir con el parámetro del informe

            // Llenar el informe
            JasperPrint jasperPrint = JasperFillManager.fillReport(informe, parametros, conexion);

            // Mostrar el informe
            JasperViewer.viewReport(jasperPrint, false);

            // Cerrar conexión
            conexion.close();

        } catch (ClassNotFoundException | SQLException | JRException e) {
            e.printStackTrace();
        }
    }

    public void generarInformeCapacidadMaximaClases() {
        try {
            // Ruta del informe
            InputStream input = getClass().getResourceAsStream("/com/robertoarcusa/tfg/informes/informeMediaCapacidadMaximaClases.jasper");

            if (input == null) {
                System.out.println("No se encontró el archivo .jasper");
                return;
            } else {
                System.out.println("Archivo .jasper encontrado correctamente");
            }

            // Cargar el driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Conexión a la base de datos
            Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/bbdd_gimnasio", "root", "");

            // Cargar el informe
            JasperReport informe = (JasperReport) JRLoader.loadObject(input);

            // Crear y establecer parámetros
            Map<String, Object> parametros = new HashMap<>();

            // Llenar el informe
            JasperPrint jasperPrint = JasperFillManager.fillReport(informe, parametros, conexion);

            // Mostrar el informe
            JasperViewer.viewReport(jasperPrint, false);

            // Cerrar conexión
            conexion.close();

        } catch (ClassNotFoundException | SQLException | JRException e) {
            e.printStackTrace();
        }
    }

    public void generarInformeTotEntrenadoresEspecialidad() {
        try {
            // Ruta del informe
            InputStream input = getClass().getResourceAsStream("/com/robertoarcusa/tfg/informes/informeTotalEspecialidadesEntrenadores.jasper");

            if (input == null) {
                System.out.println("No se encontró el archivo .jasper");
                return;
            } else {
                System.out.println("Archivo .jasper encontrado correctamente");
            }

            // Cargar el driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Conexión a la base de datos
            Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/bbdd_gimnasio", "root", "");

            // Cargar el informe
            JasperReport informe = (JasperReport) JRLoader.loadObject(input);

            // No hay parámetros
            Map<String, Object> parametros = new HashMap<>();

            // Llenar el informe
            JasperPrint jasperPrint = JasperFillManager.fillReport(informe, parametros, conexion);

            // Mostrar el informe
            JasperViewer.viewReport(jasperPrint, false);

            // Cerrar conexión
            conexion.close();

        } catch (ClassNotFoundException | SQLException | JRException e) {
            e.printStackTrace();
        }
    }

    // Getters si necesitas manejar los eventos desde fuera
    public JButton getBtnInformeSocios() {
        return btnInformeSocios;
    }

    public JButton getBtnInformeClases() {
        return btnInformeClases;
    }

    public JButton getBtnInformeEntrenadores() {
        return btnInformeEntrenadores;
    }

    public JButton getBtnInformeTotalEntrenadoresEspecialidad() {
        return btnInformeTotalEntrenadoresEspecialidad;
    }
}
