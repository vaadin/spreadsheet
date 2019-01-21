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
 * OverlayInfo
 */
@SuppressWarnings("serial")
public class OverlayInfo implements Serializable {
    /**
     * Type
     */
    public enum Type {
        /**
         * IMAGE
         */
        IMAGE,
        /**
         * COMPONENT
         */
        COMPONENT
    };
    
    /**
     * constructor
     */
    public OverlayInfo() {
    }

    /**
     * @param t
     */
    public OverlayInfo(Type t) {
        type = t;
    }    
    
    /**
     * type
     */
    public Type type = Type.IMAGE;

    /**
     * col
     */
    public int col;
    /**
     * row
     */
    public int row;
    /**
     * width
     */
    public float width;
    /**
     * height
     */
    public float height;
    /**
     * dy
     */
    public float dy;
    /**
     * dx
     */
    public float dx;

    @Override
    public String toString() {
        return "OverlayInfo: col=" + col + ", row=" + row + ", width=" + width
                + ", height=" + height + ", dx=" + dx + ", dy=" + dy;
    }
}