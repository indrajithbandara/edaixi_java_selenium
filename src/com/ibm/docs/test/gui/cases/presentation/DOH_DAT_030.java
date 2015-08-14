package com.ibm.docs.test.gui.cases.presentation;

import java.text.ParseException;
import java.util.Date;
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
import com.ibm.docs.test.gui.pages.SpreadsheetEditorPage;
import com.ibm.docs.test.gui.pages.DocsEditorPage;
import com.ibm.docs.test.gui.support.Drive;
import com.ibm.docs.test.gui.support.GLan;
import com.ibm.icu.text.DateFormat;
import com.ibm.icu.util.ULocale;

public class DOH_DAT_030 extends BaseTest{
	
	@Drive
	private LoginPage loginPage;

	@Drive
	private FilesPage filesPage;

	@Drive
	private PresentationEditorPage presentationEditorPage;
	
	@Drive
	private DocsEditorPage  DocsEditorPage;
	
	

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
		"sl-SL","id-ID" })
	
	//@GLan({"pt-BR"})
	public void atCommentDateTime(){
		
		log.info("Verify DOH-DAT-030 at comments date time");
		String localeID = GUtils.getBrowserPreferenceLan(presentationEditorPage.driver);
		String fileName = Utils.uniqueBaseName("newPresentationInFiles");
		presentationEditorPage.focus(filesPage.newPresentation(fileName));
		presentationEditorPage.selectDrawFrame("title", 0);
		DocsEditorPage.commentBtnNew.click();
		//String expectedTime = GUtils.getLocalTime(3, localeID);
		String expectedDate = GUtils.getLocalDate(2, localeID);
		String ss = DocsEditorPage.commentDialogTime.getText();
		boolean containsDate = ss.contains(expectedDate);
		ga.assertTrue("Date is shown incorrect", containsDate);
		
	}
	
	@Test
	@GLan({ "pt-BR", "pt-PT", "bg-BG", "ca-ES", "cs-CZ", "da-DK", "nl-NL",
		"fi-FI", "fr-FR", "de-DE", "el-GR", "hu-HU", "it-IT", "ja-JP",
		"kk-KZ", "ko-KR", "nn-NO", "pl-PL", "ro-RO", "ru-RU", "zh-CN",
		"es-ES", "sv-SE", "th-TH", "zh-TW", "tr-TR", "hr-HR", "sk-SK",
		"sl-SL","id-ID" })
	public void atCommentTime(){
		
		log.info("Verify DOH-DAT-030 at comments date time");
		String localeID = GUtils.getBrowserPreferenceLan(presentationEditorPage.driver);
		String fileName = Utils.uniqueBaseName("newPresentationInFiles");
		presentationEditorPage.focus(filesPage.newPresentation(fileName));
		DocsEditorPage.addCommentNew("abc");
		String expectedTime = GUtils.getLocalTime(3, localeID);
		System.out.println("expectedTime = "+expectedTime);
		
		String ss = DocsEditorPage.commentDialogTime.getText();
		System.out.println("ss = "+ss);
		
		boolean containsTime = ss.contains(expectedTime);
		ga.assertTrue("Time is shown incorrect", containsTime);
		
		
	}
	
	

}
