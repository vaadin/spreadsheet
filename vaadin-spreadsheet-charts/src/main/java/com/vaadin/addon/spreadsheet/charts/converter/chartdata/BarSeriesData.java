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
package com.vaadin.addon.spreadsheet.charts.converter.chartdata;

import com.vaadin.addon.spreadsheet.charts.converter.confwriter.AbstractSeriesDataWriter;
import com.vaadin.addon.spreadsheet.charts.converter.confwriter.BarSeriesDataWriter;

public class BarSeriesData extends ColumnSeriesData {

    @Override
    public AbstractSeriesDataWriter getSeriesDataWriter() {
        return new BarSeriesDataWriter(this);
    }
}
