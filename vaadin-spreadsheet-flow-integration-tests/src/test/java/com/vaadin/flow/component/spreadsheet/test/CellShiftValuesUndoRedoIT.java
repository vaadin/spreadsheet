package com.vaadin.flow.component.spreadsheet.test;

import com.vaadin.flow.component.spreadsheet.testbench.SheetCellElement;
import com.vaadin.flow.component.spreadsheet.testbench.SpreadsheetElement;
import com.vaadin.testbench.annotations.RunLocally;
import com.vaadin.testbench.parallel.Browser;
import com.vaadin.tests.AbstractParallelTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;

@RunLocally(Browser.CHROME)
public class CellShiftValuesUndoRedoIT extends AbstractParallelTest {

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
    public void undoRedo_CellShiftValues_ValuesAreUpdatedAsExpectedWithNoErrors() {
        testHelpers.loadFile("500x200test.xlsx");
        SheetCellElement target = spreadsheet.getCellAt("A9");
        Assert.assertEquals("9", target.getValue());

        testHelpers.selectCell("A1");

        WebElement selectionCorner = spreadsheet.findElement(
                By.className("sheet-selection")).findElement(
                By.className("s-corner"));
        // drag corner element of the selected cell to the target cell
        new Actions(driver).dragAndDrop(selectionCorner, target).perform();

        ensureValueEquals(spreadsheet, "A9", "1");
        undo();
        ensureValueEquals(spreadsheet, "A9", "9");
        redo();
        ensureValueEquals(spreadsheet, "A9", "1");

        assertNoErrorIndicatorDetected();
    }

    private void ensureValueEquals(final SpreadsheetElement spreadsheet,
            final String cellAddress, final String expectedValue) {
        waitUntil(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver arg0) {
                String value = spreadsheet.getCellAt(cellAddress).getValue();
                if (expectedValue != null) {
                    return expectedValue.equals(value);
                }
                return value == null;
            }
        });
    }

    private void undo() {
        // TODO: cleanup modifier keys solution (for macOS)
        new Actions(getDriver())
                .keyDown(Keys.CONTROL).keyDown(Keys.COMMAND)
                .sendKeys("z")
                .keyUp(Keys.CONTROL).keyUp(Keys.COMMAND)
                .build().perform();
    }

    private void redo() {
        // TODO: cleanup modifier keys solution (for macOS)
        new Actions(getDriver())
                .keyDown(Keys.CONTROL).keyDown(Keys.COMMAND)
                .sendKeys("y")
                .keyUp(Keys.CONTROL).keyUp(Keys.COMMAND)
                .build().perform();
    }

    private void assertNoErrorIndicatorDetected() {
        Assert.assertTrue("Error indicator detected when there should be none.",
                findElements(By.className("v-errorindicator")).isEmpty());
    }

}
