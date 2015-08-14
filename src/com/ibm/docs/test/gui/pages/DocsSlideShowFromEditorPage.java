package com.ibm.docs.test.gui.pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;

import com.ibm.docs.test.gui.support.EnhancedWebDriver;
import com.ibm.docs.test.gui.support.EnhancedWebElement;
import com.ibm.docs.test.gui.support.EnhancedWebElements;
import com.ibm.docs.test.gui.support.Find;

/**
 * slide show page. This slide show page is launched from editor page.
 * It's totally different from slide show page from viewer page.
 * @author xtingx
 *
 */
public class DocsSlideShowFromEditorPage extends BasePage {
	private static final Logger log = Logger.getLogger(DocsSlideShowFromEditorPage.class);	
	
	@Find("css=#slideShowContainer")
	public EnhancedWebElement slideShowContainer;	
	@Find("id=slide_EndOfShow")
	public EnhancedWebElement slideEndShow;	
	@Find("css=#slideShowContainer div.draw_frame:not(.isSpare)")
	public EnhancedWebElements drawFrames;			
	@Find("css=body")
	public EnhancedWebElement body;	
	
	public DocsSlideShowFromEditorPage(EnhancedWebDriver driver) {
		super(driver);
	}

	/**
	 * focus on DocsViewerPage.
	 * @param name
	 * @return
	 */
	public DocsSlideShowFromEditorPage focus(String name) {
		log.info("Go to IBM Docs slide show page from editor page");
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
	public DocsSlideShowFromEditorPage pageDown(){		
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
	public DocsSlideShowFromEditorPage pageUp(){		
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
	public DocsSlideShowFromEditorPage click(){		
		log.info("mouse clicking to go to next page.");	
		slideShowContainer.click();		
		slideShowContainer.waitPresence();
		return this;
	}
	
	/**
	 * whether a draw frame exist which text has specific content 
	 * @param content
	 * @return
	 */
	public Boolean isDrawFrameExistByContent(String content){
		for(EnhancedWebElement ele:drawFrames){
			if(ele.getText().replace("\ufeff", "").contains(content)){
				return true;
			}
		}
		return false;
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
