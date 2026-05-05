package Interfaz.Formularios;

import Interfaz.Menus.Menu;
import Logica.LogicaAvances;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class FormularioAvances {
    public JPanel panelAvances;
    private JLabel tituloFijado;
    private JLabel texto1;
    private JComboBox proyectosBox;
    private JLabel texto2;
    private JLabel texto3;
    private JTextField ubicacionBox;
    private JSpinner spinner;
    private JTextArea TextArea;
    private JLabel texto4;
    private JLabel texto5;
    private JButton guardarButton;
    private JButton regresarButton;
    private JTabbedPane tabbedPane1;
    private JProgressBar progressBar;
    private JComboBox itemsBox;
    private JTextField ctActualBox;
    private JLabel texto6;
    private JLabel texto7;
    private JLabel texto8;
    private JTextField nvCantidadBox;
    private JLabel porcentajeBarra;

    private ArrayList<String[]> listaItems;

    // Instanciamos la clase que acabas de crear
    private LogicaAvances logicaControl = new LogicaAvances();

    // Aquí guardaremos los IDs y Nombres para no perder el rastro
    private ArrayList<String[]> listaProyectos;

    public FormularioAvances(int idRol){

        // Llamamos a este método apenas se abre la ventana (RNF04 - Usabilidad)[cite: 1]
        cargarProyectosEnCombo();



        guardarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // 1. Validar proyecto seleccionado
                    int indexProy = proyectosBox.getSelectedIndex();
                    if (indexProy < 0) {
                        JOptionPane.showMessageDialog(null, "Por favor, seleccione un proyecto.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    // Sacamos el ID usando la lista paralela que creamos antes
                    int idProyecto = Integer.parseInt(listaProyectos.get(indexProy)[0]);

                    // 2. Recolectar datos de la Pestaña "Registrar" (RF04)
                    // Cambia "spinnerProgreso" y "txtObservaciones" por tus nombres reales
                    double porcentaje = Double.parseDouble(spinner.getValue().toString());
                    String observaciones = TextArea.getText().trim();

                    if (observaciones.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Las observaciones técnicas son obligatorias.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    // 3. Recolectar datos de la Pestaña "Modificar Items" (RF08)
                    // Cambia "txtNuevaCantidad" y "comboItems" por los nombres que les pusiste
                    String cantidadStr = nvCantidadBox.getText().trim();
                    int idItem = -1;
                    Double nuevaCantidad = null;

                    if (!cantidadStr.isEmpty()) { // Solo si el usuario decidió ajustar un ítem
                        int indexItem = itemsBox.getSelectedIndex();
                        if (indexItem >= 0) {
                            idItem = Integer.parseInt(listaItems.get(indexItem)[0]); // Asumiendo que tienes una lista paralela para ítems
                            nuevaCantidad = Double.parseDouble(cantidadStr);
                        } else {
                            JOptionPane.showMessageDialog(null, "Seleccione un ítem para modificar su cantidad.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                    }

                    // 4. Mandar todo a la clase LogicaControlObra (El Cerebro)
                    boolean exito = logicaControl.guardarReporteGeneral(idProyecto, porcentaje, observaciones, idItem, nuevaCantidad);

                    // 5. Retroalimentación visual (RNF08 y RNF10)[cite: 1]
                    if (exito) {
                        JOptionPane.showMessageDialog(null, "¡Guardado exitoso! Base de datos sincronizada.");

                        // Limpiar campos para evitar doble envío
                        TextArea.setText("");
                        nvCantidadBox.setText("");

                        // Refrescar la barra de progreso
                        actualizarProgresoVisual();
                    } else {
                        JOptionPane.showMessageDialog(null, "Error al guardar. Revisa la conexión con Clever Cloud.", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Por favor ingrese valores numéricos válidos en los porcentajes o cantidades.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error inesperado: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        });


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
                Window ventanaActual = SwingUtilities.getWindowAncestor(panelAvances);
                if (ventanaActual != null) {
                    ventanaActual.dispose();
                }


            }
        });
        proyectosBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = proyectosBox.getSelectedIndex();

                // Solo ejecutamos si seleccionó un proyecto real (índice mayor a 0)
                if (index > 0) {
                    int idProyecto = Integer.parseInt(listaProyectos.get(index)[0]);

                    // 1. Actualizar barra de progreso y texto
                    double progreso = logicaControl.obtenerProgresoActual(idProyecto);
                    progressBar.setValue((int) progreso);
                    porcentajeBarra.setText(progreso + " %"); // Cambia el nombre por el de tu campo

                    // 2. Traer ubicación (Si tienes un método para eso en tu lógica)
                    ubicacionBox.setText(listaProyectos.get(index)[2]);

                    // 2. Configurar el límite del Spinner (Reemplaza spinnerProgreso por el nombre de tu JSpinner)
                    // Arranca en 'progreso', mínimo es 'progreso', máximo 100.0, salta de a 1.0
                    spinner.setModel(new javax.swing.SpinnerNumberModel(progreso, progreso, 100.0, 1.0));

                    // 3. ¡Cargar los ítems en la segunda pestaña!
                    cargarItemsEnCombo(idProyecto);

                } else {
                    // Si el usuario vuelve a elegir "Seleccione un proyecto...", limpiamos todo
                    progressBar.setValue(0);
                    porcentajeBarra.setText("");
                    ubicacionBox.setText("");
                    spinner.setModel(new javax.swing.SpinnerNumberModel(0.0, 0.0, 100.0, 1.0)); // Lo reiniciamos a cero
                    if (itemsBox != null) itemsBox.removeAllItems();
                }
            }

        });
        itemsBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = itemsBox.getSelectedIndex();

                // Si el índice es mayor a 0, significa que eligió un ítem de verdad
                if (index > 0 && listaItems != null) {
                    // Recuerda que en cargarItemsPorProyecto guardamos: [0] ID, [1] Nombre, [2] Cantidad
                    String cantidad = listaItems.get(index)[2];

                    // Reemplaza "txtCantidadActual" por el nombre que le pusiste a tu campo
                    ctActualBox.setText(cantidad);
                } else {
                    // Si vuelve a "Seleccione un ítem...", limpiamos el campo
                    if (ctActualBox != null) {
                        ctActualBox.setText("");
                    }
                }
            }
        });
    }

    private void cargarProyectosEnCombo() {

        proyectosBox.removeAllItems(); // Limpiamos por si acaso
        listaProyectos = logicaControl.cargarProyectos(); // Traemos los datos de la BD[cite: 2]

        proyectosBox.addItem("Seleccione un proyecto...");
        // 2. Agregamos un registro "dummy" a nuestra lista paralela para que los índices sigan coincidiendo
        listaProyectos.add(0, new String[]{"-1", "Seleccione un proyecto...", ""});

        for (int i = 1; i < listaProyectos.size(); i++) {
            proyectosBox.addItem(listaProyectos.get(i)[1]);
        }
    }

    private void actualizarProgresoVisual() {
        int index = proyectosBox.getSelectedIndex();

        if (index > 0 && listaProyectos != null) {
            // 1. Sacamos el ID del proyecto seleccionado usando nuestra lista paralela
            int idProyecto = Integer.parseInt(listaProyectos.get(index)[0]);

            // 2. Le preguntamos a la base de datos el progreso actual[cite: 2]
            double progresoActual = logicaControl.obtenerProgresoActual(idProyecto);

            // 3. Pintamos los resultados en tu interfaz (Ajusta los nombres de tus componentes)
            // Si tienes una barra de progreso:
            progressBar.setValue((int) progresoActual);

            // Si tienes un Label o Campo para mostrar el número actual:
            porcentajeBarra.setText(progresoActual + " %");

            // Si tienes un campo de "Ubicación", más adelante crearemos la función para traerla también.
        }




    }

    private void cargarItemsEnCombo(int idProyecto) {
        itemsBox.removeAllItems();
        // Este método lo debes crear en LogicaControlObra (te lo dejo abajo)
        listaItems = logicaControl.cargarItemsPorProyecto(idProyecto);

        itemsBox.addItem("Seleccione un ítem...");
        listaItems.add(0, new String[]{"-1", "Seleccione un ítem...", "0"});

        for (int i = 1; i < listaItems.size(); i++) {
            itemsBox.addItem(listaItems.get(i)[1]); // Mostramos la descripción del ítem
        }
    }
}
