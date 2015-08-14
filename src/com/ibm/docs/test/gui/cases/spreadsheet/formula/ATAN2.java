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

public class ATAN2 extends BaseTest{
	
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
	@GLan("zh-CN")
	//@GLan({ "pt-BR", "pt-PT", "bg-BG", "ca-ES", "cs-CZ", "da-DK", "nl-NL",
		//"fi-FI", "fr-FR", "de-DE", "el-GR", "hu-HU", "it-IT", "ja-JP",
		//"kk-KZ", "ko-KR", "nn-NO", "pl-PL", "ro-RO", "ru-RU", "zh-CN",
		//"es-ES", "sv-SE", "th-TH", "zh-TW", "tr-TR", "hr-HR", "sk-SK",
		//"sl-SL" })
	public void atGeneralIO() {
		spreadsheetEditorPage.typeInCell("A1", "Formula").typeInCell("B1",
				"Result");
		String localeID = GUtils
				.getBrowserPreferenceLan(spreadsheetEditorPage.driver);
		//Sample 1
		String input1 = GFormula.parse("=ATAN2(1,1)", localeID);
		spreadsheetEditorPage.typeInCell("A2", "'" + input1)
				.typeInCell("B2", input1).selectRange("B2");
		String expected1 = GUtils.getFormattedNumber(0.785398163d, localeID, 9,
				false);
		String actual1 = spreadsheetEditorPage.getCellText("B2");
		ga.assertEquals(expected1, actual1);
		
		//Sample 2
		String input2 = GFormula.parse("=ATAN2(-1,-1)", localeID);
		spreadsheetEditorPage.typeInCell("A3", "'" + input2)
				.typeInCell("B3", input2).selectRange("B3");
		String expected2 = GUtils.getFormattedNumber(-2.35619449d, localeID, 8,
				false);
		String actual2 = spreadsheetEditorPage.getCellText("B3");
		ga.assertEquals(expected2, actual2);
		
		
		//Sample 3
		String inputf = GFormula.parse("=ATAN2(-1,-1)", localeID);
		String input3 = inputf + "*180/PI()";
		spreadsheetEditorPage.typeInCell("A4", "'" + input3)
				.typeInCell("B4", input3).selectRange("B4");
		String expected3 = GUtils.getFormattedNumber(-135d, localeID, 0,
				false);
		String actual3 = spreadsheetEditorPage.getCellText("B4");
		ga.assertEquals(expected3, actual3);
		
		//Sample 4
		String input4 = GFormula.parse("=DEGREES(ATAN2(-1,-1))", localeID);
		spreadsheetEditorPage.typeInCell("A5", "'" + input4)
				.typeInCell("B5", input4).selectRange("B5");
		String expected4 = GUtils.getFormattedNumber(-135d, localeID, 0,
				false);
		String actual4 = spreadsheetEditorPage.getCellText("B5");
		ga.assertEquals(expected4, actual4);
		
		
	}

	@Test
	@GLan("fi-FI")
	//@GLan({ "pt-BR", "pt-PT", "bg-BG", "ca-ES", "cs-CZ", "da-DK", "nl-NL",
		//"fi-FI", "fr-FR", "de-DE", "el-GR", "hu-HU", "it-IT", "ja-JP",
		////"kk-KZ", "ko-KR", "nn-NO", "pl-PL", "ro-RO", "ru-RU", "zh-CN",
		//"es-ES", "sv-SE", "th-TH", "zh-TW", "tr-TR", "hr-HR", "sk-SK",
		//"sl-SL" })
	
	public void atReference() {
		String localeID = GUtils
				.getBrowserPreferenceLan(spreadsheetEditorPage.driver);
		String data = GUtils.getFormattedNumber(1d, localeID,0, false);
		String data2 = GUtils.getFormattedNumber(-1d, localeID,0, false);
		
		spreadsheetEditorPage.typeInCell("A1", "Data").typeInCell("A2", data)
		.typeInCell("B2", data).typeInCell("C2", data2).typeInCell("D2", data2)
		.typeInCell("A3", "Formula").typeInCell("B3", "Result");
		String expected = GUtils.getFormattedNumber(0.785398163d, localeID, 9,
				false);
		String expected3 = GUtils.getFormattedNumber(-135d, localeID, 0,
				false);
		
		// 1.Verify cell reference.Sample 1
		log.info("Verify cell reference.");
		String input1s1 = GFormula.parse("=ATAN2(A2,B2)", localeID);
		spreadsheetEditorPage.typeInCell("A4", "'" + input1s1)
				.typeInCell("B4", input1s1).selectRange("B4");
		String actual1s1 = spreadsheetEditorPage.getCellText("B4");
		ga.assertEquals(expected, actual1s1);
		
		// Verify cell reference.Sample 2
		String expected1s2 = GUtils.getFormattedNumber(-2.35619449d, localeID, 8,
				false);
		String input1s2 = GFormula.parse("=ATAN2(C2,D2)", localeID);
		spreadsheetEditorPage.typeInCell("A5", "'" + input1s2)
				.typeInCell("B5", input1s2).selectRange("B5");
		String actual1s2 = spreadsheetEditorPage.getCellText("B5");
		ga.assertEquals(expected1s2, actual1s2);
		
		// Verify cell reference.Sample 3
		String expected1s3 = GUtils.getFormattedNumber(-135d, localeID, 0,
				false);
		 
		 String inputf = GFormula.parse("=ATAN2(C2,D2)", localeID);
		 String input1s3 = inputf + "*180/PI()";
		 spreadsheetEditorPage.typeInCell("A6", "'" + input1s3)
			.typeInCell("B6", input1s3).selectRange("B6");
	     String actual1s3 = spreadsheetEditorPage.getCellText("B6");
	     ga.assertEquals(expected1s3, actual1s3);
			
	  // Verify cell reference.Sample 4
	     String input1s4 = GFormula.parse("=DEGREES(ATAN2(C2,D2))", localeID);
	     spreadsheetEditorPage.typeInCell("A7", "'" + input1s4)
			.typeInCell("B7", input1s4).selectRange("B7");
	     String actual1s4 = spreadsheetEditorPage.getCellText("B7");
	     ga.assertEquals(expected1s3, actual1s4);
	   
		// 2.Verify sheet reference.Sample 1
		log.info("Verify sheet reference.");
		spreadsheetEditorPage.selectSheet(2);
		spreadsheetEditorPage.typeInCell("B1", "Data").typeInCell("B2", data)
				.selectRange("B2");
		spreadsheetEditorPage.selectSheet(1);
		String sheetname = spreadsheetEditorPage.getSheetName(2);
		
		String input2 = GFormula.parse("=ATAN2(" + sheetname + "!B2,B2)", localeID);
		spreadsheetEditorPage.typeInCell("A8", "'" + input2)
				.typeInCell("B8", input2).selectRange("B8");
		String actual2 = spreadsheetEditorPage.getCellText("B8");
		ga.assertEquals(expected, actual2);
		
		// 2.Verify sheet reference.Sample 2
		spreadsheetEditorPage.selectSheet(2);
		spreadsheetEditorPage.typeInCell("A1", "Data").typeInCell("A2", data2)
				.selectRange("A2");
		spreadsheetEditorPage.selectSheet(1);
		String sheetname2 = spreadsheetEditorPage.getSheetName(2);
		String input2s2 = GFormula.parse("=DEGREES(ATAN2(" + sheetname2 + "!A2,C2))", localeID);
		spreadsheetEditorPage.typeInCell("A9", "'" + input2s2)
				.typeInCell("B9", input2s2).selectRange("B9");
		String actual2s2 = spreadsheetEditorPage.getCellText("B9");
		ga.assertEquals(expected3, actual2s2);
				
		// 3.Verify renamed sheet reference.
		log.info("Verify renamed sheet reference.");
		String sheetNameNew = GData.getInstance().getDataById(44);
		spreadsheetEditorPage.renameSheet(2, sheetNameNew);
		spreadsheetEditorPage.selectSheet(1);
		String input3 = GFormula.parse("=ATAN2(" + sheetNameNew + "!B2,B2)", localeID);
		spreadsheetEditorPage.typeInCell("A10", "'" + input3)
				.typeInCell("B10", input3).selectRange("B10");
		String actual3 = spreadsheetEditorPage.getCellText("B10");
		ga.assertEquals(expected, actual3);
		// 4.Verify sheet reference after insert Row/Column.
		log.info("Verify sheet reference after insert Row/Column.");
		spreadsheetEditorPage.selectSheet(2);
		spreadsheetEditorPage.selectRange("A1");
		spreadsheetEditorPage.insertRowAbove();
		spreadsheetEditorPage.insertColumnBefore();
		spreadsheetEditorPage.selectSheet(1);
		String actual4 = spreadsheetEditorPage.getCellText("B10");
		ga.assertEquals(expected, actual4);
		// 5.Verify sheet reference after delete Row/Column.
		log.info("Verify sheet reference after delete Row/Column.");
		log.info("Verify sheet reference after insert.");
		spreadsheetEditorPage.selectSheet(2);
		spreadsheetEditorPage.selectRange("A1");
		spreadsheetEditorPage.deleteRow();
		spreadsheetEditorPage.deleteColumn();
		spreadsheetEditorPage.selectSheet(1);
		String actual5 = spreadsheetEditorPage.getCellText("B10");
		ga.assertEquals(expected, actual5);
		
		//6.Verify Name Ranged
				log.info("Verify Name Ranged.");
				spreadsheetEditorPage.selectRange("A2");
				spreadsheetEditorPage.newNamedRange("namerange");
				//String input6 = "=" + GFormula.getInstance().translate("ATAN2", localeID)
						//+ "(namerange)";
				
				String input6 = GFormula.parse("=ATAN2(namerange,B2)", localeID);
				spreadsheetEditorPage.typeInCell("A11", "'" + input6)
						.typeInCell("B11", input6).selectRange("B11");
				String actual6 = spreadsheetEditorPage.getCellText("B11");
				ga.assertEquals(expected, actual6);
		
		
	}

}
