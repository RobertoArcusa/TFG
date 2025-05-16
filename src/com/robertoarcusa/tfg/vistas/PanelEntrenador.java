package com.robertoarcusa.tfg.vistas;

import com.robertoarcusa.tfg.clases.Entrenador;
import com.robertoarcusa.tfg.clases.Socio;
import com.robertoarcusa.tfg.dao.EntrenadorDAO;
import com.robertoarcusa.tfg.enums.TipoUsuario;
import com.robertoarcusa.tfg.util.FormateadorFecha;
import com.robertoarcusa.tfg.util.Sesion;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

public class PanelEntrenador extends JPanel {

    private JTable tablaEntrenadores;
    private DefaultTableModel modeloTabla;
    private JTextField txtNombre, txtApellidos, txtEspecialidad, txtTelefono, txtSalario;
    private JButton btnGuardar, btnAgregar, btnEliminar, btnLimpiarCampos;
    private JDatePickerImpl datepickerContratacion;
    private JLabel lblFoto;
    private byte[] fotoPerfilSeleccionada;
    private JTextField txtBuscarEntrenador;


    public PanelEntrenador() {
        setLayout(new BorderLayout());

        JPanel panelCampos = new JPanel(new GridLayout(1, 3, 10, 10));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JPanel columna1 = new JPanel(new GridBagLayout());
        JPanel columna2 = new JPanel(new GridBagLayout());
        JPanel columna3 = new JPanel(new BorderLayout());

        txtNombre = new JTextField(); txtNombre.setPreferredSize(new Dimension(250, 45));
        txtApellidos = new JTextField(); txtApellidos.setPreferredSize(new Dimension(250, 45));
        txtEspecialidad = new JTextField(); txtEspecialidad.setPreferredSize(new Dimension(250, 45));
        txtTelefono = new JTextField(); txtTelefono.setPreferredSize(new Dimension(250, 45));
        txtSalario = new JTextField(); txtSalario.setPreferredSize(new Dimension(250, 45));

        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Hoy");
        p.put("text.month", "Mes");
        p.put("text.year", "Año");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        datepickerContratacion = new JDatePickerImpl(datePanel, new FormateadorFecha());
        datepickerContratacion.setPreferredSize(new Dimension(250, 45));

        lblFoto = new JLabel("Sin foto", SwingConstants.CENTER);
        lblFoto.setPreferredSize(new Dimension(150, 150));
        lblFoto.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JButton btnCargarFoto = new JButton("Cargar Foto");
        btnCargarFoto.addActionListener(e -> cargarFoto());

        JButton btnEliminarFoto = new JButton("Eliminar Foto");
        btnEliminarFoto.addActionListener(e -> {
            fotoPerfilSeleccionada = null;
            lblFoto.setIcon(null);
            lblFoto.setText("Sin foto");
        });

        JPanel panelBotonesFoto = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panelBotonesFoto.add(btnCargarFoto);
        panelBotonesFoto.add(btnEliminarFoto);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 5, 35);

        // Columna 1
        columna1.add(new JLabel("NOMBRE:"), gbc); gbc.gridx = 1;
        columna1.add(txtNombre, gbc);
        gbc.gridx = 0; gbc.gridy++;
        columna1.add(new JLabel("APELLIDOS:"), gbc); gbc.gridx = 1;
        columna1.add(txtApellidos, gbc);
        gbc.gridx = 0; gbc.gridy++;
        columna1.add(new JLabel("ESPECIALIDAD:"), gbc); gbc.gridx = 1;
        columna1.add(txtEspecialidad, gbc);
        gbc.gridx = 0; gbc.gridy++;
        columna1.add(new JLabel("TELÉFONO:"), gbc); gbc.gridx = 1;
        columna1.add(txtTelefono, gbc);

        // Columna 2
        gbc.gridx = 0; gbc.gridy = 0;
        columna2.add(new JLabel("FECHA CONTRATACIÓN:"), gbc); gbc.gridx = 1;
        columna2.add(datepickerContratacion, gbc);
        gbc.gridx = 0; gbc.gridy++;
        columna2.add(new JLabel("SALARIO:"), gbc); gbc.gridx = 1;
        columna2.add(txtSalario, gbc);

        // Columna 3
        columna3.add(new JLabel("FOTO ENTRENADOR", SwingConstants.CENTER), BorderLayout.NORTH);
        columna3.add(lblFoto, BorderLayout.CENTER);
        columna3.add(panelBotonesFoto, BorderLayout.SOUTH);

        columna1.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 0));
        columna3.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 50));

        panelCampos.add(columna1);
        panelCampos.add(columna2);
        panelCampos.add(columna3);

        modeloTabla = new DefaultTableModel(new String[]{"ID", "Nombre", "Apellidos", "Especialidad", "Teléfono", "Fecha Contratación", "Salario"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaEntrenadores = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaEntrenadores);

        btnAgregar = new JButton("Añadir entrenador");
        btnGuardar = new JButton("Modificar entrenador");
        btnEliminar = new JButton("Eliminar entrenador");
        btnLimpiarCampos = new JButton("Limpiar Campos");

        btnAgregar.addActionListener(e -> agregarNuevoEntrenador());
        btnGuardar.addActionListener(e -> modificarEntrenador());
        btnEliminar.addActionListener(e -> eliminarEntrenador());
        btnLimpiarCampos.addActionListener(e -> limpiarCampos());

        txtBuscarEntrenador = new JTextField(20);
        setTextFieldHint(txtBuscarEntrenador, "Introduce un nombre de entrenador");
        txtBuscarEntrenador.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filtrarEntrenadoresPorNombre(txtBuscarEntrenador.getText());
            }
        });

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBusqueda.add(new JLabel("BUSCAR ENTRENADOR"));
        panelBusqueda.add(txtBuscarEntrenador);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBotones.add(btnAgregar);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiarCampos);

        JPanel panelContenedorInferior = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelContenedorInferior.add(panelBusqueda);
        panelContenedorInferior.add(panelBotones);

        add(panelCampos, BorderLayout.NORTH);
        add(scrollTabla, BorderLayout.CENTER);
        add(panelContenedorInferior, BorderLayout.SOUTH);

        cargarEntrenadores();

        tablaEntrenadores.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int fila = tablaEntrenadores.getSelectedRow();
                if (fila >= 0 && tablaEntrenadores.getModel().getColumnCount() > 3) {
                    int id = (int) modeloTabla.getValueAt(fila, 0);
                    cargarDatosEntrenador(id);
                }
            }
        });

        // 🔒 Restricción si el usuario es BASIC
        Socio usuarioActual = Sesion.getUsuarioActual();
        if (usuarioActual != null && usuarioActual.getTipoUsuario() == TipoUsuario.BASIC) {
            panelCampos.setVisible(false);
            panelContenedorInferior.setVisible(false);

            DefaultTableModel modeloBasico = new DefaultTableModel(new String[]{"Nombre", "Apellidos", "Especialidad"}, 0) {
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            for (Entrenador ent : obtenerEntrenadoresDesdeDAO()) {
                modeloBasico.addRow(new Object[]{
                        ent.getNombreEntrenador(),
                        ent.getApellidosEntrenador(),
                        ent.getEspecialidad()
                });
            }

            tablaEntrenadores.setModel(modeloBasico);
        }
    }

    private List<Entrenador> obtenerEntrenadoresDesdeDAO() {
        return EntrenadorDAO.obtenerTodosLosEntrenadores();
    }

    private void cargarDatosEntrenador(int id) {
        EntrenadorDAO dao = new EntrenadorDAO();
        Entrenador entrenador = dao.obtenerEntrenadorPorId(id);
        if (entrenador != null) {
            // Limpiamos y cargamos los campos de texto
            txtNombre.setText(entrenador.getNombreEntrenador());
            txtApellidos.setText(entrenador.getApellidosEntrenador());
            txtEspecialidad.setText(entrenador.getEspecialidad());
            txtTelefono.setText(entrenador.getTelefono());
            txtSalario.setText(String.valueOf(entrenador.getSalario()));

            // Fecha de contratación
            if (entrenador.getFechaContratacion() != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(entrenador.getFechaContratacion());

                datepickerContratacion.getModel().setDate(
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)
                );
                datepickerContratacion.getModel().setSelected(true);
            }

            // Foto de perfil
            fotoPerfilSeleccionada = entrenador.getFotoPerfil();
            if (fotoPerfilSeleccionada != null) {
                ImageIcon fotoIcon = new ImageIcon(fotoPerfilSeleccionada);

                int labelWidth = lblFoto.getWidth();
                int labelHeight = lblFoto.getHeight();

                int imageWidth = fotoIcon.getIconWidth();
                int imageHeight = fotoIcon.getIconHeight();

                double widthRatio = (double) labelWidth / imageWidth;
                double heightRatio = (double) labelHeight / imageHeight;
                double scaleRatio = Math.min(widthRatio, heightRatio);

                int newWidth = (int) (imageWidth * scaleRatio);
                int newHeight = (int) (imageHeight * scaleRatio);

                Image scaledImage = fotoIcon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

                lblFoto.setIcon(new ImageIcon(scaledImage));
                lblFoto.setText("");
            } else {
                fotoPerfilSeleccionada = null;
                lblFoto.setIcon(null);
                lblFoto.setText("Sin foto");
            }
        }
    }

    private void agregarNuevoEntrenador() {
        // Verificar que todos los campos estén rellenos
        if (txtNombre.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, rellene el campo NOMBRE.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (txtApellidos.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, rellene el campo APELLIDOS.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (txtEspecialidad.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, rellene el campo ESPECIALIDAD.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (txtTelefono.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, rellene el campo TELÉFONO.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (txtSalario.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, rellene el campo SALARIO.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (datepickerContratacion.getModel().getValue() == null) {
            JOptionPane.showMessageDialog(this, "Por favor, rellene el campo FECHA DE CONTRATACIÓN.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Verificar si ya existe un teléfono registrado
        if (existeTelefono(txtTelefono.getText())) {
            JOptionPane.showMessageDialog(this, "Ya existe un entrenador con este teléfono.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Llenar y guardar el nuevo entrenador
        Entrenador nuevo = new Entrenador();
        llenarDatosDesdeFormulario(nuevo);
        EntrenadorDAO dao = new EntrenadorDAO();
        dao.agregarEntrenador(nuevo);
        limpiarCampos();
        cargarEntrenadores();
        JOptionPane.showMessageDialog(this, "Entrenador añadido.");
    }

    private void modificarEntrenador() {
        int fila = tablaEntrenadores.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un entrenador.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) modeloTabla.getValueAt(fila, 0);
        EntrenadorDAO dao = new EntrenadorDAO();
        Entrenador entrenador = dao.obtenerEntrenadorPorId(id);
        if (entrenador != null) {
            llenarDatosDesdeFormulario(entrenador);
            dao.actualizarEntrenador(entrenador);
            cargarEntrenadores();
            JOptionPane.showMessageDialog(this, "Entrenador actualizado.");
        }
    }

    private void eliminarEntrenador() {
        int fila = tablaEntrenadores.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un entrenador.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) modeloTabla.getValueAt(fila, 0);
        EntrenadorDAO dao = new EntrenadorDAO();
        Entrenador entrenador = dao.obtenerEntrenadorPorId(id);
        if (entrenador != null) {
            dao.eliminarEntrenador(entrenador);
            cargarEntrenadores();
            limpiarCampos();
            JOptionPane.showMessageDialog(this, "Entrenador eliminado.");
        }
    }

    private void llenarDatosDesdeFormulario(Entrenador entrenador) {
        entrenador.setNombreEntrenador(txtNombre.getText());
        entrenador.setApellidosEntrenador(txtApellidos.getText());
        entrenador.setEspecialidad(txtEspecialidad.getText());
        entrenador.setTelefono(txtTelefono.getText());
        entrenador.setSalario(Double.parseDouble(txtSalario.getText()));

        if (datepickerContratacion.getModel().getValue() != null) {
            java.util.Date selectedDate = (java.util.Date) datepickerContratacion.getModel().getValue();
            entrenador.setFechaContratacion(new java.sql.Date(selectedDate.getTime()));
        }

        entrenador.setFotoPerfil(fotoPerfilSeleccionada);
    }

    private boolean existeTelefono(String telefono) {
        EntrenadorDAO dao = new EntrenadorDAO();
        return dao.existeTelefono(telefono);
    }

    private void filtrarEntrenadoresPorNombre(String filtro) {
        modeloTabla.setRowCount(0);  // Limpiar la tabla
        EntrenadorDAO dao = new EntrenadorDAO();
        List<Entrenador> entrenadores = dao.obtenerTodosLosEntrenadores();
        for (Entrenador entrenador : entrenadores) {
            if (entrenador.getNombreEntrenador().toLowerCase().contains(filtro.toLowerCase())) {
                modeloTabla.addRow(new Object[]{
                        entrenador.getIdEntrenador(),
                        entrenador.getNombreEntrenador(),
                        entrenador.getApellidosEntrenador(),
                        entrenador.getEspecialidad(),
                        entrenador.getTelefono(),
                        entrenador.getSalario(),
                        entrenador.getFechaContratacion()
                });
            }
        }
    }

    private void cargarEntrenadores() {
        modeloTabla.setRowCount(0);
        EntrenadorDAO dao = new EntrenadorDAO();
        List<Entrenador> entrenadores = dao.obtenerTodosLosEntrenadores();
        for (Entrenador e : entrenadores) {
            modeloTabla.addRow(new Object[]{
                    e.getIdEntrenador(),
                    e.getNombreEntrenador(),
                    e.getApellidosEntrenador(),
                    e.getEspecialidad(),
                    e.getTelefono(),
                    e.getFechaContratacion(),
                    e.getSalario(),
                    e.getFotoPerfil()
            });
        }
    }

    private void cargarFoto() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                fotoPerfilSeleccionada = Files.readAllBytes(file.toPath());
                ImageIcon imageIcon = new ImageIcon(fotoPerfilSeleccionada);
                Image image = imageIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                lblFoto.setIcon(new ImageIcon(image));
                lblFoto.setText("");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtApellidos.setText("");
        txtEspecialidad.setText("");
        txtTelefono.setText("");
        txtSalario.setText("");
        datepickerContratacion.getModel().setSelected(false);
        fotoPerfilSeleccionada = null; // Importantísimo
        lblFoto.setIcon(null);
        lblFoto.setText("Sin foto");
    }

    private void setTextFieldHint(JTextField textField, String hint) {
        textField.setText(hint);
        textField.setForeground(Color.GRAY);

        textField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(hint)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText(hint);
                    textField.setForeground(Color.GRAY);
                }
            }
        });
    }
}