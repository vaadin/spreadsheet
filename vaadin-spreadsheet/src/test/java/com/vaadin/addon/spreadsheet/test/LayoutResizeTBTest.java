package com.vaadin.addon.spreadsheet.test;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.vaadin.addon.spreadsheet.elements.SpreadsheetElement;
import com.vaadin.addon.spreadsheet.test.demoapps.LayoutSpreadsheetUI;
import com.vaadin.addon.spreadsheet.test.tb3.MultiBrowserTest;
import com.vaadin.testbench.elements.ButtonElement;

/**
 * LayoutResizeTBTest
 */
public class LayoutResizeTBTest extends MultiBrowserTest {

    /**
     * setUp
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        openTestURL();
    }

    @Override
    protected Class<?> getUIClass() {
        return LayoutSpreadsheetUI.class;
    }

    /**
     * parentLayoutSizeUndefined_addSpreadsheet_hadDefaultSize
     * @throws IOException
     */
    @Test
    public void parentLayoutSizeUndefined_addSpreadsheet_hadDefaultSize() throws IOException {
        final SpreadsheetElement spreadsheet = $(SpreadsheetElement.class).first();
        spreadsheet.scroll(500);
        $(ButtonElement.class).caption("Hide").first().click();
        compareScreen("layoutResize");
    }
}
