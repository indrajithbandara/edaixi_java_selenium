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


public class CONVERT extends BaseTest{

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
		String input1 = GFormula.parse("=CONVERT(1,\"lbm\",\"kg\")", localeID);
		spreadsheetEditorPage.typeInCell("A2", "'" + input1)
				.typeInCell("B2", input1).selectRange("B2");
		String expected1 = GUtils.getFormattedNumber(0.45359231d, localeID, 8,
				false);
		String actual1 = spreadsheetEditorPage.getCellText("B2");
		ga.assertEquals(expected1, actual1);
		
		//Sample 2
				String input2 = GFormula.parse("=CONVERT(68,\"F\",\"C\")", localeID);
				spreadsheetEditorPage.typeInCell("A3", "'" + input2)
						.typeInCell("B3", input2).selectRange("B3");
				String expected2 = GUtils.getFormattedNumber(20d, localeID, 0,
						false);
				String actual2 = spreadsheetEditorPage.getCellText("B3");
				ga.assertEquals(expected2, actual2);
		
		//Sample 3
				String input3 = GFormula.parse("=CONVERT(CONVERT(100,\"ft\",\"m\"),\"ft\",\"m\")", localeID);
				spreadsheetEditorPage.typeInCell("A4", "'" + input3)
						.typeInCell("B4", input3).selectRange("B4");
				String expected3 = GUtils.getFormattedNumber(9.290304d, localeID, 6,
						false);
				String actual3 = spreadsheetEditorPage.getCellText("B4");
				ga.assertEquals(expected3, actual3);	
			
	}
	
	@Test
	@GLan("de-DE")
	//@GLan({ "pt-BR", "pt-PT", "bg-BG", "ca-ES", "cs-CZ", "da-DK", "nl-NL",
			//"fi-FI", "fr-FR", "de-DE", "el-GR", "hu-HU", "it-IT", "ja-JP",
			//"kk-KZ", "ko-KR", "nn-NO", "pl-PL", "ro-RO", "ru-RU", "zh-CN",
			//"es-ES", "sv-SE", "th-TH", "zh-TW", "tr-TR", "hr-HR", "sk-SK",
			//"sl-SL" })
	public void atReference() {
		String localeID = GUtils
				.getBrowserPreferenceLan(spreadsheetEditorPage.driver);
		String data = GUtils.getFormattedNumber(8d, localeID,0, false);
		String data1 = GUtils.getFormattedNumber(2d, localeID,0, false);
		
		spreadsheetEditorPage.typeInCell("A1", "Data").typeInCell("A2", data)
				.typeInCell("B1", "Data").typeInCell("B2", data1)
				.typeInCell("A3", "Formula").typeInCell("B3", "Result");
		String expected = GUtils.getFormattedNumber(28d, localeID, 0,
				false);
	
		// 1.Verify cell reference.Sample 1
		
		log.info("Verify cell reference.");
		String input1 = GFormula.parse("=COMBIN(A2,B2)", localeID);
		spreadsheetEditorPage.typeInCell("A4", "'" + input1)
				.typeInCell("B4", input1).selectRange("B4");
		String actual1 = spreadsheetEditorPage.getCellText("B4");
		ga.assertEquals(expected, actual1);
		
		// 2.Verify sheet reference.
		log.info("Verify sheet reference.");
		spreadsheetEditorPage.selectSheet(2);
		String sheetname = spreadsheetEditorPage.getSheetName(2);
		spreadsheetEditorPage.typeInCell("B1", "Data").typeInCell("B2", data)
				.selectRange("B2");
		spreadsheetEditorPage.typeInCell("A1", "Data").typeInCell("A2", data1)
		.selectRange("B2");
		
		spreadsheetEditorPage.selectSheet(1);
		String input2 = GFormula.parse("=COMBIN(" + sheetname + "!B2,"+ sheetname +"!A2)", localeID);
		spreadsheetEditorPage.typeInCell("A5", "'" + input2)
				.typeInCell("B5", input2).selectRange("B5");
		String actual2 = spreadsheetEditorPage.getCellText("B5");
		ga.assertEquals(expected, actual2);
		
		
		// 3.Verify renamed sheet reference.
		log.info("Verify renamed sheet reference.");
		String sheetNameNew = GData.getInstance().getDataById(44);
		spreadsheetEditorPage.renameSheet(2, sheetNameNew);
		spreadsheetEditorPage.selectSheet(1);
		String input3 = GFormula.parse("=COMBIN(" + sheetNameNew + "!B2,"+ sheetNameNew +"!A2)", localeID);
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
		String namerange = GData.getInstance().getDataById(44);
		spreadsheetEditorPage.newNamedRange(namerange);
		
		spreadsheetEditorPage.selectRange("B2");
		String namerange1 = GData.getInstance().getDataById(45);
		spreadsheetEditorPage.newNamedRange(namerange1);
		
		String input6 = GFormula.parse("=COMBIN("+namerange+","+namerange1+")", localeID);
		spreadsheetEditorPage.typeInCell("A7", "'" + input6)
				.typeInCell("B7", input6).selectRange("B7");
		String actual7 = spreadsheetEditorPage.getCellText("B7");
		ga.assertEquals(expected, actual7);
		
	}
	
	
}
