package com.vaadin.addon.spreadsheet.test;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import com.vaadin.addon.spreadsheet.test.pageobjects.SpreadsheetPage;
import com.vaadin.testbench.annotations.RunLocally;
import com.vaadin.testbench.parallel.Browser;

@RunLocally(Browser.PHANTOMJS)
public class NamedRangeTests extends AbstractSpreadsheetTestCase {

    @Test
    public void test() throws Exception {
        final SpreadsheetPage spreadsheetPage = headerPage.loadFile(
                "named_ranges.xlsx", this);

        spreadsheetPage.setAddressFieldValue("john");
        
        waitUntil(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver arg0) {
                return !spreadsheetPage.getSelectionFormula().equals("A1");
            }

            @Override
            public String toString() {
                return "Selection to be updated";
            }
        });
        
        String selection = spreadsheetPage.getSelectionFormula();

        Assert.assertEquals("G7:M16", selection);
    }
}
