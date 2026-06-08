package Logica.LogicaHerencia;

import Logica.LogicaBase;

import java.sql.*;
import java.util.ArrayList;

public class LogicaAvances extends LogicaBase  {


    public LogicaAvances() {
        super();
    }

    // 1. Llena el ComboBox principal de la pestaña "Registrar"
    public ArrayList<String[]> cargarProyectos(){
        ArrayList<String[]> lista = new ArrayList<>();
        // Según tu SQL, filtramos para que solo salgan los proyectos activos[cite: 2]
        String sql = "SELECT id_proyecto, nombre, ubicacion FROM proyectos WHERE estado != 'Finalizado'";

        try (PreparedStatement pst = this.conexion.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                String[] proyecto = new String[3]; // Ahora necesitamos 3 espacios
                proyecto[0] = String.valueOf(rs.getInt("id_proyecto"));
                proyecto[1] = rs.getString("nombre");
                proyecto[2] = rs.getString("ubicacion"); // Guardamos la ubicación en el espacio 3

                lista.add(proyecto);
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar proyectos: " + e.getMessage()); // Manejo de errores RNF10[cite: 1]
        }
        return lista;
    }


    // 2. Trae el % de avance actual para la barra o campo de progreso (RF04)
    public double obtenerProgresoActual(int idProyecto) {
        // Usa tu función PL/pgSQL creada en Prototipo_4.sql[cite: 2]
        String sql = "SELECT COALESCE(MAX(porcentaje), 0) AS avance FROM avances WHERE id_proyecto = ?";
        try (PreparedStatement pst = this.conexion.prepareStatement(sql)) {

            pst.setInt(1, idProyecto);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getDouble("avance");
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener progreso actual: " + e.getMessage());
        }
        return 0.0;
    }

    public boolean guardarReporteGeneral(int idProy, double porcentaje, String desc, int idItem, Double nuevaCant) {
        try {

            // ¡Importante para RNF10! Desactivamos el autoguardado para asegurar la transacción completa[cite: 1]
            this.conexion.setAutoCommit(false);

            // 1. Registrar Avance (Cumple RF04)[cite: 1]
            String sqlAvance = "INSERT INTO avances (id_proyecto, fecha, porcentaje, descripcion) VALUES (?, CURRENT_DATE, ?, ?)";
            try (PreparedStatement pstAvance = this.conexion.prepareStatement(sqlAvance)) {
                pstAvance.setInt(1, idProy);
                pstAvance.setDouble(2, porcentaje);
                pstAvance.setString(3, desc);
                pstAvance.executeUpdate(); // Usamos executeUpdate para los INSERT
            }

            // ==========================================
            // 1.5 ¡NUEVO! Actualizar el estado del proyecto a 'En ejecución' automáticamente
            String sqlUpdateEstado = "UPDATE proyectos SET estado = 'En ejecución' WHERE id_proyecto = ?";
            try (PreparedStatement pstEstado = this.conexion.prepareStatement(sqlUpdateEstado)) {
                pstEstado.setInt(1, idProy);
                pstEstado.executeUpdate();
            }
            // ==========================================

            // 2. Modificar Ítem si el usuario lo solicitó (Cumple RF08)[cite: 1]
            if (nuevaCant != null && idItem > 0) {
                String sqlUpdate = "UPDATE items SET cantidad = ? WHERE id_item = ?"; // Tabla items[cite: 2]
                try (PreparedStatement pst = this.conexion.prepareStatement(sqlUpdate)) {
                    // Como en tu SQL la cantidad es INT, la convertimos para que no haya conflicto[cite: 2]
                    pst.setInt(1, nuevaCant.intValue());
                    pst.setInt(2, idItem);
                    pst.executeUpdate();
                }
            }

            // 3. Sincronizar el Presupuesto Total (Cumple RF08)
            // Hacemos el Bypass del procedimiento usando un UPDATE directo con una subconsulta
            String sqlPresupuesto = "UPDATE presupuestos SET costo_total = " +
                    "(SELECT COALESCE(SUM(cantidad * precio_unitario), 0) FROM items WHERE id_proyecto = ?) " +
                    "WHERE id_proyecto = ?";

            try (PreparedStatement pst2 = this.conexion.prepareStatement(sqlPresupuesto)) {
                pst2.setInt(1, idProy); // Este es para el WHERE de la tabla items
                pst2.setInt(2, idProy); // Este es para el WHERE de la tabla presupuestos
                pst2.executeUpdate(); // Usamos executeUpdate para modificar datos
            }

            // Si llegamos hasta aquí sin errores, confirmamos todo el bloque
            this.conexion.commit();
            //Le dice al formulario que la operacion fue exitosa
            return true;

        } catch (SQLException e) {
            // Si algo falla, el rollback deshace todo para no dejar la base de datos corrupta (RNF10)[cite: 1]
            if (this.conexion != null) {
                try {
                    this.conexion.rollback();
                } catch (SQLException ex) {
                    System.err.println("Error crítico en rollback: " + ex.getMessage());
                }
            }
            System.err.println("Error en guardarReporteGeneral: " + e.getMessage());
            //Le dice al formulario que la operacion fallo
            return false;
        } 


    }

    public ArrayList<String[]> cargarItemsPorProyecto(int idProyecto) {
        ArrayList<String[]> lista = new ArrayList<>();
        String sql = "SELECT id_item, descripcion, cantidad FROM items WHERE id_proyecto = ?";

        try (PreparedStatement pst = this.conexion.prepareStatement(sql)) {

            pst.setInt(1, idProyecto);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String[] item = new String[3];
                item[0] = String.valueOf(rs.getInt("id_item"));
                item[1] = rs.getString("descripcion");
                item[2] = String.valueOf(rs.getInt("cantidad"));
                lista.add(item);
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar ítems: " + e.getMessage());
        }
        return lista;
    }





}
