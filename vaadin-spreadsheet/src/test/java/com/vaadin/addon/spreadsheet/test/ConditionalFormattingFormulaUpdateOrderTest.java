package com.vaadin.addon.spreadsheet.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import com.vaadin.addon.spreadsheet.test.pageobjects.SpreadsheetPage;

public class ConditionalFormattingFormulaUpdateOrderTest
    extends AbstractSpreadsheetTestCase {

    private static final String FALSE_CONDITION_COLOR = "rgba(255, 255, 255, 1)";
    private static final String TRUE_CONDITION_COLOR = "rgba(255, 199, 206, 1)";

    private SpreadsheetPage spreadsheetPage;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        spreadsheetPage = headerPage.loadFile(
            "conditional.formatting.-.formula.cells.xlsx", this);
        spreadsheetPage.selectSheetAt(0);
    }

    @Test
    public void loadSpreadsheetWithConditionalFormatting_MakeConditionTrue_CellB1FilledRed() {
        spreadsheetPage.setCellValue("A1", "6");
        assertEquals(TRUE_CONDITION_COLOR, spreadsheetPage.getCellColor("B1"));
    }
    
}
