package com.ibm.docs.test.gui.support;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to drive an object with a web driver
 * @author liuzhe@cn.ibm.com
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Drive {
	// session names
	String[] value() default {};
}