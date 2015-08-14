package com.ibm.docs.test.gui.pages;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.ibm.docs.test.common.Utils;
import com.ibm.docs.test.gui.support.EnhancedWebDriver;
import com.ibm.docs.test.gui.support.EnhancedWebElement;
import com.ibm.docs.test.gui.support.EnhancedWebElements;
import com.ibm.docs.test.gui.support.Find;

public class FilesPage extends BasePage {
	private static final Logger log = Logger.getLogger(FilesPage.class);

	// Upload button
	@Find("{css=button[id*=lconn_files_action_uploadfile]}+{css=.lotusBtn[title='Upload files from your computer']}")
	public EnhancedWebElement btnUpload;
	// Create Button
	@Find("{css=button[id*=lconn_files_action_createitem]}+{css=#com_ibm_social_layout_widget_ActionBar_0 button:nth-child(2)}")
	public EnhancedWebElement btnCreate;
	@Find("css=#lotusSidenav a[href='/files/app/person/']")
	public EnhancedWebElement btnPinnedFiles;
	// My Files on the side bar
	@Find("css=#lotusSidenav a[href*='/files/app/person/']")
	public EnhancedWebElement btnMyFiles;
	@Find("css=#lotusSidenav a[href='/files/app/shares?pivot=withme']")
	public EnhancedWebElement btnSharedWithMe;
	@Find("css=#lotusSidenav a[href='/files/app/shares?pivot=byme']")
	public EnhancedWebElement btnSharedByMe;
	@Find("css=#lotusSidenav a[href='/files/app/communityfiles']")
	public EnhancedWebElement btnCommunityFiles;
	@Find("css=#lotusSidenav a[href='/files/app/public']")
	public EnhancedWebElement btnPublicFiles;
	@Find("css=#lotusSidenav a[href='/files/app/trash']")
	public EnhancedWebElement btnTrash;

	@Find("{id=com_ibm_concord_lcext_newconcorddoc_0_text}+{css=.dijitPopup tr.dijitMenuItem:nth-child(1)}")
	public EnhancedWebElement menuNewDocument;
	// Drop-down menu "New Spreadsheet"
	@Find("{id=com_ibm_concord_lcext_newconcordsheet_0_text}+{css=.dijitPopup tr.dijitMenuItem:nth-child(2)}")
	public EnhancedWebElement menuNewSpreadsheet;
	@Find("{id=com_ibm_concord_lcext_newconcordpres_0_text}+{css=.dijitPopup tr.dijitMenuItem:nth-child(3)}")
	public EnhancedWebElement menuNewPresentation;
	@Find("{id=lconn_files_action_createcollection_0_text}+{css=.dijitPopup tr.dijitMenuItem:nth-child(4)}")
	public EnhancedWebElement menuNewFolder;

	// A mock to make upload file possible
	// @Find("css=#mock_upload_form input[name='file']")
	// public EnhancedWebElement inputUploadFile;
	// @Find("css=#mock_upload_form input[name='label']")
	// public EnhancedWebElement inputUploadLabel;
	// @Find("css=#mock_upload_form input[name='visibility']")
	// public EnhancedWebElement inputUploadVisibility;

	@Find("xpath=/html/body/div[@role='dialog' and not(contains(@style, 'none'))]//span[@class='lconnUploadContainer']/*[@class='lotusBtn']")
	public EnhancedWebElement uploadDlgBrowse;
	@Find("css=input[name=file]")
	public EnhancedWebElements uploadDlgFile;
	@Find("xpath=/html/body/div[@role='dialog' and not(contains(@style, 'none'))]//div[@class='lconnFileUploadLabel']//a")
	public EnhancedWebElement uploadDlgFileName;
	@Find("xpath=/html/body/div[@role='dialog' and not(contains(@style, 'none'))]//div[@class='lconnFileUploadLabel']//input")
	public EnhancedWebElement uploadDlgFileNameInput;
	@Find("xpath=/html/body/div[@role='dialog' and not(contains(@style, 'none'))]//input[@type='submit']")
	public EnhancedWebElement dialogOk;

	@Find(by = "{css=#list table.lotusTable}+{css=#list div.lconnEmpty}", timeout = 120)
	public EnhancedWebElement fileList;
	@Find(by = "partialLink=$0", timeout = 60)
	public EnhancedWebElement fileEntry;
	@Find("css=#list a.lconnDownloadable")
	public List<WebElement> fileListDownloadLinks;

	// ****** Detail Page ******/
	@Find("{css=button[id*='com_ibm_concord_lcext_ccdnewfrom']}+{css=#lotusContent div.lotusBtnContainer [title*='from a template file']}")
	public EnhancedWebElement detailPageCreateFileBtn;
	@Find("{css=button[id*='com_ibm_viewer_lcext_ccdview']}+{css=#lotusContent div.lotusBtnContainer [title='Preview the file in a browser']}")
	public EnhancedWebElement detailPageViewBtn;
	@Find("{css=button[id*='com_ibm_concord_lcext_ccdedit']}+{css=#lotusContent div.lotusBtnContainer [title='Edit in IBM Docs']}")
	public EnhancedWebElement detailPageEditInIBMDocsBtn;
	@Find("{css=button[id*='lconn_files_action_downloadfile']}+{css=#lotusContent div.lotusBtnContainer [title*='Download']}")
	public EnhancedWebElement detailPageDownloadBtn;
	@Find("{css=button[id*='lconn_files_action_sharefile']}+{css=#lotusContent div.lotusBtnContainer [title='Give others access to this file']}")
	public EnhancedWebElement detailPageShareBtn;
	@Find("{css=button[id*='lconn_files_action_more']}+{css=#lotusContent div.lotusBtnContainer [title='More Actions']}")
	public EnhancedWebElement detailPageMoreActionsBtn;
	@Find("{css=td[id*='com_ibm_concord_lcext_ccdpdfexport']}+{css=.dijitPopup tr.dijitMenuItem[title*='PDF']}")
	public EnhancedWebElement detailPageDownloadAsPdfBtn;
	@Find("{css=td[id*='com_ibm_concord_lcext_ccdmsformatexport']}+{css=.dijitPopup tr.dijitMenuItem[title*='Office Format']}")
	public EnhancedWebElement detailPageDownloadAsMS;
	@Find("css=#fileVersions a.lconnDownloadable[tabindex='-1']")
	public EnhancedWebElements detailPageDownloadLinks;
	@Find("css=#versions_link")
	public EnhancedWebElement versionsBtn;
	@Find("css=div.lotusDialogWrapper form")
	public EnhancedWebElement downloadVersionDialog;
	
	@Find("xpath=//table/tbody/tr/td[9]/div")
	public EnhancedWebElement versionAlignText;
	// ****** Detail Page End******/

	@Find("id=edit_in_ibm_docs_dec_div")
	public EnhancedWebElement editInIBMDocsDlgDec;

	@Find("css=input[name='_tagspres']")
	public EnhancedWebElement newDocDlgPresentationName;
	@Find("css=input[name='_tagsdoc']")
	public EnhancedWebElement newDocDlgDocumentName;
	@Find("css=input[name='_tagssheet']")
	public EnhancedWebElement newDocDlgSpreadsheetName;
	@Find("css=input[name='_shareFilePropagate']")
	public EnhancedWebElement newDocDlgShareFilePropagate;

	// ****** New Folder Dialog******/
	@Find("css=input[name='_label']")
	public EnhancedWebElement newFolderDlgName;
	@Find("css=textarea[name='_description']")
	public EnhancedWebElement newFolderDlgDescription;
	@Find("css=input[value='$0']")
	public EnhancedWebElement newFolderDlgVisibility;
	@Find("css=select[name='_shareCollectionUserType']")
	public EnhancedWebElement newFolderDlgShareUserType;
	@Find("css=select[name='_shareCollectionUserRole']")
	public EnhancedWebElement newFolderDlgShareUserRole;
	@Find("css=input[id*='lconn_share_widget_MemberInput'][id*='people']")
	public EnhancedWebElement newFolderDlgSharePeople;
	@Find("css=input[id*='lconn_share_widget_MemberInput'][id*='communities']")
	public EnhancedWebElement newFolderDlgShareCommunities;
	@Find("css=input[id*='lconn_core_AddGroups']")
	public EnhancedWebElement newFolderDlgShareGroups;
	@Find("{css=.lotusSuccess}-{link=$0}")
	public EnhancedWebElement newFolderOkMsg;
	// ****** New Folder Dialog End******/

	// ****** Share File Dialog *****/
	@Find("css=select[name='searchSourceDropdown']")
	public EnhancedWebElement shareFileDlgUserType;
	@Find("css=select[name='_shareFileUserRole']")
	public EnhancedWebElements shareFileDlgUserRole;
	@Find("css=input[id*='lconn_share_widget_MemberInput'][id*='people']")
	public EnhancedWebElements shareFileDlgPeopleName;
	@Find("css=input[id*='lconn_share_widget_MemberInput'][id*='communities']")
	public EnhancedWebElement shareFileDlgCommunityName;
	@Find("css=li[id*='lconn_share_widget_MemberInput'][id*='people_popup0']")
	public EnhancedWebElement shareFileDlgPeoplePopup;

	// ****** Share File Dialog END *****/

	@Find("css=input[name='taggedpdf']")
	public EnhancedWebElement downloadAsPDFDlgTaggedPdf;
	@Find(by = "id=download", timeout = 60)
	public EnhancedWebElement pdfDownloadLink;

	// **** New File for UT env.****/
	@Find("css=span[id*=_File_text]")
	public EnhancedWebElement menuFile;
	@Find("css=td[id*=_i_New_text]")
	public EnhancedWebElement menuNew;
	@Find("css=td[id*=_i_NewDoc_text]")
	public EnhancedWebElement menuNewDocumentUT;
	@Find("css=td[id*=_i_NewSheet_text]")
	public EnhancedWebElement menuNewSpreadsheetUT;
	@Find("css=td[id*=_i_NewPres_text]")
	public EnhancedWebElement menuNewPresentationUT;
	@Find("id=C_d_InputBoxInputArea")
	public EnhancedWebElement inputBoxInputArea;
	@Find("id=C_d_InputBoxOKButton_label")
	public EnhancedWebElement inputBoxOkBtn;

	public FilesPage(EnhancedWebDriver driver) {
		super(driver);

	}

	public FilesPage go() {
		if (driver.getUTStatus())
			return this;
		log.info("Go to Files");
		driver.get(driver.getConfig("app.files.url"));
		fileList.waitPresence();
		driver.sleep(3);
		return this;
	}

	public FilesPage upload(File file) {
		return upload(file, file.getName());
	}

	public FilesPage focus(String name) {
		log.info("Switch to My Files");
		driver.switchTo().window(name);
		return this;
	}

	public FilesPage share(String fileName, String[]... shareUsers) {
		log.info(String.format("Share %s with %s", fileName,
				Arrays.toString(shareUsers)));
		if (driver.isInternetExplorer())
			driver.sleep(2);
		openFileDetail(fileName);
		if (driver.isInternetExplorer()) {
			driver.sleep(1);
			detailPageShareBtn.sendKeys(Keys.RETURN);
		} else
			detailPageShareBtn.click(10, 10);
		int i = 0;
		for (String[] shareUser : shareUsers) {
			for (EnhancedWebElement shareDisplayed : shareFileDlgUserRole) {
				if (shareDisplayed.isDisplayed()) {
					break;
				}
				i++;
			}
			if ("person".equals(shareUser[0])) {
				new Select(shareFileDlgUserType).selectByIndex(0);
				new Select(shareFileDlgUserRole.get(i))
						.selectByValue(shareUser[1]);
				shareFileDlgPeopleName.get(i).sendKeys(shareUser[2]);
				shareFileDlgPeoplePopup.click();
			} else if ("community".equals(shareUser[0])) {
				new Select(shareFileDlgUserType).selectByIndex(2);
				new Select(shareFileDlgUserRole.get(i))
						.selectByValue(shareUser[1]);
				shareFileDlgCommunityName.sendKeys(shareUser[2]);
				shareFileDlgCommunityName.sendKeys(Keys.ENTER);
			}
		}
		shareFileDlgUserRole.get(i).submit();
		return this;
	}

	/**
	 * @param file
	 * @param label
	 * @return
	 */
	public FilesPage upload(File file, String label) {
		log.info(String.format("Upload file %s with name %s", file, label));
		// driver.javascript(JS_MOCK_UPLOAD_FORM);
		driver.sleep(1);
		if (driver.isInternetExplorer())
			btnUpload.sendKeys(Keys.ENTER);
		else
			btnUpload.click();
		driver.sleep(2);
		if (driver.isFirefox()) {
			// Disable popup file dialog on firefox
			driver.javascript("lconn.core.upload.provider.HtmlFileProvider.prototype._handleFakeButtonClick=function(_12, _13, _14) {var _15 = this._buildFileInput(_12, _13, _14,true);dojo.body().appendChild(_15);}");
			uploadDlgBrowse.click();
		}
		String filePath = driver.upload(file);
		// Temporary js fix for upload function. @author xinyutan@cn.ibm.com
		driver.javascript("document.getElementById('lconn_share_widget_Dialog_0_contents_contents').style.left='10px'");
		uploadDlgFile.getLast().sendKeys(filePath);
		driver.sleep(1);
		if (driver.isInternetExplorer())
			uploadDlgFileName.sendKeys(Keys.ENTER);
		else
			uploadDlgFileName.click();
		if (label != null) {
			uploadDlgFileNameInput.clear();
			uploadDlgFileNameInput.sendKeys(label + Keys.ENTER);
		}
		driver.sleep(1);
		dialogOk.click();
		fileEntry.setLocatorArgument(label);
		driver.sleep(1);
		fileEntry.waitPresence();
		return this;
	}

	public String uploadThenEdit(File file, String label) {
		return upload(file, label).edit(label);
	}

	/**
	 * 
	 * @param fileName
	 * @return window handle
	 */
	public String edit(String fileName) {
		log.info(String.format("Edit file %s in IBM Docs", fileName));
		if (driver.getUTStatus()) {
			editUTFile(fileName);
			return driver.getWindowHandle();
		}
		openFileDetail(fileName);
		driver.storeWindows();
		if (!detailPageEditInIBMDocsBtn.isDisplayed())
			driver.sleep(1);
		if (driver.isInternetExplorer())
			driver.sleep(2);
		detailPageEditInIBMDocsBtn.click();
		if (editInIBMDocsDlgDec.isPresent())
			dialogOk.click();
		return driver.waitNewWindow();
	}

	public String view(String fileName) {
		log.info(String.format("View file %s in IBM Docs", fileName));
		openFileDetail(fileName);
		driver.storeWindows();
		if (driver.isInternetExplorer())
			driver.sleep(2);
		detailPageViewBtn.click();
		return driver.waitNewWindow();
	}

	public String uploadThenView(File file, String label) {
		return upload(file, label).view(label);
	}

	public String editUTFile(String fileName) {
		String hrefurl = driver.getConfig("loginUrl").substring(7);
		Pattern p = Pattern.compile("[/]+");
		String[] urls = p.split(hrefurl);
		String fileurl = hrefurl.replace(urls[5], fileName);
		driver.get(fileurl);
		driver.sleep(5);
		return fileName;
	}

	public String editSharedWithMe(String fileName) {
		log.info(String.format("Edit Shared With Me file %s in IBM Docs",
				fileName));
		driver.sleep(1);
		if (driver.getUTStatus()) {
			editUTFile(fileName);
			return driver.getWindowHandle();
		}
		fileEntry.setLocatorArgument(FilenameUtils.getBaseName(fileName));
		if (!fileEntry.isPresent())
			if (driver.isInternetExplorer())
				btnSharedWithMe.sendKeys(Keys.ENTER);
			else
				btnSharedWithMe.click();
		fileEntry.click();
		driver.storeWindows();
		if (driver.isInternetExplorer())
			driver.sleep(2);
		detailPageEditInIBMDocsBtn.click();
		if (editInIBMDocsDlgDec.isPresent())
			dialogOk.click();
		return driver.waitNewWindow();
	}

	public String editDirectly(String fileName) {
		log.info(String.format("Edit file %s in IBM Docs", fileName));
		fileEntry.setLocatorArgument(FilenameUtils.getBaseName(fileName));
		if (!fileEntry.isPresent())
			btnMyFiles.click();
		String href = fileEntry.getAttribute("href");
		href = href.replace("files/app/file", "docs/app/doc/lcfiles")
				+ "/edit/content";
		return driver.openNewWindow(href);
	}

	public String newSpreadsheet(String name) {
		log.info("Create one new spreadsheet");
		if (driver.getUTStatus())
			return newSpreadsheetUT(name);
		driver.sleep(10);
		btnCreate.sendKeys(Keys.RETURN);
		driver.sleep(1);
		menuNewSpreadsheet.click();
		driver.sleep(300, TimeUnit.MILLISECONDS);
		newDocDlgSpreadsheetName.sendKeys(name);
		driver.storeWindows();
		newDocDlgSpreadsheetName.submit();
		return driver.waitNewWindow();
	}

	public String newSpreadsheetFromFile(String fileName, String newFileName) {
		log.info(String.format("Create new Spreadsheet %s from %S in IBM Docs",
				newFileName, fileName));
		openFileDetail(fileName);
		driver.storeWindows();
		detailPageCreateFileBtn.click();
		newDocDlgDocumentName.sendKeys(newFileName);
		newDocDlgDocumentName.submit();
		return driver.waitNewWindow();
	}

	public String newDocuementUT(String name) {
		menuFile.click();
		menuNew.click(1, 1);
		menuNewDocumentUT.click();
		inputBoxInputArea.clear();
		inputBoxInputArea.sendKeys(name);
		driver.storeWindows();
		inputBoxOkBtn.click();
		return driver.waitNewWindow();
	}

	public String newSpreadsheetUT(String name) {
		menuFile.click();
		menuNew.click(1, 1);
		menuNewSpreadsheetUT.click();
		inputBoxInputArea.clear();
		inputBoxInputArea.sendKeys(name);
		driver.storeWindows();
		inputBoxOkBtn.click();
		return driver.waitNewWindow();
	}

	public String newPresentationUT(String name) {
		menuFile.click();
		menuNew.click(1, 1);
		menuNewPresentationUT.click();
		inputBoxInputArea.clear();
		inputBoxInputArea.sendKeys(name);
		driver.storeWindows();
		inputBoxOkBtn.click();
		return driver.waitNewWindow();
	}

	public String newDocument(String name) {
		log.info("Create one new document");
		if (driver.getUTStatus())
			return newDocuementUT(name);
		driver.sleep(10);
		btnCreate.sendKeys(Keys.RETURN);
		driver.sleep(1);
		menuNewDocument.click();
		driver.sleep(300, TimeUnit.MILLISECONDS);
		driver.storeWindows();
		newDocDlgDocumentName.sendKeys(name);
		newDocDlgDocumentName.submit();
		return driver.waitNewWindow();
	}

	public String newDocumentFromFile(String fileName, String newFileName) {
		log.info(String.format("Create new dcoument %s from %S in IBM Docs",
				newFileName, fileName));
		openFileDetail(fileName);
		driver.storeWindows();
		detailPageCreateFileBtn.click();
		newDocDlgDocumentName.sendKeys(newFileName);
		newDocDlgDocumentName.submit();
		return driver.waitNewWindow();
	}

	public String newPresentation(String name) {
		log.info("Create one new presentation");
		if (driver.getUTStatus())
			return newPresentationUT(name);
		driver.sleep(10);
		btnCreate.sendKeys(Keys.RETURN);
		driver.sleep(1);
		menuNewPresentation.click();
		driver.sleep(300, TimeUnit.MILLISECONDS);
		newDocDlgPresentationName.sendKeys(name);
		driver.storeWindows();
		newDocDlgPresentationName.submit();
		return driver.waitNewWindow();
	}

	public String newPresentationFromFile(String fileName, String newFileName) {
		log.info(String.format(
				"Create new Presentation %s from %S in IBM Docs", newFileName,
				fileName));
		openFileDetail(fileName);
		driver.storeWindows();
		detailPageCreateFileBtn.click();
		newDocDlgPresentationName.sendKeys(newFileName);
		newDocDlgPresentationName.submit();
		return driver.waitNewWindow();
	}

	public FilesPage newFolder(String name) {
		return newFolder(name, null, null);
	}

	public FilesPage newFolder(String name, String description,
			String visiblity, String[]... shareUsers) {
		log.info("Create one new folder");
		btnCreate.click();
		menuNewFolder.click();
		newFolderDlgName.sendKeys(name);
		if (description != null) {
			newFolderDlgDescription.sendKeys(description);
		}
		if (visiblity != null) {
			newFolderDlgVisibility.setLocatorArgument(visiblity);
			newFolderDlgVisibility.click();
			for (String[] shareUser : shareUsers) {
				if ("person".equals(shareUser[0])) {
					new Select(newFolderDlgShareUserType).selectByIndex(0);
					new Select(newFolderDlgShareUserRole)
							.selectByValue(shareUser[1]);
					newFolderDlgSharePeople.sendKeys(shareUser[2]);
					newFolderDlgSharePeople.sendKeys(Keys.ENTER);
				} else if ("group".equals(shareUser[0])) {
					new Select(newFolderDlgShareUserType).selectByIndex(1);
					new Select(newFolderDlgShareUserRole)
							.selectByValue(shareUser[1]);
					newFolderDlgShareGroups.sendKeys(shareUser[2]);
					newFolderDlgShareGroups.sendKeys(Keys.ENTER);
				} else if ("community".equals(shareUser[0])) {
					new Select(newFolderDlgShareUserType).selectByIndex(2);
					new Select(newFolderDlgShareUserRole)
							.selectByValue(shareUser[1]);
					newFolderDlgShareCommunities.sendKeys(shareUser[2]);
					newFolderDlgShareCommunities.sendKeys(Keys.ENTER);
				}
			}
		}
		newFolderDlgName.submit();
		newFolderOkMsg.setLocatorArgument(name);
		newFolderOkMsg.waitPresence();
		return this;
	}

	public File download(final String fileName, final File target) {
		log.info(String.format("Download %s to %s", fileName, target));
		openFileDetail(fileName);
		versionsBtn.click();
		String fileUrl = detailPageDownloadLinks.get(0).getAttribute("href");
		fileUrl = fileUrl.replaceFirst("/version/[^/]+", "");
		return driver.download(fileUrl, target);
	}

	public File download(final String fileName, final File target,
			int versionIndex) {
		log.info(String.format("Download %s Version %s to %s", fileName,
				versionIndex, target));
		openFileDetail(fileName);
		versionsBtn.click();
		String fileUrl = detailPageDownloadLinks.get(versionIndex - 1)
				.getAttribute("href");
		return driver.download(fileUrl, target);
	}

	public File downloadAsPDF(final String fileName, final File target) {
		log.info(String.format("Download %s file as PDF to %s", fileName,
				target));
		openFileDetail(fileName);
		detailPageMoreActionsBtn.click();
		detailPageDownloadAsPdfBtn.click();
		driver.storeWindows();
		if (this.downloadVersionDialog.isPresent()) {
			this.downloadVersionDialog.submit();
		}
		downloadAsPDFDlgTaggedPdf.click();
		downloadAsPDFDlgTaggedPdf.submit();
		String pdfWin = driver.waitNewWindow();
		driver.sleep(3);
		driver.switchToWindow(pdfWin);
		String href = pdfDownloadLink.getAttribute("href");
		// driver.close();
		log.info("download file href:" + href);
		return driver.download(href, target);
	}

	public File downloadAsMS(final String fileName, final File target) {
		log.info(String.format("Download %s file as MS Office to %s", fileName,
				target));
		openFileDetail(fileName);
		driver.storeWindows();
		detailPageMoreActionsBtn.click();
		detailPageDownloadAsMS.click();
		if (this.downloadVersionDialog.isPresent()) {
			this.downloadVersionDialog.submit();
		}
		String msWin = driver.waitNewWindow();
		driver.sleep(3);
		driver.switchToWindow(msWin);
		String href = pdfDownloadLink.getAttribute("href");
		log.info("download file href:" + href);
		driver.sleep(2);
		return driver.download(href, target);
	}

	public FilesPage openFileDetail(final String fileName) {
		driver.sleep(1);
		fileEntry.setLocatorArgument(FilenameUtils.getBaseName(fileName));
		if (!fileEntry.isPresent())
			btnMyFiles.doubleClick();
		fileEntry.click();
		return this;
	}
}
