package Interfaces;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Pest {
    public JPanel panelPrincipal;
    private JTextField texto1;
    private JTextField texto2;
    private JButton copiarBoton;
    private JLabel titulo;
    private JLabel caja1;
    private JLabel caja2;

    public Pest() {
        copiarBoton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String texto = texto1.getText();
                texto2.setText(texto);
            }
        });
    }

}
