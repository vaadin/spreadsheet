package com.vaadin.addon.spreadsheet.test.testutil;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.vaadin.addon.spreadsheet.elements.SheetCellElement;
import com.vaadin.addon.spreadsheet.elements.SpreadsheetElement;
import com.vaadin.addon.spreadsheet.test.SheetClicker;
import com.vaadin.testbench.ElementQuery;
import com.vaadin.testbench.commands.CanWaitForVaadin;
import com.vaadin.testbench.commands.TestBenchCommandExecutor;
import com.vaadin.testbench.elementsbase.AbstractElement;
import com.vaadin.testbench.parallel.BrowserUtil;

/**
 * SheetController
 */
public class SheetController implements SheetClicker {

    private final WebDriver driver;
    private final CanWaitForVaadin sleeper;
    private DesiredCapabilities desiredCapabilities;

    /**
     * @param driver
     * @param sleeper
     * @param desiredCapabilities
     */
    public SheetController(WebDriver driver, CanWaitForVaadin sleeper,
            DesiredCapabilities desiredCapabilities) {
        this.driver = driver;
        this.sleeper = sleeper;
        this.desiredCapabilities = desiredCapabilities;
    }

    private SheetController insertAndTab(CharSequence k) {
        action(k).action(Keys.TAB);
        ((TestBenchCommandExecutor) driver).waitForVaadin();
        return this;
    }

    /**
     * insertAndRet
     * @param k
     * @return SheetController
     */
    public SheetController insertAndRet(CharSequence k) {
        action(k).action(Keys.RETURN).action(Keys.ENTER);
        ((TestBenchCommandExecutor) driver).waitForVaadin();
        return this;
    }

    /**
     * needsFixLeftParenthesis
     * @return boolean
     */
    protected boolean needsFixLeftParenthesis() {
        // TODO: where is this really needed?
        return BrowserUtil.isIE(desiredCapabilities, 9);
    }

    /**
     * action
     * @param k
     * @return SheetController
     */
    public SheetController action(CharSequence k) {
        waitForVaadin();
        new Actions(driver).sendKeys(k).build().perform();
        waitForVaadin();

        return this;
    }

    /**
     * cellToXPath
     * @param cell
     * @return String
     */
    public String cellToXPath(String cell) {
        int[] coordinates = numericCoordinates(cell);

        // TODO - This will not work with multiple spreadsheets, add reference
        // to sheet's XPath
        return "//*["
                + "contains(concat(' ', normalize-space(@class), ' '), ' col"
                + coordinates[0]
                + " ')"
                + "and contains(concat(' ', normalize-space(@class), ' '), ' row"
                + coordinates[1] + " ')" + "]";
    }

    private int[] numericCoordinates(String cell) {
        String alpha = "A";
        String number = "1";
        for (int i = 0; i < cell.length(); i++) {
            if (cell.charAt(i) < 65) {
                alpha = cell.substring(0, i);
                number = cell.substring(i);
                break;
            }
        }

        int col = 0;
        for (int i = 0; i < alpha.length(); i++) {
            char h = alpha.charAt(i);
            col = (h - 'A' + 1) + (col * 26);
        }
        int row = Integer.parseInt(number);

        int[] coordinates = new int[] { col, row };
        return coordinates;
    }

    /**
     * mergedCell
     * @param topLeftCell
     * @return By
     */
    public By mergedCell(String topLeftCell) {
        int[] coordinates = numericCoordinates(topLeftCell);
        return By.xpath("//div[contains(@class,'col" + coordinates[0] + " row"
                + coordinates[1] + "') and contains(@class, 'merged-cell')]");
    }

    /**
     * columnToXPath
     * @param column
     * @return By
     */
    public By columnToXPath(String column) {
        return By.xpath("//*[@class='ch col" + (column.charAt(0) - 'A' + 1)
                + "']");
    }

    /**
     * rowToXPath
     * @param row
     * @return By
     */
    public By rowToXPath(String row) {
        return By.xpath("//*[@class='rh row" + row + "']");
    }

    /**
     * getCellContent
     * @param cell
     * @return String
     */
    public String getCellContent(String cell) {
        return $(SpreadsheetElement.class).first().getCellAt(cell).getValue();
    }

    /**
     * getMergedCellContent
     * @param topLeftCell
     * @return String
     */
    public String getMergedCellContent(String topLeftCell) {
        return driver.findElement(mergedCell(topLeftCell)).getText();
    }

    /**
     * getCellStyle
     * @param cell
     * @param propertyName
     * @return String
     */
    public String getCellStyle(String cell, String propertyName) {
        return getCellElement(cell).getCssValue(propertyName);
    }

    /**
     * getCellElement
     * @param cell
     * @return WebElement
     */
    public WebElement getCellElement(String cell) {
        return driver.findElement(By.xpath(cellToXPath(cell)));
    }

    /**
     * putCellContent
     * @param cell
     * @param value
     * @return SheetController
     */
    public SheetController putCellContent(String cell, CharSequence value) {
        openInlineEditor(cell);
        waitForVaadin();
        clearInput();
        waitForVaadin();
        insertAndTab(value);
        return this;
    }

    private void openInlineEditor(String cell) {
        SheetCellElement cellElement = $(SpreadsheetElement.class).first()
                .getCellAt(cell);
        new Actions(getDriver()).doubleClick(cellElement).build().perform();
    }
    
    /**
     * getInlineEditor
     * @param cell
     * @return WebElement
     */
    public WebElement getInlineEditor(String cell) {
        openInlineEditor(cell);
        waitForVaadin();
        return getCellElement(cell).findElement(By.xpath("../input"));
    }

    private void clearInput() {
        WebElement inlineInput = driver.findElement(By.id("cellinput"));
        inlineInput.clear();
    }

    /**
     * insertColumn
     * @param values
     * @return SheetController
     */
    public SheetController insertColumn(String[] values) {
        for (String value : values) {
            insertAndRet(value);
        }
        return this;
    }

    /**
     * waitForVaadin
     */
    public final void waitForVaadin() {
        sleeper.waitForVaadin();
    }

    /**
     * getDriver
     * @return WebDriver
     */
    protected WebDriver getDriver() {
        return driver;
    }

    /**
     * $
     * @param clazz
     * @return ElementQuery<T>
     */
    public <T extends AbstractElement> ElementQuery<T> $(Class<T> clazz) {
        return new ElementQuery<T>(clazz).context(getDriver());
    }

    /**
     * selectCell
     * @param cell
     */
    public void selectCell(String cell) {
        clickCell(cell);
    }

    @Override
    public void clickCell(String cell) {
        SheetCellElement cellElement = $(SpreadsheetElement.class).first()
                .getCellAt(cell);
        new Actions(getDriver()).moveToElement(cellElement).click().build()
                .perform();
    }

    /**
     * doubleClickCell
     * @param cell
     */
    public void doubleClickCell(String cell) {
        SheetCellElement cellElement = $(SpreadsheetElement.class).first()
                .getCellAt(cell);
        new Actions(getDriver()).moveToElement(cellElement).doubleClick()
                .build().perform();
    }

    @Override
    public void clickColumn(String column) {
        $(SpreadsheetElement.class).first()
                .getColumnHeader(column.charAt(0) - 'A' + 1).click();
    }

    @Override
    public void clickRow(int row) {
        $(SpreadsheetElement.class).first().getRowHeader(row).click();
    }

    /**
     * setCellVallue
     * @param cell
     * @param value
     */
    public void setCellVallue(String cell, String value) {
        SheetCellElement cellElement = $(SpreadsheetElement.class).first()
                .getCellAt(cell);
        cellElement.setValue(value);
    }

    /**
     * clickElement
     * @param by
     * @return SheetController
     */
    public SheetController clickElement(org.openqa.selenium.By by) {
        driver.findElement(by).click();
        return this;
    }

    /**
     * navigateToCell
     * @param cell
     */
    public void navigateToCell(String cell) {
        driver.findElement(By.xpath("//*[@class='addressfield']")).clear();
        driver.findElement(By.xpath("//*[@class='addressfield']")).sendKeys(
                cell);
        sleeper.waitForVaadin();
        new Actions(driver).sendKeys(Keys.RETURN).perform();
        sleeper.waitForVaadin();
    }

    /**
     * getSelectedCell
     * @return String
     */
    public String getSelectedCell() {
        String elemClass = driver.findElement(
                By.cssSelector(".sheet-selection")).getAttribute("class");

        int rowStart = elemClass.indexOf("row");
        if (rowStart == -1) {
            return "A1";
        }

        int k = rowStart + "row".length();
        String rowNumber = "";
        while (k < elemClass.length()) {
            char digit = elemClass.charAt(k);
            if (digit == ' ') {
                break;
            }
            rowNumber += elemClass.charAt(k);
            k++;
        }

        int colStart = elemClass.indexOf("col");
        k = colStart + "col".length();
        String colNumberStr = "";
        while (k < elemClass.length()) {
            char digit = elemClass.charAt(k);
            if (digit == ' ') {
                break;
            }
            colNumberStr += elemClass.charAt(k);
            k++;
        }
        int colNumber = Integer.parseInt(colNumberStr);
        int dividend = colNumber;
        String columnName = "";
        int modulo;

        while (dividend > 0) {
            modulo = (dividend - 1) % 26;
            columnName = ((char) (65 + modulo)) + columnName;
            dividend = (dividend - modulo) / 26;
        }

        return columnName + rowNumber;
    }

    /**
     * selectRegion
     * @param from
     * @param to
     */
    public void selectRegion(String from, String to) {
        new Actions(driver).clickAndHold(getCellElement(from))
                .release(getCellElement(to)).perform();
        waitForVaadin();
    }

}
