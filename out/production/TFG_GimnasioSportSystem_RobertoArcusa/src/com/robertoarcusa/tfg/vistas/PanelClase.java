package com.robertoarcusa.tfg.vistas;

import com.robertoarcusa.tfg.clases.Clase;
import com.robertoarcusa.tfg.clases.Entrenador;
import com.robertoarcusa.tfg.clases.Socio;
import com.robertoarcusa.tfg.dao.ClaseDAO;
import com.robertoarcusa.tfg.dao.EntrenadorDAO;
import com.robertoarcusa.tfg.enums.NivelDificultad;
import com.robertoarcusa.tfg.enums.TipoUsuario;
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
import java.util.List;

/**
 * * Panel de gestión de clases en la interfaz de usuario.
 *  * Permite a los administradores y usuarios avanzados agregar, modificar, eliminar
 *  * y visualizar clases disponibles en el sistema.
 *  * También permite la carga de imágenes asociadas a cada clase y ofrece filtrado
 *  * por nombre para facilitar la búsqueda.
 *  *
 *  * Los usuarios con rol BASIC solo pueden visualizar las clases.
 *
 * @author Roberto Arcusa
 * @version 1.0
 * @since 2025
 */

public class PanelClase extends JPanel {

    private JTable tablaClases;
    private DefaultTableModel modeloTabla;
    private JTextField txtNombreClase;
    private JTextField txtCapacidadMaxima;
    private JTextField txtSala;
    private JComboBox<String> comboNivelDificultad;
    private JComboBox<Entrenador>comboEntrenador;
    private JButton btnGuardar, btnAgregar, btnEliminar, btnLimpiarCampos;
    private JLabel lblImagenClase;
    private byte[] imagenClaseSeleccionada;
    private JTextField txtBuscarClase;

    /**
     * Constructor que inicializa y configura el panel de clases.
     * Define la interfaz gráfica, carga los datos iniciales
     * y establece restricciones de acceso según el tipo de usuario.
     */
    public PanelClase() {
        setLayout(new BorderLayout(10, 10)); // Añadidos gaps como en PanelEntrenador

        // PANEL CAMPOS CON GRIDBAGLAYOUT
        JPanel panelCampos = new JPanel(new GridBagLayout());
        panelCampos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.NORTHWEST;

        // --- COLUMNA 1 ---
        JPanel columna1 = new JPanel(new GridBagLayout());
        GridBagConstraints c1 = new GridBagConstraints();
        c1.insets = new Insets(5, 5, 5, 5);
        c1.fill = GridBagConstraints.HORIZONTAL;
        c1.weightx = 1.0;

        txtNombreClase = new JTextField();
        txtNombreClase.setPreferredSize(new Dimension(350, 40));
        txtCapacidadMaxima = new JTextField();
        txtCapacidadMaxima.setPreferredSize(new Dimension(350, 40));
        txtSala = new JTextField();
        txtSala.setPreferredSize(new Dimension(350, 40));

        c1.gridx = 0; c1.gridy = 0;
        columna1.add(new JLabel("NOMBRE DE LA CLASE:"), c1);
        c1.gridx = 1;
        columna1.add(txtNombreClase, c1);

        c1.gridx = 0; c1.gridy++;
        columna1.add(new JLabel("CAPACIDAD MÁXIMA:"), c1);
        c1.gridx = 1;
        columna1.add(txtCapacidadMaxima, c1);

        c1.gridx = 0; c1.gridy++;
        columna1.add(new JLabel("SALA:"), c1);
        c1.gridx = 1;
        columna1.add(txtSala, c1);

        // --- COLUMNA 2 ---
        JPanel columna2 = new JPanel(new GridBagLayout());
        GridBagConstraints c2 = new GridBagConstraints();
        c2.insets = new Insets(5, 5, 5, 5);
        c2.fill = GridBagConstraints.HORIZONTAL;
        c2.weightx = 1.0;

        comboNivelDificultad = new JComboBox<>(new String[]{"PRINCIPIANTE", "INTERMEDIO", "AVANZADO"});
        comboNivelDificultad.setPreferredSize(new Dimension(350, 40));
        comboEntrenador = new JComboBox<>();
        cargarEntrenadores();
        comboEntrenador.setPreferredSize(new Dimension(350, 40));

        c2.gridx = 0; c2.gridy = 0;
        columna2.add(new JLabel("NIVEL DE DIFICULTAD:"), c2);
        c2.gridx = 1;
        columna2.add(comboNivelDificultad, c2);

        c2.gridx = 0; c2.gridy++;
        columna2.add(new JLabel("ENTRENADOR:"), c2);
        c2.gridx = 1;
        columna2.add(comboEntrenador, c2);

        // --- COLUMNA 3: Imagen ---
        JPanel columna3 = new JPanel(new BorderLayout(5, 5));
        JLabel lblTituloImagen = new JLabel("FOTO SALA", SwingConstants.CENTER);
        columna3.add(lblTituloImagen, BorderLayout.NORTH);

        lblImagenClase = new JLabel("Sin foto", SwingConstants.CENTER);
        lblImagenClase.setPreferredSize(new Dimension(150, 150));
        lblImagenClase.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        columna3.add(lblImagenClase, BorderLayout.CENTER);

        JButton btnCargarImagen = new JButton("CARGAR FOTO");
        btnCargarImagen.addActionListener(e -> cargarImagenClase());

        JButton btnEliminarImagen = new JButton("ELIMINAR FOTO");
        btnEliminarImagen.addActionListener(e -> {
            imagenClaseSeleccionada = null;
            lblImagenClase.setIcon(null);
            lblImagenClase.setText("Sin foto");
        });

        JPanel panelBotonesImagen = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panelBotonesImagen.add(btnCargarImagen);
        panelBotonesImagen.add(btnEliminarImagen);
        columna3.add(panelBotonesImagen, BorderLayout.SOUTH);

        // Añadir columnas al panelCampos
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.4;
        panelCampos.add(columna1, gbc);

        gbc.gridx = 1; gbc.weightx = 0.4;
        panelCampos.add(columna2, gbc);

        gbc.gridx = 2; gbc.weightx = 0.2;
        panelCampos.add(columna3, gbc);

        // MODELO Y TABLA
        modeloTabla = new DefaultTableModel(
                new String[]{"ID", "NOMBRE", "CAPACIDAD", "SALA", "NIVEL DIFICULTAD", "ENTRENADOR"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaClases = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaClases);

        // BOTONES Y BUSQUEDA
        btnAgregar = new JButton("AÑADIR CLASE");
        btnGuardar = new JButton("MODIFICAR CLASE");
        btnEliminar = new JButton("ELIMINAR CLASE");
        btnLimpiarCampos = new JButton("LIMPIAR CAMPOS");

        btnAgregar.addActionListener(e -> agregarNuevaClase());
        btnGuardar.addActionListener(e -> modificarClase());
        btnEliminar.addActionListener(e -> eliminarClase());
        btnLimpiarCampos.addActionListener(e -> limpiarCampos());

        // Ajustar tamaño preferido para hacer botones un poco más grandes (como en PanelEntrenador)
        Dimension tamañoOriginal = btnAgregar.getPreferredSize();
        Dimension tamañoNuevo = new Dimension(tamañoOriginal.width + 35, tamañoOriginal.height + 10);

        btnAgregar.setPreferredSize(tamañoNuevo);
        btnGuardar.setPreferredSize(tamañoNuevo);
        btnEliminar.setPreferredSize(tamañoNuevo);
        btnLimpiarCampos.setPreferredSize(tamañoNuevo);

        txtBuscarClase = new JTextField(20);
        setTextFieldHint(txtBuscarClase, "Introduce un nombre de una clase");
        txtBuscarClase.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filtrarClasesPorNombre(txtBuscarClase.getText());
            }
        });

        // Panel búsqueda + botones organizado igual que PanelEntrenador
        JPanel panelBusquedaBotones = new JPanel(new BorderLayout());

        // Panel para botones centrados arriba
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.add(btnAgregar);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiarCampos);

        // Panel búsqueda alineada a la izquierda abajo
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelBusqueda.add(new JLabel("BUSCAR CLASE"));
        panelBusqueda.add(txtBuscarClase);

        panelBusquedaBotones.add(panelBotones, BorderLayout.CENTER);
        panelBusquedaBotones.add(panelBusqueda, BorderLayout.SOUTH);

        // PANEL ARRIBA QUE CONTIENE CAMPOS Y BOTONES+BUSQUEDA
        JPanel panelArriba = new JPanel(new BorderLayout());
        panelArriba.add(panelCampos, BorderLayout.CENTER);
        panelArriba.add(panelBusquedaBotones, BorderLayout.SOUTH);

        // AÑADIR AL LAYOUT PRINCIPAL
        add(panelArriba, BorderLayout.NORTH);
        add(scrollTabla, BorderLayout.CENTER);

        // Cargamos los datos y aplicamos restricciones por tipo de usuario
        Socio usuarioActual = Sesion.getUsuarioActual();
        if (usuarioActual != null && usuarioActual.getTipoUsuario() == TipoUsuario.BASIC) {
            panelCampos.setVisible(false);
            panelBusquedaBotones.setVisible(false);

            DefaultTableModel modeloBasico = new DefaultTableModel(
                    new String[]{"Nombre", "Capacidad", "Sala", "Nivel Dificultad", "Entrenador"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            for (Clase c : ClaseDAO.obtenerTodasLasClases()) {
                modeloBasico.addRow(new Object[]{
                        c.getNombreClase(),
                        c.getCapacidadMaxima(),
                        c.getSala(),
                        c.getNivelDificultad(),
                        c.getEntrenador().getNombreEntrenador() + " " + c.getEntrenador().getApellidosEntrenador()
                });
            }

            tablaClases.setModel(modeloBasico);
        } else {
            cargarClases();

            tablaClases.getSelectionModel().addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) {
                    int fila = tablaClases.getSelectedRow();
                    if (fila >= 0) {
                        int id = (int) modeloTabla.getValueAt(fila, 0);
                        cargarDatosClase(id);
                    }
                }
            });
        }
    }

    /**
     * Carga los datos de una clase específica en los campos del formulario
     * a partir de su ID. También actualiza la imagen y el entrenador asignado.
     *
     * @param id el identificador único de la clase a cargar
     */
    private void cargarDatosClase(int id) {
        ClaseDAO dao = new ClaseDAO();
        Clase clase = dao.obtenerClasePorId(id);
        if (clase != null) {
            // Limpiamos y cargamos los campos de texto
            txtNombreClase.setText(clase.getNombreClase());
            txtCapacidadMaxima.setText(String.valueOf(clase.getCapacidadMaxima()));
            txtSala.setText(clase.getSala());
            comboNivelDificultad.setSelectedItem(clase.getNivelDificultad().name());  // Asegurarse de que se maneja como String

            // Establecemos el entrenador en el ComboBox
            for (int i = 0; i < comboEntrenador.getItemCount(); i++) {
                Entrenador entrenadorCombo = comboEntrenador.getItemAt(i);
                if (entrenadorCombo.getIdEntrenador() == clase.getEntrenador().getIdEntrenador()) {
                    comboEntrenador.setSelectedItem(entrenadorCombo);
                    break;
                }
            }

            // Imagen de la clase
            imagenClaseSeleccionada = clase.getImagenClase();
            if (imagenClaseSeleccionada != null) {
                ImageIcon imagenIcon = new ImageIcon(imagenClaseSeleccionada);
                Image image = imagenIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                lblImagenClase.setIcon(new ImageIcon(image));
                lblImagenClase.setText("");
            } else {
                imagenClaseSeleccionada = null;
                lblImagenClase.setIcon(null);
                lblImagenClase.setText("Sin foto");
            }
        }
    }

    /**
     * Agrega una nueva clase utilizando los datos introducidos en el formulario.
     * Realiza validaciones de entrada antes de guardar en la base de datos.
     * Muestra mensajes de error si hay campos inválidos o vacíos.
     */
    private void agregarNuevaClase() {
        // Validamos que todos los campos estén rellenados
        if (txtNombreClase.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, rellene el campo NOMBRE DE LA CLASE.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (txtCapacidadMaxima.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, rellene el campo CAPACIDAD MÁXIMA.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Validamos que la capacidad máxima sea un número válido
        try {
            Integer.parseInt(txtCapacidadMaxima.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La CAPACIDAD MÁXIMA debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (txtSala.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, rellene el campo SALA.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (comboNivelDificultad.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione el NIVEL DE DIFICULTAD.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (comboEntrenador.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un ENTRENADOR.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Si todos los campos están correctos, se crea la nueva clase y se añade
        Clase nuevaClase = new Clase();
        nuevaClase.setNombreClase(txtNombreClase.getText());
        nuevaClase.setCapacidadMaxima(Integer.parseInt(txtCapacidadMaxima.getText()));
        nuevaClase.setSala(txtSala.getText());

        // Convertimos el String a NivelDificultad (enum)
        String nivelDificultadString = (String) comboNivelDificultad.getSelectedItem();
        NivelDificultad nivelDificultad = NivelDificultad.valueOf(nivelDificultadString.toUpperCase());
        nuevaClase.setNivelDificultad(nivelDificultad);

        nuevaClase.setEntrenador((Entrenador) comboEntrenador.getSelectedItem());
        nuevaClase.setImagenClase(imagenClaseSeleccionada);

        // Insertar la clase en la base de datos
        ClaseDAO dao = new ClaseDAO();
        dao.agregarClase(nuevaClase);
        limpiarCampos();
        cargarClases();
        JOptionPane.showMessageDialog(this, "Clase añadida con éxito.");
    }

    /**
     * Modifica una clase seleccionada de la tabla con los datos actuales del formulario.
     * Valida los datos y actualiza la clase en la base de datos.
     */
    private void modificarClase() {
        int fila = tablaClases.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona una clase.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) modeloTabla.getValueAt(fila, 0);
        ClaseDAO dao = new ClaseDAO();
        Clase clase = dao.obtenerClasePorId(id);
        if (clase != null) {
            clase.setNombreClase(txtNombreClase.getText());
            clase.setCapacidadMaxima(Integer.parseInt(txtCapacidadMaxima.getText()));
            clase.setSala(txtSala.getText());

            // Convertimos el String a NivelDificultad (enum)
            String nivelDificultadString = (String) comboNivelDificultad.getSelectedItem();
            NivelDificultad nivelDificultad = NivelDificultad.valueOf(nivelDificultadString.toUpperCase());  // Convertir a enum
            clase.setNivelDificultad(nivelDificultad);

            clase.setEntrenador((Entrenador) comboEntrenador.getSelectedItem());
            clase.setImagenClase(imagenClaseSeleccionada);

            dao.actualizarClase(clase);
            cargarClases();
            JOptionPane.showMessageDialog(this, "Clase actualizada.");
        }
    }

    /**
     * Elimina la clase seleccionada de la tabla previa confirmación del usuario.
     * Si la clase no se puede eliminar, muestra un mensaje de error.
     */
    private void eliminarClase() {
        int fila = tablaClases.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona una clase.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Confirmar eliminación
        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Estás seguro de que quieres eliminar esta clase?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (opcion == JOptionPane.YES_OPTION) {
            int id = (int) modeloTabla.getValueAt(fila, 0);
            ClaseDAO dao = new ClaseDAO();
            Clase clase = dao.obtenerClasePorId(id);

            if (clase != null) {
                boolean eliminada = dao.eliminarClase(clase);
                if (eliminada) {
                    cargarClases();
                    limpiarCampos();
                    JOptionPane.showMessageDialog(this, "Clase eliminada correctamente.");
                } else {
                    JOptionPane.showMessageDialog(this,
                            "No se pudo eliminar la clase. Por favor, inténtalo de nuevo o contacta al administrador.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se encontró la clase seleccionada.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Filtra y muestra las clases en la tabla que contienen el texto especificado
     * en su nombre. El filtro no distingue entre mayúsculas y minúsculas.
     *
     * @param filtro texto que se usará para filtrar las clases por nombre
     */
    private void filtrarClasesPorNombre(String filtro) {
        modeloTabla.setRowCount(0);  // Limpiar la tabla
        ClaseDAO dao = new ClaseDAO();
        List<Clase> clases = dao.obtenerTodasLasClases();
        for (Clase clase : clases) {
            if (clase.getNombreClase().toLowerCase().contains(filtro.toLowerCase())) {
                modeloTabla.addRow(new Object[]{
                        clase.getIdClase(),
                        clase.getNombreClase(),
                        clase.getCapacidadMaxima(),
                        clase.getNivelDificultad(),
                        clase.getSala()
                });
            }
        }
    }

    /**
     * Carga la lista de entrenadores desde la base de datos
     * y los inserta en el comboBox correspondiente.
     */
    public void cargarEntrenadores() {
        comboEntrenador.removeAllItems();
        EntrenadorDAO dao = new EntrenadorDAO();
        List<Entrenador> entrenadores = dao.obtenerTodosLosEntrenadores();
        for (Entrenador e : entrenadores) {
            comboEntrenador.addItem(e);
        }
    }

    /**
     * Carga todas las clases desde la base de datos y las muestra
     * en la tabla de la interfaz gráfica.
     */
    public void cargarClases() {
        modeloTabla.setRowCount(0);
        ClaseDAO dao = new ClaseDAO();
        List<Clase> clases = dao.obtenerTodasLasClases();
        for (Clase c : clases) {
            modeloTabla.addRow(new Object[]{
                    c.getIdClase(),
                    c.getNombreClase(),
                    c.getCapacidadMaxima(),
                    c.getSala(),
                    c.getNivelDificultad(),
                    c.getEntrenador().getNombreEntrenador() + " " + c.getEntrenador().getApellidosEntrenador()
            });
        }
    }

    /**
     * Abre un selector de archivos para que el usuario elija una imagen desde su sistema.
     *
     * Si se selecciona un archivo, se lee su contenido como un arreglo de bytes y se asigna como imagen
     * de la clase actual. La imagen se escala y muestra en un JLabel del formulario.
     *
     * En caso de error de lectura del archivo, se imprime la traza del error en la consola.
     */
    private void cargarImagenClase() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                imagenClaseSeleccionada = Files.readAllBytes(file.toPath());
                ImageIcon imageIcon = new ImageIcon(imagenClaseSeleccionada);
                Image image = imageIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                lblImagenClase.setIcon(new ImageIcon(image));
                lblImagenClase.setText("");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Limpia todos los campos del formulario, restableciendo sus valores por defecto.
     *
     * Elimina el texto de los campos de entrada, reinicia las selecciones de los ComboBoxes
     * y borra la imagen seleccionada, mostrando nuevamente el texto "Sin foto".
     */
    public void limpiarCampos() {
        txtNombreClase.setText("");
        txtCapacidadMaxima.setText("");
        txtSala.setText("");
        comboNivelDificultad.setSelectedIndex(-1);
        comboEntrenador.setSelectedIndex(-1);
        imagenClaseSeleccionada = null;
        lblImagenClase.setIcon(null);
        lblImagenClase.setText("Sin foto");
    }

    /**
     * Establece un texto de sugerencia (placeholder) en un JTextField.
     *
     * Cuando el campo gana el foco, si el texto coincide con la sugerencia, se borra y el color cambia a negro.
     * Cuando pierde el foco y el campo está vacío, se vuelve a mostrar la sugerencia en color gris.
     *
     * @param textField El campo de texto al que se aplicará la sugerencia.
     * @param hint El texto de sugerencia que se mostrará cuando el campo esté vacío.
     */
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