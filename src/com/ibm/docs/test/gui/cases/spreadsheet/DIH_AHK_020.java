package com.ibm.docs.test.gui.cases.spreadsheet;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ibm.docs.test.common.GData;
import com.ibm.docs.test.common.GuideLink;
import com.ibm.docs.test.common.Utils;
import com.ibm.docs.test.gui.cases.BaseTest;
import com.ibm.docs.test.gui.pages.FilesPage;
import com.ibm.docs.test.gui.pages.LoginPage;
import com.ibm.docs.test.gui.pages.SpreadsheetEditorPage;
import com.ibm.docs.test.gui.support.Drive;
import com.ibm.docs.test.gui.support.GLan;

/**
 * Ensure that accelerator and hot keys work independently of the UI
 * translation.
 * 
 * @author xinyutan@cn.ibm.com
 * 
 */
@GuideLink("https://labweb.torolab.ibm.com/gcoc/documents/gvtguide/testareas/DIH-AHK-020.htm")
public class DIH_AHK_020 extends BaseTest {

	@Drive
	private LoginPage loginPage;

	@Drive
	private FilesPage filesPage;

	@Drive
	private SpreadsheetEditorPage spreadsheetEditorPage;

	private static String testData;

	@BeforeClass
	public static void beforeClass() {
		int id = 45;
		GData gData = GData.getInstance();
		testData = gData.getDataById(id);
	}

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
	public void atUnDoReDo() {
		String fileName = Utils.uniqueBaseName("newSpreadsheetInFiles");
		spreadsheetEditorPage.focus(filesPage.newSpreadsheet(fileName));
		log.info("Verify DIH-AHK-020 at unDo and reDo Process");
		spreadsheetEditorPage.typeInCell("C3", testData);
		spreadsheetEditorPage.clickCell("A1");
		spreadsheetEditorPage.undo();
		String actual1 = spreadsheetEditorPage.getCellText("C3");
		ga.assertEquals("UnDo failed.", "", actual1);
		spreadsheetEditorPage.redo();
		String actual2 = spreadsheetEditorPage.getCellText("C3");
		ga.assertEquals("ReDo failed.", testData, actual2);
	}

}
