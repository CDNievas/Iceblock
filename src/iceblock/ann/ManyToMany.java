package iceblock.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)

public @interface ManyToMany {

	Class<?> type();
	String hashTable();
	String colIn();
	String colOut();
	
}
