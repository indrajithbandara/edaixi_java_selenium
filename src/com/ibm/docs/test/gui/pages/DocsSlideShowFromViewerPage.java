package com.ibm.docs.test.gui.pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;

import com.ibm.docs.test.gui.support.EnhancedWebDriver;
import com.ibm.docs.test.gui.support.EnhancedWebElement;
import com.ibm.docs.test.gui.support.Find;

/**
 * slide show page. This slide show page is launched from viewer page.
 * It's totally different from slide show page from editor page.
 * @author xtingx
 *
 */
public class DocsSlideShowFromViewerPage extends BasePage {
	private static final Logger log = Logger.getLogger(DocsSlideShowFromViewerPage.class);	
	
	@Find("id=slideShowContainer")
	public EnhancedWebElement slideShowContainer;
	@Find("css=#slideShowContainer>img[src*='image000$0.jpg']")
	public EnhancedWebElement currentImage;	
	@Find("id=slide_EndOfShow")
	public EnhancedWebElement slideEndShow;	
	
	@Find("css=body")
	public EnhancedWebElement body;	
	
	public DocsSlideShowFromViewerPage(EnhancedWebDriver driver) {
		super(driver);
	}

	/**
	 * focus on DocsViewerPage.
	 * @param name
	 * @return
	 */
	public DocsSlideShowFromViewerPage focus(String name) {
		log.info("Go to IBM Docs slide show page from viewer page");
		driver.switchToWindow(name);
		driver.manage().window().setPosition(new Point(0,0));
		driver.manage().window().setSize(new Dimension(1024, 768));
		slideShowContainer.waitPresence();
		
		// Give docs some time to make its ui stable
		driver.sleep(3);
		return this;
	}	
	
	/**
	 * go to next page via pressing PageDown.	 
	 * @return
	 */
	public DocsSlideShowFromViewerPage pageDown(){		
		log.info("pressing PageDown to go to next page.");	
		if(driver.isChrome()){
			body.sendKeys(Keys.PAGE_DOWN);
		}else{
			slideShowContainer.sendKeys(Keys.PAGE_DOWN);
		}			
		slideShowContainer.waitPresence();
		return this;
	}
	
	/**
	 * go to previous page via pressing PageUp.	
	 * @return
	 */
	public DocsSlideShowFromViewerPage pageUp(){		
		log.info("pressing PageDown to go to next page.");	
		if(driver.isChrome()){
			body.sendKeys(Keys.PAGE_UP);
		}else{
			slideShowContainer.sendKeys(Keys.PAGE_UP);	
		}		
		slideShowContainer.waitPresence();
		return this;
	}
	
	/**
	 * go to next page via clicking.	
	 * @return
	 */
	public DocsSlideShowFromViewerPage click(){		
		log.info("mouse clicking to go to next page.");	
		slideShowContainer.click();		
		slideShowContainer.waitPresence();
		return this;
	}
	
	
	/**
	 * is specific page displayed in current view.
	 * @param pageNumber
	 * @return
	 */
	public boolean isPageDisplayed(int pageNumber) {
		log.info("judge whether page " + pageNumber + " is displayed in current page." );
		if(pageNumber == 1){
			currentImage.setLocatorArgument("");
		}else{
			currentImage.setLocatorArgument(String.valueOf(pageNumber -1));
		}
		currentImage.waitPresence();
		return currentImage.isPresent();
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isLastPageDisplayed(){
		log.info("judge whether the last page displayed.");
		return slideEndShow.isPresent();
	}
	

}
