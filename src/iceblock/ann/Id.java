package iceblock.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)

public @interface Id {
	
	int IDENTITY = 0;
	int ASSIGMENT = 1;
	
	int strategy() default IDENTITY;
	
}
