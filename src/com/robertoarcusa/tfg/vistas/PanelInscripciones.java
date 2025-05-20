package com.robertoarcusa.tfg.vistas;

import com.robertoarcusa.tfg.clases.Inscripcion;
import com.robertoarcusa.tfg.clases.Socio;
import com.robertoarcusa.tfg.clases.SesionClase;
import com.robertoarcusa.tfg.dao.InscripcionDAO;
import com.robertoarcusa.tfg.dao.SocioDAO;
import com.robertoarcusa.tfg.dao.SesionClaseDAO;
import com.robertoarcusa.tfg.util.Sesion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

public class PanelInscripciones extends JPanel {
    private JTable tablaInscripciones;
    private DefaultTableModel modeloTabla;
    private JComboBox<Socio> comboSocios;
    private JComboBox<SesionClase> comboSesiones;
    private JButton btnAgregar, btnModificar, btnEliminar, btnLimpiarCampos;
    private JLabel lblCapacidadDisponible;
    private JTextField txtBuscarInscripcion;
    private TableRowSorter<DefaultTableModel> sorter;

    public PanelInscripciones() {
        setLayout(new BorderLayout());

        // Crear panelCampos
        JPanel panelCampos = new JPanel(new GridLayout(1, 3, 10, 10));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Crear las columnas
        JPanel columna1 = new JPanel(new GridBagLayout());
        JPanel columna2 = new JPanel(new GridBagLayout());
        JPanel columna3 = new JPanel(new GridBagLayout());

        comboSocios = new JComboBox<>();
        comboSocios.setPreferredSize(new Dimension(250, 45));

        comboSesiones = new JComboBox<>();
        comboSesiones.setPreferredSize(new Dimension(250, 45));

        lblCapacidadDisponible = new JLabel("CAPACIDAD DISPONIBLE: ");

        cargarSocios();
        cargarSesiones();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 0, 15);

        // Columna 1 (Socio)
        columna1.add(new JLabel("SOCIO:"), gbc);
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, 0, 15);
        columna1.add(comboSocios, gbc);

        // Columna 2 (Sesión Clase)
        gbc.gridx = 0;
        gbc.gridy = 0;
        columna2.add(new JLabel("SESIÓN CLASE:"), gbc);
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, 0, 15);
        columna2.add(comboSesiones, gbc);

        // Columna 3 (Fecha Inscripción)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, 0, 15);

        // Añadir el JLabel de capacidad disponible en la última columna
        gbc.gridx = 0;
        gbc.gridy = 1;
        columna3.add(lblCapacidadDisponible, gbc);

        // Añadir las columnas al panelCampos
        panelCampos.add(columna1);
        panelCampos.add(columna2);
        panelCampos.add(columna3);

        modeloTabla = new DefaultTableModel(new String[]{"ID", "Socio", "Sesión", "Fecha Inscripción"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaInscripciones = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaInscripciones);

        btnAgregar = new JButton("Añadir Inscripción");
        btnModificar = new JButton(("Modificar Inscripción"));
        btnEliminar = new JButton("Eliminar Inscripción");
        btnLimpiarCampos = new JButton("Limpiar Campos");
        btnAgregar.addActionListener(e -> agregarInscripcion());
        btnModificar.addActionListener(e -> modificarInscripcion());
        btnEliminar.addActionListener(e -> eliminarInscripcion());
        btnLimpiarCampos.addActionListener(e -> limpiarCampos());

        txtBuscarInscripcion = new JTextField(20);
        txtBuscarInscripcion.setForeground(Color.GRAY);
        txtBuscarInscripcion.setText("Introduce un nombre de un usuario");

        txtBuscarInscripcion.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (txtBuscarInscripcion.getText().equals("Introduce un nombre de un usuario")) {
                    txtBuscarInscripcion.setText("");
                    txtBuscarInscripcion.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent e) {
                if (txtBuscarInscripcion.getText().isEmpty()) {
                    txtBuscarInscripcion.setText("Introduce un nombre de un usuario");
                    txtBuscarInscripcion.setForeground(Color.GRAY);
                }
            }
        });

        txtBuscarInscripcion.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                actualizarFiltro(txtBuscarInscripcion.getText());
            }
        });

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBotones.add(new JLabel("BUSCAR INSCRIPCIÓN"));
        panelBotones.add(txtBuscarInscripcion);
        panelBotones.add(btnAgregar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiarCampos);

        add(panelCampos, BorderLayout.NORTH);
        add(scrollTabla, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        cargarInscripciones();

        // (Empieza exactamente igual que tu clase original)
        tablaInscripciones.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int filaVista = tablaInscripciones.getSelectedRow();
                if (filaVista >= 0) {
                    int filaModelo = tablaInscripciones.convertRowIndexToModel(filaVista);
                    cargarDatosInscripcion(filaModelo);
                }
            }
        });

        comboSesiones.addActionListener(e -> actualizarCapacidadDisponible());

        sorter = new TableRowSorter<>(modeloTabla);
        tablaInscripciones.setRowSorter(sorter);

        if (Sesion.esBasic()) {
            btnLimpiarCampos.setVisible(false);  // Ocultar el botón si el usuario es BASIC
        }
    }

    private void agregarInscripcion() {
        Socio socioSeleccionado = (Socio) comboSocios.getSelectedItem();
        SesionClase sesionSeleccionada = (SesionClase) comboSesiones.getSelectedItem();

        if (socioSeleccionado == null || sesionSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "Rellena todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Asignar la fecha actual como la fecha de inscripción
        java.sql.Date fechaInscripcion = new java.sql.Date(System.currentTimeMillis());

        // Verificar que hay capacidad disponible en la sesión
        if (sesionSeleccionada.getCapacidadDisponible() > 0) {
            // Crear nueva inscripción
            Inscripcion nuevaInscripcion = new Inscripcion();
            nuevaInscripcion.setSocio(socioSeleccionado);
            nuevaInscripcion.setSesionclase(sesionSeleccionada);
            nuevaInscripcion.setFechaInscripcion(fechaInscripcion);

            // Registrar la inscripción en la base de datos
            InscripcionDAO dao = new InscripcionDAO();
            dao.agregarInscripcion(nuevaInscripcion);

            // Decrementar la capacidad disponible de la sesión
            sesionSeleccionada.setCapacidadDisponible(sesionSeleccionada.getCapacidadDisponible() - 1);
            SesionClaseDAO daoSesion = new SesionClaseDAO();
            daoSesion.actualizarSesion(sesionSeleccionada);

            cargarSesiones();
            cargarInscripciones();

            JOptionPane.showMessageDialog(this, "Inscripción añadida correctamente.");
            //limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(this, "No hay capacidad disponible para esta sesión.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificarInscripcion() {
        int filaVista = tablaInscripciones.getSelectedRow();
        if (filaVista < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona una inscripción.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int filaModelo = tablaInscripciones.convertRowIndexToModel(filaVista);
        int id = (int) modeloTabla.getValueAt(filaModelo, 0);

        // Obtener los nuevos valores de los combos
        Socio nuevoSocio = (Socio) comboSocios.getSelectedItem();
        SesionClase nuevaSesion = (SesionClase) comboSesiones.getSelectedItem();

        if (nuevoSocio == null || nuevaSesion == null) {
            JOptionPane.showMessageDialog(this, "Debes seleccionar un socio y una sesión.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Obtener la inscripción original desde la base de datos
        InscripcionDAO dao = new InscripcionDAO();
        Inscripcion inscripcion = dao.obtenerInscripcionPorId(id);

        if (inscripcion != null) {
            SesionClase sesionAntigua = inscripcion.getSesionclase();

            // Si cambia la sesión, se ajustan las capacidades
            if (!sesionAntigua.equals(nuevaSesion)) {
                // Verificar si hay capacidad en la nueva sesión
                if (nuevaSesion.getCapacidadDisponible() <= 0) {
                    JOptionPane.showMessageDialog(this, "No hay capacidad disponible en la nueva sesión.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Ajustar capacidades
                sesionAntigua.setCapacidadDisponible(sesionAntigua.getCapacidadDisponible() + 1);
                nuevaSesion.setCapacidadDisponible(nuevaSesion.getCapacidadDisponible() - 1);

                // Actualizar sesiones en la base de datos
                SesionClaseDAO sesionDAO = new SesionClaseDAO();
                sesionDAO.actualizarSesion(sesionAntigua);
                sesionDAO.actualizarSesion(nuevaSesion);
            }

            // Actualizar campos modificables
            inscripcion.setSocio(nuevoSocio);
            inscripcion.setSesionclase(nuevaSesion);

            // Guardar cambios
            dao.modificarInscripcion(inscripcion);

            cargarInscripciones();
            cargarSesiones();

            JOptionPane.showMessageDialog(this, "Inscripción modificada correctamente.");
        }
    }

    private void eliminarInscripcion() {
        int filaVista = tablaInscripciones.getSelectedRow();
        if (filaVista < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona una inscripción.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int filaModelo = tablaInscripciones.convertRowIndexToModel(filaVista);
        int id = (int) modeloTabla.getValueAt(filaModelo, 0);
        InscripcionDAO dao = new InscripcionDAO();
        Inscripcion inscripcion = dao.obtenerInscripcionPorId(id);

        if (inscripcion != null) {
            dao.eliminarInscripcion(inscripcion);

            SesionClase sesion = inscripcion.getSesionclase();
            sesion.setCapacidadDisponible(sesion.getCapacidadDisponible() + 1);

            SesionClaseDAO sesionDao = new SesionClaseDAO();
            sesionDao.actualizarSesion(sesion);

            cargarSesiones();
            cargarInscripciones();
            JOptionPane.showMessageDialog(this, "Inscripción eliminada.");
            limpiarCampos();
        }
    }

    private void actualizarFiltro(String filtro) {
        modeloTabla.setRowCount(0);
        List<Inscripcion> inscripciones = InscripcionDAO.obtenerTodasLasInscripciones();

        for (Inscripcion inscripcion : inscripciones) {
            String nombreSocio = inscripcion.getSocio().getNombreSocio();
            if (nombreSocio != null && nombreSocio.toLowerCase().contains(filtro.toLowerCase())) {
                modeloTabla.addRow(new Object[]{
                        inscripcion.getIdInscripcion(),
                        nombreSocio,
                        inscripcion.getSesionclase().getClase().getNombreClase(),
                        inscripcion.getSesionclase().getFechaHora()
                });
            }
        }
    }

    private void cargarDatosInscripcion(int filaModelo) {
        int id = (int) modeloTabla.getValueAt(filaModelo, 0);
        InscripcionDAO dao = new InscripcionDAO();
        Inscripcion inscripcion = dao.obtenerInscripcionPorId(id);

        if (inscripcion != null) {
            comboSocios.setSelectedItem(inscripcion.getSocio());
            comboSesiones.setSelectedItem(inscripcion.getSesionclase());
        }
    }

    private void cargarInscripciones() {
        modeloTabla.setRowCount(0);
        InscripcionDAO dao = new InscripcionDAO();
        List<Inscripcion> inscripciones;

        if (com.robertoarcusa.tfg.util.Sesion.esBasic()) {
            Socio usuario = com.robertoarcusa.tfg.util.Sesion.getUsuarioActual();
            inscripciones = dao.obtenerInscripcionesPorSocio(usuario.getIdSocio());
        } else {
            inscripciones = dao.obtenerTodasLasInscripciones();
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        for (Inscripcion inscripcion : inscripciones) {
            Socio socio = inscripcion.getSocio();
            SesionClase sesion = inscripcion.getSesionclase();
            String fechaHoraFormateada = sdf.format(sesion.getFechaHora());
            String sesionTexto = sesion.getClase().getNombreClase() + " - " + fechaHoraFormateada;

            modeloTabla.addRow(new Object[]{
                    inscripcion.getIdInscripcion(),
                    socio.getNombreSocio(),
                    sesionTexto,
                    inscripcion.getFechaInscripcion()
            });
        }
    }

    public void cargarSocios() {
        comboSocios.removeAllItems();

        // Si el usuario es BASIC, solo se muestra él mismo
        if (Sesion.esBasic()) {
            Socio usuario = com.robertoarcusa.tfg.util.Sesion.getUsuarioActual();
            comboSocios.addItem(usuario);
            comboSocios.setEnabled(false); // Desactiva el JComboBox para que no se pueda cambiar
        } else {
            SocioDAO dao = new SocioDAO();
            List<Socio> socios = dao.obtenerTodosLosSocios();
            if (socios != null) {
                for (Socio socio : socios) {
                    comboSocios.addItem(socio);
                }
            }
            comboSocios.setEnabled(true); // Asegura que esté habilitado para admins o editores
        }
    }

    public void cargarSesiones() {
        comboSesiones.removeAllItems();
        SesionClaseDAO dao = new SesionClaseDAO();
        List<SesionClase> sesiones = dao.obtenerTodasLasSesiones();
        if (sesiones != null) {
            for (SesionClase sesion : sesiones) {
                comboSesiones.addItem(sesion);
            }
        }
    }

    private void actualizarCapacidadDisponible() {
        SesionClase sesionSeleccionada = (SesionClase) comboSesiones.getSelectedItem();
        if (sesionSeleccionada != null) {
            lblCapacidadDisponible.setText("CAPACIDAD DISPONIBLE: " + sesionSeleccionada.getCapacidadDisponible());
        }
    }

    private void limpiarCampos() {
        if (comboSocios.getItemCount() > 0) {
            comboSocios.setSelectedIndex(-1);
        }

        if (comboSesiones.getItemCount() > 0) {
            comboSesiones.setSelectedIndex(-1);
        }
    }

}


