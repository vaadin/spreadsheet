package com.vaadin.addon.spreadsheet;

import java.io.Serializable;

import org.apache.poi.xssf.usermodel.XSSFChart;

import com.vaadin.ui.Component;

/**
 * Interface for converting a POI chart model to a Vaadin Chart component
 */
public interface ChartCreator extends Serializable {

    /**
     * Converts the XSSFChart model into a Chart Component
     * 
     * @param chartXml
     *            metadata with the chart configuration
     * @param spreadsheet
     *            spreadsheet that chart uses as data source
     * @return Chart component
     */
    public Component createChart(XSSFChart chartXml, Spreadsheet spreadsheet);
}
