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
import static org.junit.Assert.fail;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.interactions.Actions;

import com.vaadin.addon.spreadsheet.elements.SheetCellElement;
import com.vaadin.addon.spreadsheet.elements.SpreadsheetElement;
import com.vaadin.addon.spreadsheet.test.demoapps.LockedCellValueUI;
import com.vaadin.testbench.elements.ButtonElement;

public class LockedCellValueTest extends AbstractSpreadsheetTestCase {

    private Keys cmdCtrl = Platform.getCurrent().is(Platform.MAC) ? Keys.COMMAND
            : Keys.CONTROL;

    @Override
    protected Class<?> getUIClass() {
        return LockedCellValueUI.class;
    }

    @Test
    public void lockSheet_sendKeys_preventsEdit() {
        openTestURL();
        waitForElementPresent(By.className("v-spreadsheet"));

        SpreadsheetElement spreadsheet = $(SpreadsheetElement.class).first();
        // getCellAt is 1-based, not 0-based like spreadsheet.createCell
        SheetCellElement cellB2 = spreadsheet.getCellAt(2, 2);
        SheetCellElement cellC3 = spreadsheet.getCellAt(3, 3);
        SheetCellElement cellD4 = spreadsheet.getCellAt(4, 4);
        SheetCellElement cellE5 = spreadsheet.getCellAt(5, 5);

        // confirm initial state

        String b2value = cellB2.getText();
        assertEquals("Unexpected initial B2 value,", "B2 value", b2value);
        assertEquals("Unexpected initial C3 value,", "C3 value",
                cellC3.getText());
        assertEquals("Unexpected initial D4 value (not configured),", "",
                cellD4.getText());
        assertEquals("Unexpected initial E5 value (configured),", "",
                cellE5.getText());

        // ensure no changes happen while the entire sheet is locked

        sendKeys(b2value, cellC3);
        sendKeys(b2value, cellD4);
        sendKeys(b2value, cellE5);
        cellE5.waitForVaadin();

        assertEquals("C3 value shouldn't have changed,", "C3 value",
                cellC3.getValue());
        assertEquals("D4 value shouldn't have changed,", "", cellD4.getValue());
        assertEquals("E5 value shouldn't have changed,", "", cellE5.getValue());

        // ensure it's possible to update an editable cell within a locked sheet

        // make B2 and E5 editable
        ButtonElement button = $(ButtonElement.class).first();
        button.click();
        button.waitForVaadin();

        SheetCellElement cellE5_2 = spreadsheet.getCellAt(5, 5);
        sendKeys(b2value, cellE5_2);
        cellE5_2.waitForVaadin();

        try {
            waitUntil(driver -> b2value.equals(cellE5_2.getValue()));
        } catch (TimeoutException e) {
            fail("E5 value should be " + b2value + ", was: "
                    + cellE5_2.getValue());
        }
    }

    @Test
    public void lockSheet_ctrlC_ctrlV_preventsEdit() {
        openTestURL();
        waitForElementPresent(By.className("v-spreadsheet"));

        SpreadsheetElement spreadsheet = $(SpreadsheetElement.class).first();
        // getCellAt is 1-based, not 0-based like spreadsheet.createCell
        SheetCellElement cellB2 = spreadsheet.getCellAt(2, 2);
        SheetCellElement cellC3 = spreadsheet.getCellAt(3, 3);
        SheetCellElement cellD4 = spreadsheet.getCellAt(4, 4);
        SheetCellElement cellE5 = spreadsheet.getCellAt(5, 5);

        // confirm initial state

        String b2value = cellB2.getText();
        assertEquals("Unexpected initial B2 value,", "B2 value", b2value);
        assertEquals("Unexpected initial C3 value,", "C3 value",
                cellC3.getText());
        assertEquals("Unexpected initial D4 value (not configured),", "",
                cellD4.getText());
        assertEquals("Unexpected initial E5 value (configured),", "",
                cellE5.getText());

        // ensure no changes happen while the entire sheet is locked

        ctrlC(cellB2);
        ctrlV(cellC3);
        ctrlV(cellD4);
        ctrlV(cellE5);

        assertEquals("C3 value shouldn't have changed,", "C3 value",
                cellC3.getValue());
        assertEquals("D4 value shouldn't have changed,", "", cellD4.getValue());
        assertEquals("E5 value shouldn't have changed,", "", cellE5.getValue());

        // ensure it's possible to update an editable cell within a locked sheet

        // make B2 and E5 editable
        ButtonElement button = $(ButtonElement.class).first();
        button.click();
        button.waitForVaadin();

        SheetCellElement cellB2_2 = spreadsheet.getCellAt(2, 2);
        SheetCellElement cellE5_2 = spreadsheet.getCellAt(5, 5);
        ctrlC(cellB2_2);
        ctrlV(cellE5_2);
        cellE5_2.waitForVaadin();

        try {
            waitUntil(driver -> b2value.equals(cellE5_2.getValue()));
        } catch (TimeoutException e) {
            fail("E5 value should be " + b2value + ", was: "
                    + cellE5_2.getValue());
        }
    }

    private void sendKeys(String value, SheetCellElement target) {
        // target.sendKeys(value) does not work even without lock, use actions
        new Actions(getDriver()).moveToElement(target).click().sendKeys(value)
                .perform();
    }

    private void ctrlC(SheetCellElement target) {
        new Actions(getDriver()).moveToElement(target).click().keyDown(cmdCtrl)
                .sendKeys("c").keyUp(cmdCtrl).perform();
    }

    private void ctrlV(SheetCellElement target) {
        new Actions(getDriver()).moveToElement(target).click().keyDown(cmdCtrl)
                .sendKeys("v").keyUp(cmdCtrl).perform();
    }
}
