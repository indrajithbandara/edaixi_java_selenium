package com.ibm.docs.test.gui.support;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to locate a web element with the given locator
 * @author liuzhe@cn.ibm.com
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Find {
	String by() default "";
	long timeout() default 10;
	String value() default "";
}