package iceblock.auxiliar;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;

import iceblock.ann.*;

public class INSBuilder {
	
	private String table;
	private ArrayList<String> columns = new ArrayList<String>();

	public String insert(Class<?> aClass) {
		table = Auxiliar.getTableName(aClass);
		return "INSERT INTO ";
	}
	
	public String table() {
		return table;
	}
	
	public String columns(Class<?> aClass) {
		
		String str = "";
		this.mapColumns(aClass);
		
		for(int i=0; i<columns.size();i++){
			
			if(i == columns.size()-1){
				str = str + "\t" + columns.get(i) + "\n";
			} else {
				str = str + "\t" + columns.get(i) + ",\n";
			}
					
		}
		
		System.out.println(columns.size());
		
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
					
				// Type OneToOne
				} else if (ann instanceof OneToOne){
					OneToOne annot = (OneToOne)ann;
					colName = annot.name();
			
				// Type OneToMany
				} else if (ann instanceof OneToMany) {
					OneToMany annot = (OneToMany)ann;
					colName = Auxiliar.getIDColumn(annot.getClass());
					
				// Type ManyToMany
				} else if (ann instanceof ManyToMany){
					ManyToMany annot = (ManyToMany)ann;
					//this.mapAnnotation(aClass,annot,attribute);
				}
				
				this.columns.add(colName);
				
			}
		}
	}
}
