package Logica;

import  javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Pest {
    public JPanel panelPrincipal;
    private JTextField texto1;
    private JButton comprobarBotton;
    private JLabel titulo;
    private JLabel caja1;
    private JLabel caja2;
    private JPasswordField passwordBox;
    private Logica lj = new Logica();


    public Pest() {
        comprobarBotton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user = texto1.getText();
                String pass = passwordBox.getText();

                // Llamamos al método de la lógica
                if (lj.validarUsuario(user, pass)) {

                    // Si es correcto, obtenemos el ID del rol
                    int rol = lj.getIdRol();

                    // Decidimos qué panel mostrar según el rol
                    JFrame frameDestino = new JFrame();

                    switch (rol) {
                        case 1: // Administrador
                            frameDestino.setTitle("Panel de Administración - Registro de Personal (RF01)");
                            frameDestino.setContentPane(new MenuAdministrador().panelAdmin);
                            JOptionPane.showMessageDialog(null, "Bienvenido Administrador");
                            break;
                        case 2: // Ingeniero
                            frameDestino.setTitle("Panel de Ingeniería - Presupuestos (RF03)");
                            frameDestino.setContentPane(new MenuIngeniero().panelIngeniero);
                            JOptionPane.showMessageDialog(null, "Bienvenido Ingeniero");
                            break;
                        case 3: // Operario
                            frameDestino.setTitle("Panel Operativo - Avance de Obra (RF04)");
                            frameDestino.setContentPane(new MenuOperario().panelOperario);
                            JOptionPane.showMessageDialog(null, "Bienvenido Operario");
                            break;
                    }

                    // Configuración de la nueva ventana [cite: 33]
                    frameDestino.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frameDestino.pack();
                    frameDestino.setLocationRelativeTo(null);
                    frameDestino.setVisible(true);

                    // Cerramos el Login (Pest)
                    Window win = SwingUtilities.getWindowAncestor(panelPrincipal);
                    if (win != null) win.dispose();

                } else {
                    JOptionPane.showMessageDialog(null, "Acceso Denegado: Datos incorrectos ");
                }
            }
        });
    }

}
