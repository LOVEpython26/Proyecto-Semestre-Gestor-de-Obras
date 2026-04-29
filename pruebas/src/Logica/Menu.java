package Logica;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu {

    public JPanel panelMenu;
    private JButton registraUsuariosButton;
    private JButton ingenieríaYCostosButton;
    private JButton controlDeObraButton;
    private JButton sistemaYReportesButton;
    private JButton button9;

    public Menu(int idRol) {
        // 2. Ejecutamos la seguridad para ocultar/mostrar botones según quién entró
        aplicarRestricciones(idRol);

        // 3. Lógica del botón para abrir el Formulario de Registro
        registraUsuariosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Abrimos la ventana de registro que ya tienes lista
                JFrame frameRegistro = new JFrame("Registro de Personal - Sistema DSW");
                frameRegistro.setContentPane(new FormularioRegistro().panelFormulario);
                frameRegistro.pack();
                frameRegistro.setLocationRelativeTo(null); // Centrar en pantalla
                frameRegistro.setVisible(true);

                // Cerramos el menú actual para no amontonar ventanas (Mantenibilidad)
                Window ventanaActual = SwingUtilities.getWindowAncestor(panelMenu);
                if (ventanaActual != null) {
                    ventanaActual.dispose();
                }
            }
        });
    }

    // 4. Método "Cadenero": Controla la visibilidad de los botones (RF02)
    private void aplicarRestricciones(int idRol) {
        // Por seguridad, apagamos todos los botones sensibles primero
        registraUsuariosButton.setVisible(false);

        // Si en el futuro agregas más botones (ej. Presupuestos), apágalos aquí también:
        // btnPresupuestos.setVisible(false);

        // Encendemos solo lo que le toca a cada rol
        switch (idRol) {
            case 1: // ADMINISTRADOR
                registraUsuariosButton.setVisible(true); // Solo el Admin puede ver este botón
                break;
            case 2: // INGENIERO
                // btnPresupuestos.setVisible(true);
                break;
            case 3: // OPERARIO
                // btnAvances.setVisible(true);
                break;
        }
    }
}
