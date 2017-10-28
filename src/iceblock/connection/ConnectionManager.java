package iceblock.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConnectionManager {

	private static IceConnection activeConn = null;
	private static List<IceConnection> connections = new ArrayList<IceConnection>();
	
	public static void create(String driver, String url, String user, String pass, String identifier) throws ClassNotFoundException, SQLException{
		
		// Empty identifier
		if(identifier == null || identifier.isEmpty() || identifier=="") {
			 throw new IllegalStateException("You must define an identifier");
		}
		
		// Identifier that already exists
		if(ConnectionManager.idExists(identifier)) {
			throw new IllegalStateException("Identifier already exists");
		}
		
		
		Class.forName(driver);
		
		Connection conn = DriverManager.getConnection(url,user,pass);
		System.out.println("Connected to " + url + " using " + driver);
		
		IceConnection iceConn = new IceConnection(conn,identifier);
		
		connections.add(iceConn);

	}
	
	public static void changeConnection(String identifier) {
		
		IceConnection iceConn = null;
		for(IceConnection conn: connections) {
			
			if(conn.getIdentifier().equals(identifier)) {
				iceConn = conn;
			}
			
		}
		
		if(iceConn == null) {
			throw new IllegalStateException("Non detected connection with that identifier");
		}
		
		activeConn = iceConn;
		
	}
	
	public static Connection getConnection() {
		
		if(activeConn == null) {
			throw new IllegalStateException("There is no active connection. Select one first");
		}
		
		return activeConn.getConnection();
	
	}
	
	
	
	public static boolean idExists(String identifier) {
		
		for(IceConnection conn: connections) {
			
			if(conn.getIdentifier().equals(identifier)) {
				return true;
			}
			
		}
		
		return false;
		
	}
	
	
	
}
