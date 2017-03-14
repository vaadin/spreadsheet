package com.vaadin.addon.spreadsheet.test;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
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

    // Acceptable ranges for cell height after autofit for this test is
    // 110% to 120% of the font size in case of single row of text
    private static final float MINIMUM_PERCENTAGE_OF_FONT_SIZE = 1.1f;
    private static final float MAXIMUM_PERCENTAGE_OF_FONT_SIZE = 1.2f;

    private static final int EMPTY_ROW = 0;
    private static final int ROW_WITH_NORMAL_FONT = 1;
    private static final int ROW_WITH_SMALL_FONT = 2;
    private static final int ROW_WITH_HUGE_FONT = 3;
    private static final int ROW_WITH_SMALL_FONT_AND_SMALL_HEIGHT = 5;
    private static final int ROW_WITH_WRAP_TEXT_AND_NUMERIC_VALUE = 7;

    private static final float HUGE_FONT_SIZE = 48;

    private static Sheet sheet;

    @BeforeClass
    public static void setup() throws IOException, InvalidFormatException {
        ClassLoader classLoader = SpreadsheetDemoUI.class.getClassLoader();
        File file = new File(
            classLoader.getResource("test_sheets/rows_autofit_util.xlsx")
                .getFile());
        sheet = new XSSFWorkbook(file).getSheetAt(0);
    }

    private static void checkRowHeightNotChangedAfterAutofit(int row) {
        float initialHeight = (short) (sheet.getRow(row).getHeightInPoints());

        RowsAutofitUtil.autoSizeRow(sheet, row);

        assertThat(sheet.getRow(row).getHeightInPoints(), is(initialHeight));
    }

    private static void checkRowFitsFontSizeAfterAutofit(int row,
        float fontSize) {
        RowsAutofitUtil.autoSizeRow(sheet, row);
        float minimumRowHeight = fontSize * MINIMUM_PERCENTAGE_OF_FONT_SIZE;
        float maximumRowHeight = fontSize * MAXIMUM_PERCENTAGE_OF_FONT_SIZE;

        // Row height must properly fit the font size with a "security" margin
        assertThat(sheet.getRow(row).getHeightInPoints(),
            allOf(greaterThan(minimumRowHeight), lessThan(maximumRowHeight)));

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
    public void rowsAutofit_rowWithStandardSizeFontAndDefaultHeight_autoFitDoesntChangeRowHeight() {
        checkRowHeightNotChangedAfterAutofit(ROW_WITH_NORMAL_FONT);
    }

    @Test
    public void rowsAutofit_rowWithSmallFontAndDefaultHeight_autoFitDoesntChangeRowHeight() {
        checkRowHeightNotChangedAfterAutofit(ROW_WITH_SMALL_FONT);
    }

    @Test
    public void rowsAutofit_rowWithHugeFontAndSmallHeight_heightSetIntoAcceptableRange() {
        checkRowFitsFontSizeAfterAutofit(ROW_WITH_HUGE_FONT, HUGE_FONT_SIZE);
    }

    @Test
    public void rowsAutofit_rowWithWrapTextAndNumericValues_heightSetIntoAcceptableRangeForSingleRowOfText() {
        checkRowFitsFontSizeAfterAutofit(ROW_WITH_WRAP_TEXT_AND_NUMERIC_VALUE,
            HUGE_FONT_SIZE);
    }

    @Test
    public void rowsAutofit_rowWithSmallFontAndVerySmallHeight_autoFitExpandsRowHeightToDefault() {
        float initialHeight = sheet.getRow(ROW_WITH_SMALL_FONT_AND_SMALL_HEIGHT)
            .getHeightInPoints();
        RowsAutofitUtil
            .autoSizeRow(sheet, ROW_WITH_SMALL_FONT_AND_SMALL_HEIGHT);

        assertThat(sheet.getRow(ROW_WITH_SMALL_FONT_AND_SMALL_HEIGHT)
            .getHeightInPoints(), greaterThan(initialHeight));
    }
}
