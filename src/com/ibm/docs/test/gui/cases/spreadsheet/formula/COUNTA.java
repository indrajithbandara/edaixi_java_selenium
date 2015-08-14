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

public class COUNTA extends BaseTest {

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
	@GLan("de-DE")
	public void atGeneralIO() {
		log.info("Starting com.ibm.docs.test.gui.cases.spreadsheet.formula.COUNTA.atGeneralIO()...");
		spreadsheetEditorPage.typeInCell("A1", "Formula").typeInCell("B1",
				"Result");
		String localeID = GUtils
				.getBrowserPreferenceLan(spreadsheetEditorPage.driver);
		String formula = GFormula.getInstance().translate("COUNTA", localeID);
		// sample 1
		String number1 = GUtils.getFormattedNumber(39790.123d, localeID, 3,
				false);

		String number2 = GUtils.getFormattedNumber(19.32d, localeID, 3, false);
		String number3 = GUtils.getFormattedNumber(22.24d, localeID, 3, false);
		String string1 = GFormula.getInstance().translate("TRUE", localeID);
		String string2 = "#DIV/0!";
		String input = "=" + formula + "(" + number1
				+ GFormula.getSeparator(localeID)
				+ +GFormula.getSeparator(localeID) + number2
				+ GFormula.getSeparator(localeID) + number3
				+ GFormula.getSeparator(localeID) + string1
				+ GFormula.getSeparator(localeID) + string2 + ")";
		log.info("Formula: " + input);
		spreadsheetEditorPage.typeInCell("A2", "'" + input)
				.typeInCell("B2", input).selectRange("B2");

		String expected = "5";
		String actual = spreadsheetEditorPage.getCellText("B2");
		ga.assertEquals(expected, actual);

		log.info("Finished com.ibm.docs.test.gui.cases.spreadsheet.formula.COUNTA.atGeneralIO()...");

	}

	@Test
	@GLan("de-DE")
	public void atReference() {
		log.info("Starting com.ibm.docs.test.gui.cases.spreadsheet.formula.COUNTA.atReference()...");
		spreadsheetEditorPage.typeInCell("A1", "Formula").typeInCell("B1",
				"Result");
		String localeID = GUtils
				.getBrowserPreferenceLan(spreadsheetEditorPage.driver);
		String formula = GFormula.getInstance().translate("COUNTA", localeID);
		String number1 = GUtils.getFormattedNumber(39790.123d, localeID, 3,
				false);

		String number2 = GUtils.getFormattedNumber(19.32d, localeID, 3, false);
		String number3 = GUtils.getFormattedNumber(22.24d, localeID, 3, false);
		String string1 = GFormula.getInstance().translate("TRUE", localeID);
		String string2 = "#DIV/0!";
		spreadsheetEditorPage.typeInCell("A2", number1)
				.typeInCell("A3", number2).typeInCell("A4", number3)
				.typeInCell("B2", string1).typeInCell("B3", string2);
		// 1.Verify cell reference.
		log.info("--Verify cell reference--");
		String input = "=" + formula + "(A2:A4 "
				+ GFormula.getSeparator(localeID) + "B2:B4)";
		log.info("Formula: " + input);
		spreadsheetEditorPage.typeInCell("A5", "'" + input)
				.typeInCell("B5", input).selectRange("B5");

		String expected = "5";
		String actual = spreadsheetEditorPage.getCellText("B5");
		ga.assertEquals(expected, actual);

		// 2.Verify sheet reference.
		log.info("Verify sheet reference.");
		spreadsheetEditorPage.selectSheet(2);
		spreadsheetEditorPage.typeInCell("A2", number1)
				.typeInCell("A3", number2).typeInCell("A4", number3)
				.typeInCell("B2", string1).typeInCell("B3", string2);
		spreadsheetEditorPage.selectSheet(3);
		String input2 = GFormula.parse(
				"=COUNTA('" + spreadsheetEditorPage.getSheetName(2) + "'!A2,"
						+ "'" + spreadsheetEditorPage.getSheetName(2) + "'!A3,"
						+ "'" + spreadsheetEditorPage.getSheetName(2) + "'!A4,"
						+ "'" + spreadsheetEditorPage.getSheetName(2) + "'!B2,"
						+ "'" + spreadsheetEditorPage.getSheetName(2) + "'!B3,"
						+ "'" + spreadsheetEditorPage.getSheetName(2) + "'!B4"
						+ ")", localeID);
		spreadsheetEditorPage.typeInCell("A6", "'" + input2)
				.typeInCell("B6", input2).selectRange("B6");
		String actual2 = spreadsheetEditorPage.getCellText("B6");
		ga.assertEquals(expected, actual2);
		// 3.Verify renamed sheet reference.
		log.info("--Verify renamed sheet reference--");
		String sheetName = GData.getInstance().getDataById(44);
		spreadsheetEditorPage.renameSheet(2, sheetName);
		spreadsheetEditorPage.selectSheet(1);
		String input3 = GFormula.parse("=COUNTA('" + sheetName + "'!A2,'"
				+ sheetName + "'!A3,'" + sheetName + "'!A4,'" + sheetName
				+ "'!B2,'" + sheetName + "'!B3,'" + sheetName + "'!B4" + ")",
				localeID);
		log.info("Formula: " + input3);
		spreadsheetEditorPage.typeInCell("C4", "'" + input3)
				.typeInCell("D4", input3).selectRange("D4");
		String actual3 = spreadsheetEditorPage.getCellText("D4");
		ga.assertEquals(expected, actual3);
		// 4.Verify sheet reference after insert Row/Column.
		log.info("--Verify sheet reference after insert Row/Column--");
		spreadsheetEditorPage.selectSheet(2);
		spreadsheetEditorPage.selectRange("A1");
		spreadsheetEditorPage.insertRowAbove();
		spreadsheetEditorPage.insertColumnBefore();
		spreadsheetEditorPage.selectSheet(1);
		String actual4 = spreadsheetEditorPage.getCellText("D4");
		ga.assertEquals(expected, actual4);

		// 5.Verify sheet reference after delete Row/Column.
		log.info("--Verify sheet reference after delete Row/Column--");
		spreadsheetEditorPage.selectSheet(2);
		spreadsheetEditorPage.selectRange("A1");
		spreadsheetEditorPage.deleteRow();
		spreadsheetEditorPage.deleteColumn();
		spreadsheetEditorPage.selectSheet(1);
		String actual5 = spreadsheetEditorPage.getCellText("D4");
		ga.assertEquals(expected, actual5);
		// 6.Verify Name Ranged
		log.info("Verify Name Ranged.");
		String name1 = testData.get(random.nextInt(2));
		String name2 = testData.get(random.nextInt(2) + 2);
		String name3 = testData.get(random.nextInt(2) + 4);
		String name4 = testData.get(random.nextInt(2) + 6);
		String name5 = testData.get(random.nextInt(2) + 8);
		spreadsheetEditorPage.selectSheet(2);
		spreadsheetEditorPage.selectRange("A2").newNamedRange(name1);
		spreadsheetEditorPage.selectRange("A3").newNamedRange(name2);
		spreadsheetEditorPage.selectRange("A4").newNamedRange(name3);
		spreadsheetEditorPage.selectRange("B2").newNamedRange(name4);
		spreadsheetEditorPage.selectRange("B3").newNamedRange(name5);
		spreadsheetEditorPage.selectSheet(1);
		String input6 = GFormula.parse("=COUNTA(" + name1 + "," + name2 + ","
				+ name3 + "," + name4 + "," + name5 + ")", localeID);
		spreadsheetEditorPage.typeInCell("A7", "'" + input6)
				.typeInCell("B7", input6).selectRange("B7");
		String actual6 = spreadsheetEditorPage.getCellText("B7");
		ga.assertEquals(expected, actual6);
		log.info("Finished com.ibm.docs.test.gui.cases.spreadsheet.formula.COUNTA.atReference()...");
	}
}
