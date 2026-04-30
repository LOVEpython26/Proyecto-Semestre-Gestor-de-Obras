package Interfaz;

import Logica.Logica;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class FormularioItems {
    public JPanel panelItems;
    private JLabel tituloFijado;
    private JLabel texto1;
    private JComboBox proyectosBox;
    private JTextField itemBox;
    private JLabel texto2;
    private JTextField cantidadBox;
    private JLabel texto3;
    private JButton regresarButton;
    private JTextField precioBox;
    private JButton guardarButton;
    private HashMap<String, Integer> listaProyectos;
    private int idrol;


    public FormularioItems(int idRol) {

        this.idrol=idRol;

        Logica lj = new Logica();
        listaProyectos = lj.obtenerProyectosParaCombo();

        proyectosBox.removeAllItems(); // Limpieza por seguridad
        proyectosBox.addItem("Seleccione un proyecto...");

        if (listaProyectos != null && !listaProyectos.isEmpty()) {
            for (String nombreObra : listaProyectos.keySet()) {
                proyectosBox.addItem(nombreObra); // Llenamos la lista visual
            }
        } else {
            proyectosBox.addItem("No hay proyectos disponibles");
        }

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
                Window ventanaActual = SwingUtilities.getWindowAncestor(panelItems);
                if (ventanaActual != null) {
                    ventanaActual.dispose();
                }
            }
        });
        guardarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 1. Validaciones de campos vacíos o sin selección (RNF08)
                if (proyectosBox.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(null, "Por favor, seleccione un proyecto de la lista.");
                    return;
                }
                if (itemBox.getText().trim().isEmpty() || cantidadBox.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "La descripción y la cantidad son obligatorias.");
                    return;
                }

                try {
                    // 2. Captura de datos
                    String descripcion = itemBox.getText().trim();

                    // CUIDADO AQUÍ: Validamos que la cantidad sea un número entero (INT en SQL)[cite: 2]
                    int cantidad = Integer.parseInt(cantidadBox.getText().trim());

                    // Sacamos el ID real del proyecto usando el nombre seleccionado
                    String nombreProyecto = proyectosBox.getSelectedItem().toString();
                    int idProyecto = listaProyectos.get(nombreProyecto);

                    // 3. Enviamos a la Lógica
                    Logica lj = new Logica();
                    // OJO: El precio se envía como 0 (porque luego el APU lo calculará)
                    boolean exito = lj.registrarItem(idProyecto, descripcion, cantidad, 0.0);

                    if (exito) {
                        JOptionPane.showMessageDialog(null, "Ítem guardado con éxito.");
                        itemBox.setText("");
                        cantidadBox.setText("");
                        proyectosBox.setSelectedIndex(0);
                    } else {
                        JOptionPane.showMessageDialog(null, "Error al guardar en la base de datos.");
                    }

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Error: La cantidad debe ser un número entero (Ej: 150).",
                            "Formato Inválido", JOptionPane.ERROR_MESSAGE);
                }
            }

        });


    }

    private void createUIComponents() {
        proyectosBox = new JComboBox<>();
    }

}
