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
package com.vaadin.spreadsheet.charts.interactiontests;

import java.io.IOException;

import org.junit.Test;

import com.vaadin.addon.spreadsheet.elements.SpreadsheetElement;
import com.vaadin.addon.spreadsheet.test.AbstractSpreadsheetTestCase;
import com.vaadin.addon.spreadsheet.test.demoapps.SpreadsheetChartsDemoUI;

public class ScrollTBTest extends AbstractSpreadsheetTestCase {

    @Test
    public void overlayScroll_haveBigOverlay_overlayStaysOnPageWhenScroll()
            throws IOException {
        headerPage.loadFile("Tagetik11.xlsx", this);
        SpreadsheetElement spreadsheetElement = $(SpreadsheetElement.class)
                .first();
        spreadsheetElement.scroll(850);
        compareScreen("overlayScroll");
    }

    @Override
    public Class<?> getUIClass() {
        return SpreadsheetChartsDemoUI.class;
    }
}
