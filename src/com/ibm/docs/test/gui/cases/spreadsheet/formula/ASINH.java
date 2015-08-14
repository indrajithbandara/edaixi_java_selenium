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
public class ASINH extends BaseTest {
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
		String data = GUtils.getFormattedNumber(-2.5d, localeID, 1, false);
		// char separator = GFormula.getSeparator(localeID);
		String input1 = "="
				+ GFormula.getInstance().translate("ASINH", localeID) + "("
				+ data + ")";
		String input2 = "="
				+ GFormula.getInstance().translate("ASINH", localeID) + "(10)";
		spreadsheetEditorPage.typeInCell("A2", "'" + input1)
				.typeInCell("B2", input1).selectRange("B2");
		spreadsheetEditorPage.typeInCell("A3", "'" + input2)
				.typeInCell("B3", input2).selectRange("B3");
		String expected1 = GUtils.getFormattedNumber(-1.64723115d, localeID, 8,
				false);
		String actual1 = spreadsheetEditorPage.getCellText("B2");
		ga.assertEquals(expected1, actual1);
		String expected2 = GUtils.getFormattedNumber(2.99822295d, localeID, 8,
				false);
		String actual2 = spreadsheetEditorPage.getCellText("B3");
		ga.assertEquals(expected2, actual2);
	}

	@Test
	@GLan("de-DE")
	public void atReference() {
		String localeID = GUtils
				.getBrowserPreferenceLan(spreadsheetEditorPage.driver);
		String data = GUtils.getFormattedNumber(-2.5d, localeID, 1, false);
		spreadsheetEditorPage.typeInCell("A1", "Formula")
				.typeInCell("B1", "Result").typeInCell("C1", "Data")
				.typeInCell("C2", data).typeInCell("C3", "10");
		// char separator = GFormula.getSeparator(localeID);
		String input1 = "="
				+ GFormula.getInstance().translate("ASINH", localeID) + "(C2)";
		String input2 = "="
				+ GFormula.getInstance().translate("ASINH", localeID) + "(C3)";
		spreadsheetEditorPage.typeInCell("A2", "'" + input1)
				.typeInCell("B2", input1).selectRange("B2");
		spreadsheetEditorPage.typeInCell("A3", "'" + input2)
				.typeInCell("B3", input2).selectRange("B3");
		String expected1 = GUtils.getFormattedNumber(-1.64723115d, localeID, 8,
				false);
		String actual1 = spreadsheetEditorPage.getCellText("B2");
		ga.assertEquals(expected1, actual1);
		String expected2 = GUtils.getFormattedNumber(2.99822295d, localeID, 8,
				false);
		String actual2 = spreadsheetEditorPage.getCellText("B3");
		ga.assertEquals(expected2, actual2);
	}
}
