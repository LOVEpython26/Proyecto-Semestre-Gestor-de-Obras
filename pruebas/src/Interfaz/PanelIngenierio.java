package Interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PanelIngenierio {
    public JPanel panelIngenieria;
    private JLabel tituloFijado;
    private JButton configurarProyectosButton;
    private JButton atrasButton;

    public PanelIngenierio(int idRol) {
        configurarProyectosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        atrasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 1. Volvemos a crear y mostrar el Menú Principal
                JFrame frameMenu = new JFrame("Panel de Control DSW");

                // OJO AQUÍ: 'idRol' debe ser la variable global donde guardaste el rol
                // cuando entraste a esta ventana de Ingeniería.
                // Asegúrate de que 'panelMenu' sea el nombre correcto del panel en Menu.java
                frameMenu.setContentPane(new Menu(idRol).panelMenu);

                frameMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frameMenu.pack();
                frameMenu.setLocationRelativeTo(null); // Centramos la ventana
                frameMenu.setVisible(true); // Mostramos el menú

                // 2. Cerramos la ventana de Ingeniería actual para liberar RAM (RNF04)
                // Asegúrate de que 'panelIngenieria' sea el nombre de tu panel principal en esta clase
                Window ventanaActual = SwingUtilities.getWindowAncestor(panelIngenieria);
                if (ventanaActual != null) {
                    ventanaActual.dispose();
                }

            }
        });
    }
}
