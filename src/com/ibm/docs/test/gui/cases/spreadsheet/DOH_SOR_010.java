package com.ibm.docs.test.gui.cases.spreadsheet;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import java.util.Collections;
import java.util.List;

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
import com.ibm.docs.test.gui.pages.SpreadsheetEditorPage;
import com.ibm.docs.test.gui.support.Drive;
import com.ibm.docs.test.gui.support.GLan;
import com.ibm.icu.util.*;
import com.ibm.icu.text.Collator;

/**
 * Ensure that the sorted results are language-specific, not code point order.
 * preferred by the end users.
 * 
 * @author shiyac@cn.ibm.com
 * 
 */
@GuideLink("https://labweb.torolab.ibm.com/gcoc/documents/gvtguide/testareas/DOH-SOR-010.htm")
public class DOH_SOR_010 extends BaseTest {

	@Drive
	private LoginPage loginPage;

	@Drive
	private FilesPage filesPage;

	@Drive
	private SpreadsheetEditorPage spreadsheetEditorPage;

	private static List<String> testData = new ArrayList<String>();

	@BeforeClass
	public static void beforeClass() {
		int[] ids = { 2, 3, 4, 5, 11, 40, 44, 61, 62, 82 };
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
	@GLan({ "pt-BR", "pt-PT", "bg-BG", "ca-ES", "cs-CZ", "da-DK", "nl-NL",
			"fi-FI", "fr-FR", "de-DE", "el-GR", "hu-HU", "it-IT", "ja-JP",
			"kk-KZ", "ko-KR", "nn-NO", "pl-PL", "ro-RO", "ru-RU", "zh-CN",
			"es-ES", "sv-SE", "th-TH", "zh-TW", "tr-TR", "hr-HR", "sk-SK",
			"sl-SL" })
	public void atCellASC() {
		String fileName = Utils.uniqueBaseName("newSpreadsheetInFiles");
		spreadsheetEditorPage.focus(filesPage.newSpreadsheet(fileName));
		log.info("Verify DOH-SOR-010 at Cell Ax by ascending order");
		int i = 1;
		for (String value : testData) {
			String cell = "A" + i;
			spreadsheetEditorPage.typeInCell(cell, value);
			i++;
		}
		spreadsheetEditorPage.selectRange("A1:A" + (i - 1));
		spreadsheetEditorPage.SortData(1);
		assertEquals("According to the ascending order:", sortByICU(1),
				sortByDocs(0));
	}

	@Test
	@GLan({ "pt-BR", "pt-PT", "bg-BG", "ca-ES", "cs-CZ", "da-DK", "nl-NL",
			"fi-FI", "fr-FR", "de-DE", "el-GR", "hu-HU", "it-IT", "ja-JP",
			"kk-KZ", "ko-KR", "nn-NO", "pl-PL", "ro-RO", "ru-RU", "zh-CN",
			"es-ES", "sv-SE", "th-TH", "zh-TW", "tr-TR", "hr-HR", "sk-SK",
			"sl-SL" })
	public void atCellDES() {
		String fileName = Utils.uniqueBaseName("newSpreadsheetInFiles");
		spreadsheetEditorPage.focus(filesPage.newSpreadsheet(fileName));
		log.info("Verify DOH-SOR-010 at Cell Ax by descending order");
		int i = 1;
		for (String value : testData) {
			String cell = "A" + i;
			spreadsheetEditorPage.typeInCell(cell, value);
			i++;
		}
		spreadsheetEditorPage.selectRange("A1:A" + (i - 1));
		spreadsheetEditorPage.SortData(0);
		assertEquals("According to the descending order:", sortByICU(0),
				sortByDocs(0));
	}

	@Test
	@GLan({ "pt-BR", "pt-PT", "bg-BG", "ca-ES", "cs-CZ", "da-DK", "nl-NL",
			"fi-FI", "fr-FR", "de-DE", "el-GR", "hu-HU", "it-IT", "ja-JP",
			"kk-KZ", "ko-KR", "nn-NO", "pl-PL", "ro-RO", "ru-RU", "zh-CN",
			"es-ES", "sv-SE", "th-TH", "zh-TW", "tr-TR", "hr-HR", "sk-SK",
			"sl-SL" })
	public void atFilterASC() {
		soryByFilter();
		spreadsheetEditorPage.filterASC.click();
		assertEquals("According to the ascending order:", sortByICU(1),
				sortByDocs(1));
	}

	@Test
	@GLan({ "pt-BR", "pt-PT", "bg-BG", "ca-ES", "cs-CZ", "da-DK", "nl-NL",
			"fi-FI", "fr-FR", "de-DE", "el-GR", "hu-HU", "it-IT", "ja-JP",
			"kk-KZ", "ko-KR", "nn-NO", "pl-PL", "ro-RO", "ru-RU", "zh-CN",
			"es-ES", "sv-SE", "th-TH", "zh-TW", "tr-TR", "hr-HR", "sk-SK",
			"sl-SL" })
	public void atFilterDES() {
		soryByFilter();
		spreadsheetEditorPage.filterDes.click();
		assertEquals("According to the descending order:", sortByICU(0),
				sortByDocs(1));
	}

	public ArrayList<String> sortByDocs(int cellNumber) {
		ArrayList<String> sortResult = new ArrayList<String>();
		if (cellNumber == 0) {
			for (int i = cellNumber; i < testData.size(); i++) {
				sortResult
						.add(spreadsheetEditorPage.getCellText("A" + (i + 1)));
			}
		} else {
			for (int i = cellNumber; i <= testData.size(); i++) {
				sortResult
						.add(spreadsheetEditorPage.getCellText("A" + (i + 1)));
			}
		}

		return sortResult;
	}

	public ArrayList<String> sortByICU(int order) {
		String localeID = GUtils
				.getBrowserPreferenceLan(spreadsheetEditorPage.driver);
		ULocale locale = new ULocale(localeID);
		Collator collator = Collator.getInstance(locale);
		ArrayList<String> icuResult = new ArrayList<String>();
		for (String value : testData) {
			icuResult.add(value);
		}
		if (order == 1) {
			Collections.sort(icuResult, collator);
		} else {
			Collections.sort(icuResult, Collections.reverseOrder(collator));
		}
		return icuResult;
	}

	public void soryByFilter() {
		String fileName = Utils.uniqueBaseName("newSpreadsheetInFiles");
		spreadsheetEditorPage.focus(filesPage.newSpreadsheet(fileName));
		log.info("Verify DOH-SOR-010 at Cell Ax");
		spreadsheetEditorPage.typeInCell("A1", "Title1");
		spreadsheetEditorPage.typeInCell("B1", "Title2");
		int i = 2;
		for (String value : testData) {
			String cell_1 = "A" + i;
			String cell_2 = "B" + i;
			spreadsheetEditorPage.typeInCell(cell_1, value);
			spreadsheetEditorPage.typeInCell(cell_2, String.valueOf(i));
			i++;
		}
		spreadsheetEditorPage.columnHeaders.get(0).click();
		spreadsheetEditorPage.toolBarInstantFilter.click();
		spreadsheetEditorPage.filterArrow.click();
	}

}
