package com.vaadin.addon.spreadsheet.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import com.vaadin.addon.spreadsheet.test.pageobjects.SpreadsheetPage;

public class ConditionalFormattingBasedOnFormulaTest
    extends AbstractSpreadsheetTestCase {

    private static final String VALUE = "'Fooooooooooooooooooooooooo";
    private static final String FALSE_CONDITION_COLOR = "rgba(255, 255, 255, 1)";
    private static final String TRUE_CONDITION_COLOR = "rgba(255, 0, 0, 1)";
    
    private SpreadsheetPage spreadsheetPage;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        spreadsheetPage = headerPage.loadFile(
            "conditional_formatting_with_formula_on_second_sheet.xlsx", this);
        spreadsheetPage.selectSheetAt(1);
    }

    @Test
    public void loadSpreadsheetWithConditionalFormattingInA2_MakeConditionFalse_CellA2FilledWhite() {
        spreadsheetPage.setCellValue("A1", VALUE);
        spreadsheetPage.setCellValue("A2", "Not"+VALUE);
        assertEquals(FALSE_CONDITION_COLOR, spreadsheetPage.getCellColor("A2"));
    }

    @Test
    public void loadSpreadsheetWithConditionalFormattingInA2_MakeConditionTrue_CellA2FilledRed() {
        spreadsheetPage.setCellValue("A1", VALUE);
        spreadsheetPage.setCellValue("A2", VALUE);
        assertEquals(TRUE_CONDITION_COLOR, spreadsheetPage.getCellColor("A2"));
    }
}
