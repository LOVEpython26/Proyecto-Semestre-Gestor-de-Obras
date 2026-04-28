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
                String correo = texto1.getText();
                String contraseña = texto2.getText();
                boolean val = lj.validarUsuario(correo,contraseña);
                if (val == true){
                    texto1.setText("Estas dentro");
                }else {
                    texto1.setText("Rechazado");
                }


            }
        });
    }

}
