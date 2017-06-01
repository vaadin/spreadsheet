package com.vaadin.addon.spreadsheet.test;

import static org.junit.Assert.assertEquals;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.vaadin.addon.spreadsheet.test.pageobjects.SpreadsheetPage;

public class NamedRangeTests extends AbstractSpreadsheetTestCase {

    private SpreadsheetPage spreadsheetPage;
    
    // named ranges defined in the xlsx
    private Map<String, String> sheet1ranges = new HashMap<String, String>() {
        {
            put("john", "G7:M16");
            put("local", "G2:H3");
            put("numbers", "C3:C9");
        }
    };

    private List<String> rangesOnSheet1 = Arrays
        .asList("", "john", "local", "noncont", "numbers", "sheet2");

    private List<String> rangesOnSheet2 = Arrays
        .asList("", "john", "noncont", "numbers", "sheet2");

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        spreadsheetPage = headerPage.loadFile("named_ranges.xlsx", this);
    }

    @Test
    public void test() throws Exception {
        assertNamedRangeSelectValues(rangesOnSheet1);

        assertExistingSheet1RangesByTyping();

        assertExistingSheet1RangesBySelecting();

        assertExistingSheet1RangesByEnteringCellRangesAndAssertNameIsCorrect();

        selectAndAssertSheet2Range();

        assertNamedRangeSelectValues(rangesOnSheet2);

        selectAndAssertJohnRange();
    }

    private void selectAndAssertJohnRange() {
        typeAndAssertNameRange("john", sheet1ranges.get("john"));

        assertEquals("Sheet1", spreadsheetPage.getSelectedSheetName());
    }

    private void assertExistingSheet1RangesByEnteringCellRangesAndAssertNameIsCorrect() {
        for (String name : sheet1ranges.keySet()) {
            typeCellRangeAndAssertNameRange(sheet1ranges.get(name), name);
        }
    }

    private void assertNamedRangeSelectValues(List<String> expectedNamedRanges) {
        final List<String> actualNamedRanges = spreadsheetPage.getNamedRanges();

        assertEquals(expectedNamedRanges, actualNamedRanges);
    }

    private void selectAndAssertSheet2Range() {
        typeAndAssertNameRange("sheet2", "B3:D9");
        
        assertEquals("Sheet2", spreadsheetPage.getSelectedSheetName());
    }

    private void assertExistingSheet1RangesByTyping() {
        for (String name : sheet1ranges.keySet()) {
            typeAndAssertNameRange(name, sheet1ranges.get(name));
        }
    }

    private void assertExistingSheet1RangesBySelecting() {
        for (String name : sheet1ranges.keySet()) {
            selectAndAssertNameRange(name, sheet1ranges.get(name));
        }
    }

    private void typeAndAssertNameRange(String name, String expected) {
        
        spreadsheetPage.setAddressFieldValue(name);

        assertSelectedRange(name, expected);
    }

    private void typeCellRangeAndAssertNameRange(String cellRange, String name) {

        spreadsheetPage.setAddressFieldValue(cellRange);

        assertSelectedRange(name, cellRange);
    }

    private void selectAndAssertNameRange(String name, String expected) {

        spreadsheetPage.selectNamedRange(name);

        assertSelectedRange(name, expected);
    }

    private void assertSelectedRange(String name, String expected) {
        
        String selection = spreadsheetPage.getSelectionFormula();

        assertEquals("Wrong selection for range " + name, expected, selection);

        assertEquals("Wrong address field for name", name,
            spreadsheetPage.getAddressFieldValue());
    }

}
