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

/**
 * PanelSesionClase es un JPanel que proporciona una interfaz gráfica para gestionar
 * las sesiones de las clases disponibles en el sistema.
 * <p>
 * Permite al usuario agregar, modificar, eliminar y buscar sesiones de clase,
 * visualizando la información en una tabla y gestionando los detalles como clase,
 * fecha, hora y capacidad disponible.
 * <p>
 * La visibilidad y permisos de ciertos componentes están condicionados al tipo de usuario actual.
 *
 * @author Roberto Arcusa
 * @version 1.0
 * @since 2025
 */

public class PanelSesionClase extends JPanel {
    private JTable tablaSesiones;
    private DefaultTableModel modeloTabla;
    private JSpinner spinnerHora;
    private JTextField txtCapacidadDisponible;
    private JComboBox<Clase> comboClase;
    private JButton btnModificar, btnAgregar, btnEliminar, btnLimpiarCampos;
    private JDateChooser dateChooser;
    private JTextField txtBuscarSesionClase;

    /**
     * Constructor que inicializa el panel con sus componentes gráficos,
     * configura el layout, crea la tabla y el formulario para gestionar sesiones,
     * además de cargar los datos y manejar la visibilidad según el tipo de usuario.
     */
    public PanelSesionClase() {
        setLayout(new BorderLayout(10, 10));

        // PANEL CAMPOS con GridBagLayout
        JPanel panelCampos = new JPanel(new GridBagLayout());
        panelCampos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.NORTHWEST;

        // --- COLUMNA 1 ---
        JPanel columna1 = new JPanel(new GridBagLayout());
        GridBagConstraints c1 = new GridBagConstraints();
        c1.insets = new Insets(5, 5, 5, 5);
        c1.fill = GridBagConstraints.HORIZONTAL;
        c1.weightx = 1.0;

        comboClase = new JComboBox<>();
        comboClase.setPreferredSize(new Dimension(350, 40));
        comboClase.addActionListener(e -> actualizarCapacidadDisponible());

        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("yyyy-MM-dd");
        dateChooser.setPreferredSize(new Dimension(350, 40));

        c1.gridx = 0; c1.gridy = 0;
        columna1.add(new JLabel("CLASE:"), c1);
        c1.gridx = 1;
        columna1.add(comboClase, c1);

        c1.gridx = 0; c1.gridy++;
        columna1.add(new JLabel("FECHA:"), c1);
        c1.gridx = 1;
        columna1.add(dateChooser, c1);

        // --- COLUMNA 2 ---
        JPanel columna2 = new JPanel(new GridBagLayout());
        GridBagConstraints c2 = new GridBagConstraints();
        c2.insets = new Insets(5, 5, 5, 5);
        c2.fill = GridBagConstraints.HORIZONTAL;
        c2.weightx = 1.0;

        txtCapacidadDisponible = new JTextField();
        txtCapacidadDisponible.setPreferredSize(new Dimension(350, 40));
        txtCapacidadDisponible.setEditable(false);

        spinnerHora = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editorHora = new JSpinner.DateEditor(spinnerHora, "HH:mm");
        spinnerHora.setEditor(editorHora);
        spinnerHora.setPreferredSize(new Dimension(350, 40));

        c2.gridx = 0; c2.gridy = 0;
        columna2.add(new JLabel("CAPACIDAD DISPONIBLE:"), c2);
        c2.gridx = 1;
        columna2.add(txtCapacidadDisponible, c2);

        c2.gridx = 0; c2.gridy++;
        columna2.add(new JLabel("HORA:"), c2);
        c2.gridx = 1;
        columna2.add(spinnerHora, c2);

        // --- COLUMNA 3: Vacía, igual que PanelClase ---
        JPanel columna3 = new JPanel();

        // Añadir columnas a panelCampos con pesos para layout proporcional
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.4;
        panelCampos.add(columna1, gbc);

        gbc.gridx = 1; gbc.weightx = 0.4;
        panelCampos.add(columna2, gbc);

        gbc.gridx = 2; gbc.weightx = 0.2;
        panelCampos.add(columna3, gbc);

        // MODELO Y TABLA
        modeloTabla = new DefaultTableModel(
                new String[]{"ID", "Clase", "Fecha y Hora", "Capacidad Disponible"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaSesiones = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaSesiones);

        // BOTONES Y BUSQUEDA
        btnAgregar = new JButton("AÑADIR SESIÓN");
        btnModificar = new JButton("MODIFICAR SESIÓN");
        btnEliminar = new JButton("ELIMINAR SESIÓN");
        btnLimpiarCampos = new JButton("LIMPIAR CAMPOS");

        btnAgregar.addActionListener(e -> agregarSesionClase());
        btnModificar.addActionListener(e -> modificarSesionClase());
        btnEliminar.addActionListener(e -> eliminarSesionClase());
        btnLimpiarCampos.addActionListener(e -> limpiarCampos());

        // Ajustar tamaño botones para ser consistentes con PanelClase
        Dimension tamañoOriginal = btnAgregar.getPreferredSize();
        Dimension tamañoNuevo = new Dimension(tamañoOriginal.width + 35, tamañoOriginal.height + 10);

        btnAgregar.setPreferredSize(tamañoNuevo);
        btnModificar.setPreferredSize(tamañoNuevo);
        btnEliminar.setPreferredSize(tamañoNuevo);
        btnLimpiarCampos.setPreferredSize(tamañoNuevo);

        txtBuscarSesionClase = new JTextField(20);
        setTextFieldHint(txtBuscarSesionClase, "Introduce un nombre de una sesión");

        txtBuscarSesionClase.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filtrarSesionesPorNombre(txtBuscarSesionClase.getText());
            }
        });

        // Panel búsqueda + botones (misma estructura que PanelClase)
        JPanel panelBusquedaBotones = new JPanel(new BorderLayout());

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.add(btnAgregar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiarCampos);

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelBusqueda.add(new JLabel("BUSCAR SESIÓN CLASE"));
        panelBusqueda.add(txtBuscarSesionClase);

        panelBusquedaBotones.add(panelBotones, BorderLayout.CENTER);
        panelBusquedaBotones.add(panelBusqueda, BorderLayout.SOUTH);

        // PANEL ARRIBA QUE CONTIENE CAMPOS Y BOTONES+BUSQUEDA
        JPanel panelArriba = new JPanel(new BorderLayout());
        panelArriba.add(panelCampos, BorderLayout.CENTER);
        panelArriba.add(panelBusquedaBotones, BorderLayout.SOUTH);

        // Añadir al layout principal
        add(panelArriba, BorderLayout.NORTH);
        add(scrollTabla, BorderLayout.CENTER);

        // Cargar datos y manejar restricciones por tipo usuario
        Socio usuarioActual = Sesion.getUsuarioActual();
        if (usuarioActual != null && usuarioActual.getTipoUsuario() == TipoUsuario.BASIC) {
            panelCampos.setVisible(false);
            panelBusquedaBotones.setVisible(false);

            DefaultTableModel modeloBasico = new DefaultTableModel(
                    new String[]{"CLASE", "FECHA Y HORA", "CAPACIDAD DISPONIBLE"}, 0) {
                @Override
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

    /**
     * Agrega una nueva sesión de clase con los datos introducidos en el formulario.
     * Valida que se haya seleccionado una clase, una fecha y una hora,
     * y que no exista ya una sesión en la misma fecha y hora.
     * Luego actualiza la tabla y muestra mensajes informativos.
     */
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

    /**
     * Modifica la sesión de clase seleccionada en la tabla con los datos actuales del formulario.
     * Valida la selección y los datos de fecha y hora antes de actualizar.
     * Refresca la tabla y muestra mensajes según el resultado.
     */
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

    /**
     * Elimina la sesión de clase seleccionada tras confirmación del usuario.
     * Actualiza la tabla, limpia el formulario y muestra mensajes de éxito o error.
     */
    private void eliminarSesionClase() {
        int fila = tablaSesiones.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona una sesión.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Confirmar eliminación
        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Estás seguro de que quieres eliminar esta sesión?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (opcion == JOptionPane.YES_OPTION) {
            int id = (int) modeloTabla.getValueAt(fila, 0);
            SesionClaseDAO dao = new SesionClaseDAO();
            SesionClase sesion = dao.obtenerSesionPorId(id);

            if (sesion != null) {
                boolean eliminada = dao.eliminarSesion(sesion);
                if (eliminada) {
                    actualizarCapacidadDisponible();
                    cargarSesiones();
                    limpiarCampos();
                    JOptionPane.showMessageDialog(this, "Sesión eliminada correctamente.");
                } else {
                    JOptionPane.showMessageDialog(this,
                            "No se pudo eliminar la sesión. Por favor, inténtalo de nuevo o contacta al administrador.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se encontró la sesión seleccionada.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Carga todas las sesiones de clase desde la base de datos y actualiza la tabla.
     * Los datos mostrados incluyen ID, nombre de clase, fecha y hora, y capacidad disponible.
     */
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

    /**
     * Carga todas las clases disponibles en el comboBox para su selección en el formulario.
     */
    public void cargarClases() {
        comboClase.removeAllItems();
        ClaseDAO dao = new ClaseDAO();
        List<Clase> clases = dao.obtenerTodasLasClases();
        for (Clase clase : clases) {
            comboClase.addItem(clase);
        }
    }

    /**
     * Carga los datos de una sesión de clase específica en el formulario,
     * dado su ID, para facilitar la modificación.
     *
     * @param id Identificador único de la sesión a cargar.
     */
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

    /**
     * Actualiza el campo de capacidad disponible según la clase seleccionada
     * en el comboBox, mostrando la capacidad máxima de dicha clase.
     */
    private void actualizarCapacidadDisponible() {
        Clase claseSeleccionada = (Clase) comboClase.getSelectedItem();
        if (claseSeleccionada != null) {
            int capacidadMaxima = claseSeleccionada.getCapacidadMaxima();
            txtCapacidadDisponible.setText(String.valueOf(capacidadMaxima));
            txtCapacidadDisponible.setEditable(false);
        }
    }

    /**
     * Filtra las sesiones mostradas en la tabla según el nombre de la clase
     * que contenga el texto de búsqueda introducido.
     *
     * @param filtro Texto usado para filtrar las sesiones por nombre de clase.
     */
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

    /**
     * Limpia los campos del formulario restableciendo sus valores
     * a estado inicial para facilitar la entrada de nuevos datos.
     */
    public void limpiarCampos() {
        comboClase.setSelectedIndex(-1);
        dateChooser.setDate(null);
        spinnerHora.setValue(new Date());
        txtCapacidadDisponible.setText("");
    }

    /**
     * Configura un JTextField para mostrar un texto indicativo (hint) cuando está vacío y sin foco,
     * y limpiar dicho texto cuando recibe foco, mejorando la experiencia del usuario.
     *
     * @param txtBuscarSesionClase Campo de texto a configurar.
     * @param s Texto indicativo a mostrar como hint.
     */
    private void setTextFieldHint(JTextField txtBuscarSesionClase, String s) {
        txtBuscarSesionClase.setText(s);
        txtBuscarSesionClase.setForeground(Color.GRAY);

        txtBuscarSesionClase.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtBuscarSesionClase.getText().equals(s)) {
                    txtBuscarSesionClase.setText("");
                    txtBuscarSesionClase.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtBuscarSesionClase.getText().isEmpty()) {
                    txtBuscarSesionClase.setText(s);
                    txtBuscarSesionClase.setForeground(Color.GRAY);
                }
            }
        });
    }

}
