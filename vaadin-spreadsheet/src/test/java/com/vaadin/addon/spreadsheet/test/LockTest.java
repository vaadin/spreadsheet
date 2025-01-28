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

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

import com.vaadin.addon.spreadsheet.elements.SpreadsheetElement;
import com.vaadin.addon.spreadsheet.test.fixtures.TestFixtures;

public class LockTest extends AbstractSpreadsheetTestCase {

    @Ignore("Fails with all browsers, user can still add content to B2 after lock fixture is run")
    @Test
    public void testLockedCells() {
        headerPage.createNewSpreadsheet();
        SpreadsheetElement spreadsheet = $(SpreadsheetElement.class).first();
        spreadsheet.getCellAt("B2").setValue("value");
        sheetController.selectRegion("B3", "D4");
        headerPage.loadTestFixture(TestFixtures.LockCell);

        assertEquals("locked", spreadsheet.getCellAt("B2").getValue());
        spreadsheet.getCellAt("B2").setValue("new value on locked cell");
        assertEquals("value", spreadsheet.getCellAt("B2").getValue());

        assertEquals("unlocked", spreadsheet.getCellAt("C3").getValue());
        sheetController.putCellContent("C3", "value");
        assertEquals("value", spreadsheet.getCellAt("C3").getValue());
    }
}
