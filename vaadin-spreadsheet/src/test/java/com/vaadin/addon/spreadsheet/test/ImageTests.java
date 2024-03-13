package com.vaadin.addon.spreadsheet.test;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.vaadin.addon.spreadsheet.elements.SpreadsheetElement;

public class ImageTests extends AbstractSpreadsheetTestCase {

    private SpreadsheetElement spreadsheet;

    @Test
    public void testFromUpload() {
        headerPage.loadFile("picture_sheet.xlsx", this);
        testBench(driver).waitForVaadin();
        spreadsheet = $(SpreadsheetElement.class).first();

        assertInRange(200, imageWidth("C2"), 260);

        assertInRange(240, imageWidth("G4"), 260);

        assertInRange(340, imageWidth("K2"), 360);

        assertInRange(15, imageWidth("R2"), 25);
    }

    public double imageWidth(String cell) {
        WebElement image = spreadsheet.findElement(
                By.xpath(sheetController.cellToXPath(cell) + "/img"));
        return image.getSize().width;
    }
}
