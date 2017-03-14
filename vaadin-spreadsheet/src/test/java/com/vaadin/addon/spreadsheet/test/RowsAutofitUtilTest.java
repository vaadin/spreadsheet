package com.vaadin.addon.spreadsheet.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import java.io.File;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.BeforeClass;
import org.junit.Test;

import com.vaadin.addon.spreadsheet.RowsAutofitUtil;
import com.vaadin.addon.spreadsheet.test.demoapps.SpreadsheetDemoUI;

public class RowsAutofitUtilTest {

    private static Sheet sheet;

    private static final int EMPTY_ROW = 0;
    private static final int ROW_WITH_NORMAL_FONT = 1;
    private static final int ROW_WITH_SMALL_FONT = 2;
    private static final int ROW_WITH_HUGE_FONT = 3;

    @BeforeClass
    public static void setup() throws IOException, InvalidFormatException {
        ClassLoader classLoader = SpreadsheetDemoUI.class.getClassLoader();
        File file = new File(classLoader.getResource("test_sheets/rows_autofit_util.xlsx").getFile());
        sheet = new XSSFWorkbook(file).getSheetAt(0);
    }

    @Test
    public void rowsAutofit_emptyRowNotPresentOnSpreadhseet_autoFitDoesntCrashes() {
        RowsAutofitUtil.autoSizeRow(sheet, EMPTY_ROW);
    }

    @Test
    public void rowsAutofit_emptyRowWithDefaultHeight_autoFitDoesntChangeRowHeight() {
        sheet.createRow(EMPTY_ROW);
        checkRowHeightNotChangedAfterAutofit(EMPTY_ROW);
    }

    @Test
    public void rowsAutofit_rowWithStandardSizeFont_autoFitDoesntChangeRowHeight() {
        checkRowHeightNotChangedAfterAutofit(ROW_WITH_NORMAL_FONT);
    }

    @Test
    public void rowsAutofit_rowWithSmallFont_rowHeightAlreadySetToDefault_autoFitDoesntChangeRowHeight() {
        checkRowHeightNotChangedAfterAutofit(ROW_WITH_SMALL_FONT);
    }

    private static void checkRowHeightNotChangedAfterAutofit(int row) {
        short initialHeight = sheet.getRow(row).getHeight();

        RowsAutofitUtil.autoSizeRow(sheet, row);

        assertThat(sheet.getRow(row).getHeight(), is(initialHeight));
    }



}
