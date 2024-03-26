/*
 * Vaadin Spreadsheet Addon
 *
 * Copyright (C) 2012-2024 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.addon.spreadsheet.test;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.vaadin.addon.spreadsheet.elements.SpreadsheetElement;
import com.vaadin.testbench.annotations.BrowserConfiguration;
import com.vaadin.testbench.parallel.Browser;

public class FormulaTest2 extends AbstractSpreadsheetTestCase {

    private final String[] integerColumn = { "1", "2", "3", "4" };
    private final String[] mixedColumn = { "1", "3.1415", "example",
            "12-Feb-2007", "", "12/2/2007" };
    private final String[] floatColumn = { "1.11", "2.22", "3.33", "4.44",
            "5.55", "", "", "", "6.66", "7.77" };

    @Override
    @BrowserConfiguration
    public List<DesiredCapabilities> getBrowsersToTest() {
        // PhantomJS doesn't support right-click
        return getBrowserCapabilities(Browser.FIREFOX, Browser.CHROME);
    }

    SpreadsheetElement spreadSheet;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        headerPage.createNewSpreadsheet();
        spreadSheet = $(SpreadsheetElement.class).first();
    }

    @Test
    public void testGenericFormula() {
        sheetController.selectCell("A1");
        sheetController.insertColumn(integerColumn);

        sheetController.selectCell("B1");
        spreadSheet.getCellAt("B1").setValue("=SUM(A1:A4)");
        spreadSheet.getCellAt("B2").setValue("=A1+A2+A3+A4");
        spreadSheet.getCellAt("B3").setValue("=PRODUCT(A1:A4)");
        spreadSheet.getCellAt("B4").setValue("=A1*A2*A3*A4");

        assertEquals("10", sheetController.getCellContent("B1"));
        assertEquals("10", sheetController.getCellContent("B2"));
        assertEquals("24", sheetController.getCellContent("B3"));
        assertEquals("24", sheetController.getCellContent("B4"));
    }

    @Test
    public void testCount() {
        sheetController.selectCell("A1");
        sheetController.insertColumn(mixedColumn);
        sheetController.selectCell("B1");
        spreadSheet.getCellAt("B1").setValue("=COUNT(A1:A4)");
        spreadSheet.getCellAt("B2").setValue("=COUNTA(A1:A6)");

        testBench(driver).waitForVaadin();

        // Date strings must be interpreted as numeric
        assertEquals("2", spreadSheet.getCellAt("B1").getValue());
        assertEquals("5", spreadSheet.getCellAt("B2").getValue());
    }

    @Test
    public void testSubTotals() {
        spreadSheet.getCellAt("A1").setValue("10");
        spreadSheet.getCellAt("A2").setValue("20");
        spreadSheet.getCellAt("A3").setValue("=SUBTOTAL(9,A1:A2)");
        spreadSheet.getCellAt("A4").setValue("30");
        spreadSheet.getCellAt("A5").setValue("40");
        spreadSheet.getCellAt("A6").setValue("=SUBTOTAL(9,A1:A5)");
        spreadSheet.getCellAt("A7").setValue("50");

        spreadSheet.getCellAt("B1").setValue("=SUM(A1:A7)");
        spreadSheet.getCellAt("B2").setValue("=SUM(A1:A3)");
        spreadSheet.getCellAt("B3").setValue("=SUM(A1:A5)");
        spreadSheet.getCellAt("B4").setValue("=SUM(A1:A6)");

        spreadSheet.getCellAt("C1").setValue("=SUBTOTAL(9,A1:A7)");
        spreadSheet.getCellAt("C2").setValue("=SUBTOTAL(9,A1:A3)");
        spreadSheet.getCellAt("C3").setValue("=SUBTOTAL(9,A1:A5)");
        spreadSheet.getCellAt("C4").setValue("=SUBTOTAL(9,A1:A6)");

        assertEquals("280", sheetController.getCellContent("B1"));
        assertEquals("60", sheetController.getCellContent("B2"));
        assertEquals("130", sheetController.getCellContent("B3"));
        assertEquals("230", sheetController.getCellContent("B4"));

        assertEquals("150", sheetController.getCellContent("C1"));
        assertEquals("30", sheetController.getCellContent("C2"));
        assertEquals("100", sheetController.getCellContent("C3"));
        assertEquals("100", sheetController.getCellContent("C4"));
    }

    @Test
    public void testRecursiveFormulas() {
        spreadSheet.getCellAt("A1").setValue("10");
        spreadSheet.getCellAt("A2").setValue("20");
        spreadSheet.getCellAt("A3").setValue("30");

        spreadSheet.getCellAt("B1").setValue("=A1+A2");
        spreadSheet.getCellAt("B2").setValue("=B1+A3");
        spreadSheet.getCellAt("A3").setValue("30");
        assertEquals("60", spreadSheet.getCellAt("B2").getValue());

        // Change a basic cell value and check if other cells are updated.
        spreadSheet.getCellAt("A1").setValue("40");
        assertEquals("90", spreadSheet.getCellAt("B2").getValue());
    }

    @Test
    public void testFloatOperations() {
        for (int i = 0; i < floatColumn.length; i++) {
            spreadSheet.getCellAt(i + 1, 1).setValue(floatColumn[i]);
        }
        spreadSheet.getCellAt("B1").setValue("=SUM(A1:A10)");
        spreadSheet.getCellAt("B2").setValue("=COUNT(A1:A10)");
        spreadSheet.getCellAt("B3").setValue("=COUNTIF(A1:A10,\">5\")");
        spreadSheet.getCellAt("B4").setValue("=SUMIF(A1:A10,\">5\")");
        spreadSheet.getCellAt("B5").setValue("=AVERAGE(A1:A10)");
        spreadSheet.getCellAt("B6").setValue("=AVERAGE(A1:A2, A4)");

        testBench(driver).waitForVaadin();

        assertEquals("31.08", sheetController.getCellContent("B1"));
        assertEquals("7", sheetController.getCellContent("B2"));
        assertEquals("3", sheetController.getCellContent("B3"));
        assertEquals("19.98", sheetController.getCellContent("B4"));
        assertEquals("4.44", sheetController.getCellContent("B5"));
        assertEquals("2.59", sheetController.getCellContent("B6"));
    }

}
