package Interfaz;

import Logica.Logica;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.DateFormat;

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

    public FormularioProyectos(int idRol) {

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
                Logica lj = new Logica();
                boolean exito = lj.registrarProyecto(nombre, ubicacion, fecha, estado);

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
    }


}
