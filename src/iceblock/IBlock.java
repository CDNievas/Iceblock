package iceblock;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import iceblock.auxiliar.OBJBuilder;

@SuppressWarnings("unchecked")
public class IBlock {
		
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
	
	public static <T> T find(Connection conn, Class<T> aClass, Integer id) throws SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		
		String query = QueryBuilder.find(aClass,id);
		//System.out.println(query);
		
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
	
	public static <T> void insert(Connection con, Class<T> aClass, T object, Integer id) {
		String query = QueryBuilder.insert(aClass,object,id);
		System.out.println(query);
	}
	
}
