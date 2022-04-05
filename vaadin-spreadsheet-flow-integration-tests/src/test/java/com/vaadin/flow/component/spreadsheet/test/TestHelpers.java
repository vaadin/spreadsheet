package com.vaadin.flow.component.spreadsheet.test;

import com.vaadin.flow.component.combobox.testbench.ComboBoxElement;
import com.vaadin.flow.component.spreadsheet.testbench.SheetCellElement;
import com.vaadin.flow.component.spreadsheet.testbench.SpreadsheetElement;
import com.vaadin.flow.component.spreadsheet.tests.fixtures.TestFixtures;
import com.vaadin.testbench.HasElementQuery;
import org.junit.Assert;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

class TestHelpers implements HasElementQuery {

    private WebDriver driver;
    private SpreadsheetElement spreadsheetElement;

    TestHelpers(WebDriver driver) {
        this.driver = driver;
    }

    public void setSpreadsheetElement(SpreadsheetElement spreadsheetElement) {
        this.spreadsheetElement = spreadsheetElement;
    }

    public SpreadsheetElement getSpreadsheetElement() {
        return spreadsheetElement;
    }

    public void selectCell(String address) {
        // TODO: clean up solution
        new Actions(driver).moveToElement(getSpreadsheetElement().getCellAt(address)).click().build()
                .perform();
    }

    public void clickCell(String address) {
        SheetCellElement cellElement = $(SpreadsheetElement.class).first()
                .getCellAt(address);
        new Actions(driver).moveToElement(cellElement).click().build()
                .perform();
    }

    public String getCellContent(String address) {
        return $(SpreadsheetElement.class).first().getCellAt(address).getValue();
    }

    public SpreadsheetElement createNewSpreadsheet() {
        WebElement createBtn = $("vaadin-button").id("createNewBtn");
        createBtn.click();

        return $(SpreadsheetElement.class).first();
    }

    public SpreadsheetElement loadFile(String fileName) {
        ComboBoxElement testSheetSelect = $(ComboBoxElement.class).id("testSheetSelect");
        testSheetSelect.selectByText(fileName);

        WebElement updateBtn = $("vaadin-button").id("update");
        updateBtn.click();

        return $(SpreadsheetElement.class).first();
    }

    public void loadTestFixture(TestFixtures fixture) {
        $(ComboBoxElement.class).id("fixtureSelect").selectByText(
                fixture.toString());
        $("vaadin-button").id("loadFixtureBtn").click();

        // sanity check
        Assert.assertEquals("Fixture not loaded correctly", fixture.toString(),
                $(ComboBoxElement.class).id("fixtureSelect").getInputElementValue());
    }

    @Override
    public SearchContext getContext() {
        return this.driver;
    }
}
