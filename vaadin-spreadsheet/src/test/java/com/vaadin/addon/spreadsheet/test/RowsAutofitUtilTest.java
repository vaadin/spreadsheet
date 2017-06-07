package com.vaadin.addon.spreadsheet.test;

import static com.vaadin.addon.spreadsheet.test.testutil.CellHeightChecker.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import java.io.File;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Before;
import org.junit.Test;

import com.vaadin.addon.spreadsheet.RowsAutofitUtil;
import com.vaadin.addon.spreadsheet.Spreadsheet;
import com.vaadin.addon.spreadsheet.test.demoapps.SpreadsheetDemoUI;

public class RowsAutofitUtilTest {

    public static final int A_NEW_ROW_NUMBER = 1000;
    private static final int EMPTY_ROW = 0;
    private static final int ROW_WITH_NORMAL_FONT = 1;
    private static final int ROW_WITH_SMALL_FONT = 2;
    private static final int ROW_WITH_HUGE_FONT = 3;
    private static final int ROW_WITH_SMALL_FONT_AND_SMALL_HEIGHT = 5;
    private static final int ROW_WITH_WRAP_TEXT_AND_NUMERIC_VALUE = 7;
    private static final int ROW_WITH_WRAP_TEXT_AND_2_LINES_OF_TEXT = 9;
    private static final int ROW_WITH_WRAP_TEXT_AND_3_LINES_OF_TEXT = 10;
    private static final int ROW_WITH_WRAP_TEXT_MULTIPLE_CELLS_AND_3_LINES_OF_TEXT = 11;

    private static final float DEFAULT_FONT_SIZE = 11;
    private static final float MEDIUM_FONT_SIZE = DEFAULT_FONT_SIZE;
    private static final float HUGE_FONT_SIZE = 48;
    
    private static Sheet sheet;

    private RowsAutofitUtil sut;

    private void checkRowHeightNotChangedAfterAutofit(int row) {
        float initialHeight = (short) (sheet.getRow(row).getHeightInPoints());

        sut.autoSizeRow(sheet, row);

        assertThat(sheet.getRow(row).getHeightInPoints(), is(initialHeight));
    }

    @Before
    public void setup() throws IOException, InvalidFormatException {
        ClassLoader classLoader = SpreadsheetDemoUI.class.getClassLoader();
        File file = new File(
            classLoader.getResource("test_sheets/rows_autofit_util.xlsx")
                .getFile());
        sheet = new XSSFWorkbook(file).getSheetAt(0);

        sut = new RowsAutofitUtil(ROW_AUTOFIT_SECURITY_MARGIN,
            CSS_LINE_HEIGHT_PERCENTAGE,
            CSS_LETTER_SPACING_IN_POINTS);
    }

    @Test
    public void rowsAutofit_emptyRowNotPresentOnSpreadhseet_autoFitDoesntCrashes() {
        sut.autoSizeRow(sheet, EMPTY_ROW);
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
        sut.autoSizeRow(sheet, ROW_WITH_HUGE_FONT);
        float heightInPoints = sheet.getRow(ROW_WITH_HUGE_FONT)
            .getHeightInPoints();

        assertThatCellHeightIsAcceptable(heightInPoints, HUGE_FONT_SIZE,
            ROW_AUTOFIT_SECURITY_MARGIN, 1);

    }

    @Test
    public void rowsAutofit_rowWithWrapTextAndNumericValues_heightSetIntoAcceptableRangeForSingleRowOfText() {
        sut.autoSizeRow(sheet, ROW_WITH_WRAP_TEXT_AND_NUMERIC_VALUE);
        float heightInPoints = sheet
            .getRow(ROW_WITH_WRAP_TEXT_AND_NUMERIC_VALUE).getHeightInPoints();

        assertThatCellHeightIsAcceptable(heightInPoints, HUGE_FONT_SIZE,
            ROW_AUTOFIT_SECURITY_MARGIN, 1);
    }

    @Test
    public void rowsAutofit_rowWithSmallFontAndVerySmallHeight_autoFitExpandsRowHeightToDefault() {
        float initialHeight = sheet.getRow(ROW_WITH_SMALL_FONT_AND_SMALL_HEIGHT)
            .getHeightInPoints();

        sut.autoSizeRow(sheet, ROW_WITH_SMALL_FONT_AND_SMALL_HEIGHT);

        assertThat(sheet.getRow(ROW_WITH_SMALL_FONT_AND_SMALL_HEIGHT)
            .getHeightInPoints(), greaterThan(initialHeight));
    }

    @Test
    public void rowsAutofit_rowWithCustomHeight_isCustomHeightMustReturnTrue() {
        assertTrue(RowsAutofitUtil.isCustomHeight(sheet, ROW_WITH_HUGE_FONT));
    }

    @Test
    public void rowsAutofit_rowsWithoutCustomHeight_isCustomHeightMustReturnFalse() {

        sheet.createRow(A_NEW_ROW_NUMBER);

        assertFalse(
            RowsAutofitUtil.isCustomHeight(sheet, ROW_WITH_NORMAL_FONT));
        assertFalse(RowsAutofitUtil.isCustomHeight(sheet, EMPTY_ROW));
        assertFalse(RowsAutofitUtil.isCustomHeight(sheet, A_NEW_ROW_NUMBER));
    }

    @Test
    public void rowsAutofit_setCustomHeightToFalse_isCustomHeightMustReturnFalse() {
        RowsAutofitUtil.setCustomHeight(sheet, ROW_WITH_HUGE_FONT, false);
        assertFalse(RowsAutofitUtil.isCustomHeight(sheet, ROW_WITH_HUGE_FONT));
    }

    @Test
    public void rowsAutofit_setCustomHeightToTrue_isCustomHeightMustReturnTrue() {
        sheet.createRow(A_NEW_ROW_NUMBER);

        RowsAutofitUtil.setCustomHeight(sheet, ROW_WITH_NORMAL_FONT, true);
        RowsAutofitUtil.setCustomHeight(sheet, EMPTY_ROW, true);
        RowsAutofitUtil.setCustomHeight(sheet, A_NEW_ROW_NUMBER, true);

        assertTrue(RowsAutofitUtil.isCustomHeight(sheet, ROW_WITH_NORMAL_FONT));
        assertTrue(RowsAutofitUtil.isCustomHeight(sheet, EMPTY_ROW));
        assertTrue(RowsAutofitUtil.isCustomHeight(sheet, A_NEW_ROW_NUMBER));
    }

    @Test
    public void rowsAutofit_smallHeightAndTextOverflowing_autofitExpandRowToTwoLines() {
        sut.autoSizeRow(sheet, ROW_WITH_WRAP_TEXT_AND_2_LINES_OF_TEXT);

        float heightInPoints = sheet
            .getRow(ROW_WITH_WRAP_TEXT_AND_2_LINES_OF_TEXT).getHeightInPoints();

        assertThatCellHeightIsAcceptable(heightInPoints, MEDIUM_FONT_SIZE,
            ROW_AUTOFIT_SECURITY_MARGIN, 2);
    }

    @Test
    public void rowsAutofit_smallHeightAndTextOverflowing_autofitExpandRowToThreeLines() {
        sut.autoSizeRow(sheet, ROW_WITH_WRAP_TEXT_AND_3_LINES_OF_TEXT);

        float heightInPoints = sheet
            .getRow(ROW_WITH_WRAP_TEXT_AND_3_LINES_OF_TEXT).getHeightInPoints();

        assertThatCellHeightIsAcceptable(heightInPoints, MEDIUM_FONT_SIZE,
            ROW_AUTOFIT_SECURITY_MARGIN, 3);
    }

    @Test
    public void rowsAutofit_severalCellsWithDifferentContentLenghtAndType_autofitExpandRowToThreeLines() {
        sut.autoSizeRow(sheet,
            ROW_WITH_WRAP_TEXT_MULTIPLE_CELLS_AND_3_LINES_OF_TEXT);

        float heightInPoints = sheet
            .getRow(ROW_WITH_WRAP_TEXT_MULTIPLE_CELLS_AND_3_LINES_OF_TEXT)
            .getHeightInPoints();

        assertThatCellHeightIsAcceptable(heightInPoints, MEDIUM_FONT_SIZE,
            ROW_AUTOFIT_SECURITY_MARGIN, 3);
    }
}
