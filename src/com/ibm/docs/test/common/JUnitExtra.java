package com.ibm.docs.test.common;

import java.util.HashMap;
import java.util.Hashtable;

import org.junit.runner.Description;

public class JUnitExtra {

	private static final Hashtable<Description, HashMap<String, String>> extras = new Hashtable<Description, HashMap<String, String>>();
	
	private static final HashMap<String, String> global = new HashMap<String, String>();
	
	public static HashMap<String, String> getExtra(Description description) {
		return extras.get(description);
	}
	
	public static HashMap<String, String> getExtra() {
		return global;
	}
	
	public static void setExtra(Description description, HashMap<String, String> properties) {
		extras.put(description, properties);
	}
	
	public static void setExtra(HashMap<String, String> properties) {
		global.putAll(properties);
	}
	
	
	public static void removeExtra(Description description) {
		extras.remove(description);
	}
}
