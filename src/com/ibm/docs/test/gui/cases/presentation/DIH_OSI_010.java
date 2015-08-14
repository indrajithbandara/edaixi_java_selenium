package com.ibm.docs.test.gui.cases.presentation;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ibm.docs.test.common.GData;
import com.ibm.docs.test.common.GuideLink;
import com.ibm.docs.test.common.Utils;
import com.ibm.docs.test.gui.cases.BaseTest;
import com.ibm.docs.test.gui.pages.DocsEditorPage;
import com.ibm.docs.test.gui.pages.FilesPage;
import com.ibm.docs.test.gui.pages.LoginPage;
import com.ibm.docs.test.gui.pages.PresentationEditorPage;
import com.ibm.docs.test.gui.support.Drive;

/**
 * Ensure that Operating System item names (for example, path names and other resources) 
 * can be handled.
 * 
 * @author lidongyu@cn.ibm.com
 *
 */
@GuideLink("https://labweb.torolab.ibm.com/gcoc/documents/gvtguide/testareas/DIH-OSI-010.htm")
	public class DIH_OSI_010 extends BaseTest  {

		/**
		 * 
		 */
		@Drive
		private LoginPage loginPage;

		@Drive
		private FilesPage filesPage;

		@Drive
		private PresentationEditorPage presentationEditorPage;
			
		@Drive
		private DocsEditorPage docsEditorPage;
		
		private static List<String> testData = new ArrayList<String>();

		@BeforeClass
		public static void beforeClass() {
			int[] ids = {3, 4, 5, 6, 7, 8, 9, 11, 40, 44, 45};
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
		public void atNewPresentation() {
			String fileName = Utils.uniqueBaseName("newPresentationInFiles");
			presentationEditorPage.focus(filesPage.newPresentation(fileName));
			log.info("Verify DIH-OSI-010 at NewPresentation");
			for (String value : testData) {
				presentationEditorPage.focus(presentationEditorPage.newPresentation(value));
				assertEquals("Presentation name", value, presentationEditorPage.getDocTitle());
				presentationEditorPage.close();
			}
		}
		@Test
		public void atSaveAs() {
			String fileName = Utils.uniqueBaseName("newPresentationInFiles");
			presentationEditorPage.focus(filesPage.newPresentation(fileName));
			log.info("Verify DIH-OSI-010 at SaveAs");
			for (String value : testData) {
				presentationEditorPage.focus(presentationEditorPage.saveAs(value));
				assertEquals("Presentation name", value, presentationEditorPage.getDocTitle());
				presentationEditorPage.close();
			}
		}
}
