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
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.addon.charts.model.DataSeriesItem3d;
import com.vaadin.addon.charts.model.PlotOptionsBubble;
import com.vaadin.addon.spreadsheet.charts.converter.chartdata.AbstractSeriesData;
import com.vaadin.addon.spreadsheet.charts.converter.chartdata.AbstractSeriesData.SeriesPoint;

public class BubbleSeriesDataWriter extends AbstractSeriesDataWriter {

    public BubbleSeriesDataWriter(AbstractSeriesData series) {
        super(series);
    }

    @Override
    protected AbstractPlotOptions createPlotOptions() {
        return new PlotOptionsBubble();
    }

    @Override
    protected DataSeriesItem createDataSeriesItem(SeriesPoint point,
            boolean blanksAsZeros) {
        return new DataSeriesItem3d(point.xValue, point.yValue, point.zValue);
    }

}
