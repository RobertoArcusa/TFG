package com.robertoarcusa.tfg.vistas;

import com.robertoarcusa.tfg.clases.Socio;
import com.robertoarcusa.tfg.dao.SocioDAO;
import com.robertoarcusa.tfg.enums.TipoMembresia;
import com.robertoarcusa.tfg.enums.TipoUsuario;
import com.robertoarcusa.tfg.util.FormateadorFecha;
import com.robertoarcusa.tfg.util.Sesion;
import org.jdatepicker.impl.*;
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
import java.util.Properties;

public class PanelSocios extends JPanel {

    private JTable tablaSocios;
    private DefaultTableModel modeloTabla;
    private JTextField txtNombre, txtApellidos, txtDni, txtTelefono, txtContrasena;
    private JComboBox<TipoMembresia> comboMembresia;
    private JComboBox<TipoUsuario> comboTipoUsuario;
    private JButton btnGuardar, btnAgregar, btnEliminar, btnLimpiar;
    private JDatePickerImpl datePicker;
    private JLabel lblFoto;
    private byte[] fotoPerfilSeleccionada;
    private JTextField txtBuscarSocio;

    public PanelSocios() {
        setLayout(new BorderLayout(10, 10)); // Pequeños gaps

        // PANEL CAMPOS CON GRIDBAGLAYOUT
        JPanel panelCampos = new JPanel(new GridBagLayout());
        panelCampos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;

        // --- COLUMNA 1 ---
        JPanel columna1 = new JPanel(new GridBagLayout());
        GridBagConstraints c1 = new GridBagConstraints();
        c1.insets = new Insets(5, 5, 5, 5);
        c1.fill = GridBagConstraints.HORIZONTAL;
        c1.weightx = 1.0;  // Para que el JTextField se expanda horizontalmente

        c1.gridx = 0; c1.gridy = 0;
        columna1.add(new JLabel("NOMBRE:"), c1);
        c1.gridx = 1;
        txtNombre = new JTextField();
        txtNombre.setPreferredSize(new Dimension(350, 40));  // Tamaño preferido mayor
        columna1.add(txtNombre, c1);

        c1.gridx = 0; c1.gridy++;
        columna1.add(new JLabel("APELLIDOS:"), c1);
        c1.gridx = 1;
        txtApellidos = new JTextField();
        txtApellidos.setPreferredSize(new Dimension(350, 40));
        columna1.add(txtApellidos, c1);

        c1.gridx = 0; c1.gridy++;
        columna1.add(new JLabel("DNI:"), c1);
        c1.gridx = 1;
        txtDni = new JTextField();
        txtDni.setPreferredSize(new Dimension(350, 40));
        columna1.add(txtDni, c1);

        c1.gridx = 0; c1.gridy++;
        columna1.add(new JLabel("TELÉFONO:"), c1);
        c1.gridx = 1;
        txtTelefono = new JTextField();
        txtTelefono.setPreferredSize(new Dimension(350, 40));
        columna1.add(txtTelefono, c1);

        c1.gridx = 0; c1.gridy++;
        columna1.add(new JLabel("CONTRASEÑA:"), c1);
        c1.gridx = 1;
        txtContrasena = new JTextField();
        txtContrasena.setPreferredSize(new Dimension(350, 40));
        columna1.add(txtContrasena, c1);


        // --- COLUMNA 2 ---
        JPanel columna2 = new JPanel(new GridBagLayout());
        GridBagConstraints c2 = new GridBagConstraints();
        c2.insets = new Insets(5, 5, 5, 5);
        c2.fill = GridBagConstraints.HORIZONTAL;
        c2.weightx = 1.0;

        c2.gridx = 0; c2.gridy = 0;
        columna2.add(new JLabel("FECHA NACIMIENTO:"), c2);
        c2.gridx = 1;
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Hoy");
        p.put("text.month", "Mes");
        p.put("text.year", "Año");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        datePicker = new JDatePickerImpl(datePanel, new FormateadorFecha());
        datePicker.setPreferredSize(new Dimension(350, 40));  // Tamaño uniforme
        columna2.add(datePicker, c2);

        c2.gridx = 0; c2.gridy++;
        columna2.add(new JLabel("TIPO USUARIO:"), c2);
        c2.gridx = 1;
        comboTipoUsuario = new JComboBox<>(TipoUsuario.values());
        comboTipoUsuario.setPreferredSize(new Dimension(350, 40));  // Tamaño uniforme
        if (Sesion.getUsuarioActual() != null && Sesion.getUsuarioActual().getTipoUsuario() == TipoUsuario.EDITOR) {
            comboTipoUsuario.setEnabled(false);
        }
        columna2.add(comboTipoUsuario, c2);

        c2.gridx = 0; c2.gridy++;
        columna2.add(new JLabel("TIPO MEMBRESÍA:"), c2);
        c2.gridx = 1;
        comboMembresia = new JComboBox<>(TipoMembresia.values());
        comboMembresia.setPreferredSize(new Dimension(350, 40));  // Tamaño uniforme
        columna2.add(comboMembresia, c2);


        // --- COLUMNA 3: FOTO ---
        JPanel columna3 = new JPanel(new BorderLayout(5,5));
        JLabel lblTituloFoto = new JLabel("FOTO SOCIO", SwingConstants.CENTER);
        columna3.add(lblTituloFoto, BorderLayout.NORTH);
        lblFoto = new JLabel("Sin foto", SwingConstants.CENTER);
        lblFoto.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        columna3.add(lblFoto, BorderLayout.CENTER);

        JPanel panelBotonesFoto = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        JButton btnCargarFoto = new JButton("Cargar Foto");
        btnCargarFoto.addActionListener(e -> cargarFoto());
        JButton btnEliminarFoto = new JButton("Eliminar Foto");
        btnEliminarFoto.addActionListener(e -> {
            fotoPerfilSeleccionada = null;
            lblFoto.setIcon(null);
            lblFoto.setText("Sin foto");
        });
        panelBotonesFoto.add(btnCargarFoto);
        panelBotonesFoto.add(btnEliminarFoto);
        columna3.add(panelBotonesFoto, BorderLayout.SOUTH);

        // AGREGAMOS LAS COLUMNAS AL PANEL CAMPOS
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0.4;
        gbc.fill = GridBagConstraints.BOTH;
        panelCampos.add(columna1, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.4;
        panelCampos.add(columna2, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0.2;
        panelCampos.add(columna3, gbc);

        // TABLA Y MODELO
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Nombre", "Apellidos", "DNI", "Teléfono", "Fecha nacimiento", "Tipo membresía", "Tipo usuario"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaSocios = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaSocios);

        // BOTONES ACCIÓN
        btnAgregar = new JButton("Añadir socio");
        btnGuardar = new JButton("Modificar socio");
        btnEliminar = new JButton("Eliminar socio");
        btnLimpiar = new JButton("Limpiar Campos");

        btnAgregar.addActionListener(e -> agregarNuevoSocio());
        btnGuardar.addActionListener(e -> modificarSocio());
        btnEliminar.addActionListener(e -> eliminarSocio());
        btnLimpiar.addActionListener(e -> limpiarCampos());

        // BUSCADOR
        txtBuscarSocio = new JTextField(20);
        setTextFieldHint(txtBuscarSocio, "Introduce un nombre de socio");
        txtBuscarSocio.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filtrarSociosPorNombre(txtBuscarSocio.getText());
            }
        });

        // Panel combinado: búsqueda + botones, centrado
        JPanel panelBusquedaBotones = new JPanel(new GridBagLayout());
        panelBusquedaBotones.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints ci = new GridBagConstraints();
        ci.insets = new Insets(5, 5, 5, 5);
        ci.gridx = 0;
        ci.gridy = 0;

        // Subpanel con FlowLayout centrado
        JPanel panelCentral = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // Añadir búsqueda
        panelCentral.add(new JLabel("BUSCAR SOCIO"));
        panelCentral.add(txtBuscarSocio);

        // Añadir botones
        panelCentral.add(btnAgregar);
        panelCentral.add(btnGuardar);
        panelCentral.add(btnEliminar);
        panelCentral.add(btnLimpiar);

        // Agregar el panel central al panel con GridBagLayout
        panelBusquedaBotones.add(panelCentral, ci);


        // Añadir a layout principal
        add(panelCampos, BorderLayout.NORTH);
        add(scrollTabla, BorderLayout.CENTER);
        add(panelBusquedaBotones, BorderLayout.SOUTH);

        cargarSocios();

        // Selección tabla para cargar datos
        tablaSocios.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int fila = tablaSocios.getSelectedRow();
                if (fila >= 0) {
                    int id = (int) modeloTabla.getValueAt(fila, 0);
                    cargarDatosSocio(id);
                }
            }
        });
    }

    private void filtrarSociosPorNombre(String filtro) {
        modeloTabla.setRowCount(0);  // Limpiar la tabla antes de aplicar el filtro
        SocioDAO dao = new SocioDAO();

        // Filtramos los socios cuyo nombre contenga el texto introducido
        for (Socio s : dao.obtenerTodosLosSocios()) {
            if (s.getNombreSocio().toLowerCase().contains(filtro.toLowerCase())) {
                modeloTabla.addRow(new Object[]{
                        s.getIdSocio(),
                        s.getNombreSocio(),
                        s.getApellidosSocio(),
                        s.getDni(),
                        s.getTelefono(),
                        s.getFechaNacimiento(),
                        s.getTipoMembresia(),
                        s.getTipoUsuario()
                });
            }
        }
    }

    private void cargarDatosSocio(int id) {
        SocioDAO dao = new SocioDAO();
        Socio socio = dao.obtenerSocioPorId(id);
        if (socio != null) {
            // Limpiamos los campos de texto antes de cargar nuevos datos
            txtNombre.setText(socio.getNombreSocio());
            txtApellidos.setText(socio.getApellidosSocio());
            txtDni.setText(socio.getDni());
            txtTelefono.setText(socio.getTelefono());
            txtContrasena.setText(socio.getContrasena());
            comboMembresia.setSelectedItem(socio.getTipoMembresia());
            comboTipoUsuario.setSelectedItem(socio.getTipoUsuario());

            // Fecha de nacimiento
            if (socio.getFechaNacimiento() != null) {
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.setTime(socio.getFechaNacimiento());
                datePicker.getModel().setDate(cal.get(java.util.Calendar.YEAR),
                        cal.get(java.util.Calendar.MONTH),
                        cal.get(java.util.Calendar.DAY_OF_MONTH));
                datePicker.getModel().setSelected(true);
            }

            // Foto de perfil
            if (socio.getFotoPerfil() != null) {
                // Si tiene foto, la mostramos manteniendo la proporción
                ImageIcon fotoIcon = new ImageIcon(socio.getFotoPerfil());

                // Obtenemos las dimensiones del JLabel donde se va a mostrar la foto
                int labelWidth = lblFoto.getWidth();
                int labelHeight = lblFoto.getHeight();

                // Obtenemos las dimensiones originales de la foto
                int imageWidth = fotoIcon.getIconWidth();
                int imageHeight = fotoIcon.getIconHeight();

                // Calculamos la escala para mantener la proporción
                double widthRatio = (double) labelWidth / imageWidth;
                double heightRatio = (double) labelHeight / imageHeight;

                // Usamos la menor escala para asegurarnos de que la imagen quepa en el JLabel
                double scaleRatio = Math.min(widthRatio, heightRatio);

                // Calculamos el nuevo tamaño de la imagen con la proporción mantenida
                int newWidth = (int) (imageWidth * scaleRatio);
                int newHeight = (int) (imageHeight * scaleRatio);

                // Redimensionamos la imagen manteniendo la proporción
                Image scaledImage = fotoIcon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

                // Actualizamos el JLabel con la imagen escalada
                lblFoto.setIcon(new ImageIcon(scaledImage));
                lblFoto.setText("");  // Limpiamos el texto "Sin foto" si hay una imagen
            } else {
                // Si no tiene foto, mostramos un mensaje predeterminado
                lblFoto.setIcon(null);  // Eliminamos cualquier imagen
                lblFoto.setText("Sin foto");  // Mostramos texto "Sin foto"
            }
        }
    }

    private void agregarNuevoSocio() {
        if (!tablaSocios.getSelectionModel().isSelectionEmpty()) return;

        SocioDAO dao = new SocioDAO();
        if (dao.existeDni(txtDni.getText())) {
            JOptionPane.showMessageDialog(this, "Ya existe un socio con ese DNI.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Socio nuevo = new Socio();
        llenarDatosDesdeFormulario(nuevo);
        dao.agregarSocio(nuevo);
        limpiarCampos();
        cargarSocios();
        JOptionPane.showMessageDialog(this, "Socio añadido.");
    }

    private void modificarSocio() {
        int fila = tablaSocios.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un socio.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) modeloTabla.getValueAt(fila, 0);
        SocioDAO dao = new SocioDAO();
        Socio socio = dao.obtenerSocioPorId(id);
        if (socio != null) {
            llenarDatosDesdeFormulario(socio);
            dao.actualizarSocio(socio);
            cargarSocios();
            JOptionPane.showMessageDialog(this, "Socio modificado.");
        }
    }

    private void eliminarSocio() {
        int fila = tablaSocios.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un socio para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) modeloTabla.getValueAt(fila, 0);
        SocioDAO dao = new SocioDAO();

        boolean eliminado = dao.eliminarSocio(id);

        if (!eliminado) {
            JOptionPane.showMessageDialog(this, "No se pudo eliminar el socio. Por favor, inténtelo de nuevo o contacte con el administrador.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            cargarSocios();
            limpiarCampos();
            JOptionPane.showMessageDialog(this, "Socio eliminado correctamente.");
        }
    }


    private void llenarDatosDesdeFormulario(Socio socio) {
        socio.setNombreSocio(txtNombre.getText());
        socio.setApellidosSocio(txtApellidos.getText());
        socio.setDni(txtDni.getText());
        socio.setTelefono(txtTelefono.getText());
        socio.setContrasena(txtContrasena.getText());
        socio.setTipoMembresia((TipoMembresia) comboMembresia.getSelectedItem());
        socio.setTipoUsuario((TipoUsuario) comboTipoUsuario.getSelectedItem());
        socio.setFotoPerfil(fotoPerfilSeleccionada);

        if (datePicker.getModel().getValue() != null) {
            java.util.Date selectedDate = (java.util.Date) datePicker.getModel().getValue();
            socio.setFechaNacimiento(new java.sql.Date(selectedDate.getTime()));
        }
    }

    private void cargarSocios() {
        modeloTabla.setRowCount(0);
        SocioDAO dao = new SocioDAO();
        for (Socio s : dao.obtenerTodosLosSocios()) {
            modeloTabla.addRow(new Object[]{s.getIdSocio(), s.getNombreSocio(), s.getApellidosSocio(), s.getDni(), s.getTelefono(), s.getFechaNacimiento(), s.getTipoMembresia(), s.getTipoUsuario()});
        }
    }

    private void cargarFoto() {
        JFileChooser chooser = new JFileChooser();
        int opcion = chooser.showOpenDialog(this);
        if (opcion == JFileChooser.APPROVE_OPTION) {
            try {
                File file = chooser.getSelectedFile();
                fotoPerfilSeleccionada = Files.readAllBytes(file.toPath());

                // Cargamos la imagen y la redimensionamos como miniatura
                ImageIcon icono = new ImageIcon(fotoPerfilSeleccionada);
                Image imagen = icono.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                lblFoto.setIcon(new ImageIcon(imagen));
                lblFoto.setText(""); // Eliminamos el texto anterior si lo tenía
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al cargar la foto.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtApellidos.setText("");
        txtDni.setText("");
        txtTelefono.setText("");
        txtContrasena.setText("");
        comboMembresia.setSelectedIndex(-1);
        comboTipoUsuario.setSelectedIndex(-1);
        datePicker.getModel().setSelected(false);
        fotoPerfilSeleccionada = null;
        tablaSocios.clearSelection();
        lblFoto.setIcon(null);
        lblFoto.setText("Sin foto");
    }

    private void setTextFieldHint(JTextField textField, String hint) {
        textField.setText(hint);
        textField.setForeground(Color.GRAY);  // Establece el color del hint a gris

        textField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(hint)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);  // Establece el color del texto normal
                }
            }
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText(hint);
                    textField.setForeground(Color.GRAY);  // Restaurar el color gris cuando no hay texto
                }
            }
        });
    }

}
