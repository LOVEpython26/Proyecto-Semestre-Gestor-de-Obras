package Interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormualrioAPU {
    public JPanel panelAPU;
    private JLabel tituloFijado;
    private JComboBox proyectosBox;
    private JComboBox itemsBox;
    private JButton regresarButton;
    private JButton agregarAnalisisButton;
    private JComboBox recursosBox;
    private JTextField cantidadBox;
    private JTextField ctRequeridadBox;
    private JTable table;
    private JTextField totalBox;
    private JLabel titulo1;
    private JLabel texto2;
    private JLabel texto3;
    private JLabel texto4;
    private JLabel texto5;
    private JTextField vUnitarioBox;
    private JLabel texto6;
    private JLabel titulo2;
    private JLabel texto7;

    public FormualrioAPU(int idRol) {
        regresarButton.addActionListener(new ActionListener() {
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
                Window ventanaActual = SwingUtilities.getWindowAncestor(panelAPU);
                if (ventanaActual != null) {
                    ventanaActual.dispose();
                }
            }
        });
    }
}
