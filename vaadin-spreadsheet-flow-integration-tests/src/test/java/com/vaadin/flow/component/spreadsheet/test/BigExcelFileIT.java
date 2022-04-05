package com.vaadin.flow.component.spreadsheet.test;

import com.vaadin.flow.component.spreadsheet.testbench.SpreadsheetElement;
import com.vaadin.testbench.annotations.RunLocally;
import com.vaadin.testbench.parallel.Browser;
import com.vaadin.tests.AbstractParallelTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

@RunLocally(Browser.CHROME)
public class BigExcelFileIT extends AbstractParallelTest {

    private TestHelpers testHelpers;

    @Before
    public void init() {
        String url = getBaseURL().replace(super.getBaseURL(),
                super.getBaseURL() + "/vaadin-spreadsheet");
        getDriver().get(url);
        testHelpers = new TestHelpers(getDriver());
    }

    @Test
    public void openSpreadsheet_fromExcelFileWith_100_000_Rows_theContentIsRendered() throws Exception {
        SpreadsheetElement spreadsheet = testHelpers.loadFile("100_000_rows.xlsx");

        Assert.assertEquals("File opened", spreadsheet.getCellAt("A1").getValue());
    }
}
