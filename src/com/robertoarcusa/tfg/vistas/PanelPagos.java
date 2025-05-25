package com.robertoarcusa.tfg.vistas;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Properties;

import com.robertoarcusa.tfg.clases.Pago;
import com.robertoarcusa.tfg.clases.Socio;
import com.robertoarcusa.tfg.dao.PagoDAO;
import com.robertoarcusa.tfg.dao.SocioDAO;
import com.robertoarcusa.tfg.enums.Estado;
import com.robertoarcusa.tfg.enums.MetodoPago;
import com.robertoarcusa.tfg.enums.TipoCuota;
import com.robertoarcusa.tfg.enums.TipoPago;
import com.robertoarcusa.tfg.util.FormateadorFecha;
import org.jdatepicker.impl.*;

public class PanelPagos extends JPanel {

    private JTable tablaPagos;
    private DefaultTableModel modeloTabla;
    private JComboBox<String> comboSocio;
    private JDatePickerImpl datePicker;
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

        datePicker = new JDatePickerImpl(
                new JDatePanelImpl(new UtilDateModel(), new Properties() {{
                    put("text.today", "Hoy");
                    put("text.month", "Mes");
                    put("text.year", "Año");
                }}),
                new FormateadorFecha()
        );
        datePicker.setPreferredSize(new Dimension(250, 40));

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

        btnCargarRecibo = new JButton("Cargar Recibo");
        btnEliminarRecibo = new JButton("Eliminar Recibo");
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
        columna1.add(datePicker, c1);

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

    private void agregarNuevoPago() {
        Pago pago = new Pago();
        if (!llenarDatosDesdeFormulario(pago)) return;
        new PagoDAO().agregarPago(pago);
        cargarPagos();
        JOptionPane.showMessageDialog(this, "Pago añadido.");
    }

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

    private void cargarIdsSocios() {
        SocioDAO dao = new SocioDAO();
        for (Socio s : dao.obtenerTodosLosSocios()) {
            comboSocio.addItem(s.getIdSocio() + " - " + s.getNombreSocio() + " " + s.getApellidosSocio());
        }
    }

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
            datePicker.getModel().setDate(
                    cal.get(java.util.Calendar.YEAR),
                    cal.get(java.util.Calendar.MONTH),
                    cal.get(java.util.Calendar.DAY_OF_MONTH));
            datePicker.getModel().setSelected(true);

            txtImporte.setText(String.valueOf(pago.getImporte()));
            comboTipoCuota.setSelectedItem(pago.getTipoCuota());
            comboTipoPago.setSelectedItem(pago.getTipoPago());
            comboMetodoPago.setSelectedItem(pago.getMetodoPago());
            comboEstado.setSelectedItem(pago.getEstado());

            reciboSeleccionado = pago.getRecibo();
            lblRecibo.setText((reciboSeleccionado != null) ? "Recibo cargado" : "Sin recibo");
        }
    }

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

    private boolean llenarDatosDesdeFormulario(Pago pago) {
        try {
            String itemSeleccionado = (String) comboSocio.getSelectedItem();
            if (itemSeleccionado == null || !itemSeleccionado.contains(" - ")) {
                JOptionPane.showMessageDialog(this, "Selecciona un socio válido.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            int idSocio = Integer.parseInt(itemSeleccionado.split(" - ")[0]);
            Socio socio = new Socio();
            socio.setIdSocio(idSocio);
            pago.setSocio(socio);

            java.util.Date utilDate = (java.util.Date) datePicker.getModel().getValue();
            if (utilDate == null) {
                JOptionPane.showMessageDialog(this, "Selecciona una fecha de pago válida.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            pago.setFechaPago(new java.sql.Date(utilDate.getTime()));

            double importe = Double.parseDouble(txtImporte.getText().trim());
            pago.setImporte(importe);

            pago.setTipoCuota((TipoCuota) comboTipoCuota.getSelectedItem());
            pago.setTipoPago((TipoPago) comboTipoPago.getSelectedItem());
            pago.setMetodoPago((MetodoPago) comboMetodoPago.getSelectedItem());
            pago.setEstado((Estado) comboEstado.getSelectedItem());
            pago.setRecibo(reciboSeleccionado);

            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error en los datos del formulario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private void actualizarFiltro() {
        String texto = txtBuscarPago.getText().trim();
        if (texto.isEmpty() || texto.equalsIgnoreCase("Introduce ID o nombre del socio")) {
            sorter.setRowFilter(null); // No aplicar ningún filtro
        } else {
            RowFilter<DefaultTableModel, Object> filtroNombre = RowFilter.regexFilter("(?i)" + texto, 1);
            sorter.setRowFilter(filtroNombre); // Solo por nombre
        }
    }


    public void limpiarCampos() {
        comboSocio.setSelectedIndex(-1);
        datePicker.getModel().setSelected(false);
        txtImporte.setText("");
        comboTipoCuota.setSelectedIndex(-1);
        comboTipoPago.setSelectedIndex(-1);
        comboMetodoPago.setSelectedIndex(-1);
        comboEstado.setSelectedIndex(-1);
        reciboSeleccionado = null;
        lblRecibo.setText("Sin recibo");
        tablaPagos.clearSelection();
    }

    private void setTextFieldHint(JTextField textField, String hint) {
        textField.setForeground(Color.GRAY);
        textField.setText(hint);

        textField.addFocusListener(new FocusListener() {
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
                    textField.setForeground(Color.GRAY);
                    textField.setText(hint);
                }
            }
        });
    }
}