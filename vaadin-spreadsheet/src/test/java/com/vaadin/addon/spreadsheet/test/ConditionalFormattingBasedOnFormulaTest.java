package com.vaadin.addon.spreadsheet.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.HashMap;
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
    
    @Test
    @Ignore
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
    final private Map<String, BorderState> cellBorders = new HashMap<String, BorderState>() {{
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
    public void formattingRulesInD14D15D16_EvaluateFormatting_assertBorders() {

        testInitialState();

//        testD16changeToBorderless();

//        testD14changedToBorderless();
    }

    private void testInitialState() {
        
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
        
        assertTrue(messageTop, topBorderState(cell) == borderState.top);
        assertTrue(messageBottom, bottomBorderState(cell) == borderState.bottom);
        assertTrue(messageLeft, leftBorderState(cell) == borderState.left);
        assertTrue(messageRight,rightBorderState(cell) == borderState.right);
    }

//    private void testD14changedToBorderless() {
//        Map<String, BorderState> cellStates;
//        Map<String, Map<String, BorderState>> neighbourCellsStates;
//        spreadsheetPage.setCellValue("D14", "borderless");
//        matchCondition.put("D14", true);
//        cellStates = computeCellBorderStates(cells,
//            conditionalFormattingBorders, matchCondition);
//        neighbourCellsStates = computeNeighbourCellStates(cells, cellStates);
//
//        for (String cell : cells) {
//            assertBorderStates(cell, matchCondition.get(cell),
//                cellStates.get(cell), neighbourCellsStates.get(cell));
//        }
//    }
//
//    private void testD16changeToBorderless() {
//        Map<String, BorderState> cellStates;
//        Map<String, Map<String, BorderState>> neighbourCellsStates;
//        spreadsheetPage.setCellValue("D16", "borderless");
//        matchCondition.put("D16", false);
//        cellStates = computeCellBorderStates(cells,
//            conditionalFormattingBorders, matchCondition);
//        neighbourCellsStates = computeNeighbourCellStates(cells, cellStates);
//
//        for (String cell : cells) {
//            assertBorderStates(cell, matchCondition.get(cell),
//                cellStates.get(cell), neighbourCellsStates.get(cell));
//        }
//    }
//
//    /// Check Cell States
//
//    /**
//     * Check method that compare the current and the expected cell states
//     *
//     * @param cell
//     *     Address of the cell "subject of the test"
//     * @param matchRule
//     *     Conditional formatting rule state
//     * @param cellState
//     *     Current border cell state
//     * @param borderingCellState
//     *     Current border cell states of the four neighbours of <code>cell</code>
//     */
//    private void assertBorderStates(String cell, boolean matchRule,
//        BorderState cellState, Map<String, BorderState> borderingCellState) {
//        // In the testing sheet, conditional formatting rules are growing priority
//        // going from top to bottom and from right to left
//        boolean winOnAboveCell = cellStateWinOnNeighbour(matchRule, true);
//        boolean winOnRightCell = cellStateWinOnNeighbour(matchRule, false);
//        boolean winOnBelowCell = cellStateWinOnNeighbour(matchRule, false);
//        boolean winOnLeftCell = cellStateWinOnNeighbour(matchRule, true);
//        
//        boolean expectedTopState = borderState(winOnAboveCell, cellState,
//            getAboveCellState(borderingCellState, cell));
//        boolean expectedRightState = borderState(winOnRightCell, cellState,
//            getRightCellState(borderingCellState, cell));
//        boolean expectedBottomState = borderState(winOnBelowCell, cellState,
//            getBelowCellState(borderingCellState, cell));
//        boolean expectedLeftState = borderState(winOnLeftCell, cellState,
//            getLeftCellState(borderingCellState, cell));
//
//        boolean actualTopState = topBorderState(cell);
//        boolean actualRightState = rightBorderState(cell);
//        boolean actualBottomState = bottomBorderState(cell);
//        boolean actualLeftState = leftBorderState(cell);
//
//        assertEquals(createMessage(cell, "top", expectedRightState,
//            actualRightState),
//            expectedTopState, actualTopState);     // top
//        assertEquals(createMessage(cell, "right", actualRightState,
//            expectedRightState),
//            expectedRightState, actualRightState);   // right
//        assertEquals(createMessage(cell, "bottom", actualBottomState,
//            expectedBottomState),
//            expectedBottomState, actualBottomState);  // bottom
//        assertEquals(createMessage(cell, "right", actualRightState,
//            expectedRightState),
//            expectedLeftState, actualLeftState);      // left
//    }
//
//    private String createMessage(String cell, String position, boolean actualRightState,
//        boolean expectedRightState) {
//        String actualNot = (!actualRightState) ? " not" : "";
//        String expectedNot = (!expectedRightState) ? " not" : "";
//        return "The " + position + " border of " + cell + " is" + actualNot
//            + " present, but it is" + expectedNot + " expected";
//    }

    ////// CURRENT Border State of a cell at cellAddress  
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

    ///// Give the border state of a cell neighbor
    private BorderState getAboveCellState(
        Map<String, BorderState> cellBorderStates, String cell) {
        return cellBorderStates.get(getAboveCellAddress(cell));
    }

    private BorderState getLeftCellState(
        Map<String, BorderState> cellBorderStates, String cell) {
        return cellBorderStates.get(getLeftCellAddress(cell));
    }

    private BorderState getBelowCellState(
        Map<String, BorderState> cellBorderStates, String cell) {
        return cellBorderStates.get(getBelowCellAddress(cell));
    }

    private BorderState getRightCellState(
        Map<String, BorderState> cellBorderStates, String cell) {
        return cellBorderStates.get(getRightCellAddress(cell));
    }

    //// Given the address of a cell, compute the above/right/below/left cell address
    private String getAboveCellAddress(String cellAddress) {
        int row = Integer
            .parseInt(cellAddress.substring(1, cellAddress.length()));
        int aboveRow = row - 1;
        return String.valueOf(cellAddress.charAt(0)) + aboveRow;
    }

    private String getRightCellAddress(String cellAddress) {
        char column = cellAddress.charAt(0);
        char rightColumn = (char) (column + 1);
        return String.valueOf(rightColumn) + Integer
            .parseInt(cellAddress.substring(1, cellAddress.length()));
    }

    private String getBelowCellAddress(String cellAddress) {
        int row = Integer
            .parseInt(cellAddress.substring(1, cellAddress.length()));
        int belowRow = row + 1;
        return String.valueOf(cellAddress.charAt(0)) + belowRow;
    }

    private String getLeftCellAddress(String cellAddress) {
        char column = cellAddress.charAt(0);
        char leftColumn = (char) (column - 1);
        return String.valueOf(leftColumn) + Integer
            .parseInt(cellAddress.substring(1, cellAddress.length()));
    }
}
