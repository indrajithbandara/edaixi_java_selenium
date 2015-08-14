package com.ibm.docs.test.gui.support;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.Architecture;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.browserlaunchers.locators.BrowserInstallation;
import org.openqa.selenium.browserlaunchers.locators.Firefox3Locator;
import org.openqa.selenium.browserlaunchers.locators.GoogleChromeLocator;
import org.openqa.selenium.browserlaunchers.locators.InternetExplorerLocator;
import org.openqa.selenium.browserlaunchers.locators.SafariLocator;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.os.WindowsUtils;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;

import com.ibm.docs.test.common.Config;

/**
 * A factory class to search browsers (including firefox, chrome, safari, ie) on
 * local system and create web drivers without grid.</br> Search order: </br> 1.
 * Search browsers in their default installation directories in the system.
 * </br> 2. Search in ${working directory}/../browsers. In the directory,
 * browser must be place in a sub-directory named with the pattern {browser
 * name}_{version}. e.g. firefox_21, chrome_27, chrome_99.99 and etc.</br>
 * 
 * Pass DesiredCapabilities into DriverFactory.newDriver, the factory will find
 * the matched browser. Only browser name and version is took into account.
 * Browser name can be 'firefox', 'chrome', 'safari', 'internet explorer' and
 * 'any'.</br>
 * 
 * 
 * 
 * @author liuzhe@cn.ibm.com
 * 
 */
public class DriverFactory {
	private static final String[] FIREFOX_BINS_WIN = { "firefox.exe" };
	private static final String[] FIREFOX_BINS_LINUX = { "firefox" };
	private static final String[] FIREFOX_BINS_MAC = { "Contents/MacOS/firefox" };
	private static final String[] CHROME_BINS_WIN = { "Application/chrome.exe" };
	private static final String[] CHROME_BINS_LINUX = { "google-chrome" };
	private static final String[] CHROME_BINS_MAC = { "Contents/MacOS/Google Chrome" };
	private static final String[] CHROMEDRIVER_WIN = { "ChromeDriver_win/chromedriver.exe" };
	private static final String[] CHROMEDRIVER_LINUX = { "ChromeDriver_linux/chromedriver" };
	private static final String[] CHROMEDRIVER_LINUX64 = { "ChromeDriver_linux64/chromedriver" };
	private static final String[] CHROMEDRIVER_MAC = { "ChromeDriver_mac/chromedriver" };
	private static final String[] INTERNETEXPLORERDRIVER_WIN = { "InternetExplorerDriver_win/IEDriverServer.exe" };
	private static final String[] INTERNETEXPLORERDRIVER_WIN64 = { "InternetExplorerDriver_win64/IEDriverServer.exe" };
	private static final Logger log = Logger.getLogger(DriverFactory.class);
	private static final DriverFactory instance = new DriverFactory();
	private ArrayList<DesiredCapabilities> registered = new ArrayList<DesiredCapabilities>();

	private String chromeDriverBin = null;

	private String internetExplorerDriverBin = null;

	private String fPath = null;

	protected DriverFactory() {
		findDrivers();
		findDefaultBrowsers();
		findPackedBrowsers();
		Collections.sort(registered, new Comparator<DesiredCapabilities>() {

			@Override
			public int compare(DesiredCapabilities o1, DesiredCapabilities o2) {
				if (o1.getVersion() == null && o2.getVersion() != null)
					return -1;
				if (o1.getVersion() != null && o2.getVersion() == null)
					return 0;

				int c = o1.getBrowserName().compareToIgnoreCase(
						o2.getBrowserName());
				if (c != 0)
					return c;

				return o1.getVersion().compareToIgnoreCase(o2.getBrowserName())
						* -1;
			}
		});
	}

	public static WebDriver newDriver(DesiredCapabilities capabilities) {
		return instance.newInstance(capabilities);
	}

	public static WebDriver newDriver(DesiredCapabilities capabilities,
			String language) {
		return instance.newInstance(capabilities, language);
	}

	protected void findDrivers() {
		// Search ChromeDriver bin
		File driversDir = new File(Config.HOME, "drivers");
		if (Platform.getCurrent().is(Platform.WINDOWS)) {
			chromeDriverBin = findBin(driversDir, CHROMEDRIVER_WIN);
			internetExplorerDriverBin = /*
										 * Architecture.getCurrent().is(Architecture
										 * .X64) ? findBin(driversDir,
										 * INTERNETEXPLORERDRIVER_WIN64) :
										 */findBin(driversDir,
					INTERNETEXPLORERDRIVER_WIN);
		} else if (Platform.getCurrent().is(Platform.MAC))
			chromeDriverBin = findBin(driversDir, CHROMEDRIVER_MAC);
		else if (Platform.getCurrent().is(Platform.LINUX))
			chromeDriverBin = Architecture.getCurrent().is(Architecture.X64) ? findBin(
					driversDir, CHROMEDRIVER_LINUX64) : findBin(driversDir,
					CHROMEDRIVER_LINUX);

		if (chromeDriverBin == null)
			log.debug("ChromeDriver is not found or not supported on the machine!");
		if (internetExplorerDriverBin == null)
			log.debug("InternetExplorerDriver is not found or not supported on the machine!");
	}

	/**
	 * Find the system browsers
	 */
	private void findDefaultBrowsers() {
		// Firefox
		BrowserInstallation browserInstallation = new Firefox3Locator()
				.findBrowserLocation();
		if (browserInstallation != null) {
			registerFirefox(browserInstallation.launcherFilePath(), null);
			fPath = browserInstallation.launcherFilePath();
		}
		// TODO
		else
			registerFirefox("E:\\Program Files\\FireFox\\firefox.exe", null);
		// Chrome
		browserInstallation = new GoogleChromeLocator() {
			protected String[] usualWindowsLauncherLocations() {
				return new String[] {
						WindowsUtils.getLocalAppDataPath()
								+ "\\Google\\Chrome\\Application",
						WindowsUtils.getProgramFiles86Path()
								+ "\\Google\\Chrome\\Application" };
			}
		}.findBrowserLocation();
		if (browserInstallation != null)
			registerChrome(browserInstallation.launcherFilePath(), null);

		// InternetExplorer
		browserInstallation = new InternetExplorerLocator()
				.findBrowserLocation();
		if (browserInstallation != null)
			registerInternetExplorer(browserInstallation.launcherFilePath(),
					null);

		// Safari
		browserInstallation = new SafariLocator().findBrowserLocation();
		if (browserInstallation != null)
			registerSafari(browserInstallation.launcherFilePath(), null);
	}

	/**
	 * For convenience, support pre-packed browsers
	 */
	protected void findPackedBrowsers() {
		// Assume the packed browsers are installed in "../browsers"
		// Better if it's configurable?
		File[] browserDirs = new File(Config.HOME, "../browsers").listFiles();
		if (browserDirs == null || browserDirs.length == 0)
			return;

		for (File dir : browserDirs) {
			if (!dir.isDirectory())
				continue;
			String name = dir.getName();
			String[] tokens = name.split("_");
			String browserName = tokens[0].toLowerCase();
			String browserVersion = tokens.length > 1 ? tokens[1] : null;
			String bin = null;
			if (Platform.getCurrent().is(Platform.WINDOWS)) {
				if (browserName.contains("firefox")
						&& (bin = findBin(dir, FIREFOX_BINS_WIN)) != null)
					registerFirefox(bin, browserVersion);
				if (browserName.contains("chrome")
						&& (bin = findBin(dir, CHROME_BINS_WIN)) != null)
					registerChrome(bin, browserVersion);
			} else if (Platform.getCurrent().is(Platform.MAC)) {
				if (browserName.contains("firefox")
						&& (bin = findBin(dir, FIREFOX_BINS_MAC)) != null)
					registerFirefox(bin, browserVersion);
				if (browserName.contains("chrome")
						&& (bin = findBin(dir, CHROME_BINS_MAC)) != null)
					registerChrome(bin, browserVersion);
			} else if (Platform.getCurrent().is(Platform.LINUX)) {
				if (browserName.contains("firefox")
						&& (bin = findBin(dir, FIREFOX_BINS_LINUX)) != null)
					registerFirefox(bin, browserVersion);
				if (browserName.contains("chrome")
						&& (bin = findBin(dir, CHROME_BINS_LINUX)) != null)
					registerChrome(bin, browserVersion);
			}
		}
	}

	/**
	 * register firefox
	 * 
	 * @param bin
	 * @param version
	 */
	private void registerFirefox(String bin, String version) {
		DesiredCapabilities capabilities = DesiredCapabilities.firefox();
		capabilities.setCapability(FirefoxDriver.BINARY, bin);
		capabilities.setVersion(version);
		registered.add(capabilities);
		log.debug(String.format("Firefox %s '%s' is drived",
				version != null ? version : "in system", bin));
	}

	/**
	 * register chrome
	 * 
	 * @param bin
	 * @param version
	 */
	private void registerChrome(String bin, String version) {
		if (chromeDriverBin == null)
			return;

		System.setProperty("webdriver.chrome.driver", chromeDriverBin);
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		capabilities.setCapability("chrome.binary",
				FilenameUtils.normalize(bin));
		capabilities.setVersion(version);
		registered.add(capabilities);
		log.debug(String.format("Chrome %s '%s' is drived",
				version != null ? version : "in system", bin));
	}

	/**
	 * register internet explorer
	 * 
	 * @param bin
	 * @param version
	 */
	private void registerInternetExplorer(String bin, String version) {
		if (internetExplorerDriverBin == null)
			return;

		System.setProperty("webdriver.ie.driver", internetExplorerDriverBin);
		DesiredCapabilities capabilities = DesiredCapabilities
				.internetExplorer();
		registered.add(capabilities);
		log.debug(String.format("Internet Explorer %s '%s' is drived",
				version != null ? version : "in system", bin));
	}

	/**
	 * register safari
	 * 
	 * @param bin
	 * @param version
	 */
	private void registerSafari(String bin, String version) {
		DesiredCapabilities capabilities = DesiredCapabilities.safari();
		registered.add(capabilities);
		log.debug(String.format("Safari %s '%s' is drived",
				version != null ? version : "in system", bin));

	}

	/**
	 * Find executable in the given diretory
	 * 
	 * @param dir
	 * @param bins
	 * @return
	 */
	private String findBin(File dir, String[] bins) {
		for (String bin : bins) {
			File file = new File(dir, bin);
			if (file.isFile())
				return file.getAbsolutePath();
		}
		return null;
	}

	/**
	 * Find the matched browser.
	 * 
	 * @param capabilities
	 * @return
	 */
	protected DesiredCapabilities matchCapabilities(
			DesiredCapabilities capabilities) {
		String browserName = capabilities.getBrowserName();
		if (browserName == null || (browserName = browserName.trim()).isEmpty()
				|| browserName.equalsIgnoreCase("any"))
			return capabilities.merge(registered.get(0));

		ArrayList<Capabilities> nameMatched = new ArrayList<Capabilities>();
		for (Capabilities c : registered) {
			if (browserName.equalsIgnoreCase(c.getBrowserName())) {
				nameMatched.add(c);
			}
		}

		if (nameMatched.size() == 0)
			return null;

		String version = capabilities.getVersion();
		if (version == null || (version = version.trim()).isEmpty()
				|| version.equalsIgnoreCase("any"))
			return capabilities.merge(nameMatched.get(0));

		for (Capabilities c : nameMatched) {
			if (version.equalsIgnoreCase(c.getVersion())) {
				return capabilities.merge(c);
			}
		}

		return null;
	}

	/**
	 * Create WebDriver instance
	 * 
	 * @param capabilities
	 * @return
	 */
	protected WebDriver newInstance(DesiredCapabilities capabilities) {
		if (registered.isEmpty())
			return null;
		DesiredCapabilities matchedCapabilities = matchCapabilities(capabilities);
		if (matchedCapabilities == null)
			return null;
		String browserName = matchedCapabilities.getBrowserName();
		if (BrowserType.FIREFOX.equals(browserName))
			return new FirefoxDriver(matchedCapabilities);
		else if (BrowserType.CHROME.equals(browserName))
			return new ChromeDriver(matchedCapabilities);
		else if (BrowserType.SAFARI.equals(browserName))
			return new SafariDriver(matchedCapabilities);
		else if (BrowserType.IE.equals(browserName))
			return new InternetExplorerDriver(matchedCapabilities);
		return null;
	}

	/**
	 * Create WebDriver instance with specified preference language.
	 * 
	 * @param capabilities
	 * @param language
	 * @return
	 */
	protected WebDriver newInstance(DesiredCapabilities capabilities,
			String language) {
		if (registered.isEmpty())
			return null;
		DesiredCapabilities matchedCapabilities = matchCapabilities(capabilities);
		if (matchedCapabilities == null)
			return null;
		// TODO
		FirefoxBinary binary = new FirefoxBinary(new File(
				fPath));
		FirefoxProfile ffProfile = new FirefoxProfile();
		ffProfile.setPreference("intl.accept_languages", language);
		return new FirefoxDriver(binary, ffProfile, matchedCapabilities);
	}
}
