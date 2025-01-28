/*
 * Vaadin Spreadsheet Addon
 *
 * Copyright (C) 2013-2025 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.addon.spreadsheet.test;

import org.junit.Test;

public class GroupingRenderTest extends AbstractSpreadsheetTestCase {

    /**
     * SHEET-77
     */
    @Test
    public void testRenderingLargeGrouping() throws Exception {

        headerPage.loadFile("large-grouped.xlsx", this);
        compareScreen("grouping_render_large");
    }
}
