package com.vaadin.flow.component.spreadsheet.test;

import com.vaadin.flow.component.spreadsheet.testbench.SpreadsheetElement;
import com.vaadin.testbench.HasElementQuery;
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

    public SpreadsheetElement createNewSpreadsheet() {
        WebElement createBtn = $("vaadin-button").id("createNewBtn");
        createBtn.click();

        return $(SpreadsheetElement.class).first();
    }

    @Override
    public SearchContext getContext() {
        return this.driver;
    }
}
