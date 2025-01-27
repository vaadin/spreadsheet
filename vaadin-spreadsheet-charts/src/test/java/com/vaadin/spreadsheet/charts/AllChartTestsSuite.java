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
package com.vaadin.spreadsheet.charts;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.vaadin.spreadsheet.charts.typetests.ColumnAndBarTest;
import com.vaadin.spreadsheet.charts.typetests.LineAreaScatterTest;
import com.vaadin.spreadsheet.charts.typetests.PieAndDonutTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({ LineAreaScatterTest.class, ChartFeatureTest.class,
        ColumnAndBarTest.class, PieAndDonutTest.class, StyleTest.class })
public class AllChartTestsSuite {

}
