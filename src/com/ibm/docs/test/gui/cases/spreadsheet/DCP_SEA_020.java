package com.ibm.docs.test.gui.cases.spreadsheet;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.Keys;

import com.ibm.docs.test.common.GData;
import com.ibm.docs.test.common.GUtils;
import com.ibm.docs.test.common.GuideLink;
import com.ibm.docs.test.common.TestDoc;
import com.ibm.docs.test.common.Utils;
import com.ibm.docs.test.gui.cases.BaseTest;
import com.ibm.docs.test.gui.pages.FilesPage;
import com.ibm.docs.test.gui.pages.LoginPage;
import com.ibm.docs.test.gui.pages.SpreadsheetEditorPage;
import com.ibm.docs.test.gui.support.Drive;
import com.ibm.docs.test.gui.support.GLan;

/**
 * Verify that text searching takes account of language sensitivity. preferred
 * by the end users.
 * 
 * @author shiyac@cn.ibm.com
 * 
 */
@GuideLink("https://labweb.torolab.ibm.com/gcoc/documents/gvtguide/testareas/DCP-SEA-020.htm")
public class DCP_SEA_020 extends BaseTest {

	@Drive
	private LoginPage loginPage;

	@Drive
	private FilesPage filesPage;

	@Drive
	private SpreadsheetEditorPage spreadsheetEditorPage;

	private static List<String> testData = new ArrayList<String>();

	@BeforeClass
	public static void beforeClass() {
		int[] ids = { 526, 527, 528, 529, 530, 531, 532, 533, 534, 535, 536,
				537, 538, 539 };
		GData gData = GData.getInstance();
		for (int id : ids) {
			testData.add(gData.getDataById(id));
		}
	}

	@Before
	public void before() {
		loginPage.login();
		filesPage.go();
	}

	@Test
	@TestDoc("Defect 42948 - [GVT][Spreadsheet][Turkish] Incorrect result of search.")
	@GLan({ "de-DE", "ja-JP", "ko-KR", "zh-CN", "zh-TW", "tr-TR" })
	public void atFindandReplaceDialog() {
		String fileName = Utils.uniqueBaseName("newSpreadsheetInFiles");
		spreadsheetEditorPage.focus(filesPage.newSpreadsheet(fileName));
		log.info("Verify DCP-SEA-020 at Cell Ax");
		String localeID = GUtils
				.getBrowserPreferenceLan(spreadsheetEditorPage.driver);
		if (localeID.equals("de-DE")) {
			assertTrue("Check Find and Replace function: ",
					openFindReplaceDialog(testData.get(2), testData.get(3)));
		} else if (localeID.equals("tr-TR")) {
			assertTrue("Check Find and Replace function: ",
					openFindReplaceDialog(testData.get(6), testData.get(7)));
		} else {
			boolean alphabet = openFindReplaceDialog(testData.get(8),
					testData.get(9));
			boolean mix = openFindReplaceDialog(testData.get(8),
					testData.get(10));
			boolean ideographic2 = openFindReplaceDialog(testData.get(8),
					testData.get(11));
			boolean ideographic = openFindReplaceDialog(testData.get(8),
					testData.get(12));
			boolean full = openFindReplaceDialog(testData.get(8),
					testData.get(13));
			assertTrue("Check Find and Replace function: ", alphabet && mix
					&& ideographic2 && ideographic && full);
		}
	}

	@Test
	@Ignore
	@TestDoc("Defect 42975 - [GVT][Comment][Turkish] Incorrect result of search.")
	@GLan({ "de-DE", "ja-JP", "ko-KR", "zh-CN", "zh-TW", "tr-TR" })
	public void atCellComment() {
		String fileName = Utils.uniqueBaseName("newSpreadsheetInFiles");
		spreadsheetEditorPage.focus(filesPage.newSpreadsheet(fileName));
		log.info("Verify DCP-SEA-020 at Comment");
		String localeID = GUtils
				.getBrowserPreferenceLan(spreadsheetEditorPage.driver);
		if (localeID.equals("de-DE")) {
			addCommentBySearch(testData.get(2));
			assertTrue("Check Search function on Comments: ",
					searchOnComments(testData.get(3)));
		} else if (localeID.equals("tr-TR")) {
			addCommentBySearch(testData.get(6));
			assertTrue("Check Search function on Comments: ",
					searchOnComments(testData.get(7)));
		} else {
			addCommentBySearch(testData.get(8));
			boolean alphabet = searchOnComments(testData.get(9));
			boolean mix = searchOnComments(testData.get(10));
			boolean ideographic2 = searchOnComments(testData.get(11));
			boolean ideographic = searchOnComments(testData.get(12));
			boolean full = searchOnComments(testData.get(13));
			assertTrue("Check Search function on Comments: ", alphabet && mix
					&& ideographic2 && ideographic && full);
		}
	}
	
	@Test
	public void onlyTest() {
		String fileName = Utils.uniqueBaseName("newSpreadsheetInFiles");
		spreadsheetEditorPage.focus(filesPage.newSpreadsheet(fileName));
		spreadsheetEditorPage.addComment("test");
	}

	public boolean openFindReplaceDialog(String target, String search) {
		String testText = "test";
		spreadsheetEditorPage.deleteCell("B1");
		spreadsheetEditorPage.typeInCell("B1", target);
		spreadsheetEditorPage.clickCell("A1");
		spreadsheetEditorPage.menuEdit.click();
		spreadsheetEditorPage.menuFindReplace.click();
		spreadsheetEditorPage.findTextField.clear();
		spreadsheetEditorPage.findTextField.sendKeys(search);
		spreadsheetEditorPage.replaceTextField.clear();
		spreadsheetEditorPage.replaceTextField.sendKeys(testText);
		spreadsheetEditorPage.findButton.click();
		spreadsheetEditorPage.replaceButton.click();
		spreadsheetEditorPage.findOKButton.click();
		if (spreadsheetEditorPage.getCellText("B1").indexOf(testText) != -1) {
			return true;
		}
		return false;
	}

	public void addCommentBySearch(String target) {
		spreadsheetEditorPage.addComment(target);
		spreadsheetEditorPage.commentMenuList.click();
		spreadsheetEditorPage.driver.getKeyboard().sendKeys(
				Keys.chord(Keys.ENTER));
		// spreadsheetEditorPage.commentSearch.click();
	}

	public boolean searchOnComments(String search) {
		spreadsheetEditorPage.commentSearchText.clear();
		spreadsheetEditorPage.commentSearchText.sendKeys(search);
		spreadsheetEditorPage.commentSearchIcon.click();
		if (spreadsheetEditorPage.hightlightComment.isPresent()) {
			return true;
		}
		return false;
	}
}
