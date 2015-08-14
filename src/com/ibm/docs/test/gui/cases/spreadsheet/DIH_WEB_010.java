package com.ibm.docs.test.gui.cases.spreadsheet;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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

/**
 * Ensure that the Unicode character data that is inputted from web browser can
 * be handled properly in back end.
 * 
 * @author xinyutan@cn.ibm.com
 * 
 */
@GuideLink("https://labweb.torolab.ibm.com/gcoc/documents/gvtguide/testareas/DIH-WEB-010.htm")
public class DIH_WEB_010 extends BaseTest {

	@Drive
	private LoginPage loginPage;

	@Drive
	private FilesPage filesPage;

	@Drive
	private SpreadsheetEditorPage spreadsheetEditorPage;

	private static List<String> testData = new ArrayList<String>();

	@BeforeClass
	public static void beforeClass() {
		int[] ids = { 3, 4, 5, 6, 7, 8, 9, 11, 20, 40, 44, 45 };
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
	public void atCell() {
		String fileName = Utils.uniqueBaseName("newSpreadsheetInFiles");
		spreadsheetEditorPage.focus(filesPage.newSpreadsheet(fileName));
		log.info("Verify DIH-WEB-010 at Cell Ax");
		int i = 1;
		for (String value : testData) {
			String cell = "A" + i;
			spreadsheetEditorPage.typeInCell(cell, value);
			spreadsheetEditorPage.clickCell("B1");
			String actual = spreadsheetEditorPage.getCellText(cell);
			log.info("Test " + GUtils.getHexUnicode(value) + " " + value);
			ga.assertEquals("Test " + GUtils.getHexUnicode(value), value,
					actual);
			i++;
		}
	}

	@Test
	public void atCellComment() {
		String fileName = Utils.uniqueBaseName("newSpreadsheetInFiles");
		spreadsheetEditorPage.focus(filesPage.newSpreadsheet(fileName));
		log.info("Verify DIH-WEB-010 at Cell comments Ax");
		int i = 2;
		for (String value : testData) {
			String cell = "A" + i;
			spreadsheetEditorPage.selectRange(cell);
			log.info("Test " + GUtils.getHexUnicode(value) + " " + value);
			spreadsheetEditorPage.addComment(value);
			spreadsheetEditorPage.driver.sleep(1);
			String actual = spreadsheetEditorPage.commentsTexts.get(0)
					.getText();
			ga.assertEquals("Test " + GUtils.getHexUnicode(value), value,
					actual);
			i++;
		}
	}

	@Test
	public void atFormulaInputLine() {
		String fileName = Utils.uniqueBaseName("newSpreadsheetInFiles");
		spreadsheetEditorPage.focus(filesPage.newSpreadsheet(fileName));
		log.info("Verify DIH-WEB-010 at formulaInputLine");
		int i = 1;
		for (String value : testData) {
			String cell = "A" + i;
			spreadsheetEditorPage.selectRange(cell);
			spreadsheetEditorPage.formulaInputLine.sendKeys(value);
			spreadsheetEditorPage.clickCell("B1");
			String actual = spreadsheetEditorPage.getCellText(cell);
			log.info("Test " + GUtils.getHexUnicode(value) + " " + value);
			ga.assertEquals("Test " + GUtils.getHexUnicode(value), value,
					actual);
			i++;
		}
	}

	@Test
	@TestDoc("defect 42720 - Unicode Plane2 string be corrupted in Sheet name.")
	public void atSheetName() {
		String fileName = Utils.uniqueBaseName("newSpreadsheetInFiles");
		spreadsheetEditorPage.focus(filesPage.newSpreadsheet(fileName));
		log.info("Verify DIH-WEB-010 at Sheet1 name");
		for (String value : testData) {
			spreadsheetEditorPage.renameSheet(1, value);
			String actual = spreadsheetEditorPage.getSheetName(1);
			log.info("Test " + GUtils.getHexUnicode(value) + " " + value);
			ga.assertEquals("Test " + GUtils.getHexUnicode(value), value,
					actual);
		}
	}

	@Test
	public void atPublishVersion() {
		String fileName = Utils.uniqueBaseName("newSpreadsheetInFiles");
		String filesWH = filesPage.driver.getWindowHandle();
		String documentWH = filesPage.newSpreadsheet(fileName);
		spreadsheetEditorPage.focus(documentWH);
		log.info("Verify DIH-WEB-010 at Publish Version");
		for (String value : testData) {
			spreadsheetEditorPage.publishVersion(value);
			filesPage.focus(filesWH);
			filesPage.openFileDetail(fileName);
			filesPage.versionsBtn.click();
			String actual = filesPage.versionAlignText.getText();
			log.info("Test " + GUtils.getHexUnicode(value) + " " + value);
			ga.assertEquals("Test " + GUtils.getHexUnicode(value), value,
					actual);
			spreadsheetEditorPage.focus(documentWH);
		}
	}

	@Test
	public void atNamedRange() {
		String fileName = Utils.uniqueBaseName("newSpreadsheetInFiles");
		spreadsheetEditorPage.focus(filesPage.newSpreadsheet(fileName));
		log.info("Verify DIH-WEB-010 at Named Range Process");
		for (String value : testData) {
			if (value.equals(GUtils.getStringByHex("U+20AC")))
				continue;
			if (value.equals(GUtils.getStringByHex("U+00A5")))
				continue;
			spreadsheetEditorPage.selectRange("A1");
			spreadsheetEditorPage.newNamedRange(value);
			spreadsheetEditorPage.manageNamedRange();
			spreadsheetEditorPage.manageNamedRangeDlgNameByIndex
					.setLocatorArgument(1);
			String actual1 = spreadsheetEditorPage.manageNamedRangeDlgNameByIndex
					.getText();
			log.info("Test " + GUtils.getHexUnicode(value)
					+ " at NamedRange name filed.");
			ga.assertEquals("Test " + GUtils.getHexUnicode(value), value,
					actual1);
			spreadsheetEditorPage.manageNamedRangeDlgDelete.click();
			String actual2 = spreadsheetEditorPage.manageNamedRangeDeleteDlgMsg
					.getText();
			log.info("Test " + GUtils.getHexUnicode(value)
					+ " at NamedRange delete message.");
			boolean contains = actual2.contains(value);
			ga.assertTrue("Test " + GUtils.getHexUnicode(value), contains);
			spreadsheetEditorPage.manageNamedRangeDeleteDlgOK.click();
			spreadsheetEditorPage.driver.sleep(1);
			spreadsheetEditorPage.manageNamedRangeDlgClose.click();
			spreadsheetEditorPage.driver.sleep(1);
		}
	}
}
