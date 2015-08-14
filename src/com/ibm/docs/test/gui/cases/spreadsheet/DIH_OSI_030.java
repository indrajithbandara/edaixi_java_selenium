/**
 * 
 */
package com.ibm.docs.test.gui.cases.spreadsheet;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.ibm.docs.test.common.GuideLink;
import com.ibm.docs.test.common.Utils;
import com.ibm.docs.test.gui.cases.BaseTest;
import com.ibm.docs.test.gui.pages.DocumentEditorPage;
import com.ibm.docs.test.gui.pages.FilesPage;
import com.ibm.docs.test.gui.pages.LoginPage;
import com.ibm.docs.test.gui.pages.SpreadsheetEditorPage;
import com.ibm.docs.test.gui.support.Drive;

/**
 * Ensure that all characters supported by the operating system can be used.
 * 
 * @author lidongyu@cn.ibm.com
 *
 */
@GuideLink("https://labweb.torolab.ibm.com/gcoc/documents/gvtguide/testareas/DIH-OSI-030.htm")
public class DIH_OSI_030 extends BaseTest  {
	
	@Drive
	private LoginPage loginPage;

	@Drive
	private FilesPage filesPage;
	
	@Drive
	private SpreadsheetEditorPage spreadsheetEditorPage;
	
	@Drive
	private DocumentEditorPage documentEditorPage;
	
	@Before
	public void before() {
		loginPage.login();
		filesPage.go();
	}

		@Test
		public void atInputNewSpreadsheet() {
			/**make sure Uppercase or lowercase, numbers, periods, underscore, Hyphen can be used as file name.*/
			String fileName = Utils.uniqueBaseName("Ad9_-.");
			documentEditorPage.focus(filesPage.newDocument(Utils.uniqueBaseName("newSpreadsheetInEditor2")));
			spreadsheetEditorPage.focus(documentEditorPage.newSpreadsheet(fileName));
			assertEquals("Spreadsheet name", fileName, spreadsheetEditorPage.getDocTitle());
			spreadsheetEditorPage.typeInCell("A1", "data");
			spreadsheetEditorPage.clickCell("A3");
			log.info("Verify if the typed content is inputed successuly");
			assertEquals("Cell A1 content", "data", spreadsheetEditorPage.getCellText("A1"));
			spreadsheetEditorPage.publishVersion("GVT");
			spreadsheetEditorPage.close();
			documentEditorPage.close();
			
			spreadsheetEditorPage.focus(filesPage.edit(fileName));
			log.info("Verify if the typed content is saved successfully");
			assertEquals("Cell A1 content after reopening", "data", spreadsheetEditorPage.getCellText("A1"));
		}
}
