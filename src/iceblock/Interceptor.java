package iceblock;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import iceblock.ann.*;
import iceblock.auxiliar.Auxiliar;
import iceblock.auxiliar.OBJBuilder;
import iceblock.connection.ConnectionManager;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class Interceptor implements MethodInterceptor {
	
	private Object target;

	public Interceptor(Object target){
		this.target=target;
	}

	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable{
		
		String mtdName=method.getName();
		
		if (mtdName.substring(0,3).equals("get")) {
			
			Connection conn = ConnectionManager.getConnection();
			
			String fieldName = mtdName.substring(3);
			fieldName = Auxiliar.decapitalize(fieldName);
						
			Field[] fields = target.getClass().getDeclaredFields();
			Field fieldDetected = Auxiliar.getFieldByName(fieldName, fields);
			
			if(fieldDetected.isAnnotationPresent(OneToOne.class)) {
				
				OneToOne annot = fieldDetected.getAnnotation(OneToOne.class);
				
				if(annot.fetchType() == OneToOne.LAZY) {
					
					// Execute query to get ID of OneToOne field
					
					String dbFieldName = annot.name();
					
					String tableName = Auxiliar.getTableName(target.getClass());
					
					String dbIdFieldName = Auxiliar.getIDColumn(target.getClass());
					
					Field idField = Auxiliar.getIDAttr(target.getClass());
										
					Integer idTarget = (Integer)Auxiliar.getter(target.getClass(), target, idField);
					
					String query = "SELECT " + dbFieldName + " FROM " + tableName + " WHERE " + tableName + "." + dbIdFieldName + "=" + idTarget;
										
					Statement stmt = conn.createStatement();
					ResultSet result = stmt.executeQuery(query);
					
					Integer idNxtObj = null;
					while(result.next()) {
						idNxtObj = (Integer) result.getObject(1);
					}
					
					if(idNxtObj == null) {
						//throw new IllegalStateException("Didn't find any result with that ID");
						return null;
					} else {
						
						// Execute query to get the other object in the relation
						Class<?> classNxtObj = fieldDetected.getType(); 
						return IBlock.find(conn, classNxtObj, idNxtObj);
						
					}
					
				} else {
					
					return proxy.invoke(target,args);
					
				}
				
				
			} else if(fieldDetected.isAnnotationPresent(OneToMany.class)){
				
				OneToMany annot = fieldDetected.getAnnotation(OneToMany.class);
				
				Class<?> type = annot.type();
				String attr = annot.attr();
				
				// Obtain table name
				String tableName = Auxiliar.getTableName(type);
				
				// Get field from table name
				Field[] typeFields = type.getDeclaredFields();
				Field fieldAttr = Auxiliar.getFieldByName(attr, typeFields);
				
				if (fieldAttr == null){
					throw new IllegalStateException("Didn't find field '" + attr + "' in " + type);
				}
				
				String dbFieldName = this.getAttr(fieldAttr);
				
				// Get ID value from target
				Field fieldId = Auxiliar.getIDAttr(target.getClass());
				Integer id = (Integer) Auxiliar.getter(target.getClass(), target, fieldId);
				
				// Build xql
				String xql = tableName + "." + dbFieldName + "=" + id;
								
				// Execute query
				return IBlock.select(conn, type, xql);
				
				
			} else if (fieldDetected.isAnnotationPresent(ManyToMany.class)) {
				
				ManyToMany annot = fieldDetected.getAnnotation(ManyToMany.class);
				
				Class<?> typeIn = target.getClass();
				Class<?> typeOut = annot.type();
								
				// Obtain table name
				String tableName = Auxiliar.getTableName(typeIn);
				
				// Obtain id field name
				String idFieldName = Auxiliar.getIDColumn(typeIn);
				
				// Get ID value from target
				Field fieldId = Auxiliar.getIDAttr(target.getClass());
				Integer id = (Integer) Auxiliar.getter(target.getClass(), target, fieldId);
								
				/*String query = "SELECT " + attrFieldName + " FROM " + tableName + " WHERE " + idFieldName + "=" + id;
				
				// Execute SQL
				Statement stmt = conn.createStatement();
				ResultSet result = stmt.executeQuery(query);
				
				if(!result.next()) {
					throw new IllegalStateException("Result not found with the ID");
				} else {
					id = result.getInt(1);
				}
				*/
				String xql = tableName + "." + idFieldName + "=" + id;
				
				// Get others parameters
				String hashTable = annot.hashTable();
				String colIn = annot.colIn();
				String colOut = annot.colOut();
				
				return this.selectManyToMany(conn, typeIn, typeOut, idFieldName, hashTable, colIn, colOut, xql);
				
				//return IBlock.select(conn, type, xql);
				
				//return null;
				
			} else {
				
				return proxy.invoke(target,args);
				
			}
						
		} else {
			
			return proxy.invoke(target, args);

		}		
			
	}
	
	private <T> List<T> selectManyToMany(Connection conn, Class<?> classIn, Class<T> classOut, String fieldName, String hashTable, String colIn, String colOut, String xql) throws SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException{
		
		
		// Generate SQL
		String query = QueryBuilder.selectManyToMany(classIn, classOut, fieldName, hashTable, colIn, colOut, xql);
		
		// Execute SQL
		Statement stmt = conn.createStatement();
		ResultSet result = stmt.executeQuery(query);
				
		// Objects build
		T object = (T) new Object();
		List<T> objects = new ArrayList<T>(); 
				
		while(result.next()) {
			OBJBuilder objBuilder = new OBJBuilder();
			object = objBuilder.build(classOut, result);
			objects.add(object); 
		}		
				
		return objects;
		
	}
	
	private String getAttr(Field field) {
		
		if (field.isAnnotationPresent(OneToOne.class)) {
			
			return field.getAnnotation(OneToOne.class).name();
		
		} else {
			
			throw new IllegalStateException("Bad relation construction. Attr can only refeer to  a field with @OneToOne annotation");
			
		}
		
	}

}
