package com.robertoarcusa.tfg.vistas;

import com.robertoarcusa.tfg.clases.Clase;
import com.robertoarcusa.tfg.clases.SesionClase;
import com.robertoarcusa.tfg.clases.Socio;
import com.robertoarcusa.tfg.dao.ClaseDAO;
import com.robertoarcusa.tfg.dao.SesionClaseDAO;
import com.robertoarcusa.tfg.enums.TipoUsuario;
import com.robertoarcusa.tfg.util.Sesion;
import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PanelSesionClase extends JPanel {
    private JTable tablaSesiones;
    private DefaultTableModel modeloTabla;
    private JSpinner spinnerHora;
    private JTextField txtCapacidadDisponible;
    private JComboBox<Clase> comboClase;
    private JButton btnModificar, btnAgregar, btnEliminar, btnLimpiarCampos;
    private JDateChooser dateChooser;
    private JTextField txtBuscarSesionClase;

    public PanelSesionClase() {
        setLayout(new BorderLayout());

        JPanel panelCampos = new JPanel(new GridLayout(1, 3, 10, 10));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JPanel columna1 = new JPanel(new GridBagLayout());
        JPanel columna2 = new JPanel(new GridBagLayout());
        JPanel columna3 = new JPanel();

        comboClase = new JComboBox<>();
        comboClase.setPreferredSize(new Dimension(250, 45));
        comboClase.addActionListener(e -> actualizarCapacidadDisponible());

        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("yyyy-MM-dd");
        dateChooser.setPreferredSize(new Dimension(250, 45));

        txtCapacidadDisponible = new JTextField();
        txtCapacidadDisponible.setPreferredSize(new Dimension(250, 45));
        txtCapacidadDisponible.setEditable(false);

        spinnerHora = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editorHora = new JSpinner.DateEditor(spinnerHora, "HH:mm");
        spinnerHora.setEditor(editorHora);
        spinnerHora.setPreferredSize(new Dimension(250, 45));

        cargarClases();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 5, 35);

        columna1.add(new JLabel("CLASE:"), gbc); gbc.gridx = 1;
        columna1.add(comboClase, gbc);

        gbc.gridx = 0; gbc.gridy++;
        columna1.add(new JLabel("FECHA:"), gbc); gbc.gridx = 1;
        columna1.add(dateChooser, gbc);

        gbc.gridx = 0; gbc.gridy = 0;
        columna2.add(new JLabel("CAPACIDAD DISPONIBLE:"), gbc); gbc.gridx = 1;
        columna2.add(txtCapacidadDisponible, gbc);

        gbc.gridx = 0; gbc.gridy++;
        columna2.add(new JLabel("HORA:"), gbc); gbc.gridx = 1;
        columna2.add(spinnerHora, gbc);

        panelCampos.add(columna1);
        panelCampos.add(columna2);
        panelCampos.add(columna3);

        modeloTabla = new DefaultTableModel(new String[]{"ID", "Clase", "Fecha y Hora", "Capacidad Disponible"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaSesiones = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaSesiones);

        btnAgregar = new JButton("Añadir Sesión");
        btnModificar = new JButton("Modificar Sesión");
        btnEliminar = new JButton("Eliminar Sesión");
        btnLimpiarCampos = new JButton("Limpiar Campos");

        btnAgregar.addActionListener(e -> agregarSesionClase());
        btnModificar.addActionListener(e -> modificarSesionClase());
        btnEliminar.addActionListener(e -> eliminarSesionClase());
        btnLimpiarCampos.addActionListener(e -> limpiarCampos());

        txtBuscarSesionClase = new JTextField(20);
        txtBuscarSesionClase.setForeground(Color.GRAY);
        txtBuscarSesionClase.setText("Introduce el nombre de una clase");

        txtBuscarSesionClase.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (txtBuscarSesionClase.getText().equals("Introduce el nombre de una clase")) {
                    txtBuscarSesionClase.setText("");
                    txtBuscarSesionClase.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent e) {
                if (txtBuscarSesionClase.getText().isEmpty()) {
                    txtBuscarSesionClase.setText("Introduce el nombre de una clase");
                    txtBuscarSesionClase.setForeground(Color.GRAY);
                }
            }
        });

        txtBuscarSesionClase.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                filtrarSesionesPorNombre(txtBuscarSesionClase.getText());
            }
        });

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBotones.add(new JLabel("BUSCAR SESIÓN CLASE"));
        panelBotones.add(txtBuscarSesionClase);
        panelBotones.add(btnAgregar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiarCampos);

        add(panelCampos, BorderLayout.NORTH);
        add(scrollTabla, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        Socio usuarioActual = Sesion.getUsuarioActual();
        if (usuarioActual != null && usuarioActual.getTipoUsuario() == TipoUsuario.BASIC) {
            // Ocultar campos y botones
            panelCampos.setVisible(false);
            panelBotones.setVisible(false);

            // Crear tabla sin ID
            DefaultTableModel modeloBasico = new DefaultTableModel(new String[]{"Clase", "Fecha y Hora", "Capacidad Disponible"}, 0) {
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            for (SesionClase s : SesionClaseDAO.obtenerTodasLasSesiones()) {
                modeloBasico.addRow(new Object[]{
                        s.getClase().getNombreClase(),
                        s.getFechaHora().toString(),
                        s.getCapacidadDisponible()
                });
            }

            tablaSesiones.setModel(modeloBasico);
        } else {
            cargarSesiones();

            tablaSesiones.getSelectionModel().addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) {
                    int fila = tablaSesiones.getSelectedRow();
                    if (fila >= 0) {
                        int id = (int) modeloTabla.getValueAt(fila, 0);
                        cargarDatosSesion(id);
                    }
                }
            });
        }
    }

    private void agregarSesionClase() {
        Clase claseSeleccionada = (Clase) comboClase.getSelectedItem();
        if (claseSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona una clase.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Date fechaSeleccionada = dateChooser.getDate();
        if (fechaSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona una fecha.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Date horaSeleccionada = (Date) spinnerHora.getValue();
        if (horaSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona una hora.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fechaSeleccionada);
        Calendar horaCalendar = Calendar.getInstance();
        horaCalendar.setTime(horaSeleccionada);
        calendar.set(Calendar.HOUR_OF_DAY, horaCalendar.get(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, horaCalendar.get(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Timestamp timestamp = new Timestamp(calendar.getTimeInMillis());

        SesionClaseDAO dao = new SesionClaseDAO();
        if (dao.existeSesion(timestamp)) {
            JOptionPane.showMessageDialog(this, "Ya existe una sesión para esta clase a esa hora.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        SesionClase nuevaSesion = new SesionClase();
        nuevaSesion.setFechaHora(timestamp);
        nuevaSesion.setClase(claseSeleccionada);
        nuevaSesion.setCapacidadDisponible(claseSeleccionada.getCapacidadMaxima());

        dao.agregarSesion(nuevaSesion);
        cargarSesiones();
        actualizarCapacidadDisponible();
        JOptionPane.showMessageDialog(this, "Sesión añadida correctamente.");
        limpiarCampos();
    }

    private void modificarSesionClase() {
        int fila = tablaSesiones.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona una sesión.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) modeloTabla.getValueAt(fila, 0);
        SesionClaseDAO dao = new SesionClaseDAO();
        SesionClase sesion = dao.obtenerSesionPorId(id);

        if (sesion != null) {
            Date fechaSeleccionada = dateChooser.getDate();
            Date horaSeleccionada = (Date) spinnerHora.getValue();

            if (fechaSeleccionada == null || horaSeleccionada == null) {
                JOptionPane.showMessageDialog(this, "Fecha y hora no válidas.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(fechaSeleccionada);
            Calendar horaCalendar = Calendar.getInstance();
            horaCalendar.setTime(horaSeleccionada);
            calendar.set(Calendar.HOUR_OF_DAY, horaCalendar.get(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, horaCalendar.get(Calendar.MINUTE));
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            Timestamp timestamp = new Timestamp(calendar.getTimeInMillis());

            sesion.setFechaHora(timestamp);
            sesion.setCapacidadDisponible(Integer.parseInt(txtCapacidadDisponible.getText()));
            sesion.setClase((Clase) comboClase.getSelectedItem());

            dao.actualizarSesion(sesion);
            cargarSesiones();
            JOptionPane.showMessageDialog(this, "Sesión modificada correctamente.");
            limpiarCampos();
        }
    }

    private void eliminarSesionClase() {
        int fila = tablaSesiones.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona una sesión.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) modeloTabla.getValueAt(fila, 0);
        SesionClaseDAO dao = new SesionClaseDAO();
        SesionClase sesion = dao.obtenerSesionPorId(id);

        if (sesion != null) {
            dao.eliminarSesion(sesion);
            actualizarCapacidadDisponible();
            cargarSesiones();
            JOptionPane.showMessageDialog(this, "Sesión eliminada.");
            limpiarCampos();
        }
    }

    public void cargarSesiones() {
        modeloTabla.setRowCount(0);
        SesionClaseDAO dao = new SesionClaseDAO();
        List<SesionClase> sesiones = dao.obtenerTodasLasSesiones();
        for (SesionClase s : sesiones) {
            modeloTabla.addRow(new Object[]{
                    s.getIdSesion(),
                    s.getClase().getNombreClase(),
                    s.getFechaHora(),
                    s.getCapacidadDisponible()
            });
        }
    }

    public void cargarClases() {
        comboClase.removeAllItems();
        ClaseDAO dao = new ClaseDAO();
        List<Clase> clases = dao.obtenerTodasLasClases();
        for (Clase clase : clases) {
            comboClase.addItem(clase);
        }
    }

    private void cargarDatosSesion(int id) {
        SesionClaseDAO dao = new SesionClaseDAO();
        SesionClase sesion = dao.obtenerSesionPorId(id);
        if (sesion != null) {
            dateChooser.setDate(sesion.getFechaHora());
            spinnerHora.setValue(sesion.getFechaHora());
            comboClase.setSelectedItem(sesion.getClase());
            txtCapacidadDisponible.setText(String.valueOf(sesion.getCapacidadDisponible()));
        }
    }

    private void actualizarCapacidadDisponible() {
        Clase claseSeleccionada = (Clase) comboClase.getSelectedItem();
        if (claseSeleccionada != null) {
            int capacidadMaxima = claseSeleccionada.getCapacidadMaxima();
            txtCapacidadDisponible.setText(String.valueOf(capacidadMaxima));
            txtCapacidadDisponible.setEditable(false);
        }
    }

    private void limpiarCampos() {
        comboClase.setSelectedIndex(-1);
        dateChooser.setDate(null);
        spinnerHora.setValue(new Date());
        txtCapacidadDisponible.setText("");
    }

    private void filtrarSesionesPorNombre(String filtro) {
        modeloTabla.setRowCount(0);
        SesionClaseDAO dao = new SesionClaseDAO();
        List<SesionClase> sesiones = dao.obtenerTodasLasSesiones();
        for (SesionClase sesion : sesiones) {
            if (sesion.getClase().getNombreClase().toLowerCase().contains(filtro.toLowerCase())) {
                modeloTabla.addRow(new Object[]{
                        sesion.getIdSesion(),
                        sesion.getClase().getNombreClase(),
                        sesion.getFechaHora(),
                        sesion.getCapacidadDisponible()
                });
            }
        }
    }
}
