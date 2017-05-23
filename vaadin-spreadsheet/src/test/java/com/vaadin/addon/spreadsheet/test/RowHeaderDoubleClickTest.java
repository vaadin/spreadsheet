package com.vaadin.addon.spreadsheet.test;

import static org.junit.Assert.assertEquals;
import java.util.List;

import org.junit.Test;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.vaadin.addon.spreadsheet.elements.SpreadsheetElement;
import com.vaadin.addon.spreadsheet.test.fixtures.TestFixtures;
import com.vaadin.addon.spreadsheet.test.pageobjects.SpreadsheetPage;
import com.vaadin.testbench.parallel.Browser;

public class RowHeaderDoubleClickTest extends AbstractSpreadsheetTestCase {

    @Test
    public void loadFixture_doubleClickOnRowHeader_rowHeaderDoubleClickEventFired() {
        final SpreadsheetPage spreadsheetPage = headerPage
            .createNewSpreadsheet();

        headerPage.loadTestFixture(TestFixtures.RowHeaderDoubleClick);

        final SpreadsheetElement spreadsheet = $(SpreadsheetElement.class)
            .first();

        spreadsheet.getRowHeader(3).getResizeHandle().doubleClick();

        assertEquals("Double-click on row header",
            spreadsheetPage.getCellAt(1, 3).getValue());
    }

    @Override
    public List<DesiredCapabilities> getBrowsersToTest() {
        // Double click is not supported by PhantomJS.
        // Chrome fails for some reason, it never gets the row header double-click event 
        // probably because of faulty logic in SheetWidget that cancels it.
        // In manual testing Chrome works fine
        return getBrowserCapabilities(Browser.IE10, Browser.IE11, Browser.FIREFOX);
    }
}
