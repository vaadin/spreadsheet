package com.vaadin.addon.spreadsheet.test;

import org.junit.Test;

public class RowColumnStylesTest extends AbstractSpreadsheetTestCase {

    @Test
    public void styles_sheetHasRowAndColumnStyles_spreadsheetIsRenderedCorrectly()
            throws Exception {
        loadPage("row_and_column_styles.xlsx");

        compareScreen("row_and_column_styles");
    }
}
