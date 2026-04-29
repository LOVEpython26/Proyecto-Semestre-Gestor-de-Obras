package Interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormularioProyectos {
    public JPanel panelProyectos;
    private JButton salirButton;
    private JButton guardarButton;
    private JLabel tituloFijado;

    public FormularioProyectos(int idRol) {
        salirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 1. Instanciamos de nuevo el Panel del Ingeniero (padre de este formulario)
                JFrame frameIngenieria = new JFrame("Módulo de Ingeniería y Costos - DSW");

                // PASO CLAVE: Le devolvemos el idRol para que mantenga su sesión
                frameIngenieria.setContentPane(new PanelIngenierio(idRol).panelIngenieria);

                frameIngenieria.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frameIngenieria.pack();
                frameIngenieria.setLocationRelativeTo(null); // Centra la ventana
                frameIngenieria.setVisible(true);

                // 2. Cerramos este formulario actual para liberar la RAM
                // OJO: Asegúrate de que 'panelProyectos' sea el nombre de tu JPanel principal aquí
                Window ventanaActual = SwingUtilities.getWindowAncestor(panelProyectos);
                if (ventanaActual != null) {
                    ventanaActual.dispose();
                }
            }

        });
    }
}
