package Interfaz.Formularios;

import Interfaz.Menus.Menu;
import Logica.LogicaReportes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class FormularioReportes {
    public JPanel panelReportes;
    private JLabel tituloFijado;
    private JLabel texto1;
    private JComboBox proyectosBox;
    private JLabel texto2;
    private JLabel costoBox;
    private JTable table1;
    private JLabel texto3;
    private JButton regresarButton;
    private JButton guardarReporteButton;
    private JTextArea textArea1;
    private JTabbedPane tabbedPane1;
    private JTable table2;
    private LogicaReportes logica ;
    private ArrayList<Integer> listaIdsProyectos;

    public FormularioReportes(int idRol) {
        this.logica = new LogicaReportes();

        // 1. Agregar el texto por defecto ANTES de cargar los proyectos de la BD
        proyectosBox.addItem("Seleccione un proyecto...");

        // 1. Cargar los proyectos en la lista desplegable al abrir la ventana
        ArrayList<String> proyectos = logica.cargarProyectos();
        for (String p : proyectos) {
            proyectosBox.addItem(p);
        }

        // 2. EVENTO: Qué pasa cuando elijo un proyecto diferente
        proyectosBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (proyectosBox.getSelectedItem() != null) {

                    String seleccion = proyectosBox.getSelectedItem().toString();

                    // 1. FRENO DE MANO: Si escogen la opción por defecto, limpiamos la pantalla y evitamos el crash
                    if (proyectosBox.equals("Seleccione un proyecto...")) {
                        costoBox.setText("$0");
                        table1.setModel(new javax.swing.table.DefaultTableModel(new String[]{"Ítem", "Recurso", "Cantidad", "Costo Unitario"}, 0));
                        textArea1.setText("");
                        table2.setModel(new javax.swing.table.DefaultTableModel(new String[]{"Fecha del Reporte", "Resumen Guardado"}, 0));

                        return; // ¡LA MAGIA ESTÁ AQUÍ! Esto corta la ejecución para que no intente buscar un ID que no existe.
                    }

                    // 2. Si pasa el filtro anterior, es porque SÍ es un proyecto real. Sacamos el ID de forma segura.
                    try {
                        int idProy = Integer.parseInt(seleccion.split(" - ")[0]);

                        // -- Pestaña 1: Llenamos Presupuesto, Tabla APU y Texto automático --
                        costoBox.setText(logica.obtenerPresupuestoTotal(idProy));
                        table1.setModel(logica.obtenerModeloTablaAPU(idProy));
                        textArea1.setText(logica.compilarObservaciones(idProy));

                        // -- Pestaña 2: Llenamos el historial de reportes pasados --
                        table2.setModel(logica.obtenerModeloHistorialReportes(idProy));

                    } catch (NumberFormatException ex) {
                        System.err.println("Error al extraer el ID del proyecto: " + ex.getMessage());
                    }
                }
            }
        });

        // 3. EVENTO: Botón de Guardar Reporte
        guardarReporteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (proyectosBox.getSelectedItem() != null && !textArea1.getText().trim().isEmpty()) {
                    int idProy = Integer.parseInt(proyectosBox.getSelectedItem().toString().split(" - ")[0]);

                    boolean exito = logica.guardarReporte(idProy, textArea1.getText());
                    if (exito) {
                        JOptionPane.showMessageDialog(null, "¡Reporte guardado exitosamente!", "Éxito", JOptionPane.INFORMATION_MESSAGE);

                        // Magia: Actualizar la tabla 2 de inmediato para ver el reporte que acabamos de guardar
                        table2.setModel(logica.obtenerModeloHistorialReportes(idProy));
                    } else {
                        JOptionPane.showMessageDialog(null, "Error al guardar el reporte en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Seleccione un proyecto válido y asegúrese de tener información.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // 4. EVENTO: Botón de Regresar
        regresarButton.addActionListener(new ActionListener() {
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
                frameMenu.setSize(400,500);
                frameMenu.setLocationRelativeTo(null); // Centramos la ventana
                frameMenu.setVisible(true); // Mostramos el menú

                // 2. Cerramos la ventana de Ingeniería actual para liberar RAM (RNF04)
                // Asegúrate de que 'panelIngenieria' sea el nombre de tu panel principal en esta clase
                Window ventanaActual = SwingUtilities.getWindowAncestor(panelReportes);
                if (ventanaActual != null) {
                    ventanaActual.dispose();
                }

            }
        });
    }

}
