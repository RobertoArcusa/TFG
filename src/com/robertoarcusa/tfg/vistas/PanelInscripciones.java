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

        // Panel de campos con GridBagLayout para hacerlo responsive
        JPanel panelCampos = new JPanel(new GridBagLayout());
        panelCampos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints pc = new GridBagConstraints();
        pc.insets = new Insets(5, 10, 5, 10);
        pc.fill = GridBagConstraints.HORIZONTAL;
        pc.anchor = GridBagConstraints.CENTER;

        comboSocios = new JComboBox<>();
        comboSocios.setPreferredSize(new Dimension(250, 45));

        comboSesiones = new JComboBox<>();
        comboSesiones.setPreferredSize(new Dimension(250, 45));

        lblCapacidadDisponible = new JLabel("CAPACIDAD DISPONIBLE: ");

        cargarSocios();
        cargarSesiones();

        // Panel columna 1: SOCIO
        JPanel columna1 = new JPanel(new GridBagLayout());
        GridBagConstraints c1 = new GridBagConstraints();
        c1.insets = new Insets(5, 5, 5, 5);
        c1.gridx = 0; c1.gridy = 0;
        columna1.add(new JLabel("SOCIO:"), c1);
        c1.gridx = 1;
        columna1.add(comboSocios, c1);

        // Panel columna 2: SESIÓN
        JPanel columna2 = new JPanel(new GridBagLayout());
        GridBagConstraints c2 = new GridBagConstraints();
        c2.insets = new Insets(5, 5, 5, 5);
        c2.gridx = 0; c2.gridy = 0;
        columna2.add(new JLabel("SESIÓN CLASE:"), c2);
        c2.gridx = 1;
        columna2.add(comboSesiones, c2);

        // Panel columna 3: CAPACIDAD
        JPanel columna3 = new JPanel(new GridBagLayout());
        GridBagConstraints c3 = new GridBagConstraints();
        c3.insets = new Insets(5, 5, 5, 5);
        c3.gridx = 0; c3.gridy = 0;
        columna3.add(lblCapacidadDisponible, c3);

        // Añadir columnas al panelCampos
        pc.gridx = 0; pc.gridy = 0;
        panelCampos.add(columna1, pc);
        pc.gridx = 1;
        panelCampos.add(columna2, pc);
        pc.gridx = 2;
        panelCampos.add(columna3, pc);

        modeloTabla = new DefaultTableModel(new String[]{"ID", "Socio", "Sesión", "Fecha Inscripción"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaInscripciones = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaInscripciones);

        // Botones
        btnAgregar = new JButton("Añadir Inscripción");
        btnModificar = new JButton("Modificar Inscripción");
        btnEliminar = new JButton("Eliminar Inscripción");
        btnLimpiarCampos = new JButton("Limpiar Campos");

        btnAgregar.addActionListener(e -> agregarInscripcion());
        btnModificar.addActionListener(e -> modificarInscripcion());
        btnEliminar.addActionListener(e -> eliminarInscripcion());
        btnLimpiarCampos.addActionListener(e -> limpiarCampos());

        // Campo de búsqueda
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

        // Panel inferior combinado
        JPanel panelInferior = new JPanel(new GridBagLayout());
        GridBagConstraints pi = new GridBagConstraints();
        pi.insets = new Insets(5, 5, 5, 5);
        pi.gridx = 0; pi.gridy = 0;
        panelInferior.add(new JLabel("BUSCAR INSCRIPCIÓN"), pi);
        pi.gridx++;
        panelInferior.add(txtBuscarInscripcion, pi);
        pi.gridx++;
        panelInferior.add(btnAgregar, pi);
        pi.gridx++;
        panelInferior.add(btnModificar, pi);
        pi.gridx++;
        panelInferior.add(btnEliminar, pi);
        pi.gridx++;
        panelInferior.add(btnLimpiarCampos, pi);

        // Añadir todo al layout principal
        add(panelCampos, BorderLayout.NORTH);
        add(scrollTabla, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        // Carga datos
        cargarInscripciones();

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
            btnLimpiarCampos.setVisible(false);
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
            boolean eliminada = dao.eliminarInscripcion(inscripcion);
            if (!eliminada) {
                JOptionPane.showMessageDialog(this,
                        "No se puede eliminar la inscripción porque tiene datos relacionados. Primero elimínelos.",
                        "Advertencia", JOptionPane.WARNING_MESSAGE);
            } else {
                // Solo actualizar la capacidad si la eliminación fue correcta
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
                    socio.getNombreSocio() + " " + socio.getApellidosSocio(),
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


