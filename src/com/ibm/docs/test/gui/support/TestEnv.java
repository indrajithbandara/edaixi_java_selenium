package com.ibm.docs.test.gui.support;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Appender;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.ibm.docs.test.common.Config;
import com.ibm.docs.test.common.Resource;
import com.ibm.docs.test.common.ResourcePool;
import com.ibm.docs.test.common.Shadow;
import com.ibm.docs.test.common.TestLogAppender;
import com.ibm.docs.test.common.Utils;

/**
 * This class is the facade to enable a test environment. It manages a variety
 * of resources for a test. It will automatically initialize the fields of test
 * class annotated with DriveBy
 * 
 * @author liuzhe@cn.ibm.com
 * 
 */
public class TestEnv {
	private static final Logger log = Logger.getLogger(TestEnv.class);

	private HashMap<String, EnhancedWebDriver> webDrivers = new HashMap<String, EnhancedWebDriver>();

	private File testspace = new File(Config.getInstance().getTestspace());

	protected File tempDir = new File(testspace, "temp");

	private File outputDir = new File(testspace, "output");

	protected TestLogAppender testLogAppender = null;

	protected HashMap<String, String> outputProperties = new HashMap<String, String>();

	protected ArrayList<Resource> resources = new ArrayList<Resource>();

	protected HashMap<String, Object> data = new HashMap<String, Object>();

	protected String gLanguage = null;

	public TestEnv() {
		Logger logger = Logger.getRootLogger();
		if (logger != null) {
			Appender appender = logger.getAppender("testlog");
			if (appender instanceof TestLogAppender)
				testLogAppender = (TestLogAppender) appender;
		}
	}

	/**
	 * Initialize fields annotated by DriveBy in the given object
	 * 
	 * @param object
	 */
	public void initDriveBy(Object object) {
		try {
			// Search all fields annotated by DriveBy
			Field fs[] = object.getClass().getDeclaredFields();
			for (Field f : fs) {
				Drive a = f.getAnnotation(Drive.class);
				if (a != null && !Modifier.isStatic(f.getModifiers())) {
					f.setAccessible(true);
					f.set(object, mkDriveByObject(f.getType(), a.value()));
				}
			}
		} catch (TestEnvExcepiton e1) {
			throw e1;
		} catch (Exception e2) {
			throw new TestEnvExcepiton(
					"Test code has error! Check if annotation DriveBy is used correctly!",
					e2);
		}
	}

	/**
	 * Construct the object with the given driver name
	 * 
	 * @param declaredClass
	 * @param webDriverNames
	 * @return
	 * @throws Exception
	 */
	protected Object mkDriveByObject(Class<?> declaredClass,
			String[] webDriverNames) throws Exception {
		if (webDriverNames == null || webDriverNames.length == 0)
			webDriverNames = new String[] { "default" };

		if (webDriverNames.length < 2
				|| !"true".equalsIgnoreCase(Config.getInstance()
						.get("shadowed"))) {
			return declaredClass.getConstructor(EnhancedWebDriver.class)
					.newInstance(getWebDriver(webDriverNames[0]));
		} else {
			Object[][] shadows = new Object[webDriverNames.length - 1][];
			for (int i = 0; i < shadows.length; i++) {
				shadows[i] = new Object[] { getWebDriver(webDriverNames[i + 1]) };
			}
			return Shadow.createShadowed(declaredClass,
					new Class<?>[] { WebDriver.class },
					new Object[] { getWebDriver(webDriverNames[0]) }, shadows);
		}

	}

	/**
	 * Get a WebDriver with the given name. If it's not created yet, create a
	 * new one. </br> The created WebDriver will be managed centrally. When test
	 * is over, all these WebDriver will be closed.
	 * 
	 * @param name
	 * @return
	 */
	public EnhancedWebDriver getWebDriver(String name) {
		EnhancedWebDriver webDriver = webDrivers.get(name);
		if (webDriver == null) {
			DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
			// desiredCapabilities.setCapability(CapabilityType.SUPPORTS_ALERTS,
			// true);
			desiredCapabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS,
					true);
			// desiredCapabilities.setCapability(CapabilityType.HAS_NATIVE_EVENTS,
			// true);
			desiredCapabilities.setCapability(
					InternetExplorerDriver.ENABLE_PERSISTENT_HOVERING, false);
			// desiredCapabilities.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING,
			// true);
			String browser = Config.getInstance().get(
					"session." + name + ".browser", "browser", "");
			String[] tokens = browser.split(",");
			int i = 0;
			String hub = null, browserName = null, browserVersion = null;
			String token = tokens[i++].trim();
			if (token.startsWith("http://")) {
				hub = token;
				if (i < tokens.length)
					browserName = tokens[i++].trim();
			} else {
				browserName = token;
			}
			if (i < tokens.length)
				browserVersion = tokens[i++].trim();
			if (browserName == null || browserName.isEmpty())
				browserName = "any";
			if (browserVersion == null || browserVersion.isEmpty())
				browserVersion = "any";
			desiredCapabilities.setBrowserName(browserName);
			desiredCapabilities.setVersion(browserVersion);

			log.info(String.format(
					"Start session '%s' with browser %s version %s", name,
					browserName, browserVersion));
			WebDriver underlyingWebDriver = null;
			if (hub != null) {
				try {
					underlyingWebDriver = new RemoteWebDriver(new URL(hub),
							desiredCapabilities);
				} catch (MalformedURLException e) {
					throw new TestEnvExcepiton(
							String.format(
									"Failed to start session '%s' with browser %s.The hub url is invlid!.",
									name, desiredCapabilities));
				} catch (WebDriverException e) {
					throw new TestEnvExcepiton(String.format(
							"Failed to start session '%s' with browser %s.",
							name, desiredCapabilities), e);
				}
			} else {
				if (gLanguage == null) {
					underlyingWebDriver = DriverFactory
							.newDriver(desiredCapabilities);
				} else {
					underlyingWebDriver = DriverFactory.newDriver(
							desiredCapabilities, gLanguage);
				}
			}

			if (underlyingWebDriver == null)
				throw new TestEnvExcepiton(
						String.format(
								"Failed to start session '%s' with browser %s. Check if browser property is specified correctly and browser is installed.",
								name, desiredCapabilities));
			webDriver = new EnhancedWebDriver(underlyingWebDriver, name, this);
			log.debug(String.format("Session '%s' started with browser %s",
					name, webDriver.getCapabilities()));
			webDrivers.put(name, webDriver);
			try {
				webDriver.configHosts();
				webDriver.configCert();
			} catch (Exception e) {
				log.warn("Certificate is not installed", e);
			}

		}
		return webDriver;
	}

	/**
	 * Get all web drivers
	 * 
	 * @return
	 */
	public Collection<? extends EnhancedWebDriver> getWebDrivers() {
		return webDrivers.values();
	}

	/**
	 * Get a file for test output. Anything which can help to analyze the test
	 * result. e.g. screenshot, log, tested application's crash log, etc. The
	 * file will not be cleared automatically or overwritten.
	 * 
	 * @param name
	 * @return
	 */
	public File getOutputFile(String name) {
		outputDir.mkdirs();
		return new File(outputDir, name);
	}

	/**
	 * Get a temporary file for test. call clear() will remove all the files
	 * 
	 * @param name
	 * @return
	 */
	public File getTempFile(String name) {
		tempDir.mkdirs();
		return new File(tempDir, name);
	}

	/**
	 * 
	 * @param path
	 * @return
	 */
	public File getDataFile(String path) {
		return new File(Config.HOME + "/data", path);
	}

	/**
	 * Set output directory
	 */
	public void setOutputDir(String path) {
		outputDir = new File(testspace, path);
	}

	/**
	 * Set temporary directory
	 */
	public void setTempDir(String path) {
		tempDir = new File(testspace, path);
	}

	public String getOutputProperty(String key) {
		return outputProperties.get(key);
	}

	public void setOutputProperty(String key, String value) {
		outputProperties.put(key, value);
	}

	/**
	 * Call this method when the test is started
	 */
	public void start() {
		if (testLogAppender != null)
			testLogAppender.attach(getOutputFile("log.html"));
	}

	/**
	 * Release all resources used by the test.
	 */
	public void dispose() {
		for (Resource resource : resources)
			resource.release();
		for (Iterator<EnhancedWebDriver> i = webDrivers.values().iterator(); i
				.hasNext();)
			i.next().quit();
		webDrivers.clear();
		data.clear();
		FileUtils.deleteQuietly(tempDir);
		if (testLogAppender != null)
			testLogAppender.detach();
		gLanguage = null;

	}

	public String acquireResource(String name) {
		Resource resource = ResourcePool.acquire(name, 60000 * 10);
		if (resource == null)
			throw new TestEnvExcepiton("No resource avaliable!");
		resources.add(resource);
		return resource.getValue();
	}

	public void setData(String name, Object obj) {
		data.put(name, obj);
	}

	public Object getData(String name) {
		return data.get(name);
	}

	/**
	 * take a screen shot and log.
	 */
	public void takeScreenShot(String name) {
		for (Iterator<? extends EnhancedWebDriver> i = getWebDrivers()
				.iterator(); i.hasNext();) {
			EnhancedWebDriver driver = i.next();
			String fileName = Utils.uniqueBaseName(name) + driver.getName()
					+ ".png";
			driver.screenshot(getOutputFile(fileName));
			log.error("[Image:" + fileName + "]");
		}
	}
}
