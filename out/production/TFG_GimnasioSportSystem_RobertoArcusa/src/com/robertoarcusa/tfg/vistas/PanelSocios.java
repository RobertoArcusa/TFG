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
        setLayout(new BorderLayout());

        // Crear panelCampos
        JPanel panelCampos = new JPanel(new GridLayout(1, 3, 10, 10));

        // Añadir un margen arriba y abajo a panelCampos
        panelCampos.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));  // 10 píxeles de margen superior e inferior

        // Crear las columnas con GridLayout
        JPanel columna1 = new JPanel(new GridBagLayout());
        JPanel columna2 = new JPanel(new GridBagLayout());
        JPanel columna3 = new JPanel(new BorderLayout());

        txtNombre = new JTextField();
        txtNombre.setPreferredSize(new Dimension(250, 45));
        txtApellidos = new JTextField();
        txtApellidos.setPreferredSize(new Dimension(250, 45));
        txtDni = new JTextField();
        txtDni.setPreferredSize(new Dimension(250, 45));
        txtTelefono = new JTextField();
        txtTelefono.setPreferredSize(new Dimension(250, 45));
        txtContrasena = new JTextField();
        txtContrasena.setPreferredSize(new Dimension(250, 45));

        comboMembresia = new JComboBox<>(TipoMembresia.values());
        comboMembresia.setPreferredSize(new Dimension(250, 45));
        comboTipoUsuario = new JComboBox<>(TipoUsuario.values());
        comboTipoUsuario.setPreferredSize(new Dimension(250, 45));
        if (Sesion.getUsuarioActual() != null && Sesion.getUsuarioActual().getTipoUsuario() == TipoUsuario.EDITOR) {
            comboTipoUsuario.setEnabled(false); // Deshabilita el combo de tipo usuario
            //comboTipoUsuario.setVisible(false); // Oculta el combo de tipo usuario
        }

        // Configuración de JDatePicker
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Hoy");
        p.put("text.month", "Mes");
        p.put("text.year", "Año");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        datePicker = new JDatePickerImpl(datePanel, new FormateadorFecha());
        datePicker.setPreferredSize(new Dimension(250, 45));

        // Foto de perfil
        lblFoto = new JLabel("Sin foto", SwingConstants.CENTER);
        lblFoto.setPreferredSize(new Dimension(150, 150));
        lblFoto.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // Botones para cargar/eliminar foto
        JButton btnCargarFoto = new JButton("Cargar Foto");
        btnCargarFoto.addActionListener(e -> cargarFoto());

        JButton btnEliminarFoto = new JButton("Eliminar Foto");
        btnEliminarFoto.addActionListener(e -> {
            fotoPerfilSeleccionada = null;
            lblFoto.setIcon(null);
            lblFoto.setText("Sin foto");
        });

        // Panel para botones de foto
        JPanel panelBotonesFoto = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panelBotonesFoto.add(btnCargarFoto);
        panelBotonesFoto.add(btnEliminarFoto);

        // Configuración de GridBagConstraints para los componentes
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; // Coloca la etiqueta en la primera columna
        gbc.gridy = 0; // Fila
        gbc.anchor = GridBagConstraints.WEST; // Alinea la etiqueta a la izquierda
        gbc.insets = new Insets(0, 0, 0, 35);  // Muy pequeño margen entre el label y el campo

        // Rellenamos las columnas con los componentes
        // Columna 1
        columna1.add(new JLabel("NOMBRE:"), gbc);
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, 5, 35);  // Espacio entre el campo y el siguiente campo
        columna1.add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy++;
        columna1.add(new JLabel("APELLIDOS:"), gbc);
        gbc.gridx = 1;
        columna1.add(txtApellidos, gbc);

        gbc.gridx = 0; gbc.gridy++;
        columna1.add(new JLabel("DNI:"), gbc);
        gbc.gridx = 1;
        columna1.add(txtDni, gbc);

        gbc.gridx = 0; gbc.gridy++;
        columna1.add(new JLabel("TELÉFONO:"), gbc);
        gbc.gridx = 1;
        columna1.add(txtTelefono, gbc);

        // Columna 2
        gbc.gridx = 0; gbc.gridy = 0;
        columna2.add(new JLabel("FECHA NACIMIENTO:"), gbc);
        gbc.gridx = 1;
        columna2.add(datePicker, gbc);

        gbc.gridx = 0; gbc.gridy++;
        columna2.add(new JLabel("TIPO USUARIO:"), gbc);
        gbc.gridx = 1;
        columna2.add(comboTipoUsuario, gbc);

        gbc.gridx = 0; gbc.gridy++;
        columna2.add(new JLabel("TIPO MEMBRESÍA:"), gbc);
        gbc.gridx = 1;
        columna2.add(comboMembresia, gbc);

        // Columna 3
        columna3.add(new JLabel("FOTO SOCIO", SwingConstants.CENTER), BorderLayout.NORTH);
        columna3.add(lblFoto, BorderLayout.CENTER);
        columna3.add(panelBotonesFoto, BorderLayout.SOUTH); // Panel con botones de foto

        // Añadir márgenes a las columnas
        columna1.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 0));  // Añadir margen izquierdo a la columna 1
        columna3.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 50));  // Mantener el margen derecho en columna 3

        // Añadir las columnas al panelCampos
        panelCampos.add(columna1);
        panelCampos.add(columna2);
        panelCampos.add(columna3);

        modeloTabla = new DefaultTableModel(new String[]{"ID", "Nombre", "Apellidos", "DNI", "Teléfono", "Fecha nacimiento", "Tipo membresía", "Tipo usuario"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaSocios = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaSocios);

        btnAgregar = new JButton("Añadir socio");
        btnGuardar = new JButton("Modificar socio");
        btnEliminar = new JButton("Eliminar socio");
        btnLimpiar = new JButton("Limpiar Campos");

        btnAgregar.addActionListener(e -> agregarNuevoSocio());
        btnGuardar.addActionListener(e -> modificarSocio());
        btnEliminar.addActionListener(e -> eliminarSocio());
        btnLimpiar.addActionListener(e -> limpiarCampos());

        // Inicializamos el campo de texto de búsqueda
        txtBuscarSocio = new JTextField(20);
        setTextFieldHint(txtBuscarSocio, "Introduce un nombre de socio");  // Añadir hint al campo de búsqueda

        // Añadir un listener para detectar cuando se escribe en el campo de búsqueda
        txtBuscarSocio.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filtrarSociosPorNombre(txtBuscarSocio.getText());
            }
        });

        // Panel de búsqueda con su etiqueta
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Panel para la búsqueda alineado a la izquierda
        JLabel labelBuscarSocio = new JLabel("BUSCAR SOCIO");
        panelBusqueda.add(labelBuscarSocio);  // Añadir la etiqueta "BUSCAR SOCIO"
        panelBusqueda.add(txtBuscarSocio);   // Añadir el campo de búsqueda

        // Le damos un pequeño margen a la derecha del campo de búsqueda
        panelBusqueda.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 5));  // Margen: arriba, izquierda, abajo, derecha (donde 15 es para separarlo de los botones)

        // Panel de botones donde se colocan los botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));  // Los botones centrados
        panelBotones.add(btnAgregar);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        // Panel que contiene tanto la búsqueda como los botones
        JPanel panelBusquedaBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));  // Alineamos los componentes al centro
        panelBusquedaBotones.add(panelBusqueda);  // Añadimos el panel de búsqueda al panel principal
        panelBusquedaBotones.add(panelBotones);   // Añadimos el panel de botones al panel principal

        // Añadir el panel completo (de búsqueda y botones) al panel principal
        add(panelCampos, BorderLayout.NORTH);
        add(scrollTabla, BorderLayout.CENTER);
        add(panelBusquedaBotones, BorderLayout.SOUTH);

        cargarSocios();

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
        dao.eliminarSocio(id);
        cargarSocios();
        limpiarCampos();
        JOptionPane.showMessageDialog(this, "Socio eliminado.");
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
