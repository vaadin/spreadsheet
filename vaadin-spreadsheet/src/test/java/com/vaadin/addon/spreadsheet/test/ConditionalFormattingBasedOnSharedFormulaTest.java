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

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.google.common.collect.Sets;
import com.vaadin.addon.spreadsheet.test.pageobjects.SpreadsheetPage;

public class ConditionalFormattingBasedOnSharedFormulaTest
        extends AbstractSpreadsheetTestCase {

    private static final String FALSE_CONDITION_COLOR = "rgba(255, 255, 255, 1)";
    private static final String TRUE_CONDITION_COLOR = "rgba(255, 0, 0, 1)";

    private static final Set<String> cellWithTrueCondition = Sets
            .newHashSet("A2", "A3", "A4", "D1", "D3");
    private static final Set<String> cellWithFormattingCondition = getCells();

    private SpreadsheetPage spreadsheetPage;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        spreadsheetPage = headerPage
                .loadFile("conditional_formatting_shared_formula.xlsx", this);
    }

    @Test
    public void loadSpreadsheetWithConditionalFormattingInA1A2_A3B4_D1G5___CheckCellFormatting() {
        for (String cellAddress : cellWithTrueCondition) {
            assertEquals(TRUE_CONDITION_COLOR,
                    spreadsheetPage.getCellColor(cellAddress));
        }
        for (String cellAddress : cellWithFormattingCondition) {
            if (!cellWithTrueCondition.contains(cellAddress)) {
                assertEquals(FALSE_CONDITION_COLOR,
                        spreadsheetPage.getCellColor(cellAddress));
            }
        }
    }

    private static Set<String> getCells() {
        Set<String> firstCellRange = Sets.newHashSet("A1", "A2");
        Set<String> secondCellRange = Sets.newHashSet("A3", "A4", "B3", "B4");
        Set<String> thirdCellRange = new HashSet<String>();
        for (int i = 1; i <= 5; i++) {
            for (String column : new String[] { "D", "E", "F", "G" }) {
                thirdCellRange.add(column + i);
            }
        }

        Set<String> union = new HashSet<String>(firstCellRange);
        union.addAll(secondCellRange);
        union.addAll(thirdCellRange);
        return union;
    }
}
