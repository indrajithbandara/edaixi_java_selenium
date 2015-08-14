package com.ibm.docs.test.gui.cases.spreadsheet;

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
import com.ibm.docs.test.gui.pages.SpreadsheetEditorPage;
import com.ibm.docs.test.gui.pages.DocsEditorPage;
import com.ibm.docs.test.gui.support.Drive;
import com.ibm.docs.test.gui.support.GLan;
import com.ibm.icu.text.DateFormat;
import com.ibm.icu.util.ULocale;

/**
 * Ensure that dates and times are formatted appropriately based on the cultural
 * preferences.
 * 
 * @author weigan@cn.ibm.com
 * 
 */
@GuideLink("https://labweb.torolab.ibm.com/gcoc/documents/gvtguide/testareas/DOH-DAT-030.htm")
public class DOH_DAT_030 extends BaseTest {

	@Drive
	private LoginPage loginPage;

	@Drive
	private FilesPage filesPage;

	@Drive
	private SpreadsheetEditorPage spreadsheetEditorPage;
	
	@Drive
	private DocsEditorPage  DocsEditorPage;
	
	

	@Before
	public void before() {
		loginPage.login();
		filesPage.go();
	}
	
	

	@Test
	
	@TestDoc("defect 42879，42878,42877,42876,42874,42733")
	
	//@GLan({ "pt-BR", "pt-PT", "bg-BG", "ca-ES", "cs-CZ", "da-DK", "nl-NL",
		//"fi-FI", "fr-FR", "de-DE", "el-GR", "hu-HU", "it-IT", "ja-JP",
		//"kk-KZ", "ko-KR", "nn-NO", "pl-PL", "ro-RO", "ru-RU", "zh-CN",
		//"es-ES", "sv-SE", "th-TH", "zh-TW", "tr-TR", "hr-HR", "sk-SK",
		//"sl-SL" })
	
	@GLan({ "zh-TW"})
	public void atToolBar() throws Exception {
		
		log.info("Verify DOH-DAT-030 at toolbar number");
		String fileName = Utils.uniqueBaseName("newSpreadsheetInFiles");
		spreadsheetEditorPage.focus(filesPage.newSpreadsheet(fileName));
		spreadsheetEditorPage.selectToolBarNumber();
		String localeID = GUtils.getBrowserPreferenceLan(spreadsheetEditorPage.driver);
		
		//verify the date format
		if (localeID.equals("ja-JP") || localeID.equals("hu-HU") ||localeID.equals("fi-FI")||localeID.equals("ro-RO")||localeID.equals("sk-SK")||localeID.equals("zh-TW")){
			String dateTime1 = spreadsheetEditorPage.toolBarDateTimeformat1.getText();
			String dateTime3 = spreadsheetEditorPage.toolBarDateTimeformat3.getText();
			String formatDate1=GUtils.getFormattedDate("5/21/00", 3, localeID);
			String formatDate3=GUtils.getFormattedDateAndTime("5/21/00 1:30 PM", 3,localeID);
			ga.assertEquals("Short Date on Toolbar is failed.",formatDate1,dateTime1);
			ga.assertEquals("Date and Time on Toolbar is failed.",formatDate3,dateTime3);
			return;	
		}
		
		String dateTime1 = spreadsheetEditorPage.toolBarDateTimeformat1.getText();
		String dateTime2 = spreadsheetEditorPage.toolBarDateTimeformat2.getText();
		String dateTime3 = spreadsheetEditorPage.toolBarDateTimeformat3.getText();
		String formatDate1=GUtils.getFormattedDate("5/21/00", 3, localeID);
		String formatDate2=GUtils.getFormattedDate("5/21/00", 2, localeID);
		String formatDate3=GUtils.getFormattedDateAndTime("5/21/00 1:30 PM", 3,localeID);
		ga.assertEquals("Short Date on Toolbar is failed.",formatDate1,dateTime1);
		ga.assertEquals("Medium Date on Toolbar is failed.",formatDate2,dateTime2);
		ga.assertEquals("Date and Time on Toolbar is failed.",formatDate3,dateTime3);
		
		//verify the time format
		if (spreadsheetEditorPage.toolBarDateTimeformat5.isPresent()){
			
			String dateTime4 = spreadsheetEditorPage.toolBarDateTimeformat4.getText();
			String dateTime5 = spreadsheetEditorPage.toolBarDateTimeformat5.getText();
			DateFormat df12 = DateFormat.getPatternInstance(DateFormat.HOUR_MINUTE, new ULocale(localeID));
			Date dt12 = new Date();
			dt12.setHours(13);
			dt12.setMinutes(30);
			String sformattime4 = df12.format(dt12);
			DateFormat df = DateFormat.getPatternInstance(DateFormat.HOUR24_MINUTE, new ULocale(localeID));
			Date dt = new Date();
			dt.setHours(13);
			dt.setMinutes(30);
			String sformattime5 = df.format(dt);
			ga.assertEquals("Time on Toolbar is failed.",sformattime4,dateTime4);
			ga.assertEquals("24Time on Toolbar is failed.",sformattime5,dateTime5);
			
		}else{
			String stime = spreadsheetEditorPage.toolBarDateTimeformat4.getText();
			ULocale locale = new ULocale(localeID);
			DateFormat df = DateFormat.getPatternInstance(DateFormat.HOUR24_MINUTE, locale);
			Date dt = new Date();
			dt.setHours(13);
			dt.setMinutes(30);
			String sformattime = df.format(dt);
			ga.assertEquals("24Time on Toolbar is failed.",sformattime,stime);
			
		}
			
		
	}
	
	
	@Test
	@TestDoc("defect 42879，42878,42877,42876,42874,42733")
	
	//@GLan({ "pt-BR", "pt-PT", "bg-BG", "ca-ES", "cs-CZ", "da-DK", "nl-NL",
		//"fi-FI", "fr-FR", "de-DE", "el-GR", "hu-HU", "it-IT", "ja-JP", "ko-KR", "nn-NO", "pl-PL", "ro-RO", "ru-RU", "zh-CN",
		//"es-ES", "sv-SE", "th-TH", "zh-TW", "tr-TR", "hr-HR", "sk-SK",
		//"sl-SL" })
	@GLan({ "nl-NL"})
	//th-TH use Buddhist,remove it.
	//kk-kz,ru-ru only has one date.
	
	public void atSubToolBar() {
		
		log.info("Verify DOH-DAT-030 at subtoolbar number");
		String fileName = Utils.uniqueBaseName("newSpreadsheetInFiles");
		spreadsheetEditorPage.focus(filesPage.newSpreadsheet(fileName));
		spreadsheetEditorPage.selectToolBarMoreDateTime();
		String dateTime1 = spreadsheetEditorPage.subToolBarDateTimeformat1.getText();
		String dateTime2 = spreadsheetEditorPage.subToolBarDateTimeformat2.getText();
		String dateTime3 = spreadsheetEditorPage.subToolBarDateTimeformat3.getText();
		//String dateTime4 = spreadsheetEditorPage.subToolBarDateTimeformat4.getText();
		//String dateTime5 = spreadsheetEditorPage.subToolBarDateTimeformat5.getText();
		//String dateTime6 = spreadsheetEditorPage.subToolBarDateTimeformat6.getText();
        String localeID = GUtils.getBrowserPreferenceLan(spreadsheetEditorPage.driver);
        
        //Verify the date format
		String formatDate1=GUtils.getFormattedDate("5/21/00", 0, localeID);
		
		DateFormat df2 = DateFormat.getPatternInstance("YYYY MMM", new ULocale(localeID));
		Date dt2 = new Date();
		dt2.setYear(100);
		dt2.setMonth(4);
		String formatDate2 = df2.format(dt2);
		
		DateFormat df3 = DateFormat.getPatternInstance("MMM d", new ULocale(localeID));
		Date dt3 = new Date();
		dt3.setMonth(4);
		dt3.setDate(21);
		String formatDate3 = df3.format(dt3);
		
		ga.assertEquals("Full date in subtoolbar is not correct.",formatDate1,dateTime1);
		ga.assertEquals("Year+Month in subtoolbar is not correct.",formatDate2,dateTime2);
		ga.assertEquals("Month+Day in subtoolbar is not correct.",formatDate3,dateTime3);
		
		
		//verify the time format
			
		if (spreadsheetEditorPage.subToolBarDateTimeformat5.isPresent()){
	
			String dateTime4 = spreadsheetEditorPage.subToolBarDateTimeformat4.getText();
			String dateTime5 = spreadsheetEditorPage.subToolBarDateTimeformat5.getText();
			DateFormat df4 = DateFormat.getPatternInstance(DateFormat.HOUR_MINUTE_SECOND, new ULocale(localeID));
			Date dt4 = new Date();
			dt4.setHours(13);
			dt4.setMinutes(30);
			dt4.setSeconds(17);
			String sformattime4 = df4.format(dt4);
			DateFormat df = DateFormat.getPatternInstance(DateFormat.HOUR24_MINUTE_SECOND, new ULocale(localeID));
			Date dt = new Date();
			dt.setHours(13);
			dt.setMinutes(30);
			dt.setSeconds(17);
			String sformattime5 = df.format(dt);
	
			ga.assertEquals("Time on subToolbar is failed.",sformattime4,dateTime4);
			ga.assertEquals("24Time on subToolbar is failed.",sformattime5,dateTime5);	
		}else
		{
			String stime = spreadsheetEditorPage.subToolBarDateTimeformat4.getText();
			DateFormat dfs = DateFormat.getPatternInstance(DateFormat.HOUR24_MINUTE_SECOND, new ULocale(localeID));
			Date dt = new Date();
			dt.setHours(13);
			dt.setMinutes(30);
			dt.setSeconds(17);
			String sformattime = dfs.format(dt);
			ga.assertEquals("24Time on subToolbar is failed.",sformattime,stime);
			
		}
			
	}
	

	
	@Test
	@TestDoc("defect 42879，42878,42877,42876,42874,42733")
	//@GLan({ "pt-BR", "pt-PT", "bg-BG", "ca-ES", "cs-CZ", "da-DK", "nl-NL",
		//"fi-FI", "fr-FR", "de-DE", "el-GR", "hu-HU", "it-IT", "ja-JP",
		//"kk-KZ", "ko-KR", "nn-NO", "pl-PL", "ro-RO", "ru-RU", "zh-CN",
		//"es-ES", "sv-SE", "th-TH", "zh-TW", "tr-TR", "hr-HR", "sk-SK",
		//"sl-SL" })
	
	@GLan({ "fi-FI"})
	public void atMenuBar() throws Exception{
		
		log.info("Verify DOH-DAT-030 at Format Menu");
		String fileName = Utils.uniqueBaseName("newSpreadsheetInFiles");
		spreadsheetEditorPage.focus(filesPage.newSpreadsheet(fileName));
		spreadsheetEditorPage.selectFormatNumber();
		String localeID = GUtils.getBrowserPreferenceLan(spreadsheetEditorPage.driver);
		if (localeID.equals("ja-JP") || localeID.equals("hu-HU") ||localeID.equals("fi-FI")||localeID.equals("ro-RO")||localeID.equals("sk-SK")||localeID.equals("zh-TW")){
			String dateTime1 = spreadsheetEditorPage.menuDateTimeformat1.getText();
			String dateTime3 = spreadsheetEditorPage.menuDateTimeformat3.getText();
			String formatDate1=GUtils.getFormattedDate("5/21/00", 3, localeID);
			String formatDate3=GUtils.getFormattedDateAndTime("5/21/00 1:30 PM", 3,localeID);
			ga.assertEquals("Short Date on format menu is failed.",formatDate1,dateTime1);
			ga.assertEquals("Date and Time on format menu is failed.",formatDate3,dateTime3);
			return;	
		}
		
		String dateTime1 = spreadsheetEditorPage.menuDateTimeformat1.getText();
		String dateTime2 = spreadsheetEditorPage.menuDateTimeformat2.getText();
		String dateTime3 = spreadsheetEditorPage.menuDateTimeformat3.getText();
		String formatDate1=GUtils.getFormattedDate("5/21/00", 3, localeID);
		String formatDate2=GUtils.getFormattedDate("5/21/00", 2, localeID);
		String formatDate3=GUtils.getFormattedDateAndTime("5/21/00 1:30 PM", 3,localeID);
		ga.assertEquals("Short Date on format menu is failed.",formatDate1,dateTime1);
		ga.assertEquals("Medium Date on format menu is failed.",formatDate2,dateTime2);
		ga.assertEquals("Date and Time on format menu is failed.",formatDate3,dateTime3);	
		
		//verify the time format
		if (spreadsheetEditorPage.menuDateTimeformat5.isPresent()){
			
			String dateTime4 = spreadsheetEditorPage.menuDateTimeformat4.getText();
			String dateTime5 = spreadsheetEditorPage.menuDateTimeformat5.getText();
			DateFormat df12 = DateFormat.getPatternInstance(DateFormat.HOUR_MINUTE, new ULocale(localeID));
			Date dt12 = new Date();
			dt12.setHours(13);
			dt12.setMinutes(30);
			String sformattime4 = df12.format(dt12);
			DateFormat df = DateFormat.getPatternInstance(DateFormat.HOUR24_MINUTE, new ULocale(localeID));
			Date dt = new Date();
			dt.setHours(13);
			dt.setMinutes(30);
			String sformattime5 = df.format(dt);
			ga.assertEquals("Time on menu is failed.",sformattime4,dateTime4);
			ga.assertEquals("24Time on menu is failed.",sformattime5,dateTime5);
			
		}else{
			String stime = spreadsheetEditorPage.menuDateTimeformat4.getText();
			ULocale locale = new ULocale(localeID);
			DateFormat df = DateFormat.getPatternInstance(DateFormat.HOUR24_MINUTE, locale);
			Date dt = new Date();
			dt.setHours(13);
			dt.setMinutes(30);
			String sformattime = df.format(dt);
			ga.assertEquals("24Time on menu is failed.",sformattime,stime);
			
		}

		
		
		
	}
	
	@Test
	@TestDoc("defect 42879，42878,42877,42876,42874,42733")
	//@GLan({ "pt-BR", "pt-PT", "bg-BG", "ca-ES", "cs-CZ", "da-DK", "nl-NL",
		//"fi-FI", "fr-FR", "de-DE", "el-GR", "hu-HU", "it-IT", "ja-JP",
		//"kk-KZ", "ko-KR", "nn-NO", "pl-PL", "ro-RO", "ru-RU", "zh-CN",
		//"es-ES", "sv-SE", "th-TH", "zh-TW", "tr-TR", "hr-HR", "sk-SK",
		//"sl-SL" })
	@GLan({ "fr-FR"})
	//th-TH use Buddhist,remove it.
	//kk-kz,ru-ru only has 2 dates,remove it,manul testing.
	public void atSubMenuMoreDateTime() {
	
	log.info("Verify DOH-DAT-030 at submenu date time");
	String fileName = Utils.uniqueBaseName("newSpreadsheetInFiles");
	spreadsheetEditorPage.focus(filesPage.newSpreadsheet(fileName));
	spreadsheetEditorPage.selectFormatNumberMoredate();
	String dateTime1 = spreadsheetEditorPage.subMenuDateTimeformat1.getText();
	String dateTime2 = spreadsheetEditorPage.subMenuDateTimeformat2.getText();
	String dateTime3 = spreadsheetEditorPage.subMenuDateTimeformat3.getText();
    String localeID = GUtils.getBrowserPreferenceLan(spreadsheetEditorPage.driver);
    
  //Verify the date format
	String formatDate1=GUtils.getFormattedDate("5/21/00", 0, localeID);
	DateFormat df2 = DateFormat.getPatternInstance("YYYY MMM", new ULocale(localeID));
	Date dt2 = new Date();
	dt2.setYear(100);
	dt2.setMonth(4);
	String formatDate2 = df2.format(dt2);
	
	DateFormat df3 = DateFormat.getPatternInstance("MMM d", new ULocale(localeID));
	Date dt3 = new Date();
	dt3.setMonth(4);
	dt3.setDate(21);
	String formatDate3 = df3.format(dt3);
	ga.assertEquals("Full date in sub toolbar is not correct.",formatDate1,dateTime1);	
	ga.assertEquals("Year+Month  in sub toolbar is not correct.",formatDate2,dateTime2);	
	ga.assertEquals("Month+Day  in sub toolbar is not correct.",formatDate3,dateTime3);	
	
	//verify the time format
	
			if (spreadsheetEditorPage.subMenuDateTimeformat5.isPresent()){
		
				String dateTime4 = spreadsheetEditorPage.subMenuDateTimeformat4.getText();
				String dateTime5 = spreadsheetEditorPage.subMenuDateTimeformat5.getText();
				DateFormat df4 = DateFormat.getPatternInstance(DateFormat.HOUR_MINUTE_SECOND, new ULocale(localeID));
				Date dt4 = new Date();
				dt4.setHours(13);
				dt4.setMinutes(30);
				dt4.setSeconds(17);
				String sformattime4 = df4.format(dt4);
				DateFormat df = DateFormat.getPatternInstance(DateFormat.HOUR24_MINUTE_SECOND, new ULocale(localeID));
				Date dt = new Date();
				dt.setHours(13);
				dt.setMinutes(30);
				dt.setSeconds(17);
				String sformattime5 = df.format(dt);
				ga.assertEquals("Time on submenu is failed.",sformattime4,dateTime4);
				ga.assertEquals("24Time on submemu is failed.",sformattime5,dateTime5);	
			}else
			{
				String stime = spreadsheetEditorPage.subMenuDateTimeformat4.getText();
				DateFormat dfs = DateFormat.getPatternInstance(DateFormat.HOUR24_MINUTE_SECOND, new ULocale(localeID));
				Date dt = new Date();
				dt.setHours(13);
				dt.setMinutes(30);
				dt.setSeconds(17);
				String sformattime = dfs.format(dt);
				ga.assertEquals("24Time on submenu is failed.",sformattime,stime);
				
			}

	
}
	
	/*
	
	@Test
	@TestDoc(" defect 42935 - [GVT][common][HR][ES][BG]Date format in Comments is not correct" +
			" defect 42938 - [GVT][common][HU]Lost space in Medium Date pattern for HU.")
	@GLan({ "pt-BR","bg-BG", "ca-ES", "cs-CZ", "da-DK", "nl-NL",
		"fi-FI", "fr-FR", "de-DE", "el-GR", "it-IT", "ja-JP",
		"kk-KZ", "ko-KR", "nn-NO", "pl-PL", "ru-RU", "zh-CN",
		"es-ES", "sv-SE", "th-TH", "tr-TR", "hr-HR", "sk-SK",
		"sl-SL","pt-PT" })
	
	public void atCommentDateTime(){
		log.info("Verify DOH-DAT-030 at comments date time");
		String localeID = GUtils.getBrowserPreferenceLan(spreadsheetEditorPage.driver);
		String fileName = Utils.uniqueBaseName("newSpreadsheetInFiles");
		spreadsheetEditorPage.focus(filesPage.newSpreadsheet(fileName));
		DocsEditorPage.viewSidebar(true);
		DocsEditorPage.addComment("abc");
		String expected="";
		if (localeID.equals("hu-HU")||localeID.equals("ro-RO")||localeID.equals("zh-TW")){
			expected = GUtils.getLocalDate(3, localeID);
		}else {
			expected = GUtils.getLocalDate(2, localeID);
		}
		String commentStr = DocsEditorPage.comment.getText();
		boolean contains = commentStr.contains(expected);
		ga.assertTrue(localeID + " expected date: " + expected, contains);
		
		//DocsEditorPage.expandComments();
	}
	*/
	
	@Test
	@GLan({ "fr-FR"})
	public void atCommentDateTimeNew(){
		
		log.info("Verify DOH-DAT-030 at comments date time");
		String localeID = GUtils.getBrowserPreferenceLan(spreadsheetEditorPage.driver);
		String fileName = Utils.uniqueBaseName("newSpreadsheetInFiles");
		spreadsheetEditorPage.focus(filesPage.newSpreadsheet(fileName));
		DocsEditorPage.commentBtnNew.click();
		String expectedTime = GUtils.getLocalTime(3, localeID);
		String expectedDate = GUtils.getLocalDate(2, localeID);
		String ss = DocsEditorPage.commentDialogTime.getText();
		boolean containsDate = ss.contains(expectedDate);
		ga.assertTrue("Date is shown incorrect", containsDate);
		
		//DateFormat df =DateFormat.getDateTimeInstance(2, 2, new ULocale(localeID));
		// try {
			//Date myDate = df.parse(ss);
			//System.out.println("myDate = "+myDate.toString());
			
		//} catch (ParseException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		//}
		
		//boolean containsTime = ss.contains(expectedTime);
	    //ga.assertTrue("Time is shown incorrect", containsTime);
		
		//DocsEditorPage.addCommentNew("abc");
		
	}
	
	
	@Test
	@GLan({ "ko-KR"})
	public void atCommentTimeNew(){
		
		log.info("Verify DOH-DAT-030 at comments date time");
		String localeID = GUtils.getBrowserPreferenceLan(spreadsheetEditorPage.driver);
		String fileName = Utils.uniqueBaseName("newSpreadsheetInFiles");
		spreadsheetEditorPage.focus(filesPage.newSpreadsheet(fileName));
		DocsEditorPage.addCommentNew("abc");
		String expectedTime = GUtils.getLocalTime(3, localeID);
		System.out.println("expectedTime = "+expectedTime);
		
		String ss = DocsEditorPage.commentDialogTime.getText();
		System.out.println("ss = "+ss);
		
		boolean containsTime = ss.contains(expectedTime);
		ga.assertTrue("Time is shown incorrect", containsTime);
		
		
	}
	
}
