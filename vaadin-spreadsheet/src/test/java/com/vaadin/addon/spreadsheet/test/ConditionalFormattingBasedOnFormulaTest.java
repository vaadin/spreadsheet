package com.vaadin.addon.spreadsheet.test;

import static com.vaadin.addon.spreadsheet.test.ConditionalFormattingBasedOnFormulaTest.BorderState.BORDERED;
import static com.vaadin.addon.spreadsheet.test.ConditionalFormattingBasedOnFormulaTest.BorderState.BORDERLESS;
import static org.junit.Assert.assertEquals;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.vaadin.addon.spreadsheet.test.pageobjects.SpreadsheetPage;

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

    @Test
    public void loadSpreadsheetWithConditionalFormattingInA2_MakeConditionFalse_CellA2FilledWhite() {
        spreadsheetPage.setCellValue("A1", VALUE);
        spreadsheetPage.setCellValue("A2", "Not"+VALUE);
        assertEquals(FALSE_CONDITION_COLOR, spreadsheetPage.getCellColor("A2"));
    }

    @Test
    public void loadSpreadsheetWithConditionalFormattingInA2_MakeConditionTrue_CellA2FilledRed() {
        spreadsheetPage.setCellValue("A1", VALUE);
        spreadsheetPage.setCellValue("A2", VALUE);
        assertEquals(TRUE_CONDITION_COLOR, spreadsheetPage.getCellColor("A2"));
    }
    
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
    
    enum BorderState {
        BORDERED(true),
        BORDERLESS(false),
        DEFAULT_BORDER_STATE(false);

        boolean hasBorder;

        BorderState(boolean bordered) {
            this.hasBorder = bordered;
        }
    }

    final String[] cells = { "D14", "D15", "D16", "C14" };

    final Map<String, BorderState> borderRuleValues = new HashMap<String, BorderState>() {{
        put("D14", BORDERLESS);
        put("D15", BORDERLESS);
        put("D16", BORDERED);
        put("C14", BORDERED);
    }};

    final Map<String, Boolean> matchRuleValues = new HashMap<String, Boolean>() {{
        put("D14", false);
        put("D15", true);
        put("D16", true);
        put("C14", true);
    }};

    @Test
    public void formattingRulesInD14D15D16_EvaluateFormatting_assertBorders() {

        testInitialState();

        testD16changeToBorderless();

        testD14changedToBorderless();
    }

    private void testInitialState() {
        Map<String, BorderState> cellStates = computeCellBorderStates(cells,
            borderRuleValues, matchRuleValues);

        Map<String, Map<String, BorderState>> neighbourCellsStates = computeNeighbourCellStates(
            cells, cellStates);

        for (String cell : cells) {
            assertBorderStates(cell, matchRuleValues.get(cell),
                cellStates.get(cell), neighbourCellsStates.get(cell));
        }
    }

    private void testD14changedToBorderless() {
        Map<String, BorderState> cellStates;
        Map<String, Map<String, BorderState>> neighbourCellsStates;
        spreadsheetPage.setCellValue("D14", "borderless");
        matchRuleValues.put("D14", true);
        cellStates = computeCellBorderStates(cells, borderRuleValues,
            matchRuleValues);
        neighbourCellsStates = computeNeighbourCellStates(cells, cellStates);

        for (String cell : cells) {
            assertBorderStates(cell, matchRuleValues.get(cell),
                cellStates.get(cell), neighbourCellsStates.get(cell));
        }
    }

    private void testD16changeToBorderless() {
        Map<String, BorderState> cellStates;
        Map<String, Map<String, BorderState>> neighbourCellsStates;
        spreadsheetPage.setCellValue("D16", "borderless");
        matchRuleValues.put("D16", false);
        cellStates = computeCellBorderStates(cells, borderRuleValues,
            matchRuleValues);
        neighbourCellsStates = computeNeighbourCellStates(cells, cellStates);

        for (String cell : cells) {
            assertBorderStates(cell, matchRuleValues.get(cell),
                cellStates.get(cell), neighbourCellsStates.get(cell));
        }
    }

    /// Check Cell States

    /**
     * Check method that compare the current and the expected cell states
     *
     * @param cell
     *     Address of the cell "subject of the test"
     * @param matchRule
     *     Conditional formatting rule state
     * @param cellState
     *     Current border cell state
     * @param borderingCellState
     *     Current border cell states of the four neighbours of <code>cell</code>
     */
    private void assertBorderStates(String cell, boolean matchRule,
        BorderState cellState, Map<String, BorderState> borderingCellState) {
        // In the testing sheet, conditional formatting rules are growing priority
        // going from top to bottom and from right to left
        boolean winOnAboveCell = cellStateWinOnNeighbour(matchRule, true);
        boolean winOnRightCell = cellStateWinOnNeighbour(matchRule, false);
        boolean winOnBelowCell = cellStateWinOnNeighbour(matchRule, false);
        boolean winOnLeftCell = cellStateWinOnNeighbour(matchRule, true);
        
        boolean expectedTopState = borderState(winOnAboveCell, cellState,
            getAboveCellState(borderingCellState, cell));
        boolean expectedRightState = borderState(winOnRightCell, cellState,
            getRightCellState(borderingCellState, cell));
        boolean expectedBottomState = borderState(winOnBelowCell, cellState,
            getBelowCellState(borderingCellState, cell));
        boolean expectedLeftState = borderState(winOnLeftCell, cellState,
            getLeftCellState(borderingCellState, cell));

        boolean actualTopState = topBorderState(cell);
        boolean actualRightState = rightBorderState(cell);
        boolean actualBottomState = bottomBorderState(cell);
        boolean actualLeftState = leftBorderState(cell);

        assertEquals(createMessage(cell, "top", expectedRightState,
            actualRightState),
            expectedTopState, actualTopState);     // top
        assertEquals(createMessage(cell, "right", actualRightState,
            expectedRightState),
            expectedRightState, actualRightState);   // right
        assertEquals(createMessage(cell, "bottom", actualBottomState,
            expectedBottomState),
            expectedBottomState, actualBottomState);  // bottom
        assertEquals(createMessage(cell, "right", actualRightState,
            expectedRightState),
            expectedLeftState, actualLeftState);      // left
    }

    private String createMessage(String cell, String position, boolean actualRightState,
        boolean expectedRightState) {
        String actualNot = (!actualRightState) ? " not" : "";
        String expectedNot = (!expectedRightState) ? " not" : "";
        return "The "+position+" border of "+cell+" is"+actualNot+" present, but it is"+expectedNot+" expected";
    }

    private boolean cellStateWinOnNeighbour(boolean match,
        boolean hasMorePriority) {
        return match && hasMorePriority;
    }

    /// Pre-Elaboration States

    /**
     * Return the border states of neighbours (of the test subject cells)
     *
     * @param cells
     *     Subjects of the test
     * @param cellBorderStates
     *     Border states of the cells: "cell address" -> "border status"
     * @return Border states of the neighbours: "cell address" -> "states of neighbours of cell"
     * (neighbour address -> border status)
     */
    private Map<String, Map<String, BorderState>> computeNeighbourCellStates(
        String[] cells, Map<String, BorderState> cellBorderStates) {
        Map<String, Map<String, BorderState>> borderingCellsStates = new HashMap<String, Map<String, BorderState>>();
        for (String sut : cells) {
            Map<String, BorderState> sutBorderingCellsStates = new HashMap<String, BorderState>();
            sutBorderingCellsStates.put(getAboveCellAddress(sut),
                cellBorderState(cellBorderStates, getAboveCellAddress(sut)));
            sutBorderingCellsStates.put(getRightCellAddress(sut),
                cellBorderState(cellBorderStates, getRightCellAddress(sut)));
            sutBorderingCellsStates.put(getBelowCellAddress(sut),
                cellBorderState(cellBorderStates, getBelowCellAddress(sut)));
            sutBorderingCellsStates.put(getLeftCellAddress(sut),
                cellBorderState(cellBorderStates, getLeftCellAddress(sut)));
            borderingCellsStates.put(sut, sutBorderingCellsStates);
        }
        return borderingCellsStates;
    }

    /**
     * Compute the border state of the cell at <code>cellAddress</code>, discriminating
     * between the cells subject of the test and others (with default border state)
     *
     * @param cellBorderStates
     *     border states of the cells, that are subjects of the test
     * @param cellAddress
     *     current cell
     * @return border state of cell at <code>cellAddress</code>
     */
    private BorderState cellBorderState(
        Map<String, BorderState> cellBorderStates, String cellAddress) {
        BorderState state = cellBorderStates.get(cellAddress);
        if (state != null)
            return state;
        else
            return BorderState.DEFAULT_BORDER_STATE;
    }

    /**
     * Compute the cell border states of <code>cells</code>
     * starting from the cell border states applied with the conditional formatting rule (BORDERLESS or BORDERED)
     * and the conditional formatting rule states (true if the rule occurs, neither false)
     *
     * @param cells
     *     <code>cells</code> subjects of the test
     * @param borderRuleValues
     *     cell border states if the rule match
     * @param matchRuleValues
     *     conditional formatting rule states
     * @return cell border states of <code>cells</code>
     */
    private Map<String, BorderState> computeCellBorderStates(String[] cells,
        Map<String, BorderState> borderRuleValues,
        Map<String, Boolean> matchRuleValues) {
        Map<String, BorderState> cellBorderStates = new HashMap<String, BorderState>();

        for (String c : cells) {
            if (matchRuleValues.get(c)) {
                cellBorderStates.put(c, borderRuleValues.get(c));
            } else {
                cellBorderStates.put(c, BorderState.DEFAULT_BORDER_STATE);
            }
        }

        return cellBorderStates;
    }

    /// EXPECTED Border State of a cell with specific properties
    // (border priority over neighbour, border state , neighbour border state) 

    /**
     * Compute the expected value of a specific border (top or right or bottom or left) of a cell
     *
     * @param hasMorePriorityOfNeighbour
     *     true if cell has more priority of the (above or right or below or left) neighbor
     * @param cellState
     *     cell border state
     * @param neighbourState
     *     border state of the (above or right or below or left) neighbor
     * @return true if the border is visible, false neither
     */
    private boolean borderState(boolean hasMorePriorityOfNeighbour,
        BorderState cellState, BorderState neighbourState) {
        if (neighbourState.equals(BorderState.DEFAULT_BORDER_STATE)) {
            return cellState.hasBorder;
        } else {
            return (hasMorePriorityOfNeighbour) ?
                cellState.hasBorder :
                neighbourState.hasBorder;
        }
    }

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
