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

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.util.CellReference;

import com.vaadin.addon.spreadsheet.Spreadsheet;

public class ColumnToggleFixture implements SpreadsheetFixture {

    @Override
    public void loadFixture(Spreadsheet spreadsheet) {
        List<Integer> columnIndexes = new ArrayList<Integer>();
        for (CellReference cellRef : spreadsheet.getSelectedCellReferences()) {
            if (!columnIndexes.contains((int) cellRef.getCol())) {
                columnIndexes.add((int) cellRef.getCol());
            }
        }

        for (Integer col : columnIndexes) {
            spreadsheet.setColumnHidden(col, !spreadsheet.isColumnHidden(col));
        }

        spreadsheet.refreshAllCellValues();
    }
}
