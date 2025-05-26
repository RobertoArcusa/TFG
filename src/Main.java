import com.formdev.flatlaf.FlatLightLaf;
import com.robertoarcusa.tfg.util.DatabaseInitializer;
import javax.swing.*;

/**
 * Clase principal que inicia la aplicación.
 * <p>
 * Esta clase es responsable de inicializar la base de datos, configurar la apariencia visual
 * y lanzar la ventana de inicio de sesión de usuario.
 */
public class Main {
    /**
     * Método principal de la aplicación.
     * <p>
     * Este método se encarga de:
     * <ul>
     *     <li>Inicializar la base de datos, creando la estructura necesaria si no existe.</li>
     *     <li>Aplicar el tema visual {@link FlatLightLaf} para una interfaz moderna.</li>
     *     <li>Lanzar la interfaz gráfica de inicio de sesión en el hilo de eventos de Swing.</li>
     * </ul>
     *
     * @param args Argumentos de línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        // Inicializar base de datos antes de cualquier uso de Hibernate
        DatabaseInitializer.initializeDatabase();

        // Establecer el tema visual
        FlatLightLaf.setup();

        // Lanzar la ventana de login
        SwingUtilities.invokeLater(() -> {
            com.robertoarcusa.tfg.vistas.LoginUsuario ventana = new com.robertoarcusa.tfg.vistas.LoginUsuario();
            ventana.setVisible(true);
        });
    }
}