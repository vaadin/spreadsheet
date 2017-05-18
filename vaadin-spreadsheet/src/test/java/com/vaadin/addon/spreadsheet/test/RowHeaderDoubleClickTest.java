package com.vaadin.addon.spreadsheet.test;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;

import com.vaadin.addon.spreadsheet.elements.SpreadsheetElement;
import com.vaadin.addon.spreadsheet.test.fixtures.TestFixtures;
import com.vaadin.addon.spreadsheet.test.pageobjects.SpreadsheetPage;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.annotations.RunLocally;
import com.vaadin.testbench.parallel.Browser;

@RunLocally(Browser.PHANTOMJS)
public class RowHeaderDoubleClickTest extends AbstractSpreadsheetTestCase {

    @Test
    public void loadFixture_doubleclickOnRowHeader_rowHeaderDoubleClickEventFired() {
        final SpreadsheetPage spreadsheet = headerPage.createNewSpreadsheet();
        headerPage.loadTestFixture(TestFixtures.RowHeaderDoubleClick);

        TestBenchElement resizeHandle = $(SpreadsheetElement.class).first()
            .getRowHeader(12).getResizeHandle();
        final Actions action = new Actions(driver);
        action.doubleClick(resizeHandle).build().perform();
        resizeHandle.doubleClick();
        assertEquals("DoubleClicked on row header",
            spreadsheet.getCellAt(2, 2).getValue());
    }

    @Override
    public List<DesiredCapabilities> getBrowsersToTest() {
        return getBrowsersExcludingPhantomJS();
    }
}
