package com.ibm.docs.test.gui.pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;

import com.ibm.docs.test.gui.support.EnhancedWebDriver;
import com.ibm.docs.test.gui.support.EnhancedWebElement;
import com.ibm.docs.test.gui.support.EnhancedWebElements;
import com.ibm.docs.test.gui.support.Find;
import com.ibm.icu.util.Currency;
import com.ibm.icu.util.ULocale;

public class SpreadsheetEditorPage extends DocsEditorPage {
	private static final Logger log = Logger
			.getLogger(SpreadsheetEditorPage.class);
	/* Toolbar */
	@Find("css=#S_t_BgColor")
	public EnhancedWebElement toolBarBackgroundColor;
	@Find("css=#S_t_WrapText")
	public EnhancedWebElement toolBarWrap;
	@Find("css=#S_t_Currency")
	public EnhancedWebElement toolBarCurrency;
	@Find("css=#S_t_Currency_label")
	public EnhancedWebElement toolBarCurrencyLabel;
	@Find("css=#S_t_InstantFilter")
	public EnhancedWebElement toolBarInstantFilter;
	@Find("css=.dijitReset.dijitInline.dijitIcon.websheetToolbarIcon.numberFormatIcon")
	public EnhancedWebElement toolBarNumberIcon;
	@Find("css=#S_i_CurrencyCurrencyFormat1DropDown_text")
	public EnhancedWebElement toolBarCurrencyFormat1;
	@Find("css=#S_i_CurrencyCurrencyFormat2DropDown_text")
	public EnhancedWebElement toolBarCurrencyFormat2;
	@Find("css=#S_i_DateDateFormat1DropDown_text")
	public EnhancedWebElement toolBarDateTimeformat1;
	@Find("css=#S_i_DateDateFormat2DropDown_text")
	public EnhancedWebElement toolBarDateTimeformat2;
	@Find("css=#S_i_DateTimeFormat1DropDown_text")
	public EnhancedWebElement toolBarDateTimeformat3;
	@Find("css=#S_i_TimeTimeFormat2DropDown_text")
	public EnhancedWebElement toolBarDateTimeformat4;
	@Find("css=#S_i_TimeTimeFormat4DropDown_text")
	public EnhancedWebElement toolBarDateTimeformat5;
	@Find("css=#S_i_MoreTimeDateFormatesDropDown_text")
	public EnhancedWebElement toolBarMoreTimeDateDropDown;
	@Find("css=#S_i_MoreDateTimeDateFullDropDown_text")
	public EnhancedWebElement subToolBarDateTimeformat1;
	@Find("css=#S_i_MoreDateTimeDateyMMMDropDown_text")
	public EnhancedWebElement subToolBarDateTimeformat2;
	@Find("css=#S_i_MoreDateTimeDateMMMdDropDown_text")
	public EnhancedWebElement subToolBarDateTimeformat3;
	@Find("css=#S_i_MoreDateTimeTimeMediumDropDown_text")
	public EnhancedWebElement subToolBarDateTimeformat4;
	@Find("css=#S_i_MoreDateTimeTimeHmsDropDown_text")
	public EnhancedWebElement subToolBarDateTimeformat5;
	@Find("css=#S_i_MoreDateTimeTimeLongDropDown_text")
	public EnhancedWebElement subToolBarDateTimeformat6;
	@Find("css=#S_i_NumberNumberFormat2DropDown_text")
	public EnhancedWebElement toolBarNumberFormat0;
	@Find("css=#S_i_NumberNumberFormat3DropDown_text")
	public EnhancedWebElement toolBarNumberFormat2;
	@Find("css=#S_i_PercentPercentFormat1DropDown_text")
	public EnhancedWebElement toolBarPercentFormat0;
	@Find("css=#S_i_PercentPercentFormat2DropDown_text")
	public EnhancedWebElement toolBarPercentFormat2;
	@Find("css=#S_i_MoreCurrenciesDropDown_text")
	public EnhancedWebElement toolBarmoreCurrencies;
	@Find("css=div#S_m_MoreCurrenciesDropDown.dijitMenu table.dijit tbody.dijitReset td.dijitReset.dijitMenuItemLabel")
	public EnhancedWebElements moreCurrenciesDDSymbols;
	@Find("css=#S_i_MoreCurrencies$0DropDown_text")
	public EnhancedWebElement moreCurrenciesDDSymbol;

	/* color panel */
	@Find("xpath=//div[@id='S_m_BackgroundColor']//*[contains(@style, '$0')]")
	public EnhancedWebElement backgroundColor;

	/* File menu */
	@Find("css=#S_i_File")
	public EnhancedWebElement menuFile;
	@Find("css=#S_i_Settings_text")
	public EnhancedWebElement menuSetLocale;

	/* Edit menu */
	@Find("css=#S_i_Edit")
	public EnhancedWebElement menuEdit;
	@Find("css=#S_i_Undo")
	public EnhancedWebElement menuUndo;
	@Find("css=#S_i_Redo")
	public EnhancedWebElement menuRedo;
	@Find("css=#S_i_Cut")
	public EnhancedWebElement menuCut;
	@Find("css=#S_i_Copy")
	public EnhancedWebElement menuCopy;
	@Find("css=#S_i_Paste")
	public EnhancedWebElement menuPaste;
	@Find("css=#S_i_DeleteSheet")
	public EnhancedWebElement menuDeleteSheet;
	@Find("css=#S_i_RenameSheet")
	public EnhancedWebElement menuRenameSheet;
	@Find("css=#S_i_MoveSheet")
	public EnhancedWebElement menuMoveSheet;
	@Find("css=#S_i_DeleteRow")
	public EnhancedWebElement menuDeleteRow;
	@Find("css=#S_i_DeleteColumn")
	public EnhancedWebElement menuDeleteColumn;
	@Find("css=#S_i_FindReplace")
	public EnhancedWebElement menuFindReplace;

	/* Insert menu */
	@Find("css=#S_i_Insert_text")
	public EnhancedWebElement menuInsert;
	@Find("css=#S_i_InsertRowAbove")
	public EnhancedWebElement menuInsertRowAbove;
	@Find("css=#S_i_InsertColumnBefore")
	public EnhancedWebElement menuInsertColumnBefore;
	@Find("css=#S_i_InsertSheet")
	public EnhancedWebElement menuInsertSheet;
	@Find("css=#S_i_Named")
	public EnhancedWebElement menuNamedRanges;
	@Find("css=#S_i_NamedRangeManage")
	public EnhancedWebElement menuManageRanges;
	@Find("css=#S_i_NamedRangeNew_text")
	public EnhancedWebElement menuNewRange;

	/* Format menu */
	@Find("css=#S_i_Format_text")
	public EnhancedWebElement menuFormat;
	@Find("css=#S_i_Number_text")
	public EnhancedWebElement menuShowNumber;
	@Find("css=#S_i_MoreTimeDateFormates_text")
	public EnhancedWebElement menuShowMoredatetime;
	@Find("css=#S_i_NumberNumberFormat2_text")
	public EnhancedWebElement menuNumberFormat0;
	@Find("css=#S_i_NumberNumberFormat3_text")
	public EnhancedWebElement menuNumberFormat2;
	@Find("css=#S_i_PercentPercentFormat1_text")
	public EnhancedWebElement menuPercentFormat0;
	@Find("css=#S_i_PercentPercentFormat2_text")
	public EnhancedWebElement menuPercentFormat2;
	
	@Find("css=#S_i_DateDateFormat1_text")
	public EnhancedWebElement menuDateTimeformat1;
	@Find("css=#S_i_DateDateFormat2_text")
	public EnhancedWebElement menuDateTimeformat2;
	@Find("css=#S_i_DateTimeFormat1_text")
	public EnhancedWebElement menuDateTimeformat3;
	@Find("css=#S_i_TimeTimeFormat2_text")
	public EnhancedWebElement menuDateTimeformat4;
	@Find("css=#S_i_TimeTimeFormat4_text")
	public EnhancedWebElement menuDateTimeformat5;
	@Find("css=#S_i_MoreDateTimeDateFull_text")
	public EnhancedWebElement subMenuDateTimeformat1;
	@Find("css=#S_i_MoreDateTimeDateyMMM_text")
	public EnhancedWebElement subMenuDateTimeformat2;
	@Find("css=#S_i_MoreDateTimeDateMMMd_text")
	public EnhancedWebElement subMenuDateTimeformat3;
	@Find("css=#S_i_MoreDateTimeTimeMedium_text")
	public EnhancedWebElement subMenuDateTimeformat4;
	@Find("css=#S_i_MoreDateTimeTimeHms_text")
	public EnhancedWebElement subMenuDateTimeformat5;
	
	
	
	
	@Find("css=#S_i_HideRow_text")
	public EnhancedWebElement menuHideRow;
	@Find("css=#S_i_ShowRow_text")
	public EnhancedWebElement menuShowRow;
	@Find("css=#S_i_HideColumn_text")
	public EnhancedWebElement menuHideColumn;
	@Find("css=#S_i_ShowColumn_text")
	public EnhancedWebElement menuShowColumn;

	/* Data menu */
	@Find("css=#S_i_Data_text")
	public EnhancedWebElement menuData;
	@Find("css=#S_i_Sort_text")
	public EnhancedWebElement menuSort;
	@Find("css=#S_i_InstantFilter_text")
	public EnhancedWebElement menuInstantFilter;

	/* Context menu */
	@Find("css=#S_CM_ShowRow")
	public EnhancedWebElement contextMenuShowRow;
	@Find("css=#S_CM_HideRow")
	public EnhancedWebElement contextMenuHideRow;
	@Find("css=#S_CM_ShowColumn")
	public EnhancedWebElement contextMenuShowColumn;
	@Find("css=#S_CM_HideColumn")
	public EnhancedWebElement contextMenuHideColumn;

	/* Formula bar */
	@Find("css=#formulaBar_node input.namebox:not([disable])")
	public EnhancedWebElement nameBox;
	@Find("css=#formulaInputLind_label")
	public EnhancedWebElement allFormulas;
	@Find("css=#formulaInputLine")
	public EnhancedWebElement formulaInputLine;

	/* Sheet */
	@Find("css=#sheet_tab_container>.dijitVisible>.dojoxGridMasterView>.dojoxGridView:nth-child(2) .gridBottomContainer .gridContentSubview:nth-child(2) table.dojoxGridRowTable")
	public EnhancedWebElement currentSheet;
	@Find("css=#sheet_node .dijitTabContainerBottom-tabs>.dijitTabChecked")
	public EnhancedWebElement currentSheetTab;
	@Find("css=#sheet_node .dijitTabContainerBottom-tabs>.dijitTab")
	public EnhancedWebElements sheetTabs;
	@Find("css=#sheet_node .dijitTabContainerBottom-tabs>.dijitTab:nth-child($0)")
	public EnhancedWebElement sheetTabByIndex;
	// @Find("css=#sheet_tab_container>.dijitVisible>.dojoxGridMasterView>.dojoxGridView:nth-child(2) textarea")
	@Find("css=#websheet_widget_InlineEditor_0")
	public EnhancedWebElement cellEditor;
	@Find("{script=return pe.base.getCurrentSheet().getContentView().getCellNode($0, $1)}-{css=.imgcomment}")
	public EnhancedWebElement cellComment;
	// Based on current design, we have to get the cell by javascript
	@Find("script=return pe.base.getCurrentSheet().getContentView().getCellNode($0, $1)")
	public EnhancedWebElement cell;
	// @Find("css=#sheet_tab_container>.dijitVisible>.dojoxGridMasterView>.dojoxGridView:nth-child(1) .gridBottomContainer .gridContentSubview:nth-child(2) td")
	@Find("{css=div.header-wrapper}+{css=td.rowIndex}")
	public EnhancedWebElements rowHeaders;
	@Find("css=#sheet_tab_container>.dijitVisible>.dojoxGridMasterHeader>.columnHeaderNodde .columnRightHeader th")
	public EnhancedWebElements columnHeaders;
	@Find("xpath=//div[@id='sheet_tab_container']/div[contains(@class, 'dijitVisible')]/div[@class='dojoxGridMasterHeader']//th[div/text()='$0']")
	public EnhancedWebElement columnHeader;

	@Find("css=div>svg")
	public EnhancedWebElements svgDivs;

	/* Input Box */
	@Find("css=#C_d_InputBoxInputArea")
	public EnhancedWebElement inputBoxInputArea;
	@Find("css=#C_d_InputBoxOKButton")
	public EnhancedWebElement inputBoxOk;
	@Find("css=#C_d_InputBoxCancelButton")
	public EnhancedWebElement inputBoxCancel;

	/* Confirm Box */
	@Find("css=#C_d_ConfirmBoxOKButton")
	public EnhancedWebElement confirmBoxOk;
	@Find("css=#C_d_ConfirmBoxCancelButton")
	public EnhancedWebElement confirmBoxCancel;

	/* Manage Named Range dialog */
	@Find("css=#S_d_nameRangeInputArea")
	public EnhancedWebElement manageNamedRangeDlgRefersTo;
	@Find("css=#S_d_allNameRangesList tbody tr td:first-child div:nth-child($0)")
	public EnhancedWebElement manageNamedRangeDlgNameByIndex;
	@Find("css=#S_d_allNameRangesList tbody tr td:first-child div[aria-label*=$0]")
	public EnhancedWebElement manageNamedRangeDlgName;
	@Find("css=#S_d_allNameRangesList td:nth-child(1) div")
	public EnhancedWebElements manageNamedRangeDlgNames;
	@Find("css=#S_d_allNameRangesList td:nth-child(2)")
	public EnhancedWebElements manageNamedRangeDlgRanges;
	@Find("css=#ModifyButton")
	public EnhancedWebElement manageNamedRangeDlgApply;
	@Find("css=#NewButton")
	public EnhancedWebElement manageNamedRangeDlgNew;
	@Find("css=#S_d_nameRangeOKButton")
	public EnhancedWebElement manageNamedRangeDlgClose;

	@Find("css=#S_d_allNameRangesList td:nth-child(3)")
	public EnhancedWebElement manageNamedRangeDlgDelete;
	@Find("css=#C_d_ConfirmBox #ConcordWarnMsgC_d_ConfirmBox+div")
	public EnhancedWebElement manageNamedRangeDeleteDlgMsg;
	@Find("css=#C_d_ConfirmBox .dijitDialogPaneActionBar span:first-child span")
	public EnhancedWebElement manageNamedRangeDeleteDlgOK;

	/* New Named Range Dialog */
	@Find("css=#S_d_newNameName")
	public EnhancedWebElement newNamedRangeDlgName;
	@Find("css=#S_d_newNameInput")
	public EnhancedWebElement newNamedRangeDlgRefersTo;
	@Find("css=#S_d_newNameOKButton")
	public EnhancedWebElement newNamedRangeDlgOk;
	/* Set Document Locale Dialog */
	@Find("xpath=//input[@value='▼ ']")
	public EnhancedWebElement settingLocale;
	@Find("css=#S_d_spreadsheetSettingsLocaleList_popup22")
	public EnhancedWebElement settingLocaleList;
	@Find("css=#S_d_spreadsheetSettingsOKButton")
	public EnhancedWebElement settingLocaleListOKButton;

	/* Data Sort Dialog */
	@Find("css=#S_d_sortRangeOKButton_label")
	public EnhancedWebElement sortOKButton;
	@Find("css=#S_d_sortRangeAscend")
	public EnhancedWebElement asceRadioButton;
	@Find("css=#S_d_sortRangeDescend")
	public EnhancedWebElement descRadioButton;

	/* Filter Panel */
	@Find("css=#FilterHeader_st0_1")
	public EnhancedWebElement filterArrow;
	@Find("css=#S_d_FilterContextMenuSortAZ")
	public EnhancedWebElement filterASC;
	@Find("css=#S_d_FilterContextMenuSortZA")
	public EnhancedWebElement filterDes;

	/* Find and Replace Panel */
	@Find("css=#S_d_FindAndReplaceDlgFindTxt")
	public EnhancedWebElement findTextField;
	@Find("css=#S_d_FindAndReplaceDlgReplaceTxt")
	public EnhancedWebElement replaceTextField;
	@Find("css=#S_d_FindAndReplaceDlgFind_label")
	public EnhancedWebElement findButton;
	@Find("css=#S_d_FindAndReplaceDlgReplace_label")
	public EnhancedWebElement replaceButton;
	@Find("css=#S_d_FindAndReplaceDlgOKButton_label")
	public EnhancedWebElement findOKButton;

	@Find("script=document.body")
	public EnhancedWebElement test;

	public SpreadsheetEditorPage(EnhancedWebDriver driver) {
		super(driver);
	}

	/**
	 * Select a range of cells via name box
	 * 
	 * @param range
	 *            e.g. A1 A1:C3 Sheet2.C2:D5
	 * @return
	 */
	public SpreadsheetEditorPage selectRange(String range) {
		nameBox.clear();
		nameBox.sendKeys(range + Keys.RETURN);
		return this;
	}

	/**
	 * Click the given cell
	 * 
	 * @param name
	 *            cell name e.g. A1 B3
	 * @return
	 */
	public SpreadsheetEditorPage clickCell(String name) {
		int[] loc = parseLocation(name);
		cell.setLocatorArgument(loc[1], loc[0]);
		cell.click();
		return this;
	}

	/**
	 * Get the text of the given cell
	 * 
	 * @param name
	 *            cell name e.g. A1 B3
	 * @return
	 */
	public String getCellText(String name) {
		int[] loc = parseLocation(name);
		cell.setLocatorArgument(loc[1], loc[0]);
		driver.sleep(1);
		return cell.getText();
	}

	/**
	 * Get the text of the given cells
	 * 
	 * @param names
	 *            array contains the cell names
	 * @return
	 */
	public String[] getCellTexts(String... names) {
		String[] result = new String[names.length];
		for (int i = 0; i < names.length; i++) {
			result[i] = getCellText(names[i]);
		}
		return result;
	}

	/**
	 * Select the comment of the given cell
	 * 
	 * @param name
	 *            cell name
	 * @return
	 */
	public SpreadsheetEditorPage selectCellComment(String name) {
		int[] loc = parseLocation(name);
		cellComment.setLocatorArgument(loc[1], loc[0]);
		cellComment.click(4, 4);
		return this;
	}

	/**
	 * Type keys in the given cells. Note: The method doesn't ensure the cursor
	 * position after entering edit mode
	 * 
	 * @param name
	 *            cell name
	 * @param keys
	 *            keys
	 * @return
	 */
	public SpreadsheetEditorPage typeInCell(String name, String keys) {
		selectRange(name);
		int[] loc = parseLocation(name);
		cell.setLocatorArgument(loc[1], loc[0]);
		cell.doubleClick(15, 10);
		cellEditor.sendKeys(keys);
		return this;
	}

	/**
	 * Get styles of the given cells
	 * 
	 * @param cssName
	 *            css name
	 * @param names
	 *            cell names
	 * @return
	 */
	public String[] getCellStyles(String cssName, String... names) {
		String[] result = new String[names.length];
		for (int i = 0; i < names.length; i++) {
			int[] loc = parseLocation(names[i]);
			cell.setLocatorArgument(loc[1], loc[0]);
			result[i] = cell.getCssValue(cssName);

		}
		return result;
	}

	/**
	 * Get styles of the given cell
	 * 
	 * @param cssName
	 *            css name
	 * @param name
	 *            cell name
	 * @return
	 */
	public String getCellStyle(String cssName, String name) {
		int[] loc = parseLocation(name);
		cell.setLocatorArgument(loc[1], loc[0]);
		return cell.getCssValue(cssName);
	}

	/**
	 * Delete the given cell
	 * 
	 * @param name
	 *            cell name
	 * @return
	 */
	public SpreadsheetEditorPage deleteCell(String name) {
		selectRange(name);
		body.sendKeys(Keys.DELETE);
		return this;
	}

	/**
	 * Delete the selected row
	 * 
	 * @return
	 */
	public SpreadsheetEditorPage deleteRow() {
		menuEdit.click();
		menuDeleteRow.click();
		return this;
	}

	/**
	 * Delete the selected column
	 * 
	 * @return
	 */
	public SpreadsheetEditorPage deleteColumn() {
		menuEdit.click();
		menuDeleteColumn.click();
		return this;
	}

	/**
	 * Insert a row above the current row
	 * 
	 * @return
	 */
	public SpreadsheetEditorPage insertRowAbove() {
		menuInsert.click();
		menuInsertRowAbove.click();
		return this;
	}

	/**
	 * Insert a column before the current column
	 * 
	 * @return
	 */
	public SpreadsheetEditorPage insertColumnBefore() {
		menuInsert.click();
		menuInsertColumnBefore.click();
		return this;
	}

	/**
	 * Select the sheet by index
	 * 
	 * @param sheetNum
	 *            sheet number. starting with 1
	 */
	public SpreadsheetEditorPage selectSheet(int sheetNum) {
		sheetTabByIndex.setLocatorArgument(sheetNum);
		sheetTabByIndex.click();
		nameBox.waitPresence();
		return this;
	}

	/**
	 * Rename the sheet name
	 * 
	 * @param sheetNum
	 *            sheet number. starting with 1
	 * @param newName
	 */
	public SpreadsheetEditorPage renameSheet(int sheetNum, String newName) {
		selectSheet(sheetNum);
		// menuEdit.sendKeys(Keys.ENTER);
		menuEdit.click();
		driver.sleep(1);
		menuRenameSheet.click();
		inputBoxInputArea.clear();
		inputBoxInputArea.sendKeys(newName);
		inputBoxOk.click();
		return this;
	}

	/**
	 * Delete the specified sheet
	 * 
	 * @param sheetNum
	 *            sheet number. starting with 1
	 */
	public SpreadsheetEditorPage deleteSheet(int sheetNum) {
		selectSheet(sheetNum);
		menuEdit.click();
		menuDeleteSheet.click();
		confirmBoxOk.click();
		return this;
	}

	/**
	 * Move the specified sheet
	 * 
	 * @param sheetNum
	 *            sheet number. starting with 1
	 * @param targetNum
	 *            the target sheet number. starting with 1
	 */
	public SpreadsheetEditorPage moveSheet(int sheetNum, int targetNum) {
		selectSheet(sheetNum);
		menuEdit.sendKeys(Keys.ENTER);
		menuMoveSheet.click();
		inputBoxInputArea.clear();
		inputBoxInputArea.sendKeys(String.valueOf(targetNum));
		inputBoxOk.click();
		return this;
	}

	/**
	 * Insert a new sheet
	 * 
	 * @param sheetNum
	 *            sheet number
	 * @param sheetName
	 *            new sheet name
	 */
	public SpreadsheetEditorPage insertSheet(int sheetNum, String sheetName) {
		selectSheet(sheetNum);
		menuInsert.click();
		menuInsertSheet.click();
		inputBoxInputArea.clear();
		inputBoxInputArea.sendKeys(sheetName);
		inputBoxOk.click();
		return this;
	}

	/**
	 * Get name of sheet by index
	 * 
	 * @param sheetNum
	 *            sheet number. starting with 1
	 * @return sheetName
	 */
	public String getSheetName(int sheetNum) {
		sheetTabByIndex.setLocatorArgument(sheetNum);
		driver.sleep(1);
		return sheetTabByIndex.getText();
	}

	/**
	 * Hide the given column
	 * 
	 * @param name
	 *            column name. e.g. A B C D
	 * @return
	 */
	public SpreadsheetEditorPage hideColumn(String name) {
		columnHeader.setLocatorArgument(name);
		columnHeader.click();
		hideColumn();
		return this;
	}

	/**
	 * Hide the selected column
	 * 
	 * @return
	 */
	public SpreadsheetEditorPage hideColumn() {
		menuFormat.click();
		menuHideColumn.click();
		return this;
	}

	/**
	 * Show the given column
	 * 
	 * @param name
	 *            column name. e.g. A B C D
	 * @return
	 */
	public SpreadsheetEditorPage showColumn(String preColumnSymbol,
			String nextColumnSymbol) {
		String range = preColumnSymbol + "1:" + nextColumnSymbol + "1";
		this.selectRange(range);
		this.showColumn();
		return this;
	}

	/**
	 * Hide the selected column
	 * 
	 * @return
	 */
	public SpreadsheetEditorPage showColumn() {
		menuFormat.click();
		menuShowColumn.click();
		return this;
	}

	/**
	 * Hide the selected row
	 * 
	 * @return
	 */
	public SpreadsheetEditorPage hideRow() {
		menuFormat.click();
		menuHideRow.click();
		return this;
	}

	/**
	 * Show the selected row
	 * 
	 * @return
	 */
	public SpreadsheetEditorPage showRow() {
		menuFormat.click();
		menuShowRow.click();
		return this;
	}

	/**
	 * Check if row is hidden
	 * 
	 * @param name
	 * @return
	 */
	public boolean isRowHidden(int index, String name) {
		if (rowHeaders.get(index).isDisplayed()) {
			String s = rowHeaders.get(index - 1).getText();
			if (s.equals(name)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Check if column is hidden
	 * 
	 * @param name
	 * @return
	 */
	public boolean isColumnHidden(String name) {
		columnHeader.setLocatorArgument(name);
		cell.setLocatorArgument(name + "1");
		return (!columnHeader.isPresent() || !columnHeader.isDisplayed())
				&& (!cell.isPresent() || !cell.isDisplayed());
	}

	/**
	 * Open manage named range dialog
	 * 
	 * @return
	 */
	public SpreadsheetEditorPage manageNamedRange() {
		menuInsert.click();
		menuNamedRanges.click();
		menuManageRanges.click();
		return this;
	}

	/**
	 * Create a new named range
	 * 
	 * @return
	 */
	public SpreadsheetEditorPage newNamedRange(String name, String range) {
		menuInsert.click();
		menuNamedRanges.click();
		menuNewRange.click();
		newNamedRangeDlgName.sendKeys(name);
		if (range != null) {
			newNamedRangeDlgRefersTo.clear();
			log.info(String.format("Insert range %s refer to %s", name, range));
			newNamedRangeDlgRefersTo.sendKeys(range);
		}
		newNamedRangeDlgOk.click();
		return this;
	}

	/**
	 * Create a new named range based on the current selection
	 * 
	 * @param name
	 * @return
	 */
	public SpreadsheetEditorPage newNamedRange(String name) {
		return newNamedRange(name, null);
	}

	/**
	 * Set the background for the selected range
	 * 
	 * @param colorName
	 * @return
	 */
	public SpreadsheetEditorPage setCellBackground(String colorName) {
		toolBarBackgroundColor.click();
		backgroundColor.setLocatorArgument(colorName);
		backgroundColor.click();
		return this;
	}

	/**
	 * Set the background of the given cells
	 * 
	 * @param cellRange
	 * @param colorName
	 * @return
	 */
	public SpreadsheetEditorPage setCellBackground(String cellRange,
			String colorName) {
		selectRange(cellRange);
		setCellBackground(colorName);
		return this;
	}

	/**
	 * Toggle wrap of the given cells
	 * 
	 * @param cellRange
	 * @return
	 */
	public SpreadsheetEditorPage toggleWrap(String cellRange) {
		selectRange(cellRange);
		toolBarWrap.click();
		return this;
	}

	/**
	 * Set Currency of the given cells
	 * 
	 * @param cellRange
	 * @return
	 */
	public SpreadsheetEditorPage setCurrency(String cellRange) {
		selectRange(cellRange);
		toolBarCurrency.click();
		return this;
	}

	/**
	 * Get cell value. That means the original text not the calculated result
	 * 
	 * @param name
	 * @return
	 */
	public String getCellValue(String name) {
		selectRange(name);
		return formulaInputLine.getAttribute("value");
	}

	/**
	 * Get cell values. That means the original text not the calculated result
	 * 
	 * @param names
	 * @return
	 */
	public String[] getCellValues(String... names) {
		String[] result = new String[names.length];
		for (int i = 0; i < names.length; i++) {
			result[i] = getCellValue(names[i]);
		}
		return result;
	}

	/**
	 * Transfer column character no to integer e.g. A -> 1 AA -> 27 AMJ -> 1024
	 * 
	 * @param no
	 * @return
	 */
	public static int getIntColumnNo(String no) {
		int len = no.length();
		int ret = 0;
		for (int i = 0; i < len; i++) {
			char c = no.charAt(len - i - 1);
			ret += Math.pow(26, i) * (c - 'A' + 1);
		}

		return ret;
	}

	/**
	 * Transfer int character no to charactor
	 * 
	 * @param no
	 * @return
	 */
	public static String getCharColumnNo(int no) {
		String ret = "";
		int f = 0;
		do {
			f = (no - 1) / 26;
			int s = (no - 1) % 26;
			ret = (char) ('A' + s) + ret;
			no = f;
		} while (f != 0);
		return ret;
	}

	/**
	 * Parse location string into integer values
	 * 
	 * @param loc
	 *            e.g. A1
	 * @return
	 */
	public static int[] parseLocation(String loc) {
		int i = 0;
		for (; i < loc.length(); i++) {
			char c = loc.charAt(i);
			if (c >= '0' && c <= '9')
				break;
		}

		String col = loc.substring(0, i);
		String row = loc.substring(i);
		int sC = getIntColumnNo(col);
		int sR = Integer.parseInt(row) - 1;
		return new int[] { sC, sR };
	}

	/**
	 * Parse range string into integer values
	 * 
	 * @param range
	 *            e.g. A3:F9
	 * @return
	 */
	public static int[] parseRange(String range) {
		int dotIndex = range.indexOf(".");
		if (dotIndex != -1) {
			range = range.substring(dotIndex + 1);
		}

		String[] locs = range.split(":");
		int[] ret = new int[4];
		int[] start = parseLocation(locs[0]);
		ret[0] = start[0];
		ret[1] = start[1];
		if (locs.length == 1) {
			ret[2] = start[0];
			ret[3] = start[1];
		} else {
			int[] end = parseLocation(locs[1]);
			ret[2] = end[0];
			ret[3] = end[1];
		}

		return ret;
	}

	@Override
	public SpreadsheetEditorPage undo() {
		if (driver.isInternetExplorer()) {
			driver.sleep(1);
			this.menuEdit.sendKeys(Keys.RETURN);
			driver.sleep(1);
			this.menuUndo.click();
			driver.sleep(1);
		} else {
			String keys = Keys.chord(Keys.CONTROL, "z");
			driver.getKeyboard().sendKeys(keys);
		}

		driver.sleep(2);
		return this;
	}

	@Override
	public SpreadsheetEditorPage redo() {
		if (driver.isInternetExplorer()) {
			driver.sleep(1);
			this.menuEdit.sendKeys(Keys.RETURN);
			driver.sleep(1);
			this.menuRedo.click();
			driver.sleep(1);
		} else {
			String keys = Keys.chord(Keys.CONTROL, "y");
			driver.getKeyboard().sendKeys(keys);
		}

		driver.sleep(2);
		return this;
	}

	public SpreadsheetEditorPage formatCell2Currency(String range,
			String localeID) {
		String iso = Currency.getInstance(new ULocale(localeID))
				.getCurrencyCode();
		selectRange(range);
		toolBarNumberIcon.click();
		toolBarmoreCurrencies.click();
		moreCurrenciesDDSymbol.setLocatorArgument(iso);
		moreCurrenciesDDSymbol.click();
		return this;
	}

	/**
	 * Set Document Locale
	 * 
	 * @param range
	 * 
	 * @return SpreadsheetEditorPage
	 */
	public SpreadsheetEditorPage SetDocumentLocalebyShiYaChen() {
		menuFile.click();
		menuSetLocale.click();
		String keys = Keys.chord(Keys.ARROW_DOWN);
		driver.getKeyboard().sendKeys(keys);
		settingLocaleList.click();
		settingLocaleListOKButton.click();
		return this;
	}

	/**
	 * Sort Data
	 * 
	 * @param range
	 *            order : 1 asc, 0 des
	 * 
	 * @return SpreadsheetEditorPage
	 */
	public SpreadsheetEditorPage SortData(int order) {
		menuData.click();
		menuSort.click();
		if (order == 0) {
			descRadioButton.click();
		} else {
			asceRadioButton.click();
		}
		sortOKButton.click();
		return this;
	}

	public SpreadsheetEditorPage selectToolBarNumber() {
		log.info("Click toolbar number icon");
		toolBarNumberIcon.click();
		return this;
	}

	public SpreadsheetEditorPage selectToolBarMoreDateTime() {
		log.info("Click subtoolbar ");
		toolBarNumberIcon.click();
		toolBarMoreTimeDateDropDown.click();
		return this;
	}

	public SpreadsheetEditorPage selectFormatNumber() {
		log.info("Click Format--- Number ");
		menuFormat.click();
		menuShowNumber.click();
		return this;
	}

	public SpreadsheetEditorPage selectFormatNumberMoredate() {
		log.info("Click Format--- Number ");
		menuFormat.click();
		menuShowNumber.click();
		menuShowMoredatetime.click();
		return this;
	}

}
