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
package com.vaadin.addon.spreadsheet;

import java.io.Serializable;

import org.apache.poi.xssf.usermodel.XSSFChart;

import com.vaadin.ui.Component;

public interface ChartCreator extends Serializable {

    /**
     * Converts the XSSFChart model into a Chart Component
     *
     * @param chartXml
     *            metadata with the chart configuration
     * @param spreadsheet
     *            spreadsheet that chart uses as data source
     * @return a Chart matching the given model, by itself or within a layout
     */
    public Component createChart(XSSFChart chartXml, Spreadsheet spreadsheet);
}
