package com.vaadin.addon.spreadsheet.test.testutil;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.vaadin.testbench.By;

/**
 * PageHelper
 */
public class PageHelper extends SeleniumHelper {

    /**
     * @param driver
     */
    public PageHelper(WebDriver driver) {
        super(driver);
    }

    /**
     * assertTextPresent
     * @param text
     */
    public void assertTextPresent(String text) {
        List<WebElement> list = driver.findElements(By
                .xpath("//*[contains(text(),'" + text + "')]"));
        Assert.assertTrue("'" + text + "' not found in page!", list.size() > 0);
    }

    /**
     * assertTextPresent
     * @param textOptions
     */
    public void assertTextPresent(String[] textOptions) {
        boolean found = false;
        for (String text : textOptions) {
            List<WebElement> list = driver.findElements(By
                    .xpath("//*[contains(text(),'" + text + "')]"));
            if (list.size() > 0) {
                found = true;
                break;
            }
        }
        Assert.assertTrue("None of texts '" + Arrays.toString(textOptions)
                + "' found in page!", found);
    }

    /**
     * assertUrlContains
     * @param expectedUrl
     */
    public void assertUrlContains(String expectedUrl) {
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue("Current URL expectation failed: expected '"
                + expectedUrl + "' is not contained in '" + currentUrl + "'",
                currentUrl.contains(expectedUrl));
    }

}
