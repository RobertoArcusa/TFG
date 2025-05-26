package com.robertoarcusa.tfg.vistas;

import com.robertoarcusa.tfg.clases.Inscripcion;
import com.robertoarcusa.tfg.clases.Socio;
import com.robertoarcusa.tfg.clases.SesionClase;
import com.robertoarcusa.tfg.dao.InscripcionDAO;
import com.robertoarcusa.tfg.dao.SocioDAO;
import com.robertoarcusa.tfg.dao.SesionClaseDAO;
import com.robertoarcusa.tfg.enums.TipoUsuario;
import com.robertoarcusa.tfg.util.Sesion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * PanelInscripciones es un panel gráfico que gestiona la visualización y manipulación
 * de inscripciones a sesiones de clases para socios.
 * Permite agregar, modificar, eliminar inscripciones, así como buscar y filtrar inscripciones existentes.
 * Además, muestra la capacidad disponible de las sesiones seleccionadas.
 *
 * Extiende JPanel y utiliza varios componentes Swing para la interacción del usuario.
 *
 * @author Roberto Arcusa
 * @version 1.0
 * @since 2025
 */

public class PanelInscripciones extends JPanel {

    private JTable tablaInscripciones;
    private DefaultTableModel modeloTabla;
    private JComboBox<Socio> comboSocios;
    private JComboBox<SesionClase> comboSesiones;
    private JButton btnAgregar, btnModificar, btnEliminar, btnLimpiarCampos;
    private JLabel lblCapacidadDisponible;
    private JTextField txtBuscarInscripcion;
    private TableRowSorter<DefaultTableModel> sorter;

    /**
     * Constructor que inicializa los componentes gráficos y carga los datos iniciales
     * de socios, sesiones e inscripciones. Configura listeners para la interacción del usuario.
     */
    public PanelInscripciones() {
        setLayout(new BorderLayout(10, 10));

        // === PANEL CAMPOS ===
        JPanel panelCampos = new JPanel(new GridBagLayout());
        panelCampos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.NORTHWEST;

        comboSocios = new JComboBox<>();
        comboSocios.setPreferredSize(new Dimension(250, 40));

        comboSesiones = new JComboBox<>();
        comboSesiones.setPreferredSize(new Dimension(250, 40));

        lblCapacidadDisponible = new JLabel("CAPACIDAD DISPONIBLE: ");

        cargarSocios();
        cargarSesiones();

        // === COLUMNA 1 ===
        JPanel columna1 = new JPanel(new GridBagLayout());
        GridBagConstraints c1 = new GridBagConstraints();
        c1.insets = new Insets(5, 5, 5, 5);
        c1.fill = GridBagConstraints.HORIZONTAL;
        c1.weightx = 1.0;

        c1.gridx = 0; c1.gridy = 0;
        columna1.add(new JLabel("SOCIO:"), c1);
        c1.gridx = 1;
        columna1.add(comboSocios, c1);

        // === COLUMNA 2 ===
        JPanel columna2 = new JPanel(new GridBagLayout());
        GridBagConstraints c2 = new GridBagConstraints();
        c2.insets = new Insets(5, 5, 5, 5);
        c2.fill = GridBagConstraints.HORIZONTAL;
        c2.weightx = 1.0;

        c2.gridx = 0; c2.gridy = 0;
        columna2.add(new JLabel("SESIÓN CLASE:"), c2);
        c2.gridx = 1;
        columna2.add(comboSesiones, c2);

        // === COLUMNA 3 ===
        JPanel columna3 = new JPanel(new GridBagLayout());
        GridBagConstraints c3 = new GridBagConstraints();
        c3.insets = new Insets(5, 5, 5, 5);
        c3.fill = GridBagConstraints.HORIZONTAL;
        c3.weightx = 1.0;

        c3.gridx = 0; c3.gridy = 0;
        columna3.add(lblCapacidadDisponible, c3);

        // Añadir columnas al panelCampos
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.4;
        panelCampos.add(columna1, gbc);
        gbc.gridx = 1;
        panelCampos.add(columna2, gbc);
        gbc.gridx = 2; gbc.weightx = 0.2;
        panelCampos.add(columna3, gbc);

        // === TABLA ===
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Socio", "Sesión", "Fecha Inscripción"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaInscripciones = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaInscripciones);

        // === BOTONES ===
        btnAgregar = new JButton("AÑADIR INSCRIPCIÓN");
        btnModificar = new JButton("MODIFICAR INSCRIPCIÓN");
        btnEliminar = new JButton("ELIMINAR INSCRIPCIÓN");
        btnLimpiarCampos = new JButton("LIMPIAR CAMPOS");

        btnAgregar.addActionListener(e -> agregarInscripcion());
        btnModificar.addActionListener(e -> modificarInscripcion());
        btnEliminar.addActionListener(e -> eliminarInscripcion());
        btnLimpiarCampos.addActionListener(e -> limpiarCampos());

        Dimension btnSize = new Dimension(180, 40);
        btnAgregar.setPreferredSize(btnSize);
        btnModificar.setPreferredSize(btnSize);
        btnEliminar.setPreferredSize(btnSize);
        btnLimpiarCampos.setPreferredSize(btnSize);

        // === CAMPO DE BÚSQUEDA ===
        txtBuscarInscripcion = new JTextField(20);
        setTextFieldHint(txtBuscarInscripcion, "Introduce un nombre de un usuario");

        txtBuscarInscripcion.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                actualizarFiltro(txtBuscarInscripcion.getText());
            }
        });

        // === PANEL BOTONES Y BÚSQUEDA ===
        JPanel panelBusquedaBotones = new JPanel(new BorderLayout());

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.add(btnAgregar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiarCampos);

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelBusqueda.add(new JLabel("BUSCAR INSCRIPCIÓN"));
        panelBusqueda.add(txtBuscarInscripcion);

        panelBusquedaBotones.add(panelBotones, BorderLayout.CENTER);
        panelBusquedaBotones.add(panelBusqueda, BorderLayout.SOUTH);

        // === PANEL ARRIBA ===
        JPanel panelArriba = new JPanel(new BorderLayout());
        panelArriba.add(panelCampos, BorderLayout.CENTER);
        panelArriba.add(panelBusquedaBotones, BorderLayout.SOUTH);

        // Añadir al layout principal
        add(panelArriba, BorderLayout.NORTH);
        add(scrollTabla, BorderLayout.CENTER);

        // === CARGA DE DATOS Y RESTRICCIONES ===
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

        Socio usuarioActual = Sesion.getUsuarioActual();
        if (usuarioActual != null && usuarioActual.getTipoUsuario() == TipoUsuario.BASIC) {
            panelCampos.setVisible(false);
            panelBusquedaBotones.setVisible(false);

            DefaultTableModel modeloBasico = new DefaultTableModel(
                    new String[]{"SOCIO", "SESIÓN", "FECHA INSCRIPCION"}, 0) {
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            for (Inscripcion i : InscripcionDAO.obtenerTodasLasInscripciones()) {
                modeloBasico.addRow(new Object[]{
                        i.getSocio().getNombreSocio(),
                        i.getSesionclase().getClase().getNombreClase(),
                        i.getFechaInscripcion()
                });
            }

            tablaInscripciones.setModel(modeloBasico);
        }
    }

    /**
     * Añade una nueva inscripción con el socio y la sesión seleccionados.
     * Verifica que haya capacidad disponible en la sesión y actualiza la base de datos.
     * Muestra mensajes de error o confirmación según corresponda.
     */
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

    /**
     * Modifica la inscripción seleccionada con los nuevos datos de socio y sesión.
     * Ajusta la capacidad disponible de las sesiones en caso de cambio.
     * Actualiza la base de datos y recarga los datos en la tabla.
     */
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

    /**
     * Elimina la inscripción seleccionada después de una confirmación del usuario.
     * Actualiza la capacidad disponible de la sesión asociada y la base de datos.
     * Muestra mensajes de confirmación o error.
     */
    private void eliminarInscripcion() {
        int filaVista = tablaInscripciones.getSelectedRow();
        if (filaVista < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona una inscripción.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Confirmar eliminación
        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Estás seguro de que quieres eliminar esta inscripción?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (opcion == JOptionPane.YES_OPTION) {
            int filaModelo = tablaInscripciones.convertRowIndexToModel(filaVista);
            int id = (int) modeloTabla.getValueAt(filaModelo, 0);
            InscripcionDAO dao = new InscripcionDAO();
            Inscripcion inscripcion = dao.obtenerInscripcionPorId(id);

            if (inscripcion != null) {
                boolean eliminada = dao.eliminarInscripcion(inscripcion);
                if (eliminada) {
                    // Actualizar la capacidad disponible
                    SesionClase sesion = inscripcion.getSesionclase();
                    sesion.setCapacidadDisponible(sesion.getCapacidadDisponible() + 1);

                    SesionClaseDAO sesionDao = new SesionClaseDAO();
                    sesionDao.actualizarSesion(sesion);

                    cargarSesiones();
                    cargarInscripciones();
                    JOptionPane.showMessageDialog(this, "Inscripción eliminada correctamente.");
                    limpiarCampos();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "No se pudo eliminar la inscripción. Por favor, inténtalo de nuevo o contacta al administrador.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se encontró la inscripción seleccionada.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Actualiza el filtro de la tabla de inscripciones para mostrar solo
     * aquellas cuyo nombre del socio contenga el texto de búsqueda.
     *
     * @param filtro texto utilizado para filtrar las inscripciones por nombre de socio.
     */
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

    /**
     * Carga los datos de la inscripción seleccionada en los campos del formulario.
     *
     * @param filaModelo índice de la fila en el modelo de la tabla correspondiente a la inscripción.
     */
    private void cargarDatosInscripcion(int filaModelo) {
        int id = (int) modeloTabla.getValueAt(filaModelo, 0);
        InscripcionDAO dao = new InscripcionDAO();
        Inscripcion inscripcion = dao.obtenerInscripcionPorId(id);

        if (inscripcion != null) {
            comboSocios.setSelectedItem(inscripcion.getSocio());
            comboSesiones.setSelectedItem(inscripcion.getSesionclase());
        }
    }

    /**
     * Carga todas las inscripciones disponibles en la tabla.
     * Si el usuario actual es de tipo BASIC, sólo carga sus propias inscripciones.
     */
    public void cargarInscripciones() {
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

    /**
     * Carga la lista de socios en el combo correspondiente.
     * Si el usuario actual es de tipo BASIC, solo se muestra su propio socio y el combo queda deshabilitado.
     */
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

    /**
     * Carga todas las sesiones disponibles en el combo correspondiente.
     */
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

    /**
     * Actualiza la etiqueta que muestra la capacidad disponible de la sesión seleccionada.
     */
    private void actualizarCapacidadDisponible() {
        SesionClase sesionSeleccionada = (SesionClase) comboSesiones.getSelectedItem();
        if (sesionSeleccionada != null) {
            lblCapacidadDisponible.setText("CAPACIDAD DISPONIBLE: " + sesionSeleccionada.getCapacidadDisponible());
        }
    }

    /**
     * Limpia la selección de los combos de socios y sesiones.
     */
    public void limpiarCampos() {
        if (comboSocios.getItemCount() > 0) {
            comboSocios.setSelectedIndex(-1);
        }

        if (comboSesiones.getItemCount() > 0) {
            comboSesiones.setSelectedIndex(-1);
        }
    }

    /**
     * Establece un texto de sugerencia (hint) en un JTextField que desaparece
     * al obtener foco y reaparece si el campo queda vacío al perder foco.
     *
     * @param textField campo de texto donde se quiere mostrar el hint.
     * @param hint texto de sugerencia a mostrar.
     */
    private void setTextFieldHint(JTextField textField, String hint) {
        textField.setText(hint);
        textField.setForeground(Color.GRAY);

        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(hint)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText(hint);
                    textField.setForeground(Color.GRAY);
                }
            }
        });
    }

}


