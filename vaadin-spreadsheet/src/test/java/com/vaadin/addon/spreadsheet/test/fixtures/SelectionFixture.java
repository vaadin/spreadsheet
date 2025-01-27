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
package com.vaadin.addon.spreadsheet.test.fixtures;

import org.apache.poi.ss.util.CellReference;

import com.vaadin.addon.spreadsheet.Spreadsheet;

public class SelectionFixture implements SpreadsheetFixture {
    @Override
    public void loadFixture(Spreadsheet spreadsheet) {
        for (CellReference cellRef : spreadsheet.getSelectedCellReferences()) {
            spreadsheet.createCell(cellRef.getRow(), cellRef.getCol(),
                    "SELECTED");
        }
        spreadsheet.refreshAllCellValues();
    }
}
