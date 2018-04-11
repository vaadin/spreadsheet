package com.vaadin.addon.spreadsheet.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.vaadin.addon.spreadsheet.test.pageobjects.SpreadsheetPage;
import com.vaadin.testbench.parallel.Browser;

public class ConditionalFormattingOnCellDeletionTest extends AbstractSpreadsheetTestCase {

    private static final String FALSE_CONDITION_COLOR = "rgba(255, 255, 255, 1)";
    private static final String TRUE_CONDITION_COLOR = "rgba(255, 0, 0, 1)";

    private SpreadsheetPage spreadsheetPage;

    @Override
    public void setUp() throws Exception {
        loadPage("conditional_formatting_with_formula_on_second_sheet.xlsx");
        spreadsheetPage = new SpreadsheetPage(driver);
    }

    @Test
    public void conditionalFormatting_deleteCellUsedInFormula_formattingAppliedWithoutException() {
        skipBrowser("Firefox fails to delete cell value", Browser.FIREFOX);
        assertEquals(FALSE_CONDITION_COLOR, spreadsheetPage.getCellColor("A2"));

        spreadsheetPage.deleteCellValue("A2");

        assertEquals(FALSE_CONDITION_COLOR, spreadsheetPage.getCellColor("A2"));

        spreadsheetPage.deleteCellValue("A1");

        assertEquals(TRUE_CONDITION_COLOR, spreadsheetPage.getCellColor("A2"));
    }
}
