package com.ibm.docs.test.gui.pages;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import com.ibm.docs.test.common.Utils;
import com.ibm.docs.test.gui.support.EnhancedWebDriver;
import com.ibm.docs.test.gui.support.EnhancedWebElement;
import com.ibm.docs.test.gui.support.EnhancedWebElements;
import com.ibm.docs.test.gui.support.Find;

import static org.junit.Assert.*;

public class PresentationEditorPage extends DocsEditorPage {
	private static final Logger log = Logger
			.getLogger(PresentationEditorPage.class);
	private static final String JS_SELECT_TEXT = Utils.getStringResource(
			PresentationEditorPage.class, "SelectText.js");
	private static final String JS_COMPARE_OBJECT = Utils.getStringResource(
			PresentationEditorPage.class, "PresentationCompareObject.js");

	// **********Insert Menu*********//
	@Find("id=P_i_NewSlide_text")
	public EnhancedWebElement menuNewSlide;
	@Find("id=P_i_DupSlide_text")
	public EnhancedWebElement menuDupSlide;
	@Find("id=P_i_Img_text")
	public EnhancedWebElement menuImage;
	@Find("id=P_i_TextBox_text")
	public EnhancedWebElement menuNewTextBox;
	// ******Insert Menu End*******//

	// ******Insert Image dialog*******//
	@Find("css=#dijit_form_Form_0")
	public EnhancedWebElement insertImageForm;
	@Find("css=#P_d_ClipArt_ContentDiv_tablist_P_d_ClipArt_uploadTab")
	public EnhancedWebElement imageFromFileTab;
	@Find("css=#P_d_ClipArt_ContentDiv_tablist_P_d_ClipArt_galleryTab")
	public EnhancedWebElement imageGalleryTab;
	@Find("css=#S_d_InsertImageInputFile")
	public EnhancedWebElement inputFile;
	@Find("css=#P_d_ClipArtOKButton")
	public EnhancedWebElement insertImageOKButton;
	@Find("css=#P_d_ClipArt div.clipPickerDialogResultBox>div.clipPickerDialogItem:nth-child($0)")
	public EnhancedWebElement insertImageClipItem;
	// ******Insert Image dialog End********//

	// ****** Edit Menu ******//
	@Find("id=P_i_DeleteSlide")
	public EnhancedWebElement menuDeleteSlide;
	// ****** Edit Menu END******//

	// ********Format menu***********//
	@Find("id=P_i_SlideDesign_text")
	public EnhancedWebElement menuMasterStyles;

	// ********Team menu in presentation********//
	@Find("id=P_i_TeamAssignSlides")
	public EnhancedWebElement menuTeamAssignSlides;
	@Find("id=P_i_TeamEditSlide")
	public EnhancedWebElement menuTeamEditSlide;
	@Find("id=P_i_TeamReopenAssignedSlides")
	public EnhancedWebElement menuTeamReopenAssignedSlides;
	@Find("id=P_i_TeamReassignSlides")
	public EnhancedWebElement menuTeamReassignSlides;
	@Find("id=P_i_TeamMarkSComplete")
	public EnhancedWebElement menuTeamMarkSComplete;
	@Find("id=P_i_teamApproveSlides")
	public EnhancedWebElement menuTeamApproveSlides;
	@Find("id=P_i_TeamReturnForRework")
	public EnhancedWebElement menuTeamReturnForRework;
	@Find("id=P_i_TeamDeleteAssignment")
	public EnhancedWebElement menuTeamDeleteAssignment;
	@Find("id=P_i_teamAboutSlide")
	public EnhancedWebElement menuTeamAboutSlide;
	@Find("id=P_i_TeamRemoveCompletedSlides")
	public EnhancedWebElement menuTeamRemoveCompletedSlides;

	// ****** Table Menu ******//
	@Find("id=P_m_Table_text")
	public EnhancedWebElement menuTable;
	@Find("id=P_i_Create_text")
	public EnhancedWebElement menuTableCreate;
	@Find("id=P_i_DeleteTable_text")
	public EnhancedWebElement menuTableDelete;
	@Find("id=P_i_TableRow_text")
	public EnhancedWebElement menuTableRow;
	@Find("id=P_i_TableCol_text")
	public EnhancedWebElement menuTableCol;
	@Find("id=P_i_TableCell_text")
	public EnhancedWebElement menuTableCell;
	@Find("id=P_i_TableCellProperties_text")
	public EnhancedWebElement menuTableCellProperties;
	// ****** Table Menu END ******//

	// ****** Create Table Dialog ******//
	@Find("id=requiredRowField")
	public EnhancedWebElement createTableDialogRowField;
	@Find("id=requiredColField")
	public EnhancedWebElement createTableDialogColField;
	@Find("id=createTblOkBtn")
	public EnhancedWebElement createTableDialogOkBtn;
	@Find("id=createTblCancelBtn")
	public EnhancedWebElement createTableDialogCancelBtn;
	// ****** Create Table Dialog END ******//

	// ****Presentation menu***************//
	@Find("id=P_m_Presentation")
	public EnhancedWebElement menuPresentation;
	@Find("id=P_i_SlideShow")
	public EnhancedWebElement menuPlaySlideShow;
	@Find("id=P_i_SlideShowWithCoview_")
	public EnhancedWebElement menuPlaySharedSlideShow;
	@Find("id=P_i_SlideShowFromCurrent")
	public EnhancedWebElement menuPlayFromCurrentSlide;
	@Find("id=P_i_SlideTransitions")
	public EnhancedWebElement menuApplySlideTransition;
	// ****Presentation menu End************//

	@Find("css=#cke_contents_editor1 iframe")
	public EnhancedWebElement sidebarFrame;
	@Find("css=.slideWrapper")
	public EnhancedWebElements sidebarSlides;
	@Find("css=.slideWrapper:nth-child($0)")
	public EnhancedWebElement sidebarSlide;
	@Find("css=div.slideEditor")
	public EnhancedWebElement slideEditor;
	@Find("css=div.slideEditor>div.draw_frame:not(.isSpare)[draw_name='$0']")
	public EnhancedWebElement drawFrameByName;
	@Find("css=div.slideEditor>div.draw_frame:not(.isSpare)[presentation_class='$0']")
	public EnhancedWebElements drawFramesByClass;
	@Find("css=div.slideEditor>div.draw_frame:not(.isSpare)")
	public EnhancedWebElements drawFrames;

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * locate a draw frame by tab index, it is applicable for all draw frames in
	 * slide which can be reached via pressing Tab key, such as TextBox,
	 * Placeholder, Table, Image. SpeakerNotes, footer, date-time and
	 * page-number can't be located by this locater.
	 */
	@Find("script=return dojo.query('div.slideEditor>div.draw_frame:not(.isSpare):[style*=z-index]:not([presentation_class=\"notes\"])').sort(function(a,b){return a.style.zIndex-b.style.zIndex;})[$0-1];")
	public EnhancedWebElement drawFrameByTabIndex;

	@Find("css=div.slideEditor>div[class*='draw_frame'][class*='resizableContainer']:not(.isSpare)[presentation_class='$0']")
	public EnhancedWebElement drawFrameByClass;

	@Find("css=div.slideEditor>div[class*='draw_frame'][class*='resizableContainer']:not(.isSpare)[presentation_class='$0']>div.draw_text-box p:nth-child($1)")
	public EnhancedWebElement paragraphDrawFrameByClass;
	@Find("css=div.slideEditor>div[class*='draw_frame'][class*='resizableContainer']:not(.isSpare)[presentation_class='$0']>div.draw_text-box p:nth-child($1) span:nth-child($2)")
	public EnhancedWebElement spanDrawFrameByClass;

	@Find("css=div.slideEditor>div[class*='draw_frame'][class*='resizableContainer']:not(.isSpare)[presentation_class=\"notes\"]>div.draw_text-box")
	public EnhancedWebElement speakerNotes;
	@Find("css=div.slideEditor>div[class*='draw_frame'][class*='resizableContainer']:not(.isSpare)[presentation_class=\"notes\"]")
	public EnhancedWebElement speakerNotesDrawFrame;

	@Find("css=div.slideEditor>div.draw_frame:not(.isSpare)[id='$0']>div.draw_text-box")
	public EnhancedWebElement textBoxByDrawFrameID;

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Find("css=div.slideEditor>div.draw_frame:not(.isSpare) img")
	public EnhancedWebElements images;

	// imported table from pptx can be located by draw_name, new created table
	// has no draw_name before publish.
	@Find("css=div.slideEditor>div.draw_frame:not(.isSpare)[id='$0']>table.table_table")
	public EnhancedWebElement tableByDrawFrameID;
	@Find("css=div.slideEditor>div.draw_frame:not(.isSpare)[id='$0']>table.table_table>tbody>tr:nth-child($1)>td:nth-child($2)")
	public EnhancedWebElement tableCellByDrawFrameID;
	@Find("css=div.slideEditor>div.draw_frame:not(.isSpare)[id='$0']>table.table_table>tbody>tr:nth-child($1)>th:nth-child($2)")
	public EnhancedWebElement tableHeadByDrawFrameID;
	@Find("css=div.slideEditor>div.draw_frame:not(.isSpare)[id='$0']>table.table_table>tbody>tr:nth-child($1)>td:nth-child($2)>p:nth-child($3)")
	public EnhancedWebElement tableCellParagraphByDrawFrameID;
	@Find("css=div.slideEditor>div.draw_frame:not(.isSpare)[id='$0']>table.table_table>tbody>tr:nth-child($1)>th:nth-child($2)>p:nth-child($3)")
	public EnhancedWebElement tableHeadParagraphByDrawFrameID;
	@Find("css=div.slideEditor>div.draw_frame:not(.isSpare)[id='$0']>table.table_table>tbody>tr:nth-child($1)>td:nth-child($2)>p:nth-child($3)>span:nth-child($4)")
	public EnhancedWebElement tableCellSpanByDrawFrameID;
	@Find("css=div.slideEditor>div.draw_frame:not(.isSpare)[id='$0']>table.table_table>tbody>tr:nth-child($1)>th:nth-child($2)>p:nth-child($3)>span:nth-child($4)")
	public EnhancedWebElement tableHeadSpanByDrawFrameID;

	// number bullet
	@Find("css=div.slideEditor>div.draw_frame:not(.isSpare)[draw_name='$0']>div>div>div li")
	public EnhancedWebElements numberBulletsByDrawFrameName;

	// new inserted table can be located by presentation_class
	@Find("css=div.slideEditor>div.draw_frame:not(.isSpare)>table.table_table")
	public EnhancedWebElements tables;

	@Find("css=div.slideEditor table.cke_editor iframe")
	public EnhancedWebElement textboxFrame;

	@Find("css=#$0 table.cke_editor iframe")
	public EnhancedWebElement iframeByDrawFrameID;

	// **********Slide Show page******************//
	@Find("css=#slideShowContainer")
	public EnhancedWebElement shadowPiece;

	// **********Slide Show page End******************//

	public PresentationEditorPage(EnhancedWebDriver driver) {
		super(driver);
	}

	public PresentationEditorPage selectSlide(int index) {
		log.info("Select slide " + index);
		driver.switchTo().frame(sidebarFrame);
		sidebarSlide.setLocatorArgument(index);
		sidebarSlide.getCoordinates().inViewPort();
		sidebarSlide.click();
		driver.switchTo().defaultContent();
		return this;
	}

	public PresentationEditorPage newSlide() {
		log.info("Select menu Insert->New Slide ");
		menuInsert.click();
		menuNewSlide.click();
		return this;
	}

	public PresentationEditorPage duplicateSlide(int slideNo) {
		selectSlide(slideNo);
		log.info("Select menu Insert->Duplicate Slide ");
		menuInsert.click();
		menuDupSlide.click();
		return this;
	}

	/**
	 * insert given image in current slide.
	 * 
	 * @param image
	 *            full path
	 * @return
	 */
	public PresentationEditorPage insertImageFromGallery(int index) {
		menuInsert.click();
		menuImage.click();
		imageGalleryTab.click();
		insertImageClipItem.setLocatorArgument(index);
		insertImageClipItem.click();
		insertImageOKButton.click();
		return this;
	}

	public PresentationEditorPage insertImageFromFile(File image) {
		menuInsert.click();
		menuImage.click();
		imageFromFileTab.click();
		inputFile.sendKeys(driver.upload(image));
		insertImageOKButton.click();
		return this;
	}

	public PresentationEditorPage deleteSlide(int slideNo) {
		selectSlide(slideNo);
		log.info("Select menu Edit->Delete Slide ");
		menuEdit.click();
		menuDeleteSlide.click();
		return this;
	}

	public PresentationEditorPage createTable(int row, int col) {
		log.info(String.format("Create %s x %s table", row, col));
		menuTable.click();
		menuTableCreate.click();
		createTableDialogRowField.clear();
		createTableDialogRowField.sendKeys(String.valueOf(row));
		createTableDialogColField.clear();
		createTableDialogColField.sendKeys(String.valueOf(col));
		createTableDialogOkBtn.click();
		return this;
	}

	public PresentationEditorPage newTextBox() {
		log.info("Select menu Insert->Text Box");
		menuInsert.click();
		menuNewTextBox.click();
		return this;
	}

	public PresentationEditorPage selectCharactersInTextBox(int start, int end) {
		driver.switchTo().frame(textboxFrame);
		// Weird. In chrome we have to do it twice to ensure the action is
		// performed successfully
		driver.javascript(JS_SELECT_TEXT, body, start, end);
		driver.javascript(JS_SELECT_TEXT, body, start, end);
		driver.switchTo().defaultContent();
		return this;
	}

	public String getSelectedCharacterInTextBox() {
		driver.switchTo().frame(textboxFrame);
		String result = driver.getSelection();
		driver.switchTo().defaultContent();
		return result;
	}

	public PresentationEditorPage typeInIframe(String content) {
		log.info(String.format("Type '%s' into text box", content));
		driver.switchTo().frame(textboxFrame);
		body.sendKeys(content);
		driver.switchTo().defaultContent();
		return this;
	}

	public PresentationEditorPage typeInIframe(String drawFrameID,
			String content) {
		log.info(String.format("Type '%s' into drawFrame", content));
		iframeByDrawFrameID.setLocatorArgument(drawFrameID);
		iframeByDrawFrameID.waitPresence();
		driver.switchTo().frame(iframeByDrawFrameID);
		body.sendKeys(content);
		driver.switchTo().defaultContent();
		return this;
	}

	public PresentationEditorPage typeInSpeakerNotes(String drawFrameID,
			String content) {
		log.info(String.format("Type '%s' into speaker notes", content));
		iframeByDrawFrameID.setLocatorArgument(drawFrameID);
		iframeByDrawFrameID.waitPresence();
		driver.switchTo().frame(iframeByDrawFrameID);
		// driver.getMouse().click(body.getCoordinates());
		body.sendKeys(content);
		driver.switchTo().defaultContent();
		return this;
	}

	public PresentationEditorPage typeInTable(String drawFrameID,
			String content, int row, int col) {
		log.info(String.format("Type '%s' into table", content));
		iframeByDrawFrameID.setLocatorArgument(drawFrameID);
		iframeByDrawFrameID.waitPresence();
		driver.switchTo().frame(iframeByDrawFrameID);
		body.sendKeys(content);
		driver.switchTo().defaultContent();
		return this;
	}

	public String selectSpeakerNotes() {
		// Make sure nothing is selected firstly
		slideEditor.click(1, 1);
		String id = speakerNotesDrawFrame.getAttribute("id");
		// Workaround: Can not directly use webdriver click to select draw
		// frame. We use JS to simulate it.
		speakerNotes.doubleClick();
		return id;
	}

	public PresentationEditorPage selectDrawFrame(String className, int index) {
		// Make sure nothing is selected firstly
		slideEditor.click(1, 1);
		drawFramesByClass.setLocatorArgument(className);
		// Workaround: Can not directly use webdriver click to select draw
		// frame. We use JS to simulate it.
		drawFramesByClass.get(index).doubleClick();
		return this;
	}
	
	public PresentationEditorPage clickDrawFrame(String className, int index) {
		// Make sure nothing is selected firstly
		slideEditor.click(1, 1);
		drawFramesByClass.setLocatorArgument(className);
		// Workaround: Can not directly use webdriver click to select draw
		// frame. We use JS to simulate it.
		drawFramesByClass.get(index).click(1,1);
		return this;
	}

	public PresentationEditorPage selectDrawFrame(String name) {
		// Make sure nothing is selected firstly
		slideEditor.click(1, 1);
		drawFrameByName.setLocatorArgument(name);
		// Workaround: Can not directly use webdriver click to select draw
		// frame. We use JS to simulate it.
		// driver.javascript(JS_SELECT_FRAME, drawFrameByName);
		drawFrameByName.doubleClick();
		return this;
	}

	public String selectDrawFrameByTabIndex(int tabIndex) {
		// Make sure nothing is selected firstly
		slideEditor.click(1, 1);
		drawFrameByTabIndex.setLocatorArgument(tabIndex);
		String id = drawFrameByTabIndex.getAttribute("id");
		// Workaround: Can not directly use webdriver click to select draw
		// frame. We use JS to simulate it.
		// driver.javascript(JS_SELECT_FRAME, drawFrameByTabIndex);
		drawFrameByTabIndex.doubleClick();
		return id;
	}

	public PresentationEditorPage selectDrawFrameByClass(
			String presentationClass) {
		// Make sure nothing is selected firstly
		slideEditor.click(1, 1);
		drawFrameByClass.setLocatorArgument(presentationClass);
		// Workaround: Can not directly use webdriver click to select draw
		// frame. We use JS to simulate it.
		// driver.javascript(JS_SELECT_FRAME, drawFrameByClass);
		drawFrameByClass.doubleClick();
		return this;
	}

	public PresentationEditorPage selectTableCell(String drawFrameName,
			int row, int col) {
		// Make sure nothing is selected firstly
		slideEditor.click(1, 1);
		if (row == 1) {
			tableHeadByDrawFrameID.setLocatorArgument(drawFrameName, row, col);
			tableHeadByDrawFrameID.doubleClick();
			/*
			 * tableHeadByDrawFrameName.waitPresence();
			 * driver.javascript(JS_SELECT_FRAME, tableHeadByDrawFrameName);
			 */
		} else {
			tableCellByDrawFrameID.setLocatorArgument(drawFrameName, row, col);
			tableCellByDrawFrameID.doubleClick();
			/*
			 * tableCellByDrawFrameName.waitPresence();
			 * driver.javascript(JS_SELECT_FRAME, tableCellByDrawFrameName);
			 */
		}

		return this;
	}

	public EnhancedWebElement getTableCell(String drawFrameID, int row, int col) {
		EnhancedWebElement cell = tableCellByDrawFrameID.setLocatorArgument(
				drawFrameID, row, col);
		if (!cell.isPresent()) {
			cell = tableHeadByDrawFrameID.setLocatorArgument(drawFrameID, row,
					col);
			cell.waitPresence();
		}
		return cell;
	}

	public EnhancedWebElement getTableParagraph(String drawFrameID, int row,
			int col, int paragraphIndex) {
		EnhancedWebElement para = tableCellParagraphByDrawFrameID
				.setLocatorArgument(drawFrameID, row, col, paragraphIndex);
		if (!para.isPresent()) {
			para = tableHeadParagraphByDrawFrameID.setLocatorArgument(
					drawFrameID, row, col, paragraphIndex);
			para.waitPresence();
		}
		return para;
	}

	public EnhancedWebElement getTableSpan(String drawFrameID, int row,
			int col, int paragraphIndex, int spanIndex) {
		EnhancedWebElement span = tableCellSpanByDrawFrameID
				.setLocatorArgument(drawFrameID, row, col, paragraphIndex,
						spanIndex);
		if (!span.isPresent()) {
			span = tableHeadSpanByDrawFrameID.setLocatorArgument(drawFrameID,
					row, col, paragraphIndex, spanIndex);
			span.waitPresence();
		}
		return span;
	}

	public String getTextFromDrawFrameName(String name) {
		drawFrameByName.setLocatorArgument(name);
		return drawFrameByName.getText();
	}

	public String getTextFromDrawFrameClass(String className, int index) {
		drawFramesByClass.setLocatorArgument(className);
		String text = drawFramesByClass.get(index).getText();
		return text.replace("\ufeff", "");
	}

	/**
	 * get text content from draw frame's presentation_class. This is applied
	 * for spreakerNotes and placeholder.
	 * 
	 * @param className
	 * @return
	 */
	public String getTextFromDrawFrameClass(String className) {
		drawFrameByClass.setLocatorArgument(className);
		String text = drawFrameByClass.getText();
		return text.replace("\ufeff", "");
	}

	/**
	 * get box WebElement from draw frame via draw frame name.
	 * 
	 * @param name
	 * @return
	 */
	public EnhancedWebElement getBoxFromDrawFrame(EnhancedWebElement drawFrame) {
		drawFrame.waitPresence();
		log.info("get box WebElement from draw frame, draw frame id: "
				+ drawFrame.getAttribute("id") + ".");
		WebElement ele = drawFrame.findElement(By.xpath("./div/div/div"));
		return new EnhancedWebElement(driver, ele);
	}

	/*	*//**
	 * this method is used in old number bullet design. get number bullet
	 * information.
	 * 
	 * @param drawFrameName
	 * @param levels
	 *            , such as "2,3", means first level: the second one, second
	 *            level: the third one
	 * @return
	 */
	/*
	 * public String[] getNumberBulletByDrawFrameName(String drawFrameName,
	 * int[] levels){ log.info("get number bullet from draw frame: " +
	 * drawFrameName + ", number bullet levels: " + levels.toString()); String[]
	 * result = new String[]{"",""};
	 * this.numberBulletByDrawFrameName.setLocatorArgument(drawFrameName);
	 * numberBulletByDrawFrameName.waitPresence();
	 * 
	 * //get parent WebElement parent = numberBulletByDrawFrameName; for(int
	 * i=1;i<levels.length;i++){ List<WebElement> list =
	 * parent.findElements(By.xpath("./li")); parent = list.get(levels[i-1]-1);
	 * List<WebElement> parentList = parent.findElements(By.xpath("./ul"));
	 * if(parentList.size() == 0){ parentList =
	 * parent.findElements(By.xpath("./ol")); } parent = parentList.get(0); }
	 * 
	 * List<WebElement> list = parent.findElements(By.xpath("./li"));
	 * EnhancedWebElement li = new
	 * EnhancedWebElement(driver,list.get(levels[levels.length-1]-1)); result[0]
	 * = li.findElement(By.xpath("./span/span")).getText(); result[1] =
	 * li.getListStyle(); return result; }
	 */

	/**
	 * get number bullet information(level, content, number/bullet type).
	 * 
	 * @param drawFrameName
	 * @param index
	 * @return
	 */
	public String[] getNumberBulletByDrawFrameName(String drawFrameName,
			int index) {
		log.info("get number bullet from draw frame: " + drawFrameName
				+ ", index: " + index);
		String[] result = new String[] { "", "", "" };
		this.numberBulletsByDrawFrameName.setLocatorArgument(drawFrameName);
		numberBulletsByDrawFrameName.waitPresence();
		EnhancedWebElement ele = numberBulletsByDrawFrameName.get(index - 1);
		result[0] = ele.getAttribute("level");
		result[1] = ele.findElement(By.xpath("./span")).getText();
		if (!ele.getAttribute("values").isEmpty()) {
			result[2] = ele.getAttribute("values");
			System.out.println(ele.getAttribute("id") + "--"
					+ ele.getAttribute("level") + "--"
					+ ele.getAttribute("values"));
		} else {
			result[2] = ele.getListStyle();
			System.out.println(ele.getAttribute("id") + "--"
					+ ele.getAttribute("level") + "--" + ele.getListStyle());
		}

		return result;
	}

	public boolean imageEquals(File expectedImage, int index) {
		String src = images.get(index).getAttribute("src");
		File actualImage = driver.download(src, testEnv.getTempFile("."));
		boolean result = Utils.imageEquals(actualImage, expectedImage);
		return result;
	}

	/**
	 * play slide show from editor page.
	 * 
	 * @return window handle
	 */
	public String playSlideShow() {
		log.info("play slide show from editor page.");
		driver.storeWindows();
		menuPresentation.click();
		menuPlaySlideShow.click();
		return driver.waitNewWindow();
	}

	public int getSlideNumber() {
		driver.switchTo().frame(sidebarFrame);
		int number = this.sidebarSlides.size();
		driver.switchTo().defaultContent();
		return number;
	}

	public String compareObject(String expectedDomXml,
			EnhancedWebElement actualElement) {
		String msg = "";
		msg = driver.javascript(JS_COMPARE_OBJECT, expectedDomXml,
				actualElement).toString();
		return msg;
	}

	public void removeCompletedAssignments() {
		// TODO Auto-generated method stub
		log.info("Remove completed assignments by menu");
		menuTeam.click();
		menuTeamRemoveCompletedSlides.click();
	}

	public void markAssignCompleteByActions() {
		// TODO Auto-generated method stub
		log.info("Mark an assignment as complete by Actions button in sidebar");
		buttonActions.click();
		driver.sleep(500, TimeUnit.MILLISECONDS);
		menuActionsMarkSlideComplete.click();
	}

	@Find("css=div.taskContainer div.taskEntryTxt")
	EnhancedWebElement taskText;

	/**
	 * Verify if a task is existed in current file
	 * 
	 * @param username
	 * @param existed
	 *            true means that verify if there is a task assigned to username
	 */
	public void verifyTaskCreated(String username, boolean existed) {
		// TODO Auto-generated method stub
		driver.sleep(5);
		driver.switchTo().frame(sidebarFrame);
		if (taskText.isPresent() && taskText.getText().contains(username)) {
			assertTrue(
					String.format("There is a task assigned to %s", username),
					existed);
		} else {
			assertFalse(String.format("No task assigned to %s", username),
					existed);
		}
		driver.switchTo().defaultContent();
	}

	public BufferedImage screenshotCurrentSlide() {
		return screenshot(slideEditor, null);
	}

	public PresentationEditorPage switchToFrame() {
		driver.switchTo().frame(textboxFrame);
		return this;
	}

	public PresentationEditorPage copyInIframe() {
		driver.switchTo().frame(textboxFrame);

		if (driver.isInternetExplorer()) {
			menuEdit.click();
			menuPaste.click();
		} else {
			String keys = Keys.chord(Keys.CONTROL, "v");
			driver.getKeyboard().sendKeys(keys);
		}
		driver.switchTo().defaultContent();
		return this;
	}

	public PresentationEditorPage switchToDefault() {
		driver.switchTo().defaultContent();
		return this;
	}

	@Find("css=div[presentation_class='table'] iframe")
	public EnhancedWebElements tableFrames;
	@Find("xpath=/html/body/table/tbody/tr[$0]/td[$1]/p")
	public EnhancedWebElement tableFrameCell;

	public PresentationEditorPage typeInTable(String content, int index,
			int row, int col) {
		log.info(String.format("Type '%s' into table", content));
		driver.switchTo().frame(tableFrames.get(index));
		tableFrameCell.setLocatorArgument(row, col);
		tableFrameCell.click();
		body.sendKeys(content);
		driver.switchTo().defaultContent();
		slideEditor.click(1, 1);
		return this;
	}

	public String getCellText(int index, int row, int col) {
		slideEditor.click(1, 1);
		String xpath = "//table/tbody/tr[" + row + "]/td[" + col + "]/p";
		WebElement tableCell = drawFramesByClass.setLocatorArgument("table")
				.get(index).findElement(By.xpath(xpath));
		return tableCell.getText();
	}
}
