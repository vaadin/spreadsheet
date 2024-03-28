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
package com.vaadin.spreadsheet.charts.typetests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.spreadsheet.charts.ChartTestBase;

public class CombinationChartTest extends ChartTestBase {
    @Test
    public void columnAndLineTest() throws Exception {
        Configuration conf = getChartFromSampleFile(
                "TypeSample - Combination (Column + Line + Dual Axis).xlsx",
                "A6").getConfiguration();

        assertCombinationChartSeriesType(conf);

        assertEquals("", conf.getxAxis().getTitle().getText());
        assertEquals("", conf.getyAxes().getAxes().get(0).getTitle().getText());
        assertEquals("", conf.getyAxes().getAxes().get(1).getTitle().getText());
    }

    private void assertCombinationChartSeriesType(Configuration conf) {
        assertEquals("Wrong series number", 5, conf.getSeries().size());
        assertSingleSeriesType(conf.getSeries().get(0), ChartType.COLUMN);
        assertSingleSeriesType(conf.getSeries().get(1), ChartType.COLUMN);
        assertSingleSeriesType(conf.getSeries().get(2), ChartType.COLUMN);
        assertSingleSeriesType(conf.getSeries().get(3), ChartType.LINE);
        assertSingleSeriesType(conf.getSeries().get(4), ChartType.LINE);
    }

    @Test
    public void columnAndLineWithAxisTitlesTest() throws Exception {
        Configuration conf = getChartFromSampleFile(
                "TypeSample - Combination (Column + Line + Dual Axis).xlsx",
                "A24").getConfiguration();

        assertCombinationChartSeriesType(conf);

        assertEquals("Title on the left",
                conf.getyAxes().getAxes().get(0).getTitle().getText());

        assertEquals("Title on the right",
                conf.getyAxes().getAxes().get(1).getTitle().getText());
    }
}
