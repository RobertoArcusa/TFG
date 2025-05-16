import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        FlatLightLaf.setup();

        // Llamamos a la clase LoginUsuario
        SwingUtilities.invokeLater(() -> {
            com.robertoarcusa.tfg.vistas.LoginUsuario ventana = new com.robertoarcusa.tfg.vistas.LoginUsuario();
            ventana.setVisible(true);
        });

    }
}