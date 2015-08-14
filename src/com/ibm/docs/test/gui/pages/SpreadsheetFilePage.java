package com.ibm.docs.test.gui.pages;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import com.ibm.docs.test.gui.support.EnhancedWebDriver;
import com.ibm.docs.test.gui.support.EnhancedWebElement;
import com.ibm.docs.test.gui.support.EnhancedWebElements;
import com.ibm.docs.test.gui.support.Find;

public class SpreadsheetFilePage extends DocsEditorPage {
	public final static String SuffixSpreadsheet = ".xls";
	private static final Logger log = Logger
			.getLogger(SpreadsheetFilePage.class);
	private static Map<COLOR, String[]> colorMapSpreadsheet = new HashMap<COLOR, String[]>();
	private int currentSheet = 1; // Sheet1/Sheet2...
	private int currentRow = 1;
	private int currentCol = 1;
	private String currentCell = null;

	@Find("css=#formulaBar_node input.namebox")
	public EnhancedWebElement nameBox;
	@Find("css=#formulaInputLind")
	public EnhancedWebElement allFormulasBtn;
	@Find("css=#formulaInputLine")
	public EnhancedWebElement formulaBar;
	@Find("css=#C_d_ConfirmBoxOKButton")
	public EnhancedWebElement OK; // OK button in message box
	@Find("css=#C_d_ConfirmBoxCancelButton")
	public EnhancedWebElement Cancel; // Cancel button in message box

	@Find("css=#S_d_InsertTextFile")
	private EnhancedWebElement importTextDialogFile;
	@Find("css=#S_d_ImportTextOKButton_label")
	public EnhancedWebElement importTextDlgOk;

	public SpreadSheetMenu menu = new SpreadSheetMenu(driver);
	public SpreadSheetBasicToolbar basictoolbar = new SpreadSheetBasicToolbar(
			driver);
	private SimpleInputDialog simpleInputDialog = new SimpleInputDialog(driver);
	private SimpleInformationDialog simpleInformationDialog = new SimpleInformationDialog(
			driver);
	private SelectFunctionListDialog selectFunctionListDialog = new SelectFunctionListDialog(
			driver);
	private InstantFilterPanel instantfilter = new InstantFilterPanel(driver);
	private FindAndReplaceDialog findAndReplaceDialog = new FindAndReplaceDialog(
			driver);
	private SpreadSheetSetLocaleDialog setLocaleDialog = new SpreadSheetSetLocaleDialog(
			driver);
	private NewNamedRangeDialog newNamedRange = new NewNamedRangeDialog(driver);
	private ManageNamedRangesDialog manageNamedRanges = new ManageNamedRangesDialog(
			driver);
	private SelectSortingOptionDialog selectSortingOptionDialog = new SelectSortingOptionDialog(
			driver);
	private MergeCellsDialog mergeCellsDialog = new MergeCellsDialog(driver);

	// Team menu in spreadsheet
	@Find("id=S_i_AssignCells")
	public EnhancedWebElement menuAssignCells;
	@Find("id=S_i_MarkAssignmentComplete")
	public EnhancedWebElement menuMarkAssignmentComplete;
	@Find("id=S_i_ApproveAssignment")
	public EnhancedWebElement menuApproveAssignment;
	@Find("id=S_i_ReturnAssignmentForRework")
	public EnhancedWebElement menuReturnAssignmentForRework;
	@Find("id=S_i_RemoveCellAssignment")
	public EnhancedWebElement menuRemoveCellAssignment;
	@Find("id=S_i_RemoveCompletedAssignments")
	public EnhancedWebElement menuRemoveCompletedAssignments;
	@Find("id=S_i_AboutAssignment")
	public EnhancedWebElement menuAboutAssignment;

	// Get spreadsheet sheet area web elements
	@Find("css=body")
	public EnhancedWebElement body;
	@Find("css=#websheet_grid_DataGrid_$0 div#websheet_grid__View_$1 div.dojoxGridContent")
	public EnhancedWebElement sheetArea;
	@Find("css=#websheet_grid__View_$0 td")
	public EnhancedWebElements firstRowIndexList;
	@Find("css=#websheet_grid__View_$0 table.dojoxGridRowTable tr:nth-child($1) td:nth-child($2)")
	public EnhancedWebElement cell;
	@Find("css=#websheet_grid_DataGrid_$0 div.dojoxGridMasterHeader")
	public EnhancedWebElement columnHeader;
	@Find("css=div.dijitTabContainerBottom-tabs div.dijitTabContent")
	public EnhancedWebElements sheetTabs1;
	@Find("id=websheet_layout_WorksheetContainer_0_tablist_websheet_grid_DataGrid_$0")
	public EnhancedWebElement sheetTab;

	public SpreadsheetFilePage(EnhancedWebDriver driver) {
		super(driver);
	}

	private WebElement gwSheetArea(int sheetNo) {
		sheetArea.setLocatorArgument(sheetNo - 1, sheetNo * 4 - 1);
		return sheetArea;
	}

	private WebElement gwSheetArea() {
		return gwSheetArea(currentSheet);
	}

	public WebElement gwCell2(int row, int column) {
		int i = row - getFirstRowIndex() + 1;
		cell.setLocatorArgument(currentSheet * 4 - 1, i, column);
		cell.getCoordinates().inViewPort();
		return cell;
	}

	private List<WebElement> gwRows() {
		List<WebElement> rowlists = null;
		List<WebElement> gridlists = gwSheetArea().findElements(
				By.className("dojoxGridRowTable"));
		for (int i = 0; i < gridlists.size(); i++)
			if (gridlists.get(i).isDisplayed()) {
				sleep(2);
				int tbody = gridlists.get(i).findElements(By.tagName("tr"))
						.size();
				if (tbody > 1) {
					rowlists = gridlists.get(i).findElements(By.tagName("tr"));
					break;
				}
			}
		sleep(2);
		return rowlists;
	}

	private WebElement gwRow(int rowIndex) {
		return gwRows().get(rowIndex);
	}

	private WebElement gwCurrentRow() {
		return gwRow(currentRow);
	}

	private int getFirstRowIndex() {
		int firstRow = 1;
		sleep(1);
		firstRowIndexList.setLocatorArgument(currentSheet * 4 - 2);
		String firstRowIndex = firstRowIndexList.get(0).getText();
		if (!firstRowIndex.equalsIgnoreCase(""))
			firstRow = Integer.parseInt(firstRowIndex);
		if (firstRow > 1)
			log.debug("The first row index is " + firstRow);
		sleep(1);
		return firstRow;
	}

	private List<WebElement> gwCols(int row) {
		int i = row - getFirstRowIndex() + 1;
		List<WebElement> collists = gwRows().get(i).findElements(
				By.tagName("td"));
		sleep(1);
		return collists;
	}

	private WebElement gwCell(int row, int column) {
		List<WebElement> collist = gwCols(row);
		WebElement cellElement = collist.get(column - 1);
		return cellElement;
	}

	private List<WebElement> gwColsByXpath() {
		String col_xpath = "websheet_grid_DataGrid_" + (currentSheet - 1)
				+ "Hdr" + currentCol;
		List<WebElement> colLists = driver.findElements(By
				.xpath("//th[contains(@id, " + col_xpath + ")]"));
		return colLists;
	}

	private List<WebElement> gwRowsHeaderByXpath() {
		List<WebElement> rowLists = driver.findElements(By
				.xpath("//td[contains(@class, 'rowIndex')]"));
		return rowLists;
	}

	private List<WebElement> gwColHeaders() {
		String colid = "//th[contains(@id, 'websheet_grid_DataGrid_"
				+ (currentSheet - 1) + "Hdr')]";
		columnHeader.setLocatorArgument(currentSheet - 1);
		return columnHeader.findElements(By.xpath(colid));
	}

	private WebElement gwSheetTab(int index) {
		sheetTab.setLocatorArgument(index - 1);
		return sheetTab;
	}

	public SpreadSheetMenu menu() {
		return menu;
	}

	public SpreadSheetBasicToolbar basicToolBar() {
		return basictoolbar;
	}

	public SimpleInputDialog simpleInputDialog() {
		return simpleInputDialog;
	}

	public SimpleInformationDialog simpleInformationDialog() {
		return simpleInformationDialog;
	}

	public InstantFilterPanel instantFilter() {
		return instantfilter;
	}

	public FindAndReplaceDialog findAndReplaceDialog() {
		return findAndReplaceDialog;
	}

	public ManageNamedRangesDialog manageNamedRangesDialog() {
		return manageNamedRanges;
	}

	public NewNamedRangeDialog newNamedRangeDialog() {
		return newNamedRange;
	}

	public SelectFunctionListDialog selectFunctionListDialog() {
		return selectFunctionListDialog;
	}

	public SpreadSheetSetLocaleDialog setLocaleDialog() {
		return setLocaleDialog;
	}

	public SelectSortingOptionDialog selectSortingOptionDialog() {
		return selectSortingOptionDialog;
	}

	public MergeCellsDialog mergeCellsDialog() {
		return mergeCellsDialog;
	}

	// Methods in spreadsheet
	/**
	 * Verify method
	 */
	public void verifyTrue(boolean trueorfalse, String logMessage) {
		if (trueorfalse) {
			log.info("Success : " + logMessage);
		} else {
			log.error("Failed : " + logMessage);
		}
		assertEquals(true, trueorfalse);
	}

	public void verifyFalse(boolean trueorfalse, String logMessage) {
		if (!trueorfalse) {
			log.info("Success : " + logMessage);
		} else {
			log.error("Failed : " + logMessage);
		}
		assertEquals(false, trueorfalse);
	}

	/**
	 * Focus on cell by name box
	 * 
	 * @param cell
	 * @return
	 */
	public SpreadsheetFilePage cell(String cell) {
		log.info("Move focus on cell " + cell);
		currentRow = getRow(cell);
		currentCol = columnHeaderToIndex(getCol(cell));
		currentCell = cell;
		nameBox.clear();
		nameBox.sendKeys(cell + Keys.RETURN);
		sleep(1);
		return this;
	}

	/**
	 * Move focus to cell(row,column) by name box, cell(3,1) means cell(B4)
	 * 
	 * @param row
	 *            start from 0
	 * @param column
	 *            start from 0
	 * @return
	 */
	public SpreadsheetFilePage cell(int row, int column) {
		currentRow = row + 1;
		currentCol = column + 1;
		currentCell = columnHeaderToString(currentCol) + currentRow;
		nameBox.clear();
		nameBox.sendKeys(currentCell + Keys.RETURN);
		sleep(1);
		return this;
	}

	/**
	 * Share in Editor by File->Share menu item.
	 * 
	 * @param coeditor
	 * @return
	 */
	public SpreadsheetFilePage shareInEditor(String coeditor) {
		log.info(String.format("Share a file to %s by File->Share", coeditor));
		menu().shareWith();
		shareInEditorDlg(coeditor);
		return this;
	}

	/**
	 * Select all the cells between start cell and end cell
	 * 
	 * @param cells
	 *            , the cells format should be A1:C5
	 * @return SpreadsheetFilePages
	 */
	public SpreadsheetFilePage cells(String cells) {
		log.info("By namebox, select cell range: " + cells);
		if (cells.indexOf(":") < 0) {
			this.verifyTrue(false,
					"The input format is not correct when select cells");
		}
		// select the start cell as the current cell
		String cell = cells.split(":")[0];
		currentRow = getRow(cell);
		currentCol = columnHeaderToIndex(getCol(cell));
		currentCell = cell;
		nameBox.clear();
		nameBox.sendKeys(cells + Keys.RETURN);
		sleep(1);
		return this;
	}

	/**
	 * Get web element of cell
	 * 
	 * @param cell
	 * @return
	 * 
	 */
	public WebElement getCell(String cell) {
		sleep(1);
		int row = getRow(cell);
		int col = columnHeaderToIndex(getCol(cell));
		return gwCell(row, col);
	}

	/**
	 * get current cell web element
	 * 
	 * @return webElement: currentCell
	 */
	private WebElement getCurrentCell() {
		return gwCell2(currentRow, currentCol);
	}

	private WebElement getCell(int row, int col) {
		sleep(1);
		return gwCell(row, col);
	}

	public SpreadsheetFilePage setCurrentCell(int row, int col) {
		this.currentRow = row;
		this.currentCol = col;
		return this;
	}

	public WebElement getFocusedCell() {
		List<WebElement> rowlists = gwRows();
		for (WebElement row : rowlists) {
			List<WebElement> collist = row.findElements(By.tagName("td"));
			for (WebElement col : collist) {
				if (col.getAttribute("class").contains("dojoxGridCellFocus")) {
					return col;
				}
			}
		}
		return null;
	}

	/**
	 * input text by formula bar
	 * 
	 * @return
	 */
	public SpreadsheetFilePage inputText(String input) {
		log.info("Input " + input);
		sleep(3);
		formulaBar.sendKeys(input);
		formulaBar.sendKeys(Keys.RETURN);
		sleep(1);
		return this;
	}

	/**
	 * input Text Into CoEditing Area
	 * 
	 * @param input
	 *            , the text that user want to input
	 * @return SpreadsheetFilePages
	 */
	public SpreadsheetFilePage inputTextIntoCoEditingArea(String input) {
		sleep(1);

		nameBox.clear();
		nameBox.sendKeys(currentCell + Keys.RETURN);
		formulaBar.clear();
		formulaBar.sendKeys(input + Keys.RETURN);
		verifyTrue(getCell(currentRow, currentCol).getText().contains(input),
				"Input string into co-editing area.");
		log.info("Input string into co-editing area successfully.");
		return this;
	}

	/**
	 * Clear a cell content by formula bar
	 * 
	 * @return
	 */
	public SpreadsheetFilePage deleteContentByFormulaBar() {
		log.info("delete text in formula bar.");
		formulaBar.sendKeys(Keys.chord(Keys.CONTROL, "a"));
		formulaBar.sendKeys(Keys.DELETE);
		return this;
	}

	/**
	 * input function keys by formula bar
	 * 
	 * @return
	 */
	public SpreadsheetFilePage sendKeysByFormulaBar(String text) {
		log.info("Input function keys into target cell.");
		formulaBar.click();
		formulaBar.sendKeys(text);
		sleep(1);
		return this;
	}

	/**
	 * Create formula by "All formulas" toolbar icon and "All Formulas" dialog
	 */
	public SpreadsheetFilePage createFormula(String formula, String argument) {
		log.info(String
				.format("Create formula %s with argument %s by \"All Formulas\" dialog",
						formula, argument));
		allFormulasBtn.click();
		sleep(2);
		selectFunctionListDialog().selectFormula(formula).submit();
		formulaBar.sendKeys(argument + Keys.ENTER);
		return this;
	}

	/**
	 * Create formula by Insert Function menu item and "All Formulas" dialog
	 * 
	 * @param string
	 * @param string2
	 * 
	 */
	public SpreadsheetFilePage createFunction(String function, String argument) {
		log.info("Create funtion " + function + " with argument " + argument);
		menu().createFunctionMore();
		selectFunctionListDialog().selectFormula(function).submit();
		formulaBar.sendKeys(argument + Keys.RETURN);
		return this;
	}

	/**
	 * Switch to sheet no.
	 * 
	 * @param sheetNo
	 *            start from 1
	 * @return
	 */
	public SpreadsheetFilePage switchToSheet(int sheetNo) {
		sleep(2);
		log.info("Switch to sheet #" + sheetNo + ", sheet name: "
				+ gwSheetTab(sheetNo).getText());
		gwSheetTab(sheetNo).click();
		currentSheet = sheetNo;
		sleep(3);
		if (!gwSheetArea(sheetNo).isDisplayed()) {
			this.verifyTrue(false, "Switch to sheet #" + sheetNo + ".");
		}
		return this;
	}

	public SpreadsheetFilePage switchToSheet(String sheetName) {
		log.info("Switch to Sheet: " + sheetName + ".");
		sleep(2);
		int sheetnumber = 0;
		for (WebElement sheet : sheetTabs1) {
			sheetnumber++;
			if (sheet.getText().equals(sheetName)) {
				sheet.click();
				break;
			}
		}
		currentSheet = sheetnumber;
		sleep(3);
		return this;
	}

	/**
	 * switch column number to column character
	 * 
	 * @param colNum
	 *            : start from 1
	 * @return
	 */
	public String columnHeaderToString(int colNum) {
		String aa = null;
		if (colNum <= 26)
			aa = String.valueOf((char) (colNum + 'A' - 1));
		else {
			int first = colNum / 26;
			int second = colNum % 26;
			String c = String.valueOf((char) (first + 'A' - 1));
			String d = String.valueOf((char) (second + 'A' - 1));
			aa = c + d;
		}
		return aa;

	}

	public int columnHeaderToIndex(String col) {
		int colnumber = 0;
		if (col.length() < 2) { // 1 char column header
			for (char column : col.toCharArray()) {
				if (column >= 'A' && column <= 'Z') {
					colnumber = column - 'A' + 1;
				} else {
					if (column >= 'a' && column <= 'z') {
						colnumber = column - 'a' + 1;
					}
				}
			}

		} else { // more than 2 char column header
			int digit = 0;
			int number = 0;
			for (int i = col.length() - 1; i >= 0; i--) {
				char column = col.toCharArray()[i];

				if (column >= 'A' && column <= 'Z') {
					number = column - 'A' + 1;
				} else {
					if (column >= 'a' && column <= 'z') {
						number = column - 'a' + 1;
					}
				}
				int repeat = 1;
				for (int j = 0; j < digit; j++) {
					repeat = repeat * 26;
				}
				colnumber = colnumber + repeat * number;
				digit++;
			}
		}
		return colnumber;
	}

	/**
	 * Set the font style from menu
	 * 
	 * @param style
	 *            : FONTSTYLE.BOLD, FONTSTYLE.ITALIC, FONTSTYLE.UNDERLINE,
	 *            FONTSTYLE.STRIKETHROUGH
	 * @return SpreadsheetFilePages
	 */
	public SpreadsheetFilePage setFontStyleFromMenu(FONTSTYLE style) {
		// cell(currentCell);
		log.info("Set font style from menu.");
		String verifyStyle = null;

		switch (style) {
		case BOLD:
			menu().setFontStyleToBold();
			verifyStyle = "700";
			if (driver.isChrome())
				verifyStyle = "bold";
			verifyCellStyle(currentCell, "font-weight", verifyStyle);
			break;
		case ITALIC:
			menu().setFontStyleToItalic();
			verifyStyle = "italic";
			verifyCellStyle(currentCell, "font-style", verifyStyle);
			break;
		case UNDERLINE:
			menu().setFontStyleToUnderline();
			verifyStyle = "underline";
			verifyCellStyle(currentCell, "text-decoration", verifyStyle);
			break;
		case STRIKETHROUGH:
			menu().setFontStyleToStrikeThrough();
			verifyStyle = "line-through";
			verifyCellStyle(currentCell, "text-decoration", verifyStyle);
			break;
		}
		// verifyTrue(cell.getCssValue("text-decoration").toLowerCase().contains(verifyStyle),"Set selected cell font style from menu.");
		return this;
	}

	/**
	 * Set the font style from basic toolbar
	 * 
	 * @param style
	 *            : FONTSTYLE.BOLD, FONTSTYLE.ITALIC, FONTSTYLE.UNDERLINE,
	 *            FONTSTYLE.STRIKETHROUGH
	 * @return SpreadsheetFilePages
	 */
	public SpreadsheetFilePage setFontStyleFromBasicToolBar(FONTSTYLE style) {
		log.info("Set font style from basic toolbar.");
		String verifyStyle = null;

		switch (style) {
		case BOLD:
			basicToolBar().setFontToBold();
			// verifyStyle = "bold";
			verifyStyle = "700";
			if (driver.isChrome())
				verifyStyle = "bold";
			verifyCellStyle(currentCell, "font-weight", verifyStyle);
			break;
		case ITALIC:
			basicToolBar().setFontToItalic();
			verifyStyle = "italic";
			verifyCellStyle(currentCell, "font-style", verifyStyle);
			break;
		case UNDERLINE:
			basicToolBar().setFontToUnderline();
			verifyStyle = "underline";
			verifyCellStyle(currentCell, "text-decoration", verifyStyle);
			break;
		case STRIKETHROUGH:
			basicToolBar().setFontToStrike();
			verifyStyle = "line-through";
			verifyCellStyle(currentCell, "text-decoration", verifyStyle);
			break;
		}

		return this;
	}

	/**
	 * Verify formula or raw value by formula bar
	 * 
	 * @param valueNeedVerify
	 *            , format should be =SUM(TRUE)
	 * @param cells
	 *            , the cells format should be A1:C5
	 * @return SpreadsheetFilePages
	 */
	public SpreadsheetFilePage verifyFormulaText(String valueNeedVerify,
			String colRow) {
		cell(colRow);
		verifyTrue(
				formulaBar.getAttribute("value").equalsIgnoreCase(
						valueNeedVerify),
				"Verify the formula text in formula bar is " + valueNeedVerify);
		return this;
	}

	/**
	 * undo by clicking the undo button on the basic toolbar This task does not
	 * contains the validation, please add validation in the case
	 * 
	 * @return SpreadsheetFilePages
	 */
	public SpreadsheetFilePage undoFromBasicToolBar() {
		log.info("Undo by toolbar");
		basicToolBar().undo();
		sleep(1);
		return this;
	}

	/**
	 * undo by clicking the undo menu item This task does not contains the
	 * validation, please add validation in the case
	 * 
	 * @return SpreadsheetFilePages
	 */
	public SpreadsheetFilePage undoFromMenu() {
		log.info("Undo by menu");
		menu().menuUndo();
		sleep(1);
		return this;
	}

	public SpreadsheetFilePage redoFromMenu() {
		log.info("Redo by menu");
		menu().menuRedo();
		sleep(1);
		return this;
	}

	/**
	 * redo by clicking the undo button on the basic toolbar This task does not
	 * contains the validation, please add validation in the case
	 * 
	 * @return SpreadsheetFilePages
	 */
	public SpreadsheetFilePage redoFromBasicToolBar() {
		log.info("Redo by toolbar");
		basicToolBar().redo();
		sleep(1);
		return this;
	}

	/**
	 * Set the font size from basic toolbar
	 * 
	 * @param size
	 *            integer: 8,9,10,11,12,14,16,18,20,22,24
	 * @return SpreadsheetFilePages
	 */
	public SpreadsheetFilePage setFontSizeFromBasicToolBar(int size) {
		log.info("Set font size " + size + " from basic toolbar");
		basicToolBar().setFontSize(size);
		verifyFontSize(size);
		return this;
	}

	public SpreadsheetFilePage setRangeFontSizeFromBasicToolBar(int size) {
		sleep(2);
		log.info("Set font size " + size + " from basic toolbar");
		basicToolBar().setFontSize(size);
		return this;
	}

	private String pt2px(int size_pt) {
		// DecimalFormat df = new DecimalFormat("0.00");
		DecimalFormat df = new DecimalFormat("0"); // change design in
													// 1.1.8_20130615
		String size_px = df.format(size_pt * 1.3333);
		// if(size_px.endsWith(".00"))
		// size_px=size_px.substring(0, size_px.length()-3);
		// if(size_px.endsWith(".67"))
		// size_px=size_px.substring(0, size_px.length()-1);
		log.info(size_px);
		return size_px;

	}

	public SpreadsheetFilePage verifyFontSize(int size) {
		verifyCellStyle(currentCell, "font-size", pt2px(size));
		return this;
	}

	public SpreadsheetFilePage setDefaultFormat() {
		log.info("Set Default Format by menu");
		this.menu().setDefaultFormat();
		return this;
	}

	/**
	 * Set the align from basic toolbar
	 * 
	 * @param align
	 *            ALIGN.LEFT, ALIGN.CENTER, ALIGN.RIGHT
	 * @return
	 */
	public SpreadsheetFilePage setAlignFromBasicToolBar(ALIGN align) {

		// cell(currentCell);
		log.info("set cell align from toolbar: " + align);
		String verifyStyle = null;
		switch (align) {
		case LEFT:
			basicToolBar().setAlignLeft();
			verifyStyle = "left";
			break;
		case RIGHT:
			basicToolBar().setAlignRight();
			verifyStyle = "right";
			break;
		case CENTER:
			basicToolBar().setAlignCenter();
			verifyStyle = "center";
			break;
		}
		verifyCellAlign(verifyStyle);
		sleep(1);
		return this;
	}

	public SpreadsheetFilePage verifyCellAlign(String align) {
		if (align.equalsIgnoreCase("default"))
			align = "left";
		verifyCellStyle(currentCell, "text-align", align);
		sleep(1);
		return this;
	}

	/**
	 * Set the align from menu
	 * 
	 * @param align
	 *            ALIGN.LEFT, ALIGN.CENTER, ALIGN.RIGHT
	 * @return
	 */
	public SpreadsheetFilePage setAlignFromMenu(ALIGN align) {

		// cell(currentCell);
		log.info("set cell align from menu: " + align);

		String verifyStyle = null;

		switch (align) {
		case LEFT:
			basicToolBar().setAlignLeft();
			verifyStyle = "left";
			break;
		case RIGHT:
			basicToolBar().setAlignRight();
			verifyStyle = "right";
			break;
		case CENTER:
			basicToolBar().setAlignCenter();
			verifyStyle = "center";
			break;
		}
		verifyCellAlign(verifyStyle);
		sleep(1);
		return this;
	}

	/**
	 * 
	 * @param color
	 *            red,blue,black,white,yellow,green,grey
	 * @return
	 * @author zhanglzl
	 */
	public SpreadsheetFilePage setFontColorFromBasicToolBar(String color) {

		log.info("Set selected cell font color to " + color
				+ " from basic toolbar");
		sleep(1);
		basicToolBar().setFontColor(color);
		sleep(2);
		log.info("currentRow = " + currentRow + "; currentCol = " + currentCol
				+ "; style: ");
		verifyCellColor(color, "color", "Set selected cell font color");
		return this;
	}

	/**
	 * Set the background color from basic toolbar
	 * 
	 * @param color
	 *            RBG format of the color, eg. rgb(238, 232, 170)
	 * @return SpreadsheetFilePages
	 */
	public SpreadsheetFilePage setBGColorFromBasicToolBar(String color,
			String rgbColor) {

		sleep(1);
		log.info("Set selected cell background color to " + color
				+ " from basic toolbar");
		basicToolBar().setBgColor(color);
		if (driver.isInternetExplorer())
			rgbColor = this.switchColor(rgbColor);

		try {
			verifyCellStyle(currentCell, "background-color", rgbColor);
		} catch (Exception e) {
			verifyCellStyle(currentCell, "background-color", rgbColor);
		}
		return this;
	}

	/**
	 * Set the background color from basic toolbar
	 * 
	 * @param color
	 *            red,blue,black,white,yellow,green,grey
	 * @return SpreadsheetFilePages
	 * @author zhanglzl
	 */
	public SpreadsheetFilePage setBGColorFromBasicToolBar(String color) {
		log.info("Set selected cell background color to " + color
				+ " from basic toolbar");
		sleep(1);
		basicToolBar().setBgColor(color);
		sleep(3);
		verifyCellColor(color, "background-color", "Set cell background color");
		return this;
	}

	/**
	 * Set the border color from basic toolbar
	 * 
	 * @param color
	 *            RBG format of the color, eg. rgb(238, 232, 170)
	 * @return SpreadsheetFilePages
	 */
	public SpreadsheetFilePage setBorderColorFromBasicToolBar(String color,
			String rgbColor) {

		log.info("Set selected cell border color to " + color
				+ " from basic toolbar");
		basicToolBar().setBorderColor(color);
		verifyCellStyle(currentCell, "border-top-color", rgbColor);
		verifyCellStyle(currentCell, "border-right-color", rgbColor);
		verifyCellStyle(currentCell, "border-bottom-color", rgbColor);
		return this;
	}

	/**
	 * 
	 * @param color
	 *            red,blue,black,white,yellow,green,grey
	 * @return
	 * @author zhanglzl
	 */
	public SpreadsheetFilePage setBorderColorFromBasicToolBar(String color) {
		log.info("Set selected cell border color from basic toolbar");
		basicToolBar().setBorderColor(color);
		sleep(2);
		verifyCellBorderColor(color);
		return this;
	}

	/**
	 * Set a range/multi-cells border color by toolbar
	 * 
	 * @param color
	 * @return
	 */
	public SpreadsheetFilePage setCellsBorderColorFromBasicToolBar(String color) {
		log.info("Set multi-cells border color to " + color
				+ " from basic toolbar");
		basicToolBar().setBorderColor(color);
		sleep(2);
		return this;
	}

	@Deprecated
	/**
	 * Select rows from rowstart to rowend
	 * @param rowstart
	 * @param rowend
	 * @return
	 * @author zhanglzl
	 */
	public SpreadsheetFilePage selectMultiRows(int rowstart, int rowend) {

		List<WebElement> rowList = driver.findElements(By
				.xpath("//td[contains(@class, 'romIndex')]"));
		WebElement fromRow = null;
		WebElement toRow = null;
		for (WebElement rowHead : rowList) {

			if (String.valueOf(rowstart).equalsIgnoreCase(rowHead.getText())) {
				fromRow = rowHead;
			}
			if (String.valueOf(rowend).equalsIgnoreCase(rowHead.getText())) {
				toRow = rowHead;
			}
		}

		Actions builder = new Actions(driver);
		Action selectCellRange = builder.clickAndHold(fromRow)
				.moveToElement(toRow).release(toRow).build();

		selectCellRange.perform();
		sleep(1);
		return this;
	}

	/**
	 * switch color from rgb to #
	 * 
	 * @return SpreadsheetFilePages
	 */
	public String switchColor(String rgbColor) {
		String numFromRgb = rgbColor.substring(4, (rgbColor.length() - 1));
		String[] rgbElement = numFromRgb.split(",");
		String[] finalRgbElement = new String[5];
		for (int i = 0; i < rgbElement.length; i++) {
			finalRgbElement[i] = Integer.toString(
					Integer.parseInt(rgbElement[i].trim()), 16);
			if (finalRgbElement[i].length() == 1) {
				finalRgbElement[i] = "0"
						+ Integer.toString(
								Integer.parseInt(rgbElement[i].trim()), 16);
			}
		}
		String rgbTo16 = "#" + finalRgbElement[0] + finalRgbElement[1]
				+ finalRgbElement[2];
		System.out.println(rgbTo16);
		log.info("switch color from rgb to #");
		return rgbTo16;
	}

	/**
	 * Insert column at the current location from basic toolbar
	 * 
	 * @return SpreadsheetFilePages
	 */
	public SpreadsheetFilePage insertColumnFromBasicToolbar() {

		String cellValue = null;
		log.info("Insert column before column " + currentCol
				+ "by basic toolbar");
		cellValue = getCell(currentCell).getText();
		basicToolBar().setColOperation(ColOperation.BEFORE);
		verifyTrue(
				getCell(currentRow, currentCol + 1).getText().equals(cellValue),
				"Insert column before " + currentCell);

		return this;
	}

	/**
	 * Insert multi-columns at the current location from basic toolbar
	 * 
	 * @return SpreadsheetFilePages
	 */
	public SpreadsheetFilePage insertColumnsFromBasicToolbar() {
		log.info("Insert multi-columns before column " + currentCol
				+ "by basic toolbar");
		basicToolBar().setColOperation(ColOperation.BEFORE);
		return this;
	}

	/**
	 * Delete column at current location from basic toolbar
	 * 
	 * @return SpreadsheetFilePages
	 */
	public SpreadsheetFilePage deleteColumnFromBasicToolbar() {
		String cellValue = null;
		log.info("Delete column " + currentCol + " by basic toolbar");
		cellValue = getCell(currentRow, currentCol + 1).getText();
		basicToolBar().setColOperation(ColOperation.DELETE);
		verifyTrue(getCell(currentRow, currentCol).getText().equals(cellValue),
				"Delete column " + currentCol);

		return this;
	}

	/**
	 * Delete current selected column(s). If use this method, pls add your
	 * verification point
	 * 
	 * @return
	 * 
	 */
	public SpreadsheetFilePage deleteColumnsFromBasicToolbar() {
		log.info("Delete multi-column(s) by basic toolbar");
		basicToolBar().setColOperation(ColOperation.DELETE);
		return this;
	}

	/**
	 * 
	 * @return
	 * 
	 */
	public SpreadsheetFilePage insertRowFromBasicToolbar() {
		String cellValue = null;
		log.info("Insert row above row " + currentRow + " from basic toolbar");
		cellValue = getCell(currentCell).getText();
		log.info("cell value before insert row : " + cellValue);
		// focusOnCell(currentCell);
		basicToolBar().setRowOperation(RowOperation.ABOVE);
		verifyTrue(
				getCell(currentRow + 1, currentCol).getText().equals(cellValue),
				"Insert row above row " + currentRow);

		return this;
	}

	/**
	 * 
	 * 
	 */
	public SpreadsheetFilePage deleteRowFromBasicToolbar() {
		String cellValue = null;

		log.info("Delete row " + currentRow + " from basic toolbar");
		cellValue = getCell(currentRow + 1, currentCol).getText();
		basicToolBar().setRowOperation(RowOperation.DELETE);

		verifyTrue(getCell(currentRow, currentCol).getText().equals(cellValue),
				"Delete row " + currentRow);

		return this;
	}

	/**
	 * Delete current selected row(s). Need to add your verification points if
	 * use this method.
	 * 
	 * @return
	 * 
	 */
	public SpreadsheetFilePage deleteRowsFromBasicToolbar() {

		log.info("Delete row " + currentRow + " from basic toolbar");
		basicToolBar().setRowOperation(RowOperation.DELETE);
		return this;

	}

	public SpreadsheetFilePage insertColumnFromMenu() {
		String cellValue = null;
		log.info("Insert column before column " + currentCol + " from menu");
		cellValue = getCell(currentCell).getText();
		log.info("cell value before insert column : " + cellValue);
		menu().insertColumnBefore();
		verifyTrue(
				getCell(currentRow, currentCol + 1).getText().equals(cellValue),
				"Insert column before column " + currentCol);
		return this;
	}

	/**
	 * 
	 * 
	 */
	public SpreadsheetFilePage deleteColumnFromMenu() {
		String cellValue = null;
		log.info("Delete selected column " + currentCol + " from menu");
		cellValue = getCell(currentRow, currentCol + 1).getText();
		menu().deleteColumn();
		verifyTrue(getCell(currentRow, currentCol).getText().equals(cellValue),
				"Delete selected column " + currentCol);
		return this;
	}

	/**
	 * @return
	 * 
	 */
	public SpreadsheetFilePage insertRowFromMenu() {
		String cellValue = null;
		log.info("Insert row above row " + currentRow + " from menu");
		cellValue = getCell(currentRow, currentCol).getText();
		menu().insertRowAbove();
		verifyTrue(
				getCell(currentRow + 1, currentCol).getText().equals(cellValue),
				"Insert row above row " + currentRow);
		return this;
	}

	/**
	 * 
	 * 
	 */
	public SpreadsheetFilePage deleteRowFromMenu() {
		String cellValue = null;
		log.info("Delete row " + currentRow + " from menu");
		cellValue = getCell(currentRow + 1, currentCol).getText();
		menu().deleteRow();
		verifyTrue(getCell(currentRow, currentCol).getText().equals(cellValue),
				"Delete row " + currentRow);
		return this;

	}

	/**
	 * Delete multi-rows by menu. Need to add verification yourself if use it
	 * 
	 * @return
	 * 
	 */
	public SpreadsheetFilePage deleteRowsFromMenu() {
		log.info("Delete multi-rows at current location from menu");
		menu().deleteRow();
		// verifyTrue(locateCurrentCell(currentSheet,endindex+1,currentCol).getText().equals(cellValue),
		// "Delete row at current location");

		return this;

	}

	/**
	 * Insert sheet
	 * 
	 * @param string
	 * @return
	 * 
	 */
	public SpreadsheetFilePage insertSheetFromMenu(String sheetName) {
		log.info("Insert sheet '" + sheetName
				+ "' at current location from menu");
		menu().insertSheet();
		simpleInputDialog().input(sheetName);
		simpleInputDialog().submit();
		currentSheet = getSheetTotalNum();
		verifySheetExist(sheetName, true);
		return this;
	}

	/**
	 * Delete sheet by menu
	 * 
	 * @param index
	 *            start from 1
	 * @return
	 * 
	 */
	public SpreadsheetFilePage deleteSheetFromMenu(int index) {

		log.info(String.format("Delete sheet %s from menu", index));
		switchToSheet(index);
		String nextSheetName = sheetTabs1.get(index).getText();
		menu().deleteSheet();
		simpleInformationDialog().submit();

		currentSheet = 1; // delete a sheet will return to the first sheet.
		String currentSheetName = sheetTabs1.get(index - 1).getText();
		boolean foundAfterDelete = false;
		if (currentSheetName.equalsIgnoreCase(nextSheetName)) {
			foundAfterDelete = true;
		}
		verifyTrue(foundAfterDelete, "Sheet " + index + " is deleted.");
		sleep(2);
		return this;
	}

	/**
	 * Delete current sheet
	 * 
	 * @return
	 * 
	 */
	public SpreadsheetFilePage deleteSheetFromMenu() {
		log.info("Delete sheet at current location from menu");
		String currentSheetName = sheetTabs1.get(currentSheet - 1).getText();
		log.info("current sheet name = " + currentSheetName);

		menu().deleteSheet();
		simpleInformationDialog().submit();
		currentSheet = 1; // delete a sheet will return to the first sheet.

		boolean foundAfterDelete = true;
		// Start to verify if sheet 'sheetName' is removed
		for (WebElement sheet : sheetTabs1) {
			if (sheet.getText().equals(currentSheetName)) {
				sleep(2);
				foundAfterDelete = false;
				break;
			}
		}
		verifyTrue(foundAfterDelete, "sheet " + currentSheetName
				+ " is deleted");
		sleep(2);
		return this;
	}

	public SpreadsheetFilePage verifySheetTotalNum(int total) {

		Boolean totalNum = false;
		log.info("Verify total sheet number");
		if (total == getSheetTotalNum())
			totalNum = true;

		verifyTrue(totalNum, "totol sheet number is " + total);
		return this;
	}

	public SpreadsheetFilePage renameSheetFromMenu(int index, String newName) {
		sleep(2);
		switchToSheet(index);

		log.info("Rename the current sheet from menu");
		menu().renameSheet();

		simpleInputDialog().input(newName).submit();
		// List<WebElement> sheetList = gwSheetTabs1();
		boolean foundAfterRename = false;

		for (WebElement sheet : sheetTabs1) {
			if (sheet.getText().equals(newName)) {
				foundAfterRename = true;
				break;
			}
		}
		verifyTrue(foundAfterRename, "Current sheet rename to " + newName);
		return this;
	}

	/**
	 * Rename sheet 'oldsheetName' to 'newsheetName'
	 * 
	 * @param oldsheetName
	 * @param newsheetName
	 * @return
	 * 
	 */
	public SpreadsheetFilePage renameSheetFromMenu(String oldsheetName,
			String newsheetName) {
		sleep(2);
		switchToSheet(oldsheetName);

		log.info("Rename the current sheet from menu");
		menu().renameSheet();

		simpleInputDialog().input(newsheetName).submit();
		// List<WebElement> sheetList = gwSheetTabs1();

		boolean foundAfterRename = false;

		for (WebElement sheet : sheetTabs1) {
			if (sheet.getText().equals(newsheetName)) {
				foundAfterRename = true;
				break;
			}
		}

		verifyTrue(foundAfterRename, "Current sheet rename to " + newsheetName);

		return this;
	}

	public By ifElement(By... by) {
		for (int i = 0; i < by.length; i++) {
			try {
				sleep(1);
				driver.findElement(by[i]);
				log.debug("Find Element " + by[i].toString());
				return by[i];
			} catch (Exception e) {
				log.debug("Do not find Element " + by[i].toString());
			}
		}
		return null;
	}

	public SpreadsheetFilePage moveSheetFromMenu(String sheetName, int location) {
		log.info("Move the current sheet to location " + location
				+ " from menu");
		switchToSheet(sheetName);

		menu().moveSheet();

		simpleInputDialog().input(String.valueOf(location)).submit();
		sleep(2);
		verifySheetLocation(sheetName, location, true);
		return this;
	}

	/**
	 * Verify if a sheet "sheetName" is moved to location
	 * 
	 * @param sheetName
	 * @param location
	 *            : start from 1
	 * @param moreornot
	 *            : verify if a sheet is moved or not
	 * @return
	 * 
	 */
	public SpreadsheetFilePage verifySheetLocation(String sheetName,
			int location, boolean moveornot) {
		sleep(2);
		boolean foundAfterMove = false;
		// if (gwSheetTabs().get(location - 1).getText().equals(sheetName)) {
		if (sheetTabs1.get(location - 1).getText().equals(sheetName)) {
			foundAfterMove = true;
		}
		verifyTrue(foundAfterMove == moveornot, "Move sheet " + sheetName
				+ " to location" + location);

		return this;
	}

	private String getCellValue(String col, int row) {
		sleep(2);
		int i = columnHeaderToIndex(col);
		return gwCell2(row, i).getText();
	}

	public SpreadsheetFilePage verifyCellContent(String colrow,
			String valueNeedVerify) {
		log.info("Verify cell(" + colrow + ") equal to " + valueNeedVerify);
		verifyCellContent(CompareOption.EQ, colrow, valueNeedVerify);
		return this;
	}

	public SpreadsheetFilePage verifyCellContent(CompareOption option,
			String colrow, String valueNeedVerify) {
		switch (option) {
		case EQ:
			assertEquals("Verify cell(" + colrow + ") equal to "
					+ valueNeedVerify, valueNeedVerify,
					getCellValue(getCol(colrow), getRow(colrow)));
			break;
		case CT:
			verifyTrue(
					getCellValue(getCol(colrow), getRow(colrow)).contains(
							valueNeedVerify), "Verify cell(" + colrow
							+ ") contains " + valueNeedVerify);
			break;
		case NC:
			verifyTrue(
					!getCellValue(getCol(colrow), getRow(colrow)).contains(
							valueNeedVerify), "Verify cell(" + colrow
							+ ") does not contain " + valueNeedVerify);
			break;
		}
		return this;
	}

	/**
	 * Verify name box
	 * 
	 * @param valueNeedVerify
	 *            such as A1:B2
	 * @return
	 * 
	 */
	public SpreadsheetFilePage verifyNameBoxContent(String valueNeedVerify) {
		sleep(1);
		log.info("verify name box value");
		verifyTrue(
				nameBox.getAttribute("value").equalsIgnoreCase(valueNeedVerify),
				"Verify name box contains " + valueNeedVerify);
		return this;
	}

	/**
	 * @param string
	 */
	public SpreadsheetFilePage changeDataFormatFromBasicToolBar(
			DATAFORMAT dataFormat) {
		log.info("Change data format from basic toolbar.");
		switch (dataFormat) {
		case CURRENCY: {
			basicToolBar().setDataFormatToCurrency();
			break;
		}
		case PERCENT: {
			basicToolBar().setDataFormatToPercent();
			break;
		}

		}

		return this;
	}

	public String getCol(String cell) {

		String col = "";

		for (char column : cell.toCharArray()) {
			if (column >= 'A' && column <= 'Z') {
				col = col + column;
			} else {
				if (column >= 'a' && column <= 'z') {
					col = col + column;
				}
			}
		}

		return col;
	}

	public int getRow(String cell) {

		String row = "";
		for (char rownum : cell.toCharArray()) {
			if (rownum >= '0' && rownum <= '9') {
				row = row + rownum;
			}
		}
		return new Integer(row);
	}

	public SpreadsheetFilePage selectCellRange(String startCell, String endCell) {
		int startCol = columnHeaderToIndex(getCol(startCell));
		int startRow = getRow(startCell);
		int endCol = columnHeaderToIndex(getCol(endCell));
		int endRow = getRow(endCell);

		this.currentRow = startRow;
		this.currentCol = startCol;

		log.info("Select cell range from " + startCell + " to " + endCell);
		log.info(startCell + " = " + startCol + " , " + startRow);
		log.info(endCell + " = " + endCol + " , " + endRow);

		List<WebElement> rowlist = gwRows();
		List<WebElement> collist1 = gwCols(startRow - 1);
		WebElement fromCell = collist1.get(startCol - 1);
		sleep(1);
		List<WebElement> collist2 = rowlist.get(endRow - 1).findElements(
				By.tagName("td")); // add 1 row to ensure select whole target
									// range.

		WebElement toCell = collist2.get(endCol - 1);

		Actions builder = new Actions(driver);

		Action selectCellRange = builder.clickAndHold(fromCell)
				.moveToElement(toCell).release(toCell).build();

		selectCellRange.perform();
		sleep(1);
		return this;
	}

	/**
	 * Copy contents of selected cell range
	 * 
	 * @return SpreadsheetFilePages
	 */
	public SpreadsheetFilePage copy() {
		log.info("Copy by menu on IE or Copy by Keyboard on FF/Chrome");
		if (driver.isInternetExplorer()) {
			menu().copy();
		} else {
			String keys = Keys.chord(Keys.CONTROL, "c");
			driver.getKeyboard().sendKeys(keys);
		}
		return this;
	}

	/**
	 * Cut contents of selected cell range
	 * 
	 * @return SpreadsheetFilePages
	 */
	public SpreadsheetFilePage cut() {
		log.info("Cut by menu on IE or Cut by Keyboard on FF/Chrome");
		if (driver.isInternetExplorer()) {
			menu().cut();
		} else {
			String keys = Keys.chord(Keys.CONTROL, "x");
			driver.getKeyboard().sendKeys(keys);
		}
		return this;
	}

	/**
	 * Paste contents in the clipboard to the current cell
	 * 
	 * @return SpreadsheetFilePages
	 */
	public SpreadsheetFilePage paste() {
		log.info("Paste by menu on IE or paste by Keyboard on FF/Chrome");
		super.paste();
		return this;
	}

	public SpreadsheetFilePage ctrlA() {
		log.info("Select all by Ctrl+A");
		super.selectAll();
		return this;
	}

	/**
	 * Sort the selected cell range
	 * 
	 * @param includeColumnHeader
	 *            , not required, could be null
	 * @param byColumn
	 *            , not required, could be null
	 * @param bySequence
	 *            , sort by Ascending or descending
	 * @return SpreadsheetFilePages
	 * 
	 */
	public SpreadsheetFilePage sort(boolean includeColumnHeader,
			String byColumn, String bySequence) {

		log.info("Start to sort...");
		menu().sort(includeColumnHeader, byColumn, bySequence);
		log.info("End to sort");
		return this;
	}

	public SpreadsheetFilePage instantFilterByMenu() {
		log.info("Set Instant Filter by menu");
		menu().instantFilter();
		sleep(2);
		return this;
	}

	public SpreadsheetFilePage instantFilterByToolbar() {
		log.info("Set Instant Filter by toolbar");
		basicToolBar().instantFilter();
		sleep(2);
		return this;
	}

	public SpreadsheetFilePage instantFilterOpenPanel() {
		log.info("Try to open instant Filter panel");
		instantFilter().instantFilterOpenPanel();
		return this;
	}

	public SpreadsheetFilePage instantFilterSort(boolean sortascending) {
		if (sortascending)
			log.info("In instant Filter panel, Sort Ascending");
		else
			log.info("In instant Filter panel, Sort Descending");
		instantFilter().instantFilterSort(sortascending);
		return this;
	}

	/**
	 * Verify if an instant filter arrow button is created in current column
	 * 
	 * @param cell
	 * @param isexisted
	 * @return
	 * 
	 */
	public SpreadsheetFilePage verifyInstantFilterExist(String cell,
			boolean isexisted) {
		log.info("verify if instant filter button exist");
		instantFilter().verifyInstantFilterExist(cell, isexisted);
		sleep(1);
		return this;
	}

	/***
	 * Verify if given filterwords are checked or not
	 * 
	 * @param checkedornot
	 * @param filterwords
	 * @return
	 * 
	 */
	public SpreadsheetFilePage verifyInstantFilterChecked(boolean checkedornot,
			String... filterwords) {
		log.info("Verify if filterwords are checked or not in instant filter list");
		instantFilter().verifyInstantFilterChecked(checkedornot, filterwords);
		return this;
	}

	/**
	 * Verify the filter values in Instant Filter panel
	 * 
	 * @param filterwords
	 * @return
	 * 
	 */
	public SpreadsheetFilePage verifyInstantFilterKeywords(
			String... filterwords) {
		log.info("Verify if all non-empty cells are in instant filter list");
		instantFilter().verifyInstantFilterKeywords(filterwords);
		return this;
	}

	/**
	 * Click Clear in a instant filter panel to remove all selected items
	 * 
	 * @return
	 * 
	 */
	public SpreadsheetFilePage instantFilterClearAll() {
		sleep(2);
		log.info("Instant filter: clear all items in filter panel");
		instantFilter().instantFilterClearAll();
		return this;
	}

	public SpreadsheetFilePage instantFilterSelectAll() {
		sleep(2);
		log.info("Instant filter: select all items in filter panel");
		instantFilter().instantFilterSelectAll();
		return this;
	}

	public SpreadsheetFilePage instantFilterClickOK() {
		sleep(1);
		log.info("Instant filter: click OK to close instant filter panel");
		instantFilter().instantFilterClickOK();
		return this;
	}

	public SpreadsheetFilePage clickCell(String cell) {
		int row = getRow(cell);
		int col = columnHeaderToIndex(getCol(cell));
		gwCell2(row, col).click();

		return this;
	}

	public SpreadsheetFilePage clickCellAndInput(String cell, String text) {
		int row = getRow(cell);
		int col = columnHeaderToIndex(getCol(cell));
		clickCell(cell);
		gwCell2(row, col).sendKeys(text);
		return this;
	}

	/**
	 * Mark items if filterwords in Instant filter panel
	 * 
	 * @param filterwords
	 * @return
	 * 
	 */
	public SpreadsheetFilePage instantFilterCheckKeywords(String... filterwords) {
		log.info("select filter words in instant filter list");
		instantFilter().instantFilterCheckKeywords(filterwords);
		return this;
	}

	/**
	 * Wrap the text or not in the cell from basic toolbar, please input a long
	 * string(eg 'alkdfiaghirgflkdjlkfaie') to make the funtion work properly.
	 * 
	 * @return SpreadsheetFilePages
	 */
	public SpreadsheetFilePage wrapTextFromBasicToolBar() {
		log.info("Wrap text from basic toolbar");
		basicToolBar().wrapText();
		sleep(1);
		verifyCellStyle(currentCell, "white-space", "pre-wrap");
		// this.verifyTrue(currentCell().getAttribute("style").toLowerCase().contains("word-wrap"),
		// "Wrap text failed from basic toolbar");
		return this;
	}

	/**
	 * Set move right on enter, default is moving right
	 * 
	 * @return SpreadsheetFilePages
	 * 
	 */
	public SpreadsheetFilePage setMoveRightOnEnter() {
		log.info("Set move right on enter by menu");
		menu().setMoveRightOnEnter(true);
		return this;
	}

	/**
	 * Set move below on enter, default is moving right
	 * 
	 * @return SpreadsheetFilePages
	 * 
	 */
	public SpreadsheetFilePage setMoveBelowOnEnter() {
		log.info("Set move below on enter by menu");
		menu().setMoveRightOnEnter(false);
		this.cell(currentCell);
		formulaBar.sendKeys(Keys.RETURN);
		sleep(1);
		if (Integer.parseInt(getFocusedCell().getAttribute("idx")) > columnHeaderToIndex(getCol(currentCell))) {
			this.verifyTrue(false, "Set move below on enter failed");
		}
		return this;
	}

	/**
	 * Wrap the text or not in the cell from menu, please input a long string(eg
	 * 'alkdfiaghirgflkdjlkfaie') to make the funtion work properly.
	 * 
	 * @return SpreadsheetFilePages
	 */
	public SpreadsheetFilePage wrapTextFromMenu() {
		log.info("Wrap text from basic toolbar");
		menu().wrapText();
		sleep(1);
		verifyCellStyle(currentCell, "white-space", "pre-wrap");
		return this;
	}

	/**
	 * No wrap the text or not in the cell from basic toolbar, please input a
	 * long string and wrap text first.
	 * 
	 * @return SpreadsheetFilePages
	 */
	public SpreadsheetFilePage noWrapTextFromBasicToolBar() {
		log.info("Non wrap text from basic toolbar");
		basicToolBar().wrapText();
		sleep(1);
		verifyCellStyle(currentCell, "white-space", "pre");
		return this;
	}

	/**
	 * Wrap the text or not in the cell from menu, please input a long string
	 * and wrap text first.
	 * 
	 * @return SpreadsheetFilePages
	 */
	public SpreadsheetFilePage noWrapTextFrommenu() {
		log.info("Non wrap text from basic toolbar");
		menu().wrapText();
		sleep(1);
		verifyCellStyle(currentCell, "white-space", "pre");
		return this;
	}

	/**
	 * merge selected cells to one cell.
	 * 
	 * @return SpreadsheetFilePages
	 */
	public SpreadsheetFilePage mergeCellsFromBasicToolBar() {
		log.info("start to merge cells by toolbar");
		basicToolBar().mergeSplitCell();
		return this;
	}

	/**
	 * 
	 * @param merge
	 *            : select OK or Cancel in "Merge Cells" dialog
	 * @return
	 * 
	 */
	public SpreadsheetFilePage mergeCellsFromBasicToolBar(boolean ok) {
		basicToolBar().mergeSplitCell(ok);
		log.info("Yes. Select OK in Merge Cells dialog: " + ok);
		return this;
	}

	/**
	 * 
	 * @param cell
	 * @return SpreadsheetFilePages
	 * 
	 */
	public SpreadsheetFilePage splitCellFromBasicToolBar(String cell) {
		int selectedCol = columnHeaderToIndex(getCol(cell));
		int selectedRow = getRow(cell);

		log.info("Split cell " + cell + " from basic toolbar");
		if (!this.getCell(selectedRow, selectedCol + 1).getAttribute("style")
				.toLowerCase().contains("display: none")) {
			verifyTrue(Boolean.FALSE,
					"The cell could not be splitted from basic toolbar.");
		}

		this.getCell(cell);
		sleep(1);
		basicToolBar().mergeSplitCell();

		sleep(1);
		if (this.getCell(selectedRow, selectedCol + 1).getAttribute("style")
				.toLowerCase().contains("display: none")) {
			verifyTrue(Boolean.FALSE, "Split cell failed from basic toolbar.");
		}
		return this;
	}

	public SpreadsheetFilePage splitCellFromBasicToolBar() {
		sleep(1);
		log.info("start to split cell from basic toolbar");
		basicToolBar().mergeSplitCell();
		log.info("split done from basic toolbar");
		sleep(1);
		return this;
	}

	/**
	 * @return
	 * 
	 */
	public SpreadsheetFilePage refresh() {
		log.info("Refresh the current spreadsheet");
		driver.navigate().refresh();
		if (welcomeDialogOK.isPresent()) {
			welcomeDialogNotShow.click();
			welcomeDialogOK.click();
		}
		// gwSheetTab(1).isDisplayed();
		// wait.until(presenceOfElementLocatedByType("id==websheet_layout_WorksheetContainer_0_tablist_websheet_grid_DataGrid_0"));
		sleep(8);

		return this;
	}

	/**
	 * Press F5 to refresh the page
	 * 
	 * @return SpreadsheetFilePages
	 */
	public SpreadsheetFilePage refreshByF5() {
		log.info("Press F5 to refresh the current spreadsheet");
		if (driver.isFirefox())
			body.sendKeys(Keys.F5);
		else
			driver.navigate().refresh();
		if (welcomeDialogOK.isPresent()) {
			welcomeDialogNotShow.click();
			welcomeDialogOK.click();
		}
		sleep(8);
		return this;
	}

	/**
	 * @param string
	 * 
	 */
	@Find("id=ll_new_comment_field")
	public EnhancedWebElement commentTextBox;
	@Find("css=ll_new_comment_add_btn span.dijitButton")
	public EnhancedWebElement commentButton;

	public SpreadsheetFilePage addComments(String comments) {
		log.info("Add comments to current cell");
		menu().addComment();
		// driver.findElementByType("id==ll_new_comment_field").sendKeys(comments);
		commentTextBox.sendKeys(comments);
		// driver.findElementByType("id==ll_new_comment_add_btn").findElement(By.className("dijitButton")).click();
		commentButton.click();
		verifyTrue(getCurrentCell().findElement(By.className("imgcomment"))
				.isDisplayed(), "Add comment to target cells");

		boolean commentsAdded = false;

		List<WebElement> commentslist = driver.findElements(By
				.className("ll_comment_content"));

		for (WebElement commentitem : commentslist) {
			if (commentitem.getText().contains(comments)) {
				commentsAdded = true;
				break;
			}
		}

		verifyTrue(commentsAdded, "Add comment to sidebar");

		return this;
	}

	/**
	 * @param string
	 * 
	 */
	@Find("id=ll_comments_func_frame")
	public EnhancedWebElement commentSideBar;
	@Find("id=sidebar_comments_pane_button")
	public WebElement commentBtn;

	public SpreadsheetFilePage readComments(String comments) {
		log.info("Read comments");
		// driver.findElementByType("id==sidebar_comments_pane_button").click();
		commentBtn.click();
		verifyTrue(getCurrentCell().findElement(By.className("imgcomment"))
				.isDisplayed(), "Read comment in target cells");

		boolean commentsAdded = false;
		// WebElement sidebarCommentsPane = driver
		// .findElementByType("id==ll_comments_func_frame");

		if (commentSideBar.getText().contains(comments))
			commentsAdded = true;
		verifyTrue(commentsAdded, "Read comment in sidebar");
		return this;
	}

	public SpreadsheetFilePage hideRow(int index) {
		log.info("Hide row " + index);
		this.cell("A" + index);
		sleep(2);
		this.menu().hideRow();
		sleep(3);
		verifyRowHid(true);
		currentRow = index + 1;
		currentCell = "A" + currentRow;
		return this;
	}

	public SpreadsheetFilePage hideRows(int startrow, int endrow) {
		log.info("Hide row from " + startrow + " to " + endrow);
		this.menu().hideRow();
		sleep(3);
		verifyRowHid(startrow, true);
		verifyRowHid(endrow, true);
		currentRow = endrow + 1;
		currentCell = "A" + currentRow;
		log.info(currentCell);
		return this;
	}

	public SpreadsheetFilePage hideColumn(String column) {
		log.info("Hide column " + column + " by menu");
		this.menu().hideColumn();
		sleep(3);
		verifyColHid(column, true);
		currentCol = currentCol + 1;
		currentCell = columnHeaderToString(currentCol);
		return this;
	}

	public SpreadsheetFilePage hideColumn() throws Exception {
		selectRowColumn(columnHeaderToString(currentCol));
		this.menu().hideColumn();
		sleep(3);
		verifyColHid(columnHeaderToString(currentCol), true);
		currentCol = currentCol + 1;
		currentCell = columnHeaderToString(currentCol) + 1;
		return this;
	}

	public SpreadsheetFilePage showColumn(String column) {
		log.info("Show column " + column + " by menu");
		this.menu().showColumn();
		sleep(3);
		verifyColHid(column, false);
		return this;
	}

	public SpreadsheetFilePage verifyRowHid(boolean ishidden) {
		boolean hid = false;
		WebElement hideRow = gwCurrentRow();
		if (hideRow == null
				|| hideRow.getAttribute("style").toLowerCase()
						.contains("display: none")) {
			hid = true;
		}
		this.verifyTrue(hid == ishidden, "Verify hide the row: " + currentRow);
		return this;
	}

	/**
	 * 
	 * @param rowIndex
	 *            : start from 1
	 * @param ishidden
	 * @return
	 * 
	 */
	public SpreadsheetFilePage verifyRowHid(int rowIndex, boolean ishidden) {
		boolean hid = false;
		WebElement hideRow = gwRow(rowIndex);
		if (hideRow == null
				|| hideRow.getAttribute("style").toLowerCase()
						.contains("display: none")) {
			hid = true;
		}
		this.verifyTrue(hid == ishidden, "Verify hide the row " + rowIndex);
		return this;
	}

	/**
	 * Verify if Row Index is displayed or not after filtering
	 * 
	 * @param rowIndex
	 *            start from 1
	 * @param ishidden
	 * @return
	 * 
	 */
	public SpreadsheetFilePage verifyRowIndexHid(int rowIndex, boolean ishidden) {
		boolean hid = false;
		WebElement hideRow = null;
		String strrowIndex = Integer.toString(rowIndex);
		int len = gwRowsHeaderByXpath().size();
		log.info("aaa" + len);
		if (len > rowIndex)
			hideRow = gwRowsHeaderByXpath().get(rowIndex - 1);
		else
			hideRow = gwRowsHeaderByXpath().get(len - 1);
		String currentrowIndex = hideRow.getText().toLowerCase();
		log.info("Row " + rowIndex + " index is " + currentrowIndex);
		if (!currentrowIndex.contains(strrowIndex)) {
			hid = true;
		}
		this.verifyTrue(hid == ishidden, "Verify hide the row " + rowIndex);
		return this;
	}

	/**
	 * Verify if current rowIndex is as expected in all displayed rows For
	 * example, verify if the index of row 3 is expected 4 after hid row 1
	 * 
	 * @param row
	 *            start from 1
	 * @param ishidden
	 * @return
	 * 
	 */
	public SpreadsheetFilePage verifyRowIndex(int row, String expectedrowIndex) {
		WebElement hideRow = gwRowsHeaderByXpath().get(row - 1);
		String currentrowIndex = hideRow.getText().toLowerCase();
		log.info("Row " + row + " index is " + currentrowIndex);
		boolean hid = currentrowIndex.contains(expectedrowIndex);
		this.verifyTrue(hid, "Verify hide the row " + row);
		return this;
	}

	/**
	 * This verification method is just used after users refresh the file
	 * 
	 * @param rowIndex
	 * @param ishidden
	 * @return
	 * 
	 */
	public SpreadsheetFilePage verifyRowHidAfterRefresh(int rowIndex,
			boolean ishidden) {
		boolean hid = true;
		List<WebElement> showRows = gwRowsHeaderByXpath();
		for (WebElement show : showRows)
			if (show.getText().toString()
					.equalsIgnoreCase(Integer.toString(rowIndex))) {
				hid = false;
			}
		this.verifyTrue(hid == ishidden, "the row is hidden after refreshing: "
				+ rowIndex);
		return this;
	}

	public SpreadsheetFilePage verifyColTotalNum(int total) {
		int getColNum = gwColHeaders().size() - 1;
		sleep(1);
		this.verifyTrue(getColNum == total,
				"the actual total column number is " + getColNum
						+ "; expected is " + total);
		return this;
	}

	public SpreadsheetFilePage verifyColHid(String col, boolean ishidden) {
		boolean hid = false;
		sleep(1);
		currentCol = this.columnHeaderToIndex(col);
		WebElement hideCol = gwColHeaders().get(currentCol);
		if (hideCol == null
				|| hideCol.getAttribute("style").toLowerCase()
						.contains("display: none")) {
			hid = true;
		}
		this.verifyTrue(hid == ishidden, "Verify hide the column " + col);
		return this;
	}

	public SpreadsheetFilePage verifyColumnWidth(String col, String width) {
		String cell = col + "1";
		currentCol = this.columnHeaderToIndex(col);
		WebElement getCol = gwColHeaders().get(currentCol);
		sleep(1);
		verifyElementStyle(getCol, "width", width);

		// verify cell column is also changed
		cell(cell);
		// since IBM Docs 1.1.6 20130409-0501, add padding-right & padding-left
		String paddingRight = getCell(cell).getCssValue("padding-right")
				.substring(0, 1);
		String paddingLeft = getCell(cell).getCssValue("padding-left")
				.substring(0, 1);
		int width1 = Integer.parseInt(width) - Integer.parseInt(paddingRight)
				- Integer.parseInt(paddingLeft);
		width = Integer.toString(width1);
		if (getCell(currentRow, currentCol + 1).getAttribute("style")
				.toLowerCase().contains("display: none"))
			verifyCellStyle(col + "2", "width", width);
		else
			verifyCellStyle(cell, "width", width);
		return this;
	}

	public SpreadsheetFilePage showRow(int index) {
		String selectedCol = "A";
		int selectedRow = index;

		log.info("Try to show row " + index);
		if (selectedRow == 1) {
			this.cell(selectedCol + 2);
		} else {
			int fromRow = selectedRow - 1;
			int endRow = selectedRow + 1;
			// this.selectCellRange(selectedCol + fromRow, selectedCol +
			// endRow);
			this.cells(selectedCol + fromRow + ":" + selectedCol + endRow);
		}

		this.menu().showRow();

		boolean shown = false;
		WebElement hideRow = gwCurrentRow();
		if (hideRow != null
				&& !hideRow.getAttribute("style").toLowerCase()
						.contains("display: none")) {
			shown = true;
		}
		this.verifyTrue(shown, "Show the row " + index);
		return this;
	}

	/**
	 * Verify whether element class is disabled.
	 * 
	 * @param field
	 * @param isDisabled
	 * @param triggeredBy
	 * @return
	 * 
	 */
	public SpreadsheetFilePage verifyIsDisabled(EnhancedWebElement field,
			boolean isDisabled, EnhancedWebElement... triggeredBy) {
		sleep(1);
		boolean disabled = false;

		String fieldElementClass = "";
		try {
			for (EnhancedWebElement item : triggeredBy) {
				item.click();
				sleep(1);
			}

			if (field.getAttribute("text").contains("S_t_")) // toolbar
			{
				fieldElementClass = driver.findElement(
						By.xpath(String.format("//span[@widgetid='%s']", field
								.getAttribute("text").split("==")[1])))
						.getAttribute("class");
				// log.info("style:" + fieldElementClass);
				disabled = fieldElementClass.trim().contains("dijitDisabled");
				log.info("Field class attribute:" + fieldElementClass + ":"
						+ disabled);
			} else {
				fieldElementClass = field.getAttribute("class");
				disabled = fieldElementClass.contains("dijitMenuItemDisabled")
						|| fieldElementClass.contains("cke_disabled");
				log.info("Field class attribute:" + fieldElementClass + ":"
						+ disabled);
			}
		} catch (Exception e) {
			log.error("Exception while verify class is disabled:"
					+ e.getMessage());
		}

		verifyTrue(disabled == isDisabled,
				"Succeed to verify [Field:isDisabled]=" + field + ":"
						+ isDisabled);

		driver.findElement(By.tagName("body")).sendKeys(Keys.ESCAPE);

		log.info("Verify element class is disabled.");
		return this;
	}

	public SpreadsheetFilePage verifyMenuIsChecked(EnhancedWebElement field,
			boolean isChecked, EnhancedWebElement... triggeredBy) {
		sleep(1);
		boolean disabled = false;
		String fieldElementClass = "";

		log.info("Verify menu item is checked.");
		try {
			for (EnhancedWebElement item : triggeredBy) {
				item.click();
				sleep(1);
			}

			fieldElementClass = field.getAttribute("class");
			disabled = fieldElementClass
					.contains("dijitCheckedMenuItemChecked");
			log.info("Field class attribute:" + fieldElementClass + ":"
					+ disabled);

		} catch (Exception e) {
			log.error("Exception while verify class is checked:"
					+ e.getMessage());
		}

		verifyTrue(disabled == isChecked,
				"Succeed to verify [Field:isChecked]=" + field + ":"
						+ isChecked);

		driver.findElement(By.tagName("body")).sendKeys(Keys.ESCAPE);

		return this;
	}

	public SpreadsheetFilePage deleteContentByKey() {
		log.info("Delete contents by Key Delete.");
		body.sendKeys(Keys.DELETE);
		return this;
	}

	public SpreadsheetFilePage selectColumns(String colA, String colZ) {
		log.info(String.format("Try to select column %s to %s ", colA, colZ));
		int count = Math.abs(columnHeaderToIndex(colZ)
				- columnHeaderToIndex(colA));
		selectRowColumn(colA);
		if (columnHeaderToIndex(colZ) > columnHeaderToIndex(colA))
			for (int i = 0; i < count; i++)
				body.sendKeys(Keys.chord(Keys.SHIFT, Keys.RIGHT));
		else
			for (int i = 0; i < count; i++)
				body.sendKeys(Keys.chord(Keys.SHIFT, Keys.LEFT));

		return this;
	}

	public SpreadsheetFilePage selectRowColumn(String pattern) {
		sleep(2);
		Matcher m = Pattern.compile("^([A-Z]*)(\\d*)$").matcher(pattern);
		log.info("Try to select row/column " + pattern);
		if (m.matches()) {
			String col = m.group(1);
			String row = m.group(2);

			if (!col.equals("")) {
				currentCol = this.columnHeaderToIndex(col);
				currentRow = 1;
				currentCell = pattern + currentRow;
				List<WebElement> colList = gwColsByXpath();
				sleep(3);
				for (WebElement colHead : colList) {
					if (col.equalsIgnoreCase(colHead.getText())) {
						log.info("Click column head: " + col);
						colHead.click();
						break;
					}
				}
			}
			if (!row.equals("")) {
				currentCol = 1;
				currentRow = Integer.parseInt(row);
				currentCell = "A" + currentRow;
				List<WebElement> rowList = gwRowsHeaderByXpath();
				for (WebElement rowHead : rowList) {
					sleep(2);
					if ((driver.isFirefox() && driver.getBrowserVersionInt() < 9)
							&& String.valueOf(currentRow + 1).equalsIgnoreCase(
									rowHead.getText())) { // firefox 8 or lower
															// version
						rowHead.click();
						break;
					} else if ((String.valueOf(currentRow))
							.equalsIgnoreCase(rowHead.getText())) {
						rowHead.click();
						break;
					}
				}
			}
		} else {
			throw new IllegalArgumentException("Inappropriate select range.");
		}
		log.info("Successfully select row or column " + pattern);
		return this;
	}

	/**
	 * Verify the toolbar item is enabled or not
	 * 
	 * @return true: current status of the item is as same as the expected
	 * 
	 */
	public SpreadsheetFilePage verifyToolbarStatus(WebElement item,
			boolean isEnabled) {
		sleep(1);
		boolean isCorrect = false;
		boolean currentStatus = Boolean.valueOf((this.basicToolBar()
				.getItemStatus(item))[0]);
		String sCurrentStatus = "enabled";
		if (!currentStatus)
			sCurrentStatus = "disabled";

		if (currentStatus && isEnabled)
			isCorrect = true;
		if (!currentStatus && !isEnabled)
			isCorrect = true;
		this.verifyTrue(isCorrect, "Toolbar " + item + " is " + sCurrentStatus);
		return this;
	}

	/**
	 * Verify if font size value on toolbar is as expected
	 * 
	 * @param fontsize
	 * @param isEqual
	 * @return
	 * 
	 */
	public SpreadsheetFilePage verifyToolbarFontSize(String fontsize,
			boolean isEqual) {
		sleep(1);
		boolean equal = false;
		if (basicToolBar().getFontSize().equalsIgnoreCase(fontsize))
			equal = true;
		this.verifyTrue(equal == isEqual,
				"Passed: font size on toolbar is right");
		return this;

	}

	/**
	 * Open find and replace dialog from menu to find and replace.
	 * 
	 * @param target
	 *            target for find
	 * @param replace
	 *            boolean, Want to replace or not, need replacement
	 * @param replaceAll
	 *            boolean, Want to replace all or not, need replacement
	 * @param replacement
	 *            to replace the target
	 * @param matchCase
	 *            boolean, match case or not
	 * @param entireCell
	 *            boolean, match the entire cell or not
	 * @param allSheets
	 *            boolean, search all the sheets or not
	 * @return SpreadsheetFilePages throws FailedException if the find and
	 *         replace dialog is not displayed.
	 */
	public SpreadsheetFilePage findAndReplaceByMenu(String target,
			boolean replace, boolean replaceAll, String replacement,
			boolean matchCase, boolean entireCell, boolean allSheets,
			boolean isContinue) {
		log.info("Find" + target + " Replace " + replacement + "by menu");
		this.menu().findAndReplace(target, replace, replaceAll, replacement,
				matchCase, entireCell, allSheets, isContinue);
		this.currentCell = nameBox.getAttribute("value");

		return this;
	}

	public SpreadsheetFilePage findAndReplace(String target, boolean replace,
			boolean replaceAll, String replacement, boolean matchCase,
			boolean entireCell, boolean allSheets, boolean isContinue) {
		log.info("Find" + target + " Replace " + replacement);
		this.findAndReplaceDialog().findAndReplace(target, replace, replaceAll,
				replacement, matchCase, entireCell, allSheets, isContinue);
		sleep(2);
		return this;
	}

	public SpreadsheetFilePage findText(String target, boolean matchCase,
			boolean entireCell, boolean allSheets, boolean isContinue) {
		this.findAndReplace(target, false, false, "", matchCase, entireCell,
				allSheets, isContinue);
		return this;
	}

	public SpreadsheetFilePage closeFindAndReplaceDialog() {
		log.info("Close FindAndReplace dialog");
		this.findAndReplaceDialog().quit();
		return this;
	}

	/**
	 * Verify if a loop is completed in Find or Replace
	 * 
	 * @param iscompleted
	 * @return
	 * 
	 */
	public SpreadsheetFilePage verifyFindReplaceIsCompleted(boolean iscompleted) {
		sleep(1);
		boolean isend = false;
		if (this.findAndReplaceDialog().isCompleted())
			isend = true;
		verifyTrue(isend == iscompleted,
				"No more results found, looping around in find & replace");
		return this;
	}

	public SpreadsheetFilePage sendKeys(String keys) {
		log.info("Send keys:" + keys);
		sleep(1);
		this.getCell(currentCell).sendKeys(keys);
		sleep(1);
		return this;
	}

	public SpreadsheetFilePage verifyCurrentCell(String cell) {
		sleep(1);
		boolean isCurrent = false;
		if (currentCell.equalsIgnoreCase(cell)
				|| nameBox.getAttribute("value").equalsIgnoreCase(cell)) {
			isCurrent = true;
		}
		this.verifyTrue(isCurrent, "cell " + cell + " is current cell");
		return this;
	}

	/**
	 * Verify if a sheet is existed.
	 * 
	 * @param string
	 * 
	 */
	public SpreadsheetFilePage verifySheetExist(String sheetName, Boolean exist) {
		log.info("Verify if a sheet is existed");
		sleep(1);
		boolean found = false;
		sleep(1);
		for (WebElement sheet : sheetTabs1) {
			if (sheet.getText().equals(sheetName)) {
				found = true;
				break;
			}
		}
		verifyTrue(found == exist, "Current sheet name is " + sheetName);
		return this;
	}

	/**
	 * Verify the cell is displayed. For merged cells, ***
	 * 
	 * @param cell
	 * @return
	 * 
	 */
	public SpreadsheetFilePage verifyCellDisplayed(String cell, boolean exists) {
		log.info("verifyCellDisplayed: " + exists);
		boolean existed = true;
		int selectedCol = columnHeaderToIndex(getCol(cell));
		int selectedRow = getRow(cell);

		if (this.getCell(selectedRow, selectedCol).getAttribute("style")
				.toLowerCase().contains("display: none")) {
			existed = false;
		}

		this.verifyTrue(existed == exists, "Verify cell's status:" + exists);

		return this;
	}

	/**
	 * Verify if the cell is existence.
	 * 
	 * @param cell
	 * @return
	 * 
	 */
	public SpreadsheetFilePage verifyCellExists(String cell, boolean exists) {
		boolean existed = true;
		log.info("verify Cell Exists: " + exists);
		try {
			cell(cell);
		} catch (Exception e) {
			existed = false;
		}
		this.verifyTrue(existed == exists, "Verify cell's status:" + exists);
		return this;
	}

	/**
	 * Verify the style of the selected cell.
	 * 
	 * @param cell
	 * @param styleType
	 * @param value
	 * @return
	 * 
	 * @author zhanglzl
	 */
	public SpreadsheetFilePage verifyCellStyle(String cell, String styleType,
			String value) {
		sleep(1);
		boolean match = false;
		WebElement cellElement = this.getCell(cell);
		String strCSSstyle = cellElement.getCssValue(styleType).toLowerCase();
		log.info("actual value of " + styleType + ": " + strCSSstyle);
		sleep(1);
		if (!styleType.equalsIgnoreCase("display")) {
			// handle unit pt to px
			if (value.endsWith("pt")) {
				String str = value.substring(0, value.length() - 2);
				value = pt2px(Integer.parseInt(str));
				log.info("expect value is " + value + " px");
			}
			if (value.indexOf(".") > 0)
				value = driver.isChrome() ? value.substring(0,
						value.indexOf(".")) : value;
			if (strCSSstyle.contains(value)) {
				match = true;
			}
		} else {
			String strStyles = cellElement.getAttribute("style").toLowerCase();
			String[] styles = strStyles.split(";");
			for (String currentStyle : styles) {
				if (currentStyle.toLowerCase().trim().startsWith(styleType)
						&& currentStyle.toLowerCase().contains(value)) {
					match = true;
				}
			}
			if (!match && value.equalsIgnoreCase("default"))
				match = true;
		}

		this.verifyTrue(match, cell + " cell style matchs " + value);
		return this;
	}

	private SpreadsheetFilePage verifyElementStyle(WebElement cellElement,
			String styleType, String value) {
		sleep(1);
		boolean match = false;
		String strCSSstyle = cellElement.getCssValue(styleType).toLowerCase();
		log.info("actual value of " + styleType + ":" + strCSSstyle);
		sleep(1);
		// handle unit pt to px
		if (value.endsWith("pt")) {
			String str = value.substring(0, value.length() - 2);
			value = pt2px(Integer.parseInt(str));
			log.info("expect value is " + value + " px");
		}
		if (strCSSstyle.contains(value)) {
			match = true;
		}
		this.verifyTrue(match, "Header style matchs " + value);
		return this;
	}

	public SpreadsheetFilePage verifyCellBorderWidth(String cell,
			String cellwidth) {
		sleep(1);
		boolean match = false;
		WebElement cellElement = this.getCell(cell);
		String strStyles = cellElement.getAttribute("style").toLowerCase();
		String[] styles = strStyles.split(";");
		if (driver.isFirefox()) {
			for (String currentStyle : styles) {
				if (currentStyle.trim().startsWith("border-width")
						&& currentStyle.contains(cellwidth)) {
					match = true;
				}
			}
			this.verifyTrue(match, cell + " border width matchs " + cellwidth);
		} else if (driver.isInternetExplorer()
				&& driver.getBrowserVersionInt() < 9) {
			for (String currentStyle : styles) {
				if (currentStyle.contains("border-right-width")
						|| currentStyle.contains("border-bottom-width")) {
					if (currentStyle.contains(cellwidth))
						match = true;
				}
			}
			this.verifyTrue(match, "Cell border style match on IE8");
		} else {
			for (String currentStyle : styles) {
				if (currentStyle.trim().startsWith("border-width")
						&& currentStyle.contains(cellwidth)) {
					match = true;
				}
			}
			this.verifyTrue(match, cell + " border width matchs " + cellwidth);
		}
		return this;
	}

	public SpreadsheetFilePage verifyCellBorderColor(String color) {
		sleep(1);
		if (driver.isFirefox()) {
			verifyCellColor(color, "border-color",
					"selected cell border color is " + color);
		} else if (driver.isInternetExplorer()
				&& driver.getBrowserVersionInt() < 9) {
			verifyCellColor(color, "border-top-color",
					"selected cell top border color successfully on IE8");
			verifyCellColor(color, "border-bottom-color",
					"selected cell bottom border color successfully on IE8");
		} else
			verifyCellColor(color, "border-color",
					"selected cell border color is " + color);
		return this;
	}

	/**
	 * Verify cell color
	 * 
	 * @param colorvalue
	 *            : red,blue,black,white,yellow,green,grey
	 * @param colorattribute
	 * @param message
	 * @return
	 * 
	 * @author zhanglzl
	 */
	public SpreadsheetFilePage verifyCellColor(String colorvalue,
			String colorattribute, String message) {
		sleep(1);
		verifyCellColor(colorvalue, colorattribute, message, true);
		return this;
	}

	/**
	 * 
	 * @param colorvalue
	 *            : red,blue,black,white,yellow,green,grey
	 * @param colorattribute
	 *            :background-color,color
	 * @param message
	 * @param ok
	 *            : true or false
	 * @return
	 * 
	 * @author zhanglzl
	 */
	public SpreadsheetFilePage verifyCellColor(String colorvalue,
			String colorattribute, String message, boolean ok) {
		boolean match = false;
		// String gsColor = null;
		String gsStyle = getCell(currentRow, currentCol).getCssValue(
				colorattribute).toLowerCase();
		sleep(2);
		if (colorvalue.contains("default")) {
			verifyCellStyle(currentCell, "background-color", "transparent");
			return this;
		}
		for (String cellColor : COLOR.getColorValues(colorvalue)) {
			if (gsStyle.contains(cellColor))
				match = true;
		}
		if ((!gsStyle.contains(colorattribute))
				&& colorvalue.equalsIgnoreCase("default"))
			match = true;
		sleep(1);
		verifyTrue(match == ok, message);
		return this;
	}

	public SpreadsheetFilePage zoomIn() {
		driver.getKeyboard().sendKeys(Keys.CONTROL, Keys.ADD);
		sleep(5);
		return this;
	}

	// public SpreadsheetFilePage showBuildID() {
	// log.info("start to get the build id and show it");
	// try {
	// log.info("Get build ID");
	// // set cookie
	// Set<Cookie> cook = driver.manage().getCookies();
	// Iterator<Cookie> itr = cook.iterator();
	// HttpClient newclient = new HttpClient();
	// HttpState cs = newclient.getState();
	//
	// String typeQueryPath = AutoProperties.gsConnectionBaseURL
	// + "/docs/version.txt";
	// GetMethod getType = new GetMethod(typeQueryPath);
	// newclient.executeMethod(getType);
	// if (getType.getStatusCode() == 200) {
	// String timestamp = null;
	// String des = null;
	// String typeList = getType.getResponseBodyAsString();
	// JSONObject types = JSONObject.parse(typeList);
	// Set<String> allJson = types.keySet();
	// for (String name : allJson) {
	// if (name.contains("build_timestamp")) {
	// timestamp = (String) types.get(name);
	// } else if (name.contains("build_description")) {
	// des = (String) types.get(name);
	// }
	// }
	// log.error(BuildNoFlag + des + " " + timestamp);
	// } else {
	// log.error(getType.getStatusCode() + " : Can't get build number");
	// throw (new Exception());
	// }
	// sleep(2);
	// } catch (Exception e) {
	// log.info("Cannot get build number");
	// }
	// return this;
	// }

	public SpreadsheetFilePage sleep(long seconds) {
		driver.sleep(seconds);
		return this;
	}

	public void waitForSync() {
		sleep(2);
	}

	public SpreadsheetFilePage showOrHide(VIEWITEM viewItem, boolean showOrHide) {
		menu.showOrHide(viewItem, showOrHide);
		log.info(String.format("Show or hide bar: [%s:%s].", viewItem,
				showOrHide));
		return this;
	}

	/**
	 * save as draft
	 * 
	 * @return
	 * 
	 */
	public SpreadsheetFilePage saveAsDraft() {
		log.info("Save as draft");
		menu.saveAsDraft();
		return this;
	}

	/**
	 * This method is to set 'currentSheet'. Normally uses after undo sheet
	 * deletion operation which cause the deleted grid number is changed to
	 * total sheetnumber+1.
	 * 
	 * @param sheetNum
	 *            : start from 1
	 * @return
	 */
	public SpreadsheetFilePage setCurrentSheet(int sheetNum) {
		this.currentSheet = sheetNum;
		return this;
	}

	public SpreadsheetFilePage viewSidebarCollapse() {
		menu().viewSidebar(false);
		sleep(2);
		log.info("Set sidebar collapse successfully");
		return this;
	}

	public SpreadsheetFilePage viewSidebarOpen() {
		menu().viewSidebar(true);
		sleep(2);
		log.info("Set sidebar open successfully");
		return this;
	}

	public SpreadsheetFilePage viewToolbar() {
		menu().viewToolbar(true);
		sleep(2);
		log.info("Set toolbar open successfully");
		return this;
	}

	public SpreadsheetFilePage viewFormulaBar() {
		menu().viewFormulaBar(true);
		sleep(2);
		log.info("Set formula bar open successfully");
		return this;
	}

	public SpreadsheetFilePage setDocumentLocale(String local) {
		log.info("Try to set document locale to " + local);
		menu().setLocale();
		sleep(2);
		this.setLocaleDialog().setLocale(local);
		sleep(1);
		return this;

	}

	public SpreadsheetFilePage showUnsupportedMessage(boolean yesornot) {
		menu().showUnsupportedMessage(yesornot);
		sleep(2);
		if (yesornot)
			log.info("Set 'show unsupported message' successfully");
		else
			log.info("Disable 'show unsupported message' successfully");
		return this;
	}

	/**
	 * Verify range existed by its name & refersto in Manage Name Range dialog
	 * 
	 * @param index
	 *            : index of range expect to verify in Manage Name Range dialog
	 * @param expectedname
	 * @param expectedrefersto
	 * @return
	 * 
	 */
	public SpreadsheetFilePage verifyRangeExisted(int index,
			String expectedname, String expectedrefersto) {
		sleep(2);
		log.info("Open ManageNamedRanges dialog");
		menu().manageRanges();
		String tmp = manageNamedRangesDialog().getNamedRangeName(index);
		verifyTrue(tmp.contains(expectedname),
				"In ManageNamedRanges dialog, exist name range " + expectedname);
		if (!expectedrefersto.isEmpty()) {
			sleep(1);
			String refer = manageNamedRangesDialog().getNamedRangeRefersto(
					index);
			manageNamedRangesDialog().closeDlg();
			verifyTrue(refer.contains(expectedrefersto), "Refersto of "
					+ expectedname + " is correct:" + expectedrefersto);
		} else
			log.info("Refersto should be null");
		return this;
	}

	/**
	 * Verify range refersto is correct in Manage Name Range dialog
	 * 
	 * @param name
	 *            : range name
	 * @param expectedrefersto
	 * @return
	 * 
	 */
	public SpreadsheetFilePage verifyRangeRefersto(String name,
			String expectedrefersto) {
		sleep(2);
		menu().manageRanges();
		if (!expectedrefersto.isEmpty()) {
			sleep(1);
			String refer = manageNamedRangesDialog()
					.getNamedRangeRefersto(name).trim();
			log.info("the actual range refersto is " + refer);
			manageNamedRangesDialog().closeDlg();
			verifyTrue(refer.equals(expectedrefersto.trim()),
					"Refersto of Name range is :" + expectedrefersto);
		} else
			log.info("Refersto should be null");
		return this;
	}

	/**
	 * Set Refersto of a name range in Manage Name Range dialog
	 * 
	 * @param value
	 * @return
	 * 
	 */
	public SpreadsheetFilePage setNameRangeRefersto(String value) {
		sleep(2);
		log.info("Set Refersto of a name range to " + value
				+ " in Manage Name Range dialog");
		menu().manageRanges();
		manageNamedRangesDialog().setRefersto(value);
		manageNamedRangesDialog().closeDlg();
		return this;
	}

	/**
	 * New a name range by New Named Range dialog
	 * 
	 * @param name
	 *            : range name
	 * @param refersto
	 *            : range scope
	 * @return
	 * 
	 */
	public SpreadsheetFilePage newRange(String name, String refersto) {
		sleep(2);
		log.info("Create range:" + name + "," + refersto
				+ " by New Named Range dialog");
		menu().newNamedRange();
		newNamedRangeDialog().AddNewNamedRange(name, refersto);
		return this;
	}

	/**
	 * Verify if unsupported formula icon exists in current cell
	 * 
	 * @param exist
	 * @return
	 */
	public SpreadsheetFilePage verifyUnsupportedIcon(boolean exist) {
		log.info("Verify if unsupported formula icon is existed in cell"
				+ currentCell);
		sleep(2);
		int unsupported = gwCell2(currentRow, currentCol).findElements(
				By.className("unsupportedformula")).size();
		int image = gwCell2(currentRow, currentCol).findElements(
				By.tagName("img")).size();
		boolean yes = false;
		if (image == 1 && unsupported == 1)
			yes = true;
		this.verifyTrue(yes == exist, "Verify if unsupported formula icon is "
				+ exist + " existed");
		return this;
	}

	/**
	 * Verify if there is unsupported formula icon in current cell
	 * 
	 * @return
	 */
	public boolean hasUnsupportIcon() {
		int unsupported = gwCell2(currentRow, currentCol).findElements(
				By.className("unsupportedformula")).size();
		int image = gwCell2(currentRow, currentCol).findElements(
				By.tagName("img")).size();
		if (image == 1 && unsupported == 1)
			return true;
		else
			return false;
	}

	/**
	 * Click OK button to confirm CVS file import
	 * 
	 * @return
	 */
	@Find("id=S_d_ImportText")
	private EnhancedWebElement importDialog;

	public SpreadsheetFilePage importCsvOk() {
		log.info("Try to click 'Import' button on 'Import Text' dailog for csv file");
		sleep(4);
		importDialog.findElement(By.id("S_d_ImportTextOKButton")).click();
		sleep(5);
		return this;
	}

	public SpreadsheetFilePage importFile(File file) {
		log.info("Try to click 'browse' button on 'Text Import' dailog for txt or csv file");
		sleep(4);
		// importTextDialog.findElement(By.id("S_d_InsertTextFile")).click();
		importTextDialogFile.sendKeys(driver.upload(file));
		importTextDlgOk.click();
		driver.sleep(2);
		return this;
	}

	@Find("id=commaOption")
	private EnhancedWebElement commaOption;
	@Find("id=semicolonOption")
	private EnhancedWebElement semicolonOption;
	@Find("id=tabOption")
	private EnhancedWebElement tabOption;
	@Find("id=spaceOption")
	private EnhancedWebElement spaceOption;

	public SpreadsheetFilePage importFileCommaOption(File file) {
		log.info("Try to click 'browse' button on 'Text Import' dailog for txt or csv file");
		sleep(2);
		importTextDialogFile.sendKeys(driver.upload(file));
		commaOption.click();
		importTextDlgOk.click();
		driver.sleep(2);
		return this;
	}
	
	public SpreadsheetFilePage importFileSemicolonOption(File file) {
		log.info("Try to click 'browse' button on 'Text Import' dailog for txt or csv file");
		sleep(2);
		importTextDialogFile.sendKeys(driver.upload(file));
		semicolonOption.click();
		importTextDlgOk.click();
		driver.sleep(2);
		return this;
	}
	
	public SpreadsheetFilePage importFileTabOption(File file) {
		log.info("Try to click 'browse' button on 'Text Import' dailog for txt or csv file");
		sleep(2);
		importTextDialogFile.sendKeys(driver.upload(file));
		tabOption.click();
		importTextDlgOk.click();
		driver.sleep(2);
		return this;
	}
	
	public SpreadsheetFilePage importFileSpaceOption(File file) {
		log.info("Try to click 'browse' button on 'Text Import' dailog for txt or csv file");
		sleep(2);
		importTextDialogFile.sendKeys(driver.upload(file));
		spaceOption.click();
		importTextDlgOk.click();
		driver.sleep(2);
		return this;
	}

	public String getFormulaBarText() {
		return formulaBar.getAttribute("value").trim();
	}

	public String getNameBoxContent() {
		sleep(1);
		return nameBox.getAttribute("value");
	}

	/**
	 * Return cell value in (row, column)
	 * 
	 * @param row
	 *            start from 0
	 * @param column
	 *            start from 0
	 * @return
	 */
	public String getCellValue2(int row, int column) {
		return gwCell2(row + 1, column + 1).getText().trim();
	}

	public int getSheetTotalNum() {
		return sheetTabs1.size();
	}

	public int getColumnNum() {
		return gwColHeaders().size() - 1;
	}

	/**
	 * Toolbar definition
	 */
	public class SpreadSheetBasicToolbar extends BasePage {

		@Find("id=S_t_Save")
		public EnhancedWebElement Save;
		@Find("id=S_t_Undo")
		public EnhancedWebElement Undo;
		@Find("id=S_t_Redo")
		public EnhancedWebElement Redo;
		@Find("id=S_t_AddComment")
		public EnhancedWebElement AddComment;

		@Find("id=S_t_FontName")
		public EnhancedWebElement FontName;
		@Find("id=S_m_FontName_toolbar")
		public EnhancedWebElement FontNameTable;
		@Find("id=S_t_FontSize")
		public EnhancedWebElement FontSize;
		@Find("id=S_t_Bold")
		public EnhancedWebElement Bold;
		@Find("id=S_t_Italic")
		public EnhancedWebElement Italic;
		@Find("id=S_t_Underline")
		public EnhancedWebElement Underline;
		@Find("id=S_t_Strikethrough")
		public EnhancedWebElement StrikeThrough;

		@Find("id=S_t_BorderColor")
		public EnhancedWebElement BorderColor;
		@Find("id=S_t_FontColor")
		public EnhancedWebElement FontColor;
		@Find("id=S_t_BgColor")
		public EnhancedWebElement BackgroundColor;
		@Find("id=S_m_BorderColorTable")
		public EnhancedWebElement BorderColorTable;
		@Find("id=S_m_FontColorTable")
		public EnhancedWebElement FontColorTable;
		@Find("id=S_m_BackgroundColor")
		public EnhancedWebElement BackgroundColorTable;

		@Find("id=S_t_Currency")
		public EnhancedWebElement FormatAsCurrency;
		@Find("id=S_t_Percent")
		public EnhancedWebElement FormatAsPercent;
		@Find("id=S_t_NumberFormat")
		public EnhancedWebElement FormatNumber;

		@Find("id=S_t_InstantFilter")
		public EnhancedWebElement InstantFilter;

		@Find("id=S_t_LeftAlign")
		public EnhancedWebElement AlignLeft;
		@Find("id=S_t_CenterAlign")
		public EnhancedWebElement AlignCenter;
		@Find("id=S_t_RightAlign")
		public EnhancedWebElement AlignRight;
		@Find("id=S_t_MergeSplitCell")
		public EnhancedWebElement MergeSplitCell;
		@Find("id=S_t_WrapText")
		public EnhancedWebElement Wrap;

		@Find("id=S_i_InsertRowAboveDropDown")
		public EnhancedWebElement insertRowAbove;
		@Find("id=S_i_InsertRowBelowDropDown")
		public EnhancedWebElement insertRowBelow;
		@Find("id=S_i_DeleteRowDropDown")
		public EnhancedWebElement deleteRow;
		@Find("id=S_i_InsertColumnBeforeDropDown")
		public EnhancedWebElement insertColBefore;
		@Find("id=S_i_InsertColumnDropDown")
		public EnhancedWebElement insertColAfter;
		@Find("id=S_i_DeleteColumnDropDown")
		public EnhancedWebElement deleteCol;
		@Find("id=S_t_InsertDeleteRow")
		public EnhancedWebElement RowOperList;
		@Find("id=S_t_InsertDeleteCol")
		public EnhancedWebElement ColOperList;

		@Find("id=S_t_QuickFormula")
		public EnhancedWebElement QuickFormula;

		@Find("id=S_t_InsertChart")
		public EnhancedWebElement InsertChart;

		@Find("class=dijitDialogTitleBar")
		public EnhancedWebElement TitleBar;

		/**
		 * New the spreadsheet basic toolbar
		 */
		public SpreadSheetBasicToolbar(EnhancedWebDriver driver) {
			super(driver);
		}

		/**
		 * get the toolbar item status, enabled or disabled
		 * 
		 * @param item
		 *            , toolbar item name, please find the name in UI map
		 * @return, String[]: String[0]: enabled or not, true is enabled, false
		 *          is disabled; String[1]: selected or not, true is selected,
		 *          false is not selected
		 */
		public String[] getItemStatus(WebElement item) {
			String[] currentStatus = new String[2];
			String isEnabled = item.getAttribute("aria-disabled");
			if (isEnabled != null && isEnabled.equalsIgnoreCase("true"))
				currentStatus[0] = "false";
			else
				currentStatus[0] = "true";
			if (!Boolean.valueOf(currentStatus[0]))
				return currentStatus;
			String isSelected = item.getAttribute("aria-pressed");
			if (isSelected != null && isSelected.equalsIgnoreCase("true"))
				currentStatus[1] = "true";
			else
				currentStatus[1] = "false";
			return currentStatus;
		}

		/**
		 * Click the wrap button from the toolbar
		 * 
		 * @return SpreadSheetBasicToolbar
		 */
		public SpreadSheetBasicToolbar wrapText() {
			Wrap.click();
			return this;
		}

		/**
		 * Click the Instant Filter button on toolbar
		 * 
		 * @return
		 */
		public SpreadSheetBasicToolbar instantFilter() {
			InstantFilter.click();
			sleep(3);
			return this;
		}

		/**
		 * Click the merge/split button from the toolbar
		 * 
		 * @return SpreadSheetBasicToolbar
		 */
		public SpreadSheetBasicToolbar mergeSplitCell() {
			MergeSplitCell.click();
			sleep(3);
			mergeCellsDialog().submit();
			return this;
		}

		/**
		 * Click the merge/split button on toolbar, then click ok or cancel
		 * 
		 * @return SpreadSheetBasicToolbar
		 */
		public SpreadSheetBasicToolbar mergeSplitCell(Boolean ok) {
			MergeSplitCell.click();
			if (TitleBar != null && TitleBar.isDisplayed() && ok) {
				mergeCellsDialog().submit();
			} else if (!ok && TitleBar != null && TitleBar.isDisplayed()) {
				mergeCellsDialog().cancel();
			}
			return this;
		}

		/**
		 * Set font color from the toolbar
		 * 
		 * @param color
		 *            : the color name, eg. orange
		 * @return SpreadSheetBasicToolbar
		 */
		public SpreadSheetBasicToolbar setBorderColor(String color) {
			BorderColor.click();
			List<WebElement> colorElements = BorderColorTable.findElements(By
					.className("dijitPaletteCell"));
			if (colorElements.size() <= 0) {
				log.info("Could not find colors");
			}
			for (WebElement colorEle : colorElements) {
				if (colorEle.getAttribute("title").equalsIgnoreCase(color)) {
					colorEle.click();
					break;
				}
			}
			return this;
		}

		/**
		 * Set font size from toolbar
		 * 
		 * @param size
		 *            , the size of the font: 8,9,10,11,12,14,16,18,20,22,24
		 * @return SpreadSheetBasicToolbar
		 */
		@Find("id=$0")
		public EnhancedWebElement fontSize;

		public SpreadSheetBasicToolbar setFontSize(int size) {
			FontSize.click();

			String sizeValue = null;
			switch (size) {
			case 8:
				sizeValue += "S_i_Eight";
				break;
			case 9:
				sizeValue += "S_i_Nine";
				break;
			case 10:
				sizeValue += "S_i_Ten";
				break;
			case 11:
				sizeValue += "S_i_Eleven";
				break;
			case 12:
				sizeValue += "S_i_Twelve";
				break;
			case 14:
				sizeValue += "S_i_Fourteen";
				break;
			case 16:
				sizeValue += "S_i_Sixteen";
				break;
			case 18:
				sizeValue += "S_i_Eighteen";
				break;
			case 20:
				sizeValue += "S_i_Twenty";
				break;
			case 22:
				sizeValue += "S_i_TwentyTwo";
				break;
			case 24:
				sizeValue += "S_i_TwentyFour";
				break;
			default:
				log.info("unprocessed font size:" + size);
				throw new IllegalArgumentException("unprocessed font size:"
						+ size);
			}

			fontSize.setLocatorArgument(sizeValue);
			fontSize.click();
			return this;
		}

		public String getFontSize() {
			return FontSize.getText();

		}

		/**
		 * insert row above or delete current row
		 * 
		 * @param oper
		 *            operation inluding RowOperation.ABOVE, RowOperation.DELETE
		 * @return
		 */
		public SpreadSheetBasicToolbar setRowOperation(RowOperation oper) {
			RowOperList.click();

			WebElement sizeValue = null;
			switch (oper) {
			case ABOVE:
				sizeValue = insertRowAbove;
				break;
			case BELOW:
				sizeValue = insertRowBelow;
				break;
			case DELETE:
				sizeValue = deleteRow;
				break;
			}
			sizeValue.click();
			return this;
		}

		/**
		 * insert column before or delete current column
		 * 
		 * @param oper
		 *            , operation including ColOperation.BEFORE,
		 *            ColOperation.DELETE
		 * @return
		 */
		public SpreadSheetBasicToolbar setColOperation(ColOperation oper) {
			ColOperList.click();

			WebElement sizeValue = null;
			switch (oper) {
			case BEFORE:
				sizeValue = insertColBefore;
				break;
			case AFTER:
				sizeValue = insertColAfter;
				break;
			case DELETE:
				sizeValue = deleteCol;
				break;
			}
			sizeValue.click();
			return this;
		}

		/**
		 * Set the data format to currentcy by clicking currency button from
		 * toolbar
		 * 
		 * @return SpreadSheetBasicToolbar
		 */
		public SpreadSheetBasicToolbar setDataFormatToCurrency() {
			FormatAsCurrency.click();
			return this;
		}

		/**
		 * Set the data format to percent by clicking percent button on toolbar
		 * 
		 * @return SpreadSheetBasicToolbar
		 */
		public SpreadSheetBasicToolbar setDataFormatToPercent() {
			FormatAsPercent.click();
			return this;
		}

		// start common toolbar
		public SpreadSheetBasicToolbar save() {

			Save.click();
			return this;
		}

		public SpreadSheetBasicToolbar undo() {
			Undo.click();
			return this;
		}

		public SpreadSheetBasicToolbar redo() {
			Redo.click();
			return this;
		}

		public SpreadSheetBasicToolbar setFontToBold() {
			Bold.click();
			return this;
		}

		public SpreadSheetBasicToolbar setFontToItalic() {
			Italic.click();
			return this;
		}

		public SpreadSheetBasicToolbar setFontToUnderline() {
			Underline.click();
			return this;
		}

		public SpreadSheetBasicToolbar setFontToStrike() {
			StrikeThrough.click();
			return this;
		}

		/**
		 * Set the left align
		 * 
		 * @return
		 */
		public SpreadSheetBasicToolbar setAlignLeft() {
			AlignLeft.click();
			return this;
		}

		/**
		 * Set the center align
		 * 
		 * @return
		 */
		public SpreadSheetBasicToolbar setAlignCenter() {
			AlignCenter.click();
			return this;
		}

		/**
		 * Set the right align
		 * 
		 * @return
		 */
		public SpreadSheetBasicToolbar setAlignRight() {
			AlignRight.click();
			return this;
		}

		/**
		 * Set font color
		 * 
		 * @param color
		 *            the color name, eg. orange
		 * @return
		 */
		public SpreadSheetBasicToolbar setFontColor(String color) {
			FontColor.click();
			List<WebElement> colorElements = FontColorTable.findElements(By
					.className("dijitPaletteCell"));
			if (colorElements.size() <= 0) {
				log.error("Could not find colors");
			}
			for (WebElement colorEle : colorElements) {
				if (colorEle.getAttribute("title").equalsIgnoreCase(color)) {
					colorEle.click();
					break;
				}
			}
			return this;
		}

		/**
		 * Set background color
		 * 
		 * @param color
		 *            : the color name, eg. orange
		 * @return
		 */
		public SpreadSheetBasicToolbar setBgColor(String color) {
			BackgroundColor.click();
			List<WebElement> colorElements = BackgroundColorTable
					.findElements(By.className("dijitPaletteCell"));
			if (colorElements.size() <= 0) {
				log.info("Could not find colors");
			}
			for (WebElement colorEle : colorElements) {
				if (colorEle.getAttribute("title").equalsIgnoreCase(color)) {
					colorEle.click();
					break;
				}
			}
			return this;
		}

	}// end of BasicToolBar

	/*
	 * Dialog of Spreadsheet Editor for insert/rename sheet
	 */
	public class SimpleInputDialog extends BasePage {

		@Find("css=#C_d_InputBoxInputArea")
		public EnhancedWebElement inputTextBox;
		@Find("css=#C_d_InputBoxOKButton")
		public EnhancedWebElement OK;
		@Find("css=#C_d_InputBoxCancelButton")
		public EnhancedWebElement Cancel;

		public SimpleInputDialog(EnhancedWebDriver driver) {
			super(driver);
		}

		public SimpleInputDialog input(String sheetName) {
			inputTextBox.clear();
			inputTextBox.sendKeys(sheetName);
			return this;
		}

		public SimpleInputDialog inputAndSubmit(String sheetName) {
			input(sheetName);
			inputTextBox.sendKeys(sheetName + Keys.ENTER);
			return this;
		}

		public SimpleInputDialog submit() {
			OK.sendKeys(Keys.RETURN);
			return this;
		}

		public SimpleInputDialog cancel() {
			Cancel.sendKeys(Keys.RETURN);
			return this;
		}
	}

	/*
	 * Dialog of Spreadsheet Editor for delete sheet
	 */
	public class SimpleInformationDialog extends BasePage {
		public SimpleInformationDialog(EnhancedWebDriver driver) {
			super(driver);
		}

		public SimpleInformationDialog submit() {
			sleep(1);
			OK.sendKeys(Keys.RETURN);
			return this;
		}

		public SimpleInformationDialog cancel() {
			sleep(1);
			Cancel.sendKeys(Keys.RETURN);
			return this;
		}
	}

	// Instant Filter panel
	public class InstantFilterPanel extends BasePage {

		@Find("class=filterContextMenuContainer")
		public EnhancedWebElements filterPanel;
		@Find("css=div.filterContextMenuKeywordContainer div.filterContextMenuItem")
		public EnhancedWebElements filterItems;
		@Find("css=div.filterContextMenuButtonContainer span.dijitReset.dijitInline.dijitButtonText")
		public EnhancedWebElements filterOKs;
		@Find("id=FilterHeader_st$0_$1")
		public EnhancedWebElement filterIcon;
		@Find("id=FilterHeader_os$0_$1")
		public EnhancedWebElement filterIconUpload;

		public InstantFilterPanel(EnhancedWebDriver driver) {
			super(driver);
		}

		public InstantFilterPanel instantFilterCheckKeywords(
				String... filterwords) {
			List<String> cellfilter = Arrays.asList(filterwords);
			Collections.sort(cellfilter);
			sleep(1);
			// Clear all selected items
			instantFilterClearAll();
			// get all filter keywords in Filter panel:
			// filterContextMenuKeywordContainer
			// WebElement filterlist = driver
			// .findElementByClassName("filterContextMenuKeywordContainer");
			// List<WebElement> filteritems = filterKeysList.findElements(By
			// .className("filterContextMenuItem"));
			sleep(2);

			for (int gt = 0; gt < cellfilter.size(); gt++) {
				for (int i = 0; i < filterItems.size(); i++) {
					if (cellfilter.get(gt).equalsIgnoreCase(
							filterItems.get(i).getText())) {
						sleep(1);
						filterItems.get(i).click();
						break;
					}
				}
			}
			sleep(2);
			return this;
		}

		public InstantFilterPanel instantFilterClearAll() {
			sleep(3);
			// get clear in Filter panel
			// List<WebElement> containers = driver
			// .findElementsByClassName("filterContextMenuContainer");
			WebElement filterclear = null;
			int i2 = filterPanel.size();
			for (int i = 0; i < i2; i++) {
				if (filterPanel.get(i).isDisplayed())
					filterclear = filterPanel.get(i).findElement(
							By.id("S_d_FilterContextMenuClear"));
			}
			sleep(2);
			filterclear.click();
			sleep(1);
			return this;
		}

		public InstantFilterPanel instantFilterSelectAll() {
			sleep(3);
			// get clear in Filter panel
			// List<WebElement> containers = driver
			// .findElementsByClassName("filterContextMenuContainer");
			WebElement filterclear = null;
			int i2 = filterPanel.size();
			for (int i = 0; i < i2; i++) {
				if (filterPanel.get(i).isDisplayed())
					filterclear = filterPanel.get(i).findElement(
							By.id("S_d_FilterContextMenuSelectAll"));
			}
			sleep(2);
			filterclear.click();
			sleep(1);
			return this;
		}

		public InstantFilterPanel instantFilterSort(boolean sortascending) {
			sleep(3);
			// get clear in Filter panel
			// List<WebElement> containers = driver
			// .findElementsByClassName("filterContextMenuContainer");
			WebElement filterclear = null;
			int i2 = filterPanel.size();
			for (int i = 0; i < i2; i++) {
				if (filterPanel.get(i).isDisplayed())
					if (sortascending)
						filterclear = filterPanel.get(i).findElement(
								By.id("S_d_FilterContextMenuSortAZ"));
					else
						filterclear = filterPanel.get(i).findElement(
								By.id("S_d_FilterContextMenuSortZA"));
			}
			sleep(2);
			filterclear.click();
			sleep(1);
			return this;
		}

		public InstantFilterPanel instantFilterClickOK() {
			sleep(2);
			// get clear in Filter panel
			// List<WebElement> filterOKs = driver
			// .findElementByClassName("filterContextMenuButtonContainer")
			// .findElements(
			// By.xpath("//span[@class='dijitReset dijitInline dijitButtonText']"));
			for (WebElement filterOK : filterOKs) {
				if (filterOK.getText().equals("OK")) {
					sleep(1);
					filterOK.click();
				}
			}
			sleep(1);
			return this;
		}

		public InstantFilterPanel instantFilterOpenPanel() {
			sleep(2);
			// click Filter button on cell
			// String id_filter = "FilterHeader_st" + (currentSheet - 1) + "_"
			// + currentCol;
			// String id_filter_upload = "FilterHeader_os" + currentSheet + "_"
			// + currentCol;
			sleep(1);
			log.debug("Click filter icon");
			try {
				// driver.findElementById(id_filter).click();
				filterIcon.setLocatorArgument(currentSheet - 1, currentCol);
				filterIcon.click();
			} catch (Exception e) {
				// driver.findElementById(id_filter_upload).click();
				filterIconUpload.setLocatorArgument(currentSheet, currentCol);
				filterIconUpload.click();
			}
			sleep(1);
			boolean ok = false;
			// List<WebElement> containers = driver
			// .findElementsByClassName("filterContextMenuContainer");
			for (WebElement container : filterPanel)
				if (container.isDisplayed())
					ok = true;
			verifyTrue(ok, "open instant filter setting panel successfully");
			sleep(1);
			return this;

		}

		public InstantFilterPanel verifyInstantFilterChecked(boolean ischecked,
				String... filterwords) {
			List<String> cellfilter = Arrays.asList(filterwords);
			Collections.sort(cellfilter);
			sleep(2);
			// get all filter keywords in Filter panel
			// WebElement filterlist = driver
			// .findElementByClassName("filterContextMenuKeywordContainer");
			// sleep(1);
			// List<WebElement> filteritems = filterlist.findElements(By
			// .className("filterContextMenuItem"));
			sleep(1);
			for (int gt = 0; gt < cellfilter.size(); gt++) {
				for (int i = 0; i < filterItems.size(); i++) {
					if (cellfilter.get(gt).equalsIgnoreCase(
							filterItems.get(i).getText())) {
						sleep(1);
						String checked = filterItems.get(i).getAttribute(
								"aria-checked");
						log.debug("start to verify filter item "
								+ filterItems.get(i).getText()
								+ " checked status: " + checked);
						if (ischecked)
							verifyTrue(checked.contains("true"), filterItems
									.get(i).getText()
									+ " in filter panel is checked");
						else
							verifyTrue(checked.contains("false"), filterItems
									.get(i).getText()
									+ " in filter panel is NOT checked");
						sleep(1);
						break;
					}
				}
			}
			sleep(3);
			return this;
		}

		public InstantFilterPanel verifyInstantFilterExist(String cell,
				boolean isexisted) {
			boolean hasfilter = false;
			sleep(1);
			log.debug("Verify whether instant filter exist in " + cell);
			try {
				// String id_filter = "FilterHeader_st" + (currentSheet - 1) +
				// "_"
				// + currentCol;
				// driver.findElementById(id_filter);
				filterIcon.setLocatorArgument(currentSheet - 1, currentCol);
				filterIcon.click();
				hasfilter = true;
			} catch (Exception e) {
				try {
					// B2: FilterHeader_os1_2
					// driver.findElementById(id_filter_upload);
					log.debug("In uploaded file, verify instant filter existence in "
							+ cell + "is " + isexisted);

					filterIconUpload.setLocatorArgument(currentSheet,
							currentCol);
					filterIconUpload.click();
					hasfilter = true;
				} catch (Exception ee) {
				}
			}
			verifyTrue(hasfilter == isexisted, "instant filter is exsited: "
					+ hasfilter);
			sleep(1);
			return this;
		}

		public InstantFilterPanel verifyInstantFilterKeywords(
				String... filterwords) {
			List<String> cellfilter = Arrays.asList(filterwords);
			Collections.sort(cellfilter);
			sleep(1);
			// get all filter keywords in Filter panel
			// filterlist=driver.findElementByClassName("filterKeywordContainer");
			// WebElement filterlist = driver
			// .findElementByClassName("filterContextMenuKeywordContainer");
			// sleep(1);
			// List<WebElement> filteritems = filterlist.findElements(By
			// .className("filterContextMenuItem"));

			// compare size
			if (cellfilter.size() != (filterItems.size())) // defect 13068
				verifyTrue(false, "total number of filters are not correct");

			for (int i = 0; i < cellfilter.size(); i++) {
				if (!cellfilter.get(i).equalsIgnoreCase(
						filterItems.get(i).getText())) {
					verifyTrue(false,
							"value in filter panel is different in item: " + i);
				}
			}
			return this;
		}
	}

	public class FindAndReplaceDialog extends BasePage {
		@Find("id=S_d_FindAndReplaceDlgFindTxt")
		public EnhancedWebElement FindTxt;
		@Find("id=S_d_FindAndReplaceDlgFind")
		public EnhancedWebElement FindButton;
		@Find("id=S_d_FindAndReplaceDlgReplaceTxt")
		public EnhancedWebElement ReplaceTxt;
		@Find("id=S_d_FindAndReplaceDlgReplace")
		public EnhancedWebElement ReplaceButton;
		@Find("id=S_d_FindAndReplaceDlgMatchCase")
		public EnhancedWebElement MatchCase;
		@Find("id=S_d_FindAndReplaceDlgMatchCell")
		public EnhancedWebElement MatchCell;
		@Find("id=S_d_FindAndReplaceDlgSearchAll")
		public EnhancedWebElement SearchAll;
		@Find("id=S_d_FindAndReplaceDlgReplaceAll")
		public EnhancedWebElement ReplaceAll;
		@Find("id=S_d_FindAndReplaceDlgOKButton")
		public EnhancedWebElement OKButton;
		@Find("id=ConcordWarnMsgS_d_FindAndReplaceDlg")
		public EnhancedWebElement WarnMsg;

		public FindAndReplaceDialog(EnhancedWebDriver driver) {
			super(driver);
		}

		public FindAndReplaceDialog inputFindTxt(String target) {
			FindTxt.clear();
			FindTxt.sendKeys(target);
			return this;
		}

		public FindAndReplaceDialog inputReplaceTxt(String replacement) {
			ReplaceTxt.clear();
			ReplaceTxt.sendKeys(replacement);
			return this;
		}

		public FindAndReplaceDialog matchCase(boolean matchCase) {
			boolean current = MatchCase.getAttribute("aria-pressed")
					.equalsIgnoreCase("true");
			if (Boolean.valueOf(matchCase).compareTo(Boolean.valueOf(current)) != 0)
				MatchCase.click();
			return this;
		}

		public FindAndReplaceDialog matchCell(boolean matchCell) {
			boolean current = MatchCell.getAttribute("aria-pressed")
					.equalsIgnoreCase("true");
			if (Boolean.valueOf(matchCell).compareTo(Boolean.valueOf(current)) != 0)
				MatchCell.click();
			return this;
		}

		public FindAndReplaceDialog searchAll(boolean searchAll) {
			boolean current = SearchAll.getAttribute("aria-pressed")
					.equalsIgnoreCase("true");
			if (Boolean.valueOf(searchAll).compareTo(Boolean.valueOf(current)) != 0)
				SearchAll.click();
			return this;
		}

		public FindAndReplaceDialog find() {
			sleep(1);
			FindButton.click();
			return this;
		}

		public FindAndReplaceDialog replace() {
			sleep(1);
			ReplaceButton.click();
			return this;
		}

		public FindAndReplaceDialog replaceAll() {
			ReplaceAll.click();
			return this;
		}

		public FindAndReplaceDialog quit() {
			OKButton.click();
			return this;
		}

		/**
		 * Find or Replace is completed in a loop
		 * 
		 * @return
		 */
		public boolean isCompleted() {
			boolean loop_end = false;
			loop_end = WarnMsg.getText().toString()
					.contains("to end of the sheet, continue");
			log.info("find/replace is completed in a loop");
			return loop_end;
		}

		public FindAndReplaceDialog findAndReplace(String target,
				boolean replace, boolean replaceAll, String replacement,
				boolean matchCase, boolean entireCell, boolean allSheets,
				boolean isContinue) {

			findAndReplaceDialog.inputFindTxt(target);
			if (replace || replaceAll)
				findAndReplaceDialog.inputReplaceTxt(replacement);
			findAndReplaceDialog.matchCase(matchCase);
			findAndReplaceDialog.matchCell(entireCell);
			findAndReplaceDialog.searchAll(allSheets);
			if (replace) {
				findAndReplaceDialog.replace();
			} else if (replaceAll) {
				findAndReplaceDialog.replaceAll();
			} else {
				findAndReplaceDialog.find();
			}
			if (!isContinue) {
				findAndReplaceDialog.quit();
			}
			return this;
		}
	}

	/**
	 * Manage Named Ranges dialog
	 * 
	 * @author zhanglzl
	 * 
	 */
	public class ManageNamedRangesDialog extends BasePage {
		@Find("id=S_d_nameRangeInputArea")
		public EnhancedWebElement nameRange;
		@Find("id=ModifyButton")
		public EnhancedWebElement Apply;
		// public String Refersto;
		// public String New;
		@Find("id=S_d_nameRangeOKButton")
		public EnhancedWebElement Close;
		@Find("css=#S_d_allNameRangesList tr")
		public EnhancedWebElements rangeList;

		public ManageNamedRangesDialog(EnhancedWebDriver driver) {
			super(driver);
		}

		/**
		 * Get range name of the [index] item in ManageNamedRanges dialog
		 * 
		 * @param index
		 *            started from 1
		 * @return name
		 * 
		 */
		public String getNamedRangeName(int index) {
			sleep(2);
			log.info("Get range name in ManageNamedRanges dialog for item "
					+ index);
			// if (!TitleBar.isDisplayed()) {
			// throw new FailedException(
			// "Could not find ManageNamedRanges dialog");
			// }
			// List<WebElement> rangelist = driver.findElementById(
			// "S_d_allNameRangesList").findElements(By.tagName("tr"));
			sleep(2);
			// String
			// name=rangelist.get(index-1).findElements(By.tagName("td")).get(0).getText();
			// //for R1.0.2
			// String
			// name=rangelist.get(index-1).findElements(By.tagName("div")).get(0).getText();
			// //for R1.0.3
			By name1 = ifElement(By.tagName("td"), By.tagName("div"));
			String name = rangeList.get(index - 1).findElements(name1).get(0)
					.getText();
			return name;
		}

		/**
		 * Get range refersto of the required rangename in ManageNamedRanges
		 * 
		 * @param rangename
		 * @return
		 */
		public String getNamedRangeRefersto(String rangename) {
			sleep(2);
			String name = null;
			for (int i = 0; i < rangeList.size(); i++) {
				name = rangeList.get(i).findElements(By.tagName("td")).get(0)
						.getText();
				if (name.contains(rangename)) {
					return rangeList.get(i).findElements(By.tagName("td"))
							.get(1).getText();
				}
			}
			return null;
		}

		/**
		 * Get range refersto of the [index] item in ManageNamedRanges dialog
		 * 
		 * @param index
		 * @return
		 * 
		 */
		public String getNamedRangeRefersto(int index) {
			// List<WebElement> rangelist = driver.findElementById(
			// "S_d_allNameRangesList").findElements(By.tagName("tr"));
			sleep(2);
			String name = rangeList.get(index - 1)
					.findElements(By.tagName("td")).get(1).getText();
			return name;
		}

		public ManageNamedRangesDialog selectRange(int index) {
			// List<WebElement> rangelist = driver.findElementById(
			// "S_d_allNameRangesList").findElements(By.tagName("tr"));
			sleep(2);
			rangeList.get(index - 1).click();
			return this;
		}

		public ManageNamedRangesDialog setRefersto(String range) {
			nameRange.clear();
			nameRange.sendKeys(range);
			nameRange.sendKeys(Keys.TAB);
			sleep(3);
			Apply.click();
			sleep(2);
			return this;
		}

		public ManageNamedRangesDialog closeDlg() {
			sleep(2);
			Close.sendKeys(Keys.ENTER);
			sleep(2);
			return this;
		}

	}

	public class MergeCellsDialog extends BasePage {

		public MergeCellsDialog(EnhancedWebDriver driver) {
			super(driver);
		}

		public MergeCellsDialog submit() {
			sleep(1);
			OK.sendKeys(Keys.RETURN);
			return this;
		}

		public MergeCellsDialog cancel() {
			sleep(1);
			Cancel.sendKeys(Keys.RETURN);
			return this;
		}
	}

	/**
	 * Select sort
	 */

	public class SelectSortingOptionDialog extends BasePage {
		@Find("id=S_d_sortRangeWithHeader")
		public EnhancedWebElement SortWithHeader;
		@Find("id=S_d_sortRangeAscend")
		public EnhancedWebElement Ascending;
		@Find("id=S_d_sortRangeDescend")
		public EnhancedWebElement Descending;
		@Find("id=S_d_sortRangeOKButton")
		public EnhancedWebElement OKButton;
		@Find("id=S_d_sortRangeCancelButton")
		public EnhancedWebElement CancelButton;

		public SelectSortingOptionDialog(EnhancedWebDriver driver) {
			super(driver);
		}

		public SelectSortingOptionDialog rangeContainColumnLabels() {
			SortWithHeader.click();
			return this;
		}

		public SelectSortingOptionDialog sortByColumn(String column) {
			// implement it later when needed.
			return this;
		}

		public SelectSortingOptionDialog sortWith(String function) {
			if (function.equalsIgnoreCase("Ascending")) {
				Ascending.click();
			} else if (function.equalsIgnoreCase("Descending")) {
				Descending.click();
			} else {
				throw new IllegalArgumentException(
						"unrecognized function while sorting:" + function);
			}
			return this;
		}

		public SelectSortingOptionDialog submit() {
			sleep(1);
			if (OKButton != null && OKButton.isDisplayed()) {
				log.info("Click OK button in Sort dialog");
			}
			OKButton.sendKeys(Keys.RETURN);
			return this;
		}

		public SelectSortingOptionDialog cancel() {
			CancelButton.click();
			return this;
		}

	}

	/**
	 * Set a spreadsheet document locale
	 * 
	 * @author fvt
	 * 
	 */
	public class SpreadSheetSetLocaleDialog extends BasePage {

		@Find("id=S_d_spreadsheetSettingsLocaleList")
		public EnhancedWebElement setLocaleList;
		@Find("id=S_d_spreadsheetSettingsOKButton")
		public EnhancedWebElement setLocaleOK;

		public SpreadSheetSetLocaleDialog(EnhancedWebDriver driver) {
			super(driver);
		}

		public SpreadSheetSetLocaleDialog setLocale(String locale) {
			sleep(2);
			setLocaleList.clear();
			setLocaleList.sendKeys(locale);
			setLocaleList.sendKeys(Keys.TAB);
			sleep(3);
			setLocaleOK.click();
			sleep(2);
			return this;
		}
	}

	/**
	 * New Named Range dialog
	 * 
	 * @author fvt
	 * 
	 */
	public class NewNamedRangeDialog extends BasePage {
		@Find("id=C_d_newNameName")
		public EnhancedWebElement Name;
		@Find("id=C_d_newNameInput")
		public EnhancedWebElement Refersto;
		@Find("id=C_d_newNameOKButton")
		public EnhancedWebElement Add;
		@Find("id=C_d_newNameCancelButton")
		public EnhancedWebElement Cancel;

		public NewNamedRangeDialog(EnhancedWebDriver driver) {
			super(driver);
		}

		public NewNamedRangeDialog AddNewNamedRange(String name, String refersto) {
			sleep(3);
			By name1 = ifElement(By.id("S_d_newNameName"),
					By.id("C_d_newNameName"));
			driver.findElement(name1).sendKeys(name);
			// driver.findElementByType(Name);
			if (refersto != null) {
				By refersto1 = ifElement(By.id("S_d_newNameInput"),
						By.id("C_d_newNameInput"));
				driver.findElement(refersto1).clear();
				sleep(1);
				driver.findElement(refersto1).sendKeys(refersto);
				sleep(1);
			}
			driver.findElement(name1).sendKeys(Keys.ENTER);
			sleep(3);
			return this;
		}
	}

	public class SelectFunctionListDialog extends BasePage {
		@Find("id=S_d_allFormulasFormulaList")
		public EnhancedWebElement FormulaList;
		@Find("id=S_d_allFormulasOKButton")
		public EnhancedWebElement OK;
		@Find("id=S_d_allFormulasCancelButton")
		public EnhancedWebElement Cancel;

		public SelectFunctionListDialog(EnhancedWebDriver driver) {
			super(driver);
		}

		public SelectFunctionListDialog selectFormula(String function) {
			Select functionList = new Select(FormulaList);
			functionList.selectByVisibleText(function);

			return this;
		}

		public SelectFunctionListDialog submit() {
			OK.sendKeys(Keys.RETURN);
			return this;
		}

		public SelectFunctionListDialog cancel() {
			Cancel.sendKeys(Keys.RETURN);
			return this;
		}

	}

	/*****************************************************************
	 * 
	 * Menu Definition
	 * 
	 *****************************************************************/
	public class SpreadSheetMenu extends BasePage {

		@Find("css=#S_i_File")
		public EnhancedWebElement File;
		@Find("css=#S_i_Edit")
		public EnhancedWebElement Edit;
		@Find("css=#S_i_View")
		public EnhancedWebElement View;
		@Find("css=#S_i_Insert")
		public EnhancedWebElement Insert;
		@Find("css=#S_i_Format")
		public EnhancedWebElement Format;
		@Find("css=#S_i_Data")
		public EnhancedWebElement Data;
		@Find("css=#S_i_Tools")
		public EnhancedWebElement Tools;
		@Find("css=#S_i_Help")
		public EnhancedWebElement Help;
		@Find("css=#S_i_Overview")
		public EnhancedWebElement HelpContents;
		@Find("css=#S_i_About")
		public EnhancedWebElement About;

		@Find("css=#S_i_New")
		public EnhancedWebElement New;
		@Find("css=#S_i_NewDocument")
		public EnhancedWebElement NewDocument;
		@Find("css=#S_i_NewSpreadsheetTpl")
		public EnhancedWebElement NewSpreadsheetTpl;
		@Find("css=#S_i_NewSpreadsheetTplFile")
		public EnhancedWebElement NewSpreadsheetTplFile;
		@Find("css=#S_i_Discard")
		public EnhancedWebElement ReverttoLastVersion;
		@Find("css=#S_i_Settings")
		public EnhancedWebElement Setting;
		@Find("css=#S_i_Save")
		public EnhancedWebElement Save;
		@Find("css=#S_i_SaveAs")
		public EnhancedWebElement SaveAs;
		@Find("css=#S_i_Publish")
		public EnhancedWebElement Publish;
		@Find("css=#S_i_Share")
		public EnhancedWebElement Share;

		@Find("css=#S_i_Undo")
		public EnhancedWebElement Undo;
		@Find("css=#S_i_Redo")
		public EnhancedWebElement Redo;
		@Find("css=#S_i_Cut")
		public EnhancedWebElement Cut;
		@Find("css=#S_i_Copy")
		public EnhancedWebElement Copy;
		@Find("css=#S_i_Paste")
		public EnhancedWebElement Paste;
		@Find("css=#S_i_DeleteRow")
		public EnhancedWebElement DeleteRow;
		@Find("css=#S_i_DeleteColumn")
		public EnhancedWebElement DeleteColumn;
		@Find("css=#S_i_DeleteSheet")
		public EnhancedWebElement DeleteSheet;
		@Find("css=#S_i_DeleteContent")
		public EnhancedWebElement DeleteContent;
		@Find("css=#S_i_RenameSheet")
		public EnhancedWebElement RenameSheet;
		@Find("css=#S_i_MoveSheet")
		public EnhancedWebElement MoveSheet;
		@Find("css=#S_i_FindReplace")
		public EnhancedWebElement FindReplace;

		@Find("css=#S_i_Toolbar")
		public EnhancedWebElement Toolbar;
		@Find("css=#S_i_Sidebar")
		public EnhancedWebElement Sidebar;
		@Find("css=#S_i_FormulaBar")
		public EnhancedWebElement FormulaBar;
		@Find("css=#S_i_CoeditingHighlights")
		public EnhancedWebElement CoeditingHighlights;
		@Find("css=#S_i_MoveRightOnEnter")
		public EnhancedWebElement MoveRightOnEnter;
		@Find("css=#S_i_ShowUnsupportWarning")
		public EnhancedWebElement ShowUnsupportWarning;

		@Find("css=#S_i_InsertRowAbove")
		public EnhancedWebElement RowAbove;
		@Find("css=#S_i_InsertColumnBefore")
		public EnhancedWebElement ColumnBefore;
		@Find("css=#S_i_InsertChart")
		public EnhancedWebElement Chart;
		@Find("css=#S_i_InsertSheet")
		public EnhancedWebElement Sheet;
		@Find("css=#S_i_Image")
		public EnhancedWebElement Image;
		@Find("css=#S_i_Function")
		public EnhancedWebElement Function;
		@Find("css=#S_i_FunctionSum")
		public EnhancedWebElement FunctionSum;
		@Find("css=#S_i_FunctionAverage")
		public EnhancedWebElement FunctionAverage;
		@Find("css=#S_i_FunctionMore")
		public EnhancedWebElement FunctionMore;
		@Find("css=#S_i_FunctionFlag")
		public EnhancedWebElement FunctionFlag;
		@Find("css=#S_i_Named")
		public EnhancedWebElement NamedRange;
		@Find("css=#S_i_NamedRangeNew")
		public EnhancedWebElement NewRange;
		@Find("css=#S_i_NamedRangeManage")
		public EnhancedWebElement ManageRanges;

		@Find("css=#S_i_DefaultFormatting")
		public EnhancedWebElement DefaultFormatting;
		@Find("css=#S_i_FontName")
		public EnhancedWebElement Font;
		@Find("css=#S_i_ImageProperties")
		public EnhancedWebElement ImageProperties;
		@Find("css=#S_i_TextProperties")
		public EnhancedWebElement TextProperties;
		@Find("css=#S_i_Bold")
		public EnhancedWebElement Bold;
		@Find("css=#S_i_Italic")
		public EnhancedWebElement Italic;
		@Find("css=#S_i_Underline")
		public EnhancedWebElement Underline;
		@Find("css=#S_i_Strikethrough")
		public EnhancedWebElement Strikethrough;
		@Find("css=#S_i_AlignLeft")
		public EnhancedWebElement AlignLeft;
		@Find("css=#S_i_AlignCenter")
		public EnhancedWebElement AlignCenter;
		@Find("css=#S_i_AlignRight")
		public EnhancedWebElement AlignRight;
		@Find("css=#S_i_Align")
		public EnhancedWebElement Align;
		@Find("css=#S_i_Wrap")
		public EnhancedWebElement Wrap;
		@Find("css=#S_i_ShowRow")
		public EnhancedWebElement ShowRow;
		@Find("css=#S_i_HideRow")
		public EnhancedWebElement HideRow;
		@Find("css=#S_i_ShowColumn")
		public EnhancedWebElement ShowColumn;
		@Find("css=#S_i_HideColumn")
		public EnhancedWebElement HideColumn;

		@Find("css=#S_i_Sort")
		public EnhancedWebElement Sort;
		@Find("css=#S_i_InstantFilter")
		public EnhancedWebElement InstantFilter;

		@Find("css=#S_i_Team")
		public EnhancedWebElement Team;
		@Find("css=#S_i_Assignment")
		public EnhancedWebElement Assignment;
		@Find("css=#S_i_AssignCells")
		public EnhancedWebElement AssignCells;
		@Find("css=#S_i_AddComment")
		public EnhancedWebElement AddComment;
		@Find("css=#S_i_Task")
		public EnhancedWebElement Task;
		@Find("css=#S_i_AssignATask")
		public EnhancedWebElement AssignATask;
		@Find("css=#S_i_EditAssignment")
		public EnhancedWebElement EditAssignment;
		@Find("css=#S_i_ReAssign")
		public EnhancedWebElement ReAssign;
		@Find("css=#S_i_MarkAssignComplete")
		public EnhancedWebElement MarkAssignComplete;
		@Find("css=#S_i_RemoveCompletedAssign")
		public EnhancedWebElement RemoveCompletedAssign;
		@Find("css=#S_i_AboutAssign")
		public EnhancedWebElement AboutAssign;

		@Find("css=#S_i_SpellCheck")
		public EnhancedWebElement RunManualSpellCheck;

		@Find("id=C_d_PublishDialog")
		public EnhancedWebElement PublishDialog;
		@Find("id=C_d_PublishDialogVersionTextArea")
		public EnhancedWebElement PublishDialogTextArea;
		@Find("id=C_d_PublishDialogOKButton")
		public EnhancedWebElement PublishDialogOKButton;

		public SpreadSheetMenu(EnhancedWebDriver driver) {
			super(driver);
		}

		// common menu
		public SpreadSheetMenu save() {
			return fireMenu(File, Save);
		}

		public SpreadSheetMenu saveAsDraft() {
			fireMenu(File, Save);
			return this;
		}

		public SpreadSheetMenu saveAsNewVersion() {
			fireMenu(File, Publish);
			return this;
		}

		public SpreadSheetMenu saveAsNewVersion(String description) {
			fireMenu(File, Publish);
			sleep(1);
			PublishDialogTextArea.sendKeys(description);
			sleep(1);
			PublishDialogOKButton.click();
			sleep(1);
			return this;
		}

		public SpreadSheetMenu saveAsPriavteDraft() {
			fireMenu(File, Save);
			return this;
		}

		/**
		 * Open a dialog to save as a new document.
		 * 
		 * @return
		 */
		public SpreadSheetMenu saveAs() {
			return fireMenu(File, SaveAs);
		}

		public SpreadSheetMenu shareWith() {
			return fireMenu(File, Share);
		}

		public SpreadSheetMenu menuUndo() {
			return fireMenu(Edit, Undo);
		}

		public SpreadSheetMenu menuRedo() {
			return fireMenu(Edit, Redo);
		}

		public SpreadSheetMenu cut() {
			return fireMenu(Edit, Cut);
		}

		public SpreadSheetMenu copy() {
			return fireMenu(Edit, Copy);
		}

		public SpreadSheetMenu paste() {
			return fireMenu(Edit, Paste);
		}

		public SpreadSheetMenu about() {
			return fireMenu(Help, About);
		}

		/**
		 * Show or hide menu item in View menu.
		 * 
		 * @param viewItem
		 * @param showOrHide
		 *            rue means show, false means hide.
		 * 
		 */
		public SpreadSheetMenu showOrHide(VIEWITEM viewItem, boolean showOrHide) {
			EnhancedWebElement targetView = null;

			switch (viewItem) {
			case Toolbar:
				targetView = Toolbar;
				break;
			case Sidebar:
				targetView = Sidebar;
				break;
			case Assignment:
				targetView = Assignment;
				break;
			case Coediting:
				targetView = CoeditingHighlights;
				break;
			default:
				log.error("Unprocessed view option:" + viewItem);
				throw new IllegalArgumentException("Unprocessed view option:"
						+ viewItem);
			}

			fireMenu(View);
			boolean contains = targetView.getAttribute("class").contains(
					"dijitCheckedMenuItemChecked");
			if (contains != showOrHide) {

				fireMenu(targetView);
			} else {
				fireMenu(View);
			}
			return this;
		}

		// common menu end

		public SpreadSheetMenu setMoveRightOnEnter(boolean right) {
			View.click();
			sleep(1);
			String classes = MoveRightOnEnter.getAttribute("class");
			// if set move right, and the move right on enter had been checked,
			// do nothing
			if (right) {
				if (classes.contains("dijitCheckedMenuItemChecked")) {
					return this;
				}
			}
			// if set move below, and the move right on enter had not been
			// checked, do nothing
			if (!right) {
				if (!classes.contains("dijitCheckedMenuItemChecked")) {
					return this;
				}
			}
			MoveRightOnEnter.sendKeys(Keys.RETURN);
			sleep(1);
			return this;
		}

		public SpreadSheetMenu showUnsupportedMessage(boolean right) {
			View.click();
			sleep(1);
			String classes = ShowUnsupportWarning.getAttribute("class");
			if (right) {
				if (classes.contains("dijitCheckedMenuItemChecked")) {
					return this;
				}
			}
			if (!right) {
				if (!classes.contains("dijitCheckedMenuItemChecked")) {
					return this;
				}
			}
			ShowUnsupportWarning.sendKeys(Keys.RETURN);
			sleep(1);
			return this;
		}

		public SpreadSheetMenu newSpreadSheetTpl() {
			return fireMenu(File, New, NewSpreadsheetTpl, NewSpreadsheetTplFile);
		}

		public SpreadSheetMenu newNamedRange() {
			return fireMenu(Insert, NamedRange, NewRange);
		}

		public SpreadSheetMenu ManageNamedRanges() {
			return fireMenu(Insert, NamedRange, ManageRanges);
		}

		public SpreadSheetMenu setLocale() {
			return fireMenu(File, Setting);
		}

		public SpreadSheetMenu viewSidebar(boolean opened) {
			boolean checked = true;
			View.sendKeys(Keys.RETURN);
			sleep(1);
			String classes = Sidebar.getAttribute("class");
			sleep(1);
			if (!classes.contains("dijitCheckedMenuItemChecked")) {
				checked = false;
			}
			if (checked != opened)
				Sidebar.sendKeys(Keys.RETURN);

			sleep(1);
			return this;
		}

		public SpreadSheetMenu viewToolbar(boolean opened) {
			boolean checked = true;
			View.sendKeys(Keys.RETURN);
			sleep(1);
			String classes = Toolbar.getAttribute("class");
			sleep(1);
			if (!classes.contains("dijitCheckedMenuItemChecked")) {
				checked = false;
			}
			if (checked != opened)
				Toolbar.sendKeys(Keys.RETURN);
			else
				Toolbar.sendKeys(Keys.ESCAPE);
			sleep(1);
			return this;
		}

		public SpreadSheetMenu viewFormulaBar(boolean opened) {
			boolean checked = true;
			View.sendKeys(Keys.RETURN);
			String classes = FormulaBar.getAttribute("class");
			sleep(1);
			if (!classes.contains("dijitCheckedMenuItemChecked")) {
				checked = false;
			}
			if (checked != opened)
				FormulaBar.sendKeys(Keys.RETURN);
			else
				FormulaBar.sendKeys(Keys.ESCAPE);
			sleep(1);
			return this;
		}

		public SpreadSheetMenu setDefaultFormat() {
			Format.click();
			sleep(1);
			DefaultFormatting.sendKeys(Keys.RETURN);
			sleep(1);
			return this;
		}

		public SpreadSheetMenu findAndReplace(String target, boolean replace,
				boolean replaceAll, String replacement, boolean matchCase,
				boolean entireCell, boolean allSheets, boolean isContinue) {
			// if (!findAndReplaceDialog.isOpened()) {
			fireMenu(Edit, FindReplace);
			sleep(1);
			// }

			findAndReplaceDialog.inputFindTxt(target);
			if (replace || replaceAll)
				findAndReplaceDialog.inputReplaceTxt(replacement);
			findAndReplaceDialog.matchCase(matchCase);
			findAndReplaceDialog.matchCell(entireCell);
			findAndReplaceDialog.searchAll(allSheets);
			if (replace) {
				findAndReplaceDialog.replace();
			} else if (replaceAll) {
				findAndReplaceDialog.replaceAll();
			} else {
				findAndReplaceDialog.find();
			}
			if (!isContinue) {
				findAndReplaceDialog.quit();
			}
			return this;
		}

		public SpreadSheetMenu wrapText() {
			return fireMenu(Format, Wrap);
		}

		public SpreadSheetMenu showRow() {
			return fireMenu(Format, ShowRow);
		}

		public SpreadSheetMenu hideRow() {
			return fireMenu(Format, HideRow);
		}

		public SpreadSheetMenu hideColumn() {
			return fireMenu(Format, HideColumn);
		}

		public SpreadSheetMenu showColumn() {
			return fireMenu(Format, ShowColumn);
		}

		public SpreadSheetMenu newRange() {
			return fireMenu(Insert, NamedRange, NewRange);
		}

		public SpreadSheetMenu manageRanges() {
			return fireMenu(Insert, NamedRange, ManageRanges);
		}

		public SpreadSheetMenu setFontStyleToBold() {
			return fireMenu(Format, TextProperties, Bold);
		}

		public SpreadSheetMenu setFontStyleToItalic() {
			return fireMenu(Format, TextProperties, Italic);
		}

		public SpreadSheetMenu setFontStyleToUnderline() {
			return fireMenu(Format, TextProperties, Underline);
		}

		public SpreadSheetMenu setFontStyleToStrikeThrough() {
			return fireMenu(Format, TextProperties, Strikethrough);
		}

		public SpreadSheetMenu newDocument() {
			return fireMenu(File, New, NewDocument);
		}

		public SpreadSheetMenu deleteRow() {
			return fireMenu(Edit, DeleteRow);
		}

		public SpreadSheetMenu deleteColumn() {
			return fireMenu(Edit, DeleteColumn);
		}

		public SpreadSheetMenu deleteSheet() {
			return fireMenu(Edit, DeleteSheet);
		}

		public SpreadSheetMenu deleteContent() {
			return fireMenu(Edit, DeleteContent);
		}

		public SpreadSheetMenu renameSheet() {
			return fireMenu(Edit, RenameSheet);
		}

		public SpreadSheetMenu moveSheet() {
			return fireMenu(Edit, MoveSheet);
		}

		public SpreadSheetMenu insertImage() {
			return fireMenu(Insert, Image);
		}

		public SpreadSheetMenu createChart() {
			Insert.click();
			Chart.sendKeys(Keys.RETURN);
			return this;
		}

		public SpreadSheetMenu insertRowAbove() {
			return fireMenu(Insert, RowAbove);
		}

		public SpreadSheetMenu insertColumnBefore() {
			return fireMenu(Insert, ColumnBefore);
		}

		public SpreadSheetMenu insertSheet() {
			return fireMenu(Insert, Sheet);
		}

		public SpreadSheetMenu createFunction(FunctionEnum func) {
			fireMenu(Insert, Function);
			switch (func) {
			case SUM:
				FunctionSum.click();
				break;
			case AVERAGE:
				FunctionAverage.click();
				break;
			default:
				log.error("unexisted function on menu:" + func);
			}

			return this;
		}

		public SpreadSheetMenu createFunctionMore() {
			fireMenu(Insert, Function, FunctionMore);
			return this;
		}

		public SpreadSheetMenu sort(boolean includeColumnHeader,
				String byColumn, String bySequence) {
			fireMenu(Data, Sort);
			if (includeColumnHeader) {
				selectSortingOptionDialog.rangeContainColumnLabels();
				sleep(1);
			}
			selectSortingOptionDialog.sortWith(bySequence).submit();
			return this;
		}

		public SpreadSheetMenu instantFilter() {
			return fireMenu(Data, InstantFilter);
		}

		public SpreadSheetMenu assignATask() {
			return fireMenu(Team, AssignCells);
		}

		public SpreadSheetMenu markAssignComplete() {
			Team.click();
			sleep(1);
			WebElement item = MarkAssignComplete;
			if (!item.isEnabled()) {
				log.info("Cell or cell range has not been assigned");
			} else {
				item.sendKeys(Keys.RETURN);
			}
			return this;
		}

		public SpreadSheetMenu removeCompletedAssign() {
			return fireMenu(Team, RemoveCompletedAssign);
		}

		public SpreadSheetMenu addComment() {
			return fireMenu(Team, AddComment);
		}

		public SpreadSheetMenu fireMenu(EnhancedWebElement... items) {
			switch (items.length) {
			case 1:
				sleep(1);
				items[0].sendKeys(Keys.RETURN);
				break;
			case 2:
				sleep(1);
				items[0].sendKeys(Keys.RETURN);
				sleep(1);
				items[1].sendKeys(Keys.RETURN);
				break;
			case 3:
				sleep(1);
				items[0].click();
				sleep(1);
				items[1].sendKeys(Keys.RETURN);
				sleep(1);
				items[2].sendKeys(Keys.RETURN);
				break;
			case 4:
				sleep(1);
				items[0].click();
				sleep(1);
				items[1].sendKeys(Keys.RETURN);
				sleep(1);
				items[2].sendKeys(Keys.RETURN);
				sleep(1);
				items[3].sendKeys(Keys.RETURN);
				break;
			default:
				log.error("Unprocessed menu items:" + items.length);
			}
			sleep(2);
			return this;
		}

		public SpreadSheetMenu RunManualSpellCheck() {
			return fireMenu(Tools, RunManualSpellCheck);
		}
	}

	/*****************************************************************
	 * 
	 * Constant
	 * 
	 *****************************************************************/

/**
	 * EQ means '=', NE means '!=', GT means '>', LT means '<', GE means '>=', LE means '<=', CT means contains, NC means Not Contains.
	 */
	public enum CompareOption {
		EQ, NE, GT, LT, GE, LE, CT, NC
	}

	public enum DATAFORMAT {
		CURRENCY, PERCENT
	}

	public enum FunctionEnum {
		ABS, SUM, AVERAGE;
	}

	public enum RowOperation {
		ABOVE, BELOW, DELETE
	}

	public enum ColOperation {
		BEFORE, AFTER, DELETE
	}

	public enum FONTSTYLE // public constant
	{
		BOLD, ITALIC, UNDERLINE, STRIKETHROUGH
	}

	public enum ALIGN // public constant
	{
		LEFT, CENTER, RIGHT
	}

	public enum VIEWITEM {
		Toolbar, Sidebar, Assignment, Coediting, AutoSpellCheck;
	}

	static {
		String[] red = { "rgb(255, 0, 0)", "#ff0000", "#f00" };
		String[] blue = { "rgb(0, 0, 255)", "#0000ff" };
		String[] black = { "rgb(0, 0, 0)", "#000000", "#000" };
		String[] white = { "rgb(255, 255, 255)", "#ffffff", "#fff" };
		String[] yellow = { "rgb(255, 255, 0)", "#ffff00" };
		String[] yellow1 = { "#ffff99", "rgb(255, 255, 153)" };
		String[] green = { "rgb(0, 128, 0)", "#008000" };
		String[] grey = { "rgb(128, 128, 128)", "#808080" };
		String[] navy = { "rgb(0, 0, 128)", "#000080" };

		colorMapSpreadsheet.put(COLOR.Blue, blue);
		colorMapSpreadsheet.put(COLOR.Red, red);
		colorMapSpreadsheet.put(COLOR.Black, black);
		colorMapSpreadsheet.put(COLOR.White, white);
		colorMapSpreadsheet.put(COLOR.Yellow, yellow);
		colorMapSpreadsheet.put(COLOR.Yellow1, yellow1);
		colorMapSpreadsheet.put(COLOR.Green, green);
		colorMapSpreadsheet.put(COLOR.Gray, grey);
		colorMapSpreadsheet.put(COLOR.Navy, navy);
	}

	public enum COLOR {
		Black, Blue, Red, Yellow, Yellow1, White, Gray, Navy, Dark_Gray, Green, Indicator1, Indicator2, Indicator3, Indicator4, Indicator5;

		public static String[] getColorValues(String key) {
			for (COLOR c : COLOR.values()) {
				if (c.toString().equalsIgnoreCase(key))
					return colorMapSpreadsheet.get(c);
			}
			return null;
		}
	}

	public SpreadsheetFilePage createTask(String title, String assignto) {
		// TODO Auto-generated method stub
		log.info("Create task for " + assignto);
		menuTeam.click();
		driver.sleep(1);
		if (driver.isChrome())
			menuAssignCells.click();
		else
			menuAssignCells.sendKeys(Keys.ENTER);
		driver.sleep(1);
		setTask(title, assignto, true);
		verifyTaskCreated(title, assignto);
		return this;
	}

	public SpreadsheetFilePage verifyTaskCreated(String title, String assignto) {
		boolean taskExisted = false;
		driver.sleep(2);
		if (sectionBorders.size() == 0) {
			assertTrue("No task is created", false);
		}
		for (String gtitle : sectionTitles.getText()) {
			if (gtitle.contains(assignto) && gtitle.contains(title)) {
				taskExisted = true;
				break;
			}
		}
		assertTrue(String.format("Task %s is created successfully", title),
				taskExisted);
		driver.sleep(1);
		return this;
	}

	public SpreadsheetFilePage completeAssignedTask() {
		// TODO Auto-generated method stub
		completeAssignedTask(0);
		return this;
	}

	public SpreadsheetFilePage completeAssignedTask(int i) {
		// TODO Auto-generated method stub
		log.info("Try to complete a task.");
		menuTeam.click();
		menuMarkAssignmentComplete.sendKeys(Keys.RETURN);
		driver.sleep(5);
		boolean taskComplete = false;
		boolean yes = sectionCompletes.get(i).isDisplayed();
		// sectionComplete.setLocatorArgument(0);
		if (yes)
			taskComplete = true;
		assertTrue(
				String.format("Assigned task %s is completed successfully", i),
				taskComplete);
		return this;
	}

}
