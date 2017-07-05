package com.vaadin.addon.spreadsheet.test;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.vaadin.addon.spreadsheet.elements.SpreadsheetElement;
import com.vaadin.testbench.By;
import com.vaadin.testbench.parallel.Browser;

/**
 * The issue we are trying to test has a race condition: when you scroll left and 
 * then back right, if a cell element is attached before its clientWidth is 
 * measured, the cell shows fine, otherwise it will display ### for needsMeasure
 * cell, because clientWidth is 0 and Cell.java#updateInnerText thinks the 
 * content doesn't fit.
 * 
 * Because it's a race condition, it cannot be tested reliably, 
 * we are doing the best here in hope that we catch an issue if there is one.
 */
public class WrongHashesOnScrollTest extends AbstractSpreadsheetTestCase {

    @Test
    public void openSpreadsheet_scrollLeftAndRight_thereAreNoHashes()
        throws Exception {
        
        headerPage.loadFile("wrong_hashes.xlsx", this);

        final SpreadsheetElement element = $(SpreadsheetElement.class).first();

        element.scroll(250);

        element.scrollLeft(1600);

        Thread.sleep(500);

        element.scrollLeft(0);

        Thread.sleep(500);

        final List<WebElement> elements = element
            .findElements(By.cssSelector(".cell"));

        for (WebElement cell : elements) {
            Assert.assertNotEquals("Cell with class " + cell.getAttribute("class") + " fails",
                "###", cell.getText());
        }
    }

    @Override
    public List<DesiredCapabilities> getBrowsersToTest() {
        return getBrowserCapabilities(Browser.PHANTOMJS);
    }
}
