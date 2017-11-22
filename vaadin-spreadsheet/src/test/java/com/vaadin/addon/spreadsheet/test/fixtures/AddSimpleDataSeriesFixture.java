package com.vaadin.addon.spreadsheet.test.fixtures;

import com.vaadin.addon.spreadsheet.Spreadsheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

public class AddSimpleDataSeriesFixture implements SpreadsheetFixture {

    @Override
    public void loadFixture(Spreadsheet spreadsheet) {
        int column = 0;
        int firstRow = 11;
        int quantity = 5;

        spreadsheet.createCell(firstRow - 1, column, "Series");
        for (int i = 0; i < quantity; i++) {
            Cell cell = spreadsheet.createCell(i + firstRow, column, i + 1);
            cell.setCellType(CellType.NUMERIC);
        }

        spreadsheet.refreshAllCellValues();
    }
}
