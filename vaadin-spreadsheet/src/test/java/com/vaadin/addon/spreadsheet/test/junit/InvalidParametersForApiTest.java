package com.vaadin.addon.spreadsheet.test.junit;

import com.vaadin.addon.spreadsheet.Spreadsheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class InvalidParametersForApiTest {

    private Spreadsheet spreadsheet;

    @Before
    public void setup() {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue(1);

        spreadsheet = new Spreadsheet();
        spreadsheet.setWorkbook(workbook);
    }

    @Test
    public void createCell_withNullValue() {
        Cell cell = spreadsheet.createCell(0, 0, null);
        assertNotNull(cell);
    }
}
