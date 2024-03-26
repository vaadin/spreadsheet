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

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;

import com.vaadin.addon.spreadsheet.elements.SpreadsheetElement;

/**
 * Test for setting locale after loading a spreadsheet that has a frozen column.
 * Ticket #17633.
 */
public class FreezePaneLocaleUITest extends NoScreenshotTBTest {

    @Test
    public void loadSpreadsheetWithFrozenColumns_setLocaleAfterLoad_SpreadsheetLoadedCorrectly()
            throws URISyntaxException, IOException {
        // the UI does the work for this, we just have to verify it loaded
        openTestURL();

        assertTrue("Spreadsheet did not load correctly",
                $(SpreadsheetElement.class).exists());
    }

    @Override
    protected Class<?> getUIClass() {
        return FreezePaneLocaleUI.class;
    }
}
