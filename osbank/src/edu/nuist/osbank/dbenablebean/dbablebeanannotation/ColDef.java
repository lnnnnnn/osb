package edu.nuist.osbank.dbenablebean.dbablebeanannotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target( ElementType.FIELD )
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ColDef {
	String value() default ""; //the colName in the table of this attr
	boolean off() default false; //whether the attr in the table 
}
