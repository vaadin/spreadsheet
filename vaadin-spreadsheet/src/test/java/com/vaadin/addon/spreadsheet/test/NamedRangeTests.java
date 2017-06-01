package com.vaadin.addon.spreadsheet.test;

import static org.junit.Assert.assertEquals;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.vaadin.addon.spreadsheet.test.pageobjects.SpreadsheetPage;
import com.vaadin.testbench.annotations.RunLocally;
import com.vaadin.testbench.parallel.Browser;

@RunLocally(Browser.PHANTOMJS)
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

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        spreadsheetPage = headerPage.loadFile("named_ranges.xlsx", this);
    }

    @Test
    public void test() throws Exception {
        assertExistingSheet1Ranges();
        
        selectAndAssertSheet2Range();
    }

    private void selectAndAssertSheet2Range() {
        selectAndAssertNameRange("sheet2", "B3:D9");
        
        assertEquals("Sheet2", spreadsheetPage.getSelectedSheetName());
    }

    private void assertExistingSheet1Ranges() {
        for (String name : sheet1ranges.keySet()) {
            selectAndAssertNameRange(name, sheet1ranges.get(name));
        }
    }

    private void selectAndAssertNameRange(String name, String expected) {
        
        spreadsheetPage.setAddressFieldValue(name);
        
        String selection = spreadsheetPage.getSelectionFormula();
        
        assertEquals("Named range " + name, expected, selection);
    }
}
