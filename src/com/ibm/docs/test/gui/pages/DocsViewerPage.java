package com.ibm.docs.test.gui.pages;

import java.awt.image.BufferedImage;
import java.io.File;

import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;

import com.ibm.docs.test.common.GraphicsUtil;
import com.ibm.docs.test.common.Utils;
import com.ibm.docs.test.gui.support.EnhancedWebDriver;
import com.ibm.docs.test.gui.support.EnhancedWebElement;
import com.ibm.docs.test.gui.support.Find;

public class DocsViewerPage extends BasePage {
	private static final Logger log = Logger.getLogger(DocsViewerPage.class);
	
	@Find(by="{css=#selector_page}+{css=#dijit_form_NumberTextBox_0}")
	public EnhancedWebElement inputSelectorPage;
	@Find("css=#contentPane")
	public EnhancedWebElement contentPane;	
	@Find("css=#T_Print[aria-disabled=false]")
	public EnhancedWebElement btnPrint;
	@Find("css=#T_ModePagePicker>div span.prevButton")
	public EnhancedWebElement btnPrevious;
	@Find("css=#T_ModePagePicker>div span.nextButton")
	public EnhancedWebElement btnNext;
	@Find("css=#T_ModePagePicker>div>span[dojoattachpoint='textPageNum']")
	public EnhancedWebElement pageCount;
	@Find("css=body")
	public EnhancedWebElement body;	
	@Find("css=#T_Mode_Play>span")	
	public EnhancedWebElement btnPlayScreenShow;
	@Find("id=T_IBMDocsEditor")
	public EnhancedWebElement btnEdit;
	@Find("id=doc_title_text")
	public EnhancedWebElement docTitle;
	
	@Find("id=contentRow_$0")
	public EnhancedWebElement pageRow;
	//for odp,ppt and pptx, the viewer data structure is different from other file type.
	@Find("id=normalContentImg_$0")
	public EnhancedWebElement currentPage;	
	
	
	@Find("css=#contentRow_$0>td>div>img")
	public EnhancedWebElement pageRowImg;
	
	@Find("id=edit_in_ibm_docs_dec_div")
	public EnhancedWebElement editInIBMDocsDlgDec;
	
	@Find("css=.slideWrapper:nth-child($0)")
	public EnhancedWebElement sidebarSlide;	
	
	@Find("{css=#normalView}-{css=div.draw_page}")
	public EnhancedWebElement slideEditor;
	
	
	public DocsViewerPage(EnhancedWebDriver driver) {
		super(driver);
	}

	/**
	 * focus on DocsViewerPage.
	 * @param name
	 * @return
	 */
	public DocsViewerPage focus(String name) {
		log.info("Go to IBM Docs view page");
		driver.switchToWindow(name);
		driver.manage().window().maximize();
//		driver.manage().window().setPosition(new Point(0,0));
//		driver.manage().window().setSize(new Dimension(1024, 768));	
		btnPrint.waitPresence(60);
		String version = "IBM Viewer " + driver.javascript("return viewer_version;") ;
		testEnv.setOutputProperty("Viewer Version", version);
		log.debug("Viewer Version: " + version);
		driver.sleep(3);
		return this;
	}
	
	/**
	 * For html viewer: focus on DocsViewerPage.
	 * @param name
	 * @return
	 */
	public DocsViewerPage htmlViewerFocus(String name) {
		log.info("Go to IBM Docs view page");
		driver.switchToWindow(name);
		driver.manage().window().maximize();
//		driver.manage().window().setPosition(new Point(0,0));
//		driver.manage().window().setSize(new Dimension(1024, 768));	
//		btnPrint.waitPresence(60);
		driver.sleep(60);	// wait loading. TODO: change to icon status changed when dev ready
		String version = "IBM Viewer " + driver.javascript("return viewer_version;") ;
		testEnv.setOutputProperty("Viewer Version", version);
		log.debug("Viewer Version: " + version);
		driver.sleep(3);
		return this;
	}
	
	public String getDocTitle() {
		return docTitle.getText();
	}
	
	public int getCurrentPageNumber(){
		log.info("get current page number.");
		int pageNum = Integer.valueOf(inputSelectorPage.getAttribute("value")).intValue();
		return pageNum;
	}
	
	public int getPageCount(){
		log.info("get page count.");		
		int pageCount = Integer.valueOf(this.pageCount.getText()).intValue();
		return pageCount;
	}
	
	
	/**
	 * go to next page via pressing PageDown.	 
	 * @return
	 */
	public DocsViewerPage nextPageByKey(){		
		log.info("pressing PageDown to go to next page.");		
		inputSelectorPage.sendKeys(Keys.PAGE_DOWN);
		return this;
	}
	
	/**
	 * go to previous page via pressing PageUp.	
	 * @return
	 */
	public DocsViewerPage previousPageByKey(){		
		log.info("pressing PageDown to go to next page.");		
		inputSelectorPage.sendKeys(Keys.PAGE_UP);

		return this;
	}
	
	/**
	 * go to next page via clicking Next icon in toolbar.	 
	 * @return
	 */
	public DocsViewerPage nextPageByIcon(){
		log.info("click Next icon in toolbar to go to next page.");
		btnNext.click();
		return this;
	}
	
	/**
	 * go to previous page via clicking Previous icon in toolbar.	 
	 * @return
	 */
	public DocsViewerPage previousPageByIcon(){
		log.info("click Previous icon in toolbar to go to previous page.");
		btnPrevious.click();
		return this;
	}	
	
	
	/**
	 * select specific page via input page number and pressing Enter key.
	 * @param pageNumber
	 * @return
	 */
	public DocsViewerPage goToPage(int pageNumber){
		log.info("select page " + pageNumber + " by input page number in input text box, then press Enter.");
		inputSelectorPage.clear();
		inputSelectorPage.sendKeys(String.valueOf(pageNumber));
		inputSelectorPage.sendKeys(Keys.RETURN);
		return this;
	}
	
	/**
	 * is specific page displayed in current view.
	 * @param pageNumber
	 * @return
	 */
	public boolean isPageDisplayed(int pageNumber) {
		log.info("judge whether page " + pageNumber + " is displayed in current page." );
		if(btnPlayScreenShow.isPresent()){
			currentPage.setLocatorArgument(pageNumber);
			return currentPage.isPresent();
		}else{
			pageRow.setLocatorArgument(pageNumber);
			int offset = contentPane.getLocation().y - pageRow.getLocation().y;
			log.info("*************offset of pageRow and contentPane: " + offset);
			
			pageRowImg.setLocatorArgument(pageNumber);
			
			return Math.abs(offset)<10 && (!pageRowImg.getAttribute("src").contains("blank"));
		}		
	}
	
	public boolean imageEquals(File expectedImage) {	
		int pageNumber = this.getCurrentPageNumber();
		log.info("judge whether image in page " + pageNumber + " is displayed well." );
		
		File actualImage = null;
		if(btnPlayScreenShow.isPresent()){
			currentPage.setLocatorArgument(pageNumber);
			actualImage = driver.download(currentPage.getAttribute("src"), testEnv.getTempFile("."));
		}else{					
			pageRowImg.setLocatorArgument(pageNumber);
			actualImage = driver.download(pageRowImg.getAttribute("src"), testEnv.getTempFile("."));
		}		
		
		boolean result = Utils.imageEquals(actualImage, expectedImage);		
		return result;
	} 
	
	/**
	 * 
	 * @param fileName
	 * @return window handle
	 */
	public String edit() {
		log.info("Edit file from viewer page");
		
		driver.storeWindows();
		btnEdit.click();		
		if (editInIBMDocsDlgDec.isPresent())
			editInIBMDocsDlgDec.submit();
		return driver.waitNewWindow();
	}
	
	/** 
	 * 
	 * @return window handle
	 */
	public String playSlideShow() {
		log.info("play slide show from viewer page.");		
		driver.storeWindows();
		btnPlayScreenShow.click();			
		return driver.waitNewWindow();
	}
	
	public DocsViewerPage selectSlide(int index){
		log.info("Select slide " + index);
//		driver.switchTo().frame(sidebarFrame);	
		sidebarSlide.setLocatorArgument(index);	
		sidebarSlide.getCoordinates().inViewPort();
		sidebarSlide.click();
		driver.switchTo().defaultContent();
		return this;
	}
	
	public BufferedImage screenshotCurrentSlide() {
		return screenshot(slideEditor, null);
	}
	
	public BufferedImage screenshot(WebElement element, String path) {
		File file = ((TakesScreenshot)driver.getWebDriver()).getScreenshotAs(OutputType.FILE);

		BufferedImage bufferedImage = GraphicsUtil.loadImage(file.getAbsolutePath());
		if (element != null) {
			Point p = element.getLocation();
			bufferedImage = bufferedImage.getSubimage(p.getX(), p.getY(), element.getSize().getWidth(), element.getSize().getHeight());
		}
		if (path != null)
			GraphicsUtil.storeImage(bufferedImage, path);
		return bufferedImage;
	}

}
