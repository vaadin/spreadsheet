package com.vaadin.addon.spreadsheet.test;

import java.io.IOException;

import org.junit.Test;

/**
 * MergedAlignmentTest
 */
public class MergedAlignmentTest extends AbstractSpreadsheetTestCase {

    /**
     * alignment_spreadsheetWithMergedCellsWithAlignedContents_correctAlignments
     * @throws InterruptedException
     * @throws IOException
     */
    @Test
    public void alignment_spreadsheetWithMergedCellsWithAlignedContents_correctAlignments()
            throws InterruptedException, IOException {

        headerPage.loadFile("merged_aligned.xlsx", this);
        compareScreen("alignment_with_merged_cells");
    }


}
