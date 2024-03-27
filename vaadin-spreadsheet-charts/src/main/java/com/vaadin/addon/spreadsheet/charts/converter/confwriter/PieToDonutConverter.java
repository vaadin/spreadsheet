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
package com.vaadin.addon.spreadsheet.charts.converter.confwriter;

import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.PlotOptionsPie;
import com.vaadin.addon.charts.model.Series;
import com.vaadin.addon.spreadsheet.charts.converter.chartdata.ChartData;
import com.vaadin.addon.spreadsheet.charts.converter.chartdata.PieSeriesData;

public class PieToDonutConverter {

    private static final double DONUT_GAP_BETWEEN_RINGS = 0.02;

    public static void convertIfNeeded(ChartData definition,
            Configuration conf) {
        if (definition.plotData.size() > 0
                && definition.plotData.get(0) instanceof PieSeriesData) {
            PieSeriesData pieSeriesData = (PieSeriesData) definition.plotData
                    .get(0);
            if (pieSeriesData.isDonut) {
                convertPieToDonut(conf, pieSeriesData.donutHoleSizePercent);
            }
        }
    }

    private static void convertPieToDonut(Configuration conf,
            short donutHoleSizePercent) {

        final int pieSerCount = countPieSeries(conf);

        int currentPieSeries = 0;
        final double onePieShare = (1.0 - donutHoleSizePercent / 100.0)
                / pieSerCount;
        final double gap = DONUT_GAP_BETWEEN_RINGS;

        for (Series ser : conf.getSeries()) {
            if (ser.getPlotOptions().getChartType() == ChartType.PIE) {
                double currentShare = 1 - currentPieSeries * onePieShare;
                double innerSize = (currentShare - onePieShare + gap)
                        / currentShare;

                ((PlotOptionsPie) ser.getPlotOptions())
                        .setInnerSize(Math.round(innerSize * 100) + "%");

                ((PlotOptionsPie) ser.getPlotOptions())
                        .setSize(Math.round(currentShare * 100) + "%");

                currentPieSeries++;
            }
        }
    }

    private static int countPieSeries(Configuration conf) {
        int count = 0;
        for (Series ser : conf.getSeries()) {
            if (ser.getPlotOptions().getChartType() == ChartType.PIE) {
                count++;
            }
        }
        return count;
    }

}
