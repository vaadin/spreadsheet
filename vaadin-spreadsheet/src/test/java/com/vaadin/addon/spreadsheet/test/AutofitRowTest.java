package com.vaadin.addon.spreadsheet.test;

import static com.vaadin.addon.spreadsheet.test.testutil.CellHeightChecker.assertThatCellHeightIsAcceptable;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.vaadin.addon.spreadsheet.elements.SheetCellElement;
import com.vaadin.addon.spreadsheet.elements.SpreadsheetElement;

public class AutofitRowTest extends AbstractSpreadsheetTestCase {

    public static final int HUGE_FONT_SIZE = 48;
    private static final int ROW_TOO_SMALL = 4;
    private static final float ROW_AUTOFIT_SECURITY_MARGIN = 0.15f;

    @Test
    @Ignore
    public void autoFitRow_doubleClickOnRowBoundaryOfATooSmallRow_rowIsAutofitted()
        throws InterruptedException {
        final SpreadsheetElement spreadsheet = loadImageFile();

        spreadsheet.getRowHeader(ROW_TOO_SMALL).getResizeHandle().doubleClick();

        SheetCellElement cell = spreadsheet.getCellAt(ROW_TOO_SMALL, 1);
        int height = cell.getSize().getHeight();

        assertThatCellHeightIsAcceptable(height, HUGE_FONT_SIZE, ROW_AUTOFIT_SECURITY_MARGIN, 1);
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
