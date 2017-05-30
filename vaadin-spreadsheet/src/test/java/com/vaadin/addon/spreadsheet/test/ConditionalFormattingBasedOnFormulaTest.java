package com.vaadin.addon.spreadsheet.test;

import static org.junit.Assert.assertEquals;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import com.vaadin.addon.spreadsheet.test.pageobjects.SpreadsheetPage;
import com.vaadin.testbench.annotations.RunLocally;
import com.vaadin.testbench.parallel.Browser;

@RunLocally(Browser.PHANTOMJS)
public class ConditionalFormattingBasedOnFormulaTest
    extends AbstractSpreadsheetTestCase {

    private static final String VALUE = "'Fooooooooooooooooooooooooo";
    private static final String FALSE_CONDITION_COLOR = "rgba(255, 255, 255, 1)";
    private static final String TRUE_CONDITION_COLOR = "rgba(255, 0, 0, 1)";
    private static final String DIFFERENT_FROM_ZERO_CONDITION_COLOR = "rgba(255, 255, 0, 1)";
    
    private SpreadsheetPage spreadsheetPage;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        spreadsheetPage = headerPage.loadFile(
            "conditional_formatting_with_formula_on_second_sheet.xlsx", this);
        spreadsheetPage.selectSheetAt(1);
    }

    @Ignore
    @Test
    public void loadSpreadsheetWithConditionalFormattingInA2_MakeConditionFalse_CellA2FilledWhite() {
        spreadsheetPage.setCellValue("A1", VALUE);
        spreadsheetPage.setCellValue("A2", "Not"+VALUE);
        assertEquals(FALSE_CONDITION_COLOR, spreadsheetPage.getCellColor("A2"));
    }

    @Ignore
    @Test
    public void loadSpreadsheetWithConditionalFormattingInA2_MakeConditionTrue_CellA2FilledRed() {
        spreadsheetPage.setCellValue("A1", VALUE);
        spreadsheetPage.setCellValue("A2", VALUE);
        assertEquals(TRUE_CONDITION_COLOR, spreadsheetPage.getCellColor("A2"));
    }

    @Ignore
    @Test
    public void loadSpreadsheetWithConditionalFormattingRulesInRow10_EvaluateFormatting_CheckColorOfCells(){
        String a10WithConditionEqualsToOne = spreadsheetPage.getCellColor("A10");
        String b10WithConditionEqualsToZero = spreadsheetPage.getCellColor("B10");
        String c10WithConditionGreaterOfZero = spreadsheetPage.getCellColor("C10");
        String d10WithConditionLowerOfZero = spreadsheetPage.getCellColor("D10");
        assertEquals(DIFFERENT_FROM_ZERO_CONDITION_COLOR, a10WithConditionEqualsToOne);
        assertEquals(FALSE_CONDITION_COLOR, b10WithConditionEqualsToZero);
        assertEquals(DIFFERENT_FROM_ZERO_CONDITION_COLOR, c10WithConditionGreaterOfZero);
        assertEquals(DIFFERENT_FROM_ZERO_CONDITION_COLOR, d10WithConditionLowerOfZero);
    }
    
    private class BorderState {
        public boolean left;
        public boolean right;
        public boolean top;
        public boolean bottom;
        
        BorderState(boolean left, boolean top, boolean right, boolean bottom) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }
    }

    // these maps represent the initial state as in xlsx
    final private Map<String, BorderState> cellBorders = new LinkedHashMap<String, BorderState>() {{
        put("C14", new BorderState(true, true, true, true));
        put("C15", new BorderState(false, true, false, false));
        put("C16", new BorderState(false, false, true, false));
        put("D14", new BorderState(true, false, false, false));
        put("D15", new BorderState(false, false, false, true));
        put("D16", new BorderState(true, true, true, true));
        put("E14", new BorderState(false, false, false, false));
        put("E15", new BorderState(false, false, false, false));
        put("E16", new BorderState(true, false, false, false));
    }};

    @Test
    public void formattingRulesInRedRegion_assertBorders() {

        assertAllBorders();

        testD16changeToBorderless();

        testD14changeToBorderless();
    }

    private void assertAllBorders() {
        for (String cell : cellBorders.keySet()) {
            assertBorderState(cell, cellBorders.get(cell));
        }
    }

    private void assertBorderState(String cell, BorderState borderState) {
        final String messageTop =
            "Top border for cell " + cell + " does not match";
        final String messageBottom =
            "Bottom border for cell " + cell + " does not match";
        final String messageLeft =
            "Left border for cell " + cell + " does not match";
        final String messageRight =
            "Right border for cell " + cell + " does not match";
        
        assertEquals(messageTop, borderState.top, topBorderState(cell));
        assertEquals(messageBottom, borderState.bottom, bottomBorderState(cell));
        assertEquals(messageLeft, borderState.left, leftBorderState(cell));
        assertEquals(messageRight, borderState.right, rightBorderState(cell));
    }

    private void testD14changeToBorderless() {
        spreadsheetPage.setCellValue("D14", "borderless");

        cellBorders.get("D14").left = false;
        cellBorders.get("C14").right = false;
        
        assertAllBorders();
    }

    private void testD16changeToBorderless() {
        spreadsheetPage.setCellValue("D16", "borderless");

        cellBorders.put("D16", new BorderState(false, false, false, false));
        cellBorders.get("C16").right = false;
        cellBorders.get("E16").left = false;
        cellBorders.get("D15").bottom = false;

        assertAllBorders();
    }


    private boolean topBorderState(String cellAddress) {
        return bottomBorderState(getAboveCellAddress(cellAddress));
    }

    private boolean rightBorderState(String cellAddress) {
        return spreadsheetPage.hasBorderRight(cellAddress);
    }

    private boolean bottomBorderState(String cellAddress) {
        return spreadsheetPage.hasBorderBottom(cellAddress);
    }

    private boolean leftBorderState(String cellAddress) {
        return rightBorderState(getLeftCellAddress(cellAddress));
    }

    private String getAboveCellAddress(String cellAddress) {
        int row = Integer
            .parseInt(cellAddress.substring(1, cellAddress.length()));
        int aboveRow = row - 1;
        return String.valueOf(cellAddress.charAt(0)) + aboveRow;
    }

    private String getLeftCellAddress(String cellAddress) {
        char column = cellAddress.charAt(0);
        char leftColumn = (char) (column - 1);
        return String.valueOf(leftColumn) + Integer
            .parseInt(cellAddress.substring(1, cellAddress.length()));
    }
}
