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
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Locale;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.SheetVisibility;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

        assertTrue(spreadsheet.isSheetDeleteButtonEnabled());
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
                    spreadsheet.isSheetDeleteButtonEnabled());
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

        assertFalse(disabled.isSheetDeleteButtonEnabled());
        assertTrue(enabled.isSheetDeleteButtonEnabled());
        assertTrue(enabled.isClientSheetDeleteEnabled());
    }

    @Test
    public void sheetDeleteEnabled_globalChanges_updateExistingAndNewPermissionArrays() {
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

        assertFalse(spreadsheet.isSheetDeleteButtonEnabled());
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
    public void sheetDeletePermissions_structureLockedWorkbook_appliesProtectionOverrides()
            throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            workbook.createSheet("Protected").protectSheet("password");
            workbook.createSheet("Unprotected");
            workbook.lockStructure();
            TestSpreadsheet spreadsheet = new TestSpreadsheet();

            spreadsheet.setWorkbook(workbook);
            spreadsheet.beforeClientResponse(true);

            assertArrayEquals(new boolean[] { false, false },
                    spreadsheet.getSheetDeleteButtonsEnabled());
            spreadsheet.requestSheetDelete(1);
            assertEquals(2, spreadsheet.getNumberOfSheets());
            assertFalse(spreadsheet.isConfirmationShown());

            spreadsheet.setProtectedWorkbookDeleteButtonEnabled(true);
            assertArrayEquals(new boolean[] { false, true },
                    spreadsheet.getSheetDeleteButtonsEnabled());

            spreadsheet.setProtectedSheetDeleteButtonEnabled(true);
            assertArrayEquals(new boolean[] { true, true },
                    spreadsheet.getSheetDeleteButtonsEnabled());

            spreadsheet.requestSheetDelete(1);
            assertEquals(1, spreadsheet.getNumberOfSheets());
            assertEquals("Protected", workbook.getSheetName(0));
        }
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
        Sheet originalSheet = spreadsheet.getActiveSheet();

        spreadsheet.requestSheetDelete(0);

        assertEquals(1, spreadsheet.getNumberOfSheets());
        assertEquals("Sheet1", spreadsheet.getWorkbook().getSheetName(0));
        assertNotSame(originalSheet, spreadsheet.getActiveSheet());
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
    public void onSheetDelete_sheetIsEmptyAndProtected_doesNothingByDefault() {
        TestSpreadsheet spreadsheet = createSpreadsheetWithTwoSheets();
        spreadsheet.setSheetProtected(1, "password");

        spreadsheet.requestSheetDelete(1);

        assertEquals(2, spreadsheet.getNumberOfSheets());
        assertFalse(spreadsheet.isConfirmationShown());
        assertFalse(spreadsheet.isProtectedSheetDeleteButtonEnabled());
        assertFalse(spreadsheet.isProtectedWorkbookDeleteButtonEnabled());
    }

    @Test
    public void onSheetDelete_protectedSheetEnabled_showsConfirmation() {
        TestSpreadsheet spreadsheet = createSpreadsheetWithTwoSheets();
        spreadsheet.setSheetProtected(1, "password");
        spreadsheet.setProtectedSheetDeleteButtonEnabled(true);

        spreadsheet.requestSheetDelete(1);

        assertTrue(spreadsheet.isProtectedSheetDeleteButtonEnabled());
        assertTrue(spreadsheet.isConfirmationShown());
    }

    @Test
    public void onSheetDelete_protectionSettings_allRestrictionsMustAllow() {
        for (boolean sheetEnabled : new boolean[] { false, true }) {
            for (boolean workbookEnabled : new boolean[] { false, true }) {
                TestSpreadsheet spreadsheet = createSpreadsheetWithTwoSheets();
                spreadsheet.setSheetProtected(1, "password");
                spreadsheet.setWorkbookReadOnly(true);
                spreadsheet.setProtectedSheetDeleteButtonEnabled(sheetEnabled);
                spreadsheet.setProtectedWorkbookDeleteButtonEnabled(workbookEnabled);

                spreadsheet.beforeClientResponse(false);
                assertArrayEquals(new boolean[] { workbookEnabled,
                    sheetEnabled && workbookEnabled },
                    spreadsheet.getSheetDeleteButtonsEnabled());

                spreadsheet.requestSheetDelete(1);

                assertEquals(sheetEnabled && workbookEnabled,
                        spreadsheet.isConfirmationShown());
                assertEquals(2, spreadsheet.getNumberOfSheets());
            }
        }
    }

    @Test
    public void onSheetDelete_protectedWorkbookEnabled_deletesUnprotectedSheet() {
        TestSpreadsheet spreadsheet = createSpreadsheetWithTwoSheets();
        spreadsheet.setWorkbookReadOnly(true);
        spreadsheet.setProtectedWorkbookDeleteButtonEnabled(true);

        spreadsheet.requestSheetDelete(1);

        assertTrue(spreadsheet.isProtectedWorkbookDeleteButtonEnabled());
        assertEquals(1, spreadsheet.getNumberOfSheets());
    }

    @Test
    public void onSheetDelete_protectionOverrides_doNotOverrideDeletionDisabled() {
        TestSpreadsheet spreadsheet = createSpreadsheetWithTwoSheets();
        spreadsheet.setSheetProtected(1, "password");
        spreadsheet.setWorkbookReadOnly(true);
        spreadsheet.setProtectedSheetDeleteButtonEnabled(true);
        spreadsheet.setProtectedWorkbookDeleteButtonEnabled(true);

        spreadsheet.setSheetDeleteButtonEnabled(false);
        spreadsheet.requestSheetDelete(1);
        assertFalse(spreadsheet.isConfirmationShown());

        spreadsheet.setSheetDeleteButtonEnabled(true);
        Spreadsheet.setSheetDeleteButtonAllowed(false);
        spreadsheet.requestSheetDelete(1);
        assertFalse(spreadsheet.isConfirmationShown());
        assertEquals(2, spreadsheet.getNumberOfSheets());
    }

    @Test
    public void onSheetDelete_protectedSheetDisabledAfterConfirmation_keepsSheet() {
        dialogSpreadsheet.createNewSheet("Second", 10, 10);
        dialogSpreadsheet.setSheetProtected(1, "password");
        dialogSpreadsheet.setProtectedSheetDeleteButtonEnabled(true);
        dialogSpreadsheet.requestSheetDelete(1);

        dialogSpreadsheet.setProtectedSheetDeleteButtonEnabled(false);
        clickDialogButton(1);

        assertEquals(2, dialogSpreadsheet.getNumberOfSheets());
        assertTrue(ui.getWindows().isEmpty());
    }

    @Test
    public void sheetDeletePermissions_inactiveSheetProtected_updatesOnlyItsEntry() {
        TestSpreadsheet spreadsheet = createSpreadsheetWithTwoSheets();

        spreadsheet.setSheetProtected(0, "password");

        assertFalse(spreadsheet.isActiveSheetProtected());
        assertArrayEquals(new boolean[] { false, true },
            spreadsheet.getSheetDeleteButtonsEnabled());
    }

    @Test
    public void sheetDeletePermissions_activeSheetChanged_keepsPerTabPermissions() {
        TestSpreadsheet spreadsheet = createSpreadsheetWithTwoSheets();
        spreadsheet.setSheetProtected(0, "password");

        spreadsheet.setActiveSheetIndex(0);

        assertTrue(spreadsheet.isActiveSheetProtected());
        assertArrayEquals(new boolean[] { false, true },
            spreadsheet.getSheetDeleteButtonsEnabled());
    }

    @Test
    public void sheetDeletePermissions_protectionRemoved_updatesEntry() {
        TestSpreadsheet spreadsheet = createSpreadsheetWithTwoSheets();
        spreadsheet.setSheetProtected(0, "password");

        spreadsheet.setSheetProtected(0, null);

        assertArrayEquals(new boolean[] { true, true },
            spreadsheet.getSheetDeleteButtonsEnabled());
    }

    @Test
    public void sheetDeletePermissions_hiddenSheets_keepsVisibleTabOrder() {
        TestSpreadsheet spreadsheet = createSpreadsheetWithTwoSheets();
        spreadsheet.createNewSheet("Third", 10, 10);
        spreadsheet.setSheetProtected(0, "password");
        spreadsheet.setSheetProtected(1, "password");

        spreadsheet.setSheetHidden(0, SheetVisibility.HIDDEN);
        spreadsheet.setSheetHidden(1, SheetVisibility.VERY_HIDDEN);

        assertArrayEquals(new boolean[] { true },
            spreadsheet.getSheetDeleteButtonsEnabled());

        spreadsheet.setSheetHidden(1, SheetVisibility.VISIBLE);

        assertArrayEquals(new boolean[] { false, true },
            spreadsheet.getSheetDeleteButtonsEnabled());
    }

    @Test
    public void sheetDeletePermissions_tabsAddedAndRemoved_keepsEntriesAligned() {
        TestSpreadsheet spreadsheet = createSpreadsheetWithTwoSheets();
        spreadsheet.setSheetProtected(0, "password");

        spreadsheet.createNewSheet("Third", 10, 10);

        assertArrayEquals(new boolean[] { false, true, true },
            spreadsheet.getSheetDeleteButtonsEnabled());

        spreadsheet.deleteSheet(0);

        assertArrayEquals(new boolean[] { true, true },
            spreadsheet.getSheetDeleteButtonsEnabled());
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
        Sheet originalSheet = dialogSpreadsheet.getActiveSheet();

        dialogSpreadsheet.requestSheetDelete(0);
        clickDialogButton(1);

        assertEquals(1, dialogSpreadsheet.getNumberOfSheets());
        assertEquals("Sheet1",
                dialogSpreadsheet.getWorkbook().getSheetName(0));
        assertNotSame(originalSheet, dialogSpreadsheet.getActiveSheet());
        assertNull(dialogSpreadsheet.getCell(0, 0));
        assertTrue(ui.getWindows().isEmpty());
    }

    @Test
    public void protectionOverrides_settingsAreIndependentPerInstance() {
        TestSpreadsheet enabled = new TestSpreadsheet();
        TestSpreadsheet defaults = new TestSpreadsheet();

        enabled.setProtectedSheetDeleteButtonEnabled(true);
        enabled.setProtectedWorkbookDeleteButtonEnabled(true);

        assertTrue(enabled.isProtectedSheetDeleteButtonEnabled());
        assertTrue(enabled.isProtectedWorkbookDeleteButtonEnabled());
        assertFalse(defaults.isProtectedSheetDeleteButtonEnabled());
        assertFalse(defaults.isProtectedWorkbookDeleteButtonEnabled());

        enabled.setProtectedSheetDeleteButtonEnabled(false);
        assertFalse(enabled.isProtectedSheetDeleteButtonEnabled());
        assertTrue(enabled.isProtectedWorkbookDeleteButtonEnabled());
    }

    @Test
    public void onSheetDelete_protectedSheetConfirmed_deletesSheet() {
        dialogSpreadsheet.createNewSheet("Second", 10, 10);
        dialogSpreadsheet.setSheetProtected(1, "password");
        dialogSpreadsheet.setProtectedSheetDeleteButtonEnabled(true);

        dialogSpreadsheet.requestSheetDelete(1);
        clickDialogButton(1);

        assertEquals(1, dialogSpreadsheet.getNumberOfSheets());
        assertTrue(ui.getWindows().isEmpty());
    }

    @Test
    public void onSheetDelete_workbookOverrideDisabledAfterConfirmation_keepsSheet() {
        dialogSpreadsheet.createNewSheet("Second", 10, 10);
        dialogSpreadsheet.createCell(0, 0, "Content");
        dialogSpreadsheet.setWorkbookReadOnly(true);
        dialogSpreadsheet.setProtectedWorkbookDeleteButtonEnabled(true);
        dialogSpreadsheet.requestSheetDelete(1);

        dialogSpreadsheet.setProtectedWorkbookDeleteButtonEnabled(false);
        clickDialogButton(1);

        assertEquals(2, dialogSpreadsheet.getNumberOfSheets());
        assertTrue(ui.getWindows().isEmpty());
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

        boolean[] getSheetDeleteButtonsEnabled() {
            return getState(false).sheetDeleteButtonsEnabled;
        }

        boolean isClientSheetDeleteEnabled() {
            return getSheetDeleteButtonsEnabled()[0];
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

        void setWorkbookReadOnly(boolean readOnly) {
            getState().workbookProtected = readOnly;
        }
    }

    private static class TestUI extends UI {
        @Override
        protected void init(VaadinRequest request) {
        }
    }
}
