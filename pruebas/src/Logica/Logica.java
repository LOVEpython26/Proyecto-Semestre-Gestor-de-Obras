package Logica;

import ConexionRemota.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Logica {
    private String conts;
    private String usuario;
    private String rol;
    private ConexionBD conexionBD;

    public Logica() {
        this.conexionBD = new ConexionBD();
    }

    public Logica(String conts, String usuario, String rol, ConexionBD conexionBD) {
        this.conts = conts;
        this.usuario = usuario;
        this.rol = rol;
        this.conexionBD = conexionBD;
    }

    public String getConts() {
        return conts;
    }

    public void setConts(String conts) {
        this.conts = conts;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public ConexionBD getConexionBD() {
        return conexionBD;
    }

    public void setConexionBD(ConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    public boolean validarUsuario(String usuarioInput, String contsInput) {
        // 1. La consulta SQL ajustada a los nombres exactos de tu tabla
        // Usamos "contraseña" (con ñ) y "nombre" (en lugar de nombre_usuario)
        String sql = "SELECT contraseña FROM usuarios WHERE nombre = ?";
        boolean coincide = false;

        try (Connection con = this.conexionBD.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            // Le pasamos el nombre (ej. "Juan Castro") que viene de la caja de texto
            pst.setString(1, usuarioInput);

            try (ResultSet rs = pst.executeQuery()) {
                // Si encontró al usuario en la BD...
                if (rs.next()) {
                    // ⚠️ Sacamos la contraseña de la BD usando el nombre exacto de la columna
                    String passDB = rs.getString("contraseña");

                    // Comparamos la de la BD con la que escribieron (contsInput)
                    if (passDB.equals(contsInput)) {
                        coincide = true; // ¡Son iguales!
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al validar el usuario: " + e.getMessage());
        }

        return coincide;
    }


}
