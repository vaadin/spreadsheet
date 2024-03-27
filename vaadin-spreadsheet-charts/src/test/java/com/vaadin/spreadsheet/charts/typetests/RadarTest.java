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
package com.vaadin.spreadsheet.charts.typetests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.PlotOptionsArea;
import com.vaadin.spreadsheet.charts.ChartTestBase;

public class RadarTest extends ChartTestBase {

    private Number[][] chartData = {
            { 0, 0, 0, 0, 0, 0, 0, 1500, 5000, 8500, 3500, 500 },
            { 2500, 5500, 9000, 6500, 3500, 0, 0, 0, 0, 0, 0, 0 },
            { 500, 750, 1500, 2000, 5500, 7500, 8500, 7000, 3500, 2500, 500,
                    100 },
            { 0, 1500, 2500, 4000, 3500, 1500, 800, 550, 2500, 6000, 5500,
                    3000 }

    };

    @Test
    public void notFilledRadar() throws Exception {
        Configuration conf = getChartFromSampleFile("Type Sample - Radar.xlsx",
                "G14").getConfiguration();
        assertSeriesType(conf.getSeries(), ChartType.AREA);
        assertData(conf.getSeries(), chartData);
        assertTrue(conf.getChart().getPolar());
        assertNotNull(
                (((PlotOptionsArea) conf.getSeries().get(0).getPlotOptions())
                        .getFillColor() != null));
    }

    @Test
    public void filledRadar() throws Exception {
        Configuration conf = getChartFromSampleFile(
                "Type Sample - Filled Radar.xlsx", "G14").getConfiguration();

        assertSeriesType(conf.getSeries(), ChartType.AREA);
        assertTrue(conf.getChart().getPolar());
        assertNull(((PlotOptionsArea) conf.getSeries().get(0).getPlotOptions())
                .getFillColor());

    }
}
