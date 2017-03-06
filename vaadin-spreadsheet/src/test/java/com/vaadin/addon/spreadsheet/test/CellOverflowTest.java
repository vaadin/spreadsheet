package com.vaadin.addon.spreadsheet.test;

import static org.junit.Assert.assertEquals;
import java.io.IOException;

import org.junit.Test;

import com.vaadin.addon.spreadsheet.elements.SheetCellElement;
import com.vaadin.addon.spreadsheet.elements.SpreadsheetElement;
import com.vaadin.addon.spreadsheet.test.pageobjects.SpreadsheetPage;

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
    public void cellWithLongText_inputWrapped_noVerticalOverflow()
        throws IOException {
        headerPage.loadFile("TestWrapNoVerticalOverflow.xlsx", this);

        compareScreen("WrapNoVerticalOverflow");
    }

    @Test
    public void cellWithLongText_inputWrapped_overflowHidden() {
        String value = "hidden";
        SpreadsheetPage spreadsheetPage = headerPage
            .loadFile("TestWrapNoVerticalOverflow.xlsx", this);
        assertEquals(value,
            spreadsheetPage.getCellAt(3, 4).getCssValue("overflow"));
    }
}
