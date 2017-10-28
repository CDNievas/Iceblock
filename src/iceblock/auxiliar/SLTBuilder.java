package iceblock.auxiliar;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;

import iceblock.ann.*;
import iceblock.models.Join;

public class SLTBuilder {
	
	private String primaryTable;
	private ArrayList<String> columns = new ArrayList<String>();
	private ArrayList<String> tables = new ArrayList<String>();
	private ArrayList<Join> joins = new ArrayList<Join>();
	
	public String select(Class<?> aClass){
		String str = "SELECT ";
		primaryTable = Auxiliar.getTableName(aClass);
		return str;
	}
	
	public String columns(Class<?> aClass){
		
		String str = "";
		this.mapColumns(aClass);
		
		for(int i=0; i<columns.size();i++){
			
			if(i == columns.size()-1){
				str = str + "\t" + columns.get(i) + "\n";
			} else {
				str = str + "\t" + columns.get(i) + ",\n";
			}
					
		}
		
		return str;
		
	}
	
	public String from(){
		return "FROM " + primaryTable + "\n";
	}
	
	public String join(){
		
		String str="";
		
		for(int i=0; i<joins.size();i++){
			str = str + "LEFT JOIN \t" + joins.get(i).table + " ON " + joins.get(i).colIn + "=" + joins.get(i).colOut + "\n";
		}
		
		return str;
		
	}
	
	
	public String joinManyToMany(Class<?> classIn, Class<?> classOut, String fieldName, String hashTable, String colIn, String colOut){
		
		String str="";
		
		// Join IN
		String tableIn = Auxiliar.getTableName(classIn);
		str = str + "JOIN \t" + hashTable + " ON " + tableIn + "." + fieldName + "=" + hashTable + "." + colIn + "\n";
		
		// Join OUT
		String classOutIdField = Auxiliar.getIDColumn(classOut);
		String tableOut = Auxiliar.getTableName(classOut);
		str = str + "JOIN \t" + tableOut + " ON " + hashTable + "." + colOut+ "=" + tableOut + "." + classOutIdField  + "\n";
		
		for(int i=0; i<joins.size();i++){
			str = str + "LEFT JOIN \t" + joins.get(i).table + " ON " + joins.get(i).colIn + "=" + joins.get(i).colOut + "\n";
		}
		
		return str;
		
	}
	
	public String where(String xql) {
		
		String str = "";
		
		if(xql == null) {
			return str;
		} else if (xql.isEmpty() || xql.equals("")) {
			return str;
		} else {
			str = "WHERE " + xql;
			return str;
		}
		
	}
	
	public String whereId(Class<?> aClass,Integer id) {
		
		String str =  "WHERE " + primaryTable + "." + Auxiliar.getIDColumn(aClass) + "=" + id;		
		return str; 
		
	}
	
	public void mapColumns(Class<?> aClass){
		
		Field[] attrs = aClass.getDeclaredFields();
		
		for(Field attribute : attrs){
			
			// Get annotations of each attribute
			for(Annotation ann : attribute.getDeclaredAnnotations()){
				
				// Type Column
				if(ann instanceof Column){
					Column annot = (Column)ann;
					this.mapAnnotation(aClass,annot,attribute);
					
				// Type OneToOne
				} else if (ann instanceof OneToOne){
					OneToOne annot = (OneToOne)ann;
					this.mapAnnotation(aClass,annot,attribute);
			
				// Type OneToMany
				} else if (ann instanceof OneToMany) {
					OneToMany annot = (OneToMany)ann;
					this.mapAnnotation(aClass,annot,attribute);
				
				// Type ManyToMany
				} else if (ann instanceof ManyToMany){
					ManyToMany annot = (ManyToMany)ann;
					this.mapAnnotation(aClass,annot,attribute);
				}
				
			}
					
		}
		
	}
	
	// Map Column Annotation
	public void mapAnnotation(Class<?> aClass, Column annot, Field attribute){
		
		String table = Auxiliar.getTableName(aClass);
		String column = table + "." + attribute.getAnnotation(Column.class).name();		
		if(!columns.contains(column)){
			columns.add(column);
		}
			
	}
	
	// Map OneToOne Annotation
	public void mapAnnotation(Class<?> aClass, OneToOne annot, Field attribute){
		
		// LAZY
		if(annot.fetchType()==OneToOne.LAZY){
			this.mapColumnLazy(aClass,annot,attribute);
		// EAGER
		} else {
			this.mapColumnEager(aClass,annot,attribute);
		}
		
		
	}
	
	// Map OneToOne Annotation LAZY
	public void mapColumnLazy(Class<?> aClass, OneToOne annot, Field attribute){
		
		// Obtain table name
		String table = Auxiliar.getTableName(aClass);
			
		// Obtain ID column
		String idColumn = annot.name();
			
		String column = table + "." + idColumn; 
			
		if(!tables.contains(table)){
			tables.add(table);
		}
				
		if(!columns.contains(column)){
			columns.add(column);
		}
		
	}
	
	// Map OneToOne Annotation EAGER
	public void mapColumnEager(Class<?> aClass, OneToOne annot, Field attribute){
		
		Class<?> classJoin = attribute.getType();
		String tableIn = Auxiliar.getTableName(aClass);
		String tableOut = Auxiliar.getTableName(classJoin);
		
		if(!tables.contains(tableOut)){
			
			// Column In Join
			String colIn = tableIn + "." + annot.name();
			
			// Column Out Join
			String idColumn = Auxiliar.getIDColumn(classJoin);
			
			String colOut = tableOut + "." + idColumn;
			
			if(!columns.contains(colOut)){
				columns.add(colOut);
			}
			
			Join join = new Join(tableOut,colIn,colOut);
			joins.add(join);
			tables.add(tableOut);
			
		}
		
		this.mapColumns(classJoin);
		
	}
	
	// Map OneToMany Annotation
	public void mapAnnotation(Class<?> aClass, OneToMany annot, Field attribute){}
	
	// Map ManyToMany Annotation
	public void mapAnnotation(Class<?> aClass, ManyToMany annot, Field attribute){}
	
	
	
}
