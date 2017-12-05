package com.vaadin.addon.spreadsheet.test;

import java.util.List;

import org.junit.Test;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.vaadin.addon.spreadsheet.elements.SheetHeaderElement;
import com.vaadin.addon.spreadsheet.elements.SpreadsheetElement;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.parallel.Browser;

public class ResizeTest extends Test1 {

    @Test
    public void testColumnResize() {

        double originalWidth = getSize(sheetController.getCellStyle("C1",
                "width"));

        TestBenchElement resizeHandle = $(SpreadsheetElement.class).first()
                .getColumnHeader(3).getResizeHandle();
        SheetHeaderElement target = $(SpreadsheetElement.class).first()
                .getColumnHeader(5);

        new Actions(driver).dragAndDrop(resizeHandle, target).perform();

        double newWidth = getSize(sheetController.getCellStyle("C1", "width"));

        assertInRange(2.5 * originalWidth, newWidth, 3.5 * originalWidth);
    }

    @Test
    public void testRowResize() {
        sheetController.selectCell("A2");
        sheetController.selectCell("A1");

        double originalHeight = getSize(sheetController.getCellStyle("A3",
                "height"));

        new Actions(driver).dragAndDrop(
                $(SpreadsheetElement.class).first().getRowHeader(3)
                        .getResizeHandle(),
                $(SpreadsheetElement.class).first().getRowHeader(5)).perform();

        double newHeight = getSize(sheetController.getCellStyle("A3", "height"));

        assertInRange(2.3 * originalHeight, newHeight, 3.5 * originalHeight);
    }

    @Test
    public void testColumnAutoResize() {
        sheetController.putCellContent("B2", "text");
        sheetController.waitForVaadin();
        $(SpreadsheetElement.class).first().getColumnHeader(2)
                .getResizeHandle().doubleClick();
        sheetController.waitForVaadin();
        assertInRange(25, getSize(sheetController.getCellStyle("B2", "width")),
                35);

        sheetController.putCellContent("D2", "very long text inserted in D2.");
        sheetController.waitForVaadin();
        $(SpreadsheetElement.class).first().getColumnHeader(4)
                .getResizeHandle().doubleClick();
        sheetController.waitForVaadin();
        assertInRange(100,
                getSize(sheetController.getCellStyle("D2", "width")), 200);
    }

    @Override
    public List<DesiredCapabilities> getBrowsersToTest() {
        // ResizeHandle double click and dragging not working in phantomJS
        List<DesiredCapabilities> result = super.getBrowsersToTest();
        result.remove(Browser.PHANTOMJS.getDesiredCapabilities());
        return result;
    }
}
