package iceblock.auxiliar;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;

import iceblock.Interceptor;
import iceblock.ann.*;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

@SuppressWarnings("unchecked")
public class OBJBuilder {

	int usedFields = 0;
	
	public <T> T build(Class<T> aClass, ResultSet result) throws InstantiationException, IllegalAccessException, SQLException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException{
				
		// Create object
		T obj = aClass.newInstance();
		T object = (T)Enhancer.create(aClass,getMethodInterceptor(obj));
		
		// Attr of class
		Field[] classFields = aClass.getDeclaredFields();
		
		for(Field field : classFields){
			
			// Is column
			if(field.isAnnotationPresent(Column.class)) {
				
				Object valueObject = result.getObject(usedFields+1);
				
				if(field.isAnnotationPresent(Id.class) && valueObject == null) {
					return null;
				}
				
				Auxiliar.setter(aClass,object,field,valueObject);
				usedFields = usedFields + 1;
				
			// Is OneToOne
			} else if (field.isAnnotationPresent(OneToOne.class)) {
				
				
				// Is OneToOne LAZY
				if(field.getAnnotation(OneToOne.class).fetchType() == OneToOne.LAZY) {
					
					Auxiliar.setter(aClass, object, field, null);
					usedFields = usedFields + 1;
					
				// Is OneToOne EAGER
				} else {
					
					Object nxtObject = this.build(field.getType(), result);
					Auxiliar.setter(aClass, object, field, nxtObject);
					usedFields = usedFields + 1;
					
				}
				
			// Is OneToMany
			} else if (field.isAnnotationPresent(OneToMany.class)) {
				
				Auxiliar.setter(aClass, object, field, null);
				
			// Is ManyToMany
			} else if (field.isAnnotationPresent(ManyToMany.class)) {
				
				Auxiliar.setter(aClass, object, field, null);
				
			}
			
		}
		
		return object;
		
	}
	
	public static MethodInterceptor getMethodInterceptor(Object obj){
		return new Interceptor(obj);
	}
	
}
