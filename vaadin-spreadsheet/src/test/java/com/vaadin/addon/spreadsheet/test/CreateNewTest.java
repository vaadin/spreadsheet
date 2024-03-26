/*
 * Vaadin Spreadsheet Addon
 *
 * Copyright (C) 2012-2024 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.addon.spreadsheet.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.vaadin.addon.spreadsheet.test.pageobjects.SpreadsheetPage;

public class CreateNewTest extends AbstractSpreadsheetTestCase {

    private SpreadsheetPage spreadsheetPage;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        spreadsheetPage = headerPage.createNewSpreadsheet();
    }

    @Test
    public void testCreateNewSpreadsheet() throws Exception {
        assertTrue(spreadsheetPage.isDisplayed());
    }

    @Test
    public void testNewSpreadsheetHasA1Focused() throws Exception {
        assertEquals("A1", spreadsheetPage.getAddressFieldValue());
        assertTrue(spreadsheetPage.isCellSelected(1, 1));
    }
}
