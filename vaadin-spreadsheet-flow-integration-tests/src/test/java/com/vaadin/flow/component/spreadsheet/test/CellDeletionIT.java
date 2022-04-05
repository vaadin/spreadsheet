package com.vaadin.flow.component.spreadsheet.test;

import com.vaadin.flow.component.spreadsheet.testbench.SpreadsheetElement;
import com.vaadin.flow.component.spreadsheet.tests.fixtures.TestFixtures;
import com.vaadin.testbench.annotations.RunLocally;
import com.vaadin.testbench.parallel.Browser;
import com.vaadin.tests.AbstractParallelTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.List;
import java.util.stream.Collectors;

@RunLocally(Browser.CHROME)
public class CellDeletionIT extends AbstractParallelTest {

    private SpreadsheetElement spreadsheet;
    private TestHelpers testHelpers;

    @Before
    public void init() {
        String url = getBaseURL().replace(super.getBaseURL(),
                super.getBaseURL() + "/vaadin-spreadsheet");
        getDriver().get(url);
        
        testHelpers = new TestHelpers(getDriver());
        spreadsheet = testHelpers.createNewSpreadsheet();
        testHelpers.setSpreadsheetElement(spreadsheet);

        testHelpers.loadTestFixture(TestFixtures.DeletionHandler);
    }

    @Test
    public void deletionHandler_SpreadsheetWithDeletionFixture_deleteSingleCellSucceedsWhenHandlerReturnsTrue() {
        testHelpers.clickCell("B2");

        new Actions(getDriver()).sendKeys(Keys.DELETE).build().perform();

        Assert.assertEquals("", testHelpers.getCellContent("B2"));

        assertNotificationContent("Deleting: 1:1");
    }

    @Test
    public void deletionHandler_SpreadsheetWithDeletionFixture_deleteSingleCellFailsWhenHandlerReturnsFalse() {
        testHelpers.clickCell("C2");

        new Actions(getDriver()).sendKeys(Keys.DELETE).build().perform();

        Assert.assertEquals("Try to delete me!", testHelpers.getCellContent("C2"));

        assertNotificationContent("Attempting to delete: 1:2");
    }

    @Test
    public void deletionHandler_SpreadsheetWithDeletionFixture_deleteIndividualCellSucceedsWhenHandlerReturnsTrue() {
        testHelpers.clickCell("B3");
        spreadsheet.getCellAt("B5").click(5, 5,
                Keys.CONTROL);

        new Actions(getDriver()).sendKeys(Keys.DELETE).build().perform();

        Assert.assertEquals("", testHelpers.getCellContent("B3"));
        Assert.assertEquals("", testHelpers.getCellContent("B5"));

        assertNotificationContent("Deleting: 2:1;4:1");
    }

    @Test
    public void deletionHandler_SpreadsheetWithDeletionFixture_deleteIndividualCellFailsWhenHandlerReturnsFalse() {
        testHelpers.clickCell("C3");
        spreadsheet.getCellAt("C5").click(5, 5,
                Keys.CONTROL);

        new Actions(getDriver()).sendKeys(Keys.DELETE).build().perform();

        Assert.assertEquals("Try to delete us too!", testHelpers.getCellContent("C3"));
        Assert.assertEquals("Try to delete us too!", testHelpers.getCellContent("C5"));

        assertNotificationContent("Attempting to delete: 2:2;4:2");
    }

    @Test
    public void deletionHandler_SpreadsheetWithDeletionFixture_deleteCellRangeSucceedsWhenHandlerReturnsTrue() {
        testHelpers.clickCell("B6");
        spreadsheet.getCellAt("B8").click(5, 5,
                Keys.SHIFT);

        new Actions(getDriver()).sendKeys(Keys.DELETE).build().perform();

        Assert.assertEquals("", testHelpers.getCellContent("B6"));
        Assert.assertEquals("", testHelpers.getCellContent("B7"));
        Assert.assertEquals("", testHelpers.getCellContent("B8"));

        assertNotificationContent("Deleting: 5:1-7:1");
    }

    @Test
    public void deletionHandler_SpreadsheetWithDeletionFixture_deleteCellRangeFailsWhenHandlerReturnsFalse() {
        testHelpers.clickCell("C6");
        spreadsheet.getCellAt("C8").click(5, 5,
                Keys.SHIFT);

        new Actions(getDriver()).sendKeys(Keys.DELETE).build().perform();

        Assert.assertEquals("Try to delete this range!", testHelpers.getCellContent("C6"));
        Assert.assertEquals("Try to delete this range!", testHelpers.getCellContent("C7"));
        Assert.assertEquals("Try to delete this range!", testHelpers.getCellContent("C8"));

        assertNotificationContent("Attempting to delete: 5:2-7:2");
    }

    private void assertNotificationContent(String expected) {
        List<String> notifications = getNotifications().stream()
                .map(WebElement::getText).collect(Collectors.toList());
        Assert.assertTrue(String.format(
                        "Expected any of the notifications to contain the string '%s' but neither of them did. Notifications: '%s'",
                        expected, notifications),
                notifications.stream().anyMatch(
                        notification -> notification.contains(expected)));
    }

    private List<WebElement> getNotifications() {
        return findElements(By.tagName("vaadin-notification-card"));
    }
}
