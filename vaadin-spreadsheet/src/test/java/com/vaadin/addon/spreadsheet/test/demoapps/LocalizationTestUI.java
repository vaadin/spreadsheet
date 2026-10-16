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
package com.vaadin.addon.spreadsheet.test.demoapps;

import java.lang.reflect.Field;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.vaadin.addon.spreadsheet.Spreadsheet;
import com.vaadin.addon.spreadsheet.Spreadsheet.LocalizableString;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("demo")
@Widgetset("com.vaadin.addon.spreadsheet.Widgetset")
public class LocalizationTestUI extends UI {

    @Override
    protected void init(VaadinRequest request) {
        Map<Locale, Map<LocalizableString, String>> translations = localizationMap(
                "localizedStrings");
        Map<LocalizableString, String> defaults = localizationMap("defaultStrings");
        Map<Locale, Map<LocalizableString, String>> savedTranslations = new HashMap<>();
        Map<LocalizableString, String> savedDefaults = new EnumMap<>(
                LocalizableString.class);
        synchronized (Spreadsheet.class) {
            translations.forEach((locale, values) -> savedTranslations.put(
                    locale, new EnumMap<>(values)));
            savedDefaults.putAll(defaults);
            translations.clear();
            defaults.clear();
        }
        Spreadsheet.setLocalizedString(Locale.FRENCH,
                LocalizableString.SHEET_DELETE_CAPTION, "Supprimer");
        Spreadsheet sheet = new Spreadsheet(10, 10);
        sheet.setLocale(Locale.ITALIAN);
        sheet.createNewSheet("Second", 10, 10);

        HorizontalLayout controls = new HorizontalLayout(
                control("default", () -> {
                    Spreadsheet.setLocalizedString(
                            LocalizableString.SHEET_DELETE_CAPTION, "Remove");
                    sheet.refreshLocalizedStrings();
                }),
                control("changed-default", () -> {
                    Spreadsheet.setLocalizedString(
                            LocalizableString.SHEET_DELETE_CAPTION, "Delete worksheet");
                    sheet.refreshLocalizedStrings();
                }),
                control("translated", () -> sheet.setLocale(Locale.FRENCH)),
                control("untranslated", () -> sheet.setLocale(Locale.ITALIAN)),
                control("reset", () -> {
                    synchronized (Spreadsheet.class) {
                        translations.clear();
                        defaults.clear();
                    }
                    sheet.refreshLocalizedStrings();
                }),
                control("restore", () -> {
                    synchronized (Spreadsheet.class) {
                        translations.clear();
                        translations.putAll(savedTranslations);
                        defaults.clear();
                        defaults.putAll(savedDefaults);
                    }
                    sheet.refreshLocalizedStrings();
                }));
        VerticalLayout content = new VerticalLayout(controls, sheet);
        content.setSizeFull();
        content.setExpandRatio(sheet, 1);
        setContent(content);
    }

    private Button control(String id, Runnable action) {
        Button button = new Button(id, event -> action.run());
        button.setId(id);
        return button;
    }

    @SuppressWarnings("unchecked")
    private static <Key, Value> Map<Key, Value> localizationMap(String name) {
        try {
            Field field = Spreadsheet.class.getDeclaredField(name);
            field.setAccessible(true);
            return (Map<Key, Value>) field.get(null);
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException(exception);
        }
    }
}
