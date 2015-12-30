/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Broadcast
 */
public class Conexion {

	private Connection conexion;
	private Statement enunciado;
	private static Conexion instancia;
	
	public static Conexion getInstancia(){
		if(instancia == null)
			instancia = new Conexion();
		return instancia;
	}
	
	public Connection getConexion(){
		return conexion;
	}
	
	public ResultSet hacerConsulta(String consulta){
		ResultSet resultado = null;
		try {
			resultado = enunciado.executeQuery(consulta);
		} catch (SQLException e) {
		}
		return resultado;
	}
	
	public void ejecutarConsulta(String sql){
		try {
			enunciado.execute(sql);
		} catch (SQLException e) {
		}
	}
	
      
    public Connection Conectarse() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
            System.out.println("Instancio el controlador correctamente!");
            conexion = DriverManager.getConnection("jdbc:sqlserver://127.0.0.1\\SQLEXPRESS:1433;databaseName=HuellasDigitales;user=pablo;password=Thetrojanhorse1317");
            System.out.println("Conexion realizada con exito!");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
            System.out.println(e.getMessage());
        }
        return conexion;
    }

    public void desconectar() {
        try {
            conexion.close();
            System.out.println("Se ha cerrado la conexión.");
        } catch (Exception e) {
            System.out.println("Error al cerrar la conexión.");
        }

    }
}
