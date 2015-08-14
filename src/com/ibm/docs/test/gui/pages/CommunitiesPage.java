package com.ibm.docs.test.gui.pages;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;

import com.ibm.docs.test.gui.support.EnhancedWebDriver;
import com.ibm.docs.test.gui.support.EnhancedWebElement;
import com.ibm.docs.test.gui.support.EnhancedWebElements;
import com.ibm.docs.test.gui.support.Find;

public class CommunitiesPage extends BasePage {
	private static final Logger log = Logger.getLogger(CommunitiesPage.class);
	@Find("css=#createPlaceButton a")
	public EnhancedWebElement newCommunityBtn;
	@Find("id=addCommunityName")
	public EnhancedWebElement newCommunityName;
	@Find("css=input[value='$0']")
	public EnhancedWebElement newCommunityAccess;
	@Find("css=#addCommunityButton")
	public EnhancedWebElement newCommunitySaveBtn;
	@Find("partialLink=$0")
	public EnhancedWebElement communityName;
	@Find("{css=#lotusNavBar}-{link=Files}")
	public EnhancedWebElement filesNav;
	@Find("{css=button[id*='lconn_files_comm_actions_sharefiles']}+{xpath=//button[@class='lotusBtn' and .='Share Files']}")
	public EnhancedWebElement shareFileBtn;
	@Find("{css=button[id*='lconn_files_action_createitem']}+{css=button.lotusBtn[title='Create a new item']}")
	public EnhancedWebElement newFileBtn;
	
	@Find("{css=td[id*='com_ibm_concord_lcext_newconcorddoc']}+{css=.dijitMenuItem[title='New document'] .dijitMenuItemLabel}")
	public EnhancedWebElement menuNewDocument;
	@Find("{css=td[id*='com_ibm_concord_lcext_newconcordsheet']}+{css=.dijitMenuItem[title='New spreadsheet'] .dijitMenuItemLabel}")
	public EnhancedWebElement menuNewSpreadsheet;
	@Find("{css=td[id*='com_ibm_concord_lcext_newconcordpres']}+{css=.dijitMenuItem[title='New presentation'] .dijitMenuItemLabel}")
	public EnhancedWebElement menuNewPresentation;
	@Find("css=input[name='_tagspres']")
	public EnhancedWebElement newDocDlgPresentationName;
	@Find("css=input[name='_tagsdoc']")
	public EnhancedWebElement newDocDlgDocumentName;
	@Find("css=input[name='_tagssheet']")
	public EnhancedWebElement newDocDlgSpreadsheetName;
	@Find(by="partialLink=$0", timeout=60)
	public EnhancedWebElement fileEntry;
	
	//V1
	@Find("{css=input[id*=option_mycomputer]}+{css=div[id*='lconn_files_widget_FilePicker'] a.lotusAction}")
	public EnhancedWebElement shareDlgBrowseFromLocal;
	@Find("script=return dojo.query('label[title*=$0]').pop();")
	public EnhancedWebElement shareDlgFile;
	@Find("css=input[name='_shareAsEditor']")
	public EnhancedWebElement shareDlgShareAsEditor;
	//V2
	
	
	@Find("css=.lconnUploadContainer .lotusBtn")
	public EnhancedWebElement uploadDlgBrowse;
	@Find("css=input[name=file]")
	public EnhancedWebElements uploadDlgFile;
	@Find("css=a.lconnFilenameContainer")
	public EnhancedWebElement uploadDlgFileName;
	@Find("css=.lconnFileUploadLabel input[type='text']")
	public EnhancedWebElement uploadDlgFileNameInput;
	@Find("css=.lotusDialogFooter input[type='submit']")
	public EnhancedWebElements dialogOk;
	
	@Find("{css=button[id*='com_ibm_viewer_lcext_ccdview']}+{css=#lotusContent div.lotusBtnContainer [title='Preview the file in a browser']}")
	public EnhancedWebElement detailPageViewBtn;
	@Find("{css=button[id*='com_ibm_concord_lcext_ccdedit']}+{css=#lotusContent div.lotusBtnContainer [title='Edit in IBM Docs']}")
	public EnhancedWebElement detailPageEditInIBMDocsBtn;
	
	@Find("id=edit_in_ibm_docs_dec_div")
	public EnhancedWebElement editInIBMDocsDlgDec;
	
	public CommunitiesPage(EnhancedWebDriver driver) {
		super(driver);
	}

	

	public CommunitiesPage go() {
		log.info("Go to Communities");
		driver.get(driver.getConfig("app.communities.url"));
		driver.sleep(5);
		return this;
	}
	
	public CommunitiesPage newCommunity(String name) {
		return newCommunity(name, null);
	}
	public CommunitiesPage newCommunity(String name, String access) {
		driver.sleep(1);
		if (!newCommunityBtn.isDisplayed())
			newCommunityBtn.waitPresence();
		if (driver.isInternetExplorer())
			newCommunityBtn.sendKeys(Keys.ENTER);
		else
			newCommunityBtn.click();
		driver.sleep(2);
		if (!newCommunityName.isDisplayed())
			newCommunityName.waitPresence();
		newCommunityName.sendKeys(name);
		if (access != null) {
			newCommunityAccess.setLocatorArgument(access);
			newCommunityAccess.click();
		}
		driver.sleep(1);
		newCommunitySaveBtn.click();
		driver.sleep(4);
		communityName.setLocatorArgument(name);
		communityName.waitPresence();
		driver.sleep(2);
		return this;
	}
	
	public CommunitiesPage shareFileFromLocal(File file, String label) {
		log.info("Share a file from local computer");
		if (driver.isInternetExplorer()) {
			driver.sleep(2);
			filesNav.sendKeys(Keys.ENTER);
		} else
			filesNav.doubleClick();
		driver.sleep(2);
		shareFileBtn.click();
		driver.sleep(1);
		shareDlgBrowseFromLocal.click();
		driver.sleep(1);
		if (driver.isFirefox()) {
			//Disable popup file dialog on firefox
			driver.javascript("lconn.core.upload.provider.HtmlFileProvider.prototype._handleFakeButtonClick=function(_12, _13, _14) {var _15 = this._buildFileInput(_12, _13, _14,true);dojo.body().appendChild(_15);}");
			uploadDlgBrowse.click();
		}
		
		uploadDlgFile.getLast().sendKeys(driver.upload(file));
		uploadDlgFileName.click();
		if (label != null) {
			uploadDlgFileNameInput.clear();
			uploadDlgFileNameInput.sendKeys(label);
			uploadDlgFileNameInput.sendKeys(Keys.RETURN);
		}
		for(int i=0;i<dialogOk.size();i++)
			if(dialogOk.get(i).isDisplayed()){
				dialogOk.get(i).submit();
				break;
			}
		fileEntry.setLocatorArgument(label);
		fileEntry.waitPresence();
		return this;
	}
	
	public CommunitiesPage shareFile(String file, boolean allowEdit) {
		if (driver.isInternetExplorer()) {
			driver.sleep(2);
			filesNav.sendKeys(Keys.ENTER);
		} else
			filesNav.doubleClick();
		driver.sleep(5);
		shareFileBtn.click();
		shareDlgFile.setLocatorArgument(file);
		shareDlgFile.waitPresence();
		driver.sleep(1);
		shareDlgFile.click();
	
		if (allowEdit)
			shareDlgShareAsEditor.click();
		shareDlgFile.submit();
		fileEntry.setLocatorArgument(file);
		fileEntry.waitPresence();
		return this;
	}

	public String newSpreadsheet(String name) {
		log.info("Create one new spreadsheet");
		filesNav.click();
		driver.sleep(8);
		newFileBtn.click();
		driver.sleep(3);
		menuNewSpreadsheet.click();
		newDocDlgSpreadsheetName.sendKeys(name);
		driver.storeWindows();
		newDocDlgSpreadsheetName.submit();
		return driver.waitNewWindow();
	}
	
	public String newDocument(String name) {
		log.info("Create one new document");
		filesNav.click();
		driver.sleep(8);
		newFileBtn.click();	
		driver.sleep(3);
		menuNewDocument.click();
		driver.sleep(500,TimeUnit.MILLISECONDS);
		newDocDlgDocumentName.sendKeys(name);
		driver.storeWindows();
		newDocDlgDocumentName.submit();
		return driver.waitNewWindow();
	}
	
	public String newPresentation(String name) {
		log.info("Create one new presentation");
		filesNav.click();
		driver.sleep(8);
		newFileBtn.click();	
		driver.sleep(3);
		menuNewPresentation.click();
		newDocDlgPresentationName.sendKeys(name);
		driver.storeWindows();
		newDocDlgPresentationName.submit();
		return driver.waitNewWindow();
	}
	
	/**
	 * 
	 * @param fileName
	 * @return window handle
	 */
	public String edit(String fileName) {
		log.info(String.format("Edit file %s in IBM Docs", fileName));
		openFileDetail(fileName);
		driver.storeWindows();
		detailPageEditInIBMDocsBtn.click();
		if (editInIBMDocsDlgDec.isPresent())
			editInIBMDocsDlgDec.submit();
		return driver.waitNewWindow();
	}
	
	
	/**
	 * 
	 * @param fileName
	 * @return window handle
	 */
	public String view(String fileName) {
		log.info(String.format("View file %s in IBM Docs", fileName));
		openFileDetail(fileName);
		driver.sleep(2);
		driver.storeWindows();
		if (!detailPageViewBtn.isDisplayed())
			detailPageViewBtn.waitPresence();
		if (driver.isInternetExplorer())
			detailPageViewBtn.sendKeys(Keys.ENTER);
		else
			detailPageViewBtn.click();
		return driver.waitNewWindow();
	}
	
	public CommunitiesPage openFileDetail(final String fileName) {
		fileEntry.setLocatorArgument(FilenameUtils.getBaseName(fileName));
		if (!fileEntry.isPresent()) {
			driver.navigate().refresh();
			filesNav.click();
			driver.sleep(8);
		}
		fileEntry.click();
		return this;
	}
	

}
