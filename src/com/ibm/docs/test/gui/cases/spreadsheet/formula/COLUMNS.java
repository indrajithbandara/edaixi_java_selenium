package com.ibm.docs.test.gui.cases.spreadsheet.formula;

import org.junit.Before;
import org.junit.Test;

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
 * @author shiyac@cn.ibm.com
 * 
 */
public class COLUMNS extends BaseTest {
	@Drive
	private LoginPage loginPage;

	@Drive
	private FilesPage filesPage;

	@Drive
	private SpreadsheetEditorPage spreadsheetEditorPage;

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
		spreadsheetEditorPage.typeInCell("A1", "Formula").typeInCell("B1",
				"Result");
		String localeID = GUtils
				.getBrowserPreferenceLan(spreadsheetEditorPage.driver);
		String data1 = GUtils.getFormattedNumber(2.5d, localeID, 1, false);
		String data2 = GUtils.getFormattedNumber(3.5d, localeID, 1, false);
		String data3 = GUtils.getFormattedNumber(5.0d, localeID, 1, false);
		String data4 = GUtils.getFormattedNumber(6.0d, localeID, 1, false);
		char separator = GFormula.getSeparator(localeID);
		String input1 = "=" + GFormula.getInstance().translate("AND", localeID)
				+ "(" + GFormula.getInstance().translate("TRUE", localeID)
				+ separator
				+ GFormula.getInstance().translate("TRUE", localeID) + ")";
		String input2 = "=" + GFormula.getInstance().translate("AND", localeID)
				+ "(" + GFormula.getInstance().translate("TRUE", localeID)
				+ separator
				+ GFormula.getInstance().translate("FALSE", localeID) + ")";
		String input3 = "=" + GFormula.getInstance().translate("AND", localeID)
				+ "(" + data1 + "+" + data1 + "=" + data3 + separator + data1
				+ "+" + data2 + "=" + data4 + ")";
		spreadsheetEditorPage.typeInCell("A2", "'" + input1)
				.typeInCell("B2", input1).selectRange("B2");
		spreadsheetEditorPage.typeInCell("A3", "'" + input2)
				.typeInCell("B3", input2).selectRange("B3");
		spreadsheetEditorPage.typeInCell("A4", "'" + input3)
				.typeInCell("B4", input3).selectRange("B4");
		String expected1 = GFormula.getInstance().translate("TRUE", localeID);
		String actual1 = spreadsheetEditorPage.getCellText("B2");
		ga.assertEquals(expected1, actual1);
		String expected2 = GFormula.getInstance().translate("FALSE", localeID);
		String actual2 = spreadsheetEditorPage.getCellText("B3");
		ga.assertEquals(expected2, actual2);
		String expected3 = GFormula.getInstance().translate("TRUE", localeID);
		String actual3 = spreadsheetEditorPage.getCellText("B4");
		ga.assertEquals(expected3, actual3);
	}

	@Test
	@GLan("de-DE")
	public void atReference() {
		spreadsheetEditorPage.typeInCell("A1", "Data")
				.typeInCell("A4", "Formula").typeInCell("B4", "Result");
		String localeID = GUtils
				.getBrowserPreferenceLan(spreadsheetEditorPage.driver);
		String data1 = GUtils.getFormattedNumber(50.321d, localeID, 3, false);
		String data2 = GUtils.getFormattedNumber(100.321d, localeID, 3, false);
		spreadsheetEditorPage.typeInCell("A2", data1);
		spreadsheetEditorPage.typeInCell("A3", data2);
		char separator = GFormula.getSeparator(localeID);
		String input1 = "=" + GFormula.getInstance().translate("AND", localeID)
				+ "(1<A2" + separator + "A2<100)";
		String input2 = "=" + GFormula.getInstance().translate("IF", localeID)
				+ "(" + GFormula.getInstance().translate("AND", localeID)
				+ "(1<A3" + separator + "A3<100)" + separator + "A3"
				+ separator + "'The value is out of range.')";
		String input3 = "=" + GFormula.getInstance().translate("IF", localeID)
				+ "(" + GFormula.getInstance().translate("AND", localeID)
				+ "(1<A2" + separator + "A2<100)" + separator + "A2"
				+ separator + "'The value is out of range.')";
		spreadsheetEditorPage.typeInCell("A5", "'" + input1)
				.typeInCell("B5", input1).selectRange("B5");
		spreadsheetEditorPage.typeInCell("A6", "'" + input2)
				.typeInCell("B6", input2).selectRange("B6");
		spreadsheetEditorPage.typeInCell("A7", "'" + input3)
				.typeInCell("B7", input3).selectRange("B7");
		String expected1 = GFormula.getInstance().translate("TRUE", localeID);
		String actual1 = spreadsheetEditorPage.getCellText("B5");
		ga.assertEquals(expected1, actual1);
		String expected2 = "The value is out of range.";
		String actual2 = spreadsheetEditorPage.getCellText("B6");
		ga.assertEquals(expected2, actual2);
		String expected3 = data1;
		String actual3 = spreadsheetEditorPage.getCellText("B7");
		ga.assertEquals(expected3, actual3);
	}
}
