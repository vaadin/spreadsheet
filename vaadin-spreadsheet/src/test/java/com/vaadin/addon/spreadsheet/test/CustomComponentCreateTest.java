/*
 * Vaadin Spreadsheet Addon
 *
 * Copyright (C) 2013-2024 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.addon.spreadsheet.test;

import org.junit.Test;

import com.vaadin.addon.spreadsheet.test.fixtures.TestFixtures;

public class CustomComponentCreateTest extends AbstractSpreadsheetTestCase {

    /**
     * Ticket #18546
     *
     * Note that this screenshot tests different themes so hence page must be
     * loaded between screenshot comparisons.
     */
    @Test
    public void CustomEditor_AddEditor_DisplayEditorImmediately()
            throws Exception {

        headerPage.createNewSpreadsheet();
        headerPage.loadTestFixture(TestFixtures.CustomEditor);
        compareScreen("customeditor_addcell_displayeditor");
    }

}
