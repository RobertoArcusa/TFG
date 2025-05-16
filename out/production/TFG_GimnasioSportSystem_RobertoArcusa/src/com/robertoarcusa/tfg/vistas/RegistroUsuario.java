package com.robertoarcusa.tfg.vistas;

import com.robertoarcusa.tfg.clases.Socio;
import com.robertoarcusa.tfg.enums.TipoUsuario;
import com.robertoarcusa.tfg.util.DNIUtils;
import com.robertoarcusa.tfg.util.HibernateUtil;
import com.robertoarcusa.tfg.util.Seguridad;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class RegistroUsuario extends JFrame {
    private JTextField txtNombre;
    private JTextField txtApellidos;
    private JTextField txtDni;
    private JPasswordField txtContrasena;
    private JButton btnRegistrar;
    private JButton btnIniciarSesion;

    public RegistroUsuario() {
        setTitle("Registro de Usuario");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/com/robertoarcusa/tfg/resources/Icono_SportSystem.png"));
        Image icono = originalIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        setIconImage(icono);

        // Colores y fondo
        Color azulOscuro = new Color(0, 102, 204);
        Color fondoOscuro = new Color(28, 28, 28);

        getContentPane().setBackground(fondoOscuro);

        // Contenedor centrado
        JPanel contenedorCentrado = new JPanel(new GridBagLayout());
        contenedorCentrado.setBackground(fondoOscuro);
        add(contenedorCentrado, BorderLayout.CENTER);

        // Panel principal con BoxLayout
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBackground(fondoOscuro);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        //panelPrincipal.setMaximumSize(new Dimension(500, 600)); // Limita tamaño

        // Fuente para los labels
        Font labelFont = new Font("Arial", Font.PLAIN, 18);  // Fuente más grande (18px)

        // LOGO
        JLabel logo = new JLabel();
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        ImageIcon imagenOriginal = new ImageIcon(getClass().getResource("/com/robertoarcusa/tfg/resources/logo_inicio.png"));
        Image imagenEscalada = imagenOriginal.getImage().getScaledInstance(500, 260, Image.SCALE_SMOOTH);
        logo.setIcon(new ImageIcon(imagenEscalada));
        panelPrincipal.add(logo);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 10)));  // Espacio después del logo

        // TÍTULO
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 20)));  // Margen arriba
        JLabel lblBienvenido = new JLabel("¡Regístrate para continuar!");
        lblBienvenido.setFont(new Font("Arial", Font.BOLD, 22));
        lblBienvenido.setForeground(azulOscuro);
        lblBienvenido.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelPrincipal.add(lblBienvenido);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 20)));  // Margen abajo

        // CAMPOS DE REGISTRO (con GridBagLayout)
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 20, 5, 0);  // Espacio entre los elementos (5px arriba y abajo)

        // NOMBRE
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setForeground(Color.WHITE);
        lblNombre.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(lblNombre, gbc);

        txtNombre = new JTextField(20);
        txtNombre.setFont(new Font("Arial", Font.PLAIN, 16));
        txtNombre.setBackground(fondoOscuro); // Establecer fondo
        txtNombre.setForeground(Color.WHITE); // Establecer texto en color blanco
        txtNombre.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(azulOscuro, 2), // Borde azul
                BorderFactory.createEmptyBorder(5, 10, 5, 10) // Margen interno (5px arriba y abajo, 10px a la izquierda y derecha)
        ));
        txtNombre.setPreferredSize(new Dimension(250, 40)); // Aumentar altura de la caja de texto (por ejemplo, 40px)
        txtNombre.setCaretColor(Color.WHITE); // Hacer que el cursor sea blanco

        gbc.gridx = 1;
        formPanel.add(txtNombre, gbc);

        // APELLIDOS
        JLabel lblApellidos = new JLabel("Apellidos:");
        lblApellidos.setForeground(Color.WHITE);
        lblApellidos.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(lblApellidos, gbc);

        txtApellidos = new JTextField(20);
        txtApellidos.setFont(new Font("Arial", Font.PLAIN, 16));
        txtApellidos.setBackground(fondoOscuro); // Establecer fondo
        txtApellidos.setForeground(Color.WHITE); // Establecer texto en color blanco
        txtApellidos.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(azulOscuro, 2), // Borde azul
                BorderFactory.createEmptyBorder(5, 10, 5, 10) // Margen interno (5px arriba y abajo, 10px a la izquierda y derecha)
        ));
        txtApellidos.setPreferredSize(new Dimension(250, 40)); // Aumentar altura de la caja de texto
        txtApellidos.setCaretColor(Color.WHITE); // Hacer que el cursor sea blanco

        gbc.gridx = 1;
        formPanel.add(txtApellidos, gbc);

        // DNI
        JLabel lblDni = new JLabel("DNI:");
        lblDni.setForeground(Color.WHITE);
        lblDni.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(lblDni, gbc);

        txtDni = new JTextField(20);
        txtDni.setFont(new Font("Arial", Font.PLAIN, 16));
        txtDni.setBackground(fondoOscuro); // Establecer fondo
        txtDni.setForeground(Color.WHITE); // Establecer texto en color blanco
        txtDni.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(azulOscuro, 2), // Borde azul
                BorderFactory.createEmptyBorder(5, 10, 5, 10) // Margen interno (5px arriba y abajo, 10px a la izquierda y derecha)
        ));
        txtDni.setPreferredSize(new Dimension(250, 40)); // Aumentar altura de la caja de texto
        txtDni.setCaretColor(Color.WHITE); // Hacer que el cursor sea blanco

        gbc.gridx = 1;
        formPanel.add(txtDni, gbc);

        // CONTRASEÑA
        JLabel lblContrasena = new JLabel("Contraseña:");
        lblContrasena.setForeground(Color.WHITE);
        lblContrasena.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(lblContrasena, gbc);

        txtContrasena = new JPasswordField(20);
        txtContrasena.setFont(new Font("Arial", Font.PLAIN, 16));
        txtContrasena.setBackground(fondoOscuro); // Establecer fondo
        txtContrasena.setForeground(Color.WHITE); // Establecer texto en color blanco
        txtContrasena.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(azulOscuro, 2), // Borde azul
                BorderFactory.createEmptyBorder(5, 10, 5, 10) // Margen interno (5px arriba y abajo, 10px a la izquierda y derecha)
        ));
        txtContrasena.setPreferredSize(new Dimension(250, 40)); // Aumentar altura de la caja de texto
        txtContrasena.setCaretColor(Color.WHITE); // Hacer que el cursor sea blanco

        gbc.gridx = 1;
        formPanel.add(txtContrasena, gbc);

        panelPrincipal.add(formPanel);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 20)));  // Espacio entre formulario y botones

        // BOTÓN REGISTRARSE
        btnRegistrar = new JButton("REGÍSTRATE");
        btnRegistrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRegistrar.setBackground(azulOscuro);
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setFont(new Font("Arial", Font.BOLD, 18));  // Fuente más grande (18px)
        btnRegistrar.setPreferredSize(new Dimension(240, 40));  // Tamaño preferido del botón
        btnRegistrar.addActionListener(this::registrarSocio);
        panelPrincipal.add(btnRegistrar);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 15)));

        // BOTÓN VOLVER AL LOGIN
        btnIniciarSesion = new JButton("¿Ya tienes cuenta? Inicia sesión aquí");
        btnIniciarSesion.setBorderPainted(false);
        btnIniciarSesion.setContentAreaFilled(false);
        btnIniciarSesion.setForeground(azulOscuro);
        btnIniciarSesion.setFont(new Font("Arial", Font.PLAIN, 16));
        btnIniciarSesion.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnIniciarSesion.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnIniciarSesion.addActionListener(e -> iniciarSesion());

        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 30)));  // Espacio antes del botón
        panelPrincipal.add(btnIniciarSesion);

        // Agregamos el panel principal al contenedor centrado
        contenedorCentrado.add(panelPrincipal);
    }


    private void registrarSocio(ActionEvent e) {
        String nombre = txtNombre.getText().trim();
        String apellidos = txtApellidos.getText().trim();
        String dni = txtDni.getText().trim().toUpperCase();  // Asegúrate de convertirlo a mayúsculas aquí
        String contrasena = new String(txtContrasena.getPassword());

        // Validar que los campos no estén vacíos
        if (nombre.isEmpty() || apellidos.isEmpty() || dni.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar que el DNI tenga el formato correcto
        if (!DNIUtils.esDNIValido(dni)) {
            JOptionPane.showMessageDialog(this, "El DNI introducido no es válido.", "DNI inválido", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Comprobar si ya existe un usuario con el mismo DNI
        if (dniExiste(dni)) {
            JOptionPane.showMessageDialog(this, "Ya existe un usuario registrado con ese DNI.", "DNI duplicado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Crear nuevo Socio
        Socio socio = new Socio();
        socio.setNombreSocio(nombre);
        socio.setApellidosSocio(apellidos);
        socio.setDni(dni);
        socio.setContrasena(Seguridad.hashear(contrasena)); // Asegúrate de hashear la contraseña
        socio.setTipoUsuario(TipoUsuario.BASIC);

        // Guardar el socio en la base de datos
        if (guardarSocio(socio)) {
            JOptionPane.showMessageDialog(this, "¡Usuario registrado con éxito!", "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
        } else {
            JOptionPane.showMessageDialog(this, "Hubo un error al registrar el usuario.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private boolean dniExiste(String dni) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query<Long> query = session.createQuery("SELECT COUNT(*) FROM Socio WHERE dni = :dni", Long.class);
            query.setParameter("dni", dni);
            return query.uniqueResult() > 0;
        } finally {
            session.close();
        }
    }

    private boolean guardarSocio(Socio socio) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            session.save(socio);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            if (session.getTransaction() != null) session.getTransaction().rollback();
            return false;
        } finally {
            session.close();
        }
    }

    private void limpiarFormulario() {
        txtNombre.setText("");
        txtApellidos.setText("");
        txtDni.setText("");
        txtContrasena.setText("");
    }

    private void iniciarSesion() {
        SwingUtilities.invokeLater(() -> {
            new LoginUsuario().setVisible(true);
            dispose();
        });
    }
}
