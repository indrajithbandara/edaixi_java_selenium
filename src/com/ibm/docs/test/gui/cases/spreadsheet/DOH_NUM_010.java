package com.ibm.docs.test.gui.cases.spreadsheet;

import org.junit.Before;
import org.junit.Test;

import com.ibm.docs.test.common.GUtils;
import com.ibm.docs.test.common.GuideLink;
import com.ibm.docs.test.common.Utils;
import com.ibm.docs.test.gui.cases.BaseTest;
import com.ibm.docs.test.gui.pages.FilesPage;
import com.ibm.docs.test.gui.pages.LoginPage;
import com.ibm.docs.test.gui.pages.SpreadsheetEditorPage;
import com.ibm.docs.test.gui.support.Drive;
import com.ibm.docs.test.gui.support.GLan;

/**
 * Ensure that the software formats numeric information in a locale sensitive
 * manner, for integers, percentages, decimals and scientific notation.
 * 
 * @author xinyutan@cn.ibm.com
 * 
 */
@GuideLink("https://labweb.torolab.ibm.com/gcoc/documents/gvtguide/testareas/DOH-NUM-010.htm")
public class DOH_NUM_010 extends BaseTest {

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
	@GLan({ "pt-BR", "pt-PT", "bg-BG", "ca-ES", "cs-CZ", "da-DK", "nl-NL",
			"fi-FI", "fr-FR", "de-DE", "el-GR", "hu-HU", "it-IT", "ja-JP",
			"kk-KZ", "ko-KR", "nn-NO", "pl-PL", "ro-RO", "ru-RU", "zh-CN",
			"es-ES", "sv-SE", "th-TH", "zh-TW", "tr-TR", "hr-HR", "sk-SK",
			"sl-SL" })
	public void atToolBarNumberIconMenuNumber() {
		String fileName = Utils.uniqueBaseName("newSpreadsheetInFiles");
		spreadsheetEditorPage.focus(filesPage.newSpreadsheet(fileName));
		log.info("Verify DOH-CUR-010 at toolBar menu Number.");
		double number = 1234;
		String localeID = GUtils
				.getBrowserPreferenceLan(spreadsheetEditorPage.driver);
		String expected1 = GUtils.getFormattedNumber(number, localeID, 0, true);
		String expected2 = GUtils.getFormattedNumber(number, localeID, 2, true);
		spreadsheetEditorPage.toolBarNumberIcon.click();
		String actual1 = spreadsheetEditorPage.toolBarNumberFormat0.getText();
		String actual2 = spreadsheetEditorPage.toolBarNumberFormat2.getText();
		ga.assertEquals("Verify " + localeID + " Number " + expected1
				+ " in toolBar menu item1", expected1, actual1);
		ga.assertEquals("Verify " + localeID + " Number " + expected2
				+ " in toolBar menu item2", expected2, actual2);
	}

	@Test
	@GLan({ "pt-BR", "pt-PT", "bg-BG", "ca-ES", "cs-CZ", "da-DK", "nl-NL",
			"fi-FI", "fr-FR", "de-DE", "el-GR", "hu-HU", "it-IT", "ja-JP",
			"kk-KZ", "ko-KR", "nn-NO", "pl-PL", "ro-RO", "ru-RU", "zh-CN",
			"es-ES", "sv-SE", "th-TH", "zh-TW", "tr-TR", "hr-HR", "sk-SK",
			"sl-SL" })
	public void atToolBarNumberIconMenuPercent() {
		String fileName = Utils.uniqueBaseName("newSpreadsheetInFiles");
		spreadsheetEditorPage.focus(filesPage.newSpreadsheet(fileName));
		log.info("Verify DOH-CUR-010 at toolBar menu Percent.");
		double number = 0.1234;
		String localeID = GUtils
				.getBrowserPreferenceLan(spreadsheetEditorPage.driver);
		String expected1 = GUtils.getFormattedPercent(number, localeID, 0,
				false);
		String expected2 = GUtils.getFormattedPercent(number, localeID, 2,
				false);
		spreadsheetEditorPage.toolBarNumberIcon.click();
		String actual1 = spreadsheetEditorPage.toolBarPercentFormat0.getText();
		String actual2 = spreadsheetEditorPage.toolBarPercentFormat2.getText();
		ga.assertEquals("Verify " + localeID + " Percent " + expected1
				+ " in toolBar menu item1", expected1, actual1);
		ga.assertEquals("Verify " + localeID + " Percent " + expected2
				+ " in toolBar menu item2", expected2, actual2);
	}

	@Test
	@GLan({ "pt-BR", "pt-PT", "bg-BG", "ca-ES", "cs-CZ", "da-DK", "nl-NL",
			"fi-FI", "fr-FR", "de-DE", "el-GR", "hu-HU", "it-IT", "ja-JP",
			"kk-KZ", "ko-KR", "nn-NO", "pl-PL", "ro-RO", "ru-RU", "zh-CN",
			"es-ES", "sv-SE", "th-TH", "zh-TW", "tr-TR", "hr-HR", "sk-SK",
			"sl-SL" })
	public void atFormatMenuNumber() {
		String fileName = Utils.uniqueBaseName("newSpreadsheetInFiles");
		spreadsheetEditorPage.focus(filesPage.newSpreadsheet(fileName));
		log.info("Verify DOH-CUR-010 at Format menu Number.");
		double number = 1234;
		String localeID = GUtils
				.getBrowserPreferenceLan(spreadsheetEditorPage.driver);
		String expected1 = GUtils.getFormattedNumber(number, localeID, 0, true);
		String expected2 = GUtils.getFormattedNumber(number, localeID, 2, true);
		spreadsheetEditorPage.menuFormat.click();
		spreadsheetEditorPage.menuShowNumber.click();
		String actual1 = spreadsheetEditorPage.menuNumberFormat0.getText();
		String actual2 = spreadsheetEditorPage.menuNumberFormat2.getText();
		ga.assertEquals("Verify " + localeID + " Number " + expected1
				+ " in Format menu item1", expected1, actual1);
		ga.assertEquals("Verify " + localeID + " Number " + expected2
				+ " in Format menu item2", expected2, actual2);
	}

	@Test
	@GLan({ "pt-BR", "pt-PT", "bg-BG", "ca-ES", "cs-CZ", "da-DK", "nl-NL",
			"fi-FI", "fr-FR", "de-DE", "el-GR", "hu-HU", "it-IT", "ja-JP",
			"kk-KZ", "ko-KR", "nn-NO", "pl-PL", "ro-RO", "ru-RU", "zh-CN",
			"es-ES", "sv-SE", "th-TH", "zh-TW", "tr-TR", "hr-HR", "sk-SK",
			"sl-SL" })
	public void atFormatMenuPercent() {
		String fileName = Utils.uniqueBaseName("newSpreadsheetInFiles");
		spreadsheetEditorPage.focus(filesPage.newSpreadsheet(fileName));
		log.info("Verify DOH-CUR-010 at Format menu Percent.");
		double number = 0.1234;
		String localeID = GUtils
				.getBrowserPreferenceLan(spreadsheetEditorPage.driver);
		String expected1 = GUtils.getFormattedPercent(number, localeID, 0,
				false);
		String expected2 = GUtils.getFormattedPercent(number, localeID, 2,
				false);
		spreadsheetEditorPage.menuFormat.click();
		spreadsheetEditorPage.menuShowNumber.click();
		String actual1 = spreadsheetEditorPage.menuPercentFormat0.getText();
		String actual2 = spreadsheetEditorPage.menuPercentFormat2.getText();
		ga.assertEquals("Verify " + localeID + " Percent " + expected1
				+ " in Format menu item1", expected1, actual1);
		ga.assertEquals("Verify " + localeID + " Percent " + expected2
				+ " in Format menu item2", expected2, actual2);
	}

	@Test
	@GLan({ "pt-BR", "pt-PT", "bg-BG", "ca-ES", "cs-CZ", "da-DK", "nl-NL",
			"fi-FI", "fr-FR", "de-DE", "el-GR", "hu-HU", "it-IT", "ja-JP",
			"kk-KZ", "ko-KR", "nn-NO", "pl-PL", "ro-RO", "ru-RU", "zh-CN",
			"es-ES", "sv-SE", "th-TH", "zh-TW", "tr-TR", "hr-HR", "sk-SK",
			"sl-SL" })
	public void atCellNumberFormatting() {
		String fileName = Utils.uniqueBaseName("newSpreadsheetInFiles");
		spreadsheetEditorPage.focus(filesPage.newSpreadsheet(fileName));
		log.info("Verify DOH-CUR-010 Number Formatting at Cell A1.");
		double number = 123456.789;
		String localeID = GUtils
				.getBrowserPreferenceLan(spreadsheetEditorPage.driver);
		String input = GUtils.getFormattedNumber(number, localeID, 3, false);
		String expected1 = GUtils.getFormattedNumber(number, localeID, 0, true);
		String expected2 = GUtils.getFormattedNumber(number, localeID, 2, true);
		spreadsheetEditorPage.driver.sleep(1);
		spreadsheetEditorPage.typeInCell("A1", input);
		spreadsheetEditorPage.menuFormat.click();
		spreadsheetEditorPage.menuShowNumber.click();
		spreadsheetEditorPage.menuNumberFormat0.click();
		spreadsheetEditorPage.typeInCell("A2", input);
		spreadsheetEditorPage.menuFormat.click();
		spreadsheetEditorPage.menuShowNumber.click();
		spreadsheetEditorPage.menuNumberFormat2.click();
		spreadsheetEditorPage.driver.sleep(1);
		String actual1 = spreadsheetEditorPage.getCellText("A1");
		String actual2 = spreadsheetEditorPage.getCellText("A2");
		ga.assertEquals("Verify " + localeID + " Number " + expected1
				+ " at Cell A1.", expected1, actual1);
		ga.assertEquals("Verify " + localeID + " Number " + expected2
				+ " at Cell A2.", expected2, actual2);
	}

	@Test
	@GLan({ "pt-BR", "pt-PT", "bg-BG", "ca-ES", "cs-CZ", "da-DK", "nl-NL",
			"fi-FI", "fr-FR", "de-DE", "el-GR", "hu-HU", "it-IT", "ja-JP",
			"kk-KZ", "ko-KR", "nn-NO", "pl-PL", "ro-RO", "ru-RU", "zh-CN",
			"es-ES", "sv-SE", "th-TH", "zh-TW", "tr-TR", "hr-HR", "sk-SK",
			"sl-SL" })
	public void atCellPercentFormatting() {
		String fileName = Utils.uniqueBaseName("newSpreadsheetInFiles");
		spreadsheetEditorPage.focus(filesPage.newSpreadsheet(fileName));
		log.info("Verify DOH-CUR-010 Percent Formatting at Cell A1.");
		double number = 123456.789;
		String localeID = GUtils
				.getBrowserPreferenceLan(spreadsheetEditorPage.driver);
		String input = GUtils.getFormattedNumber(number, localeID, 3, false);
		String expected1 = GUtils.getFormattedPercent(number, localeID, 0,
				false);
		String expected2 = GUtils.getFormattedPercent(number, localeID, 2,
				false);
		spreadsheetEditorPage.driver.sleep(1);
		spreadsheetEditorPage.typeInCell("A1", input);
		spreadsheetEditorPage.menuFormat.click();
		spreadsheetEditorPage.menuShowNumber.click();
		spreadsheetEditorPage.menuPercentFormat0.click();
		spreadsheetEditorPage.typeInCell("A2", input);
		spreadsheetEditorPage.menuFormat.click();
		spreadsheetEditorPage.menuShowNumber.click();
		spreadsheetEditorPage.menuPercentFormat2.click();
		spreadsheetEditorPage.driver.sleep(1);
		String actual1 = spreadsheetEditorPage.getCellText("A1");
		String actual2 = spreadsheetEditorPage.getCellText("A2");
		ga.assertEquals("Verify " + localeID + " Percent " + expected1
				+ " at Cell A1.", expected1, actual1);
		ga.assertEquals("Verify " + localeID + " Percent " + expected2
				+ " at Cell A2.", expected2, actual2);
	}
}
