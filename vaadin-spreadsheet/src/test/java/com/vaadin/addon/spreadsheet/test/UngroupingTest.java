package com.vaadin.addon.spreadsheet.test;

import static org.junit.Assert.assertFalse;

import java.util.List;

import org.junit.Test;
import org.openqa.selenium.WebElement;

import com.vaadin.addon.spreadsheet.test.pageobjects.SpreadsheetPage;

public class UngroupingTest extends AbstractSpreadsheetTestCase {


    /**
     * Ticket 599#
     */
    @Test
    public void grouping_expandColumnGroup_groupingElementsHaveCorrectValues() throws Exception {
        loadPage("ungrouping_cellUpdating.xlsx");
        SpreadsheetPage spreadsheetPage = new SpreadsheetPage(driver);
        List<WebElement> groupings = spreadsheetPage.getGroupings();
        groupings.get(1).click();
        String cellValue = spreadsheetPage.getCellValue(3, 1);
        assertFalse(cellValue.contains("#"));
    }

}
