package Logica;

import  javax.swing.*;
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
                String nombreUsuario = texto1.getText();
                String contraseña = texto2.getText();
                boolean val = lj.validarUsuario(nombreUsuario,contraseña);
                if (val) { // Si val es true (El usuario existe y la clave coincide)
                    // Obtenemos el rol que acabamos de setear en la lógica
                    String miRol = lj.getRol();

                    // Mostramos el popup personalizado
                    JOptionPane.showMessageDialog(null,
                            "¡Acceso concedido!\nBienvenido " + nombreUsuario + ".\nTu rol es: " + miRol,
                            "Login Exitoso",
                            JOptionPane.INFORMATION_MESSAGE);

                } else { // Si la clave o el usuario están mal
                    // SOLO EL POPUP DE ERROR
                    JOptionPane.showMessageDialog(null, "Datos incorrectos. Intenta de nuevo.", "Error de Acceso", JOptionPane.ERROR_MESSAGE);

                    // Limpiamos las cajas para que vuelva a escribir
                    texto1.setText("");
                    texto2.setText("");
                }


            }
        });
    }

}
