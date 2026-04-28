package Logica;

import  javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Pest {
    public JPanel panelPrincipal;
    private JTextField texto1;
    private JTextField texto2;
    private JButton comprobarBotton;
    private JLabel titulo;
    private JLabel caja1;
    private JLabel caja2;
    private Logica lj = new Logica();


    public Pest() {
        comprobarBotton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String correo = texto1.getText();
                String contraseña = texto2.getText();
                boolean val = lj.validarUsuario(correo,contraseña);

                if (val == true){
                    // --- ESTO ES LO QUE SE AGREGA PARA ABRIR LA OTRA VENTANA ---

                    // 1. Preparamos y mostramos la pantalla de "confirmacion"
                    JFrame frameConfirmacion = new JFrame("Confirmación");
                    frameConfirmacion.setContentPane(new confirmacion().panel1);
                    frameConfirmacion.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frameConfirmacion.pack();
                    frameConfirmacion.setLocationRelativeTo(null); // Para que salga en el centro
                    frameConfirmacion.setVisible(true);

                    // 2. Cerramos esta pantalla actual del Login
                    Window ventanaActual = SwingUtilities.getWindowAncestor(panelPrincipal);
                    if (ventanaActual != null) {
                        ventanaActual.dispose();
                    }

                } else {
                    // --- ESTO ES LO QUE SE AGREGA SI LA CONTRASEÑA ESTÁ MAL ---
                    // En vez de poner "Rechazado" en la caja, lanzamos una alerta visual
                    JOptionPane.showMessageDialog(null, "Datos incorrectos. Intenta de nuevo.", "Error de Acceso", JOptionPane.ERROR_MESSAGE);

                    // Limpiamos las cajas para que vuelva a escribir
                    texto1.setText("");
                    texto2.setText("");
                }
            }
        });
    }


}
