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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Locale;

import org.apache.poi.ss.usermodel.SheetVisibility;
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
    private boolean savedGlobalSheetDeleteEnabled;

    @Before
    public void setUp() {
        savedGlobalSheetDeleteEnabled = Spreadsheet.isSheetDeleteButtonAllowed();
        Spreadsheet.setSheetDeleteButtonAllowed(true);
        dialogSpreadsheet = new DialogSpreadsheet();
        ui = new TestUI();
        ui.setLocale(Locale.getDefault());
        UI.setCurrent(ui);
        ui.setContent(dialogSpreadsheet);
    }

    @After
    public void tearDown() {
        UI.setCurrent(null);
        Spreadsheet.setSheetDeleteButtonAllowed(savedGlobalSheetDeleteEnabled);
    }

    @Test
    public void sheetDeleteEnabled_newInstance_enabledByDefault() {
        TestSpreadsheet spreadsheet = new TestSpreadsheet();

        assertTrue(spreadsheet.isSheetDeleteButtonAllowed());
        assertTrue(spreadsheet.isClientSheetDeleteEnabled());
    }

    @Test
    public void sheetDeleteEnabled_globalAndLocalSettings_bothMustBeEnabled() {
        TestSpreadsheet spreadsheet = new TestSpreadsheet();

        for (boolean global : new boolean[] { true, false }) {
            Spreadsheet.setSheetDeleteButtonAllowed(global);
            for (boolean local : new boolean[] { true, false }) {
                spreadsheet.setSheetDeleteButtonEnabled(local);

                assertEquals(global && local,
                        spreadsheet.isSheetDeleteButtonAllowed());
                assertEquals(global && local,
                        spreadsheet.isClientSheetDeleteEnabled());
            }
        }
    }

    @Test
    public void sheetDeleteEnabled_localSetting_doesNotAffectOtherInstances() {
        TestSpreadsheet disabled = new TestSpreadsheet();
        TestSpreadsheet enabled = new TestSpreadsheet();

        disabled.setSheetDeleteButtonEnabled(false);

        assertFalse(disabled.isSheetDeleteButtonAllowed());
        assertTrue(enabled.isSheetDeleteButtonAllowed());
        assertTrue(enabled.isClientSheetDeleteEnabled());
    }

    @Test
    public void sheetDeleteEnabled_globalChanges_updateExistingAndNewClients() {
        TestSpreadsheet existing = new TestSpreadsheet();
        Spreadsheet.setSheetDeleteButtonAllowed(false);
        TestSpreadsheet createdWhileDisabled = new TestSpreadsheet();

        existing.beforeClientResponse(false);
        createdWhileDisabled.beforeClientResponse(true);

        assertFalse(existing.isClientSheetDeleteEnabled());
        assertFalse(createdWhileDisabled.isClientSheetDeleteEnabled());

        Spreadsheet.setSheetDeleteButtonAllowed(true);
        existing.beforeClientResponse(false);
        createdWhileDisabled.beforeClientResponse(false);

        assertTrue(existing.isClientSheetDeleteEnabled());
        assertTrue(createdWhileDisabled.isClientSheetDeleteEnabled());
    }

    @Test
    public void sheetDeleteEnabled_globalReenabled_preservesLocalSetting() {
        TestSpreadsheet spreadsheet = new TestSpreadsheet();
        spreadsheet.setSheetDeleteButtonEnabled(false);
        Spreadsheet.setSheetDeleteButtonAllowed(false);
        spreadsheet.beforeClientResponse(false);

        Spreadsheet.setSheetDeleteButtonAllowed(true);
        spreadsheet.beforeClientResponse(false);

        assertFalse(spreadsheet.isSheetDeleteButtonAllowed());
        assertFalse(spreadsheet.isClientSheetDeleteEnabled());
    }

    @Test
    public void onSheetDelete_globallyDisabled_doesNothing() {
        TestSpreadsheet spreadsheet = createSpreadsheetWithTwoSheets();
        Spreadsheet.setSheetDeleteButtonAllowed(false);

        spreadsheet.requestSheetDelete(1);

        assertEquals(2, spreadsheet.getNumberOfSheets());
        assertFalse(spreadsheet.isConfirmationShown());
    }

    @Test
    public void onSheetDelete_locallyDisabled_doesNothing() {
        TestSpreadsheet spreadsheet = createSpreadsheetWithTwoSheets();
        spreadsheet.setSheetDeleteButtonEnabled(false);

        spreadsheet.requestSheetDelete(1);

        assertEquals(2, spreadsheet.getNumberOfSheets());
        assertFalse(spreadsheet.isConfirmationShown());
    }

    @Test
    public void deleteSheet_disabled_stillAllowsProgrammaticDeletion() {
        TestSpreadsheet spreadsheet = createSpreadsheetWithTwoSheets();
        spreadsheet.setSheetDeleteButtonEnabled(false);
        Spreadsheet.setSheetDeleteButtonAllowed(false);

        spreadsheet.deleteSheet(1);

        assertEquals(1, spreadsheet.getNumberOfSheets());
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
        public void sheetProtection_inactiveSheetProtected_updatesOnlyItsTab() {
        TestSpreadsheet spreadsheet = createSpreadsheetWithTwoSheets();

        spreadsheet.setSheetProtected(0, "password");

        assertFalse(spreadsheet.isActiveSheetProtected());
        assertArrayEquals(new boolean[] { true, false },
            spreadsheet.getProtectedSheets());
        }

        @Test
        public void sheetProtection_activeSheetChanged_keepsPerTabProtection() {
        TestSpreadsheet spreadsheet = createSpreadsheetWithTwoSheets();
        spreadsheet.setSheetProtected(0, "password");

            spreadsheet.setActiveSheetIndex(0);

        assertTrue(spreadsheet.isActiveSheetProtected());
        assertArrayEquals(new boolean[] { true, false },
            spreadsheet.getProtectedSheets());
        }

        @Test
        public void sheetProtection_removed_updatesTab() {
        TestSpreadsheet spreadsheet = createSpreadsheetWithTwoSheets();
        spreadsheet.setSheetProtected(0, "password");

        spreadsheet.setSheetProtected(0, null);

        assertArrayEquals(new boolean[] { false, false },
            spreadsheet.getProtectedSheets());
        }

        @Test
        public void sheetProtection_hiddenSheets_keepsVisibleTabOrder() {
        TestSpreadsheet spreadsheet = createSpreadsheetWithTwoSheets();
        spreadsheet.createNewSheet("Third", 10, 10);
        spreadsheet.setSheetProtected(0, "password");
        spreadsheet.setSheetProtected(1, "password");

        spreadsheet.setSheetHidden(0, SheetVisibility.HIDDEN);
        spreadsheet.setSheetHidden(1, SheetVisibility.VERY_HIDDEN);

        assertArrayEquals(new boolean[] { false },
            spreadsheet.getProtectedSheets());

        spreadsheet.setSheetHidden(1, SheetVisibility.VISIBLE);

        assertArrayEquals(new boolean[] { true, false },
            spreadsheet.getProtectedSheets());
        }

        @Test
        public void sheetProtection_tabsAddedAndRemoved_keepsFlagsAligned() {
        TestSpreadsheet spreadsheet = createSpreadsheetWithTwoSheets();
        spreadsheet.setSheetProtected(0, "password");

        spreadsheet.createNewSheet("Third", 10, 10);

        assertArrayEquals(new boolean[] { true, false, false },
            spreadsheet.getProtectedSheets());

        spreadsheet.deleteSheet(0);

        assertArrayEquals(new boolean[] { false, false },
            spreadsheet.getProtectedSheets());
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
    public void onSheetDelete_globallyDisabledAfterConfirmation_keepsSheet() {
        dialogSpreadsheet.createNewSheet("Second", 10, 10);
        dialogSpreadsheet.createCell(0, 0, "Content");
        dialogSpreadsheet.requestSheetDelete(1);

        Spreadsheet.setSheetDeleteButtonAllowed(false);
        clickDialogButton(1);

        assertEquals(2, dialogSpreadsheet.getNumberOfSheets());
        assertTrue(ui.getWindows().isEmpty());
    }

    @Test
    public void onSheetDelete_locallyDisabledAfterConfirmation_keepsSheet() {
        dialogSpreadsheet.createNewSheet("Second", 10, 10);
        dialogSpreadsheet.createCell(0, 0, "Content");
        dialogSpreadsheet.requestSheetDelete(1);

        dialogSpreadsheet.setSheetDeleteButtonEnabled(false);
        clickDialogButton(1);

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

        boolean[] getProtectedSheets() {
            return getState(false).protectedSheets;
        }

        boolean isClientSheetDeleteEnabled() {
            return getState(false).sheetDeleteEnabled;
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
