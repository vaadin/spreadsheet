package com.vaadin.flow.component.spreadsheet.test;

import com.vaadin.flow.component.spreadsheet.testbench.SheetCellElement;
import com.vaadin.flow.component.spreadsheet.testbench.SpreadsheetElement;
import com.vaadin.testbench.annotations.RunLocally;
import com.vaadin.testbench.parallel.Browser;
import com.vaadin.tests.AbstractParallelTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;

/**
 * Arrow key navigation tests.
 */
@RunLocally(Browser.CHROME)
public class CellArrowKeyNavigationIT extends AbstractParallelTest {

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
    public void shouldNotChangeCellWhenEditingAndArrowRightKeyIsPressed() {
        final SheetCellElement b2 = spreadsheet.getCellAt("B2");
        b2.setValue("123");

        testHelpers.selectCell("B2");
        new Actions(getDriver()).sendKeys(Keys.F2).build().perform(); //edit mode
        new Actions(getDriver()).sendKeys(Keys.ARROW_RIGHT).build().perform();
        new Actions(getDriver()).sendKeys(Keys.ESCAPE).build().perform();

        Assert.assertTrue(spreadsheet.getCellAt("B2").isCellSelected());
    }

    @Test
    public void shouldNotChangeCellWhenEditingAndArrowLeftKeyIsPressed() {
        final SheetCellElement b2 = spreadsheet.getCellAt("B2");
        b2.setValue("123");

        testHelpers.selectCell("B2");
        new Actions(getDriver()).sendKeys(Keys.F2).build().perform(); //edit mode
        new Actions(getDriver()).sendKeys(Keys.ARROW_LEFT).build().perform();
        new Actions(getDriver()).sendKeys(Keys.ESCAPE).build().perform();

        Assert.assertTrue(spreadsheet.getCellAt("B2").isCellSelected());
    }

    @Test
    public void shouldNotChangeCellWhenEditingAndArrowUpKeyIsPressed() {
        final SheetCellElement b2 = spreadsheet.getCellAt("B2");
        b2.setValue("123");

        testHelpers.selectCell("B2");
        new Actions(getDriver()).sendKeys(Keys.F2).build().perform(); //edit mode
        new Actions(getDriver()).sendKeys(Keys.ARROW_UP).build().perform();
        new Actions(getDriver()).sendKeys(Keys.ESCAPE).build().perform();

        Assert.assertTrue(spreadsheet.getCellAt("B2").isCellSelected());
    }

    @Test
    public void shouldNotChangeCellWhenEditingAndArrowDownKeyIsPressed() {
        final SheetCellElement b2 = spreadsheet.getCellAt("B2");
        b2.setValue("123");

        testHelpers.selectCell("B2");
        new Actions(getDriver()).sendKeys(Keys.F2).build().perform(); //edit mode
        new Actions(getDriver()).sendKeys(Keys.ARROW_DOWN).build().perform();
        new Actions(getDriver()).sendKeys(Keys.ESCAPE).build().perform();

        Assert.assertTrue(spreadsheet.getCellAt("B2").isCellSelected());
    }

    @Test
    public void shouldNotChangeCellWhenDoubleClickEditingAndArrowRightKeyIsPressed() {
        final SheetCellElement b2 = spreadsheet.getCellAt("B2");
        b2.setValue("123");

        testHelpers.selectCell("A1");
        spreadsheet.getCellAt("B2").doubleClick(); //edit mode
        new Actions(getDriver()).sendKeys(Keys.ARROW_RIGHT).build().perform();
        new Actions(getDriver()).sendKeys(Keys.ESCAPE).build().perform();

        Assert.assertTrue(spreadsheet.getCellAt("B2").isCellSelected());
    }

    @Test
    public void shouldNotChangeCellWhenDoubleClickEditingAndArrowLeftKeyIsPressed() {
        final SheetCellElement b2 = spreadsheet.getCellAt("B2");
        b2.setValue("123");

        testHelpers.selectCell("A1");
        spreadsheet.getCellAt("B2").doubleClick(); //edit mode
        new Actions(getDriver()).sendKeys(Keys.ARROW_LEFT).build().perform();
        new Actions(getDriver()).sendKeys(Keys.ESCAPE).build().perform();

        Assert.assertTrue(spreadsheet.getCellAt("B2").isCellSelected());
    }

    @Test
    public void shouldNotChangeCellWhenDoubleClickEditingAndArrowDownKeyIsPressed() {
        final SheetCellElement b2 = spreadsheet.getCellAt("B2");
        b2.setValue("123");

        testHelpers.selectCell("A1");
        spreadsheet.getCellAt("B2").doubleClick(); //edit mode
        new Actions(getDriver()).sendKeys(Keys.ARROW_DOWN).build().perform();
        new Actions(getDriver()).sendKeys(Keys.ESCAPE).build().perform();

        Assert.assertTrue(spreadsheet.getCellAt("B2").isCellSelected());
    }

    @Test
    public void shouldNotChangeCellWhenDoubleClickEditingAndArrowUpKeyIsPressed() {
        final SheetCellElement b2 = spreadsheet.getCellAt("B2");
        b2.setValue("123");

        testHelpers.selectCell("A1");
        spreadsheet.getCellAt("B2").doubleClick(); //edit mode
        new Actions(getDriver()).sendKeys(Keys.ARROW_UP).build().perform();
        new Actions(getDriver()).sendKeys(Keys.ESCAPE).build().perform();

        Assert.assertTrue(spreadsheet.getCellAt("B2").isCellSelected());
    }

    @Test
    public void shouldSelectCellToTheRightWhenSingleClickAndArrowRightKeyIsPressed() {
        testHelpers.selectCell("B2");
        new Actions(getDriver()).sendKeys(Keys.NUMPAD1).build().perform();
        new Actions(getDriver()).sendKeys(Keys.NUMPAD2).build().perform();
        new Actions(getDriver()).sendKeys(Keys.NUMPAD3).build().perform();

        new Actions(getDriver()).sendKeys(Keys.ARROW_RIGHT).build().perform();

        Assert.assertTrue(spreadsheet.getCellAt("C2").isCellSelected());
    }

    @Test
    public void shouldSelectCellToTheLeftWhenSingleClickAndArrowLeftKeyIsPressed() {
        testHelpers.selectCell("B2");
        new Actions(getDriver()).sendKeys(Keys.NUMPAD1).build().perform();
        new Actions(getDriver()).sendKeys(Keys.NUMPAD2).build().perform();
        new Actions(getDriver()).sendKeys(Keys.NUMPAD3).build().perform();

        new Actions(getDriver()).sendKeys(Keys.ARROW_LEFT).build().perform();

        Assert.assertTrue(spreadsheet.getCellAt("A2").isCellSelected());
    }

    @Test
    public void shouldSelectCellToTheTopWhenSingleClickAndArrowUpKeyIsPressed() {
        testHelpers.selectCell("B2");
        new Actions(getDriver()).sendKeys(Keys.NUMPAD1).build().perform();
        new Actions(getDriver()).sendKeys(Keys.NUMPAD2).build().perform();
        new Actions(getDriver()).sendKeys(Keys.NUMPAD3).build().perform();

        new Actions(getDriver()).sendKeys(Keys.ARROW_UP).build().perform();

        Assert.assertTrue(spreadsheet.getCellAt("B1").isCellSelected());
    }

    @Test
    public void shouldSelectCellToTheBottomWhenSingleClickAndArrowDownKeyIsPressed() {
        testHelpers.selectCell("B2");
        new Actions(getDriver()).sendKeys(Keys.NUMPAD1).build().perform();
        new Actions(getDriver()).sendKeys(Keys.NUMPAD2).build().perform();
        new Actions(getDriver()).sendKeys(Keys.NUMPAD3).build().perform();

        new Actions(getDriver()).sendKeys(Keys.ARROW_DOWN).build().perform();

        Assert.assertTrue(spreadsheet.getCellAt("B3").isCellSelected());
    }


//    private void selectCell(String address) {
//        // TODO: clean up solution
//        new Actions(getDriver()).moveToElement(spreadsheet.getCellAt(address)).click().build()
//                .perform();
//    }
//
//    private SpreadsheetElement createNewSpreadsheet() {
//        WebElement createBtn = $("vaadin-button").id("createNewBtn");
//        createBtn.click();
//
//        return $(SpreadsheetElement.class).first();
//    }
}
