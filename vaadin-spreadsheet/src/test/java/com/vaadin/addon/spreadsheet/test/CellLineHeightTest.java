package com.vaadin.addon.spreadsheet.test;

import org.junit.Test;

public class CellLineHeightTest extends AbstractSpreadsheetTestCase {

    @Test
    public void cellLineHeightSetViaCSS_sheetHasCellsWithWrappedText_textDoesNotOverflowToOtherCells()
            throws Exception {
        headerPage.loadFile("cell_line_height.xlsx", this);

        compareScreen("cell_line_height_test");
    }
}
