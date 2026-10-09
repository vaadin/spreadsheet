package com.vaadin.addon.spreadsheet.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;

import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.vaadin.addon.spreadsheet.elements.SpreadsheetElement;
import com.vaadin.addon.spreadsheet.test.demoapps.LocalizationTestUI;
import com.vaadin.testbench.annotations.BrowserConfiguration;
import com.vaadin.testbench.parallel.Browser;

public class SheetTabLocalizationTest extends AbstractSpreadsheetTestCase {

    @Override
    protected Class<?> getUIClass() {
        return LocalizationTestUI.class;
    }

    @Override
    @BrowserConfiguration
    public List<DesiredCapabilities> getBrowsersToTest() {
        return getBrowserCapabilities(Browser.CHROME);
    }

    @After
    public void restoreLocalizedStrings() {
        if (getDriver() != null
                && !getDriver().findElements(By.id("restore")).isEmpty()) {
            clickControl("restore");
        }
    }

    @Test
    public void changedDefaultAndLocale_updateSheetTabCaptions() {
        assertDeleteCaptions("Delete sheet");
        clickControl("translated");
        assertDeleteCaptions("Supprimer");
        clickControl("untranslated");
        assertDeleteCaptions("Delete sheet");

        clickControl("default");
        assertDeleteCaptions("Remove");
        clickControl("changed-default");
        assertDeleteCaptions("Delete worksheet");
        $(SpreadsheetElement.class).first().addSheet();
        assertDeleteCaptions("Delete worksheet");

        clickControl("default");
        assertDeleteCaptions("Remove");
        clickControl("translated");
        assertDeleteCaptions("Supprimer");
        clickControl("changed-default");
        assertDeleteCaptions("Supprimer");
        clickControl("untranslated");
        assertDeleteCaptions("Delete worksheet");
        clickControl("translated");
        assertDeleteCaptions("Supprimer");
        clickControl("reset");
        assertDeleteCaptions("Delete sheet");
        clickControl("untranslated");
        assertDeleteCaptions("Delete sheet");
        clickControl("translated");
        assertDeleteCaptions("Delete sheet");
        clickControl("default");
        assertDeleteCaptions("Remove");
        clickControl("changed-default");
        assertDeleteCaptions("Delete worksheet");
    }

    private void clickControl(String id) {
        getDriver().findElement(By.id(id)).click();
        testBench().waitForVaadin();
    }

    private void assertDeleteCaptions(String expected) {
        testBench().waitForVaadin();
        List<WebElement> controls = getDriver().findElements(
                By.className("sheet-tabsheet-delete"));
        assertFalse("Expected sheet delete controls", controls.isEmpty());
        for (WebElement control : controls) {
            assertEquals(expected, control.getAttribute("title"));
            assertEquals(expected, control.getAttribute("aria-label"));
        }
    }
}