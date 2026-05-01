package Logica;

import ConexionRemota.ConexionBD;

import java.sql.*;
import java.util.HashMap;

public class LogicaProyectos {
    public boolean registrarProyecto(String nombre, String ubicacion, String fecha, String estado) {

        // 1. Instanciamos la clase de tu conexión (Ajusta los nombres si es necesario)
        ConexionBD conexion = new ConexionBD();
        Connection con = conexion.getConnection(); // Cambia "conectar()" si tu método se llama distinto

        // 2. Preparamos el SQL.
        // Nota: Omitimos "id_proyecto" porque al ser SERIAL, la BD lo crea solo.
        // El ::date es un truco de nivel pro en PostgreSQL para que convierta el String "YYYY-MM-DD" directamente a fecha.
        String sql = "INSERT INTO proyectos (nombre, ubicacion, fecha_inicio, estado) VALUES (?, ?, ?::date, ?)";

        try {
            // 3. Usamos PreparedStatement para evitar ataques de Inyección SQL (Seguridad)
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, nombre);
            pst.setString(2, ubicacion);
            pst.setString(3, fecha);
            pst.setString(4, estado);

            // 4. Ejecutamos la inserción en Clever Cloud
            int filasInsertadas = pst.executeUpdate();

            // 5. Cerramos la conexión para no saturar el servidor remoto (Buena práctica)
            con.close();

            // Si insertó 1 o más filas, devuelve true (Éxito)
            return filasInsertadas > 0;

        } catch (SQLException e) {
            // Imprimimos el error en consola para que el desarrollador sepa qué falló
            System.err.println("Error SQL en registrarProyecto: " + e.getMessage());
            return false; // Devuelve false para que la interfaz muestre el mensaje de error
        }
    }

    public HashMap<String, Integer> obtenerProyectosParaCombo() {
        // Creamos un "diccionario" donde la clave es el Nombre (String) y el valor es el ID (Integer)
        HashMap<String, Integer> mapaProyectos = new HashMap<>();

        ConexionBD conexion = new ConexionBD();
        Connection con = conexion.getConnection();

        // Consulta SQL basándonos estrictamente en tu tabla proyectos
        String sql = "SELECT id_proyecto, nombre FROM proyectos WHERE estado != 'Terminado'";

        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                // Guardamos el nombre y el id correspondientes en el mapa
                int id = rs.getInt("id_proyecto");
                String nombre = rs.getString("nombre");

                mapaProyectos.put(nombre, id);
            }

            con.close(); // RNF01: Mantenemos las conexiones limpias
        } catch (SQLException e) {
            System.err.println("Error al cargar la lista de proyectos: " + e.getMessage());
        }

        return mapaProyectos;
    }

}
