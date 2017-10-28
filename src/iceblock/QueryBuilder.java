package iceblock;

import iceblock.auxiliar.*;

public class QueryBuilder {
	
	public static String select(Class<?> aClass, String xql){
		
		SLTBuilder sltBuilder = new SLTBuilder();
		
		// SELECT 
		String query = sltBuilder.select(aClass);
		
		// Columns
		query = query + sltBuilder.columns(aClass);
		
		// FROM
		query = query + sltBuilder.from();
		
		// JOINS
		query = query + sltBuilder.join();
		
		// WHERE
		query = query + sltBuilder.where(xql);
		
		return query;
				
	}
	
	public static String find(Class<?> aClass, Integer id) {
		
		SLTBuilder sltBuilder = new SLTBuilder();
		
		// SELECT 
		String query = sltBuilder.select(aClass);
		
		// Columns
		query = query + sltBuilder.columns(aClass);
		
		// FROM
		query = query + sltBuilder.from();
		
		// JOINS
		query = query + sltBuilder.join();
		
		// WHERE
		query = query + sltBuilder.whereId(aClass, id);
		
		return query;
		
	}
	
	public static String selectManyToMany(Class<?> classIn, Class<?> classOut, String fieldName, String hashTable, String colIn, String colOut, String xql) {
		
		SLTBuilder sltBuilder = new SLTBuilder();

		// SELECT
		String query = sltBuilder.select(classIn);
		
		// Columns
		query = query + sltBuilder.columns(classOut);
		
		// FROM
		query = query + sltBuilder.from();
		
		// JOINS
		query = query + sltBuilder.joinManyToMany(classIn, classOut, fieldName, hashTable, colIn, colOut);
		
		// WHERE
		query = query + sltBuilder.where(xql);
		
		return query;
				
	}
	
	public static <T> String insert(Class<T> aClass, T object, Integer id){
		
		INSBuilder insBuilder = new INSBuilder();

		//INSERT
		String query = insBuilder.insert(aClass);
		
		//Table
		query = query + insBuilder.table();
		
		//Columns
		query = query + insBuilder.columns(aClass);
		
		//VALUES
		//query = query + insBuilder.values();
		
		//Object Values
		//query = query + insBuilder.objValues(aClass,object);
		
		return query;
	
	}
	
	public static <T> String delete(Class<T> aClass, String xql) {
		
		//DELBuilder delBuilder = new delBuilder();
		
		//DELETE
		//String query = delBuilder.delete();
		
		//Table
		//query = query + delBuilder.table();
		
		//WHERE
		//query = query + delBuilder.where(xql);
		
		return null;
		
	}
	
}