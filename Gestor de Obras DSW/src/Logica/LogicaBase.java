package Logica;

import ConexionRemota.ConexionBD;

import java.sql.Connection;

public class LogicaBase {

    protected Connection conexion;

    public LogicaBase() {

        ConexionBD conexbd = new ConexionBD();

        this.conexion = conexbd.getConnection();
    }
}
