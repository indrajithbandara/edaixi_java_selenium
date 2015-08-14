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
public class ASIN extends BaseTest {
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
		String data = GUtils.getFormattedNumber(-0.5d, localeID, 1, false);
		// char separator = GFormula.getSeparator(localeID);
		String input1 = "="
				+ GFormula.getInstance().translate("ASIN", localeID) + "("
				+ data + ")";
		String input2 = "="
				+ GFormula.getInstance().translate("ASIN", localeID) + "("
				+ data + ")*180/"
				+ GFormula.getInstance().translate("PI", localeID) + "()";
		String input3 = "="
				+ GFormula.getInstance().translate("DEGREES", localeID) + "("
				+ GFormula.getInstance().translate("ASIN", localeID) + "("
				+ data + "))";
		spreadsheetEditorPage.typeInCell("A2", "'" + input1)
				.typeInCell("B2", input1).selectRange("B2");
		spreadsheetEditorPage.typeInCell("A3", "'" + input2)
				.typeInCell("B3", input2).selectRange("B3");
		spreadsheetEditorPage.typeInCell("A4", "'" + input3)
				.typeInCell("B4", input3).selectRange("B4");
		String expected1 = GUtils.getFormattedNumber(0.52359878d, localeID, 8,
				false);
		String actual1 = spreadsheetEditorPage.getCellText("B2");
		ga.assertEquals(expected1, actual1);
		String expected2 = "-30";
		String actual2 = spreadsheetEditorPage.getCellText("B3");
		ga.assertEquals(expected2, actual2);
		String expected3 = "-30";
		String actual3 = spreadsheetEditorPage.getCellText("B4");
		ga.assertEquals(expected3, actual3);
	}

	@Test
	@GLan("de-DE")
	public void atReference() {
		String localeID = GUtils
				.getBrowserPreferenceLan(spreadsheetEditorPage.driver);
		String data = GUtils.getFormattedNumber(-0.5d, localeID, 1, false);
		spreadsheetEditorPage.typeInCell("A1", "Formula")
				.typeInCell("B1", "Result").typeInCell("C1", "Data")
				.typeInCell("C2", data);
		// char separator = GFormula.getSeparator(localeID);
		String input1 = "="
				+ GFormula.getInstance().translate("ASIN", localeID) + "(C2)";
		String input2 = "="
				+ GFormula.getInstance().translate("ASIN", localeID)
				+ "(C2)*180/"
				+ GFormula.getInstance().translate("PI", localeID) + "()";
		String input3 = "="
				+ GFormula.getInstance().translate("DEGREES", localeID)
				+ "(B2)";
		spreadsheetEditorPage.typeInCell("A2", "'" + input1)
				.typeInCell("B2", input1).selectRange("B2");
		spreadsheetEditorPage.typeInCell("A3", "'" + input2)
				.typeInCell("B3", input2).selectRange("B3");
		spreadsheetEditorPage.typeInCell("A4", "'" + input3)
				.typeInCell("B4", input3).selectRange("B4");
		String expected1 = GUtils.getFormattedNumber(-0.52359878d, localeID, 8,
				false);
		String actual1 = spreadsheetEditorPage.getCellText("B2");
		ga.assertEquals(expected1, actual1);
		String expected2 = "-30";
		String actual2 = spreadsheetEditorPage.getCellText("B3");
		ga.assertEquals(expected2, actual2);
		String expected3 = "-30";
		String actual3 = spreadsheetEditorPage.getCellText("B4");
		ga.assertEquals(expected3, actual3);
	}
}
