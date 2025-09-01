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
package com.vaadin.addon.spreadsheet.charts.converter.xssfreader;

import org.openxmlformats.schemas.drawingml.x2006.chart.CTDoughnutChart;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTPie3DChart;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTPieChart;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTPieSer;

import com.vaadin.addon.spreadsheet.Spreadsheet;
import com.vaadin.addon.spreadsheet.charts.converter.chartdata.PieSeriesData;

public class PieSeriesReader
        extends AbstractSeriesReader<CTPieSer, PieSeriesData> {

    private boolean isDoughnut = false;

    /**
     * In Excel donuts have only one exploded ring, this flag marks that we
     * handled it
     */
    private boolean isExplodedDoughnutHandled = false;

    public PieSeriesReader(CTPieChart ctChart, Spreadsheet spreadsheet,
            boolean showDataInHiddenCells) {
        super(ctChart, spreadsheet, showDataInHiddenCells);
    }

    public PieSeriesReader(CTDoughnutChart ctChart, Spreadsheet spreadsheet,
            boolean showDataInHiddenCells) {
        super(ctChart, spreadsheet, showDataInHiddenCells);
        isDoughnut = true;
    }

    public PieSeriesReader(CTPie3DChart ctChart, Spreadsheet spreadsheet,
            boolean showDataInHiddenCells) {
        super(ctChart, spreadsheet, true, showDataInHiddenCells);
    }

    @Override
    protected PieSeriesData createSeriesDataObject(CTPieSer serie) {
        return new PieSeriesData();
    }

    @Override
    protected void fillSeriesData(PieSeriesData seriesData, CTPieSer serie) {
        super.fillSeriesData(seriesData, serie);

        if (!isDoughnut || !isExplodedDoughnutHandled) {
            seriesData.isExploded = serie.isSetExplosion();
        }

        if (isDoughnut) {
            isExplodedDoughnutHandled = true;
            seriesData.isDonut = true;

            // This cast was potentially dangerous - chart.getHoleSize().getVal()
            // might not be naturally castable to short. We now parse the returned
            // value in hopes of getting a more reliable result, but if the result
            // is wrong, we want it to look obviously wrong.
            short sizePct = 90; 
            try {
                CTDoughnutChart chart = (CTDoughnutChart) getChart();
                Object val = chart.getHoleSize().getVal();
                sizePct = Short.parseShort(val.toString());
            } catch (NumberFormatException ex) {
            } catch (NullPointerException ex) {
            }
            seriesData.donutHoleSizePercent = sizePct;
        }
    }
}
