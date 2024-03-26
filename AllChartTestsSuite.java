/*
 * Vaadin Spreadsheet Addon
 *
 * Copyright (C) 2012-2024 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.spreadsheet.charts;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.vaadin.spreadsheet.charts.interactiontests.InteractionTests;
import com.vaadin.spreadsheet.charts.typetests.LineAreaScatterTests;
import com.vaadin.spreadsheet.charts.typetests.ColumnAndBarTests;
import com.vaadin.spreadsheet.charts.typetests.PieAndDonutTests;

@RunWith(Suite.class)
@Suite.SuiteClasses({ LineAreaScatterTests.class, ChartFeatureTests.class,
        ColumnAndBarTests.class, // InteractionTests.class,
        PieAndDonutTests.class, StyleTests.class })
public class AllChartTestsSuite {

}
