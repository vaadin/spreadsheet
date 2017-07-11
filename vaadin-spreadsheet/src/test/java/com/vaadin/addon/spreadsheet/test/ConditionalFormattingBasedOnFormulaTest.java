package com.vaadin.addon.spreadsheet.test;

import static org.junit.Assert.assertEquals;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;

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
        
        BorderState(BorderState toCopy) {
            this.left = toCopy.left;
            this.top = toCopy.top;
            this.right = toCopy.right;
            this.bottom = toCopy.bottom;
        }
    }

    // these maps represent the initial state as in xlsx
    final private Map<String, BorderState> initialCellBorders = Collections
        .unmodifiableMap(new HashMap() {{
        put("C14", new BorderState(true, true, true, true));
        put("C15", new BorderState(false, true, false, false));
        put("C16", new BorderState(false, false, true, false));
        put("D14", new BorderState(true, false, false, false));
        put("D15", new BorderState(false, false, false, true));
        put("D16", new BorderState(true, true, true, true));
        put("E14", new BorderState(false, false, false, false));
        put("E15", new BorderState(false, false, false, false));
        put("E16", new BorderState(true, false, false, false));
    }});

    private Map<String, BorderState> cloneInitialBorderState() {
        Map<String, BorderState> result = new HashMap<String, BorderState>();

        for (String cell : initialCellBorders.keySet()) {
            result.put(cell, new BorderState(initialCellBorders.get(cell)));
        }

        return result;
    }

    @Test
    public void formattingRulesInRedRegion_assertBorders() {

        assertAllBorders(initialCellBorders);

        testD16changeToBorderless();

        undo();
        assertAllBorders(initialCellBorders);
        
        testD14changeToBorderless();

        undo();
        assertAllBorders(initialCellBorders);

        testD15changeToDefault();

        undo();
        assertAllBorders(initialCellBorders);
    }

    private void assertAllBorders(Map<String, BorderState> expectedBorders) {
        for (String cell : expectedBorders.keySet()) {
            assertBorderState(cell, expectedBorders.get(cell));
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

        assertEquals(messageTop, borderState.top,
            spreadsheetPage.hasBorderTop(cell));

        assertEquals(messageBottom, borderState.bottom,
            spreadsheetPage.hasBorderBottom(cell));

        assertEquals(messageLeft, borderState.left,
            spreadsheetPage.hasBorderLeft(cell));

        assertEquals(messageRight, borderState.right,
            spreadsheetPage.hasBorderRight(cell));
    }

    private void testD14changeToBorderless() {
        spreadsheetPage.setCellValue("D14", "borderless");

        Map<String, BorderState> expectedCellBorders = cloneInitialBorderState();

        expectedCellBorders.get("D14").left = false;
        expectedCellBorders.get("C14").right = false;
        
        assertAllBorders(expectedCellBorders);
    }

    private void testD16changeToBorderless() {
        spreadsheetPage.setCellValue("D16", "borderless");

        Map<String, BorderState> expectedCellBorders = cloneInitialBorderState();

        expectedCellBorders
            .put("D16", new BorderState(false, false, false, false));
        expectedCellBorders.get("C16").right = false;
        expectedCellBorders.get("E16").left = false;
        expectedCellBorders.get("D15").bottom = false;

        assertAllBorders(expectedCellBorders);
    }

    private void testD15changeToDefault() {
        spreadsheetPage.setCellValue("D15", "");

        Map<String, BorderState> expectedCellBorders = cloneInitialBorderState();

        expectedCellBorders.put("D15", new BorderState(true, true, true, true));
        expectedCellBorders.get("C15").right = true;
        expectedCellBorders.get("E15").left = true;
        expectedCellBorders.get("D14").bottom = true;

        assertAllBorders(expectedCellBorders);
    }
    
    private void undo() {
        new Actions(getDriver())
            .sendKeys(Keys.chord(Keys.META, Keys.CONTROL, "z")).build().perform();
    }
}
