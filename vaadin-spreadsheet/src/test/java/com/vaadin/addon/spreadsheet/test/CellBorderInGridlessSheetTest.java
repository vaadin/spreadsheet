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

import com.vaadin.addon.spreadsheet.test.pageobjects.SpreadsheetPage;

public class CellBorderInGridlessSheetTest extends AbstractSpreadsheetTestCase {

    @Test
    public void openSpreadsheet_fromExcelFileWith_bordersAndNoGrid_thereAreBorders()
            throws Exception {
        SpreadsheetPage spreadsheetPage = headerPage
                .loadFile("test_borders.xlsx", this);

        compareScreen("bordersAndNoGrid");
    }

}
