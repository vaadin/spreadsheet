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

import com.vaadin.addon.spreadsheet.elements.SpreadsheetElement;
import com.vaadin.addon.spreadsheet.test.demoapps.EmptySpreadsheetUI;
import com.vaadin.addon.spreadsheet.test.tb3.MultiBrowserTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DefaultSizeTBTest extends MultiBrowserTest {

    @Before
    public void setUp() throws Exception {
        openTestURL();
    }

    @Override
    protected Class<?> getUIClass() {
        return EmptySpreadsheetUI.class;
    }

    @Test
    public void parentLayoutSizeUndefined_addSpreadsheet_hadDefaultSize() {
        final SpreadsheetElement spreadsheet = $(SpreadsheetElement.class).first();

        Assert.assertEquals(500,spreadsheet.getSize().getWidth());
        Assert.assertEquals(400,spreadsheet.getSize().getHeight());

    }
}
