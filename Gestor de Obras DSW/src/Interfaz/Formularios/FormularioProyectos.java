package Interfaz.Formularios;

import Interfaz.Menus.PanelIngenierio;
import Logica.LogicaHerencia.LogicaProyectos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.HashMap;

public class FormularioProyectos {
    public JPanel panelProyectos;
    private JButton salirButton;
    private JButton guardarButton;
    private JLabel tituloFijado;
    private JTextField nombreBox;
    private JTextField ubicacionBox;
    private JLabel texto1;
    private JLabel texto2;
    private JLabel texto3;
    private JTextField fhInicioBox;
    private JTabbedPane tabbedPane1;
    private JPanel montarPanel;
    private JPanel actualizarPanel;
    private JPanel eliminarPanel;
    private JComboBox proyectoBox;
    private JButton actualizarEstadoButton;
    private JComboBox estadosBox;
    private JComboBox proyecto2Box3;
    private JButton eliminarButton;
    private JLabel TituloFijado;
    HashMap<String, Integer> listaProyectos;


    public FormularioProyectos(int idRol) {

        recargarComboProyectos();

        if (listaProyectos != null && !listaProyectos.isEmpty()) {
            for (String nombreObra : listaProyectos.keySet()) {
                proyectoBox.addItem(nombreObra); // Llenamos la lista visual
                proyecto2Box3.addItem(nombreObra);
            }
        } else {
            proyectoBox.addItem("No hay proyectos disponibles");
            proyecto2Box3.addItem("No hay proyectos disponibles");
        }

        fhInicioBox.setText("AAAA-MM-DD"); // Si no escribió nada, vuelve la guía
        fhInicioBox.setForeground(Color.GRAY);

        salirButton.addActionListener(new ActionListener() {
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
                Window ventanaActual = SwingUtilities.getWindowAncestor(panelProyectos);
                if (ventanaActual != null) {
                    ventanaActual.dispose();
                }
            }

        });
        guardarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String nombre = nombreBox.getText().trim();
                String ubicacion = ubicacionBox.getText().trim();
                String fecha = fhInicioBox.getText().trim();



                // El estado es "Activo" por defecto como acordamos
                String estado = "Activo";

                // 2. Validación básica (RNF08) para no enviar basura a la BD
                if (nombre.isEmpty() || ubicacion.isEmpty() || fecha.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Error: Debes llenar todos los campos.");
                    return;
                }

                // 3. Llamada a la lógica (Aquí tus compañeros conectan el SQL)
                LogicaProyectos lj = new LogicaProyectos(nombre, ubicacion, fecha, estado);
                boolean exito = lj.registrar();

                if (exito) {
                    JOptionPane.showMessageDialog(null, "¡Proyecto guardado con éxito!");

                    // --- AQUÍ ESTÁ LO QUE PEDISTE: LIMPIAR CAMPOS ---
                    nombreBox.setText("");
                    ubicacionBox.setText("");
                    fhInicioBox.setText("");

                    // Ponemos el cursor en el primer campo para el siguiente registro
                    nombreBox.requestFocus();
                } else {
                    JOptionPane.showMessageDialog(null, "Hubo un error al guardar en la base de datos.");
                }
            }

        });
        fhInicioBox.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (fhInicioBox.getText().equals("AAAA-MM-DD")) {
                    fhInicioBox.setText(""); // Se limpia solo al hacer clic
                    fhInicioBox.setForeground(Color.GRAY); // Color de escritura real
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (fhInicioBox.getText().isEmpty()) {
                    fhInicioBox.setText("AAAA-MM-DD"); // Si no escribió nada, vuelve la guía
                    fhInicioBox.setForeground(Color.GRAY);
                }
            }
        });
        eliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // 1. Validación básica
                if (proyecto2Box3.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(null, "Selecciona un proyecto para eliminar.");
                    return;
                }

                // 2. Obtener el texto seleccionado
                String pytSeleccionado = proyecto2Box3.getSelectedItem().toString();

                // --- LA VALIDACIÓN CRÍTICA ---
                // Preguntamos si el mapa contiene esa llave antes de intentar sacar el ID
                if (!listaProyectos.containsKey(pytSeleccionado)) {
                    JOptionPane.showMessageDialog(null, "Por favor, selecciona un proyecto válido de la lista.");
                    return; // Detenemos la ejecución aquí
                }

                // 2.5. Confirmación de seguridad
                int confirmacion = JOptionPane.showConfirmDialog(null,
                        "¿Estás seguro de que deseas eliminar este proyecto? Esta acción no se puede deshacer.",
                        "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

                if (confirmacion == JOptionPane.YES_OPTION) {
                    try {

                        // 3. Obtener ID del mapa
                        pytSeleccionado = proyecto2Box3.getSelectedItem().toString();
                        int idProyecto = listaProyectos.get(pytSeleccionado);

                        // 4. Disparar lógica
                        LogicaProyectos lp = new LogicaProyectos();
                        lp.setIdProyecto(idProyecto);

                        if (lp.eliminar()) {
                            JOptionPane.showMessageDialog(null, "Proyecto eliminado con éxito.");

                            //Recargar el combo aquí para que desaparezca el proyecto borrado

                        } else {
                            JOptionPane.showMessageDialog(null, "Error al eliminar. Puede que el proyecto tenga registros asociados.");
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                    }
                }


            }
        });
        proyecto2Box3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {


            }


        });
        proyectoBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // --- ESCUDO PROTECTOR ---
                // Si no hay nada seleccionado, o el mapa aún no se carga, detenemos el código aquí.
                if (proyectoBox.getSelectedItem() == null || listaProyectos == null) {
                    return;
                }

                try {
                    // 1. Vemos qué proyecto seleccionó el usuario
                    String pySeleccionado = proyectoBox.getSelectedItem().toString();

                    // 2. Verificamos que ese nombre exista en nuestro HashMap
                    if (listaProyectos.containsKey(pySeleccionado)) {

                        // Sacamos el ID
                        int idProyecto = listaProyectos.get(pySeleccionado);

                        // 3. Vamos a la base de datos a preguntar su estado actual
                        LogicaProyectos lp = new LogicaProyectos();
                        String estadoActual = lp.obtenerEstadoProyecto(idProyecto);

                        // 4. ¡Magia! Hacemos que el ComboBox de estado cambie a ese valor
                        if (!estadoActual.isEmpty()) {
                            estadosBox.setSelectedItem(estadoActual);
                        }
                        //Comprobacion en la terminal que los datos de la BD y el combox sean iguales
                        System.out.println("Lo que trae la BD: '" + estadoActual + "'");
                    }
                } catch (Exception e) {
                    System.err.println("Ignorando evento de carga del combo");
                }



            }
        });


        actualizarEstadoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // 1. VALIDACIÓN RÁPIDA (Protege contra errores de usuario)
                if (proyectoBox.getSelectedItem() == null || estadosBox.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(null, "Por favor, selecciona un proyecto y un estado.");
                    return;
                }

                String pytSeleccionado = proyectoBox.getSelectedItem().toString();

                // --- LA VALIDACIÓN CRÍTICA ---
                // Preguntamos si el mapa contiene esa llave antes de intentar sacar el ID
                if (!listaProyectos.containsKey(pytSeleccionado)) {
                    JOptionPane.showMessageDialog(null, "Por favor, selecciona un proyecto válido de la lista.");
                    return; // Detenemos la ejecución aquí
                }

                try {
                    // 2. OBTENER DATOS DE LA INTERFAZ
                    // Leemos lo que el usuario ve
                    String nuevoEstado = estadosBox.getSelectedItem().toString();

                    // 3. TRADUCCIÓN (Usamos el diccionario mapaProyectos para sacar el ID)
                    // Si el nombre no está en el mapa, esto daría error, por eso el if
                    if (listaProyectos.containsKey(pytSeleccionado)) {
                        int idProyecto = listaProyectos.get(pytSeleccionado);

                        // 4. CARGAR LA MOCHILA
                        LogicaProyectos lp = new LogicaProyectos();
                        lp.setIdProyecto(idProyecto);
                        lp.setEstado(nuevoEstado);

                        // 5. DISPARAR LA ACCIÓN (El CRUD real)
                        if (lp.actualizar()) {
                            JOptionPane.showMessageDialog(null, "¡Proyecto actualizado correctamente!");
                            // Opcional: Aquí podrías recargar el combo si algo cambió
                        } else {
                            JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos.");
                        }
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Ocurrió un error inesperado: " + ex.getMessage());
                }


            }
        });


    }

    private void recargarComboProyectos() {
        // 1. Limpiamos las interfaces
        proyectoBox.removeAllItems();
        proyecto2Box3.removeAllItems(); // Tu segundo combo

        // 2. Agregamos el texto de ayuda
        proyectoBox.addItem("Seleccione un proyecto...");
        proyecto2Box3.addItem("Seleccione un proyecto...");

        // 3. Traemos los datos de la lógica
        LogicaProyectos lj = new LogicaProyectos();

        // Guardamos el resultado en la variable lista proyectos de CLASE, no una variable local
        this.listaProyectos = lj.obtenerProyectosParaCombo();

        // 4. Llenamos los combos usando el mapa global
        if (this.listaProyectos != null) {
            for (String nombre : this.listaProyectos.keySet()) {
                proyectoBox.addItem(nombre);
                proyecto2Box3.addItem(nombre);
            }
        }

        // 5. Llenado del combo de estados (lo que ya tenías)
        estadosBox.removeAllItems();
        estadosBox.addItem("Activo");
        estadosBox.addItem("En progreso");
        estadosBox.addItem("En ejecución");
        estadosBox.addItem("Pausado");
        estadosBox.addItem("Terminado");
    }


}
