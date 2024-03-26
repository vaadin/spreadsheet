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

import com.vaadin.addon.charts.model.AbstractPlotOptions;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.PlotOptionsLine;
import com.vaadin.addon.spreadsheet.charts.converter.chartdata.LineSeriesData;

public class LineSeriesDataWriter extends AbstractSeriesDataWriter {

    public LineSeriesDataWriter(LineSeriesData series) {
        super(series);
    }

    @Override
    protected LineSeriesData getSeriesData() {
        return (LineSeriesData) super.getSeriesData();
    }

    @Override
    protected AbstractPlotOptions createPlotOptions() {
        return new PlotOptionsLine();
    }

    @Override
    protected PlotOptionsLine getPlotOptions() {
        return (PlotOptionsLine) super.getPlotOptions();
    }

    @Override
    protected void configureDataSeries(DataSeries dataSeriesForWriting) {
        super.configureDataSeries(dataSeriesForWriting);

        getPlotOptions().setDashStyle(
                LineSeriesWriterUtils.getDashStyle(getSeriesData().dashStyle));
        getPlotOptions().setMarker(
                LineSeriesWriterUtils.getMarker(getSeriesData().markerSymbol));
    }
}
