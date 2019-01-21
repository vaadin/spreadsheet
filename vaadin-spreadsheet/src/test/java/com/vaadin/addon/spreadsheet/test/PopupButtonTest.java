package com.vaadin.addon.spreadsheet.test;

import static org.junit.Assert.assertTrue;
import java.io.IOException;

import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import com.vaadin.addon.spreadsheet.elements.SheetCellElement;
import com.vaadin.addon.spreadsheet.elements.SpreadsheetElement;
import com.vaadin.addon.spreadsheet.test.fixtures.TestFixtures;

/**
 * PopupButtonTest
 */
public class PopupButtonTest extends AbstractSpreadsheetTestCase {

    @Override
    public void setUp() throws Exception {
        super.setUp();
        headerPage.createNewSpreadsheet();
    }

    /**
     * popupButton_addAndShowListSelectPopup_PopupShownCorrectly
     */
    @Test
    public void popupButton_addAndShowListSelectPopup_PopupShownCorrectly() {
        headerPage.loadTestFixture(TestFixtures.PopupButton);
        SpreadsheetElement spreadsheetElement = $(SpreadsheetElement.class)
                .first();
        final SheetCellElement d1 = spreadsheetElement.getCellAt("D1");
        d1.click();

        waitUntil(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                return d1.hasPopupButton();
            }
        });

        assertTrue(spreadsheetElement.isPopupButtonPopupVisible());
    }

    /**
     * popupButton_addAndShowTabsheetPopup_PopupShownCorrectly
     * @throws IOException
     */
    @Test
    public void popupButton_addAndShowTabsheetPopup_PopupShownCorrectly()
            throws IOException {
        headerPage.loadTestFixture(TestFixtures.TabsheetPopupButton);
        SpreadsheetElement spreadsheetElement = $(SpreadsheetElement.class)
                .first();
        final SheetCellElement d1 = spreadsheetElement.getCellAt("D1");
        d1.click();

        waitUntil(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                return d1.hasPopupButton();
            }
        });

        d1.popupButtonClick();

        assertTrue("PopupButton popup not visible",
                spreadsheetElement.isPopupButtonPopupVisible());

        compareScreen("popupbutton_tabsheet");
    }

    /**
     * popupButton_addAndShowTablePopup_PopupShownCorrectly
     * @throws IOException
     */
    @Test
    public void popupButton_addAndShowTablePopup_PopupShownCorrectly()
            throws IOException {
        headerPage.loadTestFixture(TestFixtures.TablePopupButton);
        SpreadsheetElement spreadsheetElement = $(SpreadsheetElement.class)
                .first();
        final SheetCellElement d1 = spreadsheetElement.getCellAt("D1");
        d1.click();

        waitUntil(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                return d1.hasPopupButton();
            }
        });

        d1.popupButtonClick();

        assertTrue("PopupButton popup not visible",
                spreadsheetElement.isPopupButtonPopupVisible());

        compareScreen("popupbutton_table");
    }

    /**
     * popupButton_showPopupAndScroll_popupRemoved
     */
    @Test
    public void popupButton_showPopupAndScroll_popupRemoved() {
        headerPage.loadTestFixture(TestFixtures.PopupButton);
        final SpreadsheetElement spreadsheetElement = $(
                SpreadsheetElement.class).first();
        final SheetCellElement d1 = spreadsheetElement.getCellAt("D1");
        d1.click();

        waitUntil(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                return d1.hasPopupButton();
            }
        });

        spreadsheetElement.scroll(1000);

        waitUntil(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                return !spreadsheetElement.isPopupButtonPopupVisible();
            }
        });
    }

    /**
     * popupButton_cellHasAPopupButtonAndFreezePaneIsAdded_theCellStillHasAPopupButton
     */
    @Test
    public void popupButton_cellHasAPopupButtonAndFreezePaneIsAdded_theCellStillHasAPopupButton() {
        headerPage.loadTestFixture(TestFixtures.PopupButton);
        SpreadsheetElement spreadsheetElement = $(SpreadsheetElement.class)
                .first();
        final SheetCellElement d1 = spreadsheetElement.getCellAt("D1");
        d1.click();
        waitUntil(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                return d1.hasPopupButton();
            }
        });

        headerPage.addFreezePane();

        assertTrue(spreadsheetElement.getCellAt("D1").hasPopupButton());
    }

    /**
     * popupButtonCellWidthWideText_changeValues_cellContainsPopupButton
     */
    @Test
    public void popupButtonCellWidthWideText_changeValues_cellContainsPopupButton() {
        headerPage.loadTestFixture(TestFixtures.PopupButton);

        final SpreadsheetElement spreadsheetElement = $(
            SpreadsheetElement.class).first();

        final SheetCellElement cell = spreadsheetElement.getCellAt("D1");

        // these actions trigger addition/removal of inner element, 
        // which used to accidentally remove the popup button and other overlays

        insertValue_assertPopupButtonPresent(cell, "looooooooooooong text");

        insertValue_assertPopupButtonPresent(cell,"");
    }

    private void insertValue_assertPopupButtonPresent(SheetCellElement cell,
        String newValue) {

        cell.setValue(newValue);

        assertTrue(cell.hasPopupButton());
    }
}
