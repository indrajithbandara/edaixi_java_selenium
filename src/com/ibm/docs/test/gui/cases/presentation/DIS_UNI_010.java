package com.ibm.docs.test.gui.cases.presentation;
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
import com.ibm.docs.test.gui.pages.FilesPage;
import com.ibm.docs.test.gui.pages.LoginPage;
import com.ibm.docs.test.gui.pages.PresentationEditorPage;
import com.ibm.docs.test.gui.support.Drive;
import com.ibm.docs.test.gui.support.EnhancedWebElement;


/**
 * Ensure that the complete Unicode character range (including currently
 * unassigned code points) can be received.
 * 
 * @author ganweibj@cn.ibm.com
 * 
 */

@GuideLink("https://labweb.torolab.ibm.com/gcoc/documents/gvtguide/testareas/DIS-UNI-010.htm")
public class DIS_UNI_010 extends BaseTest{
	@Drive
	private LoginPage loginPage;

	@Drive
	private FilesPage filesPage;

	@Drive
	private PresentationEditorPage presentationEditorPage;

	private static List<String> testData = new ArrayList<String>();
	private static List<String> testData2 = new ArrayList<String>();

	@BeforeClass
	public static void beforeClass() {
		int[] ids = { 3, 4, 5, 6, 7, 8, 9, 11, 40, 44, 45 };
		GData gData = GData.getInstance();
		for (int id : ids) {
			testData.add(gData.getDataById(id));
		}
		
		int[] ids2 = { 3, 4, 5, 6, 7, 8, 9, 40, 44, 45 };
		GData gData2 = GData.getInstance();
		for (int id : ids2) {
			testData2.add(gData.getDataById(id));
		}
	}

	@Before
	public void before() {
		loginPage.login();
		filesPage.go();
	}
	
	
	@Test
	public void titleUnicodeShow(){
		
		List<File> files = new ArrayList<File>();
		files.add(env.getDataFile("gvt/MSPPTX2013.pptx"));
		//files.add(env.getDataFile("gvt/MSPPT2013.ppt"));
		files.add(env.getDataFile("gvt/MSPPT2010.pptx"));
		//files.add(env.getDataFile("gvt/MSPPT2010.ppt"));
		files.add(env.getDataFile("gvt/symtitle.odp"));
		for (File file : files) {
			String fileName = Utils.uniqueBaseName(file);	
			String editorName = filesPage.upload(file, fileName).edit(fileName);
			presentationEditorPage.focus(editorName);	
			for(int i=1;i<=11;i++){
				presentationEditorPage.selectSlide(i);
				String titleContent = presentationEditorPage.getTextFromDrawFrameClass("title");
				String sTitle=testData.get(i-1);
				ga.assertEquals("Compare title",sTitle,titleContent);
				
			}
			filesPage.go();
		}
	
	}	
	
	@Test
	public void subTitleUnicodeShow(){
		
		
		List<File> files = new ArrayList<File>();
		files.add(env.getDataFile("gvt/MSPPTX2013subtitle.pptx"));
		//files.add(env.getDataFile("gvt/MSPPT2013subtitle.ppt"));
		files.add(env.getDataFile("gvt/MSPPT2010subtitle.pptx"));
		//files.add(env.getDataFile("gvt/MSPPT2010subtitle.ppt"));
		files.add(env.getDataFile("gvt/symsubtitle.odp"));
		for (File file : files) {
		
			String fileName = Utils.uniqueBaseName(file);	
			String editorName = filesPage.upload(file, fileName).edit(fileName);
			presentationEditorPage.focus(editorName);	
			for(int i=1;i<=11;i++){
				presentationEditorPage.selectSlide(i);
				String subTitleContent = presentationEditorPage.getTextFromDrawFrameClass("subtitle");
				String sTitle=testData.get(i-1);
				ga.assertEquals("Compare subtitle",sTitle,subTitleContent);
				
		}
			filesPage.go();
		}
	
	}
	@Test
	public void outlineUnicodeShow(){
		
		List<File> files = new ArrayList<File>();
		files.add(env.getDataFile("gvt/MSPPTX2013outline.pptx"));
		//files.add(env.getDataFile("gvt/MSPPT2013outline.ppt"));
		files.add(env.getDataFile("gvt/MSPPT2010outline.pptx"));
		//files.add(env.getDataFile("gvt/MSPPT2010outline.ppt"));
		files.add(env.getDataFile("gvt/symoutline.odp"));
		
		for (File file : files) {
			String fileName = Utils.uniqueBaseName(file);	
			String editorName = filesPage.upload(file, fileName).edit(fileName);
			presentationEditorPage.focus(editorName);	
			presentationEditorPage.selectSlide(2);
			String outLineContent = presentationEditorPage.getTextFromDrawFrameClass("outline");
			for (int i=1;i<=10;i++){
				String tData=testData2.get(i-1);
				boolean contains = outLineContent.contains(tData);
				ga.assertTrue(tData+"is shown incorrect", contains);
			}
			filesPage.go();
		}
	}

	@Test
	public void atTableUnicodeShow(){
		
		List<File> files = new ArrayList<File>();
		files.add(env.getDataFile("gvt/MSPPTX2013tableshow.pptx"));
		//files.add(env.getDataFile("gvt/MSPPT2013tableshow.ppt"));
		files.add(env.getDataFile("gvt/MSPPT2010tableshow.pptx"));
		//files.add(env.getDataFile("gvt/MSPPT2010tableshow.ppt"));
		files.add(env.getDataFile("gvt/symtableshow.odp"));
		for (File file : files) {
			String fileName = Utils.uniqueBaseName(file);	
			String editorName = filesPage.upload(file, fileName).edit(fileName);
			presentationEditorPage.focus(editorName);	
			presentationEditorPage.selectSlide(2);
			String tableContent = presentationEditorPage.getTextFromDrawFrameClass("table");
			for (int i=1;i<=11;i++){
				String tData=testData.get(i-1);
				boolean contains = tableContent.contains(tData);
				ga.assertTrue(tData+"is shown incorrect", contains);
			}
			filesPage.go();
		}
	}
	@Test
	public void atSpeakerNotesUnicodeShow(){
		
		List<File> files = new ArrayList<File>();
		files.add(env.getDataFile("gvt/MSPPTX2013speakernotes.pptx"));
		//files.add(env.getDataFile("gvt/MSPPT2013speakernotes.ppt"));
		files.add(env.getDataFile("gvt/MSPPT2010speakernotes.pptx"));
		//files.add(env.getDataFile("gvt/MSPPT2010speakernotes.ppt"));
		files.add(env.getDataFile("gvt/symspeakernotes.odp"));
		for (File file : files) {
			String fileName = Utils.uniqueBaseName(file);	
			String editorName = filesPage.upload(file, fileName).edit(fileName);
			presentationEditorPage.focus(editorName);	
			presentationEditorPage.selectSlide(1);
			String speakernotesContent = presentationEditorPage.getTextFromDrawFrameClass("notes");
			for (int i=1;i<=11;i++){
				String tData=testData.get(i-1);
				boolean contains = speakernotesContent.contains(tData);
				ga.assertTrue(tData+"is shown incorrect", contains);
			}
			filesPage.go();
		}
	}
	

}
