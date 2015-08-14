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
public class BASE extends BaseTest {
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

	//@Test
	@GLan({"de-DE","ja","en-us","zh-cn"})
	public void atGeneralIO() {
		log.info("Starting com.ibm.docs.test.gui.cases.spreadsheet.formula.BASE.atGeneralIO()...");		
		String localeID = GUtils
				.getBrowserPreferenceLan(spreadsheetEditorPage.driver);
		String formula = GFormula.getInstance().translate("BASE", localeID);
		
		spreadsheetEditorPage.typeInCell("A1", "Formula")
							.typeInCell("B1", "Result");

		String Radix2 = GUtils.getFormattedNumber(2d, localeID, 0, false);
		String Radix16 = GUtils.getFormattedNumber(16d, localeID, 0, false);
		
		String number1 = GUtils.getFormattedNumber(65535d, localeID, 0, false);
		String number2 = GUtils.getFormattedNumber(100d, localeID, 0, false);
		String number3 = GUtils.getFormattedNumber(15d, localeID, 0, false);

		// BASE(65535,2) = 1111111111111111
		String input1 = "=" + formula
				+ "("				
				+ number1 + GFormula.getSeparator(localeID)
				+ Radix2				
				+ ")";
		log.info("Formula: " + input1);		
		spreadsheetEditorPage.typeInCell("A2", "'" + input1)
				.typeInCell("B2", input1).selectRange("B2");
		
		// TODO: need consider script of digits? 
		String expected1 = "1111111111111111";
		String actual1 = spreadsheetEditorPage.getCellText("B2");
		ga.assertEquals(expected1, actual1);

		   
		// BASE(100,16) = 64 
		String input2 = "=" + formula
				+ "("				
				+ number2 + GFormula.getSeparator(localeID)
				+ Radix16				
				+ ")";
		log.info("Formula: " + input2);		
		spreadsheetEditorPage.typeInCell("A3", "'" + input2)
				.typeInCell("B3", input2).selectRange("B3");
		
		// TODO: need consider script of digits? 
		String expected2 = "64";
		String actual2 = spreadsheetEditorPage.getCellText("B3");
		ga.assertEquals(expected2, actual2);
		
		
		// BASE(15,2,10) = 0000001111		
		String input3 = "=" + formula
				+ "("				
				+ number3 + GFormula.getSeparator(localeID)
				+ Radix2 + GFormula.getSeparator(localeID)				
				+ "10)";
		log.info("Formula: " + input3);		
		spreadsheetEditorPage.typeInCell("A4", "'" + input3)
				.typeInCell("B4", input3).selectRange("B4");
		
		// TODO: need consider script of digits? 
		String expected3 = "0000001111";
		String actual3 = spreadsheetEditorPage.getCellText("B4");
		ga.assertEquals(expected3, actual3);
		
		log.info("Finished com.ibm.docs.test.gui.cases.spreadsheet.formula.BASE.atGeneralIO()...");		
	}

	@Test
	@GLan({"de-DE","ja","en-us","zh-cn"})
	public void atReference() {
		log.info("Starting com.ibm.docs.test.gui.cases.spreadsheet.formula.BASE.atReference()...");	
		
		String localeID = GUtils
				.getBrowserPreferenceLan(spreadsheetEditorPage.driver);
		String formula = GFormula.getInstance().translate("BASE", localeID);

		spreadsheetEditorPage.typeInCell("A1", "Number")
				.typeInCell("B1", "Radix")
				.typeInCell("C1", "Formula")
				.typeInCell("D1", "Result");

		String number1 = GUtils.getFormattedNumber(65535d, localeID, 0, false);
		String Radix2 = GUtils.getFormattedNumber(2d, localeID, 0, false);
		spreadsheetEditorPage.typeInCell("A2", number1)
				.typeInCell("B2", Radix2);

		
		// 1.Verify cell reference.
		log.info("--Verify cell reference--");
		
		// BASE(A2,B2) = 1111111111111111
		String input1 = "=" + formula + "(A2" + GFormula.getSeparator(localeID) + "B2)";
		log.info("Formula: " + input1);		
		spreadsheetEditorPage.typeInCell("C2", "'" + input1)
				.typeInCell("D2", input1).selectRange("D2");
		
		// TODO: need consider script of digits?		
		String expected1 = "1111111111111111";
		String actual1 = spreadsheetEditorPage.getCellText("D2");
		ga.assertEquals(expected1, actual1);
	
		
		// 2.Verify sheet reference.
		log.info("--Verify sheet reference--");
		
		// BASE(Sheet2!A2, Sheet2!B2) = 64
		String Radix16 = GUtils.getFormattedNumber(16d, localeID, 0, false);
		String number2 = GUtils.getFormattedNumber(100d, localeID, 0, false);
		
		spreadsheetEditorPage.selectSheet(2);
		spreadsheetEditorPage.typeInCell("A1", number2).typeInCell("B1", Radix16).selectRange("B1");
		spreadsheetEditorPage.selectSheet(1);
		
		String input2 = "=" + formula + "("
				+ "'" + spreadsheetEditorPage.getSheetName(2) + "'!A1"
				+ GFormula.getSeparator(localeID)
				+ "'" + spreadsheetEditorPage.getSheetName(2) + "'!B1"
				+ ")";
		log.info("Formula: " + input2);		
		spreadsheetEditorPage.typeInCell("C3", "'" + input2).
				typeInCell("D3", input2).selectRange("D3");
		
		// TODO: need consider script of digits?		
		String expected2 = "64";
		String actual2 = spreadsheetEditorPage.getCellText("D3");
		ga.assertEquals(expected2, actual2);
		
		
		// 3.Verify renamed sheet reference.
		log.info("--Verify renamed sheet reference--");
		String sheetName = GData.getInstance().getDataById(44);
		spreadsheetEditorPage.renameSheet(2, sheetName);
		spreadsheetEditorPage.selectSheet(1);
		
		String input3 = "=" + formula + "("
				+ "'" + spreadsheetEditorPage.getSheetName(2) + "'!A1"
				+ GFormula.getSeparator(localeID)
				+ "'" + spreadsheetEditorPage.getSheetName(2) + "'!B1"
				+ ")";
		log.info("Formula: " + input3);		
		spreadsheetEditorPage.typeInCell("C4", "'" + input3).typeInCell("D4", input3).selectRange("D4");
		
		String expected3 = expected2;
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
	
		log.info("Finished com.ibm.docs.test.gui.cases.spreadsheet.formula.BASE.atReference()...");		
	}
}
