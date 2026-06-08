package Logica.LogicaHerencia;

import ConexionRemota.ConexionBD;
import Logica.LogicaBase;
import Logica.OperacionesCRUD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class LogicaRecursos extends LogicaBase implements OperacionesCRUD {

    String nombre;
    String tipo;
    double costo;
    int idrecurso;

    public LogicaRecursos() {
        super();
    }

    public LogicaRecursos(String nombre, String tipo, double costo) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.costo = costo;
    }

    @Override
    public boolean registrar() {
        // SQL exacto basado en tu tabla 'recursos'
        String sql = "INSERT INTO recursos (nombre, tipo, costo) VALUES (?, ?, ?)";

        try {
            PreparedStatement pst = this.conexion.prepareStatement(sql);
            pst.setString(1, nombre);
            pst.setString(2, tipo);
            pst.setDouble(3, costo);

            int rows = pst.executeUpdate();
            this.conexion.close(); // RNF01: Garantía de persistencia
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Error SQL al registrar recurso: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean actualizar() {
        // 1. Sentencia SQL: Actualizamos las columnas que el usuario puede editar
        String sql = "UPDATE recursos SET costo = ? WHERE id_recurso = ?";

        // 2. Usamos tu conexión herederada

        try (PreparedStatement pst = this.conexion.prepareStatement(sql)) {

            // 3. Cargamos los datos (asegúrate de tener estos atributos en tu clase)
            pst.setDouble(1,this.costo);
            pst.setInt(2,idrecurso);

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
        String sql = "DELETE FROM recursos WHERE id_recurso = ?";

        try (PreparedStatement pst = this.conexion.prepareStatement(sql)) {

            // 2. Usamos el ID cargado en la mochila
            pst.setInt(1, this.idrecurso);

            // 3. Ejecutamos y verificamos
            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar item: " + e.getMessage());
            return false;
        }
    }

    public HashMap<String, Integer> obtenerRecursosParaCombo() {
        HashMap<String, Integer> mapaActividades = new HashMap<>();

        // Cambia 'actividades' por el nombre de tu tabla real
        String sql = "SELECT id_recurso, nombre FROM recursos";

        try (PreparedStatement pst = this.conexion.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                // Guardamos el Nombre como llave y el ID como valor
                mapaActividades.put(rs.getString("nombre"), rs.getInt("id_recurso"));
            }

        } catch (SQLException e) {

            System.err.println("Error al cargar lista de recursos: " + e.getMessage());

        }

        return mapaActividades;
    }

    public void setIdrecurso(int idrecurso) {
        this.idrecurso = idrecurso;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }
}
