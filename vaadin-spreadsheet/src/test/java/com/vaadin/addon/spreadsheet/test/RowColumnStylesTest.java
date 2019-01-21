package com.vaadin.addon.spreadsheet.test;

import org.junit.Test;

/**
 * RowColumnStylesTest
 */
public class RowColumnStylesTest extends AbstractSpreadsheetTestCase {

    /**
     * styles_sheetHasRowAndColumnStyles_spreadsheetIsRenderedCorrectly
     * @throws Exception
     */
    @Test
    public void styles_sheetHasRowAndColumnStyles_spreadsheetIsRenderedCorrectly()
            throws Exception {
        headerPage.loadFile("row_and_column_styles.xlsx", this);

        compareScreen("row_and_column_styles");
    }
}
