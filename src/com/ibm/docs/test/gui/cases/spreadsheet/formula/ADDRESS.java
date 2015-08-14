package com.ibm.docs.test.gui.cases.spreadsheet.formula;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ibm.docs.test.common.GData;
import com.ibm.docs.test.common.GFormula;
import com.ibm.docs.test.common.GUtils;
import com.ibm.docs.test.common.Utils;
import com.ibm.docs.test.gui.cases.BaseTest;
import com.ibm.docs.test.gui.pages.FilesPage;
import com.ibm.docs.test.gui.pages.LoginPage;
import com.ibm.docs.test.gui.pages.SpreadsheetEditorPage;
import com.ibm.docs.test.gui.support.Drive;
import com.ibm.docs.test.gui.support.GLan;

/**
 * @author xinyutan@cn.ibm.com
 * 
 */
public class ADDRESS extends BaseTest {
	@Drive
	private LoginPage loginPage;

	@Drive
	private FilesPage filesPage;

	@Drive
	private SpreadsheetEditorPage spreadsheetEditorPage;

	private static List<String> testData = new ArrayList<String>();

	private static Random random = new Random();

	@BeforeClass
	public static void beforeClass() {
		int[] ids = { 3, 4, 5, 6, 7, 11, 20, 40, 44, 45 };
		GData gData = GData.getInstance();
		for (int id : ids) {
			testData.add(gData.getDataById(id));
		}
	}

	@Before
	public void before() {
		loginPage.login();
		filesPage.go();
		String filename = Utils.uniqueBaseName("newSpreadsheetInFiles");
		spreadsheetEditorPage.focus(filesPage.newSpreadsheet(filename));
		spreadsheetEditorPage.driver
				.javascript("pe.base.enableFormulaAutoComplete=false");
	}

	@Test
	@GLan({ "pt-BR", "pt-PT", "bg-BG", "ca-ES", "cs-CZ", "da-DK", "nl-NL",
		"fi-FI", "fr-FR", "de-DE", "el-GR", "hu-HU", "it-IT", "ja-JP",
		"kk-KZ", "ko-KR", "nn-NO", "pl-PL", "ro-RO", "ru-RU", "zh-CN",
		"es-ES", "sv-SE", "th-TH", "zh-TW", "tr-TR", "hr-HR", "sk-SK",
		"sl-SI", "id-ID" })
	public void atGeneralIO() {
		spreadsheetEditorPage.typeInCell("A1", "Formula").typeInCell("B1",
				"Result");
		String localeID = GUtils
				.getBrowserPreferenceLan(spreadsheetEditorPage.driver);
		// test 1
		String input1 = GFormula.parse("=ADDRESS(2,3)", localeID);
		String expected1 = "$C$2";
		spreadsheetEditorPage.typeInCell("A2", "'" + input1)
				.typeInCell("B2", input1).selectRange("B2");
		String actual1 = spreadsheetEditorPage.getCellText("B2");
		ga.assertEquals(expected1, actual1);
		// test 2
		String input2 = GFormula.parse("=ADDRESS(2,3,2)", localeID);
		String expected2 = "C$2";
		spreadsheetEditorPage.typeInCell("A3", "'" + input2)
				.typeInCell("B3", input2).selectRange("B3");
		String actual2 = spreadsheetEditorPage.getCellText("B3");
		ga.assertEquals(expected2, actual2);
		// test 3
		String sheetName = testData.get(random.nextInt(10));
		String input3 = GFormula.parse("=ADDRESS(2,3,4,1,\"" + sheetName
				+ "\")", localeID);
		String expected3 = "'" + sheetName + "'!C2";
		spreadsheetEditorPage.typeInCell("A4", "'" + input3)
				.typeInCell("B4", input3).selectRange("B4");
		String actual3 = spreadsheetEditorPage.getCellText("B4");
		ga.assertEquals(expected3, actual3);
	}

	@Test
	@GLan({ "pt-BR", "pt-PT", "bg-BG", "ca-ES", "cs-CZ", "da-DK", "nl-NL",
		"fi-FI", "fr-FR", "de-DE", "el-GR", "hu-HU", "it-IT", "ja-JP",
		"kk-KZ", "ko-KR", "nn-NO", "pl-PL", "ro-RO", "ru-RU", "zh-CN",
		"es-ES", "sv-SE", "th-TH", "zh-TW", "tr-TR", "hr-HR", "sk-SK",
		"sl-SI", "id-ID" })
	public void atReference() {
		String localeID = GUtils
				.getBrowserPreferenceLan(spreadsheetEditorPage.driver);
		String data1 = GUtils.getFormattedNumber(2d, localeID, 0, false);
		String data2 = GUtils.getFormattedNumber(3d, localeID, 0, false);
		String data3 = GUtils.getFormattedNumber(4d, localeID, 0, false);
		String data4 = GUtils.getFormattedNumber(1d, localeID, 0, false);
		String data5 = testData.get(random.nextInt(10));
		spreadsheetEditorPage.typeInCell("A1", "Data").typeInCell("A2", data1)
				.typeInCell("B1", "Data").typeInCell("B2", data2)
				.typeInCell("C1", "Data").typeInCell("C2", data3)
				.typeInCell("D1", "Data").typeInCell("D2", data4)
				.typeInCell("E1", "Data").typeInCell("E2", data5)
				.typeInCell("A3", "Formula").typeInCell("B3", "Result");
		String expected = "'" + data5 + "'!C2";
		// 1.Verify cell reference.
		log.info("Verify cell reference.");
		String input1 = GFormula.parse("=ADDRESS(A2,B2,C2,D2,E2)", localeID);
		spreadsheetEditorPage.typeInCell("A4", "'" + input1)
				.typeInCell("B4", input1).selectRange("B4");
		String actual1 = spreadsheetEditorPage.getCellText("B4");
		ga.assertEquals(expected, actual1);
		// 2.Verify sheet reference.
		log.info("Verify sheet reference.");
		spreadsheetEditorPage.selectSheet(2);
		spreadsheetEditorPage.typeInCell("A1", "Data").typeInCell("A2", data1)
				.typeInCell("B1", "Data").typeInCell("B2", data2)
				.typeInCell("C1", "Data").typeInCell("C2", data3)
				.typeInCell("D1", "Data").typeInCell("D2", data4)
				.typeInCell("E1", "Data").typeInCell("E2", data5)
				.selectRange("A1");
		spreadsheetEditorPage.selectSheet(1);
		String input2 = GFormula.parse(
				"=ADDRESS('" + spreadsheetEditorPage.getSheetName(2) + "'!A2,"
						+ "'" + spreadsheetEditorPage.getSheetName(2) + "'!B2,"
						+ "'" + spreadsheetEditorPage.getSheetName(2) + "'!C2,"
						+ "'" + spreadsheetEditorPage.getSheetName(2) + "'!D2,"
						+ "'" + spreadsheetEditorPage.getSheetName(2) + "'!E2"
						+ ")", localeID);
		spreadsheetEditorPage.typeInCell("A5", "'" + input2)
				.typeInCell("B5", input2).selectRange("B5");
		String actual2 = spreadsheetEditorPage.getCellText("B5");
		ga.assertEquals(expected, actual2);
		// 3.Verify renamed sheet reference.
		log.info("Verify renamed sheet reference.");
		String sheetName = testData.get(random.nextInt(10));
		spreadsheetEditorPage.renameSheet(2, sheetName);
		spreadsheetEditorPage.selectSheet(1);
		String input3 = GFormula.parse("=ADDRESS('" + sheetName + "'!A2,'"
				+ sheetName + "'!B2,'" + sheetName + "'!C2,'" + sheetName
				+ "'!D2,'" + sheetName + "'!E2" + ")", localeID);
		spreadsheetEditorPage.typeInCell("A6", "'" + input3)
				.typeInCell("B6", input3).selectRange("B6");
		String actual3 = spreadsheetEditorPage.getCellText("B6");
		ga.assertEquals(expected, actual3);
		// 4.Verify sheet reference after insert Row/Column.
		log.info("Verify sheet reference after insert Row/Column.");
		spreadsheetEditorPage.selectSheet(2);
		spreadsheetEditorPage.selectRange("A1");
		spreadsheetEditorPage.insertRowAbove();
		spreadsheetEditorPage.insertColumnBefore();
		spreadsheetEditorPage.selectSheet(1);
		String actual4 = spreadsheetEditorPage.getCellText("B6");
		ga.assertEquals(expected, actual4);
		// 5.Verify sheet reference after delete Row/Column.
		log.info("Verify sheet reference after delete Row/Column.");
		spreadsheetEditorPage.selectSheet(2);
		spreadsheetEditorPage.selectRange("A1");
		spreadsheetEditorPage.deleteRow();
		spreadsheetEditorPage.deleteColumn();
		spreadsheetEditorPage.selectSheet(1);
		String actual5 = spreadsheetEditorPage.getCellText("B6");
		ga.assertEquals(expected, actual5);
		// 6. Verify named range reference.
		log.info("Verify named range reference.");
		String name1 = testData.get(random.nextInt(2));
		String name2 = testData.get(random.nextInt(2)+2);
		String name3 = testData.get(random.nextInt(2)+4);
		String name4 = testData.get(random.nextInt(2)+6);
		String name5 = testData.get(random.nextInt(2)+8);
		spreadsheetEditorPage.selectSheet(2);
		spreadsheetEditorPage.selectRange("A2").newNamedRange(name1);
		spreadsheetEditorPage.selectRange("B2").newNamedRange(name2);
		spreadsheetEditorPage.selectRange("C2").newNamedRange(name3);
		spreadsheetEditorPage.selectRange("D2").newNamedRange(name4);
		spreadsheetEditorPage.selectRange("E2").newNamedRange(name5);
		spreadsheetEditorPage.selectSheet(1);
		String input6 = GFormula.parse("=ADDRESS(" + name1 + "," + name2 + ","
				+ name3 + "," + name4 + "," + name5 + ")", localeID);
		spreadsheetEditorPage.typeInCell("A7", "'" + input6)
				.typeInCell("B7", input6).selectRange("B7");
		String actual6 = spreadsheetEditorPage.getCellText("B7");
		ga.assertEquals(expected, actual6);
	}
}