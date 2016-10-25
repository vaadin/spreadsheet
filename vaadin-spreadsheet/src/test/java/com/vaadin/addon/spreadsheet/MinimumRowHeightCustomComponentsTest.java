package com.vaadin.addon.spreadsheet;

import com.vaadin.ui.Component;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MinimumRowHeightCustomComponentsTest {

    private static final float CUSTOM_ROW_HEIGTH = 15f;
    private Spreadsheet spreadsheet;

    @Before
    public void setUp() throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook();
        final XSSFSheet first = workbook.createSheet("First");
        final XSSFRow row = first.createRow(1);
        row.setHeightInPoints(CUSTOM_ROW_HEIGTH);
        final XSSFCell onlyCell = row.createCell(1);
        final Component component = mock(Component.class);
        when(component.getConnectorId()).thenReturn("id");

        SpreadsheetComponentFactory factory = new SpreadsheetComponentFactory() {
            @Override
            public Component getCustomComponentForCell(
                    final Cell cell, final int rowIndex, final int columnIndex,
                    final Spreadsheet spreadsheet,
                    final Sheet sheet) {
                if (cell != null &&
                        cell.getRowIndex() == onlyCell.getRowIndex() && cell.getColumnIndex() == onlyCell.getColumnIndex()) {
                    return component;
                }
                return null;
            }

            @Override
            public Component getCustomEditorForCell(
                    final Cell cell, final int rowIndex, final int columnIndex,
                    final Spreadsheet spreadsheet,
                    final Sheet sheet) {
                return null;
            }

            @Override
            public void onCustomEditorDisplayed(
                    final Cell cell, final int rowIndex, final int columnIndex,
                    final Spreadsheet spreadsheet,
                    final Sheet sheet, final Component customEditor) {
            }
        };
        spreadsheet = new Spreadsheet(workbook);
        spreadsheet.setSpreadsheetComponentFactory(factory);
    }

    @Test
    public void defaultMinimumRowHeightIs30() throws Exception {
        spreadsheet.onSheetScroll(1, 1, 20, 20);
        spreadsheet.reloadVisibleCellContents();
        assertThat(spreadsheet.getState().rowH[1], is(30f));
    }

    @Test
    public void defaultMinimumRowHeightDifferentFromDefault() throws Exception {
        spreadsheet.setMinimumRowHeightForComponents(10);
        spreadsheet.onSheetScroll(1, 1, 20, 20);
        spreadsheet.reloadVisibleCellContents();
        assertThat(spreadsheet.getState().rowH[1], is(CUSTOM_ROW_HEIGTH));
    }
}
