/*
 * Vaadin Spreadsheet Addon
 *
 * Copyright (C) 2013-2025 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.addon.spreadsheet.test;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.junit.Test;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;

import com.vaadin.addon.spreadsheet.elements.SheetCellElement;
import com.vaadin.addon.spreadsheet.elements.SpreadsheetElement;
import com.vaadin.addon.spreadsheet.test.fixtures.TestFixtures;
import com.vaadin.addon.spreadsheet.test.pageobjects.SpreadsheetPage;

public class GenericTest extends AbstractSpreadsheetTestCase {

    @Test
    public void testKeyboardNavigation() {
        headerPage.createNewSpreadsheet();
        final SheetCellElement a1 = $(SpreadsheetElement.class).first()
                .getCellAt("A1");
        a1.setValue("X");

        new Actions(getDriver()).sendKeys(Keys.ARROW_RIGHT)
                .sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.ARROW_DOWN)
                .sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_LEFT)
                .sendKeys(Keys.ARROW_UP).sendKeys("Y").sendKeys(Keys.RETURN)
                .sendKeys(Keys.ENTER).build().perform();

        final SheetCellElement c2 = $(SpreadsheetElement.class).first()
                .getCellAt("C2");
        assertEquals("X", a1.getValue());
        assertEquals("Y", c2.getValue());
    }

    @Test
    public void testDates() {
        setLocale(Locale.US);
        headerPage.createNewSpreadsheet();
        final SheetCellElement a1 = $(SpreadsheetElement.class).first()
                .getCellAt("A1");
        final SheetCellElement a2 = $(SpreadsheetElement.class).first()
                .getCellAt("A2");
        final SheetCellElement b1 = $(SpreadsheetElement.class).first()
                .getCellAt("B1");
        a1.setValue("=TODAY()");
        a2.setValue("6/7/2009");
        b1.setValue("=A1+3");

        testBench(driver).waitForVaadin();
        Calendar start = new GregorianCalendar(1900, Calendar.JANUARY, 0);
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DAY_OF_YEAR, 1);
        Long today = new Long(
                (now.getTime().getTime() - start.getTime().getTime())
                        / (1000 * 60 * 60 * 24));

        assertEquals(today.toString(), a1.getValue());
        assertEquals(today + 3, Long.parseLong(b1.getValue()));
    }

    @Test
    public void numericCell_newPercentageCell_cellStaysNumeric() {
        setLocale(Locale.US);
        SpreadsheetPage spreadsheetPage = headerPage.createNewSpreadsheet();
        // need to move selection so that fill indicator is not clicked while
        // selecting A2
        spreadsheetPage.clickOnCell("F1");

        spreadsheetPage.setCellValue("A2", "19");
        assertEquals("19", spreadsheetPage.getCellValue("A2"));
        spreadsheetPage.setCellValue("A3", "19%");
        assertEquals("19.00%", spreadsheetPage.getCellValue("A3"));

        // force reload of the sheet
        spreadsheetPage.addSheet();
        spreadsheetPage.selectSheetAt(0);

        assertEquals("19", spreadsheetPage.getCellValue("A2"));
        assertEquals("19.00%", spreadsheetPage.getCellValue("A3"));
    }

    @Test
    public void percentageCell_newNumericCell_cellStaysNumeric() {
        setLocale(Locale.US);
        SpreadsheetPage spreadsheetPage = headerPage.createNewSpreadsheet();
        // need to move selection so that fill indicator is not clicked while
        // selecting A2
        spreadsheetPage.clickOnCell("F1");

        spreadsheetPage.setCellValue("A2", "19%");
        assertEquals("19.00%", spreadsheetPage.getCellValue("A2"));
        spreadsheetPage.setCellValue("A3", "19");
        assertEquals("19", spreadsheetPage.getCellValue("A3"));

        // force reload of the sheet
        spreadsheetPage.addSheet();
        spreadsheetPage.selectSheetAt(0);

        assertEquals("19.00%", spreadsheetPage.getCellValue("A2"));
        assertEquals("19", spreadsheetPage.getCellValue("A3"));

    }

    @Test
    public void testFormats() {
        setLocale(Locale.US);
        SpreadsheetPage spreadsheet = headerPage.createNewSpreadsheet();
        headerPage.loadTestFixture(TestFixtures.Formats);
        final SheetCellElement a1 = $(SpreadsheetElement.class).first()
                .getCellAt("A1");

        assertEquals("example", spreadsheet.getCellValue("B2"));
        assertEquals("example", spreadsheet.getCellValue("C2"));
        assertEquals("example", spreadsheet.getCellValue("D2"));
        assertEquals("example", spreadsheet.getCellValue("E2"));
        assertEquals("example", spreadsheet.getCellValue("F2"));

        assertEquals("38247.12269", spreadsheet.getCellValue("B3"));
        assertEquals("38247.12", spreadsheet.getCellValue("C3"));
        assertEquals("3824712.27%", spreadsheet.getCellValue("D3"));
        assertEquals("17-Sep-04", spreadsheet.getCellValue("E3"));
        assertEquals("3.82E+04", spreadsheet.getCellValue("F3"));

        assertEquals("3.1415", spreadsheet.getCellValue("B6"));
        assertEquals("3.14", spreadsheet.getCellValue("C6"));
        assertEquals("314.15%", spreadsheet.getCellValue("D6"));
        assertEquals("3-Jan-00", spreadsheet.getCellValue("E6"));
        assertEquals("3.14E+00", spreadsheet.getCellValue("F6"));
    }

    @Test
    public void testStringCellType() {
        SpreadsheetPage spreadsheet = headerPage.createNewSpreadsheet();
        headerPage.loadTestFixture(TestFixtures.Formats);
        final SheetCellElement b2 = $(SpreadsheetElement.class).first()
                .getCellAt("B2");

        b2.setValue("example");
        assertEquals("example", spreadsheet.getCellValue("B2"));

        b2.setValue("12");
        assertEquals("12", spreadsheet.getCellValue("B2"));

        b2.setValue("example 2");
        assertEquals("example 2", spreadsheet.getCellValue("B2"));
    }
}
