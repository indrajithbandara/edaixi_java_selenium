package com.ibm.docs.test.gui.support;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Keyboard;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.io.Zip;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.Command;
import org.openqa.selenium.remote.CommandExecutor;
import org.openqa.selenium.remote.CommandInfo;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.openqa.selenium.remote.HttpVerb;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.Response;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.collect.ImmutableMap;
import com.ibm.docs.test.common.Config;
import com.ibm.docs.test.common.HttpClientFactory;
import com.ibm.docs.test.common.Utils;

/**
 * Provide several add-on features to ease test code development. For example: </br>
 * 1. Javascript code debug
 * 2. Adjust waiting time depending on network speed.
 * 
 * @author liuzhe@cn.ibm.com
 *
 */
public class EnhancedWebDriver implements WebDriver, WrapsDriver, HasInputDevices {
	
	private static final Logger log = Logger.getLogger(EnhancedWebDriver.class);
	
	private static final Pattern HOST_IP_PATTERN = Pattern.compile("proxyId\":\"http://([^:]*):");
	
	protected WebDriver driver;

	protected String name;
	
	protected String configKeyPrefix;
	
	protected TestEnv testEnv;
	
	protected boolean javascriptDebug = false;
	
	private LinkedList<String> windowHistories = new LinkedList<String>();
	
    private Set<String> windowHandles;
	
    private double sleepFactor;
    
	public EnhancedWebDriver(WebDriver driver, String name, TestEnv testEnv) {
		this.driver = driver;
		this.name = name;
		this.configKeyPrefix = "session." + name + ".";
		this.testEnv = testEnv;
		getCapabilities().setCapability("hostIP", getHostIP());
		this.javascriptDebug = BooleanUtils.toBoolean(getConfig("javascript.debug"));
		this.sleepFactor = NumberUtils.toDouble(getConfig("sleep.factor"), 1.0);
	}

 
	protected void configHosts() {
		String hostsV = getConfig("hosts");
		if (hostsV == null)
			return;
		String scriptPath = upload(new File(Config.HOME, "bin/addhost.exe"));
		String[] hosts = hostsV.split(",");
		for (String hostV : hosts) {
			String[] host = hostV.split(" ");
			log.debug(shell(scriptPath, host[0], host[1]));
		}
	}
	
	protected void configCert() {
		String serverUrl = getConfig("certificateUrl");
		if (serverUrl == null)
			return;
		String scriptPath = upload(new File(Config.HOME, "bin/installcert.exe"));
		log.debug(shell(scriptPath, serverUrl));
	}
	
	public String shell(String executable, String... arguments) {
		if (driver.getClass() == RemoteWebDriver.class) {
			RemoteWebDriver remoteWebDriver = (RemoteWebDriver) driver;
			CommandExecutor executor = remoteWebDriver.getCommandExecutor();
			if (executor instanceof HttpCommandExecutor) {
		    	HttpCommandExecutor httpCommandExecutor = (HttpCommandExecutor) executor;
		    	URL url = httpCommandExecutor.getAddressOfRemoteServer();
		    	HashMap<String, CommandInfo> map = new HashMap<String, CommandInfo>();
		    	map.put("EXECUTE_SHELL", new CommandInfo("/session/:sessionId/executeShell", HttpVerb.POST));
		    	executor = new HttpCommandExecutor(map, url);
				try {
					Response response = executor.execute(new Command(remoteWebDriver.getSessionId(), "EXECUTE_SHELL", ImmutableMap.of("executable", executable, "arguments", arguments)));
					return (String) response.getValue();
				} catch (IOException e) {
					throw new WebDriverException("error", e);
				}
			} else {
				throw new WebDriverException("Not support!");
			}
		} else {
			try {
				return Utils.exec(executable, arguments);
			}  catch (Exception e) {
				throw new WebDriverException("error", e);
			}
		}
	}
	
	/**
	 * Get name
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the test env the web driver belonging to
	 * @return
	 */
	public TestEnv getTestEnv() {
		return testEnv;
	}
	
	/**
	 * Take screen shot of the web driver and save it into a file
	 * @param file
	 */
	public BufferedImage screenshot(File file) {
		try {
			TakesScreenshot takesScreenshot = null;
			if (driver instanceof TakesScreenshot)
				takesScreenshot = (TakesScreenshot) driver;
			else
				takesScreenshot = ((TakesScreenshot) new Augmenter().augment(driver));
			File scrFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
	        BufferedImage img = ImageIO.read(scrFile);
	        if (img != null) {
	        	FileUtils.forceMkdir(file.getParentFile());
	        	if (ImageIO.write(img, FilenameUtils.getExtension(file.getName()), file))
	        		return img;
	        }
	        
		} catch (Exception e) {
			log.debug("Failed to take screen shot of webdriver! Maybe browser crashed or closed accidently!", e);
		}
		
		return null;
	}
	
	@Override
	public void close() {	
		log.info("Close the file");
		String curHandle = driver.getWindowHandle();
		try{
			this.javascript("window.close();");
		}catch(Exception e){
			log.debug("Failed to close tab via directly invoking window.close.", e);
			try{
				//it failed to close tab via invoking javascript window.close, first open a blank page and then invoke window.close to close it.
				this.javascript("window.open(\"\",\"_self\",\"\"); window.close();");
			}catch(Exception ee){
				log.debug("Failed again to close tab via first opening blank page, and then invoking window.close.", ee);
			}
		}
		
		//driver.close();
		while(windowHistories.remove(curHandle));
		String preWindowHandle =windowHistories.pop();
		if (preWindowHandle != null) {
			driver.switchTo().window(preWindowHandle);
			preWindowHandle = null;
		}
	}

	@Override
	public WebElement findElement(By by) {
		return driver.findElement(by);
	}

	@Override
	public List<WebElement> findElements(By by) {
		return driver.findElements(by);
	}

	@Override
	public void get(String url) {
		driver.get(url);
	}

	@Override
	public String getCurrentUrl() {
		return driver.getCurrentUrl();
	}

	@Override
	public String getPageSource() {
		return driver.getPageSource();
	}

	@Override
	public String getTitle() {
		return driver.getTitle();
	}

	@Override
	public String getWindowHandle() {
		return driver.getWindowHandle();
	}

	@Override
	public Set<String> getWindowHandles() {
		return driver.getWindowHandles();
	}

	@Override
	public Options manage() {
		return driver.manage();
	}
	@Override
	public Navigation navigate() {
		return driver.navigate();
	}
	
	@Override
	public void quit() {
		try {
			driver.quit();
		} catch (Exception e) {
			//ignored!
		}
	}

	@Override
	public TargetLocator switchTo() {
		return driver.switchTo();
	}

	public void switchToWindow(String handle) {
		String curHanlde = driver.getWindowHandle();
		if (curHanlde.equals(handle))
			return;
		driver.switchTo().window(handle);
		windowHistories.push(curHanlde);
		sleep(1);
	}
	
	@Override
	public WebDriver getWrappedDriver() {
		return driver;
	}
	
	/**
	 * Get a configuration property
	 * @param key
	 * @return
	 */
	public String getConfig(String key) {
		return Config.getInstance().get(configKeyPrefix + key, key, null);
	}
	
	/**
	 * If a server is development environment with no Connection Files, will execute UT related scenario. 
	 * Configure loginUrl like http://9.123.0.0:9080/docs/app/doc/concord.storage/**.doc/edit/content
	 * @return 
	 */
	public boolean getUTStatus(){
		return Config.getInstance().get("loginUrl").contains("concord.storage")?true:false;
	}
	
	/**
	 * Execute javascript on current web page. This method provide a nice way to debug javascript easily. </br>
	 * Known Issues: The debug method doesn't work well with Firebug. Suggest to use IE, Chrome and Safari.</br>
	 * Use Chrome:</br>
	 * 1. Set a Java breakpoint at the last line in the method.</br>
	 * 2. Open Chrome Development Tool</br>
	 * 3. Open source named "webdriverCall.js", in which your javascript source code stays.</br>
	 * 4. Set a Javascript breakpoint</br>
	 * 5. In console, run "webdriverCall()". </br>
	 * Note: because ChromeDriver always closes development tool when it sends command to Chrome, so you have to trigger your javascript manually.</br>
	 * </br>
	 * Use IE:</br>
	 * 1. Set a Java breakpoint after IE is open. Open IE "developer tools" and click "Starting debugging". </br>
	 * Note: IE debugging need to reload the page, so in order to avoid this, you should enable IE debugging immediately after IE is open. </br>
	 * 2. Set a Java breakpoint at the last line in the method.</br>
	 * 3. In IE debugger, browse others/eval to find your code </br>
	 * 4. Set a Javascript breakpoint</br>
	 * 5. Resume your java code</br>
	 * 6. Enjoy debugging!</br>
	 * 
	 * @param code Javascript code
	 * @param args the arguments passed into javascript code
	 * @return
	 */
	public Object javascript(String code, Object... args) {
		JavascriptExecutor executor = ((JavascriptExecutor) driver);
		if (javascriptDebug) {
			// Firstly inject the code into browser via eval. 
			// "//@ sourceURL=webdriverCall" is the magic key to enable the code debuggable for chrome.
			Object[] codeAndArgs = new Object[args.length + 1];
			codeAndArgs[0] = code;
			System.arraycopy(args, 0, codeAndArgs, 1, args.length);
			executor.executeScript("var args = Array.prototype.slice.call(arguments); var code = 'webdriverCall = function() {var arguments = window.webdriverArguments;\\n' + args.shift() + '};//@ sourceURL=webdriverCall.js';eval(code); window.webdriverArguments=args;", codeAndArgs);
			// To debug javascript, you should set a breakpoint at the following line.
			return executor.executeScript("return webdriverCall();");
		} else {
			return executor.executeScript(code, args);
		}
	} 
	
	
	/**
	 * Get browser name
	 * @return
	 */
	public String getBrowserName() {
		return getCapabilities().getBrowserName();
	}
	
	/**
	 * Get browser version
	 * @return
	 */
	public String getBrowserVersion() {
		return getCapabilities().getVersion();
	}
	
	public double getBrowserVersionInt(){
		return Double.parseDouble(getBrowserVersion());
	}
	
	public DesiredCapabilities getCapabilities() {
		  return (DesiredCapabilities)((RemoteWebDriver)driver).getCapabilities();
	}
	
	public boolean isInternetExplorer() {
		return "internet explorer".equalsIgnoreCase(getCapabilities().getBrowserName());
	}
	
	public boolean isFirefox() {
		return "firefox".equalsIgnoreCase(getCapabilities().getBrowserName());
	}
	
	public boolean isChrome() {
		return "chrome".equalsIgnoreCase(getCapabilities().getBrowserName());
	}
	
	public boolean isSafari() {
		return "safari".equalsIgnoreCase(getCapabilities().getBrowserName());
	}
	
	/**
	 * Get IP address of the host in which the web driver is running
	 * @return
	 */
    public String getHostIP() {
    	if (driver.getClass() == RemoteWebDriver.class) {
	    	RemoteWebDriver remoteWebDriver = (RemoteWebDriver) driver;
	    	CommandExecutor executor = remoteWebDriver.getCommandExecutor();
	    	if (executor instanceof HttpCommandExecutor) {
		    	HttpCommandExecutor httpCommandExecutor = (HttpCommandExecutor) executor;
		    	URL url = httpCommandExecutor.getAddressOfRemoteServer();
		    	SessionId session = remoteWebDriver.getSessionId();
		    	try {
					HttpGet httpMethod = new HttpGet("http://" + url.getHost() + ":" + url.getPort() + "/grid/api/testsession?session=" + session);
					DefaultHttpClient httpClient = new DefaultHttpClient();
					HttpResponse response = httpClient.execute(httpMethod);
					String responseContent = EntityUtils.toString(response.getEntity());
					if (response.getStatusLine().getStatusCode() == 200) {
						Matcher matcher = HOST_IP_PATTERN.matcher(responseContent);
						if (matcher.find()) 
							return matcher.group(1);
					}
				} catch (Exception e) {
					//ignore never occur
					
				}
	    	}
	    	// Can not detect the IP. return this invalid IP
	    	return "0.0.0.0";
    	}
    	
		try {
			InetAddress address = InetAddress.getLocalHost();
			 return address.getHostAddress();  
		} catch (UnknownHostException e) {
			
		}   
		// Can not detect the IP. return this invalid IP
		return "0.0.0.0";
    }
    
    public String upload(File file) {
        if (!file.isFile()) 
	          throw new WebDriverException("Only file can be uploaded!" + file);
    	if (driver.getClass() == RemoteWebDriver.class) {
	    	RemoteWebDriver remoteWebDriver = (RemoteWebDriver) driver;
	    	CommandExecutor executor = remoteWebDriver.getCommandExecutor();
	        try {
	          String zip = new Zip().zipFile(file.getParentFile(), file);
	          Response response = executor.execute(new Command(remoteWebDriver.getSessionId(), DriverCommand.UPLOAD_FILE, ImmutableMap.of("file", zip)));
	          return (String) response.getValue();
	        } catch (IOException e) {
	          throw new WebDriverException("Could not upload " + file, e);
	        }
    	} else {
    		log.info("Input file info in \"File Upload\" dialog successfully");
    		return FilenameUtils.normalize(file.getAbsolutePath());
    	}
      }
  
    
    public void storeWindows() {
    	windowHandles = driver.getWindowHandles();
    }
    
    public String waitNewWindow() {
    	return waitNewWindow(10);
    }
    
	public String waitNewWindow(int timeout) {
		return new WebDriverWait(driver, timeout).until(new ExpectedCondition<String>() {
			@Override
			public String apply(WebDriver driver) {
				Set<String> newHandles = driver.getWindowHandles();
				for (String h : newHandles) {
					if (!windowHandles.contains(h)) {
						return h;
					}
				}
				return null;
			}
		});
	}
	
	public String openNewWindow(String url) {
		storeWindows();
		String code = "var d=document,a=d.createElement('a');a.target='_blank';a.href=arguments[0];a.innerHTML='.';d.body.appendChild(a);return a";
		WebElement anchor = (WebElement) javascript(code, url);
		anchor.click();
		javascript("var a=arguments[0];a.parentNode.removeChild(a);", anchor);
		return waitNewWindow();
	}

	public String extractData(String url, String pattern, String replacement) {
		if (url == null)
			return null;
		
		get(url);
		String source = getPageSource();
		if (pattern == null)
			return source;
		
		Matcher matcher = Pattern.compile(pattern).matcher(source);
		if (matcher.find()) 
			return matcher.replaceAll(replacement);
		return null;
	}
	
	public void sleep(long seconds) {
		sleep(seconds, TimeUnit.SECONDS);
	}
	
	public void sleep(long duration, TimeUnit unit) {
		try {
			Thread.sleep((long)(sleepFactor * unit.toMillis(duration)));
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
	
	public String getSelection() {
		return (String) javascript("return window.getSelection().toString();");
	}

	@Override
	public Keyboard getKeyboard() {
		return ((HasInputDevices)driver).getKeyboard();
	}

	@Override
	public Mouse getMouse() {
		return ((HasInputDevices)driver).getMouse();
	}
	
	public DefaultHttpClient getHttpClient() {
		DefaultHttpClient client = HttpClientFactory.createTrustAllClient();
		Set<Cookie> cookies = driver.manage().getCookies();
		
		String defaultDomain = null;
		try {
			URL uri = new URL(driver.getCurrentUrl());
			defaultDomain = uri.getHost();
		} catch (MalformedURLException e) {
			//ignore 
		}
		
		CookieStore cookieStore = client.getCookieStore();
		for (Cookie c : cookies) {
			BasicClientCookie duplicateCookie = new BasicClientCookie(c.getName(), c.getValue());
			duplicateCookie.setDomain(c.getDomain() == null ? defaultDomain : c.getDomain());
			duplicateCookie.setSecure(c.isSecure());
			duplicateCookie.setExpiryDate(c.getExpiry());
			duplicateCookie.setPath(c.getPath());
			cookieStore.addCookie(duplicateCookie);
		}
		
		return client;
	}
	
	public File download(String url, File target)  {
		try {
	        URL uri = new URL(url);
	        DefaultHttpClient client = getHttpClient();
	        HttpGet httpget = new HttpGet(url);
	        HttpResponse response = client.execute(httpget);
	        int statusCode = response.getStatusLine().getStatusCode();
	        if (statusCode != 200)
	        	throw new NotFoundException(String.format("Could not download '%s'. Server returned code: %s", url, statusCode));
	        
	        if (target.isDirectory()) {
	        	String path = uri.getPath();
	        	Header header = response.getFirstHeader("Content-Disposition");
	        	if (header != null) {
	        		String value = header.getValue();
	        		int s = -1, e = -1;
	        		String prefix = "filename=";
	        		if (value != null && (s = value.indexOf(prefix)) >= 0) {
	        			s = s + prefix.length();
	        			e = value.indexOf(";", s);
	        			path = e >= 0 ? value.substring(s , e) : value.substring(s);
	        			path = path.trim();
	        			path.replace("\"", "");
	        		}
	        	}
	        	target = new File(target, FilenameUtils.getName(path));
	        }
	        FileUtils.forceMkdir(target.getParentFile());
	        IOUtils.copy(response.getEntity().getContent(), new FileOutputStream(target));
	        return target;
        } catch (NotFoundException e) {
        	throw e;
        } catch (Exception e) {
        	e.printStackTrace();
        	throw new WebDriverException(String.format("Could not download '%s'.", url, e));
		}
	}
	
	public WebDriver getWebDriver() {
		return driver;
	}
	
}
