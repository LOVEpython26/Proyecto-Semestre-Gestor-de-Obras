package Interfaz.Formularios;

import Interfaz.Menus.PanelIngenierio;
import Logica.LogicaRecursos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

    public FormularioRecursos(int idRol) {

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
                    LogicaRecursos lj = new Logica.LogicaRecursos();
                    boolean exito = lj.registrarRecurso(nombre, tipo, costo);

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
