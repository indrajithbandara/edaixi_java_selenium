package com.ibm.docs.test.gui.cases.presentation;

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

/**
 * Ensure that the Unicode character data that is inputted from web browser can
 * be handled properly in back end.
 * 
 * @author xinyutan@cn.ibm.com
 * 
 */
@GuideLink("https://labweb.torolab.ibm.com/gcoc/documents/gvtguide/testareas/DIH-WEB-010.htm")
public class DIH_WEB_010 extends BaseTest {

	@Drive
	private LoginPage loginPage;

	@Drive
	private FilesPage filesPage;

	@Drive
	private PresentationEditorPage presentationEditorPage;

	private static List<String> testData = new ArrayList<String>();

	@BeforeClass
	public static void beforeClass() {
		int[] ids = { 3, 4, 5, 6, 7, 8, 9, 10, 11, 20, 40, 44, 45 };
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
	public void atTitle() {
		String fileName = Utils.uniqueBaseName("newPresentationInFiles");
		presentationEditorPage.focus(filesPage.newPresentation(fileName));
		log.info("Verify DIH-WEB-010 at Presentation title");
		String deletes = Keys.chord(Keys.CONTROL, "a") + Keys.BACK_SPACE;
		for (String value : testData) {
			presentationEditorPage.selectDrawFrame("title", 0);
			presentationEditorPage.typeInIframe(deletes);
			presentationEditorPage.typeInIframe(value);
			presentationEditorPage.slideEditor.click(1, 1);
			String actual = presentationEditorPage.getTextFromDrawFrameClass(
					"title", 0);
			log.info("Test " + GUtils.getHexUnicode(value) + " " + value);
			ga.assertEquals("Test " + GUtils.getHexUnicode(value), value,
					actual);
			presentationEditorPage.publishVersion("Publish");
			presentationEditorPage.close();
			presentationEditorPage.focus(filesPage.edit(fileName));
			String actual2 = presentationEditorPage.getTextFromDrawFrameClass(
					"title", 0);
			log.info("Test after publish and reopen "
					+ GUtils.getHexUnicode(value) + " " + value);
			ga.assertEquals("Test " + GUtils.getHexUnicode(value), value,
					actual2);
		}
	}

	@Test
	public void atSubTitle() {
		String fileName = Utils.uniqueBaseName("newPresentationInFiles");
		presentationEditorPage.focus(filesPage.newPresentation(fileName));
		log.info("Verify DIH-WEB-010 at Presentation subTitle");
		String deletes = Keys.chord(Keys.CONTROL, "a") + Keys.BACK_SPACE;
		for (String value : testData) {
			presentationEditorPage.selectDrawFrame("subtitle", 0);
			presentationEditorPage.typeInIframe(deletes);
			presentationEditorPage.typeInIframe(value);
			presentationEditorPage.slideEditor.click(1, 1);
			String actual = presentationEditorPage.getTextFromDrawFrameClass(
					"subtitle", 0);
			log.info("Test " + GUtils.getHexUnicode(value) + " " + value);
			ga.assertEquals("Test " + GUtils.getHexUnicode(value), value,
					actual);
			presentationEditorPage.publishVersion("Publish");
			presentationEditorPage.close();
			presentationEditorPage.focus(filesPage.edit(fileName));
			String actual2 = presentationEditorPage.getTextFromDrawFrameClass(
					"subtitle", 0);
			log.info("Test after publish and reopen "
					+ GUtils.getHexUnicode(value) + " " + value);
			ga.assertEquals("Test " + GUtils.getHexUnicode(value), value,
					actual2);
		}
	}

	@Test
	public void atOutline() {
		String fileName = Utils.uniqueBaseName("newPresentationInFiles");
		presentationEditorPage.focus(filesPage.newPresentation(fileName));
		log.info("Verify DIH-WEB-010 at Presentation outLine");
		String deletes = Keys.chord(Keys.CONTROL, "a") + Keys.BACK_SPACE;
		presentationEditorPage.newSlide();
		for (String value : testData) {
			presentationEditorPage.selectDrawFrame("outline", 0);
			presentationEditorPage.typeInIframe(deletes);
			presentationEditorPage.typeInIframe(value);
			presentationEditorPage.slideEditor.click(1, 1);
			String actual = presentationEditorPage.getTextFromDrawFrameClass(
					"outline", 0);
			log.info("Test " + GUtils.getHexUnicode(value) + " " + value);
			ga.assertEquals("Test " + GUtils.getHexUnicode(value), value,
					actual);
			/*
			 * presentationEditorPage.publishVersion("Publish");
			 * presentationEditorPage.close();
			 * presentationEditorPage.focus(filesPage.edit(fileName));
			 * presentationEditorPage.selectSlide(2);
			 * presentationEditorPage.driver.sleep(1); String actual2 =
			 * presentationEditorPage.getTextFromDrawFrameClass( "outline", 0);
			 * log.info("Test after publish and reopen " +
			 * GUtils.getHexUnicode(value) + " " + value);
			 * ga.assertEquals("Test " + GUtils.getHexUnicode(value), value,
			 * actual2);
			 */
		}
	}

	@Test
	public void atComments() {
		String fileName = Utils.uniqueBaseName("newPresentationInFiles");
		presentationEditorPage.focus(filesPage.newPresentation(fileName));
		log.info("Verify DIH-WEB-010 at Presentation comments");
		for (String value : testData) {
			presentationEditorPage.selectDrawFrame("title", 0);
			presentationEditorPage.addComment(value);
			presentationEditorPage.driver.sleep(2);
			String actual = presentationEditorPage.commentsTexts.get(0)
					.getText();
			log.info("Test " + GUtils.getHexUnicode(value) + " " + value);
			ga.assertEquals("Test " + GUtils.getHexUnicode(value), value,
					actual);
		}
	}

	@Test
	public void atPublishVersion() {
		String fileName = Utils.uniqueBaseName("newPresentationInFiles");
		String filesWH = filesPage.driver.getWindowHandle();
		String documentWH = filesPage.newPresentation(fileName);
		presentationEditorPage.focus(documentWH);
		log.info("Verify DIH-WEB-010 at Presentation publishVersion");
		for (String value : testData) {
			presentationEditorPage.publishVersion(value);
			filesPage.focus(filesWH);
			filesPage.openFileDetail(fileName);
			filesPage.versionsBtn.click();
			String actual = filesPage.versionAlignText.getText();
			log.info("Test " + GUtils.getHexUnicode(value) + " " + value);
			ga.assertEquals("Test " + GUtils.getHexUnicode(value), value,
					actual);
			presentationEditorPage.focus(documentWH);
		}
	}

	@Test
	public void atTextBox() {
		String fileName = Utils.uniqueBaseName("newPresentationInFiles");
		presentationEditorPage.focus(filesPage.newPresentation(fileName));
		log.info("Verify DIH-WEB-010 at Presentation textBox");
		String deletes = Keys.chord(Keys.CONTROL, "a") + Keys.BACK_SPACE;
		presentationEditorPage.newTextBox();
		presentationEditorPage.typeInIframe("Star Craft 2");
		presentationEditorPage.slideEditor.click(1, 1);
		for (String value : testData) {
			presentationEditorPage.selectDrawFrame("", 0);
			presentationEditorPage.typeInIframe(deletes);
			presentationEditorPage.typeInIframe(value);
			presentationEditorPage.slideEditor.click(1, 1);
			String actual = presentationEditorPage.getTextFromDrawFrameClass(
					"", 0);
			log.info("Test " + GUtils.getHexUnicode(value) + " " + value);
			ga.assertEquals("Test " + GUtils.getHexUnicode(value), value,
					actual);
			presentationEditorPage.publishVersion("Publish");
			presentationEditorPage.close();
			presentationEditorPage.focus(filesPage.edit(fileName));
			String actual2 = presentationEditorPage.getTextFromDrawFrameClass(
					"", 0);
			log.info("Test after publish and reopen "
					+ GUtils.getHexUnicode(value) + " " + value);
			ga.assertEquals("Test " + GUtils.getHexUnicode(value), value,
					actual2);
		}
	}

	@Test
	public void atTable() {
		String fileName = Utils.uniqueBaseName("newPresentationInFiles");
		presentationEditorPage.focus(filesPage.newPresentation(fileName));
		log.info("Verify DIH-WEB-010 at Presentation table cells");
		presentationEditorPage.createTable(2, 10);
		presentationEditorPage.slideEditor.click(1, 1);
		int x = 1, y = 1;
		for (String value : testData) {
			presentationEditorPage.selectDrawFrame("table", 0);
			presentationEditorPage.typeInTable(value, 0, x, y);
			String actual = presentationEditorPage.getCellText(0, x, y);
			log.info("Test " + GUtils.getHexUnicode(value) + " " + value);
			ga.assertEquals("Test " + GUtils.getHexUnicode(value), value,
					actual);
			y++;
			if (y > 10) {
				y = 1;
				x = 2;
			}
		}
		presentationEditorPage.publishVersion("Publish");
		presentationEditorPage.close();
		presentationEditorPage.focus(filesPage.edit(fileName));
		x = 1;
		y = 1;
		for (String value : testData) {
			presentationEditorPage.selectDrawFrame("table", 0);
			String actual = presentationEditorPage.getCellText(0, x, y);
			log.info("Test after publish" + GUtils.getHexUnicode(value) + " "
					+ value);
			ga.assertEquals("Test " + GUtils.getHexUnicode(value), value,
					actual);
			y++;
			if (y > 10) {
				y = 1;
				x = 2;
			}
		}
	}

	@Test
	public void atNotes() {
		String fileName = Utils.uniqueBaseName("newPresentationInFiles");
		presentationEditorPage.focus(filesPage.newPresentation(fileName));
		log.info("Verify DIH-WEB-010 at Presentation speakerNotes");
		String deletes = Keys.chord(Keys.CONTROL, "a") + Keys.BACK_SPACE;
		for (String value : testData) {
			String id = presentationEditorPage.selectSpeakerNotes();
			presentationEditorPage.typeInSpeakerNotes(id, deletes);
			presentationEditorPage.typeInSpeakerNotes(id, value);
			presentationEditorPage.slideEditor.click(1, 1);
			String actual = presentationEditorPage.getTextFromDrawFrameClass(
					"notes", 0);
			log.info("Test " + GUtils.getHexUnicode(value) + " " + value);
			ga.assertEquals("Test " + GUtils.getHexUnicode(value), value,
					actual);
			presentationEditorPage.publishVersion("Publish");
			presentationEditorPage.close();
			presentationEditorPage.focus(filesPage.edit(fileName));
			String actual2 = presentationEditorPage.getTextFromDrawFrameClass(
					"notes", 0);
			log.info("Test after publish and reopen "
					+ GUtils.getHexUnicode(value) + " " + value);
			ga.assertEquals("Test " + GUtils.getHexUnicode(value), value,
					actual2);
		}
	}

}
