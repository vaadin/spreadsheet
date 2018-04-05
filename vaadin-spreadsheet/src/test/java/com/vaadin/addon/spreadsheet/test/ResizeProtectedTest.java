package com.vaadin.addon.spreadsheet.test;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.vaadin.addon.spreadsheet.elements.SheetHeaderElement;
import com.vaadin.addon.spreadsheet.elements.SpreadsheetElement;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.parallel.Browser;

/**
 * Tests for number formatting
 *
 */
public class ResizeProtectedTest extends AbstractSpreadsheetTestCase {

    @Test
    public void resizing_protectedSheet_columnResizeFails() throws Exception {
        loadPage("multiple_sheets_protected.xlsx");
        SpreadsheetElement spreadsheet = $(SpreadsheetElement.class).first();
        double originalWidth = spreadsheet.getCellAt(2, 2).getSize()
                .getWidth();

        TestBenchElement resizeHandle = $(SpreadsheetElement.class).first()
                .getColumnHeader(2).getResizeHandle();
        SheetHeaderElement target = $(SpreadsheetElement.class).first()
                .getColumnHeader(4);

        new Actions(driver).dragAndDrop(resizeHandle, target).perform();

        double newWidth = spreadsheet.getCellAt(2, 2).getSize().getWidth();

        Assert.assertTrue(String.format(
                "Width changed when it shouldn't have. Was: %s, now: %s.",
                originalWidth, newWidth), originalWidth == newWidth);
    }

    @Test
    public void resizing_protectedSheetWithFormatColumnsEnabled_columnResizeSuccessful()
            throws Exception {
        loadPage("protected_format_columns.xlsx");

        SpreadsheetElement spreadsheet = $(SpreadsheetElement.class).first();
        final double originalWidth = spreadsheet.getCellAt(2, 2).getSize()
                .getWidth();

        TestBenchElement resizeHandle = spreadsheet.getColumnHeader(2)
                .getResizeHandle();
        SheetHeaderElement target = spreadsheet.getColumnHeader(4);

        new Actions(driver).dragAndDrop(resizeHandle, target).perform();

        double newWidth = spreadsheet.getCellAt(2, 2).getSize().getWidth();

        assertInRange(2.5 * originalWidth, newWidth, 3.5 * originalWidth);
    }

    @Override
    protected void assertInRange(double from, double value, double to) {
        Assert.assertTrue("Value [" + value + "] is not in range: [" + from
                + " - " + to + "]", value >= from && value <= to);
    }

    @Override
    public List<DesiredCapabilities> getBrowsersToTest() {
        // ResizeHandle double click and dragging not working in phantomJS
        List<DesiredCapabilities> result = super.getBrowsersToTest();
        result.remove(Browser.PHANTOMJS.getDesiredCapabilities());
        return result;
    }
}
