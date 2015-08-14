package com.ibm.docs.test.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This is used to add test case Notes link, such as Notes://GREATWALL/4825791200274DE1/8067278252322BA74825799D001DEEBB
 * @author fvt
 *
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TestLink {
	String value() default "";
}
