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

/**
 * Ventana de inicio de sesión para usuarios de la aplicación.
 * Permite al usuario introducir su DNI y contraseña, y autentica su acceso.
 *
 * @author Roberto Arcusa
 * @version 1.0
 * @since 2025
 */

public class LoginUsuario extends JFrame {
    private JTextField txtDni;
    private JPasswordField txtContrasena;
    private JButton btnLogin;
    private JButton btnVolverRegistro;

    /**
     * Constructor que inicializa y configura la interfaz gráfica
     * del formulario de inicio de sesión.
     */
    public LoginUsuario() {
        setTitle("Inicio de Sesión");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
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
        JLabel lblBienvenido = new JLabel("¡Bienvenido de nuevo!");
        lblBienvenido.setFont(new Font("Arial", Font.BOLD, 22));
        lblBienvenido.setForeground(azulOscuro);
        lblBienvenido.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelPrincipal.add(lblBienvenido);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 20, 5, 0);

        JLabel lblDni = new JLabel("DNI:");
        lblDni.setForeground(Color.WHITE);
        lblDni.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(lblDni, gbc);

        txtDni = new JTextField(20);
        configurarCampoTexto(txtDni, azulOscuro, fondoOscuro);
        gbc.gridx = 1;
        formPanel.add(txtDni, gbc);

        JLabel lblContrasena = new JLabel("Contraseña:");
        lblContrasena.setForeground(Color.WHITE);
        lblContrasena.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(lblContrasena, gbc);

        txtContrasena = new JPasswordField(20);
        configurarCampoTexto(txtContrasena, azulOscuro, fondoOscuro);
        gbc.gridx = 1;
        formPanel.add(txtContrasena, gbc);

        panelPrincipal.add(formPanel);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 20)));

        btnLogin = new JButton("INICIAR SESIÓN");
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setBackground(azulOscuro);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 18));
        btnLogin.setPreferredSize(new Dimension(240, 40));
        btnLogin.addActionListener(e -> autenticarUsuario());
        panelPrincipal.add(btnLogin);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 15)));

        btnVolverRegistro = new JButton("¿No tienes cuenta? Regístrate aquí");
        btnVolverRegistro.setBorderPainted(false);
        btnVolverRegistro.setContentAreaFilled(false);
        btnVolverRegistro.setForeground(azulOscuro);
        btnVolverRegistro.setFont(new Font("Arial", Font.PLAIN, 16));
        btnVolverRegistro.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnVolverRegistro.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnVolverRegistro.addActionListener(e -> volverARegistro());

        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 30)));
        panelPrincipal.add(btnVolverRegistro);

        contenedorCentrado.add(panelPrincipal);
    }

    /**
     * Configura el estilo visual de un campo de texto.
     *
     * @param campo      Campo de texto a configurar.
     * @param bordeColor Color del borde del campo.
     * @param fondo      Color de fondo del campo.
     */
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

    /**
     * Redirige al usuario a la ventana de registro de cuenta.
     * Se cierra la ventana actual.
     */
    private void volverARegistro() {
        SwingUtilities.invokeLater(() -> {
            new RegistroUsuario().setVisible(true);
            dispose();
        });
    }

    /**
     * Realiza el proceso de autenticación del usuario.
     * Verifica si los campos están completos, valida el DNI,
     * y comprueba las credenciales en la base de datos.
     */
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

    /**
     * Busca en la base de datos el usuario que corresponde al DNI ingresado.
     *
     * @param dni DNI del usuario a buscar.
     * @return Objeto Socio si existe el usuario, o null si no se encuentra.
     */
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

    /**
     * Abre la ventana principal de la aplicación tras un inicio de sesión exitoso
     * y cierra la ventana actual.
     */
    private void avanzarASiguientePantalla() {
        new VentanaPrincipal().setVisible(true);
        dispose();
    }
}
