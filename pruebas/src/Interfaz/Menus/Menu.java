package Interfaz.Menus;

import Interfaz.Formularios.FormularioAvances;
import Interfaz.Formularios.FormularioRegistro;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu {

    public JPanel panelMenu;
    private JButton registraUsuariosButton;
    private JButton proyectosYCostosButton;
    private JButton controlDeObraButton;
    private JButton sistemaYReportesButton;
    private JButton cerrarSesionButton;

    public Menu(int idRol) {
        // 2. Ejecutamos la seguridad para ocultar/mostrar botones según quién entró
        aplicarRestricciones(idRol);

        // 3. Lógica del botón para abrir el Formulario de Registro
        registraUsuariosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Abrimos la ventana de registro que ya tienes lista
                JFrame frameRegistro = new JFrame("Registro de Personal - Sistema DSW");
                frameRegistro.setContentPane(new FormularioRegistro(idRol).panelFormulario);
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
        cerrarSesionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirmacion = JOptionPane.showConfirmDialog(null,
                        "¿Estás seguro de que deseas cerrar sesión?",
                        "Confirmar Salida",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);

                // 2. Si responde que "Sí" (YES_OPTION), hacemos el cambio de ventanas
                if (confirmacion == JOptionPane.YES_OPTION) {

                    // A. Abrimos de nuevo el Login (tu clase Pest)
                    JFrame frameLogin = new JFrame("Acceso al Sistema DSW");
                    // OJO: Asegúrate de que 'panelPrincipal' sea el nombre del panel en tu Pest.java
                    frameLogin.setContentPane(new Pest().panelPrincipal);
                    frameLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frameLogin.pack();
                    frameLogin.setLocationRelativeTo(null); // Centrar en pantalla
                    frameLogin.setVisible(true);

                    // B. Cerramos la ventana del Menú actual para liberar memoria
                    Window ventanaActual = SwingUtilities.getWindowAncestor(panelMenu);
                    if (ventanaActual != null) {
                        ventanaActual.dispose();
                    }
                }
                // Si dice que "No", el if no se ejecuta y simplemente se queda en el Menú.
            }
        });
        proyectosYCostosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 1. Creamos y configuramos la nueva ventana del Módulo de Ingeniería
                JFrame frameIngenieria = new JFrame("Módulo de Ingeniería y Costos - DSW");

                // OJO: Si tu clase se llama PanelIngeniero, cámbialo aquí.
                // También le pasamos el idRol (si tu constructor lo pide) para mantener la sesión.
                frameIngenieria.setContentPane(new PanelIngenierio(idRol).panelIngenieria);

                frameIngenieria.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frameIngenieria.pack();
                frameIngenieria.setLocationRelativeTo(null); // Esto centra la ventana en la pantalla
                frameIngenieria.setVisible(true); // ¡Mostramos el panel!

                // 2. Cerramos la ventana del Menú actual para limpiar la memoria (RNF04)
                // Asegúrate de que "panelMenu" sea el nombre del JPanel principal de esta ventana
                Window ventanaActual = SwingUtilities.getWindowAncestor(panelMenu);
                if (ventanaActual != null) {
                    ventanaActual.dispose();
                }
            }
        });
        controlDeObraButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // 1. Creamos y configuramos la nueva ventana del Módulo de Ingeniería
                JFrame frameIngenieria = new JFrame("Módulo de Ingeniería y Costos - DSW");

                // OJO: Si tu clase se llama PanelIngeniero, cámbialo aquí.
                // También le pasamos el idRol (si tu constructor lo pide) para mantener la sesión.
                frameIngenieria.setContentPane(new FormularioAvances(idRol).panelAvances);

                frameIngenieria.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frameIngenieria.pack();
                frameIngenieria.setLocationRelativeTo(null); // Esto centra la ventana en la pantalla
                frameIngenieria.setVisible(true); // ¡Mostramos el panel!

                // 2. Cerramos la ventana del Menú actual para limpiar la memoria (RNF04)
                // Asegúrate de que "panelMenu" sea el nombre del JPanel principal de esta ventana
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
        proyectosYCostosButton.setVisible(false);

        // Si en el futuro agregas más botones (ej. Presupuestos), apágalos aquí también:
        // btnPresupuestos.setVisible(false);

        // Encendemos solo lo que le toca a cada rol
        switch (idRol) {
            case 1: // ADMINISTRADOR
                registraUsuariosButton.setVisible(true); // Solo el Admin puede ver estos boton
                proyectosYCostosButton.setVisible(true);
                break;
            case 2: // INGENIERO
                proyectosYCostosButton.setVisible(true);
                break;
            case 3: // OPERARIO
                break;
        }
    }
}
