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
package com.vaadin.addon.spreadsheet.charts.converter.confwriter;

import com.vaadin.addon.charts.model.AbstractPlotOptions;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.PlotOptionsArea;
import com.vaadin.addon.charts.model.Stacking;
import com.vaadin.addon.spreadsheet.charts.converter.Utils;
import com.vaadin.addon.spreadsheet.charts.converter.chartdata.AreaSeriesData;

public class AreaSeriesDataWriter extends AbstractSeriesDataWriter {

    public AreaSeriesDataWriter(AreaSeriesData series) {
        super(series);
    }

    @Override
    protected AreaSeriesData getSeriesData() {
        return (AreaSeriesData) super.getSeriesData();
    }

    @Override
    protected PlotOptionsArea getPlotOptions() {
        return (PlotOptionsArea) super.getPlotOptions();
    }

    @Override
    protected AbstractPlotOptions createPlotOptions() {
        return new PlotOptionsArea();
    }

    @Override
    protected void configureDataSeries(DataSeries dataSeriesForWriting) {
        super.configureDataSeries(dataSeriesForWriting);

        String stacking = getSeriesData().stacking.toString();

        getPlotOptions().setStacking(Utils.getEnumValueOrDefault(Stacking.class,
                stacking, Stacking.NONE));

        getPlotOptions().setDashStyle(
                LineSeriesWriterUtils.getDashStyle(getSeriesData().dashStyle));
        getPlotOptions().setMarker(
                LineSeriesWriterUtils.getMarker(getSeriesData().markerSymbol));

    }
}
