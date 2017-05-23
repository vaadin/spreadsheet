package com.vaadin.addon.spreadsheet.test.fixtures;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.vaadin.addon.spreadsheet.Spreadsheet;

public class RowHeaderDoubleClickFixture implements SpreadsheetFixture {
    @Override
    public void loadFixture(final Spreadsheet spreadsheet) {

        spreadsheet.addRowHeaderDoubleClickListener(
            new Spreadsheet.RowHeaderDoubleClickListener() {
                @Override
                public void onRowHeaderDoubleClick(
                    Spreadsheet.RowHeaderDoubleClickEvent event) {

                    final Cell cell = spreadsheet.getActiveSheet()
                        .getRow(event.getRowIndex())
                        .getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

                    cell.setCellValue("Double-click on row header");

                    spreadsheet.refreshAllCellValues();
                }
            });
    }
}
