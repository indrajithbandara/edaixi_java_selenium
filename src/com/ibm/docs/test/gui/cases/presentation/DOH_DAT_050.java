package com.ibm.docs.test.gui.cases.presentation;

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
import com.ibm.docs.test.gui.pages.PresentationEditorPage;
import com.ibm.docs.test.gui.support.Drive;
import com.ibm.docs.test.gui.support.GLan;

/**
 * Ensure that dates and times that are presented in a text message are
 * formatted using the user's preferences.
 * 
 * @author xinyutan@cn.ibm.com
 * 
 */
@GuideLink("https://labweb.torolab.ibm.com/gcoc/documents/gvtguide/testareas/DOH-DAT-050.htm")
public class DOH_DAT_050 extends BaseTest {


	@Drive
	private LoginPage loginPage;

	@Drive
	private FilesPage filesPage;

	@Drive
	private PresentationEditorPage presentationEditorPage;

	@Before
	public void before() {
		loginPage.login();
		filesPage.go();
	}

	@Test
	@TestDoc("defect 42722 - Please use official time for da-DK.")
	@GLan({ "pt-BR", "pt-PT", "bg-BG", "ca-ES", "cs-CZ", "da-DK", "nl-NL",
			"fi-FI", "fr-FR", "de-DE", "el-GR", "hu-HU", "it-IT", "ja-JP",
			"kk-KZ", "ko-KR", "nn-NO", "pl-PL", "ro-RO", "ru-RU", "zh-CN",
			"es-ES", "sv-SE", "th-TH", "zh-TW", "tr-TR", "hr-HR", "sk-SK",
			"sl-SL" })
	public void atpublishedMessage() {
		String fileName = Utils.uniqueBaseName("newPresentationInFiles");
		presentationEditorPage.focus(filesPage.newPresentation(fileName));
		log.info("Verify DOH-DAT-050 at Published Message");
		String localeID = GUtils
				.getBrowserPreferenceLan(presentationEditorPage.driver);
		String actual = presentationEditorPage.publishVersion();
		String expected = GUtils.getLocalTime(3, localeID);
		boolean contains = actual.contains(expected);
		assertTrue(localeID + " expected time: " + expected, contains);
	}
}
