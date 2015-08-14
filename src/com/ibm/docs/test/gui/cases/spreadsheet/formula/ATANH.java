package com.ibm.docs.test.gui.cases.spreadsheet.formula;

import org.junit.Before;
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


public class ATANH extends BaseTest{
	
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
		//Sample 1
		String input1 = GFormula.parse("=ATANH(0.76159416)", localeID);
		spreadsheetEditorPage.typeInCell("A2", "'" + input1)
				.typeInCell("B2", input1).selectRange("B2");
		String expected1 = GUtils.getFormattedNumber(1.00000001d, localeID, 8,
				false);
		String actual1 = spreadsheetEditorPage.getCellText("B2");
		ga.assertEquals(expected1, actual1);
		
		//Sample 2
		
		String input2 = GFormula.parse("=ATANH(-0.1)", localeID);
		spreadsheetEditorPage.typeInCell("A3", "'" + input2)
		.typeInCell("B3", input2).selectRange("B3");
		
		String expected2 = GUtils.getFormattedNumber(-0.100335348d, localeID, 9,
				false);
		String actual2 = spreadsheetEditorPage.getCellText("B3");
		ga.assertEquals(expected2, actual2);
		
		
	}
	

	@Test
	@GLan("de-DE")
	public void atReference() {
		String localeID = GUtils
				.getBrowserPreferenceLan(spreadsheetEditorPage.driver);
		String data = GUtils.getFormattedNumber(0.76159416d, localeID,8, false);
		
		spreadsheetEditorPage.typeInCell("A1", "Data").typeInCell("A2", data)
				.typeInCell("A3", "Formula").typeInCell("B3", "Result").selectRange("A1");
		String expected = GUtils.getFormattedNumber(1.00000001d, localeID, 8,
				false);
		// 1.Verify cell reference.Sample 1
		
		log.info("Verify cell reference.");
		String input1 = "=" + GFormula.getInstance().translate("ATANH", localeID)
				+ "(A2)";
		spreadsheetEditorPage.typeInCell("A4", "'" + input1)
				.typeInCell("B4", input1).selectRange("B4");
		String actual1 = spreadsheetEditorPage.getCellText("B4");
		ga.assertEquals(expected, actual1);
		
		// 2.Verify sheet reference.
		log.info("Verify sheet reference.");
	
		spreadsheetEditorPage.selectSheet(2);
		spreadsheetEditorPage.typeInCell("B1", "Data").typeInCell("B2", data)
				.selectRange("B2");
		
		spreadsheetEditorPage.selectSheet(1);
		String input2 = "=" + GFormula.getInstance().translate("ATANH", localeID)
				+ "(" + spreadsheetEditorPage.getSheetName(2) + "!B2)";
		spreadsheetEditorPage.typeInCell("A5", "'" + input2)
				.typeInCell("B5", input2).selectRange("B5");
		String actual2 = spreadsheetEditorPage.getCellText("B5");
		ga.assertEquals(expected, actual2);
		
		
		// 3.Verify renamed sheet reference.
		log.info("Verify renamed sheet reference.");
		String sheetName = GData.getInstance().getDataById(44);
		spreadsheetEditorPage.renameSheet(2, sheetName);
		spreadsheetEditorPage.selectSheet(1);
		String input3 = "=" + GFormula.getInstance().translate("ATANH", localeID)
				+ "('" + spreadsheetEditorPage.getSheetName(2) + "'!B2)";
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
		log.info("Verify sheet reference after insert.");
		spreadsheetEditorPage.selectSheet(2);
		spreadsheetEditorPage.selectRange("A1");
		spreadsheetEditorPage.deleteRow();
		spreadsheetEditorPage.deleteColumn();
		spreadsheetEditorPage.selectSheet(1);
		String actual5 = spreadsheetEditorPage.getCellText("B6");
		ga.assertEquals(expected, actual5);
		
		//6.Verify Name Ranged
		log.info("Verify Name Ranged.");
		spreadsheetEditorPage.selectRange("A2");
		spreadsheetEditorPage.newNamedRange("namerange");
		String input6 = "=" + GFormula.getInstance().translate("ATANH", localeID)
				+ "(namerange)";
		spreadsheetEditorPage.typeInCell("A7", "'" + input6)
				.typeInCell("B7", input6).selectRange("B7");
		String actual7 = spreadsheetEditorPage.getCellText("B7");
		ga.assertEquals(expected, actual7);
		
		
	}
	

}
