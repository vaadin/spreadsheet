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

import org.junit.Test;

import com.vaadin.addon.spreadsheet.elements.SpreadsheetElement;

public class LoadFileWithFrozenPaneScrolled
        extends AbstractSpreadsheetTestCase {

    @Test
    public void loadFileWithFrozenPaneScrolled_firstColumnIsA() {
        headerPage.loadFile("frozen_pane_scrolled.xlsx", this);

        final SpreadsheetElement spreadsheet = $(SpreadsheetElement.class)
                .first();

        final String row1 = spreadsheet.getRowHeader(1).getText();
        final String column1 = spreadsheet.getColumnHeader(1).getText();

        assertEquals("A1", column1 + row1);
    }
}
