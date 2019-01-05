package com.vaadin.addon.spreadsheet.test.pageobjects;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.util.CellRangeAddress;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import com.vaadin.addon.spreadsheet.elements.AddressUtil;
import com.vaadin.addon.spreadsheet.elements.SheetCellElement;
import com.vaadin.addon.spreadsheet.elements.SpreadsheetElement;
import com.vaadin.addon.spreadsheet.test.testutil.ContextMenuHelper;
import com.vaadin.testbench.By;

/**
 * SpreadsheetPage
 */
public class SpreadsheetPage extends Page {

    /**
     * BACKGROUND_COLOR
     */
    public static final String BACKGROUND_COLOR = "background-color";

    /**
     * @param driver
     */
    public SpreadsheetPage(WebDriver driver) {
        super(driver);
    }

    /**
     * isDisplayed
     * @return boolean
     */
    public boolean isDisplayed() {
        try {
            return driver.findElement(By.className("v-spreadsheet")) != null;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * getAddressFieldValue
     * @return String
     */
    public String getAddressFieldValue() {
        return getAddressField().getAttribute("value");
    }

    private WebElement getAddressField() {
        return driver.findElement(By.cssSelector("input.addressfield"));
    }

    /**
     * isCellSelected
     * @param col
     * @param row
     * @return boolean
     */
    public boolean isCellSelected(int col, int row) {
        return $(SpreadsheetElement.class).first().getCellAt(row, col)
                .isCellSelected();
    }

    /**
     * isCellSelected
     * @param address
     * @return boolean
     */
    public boolean isCellSelected(String address) {
        Point point = AddressUtil.addressToPoint(address);
        return isCellSelected(point.getX(), point.getY());
    }

    /**
     * getCellAt
     * @param col
     * @param row
     * @return SheetCellElement
     */
    public SheetCellElement getCellAt(int col, int row) {
        return $(SpreadsheetElement.class).first().getCellAt(row, col);
    }

    /**
     * clickOnCell
     * @param col
     * @param row
     */
    public void clickOnCell(int col, int row) {
        getCellAt(col, row).click();
    }

    /**
     * clickOnCell
     * @param address
     */
    public void clickOnCell(String address) {
        $(SpreadsheetElement.class).first().getCellAt(address).click();
    }

    /**
     * clickOnCell
     * @param address
     * @param modifiers
     */
    public void clickOnCell(String address, Keys... modifiers) {
        WebElement cell = getCellAt(address);
        Actions actions = new Actions(driver);
        actions.moveToElement(cell, 1, 1);
        for (Keys modifier : modifiers) {
            actions.keyDown(modifier);
        }
        actions.click();
        for (Keys modifier : modifiers) {
            actions.keyUp(modifier);
        }
        actions.build().perform();
    }

    /**
     * clickOnCell
     * @param column
     * @param row
     * @param modifiers
     */
    public void clickOnCell(int column, int row, Keys... modifiers) {
        WebElement cell = getCellAt(column, row);
        Actions actions = new Actions(driver);
        actions.moveToElement(cell, 1, 1);
        for (Keys modifier : modifiers) {
            actions.keyDown(modifier);
        }
        actions.click();
        for (Keys modifier : modifiers) {
            actions.keyUp(modifier);
        }
        actions.build().perform();
    }

    /**
     * setAddressFieldValue
     * @param address
     */
    public void setAddressFieldValue(String address) {
        WebElement addressField = getAddressField();
        addressField.clear();
        addressField.sendKeys(address + Keys.ENTER);
    }

    /**
     * dragFromCellToCell
     * @param from
     * @param to
     */
    public void dragFromCellToCell(String from, String to) {
        WebElement fromCell = getCellAt(from);
        WebElement toCell = getCellAt(to);

        new Actions(driver).dragAndDrop(fromCell, toCell).build().perform();
    }

    /**
     * clickOnFormulaField
     */
    public void clickOnFormulaField() {
        getFormulaField().click();
    }

    /**
     * getFormulaField
     * @return WebElement
     */
    private WebElement getFormulaField() {
        return driver.findElement(By.className("functionfield"));
    }

    /**
     * setFormulaFieldValue
     * @param value
     */
    public void setFormulaFieldValue(String value) {
        WebElement formulaField = getFormulaField();
        formulaField.clear();
        formulaField.sendKeys(value);
    }

    /**
     * getCellValue
     * @param col
     * @param row
     * @return String
     */
    public String getCellValue(int col, int row) {
        return getCellAt(col, row).getText();
    }

    /**
     * getCellValue
     * @param address
     * @return String
     */
    public String getCellValue(String address) {
        Point point = AddressUtil.addressToPoint(address);
        return getCellValue(point.getX(), point.getY());
    }
    
    /**
     * setCellValue
     * @param address
     * @param newValue
     */
    public void setCellValue(String address, String newValue) {
        getCellAt(address).setValue(newValue);
    }
    
    /**
     * getCellColor
     * @param cellAddress
     * @return String
     */
    public String getCellColor(String cellAddress) {
        SheetCellElement cellAt = getCellAt(cellAddress);
        return cellAt.getCssValue(BACKGROUND_COLOR);
    }

    /**
     * isCellActiveWithinSelection
     * @param address
     * @return boolean
     */
    public boolean isCellActiveWithinSelection(String address) {
        SheetCellElement cell = getCellAt(address);
        return cell.isCellSelected()
                && !cell.getAttribute("class").contains("cell-range");
    }

    /**
     * clickOnColumnHeader
     * @param columnAddress
     * @param modifiers
     */
    public void clickOnColumnHeader(String columnAddress, Keys... modifiers) {
        Point colPoint = AddressUtil.addressToPoint(columnAddress + "1");
        clickOnColumnHeader(colPoint.x, modifiers);
    }

    /**
     * clickOnColumnHeader
     * @param column
     * @param modifiers
     */
    public void clickOnColumnHeader(int column, Keys... modifiers) {
        Actions actions = new Actions(driver);
        WebElement header = driver.findElement(By.cssSelector(".ch.col"
                + column));
        actions.moveToElement(header, 1, 1);
        for (Keys modifier : modifiers) {
            actions.keyDown(modifier);
        }
        actions.click(driver.findElement(By.cssSelector(".ch.col" + column)));
        for (Keys modifier : modifiers) {
            actions.keyUp(modifier);
        }
        actions.build().perform();
    }

    /**
     * clickOnRowHeader
     * @param row
     * @param modifiers
     */
    public void clickOnRowHeader(int row, Keys... modifiers) {
        Actions actions = new Actions(driver);
        actions.moveToElement(
                driver.findElement(By.cssSelector(".rh.row" + row)), 1, 1);
        for (Keys modifier : modifiers) {
            actions.keyDown(modifier);
        }
        actions.click(driver.findElement(By.cssSelector(".rh.row" + row)));
        for (Keys modifier : modifiers) {
            actions.keyUp(modifier);
        }
        actions.build().perform();
    }
    
    /**
     * deleteCellValue
     * @param cellAddress
     */
    public void deleteCellValue(String cellAddress){
        clickOnCell(cellAddress);
        new Actions(getDriver()).sendKeys(Keys.DELETE).build().perform();
    }

    /**
     * getFormulaFieldValue
     * @return String
     */
    public String getFormulaFieldValue() {
        return getFormulaField().getAttribute("value");
    }

    /**
     * selectSheetAt
     * @param sheetIndex
     */
    public void selectSheetAt(int sheetIndex) {
        SpreadsheetElement spreadsheet = $(SpreadsheetElement.class).first();
        spreadsheet.selectSheetAt(sheetIndex);
    }

    /**
     * addSheet
     */
    public void addSheet() {
        SpreadsheetElement spreadsheet = $(SpreadsheetElement.class).first();
        spreadsheet.addSheet();
    }

    private SheetCellElement getCellAt(String address) {
        Point point = AddressUtil.addressToPoint(address);
        return getCellAt(point.getX(), point.getY());
    }

    /**
     * contextClickOnRowHeader
     * @param i
     */
    public void contextClickOnRowHeader(int i) {
        $(SpreadsheetElement.class).first().getRowHeader(i).contextClick();
    }

    /**
     * contextClickOnColumnHeader
     * @param column
     */
    public void contextClickOnColumnHeader(char column) {
        $(SpreadsheetElement.class).first().getColumnHeader(column - 'A' + 1)
            .contextClick();
    }
    
    /**
     * clickContextMenuItem
     * @param caption
     */
    public void clickContextMenuItem(String caption) {
        final ContextMenuHelper contextMenu = new ContextMenuHelper(
            getDriver());

        contextMenu.clickItem(caption);
    }
    
    /**
     * unhideRow
     * @param i
     */
    public void unhideRow(int i) {
        contextClickOnRowHeader(i + 1);
        clickContextMenuItem("Unhide row " + i);
    }

    /**
     * unhideColumn
     * @param c
     */
    public void unhideColumn(char c) {
        contextClickOnColumnHeader((char) (c + 1));
        clickContextMenuItem("Unhide column " + c);
    }
        
    /**
     * getSelectionFormula
     * @return String
     */
    public String getSelectionFormula() {
        final SpreadsheetElement sprElement = $(SpreadsheetElement.class).first();

        WebElement selection = findElement(org.openqa.selenium.By.className("sheet-selection"));
        final String[] classes = selection.getAttribute("class").split(" ");

        int startRow = -1;
        int startColumn = -1;

        for (String c : classes) {
            if (c.startsWith("row")) {
                startRow = Integer.parseInt(c.substring(3));
            }
            if (c.startsWith("col")) {
                startColumn = Integer.parseInt(c.substring(3));
            }
        }

        int endRow = startRow + 1;
        while (sprElement.getCellAt(endRow, startColumn).isCellSelected()) {
            endRow++;
        }
        endRow--;

        int endColumn = startColumn;
        while (sprElement.getCellAt(startRow, endColumn).isCellSelected()) {
            endColumn++;
        }
        endColumn--;

        return new CellRangeAddress(startRow - 1, endRow - 1, startColumn - 1,
            endColumn - 1).formatAsString();
    }

    /**
     * getSelectedSheetName
     * @return String
     */
    public String getSelectedSheetName() {
        WebElement selectedSheetTab = findElement(
            By.cssSelector(".sheet-tabsheet-tab.selected-tab"));

        return selectedSheetTab.getText();
    }

    /**
     * getNamedRanges
     * @return List<String>
     */
    public List<String> getNamedRanges() {
        final List<WebElement> options = new Select(
            findElement(By.className("namedrangebox"))).getOptions();

        final List<String> optionStrings = new ArrayList<String>();
        
        for (WebElement option : options) {
            optionStrings.add(option.getText());
        }

        return optionStrings;
    }

    /**
     * selectNamedRange
     * @param name
     */
    public void selectNamedRange(String name) {
        new Select(findElement(By.className("namedrangebox")))
            .selectByVisibleText(name);
    }

    /**
     * getGroupings
     * @return List<WebElement>
     */
    public List<WebElement> getGroupings() {
        return $(SpreadsheetElement.class).first()
                .findElements(By.cssSelector(".col-group-pane .grouping.plus"));
    }
}
