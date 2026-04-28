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
        /* 1. La consulta SQL con JOIN.
         Traemos la 'contraseña' de la tabla 'usuarios' y el 'nombre' de la tabla 'roles'.
         Le ponemos alias a la tabla roles para que no haya confusión con la columna 'nombre'.
        */
        String sql = "SELECT u.contraseña, r.nombre AS nombre_rol " +
                "FROM usuarios u " +
                "INNER JOIN roles r ON u.id_rol = r.id_rol " +
                "WHERE u.nombre = ?";
        boolean coincide = false;

        try (Connection con = this.conexionBD.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            // Le pasamos el nombre (ej. "Ana Torres")
            pst.setString(1, usuarioInput);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    // Sacamos la contraseña de la BD
                    String passDB = rs.getString("contraseña");

                    // Comparamos la de la BD con la que escribieron
                    if (passDB.equals(contsInput)) {

                        // 2. ¡AQUÍ CAPTURAMOS EL ROL DIRECTO DESDE LA BD!
                        // Guardamos "Administrador", "Ingeniero" u "Operario"
                        this.rol = rs.getString("nombre_rol");

                        coincide = true;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al validar el usuario: " + e.getMessage());
        }

        return coincide;
    }


}
