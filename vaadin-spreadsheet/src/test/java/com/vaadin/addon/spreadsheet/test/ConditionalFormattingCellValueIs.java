package com.vaadin.addon.spreadsheet.test;

import static org.junit.Assert.assertEquals;
import java.io.IOException;

import org.junit.Test;

import com.vaadin.addon.spreadsheet.elements.SheetCellElement;
import com.vaadin.addon.spreadsheet.elements.SpreadsheetElement;

public class ConditionalFormattingCellValueIs extends AbstractSpreadsheetTestCase {

    private static final String VALUE = "'Foo";
    private static final String FALSE_CONDITION_COLOR = "rgba(255, 255, 255, 1)";
    private static final String TRUE_CONDITION_COLOR = "rgba(255, 0, 0, 1)";

    private SpreadsheetElement spreadSheet;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        headerPage.loadFile("conditional_formatting_cell_is.xlsx", this);
        spreadSheet = $(SpreadsheetElement.class).first();
        spreadSheet.selectSheetAt(1);
    }

    @Test
    public void loadSpreadsheetWithConditionalFormattingInA2_MakeConditionFalse_CellA2FilledWhite()
        throws IOException {

        final SheetCellElement b2 = spreadSheet.getCellAt("B2");
        final SheetCellElement b3 = spreadSheet.getCellAt("B3");

        b2.setValue(VALUE);
        b3.setValue("'Not" + VALUE);

        String cellColor = spreadSheet.getCellAt("B3")
            .getCssValue("background-color");

        assertEquals(FALSE_CONDITION_COLOR, cellColor);
    }

    @Test
    public void loadSpreadsheetWithConditionalFormattingInA2_MakeConditionTrue_CellA2FilledRed()
        throws IOException {

        final SheetCellElement b2 = spreadSheet.getCellAt("B2");
        final SheetCellElement b3 = spreadSheet.getCellAt("B3");

        b2.setValue(VALUE);
        b3.setValue(VALUE);

        String cellColor = spreadSheet.getCellAt("B3")
            .getCssValue("background-color");

        assertEquals(TRUE_CONDITION_COLOR, cellColor);
    }
}
