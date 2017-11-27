package iceblock.auxiliar;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import iceblock.IBlock;
import iceblock.ann.*;
import iceblock.connection.ConnectionManager;

public class INSBuilder {
	
	private String table;
	private ArrayList<String> columns = new ArrayList<String>();

	public String insert(Class<?> aClass) {
		this.table = Auxiliar.getTableName(aClass);
		return "INSERT INTO ";
	}
	
	public String table() {
		return this.table + "\n";
	}
	
	public String columns(Class<?> aClass) {
		
		String str = "(";
		this.mapColumns(aClass);
		
		for(int i=1; i<columns.size();i++){
			
			if(i == columns.size()-1){
				str = str + "\t" + columns.get(i) + "\n";
			} else {
				str = str + "\t" + columns.get(i) + ",\n";
			}
					
		}
		
		str = str + ")";
		
		return str;
		
	}
	
	public <T> String values(Class<T> aClass, T object) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException, InstantiationException {
		String str = "VALUES (";
	
		Field[] attrs = aClass.getDeclaredFields();
		
		ArrayList<String> values = new ArrayList<String>();
		
		for(Field attribute : attrs){
			
			// Get annotations of each attribute
			for(Annotation ann : attribute.getDeclaredAnnotations()){
														
				// Type Column
				if(ann instanceof Column){
					
					if((attribute.getType().getSimpleName()).equals("String")) {
						values.add("'"+(Auxiliar.getter(aClass, object, attribute).toString())+"'");
					} else {
						values.add((Auxiliar.getter(aClass, object, attribute).toString()));
					}
					
				// Type OneToOne
				} else if (ann instanceof OneToOne){
					OneToOne annot = (OneToOne) ann;
					
					Class<T> classValue = (Class<T>) attribute.getType();
					
					// Obtain relation object
					T objectValue = (T) Auxiliar.getter(aClass,object,attribute);
					
					if(objectValue == null) {
						
						values.add("null");
					
					} else {
						/* NO RECURSIVO
						// Obtain ID from relation object
						Field idAttr = Auxiliar.getIDAttr(classValue);
						
						Object idObjectValue = Auxiliar.getter(classValue, objectValue, idAttr);
						
						if(idObjectValue == null) {
							values.add("null");

						} else {
							values.add(idObjectValue.toString());
						}*/
						Field idAttr = Auxiliar.getIDAttr(classValue);
						Integer idObjectValue = (Integer) Auxiliar.getter(classValue, objectValue, idAttr);
						
						Connection conn = ConnectionManager.getConnection();

						if((IBlock.find(conn,classValue,idObjectValue)) == null) {
							idObjectValue = IBlock.insert(conn, classValue, objectValue);
						}
						
						values.add(idObjectValue.toString());

						
					}
																				
				}
							
			}
		}
		
		for(int i=1; i<columns.size();i++){
			
			if(i == values.size()-1){
				str = str + values.get(i);
			} else {
				str = str + values.get(i) + ",";
			}
					
		}

		str = str + ")";
		return str;
		
	}
	
	public void mapColumns(Class<?> aClass){
		
		Field[] attrs = aClass.getDeclaredFields();
		
		for(Field attribute : attrs){
			
			// Get annotations of each attribute
			for(Annotation ann : attribute.getDeclaredAnnotations()){
				
				String colName = "";
				
				// Type Column
				if(ann instanceof Column){
					Column annot = (Column)ann;
					colName = annot.name();
					this.columns.add(colName);
					
				// Type OneToOne
				} else if (ann instanceof OneToOne){
					OneToOne annot = (OneToOne)ann;
					colName = annot.name();
					this.columns.add(colName);
				}
				
			}
		}
	}
}
