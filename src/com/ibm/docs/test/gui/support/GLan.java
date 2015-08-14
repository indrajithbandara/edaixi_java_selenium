package com.ibm.docs.test.gui.support;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Announce a text case to execute in specified language/languages suite. The
 * announced test case will always be executed on a firefox driver. Impact with
 * hub test.
 * 
 * @author xinyutan@cn.ibm.com
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface GLan {
	String[] value() default { "en-US" };
}
