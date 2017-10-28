package iceblock.connection;
import java.sql.Connection;

public class IceConnection{

	private Connection conn;
	private String identifier;
	
	public IceConnection(Connection _conn, String _identifier) {
		conn = _conn;
		identifier = _identifier;
	}
	
	public String getIdentifier() {
		return identifier;
	}
	
	public Connection getConnection() {
		return conn;
	}
	
}
