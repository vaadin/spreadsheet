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
package com.vaadin.addon.spreadsheet.test.fixtures;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.util.CellReference;

import com.vaadin.addon.spreadsheet.Spreadsheet;

public class RowToggleFixture implements SpreadsheetFixture {

    @Override
    public void loadFixture(Spreadsheet spreadsheet) {
        List<Integer> rowIndexes = new ArrayList<Integer>();

        for (CellReference cellRef : spreadsheet.getSelectedCellReferences()) {
            if (!rowIndexes.contains(cellRef.getRow())) {
                rowIndexes.add(cellRef.getRow());
            }
        }

        for (Integer row : rowIndexes) {
            spreadsheet.setRowHidden(row, !spreadsheet.isRowHidden(row));
        }

        spreadsheet.refreshAllCellValues();
    }
}
