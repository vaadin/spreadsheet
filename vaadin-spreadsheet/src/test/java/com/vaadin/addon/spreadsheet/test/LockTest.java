package com.vaadin.addon.spreadsheet.test;

import com.vaadin.addon.spreadsheet.elements.SheetCellElement;
import com.vaadin.addon.spreadsheet.elements.SpreadsheetElement;
import com.vaadin.addon.spreadsheet.test.fixtures.TestFixtures;
import org.junit.Test;
import org.openqa.selenium.WebDriverException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class LockTest extends AbstractSpreadsheetTestCase {

    private SpreadsheetElement spreadSheet;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        headerPage.createNewSpreadsheet();
        spreadSheet = $(SpreadsheetElement.class).first();
    }

    @Test(expected = WebDriverException.class)
    public void testLockedCells() {

        SheetCellElement b2 = spreadSheet.getCellAt("B2");
        b2.setValue("value");

        sheetController.selectRegion("B2","D4");
        headerPage.loadTestFixture(TestFixtures.LockCell);

        assertThat(b2.getValue(), equalTo("value"));
        b2.setValue("new value on locked cell"); // expect exception
    }

    @Test
    public void testUnlockCell() {

        SheetCellElement b2 = spreadSheet.getCellAt("B2");
        b2.setValue("value");
        sheetController.selectRegion("B2","D4");
        headerPage.loadTestFixture(TestFixtures.LockCell);

        assertThat(b2.getValue(), equalTo("value"));
        sheetController.selectRegion("B2","D2");
        headerPage.loadTestFixture(TestFixtures.UnlockCell); // unlock a region

        b2.setValue("cell is now unlocked");
        assertThat(b2.getValue(), equalTo("cell is now unlocked"));
    }
}
