package Logica.LogicaHerencia;

import ConexionRemota.ConexionBD;
import Logica.LogicaBase;
import Logica.OperacionesCRUD;

import java.sql.*;
import java.util.HashMap;

public class LogicaProyectos extends LogicaBase implements OperacionesCRUD {

    private String nombre;
    private String ubicacion;
    private String fecha;
    private String estado;
    private int idProyecto;

    public LogicaProyectos() {
        super();
    }

    public LogicaProyectos(String nombre, String ubicacion, String fecha, String estado) {
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.fecha = fecha;
        this.estado = estado;
    }

    @Override
    public boolean registrar() {
        // Preparamos el SQL.
        // Nota: Omitimos "id_proyecto" porque al ser SERIAL, la BD lo crea solo.
        // El::date es un truco de nivel pro en PostgreSQL para que convierta el String "YYYY-MM-DD" directamente a fecha.
        String sql = "INSERT INTO proyectos (nombre, ubicacion, fecha_inicio, estado) VALUES (?, ?, ?::date, ?)";
        try {
            // 3. Usamos PreparedStatement para evitar ataques de Inyección SQL (Seguridad)
            PreparedStatement pst = this.conexion.prepareStatement(sql);
            pst.setString(1, nombre);
            pst.setString(2, ubicacion);
            pst.setString(3, fecha);
            pst.setString(4, estado);

            // 4. Ejecutamos la inserción en Clever Cloud
            int filasInsertadas = pst.executeUpdate();

            // 5. Cerramos la conexión para no saturar el servidor remoto (Buena práctica)
            this.conexion.close();

            // Si insertó 1 o más filas, devuelve true (Éxito)
            return filasInsertadas > 0;

        } catch (SQLException e) {
            // Imprimimos el error en consola para que el desarrollador sepa qué falló
            System.err.println("Error SQL en registrarProyecto: " + e.getMessage());
            return false; // Devuelve false para que la interfaz muestre el mensaje de error
        }
    }

    @Override
    public boolean actualizar() {
        // 1. Sentencia SQL: Actualizamos el estado filtrando por el ID único
        String sql = "UPDATE proyectos SET estado = ? WHERE id_proyecto = ?";

        try (PreparedStatement pst = this.conexion.prepareStatement(sql)) {

            // 3. Cargamos los datos desde los atributos (la "mochila")
            // pst.set(posicion, valor)
            pst.setString(1, this.estado);      // Nuevo estado
            pst.setInt(2, this.idProyecto);     // ID del proyecto a modificar
            
            // 4. Ejecutamos y verificamos si hubo cambios
            return pst.executeUpdate() > 0;


        } catch (SQLException e) {
            System.err.println("Error al actualizar proyecto: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar() {
        // 1. Sentencia SQL de eliminación
        String sql = "DELETE FROM proyectos WHERE id_proyecto = ?";

        try (PreparedStatement pst = this.conexion.prepareStatement(sql)) {

            // 2. Usamos el ID que cargamos en la "mochila"
            pst.setInt(1, this.idProyecto);

            // 3. Ejecutamos y verificamos si borró algo
            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar proyecto: " + e.getMessage());
            return false;
        }
    }



    public HashMap<String, Integer> obtenerProyectosParaCombo() {
        // Creamos un "diccionario" donde la clave es el Nombre (String) y el valor es el ID (Integer)
        HashMap<String, Integer> mapaProyectos = new HashMap<>();

        // Consulta SQL basándonos estrictamente en tu tabla proyectos
        String sql = "SELECT id_proyecto, nombre FROM proyectos WHERE estado != 'Terminado'";

        try {
            Statement st = this.conexion.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                // Guardamos el nombre y el id correspondientes en el mapa
                int id = rs.getInt("id_proyecto");
                String nombre = rs.getString("nombre");

                mapaProyectos.put(nombre, id);
            }

            this.conexion.close(); // RNF01: Mantenemos las conexiones limpias
        } catch (SQLException e) {
            System.err.println("Error al cargar la lista de proyectos: " + e.getMessage());
        }

        return mapaProyectos;
    }

    public String obtenerEstadoProyecto(int idProyecto) {
        String estadoActual = "";
        String sql = "SELECT estado FROM proyectos WHERE id_proyecto = ?";

        try (PreparedStatement pst = this.conexion.prepareStatement(sql)) {

            pst.setInt(1, idProyecto);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                estadoActual = rs.getString("estado");

            }
            this.conexion.close();
        } catch (SQLException e) {
            System.err.println("Error al obtener el estado: " + e.getMessage());
        }
        return estadoActual;
    }


    public void setIdProyecto(int idProyecto) {
        this.idProyecto = idProyecto;
    }


    public void setEstado(String estado) {
        this.estado = estado;
    }
}
