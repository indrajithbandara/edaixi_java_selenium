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


public class ATAN extends BaseTest{
	
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
	//@GLan({ "pt-BR", "pt-PT", "bg-BG", "ca-ES", "cs-CZ", "da-DK", "nl-NL",
		//"fi-FI", "fr-FR", "de-DE", "el-GR", "hu-HU", "it-IT", "ja-JP",
		//"kk-KZ", "ko-KR", "nn-NO", "pl-PL", "ro-RO", "ru-RU", "zh-CN",
		//"es-ES", "sv-SE", "th-TH", "zh-TW", "tr-TR", "hr-HR", "sk-SK",
		//"sl-SL" })
	
	@GLan("ro-RO")
	public void atGeneralIO() {
		spreadsheetEditorPage.typeInCell("A1", "Formula").typeInCell("B1",
				"Result");
		String localeID = GUtils
				.getBrowserPreferenceLan(spreadsheetEditorPage.driver);
		//Sample 1
		String input1 = "=" + GFormula.getInstance().translate("ATAN", localeID)
				+ "("
				+ GUtils.getFormattedNumber(1d, localeID, 3, false)
				+ ")";
		spreadsheetEditorPage.typeInCell("A2", "'" + input1)
				.typeInCell("B2", input1).selectRange("B2");
		String expected1 = GUtils.getFormattedNumber(0.785398163d, localeID, 9,
				false);
		String actual1 = spreadsheetEditorPage.getCellText("B2");
		ga.assertEquals(expected1, actual1);
		
		//Sample 2
		
		String input2 = "=" + GFormula.getInstance().translate("ATAN", localeID)
				+ "("
				+ GUtils.getFormattedNumber(1d, localeID, 3, false)
				+ ")" + "*" + "180/PI()";
		spreadsheetEditorPage.typeInCell("A3", "'" + input2)
		.typeInCell("B3", input2).selectRange("B3");
		
		String expected2 = GUtils.getFormattedNumber(45d, localeID, 0,
				false);
		String actual2 = spreadsheetEditorPage.getCellText("B3");
		ga.assertEquals(expected2, actual2);
		
		//Sample 3
		String input3 = GFormula.parse("=DEGREES(ATAN(1))", localeID);
		String expected3 = GUtils.getFormattedNumber(45d, localeID, 0, false);
		spreadsheetEditorPage.typeInCell("A4", "'" + input3)
				.typeInCell("B4", input3).selectRange("B4");
		String actual3 = spreadsheetEditorPage.getCellText("B4");
		ga.assertEquals(expected3, actual3);
			
	}

	@Test
	//@GLan("de-DE")
	@GLan({ "pt-BR", "pt-PT", "bg-BG", "ca-ES", "cs-CZ", "da-DK", "nl-NL",
			"fi-FI", "fr-FR", "de-DE", "el-GR", "hu-HU", "it-IT", "ja-JP",
			"kk-KZ", "ko-KR", "nn-NO", "pl-PL", "ro-RO", "ru-RU", "zh-CN",
			"es-ES", "sv-SE", "th-TH", "zh-TW", "tr-TR", "hr-HR", "sk-SK",
			"sl-SL" })
	public void atReference() {
		String localeID = GUtils
				.getBrowserPreferenceLan(spreadsheetEditorPage.driver);
		String data = GUtils.getFormattedNumber(1d, localeID,0, false);
		
		spreadsheetEditorPage.typeInCell("A1", "Data").typeInCell("A2", data)
				.typeInCell("A3", "Formula").typeInCell("B3", "Result");
		String expected = GUtils.getFormattedNumber(0.785398163d, localeID, 9,
				false);
	
		// 1.Verify cell reference.Sample 1
		
		log.info("Verify cell reference.");
		String input1 = "=" + GFormula.getInstance().translate("ATAN", localeID)
				+ "(A2)";
		spreadsheetEditorPage.typeInCell("A4", "'" + input1)
				.typeInCell("B4", input1).selectRange("B4");
		String actual1 = spreadsheetEditorPage.getCellText("B4");
		ga.assertEquals(expected, actual1);
		
		// Verify cell reference.Sample 2
		
		String expected11 = GUtils.getFormattedNumber(45d, localeID, 0,
				false);
		
		String input11 = "=" + GFormula.getInstance().translate("ATAN", localeID)
				+ "(A2)" + "*" + "180/PI()";
		spreadsheetEditorPage.typeInCell("A10", "'" + input11)
				.typeInCell("B10", input11).selectRange("B10");
		String actual11 = spreadsheetEditorPage.getCellText("B10");
		ga.assertEquals(expected11, actual11);
		
		// Verify cell reference.Sample 3
					
				String input111 = GFormula.parse("=DEGREES(ATAN(A2))", localeID);
				spreadsheetEditorPage.typeInCell("A11", "'" + input111)
						.typeInCell("B11", input111).selectRange("B11");
				String actual111 = spreadsheetEditorPage.getCellText("B11");
				ga.assertEquals(expected11, actual111);
		
		
		// 2.Verify sheet reference.
		log.info("Verify sheet reference.");
		spreadsheetEditorPage.selectSheet(2);
		spreadsheetEditorPage.typeInCell("B1", "Data").typeInCell("B2", data)
				.selectRange("B2");
		spreadsheetEditorPage.selectSheet(1);
		String input2 = "=" + GFormula.getInstance().translate("ATAN", localeID)
				+ "('" + spreadsheetEditorPage.getSheetName(2) + "'!B2)";
		spreadsheetEditorPage.typeInCell("A5", "'" + input2)
				.typeInCell("B5", input2).selectRange("B5");
		String actual2 = spreadsheetEditorPage.getCellText("B5");
		ga.assertEquals(expected, actual2);
		// 3.Verify renamed sheet reference.
		log.info("Verify renamed sheet reference.");
		String sheetName = GData.getInstance().getDataById(44);
		spreadsheetEditorPage.renameSheet(2, sheetName);
		spreadsheetEditorPage.selectSheet(1);
		String input3 = "=" + GFormula.getInstance().translate("ATAN", localeID)
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
		String input6 = "=" + GFormula.getInstance().translate("ATAN", localeID)
				+ "(namerange)";
		spreadsheetEditorPage.typeInCell("A7", "'" + input6)
				.typeInCell("B7", input6).selectRange("B7");
		String actual7 = spreadsheetEditorPage.getCellText("B7");
		ga.assertEquals(expected, actual7);
		
	}

}
