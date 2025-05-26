package com.robertoarcusa.tfg.vistas;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.robertoarcusa.tfg.clases.Socio;
import com.robertoarcusa.tfg.enums.TipoUsuario;
import com.robertoarcusa.tfg.util.Sesion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.*;
import java.net.URL;
import java.util.Properties;

import javax.swing.border.EmptyBorder; // Importar para usar EmptyBorder

/**
 * Clase que representa la ventana principal de la aplicación SportSystem.
 * Esta ventana actúa como el panel de administración principal, con múltiples pestañas para
 * gestionar socios, entrenadores, clases, sesiones, inscripciones, pagos e informes.
 * <p>
 * Además, permite alternar entre modo oscuro y modo claro, guardando esta preferencia
 * en un archivo de configuración para mantenerla entre sesiones.
 * <p>
 * El acceso a ciertas pestañas se limita según el tipo de usuario (por ejemplo, usuarios BASIC tienen pestañas restringidas).
 *
 * @author Roberto Arcusa
 * @version 1.0
 * @since 2025
 */

public class VentanaPrincipal extends JFrame {

    private JTabbedPane tabbedPane;
    private JMenuBar menuBar;
    private JMenu opcionesMenu;
    private JMenuItem cambiarModoMenuItem;
    private JMenuItem salirMenuItem;
    private JLabel logoLabel;
    private boolean modoOscuro;

    /**
     * Constructor que inicializa la ventana principal, configurando el modo de apariencia,
     * creando la barra de menú, configurando las pestañas funcionales y ajustando la interfaz.
     */
    public VentanaPrincipal() {
        // Cargar el modo por defecto desde el archivo de configuración
        modoOscuro = cargarModo();  // Cargar la configuración del modo (oscuro o claro)

        try {
            if (modoOscuro) {
                UIManager.setLookAndFeel(new FlatDarkLaf());
            } else {
                UIManager.setLookAndFeel(new FlatLightLaf());
            }
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // Configuración de la ventana principal
        setTitle("Panel de administración - SportSystem");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1024, 768));

        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/com/robertoarcusa/tfg/resources/Icono_SportSystem.png"));
        Image icono = originalIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        setIconImage(icono);

        // Crear el menú
        menuBar = new JMenuBar();

        // Crear el menú de opciones
        opcionesMenu = new JMenu("Opciones");

        // Crear el item para cambiar entre modo claro y oscuro
        cambiarModoMenuItem = new JMenuItem(modoOscuro ? "Cambiar a modo claro" : "Cambiar a modo oscuro");
        cambiarModoMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (modoOscuro) {
                    aplicarModoClaro();
                } else {
                    aplicarModoOscuro();
                }
            }
        });

        // Crear el item de salir
        salirMenuItem = new JMenuItem("Salir");
        salirMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Cerrar la ventana actual y abrir la pantalla de LoginUsuario
                abrirLoginUsuario();
            }
        });

        // Añadir los items al menú
        opcionesMenu.add(cambiarModoMenuItem);
        opcionesMenu.add(salirMenuItem);

        // Añadir el menú a la barra de menús
        menuBar.add(opcionesMenu);
        setJMenuBar(menuBar);

        // Crear el panel principal con BorderLayout
        JPanel panelPrincipal = new JPanel(new BorderLayout());

        // Añadir el borde vacío (márgenes)
        panelPrincipal.setBorder(new EmptyBorder(10, 20, 30, 20));  // 10px arriba, 20px a los lados, 30px abajo

        // Crear un panel para centrar el logo
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));  // Usamos FlowLayout.CENTER para centrar el logo
        logoLabel = new JLabel();

        // Cargar la imagen del logo
        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/com/robertoarcusa/tfg/resources/Letras_Logotipo_SportSystem.png"));
        if (logoIcon != null) {
            // Ajustar la imagen para que se acomode bien en el tamaño del JLabel
            logoLabel.setIcon(new ImageIcon(logoIcon.getImage().getScaledInstance(1450, 55, Image.SCALE_SMOOTH)));
        } else {
            System.out.println("No se pudo cargar la imagen.");
        }

        // Agregar el logo al panel que lo centrará
        logoPanel.add(logoLabel);

        // Colocar el panel con el logo en la parte superior (NORTH) del panel principal
        panelPrincipal.add(logoPanel, BorderLayout.NORTH);

        // Crear el JTabbedPane
        tabbedPane = new JTabbedPane();

        // Añadir pestañas funcionales
        tabbedPane.addTab("SOCIOS", null, new PanelSocios(), "Gestión de socios");
        tabbedPane.addTab("ENTRENADORES", null, new PanelEntrenador(), "Gestión de entrenadores");
        tabbedPane.addTab("CLASES", null, new PanelClase(), "Gestión de clases");
        tabbedPane.addTab("SESIONES CLASES", null, new PanelSesionClase(), "Gestión de sesiones de clases");
        tabbedPane.addTab("INSCRIPCIONES", null, new PanelInscripciones(), "Gestión de inscripciones");
        tabbedPane.addTab("PAGOS", null, new PanelPagos(), "Gestión de pagos");
        tabbedPane.addTab("INFORMES", null, new PanelInformes(), "Generador de informes");

        // Simular separación usando una pestaña invisible expandible
        JPanel spacerPanel = new JPanel();
        tabbedPane.addTab("", spacerPanel);
        tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, Box.createHorizontalGlue());
        tabbedPane.setEnabledAt(tabbedPane.getTabCount() - 1, false); // no seleccionable

        // Seleccionar "SOCIOS" como pestaña inicial
        tabbedPane.setSelectedIndex(0);

        // Verificar si el usuario actual es de tipo BASIC y ocultar pestañas
        Socio usuarioActual = Sesion.getUsuarioActual();
        if (usuarioActual != null && usuarioActual.getTipoUsuario() == TipoUsuario.BASIC) {
            for (int i = tabbedPane.getTabCount() - 1; i >= 0; i--) {
                String titulo = tabbedPane.getTitleAt(i).toUpperCase();
                if (titulo.equals("SOCIOS") || titulo.equals("PAGOS") || titulo.equals("INFORMES")) {
                    tabbedPane.removeTabAt(i);
                }
            }
        }

        // Eventos al cambiar pestañas
        tabbedPane.addChangeListener(e -> {
            Component selected = tabbedPane.getSelectedComponent();
            if (selected instanceof PanelClase) {
                ((PanelClase) selected).cargarEntrenadores();
                ((PanelClase) selected).cargarClases();
                ((PanelClase) selected).limpiarCampos();
            } else if (selected instanceof PanelSesionClase) {
                ((PanelSesionClase) selected).cargarClases();
                ((PanelSesionClase) selected).cargarSesiones();
                ((PanelSesionClase) selected).limpiarCampos();
            } else if (selected instanceof PanelInscripciones) {
                ((PanelInscripciones) selected).cargarSocios();
                ((PanelInscripciones) selected).cargarSesiones();
                ((PanelInscripciones) selected).cargarInscripciones();
                ((PanelInscripciones) selected).limpiarCampos();
            } else if (selected instanceof PanelPagos) {
                ((PanelPagos) selected).cargarPagos();
                ((PanelPagos) selected).limpiarCampos();
            } else if (selected instanceof PanelSocios) {
                ((PanelSocios) selected).limpiarCampos();
            }
        });

        // Añadir el tabbedPane al panel principal
        panelPrincipal.add(tabbedPane, BorderLayout.CENTER);
        add(panelPrincipal);

        // Añadir listener para controlar maximizar/restaurar tamaño
        final Dimension tamañoFijo = new Dimension(1440, 900);
        addWindowStateListener(new WindowStateListener() {
            @Override
            public void windowStateChanged(WindowEvent e) {
                if ((e.getNewState() & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH) {
                    // Ventana maximizada - deja que se maximice normalmente

                } else if ((e.getNewState() & Frame.NORMAL) == Frame.NORMAL) {
                    // Ventana restaurada - vuelve al tamaño fijo
                    setSize(tamañoFijo);
                    setLocationRelativeTo(null); // Centrar ventana
                }
            }
        });
    }

    /**
     * Abre la ventana de login y cierra la ventana principal actual.
     */
    private void abrirLoginUsuario() {
        LoginUsuario loginUsuario = new LoginUsuario();
        loginUsuario.setVisible(true);
        this.dispose();  // Cierra la ventana actual
    }

    /**
     * Aplica el tema visual de modo oscuro a la interfaz y actualiza la configuración guardada.
     */
    private void aplicarModoOscuro() {
        modoOscuro = true;
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        SwingUtilities.updateComponentTreeUI(this);  // Actualizar la interfaz

        // Guardar el estado del modo
        guardarModo(true);

        cambiarModoMenuItem.setText("Cambiar a modo claro");
    }

    /**
     * Aplica el tema visual de modo claro a la interfaz y actualiza la configuración guardada.
     */
    private void aplicarModoClaro() {
        modoOscuro = false;
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        SwingUtilities.updateComponentTreeUI(this);  // Actualizar la interfaz

        // Guardar el estado del modo
        guardarModo(false);

        cambiarModoMenuItem.setText("Cambiar a modo oscuro");
    }

    /**
     * Carga el estado actual del modo (oscuro o claro) desde un archivo de configuración.
     *
     * @return true si el modo oscuro está activado, false si está en modo claro o si ocurre algún error.
     */
    private boolean cargarModo() {
        Properties propiedades = new Properties();
        try (InputStream input = new FileInputStream("config.properties")) {
            propiedades.load(input);
            return Boolean.parseBoolean(propiedades.getProperty("modoOscuro", "false"));
        } catch (IOException e) {
            e.printStackTrace();
            return false;  // Si hay error, por defecto será claro
        }
    }

    /**
     * Guarda el estado actual del modo (oscuro o claro) en un archivo de configuración.
     *
     * @param modoOscuro true para modo oscuro, false para modo claro.
     */
    private void guardarModo(boolean modoOscuro) {
        Properties propiedades = new Properties();
        try (OutputStream output = new FileOutputStream("config.properties")) {
            propiedades.setProperty("modoOscuro", String.valueOf(modoOscuro));
            propiedades.store(output, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
