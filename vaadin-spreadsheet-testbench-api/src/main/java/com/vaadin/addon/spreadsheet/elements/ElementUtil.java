/**
 * Copyright 2019 HealthPivots, all rights reserved
 *
 * @Created: Apr 10, 2019
 */
package com.vaadin.addon.spreadsheet.elements;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import com.vaadin.testbench.TestBenchDriverProxy;

/**
 * ElementUtil
 */
public class ElementUtil {

    /**
     * Helper to work around lack of PhantomJS direct support for contextClick events.
     * May need to wait for the context click to appear after calling.
     * 
     * @param driver
     * @param webElement

     * @see com.vaadin.testbench.TestBenchElement#contextClick()
     * @see "https://github.com/ariya/phantomjs/issues/14005"
     */
    public static void phantomJSContextClick(WebDriver driver, WebElement webElement) {
        WebDriver actualDriver = driver;
        if (actualDriver instanceof TestBenchDriverProxy) {
            actualDriver = ((TestBenchDriverProxy)actualDriver).getActualDriver();
        }
        if (actualDriver instanceof PhantomJSDriver) {
            String script =
                    "var element = arguments[0];" +
                    "var event = document.createEvent('HTMLEvents');" +
                    "event.initEvent('contextmenu', true, false);" +
                    "element.dispatchEvent(event);";
            ((JavascriptExecutor)driver)
                .executeScript(script, new Object[]{webElement});
        }
    }

    private ElementUtil() {
        // unused
    }

}
