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
package com.vaadin.addon.spreadsheet.charts.converter.chartdata;

import com.vaadin.addon.spreadsheet.charts.converter.confwriter.AbstractSeriesDataWriter;
import com.vaadin.addon.spreadsheet.charts.converter.confwriter.PieSeriesDataWriter;

public class PieSeriesData extends AbstractSeriesData {

    public boolean isExploded;
    public boolean isDonut;
    public short donutHoleSizePercent;

    @Override
    public AbstractSeriesDataWriter getSeriesDataWriter() {
        return new PieSeriesDataWriter(this);
    }
}
