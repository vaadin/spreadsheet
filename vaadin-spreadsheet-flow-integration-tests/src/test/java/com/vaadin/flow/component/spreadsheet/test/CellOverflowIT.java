package com.vaadin.flow.component.spreadsheet.test;

import com.vaadin.flow.component.spreadsheet.testbench.SheetCellElement;
import com.vaadin.flow.component.spreadsheet.testbench.SpreadsheetElement;
import com.vaadin.testbench.annotations.RunLocally;
import com.vaadin.testbench.parallel.Browser;
import com.vaadin.tests.AbstractParallelTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

@RunLocally(Browser.CHROME)
public class CellOverflowIT extends AbstractParallelTest {

    private SpreadsheetElement spreadsheet;
    private TestHelpers testHelpers;

    @Before
    public void init() {
        String url = getBaseURL().replace(super.getBaseURL(),
                super.getBaseURL() + "/vaadin-spreadsheet");
        getDriver().get(url);

        testHelpers = new TestHelpers(getDriver());
        spreadsheet = testHelpers.createNewSpreadsheet();
        testHelpers.setSpreadsheetElement(spreadsheet);
    }

    @Test
    public void cellOverflow_stringFormula_overflowsAsText()
            throws IOException {

        String valueToTest = "aaaaabbbbccccddddeeee";

        spreadsheet.getCellAt("B1").setValue(valueToTest);

        SheetCellElement a1 = spreadsheet.getCellAt("A1");

        a1.setValue("=B1");
        Assert.assertEquals(valueToTest, a1.getValue());
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
        SheetCellElement cellElement = spreadsheet.getCellAt(cell);

        Assert.assertEquals("hidden", cellElement.getCssValue("overflow"));
    }

    private void loadWrapTextTest() {
        testHelpers.loadFile("wrap_text_test.xlsx");
    }
}
