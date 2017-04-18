package com.vaadin.addon.spreadsheet.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;

import com.vaadin.addon.spreadsheet.elements.SheetCellElement;
import com.vaadin.addon.spreadsheet.elements.SpreadsheetElement;
import com.vaadin.addon.spreadsheet.test.fixtures.TestFixtures;
import com.vaadin.testbench.TestBenchElement;

public class PopupButtonTest extends AbstractSpreadsheetTestCase {

    @Override
    public void setUp() throws Exception {
        super.setUp();
        headerPage.createNewSpreadsheet();
    }

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

    @Test
    public void popupButton_HideUnhideColumn_cellContainsPopupButton() {
        headerPage.loadTestFixture(TestFixtures.PopupButton);
        final SpreadsheetElement spreadsheetElement = $(
            SpreadsheetElement.class).first();
        final SheetCellElement cell = spreadsheetElement.getCellAt("D1");

//        spreadsheetElement.getColumnHeader(4).contextClick();
//        spreadsheetElement.getContextMenu().getItem("Hide column D").click();
//
//        spreadsheetElement.getColumnHeader(3).contextClick();
//        spreadsheetElement.getContextMenu().getItem("Unhide column D").click();
//
//
//        waitUntil(new ExpectedCondition<Boolean>() {
//            @Override
//            public Boolean apply(WebDriver webDriver) {
//                return cell.hasPopupButton();
//            }
//        });
//
//        assertTrue(spreadsheetElement.getCellAt("D1").hasPopupButton());

        TestBenchElement resizeHandle = spreadsheetElement.getColumnHeader(4)
            .getResizeHandle();

        int width = Integer
            .valueOf(cell.getCssValue("width").replaceAll("px", ""));

        Actions actions = new Actions(driver);
        actions.dragAndDropBy(resizeHandle, -1 * width, 0).perform();

        assertEquals("0px", cell.getCssValue("width"));

        actions.dragAndDropBy(resizeHandle, width, 0);

        assertEquals(width, cell.getCssValue("width"));

        waitUntil(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                return cell.hasPopupButton();
            }
        });

        assertTrue(spreadsheetElement.getCellAt("D1").hasPopupButton());
    }
}
