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
package com.vaadin.spreadsheet.charts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.HorizontalAlign;
import com.vaadin.addon.charts.model.LayoutDirection;
import com.vaadin.addon.charts.model.VerticalAlign;

public class ChartFeatureTests extends ChartTestBase {

    @Test
    public void axisTitles_loadSampleB3_titlesAbsent() throws Exception {
        Configuration conf = getChartFromSampleFile(
                "FeatureSample - Axis Title Options.xlsx", "B3")
                        .getConfiguration();

        assertEquals("", conf.getxAxis().getTitle().getText());
        assertEquals("", conf.getyAxis().getTitle().getText());
    }

    @Test
    public void axisTitles_loadSampleG3_titlesPresentAndCorrect()
            throws Exception {
        Configuration conf = getChartFromSampleFile(
                "FeatureSample - Axis Title Options.xlsx", "G3")
                        .getConfiguration();

        assertEquals("horizontal title", conf.getyAxis().getTitle().getText());
        assertEquals("Title below axis", conf.getxAxis().getTitle().getText());
    }

    @Test
    public void chartTitle_loadSampleA3_titlesAbsent() throws Exception {
        Configuration conf = getChartFromSampleFile(
                "FeatureSample - Custom Title Position.xlsx", "A3")
                        .getConfiguration();

        assertEquals("", conf.getTitle().getText());
    }

    @Test
    public void chartTitle_loadSampleE3_titlesPresentAbove() throws Exception {
        Configuration conf = getChartFromSampleFile(
                "FeatureSample - Custom Title Position.xlsx", "E3")
                        .getConfiguration();

        assertEquals("Title above", conf.getTitle().getText());
        assertEquals(Boolean.FALSE, conf.getTitle().getFloating());
    }

    @Test
    public void chartTitle_loadSampleI3_titlesPresentFloating()
            throws Exception {
        Configuration conf = getChartFromSampleFile(
                "FeatureSample - Custom Title Position.xlsx", "I3")
                        .getConfiguration();

        assertEquals("Title overlay", conf.getTitle().getText());
        assertEquals(Boolean.TRUE, conf.getTitle().getFloating());
    }

    @Test
    public void chartLegend_loadSampleA7_legendAbsent() throws Exception {
        Configuration conf = getChartFromSampleFile(
                "FeatureSample - Legend Position.xlsx", "A7")
                        .getConfiguration();

        assertEquals(Boolean.FALSE, conf.getLegend().getEnabled());
    }

    @Test
    public void chartLegend_loadSampleI7_legendOnTop() throws Exception {
        Configuration conf = getChartFromSampleFile(
                "FeatureSample - Legend Position.xlsx", "I7")
                        .getConfiguration();

        assertEquals(VerticalAlign.TOP, conf.getLegend().getVerticalAlign());
        assertEquals(HorizontalAlign.CENTER, conf.getLegend().getAlign());
        assertEquals(LayoutDirection.HORIZONTAL, conf.getLegend().getLayout());
    }

    @Test
    public void chartLegend_loadSampleR7_legendOnLeft() throws Exception {
        Configuration conf = getChartFromSampleFile(
                "FeatureSample - Legend Position.xlsx", "R7")
                        .getConfiguration();

        assertEquals(VerticalAlign.MIDDLE, conf.getLegend().getVerticalAlign());
        assertEquals(HorizontalAlign.LEFT, conf.getLegend().getAlign());
        assertEquals(LayoutDirection.VERTICAL, conf.getLegend().getLayout());
    }

    @Test
    public void chartLegend_loadSampleA25_legendOnTopRight() throws Exception {
        Configuration conf = getChartFromSampleFile(
                "FeatureSample - Legend Position.xlsx", "A25")
                        .getConfiguration();

        assertEquals(VerticalAlign.TOP, conf.getLegend().getVerticalAlign());
        assertEquals(HorizontalAlign.RIGHT, conf.getLegend().getAlign());
        assertEquals(LayoutDirection.VERTICAL, conf.getLegend().getLayout());
    }

    @Test
    public void chartLegend_loadSampleI25_legendOnBottom() throws Exception {
        Configuration conf = getChartFromSampleFile(
                "FeatureSample - Legend Position.xlsx", "I25")
                        .getConfiguration();

        assertEquals(VerticalAlign.BOTTOM, conf.getLegend().getVerticalAlign());
        assertEquals(HorizontalAlign.CENTER, conf.getLegend().getAlign());
        assertEquals(LayoutDirection.HORIZONTAL, conf.getLegend().getLayout());
    }

    @Test
    public void chartLegend_loadSampleR25_legendOnRight() throws Exception {
        Configuration conf = getChartFromSampleFile(
                "FeatureSample - Legend Position.xlsx", "R25")
                        .getConfiguration();

        assertEquals(VerticalAlign.MIDDLE, conf.getLegend().getVerticalAlign());
        assertEquals(HorizontalAlign.RIGHT, conf.getLegend().getAlign());
        assertEquals(LayoutDirection.VERTICAL, conf.getLegend().getLayout());
    }

    /**
     * In Vaadin charts if both title and legend are aligned to top, the legend
     * overlaps the title. This test checks if a y-offset is set, a possible
     * workaround for this issue.
     */
    @Test
    public void chartLegend_loadSampleI43_legendOnTopAndYOffsetIsSet()
            throws Exception {
        Configuration conf = getChartFromSampleFile(
                "FeatureSample - Legend Position.xlsx", "I43")
                        .getConfiguration();

        assertEquals(VerticalAlign.TOP, conf.getLegend().getVerticalAlign());
        assertEquals(HorizontalAlign.CENTER, conf.getLegend().getAlign());
        assertEquals(LayoutDirection.HORIZONTAL, conf.getLegend().getLayout());

        assertTrue(
                "Vertical offset for legend is not set, overlapping might occur",
                conf.getLegend().getY() != null);
    }

    private static final Double[] ZEROS = { 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d,
            0d };

    private static final Double[] NULLS = { null, null, null, null, null, null,
            null, null, null, null };

    private static final Double[][] blanksToZerosData = { ZEROS, ZEROS, ZEROS,
            ZEROS, ZEROS, ZEROS,
            { 10d, 0d, 43d, 16d, 0d, 8d, 0d, 0d, 35d, 78d } };

    private static final Double[][] blanksToNullsData = { NULLS, NULLS, NULLS,
            NULLS, NULLS, NULLS,
            { 10d, null, 43d, 16d, null, 8d, null, null, 35d, 78d } };

    @Test
    public void blanksAsZeros_loadSampleB14_blanksTreatedAsZeros()
            throws Exception {
        Configuration conf = getChartFromSampleFile(
                "FeatureSample - Blanks as zeros.xlsm", "B14")
                        .getConfiguration();

        assertData(conf.getSeries(), blanksToZerosData);
    }

    @Test
    public void blanksAsZeros_loadSampleB29_blanksTreatedAsNulls()
            throws Exception {
        Configuration conf = getChartFromSampleFile(
                "FeatureSample - Blanks as zeros.xlsm", "B29")
                        .getConfiguration();

        assertData(conf.getSeries(), blanksToNullsData);
    }

    @Test
    public void blanksAsZeros_loadSampleK14_blanksTreatedAsZeros()
            throws Exception {
        Configuration conf = getChartFromSampleFile(
                "FeatureSample - Blanks as zeros.xlsm", "K14")
                        .getConfiguration();

        assertData(conf.getSeries(), blanksToZerosData);
    }

    @Test
    public void blanksAsZeros_loadSampleK29_blanksTreatedAsNulls()
            throws Exception {
        Configuration conf = getChartFromSampleFile(
                "FeatureSample - Blanks as zeros.xlsm", "K29")
                        .getConfiguration();

        assertData(conf.getSeries(), blanksToNullsData);
    }

    @Test
    public void dualAxis_loadSampleA6_hasTwoAxesAndAssignedCorrectlyToSeries()
            throws Exception {
        Configuration conf = getChartFromSampleFile(
                "TypeSample - Combination (Column + Line + Dual Axis).xlsx",
                "A6").getConfiguration();

        assertEquals(2, conf.getyAxes().getNumberOfAxes());

        assertEquals(new Integer(0),
                ((DataSeries) conf.getSeries().get(0)).getyAxis());
        assertEquals(new Integer(0),
                ((DataSeries) conf.getSeries().get(1)).getyAxis());
        assertEquals(new Integer(0),
                ((DataSeries) conf.getSeries().get(2)).getyAxis());
        assertEquals(new Integer(1),
                ((DataSeries) conf.getSeries().get(3)).getyAxis());
        assertEquals(new Integer(1),
                ((DataSeries) conf.getSeries().get(4)).getyAxis());
    }

}
