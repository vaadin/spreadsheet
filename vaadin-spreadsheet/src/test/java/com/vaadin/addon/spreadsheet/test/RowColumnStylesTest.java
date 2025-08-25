/*
 * Vaadin Spreadsheet Addon
 *
 * Copyright (C) 2013-2025 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.addon.spreadsheet.test;

import org.junit.Test;

public class RowColumnStylesTest extends AbstractSpreadsheetTestCase {

    @Test
    public void styles_sheetHasRowAndColumnStyles_spreadsheetIsRenderedCorrectly()
            throws Exception {
        headerPage.loadFile("row_and_column_styles.xlsx", this);

        compareScreen("row_and_column_styles");
    }
}
