package com.ibm.docs.test.gui.pages;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import com.ibm.docs.test.gui.support.EnhancedWebDriver;
import com.ibm.docs.test.gui.support.EnhancedWebElement;
import com.ibm.docs.test.gui.support.EnhancedWebElements;
import com.ibm.docs.test.gui.support.Find;
import com.ibm.docs.test.gui.support.TestEnv;

/**
 * All page object classes must be the sub-class of the class. 
 * The superclass enables the page factory to initialize all fields annotated by FindBy. 
 * @author liuzhe@cn.ibm.com
 *
 */
public class BasePage {
	
	public EnhancedWebDriver driver;
	
	public TestEnv testEnv;
	
	public BasePage(EnhancedWebDriver driver) {
		this.driver = driver;
		this.testEnv = driver.getTestEnv();
		initElements(driver, this);
	}

	
	public void close() {
		driver.close();
	}

	public static void initElements(EnhancedWebDriver driver, Object object) {
		List<Field> fields = getAllFields(object.getClass());
		for (Field field : fields) {
			Find located = field.getAnnotation(Find.class);
			if (located == null || Modifier.isStatic(field.getModifiers()))
				continue;
			try {
				String description = field.getDeclaringClass().getName() + "." + field.getName();
				String locator = located.by().isEmpty() ? located.value() : located.by();
				field.setAccessible(true);
				if (field.getType() == EnhancedWebElement.class) {
					field.set(object, new EnhancedWebElement(driver, locator, located.timeout(), description));
				} else if (field.getType() == EnhancedWebElements.class) {
					field.set(object, new EnhancedWebElements(driver, locator, located.timeout(), description));
				}
			} catch (IllegalAccessException e) {
				throw new IllegalArgumentException(e);
			}
		}
	}
	
	protected static List<Field> getAllFields(Class<?> type) {
		ArrayList<Field> fields = new ArrayList<Field>();
		while(type != null) {
		    for (Field field: type.getDeclaredFields()) 
		        fields.add(field);
		    type = type.getSuperclass();
		}
		
	    return fields;
	}
}
