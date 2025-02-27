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
package com.vaadin.addon.spreadsheet.test.testutil;

import java.util.Iterator;
import java.util.Set;

import org.openqa.selenium.WebDriver;

import com.vaadin.testbench.TestBenchTestCase;

public class PopupHelper extends SeleniumHelper {

    private String mainWindowHandle;

    public PopupHelper(WebDriver driver) {
        super(driver);
        mainWindowHandle = driver.getWindowHandle();
    }

    public void switchToPopup() {
        Set<String> handleSet = driver.getWindowHandles();

        Iterator<String> ite = handleSet.iterator();
        while (ite.hasNext()) {
            String popupHandle = ite.next();
            if (!popupHandle.contains(mainWindowHandle)) {
                driver.switchTo().window(popupHandle);
                TestBenchTestCase.testBench(driver).waitForVaadin();
                return;
            }
        }
    }

    public void backToMainWindow() {
        driver.switchTo().window(mainWindowHandle);
        TestBenchTestCase.testBench(driver).waitForVaadin();
    }
}
