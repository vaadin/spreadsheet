package com.vaadin.addon.spreadsheet.test;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;

import com.vaadin.addon.spreadsheet.elements.SpreadsheetElement;
import com.vaadin.addon.spreadsheet.test.testutil.SheetController;

public class ConditionalFormattingOnCellDeletionTest extends AbstractSpreadsheetTestCase {

    private static final String FALSE_CONDITION_COLOR = "rgba(255, 255, 255, 1)";
    private static final String TRUE_CONDITION_COLOR = "rgba(255, 0, 0, 1)";

    private SheetController sheetController;
    private SpreadsheetElement spreadSheet;
    
    @Override
    public void setUp() throws Exception {
        setDebug(true);
        super.setUp();
        headerPage.loadFile("conditional_formatting_with_formula_on_second_sheet.xlsx", this);
        spreadSheet = $(SpreadsheetElement.class).first();
        spreadSheet.selectSheetAt(1);
        sheetController = new SheetController(driver, testBench(driver),
            getDesiredCapabilities());
    }

    @Test
    public void deletionHandler_SpreadsheetWithDeletionFixture_deleteSingleCellFailsWhenHandlerReturnsFalse() {

        assertEquals(FALSE_CONDITION_COLOR, getCellColor());
        
        sheetController.clickCell("A2");
        new Actions(getDriver()).sendKeys(Keys.DELETE).build().perform();

        assertEquals(FALSE_CONDITION_COLOR, getCellColor());

        sheetController.clickCell("A1");
        new Actions(getDriver()).sendKeys(Keys.DELETE).build().perform();

        assertEquals(TRUE_CONDITION_COLOR, getCellColor());
    }

    private String getCellColor() {
        return spreadSheet.getCellAt("A2")
                .getCssValue("background-color");
    }
}
