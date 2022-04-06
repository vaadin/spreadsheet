package com.vaadin.flow.component.spreadsheet.test;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConditionalFormattingBasedOnFormulaIT
    extends AbstractSpreadsheetIT {

    private static final String VALUE = "'Fooooooooooooooooooooooooo";
    private static final String FALSE_CONDITION_COLOR = "rgba(255, 255, 255, 1)";
    private static final String TRUE_CONDITION_COLOR = "rgba(255, 0, 0, 1)";
    private static final String DIFFERENT_FROM_ZERO_CONDITION_COLOR = "rgba(255, 255, 0, 1)";

    @Before
    public void init() {
        String url = getBaseURL().replace(super.getBaseURL(),
                super.getBaseURL() + "/vaadin-spreadsheet");
        getDriver().get(url);

        createNewSpreadsheet();
        loadFile("conditional_formatting_with_formula_on_second_sheet.xlsx");
        selectSheetAt(1);
    }

    @Test
    public void loadSpreadsheetWithConditionalFormattingInA2_MakeConditionFalse_CellA2FilledWhite() {
        getSpreadsheet().getCellAt("A1").setValue(VALUE);
        getSpreadsheet().getCellAt("A2").setValue("Not"+VALUE);
        assertEquals(FALSE_CONDITION_COLOR, getCellColor("A2"));
    }

    @Test
    public void loadSpreadsheetWithConditionalFormattingInA2_MakeConditionTrue_CellA2FilledRed() {
        getSpreadsheet().getCellAt("A1").setValue(VALUE);
        getSpreadsheet().getCellAt("A2").setValue(VALUE);
        assertEquals(TRUE_CONDITION_COLOR, getCellColor("A2"));
    }
    
    @Test
    public void loadSpreadsheetWithConditionalFormattingRulesInRow10_EvaluateFormatting_CheckColorOfCells(){
        String a10WithConditionEqualsToOne = getCellColor("A10");
        String b10WithConditionEqualsToZero = getCellColor("B10");
        String c10WithConditionGreaterOfZero = getCellColor("C10");
        String d10WithConditionLowerOfZero = getCellColor("D10");
        assertEquals(DIFFERENT_FROM_ZERO_CONDITION_COLOR, a10WithConditionEqualsToOne);
        assertEquals(FALSE_CONDITION_COLOR, b10WithConditionEqualsToZero);
        assertEquals(DIFFERENT_FROM_ZERO_CONDITION_COLOR, c10WithConditionGreaterOfZero);
        assertEquals(DIFFERENT_FROM_ZERO_CONDITION_COLOR, d10WithConditionLowerOfZero);
    }
}