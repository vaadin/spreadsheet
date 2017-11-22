package com.vaadin.addon.spreadsheet.test.fixtures;

import com.vaadin.addon.spreadsheet.Spreadsheet;

public class AddClickMeTextToCellsFixture implements SpreadsheetFixture {

    @Override
    public void loadFixture(Spreadsheet spreadsheet) {
        int column = 1;
        int firstRow = 1;
        int quantity = 3;


        for (int row = firstRow; row < firstRow + quantity; row++) {
            spreadsheet.createCell(row, column, "Click Me");
        }

        spreadsheet.refreshAllCellValues();
    }
}
