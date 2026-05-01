package Interfaz;

import Logica.LogicaAPU;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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

    // Instancia de nuestra lógica limpia
    private LogicaAPU logicaAPU;

    // Listas para guardar los datos traídos de la BD y no perder los IDs
    private ArrayList<String[]> listaProyectos;
    private ArrayList<String[]> listaItems;
    private ArrayList<String[]> listaRecursos;

    // Modelo para manejar la tabla
    private DefaultTableModel modeloTabla;


    public FormualrioAPU(int idRol) {

        logicaAPU = new LogicaAPU();

        // 1. Preparamos la interfaz gráfica inicial
        configurarTabla();
        cargarProyectos();
        cargarRecursos();
        actualizarTotalAPU();


        // ==============================================================
        // EVENTOS EN CASCADA (La magia de la interfaz)
        // ==============================================================

        proyectosBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = proyectosBox.getSelectedIndex();
                if (index > 0) { // Si eligió un proyecto válido (mayor a 0)
                    // Obtenemos el ID del proyecto desde nuestra lista (index - 1 por el "Seleccione...")
                    int idProyecto = Integer.parseInt(listaProyectos.get(index - 1)[0]);
                    cargarItems(idProyecto);
                } else {
                    itemsBox.removeAllItems(); // Si elige "Seleccione...", limpiamos los ítems
                    cantidadBox.setText("0");
                }
            }
        });

        // Evento 2: Al seleccionar un Ítem -> Mostrar su Cantidad Contratada
        itemsBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = itemsBox.getSelectedIndex();
                if (index > 0 && listaItems != null) {
                    // La cantidad viene en la posición [2] del array
                    String cantidad = listaItems.get(index - 1)[2];
                    cantidadBox.setText(cantidad);
                } else {
                    cantidadBox.setText("0");
                }
            }
        });

        // Evento 3: Al seleccionar un Recurso -> Mostrar su Costo Base
        recursosBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = recursosBox.getSelectedIndex();
                if (index > 0 && listaRecursos != null) {
                    // El costo viene en la posición [2] del array
                    String costo = listaRecursos.get(index - 1)[2];
                    vUnitarioBox.setText(costo);
                } else {
                    vUnitarioBox.setText("0");
                }
            }
        });

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
        proyectosBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = proyectosBox.getSelectedIndex();
                if (index > 0) { // Si eligió un proyecto válido (mayor a 0)
                    // Obtenemos el ID del proyecto desde nuestra lista (index - 1 por el "Seleccione...")
                    int idProyecto = Integer.parseInt(listaProyectos.get(index - 1)[0]);
                    cargarItems(idProyecto);
                } else {
                    itemsBox.removeAllItems(); // Si elige "Seleccione...", limpiamos los ítems
                    cantidadBox.setText("0");
                }
            }
        });

        agregarAnalisisButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 1. Validar que el ingeniero haya seleccionado un Proyecto
                int indexProyecto = proyectosBox.getSelectedIndex();
                if (indexProyecto <= 0) {
                    JOptionPane.showMessageDialog(null, "Por favor, seleccione un Proyecto primero.", "Error", JOptionPane.WARNING_MESSAGE);
                    return; // Corta la ejecución aquí
                }

                // 2. Validar que el ingeniero haya seleccionado un Ítem (Actividad)
                int indexItem = itemsBox.getSelectedIndex();
                if (indexItem <= 0) {
                    JOptionPane.showMessageDialog(null, "Por favor, seleccione una actividad (Ítem) a la cual agregarle recursos.", "Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // 3. Validar que se haya seleccionado un recurso real (no el texto "Seleccione...")
                int indexRecurso = recursosBox.getSelectedIndex();
                if (indexRecurso <= 0) {
                    JOptionPane.showMessageDialog(null, "Por favor, seleccione un recurso de la lista.", "Error", JOptionPane.WARNING_MESSAGE);
                    return; // Corta la ejecución aquí
                }

                // 2. Validar que la cantidad ingresada sea un número correcto
                String cantidadStr = ctRequeridadBox.getText().trim();
                if (cantidadStr.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor ingrese la cantidad requerida (Rendimiento).", "Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                double rendimiento = 0.0;
                try {
                    // Convertimos el texto a número decimal
                    rendimiento = Double.parseDouble(cantidadStr);
                    if (rendimiento <= 0) {
                        JOptionPane.showMessageDialog(null, "El rendimiento debe ser mayor a 0.", "Error", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Ingrese un número válido para el rendimiento.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // 3. Extraer los datos del recurso seleccionado de nuestra lista en memoria
                String[] recursoInfo = listaRecursos.get(indexRecurso - 1);
                String idRecurso = recursoInfo[0];
                String nombreRecurso = recursoInfo[1];
                double valorUnitario = Double.parseDouble(recursoInfo[2]);

                // 4. Calcular el subtotal de este material
                double subtotal = rendimiento * valorUnitario;

                // 5. Agregar la fila a la tabla visual (JTable)
                // El orden debe coincidir con las columnas que creamos antes
                modeloTabla.addRow(new Object[]{idRecurso, nombreRecurso, rendimiento, valorUnitario, subtotal});

                // 6. Actualizar el Gran Total
                actualizarTotalAPU();

                // 7. Limpiar la interfaz para que el usuario agregue el siguiente material más rápido
                recursosBox.setSelectedIndex(0);
                ctRequeridadBox.setText("");
            }

        });
    }

    // ==============================================================
    // MÉTODOS DE APOYO PARA LLENAR LA INTERFAZ
    // ==============================================================

    private void configurarTabla() {
        // Le damos estructura a la JTable vacía que tenías en IntelliJ
        String[] columnas = {"ID Recurso", "Nombre Recurso", "Rendimiento", "Valor Unitario", "Subtotal"};
        modeloTabla = new DefaultTableModel(columnas, 0){
            public boolean isCellEditable(int row, int column) {
                return false; // Bloquea la edición manual en todas las celdas
            }
        };
        table.setModel(modeloTabla);
    }

    private void cargarProyectos() {
        proyectosBox.removeAllItems();
        proyectosBox.addItem("Seleccione un proyecto...");

        listaProyectos = logicaAPU.obtenerProyectos();
        for (String[] proyecto : listaProyectos) {
            proyectosBox.addItem(proyecto[1]); // Agregamos el Nombre al ComboBox
        }
    }

    private void cargarItems(int idProyecto) {
        itemsBox.removeAllItems();
        itemsBox.addItem("Seleccione una actividad...");

        listaItems = logicaAPU.obtenerItemsPorProyecto(idProyecto);
        for (String[] item : listaItems) {
            itemsBox.addItem(item[1]); // Agregamos la Descripción al ComboBox
        }
    }

    private void cargarRecursos() {
        recursosBox.removeAllItems();
        recursosBox.addItem("Seleccione un recurso...");

        listaRecursos = logicaAPU.obtenerRecursos();
        for (String[] recurso : listaRecursos) {
            recursosBox.addItem(recurso[1]); // Agregamos el Nombre al ComboBox
        }

    }

    private void actualizarTotalAPU() {
        double granTotal = 0.0;

        // Recorremos todas las filas que existan en la tabla actualmente
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
          // Sacamos el valor de la columna 4 (que es el Subtotal) y lo sumamos
          granTotal += Double.parseDouble(modeloTabla.getValueAt(i, 4).toString());
        }

        // Lo mostramos en tu campo de texto "totalBox"
        totalBox.setText(String.valueOf(granTotal));
    }






}
