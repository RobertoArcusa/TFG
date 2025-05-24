package com.robertoarcusa.tfg.vistas;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
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
        setLayout(new BorderLayout());

        // Panel de campos responsive
        JPanel panelCampos = new JPanel(new GridBagLayout());
        panelCampos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints pc = new GridBagConstraints();
        pc.insets = new Insets(5, 10, 5, 10);
        pc.fill = GridBagConstraints.HORIZONTAL;
        pc.anchor = GridBagConstraints.NORTHWEST;
        pc.weighty = 1.0;

        // Componentes
        comboSocio = new JComboBox<>();
        cargarIdsSocios();
        comboSocio.setPreferredSize(new Dimension(250, 45));

        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Hoy");
        p.put("text.month", "Mes");
        p.put("text.year", "Año");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        datePicker = new JDatePickerImpl(datePanel, new FormateadorFecha());
        datePicker.setPreferredSize(new Dimension(250, 45));

        txtImporte = new JTextField();
        txtImporte.setPreferredSize(new Dimension(250, 45));

        comboTipoCuota = new JComboBox<>(TipoCuota.values());
        comboTipoCuota.setPreferredSize(new Dimension(250, 45));

        comboTipoPago = new JComboBox<>(TipoPago.values());
        comboTipoPago.setPreferredSize(new Dimension(250, 45));

        comboMetodoPago = new JComboBox<>(MetodoPago.values());
        comboMetodoPago.setPreferredSize(new Dimension(250, 45));

        comboEstado = new JComboBox<>(Estado.values());
        comboEstado.setPreferredSize(new Dimension(250, 45));

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

        JPanel panelBotonesRecibo = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panelBotonesRecibo.add(btnCargarRecibo);
        panelBotonesRecibo.add(btnEliminarRecibo);

        // Columnas internas
        JPanel columna1 = new JPanel(new GridBagLayout());
        JPanel columna2 = new JPanel(new GridBagLayout());
        JPanel columna3 = new JPanel(new BorderLayout());

        // Configuración GridBagConstraints para columnas internas
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;

        // Columna 1
        c.gridx = 0; c.gridy = 0;
        columna1.add(new JLabel("SOCIO:"), c);
        c.gridx = 1;
        columna1.add(comboSocio, c);

        c.gridx = 0; c.gridy++;
        columna1.add(new JLabel("FECHA PAGO:"), c);
        c.gridx = 1;
        columna1.add(datePicker, c);

        c.gridx = 0; c.gridy++;
        columna1.add(new JLabel("IMPORTE:"), c);
        c.gridx = 1;
        columna1.add(txtImporte, c);

        c.gridx = 0; c.gridy++;
        columna1.add(new JLabel("TIPO CUOTA:"), c);
        c.gridx = 1;
        columna1.add(comboTipoCuota, c);

        // Columna 2
        c.gridx = 0; c.gridy = 0;
        columna2.add(new JLabel("TIPO PAGO:"), c);
        c.gridx = 1;
        columna2.add(comboTipoPago, c);

        c.gridx = 0; c.gridy++;
        columna2.add(new JLabel("MÉTODO PAGO:"), c);
        c.gridx = 1;
        columna2.add(comboMetodoPago, c);

        c.gridx = 0; c.gridy++;
        columna2.add(new JLabel("ESTADO:"), c);
        c.gridx = 1;
        columna2.add(comboEstado, c);

        // Columna 3 (imagen recibo)
        columna3.add(new JLabel("FOTO RECIBO", SwingConstants.CENTER), BorderLayout.NORTH);
        columna3.add(lblRecibo, BorderLayout.CENTER);
        columna3.add(panelBotonesRecibo, BorderLayout.SOUTH);

        // Añadir columnas al panelCampos con pesos para que sean responsivas
        pc.weightx = 0.33;
        pc.gridx = 0; pc.gridy = 0;
        panelCampos.add(columna1, pc);

        pc.gridx = 1;
        panelCampos.add(columna2, pc);

        pc.gridx = 2;
        panelCampos.add(columna3, pc);

        // Tabla y modelo
        modeloTabla = new DefaultTableModel(new String[]{
                "ID", "Socio", "Fecha", "Importe", "Tipo Cuota", "Tipo Pago", "Método", "Estado"
        }, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaPagos = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaPagos);

        btnAgregar = new JButton("Añadir pago");
        btnGuardar = new JButton("Modificar pago");
        btnEliminar = new JButton("Eliminar pago");
        btnLimpiar = new JButton("Limpiar Campos");

        btnAgregar.addActionListener(e -> agregarNuevoPago());
        btnGuardar.addActionListener(e -> modificarPago());
        btnEliminar.addActionListener(e -> eliminarPago());
        btnLimpiar.addActionListener(e -> limpiarCampos());

        txtBuscarPago = new JTextField(20);
        txtBuscarPago.setForeground(Color.GRAY);
        txtBuscarPago.setText("Introduce ID o nombre del socio");

        txtBuscarPago.addFocusListener(new FocusListener() {
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
            public void insertUpdate(DocumentEvent e) {
                actualizarFiltro();
            }

            public void removeUpdate(DocumentEvent e) {
                actualizarFiltro();
            }

            public void changedUpdate(DocumentEvent e) {
                actualizarFiltro();
            }
        });

        // Panel inferior con GridBagLayout para más control responsivo
        JPanel panelBotones = new JPanel(new GridBagLayout());
        GridBagConstraints pb = new GridBagConstraints();
        pb.insets = new Insets(5, 5, 5, 5);
        pb.gridx = 0; pb.gridy = 0;
        panelBotones.add(new JLabel("BUSCAR PAGO"), pb);
        pb.gridx++;
        pb.weightx = 1.0;
        pb.fill = GridBagConstraints.HORIZONTAL;
        panelBotones.add(txtBuscarPago, pb);
        pb.weightx = 0;
        pb.fill = GridBagConstraints.NONE;
        pb.gridx++;
        panelBotones.add(btnAgregar, pb);
        pb.gridx++;
        panelBotones.add(btnGuardar, pb);
        pb.gridx++;
        panelBotones.add(btnEliminar, pb);
        pb.gridx++;
        panelBotones.add(btnLimpiar, pb);

        // Añadir al panel principal
        add(panelCampos, BorderLayout.NORTH);
        add(scrollTabla, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

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

        int idPago = (int) modeloTabla.getValueAt(fila, 0);
        PagoDAO dao = new PagoDAO();
        boolean eliminado = dao.eliminarPago(idPago);

        if (!eliminado) {
            JOptionPane.showMessageDialog(this,
                    "No se puede eliminar el pago porque tiene datos relacionados o ha ocurrido un error.",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
        } else {
            cargarPagos();
            limpiarCampos();
            JOptionPane.showMessageDialog(this, "Pago eliminado.");
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


    private void limpiarCampos() {
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
}