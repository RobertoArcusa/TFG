package com.robertoarcusa.tfg.vistas;

import com.robertoarcusa.tfg.clases.Socio;
import com.robertoarcusa.tfg.util.DNIUtils;
import com.robertoarcusa.tfg.util.Seguridad;
import com.robertoarcusa.tfg.util.Sesion;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LoginUsuario extends JFrame {
    private JTextField txtDni;
    private JPasswordField txtContrasena;
    private JButton btnLogin;
    private JButton btnVolverRegistro;

    public LoginUsuario() {
        setTitle("Inicio de Sesión");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/com/robertoarcusa/tfg/resources/Icono_SportSystem.png"));
        Image icono = originalIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        setIconImage(icono);

        Color azulOscuro = new Color(0, 102, 204);
        Color fondoOscuro = new Color(28, 28, 28);

        getContentPane().setBackground(fondoOscuro);

        // Centramos el contenedor para evitar que se expanda verticalmente
        JPanel contenedorCentrado = new JPanel(new GridBagLayout());
        contenedorCentrado.setBackground(fondoOscuro);
        add(contenedorCentrado, BorderLayout.CENTER);

        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBackground(fondoOscuro);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        //panelPrincipal.setMaximumSize(new Dimension(500, 600)); // Limita tamaño

        // Ajustamos fuente de los labels (DNI y Contraseña)
        Font labelFont = new Font("Arial", Font.PLAIN, 18);

        // LOGO
        JLabel logo = new JLabel();
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        ImageIcon imagenOriginal = new ImageIcon(getClass().getResource("/com/robertoarcusa/tfg/resources/logo_inicio.png"));
        Image imagenEscalada = imagenOriginal.getImage().getScaledInstance(500, 270, Image.SCALE_SMOOTH);
        logo.setIcon(new ImageIcon(imagenEscalada));
        panelPrincipal.add(logo);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 10)));

        // TÍTULO
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 20))); // Añadimos margen arriba
        JLabel lblBienvenido = new JLabel("¡Bienvenido de nuevo!");
        lblBienvenido.setFont(new Font("Arial", Font.BOLD, 22));
        lblBienvenido.setForeground(azulOscuro);
        lblBienvenido.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelPrincipal.add(lblBienvenido);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 20))); // Añadimos margen abajo

        // CAMPOS DE LOGIN (con GridBagLayout)
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST; // Alineación hacia la izquierda
        gbc.insets = new Insets(5, 20, 5, 0); // Espacio entre los elementos

        JLabel lblDni = new JLabel("DNI:");
        lblDni.setForeground(Color.WHITE);
        lblDni.setFont(labelFont);
        gbc.gridx = 0; // Columna 0
        gbc.gridy = 0; // Fila 0
        formPanel.add(lblDni, gbc);

        txtDni = new JTextField(20);
        txtDni.setFont(new Font("Arial", Font.PLAIN, 16)); // Fuente de tamaño 16px para el campo de texto
        txtDni.setBackground(fondoOscuro); // Establecer fondo
        txtDni.setForeground(Color.WHITE); // Establecer texto en color blanco
        txtDni.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(azulOscuro, 2), // Borde azul
                BorderFactory.createEmptyBorder(5, 10, 5, 10) // Margen interno (5px arriba y abajo, 10px a la izquierda y derecha)
        ));
        txtDni.setPreferredSize(new Dimension(250, 40)); // Aumentar altura de la caja de texto (por ejemplo, 40px)
        txtDni.setCaretColor(Color.WHITE); // Hacer que el cursor sea blanco

        gbc.gridx = 1; // Columna 1
        formPanel.add(txtDni, gbc);

        JLabel lblContrasena = new JLabel("Contraseña:");
        lblContrasena.setForeground(Color.WHITE);
        lblContrasena.setFont(labelFont); // Aplica la fuente al label de Contraseña
        gbc.gridx = 0; // Columna 0
        gbc.gridy = 1; // Fila 1
        formPanel.add(lblContrasena, gbc);

        txtContrasena = new JPasswordField(20);
        txtContrasena.setFont(new Font("Arial", Font.PLAIN, 16)); // Fuente de tamaño 16px para el campo de texto
        txtContrasena.setBackground(fondoOscuro); // Establecer fondo
        txtContrasena.setForeground(Color.WHITE); // Establecer texto en color blanco
        txtContrasena.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(azulOscuro, 2), // Borde azul
                BorderFactory.createEmptyBorder(5, 10, 5, 10) // Margen interno (5px arriba y abajo, 10px a la izquierda y derecha)
        ));
        txtContrasena.setPreferredSize(new Dimension(250, 40)); // Aumentar altura de la caja de texto (por ejemplo, 40px)
        txtContrasena.setCaretColor(Color.WHITE); // Hacer que el cursor sea blanco

        gbc.gridx = 1; // Columna 1
        formPanel.add(txtContrasena, gbc);

        panelPrincipal.add(formPanel);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 20))); // Espacio entre formulario y botones

        // BOTÓN LOGIN
        btnLogin = new JButton("INICIAR SESIÓN");
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setBackground(azulOscuro);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 18));  // Fuente más grande (18px)
        btnLogin.setPreferredSize(new Dimension(240, 40));  // Tamaño preferido del botón (250px de ancho y 50px de alto)
        btnLogin.addActionListener(e -> autenticarUsuario());
        panelPrincipal.add(btnLogin);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 15)));


        // BOTÓN REGISTRO
        btnVolverRegistro = new JButton("¿No tienes cuenta? Regístrate aquí");
        btnVolverRegistro.setBorderPainted(false);
        btnVolverRegistro.setContentAreaFilled(false);
        btnVolverRegistro.setForeground(azulOscuro);

        // Aquí aumentamos el tamaño de la fuente (por ejemplo, de 13px a 16px)
        btnVolverRegistro.setFont(new Font("Arial", Font.PLAIN, 16));

        btnVolverRegistro.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnVolverRegistro.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnVolverRegistro.addActionListener(e -> volverARegistro());

        // Añadir espacio antes del botón para que esté más abajo
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 30))); // Espacio superior al botón

        panelPrincipal.add(btnVolverRegistro);

        // Agrega el panel principal al contenedor centrado
        contenedorCentrado.add(panelPrincipal);
    }

    private void volverARegistro() {
        SwingUtilities.invokeLater(() -> {
            new RegistroUsuario().setVisible(true);
            dispose();
        });
    }

    private void autenticarUsuario() {
        String dni = txtDni.getText().trim();
        String contrasena = new String(txtContrasena.getPassword());

        if (dni.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, rellena todos los campos.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!DNIUtils.esDNIValido(dni)) {
            JOptionPane.showMessageDialog(this, "El DNI ingresado no es válido.", "Error de autenticación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Socio socio = buscarUsuarioPorDNI(dni);
        if (socio == null) {
            JOptionPane.showMessageDialog(this, "El DNI introducido no existe.", "Error de autenticación", JOptionPane.ERROR_MESSAGE);
        } else {
            if (!Seguridad.verificar(contrasena, socio.getContrasena(), dni)) {
                JOptionPane.showMessageDialog(this, "Contraseña incorrecta.", "Error de autenticación", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "¡Inicio de sesión exitoso!", "Bienvenido", JOptionPane.INFORMATION_MESSAGE);
                Sesion.setUsuarioActual(socio);
                avanzarASiguientePantalla();
            }
        }
    }

    private Socio buscarUsuarioPorDNI(String dni) {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        Socio socio = null;

        try {
            session.beginTransaction();
            Query<Socio> query = session.createQuery("FROM Socio WHERE dni = :dni", Socio.class);
            query.setParameter("dni", dni);
            List<Socio> resultados = query.list();
            if (!resultados.isEmpty()) {
                socio = resultados.get(0);
            }
            session.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ha ocurrido un error al buscar el usuario.", "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            session.close();
            sessionFactory.close();
        }

        return socio;
    }

    private void avanzarASiguientePantalla() {
        new VentanaPrincipal().setVisible(true);
        dispose();
    }
}
