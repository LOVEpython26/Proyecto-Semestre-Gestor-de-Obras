package Logica;

import ConexionRemota.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Logica {
    private String conts;
    private String usuario;
    private int idRol;
    private ConexionBD conexionBD;

    public Logica() {
        this.conexionBD = new ConexionBD();
    }

    public Logica(String conts, String usuario, int idRol, ConexionBD conexionBD) {
        this.conts = conts;
        this.usuario = usuario;
        this.idRol = idRol;
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

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public ConexionBD getConexionBD() {
        return conexionBD;
    }

    public void setConexionBD(ConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    public boolean validarUsuario(String usuarioInput, String contsInput) {
        String sql = "SELECT contraseña, id_rol FROM usuarios WHERE nombre = ?";
        boolean coincide = false;

        try (Connection con = this.conexionBD.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, usuarioInput);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    String passDB = rs.getString("contraseña");

                    if (passDB.equals(contsInput)) {
                        // Guardamos el ID del rol encontrado
                        this.idRol = rs.getInt("id_rol");
                        coincide = true;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en la validación BD: " + e.getMessage());
        }
        return coincide;
    }


}
