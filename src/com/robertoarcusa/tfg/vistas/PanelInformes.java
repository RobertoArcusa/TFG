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
        // Layout vertical para apilar título y panel de botones
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // TÍTULO
        JLabel titulo = new JLabel("GENERADOR DE INFORMES", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 35));
        titulo.setForeground(new Color(0, 102, 204));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(titulo);

        // Espacio vertical entre título y botones (pequeño)
        add(Box.createRigidArea(new Dimension(0, 5)));

        // PANEL DE BOTONES
        JPanel panelBotones = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        // Crear botones (igual que antes)
        btnInformeSocios = new JButton("LISTADO DE SOCIOS");
        btnInformeEntrenadores = new JButton("LISTADO ENTRENADORES POR ESPECIALIDAD");
        btnInformeClases = new JButton("LISTADO MEDIA CAPACIDAD MÁXIMA CLASES");
        btnInformeTotalEntrenadoresEspecialidad = new JButton("TOTAL ENTRENADORES POR ESPECIALIDAD");
        JButton btnGraficoPagos = new JButton("LISTADO GRÁFICO TIPOS DE PAGO");

        Dimension buttonSize = new Dimension(400, 100);
        Font buttonFont = new Font("Arial", Font.PLAIN, 16);

        JButton[] botones = {
                btnInformeSocios,
                btnInformeEntrenadores,
                btnInformeClases,
                btnInformeTotalEntrenadoresEspecialidad,
                btnGraficoPagos
        };

        for (JButton boton : botones) {
            boton.setPreferredSize(buttonSize);
            boton.setFont(buttonFont);
        }

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1;
        panelBotones.add(btnInformeSocios, gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        panelBotones.add(btnInformeEntrenadores, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panelBotones.add(btnInformeClases, gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        panelBotones.add(btnInformeTotalEntrenadoresEspecialidad, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        panelBotones.add(btnGraficoPagos, gbc);

        // **Aquí descomenta para centrar el panel**
        panelBotones.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(panelBotones);

        // EVENTOS igual que antes
        btnInformeSocios.addActionListener(e -> generarInformeSocios());

        btnInformeEntrenadores.addActionListener(e -> {
            String especialidad = JOptionPane.showInputDialog(null, "Introduce la especialidad:");
            if (especialidad != null && !especialidad.trim().isEmpty()) {
                generarInformeEntrenadoresPorEspecialidad(especialidad.trim());
            }
        });

        btnInformeClases.addActionListener(e -> generarInformeCapacidadMaximaClases());

        btnInformeTotalEntrenadoresEspecialidad.addActionListener(e -> generarInformeTotEntrenadoresEspecialidad());

        btnGraficoPagos.addActionListener(e -> generarGraficoTiposDePago());
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

    public void generarGraficoTiposDePago() {
        try {
            // Ruta del informe
            InputStream input = getClass().getResourceAsStream("/com/robertoarcusa/tfg/informes/informeGraficoTipoPago.jasper");

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

            // No se necesita enviar parámetros
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
