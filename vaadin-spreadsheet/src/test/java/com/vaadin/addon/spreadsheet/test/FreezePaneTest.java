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

import org.junit.Test;

import com.vaadin.addon.spreadsheet.elements.SheetHeaderElement;
import com.vaadin.addon.spreadsheet.elements.SpreadsheetElement;

public class FreezePaneTest extends AbstractSpreadsheetTestCase {

    @Test
    public void addFreezePane_verticalAndHorizontal_firstHeaderIsPlacedCorrectly()
            throws Exception {
        headerPage.createNewSpreadsheet();

        headerPage.addFreezePane();

        SpreadsheetElement spreadsheetElement = $(SpreadsheetElement.class)
                .first();
        SheetHeaderElement firstColumnHeader = spreadsheetElement
                .getColumnHeader(1);
        SheetHeaderElement firstRowHeader = spreadsheetElement.getRowHeader(1);
        assertEquals("A", firstColumnHeader.getText());
        assertEquals("0px",
                firstColumnHeader.getWrappedElement().getCssValue("left"));
        assertEquals("1", firstRowHeader.getText());
        assertEquals("0px",
                firstRowHeader.getWrappedElement().getCssValue("top"));
    }

    @Test
    public void addFreezePane_onlyVertical_firstHeaderIsPlacedCorrectly()
            throws Exception {
        headerPage.createNewSpreadsheet();

        headerPage.addFreezePane(0, 1);

        SpreadsheetElement spreadsheetElement = $(SpreadsheetElement.class)
                .first();
        SheetHeaderElement firstColumnHeader = spreadsheetElement
                .getColumnHeader(1);
        SheetHeaderElement firstRowHeader = spreadsheetElement.getRowHeader(1);
        assertEquals("A", firstColumnHeader.getText());
        assertEquals("0px",
                firstColumnHeader.getWrappedElement().getCssValue("left"));
        assertEquals("1", firstRowHeader.getText());
        assertEquals("0px",
                firstRowHeader.getWrappedElement().getCssValue("top"));
    }

    @Test
    public void addFreezePane_onlyHorizontal_firstHeaderIsPlacedCorrectly()
            throws Exception {
        headerPage.createNewSpreadsheet();

        headerPage.addFreezePane(1, 0);

        SpreadsheetElement spreadsheetElement = $(SpreadsheetElement.class)
                .first();
        SheetHeaderElement firstColumnHeader = spreadsheetElement
                .getColumnHeader(1);
        SheetHeaderElement firstRowHeader = spreadsheetElement.getRowHeader(1);
        assertEquals("A", firstColumnHeader.getText());
        assertEquals("0px",
                firstColumnHeader.getWrappedElement().getCssValue("left"));
        assertEquals("1", firstRowHeader.getText());
        assertEquals("0px",
                firstRowHeader.getWrappedElement().getCssValue("top"));
    }
}
