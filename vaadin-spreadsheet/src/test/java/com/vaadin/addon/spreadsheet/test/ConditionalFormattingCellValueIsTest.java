package com.vaadin.addon.spreadsheet.test;

import static org.junit.Assert.assertEquals;
import java.io.IOException;

import org.junit.Test;

import com.vaadin.addon.spreadsheet.elements.SheetCellElement;
import com.vaadin.addon.spreadsheet.elements.SpreadsheetElement;

public class ConditionalFormattingCellValueIsTest extends AbstractSpreadsheetTestCase {

    private static final String STRING_VALUE = "'Foo";
    public static final String NUMBER_VALUE = "1";
    public static final String DIFFERENT_NUMBER_VALUE = "2";
    public static final String TRUE_VALUE = "TRUE";
    public static final String FALSE_VALUE = "FALSE";
    private static final String FALSE_CONDITION_COLOR = "rgba(255, 255, 255, 1)";
    private static final String TRUE_CONDITION_COLOR = "rgba(255, 0, 0, 1)";

    private SpreadsheetElement spreadSheet;
    private SheetCellElement b2;
    private SheetCellElement b3;
    private SheetCellElement b4;
    private SheetCellElement d2;
    private SheetCellElement d3;
    private SheetCellElement f2;
    private SheetCellElement f3;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        headerPage.loadFile("conditional_formatting_cell_is.xlsx", this);
        spreadSheet = $(SpreadsheetElement.class).first();
        spreadSheet.selectSheetAt(1);

        b2 = spreadSheet.getCellAt("B2");
        b3 = spreadSheet.getCellAt("B3");
        b4 = spreadSheet.getCellAt("B4");

        d2 = spreadSheet.getCellAt("D2");
        d3 = spreadSheet.getCellAt("D3");

        f2 = spreadSheet.getCellAt("F2");
        f3 = spreadSheet.getCellAt("F3");
    }

    @Test
    public void loadSpreadsheetWithEqualConditionFormattingInB3_MakeConditionFalse_CellB3FilledWhite()
        throws IOException {

        b2.setValue(STRING_VALUE);
        b3.setValue("'Not" + STRING_VALUE);

        d2.setValue(NUMBER_VALUE);
        d3.setValue(DIFFERENT_NUMBER_VALUE);

        f2.setValue(TRUE_VALUE);
        f3.setValue(FALSE_VALUE);

        String cellColorStringCase = spreadSheet.getCellAt("B3")
            .getCssValue("background-color");
        String cellColorNumberCase = spreadSheet.getCellAt("D3")
            .getCssValue("background-color");
        String cellColorBooleanCase = spreadSheet.getCellAt("F3")
            .getCssValue("background-color");

        assertEquals(FALSE_CONDITION_COLOR, cellColorStringCase);
        assertEquals(FALSE_CONDITION_COLOR, cellColorNumberCase);
        assertEquals(FALSE_CONDITION_COLOR, cellColorBooleanCase);
    }

    @Test
    public void loadSpreadsheetWithEqualConditionFormattingInB3_MakeConditionTrue_CellB3FilledRed()
        throws IOException {

        b2.setValue(STRING_VALUE);
        b3.setValue(STRING_VALUE);

        d2.setValue(NUMBER_VALUE);
        d3.setValue(NUMBER_VALUE);

        f2.setValue(TRUE_VALUE);
        f3.setValue(TRUE_VALUE);

        String cellColorStringCase = spreadSheet.getCellAt("B3")
            .getCssValue("background-color");
        String cellColorNumberCase = spreadSheet.getCellAt("D3")
            .getCssValue("background-color");
        String cellColorBooleanCase = spreadSheet.getCellAt("F3")
            .getCssValue("background-color");

        assertEquals(TRUE_CONDITION_COLOR, cellColorStringCase);
        assertEquals(TRUE_CONDITION_COLOR, cellColorNumberCase);
        assertEquals(TRUE_CONDITION_COLOR, cellColorBooleanCase);
    }

    @Test
    public void loadSpreadsheetWithNotEqualConditionFormattingInB4_insertIncoherentValue_CellB4FilledRed()
        throws IOException {

        b2.setValue(STRING_VALUE);
        b4.setValue(NUMBER_VALUE);

        String cellColor = spreadSheet.getCellAt("B4")
            .getCssValue("background-color");

        assertEquals(TRUE_CONDITION_COLOR, cellColor);
    }
}
