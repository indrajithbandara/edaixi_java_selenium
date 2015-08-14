package com.ibm.docs.test.common;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

/**
 * The class is accessed to configuration properties file.
 * By default, the properties file is located in config/default.properties. 
 * Set a file path or url to system property "test.config" to overwrite it.
 * @author liuzhe@cn.ibm.com
 *
 */
public class Config {
	private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\$\\{([^\\}]+)\\}");
	
	public static final String HOME = System.getenv("TEST_HOME") == null ? System.getProperty("user.dir") : System.getenv("TEST_HOME"); 
	
	private static final Logger log = Logger.getLogger(Config.class);
	
	private static final Config instance = new Config();
	
	private Properties properties = new Properties();
	
	
	
	public static Config getInstance() {
		return instance;
	}

	private static Properties loadProperties(URL url) {
		if (url == null)
			return null;
		
		InputStream inputStream = null;
		try {
			inputStream = url.openStream();
			Properties properties = new Properties();
			properties.load(inputStream);
			String include = properties.getProperty("include");
			if (include != null) {
				URL includeUrl = null;
				try {
					includeUrl = new URL(include);
				} catch (MalformedURLException e) {
					File includeFile = new File(include);
					if (!includeFile.isAbsolute()) 
						include = FilenameUtils.normalize(url.getPath() + "/../" + include, true);
					else {
						include = FilenameUtils.normalize(include, true);
						if (File.separatorChar == '\\') 
							include = '/' + include;
					}
					
					try {
						includeUrl = new URL(url, include);
					} catch (MalformedURLException e1) {
						
					}
				}
				
				Properties includeProperties = loadProperties(includeUrl);
				if (includeProperties != null) {
					includeProperties.putAll(properties);
					properties = includeProperties;
				}
			}
			properties.remove("include");
			return properties;
		} catch (IOException e) {
			log.debug(e);
			return null;
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
	}
	
	protected Config() {
		String configLocation = System.getProperty("test.config");
		URL url = null;
		if (configLocation == null)
			try {
				url = new File(HOME, "config/default.properties").toURI().toURL();
			} catch (MalformedURLException e2) {
			}
		else {
			try {
				url = new URL(configLocation);
			} catch (MalformedURLException e) {
				try {
					url = new File(configLocation).toURI().toURL();
				} catch (MalformedURLException e1) {
				}
			}
		}
		
		properties = loadProperties(url);
		if (properties == null)
			throw new ConfigException(String.format("Failed to load configuration. please check if the file '%s' is available!", configLocation));
	}
	
	protected Config(Properties properties) {
		this.properties = properties;
	}
	
	
	/**
	 * Get an expanded value
	 * @param key Property key
	 * @param defaultValue The default value returned when the key does not exist
	 * @return
	 */
	public String get(String key, String defaultValue) {
		return expand(getValue(key, defaultValue));
	}

	/**
	 * Get an expanded value. If key1 is not found, key2 will be used. If key2 is still not found, default value will be used.
	 * @param key1
	 * @param key2
	 * @param defaultValue
	 * @return
	 */
	public String get(String key1, String key2, String defaultValue) {
		String v = get(key1);
		if (v == null)
			v = get(key2, defaultValue);
		return v;
	}
	
	/**
	 * Get an expanded value. Return null when the key doesn't exist
	 * @param key Property key
	 * @return
	 */
	public String get(String key) {
		return get(key, (String)null);
	}

	/**
	 * Get the value of the given key If the key is not found, defaultValue will be used. Note the value will not be expanded.
	 * The search order:
	 * 1. system properties
	 * 2. env variable
	 * 3. properties file
	 * @param key
	 * @return
	 */
	public String getValue(String key, String defaultValue) {
		//Search in system properties
		String value = System.getProperty(key);
		if (value != null)
			return value;
		//Search in env variables
		if (key.startsWith("env."))
			value = System.getenv().get(key.substring(4));
		if (value != null)
			return value;
		return properties.getProperty(key, defaultValue);
	}
	
	/**
	 * Get the value of the given key If the key is not found, null will be returned. Note the value will not be expanded.
	 * @param key
	 * @see get(String key, String defaultValue)
	 * @return
	 */
	public String getValue(String key) {
		return getValue(key, null);
	}
	
	/**
	 * Expand the given string. The variable in the string will be replaced with it value.
	 * e.g. Define properties like the following </br>
	 * app.home=C:/apphome </br>
	 * app.executable=${app.home}/app.exe </br>
	 * Invoke the method </br>
	 * expand("${app.home}/app.exe")</br>
	 * You get "C:/apphome/app.ext"
	 * @param text
	 * @return
	 */
	public String expand(String text) {
		if (text == null)
			return text;
		
		Matcher matcher = VARIABLE_PATTERN.matcher(text);
		StringBuffer result = new StringBuffer();
		while (matcher.find()) {
			String str = get(matcher.group(1));
			if (str == null)
				str = matcher.group();
			matcher.appendReplacement(result, str.replace("\\", "\\\\")
					.replace("$", "\\$"));
		}
		matcher.appendTail(result);
		return result.toString();
	}


	public String getTestspace() {
		return get("testspace", FilenameUtils.normalize(Config.HOME + "/../testspace"));
	}

}
