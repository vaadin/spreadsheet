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

import static org.junit.Assert.assertFalse;

import java.util.List;

import org.junit.Test;
import org.openqa.selenium.WebElement;

import com.vaadin.addon.spreadsheet.test.pageobjects.SpreadsheetPage;

public class UngroupingTest extends AbstractSpreadsheetTestCase {

    private SpreadsheetPage spreadsheetPage;

    /**
     * Ticket 599#
     */
    @Test
    public void grouping_expandColumnGroup_groupingElementsHaveCorrectValues()
            throws Exception {
        spreadsheetPage = headerPage.loadFile("ungrouping_cellUpdating.xlsx",
                this);
        List<WebElement> groupings = spreadsheetPage.getGroupings();
        groupings.get(1).click();
        String cellValue = spreadsheetPage.getCellValue(3, 1);
        assertFalse(cellValue.contains("#"));
    }

}
