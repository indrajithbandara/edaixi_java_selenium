/**
 * 
 */
package com.ibm.docs.test.gui.cases.spreadsheet;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ibm.docs.test.common.GData;
import com.ibm.docs.test.common.GUtils;
import com.ibm.docs.test.common.GuideLink;
import com.ibm.docs.test.common.TestDoc;
import com.ibm.docs.test.common.Utils;
import com.ibm.docs.test.gui.cases.BaseTest;
import com.ibm.docs.test.gui.pages.DocsEditorPage;
import com.ibm.docs.test.gui.pages.FilesPage;
import com.ibm.docs.test.gui.pages.LoginPage;
import com.ibm.docs.test.gui.pages.SpreadsheetEditorPage;
import com.ibm.docs.test.gui.pages.SpreadsheetFilePage;
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
	private SpreadsheetEditorPage spreadsheetEditorPage;
	
	@Drive
	private SpreadsheetFilePage spreadsheetFilePage;
	
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
	public void atSaveAs() {
		String fileName = Utils.uniqueBaseName("newSpreadsheetInFiles");
		spreadsheetEditorPage.focus(filesPage.newSpreadsheet(fileName));
		log.info("Verify DIH-OSI-010 at SaveAs");
		for (String value : testData) {
			spreadsheetEditorPage.focus(spreadsheetEditorPage.saveAs(value));
			assertEquals("Spreadsheet name", value, spreadsheetEditorPage.getDocTitle());
			spreadsheetEditorPage.close();
		}
	}
	
	@TestDoc("defect 42762: [GVT][Spreadsheet] Some U3B and G2 words cannot be displayed correctly when importing  txt and csv file into ibm docs.")
	@Test
	 public void atImportFromSpreadsheet(){   
		String fileName = Utils.uniqueBaseName("newSpreadsheetInFiles");
		spreadsheetEditorPage.focus(filesPage.newSpreadsheet(fileName));
		log.info("Verify DIH-OSI-010 at ImportCSV");
		docsEditorPage.importFromSpreadsheet();
		File file = env.getDataFile("/gvt/鯵㈱表噂ソ十豹竹敷～兎椄.txt");
		spreadsheetFilePage.importFile(file);
		
		int i = 1;
		for (String value : testData) {
			String cell = "A" + i;
			String actual = spreadsheetEditorPage.getCellText(cell);
			log.info("Test " + GUtils.getHexUnicode(value) + " " + value);
			ga.assertEquals("Test " + GUtils.getHexUnicode(value), value,
					actual);
			i++;
		}
//		File file2 = env.getDataFile("/gvt/𠀀𠀁𠀂𠀃𠀄𪛔𪛕𪛖鯵㈱表噂ソ十豹竹敷～兎椄.csv");
//		spreadsheetFilePage.importFile(file2);	
		spreadsheetFilePage.close();
		log.info("Verify if the sample files have been opened successfully");
	    };
}
