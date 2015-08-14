package com.ibm.docs.test.gui.pages;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;

import com.ibm.docs.test.common.GraphicsUtil;
import com.ibm.docs.test.gui.support.EnhancedWebDriver;
import com.ibm.docs.test.gui.support.EnhancedWebElement;
import com.ibm.docs.test.gui.support.EnhancedWebElements;
import com.ibm.docs.test.gui.support.Find;

public class DocsEditorPage extends BasePage {
	private static final Logger log = Logger.getLogger(DocsEditorPage.class);

	// ****** File Menu ******//
	@Find("css=span[id*=_File_text]")
	public EnhancedWebElement menuFile;
	@Find("css=td[id*=_i_New_text]")
	public EnhancedWebElement menuNew;
	@Find("css=td[id*=_i_NewDoc_text]")
	public EnhancedWebElement menuNewDocument;
	@Find("css=td[id*=_i_NewSheet_text]")
	public EnhancedWebElement menuNewSpreadsheet;
	@Find("css=td[id*=_i_NewPres_text]")
	public EnhancedWebElement menuNewPresentation;
	@Find("css=td[id*=_i_NewFromTemplate_text]")
	public EnhancedWebElement menuNewFromTemplate;
	@Find("css=td[id*=_i_NewDocFromTemp_text]")
	public EnhancedWebElement menuNewDocumentFromTemplate;
	@Find("css=td[id*=_i_NewSheetFromTemp_text]")
	public EnhancedWebElement menuNewSpreadsheetFromTemplate;
	@Find("css=td[id*=_i_Discard_text]")
	public EnhancedWebElement menuRevert;
	@Find("css=td[id*=_i_Save_text]")
	public EnhancedWebElement menuSave;
	@Find("css=td[id*=_i_SaveAs_text]")
	public EnhancedWebElement menuSaveAs;
	@Find("css=td[id*=_i_Import_text]")
	public EnhancedWebElement menuImport;
	@Find("css=td[id*=_i_Publish_text]")
	public EnhancedWebElement menuPublishVersion;
	@Find("css=td[id*=_i_Share_text]")
	public EnhancedWebElement menuShare;
	@Find("css=td[id*=_i_ViewFileDetails_text]")
	public EnhancedWebElement menuViewFileDetails;
	@Find("css=td[id*=_i_recentOpenFiles_text]")
	public EnhancedWebElement menuRecentOpenFiles;
	@Find("css=td[id*=_i_PrintToPDF_text]")
	public EnhancedWebElement menuPrintToPDF;
	@Find("css=td[id*=_i_Print_text]")
	public EnhancedWebElement menuPrintText;
	
    //comment
	@Find("css=.time")
	public EnhancedWebElement commentDialogTime;
	@Find("css=textarea.scrollbar")
	public EnhancedWebElement commentTextNew;
	@Find("css=.btn.comment")
	public EnhancedWebElement submitCommentBtn;
	
	
	
	// ****** File Menu End******//

	// ****** Edit Menu ******//
	@Find("css=span[id*=_Edit_text]")
	public EnhancedWebElement menuEdit;
	@Find("css=td[id*=_i_Undo_text]")
	public EnhancedWebElement menuUndo;
	@Find("css=td[id*=_i_Redo_text]")
	public EnhancedWebElement menuRedo;
	@Find("css=td[id*=_i_Cut_text]")
	public EnhancedWebElement menuCut;
	@Find("css=td[id*=_i_Copy_text]")
	public EnhancedWebElement menuCopy;
	@Find("css=td[id*=_i_Paste_text]")
	public EnhancedWebElement menuPaste;
	@Find("css=td[id*=_i_SelectAll_text]")
	public EnhancedWebElement menuSelectAll;
	// ****** Edit Menu END******//

	// ****** View Menu******//
	@Find("css=tr[id*=_i_Sidebar]")
	public EnhancedWebElement Sidebar;
	@Find("css=tr[id*=_i_Toolbar]")
	public EnhancedWebElement Toolbar;
	@Find("css=span[id*=_View_text]")
	public EnhancedWebElement View;
	// ****** View Menu END******//

	// ******Insert Menu*******//
	@Find("css=span[id*=_Insert_text]")
	public EnhancedWebElement menuInsert;
	@Find("css=#S_d_ImportTextOKButton_label")
	public EnhancedWebElement importTextOKDlgOk;

	// ******Format Menu*******//
	@Find("css=span[id*=_Format_text]")
	public EnhancedWebElement menuFormat;
	@Find("css=td[id*=_i_TextPro]")
	public EnhancedWebElement menuTextProperties;
	@Find("css=td[id*=_i_Bold_text]")
	public EnhancedWebElement menuBold;
	@Find("css=td[id*=_i_Italic_text]")
	public EnhancedWebElement menuItalic;
	@Find("css=td[id*=_i_Underline_text]")
	public EnhancedWebElement menuUnderline;
	@Find("css=[id*=_i_Strikethrough_text]")
	public EnhancedWebElement menuStrikethrough;
	// ****** Format Menu END******//

	// ************Table menu**************//
	@Find("id*=_m_Table")
	public EnhancedWebElement menuTable;
	@Find("id*=_i_Create")
	public EnhancedWebElement menuCreateTable;
	// ************Table menu End**********//

	// ************Team menu**************//
	@Find("css=span[id*=_Team_text]")
	public EnhancedWebElement menuTeam;
	@Find("css=td[id*=_TeamAddComment_text]")
	public EnhancedWebElement menuAddComment;
	@Find("xpath=//table[@class='dijitReset dijitMenuTable'")
	public EnhancedWebElement menuSelectBtn;
	@Find("id*=_i_EditAssignment")
	public EnhancedWebElement menuEditAssignment;
	@Find("id*=_i_ReopenAssignment")
	public EnhancedWebElement menuReopenAssignment;
	@Find("id*=_i_ReassignAssignment")
	public EnhancedWebElement menuReassignAssignment;
	// ************Team menu End**********//

	// ************Assign a Section/Cells/Slides dialog**************//
	@Find("id=C_d_AssignASectionDialogtaskTitle")
	public EnhancedWebElement TaskTitle;
	@Find("id=C_d_AssignASectionDialogassignee")
	public EnhancedWebElement TaskAssignto;
	@Find("id=C_d_AssignASectionDialogtaskDescription")
	public EnhancedWebElement TaskDescription;
	@Find("id=C_d_AssignASectionDialogassignee_popup")
	public EnhancedWebElement AssigneePopup;
	@Find("css=#C_d_AssignASectionDialogassignee_popup li")
	public EnhancedWebElements AssigneePopupName;
	@Find("id=C_d_AssignASectionDialogwriteType")
	public EnhancedWebElement WriteSection;
	@Find("id=C_d_AssignASectionDialogreviewType")
	public EnhancedWebElement ReviewSection;
	@Find("id=C_d_AssignASectionDialogOKButton")
	public EnhancedWebElement TaskSubmit;
	@Find("id=C_d_AssignASectionDialogCancelButton")
	public EnhancedWebElement TaskCancel;
	// ************Assign dialog End**************//

	// ************Assignment section in editor**************//
	@Find("css=div.range-border.active-cell-border.concordTaskWriteBorderTop")
	public EnhancedWebElements sectionBorders;
	@Find("css=div.concordTaskWriteBorderTop span.concordTaskTitle")
	public EnhancedWebElements sectionTitles;
	@Find("css=div.concordTaskWriteCompleteBorder")
	public EnhancedWebElements sectionCompletes;
	@Find("script=return dojo.query('div.concordTaskWriteCompleteBorder')[$0];")
	public EnhancedWebElement sectionComplete;
	// ************Assignment section in editor End**************//

	// ************Assignment section in sidebar**************//
	@Find("css=span.ll_assign_a_section_cursor")
	public EnhancedWebElement linkAssign;
	@Find("css=div#taskActionDiv span#dijit_form_DropDownButton_0")
	public EnhancedWebElement buttonActions;
	@Find("css=tr#P_ssp_MarkSlidesDone")
	public EnhancedWebElement menuActionsMarkSlideComplete;
	// ************Assignment section in sidebar End**************//

	// ******Publish Dialog******//
	@Find("id=C_d_PublishDialogVersionTextArea")
	public EnhancedWebElement publishDialogVersionTextArea;
	@Find("id=C_d_PublishDialogOKButton")
	public EnhancedWebElement publishDialogOKButton;
	@Find("id=C_d_PublishDialogCancelButton")
	public EnhancedWebElement publishDialogCancelButton;
	@Find("id=lotus_error_message_published")
	public EnhancedWebElement publishedMessage;
	// ******Publish Dialog END******//

	// ******Share a document Dialog******//
	@Find("id=C_d_ShareDocumentDialogemailAddress")
	public EnhancedWebElement shareDocumentDialogemailAddress;
	@Find("id=C_d_ShareDocumentDialogOKButton")
	public EnhancedWebElement shareDocumentDialogOKButton;
	@Find("id=C_d_ShareDocumentDialogCancelButton")
	public EnhancedWebElement shareDocumentDialogCancelButton;
	// ******Share a document Dialog END******//

	// ******Revert Dialog****//
	@Find("id=C_d_ConfirmBoxOKButton_label")
	public EnhancedWebElement confirmDlgOk;
	@Find("id=C_d_ConfirmBoxNoButton_label")
	public EnhancedWebElement confirmDlgNo;
	// ******Revert Dialog END****//

	// ******Welcome Dialog****//
	@Find("id=C_d_WelcomeOKButton_label")
	public EnhancedWebElement welcomeDialogOK;
	@Find("id=C_d_WelcomeNotShowChk")
	public EnhancedWebElement welcomeDialogNotShow;
	// ******Welcome Dialog END****//

	@Find("id=C_d_InputBoxInputArea")
	public EnhancedWebElement inputBoxInputArea;
	@Find("id=C_d_InputBoxOKButton_label")
	public EnhancedWebElement inputBoxOkBtn;

	// ******Spreadsheet template Dialog****//
	@Find("id=S_d_TemplateOKButton")
	public EnhancedWebElement spreadsheetTemplatesDlgOk;
	@Find("css=#spreadsheetTemplatesDlgResults>li")
	public EnhancedWebElements spreadsheetTemplatesDlgResults;
	@Find("id=$0")
	public EnhancedWebElement spreadsheetTemplatesDlgResult;
	// ******Spreadsheet template Dialog END****//

	@Find("id=C_d_SaveAsDialogInputArea")
	public EnhancedWebElement saveAsDlgInputArea;
	@Find("id=C_d_SaveAsDialogOKButton")
	public EnhancedWebElement saveAsDlgOk;
	@Find(by = "css=div.lotusSuccess[id='lotus_error_message_savedas']", timeout = 30)
	public EnhancedWebElement saveAsSuccessMsg;
	@Find("css=span[id*=PrintToPdfOKButton_label]")
	public EnhancedWebElement printToPdfDlgOK;
	@Find(by = "id=download", timeout = 60)
	public EnhancedWebElement pdfDownloadLink;

	@Find("id=doc_title_text")
	public EnhancedWebElement docTitle;
	@Find("id=ll_new_comment_field")
	public EnhancedWebElement commentText;
	@Find("id=sidebar_comments_pane_button")
	public EnhancedWebElement commentBtn;
	@Find("css=#concord_comment_btn_label")
	public EnhancedWebElement commentBtnNew;

	@Find("css=#ll_new_comment_add_btn > span > span")
	public EnhancedWebElement commentAddBtn;
	@Find("css=#ll_pane_btn_img_comments_id")
	public EnhancedWebElement commentMenuList;
	@Find("css=#dijit_CheckedMenuItem_0_text")
	public EnhancedWebElement commentSearch;
	@Find("css=#ll_comment_search_input")
	public EnhancedWebElement commentSearchText;
	@Find("css=#ll_comment_search_icon")
	public EnhancedWebElement commentSearchIcon;
	@Find("{css=div.ll_comment_div_orig_expanded span.ll_responsd_btn}+{css=div.ll_comment_div_orig_ie_expanded span.ll_responsd_btn_ie}")
	public EnhancedWebElement commentResponseBtn;
	@Find("css=div[id*='ll_comments_orig'] > div:first-child")
	public EnhancedWebElements comments;
	@Find("xpath=//div[contains(@class, 'll_comment_div_orig') and contains(div, '$0')]")
	public EnhancedWebElement comment;
	@Find("xpath=//div[@id='ll_comments_func_frame']/div[$0+1]/div[1]/div[1]")
	public EnhancedWebElement commentGroup;
	@Find("css=div.commonsprites-atnTrash16[tabindex='0']")
	public EnhancedWebElement commentDeleteBtn;
	@Find("css=#sidebar_comments_pane_button")
	public EnhancedWebElement commentsPaneBtn;
	@Find("xpath=//div[$0]/div/div[contains(@class, 'll_comment_div_orig')]/div")
	public EnhancedWebElement commentStr;
	@Find("css=#ll_comments_empty_info")
	public EnhancedWebElement commentEmpty;
	@Find("css=span[class=ll_filter_hightlight]")
	public EnhancedWebElement hightlightComment;

	@Find("css=textarea.scrollbar")
	public EnhancedWebElement commentsText;
	@Find("xpath=//a[@class='btn comment']")
	public EnhancedWebElement commentButton;
	@Find("xpath=//span[@class='dijitReset dijitInline dijitButtonNode lotusBtn']")
	public EnhancedWebElement commentsButton;

	@Find(by = "css=#lotus_saved_image:not([style*='none'])", timeout = 120)
	public EnhancedWebElement loadedIndicator;

	@Find("css=body")
	public EnhancedWebElement body;

	/******** unsupport feature dialog ***********************/
	@Find("css=#C_d_UnsupportFeature")
	public EnhancedWebElement unsupportFeatureDialog;
	@Find("css=#C_d_UnsupportFeatureNotShowChk")
	public EnhancedWebElement unsupportFeatureDialogNotShow;
	@Find("css=#C_d_UnsupportFeatureOKButton")
	public EnhancedWebElement unsupportFeatureDialogOK;
	/******** unsupport feature dialog end ******************/

	// ***********Toolbars******************//
	@Find("css=a.cke_button_justifyleft")
	public EnhancedWebElement toolbarAlignLeft;
	@Find("css=a.cke_button_justifyright")
	public EnhancedWebElement toolbarAlignRight;
	@Find("css=a.cke_button_justifycenter")
	public EnhancedWebElement toolbarAlignCenter;

	@Find("css=a.cke_button_bold:not(.cke_disabled)")
	public EnhancedWebElement toolbarBold;
	@Find("css=a.cke_button_italic:not(.cke_disabled)")
	public EnhancedWebElement toolbarItalic;
	@Find("css=a.cke_button_underline:not(.cke_disabled)")
	public EnhancedWebElement toolbarUnderline;
	@Find("css=a.cke_button_strike:not(.cke_disabled)")
	public EnhancedWebElement toolbarStrikethrough;

	// ***********Toolbars end******************//

	public DocsEditorPage(EnhancedWebDriver driver) {
		super(driver);
	}

	public DocsEditorPage focus(String name) {
		log.info("Go to IBM Docs editor");
		driver.sleep(2);
		driver.switchToWindow(name);
		driver.manage().window().maximize();
		if (driver.getUTStatus())
			return this;
		// driver.manage().window().setPosition(new Point(0,0));
		// driver.manage().window().setSize(new Dimension(1280, 768));
		if (!loadedIndicator.isPresent())
			driver.sleep(2);
		// loadedIndicator.waitPresence();
		String version = (String) driver.javascript("return concord_version;");
		testEnv.setOutputProperty("Docs Version", version);
		log.debug("Docs Version: " + version);
		if (welcomeDialogOK.isPresent()) {
			welcomeDialogNotShow.click();
			welcomeDialogOK.click();
		}

		if (unsupportFeatureDialog.isPresent()) {
			unsupportFeatureDialogNotShow.click();
			unsupportFeatureDialogOK.click();
		}
		// Give docs some time to make its ui stable
		driver.sleep(5);

		return this;
	}

	public String getDocTitle() {
		return docTitle.getAttribute("title");
	}

	public DocsEditorPage revertToLastVersion() {
		log.info("Select menu File->Revert to Last Version");
		menuFile.click();
		menuRevert.click();
		confirmDlgOk.click();
		loadedIndicator.waitPresence();
		return this;
	}

	public DocsEditorPage publishVersion(String description) {
		log.info("Publish the file");
		menuFile.click();
		menuPublishVersion.click();
		publishDialogVersionTextArea.clear();
		publishDialogVersionTextArea.sendKeys(description);
		publishDialogOKButton.click();
		publishedMessage.waitPresence(60);
		driver.sleep(2);
		return this;
	}

	public String publishVersion() {
		log.info("Publish the file");
		menuFile.click();
		menuPublishVersion.click();
		publishDialogOKButton.click();
		publishedMessage.waitPresence(60);
		String message = publishedMessage.getText();
		return message;
	}

	public String newSpreadsheet(String name) {
		log.info("Select menu File->New->Spreadsheet");
		menuFile.click();
		driver.sleep(500, TimeUnit.MILLISECONDS);
		menuNew.click(1, 1);
		menuNewSpreadsheet.click();
		inputBoxInputArea.clear();
		inputBoxInputArea.sendKeys(name);
		driver.storeWindows();
		inputBoxOkBtn.click();
		return driver.waitNewWindow();
	}

	public String newSpreadsheetFromTemplate(String name, int templateIndex) {
		log.info("Select menu File->New->From Template->Spreadsheet");
		menuFile.click();
		menuNew.click(1, 1);
		menuNewFromTemplate.click();
		menuNewSpreadsheetFromTemplate.click();
		log.info(String.format("Select template %s ", templateIndex + 1));
		spreadsheetTemplatesDlgResults.get(templateIndex).click();
		spreadsheetTemplatesDlgOk.click();
		inputBoxInputArea.clear();
		inputBoxInputArea.sendKeys(name);
		driver.storeWindows();
		inputBoxOkBtn.click();
		return driver.waitNewWindow();
	}

	public String newSpreadsheetFromTemplate(String name, String tempalteId) {
		log.info("Select menu File->New->From Template->Spreadsheet");
		menuFile.click();
		menuNew.click(1, 1);
		menuNewFromTemplate.click();
		menuNewSpreadsheetFromTemplate.click();
		log.info(String.format("Select template %s", tempalteId));
		spreadsheetTemplatesDlgResult.setLocatorArgument(tempalteId);
		spreadsheetTemplatesDlgResult.click();
		spreadsheetTemplatesDlgOk.click();
		inputBoxInputArea.clear();
		inputBoxInputArea.sendKeys(name);
		driver.storeWindows();
		inputBoxOkBtn.click();
		return driver.waitNewWindow();
	}

	public String newDocument(String name) {
		log.info("Select menu File->New->Document");
		menuFile.click();
		menuNew.click(1, 1);
		menuNewDocument.click();
		inputBoxInputArea.clear();
		inputBoxInputArea.sendKeys(name);
		driver.storeWindows();
		inputBoxOkBtn.click();
		return driver.waitNewWindow();
	}

	public String newDocumentFromTemplate(String name) {
		log.info("Select menu File->New->From Template->Document");
		menuFile.click();
		menuNew.click(1, 1);
		menuNewFromTemplate.click();
		menuNewDocumentFromTemplate.click();
		inputBoxInputArea.clear();
		inputBoxInputArea.sendKeys(name);
		driver.storeWindows();
		inputBoxOkBtn.click();
		return driver.waitNewWindow();
	}

	public String newPresentation(String name) {
		log.info("Select menu File->New->Presentation");
		menuFile.click();
		menuNew.click(1, 1);
		menuNewPresentation.click();
		inputBoxInputArea.clear();
		inputBoxInputArea.sendKeys(name);
		driver.storeWindows();
		inputBoxOkBtn.click();
		return driver.waitNewWindow();
	}

	public DocsEditorPage saveByMenu() {
		log.info("Select menu File->Save");
		menuFile.click();
		menuSave.click();
		return this;
	}

	public String saveAs(String name) {
		log.info("Select menu File->Save As");
		menuFile.click();
		menuSaveAs.click();
		saveAsDlgInputArea.sendKeys(name);
		driver.storeWindows();
		saveAsDlgOk.click();
		return driver.waitNewWindow();
	}

	public String importFromSpreadsheet() {
		log.info("Select menu File->Import...");
		menuFile.click();
		menuImport.click();
		return driver.waitNewWindow();
	}

	public File printToPdf(File target) {
		menuFile.click();
		menuPrintToPDF.click();
		driver.storeWindows();
		printToPdfDlgOK.click();
		driver.switchToWindow(driver.waitNewWindow());
		String href = (String) driver
				.javascript(
						"return location.protocol + '//' + location.host + arguments[0].getAttribute('href');",
						pdfDownloadLink);
		driver.close();
		return driver.download(href, target);
	}

	public DocsEditorPage undo() {
		if (driver.isInternetExplorer()) {
			this.menuEdit.click();
			this.menuUndo.click();
		} else {
			String keys = Keys.chord(Keys.CONTROL, "z");
			driver.getKeyboard().sendKeys(keys);
		}

		driver.sleep(2);
		return this;
	}

	public DocsEditorPage redo() {
		if (driver.isInternetExplorer()) {
			this.menuEdit.click();
			this.menuRedo.click();
		} else {
			String keys = Keys.chord(Keys.CONTROL, "y");
			driver.getKeyboard().sendKeys(keys);
		}
		driver.sleep(2);
		return this;
	}

	// public DocsEditorPage addComment(String message) {
	// if (!commentText.isPresent() || !commentText.isDisplayed())
	// commentBtn.click();
	// commentText.sendKeys(message);
	// commentAddBtn.click();
	// comment.setLocatorArgument(message);
	// comment.waitPresence();
	// return this;
	// }

	@Find("css=span[id*=concord_comment_btn_label]")
	public EnhancedWebElement test;

	public DocsEditorPage addComment(String message) {
		commentsButton.click();
		// menuTeam.click();
		// menuAddComment.click();
		if (!commentsText.isPresent() || !commentsText.isDisplayed()) {
			commentsButton.click();
			// menuTeam.click();
			// menuAddComment.click();
		}
		commentsText.clear();
		commentsText.sendKeys(message);
		commentButton.click();
		return this;
	}
	
public DocsEditorPage addCommentNew(String message) {
		
		commentBtnNew.click();
		commentTextNew.sendKeys(message);
		submitCommentBtn.click();
		//comment.setLocatorArgument(message);
		return this;
	}
	
	

	@Find("xpath=//div[@class='scrolled-window']//div/div[2]/div[1]")
	public EnhancedWebElements commentsTexts;

	/**
	 * Delete n-th comment, start form 1
	 * 
	 * @param num
	 * @return
	 */
	public DocsEditorPage deleteComment(int num) {
		commentGroup.setLocatorArgument(num);
		if (commentGroup.getAttribute("class").contains("collapse"))
			commentGroup.click();
		commentDeleteBtn.click();
		confirmDlgOk.click();
		return this;
	}

	public DocsEditorPage expandComments() {
		if (!commentsPaneBtn.getAttribute("class").contains("dijitSelected")) {
			commentsPaneBtn.click();
		}
		return this;
	}

	public DocsEditorPage deleteSelectedComment() {
		commentDeleteBtn.click();
		confirmDlgOk.click();
		return this;
	}

	public DocsEditorPage cut() {
		if (driver.isInternetExplorer()) {
			menuEdit.click();
			menuCut.click();
		} else {
			String keys = Keys.chord(Keys.CONTROL, "x");
			driver.getKeyboard().sendKeys(keys);
		}

		return this;
	}

	public DocsEditorPage copy() {
		if (driver.isInternetExplorer()) {
			menuEdit.click();
			menuCopy.click();
		} else {
			String keys = Keys.chord(Keys.CONTROL, "c");
			driver.getKeyboard().sendKeys(keys);
		}

		return this;
	}

	public DocsEditorPage paste() {
		if (driver.isInternetExplorer()) {
			menuEdit.click();
			menuPaste.click();
		} else {
			String keys = Keys.chord(Keys.CONTROL, "v");
			driver.getKeyboard().sendKeys(keys);
		}
		return this;
	}

	public DocsEditorPage selectAll() {
		if (driver.isInternetExplorer()) {
			menuEdit.click();
			menuSelectAll.click();
		} else {
			String keys = Keys.chord(Keys.CONTROL, "a");
			driver.getKeyboard().sendKeys(keys);
		}
		return this;
	}

	public DocsEditorPage bold() {
		log.info("set bold via clicking Bold toolbar.");
		this.menuFormat.click();
		this.menuTextProperties.click();
		this.menuBold.click();
		return this;
	}

	public DocsEditorPage italic() {
		log.info("set italic via clicking Italic toolbar.");
		this.menuFormat.click();
		this.menuTextProperties.click();
		this.menuItalic.click();
		return this;
	}

	public DocsEditorPage underline() {
		log.info("set underline via clicking Underline toolbar.");
		this.menuFormat.click();
		this.menuTextProperties.click();
		this.menuUnderline.click();
		return this;
	}

	public DocsEditorPage strikethrough() {
		log.info("set strikethrough via menu Format-->Text Properties-->Strikethrough.");
		menuFormat.click();
		menuTextProperties.click();
		menuStrikethrough.click();
		driver.sleep(1);
		return this;
	}

	public DocsEditorPage alignLeft() {
		log.info("set align left via clicking AlignLeft toolbar.");
		toolbarAlignLeft.click();
		driver.sleep(1);
		return this;
	}

	public DocsEditorPage alignRight() {
		log.info("set align right via clicking alignRight toolbar.");
		toolbarAlignRight.click();
		driver.sleep(1);
		return this;
	}

	public DocsEditorPage alignCenter() {
		log.info("set align center via clicking alignCenter toolbar.");
		toolbarAlignCenter.click();
		driver.sleep(1);
		return this;
	}

	public DocsEditorPage viewSidebar(boolean opened) {
		boolean checked = true;
		View.click();
		driver.sleep(1);
		String classes = Sidebar.getAttribute("class");
		if (!classes.contains("dijitCheckedMenuItemChecked")) {
			checked = false;
		}
		if (checked != opened)
			Sidebar.sendKeys(Keys.ENTER);
		else
			Sidebar.sendKeys(Keys.ESCAPE);
		return this;
	}

	public DocsEditorPage viewToolbar(boolean opened) {
		boolean checked = true;
		View.click();
		driver.sleep(1);
		String classes = Toolbar.getAttribute("class");
		if (!classes.contains("dijitCheckedMenuItemChecked")) {
			checked = false;
		}
		if (checked != opened)
			Toolbar.sendKeys(Keys.ENTER);
		else
			Toolbar.sendKeys(Keys.ESCAPE);
		return this;
	}

	/**
	 * Fill values in Assignment dialog
	 * 
	 * @param title
	 *            task titile
	 * @param assignto
	 *            assign task to sb.
	 * @param writesection
	 *            true means that write this section, or else review this
	 *            section
	 * @return
	 */
	public DocsEditorPage setTask(String title, String assignto,
			boolean writesection) {
		log.info("Set task properties by Assign Task dialog");
		driver.sleep(1);
		TaskTitle.sendKeys(title);
		TaskDescription.sendKeys("Section test");
		if (writesection) {
			if (!WriteSection.isSelected())
				WriteSection.click();
		} else {
			if (!ReviewSection.isSelected())
				ReviewSection.click();
		}
		TaskAssignto.sendKeys(assignto);
		if (AssigneePopup.isDisplayed())
			AssigneePopupName.get(1).click();
		TaskSubmit.click();
		return this;
	}

	public DocsEditorPage clickAssignLink() {
		log.info("Click Assign link in sidebar");
		linkAssign.click();
		return this;
	}

	public DocsEditorPage focusHTML(String name) {
		driver.sleep(2);
		driver.switchToWindow(name);
		log.info("Go to IBM Docs HTML page");
		driver.manage().window().maximize();
		menuFile.waitPresence(60);
		// Enable after viewer_version is ready
		// String version = "IBM Viewer " +
		// driver.javascript("return viewer_version;") ;
		// testEnv.setOutputProperty("Viewer Version", version);
		// log.debug("Viewer Version: " + version);
		driver.sleep(3);
		return this;
	}

	/**
	 * Fill in Share dialog in Editor
	 * 
	 * @param coeditor
	 * @return
	 */
	public DocsEditorPage shareInEditorDlg(String coeditor) {
		shareDocumentDialogemailAddress.sendKeys(coeditor);
		driver.sleep(1);
		shareDocumentDialogemailAddress.sendKeys(Keys.DOWN);
		driver.sleep(500, TimeUnit.MILLISECONDS);
		shareDocumentDialogemailAddress.sendKeys(Keys.TAB);
		driver.sleep(500, TimeUnit.MILLISECONDS);
		if (driver.isInternetExplorer())
			shareDocumentDialogOKButton.sendKeys(Keys.ENTER);
		else
			shareDocumentDialogOKButton.click();
		driver.sleep(1);
		return this;
	}

	public BufferedImage screenshot(WebElement element, String path) {
		File file = ((TakesScreenshot) driver.getWebDriver())
				.getScreenshotAs(OutputType.FILE);

		BufferedImage bufferedImage = GraphicsUtil.loadImage(file
				.getAbsolutePath());
		if (element != null) {
			Point p = element.getLocation();
			bufferedImage = bufferedImage
					.getSubimage(p.getX(), p.getY(), element.getSize()
							.getWidth(), element.getSize().getHeight());
		}
		if (path != null)
			GraphicsUtil.storeImage(bufferedImage, path);
		return bufferedImage;
	}
}
