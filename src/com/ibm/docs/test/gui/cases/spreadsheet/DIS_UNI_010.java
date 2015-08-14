package com.ibm.docs.test.gui.cases.spreadsheet;

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
import com.ibm.docs.test.gui.pages.FilesPage;
import com.ibm.docs.test.gui.pages.LoginPage;
import com.ibm.docs.test.gui.pages.SpreadsheetEditorPage;
import com.ibm.docs.test.gui.pages.SpreadsheetFilePage;
import com.ibm.docs.test.gui.support.Drive;

/**
 * Ensure that the complete Unicode character range (including currently
 * unassigned code points) can be received.
 * 
 * @author xinyutan@cn.ibm.com
 * 
 */
@GuideLink("https://labweb.torolab.ibm.com/gcoc/documents/gvtguide/testareas/DIS-UNI-010.htm")
public class DIS_UNI_010 extends BaseTest {

	@Drive
	private LoginPage loginPage;

	@Drive
	private FilesPage filesPage;

	@Drive
	private SpreadsheetEditorPage spreadsheetEditorPage;

	@Drive
	private SpreadsheetFilePage spreadsheetFilePage;

	private static List<String> testData = new ArrayList<String>();

	@BeforeClass
	public static void beforeClass() {
		int[] ids = { 3, 4, 5, 6, 7, 8, 9, 40, 44, 45 };
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
	public void atUploadCell() {
		List<File> files = new ArrayList<File>();
		files.add(env.getDataFile("gvt/MSExcel2010.xls"));
		files.add(env.getDataFile("gvt/MSExcel2010.xlsx"));
		files.add(env.getDataFile("gvt/MSExcel2013.xls"));
		files.add(env.getDataFile("gvt/MSExcel2013.xlsx"));
		files.add(env.getDataFile("gvt/Symphony.ods"));
		for (File file : files) {
			String fileName = Utils.uniqueBaseName(file);
			spreadsheetEditorPage.focus(filesPage
					.uploadThenEdit(file, fileName));
			int i = 1;
			log.info("Test DIS-UNI-010 at uploaded file: " + file.getName());
			for (String value : testData) {
				String cell = "A" + i;
				String actual = spreadsheetEditorPage.getCellText(cell);
				spreadsheetEditorPage.clickCell(cell);
				log.info("Test " + GUtils.getHexUnicode(value) + " " + value);
				ga.assertEquals("Test " + GUtils.getHexUnicode(value), value,
						actual);
				i++;
			}
			filesPage.go();
		}
	}

	@Test
	public void atUploadSheetName() {
		List<File> files = new ArrayList<File>();
		files.add(env.getDataFile("gvt/MSExcel2010.xls"));
		files.add(env.getDataFile("gvt/MSExcel2010.xlsx"));
		files.add(env.getDataFile("gvt/MSExcel2013.xls"));
		files.add(env.getDataFile("gvt/MSExcel2013.xlsx"));
		files.add(env.getDataFile("gvt/Symphony.ods"));
		for (File file : files) {
			String fileName = Utils.uniqueBaseName(file);
			spreadsheetEditorPage.focus(filesPage
					.uploadThenEdit(file, fileName));
			int i = 1;
			log.info("Test DIS-UNI-010 at uploaded file: " + file.getName());
			for (String value : testData) {
				String actual = spreadsheetEditorPage.getSheetName(i);
				log.info("Test " + GUtils.getHexUnicode(value) + " " + value);
				ga.assertEquals("Test " + GUtils.getHexUnicode(value), value,
						actual);
				i++;
			}
			filesPage.go();
		}
	}

	@Test
	@TestDoc("defect 42762 - Unicode Plane2 string be corrupted in importing.")
	public void atImportCommaOption() {
		List<File> files = new ArrayList<File>();
		files.add(env.getDataFile("gvt/Comma.txt"));
		String[] cellCs = { "A", "B" };
		String fileName = Utils.uniqueBaseName("newSpreadsheetInFiles");
		spreadsheetFilePage.focus(filesPage.newSpreadsheet(fileName));
		int j = 0;
		for (File file : files) {
			spreadsheetFilePage.importFromSpreadsheet();
			spreadsheetFilePage.importFileCommaOption(file);
			int i = 1;
			log.info("Test DIS-UNI-010 at uploaded file: " + file.getName());
			for (String value : testData) {
				String cell = cellCs[j] + i;
				String actual = spreadsheetEditorPage.getCellText(cell);
				spreadsheetFilePage.clickCell(cell);
				log.info("Actual " + GUtils.getHexUnicode(actual) + " " + actual);
				ga.assertEquals("Test " + GUtils.getHexUnicode(value), value,
						actual);
				i++;
			}
			spreadsheetFilePage.clickCell("B1");
			j++;
		}
	}
	
	@Test
	public void atImportSemicolonOption() {
		List<File> files = new ArrayList<File>();
		files.add(env.getDataFile("gvt/Semicolon.txt"));
		String[] cellCs = { "A", "B" };
		String fileName = Utils.uniqueBaseName("newSpreadsheetInFiles");
		spreadsheetFilePage.focus(filesPage.newSpreadsheet(fileName));
		int j = 0;
		for (File file : files) {
			spreadsheetFilePage.importFromSpreadsheet();
			spreadsheetFilePage.importFileSemicolonOption(file);
			int i = 1;
			log.info("Test DIS-UNI-010 at uploaded file: " + file.getName());
			for (String value : testData) {
				String cell = cellCs[j] + i;
				String actual = spreadsheetEditorPage.getCellText(cell);
				spreadsheetFilePage.clickCell(cell);
				log.info("Actual " + GUtils.getHexUnicode(actual) + " " + actual);
				ga.assertEquals("Test " + GUtils.getHexUnicode(value), value,
						actual);
				i++;
			}
			spreadsheetFilePage.clickCell("B1");
			j++;
		}
	}
	
	@Test
	public void atImportSpaceOption() {
		List<File> files = new ArrayList<File>();
		files.add(env.getDataFile("gvt/Space.txt"));
		String[] cellCs = { "A", "B" };
		String fileName = Utils.uniqueBaseName("newSpreadsheetInFiles");
		spreadsheetFilePage.focus(filesPage.newSpreadsheet(fileName));
		int j = 0;
		for (File file : files) {
			spreadsheetFilePage.importFromSpreadsheet();
			spreadsheetFilePage.importFileSpaceOption(file);
			int i = 1;
			log.info("Test DIS-UNI-010 at uploaded file: " + file.getName());
			for (String value : testData) {
				String cell = cellCs[j] + i;
				String actual = spreadsheetEditorPage.getCellText(cell);
				spreadsheetFilePage.clickCell(cell);
				log.info("Actual " + GUtils.getHexUnicode(actual) + " " + actual);
				ga.assertEquals("Test " + GUtils.getHexUnicode(value), value,
						actual);
				i++;
			}
			spreadsheetFilePage.clickCell("B1");
			j++;
		}
	}
	
	@Test
	public void atImportTabOption() {
		List<File> files = new ArrayList<File>();
		files.add(env.getDataFile("gvt/Tab.txt"));
		String[] cellCs = { "A", "B" };
		String fileName = Utils.uniqueBaseName("newSpreadsheetInFiles");
		spreadsheetFilePage.focus(filesPage.newSpreadsheet(fileName));
		int j = 0;
		for (File file : files) {
			spreadsheetFilePage.importFromSpreadsheet();
			spreadsheetFilePage.importFileTabOption(file);
			int i = 1;
			log.info("Test DIS-UNI-010 at uploaded file: " + file.getName());
			for (String value : testData) {
				String cell = cellCs[j] + i;
				String actual = spreadsheetEditorPage.getCellText(cell);
				spreadsheetFilePage.clickCell(cell);
				log.info("Actual " + GUtils.getHexUnicode(actual) + " " + actual);
				ga.assertEquals("Test " + GUtils.getHexUnicode(value), value,
						actual);
				i++;
			}
			spreadsheetFilePage.clickCell("B1");
			j++;
		}
	}
	
}
