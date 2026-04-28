package ConexionRemota;

import javax.swing.*;
import java.sql.*;

public class ConexionBD {
    private String url;
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    // Declaramos el usuario y contraseña globales para la clase
    private String userBD;
    private String pswBD;

    public ConexionBD() {
        String hostBD = "bltfuqqegb4rlc8w1w7m-postgresql.services.clever-cloud.com";
        String nombreBD = "bltfuqqegb4rlc8w1w7m";

        // ⚠️ OJO AQUÍ: Dejé las credenciales que tenías abajo en tu código.
        // Si no te conecta, cámbialas por el otro par de credenciales que tenías arriba.
        this.userBD = "uasxf3wbemmygdd2nukh";
        this.pswBD = "nM6sZw9KWGOxrjCLCLGtttAROiWXaR";

        // CAMBIO 1: Cambiamos "jdbc:mysql://" por "jdbc:postgresql://"
        this.url = "jdbc:postgresql://" + hostBD + ":50013/" + nombreBD;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()){
                // CAMBIO 2: Cambiamos el driver al de Postgres
                Class.forName("org.postgresql.Driver");

                // Usamos las credenciales que definimos en el constructor
                connection = DriverManager.getConnection(url, userBD, pswBD);
            }
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null,"Error en la conexión: "+e.getMessage());
            resultSet = null;
        }
        return connection;
    }

    private boolean ejecutarUpdate(String sql, String operacion) {
        boolean conf = false;
        try {
            statement = getConnection().createStatement();
            statement.executeUpdate(sql);
            conf = true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al " + operacion + ": " + e.getMessage());
        }
        return conf;
    }

    public boolean insertarDB(String sentenciasSQL){
        return ejecutarUpdate(sentenciasSQL,"Insertar");
    }

    public ResultSet consultarBD(String sentenciasSQL){
        try{
            statement = getConnection().createStatement();
            resultSet = statement.executeQuery(sentenciasSQL);
        }catch (SQLException e){
            JOptionPane.showMessageDialog(null,"Error en la consulta: "+ e.getMessage());
            resultSet = null;
        }
        return resultSet;
    }

    public boolean actualizarDB(String sentenciasSQL){
        return ejecutarUpdate(sentenciasSQL,"Actualizar");
    }

    public boolean borrarDB(String sentenciasSQL){
        return ejecutarUpdate(sentenciasSQL,"Borrar");
    }

    public boolean setAutoCommitBD(boolean bandera){
        try{
            getConnection().setAutoCommit(bandera);
            return true;
        }catch (SQLException e){
            return false;
        }
    }

    public boolean commitBD(){
        try{
            getConnection().commit();
            return true;
        }catch (SQLException e){
            return false;
        }
    }

    public void closeConnection(Connection connection){
        try{
            if (connection != null && !connection.isClosed()){
                connection.close();
            }
        }catch (SQLException e){
            JOptionPane.showMessageDialog(null,"Error al cerrar: "+e.getMessage());
        }
    }

    public void cerrarConexion(){
        closeConnection(this.connection);
    }

    public boolean rollbackBD(){
        try{
            getConnection().rollback();
            return true;
        }catch (SQLException e){
            return false;
        }
    }
}