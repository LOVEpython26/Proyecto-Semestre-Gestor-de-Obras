package Logica;

import ConexionRemota.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LogicaItems {
    public boolean registrarItem(int idProyecto, String descripcion, int cantidad, double precioUnitario) {
        ConexionBD conexion = new ConexionBD();
        Connection con = conexion.getConnection();

        // Consulta SQL respetando exactamente las columnas de tu tabla 'items'[cite: 2]
        String sql = "INSERT INTO items (id_proyecto, descripcion, cantidad, precio_unitario) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement pst = con.prepareStatement(sql);

            // Asignamos los valores a los signos de interrogación
            pst.setInt(1, idProyecto);         // El ID numérico que sacamos del HashMap
            pst.setString(2, descripcion);     // Ej: "Excavación"[cite: 2]
            pst.setInt(3, cantidad);           // Ej: 100[cite: 2]
            pst.setDouble(4, precioUnitario);  // Entrará como 0.0 por ahora, esperando al APU

            int filasAfectadas = pst.executeUpdate();

            con.close(); // RNF01: Cerramos la conexión para no saturar Clever Cloud

            // Si filasAfectadas es mayor a 0, significa que se insertó correctamente
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error SQL al registrar el ítem de obra: " + e.getMessage());
            return false;
        }

    }
}
