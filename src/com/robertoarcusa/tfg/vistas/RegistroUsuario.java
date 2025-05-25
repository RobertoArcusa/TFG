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
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Abrir maximizada
        setMinimumSize(new Dimension(1024, 768));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/com/robertoarcusa/tfg/resources/Icono_SportSystem.png"));
        Image icono = originalIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        setIconImage(icono);

        Color azulOscuro = new Color(0, 102, 204);
        Color fondoOscuro = new Color(28, 28, 28);

        getContentPane().setBackground(fondoOscuro);

        JPanel contenedorCentrado = new JPanel(new GridBagLayout());
        contenedorCentrado.setBackground(fondoOscuro);
        add(contenedorCentrado, BorderLayout.CENTER);

        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBackground(fondoOscuro);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        Font labelFont = new Font("Arial", Font.PLAIN, 18);

        JLabel logo = new JLabel();
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        ImageIcon imagenOriginal = new ImageIcon(getClass().getResource("/com/robertoarcusa/tfg/resources/logo_inicio.png"));
        Image imagenEscalada = imagenOriginal.getImage().getScaledInstance(480, 240, Image.SCALE_SMOOTH);
        logo.setIcon(new ImageIcon(imagenEscalada));
        panelPrincipal.add(logo);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 10)));

        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 20)));
        JLabel lblBienvenido = new JLabel("¡Regístrate para continuar!");
        lblBienvenido.setFont(new Font("Arial", Font.BOLD, 22));
        lblBienvenido.setForeground(azulOscuro);
        lblBienvenido.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelPrincipal.add(lblBienvenido);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);

        txtNombre = crearCampoFormulario(formPanel, "Nombre:", labelFont, azulOscuro, fondoOscuro);
        txtApellidos = crearCampoFormulario(formPanel, "Apellidos:", labelFont, azulOscuro, fondoOscuro);
        txtDni = crearCampoFormulario(formPanel, "DNI:", labelFont, azulOscuro, fondoOscuro);
        txtContrasena = new JPasswordField(20);
        configurarCampoTexto(txtContrasena, azulOscuro, fondoOscuro);
        agregarFilaFormulario(formPanel, "Contraseña:", txtContrasena, labelFont, azulOscuro);

        panelPrincipal.add(formPanel);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 20)));

        btnRegistrar = new JButton("REGÍSTRATE");
        btnRegistrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRegistrar.setBackground(azulOscuro);
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setFont(new Font("Arial", Font.BOLD, 18));
        btnRegistrar.setPreferredSize(new Dimension(240, 40));
        btnRegistrar.addActionListener(this::registrarSocio);
        panelPrincipal.add(btnRegistrar);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 15)));

        btnIniciarSesion = new JButton("¿Ya tienes cuenta? Inicia sesión aquí");
        btnIniciarSesion.setBorderPainted(false);
        btnIniciarSesion.setContentAreaFilled(false);
        btnIniciarSesion.setForeground(azulOscuro);
        btnIniciarSesion.setFont(new Font("Arial", Font.PLAIN, 16));
        btnIniciarSesion.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnIniciarSesion.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnIniciarSesion.addActionListener(e -> iniciarSesion());

        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 30)));
        panelPrincipal.add(btnIniciarSesion);

        contenedorCentrado.add(panelPrincipal);
    }

    private JTextField crearCampoFormulario(JPanel formPanel, String labelText, Font font, Color bordeColor, Color fondo) {
        JTextField campo = new JTextField(20);
        configurarCampoTexto(campo, bordeColor, fondo);
        agregarFilaFormulario(formPanel, labelText, campo, font, bordeColor);
        return campo;
    }

    private void configurarCampoTexto(JTextField campo, Color bordeColor, Color fondo) {
        campo.setFont(new Font("Arial", Font.PLAIN, 16));
        campo.setBackground(fondo);
        campo.setForeground(Color.WHITE);
        campo.setCaretColor(Color.WHITE);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bordeColor, 2),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        campo.setPreferredSize(new Dimension(250, 40));
    }

    private void agregarFilaFormulario(JPanel formPanel, String labelText, JComponent campo, Font font, Color colorTexto) {
        JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fila.setOpaque(false);
        JLabel label = new JLabel(labelText);
        label.setForeground(Color.WHITE);
        label.setFont(font);
        label.setPreferredSize(new Dimension(120, 30));
        fila.add(label);
        fila.add(campo);
        formPanel.add(fila);
    }

    private void registrarSocio(ActionEvent e) {
        String nombre = txtNombre.getText().trim();
        String apellidos = txtApellidos.getText().trim();
        String dni = txtDni.getText().trim().toUpperCase();
        String contrasena = new String(txtContrasena.getPassword());

        if (nombre.isEmpty() || apellidos.isEmpty() || dni.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!DNIUtils.esDNIValido(dni)) {
            JOptionPane.showMessageDialog(this, "El DNI introducido no es válido.", "DNI inválido", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (dniExiste(dni)) {
            JOptionPane.showMessageDialog(this, "Ya existe un usuario registrado con ese DNI.", "DNI duplicado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Socio socio = new Socio();
        socio.setNombreSocio(nombre);
        socio.setApellidosSocio(apellidos);
        socio.setDni(dni);
        socio.setContrasena(Seguridad.hashear(contrasena));
        socio.setTipoUsuario(TipoUsuario.BASIC);

        if (guardarSocio(socio)) {
            JOptionPane.showMessageDialog(this, "¡Usuario registrado con éxito!", "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
            iniciarSesion();
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