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

import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.vaadin.addon.spreadsheet.elements.SheetCellElement;
import com.vaadin.addon.spreadsheet.elements.SpreadsheetElement;
import com.vaadin.addon.spreadsheet.test.fixtures.TestFixtures;
import com.vaadin.addon.spreadsheet.test.testutil.ContextMenuHelper;
import com.vaadin.addon.spreadsheet.test.testutil.ModifierController;
import com.vaadin.addon.spreadsheet.test.testutil.SheetController;
import com.vaadin.testbench.annotations.BrowserConfiguration;

public class ContextMenuTest extends AbstractSpreadsheetTestCase {

    private ContextMenuHelper contextMenu;
    private SheetController ctrl;
    private ModifierController shift;

    @Override
    @BrowserConfiguration
    public List<DesiredCapabilities> getBrowsersToTest() {
        // PhantomJS doesn't support right-click
        return getBrowsersExcludingPhantomJS();
    }

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        headerPage.createNewSpreadsheet();
        contextMenu = new ContextMenuHelper(driver);
        ctrl = new ModifierController(driver, Keys.CONTROL,
                testBench(getDriver()), getDesiredCapabilities());
        shift = new ModifierController(driver, Keys.SHIFT,
                testBench(getDriver()), getDesiredCapabilities());
    }

    @Test
    public void testSingleCell() {
        headerPage.loadTestFixture(TestFixtures.Action);
        SheetCellElement b2 = $(SpreadsheetElement.class).first()
                .getCellAt("B2");
        b2.click();

        b2.contextClick();
        contextMenu.clickItem("Number");
        assertEquals("42", b2.getValue());
    }

    @Test
    @Ignore("Fails with Firefox")
    public void testMultipleCells() {
        headerPage.loadTestFixture(TestFixtures.Action);

        sheetController.selectCell("B2");
        ctrl.selectCell("C3");
        shift.selectCell("D4");

        $(SpreadsheetElement.class).first().getCellAt("C3").contextClick();
        contextMenu.clickItem("Number");

        sheetController.selectCell("A1");

        sheetController.selectCell("B2");
        SheetCellElement c3 = $(SpreadsheetElement.class).first()
                .getCellAt("C3");
        new Actions(getDriver()).keyDown(Keys.CONTROL).click(c3)
                .keyUp(Keys.CONTROL).contextClick(c3).build().perform();
        contextMenu.clickItem("Double cell values");

        sheetController.selectCell("A1");

        assertEquals("84", sheetController.getCellContent("B2"));
        assertEquals("84", sheetController.getCellContent("C3"));
        assertEquals("42", sheetController.getCellContent("C4"));
        assertEquals("42", sheetController.getCellContent("D3"));
        assertEquals("42", sheetController.getCellContent("D4"));
    }

    @Test
    @Ignore("Fails with Firefox")
    public void testHeaders() throws InterruptedException {
        headerPage.loadTestFixture(TestFixtures.Action);

        $(SpreadsheetElement.class).first().getColumnHeader(3).contextClick();
        contextMenu.clickItem("Column action");

        assertEquals("first column", sheetController.getCellContent("C3"));
        assertEquals("last column", sheetController.getCellContent("C4"));
    }
}
