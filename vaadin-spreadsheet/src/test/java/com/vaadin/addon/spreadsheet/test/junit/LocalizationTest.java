package com.vaadin.addon.spreadsheet.test.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.vaadin.addon.spreadsheet.Spreadsheet;
import com.vaadin.addon.spreadsheet.Spreadsheet.LocalizableString;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class LocalizationTest {

    private DialogSpreadsheet dialogSpreadsheet;
    private UI ui;
    private Map<Locale, Map<LocalizableString, String>> savedLocalizedStrings;
    private Map<LocalizableString, String> savedDefaultStrings;

    @Before
    public void setUp() throws ReflectiveOperationException {
        synchronized (Spreadsheet.class) {
            Map<Locale, Map<LocalizableString, String>> translations = getLocalizationMap(
                    "localizedStrings");
            savedLocalizedStrings = new HashMap<>();
            translations.forEach((locale, values) -> savedLocalizedStrings
                    .put(locale, new EnumMap<>(values)));
            savedDefaultStrings = new EnumMap<>(LocalizableString.class);
            savedDefaultStrings.putAll(getLocalizationMap("defaultStrings"));
            translations.clear();
            getLocalizationMap("defaultStrings").clear();
        }
        dialogSpreadsheet = new DialogSpreadsheet();
        ui = new TestUI();
        ui.setLocale(Locale.getDefault());
        UI.setCurrent(ui);
        ui.setContent(dialogSpreadsheet);
    }

    @After
    public void tearDown() throws ReflectiveOperationException {
        UI.setCurrent(null);
        synchronized (Spreadsheet.class) {
            Map<Locale, Map<LocalizableString, String>> translations = getLocalizationMap(
                    "localizedStrings");
            translations.clear();
            translations.putAll(savedLocalizedStrings);
            Map<LocalizableString, String> defaults = getLocalizationMap(
                    "defaultStrings");
            defaults.clear();
            defaults.putAll(savedDefaultStrings);
        }
    }

    @SuppressWarnings("unchecked")
    private static <Key, Value> Map<Key, Value> getLocalizationMap(String name)
            throws ReflectiveOperationException {
        Field field = Spreadsheet.class.getDeclaredField(name);
        field.setAccessible(true);
        return (Map<Key, Value>) field.get(null);
    }

    @Test
    public void onSheetDelete_confirmationUsesDefaultText() {
        assertDialogText("Confirm sheet deletion",
                "Are you sure you want to delete sheet 'Sheet1'?",
                "Cancel", "Delete sheet");
    }

    @Test
    public void onSheetDelete_confirmationUsesCustomText() {
        Spreadsheet.setLocalizedString(
                LocalizableString.SHEET_DELETE_CAPTION, "Remove");
        Spreadsheet.setLocalizedString(
                LocalizableString.SHEET_DELETE_CANCEL_CAPTION, "Keep");
        Spreadsheet.setLocalizedString(
                LocalizableString.SHEET_DELETE_CONFIRMATION_CAPTION,
                "Remove worksheet");
        Spreadsheet.setLocalizedString(
                LocalizableString.SHEET_DELETE_CONFIRMATION_MESSAGE,
                "'{0}' will be removed. Proceed?");

        dialogSpreadsheet.beforeClientResponse(false);
        assertEquals("Remove", dialogSpreadsheet.getSheetDeleteCaption());
        assertDialogText("Remove worksheet",
                "'Sheet1' will be removed. Proceed?", "Keep", "Remove");
    }

    @Test
    public void onSheetDelete_confirmationUsesCurrentLocale() {
        Spreadsheet.setLocalizedString(Locale.FRENCH,
                LocalizableString.SHEET_DELETE_CAPTION, "Supprimer");
        Spreadsheet.setLocalizedString(Locale.GERMAN,
                LocalizableString.SHEET_DELETE_CAPTION, "Entfernen");
        Spreadsheet.setLocalizedString(Locale.FRENCH,
                LocalizableString.SHEET_DELETE_CONFIRMATION_CAPTION,
                "Confirmation");
        Spreadsheet.setLocalizedString(Locale.FRENCH,
                LocalizableString.SHEET_DELETE_CANCEL_CAPTION, "Annuler");
        Spreadsheet.setLocalizedString(Locale.FRENCH,
                LocalizableString.SHEET_DELETE_CONFIRMATION_MESSAGE,
                "Supprimer '{0}' ?");

        dialogSpreadsheet.setLocale(Locale.GERMAN);
        assertEquals("Entfernen", dialogSpreadsheet.getSheetDeleteCaption());
        dialogSpreadsheet.setLocale(Locale.FRENCH);
        assertEquals("Supprimer", dialogSpreadsheet.getSheetDeleteCaption());
        assertDialogText("Confirmation", "Supprimer 'Sheet1' ?",
                "Annuler", "Supprimer");
    }

    @Test
    public void onSheetDelete_missingLocaleEntryUsesDefaultOrEnglish() {
        Spreadsheet.setLocalizedString(
                LocalizableString.SHEET_DELETE_CAPTION, "Remove");
        Spreadsheet.setLocalizedString(Locale.FRENCH,
                LocalizableString.SHEET_DELETE_CANCEL_CAPTION, "Annuler");
        dialogSpreadsheet.setLocale(Locale.FRENCH);

        assertEquals("Remove", dialogSpreadsheet.getSheetDeleteCaption());
        assertDialogText("Confirm sheet deletion",
                "Are you sure you want to delete sheet 'Sheet1'?",
                "Annuler", "Remove");
    }

    @Test
    public void onSheetDelete_unconfiguredLocaleUsesEnglish() {
        Spreadsheet.setLocalizedString(Locale.FRENCH,
                LocalizableString.SHEET_DELETE_CAPTION, "Supprimer");
        dialogSpreadsheet.setLocale(Locale.ITALIAN);

        assertEquals("Delete sheet", dialogSpreadsheet.getSheetDeleteCaption());
        assertDialogText("Confirm sheet deletion",
                "Are you sure you want to delete sheet 'Sheet1'?",
                "Cancel", "Delete sheet");
    }

    @Test
    public void refreshLocalizedStrings_changedDefaultMarksClientStateDirty() {
        dialogSpreadsheet.setLocale(Locale.ITALIAN);
        Spreadsheet.setLocalizedString(
                LocalizableString.SHEET_DELETE_CAPTION, "Remove");
        dialogSpreadsheet.markedDirty = false;

        dialogSpreadsheet.refreshLocalizedStrings();

        assertTrue(dialogSpreadsheet.markedDirty);
        assertEquals("Remove", dialogSpreadsheet.getSheetDeleteCaption());
        Spreadsheet.setLocalizedString(
                LocalizableString.SHEET_DELETE_CAPTION, "Delete worksheet");
        dialogSpreadsheet.markedDirty = false;

        dialogSpreadsheet.refreshLocalizedStrings();

        assertTrue(dialogSpreadsheet.markedDirty);
        assertEquals("Delete worksheet", dialogSpreadsheet.getSheetDeleteCaption());
    }

    @Test
    public void setLocalizedString_changedDefaultUpdatesExistingClientCaption() {
        dialogSpreadsheet.setLocale(Locale.ITALIAN);
        dialogSpreadsheet.beforeClientResponse(false);
        assertEquals("Delete sheet", dialogSpreadsheet.getSheetDeleteCaption());

        Spreadsheet.setLocalizedString(
                LocalizableString.SHEET_DELETE_CAPTION, "Remove");
        dialogSpreadsheet.beforeClientResponse(false);
        assertEquals("Remove", dialogSpreadsheet.getSheetDeleteCaption());

        Spreadsheet.setLocalizedString(
                LocalizableString.SHEET_DELETE_CAPTION, "Delete worksheet");
        dialogSpreadsheet.beforeClientResponse(false);
        assertEquals("Delete worksheet", dialogSpreadsheet.getSheetDeleteCaption());
    }

    @Test
    public void setLocale_clientCaptionSwitchesBetweenTranslationAndDefault() {
        Spreadsheet.setLocalizedString(
                LocalizableString.SHEET_DELETE_CAPTION, "Remove");
        Spreadsheet.setLocalizedString(Locale.FRENCH,
                LocalizableString.SHEET_DELETE_CAPTION, "Supprimer");

        dialogSpreadsheet.setLocale(Locale.ITALIAN);
        assertEquals("Remove", dialogSpreadsheet.getSheetDeleteCaption());
        dialogSpreadsheet.setLocale(Locale.FRENCH);
        assertEquals("Supprimer", dialogSpreadsheet.getSheetDeleteCaption());
        dialogSpreadsheet.setLocale(Locale.ITALIAN);
        assertEquals("Remove", dialogSpreadsheet.getSheetDeleteCaption());
    }

    @Test
    public void setLocale_clientCaptionSwitchesBetweenTranslationAndFallback() {
        Spreadsheet.setLocalizedString(Locale.FRENCH,
                LocalizableString.SHEET_DELETE_CAPTION, "Supprimer");

        dialogSpreadsheet.setLocale(Locale.FRENCH);
        assertEquals("Supprimer", dialogSpreadsheet.getSheetDeleteCaption());
        dialogSpreadsheet.setLocale(Locale.ITALIAN);
        assertEquals("Delete sheet", dialogSpreadsheet.getSheetDeleteCaption());
        dialogSpreadsheet.setLocale(Locale.FRENCH);
        assertEquals("Supprimer", dialogSpreadsheet.getSheetDeleteCaption());
    }

    @Test
    public void setLocalizedString_currentLocaleUpdatesClientCaption() {
        dialogSpreadsheet.setLocale(Locale.FRENCH);
        Spreadsheet.setLocalizedString(Locale.FRENCH,
                LocalizableString.SHEET_DELETE_CAPTION, "Supprimer");
        Spreadsheet.setLocalizedString(
                LocalizableString.SHEET_DELETE_CAPTION, "Remove");

        dialogSpreadsheet.beforeClientResponse(false);
        assertEquals("Supprimer", dialogSpreadsheet.getSheetDeleteCaption());
        dialogSpreadsheet.setLocale(Locale.GERMAN);
        assertEquals("Remove", dialogSpreadsheet.getSheetDeleteCaption());
    }

    @Test
    public void beforeClientResponse_inheritedLocaleUpdatesClientCaption() {
        Spreadsheet.setLocalizedString(Locale.FRENCH,
                LocalizableString.SHEET_DELETE_CAPTION, "Supprimer");
        ui.setLocale(Locale.FRENCH);

        dialogSpreadsheet.beforeClientResponse(false);

        assertEquals("Supprimer", dialogSpreadsheet.getSheetDeleteCaption());
        assertDialogText("Confirm sheet deletion",
                "Are you sure you want to delete sheet 'Sheet1'?",
                "Cancel", "Supprimer");
    }

    @Test
    public void setLocalizedString_configurationIsSharedAcrossInstances() {
        DialogSpreadsheet otherSpreadsheet = new DialogSpreadsheet();
        dialogSpreadsheet.setLocale(Locale.FRENCH);
        otherSpreadsheet.setLocale(Locale.GERMAN);

        Spreadsheet.setLocalizedString(
                LocalizableString.SHEET_DELETE_CAPTION, "Remove");
        Spreadsheet.setLocalizedString(Locale.FRENCH,
                LocalizableString.SHEET_DELETE_CAPTION, "Supprimer");

        dialogSpreadsheet.beforeClientResponse(false);
        otherSpreadsheet.beforeClientResponse(false);

        assertEquals("Supprimer", dialogSpreadsheet.getSheetDeleteCaption());
        assertEquals("Remove", otherSpreadsheet.getSheetDeleteCaption());
        assertDialogText("Confirm sheet deletion",
                "Are you sure you want to delete sheet 'Sheet1'?",
                "Cancel", "Supprimer");
    }

    private void assertDialogText(String caption, String message,
            String cancelCaption, String deleteCaption) {
        dialogSpreadsheet.createCell(0, 0, "Content");
        dialogSpreadsheet.requestSheetDelete(0);

        Window confirmation = ui.getWindows().iterator().next();
        assertEquals(caption, confirmation.getCaption());
        VerticalLayout content = (VerticalLayout) confirmation.getContent();
        assertEquals(message, ((Label) content.getComponent(0)).getValue());
        HorizontalLayout actions = (HorizontalLayout) content.getComponent(1);
        assertEquals(cancelCaption, actions.getComponent(0).getCaption());
        assertEquals(deleteCaption, actions.getComponent(1).getCaption());
    }

    private static class DialogSpreadsheet extends Spreadsheet {
                private boolean markedDirty;

                @Override
                public void markAsDirty() {
                        markedDirty = true;
                        super.markAsDirty();
                }

        void requestSheetDelete(int sheetIndex) {
            onSheetDelete(sheetIndex);
        }

        String getSheetDeleteCaption() {
            return getState(false).sheetDeleteCaption;
        }
    }

    private static class TestUI extends UI {
        @Override
        protected void init(VaadinRequest request) {
        }
    }
}