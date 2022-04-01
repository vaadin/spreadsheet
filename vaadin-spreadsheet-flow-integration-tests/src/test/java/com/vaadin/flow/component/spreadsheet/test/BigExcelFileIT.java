package com.vaadin.flow.component.spreadsheet.test;

import com.vaadin.flow.component.combobox.testbench.ComboBoxElement;
import com.vaadin.flow.component.spreadsheet.testbench.SpreadsheetElement;
import com.vaadin.testbench.annotations.RunLocally;
import com.vaadin.testbench.parallel.Browser;
import com.vaadin.tests.AbstractParallelTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebElement;

@RunLocally(Browser.CHROME)
public class BigExcelFileIT extends AbstractParallelTest {

    @Before
    public void init() {
        String url = getBaseURL().replace(super.getBaseURL(),
                super.getBaseURL() + "/vaadin-spreadsheet");
        getDriver().get(url);
    }

    @Test
    public void openSpreadsheet_fromExcelFileWith_100_000_Rows_theContentIsRendered() throws Exception {
        SpreadsheetElement spreadsheet = loadFile("100_000_rows.xlsx");

        Assert.assertEquals("File opened", spreadsheet.getCellAt("A1").getValue());
    }

    private SpreadsheetElement loadFile(String fileName) {
        ComboBoxElement testSheetSelect = $(ComboBoxElement.class).id("testSheetSelect");
        testSheetSelect.selectByText(fileName);

        WebElement updateBtn = $("vaadin-button").id("update");
        updateBtn.click();

        return $(SpreadsheetElement.class).first();
    }
}
