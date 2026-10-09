/*
 * Vaadin Spreadsheet Addon
 *
 * Copyright (C) 2013-2026 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.addon.spreadsheet.test.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.vaadin.addon.spreadsheet.Spreadsheet;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class SheetDeleteTest {

    private DialogSpreadsheet dialogSpreadsheet;
    private UI ui;

    @Before
    public void setUp() {
        dialogSpreadsheet = new DialogSpreadsheet();
        ui = new TestUI();
        ui.setLocale(Locale.getDefault());
        UI.setCurrent(ui);
        ui.setContent(dialogSpreadsheet);
    }

    @After
    public void tearDown() {
        UI.setCurrent(null);
    }

    @Test
    public void onSheetDelete_workbookIsReadOnly_doesNothing() {
        TestSpreadsheet spreadsheet = createSpreadsheetWithTwoSheets();
        spreadsheet.setWorkbookReadOnly(true);

        spreadsheet.requestSheetDelete(1);

        assertEquals(2, spreadsheet.getNumberOfSheets());
        assertFalse(spreadsheet.isConfirmationShown());
    }

    @Test
    public void onSheetDelete_sheetIsEmptyAndUnprotected_deletesImmediately() {
        TestSpreadsheet spreadsheet = createSpreadsheetWithTwoSheets();

        spreadsheet.requestSheetDelete(1);

        assertEquals(1, spreadsheet.getNumberOfSheets());
        assertFalse(spreadsheet.isConfirmationShown());
    }

    @Test
    public void onSheetDelete_lastSheet_createsReplacementSheet() {
        TestSpreadsheet spreadsheet = new TestSpreadsheet();

        spreadsheet.requestSheetDelete(0);

        assertEquals(1, spreadsheet.getNumberOfSheets());
        assertEquals("Sheet1", spreadsheet.getWorkbook().getSheetName(0));
        assertFalse(spreadsheet.isConfirmationShown());
    }

    @Test
    public void onSheetDelete_sheetHasContent_showsConfirmation() {
        TestSpreadsheet spreadsheet = createSpreadsheetWithTwoSheets();
        spreadsheet.createCell(0, 0, "Content");

        spreadsheet.requestSheetDelete(1);

        assertEquals(2, spreadsheet.getNumberOfSheets());
        assertTrue(spreadsheet.isConfirmationShown());
    }

    @Test
    public void onSheetDelete_sheetIsEmptyAndProtected_showsConfirmation() {
        TestSpreadsheet spreadsheet = createSpreadsheetWithTwoSheets();
        spreadsheet.setSheetProtected(1, "password");

        spreadsheet.requestSheetDelete(1);

        assertEquals(2, spreadsheet.getNumberOfSheets());
        assertTrue(spreadsheet.isConfirmationShown());
    }

    @Test
    public void onSheetDelete_deleteConfirmed_deletesSheet() {
        dialogSpreadsheet.createNewSheet("Second", 10, 10);
        dialogSpreadsheet.createCell(0, 0, "Content");

        dialogSpreadsheet.requestSheetDelete(1);

        clickDialogButton(1);

        assertEquals(1, dialogSpreadsheet.getNumberOfSheets());
        assertTrue(ui.getWindows().isEmpty());
    }

    @Test
    public void onSheetDelete_deleteCancelled_keepsSheet() {
        dialogSpreadsheet.createNewSheet("Second", 10, 10);
        dialogSpreadsheet.createCell(0, 0, "Content");

        dialogSpreadsheet.requestSheetDelete(1);

        clickDialogButton(0);

        assertEquals(2, dialogSpreadsheet.getNumberOfSheets());
        assertTrue(ui.getWindows().isEmpty());
    }

    @Test
    public void onSheetDelete_lastSheetDeleteConfirmed_createsReplacementSheet() {
        dialogSpreadsheet.createCell(0, 0, "Content");

        dialogSpreadsheet.requestSheetDelete(0);
        clickDialogButton(1);

        assertEquals(1, dialogSpreadsheet.getNumberOfSheets());
        assertEquals("Sheet1",
                dialogSpreadsheet.getWorkbook().getSheetName(0));
    }

    private void clickDialogButton(int buttonIndex) {
        Window confirmation = ui.getWindows().iterator().next();
        VerticalLayout content = (VerticalLayout) confirmation.getContent();
        HorizontalLayout actions = (HorizontalLayout) content.getComponent(1);
        ((Button) actions.getComponent(buttonIndex)).click();
    }

    private TestSpreadsheet createSpreadsheetWithTwoSheets() {
        TestSpreadsheet spreadsheet = new TestSpreadsheet();
        spreadsheet.createNewSheet("Second", 10, 10);
        return spreadsheet;
    }

    private static class TestSpreadsheet extends Spreadsheet {
        private boolean confirmationShown;

        void requestSheetDelete(int sheetIndex) {
            onSheetDelete(sheetIndex);
        }

        void setWorkbookReadOnly(boolean readOnly) {
            getState().workbookProtected = readOnly;
        }

        boolean isConfirmationShown() {
            return confirmationShown;
        }

        @Override
        protected void showSheetDeleteConfirmation(int sheetIndex) {
            confirmationShown = true;
        }
    }

    private static class DialogSpreadsheet extends Spreadsheet {
        void requestSheetDelete(int sheetIndex) {
            onSheetDelete(sheetIndex);
        }
    }

    private static class TestUI extends UI {
        @Override
        protected void init(VaadinRequest request) {
        }
    }
}
