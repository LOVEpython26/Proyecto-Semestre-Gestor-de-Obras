package Logica;

import ConexionRemota.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LogicaUsuario {
    private String conts;
    private String usuario;
    private int idRol;
    private ConexionBD conexionBD;

    public LogicaUsuario() {
        this.conexionBD = new ConexionBD();
    }

    public LogicaUsuario(String conts, String usuario, int idRol, ConexionBD conexionBD) {
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

    public boolean registrarUsuario(String nombre, String correo, String pass, int idRol) {
        // Consulta SQL para insertar en tu tabla 'usuarios'
        String sql = "INSERT INTO usuarios (nombre, correo, contraseña, id_rol) VALUES (?, ?, ?, ?)";
        boolean exito = false;

        try (Connection con = this.conexionBD.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, nombre);
            pst.setString(2, correo);
            pst.setString(3, pass);
            pst.setInt(4, idRol);

            int filas = pst.executeUpdate();
            if (filas > 0) exito = true;

        } catch (SQLException e) {
            System.err.println("Error al registrar: " + e.getMessage());
        }
        return exito;
    }

    public boolean existeUsuario(String correo) {
        // Consulta SQL para contar cuántos usuarios tienen ese mismo correo
        String sql = "SELECT COUNT(*) FROM usuarios WHERE correo = ?";
        boolean existe = false;

        try (Connection con = this.conexionBD.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, correo);
            ResultSet rs = pst.executeQuery();

            // Si el conteo es mayor a 0, significa que el usuario ya existe
            if (rs.next() && rs.getInt(1) > 0) {
                existe = true;
            }

        } catch (SQLException e) {
            System.err.println("Error al verificar duplicidad: " + e.getMessage());
        }
        return existe;
    }

    public boolean existeContrasena(String pass) {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE contraseña = ?";
        boolean existe = false;

        try (Connection con = this.conexionBD.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, pass);
            ResultSet rs = pst.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                existe = true; // Sí encontró a alguien con esa misma clave
            }

        } catch (SQLException e) {
            System.err.println("Error al verificar duplicidad de contraseña: " + e.getMessage());
        }
        return existe;
    }


}
