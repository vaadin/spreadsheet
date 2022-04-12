package com.vaadin.flow.component.spreadsheet.test;

import com.vaadin.flow.component.combobox.testbench.ComboBoxElement;
import com.vaadin.flow.component.spreadsheet.testbench.SheetCellElement;
import com.vaadin.flow.component.spreadsheet.testbench.SpreadsheetElement;
import com.vaadin.flow.component.spreadsheet.tests.fixtures.TestFixtures;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;
import com.vaadin.testbench.annotations.RunLocally;
import com.vaadin.testbench.parallel.Browser;
import com.vaadin.tests.AbstractParallelTest;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.Locale;
import java.util.NoSuchElementException;

@RunLocally(Browser.CHROME)
public abstract class AbstractSpreadsheetIT extends AbstractParallelTest {

    private SpreadsheetElement spreadsheet;
    private static final String BACKGROUND_COLOR = "background-color";

    public void selectCell(String address) {
        // TODO: clean up solution
        new Actions(getDriver()).moveToElement(getSpreadsheet().getCellAt(address)).click().build()
                .perform();
    }

    public void selectCell(String address, boolean ctrl, boolean shift) {
        // TODO: clean up solution
        if (ctrl) {
            new Actions(getDriver()).moveToElement(getSpreadsheet().getCellAt(address))
                    .keyDown(Keys.CONTROL).keyDown(Keys.COMMAND)
                    .click()
                    .keyUp(Keys.CONTROL).keyUp(Keys.COMMAND)
                    .build().perform();
        } else if (shift) {
            new Actions(getDriver()).moveToElement(getSpreadsheet().getCellAt(address))
                    .keyDown(Keys.SHIFT)
                    .click()
                    .keyUp(Keys.SHIFT)
                    .build().perform();
        } else if (ctrl && shift) {
            new Actions(getDriver()).moveToElement(getSpreadsheet().getCellAt(address))
                    .keyDown(Keys.SHIFT).keyDown(Keys.SHIFT).keyDown(Keys.CONTROL).keyDown(Keys.COMMAND)
                    .click()
                    .keyUp(Keys.SHIFT).keyUp(Keys.SHIFT).keyUp(Keys.CONTROL).keyUp(Keys.COMMAND)
                    .build().perform();
        } else {
            selectCell(address);
        }
    }

    public void selectSheetAt(int sheetIndex) {
        getSpreadsheet().selectSheetAt(sheetIndex);
    }

    public void clickCell(String address) {
        SheetCellElement cellElement = getSpreadsheet()
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

    public void setCellValue(String address, String value) {
        getSpreadsheet().getCellAt(address).setValue(value);
    }

    public void deleteCellValue(String cellAddress){
        clickCell(cellAddress);
        new Actions(getDriver()).sendKeys(Keys.DELETE).build().perform();
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

    public void addFreezePane() {
        $("vaadin-button").id("freezePane").click();
        $("vaadin-button").id("submitValues").click();
    }

    public void addFreezePane(int horizontalSplitPosition, int verticalSplitPosition) {
        $("vaadin-button").id("freezePane").click();
        $(TextFieldElement.class).id("verticalSplitPosition").setValue(String.valueOf(verticalSplitPosition));
        $(TextFieldElement.class).id("horizontalSplitPosition").setValue(String.valueOf(horizontalSplitPosition));
        $("vaadin-button").id("submitValues").click();
    }

    public void setLocale(Locale locale) {
        ComboBoxElement localeSelect = $(ComboBoxElement.class).id("localeSelect");
        localeSelect.selectByText(locale.getDisplayName());
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

    public String getAddressFieldValue() {
        return getAddressField().getAttribute("value");
    }

    private WebElement getAddressField() {
        return findElement(By.cssSelector("input.addressfield"));
    }

    private WebElement getFormulaField() {
        return driver.findElement(By.className("functionfield"));
    }

    public String getFormulaFieldValue() {
        return getFormulaField().getAttribute("value");
    }

    public void insertColumn(String[] values) {
        for (String value : values) {
            insertAndRet(value);
        }
    }

    private void insertAndRet(CharSequence k) {
        action(k);
        action(Keys.RETURN);
        action(Keys.ENTER);
    }

    private void action(CharSequence k) {
        new Actions(driver).sendKeys(k).build().perform();
    }

    public boolean isCellSelected(int col, int row) {
        return getSpreadsheet().getCellAt(row, col)
                .isCellSelected();
    }

    public String getCellColor(String address) {
        return getSpreadsheet().getCellAt(address).getCssValue(BACKGROUND_COLOR);
    }

    public void navigateToCell(String cell) {
        driver.findElement(By.xpath("//*[@class='addressfield']")).clear();
        driver.findElement(By.xpath("//*[@class='addressfield']")).sendKeys(
                cell);
        new Actions(driver).sendKeys(Keys.RETURN).perform();
    }

    // Context menu helpers

    public void clickItem(String caption) {
        try {
            new Actions(getDriver())
                    .click(getDriver().findElement(By.xpath("//div[@class='popupContent']//*[text()='"+caption+"']")))
                    .perform();
        } catch (NoSuchElementException ex) {
            throw new RuntimeException("Menu item '"+caption+"' not found", ex);
        }
    }

    public boolean hasOption (String caption) {
        return getDriver().findElements(By.xpath("//div[@class='popupContent']//*[text()='"+caption+"']")).size()!=0;
    }
}
