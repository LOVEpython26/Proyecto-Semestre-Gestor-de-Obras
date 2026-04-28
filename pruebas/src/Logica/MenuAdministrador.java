package Logica;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuAdministrador {
    public JPanel panelAdmin;
    private JLabel titulofijado;
    private JButton registraUsuariosButton;

    public MenuAdministrador() {

        registraUsuariosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frameRegistro = new JFrame("Registro de Personal - Sistema DSW");
                frameRegistro.setContentPane(new FormularioRegistro().panelFormulario);
                frameRegistro.pack();
                frameRegistro.setLocationRelativeTo(null);
                frameRegistro.setVisible(true);

                // Configuración de la nueva ventana
                frameRegistro.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frameRegistro.pack();
                frameRegistro.setLocationRelativeTo(null);
                frameRegistro.setVisible(true);



                Window win = SwingUtilities.getWindowAncestor(panelAdmin);
                if (win != null) win.dispose();


            }
        });
    }
}
