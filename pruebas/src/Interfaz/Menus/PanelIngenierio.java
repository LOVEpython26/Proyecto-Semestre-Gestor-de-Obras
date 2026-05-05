package Interfaz.Menus;

import Interfaz.Formularios.FormularioAPU;
import Interfaz.Formularios.FormularioItems;
import Interfaz.Formularios.FormularioProyectos;
import Interfaz.Formularios.FormularioRecursos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PanelIngenierio {
    public JPanel panelIngenieria;
    private JLabel tituloFijado;
    private JButton configurarProyectosButton;
    private JButton atrasButton;
    private JButton gestionDeRecursosButton;
    private JButton gestiónDeÍtemsButton;
    private JButton gestionAPUButton;

    public PanelIngenierio(int idRol) {
        configurarProyectosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frameProyectos = new JFrame("Gestión de Proyectos - DSW");

                // PASO CLAVE: Le pasamos el 'idRol' para mantener la sesión activa.
                // OJO: Asegúrate de que tu clase se llame 'FormularioProyectos' y su panel principal 'panelProyectos'.
                frameProyectos.setContentPane(new FormularioProyectos(idRol).panelProyectos);

                frameProyectos.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frameProyectos.pack();
                frameProyectos.setSize(400,500);
                frameProyectos.setLocationRelativeTo(null); // Centra la ventana en la pantalla
                frameProyectos.setVisible(true); // Muestra el nuevo formulario

                // 2. Cerramos la ventana de Ingeniería actual para liberar memoria (RNF04)
                // Asegúrate de que "panelIngenieria" sea el nombre del JPanel principal de esta clase.
                Window ventanaActual = SwingUtilities.getWindowAncestor(panelIngenieria);
                if (ventanaActual != null) {
                    ventanaActual.dispose();
                }
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
        gestionDeRecursosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frameProyectos = new JFrame("Gestión de Recursos - DSW");

                // PASO CLAVE: Le pasamos el 'idRol' para mantener la sesión activa.
                // OJO: Asegúrate de que tu clase se llame 'FormularioProyectos' y su panel principal 'panelProyectos'.
                frameProyectos.setContentPane(new FormularioRecursos(idRol).panelRecursos);

                frameProyectos.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frameProyectos.pack();
                frameProyectos.setLocationRelativeTo(null); // Centra la ventana en la pantalla
                frameProyectos.setVisible(true); // Muestra el nuevo formulario

                // 2. Cerramos la ventana de Ingeniería actual para liberar memoria (RNF04)
                // Asegúrate de que "panelIngenieria" sea el nombre del JPanel principal de esta clase.
                Window ventanaActual = SwingUtilities.getWindowAncestor(panelIngenieria);
                if (ventanaActual != null) {
                    ventanaActual.dispose();
                }
            }

        });
        gestiónDeÍtemsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frameProyectos = new JFrame("Gestión de Items - DSW");

                // PASO CLAVE: Le pasamos el 'idRol' para mantener la sesión activa.
                // OJO: Asegúrate de que tu clase se llame 'FormularioProyectos' y su panel principal 'panelProyectos'.
                frameProyectos.setContentPane(new FormularioItems(idRol).panelItems);

                frameProyectos.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frameProyectos.pack();
                frameProyectos.setLocationRelativeTo(null); // Centra la ventana en la pantalla
                frameProyectos.setVisible(true); // Muestra el nuevo formulario

                // 2. Cerramos la ventana de Ingeniería actual para liberar memoria (RNF04)
                // Asegúrate de que "panelIngenieria" sea el nombre del JPanel principal de esta clase.
                Window ventanaActual = SwingUtilities.getWindowAncestor(panelIngenieria);
                if (ventanaActual != null) {
                    ventanaActual.dispose();
                }
            }

        });
        gestionAPUButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frameProyectos = new JFrame("Gestión de APU - DSW");

                // PASO CLAVE: Le pasamos el 'idRol' para mantener la sesión activa.
                // OJO: Asegúrate de que tu clase se llame 'FormularioProyectos' y su panel principal 'panelProyectos'.
                frameProyectos.setContentPane(new FormularioAPU(idRol).panelAPU);

                frameProyectos.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frameProyectos.pack();
                frameProyectos.setLocationRelativeTo(null); // Centra la ventana en la pantalla
                frameProyectos.setVisible(true); // Muestra el nuevo formulario

                // 2. Cerramos la ventana de Ingeniería actual para liberar memoria (RNF04)
                // Asegúrate de que "panelIngenieria" sea el nombre del JPanel principal de esta clase.
                Window ventanaActual = SwingUtilities.getWindowAncestor(panelIngenieria);
                if (ventanaActual != null) {
                    ventanaActual.dispose();
                }
            }
        });
    }
}
