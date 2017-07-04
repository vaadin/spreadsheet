package com.vaadin.addon.spreadsheet.test;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.vaadin.addon.spreadsheet.elements.SheetCellElement;
import com.vaadin.addon.spreadsheet.elements.SpreadsheetElement;

public class CellOverflowTest extends AbstractSpreadsheetTestCase {

    @Test
    public void cellTextInput_longHtmlText_inputWrappedAndShownAsText()
        throws IOException {
        headerPage.createNewSpreadsheet();

        final SpreadsheetElement spreadsheetElement = $(
            SpreadsheetElement.class).first();

        final SheetCellElement a1 = spreadsheetElement.getCellAt("A1");
        final SheetCellElement b1 = spreadsheetElement.getCellAt("B1");

        a1.setValue("<span>Fooooooooooooooooooooooooo</span>");
        b1.setValue("bar");

        compareScreen("longHtmlTextWrapped");
    }

    @Test
    public void cellTextInput_htmlText_renderedAsText() throws IOException {
        headerPage.createNewSpreadsheet();

        final SpreadsheetElement spreadsheetElement = $(
            SpreadsheetElement.class).first();

        final SheetCellElement a1 = spreadsheetElement.getCellAt("A1");

        a1.setValue("<span>Foo</span>");

        compareScreen("htmlText");
    }

    @Test
    public void verticalOverflowCells_noOverflow() {
        loadWrapTextTest();

        assertNoOverflowForCell("C4");
        assertNoOverflowForCell("C13");
    }

    @Test
    public void longWordInCellWithWrapText_noOverflow() {
        loadWrapTextTest();

        assertNoOverflowForCell("E8");
    }

    @Test
    public void sameContentInTwoCellsWithDifferentWidths_noOverflow() {
        loadWrapTextTest();

        assertNoOverflowForCell("E4");
        assertNoOverflowForCell("E13");
    }

    private void assertNoOverflowForCell(String cell) {
        final SpreadsheetElement spr = $(SpreadsheetElement.class).first();

        final SheetCellElement cellElement = spr.getCellAt(cell);

        Assert.assertEquals("hidden", cellElement.getCssValue("overflow"));
    }

    private void loadWrapTextTest() {
        headerPage.loadFile("wrap_text_test.xlsx", this);
    }
}
