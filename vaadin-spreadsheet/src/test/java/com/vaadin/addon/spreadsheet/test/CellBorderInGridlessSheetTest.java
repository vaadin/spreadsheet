package com.vaadin.addon.spreadsheet.test;

import org.junit.Test;

import com.vaadin.addon.spreadsheet.test.pageobjects.SpreadsheetPage;

/**
 * CellBorderInGridlessSheetTest
 */
public class CellBorderInGridlessSheetTest  extends AbstractSpreadsheetTestCase{
    
    /**
     * openSpreadsheet_fromExcelFileWith_bordersAndNoGrid_thereAreBorders
     * @throws Exception
     */
    @Test
    public void openSpreadsheet_fromExcelFileWith_bordersAndNoGrid_thereAreBorders() throws Exception {
        SpreadsheetPage spreadsheetPage = headerPage.loadFile(
                "test_borders.xlsx", this);

        compareScreen("bordersAndNoGrid");
    }

}
