package com.robertoarcusa.tfg.vistas;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.*;
import java.nio.file.Files;


import com.robertoarcusa.tfg.clases.Pago;
import com.robertoarcusa.tfg.clases.Socio;
import com.robertoarcusa.tfg.dao.PagoDAO;
import com.robertoarcusa.tfg.dao.SocioDAO;
import com.robertoarcusa.tfg.enums.Estado;
import com.robertoarcusa.tfg.enums.MetodoPago;
import com.robertoarcusa.tfg.enums.TipoCuota;
import com.robertoarcusa.tfg.enums.TipoPago;
import com.toedter.calendar.JDateChooser;

/**
 * PanelPagos es una clase que extiende JPanel y proporciona una interfaz gráfica
 * para gestionar los pagos de los socios en la aplicación.
 * <p>
 * Este panel incluye:
 * <ul>
 *   <li>Formulario para agregar, modificar y eliminar pagos, incluyendo selección de socio, fecha, importe, tipo y estado del pago.</li>
 *   <li>Visualización de los pagos existentes en una tabla con opciones de búsqueda y filtrado por ID o nombre del socio.</li>
 *   <li>Gestión de recibos asociados a los pagos, permitiendo cargar y eliminar archivos adjuntos.</li>
 *   <li>Control completo sobre los datos mediante botones para añadir, modificar, eliminar pagos y limpiar los campos del formulario.</li>
 * </ul>
 * <p>
 * La clase interactúa con la capa de datos a través de objetos DAO para persistir los pagos,
 * y mantiene la UI sincronizada con los datos almacenados.
 *
 * @author Roberto Arcusa
 * @version 1.0
 * @since 2025
 */

public class PanelPagos extends JPanel {

    private JTable tablaPagos;
    private DefaultTableModel modeloTabla;
    private JComboBox<String> comboSocio;
    private JDateChooser dateChooserFechaPago;
    private JTextField txtImporte;
    private JComboBox<TipoCuota> comboTipoCuota;
    private JComboBox<TipoPago> comboTipoPago;
    private JComboBox<MetodoPago> comboMetodoPago;
    private JComboBox<Estado> comboEstado;
    private JButton btnAgregar, btnGuardar, btnEliminar, btnLimpiar, btnCargarRecibo, btnEliminarRecibo;
    private JLabel lblRecibo;
    private byte[] reciboSeleccionado;
    private JTextField txtBuscarPago;
    private TableRowSorter<DefaultTableModel> sorter;

    /**
     * Constructor que inicializa todos los componentes gráficos del panel,
     * configura el layout, crea la tabla de pagos, el formulario, botones, y la lógica
     * para la búsqueda y selección de pagos.
     */
    public PanelPagos() {
        setLayout(new BorderLayout(10, 10));

        // === PANEL CAMPOS ===
        JPanel panelCampos = new JPanel(new GridBagLayout());
        panelCampos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.NORTHWEST;

        comboSocio = new JComboBox<>();
        comboSocio.setPreferredSize(new Dimension(250, 40));
        cargarIdsSocios();

        dateChooserFechaPago = new JDateChooser();
        dateChooserFechaPago.setDateFormatString("dd-MM-yyyy");

        dateChooserFechaPago.setPreferredSize(new Dimension(250, 40));

        txtImporte = new JTextField();
        txtImporte.setPreferredSize(new Dimension(250, 40));

        comboTipoCuota = new JComboBox<>(TipoCuota.values());
        comboTipoCuota.setPreferredSize(new Dimension(250, 40));

        comboTipoPago = new JComboBox<>(TipoPago.values());
        comboTipoPago.setPreferredSize(new Dimension(250, 40));

        comboMetodoPago = new JComboBox<>(MetodoPago.values());
        comboMetodoPago.setPreferredSize(new Dimension(250, 40));

        comboEstado = new JComboBox<>(Estado.values());
        comboEstado.setPreferredSize(new Dimension(250, 40));

        lblRecibo = new JLabel("Sin recibo", SwingConstants.CENTER);
        lblRecibo.setPreferredSize(new Dimension(150, 150));
        lblRecibo.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        btnCargarRecibo = new JButton("CARGAR RECIBO");
        btnEliminarRecibo = new JButton("ELIMINAR RECIBO");
        btnCargarRecibo.addActionListener(e -> cargarRecibo());
        btnEliminarRecibo.addActionListener(e -> {
            reciboSeleccionado = null;
            lblRecibo.setText("Sin recibo");
        });

        // === COLUMNA 1 ===
        JPanel columna1 = new JPanel(new GridBagLayout());
        GridBagConstraints c1 = new GridBagConstraints();
        c1.insets = new Insets(5, 5, 5, 5);
        c1.fill = GridBagConstraints.HORIZONTAL;
        c1.weightx = 1.0;

        c1.gridx = 0; c1.gridy = 0;
        columna1.add(new JLabel("SOCIO:"), c1);
        c1.gridx = 1;
        columna1.add(comboSocio, c1);

        c1.gridy++;
        c1.gridx = 0;
        columna1.add(new JLabel("FECHA PAGO:"), c1);
        c1.gridx = 1;
        columna1.add(dateChooserFechaPago, c1);

        c1.gridy++;
        c1.gridx = 0;
        columna1.add(new JLabel("IMPORTE:"), c1);
        c1.gridx = 1;
        columna1.add(txtImporte, c1);

        c1.gridy++;
        c1.gridx = 0;
        columna1.add(new JLabel("TIPO CUOTA:"), c1);
        c1.gridx = 1;
        columna1.add(comboTipoCuota, c1);

        // === COLUMNA 2 ===
        JPanel columna2 = new JPanel(new GridBagLayout());
        GridBagConstraints c2 = new GridBagConstraints();
        c2.insets = new Insets(5, 5, 5, 5);
        c2.fill = GridBagConstraints.HORIZONTAL;
        c2.weightx = 1.0;

        c2.gridx = 0; c2.gridy = 0;
        columna2.add(new JLabel("TIPO PAGO:"), c2);
        c2.gridx = 1;
        columna2.add(comboTipoPago, c2);

        c2.gridy++;
        c2.gridx = 0;
        columna2.add(new JLabel("MÉTODO PAGO:"), c2);
        c2.gridx = 1;
        columna2.add(comboMetodoPago, c2);

        c2.gridy++;
        c2.gridx = 0;
        columna2.add(new JLabel("ESTADO:"), c2);
        c2.gridx = 1;
        columna2.add(comboEstado, c2);

        // === COLUMNA 3 ===
        JPanel columna3 = new JPanel(new GridBagLayout());
        GridBagConstraints c3 = new GridBagConstraints();
        c3.insets = new Insets(5, 5, 5, 5);
        c3.fill = GridBagConstraints.HORIZONTAL;
        c3.weightx = 1.0;

        c3.gridx = 0; c3.gridy = 0;
        columna3.add(new JLabel("FOTO RECIBO:"), c3);
        c3.gridy++;
        columna3.add(lblRecibo, c3);
        c3.gridy++;
        columna3.add(btnCargarRecibo, c3);
        c3.gridy++;
        columna3.add(btnEliminarRecibo, c3);

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.4;
        panelCampos.add(columna1, gbc);
        gbc.gridx = 1;
        panelCampos.add(columna2, gbc);
        gbc.gridx = 2; gbc.weightx = 0.2;
        panelCampos.add(columna3, gbc);

        // === TABLA ===
        modeloTabla = new DefaultTableModel(new String[]{
                "ID", "Socio", "Fecha", "Importe", "Tipo Cuota", "Tipo Pago", "Método", "Estado"
        }, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaPagos = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaPagos);

        // === BOTONES ===
        btnAgregar = new JButton("AÑADIR PAGO");
        btnGuardar = new JButton("MODIFICAR PAGO");
        btnEliminar = new JButton("ELIMINAR PAGO");
        btnLimpiar = new JButton("LIMPIAR CAMPOS");

        Dimension btnSize = new Dimension(180, 40);
        for (JButton b : new JButton[]{btnAgregar, btnGuardar, btnEliminar, btnLimpiar}) {
            b.setPreferredSize(btnSize);
        }

        btnAgregar.addActionListener(e -> agregarNuevoPago());
        btnGuardar.addActionListener(e -> modificarPago());
        btnEliminar.addActionListener(e -> eliminarPago());
        btnLimpiar.addActionListener(e -> limpiarCampos());

        // === CAMPO BÚSQUEDA ===
        txtBuscarPago = new JTextField(20);
        txtBuscarPago.setForeground(Color.GRAY);
        txtBuscarPago.setText("Introduce ID o nombre del socio");
        txtBuscarPago.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (txtBuscarPago.getText().equals("Introduce ID o nombre del socio")) {
                    txtBuscarPago.setText("");
                    txtBuscarPago.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent e) {
                if (txtBuscarPago.getText().isEmpty()) {
                    txtBuscarPago.setText("Introduce ID o nombre del socio");
                    txtBuscarPago.setForeground(Color.GRAY);
                }
            }
        });

        txtBuscarPago.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { actualizarFiltro(); }
            public void removeUpdate(DocumentEvent e) { actualizarFiltro(); }
            public void changedUpdate(DocumentEvent e) { actualizarFiltro(); }
        });

        // === PANEL BOTONES Y BÚSQUEDA ===
        JPanel panelBusquedaBotones = new JPanel(new BorderLayout());

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.add(btnAgregar);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelBusqueda.add(new JLabel("BUSCAR PAGO"));
        panelBusqueda.add(txtBuscarPago);

        panelBusquedaBotones.add(panelBotones, BorderLayout.CENTER);
        panelBusquedaBotones.add(panelBusqueda, BorderLayout.SOUTH);

        // === PANEL ARRIBA ===
        JPanel panelArriba = new JPanel(new BorderLayout());
        panelArriba.add(panelCampos, BorderLayout.CENTER);
        panelArriba.add(panelBusquedaBotones, BorderLayout.SOUTH);

        add(panelArriba, BorderLayout.NORTH);
        add(scrollTabla, BorderLayout.CENTER);

        // === DATOS ===
        cargarPagos();

        tablaPagos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int fila = tablaPagos.getSelectedRow();
                if (fila >= 0) {
                    int id = (int) modeloTabla.getValueAt(fila, 0);
                    cargarDatosPago(id);
                }
            }
        });

        sorter = new TableRowSorter<>(modeloTabla);
        tablaPagos.setRowSorter(sorter);
    }

    /**
     * Agrega un nuevo pago basado en los datos ingresados en el formulario.
     * Valida los datos antes de intentar agregar el pago y recarga la tabla con los pagos actualizados.
     * Muestra mensajes de confirmación o error según corresponda.
     */
    private void agregarNuevoPago() {
        Pago pago = new Pago();
        if (!llenarDatosDesdeFormulario(pago)) return;
        new PagoDAO().agregarPago(pago);
        cargarPagos();
        JOptionPane.showMessageDialog(this, "Pago añadido.");
    }

    /**
     * Modifica el pago seleccionado en la tabla con los datos actuales del formulario.
     * Valida que un pago esté seleccionado y que los datos del formulario sean válidos antes de actualizar.
     * Refresca la tabla con los datos actualizados y muestra mensajes informativos.
     */
    private void modificarPago() {
        int fila = tablaPagos.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un pago.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        PagoDAO dao = new PagoDAO();
        Pago pago = dao.obtenerPagoPorId(id);
        if (pago != null) {
            if (!llenarDatosDesdeFormulario(pago)) return;
            dao.actualizarPago(pago);
            cargarPagos();
            JOptionPane.showMessageDialog(this, "Pago actualizado.");
        }
    }

    /**
     * Elimina el pago seleccionado en la tabla tras confirmar la acción con el usuario.
     * Actualiza la tabla y limpia el formulario si la eliminación es exitosa.
     * Muestra mensajes de confirmación o error según corresponda.
     */
    private void eliminarPago() {
        int fila = tablaPagos.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un pago para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Confirmar eliminación
        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Estás seguro de que quieres eliminar este pago?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (opcion == JOptionPane.YES_OPTION) {
            int idPago = (int) modeloTabla.getValueAt(fila, 0);
            PagoDAO dao = new PagoDAO();
            boolean eliminado = dao.eliminarPago(idPago);

            if (eliminado) {
                cargarPagos();
                limpiarCampos();
                JOptionPane.showMessageDialog(this, "Pago eliminado correctamente.");
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se pudo eliminar el pago. Por favor, inténtalo de nuevo o contacta al administrador.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Carga en el comboBox todos los socios disponibles obtenidos a través del DAO.
     * Cada elemento contiene el ID y el nombre completo del socio para facilitar la selección.
     */
    private void cargarIdsSocios() {
        SocioDAO dao = new SocioDAO();
        for (Socio s : dao.obtenerTodosLosSocios()) {
            comboSocio.addItem(s.getIdSocio() + " - " + s.getNombreSocio() + " " + s.getApellidosSocio());
        }
    }

    /**
     * Carga todos los pagos desde la base de datos mediante el DAO y actualiza la tabla
     * para mostrar la información actualizada de cada pago.
     */
    public void cargarPagos() {
        modeloTabla.setRowCount(0);
        PagoDAO dao = new PagoDAO();
        for (Pago p : dao.obtenerTodosLosPagos()) {
            Socio socio = p.getSocio();
            String socioTexto = socio.getNombreSocio() + " " + socio.getApellidosSocio();  // ⬅ Aquí el cambio
            modeloTabla.addRow(new Object[]{
                    p.getIdPago(),
                    socioTexto,  // ⬅ Aquí antes solo era p.getSocio().getIdSocio()
                    p.getFechaPago(),
                    p.getImporte(),
                    p.getTipoCuota(),
                    p.getTipoPago(),
                    p.getMetodoPago(),
                    p.getEstado()
            });
        }
    }

    /**
     * Carga los datos de un pago específico (identificado por su ID) en el formulario,
     * incluyendo la selección del socio, fecha, importe, tipos, estado y recibo.
     *
     * @param id El identificador único del pago que se desea cargar.
     */
    private void cargarDatosPago(int id) {
        PagoDAO dao = new PagoDAO();
        Pago pago = dao.obtenerPagoPorId(id);
        if (pago != null) {
            int idSocio = pago.getSocio().getIdSocio();
            String nombreCompleto = pago.getSocio().getNombreSocio() + " " + pago.getSocio().getApellidosSocio();
            String itemBuscar = idSocio + " - " + nombreCompleto;

            for (int i = 0; i < comboSocio.getItemCount(); i++) {
                if (comboSocio.getItemAt(i).equalsIgnoreCase(itemBuscar)) {
                    comboSocio.setSelectedIndex(i);
                    break;
                }
            }

            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(pago.getFechaPago());
            dateChooserFechaPago.setDate(cal.getTime());
            txtImporte.setText(String.valueOf(pago.getImporte()));
            comboTipoCuota.setSelectedItem(pago.getTipoCuota());
            comboTipoPago.setSelectedItem(pago.getTipoPago());
            comboMetodoPago.setSelectedItem(pago.getMetodoPago());
            comboEstado.setSelectedItem(pago.getEstado());

            reciboSeleccionado = pago.getRecibo();
            lblRecibo.setText((reciboSeleccionado != null) ? "Recibo cargado" : "Sin recibo");
        }
    }

    /**
     * Abre un diálogo de selección de archivo para cargar un recibo (archivo) asociado
     * al pago. El recibo se almacena en un array de bytes y se muestra una indicación visual.
     * Maneja posibles errores durante la carga del archivo.
     */
    private void cargarRecibo() {
        JFileChooser chooser = new JFileChooser();
        int opcion = chooser.showOpenDialog(this);
        if (opcion == JFileChooser.APPROVE_OPTION) {
            try {
                File file = chooser.getSelectedFile();
                reciboSeleccionado = Files.readAllBytes(file.toPath());
                lblRecibo.setText("Recibo cargado");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al cargar el recibo.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Llena el objeto Pago con los datos ingresados en el formulario.
     * Valida que todos los campos requeridos sean correctos y notifica al usuario si hay errores.
     *
     * @param pago Objeto Pago que será llenado con los datos del formulario.
     * @return true si los datos son válidos y el objeto fue llenado correctamente; false en caso contrario.
     */
    private boolean llenarDatosDesdeFormulario(Pago pago) {
        // Validación de campos obligatorios
        if (comboSocio.getSelectedItem() == null ||
                dateChooserFechaPago.getDate() == null ||
                txtImporte.getText().trim().isEmpty() ||
                comboTipoCuota.getSelectedItem() == null ||
                comboTipoPago.getSelectedItem() == null ||
                comboMetodoPago.getSelectedItem() == null ||
                comboEstado.getSelectedItem() == null) {

            JOptionPane.showMessageDialog(this,
                    "Por favor, complete todos los campos obligatorios:\n" +
                            "- Socio\n" +
                            "- Fecha de pago\n" +
                            "- Importe\n" +
                            "- Tipo de cuota\n" +
                            "- Tipo de pago\n" +
                            "- Método de pago\n" +
                            "- Estado",
                    "Campos obligatorios",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        try {
            // Obtener ID del socio seleccionado
            String itemSeleccionado = (String) comboSocio.getSelectedItem();
            if (!itemSeleccionado.contains(" - ")) {
                JOptionPane.showMessageDialog(this, "Selecciona un socio válido.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            int idSocio = Integer.parseInt(itemSeleccionado.split(" - ")[0]);
            Socio socio = new Socio();
            socio.setIdSocio(idSocio);
            pago.setSocio(socio);

            // Fecha
            java.util.Date utilDate = dateChooserFechaPago.getDate();
            pago.setFechaPago(new java.sql.Date(utilDate.getTime()));

            // Importe
            double importe;
            try {
                importe = Double.parseDouble(txtImporte.getText().trim());
                if (importe <= 0) {
                    JOptionPane.showMessageDialog(this,
                            "El IMPORTE debe ser un número positivo.",
                            "Importe inválido",
                            JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                        "El IMPORTE debe ser un número válido.",
                        "Formato inválido",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
            pago.setImporte(importe);

            // Enums
            pago.setTipoCuota((TipoCuota) comboTipoCuota.getSelectedItem());
            pago.setTipoPago((TipoPago) comboTipoPago.getSelectedItem());
            pago.setMetodoPago((MetodoPago) comboMetodoPago.getSelectedItem());
            pago.setEstado((Estado) comboEstado.getSelectedItem());

            // Archivo de recibo (opcional, según tu lógica)
            pago.setRecibo(reciboSeleccionado);

            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error en los datos del formulario: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Actualiza el filtro aplicado a la tabla de pagos para buscar por ID o nombre del socio,
     * usando el texto ingresado en el campo de búsqueda. Si el texto está vacío o es el texto por defecto,
     * se elimina el filtro.
     */
    private void actualizarFiltro() {
        String texto = txtBuscarPago.getText().trim();
        if (texto.isEmpty() || texto.equalsIgnoreCase("Introduce ID o nombre del socio")) {
            sorter.setRowFilter(null); // No aplicar ningún filtro
        } else {
            RowFilter<DefaultTableModel, Object> filtroNombre = RowFilter.regexFilter("(?i)" + texto, 1);
            sorter.setRowFilter(filtroNombre); // Solo por nombre
        }
    }

    /**
     * Limpia todos los campos del formulario, resetea las selecciones de combos,
     * borra el recibo seleccionado y limpia la selección en la tabla de pagos.
     */
    public void limpiarCampos() {
        comboSocio.setSelectedIndex(-1);
        dateChooserFechaPago.setDate(null);
        txtImporte.setText("");
        comboTipoCuota.setSelectedIndex(-1);
        comboTipoPago.setSelectedIndex(-1);
        comboMetodoPago.setSelectedIndex(-1);
        comboEstado.setSelectedIndex(-1);
        reciboSeleccionado = null;
        lblRecibo.setText("Sin recibo");
        tablaPagos.clearSelection();
    }

}