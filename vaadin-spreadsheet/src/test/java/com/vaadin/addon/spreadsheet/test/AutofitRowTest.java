package com.vaadin.addon.spreadsheet.test;

import java.util.List;

import org.junit.Test;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.vaadin.addon.spreadsheet.elements.SpreadsheetElement;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.annotations.RunLocally;
import com.vaadin.testbench.parallel.Browser;

@RunLocally(Browser.FIREFOX)
public class AutofitRowTest extends AbstractSpreadsheetTestCase {

    private static final int ROW_TOO_SMALL = 4;

    @Test
    public void autoFitRow_doubleClickOnRowBoundaryWithTooSmall_rowIsAutofitted()
        throws InterruptedException {
        final SpreadsheetElement spreadsheet = loadImageFile();

        TestBenchElement resizeHandle = spreadsheet.getRowHeader(ROW_TOO_SMALL)
            .getResizeHandle();

        System.out.println(
            "---> ###### before " + spreadsheet.getCellAt(ROW_TOO_SMALL, 1)
                .getSize().getHeight());

        resizeHandle.doubleClick();

        System.out.println(
            "---> ###### after " + spreadsheet.getCellAt(ROW_TOO_SMALL, 1)
                .getSize().getHeight());

    }

    private SpreadsheetElement loadImageFile() {
        headerPage.loadFile("rows_autofit_util.xlsx", this);
        return $(SpreadsheetElement.class).first();
    }

    @Override
    public List<DesiredCapabilities> getBrowsersToTest() {
        return getBrowsersExcludingPhantomJS();
    }
}
