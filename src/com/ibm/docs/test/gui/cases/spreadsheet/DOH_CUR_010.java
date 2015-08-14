package com.ibm.docs.test.gui.cases.spreadsheet;

import static org.junit.Assert.*;

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
import com.ibm.icu.text.Normalizer;

/**
 * In global software, the currency values must be displayed in formats that are
 * preferred by the end users.
 * 
 * @author xinyutan@cn.ibm.com
 * 
 */
@GuideLink("https://labweb.torolab.ibm.com/gcoc/documents/gvtguide/testareas/DOH-CUR-010.htm")
public class DOH_CUR_010 extends BaseTest {

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
	public void atToolBarCurrency() {
		String fileName = Utils.uniqueBaseName("newSpreadsheetInFiles");
		spreadsheetEditorPage.focus(filesPage.newSpreadsheet(fileName));
		log.info("Verify DOH-CUR-010 at toolBar Currency");
		String localeID = GUtils
				.getBrowserPreferenceLan(spreadsheetEditorPage.driver);
		String expected = GUtils.getCurrencySymbol(localeID);
		String actual = Normalizer
				.normalize(spreadsheetEditorPage.toolBarCurrency.getText(),
						Normalizer.NFC);
		assertEquals("Verify currecny symbol displayed correctly in toolBar in"
				+ localeID, expected, actual);
	}

	@Test
	@GLan({ "pt-BR", "pt-PT", "bg-BG", "ca-ES", "cs-CZ", "da-DK", "nl-NL",
			"fi-FI", "fr-FR", "de-DE", "el-GR", "hu-HU", "it-IT", "ja-JP",
			"kk-KZ", "ko-KR", "nn-NO", "pl-PL", "ro-RO", "ru-RU", "zh-CN",
			"es-ES", "sv-SE", "th-TH", "zh-TW", "tr-TR", "hr-HR", "sk-SK" })
	public void atCell() {
		String fileName = Utils.uniqueBaseName("newSpreadsheetInFiles");
		spreadsheetEditorPage.focus(filesPage.newSpreadsheet(fileName));
		log.info("Verify DOH-CUR-010 at Cell A1");
		double number = 123456789.876;
		String localeID = GUtils
				.getBrowserPreferenceLan(spreadsheetEditorPage.driver);
		String input = GUtils.getFormattedNumber(number, localeID, 3, false);
		spreadsheetEditorPage.typeInCell("A1", input);
		spreadsheetEditorPage.formatCell2Currency("A1", localeID);
		String expected = GUtils.getFormattedCurrency(number, localeID, 2);
		String actual = Normalizer.normalize(
				spreadsheetEditorPage.getCellText("A1"), Normalizer.NFC);
		assertEquals("Verify currecny displayed correctly in " + localeID
				+ " in cell.", expected, actual);
	}

	@Test
	@GLan({ "pt-BR", "pt-PT", "bg-BG", "ca-ES", "cs-CZ", "da-DK", "nl-NL",
			"fi-FI", "fr-FR", "de-DE", "el-GR", "hu-HU", "it-IT", "ja-JP",
			"kk-KZ", "ko-KR", "nn-NO", "pl-PL", "ro-RO", "ru-RU", "zh-CN",
			"es-ES", "sv-SE", "th-TH", "zh-TW", "tr-TR", "hr-HR", "sk-SK",
			"sl-SL" })
	public void atCellByToolBar() {
		String fileName = Utils.uniqueBaseName("newSpreadsheetInFiles");
		spreadsheetEditorPage.focus(filesPage.newSpreadsheet(fileName));
		log.info("Verify DOH-CUR-010 at Cell A1");
		double number = 123456789.876;
		String localeID = GUtils
				.getBrowserPreferenceLan(spreadsheetEditorPage.driver);
		String input = GUtils.getFormattedNumber(number, localeID, 3, false);
		spreadsheetEditorPage.typeInCell("A1", input);
		spreadsheetEditorPage.setCurrency("A1");
		String expected = GUtils.getFormattedCurrency(number, localeID, 2);
		String actual = spreadsheetEditorPage.getCellText("A1");
		assertEquals("Verify currecny displayed correctly in " + localeID
				+ " in cell.", expected, actual);
	}

	@Test
	@GLan({ "pt-BR", "pt-PT", "bg-BG", "ca-ES", "cs-CZ", "da-DK", "nl-NL",
			"fi-FI", "fr-FR", "de-DE", "el-GR", "hu-HU", "it-IT", "ja-JP",
			"kk-KZ", "ko-KR", "nn-NO", "pl-PL", "ro-RO", "ru-RU", "zh-CN",
			"es-ES", "sv-SE", "th-TH", "zh-TW", "tr-TR", "hr-HR", "sk-SK",
			"sl-SL" })
	public void atToolBarNumberIconMenu() {
		String fileName = Utils.uniqueBaseName("newSpreadsheetInFiles");
		spreadsheetEditorPage.focus(filesPage.newSpreadsheet(fileName));
		log.info("Verify DOH-CUR-010 at toolBar menu");
		double number = 1234;
		String localeID = GUtils
				.getBrowserPreferenceLan(spreadsheetEditorPage.driver);
		String expected1 = GUtils.getFormattedCurrency(number, localeID, 0);
		String expected2 = GUtils.getFormattedCurrency(number, localeID, 2);
		spreadsheetEditorPage.toolBarNumberIcon.click();
		String actual1 = spreadsheetEditorPage.toolBarCurrencyFormat1.getText();
		String actual2 = spreadsheetEditorPage.toolBarCurrencyFormat2.getText();
		ga.assertEquals("Verify " + localeID + " currency " + expected1
				+ " in toolBar menu item1", expected1, actual1);
		ga.assertEquals("Verify " + localeID + " currency " + expected2
				+ " in toolBar menu item2", expected2, actual2);
	}

}
