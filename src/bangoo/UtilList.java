package bangoo;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class UtilList {

	@SuppressWarnings("unchecked")
	public static <S,T> List<S> map(Class<S> newClass, List<T> aList, Method aMethod, Object ...args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		
		List<S> newList = new ArrayList<S>(); 
		
		for(T object : aList) {
			newList.add((S)aMethod.invoke(object, args));
		}
		
		return newList;
		
	}
	
}
