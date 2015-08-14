package com.ibm.docs.test.gui.pages;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;

import com.ibm.docs.test.common.Utils;
import com.ibm.docs.test.gui.support.EnhancedWebDriver;
import com.ibm.docs.test.gui.support.EnhancedWebElement;
import com.ibm.docs.test.gui.support.EnhancedWebElements;
import com.ibm.docs.test.gui.support.Find;

public class DocumentEditorPage extends DocsEditorPage {
	private static final Logger log = Logger.getLogger(DocumentEditorPage.class);
	
	private static final String JS_SELECT_TEXT= Utils.getStringResource(DocumentEditorPage.class, "SelectText.js");
	
	@Find("css=#D_m_Insert_text")
	public EnhancedWebElement menuInsert;
	@Find("css=#D_i_Weblink_text")
	public EnhancedWebElement menuInsertLink;	
	@Find("css=#D_i_Image_text")
	public EnhancedWebElement menuInsertImage;
	@Find("css=#D_i_SpecialChar_text")
	public EnhancedWebElement menuInsertSpecialChar;
	@Find("css=#D_i_Pagebreak_text")
	public EnhancedWebElement menuInsertPageBreak;
	
	
	@Find("css=#cke_contents_editor1 iframe")
	public EnhancedWebElement editorFrame;
	

	@Find("css=#concordTemplatesDialogResults div[id*='$0']")
	public EnhancedWebElement templateDlgResult;
	
	@Find("css=#D_d_TemplateOKButton_label")
	public EnhancedWebElement templateDlgOk;
	
	@Find("css=#C_d_LinkUrlInput")
	public EnhancedWebElement linkDlgURL;
	@Find("css=#C_d_LinkProtocol input")
	public EnhancedWebElement linkDlgProtocol;
	@Find("css=#C_d_LinkLinkType input")
	public EnhancedWebElement linkDlgType;
	@Find("css=#C_d_LinkOkButton_label")
	public EnhancedWebElement linkDlgOk;

	@Find("link=$0")
	public EnhancedWebElement linkByText;
	
	@Find("css=#S_d_InsertImageInputFile")
	public EnhancedWebElement insertImageDlgFile;
	@Find("css=#S_d_InsertImageOKButton_label")
	public EnhancedWebElement insertImageDlgOk;
	
	@Find("css=img")
	public EnhancedWebElements images;
	
	@Find("css=body>p:nth-of-type($0)")
	public EnhancedWebElement paragraph;
	@Find("css=body>p")
	public EnhancedWebElements paragraphs;
	@Find("css=body>p:nth-of-type($0)>span:nth-of-type($1)")
	public EnhancedWebElement span;
	@Find("css=body>h$0")
	public EnhancedWebElements headings;
	
	//***********Team menu in document***********//	
	@Find("id=D_i_AssignSec")
	public EnhancedWebElement menuTeamAssign;	
	@Find("id=D_i_ApproveSec")
	public EnhancedWebElement menuApproveSection;	
	@Find("id=D_i_ReturnSection")
	public EnhancedWebElement menuReturnSection;	
	@Find("id=D_i_RemoveSection")
	public EnhancedWebElement menuRemoveSection;	
	@Find("id=D_i_RemoveCompleteAssign")
	public EnhancedWebElement menuRemoveCompleteAssign;	
	@Find("id=D_i_AboutSec")
	public EnhancedWebElement menuAboutSection;	
	
	//***********Format Align menu**************//
	@Find("id*=D_i_Align_text")
	public EnhancedWebElement menuAlign;
	@Find("id*=D_i_AlignLeft_text")
	public EnhancedWebElement menuAlignLeft;
	@Find("id*=D_i_AlignRight_text")
	public EnhancedWebElement menuAlignRight;
	@Find("id*=D_i_AlignCenter_text")
	public EnhancedWebElement menuAlignCenter;
	@Find("id*=D_i_AlignJustify_text")
	public EnhancedWebElement menuAlignJustify;
	
	//***********Format Heading menu**************//
	@Find("id=D_i_Heading_text")
	public EnhancedWebElement menuHeading;
	@Find("id=D_i_H$0_text")
	public EnhancedWebElement menuHeadingItem;
	
	//****** Create Table Dialog ******//
	@Find("id=C_d_InsertTableRows")
	public EnhancedWebElement createTableDialogRowField;
	@Find("id=C_d_InsertTableCols")
	public EnhancedWebElement createTableDialogColField;
	@Find("id=C_d_InsertTableOkButton")
	public EnhancedWebElement createTableDialogOkBtn;
	@Find("id=C_d_InsertTableCancelButton")
	public EnhancedWebElement createTableDialogCancelBtn;
	//****** Create Table Dialog END ******//	
	
	
	public DocumentEditorPage(EnhancedWebDriver driver) {
		super(driver);
	}

	public DocumentEditorPage selectCharacters(int start, int end) {
		log.info(String.format("Select charaters from %s to %s", start, end));
		driver.switchTo().frame(editorFrame);
		driver.javascript(JS_SELECT_TEXT, body, start, end);
		driver.switchTo().defaultContent();
		driver.sleep(2);
		return this;
	}

	public DocumentEditorPage selectAll() {
		log.info(String.format("Select all charaters"));
		this.menuEdit.click();
		this.menuSelectAll.click();
		return this;
	}
	
	public DocumentEditorPage typeInEditor(String keys) {
		log.info(String.format("Type %s in editor area", keys));
		driver.switchTo().frame(editorFrame);
		if(driver.isChrome()) driver.sleep(1);
		body.sendKeys(keys);
		driver.switchTo().defaultContent();
		driver.sleep(2);
		return this;
	}	

	public DocumentEditorPage clickInEditor(int x, int y) {
		driver.switchTo().frame(editorFrame);
		body.click(x, y);
		driver.switchTo().defaultContent();
		return this;
	}	
	
	public String getParagraphText(int n) {
		driver.switchTo().frame(editorFrame);
		String text = paragraph.setLocatorArgument(n).getText();
		driver.switchTo().defaultContent();
		return text.replace("\ufeff", "");
	}
	
	public String getHeadingText(int headingLevel, int index){
		driver.switchTo().frame(editorFrame);
		headings.setLocatorArgument(headingLevel);
		String text = headings.get(index-1).getText();
		driver.switchTo().defaultContent();
		return text.replace("\ufeff", "");
	}
	
	public String[] getHeadingsText(int[][] locator){
		String[] contents = new String[locator.length];
		driver.switchTo().frame(editorFrame);
		for(int i=0;i<locator.length;i++){
			headings.setLocatorArgument(locator[i][0]);
			contents[i] = headings.get(locator[i][1] -1).getText().replace("\ufeff", "");			
		}		
		driver.switchTo().defaultContent();
		return contents;
	}
	
	public String[] getParagraphTexts() {
		driver.switchTo().frame(editorFrame);
		String[] texts = paragraphs.getText();
		driver.switchTo().defaultContent();
		for (int i = 0; i < texts.length;i++) {
			texts[i] = texts[i].replace("\ufeff", "");
		}
 		return texts;
	}
	
	public DocumentEditorPage selectTemplate(String templateId) {
		templateDlgResult.setLocatorArgument(templateId);
		templateDlgResult.click();
		templateDlgOk.click();
		return this;
	}
	
	public DocumentEditorPage setLink(String url) {
		menuInsert.click();
		menuInsertLink.click();
		linkDlgURL.sendKeys(url);
		linkDlgOk.click();
		return this;
	}
	
	public String getLinkUrl(String text) {
		driver.switchTo().frame(editorFrame);
		linkByText.setLocatorArgument(text);
		String url = linkByText.getAttribute("href");
		driver.switchTo().defaultContent();
		return url;
	}

	public DocumentEditorPage insertImage(File image) {
		menuInsert.click();
		menuInsertImage.click();
		insertImageDlgFile.sendKeys(driver.upload(image));
		insertImageDlgOk.click();
		driver.sleep(2);
		return this;
	}
	
	public DocumentEditorPage createTable(int row, int col){
		log.info(String.format("Create %s x %s table in document", row, col));
		menuTable.click();
		menuCreateTable.click();
		createTableDialogRowField.clear();
		createTableDialogRowField.sendKeys(String.valueOf(row));
		createTableDialogColField.clear();
		createTableDialogColField.sendKeys(String.valueOf(col));
		createTableDialogOkBtn.click();		
		return this;
	}
	
	public boolean imageEquals(File expectedImage, int index) {
		driver.switchTo().frame(editorFrame);
		String src = images.get(index).getAttribute("src");
		File actualImage = driver.download(src, testEnv.getTempFile("."));
		boolean result = Utils.imageEquals(actualImage, expectedImage);
		driver.switchTo().defaultContent();
		return result;
	}	

	
	public String getParagraphCssStyle(int paragraphIndex, String style){
		log.info("get css style: " + style + " of paragraph " + paragraphIndex);
		driver.switchTo().frame(editorFrame);
		paragraph.setLocatorArgument(paragraphIndex);
		paragraph.waitPresence();
		String v = paragraph.getCssValue(style);
		driver.switchTo().defaultContent();
		return v;		
	}
	
	public String getSpanCssStyle(int paragraphIndex, int spanIndex, String style){
		log.info("get css style: " + style + " of paragraph " + paragraphIndex + " span " + spanIndex);
		driver.switchTo().frame(editorFrame);
		span.setLocatorArgument(paragraphIndex,spanIndex);
		span.waitPresence();
		String v = span.getCssValue(style);
		driver.switchTo().defaultContent();
		return v;		
	}
	
	public DocumentEditorPage setHeading(int headingLevel){
		this.menuFormat.click();
		this.menuHeading.click();
		this.menuHeadingItem.setLocatorArgument(headingLevel);
		this.menuHeadingItem.click();
		return this;
	}

	public DocumentEditorPage refresh() {
		// TODO Auto-generated method stub
		driver.switchTo().frame(editorFrame);
		body.sendKeys(Keys.F5);
		driver.sleep(8);
		driver.switchTo().defaultContent();
		return this;
	}

	@Find("css=body>fieldset:nth-of-type($0) p")
	EnhancedWebElements paragraphsInATask;
	@Find("css=body>fieldset:nth-of-type($0) legend")
	EnhancedWebElement legend;
	@Find("css=body>fieldset:nth-of-type($0)")
	EnhancedWebElement fieldset;
	public DocumentEditorPage verifyTaskCreated(String title, String assignto, int index, boolean existed) {
		// TODO Auto-generated method stub
		driver.switchTo().frame(editorFrame);
		legend.setLocatorArgument(index);
		if (!legend.isPresent()||legend.getText().contains("Creating") || legend.getText().contains("Loading")){
			driver.sleep(5);
			}
		log.debug(legend.getText()+legend.getText().contains(assignto)+ legend.getText().contains(title));
		if (legend.isPresent() && legend.getText().contains(title)&& legend.getText().contains(assignto)) {
			assertTrue(String.format("There is a task %s assigned to %s", title, assignto), existed);
		} else {
			assertFalse(String.format("No task %s assigned to %s", title, assignto), existed);
		}
			driver.switchTo().defaultContent();
			return this;
		}
	
	/**
	 * Type keys into task index
	 * @param keys
	 * @param index task index in HTML body
	 * @return
	 */
	public DocumentEditorPage typeInTask(String keys, int index) {
		log.info(String.format("Type %s in task No %s", keys, index));
		driver.switchTo().frame(editorFrame);
			paragraphsInATask.setLocatorArgument(index);
			int o = paragraphsInATask.size()-1;
			paragraphsInATask.get(o).click();
			driver.getKeyboard().sendKeys(keys);
		driver.switchTo().defaultContent();
		return this;
	}	

	/**
	 * Verify if string is displayed within a task
	 * @param keywords 
	 * @param contain true means that text is in task, or else not
	 * @param index task created index
	 */
	public DocumentEditorPage verifyTextInAssignment(String keywords, boolean contain, int index) {
		// TODO Auto-generated method stub
		driver.sleep(3);
		boolean actual = false;
		driver.switchTo().frame(editorFrame);
		paragraphsInATask.setLocatorArgument(index);
		for(int i=0; i<paragraphsInATask.size();i++){
			if(paragraphsInATask.get(i).getText().contains(keywords)){
				actual = true;
			}
		}
		assertTrue(String.format("%s is in task No.%s :%s", keywords,index,contain),actual==contain);
		driver.switchTo().defaultContent();
		return this;
	}

	public DocumentEditorPage createTask(String title, String user) {
		log.info(String.format("Create a task %s and assign to %s", title, user));
		menuTeam.click();
		menuTeamAssign.click();
		setTask(title, user, true);
		return this;
	}
}
