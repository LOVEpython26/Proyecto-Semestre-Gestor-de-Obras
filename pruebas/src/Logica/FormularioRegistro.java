package Logica;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormularioRegistro {
    public JPanel panelFormulario;
    private JLabel tituloFijado;
    private JLabel nombreUsuario;
    private JTextField nombreBox;
    private JLabel rolUsuario;
    private JComboBox desplegableRoles;
    private JTextField correoBox;
    private JLabel correoUsuario;
    private JButton registrarButton;
    private JLabel contraseñaUsuario;
    private JPasswordField passwordField1;
    private JLabel confrimarContraseña;
    private JPasswordField passwordField2;

    public FormularioRegistro() {
        // Lógica de tu botón adaptada a tus nombres de variables
        registrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = nombreBox.getText().trim();
                String correo = correoBox.getText().trim();
                String pass = new String(passwordField1.getPassword());
                String passConfirmacion = new String(passwordField2.getPassword()); // <-- Nuevo campo capturado
                int indexSeleccionado = desplegableRoles.getSelectedIndex();

                // 2. Validación (RNF08)

                // A. Revisar que ningún campo esté vacío
                if (nombre.isEmpty() || correo.isEmpty() || pass.isEmpty() || passConfirmacion.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos administrativos.", "Datos Incompletos", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // B. Revisar que hayan elegido un cargo real
                if (indexSeleccionado == 0) {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un cargo válido para asignar el perfil.", "Selección de Cargo", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // C. RECTIFICACIÓN DE CONTRASEÑA (Minimizar error humano)
                if (!pass.equals(passConfirmacion)) {
                    JOptionPane.showMessageDialog(null, "Las contraseñas no coinciden. Verifícalas para evitar errores de acceso.", "Error de Seguridad", JOptionPane.ERROR_MESSAGE);
                    // Limpiamos SOLO las contraseñas para que no tengan que volver a escribir el nombre y correo
                    passwordField1.setText("");
                    passwordField2.setText("");
                    return;
                }

                // 3. Enviar a base de datos (RNF03)
                Logica lj = new Logica();
                boolean exito = lj.registrarUsuario(nombre, correo, pass, indexSeleccionado);

                if (exito) {
                    JOptionPane.showMessageDialog(null, "¡Personal registrado exitosamente!", "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
                    // Limpiamos todos los campos para dejarlo listo para un nuevo registro
                    nombreBox.setText("");
                    correoBox.setText("");
                    passwordField1.setText("");
                    passwordField2.setText(""); // Limpiamos también la confirmación
                    desplegableRoles.setSelectedIndex(0);
                } else {
                    JOptionPane.showMessageDialog(null, "Error al registrar el usuario en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        });
    }

    private void createUIComponents() {
        String[] roles = {"--- Seleccione un Cargo ---","Administrador", "Ingeniero", "Operario"};

        // ¡LA SOLUCIÓN!
        // Usamos la variable global directamente sin ponerle "JComboBox" al principio
        desplegableRoles = new JComboBox(roles);

        // Empieza mostrando la instrucción
        desplegableRoles.setSelectedIndex(0);
    }





}
