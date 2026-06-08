package Logica.LogicaHerencia;

import ConexionRemota.ConexionBD;
import Logica.LogicaBase;
import Logica.OperacionesCRUD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class LogicaItems extends LogicaBase implements OperacionesCRUD {

    int idProyecto;
    String descripcion;
    int cantidad;
    double precioUnitario;
    int idItem;

    public LogicaItems() {
        super();
    }

    public LogicaItems(int idProyecto, String descripcion, int cantidad, double precioUnitario) {
        this.idProyecto = idProyecto;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    @Override
    public boolean registrar() {
        // Consulta SQL respetando exactamente las columnas de tu tabla 'items'[cite: 2]
        String sql = "INSERT INTO items (id_proyecto, descripcion, cantidad, precio_unitario) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement pst = this.conexion.prepareStatement(sql);

            // Asignamos los valores a los signos de interrogación
            pst.setInt(1, idProyecto);         // El ID numérico que sacamos del HashMap
            pst.setString(2, descripcion);     // Ej: "Excavación"[cite: 2]
            pst.setInt(3, cantidad);           // Ej: 100[cite: 2]
            pst.setDouble(4, precioUnitario);  // Entrará como 0.0 por ahora, esperando al APU

            int filasAfectadas = pst.executeUpdate();

            this.conexion.close(); // RNF01: Cerramos la conexión para no saturar Clever Cloud

            // Si filasAfectadas es mayor a 0, significa que se insertó correctamente
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error SQL al registrar el ítem de obra: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean actualizar() {
        // 1. Sentencia SQL: Actualizamos las columnas que el usuario puede editar
        String sql = "UPDATE items SET descripcion = ?, cantidad = ? WHERE id_item = ?";

        // 2. Usamos tu conexión herederada

        try (PreparedStatement pst = this.conexion.prepareStatement(sql)) {

            // 3. Cargamos los datos (asegúrate de tener estos atributos en tu clase)
            pst.setString(1, this.descripcion); // Nueva descripción
            pst.setInt(2, this.cantidad);       // Nueva cantidad
            pst.setInt(3, this.idItem);         // ID del item para identificarlo

            // 4. Ejecutamos
            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar item: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar() {
        // 1. Sentencia SQL de borrado
        String sql = "DELETE FROM items WHERE id_item = ?";

        try (PreparedStatement pst = this.conexion.prepareStatement(sql)) {

            // 2. Usamos el ID cargado en la mochila
            pst.setInt(1, this.idItem);

            // 3. Ejecutamos y verificamos
            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar item: " + e.getMessage());
            return false;
        }
    }

    public HashMap<String, Integer> obtenerActividadesParaCombo() {
        HashMap<String, Integer> mapaActividades = new HashMap<>();

        // Cambia 'actividades' por el nombre de tu tabla real
        String sql = "SELECT id_item, descripcion FROM items";

        try (PreparedStatement pst = this.conexion.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                // Guardamos el Nombre como llave y el ID como valor
                mapaActividades.put(rs.getString("descripcion"), rs.getInt("id_item"));
            }

        } catch (SQLException e) {

            System.err.println("Error al cargar lista de actividades: " + e.getMessage());

        }

        return mapaActividades;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setIdItem(int idItem) {
        this.idItem = idItem;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
