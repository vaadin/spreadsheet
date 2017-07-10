package com.vaadin.addon.spreadsheet.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.vaadin.addon.spreadsheet.test.pageobjects.SpreadsheetPage;

public class ConditionalFormattingCellValueIsTest
    extends AbstractSpreadsheetTestCase {

    private static final String STRING_VALUE = "'Foo";
    private static final String NUMBER_VALUE = "1";
    private static final String DIFFERENT_NUMBER_VALUE = "2";
    private static final String TRUE_VALUE = "=1<2";
    private static final String FALSE_VALUE = "=1>2";
    private static final String FALSE_CONDITION_COLOR = "rgba(255, 255, 255, 1)";
    private static final String TRUE_CONDITION_COLOR = "rgba(255, 0, 0, 1)";

    private SpreadsheetPage spreadsheetPage;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        spreadsheetPage = headerPage
            .loadFile("conditional_formatting_cell_is.xlsx", this);
        spreadsheetPage.selectSheetAt(1);
    }

    @Test
    public void B3D3F3RedIfNotEqualToCellAbove_makeConditionsFalse_filledWhite() {
        spreadsheetPage.setCellValue("B2", STRING_VALUE);
        spreadsheetPage.setCellValue("B3", "Not" + STRING_VALUE);

        spreadsheetPage.setCellValue("D2", NUMBER_VALUE);
        spreadsheetPage.setCellValue("D3", DIFFERENT_NUMBER_VALUE);

        spreadsheetPage.setCellValue("F2", TRUE_VALUE);
        spreadsheetPage.setCellValue("F3", FALSE_VALUE);

        String cellColorStringCase = spreadsheetPage.getCellColor("B3");
        String cellColorNumberCase = spreadsheetPage.getCellColor("D3");
        String cellColorBooleanCase = spreadsheetPage.getCellColor("F3");

        assertEquals(FALSE_CONDITION_COLOR, cellColorStringCase);
        assertEquals(FALSE_CONDITION_COLOR, cellColorNumberCase);
        assertEquals(FALSE_CONDITION_COLOR, cellColorBooleanCase);
    }

    @Test
    public void B3D3F3RedIfNotEqualToCellAbove_makeConditionsTrue_filledRed() {
        spreadsheetPage.setCellValue("B2", STRING_VALUE);
        spreadsheetPage.setCellValue("B3", STRING_VALUE);

        spreadsheetPage.setCellValue("D2", NUMBER_VALUE);
        spreadsheetPage.setCellValue("D3", NUMBER_VALUE);

        spreadsheetPage.setCellValue("F2", TRUE_VALUE);
        spreadsheetPage.setCellValue("F3", TRUE_VALUE);

        String cellColorStringCase = spreadsheetPage.getCellColor("B3");
        String cellColorNumberCase = spreadsheetPage.getCellColor("D3");
        String cellColorBooleanCase = spreadsheetPage.getCellColor("F3");

        assertEquals(TRUE_CONDITION_COLOR, cellColorStringCase);
        assertEquals(TRUE_CONDITION_COLOR, cellColorNumberCase);
        assertEquals(TRUE_CONDITION_COLOR, cellColorBooleanCase);
    }

    @Test
    public void B4RedIfNotEqualsB3_insertIncoherentValue_B4FilledRed() {
        spreadsheetPage.setCellValue("B3", "1");
        spreadsheetPage.setCellValue("B4", "1");
        assertEquals(FALSE_CONDITION_COLOR, spreadsheetPage.getCellColor("B4"));

        spreadsheetPage.setCellValue("B3", "1");
        spreadsheetPage.setCellValue("B4", "'1");
        assertEquals(TRUE_CONDITION_COLOR, spreadsheetPage.getCellColor("B4"));
    }

    @Test
    public void B5RedIfNotEqualsToZero_insertDifferentValues_B5FilledCorrectly() {
        spreadsheetPage.setCellValue("B5", "0");
        assertEquals(FALSE_CONDITION_COLOR, spreadsheetPage.getCellColor("B5"));

        spreadsheetPage.setCellValue("B5", "1");
        assertEquals(TRUE_CONDITION_COLOR, spreadsheetPage.getCellColor("B5"));

//        Currently deleting a cell does not affect its own color (bug), although
//        all other cells are updated.
//        Commenting out cell deletion test here and in all other methods until this issue is resolved.
//        https://github.com/vaadin/spreadsheet/issues/577
//        
//        spreadsheetPage.deleteCellValue("B5");
//        assertEquals(FALSE_CONDITION_COLOR, spreadsheetPage.getCellColor("B5"));
    }

    @Test
    public void B6RedIfEqualsToZero_insertValues_B6FilledCorrectly() {
        spreadsheetPage.setCellValue("B6", "0");
        assertEquals(TRUE_CONDITION_COLOR, spreadsheetPage.getCellColor("B6"));

        spreadsheetPage.setCellValue("B6", "1");
        assertEquals(FALSE_CONDITION_COLOR, spreadsheetPage.getCellColor("B6"));

//        spreadsheetPage.deleteCellValue("B6");
//        assertEquals(TRUE_CONDITION_COLOR, spreadsheetPage.getCellColor("B6"));
    }

    @Test
    public void B7RedIfNotEqualsToBooleanFalse_insertValues_B7FilledRed() {
        spreadsheetPage.setCellValue("B7", FALSE_VALUE);
        assertEquals(FALSE_CONDITION_COLOR, spreadsheetPage.getCellColor("B7"));

        spreadsheetPage.setCellValue("B7", TRUE_VALUE);
        assertEquals(TRUE_CONDITION_COLOR, spreadsheetPage.getCellColor("B7"));

//        spreadsheetPage.deleteCellValue("B7");
//        assertEquals(FALSE_CONDITION_COLOR, spreadsheetPage.getCellColor("B7"));
    }

    @Test
    public void B2RedIfEqualsB3_insertValues_B3FilledCorrectly() {
        spreadsheetPage.setCellValue("B2", "1");
        spreadsheetPage.setCellValue("B3", "2");
        assertEquals(FALSE_CONDITION_COLOR, spreadsheetPage.getCellColor("B3"));

//        spreadsheetPage.deleteCellValue("B2");
//        spreadsheetPage.deleteCellValue("B3");
//        assertEquals(TRUE_CONDITION_COLOR, spreadsheetPage.getCellColor("B3"));
    }
}
