package iceblock;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import iceblock.auxiliar.OBJBuilder;

@SuppressWarnings("unchecked")
public class IBlock {
	
	// Devuelve lista de objetos
	public static <T> List<T> select(Connection conn, Class<T> aClass, String xql) throws SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException{
		
		String query = QueryBuilder.select(aClass, xql);
		//System.out.println(query);

		// Execute SQL
		Statement stmt = conn.createStatement();
		ResultSet result = stmt.executeQuery(query);
		
		// Objects build
		T object = (T) new Object();
		List<T> objects = new ArrayList<T>(); 
		
		while(result.next()) {
			OBJBuilder objBuilder = new OBJBuilder();
			object = objBuilder.build(aClass, result);
			objects.add(object); 
		}
		
		return objects;
		
	}
	
	// Devuelve null o objeto
	public static <T> T find(Connection conn, Class<T> aClass, Integer id) throws SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		
		String query = QueryBuilder.find(aClass,id);
		
		// Execute SQL
		Statement stmt = conn.createStatement();
		ResultSet result = stmt.executeQuery(query);
		
		// Build object
		T object = (T) new Object();
		
		if(!result.next()) {
			return null;
		} else {
			OBJBuilder objBuilder = new OBJBuilder();
			object = objBuilder.build(aClass,result);
			
			return object;
		}

	}
	
	// Devuelve ID de objeto insertado
	public static <T> Integer insert(Connection conn, Class<T> aClass, T object) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException, InstantiationException {
		
		String query = QueryBuilder.insert(aClass,object);
		
		Integer key = null;
		
		PreparedStatement result = conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
		int updated = result.executeUpdate();
		if (updated == 1) {
			ResultSet generatedKeys = result.getGeneratedKeys();
			if (generatedKeys.next()) {
				key = generatedKeys.getInt(1);
			}
		}
		
		return key;
		
	}
	
	// Devuelve cantidad de filas eliminadas
	public static <T> Integer delete(Connection conn, Class<T> aClass, String xql) throws SQLException {
		
		String query = QueryBuilder.delete(aClass, xql);
		
		// Execute SQL
		Statement stmt = conn.createStatement();
		return stmt.executeUpdate(query);
		
	}
	
}
