package com.ibm.docs.test.gui.pages;

import org.apache.log4j.Logger;

import com.ibm.docs.test.gui.support.EnhancedWebDriver;
import com.ibm.docs.test.gui.support.EnhancedWebElement;
import com.ibm.docs.test.gui.support.Find;

public class DohPage extends BasePage {
	private static final Logger log = Logger.getLogger(DohPage.class);
	private String tempUrl = "http://@server/docs/static/@timeStamp/js/util/doh/runner.html?registerModulePath=@registerModulePath&testModule=@testModule&group=@group&testFiles=@testFiles&repoId=lcfiles";

	private int maxRunTime = 600; //default run time is 10 minutes
	
	@Find("{css=div[id=testListContainer] table tfoot}")
	public EnhancedWebElement resultPanel;
	
	@Find("{css=div[id=testListContainer] table tfoot tr td:nth-child(2)}")
	public EnhancedWebElement resultSumary;
	
	@Find("{css=div[id=testListContainer] table tfoot tr td:nth-child(2) span:nth-child(1)}")
	public EnhancedWebElement resultError;
	
	@Find("{css=div[id=testListContainer] table tfoot tr td:nth-child(2) span:nth-child(2)}")
	public EnhancedWebElement resultFailure;
	
	public DohPage(EnhancedWebDriver driver) {
		super(driver);
	}	
	
	public void setMaxRunTime(int seconds){
		this.maxRunTime = seconds;
	}
	
	public DohPage go(String server,String timeStamp, String registerModulePath, String testModule, String group, String testFiles) {
		//if(driver.getUTStatus()) return this;
		String url = tempUrl;
		url = url.replace("@server", server).replace("@timeStamp",timeStamp).replace("@registerModulePath", registerModulePath);
		url = url.replace("@testModule", testModule).replace("@group", group).replace("@testFiles", testFiles);
		log.info("Go to DOH page.");
		log.info("the doh url is: " + url);
		driver.get(url);		
		return this;
	}
	
	public String getTestResult(){
		resultPanel.waitPresence(maxRunTime);
		return resultSumary.getText();
	}
	
	public String getError(){
		resultPanel.waitPresence(maxRunTime);
		return resultError.getText();
	}
	
	public String getFailure(){
		resultPanel.waitPresence(maxRunTime);
		return resultFailure.getText();
	}
	
	
	public void close() {
		driver.close();
	}
}
