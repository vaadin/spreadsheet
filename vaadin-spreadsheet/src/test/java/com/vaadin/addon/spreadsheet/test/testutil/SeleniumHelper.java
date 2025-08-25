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

import org.openqa.selenium.WebDriver;

public class SeleniumHelper {

    protected WebDriver driver;

    public SeleniumHelper(WebDriver driver) {
        this.driver = driver;
    }
}
