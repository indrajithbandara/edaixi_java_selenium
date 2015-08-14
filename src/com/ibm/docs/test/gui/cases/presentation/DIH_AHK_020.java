package com.ibm.docs.test.gui.cases.presentation;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Keys;

import com.ibm.docs.test.common.GData;
import com.ibm.docs.test.common.Utils;
import com.ibm.docs.test.gui.cases.BaseTest;
import com.ibm.docs.test.gui.pages.FilesPage;
import com.ibm.docs.test.gui.pages.LoginPage;
import com.ibm.docs.test.gui.pages.PresentationEditorPage;
import com.ibm.docs.test.gui.support.Drive;
import com.ibm.docs.test.gui.support.GLan;

/**
 * Ensure that accelerator and hot keys work independently of the UI
 * translation.
 * 
 * @author xinyutan@cn.ibm.com
 * 
 */
public class DIH_AHK_020 extends BaseTest {

	@Drive
	private LoginPage loginPage;

	@Drive
	private FilesPage filesPage;

	@Drive
	private PresentationEditorPage presentationEditorPage;

	private static String before;

	private static String after;

	@BeforeClass
	public static void beforeClass() {
		int id1 = 44;
		int id2 = 45;
		GData gData = GData.getInstance();
		before = gData.getDataById(id1);
		after = gData.getDataById(id2);
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
		String fileName = Utils.uniqueBaseName("newPresentationInFiles");
		presentationEditorPage.focus(filesPage.newPresentation(fileName));
		log.info("Verify DIH-AHK-020 at unDo and reDo Process");
		CharSequence d = Keys.BACK_SPACE;
		String deletes = Keys.chord(d, d, d, d, d, d, d, d, d, d, d, d, d, d,
				d, d, d, d, d, d, d, d, d, d, d, d, d, d, d, d, d, d, d, d, d);
		presentationEditorPage.selectDrawFrame("title", 0);
		presentationEditorPage.typeInIframe(before);
		presentationEditorPage.slideEditor.click(1, 1);
		presentationEditorPage.selectDrawFrame("title", 0);
		presentationEditorPage.typeInIframe(deletes);
		presentationEditorPage.driver.sleep(2);
		presentationEditorPage.typeInIframe(after);
		presentationEditorPage.slideEditor.click(1, 1);
		presentationEditorPage.undo();
		presentationEditorPage.driver.sleep(2);
		String actual1 = presentationEditorPage.getTextFromDrawFrameClass(
				"title", 0);
		ga.assertEquals("UnDo failed.", "", actual1);
		presentationEditorPage.redo();
		String actual2 = presentationEditorPage.getTextFromDrawFrameClass(
				"title", 0);
		ga.assertEquals("ReDo failed.", after, actual2);
	}

}
