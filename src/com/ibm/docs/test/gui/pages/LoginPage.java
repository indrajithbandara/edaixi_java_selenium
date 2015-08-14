package com.ibm.docs.test.gui.pages;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.text.StrTokenizer;
import org.apache.log4j.Logger;

import com.ibm.docs.test.gui.support.EnhancedWebDriver;
import com.ibm.docs.test.gui.support.EnhancedWebElement;
import com.ibm.docs.test.gui.support.Find;

/**
 * @author liuzhe@cn.ibm.com
 *
 */
public class LoginPage extends BasePage {
	private static final Logger log = Logger.getLogger(LoginPage.class);
	@Find("id=username")
	public EnhancedWebElement textboxUserName;
	@Find("id=continue")
	public EnhancedWebElement buttonContinue; //for Daily new login page
	@Find("id=password")
	public EnhancedWebElement textboxPassword;
	@Find("css=input[type='submit']")
	public EnhancedWebElement buttonSubmit;
	@Find("css=input[id='submit_form']")
	public EnhancedWebElement buttonSubmitSC;
	@Find(by="{id=headerUserName}+{id=bss-usersMenu_btn}", timeout=30)
	public EnhancedWebElement indicatorLogged;
	
	private static boolean versionChecked = false;
	
	private static Random random = new Random();
	
	public LoginPage(EnhancedWebDriver driver) {
		super(driver);
	}
	
	@SuppressWarnings("unchecked")
	public String login() {
		List<String> users = (List<String>) testEnv.getData("users");
		List<String> passwords = (List<String>) testEnv.getData("passwords");
		if (users == null) {
			List<String> tokens = StrTokenizer.getCSVInstance(driver.getConfig("users")).getTokenList();
			users = new ArrayList<String>();
			passwords = new ArrayList<String>();
			for (int i = 0; i < tokens.size();i++) {
				users.add(tokens.get(i++));
				passwords.add(tokens.get(i));
			}
			
			testEnv.setData("users", users);
			testEnv.setData("passwords", passwords);
		}
		int i = random.nextInt(users.size());
		String user = users.remove(i);
		String password = passwords.remove(i);
		testEnv.setData("user", user);
		testEnv.setData("password", password);
//		String resurce = testEnv.acquireResource(driver.getConfig("login.user"));
//		String[] userAndPassword = resource.split(":");
		login(user, password);
		return user;
	}
	
	public void login(String username, String password) {
		String loginURL = null;
		if(driver.getUTStatus()){
			username = username.replaceAll("@", "%40");
			loginURL = "http://"+username+":"+password+"@"+driver.getConfig("loginUrl").substring(7);
		}
		else
			loginURL = driver.getConfig("loginUrl");
		testEnv.setOutputProperty("Browser", driver.getBrowserName() + ' ' + driver.getBrowserVersion());
		testEnv.setOutputProperty("Login Address", "<a href=\""+ loginURL + "\">" + loginURL + "</a>");
		testEnv.setOutputProperty("User", username);
		
		log.info(String.format("Go to %s and log in with user '%s' and password '%s'", loginURL, username, password));
		driver.manage().window().maximize();
//		driver.manage().window().setPosition(new Point(0,0));
//		driver.manage().window().setSize(new Dimension(1280, 768));
		driver.get(loginURL);
		if(driver.getUTStatus()){
			driver.sleep(35);
			return;
		}
		textboxUserName.sendKeys(username);
		driver.sleep(500, TimeUnit.MILLISECONDS);
		if(buttonContinue.isPresent())
			buttonContinue.click();
		driver.sleep(500, TimeUnit.MILLISECONDS);
		textboxPassword.clear();
		textboxPassword.sendKeys(password);
		if(buttonSubmitSC.isPresent())
			buttonSubmitSC.click();
		else
			buttonSubmit.click();
		indicatorLogged.waitPresence();

				
//		String filesVersion = driver.extractData(driver.getConfig("${server.url}/files/templates/about.jsp"), "[\\s|\\S]*v(\\d+\\.\\d+\\.\\d+.\\d+)[\\s|\\S]*(\\d{8}-\\d{4})[\\s|\\S]*", "Files $1 $2");
//		log.debug("Files Version: " + (filesVersion == null ? "unkown" : filesVersion));
//		if (filesVersion != null)
//			testEnv.setOutputProperty("Files Version", filesVersion);
		
		if (!versionChecked) {
			String conversionVersionUrl = driver.getConfig("app.conversion.versionUrl");
			if (conversionVersionUrl != null) {
				String conversionVersion = driver.extractData(conversionVersionUrl, driver.getConfig("app.conversion.versionPattern"), driver.getConfig("app.conversion.versionReplacement"));
				log.debug("Conversion Version: " + (conversionVersion == null ? "unkown" : "Conversion " + conversionVersion));
				if (conversionVersion != null)
					testEnv.setOutputProperty("Conversion Version", conversionVersion);
			}
			
			String conversionLibVersionUrl = driver.getConfig("app.conversionLib.versionUrl");
			if (conversionLibVersionUrl != null) {
				String conversionLibVersion = driver.extractData(conversionLibVersionUrl, driver.getConfig("app.conversionLib.versionPattern"), driver.getConfig("app.conversionLib.versionReplacement"));
				log.debug("ConversionLib Version: " + (conversionLibVersion == null ? "unkown" : conversionLibVersion));
				if (conversionLibVersion != null)
					testEnv.setOutputProperty("ConversionLib Version", conversionLibVersion);
				
			}
			
			versionChecked = true;
		}
	}
}
