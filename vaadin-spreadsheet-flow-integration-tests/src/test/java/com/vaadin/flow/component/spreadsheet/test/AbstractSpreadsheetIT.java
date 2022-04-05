package com.vaadin.flow.component.spreadsheet.test;

import com.vaadin.flow.component.combobox.testbench.ComboBoxElement;
import com.vaadin.flow.component.spreadsheet.testbench.SheetCellElement;
import com.vaadin.flow.component.spreadsheet.testbench.SpreadsheetElement;
import com.vaadin.flow.component.spreadsheet.tests.fixtures.TestFixtures;
import com.vaadin.testbench.annotations.RunLocally;
import com.vaadin.testbench.parallel.Browser;
import com.vaadin.tests.AbstractParallelTest;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

@RunLocally(Browser.CHROME)
public abstract class AbstractSpreadsheetIT extends AbstractParallelTest {

    private SpreadsheetElement spreadsheet;

    public void selectCell(String address) {
        // TODO: clean up solution
        new Actions(getDriver()).moveToElement(spreadsheet.getCellAt(address)).click().build()
                .perform();
    }

    public void clickCell(String address) {
        SheetCellElement cellElement = spreadsheet
                .getCellAt(address);
        new Actions(getDriver()).moveToElement(cellElement).click().build()
                .perform();
    }

    public String getCellContent(String address) {
        return $(SpreadsheetElement.class).first().getCellAt(address).getValue();
    }

    public void setSpreadsheet(SpreadsheetElement spreadsheet) {
        this.spreadsheet = spreadsheet;
    }

    public SpreadsheetElement getSpreadsheet() {
        return spreadsheet;
    }

    public void createNewSpreadsheet() {
        WebElement createBtn = $("vaadin-button").id("createNewBtn");
        createBtn.click();

        setSpreadsheet($(SpreadsheetElement.class).first());
    }

    public void loadFile(String fileName) {
        ComboBoxElement testSheetSelect = $(ComboBoxElement.class).id("testSheetSelect");
        testSheetSelect.selectByText(fileName);

        WebElement updateBtn = $("vaadin-button").id("update");
        updateBtn.click();

        setSpreadsheet($(SpreadsheetElement.class).first());
    }

    public void loadTestFixture(TestFixtures fixture) {
        $(ComboBoxElement.class).id("fixtureSelect").selectByText(
                fixture.toString());
        $("vaadin-button").id("loadFixtureBtn").click();

        // sanity check
        Assert.assertEquals("Fixture not loaded correctly", fixture.toString(),
                $(ComboBoxElement.class).id("fixtureSelect").getInputElementValue());
    }

    public void assertNoErrorIndicatorDetected() {
        Assert.assertTrue("Error indicator detected when there should be none.",
                findElements(By.className("v-errorindicator")).isEmpty());
    }
}
