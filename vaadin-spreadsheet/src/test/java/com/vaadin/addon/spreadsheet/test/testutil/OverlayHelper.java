package com.vaadin.addon.spreadsheet.test.testutil;

import java.util.List;

import org.apache.poi.ss.util.CellReference;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * OverlayHelper
 */
public class OverlayHelper extends SeleniumHelper {

    /**
     * @param driver
     */
    public OverlayHelper(WebDriver driver) {
        super(driver);
    }

    /**
     * getOverlayElement
     * @param cell
     * @return WebElement
     */
    public WebElement getOverlayElement(String cell) {
        int[] coordinates = numericCoordinates(cell);

        WebElement element = driver.findElement(By
                .cssSelector(".sheet-image.col" + coordinates[0] + ".row"
                        + coordinates[1]));
        return element;
    }

    /**
     * isOverlayPresent
     * @param cell
     * @return boolean
     */
    public boolean isOverlayPresent(String cell) {
        int[] coordinates = numericCoordinates(cell);

        List<WebElement> elements = driver.findElements(By
                .cssSelector(".sheet-image.col" + coordinates[0] + ".row"
                        + coordinates[1]));
        return elements.size() > 0;
    }

    /**
     * numericCoordinates
     * @param cell
     * @return int[]
     */
    public int[] numericCoordinates(String cell) {
        CellReference cellReference = new CellReference(cell);
        return new int[] { cellReference.getCol() + 1,
                cellReference.getRow() + 1 };
    }
}
