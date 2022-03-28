package com.vaadin.flow.component.spreadsheet.tests;

import com.vaadin.testbench.annotations.RunLocally;
import com.vaadin.testbench.parallel.Browser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

@RunLocally(Browser.CHROME)
public class SpreadSheetIT extends AbstractParallelTest {

    @Before
    public void init() {
        getDriver().get(getBaseURL());
    }

    @Test
    public void spreadsheetIsPresent() {
        Assert.assertTrue(true);
    }
}
