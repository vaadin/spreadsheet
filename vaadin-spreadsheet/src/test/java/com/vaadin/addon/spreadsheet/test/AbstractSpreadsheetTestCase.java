/*
 * Vaadin Spreadsheet Addon
 *
 * Copyright (C) 2013-2025 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.addon.spreadsheet.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Locale;

import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.vaadin.addon.spreadsheet.test.demoapps.SpreadsheetDemoUI;
import com.vaadin.addon.spreadsheet.test.pageobjects.HeaderPage;
import com.vaadin.addon.spreadsheet.test.tb3.MultiBrowserTest;
import com.vaadin.addon.spreadsheet.test.testutil.SheetController;
import com.vaadin.testbench.elements.NativeSelectElement;

public abstract class AbstractSpreadsheetTestCase extends MultiBrowserTest {

    protected HeaderPage headerPage;
    protected SheetController sheetController;

    @Before
    public void setUp() throws Exception {
        openTestURL();
        headerPage = new HeaderPage(getDriver());
        sheetController = new SheetController(driver, testBench(driver),
                getDesiredCapabilities());
    }

    @Override
    protected Class<?> getUIClass() {
        return SpreadsheetDemoUI.class;
    }

    protected void assertInRange(double from, double value, double to) {
        assertTrue("Value [" + value + "] is not in range: [" + from + " - "
                + to + "]", value >= from && value <= to);
    }

    protected void assertNoErrorIndicatorDetected() {
        assertTrue("Error indicator detected when there should be none.",
                findElements(By.className("v-errorindicator")).isEmpty());
    }

    protected void assertAddressFieldValue(String expected, String actual) {
        assertEquals(
                "Expected " + expected + " on addressField, actual:" + actual,
                expected, actual);
    }

    protected void assertNotSelectedCell(String cell, boolean selected) {
        assertFalse("Cell " + cell + " should not be selected cell", selected);
    }

    protected void assertSelectedCell(String cell, boolean selected) {
        assertTrue("Cell " + cell + " should be the selected cell", selected);
    }

    protected void waitForElementPresent(By locator) {
        waitUntil(ExpectedConditions.presenceOfElementLocated(locator));
    }

    protected void setLocale(Locale locale) {
        $(NativeSelectElement.class).id("localeSelect")
                .selectByText(locale.getDisplayName());
        assertEquals("Unexpected locale,", locale.getDisplayName(),
                $(NativeSelectElement.class).id("localeSelect").getValue());
    }

    /**
     * Navigates with theme parameter to spread sheet file. This way is
     * necessary to change the theme.
     *
     * @param theme
     *            theme to load
     */
    protected void loadPage(String theme, String spreadsheetFile)
            throws Exception {
        driver.get(getTestUrl() + "?theme=" + theme);
        headerPage.loadFile(spreadsheetFile, this);
        testBench(driver).waitForVaadin();
    }

    /**
     * Navigates with file fragment.
     *
     * @param spreadsheetFile
     *            file to load
     */
    protected void loadPage(String spreadsheetFile) throws Exception {
        driver.get(getTestUrl() + "#file/" + spreadsheetFile);
        testBench(driver).waitForVaadin();
    }

    protected void clearLog() {
        List<WebElement> buttons = findElements(
                By.className("v-debugwindow-button"));
        for (int i = 0; i < buttons.size(); i++) {
            WebElement button = buttons.get(i);
            String title = button.getAttribute("title");
            if (title != null && title.startsWith("Clear log")) {
                testBench().waitForVaadin();
                button.click();
                break;
            }
        }
    }

    protected double getSize(String size) {
        return Double.parseDouble(size.replaceAll("[^.0-9]", ""));
    }
}
