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

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.Locale;

import org.junit.Test;

import com.vaadin.addon.spreadsheet.elements.SpreadsheetElement;
import com.vaadin.addon.spreadsheet.test.fixtures.TestFixtures;

public class GeneralRoundingTest extends AbstractSpreadsheetTestCase {

    @Test
    public void generalFormat_spreadsheetWithGeneralFormatAndLocaleUS_numbersRoundedCorrectly() {
        setLocale(Locale.US);

        headerPage.loadFile("general_round.xlsx", this);
        SpreadsheetElement spreadsheet = $(SpreadsheetElement.class).first();

        String cellBeforeResize = spreadsheet.getCellAt("A7").getValue();
        assertThat(cellBeforeResize, not(containsString(",")));
        assertThat(cellBeforeResize, not(containsString("-")));
        assertThat(cellBeforeResize, not(containsString("#")));

        headerPage.loadTestFixture(TestFixtures.FirstColumnWidth);

        String cellAfterResize = spreadsheet.getCellAt("A7").getValue();

        assertLessThan("Number not shortened", cellAfterResize.length(),
                cellBeforeResize.length());
        assertThat(cellAfterResize, not(containsString(",")));
        assertThat(cellAfterResize, not(containsString("-")));
        assertThat(cellAfterResize, not(containsString("#")));
    }

    @Test
    public void generalFormat_spreadsheetWithGeneralFormatAndLocaleFI_numbersRoundedCorrectly() {
        setLocale(new Locale("fi", "FI"));
        headerPage.loadFile("general_round.xlsx", this);
        SpreadsheetElement spreadsheet = $(SpreadsheetElement.class).first();

        String cellBeforeResize = spreadsheet.getCellAt("A7").getValue();
        assertThat(cellBeforeResize, not(containsString(".")));
        assertThat(cellBeforeResize, not(containsString("-")));
        assertThat(cellBeforeResize, not(containsString("#")));

        headerPage.loadTestFixture(TestFixtures.FirstColumnWidth);

        String cellAfterResize = spreadsheet.getCellAt("A7").getValue();

        assertLessThan("Number not shortened", cellAfterResize.length(),
                cellBeforeResize.length());
        assertThat(cellAfterResize, not(containsString(".")));
        assertThat(cellAfterResize, not(containsString("-")));
        assertThat(cellAfterResize, not(containsString("#")));
    }

    @Test
    public void generalFormat_spreadsheetWithGeneralFormatAndLocaleUS_negativeNumbersRoundedCorrectly() {
        assertEquals("Check US locale", Locale.US.toString(), "en_US");
        setLocale(Locale.US);

        headerPage.loadFile("negative_general_round.xlsx", this);
        SpreadsheetElement spreadsheet = $(SpreadsheetElement.class).first();

        String cellBeforeResize = spreadsheet.getCellAt("A7").getValue();
        assertThat(cellBeforeResize, not(containsString(",")));
        assertThat(cellBeforeResize, containsString("-"));
        assertThat(cellBeforeResize, not(containsString("#")));

        headerPage.loadTestFixture(TestFixtures.FirstColumnWidth);

        String cellAfterResize = spreadsheet.getCellAt("A7").getValue();

        assertLessThan("Number not shortened", cellAfterResize.length(),
                cellBeforeResize.length());
        assertThat(cellAfterResize, not(containsString(",")));
        assertThat(cellAfterResize, containsString("-"));
        assertThat(cellAfterResize, not(containsString("#")));
    }

    @Test
    public void generalFormat_spreadsheetWithGeneralFormatAndLocaleFI_negativeNumbersRoundedCorrectly() {
        setLocale(new Locale("fi", "FI"));
        headerPage.loadFile("negative_general_round.xlsx", this);
        SpreadsheetElement spreadsheet = $(SpreadsheetElement.class).first();

        String cellBeforeResize = spreadsheet.getCellAt("A7").getValue();
        assertThat(cellBeforeResize, not(containsString(".")));
        assertThat(cellBeforeResize, containsString("-"));
        assertThat(cellBeforeResize, not(containsString("#")));

        headerPage.loadTestFixture(TestFixtures.FirstColumnWidth);

        String cellAfterResize = spreadsheet.getCellAt("A7").getValue();

        assertLessThan("Number not shortened", cellAfterResize.length(),
                cellBeforeResize.length());
        assertThat(cellAfterResize, not(containsString(".")));
        assertThat(cellAfterResize, containsString("-"));
        assertThat(cellAfterResize, not(containsString("#")));
    }

}
