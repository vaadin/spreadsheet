package com.vaadin.addon.spreadsheet.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

    @Test
    public void loadSpreadsheetWithConditionalFormattingCellBorderRulesInD15_EvaluateFormatting_CheckColorOfCells(){
        // D15
        assertFalse(spreadsheetPage.hasBorderBottom("C14")); // up
        assertTrue(spreadsheetPage.hasBorderBottom("D15"));  // bottom (up for D16)
        assertFalse(spreadsheetPage.hasBorderRight("D15"));  // right
        assertFalse(spreadsheetPage.hasBorderRight("C15"));  // left

        // D16
        assertTrue(spreadsheetPage.hasBorderBottom("D16"));  // bottom
        assertTrue(spreadsheetPage.hasBorderRight("D16"));  // right
        assertTrue(spreadsheetPage.hasBorderRight("C16"));  // left
    }
}
