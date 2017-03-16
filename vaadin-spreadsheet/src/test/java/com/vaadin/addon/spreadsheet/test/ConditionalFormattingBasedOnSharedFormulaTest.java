package com.vaadin.addon.spreadsheet.test;

import static org.junit.Assert.assertEquals;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.google.common.collect.Sets;
import com.vaadin.addon.spreadsheet.elements.SpreadsheetElement;

public class ConditionalFormattingBasedOnSharedFormulaTest
    extends AbstractSpreadsheetTestCase {
    
    private static final String FALSE_CONDITION_COLOR = "rgba(255, 255, 255, 1)";
    private static final String TRUE_CONDITION_COLOR = "rgba(255, 0, 0, 1)";
    
    private static final Set<String> cellWithTrueCondition = Sets.newHashSet("A2", "A3", "A4", "D1", "D3");
    private static final Set<String> cellWithFormattingCondition = getCells();

    private SpreadsheetElement spreadSheet;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        headerPage.loadFile(
            "conditional_formatting_shared_formula.xlsx", this);
        spreadSheet = $(SpreadsheetElement.class).first();
    }

    @Test
    public void loadSpreadsheetWithConditionalFormattingInA1A2_A3B4_D1G5___CheckCellFormatting()
        throws IOException {
        for (String cellAddress : cellWithTrueCondition) {
            hasRedColor(cellAddress);
        }
        for (String cellAddress : cellWithFormattingCondition) {
            if (!cellWithTrueCondition.contains(cellAddress)) {
                hasWitheColor(cellAddress);
            }
        }
    }

    private void hasRedColor(String cellAddress) {
        String cellColor = spreadSheet.getCellAt(cellAddress)
            .getCssValue("background-color");
        assertEquals(TRUE_CONDITION_COLOR, cellColor);
    }

    private void hasWitheColor(String cellAddress) {
        String cellColor = spreadSheet.getCellAt(cellAddress)
            .getCssValue("background-color");
        assertEquals(FALSE_CONDITION_COLOR, cellColor);
    }

    private static Set<String> getCells() {
        Set<String> firstCellRange = Sets.newHashSet("A1","A2");
        Set<String> secondCellRange = Sets.newHashSet("A3","A4","B3","B4");
        Set<String> thirdCellRange = new HashSet<String>();
        for (int i=1; i<=5; i++){
            for (String column : new String[]{"D","E","F","G"}){
                thirdCellRange.add(column+i);
            }
        }

        Set<String> union = new HashSet<String>(firstCellRange);
        union.addAll(secondCellRange);
        union.addAll(thirdCellRange);
        return union;
    }

}
