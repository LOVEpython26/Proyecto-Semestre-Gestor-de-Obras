package Logica;

import ConexionRemota.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class LogicaAPU {

    /**
     * 1. Cargar Proyectos
     * Trae los proyectos que están activos para el primer ComboBox.
     */
    public ArrayList<String[]> obtenerProyectos() {
        ArrayList<String[]> lista = new ArrayList<>();
        ConexionBD conexion = new ConexionBD();

        // Consulta a la tabla proyectos[cite: 1]
        String sql = "SELECT id_proyecto, nombre FROM proyectos WHERE estado != 'Terminado'";

        try (Connection con = conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                // Guardamos ID [0] y Nombre [1]
                String[] proyecto = new String[2];
                proyecto[0] = String.valueOf(rs.getInt("id_proyecto"));
                proyecto[1] = rs.getString("nombre");
                lista.add(proyecto);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener proyectos para APU: " + e.getMessage());
        }
        return lista;
    }

    /**
     * 2. Cargar Ítems en Cascada
     * Depende del proyecto seleccionado. Trae la descripción y la cantidad.
     */
    public ArrayList<String[]> obtenerItemsPorProyecto(int idProyecto) {
        ArrayList<String[]> lista = new ArrayList<>();
        ConexionBD conexion = new ConexionBD();

        // Consulta a la tabla items filtrando por la llave foránea id_proyecto[cite: 1]
        String sql = "SELECT id_item, descripcion, cantidad FROM items WHERE id_proyecto = ?";

        try (Connection con = conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, idProyecto);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    // Guardamos ID [0], Descripción [1] y Cantidad Contratada [2]
                    String[] item = new String[3];
                    item[0] = String.valueOf(rs.getInt("id_item"));
                    item[1] = rs.getString("descripcion");
                    item[2] = String.valueOf(rs.getInt("cantidad"));
                    lista.add(item);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener ítems en cascada: " + e.getMessage());
        }
        return lista;
    }

    /**
     * 3. Cargar Recursos
     * Trae el catálogo de materiales y su precio base.
     */
    public ArrayList<String[]> obtenerRecursos() {
        ArrayList<String[]> lista = new ArrayList<>();
        ConexionBD conexion = new ConexionBD();

        // Consulta a la tabla recursos para traer el costo[cite: 1]
        String sql = "SELECT id_recurso, nombre, costo FROM recursos";

        try (Connection con = conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                // Guardamos ID [0], Nombre [1] y Costo [2]
                String[] recurso = new String[3];
                recurso[0] = String.valueOf(rs.getInt("id_recurso"));
                recurso[1] = rs.getString("nombre");
                recurso[2] = String.valueOf(rs.getBigDecimal("costo"));
                lista.add(recurso);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener recursos para APU: " + e.getMessage());
        }
        return lista;
    }
}
