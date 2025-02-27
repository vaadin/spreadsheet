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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;

import com.vaadin.addon.spreadsheet.elements.SheetCellElement;
import com.vaadin.addon.spreadsheet.elements.SpreadsheetElement;

/**
 * Arrow key navigation tests.
 */
public class CellArrowKeyNavigationTest extends AbstractSpreadsheetTestCase {

    private SpreadsheetElement spreadSheet;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        headerPage.createNewSpreadsheet();
        spreadSheet = $(SpreadsheetElement.class).first();
    }

    @Test
    public void shouldNotChangeCellWhenEditingAndArrowRightKeyIsPressed() {
        final SheetCellElement b2 = spreadSheet.getCellAt("B2");
        b2.setValue("123");

        sheetController.selectCell("B2");
        new Actions(getDriver()).sendKeys(Keys.F2).build().perform(); // edit
                                                                      // mode

        new Actions(getDriver()).sendKeys(Keys.ARROW_RIGHT).build().perform();

        String selectedCell = sheetController.getSelectedCell();
        assertThat(selectedCell, is("B2"));
    }

    @Test
    public void shouldNotChangeCellWhenEditingAndArrowLeftKeyIsPressed() {
        final SheetCellElement b2 = spreadSheet.getCellAt("B2");
        b2.setValue("123");

        sheetController.selectCell("B2");
        new Actions(getDriver()).sendKeys(Keys.F2).build().perform(); // edit
                                                                      // mode

        new Actions(getDriver()).sendKeys(Keys.ARROW_LEFT).build().perform();

        String selectedCell = sheetController.getSelectedCell();
        assertThat(selectedCell, is("B2"));
    }

    @Test
    public void shouldNotChangeCellWhenEditingAndArrowUpKeyIsPressed() {
        final SheetCellElement b2 = spreadSheet.getCellAt("B2");
        b2.setValue("123");

        sheetController.selectCell("B2");
        new Actions(getDriver()).sendKeys(Keys.F2).build().perform(); // edit
                                                                      // mode

        new Actions(getDriver()).sendKeys(Keys.ARROW_UP).build().perform();

        String selectedCell = sheetController.getSelectedCell();
        assertThat(selectedCell, is("B2"));
    }

    @Test
    public void shouldNotChangeCellWhenEditingAndArrowDownKeyIsPressed() {
        final SheetCellElement b2 = spreadSheet.getCellAt("B2");
        b2.setValue("123");

        sheetController.selectCell("B2");
        new Actions(getDriver()).sendKeys(Keys.F2).build().perform(); // edit
                                                                      // mode

        new Actions(getDriver()).sendKeys(Keys.ARROW_DOWN).build().perform();

        String selectedCell = sheetController.getSelectedCell();
        assertThat(selectedCell, is("B2"));
    }

    @Test
    public void shouldNotChangeCellWhenDoubleClickEditingAndArrowRightKeyIsPressed() {
        final SheetCellElement b2 = spreadSheet.getCellAt("B2");
        b2.setValue("123");

        sheetController.selectCell("A1");
        sheetController.doubleClickCell("B2"); // edit mode

        new Actions(getDriver()).sendKeys(Keys.ARROW_RIGHT).build().perform();

        String selectedCell = sheetController.getSelectedCell();
        assertThat(selectedCell, is("B2"));
    }

    @Test
    public void shouldNotChangeCellWhenDoubleClickEditingAndArrowLeftKeyIsPressed() {
        final SheetCellElement b2 = spreadSheet.getCellAt("B2");
        b2.setValue("123");

        sheetController.selectCell("A1");
        sheetController.doubleClickCell("B2"); // edit mode

        new Actions(getDriver()).sendKeys(Keys.ARROW_LEFT).build().perform();

        String selectedCell = sheetController.getSelectedCell();
        assertThat(selectedCell, is("B2"));
    }

    @Test
    public void shouldNotChangeCellWhenDoubleClickEditingAndArrowDownKeyIsPressed() {
        final SheetCellElement b2 = spreadSheet.getCellAt("B2");
        b2.setValue("123");

        sheetController.selectCell("A1");
        sheetController.doubleClickCell("B2"); // edit mode

        new Actions(getDriver()).sendKeys(Keys.ARROW_DOWN).build().perform();

        String selectedCell = sheetController.getSelectedCell();
        assertThat(selectedCell, is("B2"));
    }

    @Test
    public void shouldNotChangeCellWhenDoubleClickEditingAndArrowUpKeyIsPressed() {
        final SheetCellElement b2 = spreadSheet.getCellAt("B2");
        b2.setValue("123");

        sheetController.selectCell("A1");
        sheetController.doubleClickCell("B2"); // edit mode

        new Actions(getDriver()).sendKeys(Keys.ARROW_UP).build().perform();

        String selectedCell = sheetController.getSelectedCell();
        assertThat(selectedCell, is("B2"));
    }

    @Test
    public void shouldSelectCellToTheRightWhenSingleClickAndArrowRightKeyIsPressed() {
        sheetController.clickCell("B2");
        new Actions(getDriver()).sendKeys(Keys.NUMPAD1).build().perform();
        new Actions(getDriver()).sendKeys(Keys.NUMPAD2).build().perform();
        new Actions(getDriver()).sendKeys(Keys.NUMPAD3).build().perform();

        new Actions(getDriver()).sendKeys(Keys.ARROW_RIGHT).build().perform();

        String selectedCell = sheetController.getSelectedCell();
        assertThat(selectedCell, is("C2"));
    }

    @Test
    public void shouldSelectCellToTheLeftWhenSingleClickAndArrowLeftKeyIsPressed() {
        sheetController.clickCell("B2");
        new Actions(getDriver()).sendKeys(Keys.NUMPAD1).build().perform();
        new Actions(getDriver()).sendKeys(Keys.NUMPAD2).build().perform();
        new Actions(getDriver()).sendKeys(Keys.NUMPAD3).build().perform();

        new Actions(getDriver()).sendKeys(Keys.ARROW_LEFT).build().perform();

        String selectedCell = sheetController.getSelectedCell();
        assertThat(selectedCell, is("A2"));
    }

    @Test
    public void shouldSelectCellToTheTopWhenSingleClickAndArrowUpKeyIsPressed() {
        sheetController.clickCell("B2");
        new Actions(getDriver()).sendKeys(Keys.NUMPAD1).build().perform();
        new Actions(getDriver()).sendKeys(Keys.NUMPAD2).build().perform();
        new Actions(getDriver()).sendKeys(Keys.NUMPAD3).build().perform();

        new Actions(getDriver()).sendKeys(Keys.ARROW_UP).build().perform();

        String selectedCell = sheetController.getSelectedCell();
        assertThat(selectedCell, is("B1"));
    }

    @Test
    public void shouldSelectCellToTheBottomWhenSingleClickAndArrowDownKeyIsPressed() {
        sheetController.clickCell("B2");
        new Actions(getDriver()).sendKeys(Keys.NUMPAD1).build().perform();
        new Actions(getDriver()).sendKeys(Keys.NUMPAD2).build().perform();
        new Actions(getDriver()).sendKeys(Keys.NUMPAD3).build().perform();

        new Actions(getDriver()).sendKeys(Keys.ARROW_DOWN).build().perform();

        String selectedCell = sheetController.getSelectedCell();
        assertThat(selectedCell, is("B3"));
    }

}
