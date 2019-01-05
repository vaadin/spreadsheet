package com.vaadin.addon.spreadsheet.client;

/*
 * #%L
 * Vaadin Spreadsheet
 * %%
 * Copyright (C) 2013 - 2015 Vaadin Ltd
 * %%
 * This program is available under Commercial Vaadin Add-On License 3.0
 * (CVALv3).
 * 
 * See the file license.html distributed with this software for more
 * information about licensing.
 * 
 * You should have received a copy of the CVALv3 along with this program.
 * If not, see <http://vaadin.com/license/cval-3>.
 * #L%
 */

import java.io.Serializable;

/**
 * MergedRegion
 */
@SuppressWarnings("serial")
public class MergedRegion implements Serializable {
    /**
     * id
     */
    public int id;
    /**
     * col1
     */
    public int col1;
    /**
     * col2
     */
    public int col2;
    /**
     * row1
     */
    public int row1;
    /**
     * row2
     */
    public int row2;

    /**
     * constructor
     */
    public MergedRegion() {
    }

    /**
     * @param c1
     * @param r1
     * @param c2
     * @param r2
     */
    public MergedRegion(int c1, int r1, int c2, int r2) {
        col1 = c1;
        row1 = r1;
        col2 = c2;
        row2 = r2;
    }
}