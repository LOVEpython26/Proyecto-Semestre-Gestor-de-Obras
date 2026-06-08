package Interfaz.Formularios;

import Interfaz.Menus.PanelIngenierio;
import Logica.LogicaHerencia.LogicaItems;
import Logica.LogicaHerencia.LogicaRecursos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class FormularioRecursos {
    public JPanel panelRecursos;
    private JLabel tituloFijado;
    private JLabel nombreRecurso;
    private JTextField nombreRBox;
    private JLabel costoRecurso;
    private JTextField costoRBox;
    private JComboBox desplegableRecursos;
    private JLabel tipoRecurso;
    private JButton regresarButton;
    private JButton guardarButton;
    private JTabbedPane tabbedPane1;
    private JPanel registrarPanel;
    private JPanel actualizarPanel;
    private JPanel eliminarPanel;
    private JComboBox recursoBox1;
    private JButton modificarRecursoButton;
    private JTextField costo;
    private JComboBox recursosBox2;
    private JButton eliminarButton;
    private HashMap<String, Integer> mapaRecursos; // El diccionario global


    public FormularioRecursos(int idRol) {
        //Llenar las listas despegables
        recargarComboRecursos();

        regresarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 1. Volvemos a crear y mostrar el Menú del Administrador
                JFrame frameMenu = new JFrame("Panel de Administración - Sistema DSW");
                // OJO: Asegúrate de que "panelAdmin" sea el nombre correcto de tu panel principal en MenuAdministrador
                frameMenu.setContentPane(new PanelIngenierio(idRol).panelIngenieria);
                frameMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frameMenu.pack();
                frameMenu.setSize(400,500);
                frameMenu.setLocationRelativeTo(null); // Lo centra en la pantalla
                frameMenu.setVisible(true);

                // 2. Ahora sí, cerramos el formulario de registro actual
                Window ventanaActual = SwingUtilities.getWindowAncestor(panelRecursos);
                if (ventanaActual != null) {
                    ventanaActual.dispose();
                }
            }
        });

        guardarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // 1. Captura de datos desde la interfaz (RNF04)
                String nombre = nombreRBox.getText().trim();
                // Obtenemos el tipo (Material, Equipo o Mano de obra) del ComboBox
                String tipo = desplegableRecursos.getSelectedItem().toString();
                String costoTexto = costoRBox.getText().trim();

                // 2. Validación de campos vacíos (RNF08)
                if (nombre.isEmpty() || costoTexto.isEmpty()) {
                    // Entra aquí si el usuario dejó el nombre o el costo en blanco
                    JOptionPane.showMessageDialog(null, "Faltan datos : Por favor, Complete los campos.",
                            "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
                    return; // Detiene la ejecución para que no avance
                }

                if (desplegableRecursos.getSelectedIndex() == 0) {
                    // Entra aquí si sí hay texto, pero se les olvidó abrir el desplegable
                    JOptionPane.showMessageDialog(null, "Falta selección: Debes elegir un tipo de recurso.",
                            "Tipo no seleccionado", JOptionPane.WARNING_MESSAGE);
                    return; // Detiene la ejecución
                }

                try {
                    // 3. Conversión de texto a número (Double para coincidir con NUMERIC en SQL)
                    double costo = Double.parseDouble(costoTexto);

                    // 4. Llamado a la lógica de negocio (RF06)
                    LogicaRecursos lj = new LogicaRecursos(nombre, tipo, costo);
                    boolean exito = lj.registrar();

                    if (exito) {
                        JOptionPane.showMessageDialog(null, "Recurso '" + nombre + "' registrado correctamente.");

                        // 5. Limpieza automática de campos para el siguiente registro
                        nombreRBox.setText("");
                        costoRBox.setText("");
                        desplegableRecursos.setSelectedIndex(0); // Vuelve a la primera opción
                        nombreRBox.requestFocus(); // Pone el foco para seguir escribiendo
                    } else {
                        JOptionPane.showMessageDialog(null, "No se pudo conectar con el servidor para guardar.");
                    }

                } catch (NumberFormatException ex) {
                    // RNF10: Manejo de error si el usuario escribe letras en el campo de costo
                    JOptionPane.showMessageDialog(null, "Error: El costo debe ser un número válido (ej: 50000.00).",
                            "Error de Formato", JOptionPane.ERROR_MESSAGE);
                }
            }



        });
        modificarRecursoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // 1. Validaciones de campos vacíos o sin selección (RNF08)
                if (recursoBox1.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(null, "Por favor, seleccione un recurso de la lista.");
                    return;
                }
                if (costo.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "El precio es obligatorio.");
                    return;
                }

                try {
                    // 2. Extraer el nombre del ComboBox
                    String nombreRecurso = recursoBox1.getSelectedItem().toString();

                    // 3. Traducir nombre a ID mediante el mapa global
                    int idrecurso = mapaRecursos.get(nombreRecurso);

                    // 4. Cargar la "mochila" con los datos de la interfaz
                    LogicaRecursos lr = new LogicaRecursos();

                    lr.setIdrecurso(idrecurso); // Asumiendo que usas el ID para buscar el registro
                    lr.setCosto(Double.parseDouble(costo.getText().trim()));

                    // 5. Ejecutar la lógica de actualización
                    if (lr.actualizar()) {
                        JOptionPane.showMessageDialog(null, "¡Recurso actualizado correctamente!");
                        //limpiar campos y recargar
                        costo.setText("");
                        recargarComboRecursos();
                    } else {
                        JOptionPane.showMessageDialog(null, "Error: No se pudo actualizar el recurso.");
                        //limpiar campos
                        costo.setText("");
                    }

                } catch (NumberFormatException ex) {
                    // Manejo de error: Si el usuario escribe letras en el campo "Cantidad"
                    JOptionPane.showMessageDialog(null, "Error: El costo debe ser un número entero válido.");
                } catch (Exception ex) {
                    // Manejo de error genérico para cualquier otro fallo
                    JOptionPane.showMessageDialog(null, "Error inesperado: " + ex.getMessage());
                }

            }
        });
        eliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // 1. Verificamos si hay algo seleccionado
                if (recursosBox2.getSelectedItem() == null || recursosBox2.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(null, "Selecciona un recurso válido para eliminar.");
                    return;
                }

                // 2. Confirmación de seguridad
                int confirmacion = JOptionPane.showConfirmDialog(null,
                        "¿Estás seguro de eliminar este item? Esta acción es irreversible.",
                        "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

                if (confirmacion == JOptionPane.YES_OPTION) {
                    try {
                        // 3. Obtenemos el nombre y buscamos su ID en el mapa
                        String recursoSeleccionado = recursosBox2.getSelectedItem().toString();

                        // Aseguramos que el mapa contenga la llave antes de obtener el ID
                        if (mapaRecursos.containsKey(recursoSeleccionado)) {
                            int idSeleccionado = mapaRecursos.get(recursoSeleccionado);

                            // 4. Disparamos la lógica
                            LogicaRecursos lr = new LogicaRecursos();
                            lr.setIdrecurso(idSeleccionado);

                            if (lr.eliminar()) {
                                JOptionPane.showMessageDialog(null, "Recurso eliminado con éxito.");
                                // Recargamos el combo para limpiar la vista
                                recargarComboRecursos();
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
        String[] recursos = {"--- Seleccione un Tipo ---","Material", "Mano de Obra", "Equipo"};

        // ¡LA SOLUCIÓN!
        // Usamos la variable global directamente sin ponerle "JComboBox" al principio
        desplegableRecursos = new JComboBox(recursos);

        // Empieza mostrando la instrucción
        desplegableRecursos.setSelectedIndex(0);
    }

    private void recargarComboRecursos() {
        LogicaRecursos lr = new LogicaRecursos();

        // 1. Cargamos el mapa global
        this.mapaRecursos = lr.obtenerRecursosParaCombo();

        // 2. Limpiamos el combo
        recursoBox1.removeAllItems();
        recursoBox1.addItem("Seleccione un recurso...");

        recursosBox2.removeAllItems();
        recursosBox2.addItem("Seleccione un recurso...");

        // 3. Llenamos con las llaves del mapa
        if (this.mapaRecursos != null) {
            for (String nombre : this.mapaRecursos.keySet()) {
                recursoBox1.addItem(nombre);
                recursosBox2.addItem(nombre);
            }
        }
    }


}
