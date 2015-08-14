package com.ibm.docs.test.gui.cases.spreadsheet;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.Keys;

import com.ibm.docs.test.common.GData;
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
 * Ensure that Unicode data when compared, does so using Unicode Normal Form C (NFC)
 * by the end users.
 * 
 * @author shiyac@cn.ibm.com
 * 
 */
@GuideLink("https://labweb.torolab.ibm.com/gcoc/documents/gvtguide/testareas/DCP-NFC-010.htm")
public class DCP_NFC_010 extends BaseTest {

	@Drive
	private LoginPage loginPage;

	@Drive
	private FilesPage filesPage;

	@Drive
	private SpreadsheetEditorPage spreadsheetEditorPage;

	private static List<String> testData = new ArrayList<String>();

	@BeforeClass
	public static void beforeClass() {
		int[] ids = { 408, 409, 410, 411 };
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
	@TestDoc("Defect 7173 - [GVT][Spreadsheet][pt-BR]Can't search out special characters like 'résumé', دِّ, which has multiple unicode point")
	@GLan({ "pt-BR", "pt-PT", "bg-BG", "ca-ES", "cs-CZ", "da-DK", "nl-NL",
			"fi-FI", "fr-FR", "de-DE", "el-GR", "hu-HU", "it-IT", "ja-JP",
			"kk-KZ", "ko-KR", "nn-NO", "pl-PL", "ro-RO", "ru-RU", "zh-CN",
			"es-ES", "sv-SE", "th-TH", "zh-TW", "tr-TR", "hr-HR", "sk-SK",
			"sl-SL" })
	public void atFindandReplaceDialog() {
		String fileName = Utils.uniqueBaseName("newSpreadsheetInFiles");
		spreadsheetEditorPage.focus(filesPage.newSpreadsheet(fileName));
		log.info("Verify DCP-NFC-010 at Cell A1 ");
		ga.assertTrue("Check Find and Replace function: ",
				openFindReplaceDialog(testData.get(0), testData.get(1)));
		ga.assertTrue("Check Find and Replace function: ",
				openFindReplaceDialog(testData.get(2), testData.get(3)));
	}

	@Test
	@Ignore
	@TestDoc("Defect 43162 - [GVT][comments]Can't search out special characters like 'résumé', دِّ, which has multiple unicode point.")
	@GLan({ "pt-BR", "pt-PT", "bg-BG", "ca-ES", "cs-CZ", "da-DK", "nl-NL",
			"fi-FI", "fr-FR", "de-DE", "el-GR", "hu-HU", "it-IT", "ja-JP",
			"kk-KZ", "ko-KR", "nn-NO", "pl-PL", "ro-RO", "ru-RU", "zh-CN",
			"es-ES", "sv-SE", "th-TH", "zh-TW", "tr-TR", "hr-HR", "sk-SK",
			"sl-SL" })
	public void atCellComment() {
		String fileName = Utils.uniqueBaseName("newSpreadsheetInFiles");
		spreadsheetEditorPage.focus(filesPage.newSpreadsheet(fileName));
		log.info("Verify DCP-NFC-010 at Comment");
		spreadsheetEditorPage.addComment(testData.get(0));
		//addComment_custom(testData.get(0));
		spreadsheetEditorPage.clickCell("B1");
		spreadsheetEditorPage.addComment(testData.get(2));
		addCommentBySearch();
		ga.assertTrue("Check Search function on Comments: ",
				searchOnComments(testData.get(1)));
		ga.assertTrue("Check Search function on Comments: ",
				searchOnComments(testData.get(3)));
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

	public void addCommentBySearch() {
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

	public void addComment_custom(String message) {
		if (!spreadsheetEditorPage.commentText.isPresent()
				|| !spreadsheetEditorPage.commentText.isDisplayed())
			spreadsheetEditorPage.commentBtn.click();
		spreadsheetEditorPage.commentText.sendKeys(message);
		spreadsheetEditorPage.commentAddBtn.click();
	}
}
