import com.formdev.flatlaf.FlatLightLaf;
import com.robertoarcusa.tfg.util.DatabaseInitializer;

import javax.swing.*;

public class Main {
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