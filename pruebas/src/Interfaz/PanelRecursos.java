package Interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PanelRecursos {
    public JPanel panelRecursos;
    private JLabel tituloFijado;
    private JLabel nombreRecurso;
    private JTextField nombreRBox;
    private JLabel costoRecurso;
    private JTextField costoRBox;
    private JComboBox desplegableRecursos;
    private JLabel tipoRecurso;
    private JButton regresarButton;

    public PanelRecursos(int idRol) {

        regresarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 1. Volvemos a crear y mostrar el Menú del Administrador
                JFrame frameMenu = new JFrame("Panel de Administración - Sistema DSW");
                // OJO: Asegúrate de que "panelAdmin" sea el nombre correcto de tu panel principal en MenuAdministrador
                frameMenu.setContentPane(new PanelIngenierio(idRol).panelIngenieria);
                frameMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frameMenu.pack();
                frameMenu.setLocationRelativeTo(null); // Lo centra en la pantalla
                frameMenu.setVisible(true);

                // 2. Ahora sí, cerramos el formulario de registro actual
                Window ventanaActual = SwingUtilities.getWindowAncestor(panelRecursos);
                if (ventanaActual != null) {
                    ventanaActual.dispose();
                }
            }
        });
    }

    private void createUIComponents() {
        String[] recursos = {"--- Seleccione un Tipo ---","Material", "Mano de Obra", "Equipo"};

        // ¡LA SOLUCIÓN!
        // Usamos la variable global directamente sin ponerle "JComboBox" al principio
        desplegableRecursos = new JComboBox(recursos);

        // Empieza mostrando la instrucción
        desplegableRecursos.setSelectedIndex(0);
    }
}
