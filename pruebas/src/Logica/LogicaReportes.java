package Logica;

import ConexionRemota.ConexionBD;

import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class LogicaReportes {
    // 1. Cargar Proyectos para el ComboBox
    public ArrayList<String> cargarProyectos() {
        ArrayList<String> lista = new ArrayList<>();
        String sql = "SELECT id_proyecto, nombre FROM proyectos ORDER BY id_proyecto";

        try (Connection con = new ConexionBD().getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                // Guarda el formato "ID - Nombre" para poder extraer el ID fácilmente luego
                lista.add(rs.getInt("id_proyecto") + " - " + rs.getString("nombre"));
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar proyectos: " + e.getMessage());
        }
        return lista;
    }

    // 2. Traer el Presupuesto Total del proyecto
    public String obtenerPresupuestoTotal(int idProyecto) {
        // Solo calculamos la suma de (cantidad * costo_unitario) de los ítems del proyecto.
        // Usamos COALESCE para que si no hay nada, devuelva 0 en vez de null.
        String sql = "SELECT COALESCE(SUM(a.cantidad * a.costo_unitario), 0) AS total_utilizado " +
                "FROM apu a " +
                "JOIN items i ON a.id_item = i.id_item " +
                "WHERE i.id_proyecto = ?";

        try (Connection con = new ConexionBD().getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, idProyecto);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                // Extraemos la suma total calculada
                return "$" + rs.getBigDecimal("total_utilizado").toString();
            }
        } catch (SQLException e) {
            System.err.println("Error al calcular presupuesto utilizado: " + e.getMessage());
        }
        return "$0";
    }

    // 3. Llenar la Tabla APU (Ítems Utilizados) cruzando apu, items y recursos
    public DefaultTableModel obtenerModeloTablaAPU(int idProyecto) {
        // Nombres de las columnas para table1
        DefaultTableModel modelo = new DefaultTableModel(new String[]{"Ítem", "Recurso", "Cantidad", "Costo Unitario"}, 0);

        // Consulta JOIN exacta a tu script SQL
        String sql = "SELECT i.descripcion AS item, r.nombre AS recurso, a.cantidad, a.costo_unitario " +
                "FROM apu a " +
                "JOIN items i ON a.id_item = i.id_item " +
                "JOIN recursos r ON a.id_recurso = r.id_recurso " +
                "WHERE i.id_proyecto = ?";

        try (Connection con = new ConexionBD().getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, idProyecto);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                modelo.addRow(new Object[]{
                        rs.getString("item"),
                        rs.getString("recurso"),
                        rs.getDouble("cantidad"),
                        "$" + rs.getBigDecimal("costo_unitario")
                });
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar APU: " + e.getMessage());
        }
        return modelo;
    }

    // 4. Compilar el historial de avances para el TextArea (Generación automática)
    public String compilarObservaciones(int idProyecto) {
        StringBuilder texto = new StringBuilder();
        String sql = "SELECT fecha, porcentaje, descripcion FROM avances WHERE id_proyecto = ? ORDER BY fecha ASC";

        try (Connection con = new ConexionBD().getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, idProyecto);
            ResultSet rs = pst.executeQuery();
            boolean hayDatos = false;

            while (rs.next()) {
                hayDatos = true;
                texto.append("Fecha: ").append(rs.getDate("fecha"))
                        .append(" | Avance: ").append(rs.getDouble("porcentaje")).append("%\n")
                        .append("Observación: ").append(rs.getString("descripcion")).append("\n\n");
            }

            if (!hayDatos) {
                texto.append("No hay observaciones o avances previos registrados para este proyecto.");
            }
        } catch (SQLException e) {
            System.err.println("Error al compilar observaciones: " + e.getMessage());
        }
        return texto.toString();
    }

    // 5. Llenar la Tabla del Historial de Reportes (Pestaña 2)
    public DefaultTableModel obtenerModeloHistorialReportes(int idProyecto) {
        DefaultTableModel modelo = new DefaultTableModel(new String[]{"Fecha del Reporte", "Resumen Corto"}, 0);
        String sql = "SELECT fecha, resumen FROM reportes WHERE id_proyecto = ? ORDER BY fecha DESC";

        try (Connection con = new ConexionBD().getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, idProyecto);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String resumenCompleto = rs.getString("resumen");
                String resumenCorto = resumenCompleto;

                // Si el texto tiene más de 60 caracteres, lo cortamos y le ponemos "..."
                if (resumenCompleto != null && resumenCompleto.length() > 60) {
                    resumenCorto = resumenCompleto.substring(0, 60) + "...";
                }

                modelo.addRow(new Object[]{
                        rs.getDate("fecha"),
                        resumenCorto
                });
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar historial de reportes: " + e.getMessage());
        }
        return modelo;
    }

    // 6. Guardar el nuevo Reporte en la base de datos
    public boolean guardarReporte(int idProyecto, String resumen) {
        // Se usa CURRENT_DATE para que PostgreSQL asigne la fecha del sistema automáticamente
        String sql = "INSERT INTO reportes (id_proyecto, fecha, resumen) VALUES (?, CURRENT_DATE, ?)";

        try (Connection con = new ConexionBD().getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, idProyecto);
            pst.setString(2, resumen);

            int filasAfectadas = pst.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al guardar el reporte: " + e.getMessage());
            return false;
        }
    }
}