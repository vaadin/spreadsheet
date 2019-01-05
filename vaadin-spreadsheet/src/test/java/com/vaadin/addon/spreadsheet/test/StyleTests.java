package com.vaadin.addon.spreadsheet.test;

import java.io.IOException;

import org.junit.Test;

import com.vaadin.addon.spreadsheet.test.fixtures.TestFixtures;
import com.vaadin.addon.spreadsheet.test.testutil.SheetController;

/**
 * StyleTests
 */
public class StyleTests extends AbstractSpreadsheetTestCase {

    /**
     * sheetController
     */
    private SheetController sheetController;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        sheetController = new SheetController(driver, testBench(driver),
                getDesiredCapabilities());
    }

    /**
     * cellBorders_mergeCells_NeighborCellsKeepBorderStyles
     * @throws IOException
     */
    @Test
    public void cellBorders_mergeCells_NeighborCellsKeepBorderStyles() throws IOException {
        headerPage.loadFile("merged_borders.xlsx", this);
        headerPage.loadTestFixture(TestFixtures.StyleMergeReigions);

        compareScreen("merged_borders");
    }
}
