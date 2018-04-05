package com.vaadin.addon.spreadsheet.test;

import org.junit.Test;

public class CellBorderInGridlessSheetTest  extends AbstractSpreadsheetTestCase{

    @Override
    public void setUp() throws Exception {
        loadPage("test_borders.xlsx");
    }

    @Test
    public void openSpreadsheet_fromExcelFileWith_bordersAndNoGrid_thereAreBorders() throws Exception {
        compareScreen("bordersAndNoGrid");
    }

}
