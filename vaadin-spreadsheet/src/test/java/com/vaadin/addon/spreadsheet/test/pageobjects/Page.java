package com.vaadin.addon.spreadsheet.test.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.vaadin.testbench.TestBenchTestCase;
import com.vaadin.testbench.elements.ButtonElement;

/**
 * Page
 */
public class Page extends TestBenchTestCase {
    /**
     * @param driver
     */
    public Page(WebDriver driver) {
        setDriver(driver);
    }

    /**
     * buttonWithCaption
     * @param caption
     * @return WebElement
     */
    protected WebElement buttonWithCaption(String caption) {
        return $(ButtonElement.class).caption(caption).first();
    }
}
