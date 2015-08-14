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

/**
 * In global software, the currency values must be displayed in formats that are
 * preferred by the end users.
 * 
 * @author shiyac@cn.ibm.com
 * 
 */
@GuideLink("https://labweb.torolab.ibm.com/gcoc/documents/gvtguide/testareas/DOH-CUR-030.htm")
public class DOH_CUR_030 extends BaseTest {

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
	public void atCell() {
		String fileName = Utils.uniqueBaseName("newSpreadsheetInFiles");
		spreadsheetEditorPage.focus(filesPage.newSpreadsheet(fileName));
		log.info("Verify DOH-CUR-030 at Cell A1");
		String cell = "A1";
		double number = 1234567.89;
		String localeID = GUtils
				.getBrowserPreferenceLan(spreadsheetEditorPage.driver);
		String input = GUtils.getFormattedNumber(number, localeID, 2, false);
		spreadsheetEditorPage.typeInCell(cell, input);
		String currencySymbol = spreadsheetEditorPage.toolBarCurrencyLabel
				.getText();
		spreadsheetEditorPage.selectRange(cell);
		spreadsheetEditorPage.toolBarCurrency.click();		
		spreadsheetEditorPage.SetDocumentLocalebyShiYaChen();
		String Reslut = spreadsheetEditorPage.getCellText(cell);
		boolean isCorrect = false;
		if (Reslut.indexOf(currencySymbol) != -1) {
			isCorrect = true;
		}
		assertTrue(
				"Regional setting should not change the currency of existing data or data created by other users. ",
				isCorrect);
	}
}
