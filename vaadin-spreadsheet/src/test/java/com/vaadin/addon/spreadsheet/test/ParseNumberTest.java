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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Locale;

import org.junit.Test;

import com.vaadin.addon.spreadsheet.SpreadsheetUtil;

public class ParseNumberTest {

    @Test
    public void testNumberParsingWithEnLocale() {
        Locale locale = new Locale("en");

        Double result = SpreadsheetUtil.parseNumber(null, locale);
        assertNull(result);

        result = SpreadsheetUtil.parseNumber("", locale);
        assertNull(result);

        result = SpreadsheetUtil.parseNumber("s42", locale);
        assertNull(result);

        result = SpreadsheetUtil.parseNumber("42s", locale);
        assertNull(result);

        result = SpreadsheetUtil.parseNumber("42", locale);
        assertNotNull(result);

        result = SpreadsheetUtil.parseNumber("4.2", locale);
        assertNotNull(result);

        result = SpreadsheetUtil.parseNumber("4,3", locale);
        assertNull(result);

        result = SpreadsheetUtil.parseNumber("4 3", locale);
        assertNull(result);

        result = SpreadsheetUtil.parseNumber("4E2", locale);
        assertNotNull(result);

        result = SpreadsheetUtil.parseNumber("4.2E2", locale);
        assertNotNull(result);

        result = SpreadsheetUtil.parseNumber("4,2E2", locale);
        assertNull(result);

        result = SpreadsheetUtil.parseNumber("4 002", locale);
        assertNull(result);

        result = SpreadsheetUtil.parseNumber("4,002", locale);
        assertNotNull(result);

        result = SpreadsheetUtil.parseNumber("4.002", locale);
        assertNotNull(result);

        result = SpreadsheetUtil.parseNumber("4 002.42", locale);
        assertNull(result);

        result = SpreadsheetUtil.parseNumber("4,002.42", locale);
        assertNotNull(result);

        result = SpreadsheetUtil.parseNumber("4.002,42", locale);
        assertNull(result);
    }

    @Test
    public void testNumberParsingWithFiLocale() {
        Locale locale = new Locale("fi");

        Double result = SpreadsheetUtil.parseNumber(null, locale);
        assertNull(result);

        result = SpreadsheetUtil.parseNumber("", locale);
        assertNull(result);

        result = SpreadsheetUtil.parseNumber("s42", locale);
        assertNull(result);

        result = SpreadsheetUtil.parseNumber("42s", locale);
        assertNull(result);

        result = SpreadsheetUtil.parseNumber("42", locale);
        assertNotNull(result);

        result = SpreadsheetUtil.parseNumber("4.2", locale);
        assertNull(result);

        result = SpreadsheetUtil.parseNumber("4,3", locale);
        assertNotNull(result);

        result = SpreadsheetUtil.parseNumber("4 3", locale);
        assertNull(result);

        result = SpreadsheetUtil.parseNumber("4E2", locale);
        assertNotNull(result);

        result = SpreadsheetUtil.parseNumber("4.2E2", locale);
        assertNull(result);

        result = SpreadsheetUtil.parseNumber("4,2E2", locale);
        assertNotNull(result);

        result = SpreadsheetUtil.parseNumber("4 002", locale);
        assertNotNull(result);

        result = SpreadsheetUtil.parseNumber("4,002", locale);
        assertNotNull(result);

        result = SpreadsheetUtil.parseNumber("4.002", locale);
        assertNull(result);

        result = SpreadsheetUtil.parseNumber("4 002.42", locale);
        assertNull(result);

        result = SpreadsheetUtil.parseNumber("4,002.42", locale);
        assertNull(result);

        result = SpreadsheetUtil.parseNumber("4.002,42", locale);
        assertNull(result);
    }

    @Test
    public void testNumberParsingWithItLocale() {
        Locale locale = new Locale("it");

        Double result = SpreadsheetUtil.parseNumber(null, locale);
        assertNull(result);

        result = SpreadsheetUtil.parseNumber("", locale);
        assertNull(result);

        result = SpreadsheetUtil.parseNumber("s42", locale);
        assertNull(result);

        result = SpreadsheetUtil.parseNumber("42s", locale);
        assertNull(result);

        result = SpreadsheetUtil.parseNumber("42", locale);
        assertNotNull(result);

        result = SpreadsheetUtil.parseNumber("4.2", locale);
        assertNull(result);

        result = SpreadsheetUtil.parseNumber("4,3", locale);
        assertNotNull(result);

        result = SpreadsheetUtil.parseNumber("4 3", locale);
        assertNull(result);

        result = SpreadsheetUtil.parseNumber("4E2", locale);
        assertNotNull(result);

        result = SpreadsheetUtil.parseNumber("4.2E2", locale);
        assertNull(result);

        result = SpreadsheetUtil.parseNumber("4,2E2", locale);
        assertNotNull(result);

        result = SpreadsheetUtil.parseNumber("4 002", locale);
        assertNull(result);

        result = SpreadsheetUtil.parseNumber("4,002", locale);
        assertNotNull(result);

        result = SpreadsheetUtil.parseNumber("4.002", locale);
        assertNotNull(result);

        result = SpreadsheetUtil.parseNumber("4 002.42", locale);
        assertNull(result);

        result = SpreadsheetUtil.parseNumber("4,002.42", locale);
        assertNull(result);

        result = SpreadsheetUtil.parseNumber("4.002,42", locale);
        assertNotNull(result);
    }

}
