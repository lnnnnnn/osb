package edu.nuist.osbank.dbenablebean.dbablebeanannotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target( ElementType.TYPE )
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SP {
	String value() default "";
	String sps() default "";
	String table() default "";
	String create() default "";
}
