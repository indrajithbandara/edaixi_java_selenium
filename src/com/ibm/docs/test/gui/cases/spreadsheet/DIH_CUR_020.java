/**
 * 
 */
package com.ibm.docs.test.gui.cases.spreadsheet;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.ibm.docs.test.common.GUtils;
import com.ibm.docs.test.common.GuideLink;
import com.ibm.docs.test.common.TestDoc;
import com.ibm.docs.test.common.Utils;
import com.ibm.docs.test.gui.cases.BaseTest;
import com.ibm.docs.test.gui.pages.FilesPage;
import com.ibm.docs.test.gui.pages.LoginPage;
import com.ibm.docs.test.gui.pages.SpreadsheetEditorPage;
import com.ibm.docs.test.gui.support.Drive;
import com.ibm.docs.test.gui.support.GLan;

/**
 * 
 * Test to ensure that all valid formats for accepted currencies are accepted, 
 * including: decimal separator, digit grouping separator, negative indication, 
 * all decimal numerals (e.g. Indic digits), leading/trailing zeros, 
 * and language-specific currency symbols and positions (relative to the number)
 * 
 * @author lidongyu@cn.ibm.com
 *
 */
@GuideLink("https://labweb.torolab.ibm.com/gcoc/documents/gvtguide/testareas/DIH-CUR-020.htm")
public class DIH_CUR_020 extends BaseTest {

	/**
	 * 
	 */
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
	}
	
	@Test
	@TestDoc("42942: [GVT][Regression][FR]Monetary values cannot be parsed correctly in the spreadsheet.")
	@GLan({ "pt-BR", "pt-PT", "bg-BG", "ca-ES", "cs-CZ", "da-DK", "nl-NL",
		"fi-FI", "fr-FR", "de-DE", "el-GR", "hu-HU", "it-IT", "ja-JP",
		"kk-KZ", "ko-KR", "nn-NO", "pl-PL", "ro-RO", "ru-RU", "zh-CN",
		"es-ES", "sv-SE", "th-TH", "zh-TW", "tr-TR", "hr-HR", "sk-SK",
		"sl-SL" })
	public void atCell(){
		String fileName = Utils.uniqueBaseName("atCell");
		spreadsheetEditorPage.focus(filesPage.newSpreadsheet(fileName));
		log.info("Verify DIH-CUR-020 at Cell");
		double positiveNumber = 123456789.46;
		double negativeNumber = -123456789.46;
		String localeID=GUtils.getBrowserPreferenceLan(spreadsheetEditorPage.driver);
		String inputPositiveNumber= GUtils.getFormattedCurrency(positiveNumber, localeID, 2);
		String inputNegativeNumber=GUtils.getFormattedCurrency(negativeNumber, localeID, 2);
		spreadsheetEditorPage.typeInCell("A1", inputPositiveNumber);
		spreadsheetEditorPage.typeInCell("A2", inputNegativeNumber);
		String expected1 = GUtils.getFormattedNumber(positiveNumber, localeID, 2, false);
		String expected2 = GUtils.getFormattedNumber(negativeNumber, localeID, 2, false);
		String actual1 = GUtils.trimNBSP(spreadsheetEditorPage
				.getCellValue("A1"));
		String actual2 = GUtils.trimNBSP(spreadsheetEditorPage.getCellValue("A2"));
   	    ga.assertEquals("Verify currency displayed correctly in " + localeID
				+ " in cell.", expected1, actual1);
   	    ga.assertEquals("Verify currency displayed correctly in " + localeID
				+ " in cell.", expected2, actual2);
	}
	@GLan({ "pt-BR", "pt-PT", "bg-BG", "ca-ES", "cs-CZ", "da-DK", "nl-NL",
		"fi-FI", "fr-FR", "de-DE", "el-GR", "hu-HU", "it-IT", "ja-JP",
		"kk-KZ", "ko-KR", "nn-NO", "pl-PL", "ro-RO", "ru-RU", "zh-CN",
		"es-ES", "sv-SE", "th-TH", "zh-TW", "tr-TR", "hr-HR", "sk-SK",
		"sl-SL" })
	@TestDoc("42942: [GVT][Regression][FR]Monetary values cannot be parsed correctly in the spreadsheet.")
	@Test
	public void atFormulaInputLine() {
		String fileName = Utils.uniqueBaseName("newSpreadsheetInFiles");
		spreadsheetEditorPage.focus(filesPage.newSpreadsheet(fileName));
		log.info("Verify DIH-CUR-020 at formulaInputLine");
		double number=12345678.46;
		String localeID=GUtils.getBrowserPreferenceLan(spreadsheetEditorPage.driver);
		String input=GUtils.getFormattedCurrency(number, localeID, 2);
		spreadsheetEditorPage.selectRange("A1");
		spreadsheetEditorPage.formulaInputLine.sendKeys(input);
		String expected = GUtils.getFormattedNumber(number, localeID, 2, false);
		String actual=GUtils.trimNBSP(spreadsheetEditorPage.getCellValue("A1"));
	    assertEquals("Verify currency displayed correctly in " + localeID
				+ " in cell.", expected, actual);
		}
	}
