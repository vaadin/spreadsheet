package com.vaadin.flow.component.spreadsheet.test;

import com.vaadin.testbench.annotations.RunLocally;
import com.vaadin.testbench.parallel.Browser;
import com.vaadin.tests.AbstractParallelTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebElement;

@RunLocally(Browser.CHROME)
public class SpreadSheetIT extends AbstractParallelTest {

    WebElement spreadsheet;

    @Before
    public void init() {
        String url = getBaseURL().replace(super.getBaseURL(),
                super.getBaseURL() + "/vaadin-spreadsheet");
        getDriver().get(url);
    }

    @Test
    public void editColumnsAdded() {
        $("vaadin-button").id("createNewBtn").click();
        spreadsheet = $("vaadin-spreadsheet").waitForFirst();
        Assert.assertNotNull(spreadsheet);
    }
}
