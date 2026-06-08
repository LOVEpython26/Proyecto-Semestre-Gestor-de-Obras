package Interfaz.Formularios;

import Interfaz.Menus.PanelIngenierio;
import Logica.LogicaHerencia.LogicaItems;
import Logica.LogicaHerencia.LogicaProyectos;

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
    private JTabbedPane tabbedPane1;
    private JPanel vincularPanel;
    private JComboBox comboxActividades1;
    private JTextField descripcion;
    private JTextField cantidad;
    private JButton actualizarActividadButton;
    private JComboBox comboxActividades2;
    private JButton eliminarActividadButton;
    private HashMap<String, Integer> listaProyectos;
    private int idrol;
    private HashMap<String, Integer> mapaActividades; // El diccionario global


    public FormularioItems(int idRol) {
        //Llena lso formularios de actividades
        recargarComboActividades();

        this.idrol=idRol;
        //Capa de Lógica: Instanciamos la clase de lógica para acceder a los datos
        LogicaProyectos lj = new LogicaProyectos();
        //Carga de datos: Obtenemos el 'diccionario' (HashMap) de proyectos
        // para poblar los ComboBox y mapear nombres reales con IDs de BD.
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
                frameIngenieria.setSize(400,500);
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
                    LogicaItems lj = new LogicaItems(idProyecto, descripcion, cantidad,0.0);
                    // OJO: El precio se envía como 0 (porque luego el APU lo calculará)
                    boolean exito = lj.registrar();

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


        actualizarActividadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                // 1. Validaciones de campos vacíos o sin selección (RNF08)
                if (comboxActividades1.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(null, "Por favor, seleccione un actividad de la lista.");
                    return;
                }
                if (descripcion.getText().trim().isEmpty() || cantidad.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "La descripción y la cantidad son obligatorias.");
                    return;
                }

                try {
                    // 2. Extraer el nombre del ComboBox
                    String nombreActividad = comboxActividades1.getSelectedItem().toString();

                    // 3. Traducir nombre a ID mediante el mapa global
                    int idActividad = mapaActividades.get(nombreActividad);

                    // 4. Cargar la "mochila" con los datos de la interfaz
                    LogicaItems li = new LogicaItems();

                    li.setIdItem(idActividad); // Asumiendo que usas el ID para buscar el registro
                    li.setDescripcion(descripcion.getText().trim());
                    li.setCantidad(Integer.parseInt(cantidad.getText().trim()));

                    // 5. Ejecutar la lógica de actualización
                    if (li.actualizar()) {
                        JOptionPane.showMessageDialog(null, "¡Ítem actualizado correctamente!");
                        //limpiar campos y recargar
                        descripcion.setText("");
                        cantidad.setText("");
                        recargarComboActividades();
                    } else {
                        JOptionPane.showMessageDialog(null, "Error: No se pudo actualizar el registro.");
                        //limpiar campos
                        descripcion.setText("");
                        cantidad.setText("");
                    }

                } catch (NumberFormatException ex) {
                    // Manejo de error: Si el usuario escribe letras en el campo "Cantidad"
                    JOptionPane.showMessageDialog(null, "Error: La cantidad debe ser un número entero válido.");
                } catch (Exception ex) {
                    // Manejo de error genérico para cualquier otro fallo
                    JOptionPane.showMessageDialog(null, "Error inesperado: " + ex.getMessage());
                }
            }
        });
        eliminarActividadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // 1. Verificamos si hay algo seleccionado
                if (comboxActividades2.getSelectedItem() == null || comboxActividades2.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(null, "Selecciona un item válido para eliminar.");
                    return;
                }

                // 2. Confirmación de seguridad
                int confirmacion = JOptionPane.showConfirmDialog(null,
                        "¿Estás seguro de eliminar este item? Esta acción es irreversible.",
                        "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

                if (confirmacion == JOptionPane.YES_OPTION) {
                    try {
                        // 3. Obtenemos el nombre y buscamos su ID en el mapa
                        String nombreSeleccionado = comboxActividades2.getSelectedItem().toString();

                        // Aseguramos que el mapa contenga la llave antes de obtener el ID
                        if (mapaActividades.containsKey(nombreSeleccionado)) {
                            int idSeleccionado = mapaActividades.get(nombreSeleccionado);

                            // 4. Disparamos la lógica
                            LogicaItems li = new LogicaItems();
                            li.setIdItem(idSeleccionado);

                            if (li.eliminar()) {
                                JOptionPane.showMessageDialog(null, "Item eliminado con éxito.");
                                // Recargamos el combo para limpiar la vista
                                recargarComboActividades();
                            } else {
                                JOptionPane.showMessageDialog(null, "Error al eliminar. Revisa las relaciones.");
                            }
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error inesperado: " + ex.getMessage());
                    }
                }
            }
        });
    }

    private void createUIComponents() {
        proyectosBox = new JComboBox<>();
    }

    private void recargarComboActividades() {
        LogicaItems la = new LogicaItems();

        // 1. Cargamos el mapa global
        this.mapaActividades = la.obtenerActividadesParaCombo();

        // 2. Limpiamos el combo
        comboxActividades1.removeAllItems();
        comboxActividades1.addItem("Seleccione una actividad...");

        comboxActividades2.removeAllItems();
        comboxActividades2.addItem("Seleccione una actividad...");

        // 3. Llenamos con las llaves del mapa
        if (this.mapaActividades != null) {
            for (String nombre : this.mapaActividades.keySet()) {
                comboxActividades1.addItem(nombre);
                comboxActividades2.addItem(nombre);
            }
        }
    }

}
