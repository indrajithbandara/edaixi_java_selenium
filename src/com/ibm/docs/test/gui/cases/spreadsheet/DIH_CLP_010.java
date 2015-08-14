package com.ibm.docs.test.gui.cases.spreadsheet;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ibm.docs.test.common.GData;
import com.ibm.docs.test.common.GuideLink;
import com.ibm.docs.test.common.Utils;
import com.ibm.docs.test.gui.cases.BaseTest;
import com.ibm.docs.test.gui.pages.FilesPage;
import com.ibm.docs.test.gui.pages.LoginPage;
import com.ibm.docs.test.gui.pages.SpreadsheetEditorPage;
import com.ibm.docs.test.gui.support.Drive;
import com.ibm.docs.test.gui.support.GLan;

/**
 * Ensure that all Unicode characters can be cut, copied, and pasted. preferred
 * by the end users.
 * 
 * @author shiyac@cn.ibm.com
 * 
 */
@GuideLink("https://labweb.torolab.ibm.com/gcoc/documents/gvtguide/testareas/DIH-CLP-010.htm")
public class DIH_CLP_010 extends BaseTest {

	@Drive
	private LoginPage loginPage;

	@Drive
	private FilesPage filesPage;

	@Drive
	private SpreadsheetEditorPage spreadsheetEditorPage;

	private static List<String> testData = new ArrayList<String>();

	@BeforeClass
	public static void beforeClass() {
		int[] ids = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 19, 20, 21, 22, 23,
				24, 29, 40, 43, 44, 45 };
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
	@GLan({ "pt-BR", "pt-PT", "bg-BG", "ca-ES", "cs-CZ", "da-DK", "nl-NL",
		"fi-FI", "fr-FR", "de-DE", "el-GR", "hu-HU", "it-IT", "ja-JP",
		"kk-KZ", "ko-KR", "nn-NO", "pl-PL", "ro-RO", "ru-RU", "zh-CN",
		"es-ES", "sv-SE", "th-TH", "zh-TW", "tr-TR", "hr-HR", "sk-SK",
		"sl-SL" })
	public void atCellCopy2Cell() {
		String fileName = Utils.uniqueBaseName("newSpreadsheetInFiles");
		spreadsheetEditorPage.focus(filesPage.newSpreadsheet(fileName));
		log.info("Verify DIH-CLP-010 at Cell Ax ");
		int i = 1;
		for (String value : testData) {
			String cell = "A" + i;
			spreadsheetEditorPage.typeInCell(cell, value);
			i++;
		}
		spreadsheetEditorPage.selectRange("A1:A" + (i - 1)).copy();
		spreadsheetEditorPage.selectRange("B1").paste();
		ArrayList<String> rangeA = getColumnText("A");
		ArrayList<String> rangeB = getColumnText("B");
		assertEquals("Unicode characters copy correctly", rangeA, rangeB);
	}

	@Test
	@GLan({ "pt-BR", "pt-PT", "bg-BG", "ca-ES", "cs-CZ", "da-DK", "nl-NL",
		"fi-FI", "fr-FR", "de-DE", "el-GR", "hu-HU", "it-IT", "ja-JP",
		"kk-KZ", "ko-KR", "nn-NO", "pl-PL", "ro-RO", "ru-RU", "zh-CN",
		"es-ES", "sv-SE", "th-TH", "zh-TW", "tr-TR", "hr-HR", "sk-SK",
		"sl-SL" })
	public void atCellCut2Cell() {
		String fileName = Utils.uniqueBaseName("newSpreadsheetInFiles");
		spreadsheetEditorPage.focus(filesPage.newSpreadsheet(fileName));
		log.info("Verify DIH-CLP-010 at Cell Ax ");
		int i = 1;
		for (String value : testData) {
			String cell = "A" + i;
			spreadsheetEditorPage.typeInCell(cell, value);
			i++;
		}
		spreadsheetEditorPage.selectRange("A1:A" + (i - 1)).cut();
		spreadsheetEditorPage.selectRange("B1").paste();
		ArrayList<String> range = getTestData();
		ArrayList<String> rangeB = getColumnText("B");
		assertEquals("Unicode characters cut correctly", range, rangeB);
	}

	public ArrayList<String> getColumnText(String columnName) {
		ArrayList<String> result = new ArrayList<String>();
		for (int i = 0; i < testData.size(); i++) {
			result.add(spreadsheetEditorPage.getCellText(columnName + (i + 1)));
		}
		return result;
	}
	
	public ArrayList<String> getTestData() {
		ArrayList<String> result = new ArrayList<String>();
		for (String value : testData) {
			result.add(value);
		}
		return result;
	}
}
