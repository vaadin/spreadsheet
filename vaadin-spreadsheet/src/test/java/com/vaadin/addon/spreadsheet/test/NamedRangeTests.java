package com.vaadin.addon.spreadsheet.test;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import com.vaadin.addon.spreadsheet.test.pageobjects.SpreadsheetPage;
import com.vaadin.testbench.annotations.RunLocally;
import com.vaadin.testbench.parallel.Browser;

@RunLocally(Browser.FIREFOX)
public class NamedRangeTests extends AbstractSpreadsheetTestCase {

    @Test
    public void test() throws Exception {
        final SpreadsheetPage spreadsheetPage = headerPage.loadFile(
                "named_ranges.xlsx", this);

        spreadsheetPage.setAddressFieldValue("john");
        
        Thread.sleep(10000);

        waitUntil(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver arg0) {
                return spreadsheetPage.getSelectionFormula().equals("A1");
            }

            @Override
            public String toString() {
                return "Selection to be updated";
            }
        });
        
        String selection = spreadsheetPage.getSelectionFormula();

        System.out.println(selection);

//        assertEquals("File opened", spreadsheetPage.getCellValue("A1"));
        Assert.fail();
    }
}
