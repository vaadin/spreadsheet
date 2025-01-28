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

import com.vaadin.addon.spreadsheet.Spreadsheet;

public class DeleteRowFixture implements SpreadsheetFixture {
    @Override
    public void loadFixture(Spreadsheet spreadsheet) {

        spreadsheet.shiftRows(
                spreadsheet.getSelectedCellReference().getRow() + 1,
                spreadsheet.getRows() - 1, -1);

        spreadsheet.setMaxRows(spreadsheet.getRows() - 1);
        spreadsheet.refreshAllCellValues();
    }
}
