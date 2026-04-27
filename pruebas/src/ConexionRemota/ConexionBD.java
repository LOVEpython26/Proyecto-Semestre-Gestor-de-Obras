package ConexionRemota;

import javax.swing.*;
import java.sql.*;

public class ConexionBD {
        private String url;
        private Connection connection;
        private Statement statement;
        private ResultSet resultSet;

        public ConexionBD() {
            String hostBD = "bltfuqqegb4rlc8w1w7m-postgresql.services.clever-cloud.com";
            String nombreBD = "bltfuqqegb4rlc8w1w7m";
            String userBD = "uasxf3wbemmygdd2nukh";
            String pswBD = "nM6sZw9KWGOxrjCLCLGtttAROiWXaR";

            this.url = "jdbc:mysql://"+hostBD+":50013/"+nombreBD;
        }

        public Connection getConnection() {
            try {
                if (connection == null || connection.isClosed()){
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    String userBD = "upowwfdvepzypvs3";
                    String pswBD = "fcavKSO5q6BGXmwSN99j";
                    connection = DriverManager.getConnection(url,userBD,pswBD);

                }
            } catch (ClassNotFoundException | SQLException e) {
                JOptionPane.showMessageDialog(null,"Error en la consulta: "+e.getMessage());
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
                JOptionPane.showMessageDialog(null,"Error al cerrar"+e.getMessage());
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
