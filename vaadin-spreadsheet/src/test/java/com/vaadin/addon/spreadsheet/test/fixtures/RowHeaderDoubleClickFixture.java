package com.vaadin.addon.spreadsheet.test.fixtures;

import org.apache.poi.ss.usermodel.Cell;

import com.vaadin.addon.spreadsheet.Spreadsheet;

public class RowHeaderDoubleClickFixture implements SpreadsheetFixture {
    @Override
    public void loadFixture(final Spreadsheet spreadsheet) {

        final Cell cell = spreadsheet.createCell(1, 1, "EMPTY");

        spreadsheet.addRowHeaderDoubleClickListner(
            new Spreadsheet.RowHeaderDoubleClickListner() {
                @Override
                public void onRowHeaderDoubleClick(
                    Spreadsheet.RowHeaderDoubleClickEvent event) {
                    cell.setCellValue(
                        "DoubleClicked on row header" + event.rowIndex);
                    spreadsheet.refreshAllCellValues();
                }
            });
        spreadsheet.refreshAllCellValues();
    }
}
