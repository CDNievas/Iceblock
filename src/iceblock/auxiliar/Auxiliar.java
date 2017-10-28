package iceblock.auxiliar;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import iceblock.ann.*;

public class Auxiliar {

	public static String getTableName(Class<?> aClass){
				
		if (!aClass.isAnnotationPresent(Table.class)){
			throw new IllegalStateException("Didn't find any attribute with @Table annotation" + aClass.getSimpleName());
		}
	
		return aClass.getAnnotation(Table.class).name();
		
	}
	
	public static Boolean isJavaClass(Field attribute){
		return attribute.getType().getName().contains("java.");
	}
	
	public static String getIDColumn(Class<?> aClass){
		
		String idColumn = null;
		
		for(Field attribute : aClass.getDeclaredFields()){
			if(attribute.isAnnotationPresent(Id.class)){
				idColumn = attribute.getAnnotation(Column.class).name();
			}
			
		}
		
		if (idColumn == null){
			throw new IllegalStateException("Didn't find any attribute with @ID annotation in '" + aClass.getSimpleName() + "'");
		} 
		
		return idColumn;
		
	}
	
	public static Field getIDAttr(Class<?> aClass) {
		
		Field idAttr = null;
		
		for(Field attribute : aClass.getDeclaredFields()){
			if(attribute.isAnnotationPresent(Id.class)){
				idAttr = attribute;
			}
			
		}
		
		if (idAttr == null){
			throw new IllegalStateException("Didn't find any attribute with @ID annotation in '" + aClass.getSimpleName() + "'");
		} 
		
		return idAttr;
		
	}

	public static <T> void setter(Class<T> aClass, T object, Field field, Object valueObject) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		String setterName = "set" + Auxiliar.capitalize(field.getName());		
		Method setterMethod = aClass.getDeclaredMethod(setterName, field.getType());
		
		setterMethod.invoke(object, valueObject);
		
	}
	
	public static <T> Object getter(Class<? extends Object> aClass, Object target, Field field) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		String getterName = "get" + Auxiliar.capitalize(field.getName());				
		Method getterMethod = aClass.getDeclaredMethod(getterName);
		
		return getterMethod.invoke(target);
		
	}
	
	public static Field getFieldByName(String fieldName, Field[] fields) {
		
		Field fieldDetected = null;
		
		for(Field field : fields) {
							
			if(field.getName().equals(fieldName)) {
				fieldDetected = field;
			}
			
		}
		
		return fieldDetected;
		
	}
	
	public static String capitalize(final String line) {
		return Character.toUpperCase(line.charAt(0)) + line.substring(1);
	}

	public static String decapitalize(String line) {
		return Character.toLowerCase(line.charAt(0)) + line.substring(1);
	}
	
}
