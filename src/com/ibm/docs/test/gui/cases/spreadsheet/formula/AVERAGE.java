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

/**
 * @author jianshud@cn.ibm.com
 * 
 */
public class AVERAGE extends BaseTest {
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
	@GLan({"de-DE","ja","en-us","zh-cn"})
	public void atGeneralIO() {
		log.info("Starting com.ibm.docs.test.gui.cases.spreadsheet.formula.AVERAGE.atGeneralIO()...");		
		String localeID = GUtils
				.getBrowserPreferenceLan(spreadsheetEditorPage.driver);
		
		spreadsheetEditorPage.typeInCell("A1", "Formula")
							.typeInCell("B1", "Result");
		
		// AVERAGE(1834.321,-574.321,4500.963) = 1920.321
		String number1 = GUtils.getFormattedNumber(1834.321d, localeID, 3, false);
		String number2 = GUtils.getFormattedNumber(-574.321d, localeID, 3, false);
		String number3 = GUtils.getFormattedNumber(4500.963d, localeID, 3, false);

		String input = "=" + GFormula.getInstance().translate("AVERAGE", localeID)
				+ "("				
				+ number1 + GFormula.getSeparator(localeID)
				+ number2 + GFormula.getSeparator(localeID)
				+ number3				
				+ ")";
		log.info("Formula: " + input);		
		spreadsheetEditorPage.typeInCell("A2", "'" + input)
				.typeInCell("B2", input).selectRange("B2");
		
		String expected = GUtils.getFormattedNumber(1920.321d, localeID, 3, false);
		String actual = spreadsheetEditorPage.getCellText("B2");
		ga.assertEquals(expected, actual);

		log.info("Finished com.ibm.docs.test.gui.cases.spreadsheet.formula.AVERAGE.atGeneralIO()...");		
	}

	@Test
	@GLan({"de-DE","ja","en-us","zh-cn"})
	public void atReference() {
		log.info("Starting com.ibm.docs.test.gui.cases.spreadsheet.formula.AVERAGE.atReference()...");	
		
		String localeID = GUtils
				.getBrowserPreferenceLan(spreadsheetEditorPage.driver);
		String formula = GFormula.getInstance().translate("AVERAGE", localeID);

		spreadsheetEditorPage.typeInCell("A1", "Data A")
				.typeInCell("B1", "Data B")
				.typeInCell("C1", "Formula")
				.typeInCell("D1", "Result");

		String number1 = GUtils.getFormattedNumber(1234.321d, localeID, 3, false);
		String number2 = GUtils.getFormattedNumber(-574.321d, localeID, 3, false);
		String number3 = GUtils.getFormattedNumber(20000d, localeID, 3, false);
		String number4 = GUtils.getFormattedNumber(-80000d, localeID, 3, false);
		String number5 = GUtils.getFormattedNumber(4000.444d, localeID, 3, false);
		spreadsheetEditorPage.typeInCell("A2", number1)
				.typeInCell("A3", number2)
				.typeInCell("A4", number3)
				.typeInCell("B2", number4);

		// 1.Verify cell reference.
		log.info("--Verify cell reference--");
		
		// AVERAGE(A2:A4,4000.444) = 6165.111
		String input1 = "=" + formula + "(A2:A4" + GFormula.getSeparator(localeID) + number5 + ")";
		log.info("Formula: " + input1);		
		spreadsheetEditorPage.typeInCell("C2", "'" + input1)
				.typeInCell("D2", input1).selectRange("D2");
		
		String expected1 = GUtils.getFormattedNumber(6165.111d, localeID, 3, false);
		String actual1 = spreadsheetEditorPage.getCellText("D2");
		ga.assertEquals(expected1, actual1);
		
		// 2.Verify sheet reference.
		log.info("--Verify sheet reference--");
		
		// AVERAGE(Sheet2!A2, Sheet2!B2) = -39382.8395
		spreadsheetEditorPage.selectSheet(2);
		spreadsheetEditorPage.typeInCell("A1", "Data A")
				.typeInCell("B1", "Data B");		
		spreadsheetEditorPage.typeInCell("A2", number1).typeInCell("B2", number4).selectRange("B2");
		
		spreadsheetEditorPage.selectSheet(1);
		String input2 = "=" + formula + "("
				+ "'" + spreadsheetEditorPage.getSheetName(2) + "'!A2"
				+ GFormula.getSeparator(localeID)
				+ "'" + spreadsheetEditorPage.getSheetName(2) + "'!B2"
				+ ")";
		log.info("Formula: " + input2);		
		spreadsheetEditorPage.typeInCell("C3", "'" + input2).
				typeInCell("D3", input2).selectRange("D3");
		
		String expected2 = GUtils.getFormattedNumber(-39382.8395d, localeID, 4, false);
		String actual2 = spreadsheetEditorPage.getCellText("D3");
		ga.assertEquals(expected2, actual2);
		
		
		// 3.Verify renamed sheet reference.
		log.info("--Verify renamed sheet reference--");
		String sheetName = GData.getInstance().getDataById(44);
		spreadsheetEditorPage.renameSheet(2, sheetName);
		spreadsheetEditorPage.selectSheet(1);
		String input3 = "=" + formula + "("
				+ "'" + spreadsheetEditorPage.getSheetName(2) + "'!A2"
				+ GFormula.getSeparator(localeID)
				+ "'" + spreadsheetEditorPage.getSheetName(2) + "'!B2"
				+ ")";
		log.info("Formula: " + input3);		
		spreadsheetEditorPage.typeInCell("C4", "'" + input3).typeInCell("D4", input3).selectRange("D4");
		
		String expected3 = GUtils.getFormattedNumber(-39382.8395d, localeID, 4, false);
		String actual3 = spreadsheetEditorPage.getCellText("D4");
		ga.assertEquals(expected3, actual3);
		
		
		// 4.Verify sheet reference after insert Row/Column.
		log.info("--Verify sheet reference after insert Row/Column--");
		spreadsheetEditorPage.selectSheet(2);
		spreadsheetEditorPage.selectRange("A1");
		spreadsheetEditorPage.insertRowAbove();
		spreadsheetEditorPage.insertColumnBefore();
		spreadsheetEditorPage.selectSheet(1);
		String actual4 = spreadsheetEditorPage.getCellText("D4");
		ga.assertEquals(expected3, actual4);

		
		// 5.Verify sheet reference after delete Row/Column.
		log.info("--Verify sheet reference after delete Row/Column--");
		spreadsheetEditorPage.selectSheet(2);
		spreadsheetEditorPage.selectRange("A1");
		spreadsheetEditorPage.deleteRow();
		spreadsheetEditorPage.deleteColumn();
		spreadsheetEditorPage.selectSheet(1);
		String actual5 = spreadsheetEditorPage.getCellText("D4");
		ga.assertEquals(expected3, actual5);
	
		log.info("Finished com.ibm.docs.test.gui.cases.spreadsheet.formula.AVERAGE.atReference()...");		
	}
}
