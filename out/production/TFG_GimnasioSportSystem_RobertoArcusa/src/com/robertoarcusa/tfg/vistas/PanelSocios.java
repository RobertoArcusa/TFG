package com.robertoarcusa.tfg.vistas;

import com.robertoarcusa.tfg.clases.Socio;
import com.robertoarcusa.tfg.dao.SocioDAO;
import com.robertoarcusa.tfg.enums.TipoMembresia;
import com.robertoarcusa.tfg.enums.TipoUsuario;
import com.robertoarcusa.tfg.util.DNIUtils;
import com.robertoarcusa.tfg.util.Seguridad;
import com.robertoarcusa.tfg.util.Sesion;
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
import com.toedter.calendar.JDateChooser;

/**
 * Panel para la gestión de socios en la interfaz gráfica.
 * Permite visualizar, agregar, modificar, eliminar y buscar socios,
 * así como cargar y mostrar la foto de perfil de cada socio.
 *
 * Contiene un formulario con campos para nombre, apellidos, DNI, teléfono,
 * contraseña, fecha de nacimiento, tipo de membresía y tipo de usuario.
 * Además, incluye una tabla que muestra la lista de socios registrados.
 *
 * La clase se basa en Swing y utiliza un modelo de tabla para gestionar
 * la visualización de los datos.
 *
 * @author Roberto Arcusa
 * @version 1.0
 * @since 2025
 */

public class PanelSocios extends JPanel {

    private JTable tablaSocios;
    private DefaultTableModel modeloTabla;
    private JTextField txtNombre, txtApellidos, txtDni, txtTelefono, txtContrasena;
    private JComboBox<TipoMembresia> comboMembresia;
    private JComboBox<TipoUsuario> comboTipoUsuario;
    private JButton btnModificarSocio, btnAgregarSocio, btnEliminarSocio, btnLimpiarCampos;
    private JDateChooser dateChooser;
    private JLabel lblFoto;
    private byte[] fotoPerfilSeleccionada;
    private JTextField txtBuscarSocio;

    /**
     * Constructor que inicializa el panel con todos sus componentes,
     * configura el diseño, establece eventos y carga la lista inicial de socios.
     */
    public PanelSocios(Sesion sesion) {
        setLayout(new BorderLayout(10, 10)); // Pequeños gaps

        // PANEL CAMPOS CON GRIDBAGLAYOUT
        JPanel panelCampos = new JPanel(new GridBagLayout());
        panelCampos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.NORTHWEST;

        // --- COLUMNA 1 ---
        JPanel columna1 = new JPanel(new GridBagLayout());
        GridBagConstraints c1 = new GridBagConstraints();
        c1.insets = new Insets(5, 5, 5, 5);
        c1.fill = GridBagConstraints.HORIZONTAL;
        c1.weightx = 1.0;

        c1.gridx = 0; c1.gridy = 0;
        columna1.add(new JLabel("NOMBRE:"), c1);
        c1.gridx = 1;
        txtNombre = new JTextField();
        txtNombre.setPreferredSize(new Dimension(350, 40));
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
        dateChooser = new JDateChooser();
        dateChooser.setPreferredSize(new Dimension(350, 40));
        dateChooser.setDateFormatString("dd/MM/yyyy");
        columna2.add(dateChooser, c2);

        c2.gridx = 0; c2.gridy++;
        columna2.add(new JLabel("TIPO USUARIO:"), c2);
        c2.gridx = 1;
        comboTipoUsuario = new JComboBox<>(TipoUsuario.values());
        comboTipoUsuario.setPreferredSize(new Dimension(350, 40));
        if (sesion.getUsuarioActual() != null && sesion.getUsuarioActual().getTipoUsuario() == TipoUsuario.EDITOR) {
            comboTipoUsuario.setEnabled(false);
            comboTipoUsuario.setSelectedIndex(-1);
        }
        columna2.add(comboTipoUsuario, c2);

        c2.gridx = 0; c2.gridy++;
        columna2.add(new JLabel("TIPO MEMBRESÍA:"), c2);
        c2.gridx = 1;
        comboMembresia = new JComboBox<>(TipoMembresia.values());
        comboMembresia.setPreferredSize(new Dimension(350, 40));
        columna2.add(comboMembresia, c2);

        // --- COLUMNA 3: FOTO ---
        JPanel columna3 = new JPanel(new BorderLayout(5,5));
        JLabel lblTituloFoto = new JLabel("FOTO SOCIO", SwingConstants.CENTER);
        columna3.add(lblTituloFoto, BorderLayout.NORTH);
        lblFoto = new JLabel("Sin foto", SwingConstants.CENTER);
        lblFoto.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        columna3.add(lblFoto, BorderLayout.CENTER);

        JPanel panelBotonesFoto = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        JButton btnCargarFoto = new JButton("CARGAR FOTO");
        btnCargarFoto.addActionListener(e -> cargarFoto());
        JButton btnEliminarFoto = new JButton("ELIMINAR FOTO");
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
        gbc.weightx = 1.0;
        panelCampos.add(columna1, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panelCampos.add(columna2, gbc);

        gbc.gridx = 2;
        gbc.weightx = 1.0;
        panelCampos.add(columna3, gbc);

        // BUSCADOR Y BOTONES
        txtBuscarSocio = new JTextField(20);
        setTextFieldHint(txtBuscarSocio, "Introduce un nombre de socio");
        txtBuscarSocio.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filtrarSociosPorNombre(txtBuscarSocio.getText());
            }
        });

        btnAgregarSocio = new JButton("AÑADIR SOCIO");
        btnModificarSocio = new JButton("MODIFICAR SOCIO");
        btnEliminarSocio = new JButton("ELIMINAR SOCIO");
        btnLimpiarCampos = new JButton("LIMPIAR CAMPOS");

        // Ajustar tamaño preferido para hacer botones un poco más grandes
        Dimension tamañoOriginal = btnAgregarSocio.getPreferredSize();
        Dimension tamañoNuevo = new Dimension(tamañoOriginal.width + 35, tamañoOriginal.height + 10);

        btnAgregarSocio.setPreferredSize(tamañoNuevo);
        btnModificarSocio.setPreferredSize(tamañoNuevo);
        btnEliminarSocio.setPreferredSize(tamañoNuevo);
        btnLimpiarCampos.setPreferredSize(tamañoNuevo);

        btnAgregarSocio.addActionListener(e -> agregarNuevoSocio());
        btnModificarSocio.addActionListener(e -> modificarSocio());
        btnEliminarSocio.addActionListener(e -> eliminarSocio());
        btnLimpiarCampos.addActionListener(e -> limpiarCampos());

        JPanel panelBusquedaBotones = new JPanel(new BorderLayout());

        // Panel para botones centrados arriba
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        panelBotones.add(btnAgregarSocio);
        panelBotones.add(btnModificarSocio);
        panelBotones.add(btnEliminarSocio);
        panelBotones.add(btnLimpiarCampos);

        // Panel para búsqueda alineada a la izquierda abajo
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBusqueda.add(new JLabel("BUSCAR SOCIO"));
        panelBusqueda.add(txtBuscarSocio);

        panelBusquedaBotones.add(panelBotones, BorderLayout.CENTER);
        panelBusquedaBotones.add(panelBusqueda, BorderLayout.SOUTH);

        // TABLA Y MODELO
        modeloTabla = new DefaultTableModel(new String[]{"ID", "NOMBRE", "APELLIDOS", "DNI", "TELÉFONO", "FECHA NACIMIENTO", "TIPO MEMBRESÍA", "TIPO USUARIO"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaSocios = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaSocios);

        // Añadir a layout principal
        JPanel panelArriba = new JPanel(new BorderLayout());
        panelArriba.add(panelCampos, BorderLayout.CENTER);
        panelArriba.add(panelBusquedaBotones, BorderLayout.SOUTH);

        add(panelArriba, BorderLayout.NORTH);
        add(scrollTabla, BorderLayout.CENTER);

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

    /**
     * Filtra y muestra en la tabla los socios cuyo nombre contiene el texto dado.
     *
     * @param filtro Texto usado para filtrar los nombres de los socios.
     */
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

    /**
     * Carga los datos de un socio identificado por su ID en los campos del formulario.
     * También carga la foto de perfil si está disponible.
     *
     * @param id ID del socio cuyos datos se cargarán.
     */
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
                java.util.Date fecha = socio.getFechaNacimiento();
                dateChooser.setDate(fecha);
            } else {
                dateChooser.setDate(null);
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

    /**
     * Agrega un nuevo socio a la base de datos con los datos introducidos en el formulario.
     * Realiza validaciones básicas, como evitar duplicados de DNI.
     */
    private void agregarNuevoSocio() {
        if (!tablaSocios.getSelectionModel().isSelectionEmpty()) return;

        // Validar campos obligatorios
        if (txtNombre.getText().trim().isEmpty() ||
                txtApellidos.getText().trim().isEmpty() ||
                txtDni.getText().trim().isEmpty() ||
                txtContrasena.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, complete todos los campos obligatorios:\n- Nombre\n- Apellidos\n- DNI\n- Contraseña",
                    "Campos obligatorios",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validar formato del DNI
        if (!DNIUtils.esDNIValido(txtDni.getText().trim())) {
            JOptionPane.showMessageDialog(this,
                    "El DNI introducido no es válido. Asegúrese de que tiene un formato correcto.",
                    "DNI inválido",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        SocioDAO dao = new SocioDAO();

        if (dao.existeDni(txtDni.getText().trim())) {
            JOptionPane.showMessageDialog(this,
                    "Ya existe un socio con ese DNI.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Socio nuevo = new Socio();
        llenarDatosDesdeFormulario(nuevo);

        if (nuevo.getTipoUsuario() == null) {
            nuevo.setTipoUsuario(TipoUsuario.BASIC);
        }

        dao.agregarSocio(nuevo);
        limpiarCampos();
        cargarSocios();
        JOptionPane.showMessageDialog(this, "Socio añadido.");
    }


    /**
     * Modifica los datos del socio actualmente seleccionado en la tabla,
     * actualizando la información con los datos del formulario.
     */
    private void modificarSocio() {
        int fila = tablaSocios.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un socio.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validar el DNI antes de modificar
        String dni = txtDni.getText().trim();
        if (!DNIUtils.esDNIValido(dni)) {
            JOptionPane.showMessageDialog(this,
                    "El DNI introducido no es válido. Asegúrese de que tiene un formato correcto.",
                    "DNI inválido",
                    JOptionPane.ERROR_MESSAGE);
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

    /**
     * Elimina el socio actualmente seleccionado en la tabla,
     * previa confirmación del usuario.
     */
    private void eliminarSocio() {
        int fila = tablaSocios.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un socio para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Confirmar eliminación
        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Estás seguro de que quieres eliminar este socio?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (opcion == JOptionPane.YES_OPTION) {
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
    }

    /**
     * Llena un objeto Socio con los datos actuales del formulario,
     * incluyendo el hash de la contraseña y la foto seleccionada.
     *
     * @param socio Instancia de Socio a llenar con los datos del formulario.
     */
    private void llenarDatosDesdeFormulario(Socio socio) {
        socio.setNombreSocio(txtNombre.getText());
        socio.setApellidosSocio(txtApellidos.getText());
        socio.setDni(txtDni.getText());
        socio.setTelefono(txtTelefono.getText());
        socio.setContrasena(Seguridad.hashear(txtContrasena.getText()));
        socio.setTipoMembresia((TipoMembresia) comboMembresia.getSelectedItem());
        socio.setTipoUsuario((TipoUsuario) comboTipoUsuario.getSelectedItem());
        socio.setFotoPerfil(fotoPerfilSeleccionada);

        if (dateChooser.getDate() != null) {
            java.util.Date selectedDate = dateChooser.getDate();
            socio.setFechaNacimiento(new java.sql.Date(selectedDate.getTime()));
        }

    }

    /**
     * Carga todos los socios desde la base de datos y los muestra en la tabla.
     */
    private void cargarSocios() {
        modeloTabla.setRowCount(0);
        SocioDAO dao = new SocioDAO();
        for (Socio s : dao.obtenerTodosLosSocios()) {
            modeloTabla.addRow(new Object[]{s.getIdSocio(), s.getNombreSocio(), s.getApellidosSocio(), s.getDni(), s.getTelefono(), s.getFechaNacimiento(), s.getTipoMembresia(), s.getTipoUsuario()});
        }
    }

    /**
     * Abre un diálogo para seleccionar una foto desde el sistema de archivos,
     * la carga, redimensiona y muestra en el panel correspondiente.
     */
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

    /**
     * Limpia todos los campos del formulario, la selección de la tabla,
     * y restablece el estado visual inicial (incluida la imagen predeterminada).
     */
    public void limpiarCampos() {
        txtNombre.setText("");
        txtApellidos.setText("");
        txtDni.setText("");
        txtTelefono.setText("");
        txtContrasena.setText("");
        comboMembresia.setSelectedIndex(-1);
        comboTipoUsuario.setSelectedIndex(-1);
        dateChooser.setDate(null);
        fotoPerfilSeleccionada = null;
        tablaSocios.clearSelection();
        lblFoto.setIcon(null);
        lblFoto.setText("Sin foto");
    }

    /**
     * Añade un texto de sugerencia (hint) a un JTextField que desaparece
     * cuando el campo recibe foco y vuelve a aparecer si está vacío al perder foco.
     *
     * @param textField El campo de texto al que se le aplicará el hint.
     * @param hint Texto de sugerencia a mostrar.
     */
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
