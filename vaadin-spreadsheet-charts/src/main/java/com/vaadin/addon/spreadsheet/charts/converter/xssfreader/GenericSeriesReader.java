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
package com.vaadin.addon.spreadsheet.charts.converter.xssfreader;

import org.apache.xmlbeans.XmlObject;

import com.vaadin.addon.charts.model.AbstractPlotOptions;
import com.vaadin.addon.charts.model.PlotOptionsLine;
import com.vaadin.addon.spreadsheet.Spreadsheet;
import com.vaadin.addon.spreadsheet.charts.converter.chartdata.AbstractSeriesData;
import com.vaadin.addon.spreadsheet.charts.converter.confwriter.AbstractSeriesDataWriter;

/**
 * Can be used to test reading of a new chart type or as a temporary means to
 * see the data.
 */
@SuppressWarnings("rawtypes")
public class GenericSeriesReader extends AbstractSeriesReader {

    public GenericSeriesReader(XmlObject ctChart, Spreadsheet spreadsheet,
            boolean showDataInHiddenCells) {
        super(ctChart, spreadsheet, showDataInHiddenCells);
    }

    @Override
    protected AbstractSeriesData createSeriesDataObject(XmlObject serie) {
        return new AbstractSeriesData() {
            @Override
            public AbstractSeriesDataWriter getSeriesDataWriter() {
                return new AbstractSeriesDataWriter(this) {
                    @Override
                    protected AbstractPlotOptions createPlotOptions() {
                        return new PlotOptionsLine();
                    }
                };
            }
        };
    }
}
