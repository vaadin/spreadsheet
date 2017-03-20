package com.vaadin.addon.spreadsheet.test;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import com.vaadin.addon.spreadsheet.test.pageobjects.SpreadsheetPage;

public class ConditionalFormattingOnCellDeletionTest extends AbstractSpreadsheetTestCase {

    private static final String FALSE_CONDITION_COLOR = "rgba(255, 255, 255, 1)";
    private static final String TRUE_CONDITION_COLOR = "rgba(255, 0, 0, 1)";

    private SpreadsheetPage spreadsheetPage;
    
    @Override
    public void setUp() throws Exception {
        setDebug(true);
        super.setUp();
        spreadsheetPage = headerPage.loadFile("conditional_formatting_with_formula_on_second_sheet.xlsx", this);
        this.spreadsheetPage.selectSheetAt(1);
    }

    @Test
    public void deletionHandler_SpreadsheetWithDeletionFixture_deleteSingleCellFailsWhenHandlerReturnsFalse() {

        assertEquals(FALSE_CONDITION_COLOR, getA2CellColor());
        
        spreadsheetPage.deleteCellValue(1,2);

        assertEquals(FALSE_CONDITION_COLOR, getA2CellColor());
        
        spreadsheetPage.deleteCellValue(1,1);

        assertEquals(TRUE_CONDITION_COLOR, getA2CellColor());
    }

    private String getA2CellColor() {
        return spreadsheetPage.getCellColor(1,2);
    }
}
