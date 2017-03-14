package com.vaadin.addon.spreadsheet.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFTable;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable;

import com.vaadin.addon.spreadsheet.Spreadsheet;

public class ColumnFiltersTest {

    private XSSFWorkbook workbook;
    private Spreadsheet spreadsheet;


    @Test
    public void sheetWithFilters_loadWorkbook_filtersPreserved(){
        workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();

        Row row1 = sheet.createRow(1);
        row1.createCell(1).setCellValue("col1");

        Row row2 = sheet.createRow(2);
        row2.createCell(1).setCellValue(1);

        Row row3 = sheet.createRow(3);
        row3.createCell(1).setCellValue(2);

        sheet.setAutoFilter(new CellRangeAddress(1, 3, 1, 1));

        spreadsheet = new Spreadsheet(workbook);
        assertNotNull(spreadsheet.getTables());
        assertEquals(1, spreadsheet.getTables().size());
    }

    @Test
    public void sheetWithTables_loadWorkbook_tablesPreserved(){
        workbook = new XSSFWorkbook();
        XSSFSheet sheet1 = workbook.createSheet();

        Row r1 = sheet1.createRow(1);
        r1.createCell(1).setCellValue("col1");

        Row r2 = sheet1.createRow(2);
        r2.createCell(1).setCellValue(1);

        Row r3 = sheet1.createRow(3);
        r3.createCell(1).setCellValue(2);

        XSSFTable table = sheet1.createTable();
        CTTable ctTable = table.getCTTable();
        ctTable.setRef("B2:B4");

        spreadsheet = new Spreadsheet(workbook);
        assertNotNull(spreadsheet.getTables());
        assertEquals(1, spreadsheet.getTables().size());
        assertEquals(CellRangeAddress.valueOf("B2:B4"),
            spreadsheet.getTables().iterator().next().getFullTableRegion());
    }
}
