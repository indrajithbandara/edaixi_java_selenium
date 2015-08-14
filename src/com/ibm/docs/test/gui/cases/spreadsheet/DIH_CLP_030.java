package com.ibm.docs.test.gui.cases.spreadsheet;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ibm.docs.test.common.GData;
import com.ibm.docs.test.common.GUtils;
import com.ibm.docs.test.common.GuideLink;
import com.ibm.docs.test.common.Utils;
import com.ibm.docs.test.gui.cases.BaseTest;
import com.ibm.docs.test.gui.pages.FilesPage;
import com.ibm.docs.test.gui.pages.LoginPage;
import com.ibm.docs.test.gui.pages.SpreadsheetEditorPage;
import com.ibm.docs.test.gui.support.Drive;
import com.ibm.docs.test.gui.support.GLan;

/**
 * Ensure that there is no loss of control codes and other formatting characters
 * during cut and paste. preferred by the end users.
 * 
 * @author shiyac@cn.ibm.com
 * 
 */
@GuideLink("https://labweb.torolab.ibm.com/gcoc/documents/gvtguide/testareas/DIH-CLP-030.htm")
public class DIH_CLP_030 extends BaseTest {

	@Drive
	private LoginPage loginPage;

	@Drive
	private FilesPage filesPage;

	@Drive
	private SpreadsheetEditorPage spreadsheetEditorPage;

	private static List<String> testData = new ArrayList<String>();

	@BeforeClass
	public static void beforeClass() {
		int[] ids = { 1216 };
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
		log.info("Verify DIH-CLP-030 at Cell A1 ");
		int i = 1;
		for (String value : testData) {
			String cell = "A" + i;
			spreadsheetEditorPage.typeInCell(cell, value);
			i++;
		}
		spreadsheetEditorPage.selectRange("A1").copy();
		spreadsheetEditorPage.selectRange("B1").paste();
		String cellText = spreadsheetEditorPage.getCellText("B1");
		String actualHex = GUtils.getHexUnicode(cellText);
		String expectedHex = GUtils.getHexUnicode(testData.get(0));
		// UTF16.StringComparator comparator = new UTF16.StringComparator();
		// int result = comparator.compare(testData.get(0), cellText);
		assertEquals("Copy Cell, No loss of control codes", expectedHex,
				actualHex);
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
		log.info("Verify DIH-CLP-030 at Cell A1 ");
		int i = 1;
		for (String value : testData) {
			String cell = "A" + i;
			spreadsheetEditorPage.typeInCell(cell, value);
			i++;
		}
		spreadsheetEditorPage.selectRange("A1").cut();
		spreadsheetEditorPage.selectRange("B1").paste();
		String cellText = spreadsheetEditorPage.getCellText("B1");
		String actualHex = GUtils.getHexUnicode(cellText);
		String expectedHex = GUtils.getHexUnicode(testData.get(0));
		// UTF16.StringComparator comparator = new UTF16.StringComparator();
		// int result = comparator.compare(testData.get(0), cellText);
		assertEquals("Cut Cell, No loss of control codes", expectedHex,
				actualHex);
	}
}
