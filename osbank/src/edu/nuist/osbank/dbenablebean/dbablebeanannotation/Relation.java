package edu.nuist.osbank.dbenablebean.dbablebeanannotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target( ElementType.FIELD )
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Relation {
	
	String rTable(); //relation table
	String sIdCol(); //the colname of this class id in the table
	String dIdCol(); //the colname of this  related class in the table
	String dType() default ""; // the col Type of this related class in the table, only for specify
	String indexCol() default "seq"; // the colname from array index
	boolean indexed() default false; // whether keep the order of array 
	boolean coDeal() default false; // whether update the relation table recodes when this class update 
	
}
