package com.ibm.docs.test.gui.cases.presentation;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import com.ibm.docs.test.common.GData;
import com.ibm.docs.test.common.GUtils;
import com.ibm.docs.test.common.GuideLink;
import com.ibm.docs.test.common.Utils;
import com.ibm.docs.test.gui.cases.BaseTest;
import com.ibm.docs.test.gui.pages.FilesPage;
import com.ibm.docs.test.gui.pages.LoginPage;
import com.ibm.docs.test.gui.pages.PresentationEditorPage;
import com.ibm.docs.test.gui.support.Drive;
import com.ibm.docs.test.gui.support.GLan;

/**
 * Ensure that there is no loss of control codes and other formatting characters
 * during cut and paste. preferred by the end users.
 * 
 * @author xinyutan@cn.ibm.com
 * 
 */
@GuideLink("https://labweb.torolab.ibm.com/gcoc/documents/gvtguide/testareas/DIH-CLP-030.htm")
public class DIH_CLP_030 extends BaseTest {

	@Drive
	private LoginPage loginPage;

	@Drive
	private FilesPage filesPage;

	@Drive
	private PresentationEditorPage presentationEditorPage;

	private static String testData;

	@BeforeClass
	public static void beforeClass() {
		int id1 = 1216;
		GData gData = GData.getInstance();
		testData = gData.getDataById(id1);
	}

	@Before
	public void before() {
		loginPage.login();
		filesPage.go();
	}

	@Test
	public void atCopy() {
		String fileName = Utils.uniqueBaseName("newPresentationInFiles");
		presentationEditorPage.focus(filesPage.newPresentation(fileName));
		log.info("Verify DIH-CLP-030 at Copy text.");
		presentationEditorPage.selectDrawFrame("title", 0);
		presentationEditorPage.typeInIframe(testData);
		presentationEditorPage.selectDrawFrame("title", 0).selectAll().copy();
		presentationEditorPage.typeInIframe("removeCtrl");
		presentationEditorPage.selectDrawFrame("subtitle", 0).paste();
		presentationEditorPage.slideEditor.click(1, 1);
		String target = presentationEditorPage.getTextFromDrawFrameClass(
				"subtitle", 0);
		String actualHex = GUtils.getHexUnicode(target);
		String expectedHex = GUtils.getHexUnicode(testData);
		assertEquals("Copy Cell, No loss of control codes", expectedHex,
				actualHex);
	}

	@Test
	public void atCut() {
		String fileName = Utils.uniqueBaseName("newPresentationInFiles");
		presentationEditorPage.focus(filesPage.newPresentation(fileName));
		log.info("Verify DIH-CLP-030 at Cut text.");
		presentationEditorPage.selectDrawFrame("title", 0);
		presentationEditorPage.typeInIframe(testData);
		presentationEditorPage.selectDrawFrame("title", 0).selectAll().cut();
		presentationEditorPage.typeInIframe("removeCtrl");
		presentationEditorPage.selectDrawFrame("subtitle", 0).paste();
		presentationEditorPage.slideEditor.click(1, 1);
		String target = presentationEditorPage.getTextFromDrawFrameClass(
				"subtitle", 0);
		String actualHex = GUtils.getHexUnicode(target);
		String expectedHex = GUtils.getHexUnicode(testData);
		assertEquals("Cut Cell, No loss of control codes", expectedHex,
				actualHex);
	}

	@Test
	public void atCopyTextBox() {
		String fileName = Utils.uniqueBaseName("newPresentationInFiles");
		presentationEditorPage.focus(filesPage.newPresentation(fileName));
		log.info("Verify DIH-CLP-030 at Copy textbox.");
		presentationEditorPage.selectDrawFrame("title", 0);
		presentationEditorPage.typeInIframe(testData);
		presentationEditorPage.clickDrawFrame("title", 0).copy();
		presentationEditorPage.slideEditor.click(1, 1);
		presentationEditorPage.paste();
		presentationEditorPage.slideEditor.click(1, 1);
		presentationEditorPage.driver.sleep(1);
		String target = presentationEditorPage.getTextFromDrawFrameClass("title", 1);
		String actualHex = GUtils.getHexUnicode(target);
		String expectedHex = GUtils.getHexUnicode(testData);
		assertEquals("Cut Cell, No loss of control codes", expectedHex,
				actualHex);
	}

	@Test
	public void atCutTextBox() {
		String fileName = Utils.uniqueBaseName("newPresentationInFiles");
		presentationEditorPage.focus(filesPage.newPresentation(fileName));
		log.info("Verify DIH-CLP-030 at Cut textbox.");
		presentationEditorPage.selectDrawFrame("title", 0);
		presentationEditorPage.typeInIframe(testData);
		presentationEditorPage.clickDrawFrame("title", 0).cut();
		presentationEditorPage.slideEditor.click(1, 1);
		presentationEditorPage.paste();
		presentationEditorPage.slideEditor.click(1, 1);
		presentationEditorPage.driver.sleep(1);
		String target = presentationEditorPage.getTextFromDrawFrameClass("title", 0);
		String actualHex = GUtils.getHexUnicode(target);
		String expectedHex = GUtils.getHexUnicode(testData);
		assertEquals("Cut Cell, No loss of control codes", expectedHex,
				actualHex);
	}
}
