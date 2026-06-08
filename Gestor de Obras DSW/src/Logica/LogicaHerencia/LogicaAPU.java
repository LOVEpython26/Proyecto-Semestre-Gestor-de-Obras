package Logica.LogicaHerencia;

import ConexionRemota.ConexionBD;
import Logica.LogicaBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class LogicaAPU extends LogicaBase {

    public LogicaAPU() {
        super();
    }

    /**
     * 1. Cargar Proyectos
     * Trae los proyectos que están activos para el primer ComboBox.
     */
    public ArrayList<String[]> obtenerProyectos() {
        ArrayList<String[]> lista = new ArrayList<>();

        // Consulta a la tabla proyectos[cite: 1]
        String sql = "SELECT id_proyecto, nombre FROM proyectos WHERE estado != 'Terminado'";

        try (PreparedStatement pst = this.conexion.prepareStatement(sql);
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

        // Consulta a la tabla items filtrando por la llave foránea id_proyecto[cite: 1]
        String sql = "SELECT id_item, descripcion, cantidad FROM items WHERE id_proyecto = ?";

        try (PreparedStatement pst = this.conexion.prepareStatement(sql)) {

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

        // Consulta a la tabla recursos para traer el costo[cite: 1]
        String sql = "SELECT id_recurso, nombre, costo FROM recursos";

        try (PreparedStatement pst = this.conexion.prepareStatement(sql);
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

    /**
     * Guarda el APU completo: Inserta los recursos en la tabla 'apu'
     * y actualiza el precio total en la tabla 'items'.
     */
    public boolean guardarAnalisisCompleto(int idItem, double precioTotal, ArrayList<Object[]> datosTabla) {

        // 1. SQL para insertar cada material en la tabla apu
        String sqlAPU = "INSERT INTO apu (id_item, id_recurso, cantidad, costo_unitario) VALUES (?, ?, ?, ?)";

        // 2. SQL para actualizar el precio del ítem principal
        String sqlItem = "UPDATE items SET precio_unitario = ? WHERE id_item = ?";

        try {
            // Apagamos el guardado automático para proteger la base de datos si algo falla a la mitad
            this.conexion.setAutoCommit(false);

            // A. Guardar todos los recursos en la tabla APU
            try (PreparedStatement pstAPU = this.conexion.prepareStatement(sqlAPU)) {
                for (Object[] fila : datosTabla) {
                    pstAPU.setInt(1, idItem);
                    pstAPU.setInt(2, Integer.parseInt(fila[0].toString()));       // ID Recurso
                    pstAPU.setDouble(3, Double.parseDouble(fila[2].toString()));  // Rendimiento (Cantidad)
                    pstAPU.setDouble(4, Double.parseDouble(fila[3].toString()));  // Valor Unitario
                    pstAPU.addBatch(); // Lo agregamos a la "cola" de guardado
                }
                pstAPU.executeBatch(); // Ejecutamos toda la cola de un solo golpe
            }

            // B. Actualizar el precio total en la tabla ITEMS
            try (PreparedStatement pstItem = this.conexion.prepareStatement(sqlItem)) {
                pstItem.setDouble(1, precioTotal);
                pstItem.setInt(2, idItem);
                pstItem.executeUpdate();
            }

            // Si llegamos hasta aquí sin errores, confirmamos TODOS los cambios en la base de datos
            this.conexion.commit();
            //Le informa al formulario que la operacion fue exitosa
            return true;

        } catch (SQLException e) {
            System.err.println("Error crítico al guardar el APU: " + e.getMessage());
            return false;
        }
    }
}
