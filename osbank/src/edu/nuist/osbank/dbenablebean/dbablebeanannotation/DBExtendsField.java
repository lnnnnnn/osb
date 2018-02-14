package edu.nuist.osbank.dbenablebean.dbablebeanannotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target( ElementType.FIELD )
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DBExtendsField {
	boolean value() default true; //the Field Should be Extends from super to children
	
}
