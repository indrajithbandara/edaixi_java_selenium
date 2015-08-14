package com.ibm.docs.test.gui.cases.presentation;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Keys;

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
 * Ensure that all Unicode characters can be cut, copied, and pasted. preferred
 * by the end users.
 * 
 * @author xinyutan@cn.ibm.com
 * 
 */
@GuideLink("https://labweb.torolab.ibm.com/gcoc/documents/gvtguide/testareas/DIH-CLP-010.htm")
public class DIH_CLP_010 extends BaseTest {

	@Drive
	private LoginPage loginPage;

	@Drive
	private FilesPage filesPage;

	@Drive
	private PresentationEditorPage presentationEditorPage;

	private static List<String> testData = new ArrayList<String>();

	@BeforeClass
	public static void beforeClass() {
		int[] ids = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 19, 20, 21, 22, 23,
				24, 29, 40, 43, 44, 45 };
		GData gData = GData.getInstance();
		for (int id : ids) {
			testData.add(gData.getDataById(id));
		}
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
		log.info("Verify DIH-CLP-010 at Copy text.");
		String deletes = Keys.chord(Keys.CONTROL, "a") + Keys.BACK_SPACE;
		for (String value : testData) {
			presentationEditorPage.selectDrawFrame("title", 0);
			presentationEditorPage.typeInIframe(deletes);
			presentationEditorPage.typeInIframe(value);
			presentationEditorPage.selectDrawFrame("title", 0).selectAll()
					.copy();
			presentationEditorPage.typeInIframe("removeCtrl");
			presentationEditorPage.selectDrawFrame("subtitle", 0);
			presentationEditorPage.typeInIframe(deletes).paste();
			presentationEditorPage.typeInIframe("removeCtrl");
			presentationEditorPage.slideEditor.click(1, 1);
			String actual = presentationEditorPage.getTextFromDrawFrameClass(
					"subtitle", 0).replace("removeCtrl", "");
			ga.assertEquals("Verify string after copy", value,
					actual);
		}

	}

	@Test
	public void atCut() {
		String fileName = Utils.uniqueBaseName("newPresentationInFiles");
		presentationEditorPage.focus(filesPage.newPresentation(fileName));
		log.info("Verify DIH-CLP-010 at Cut text.");
		String deletes = Keys.chord(Keys.CONTROL, "a") + Keys.BACK_SPACE;
		for (String value : testData) {
			presentationEditorPage.selectDrawFrame("title", 0);
			presentationEditorPage.typeInIframe(deletes);
			presentationEditorPage.typeInIframe(value);
			presentationEditorPage.selectDrawFrame("title", 0).selectAll()
					.cut();
			presentationEditorPage.typeInIframe("removeCtrl");
			presentationEditorPage.selectDrawFrame("subtitle", 0);
			presentationEditorPage.typeInIframe(deletes).paste();
			presentationEditorPage.typeInIframe("removeCtrl");
			presentationEditorPage.slideEditor.click(1, 1);
			String actual = presentationEditorPage.getTextFromDrawFrameClass(
					"subtitle", 0).replace("removeCtrl", "");
			ga.assertEquals("Verify string after cut", value,
					actual);
		}
	}
	
}
