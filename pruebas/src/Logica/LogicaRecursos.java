package Logica;

import ConexionRemota.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LogicaRecursos {
    public boolean registrarRecurso(String nombre, String tipo, double costo) {
        ConexionBD conexion = new ConexionBD();
        Connection con = conexion.getConnection();

        // SQL exacto basado en tu tabla 'recursos'
        String sql = "INSERT INTO recursos (nombre, tipo, costo) VALUES (?, ?, ?)";

        try {
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, nombre);
            pst.setString(2, tipo);
            pst.setDouble(3, costo);

            int rows = pst.executeUpdate();
            con.close(); // RNF01: Garantía de persistencia
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Error SQL al registrar recurso: " + e.getMessage());
            return false;
        }
    }

}
