/*
 * Vaadin Spreadsheet Addon
 *
 * Copyright (C) 2013-2024 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.addon.spreadsheet.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.vaadin.addon.spreadsheet.test.pageobjects.SpreadsheetPage;

public class BigExcelFileTest extends AbstractSpreadsheetTestCase {

    @Test
    public void openSpreadsheet_fromExcelFileWith_100_000_Rows_theContentIsRendered()
            throws Exception {
        SpreadsheetPage spreadsheetPage = headerPage
                .loadFile("100_000_rows.xlsx", this);

        assertEquals("File opened", spreadsheetPage.getCellValue("A1"));
    }
}
