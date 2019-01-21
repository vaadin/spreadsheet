package com.vaadin.addon.spreadsheet.test;

import org.junit.Test;

/**
 * GroupingRenderTest
 */
public class GroupingRenderTest extends AbstractSpreadsheetTestCase {

    /**
     * SHEET-77
     * @throws Exception 
     */
    @Test
    public void testRenderingLargeGrouping() throws Exception {

        headerPage.loadFile("large-grouped.xlsx", this);
        compareScreen("grouping_render_large");
    }
}
