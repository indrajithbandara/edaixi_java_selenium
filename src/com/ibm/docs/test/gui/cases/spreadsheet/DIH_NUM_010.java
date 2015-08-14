package com.ibm.docs.test.gui.cases.spreadsheet;

import static org.junit.Assert.*;

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
 * Ensure that input fields that accept numbers as text correctly interpret the
 * values according to the user's cultural preferences.
 * 
 * @author xinyutan@cn.ibm.com
 * 
 */
@GuideLink("https://labweb.torolab.ibm.com/gcoc/documents/gvtguide/testareas/DIH-NUM-010.htm")
public class DIH_NUM_010 extends BaseTest {

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
	@TestDoc("defect 42727 - Number could not be parsed correctly for some languages.")
	@GLan({ "pt-BR", "pt-PT", "bg-BG", "ca-ES", "cs-CZ", "da-DK", "nl-NL",
			"fi-FI", "fr-FR", "de-DE", "el-GR", "hu-HU", "it-IT", "ja-JP",
			"kk-KZ", "ko-KR", "nn-NO", "pl-PL", "ro-RO", "ru-RU", "zh-CN",
			"es-ES", "sv-SE", "th-TH", "zh-TW", "tr-TR", "hr-HR", "sk-SK",
			"sl-SL" })
	public void atCell() {
		String fileName = Utils.uniqueBaseName("newSpreadsheetInFiles");
		spreadsheetEditorPage.focus(filesPage.newSpreadsheet(fileName));
		log.info("Verify DIH-NUM-010 at Cell A1");
		double number = 123456789.876;
		String localeID = GUtils
				.getBrowserPreferenceLan(spreadsheetEditorPage.driver);
		String input = GUtils.getFormattedNumber(number, localeID, 3, true);
		spreadsheetEditorPage.typeInCell("A1", input);
		String expected = GUtils.getFormattedNumber(number, localeID, 3, false);
		String actual = spreadsheetEditorPage.getCellValue("A1");
		assertEquals("Verify number " + input + " could be acceptted in "
				+ localeID, expected, actual);
	}

}
