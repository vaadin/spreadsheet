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

import com.vaadin.shared.AbstractComponentState;

/**
 * PopupButtonState
 */
@SuppressWarnings("serial")
public class PopupButtonState extends AbstractComponentState {

    /** 1-based */
    public int col;
    /** 1-based */
    public int row;
    /**
     * active
     */
    public boolean active;
    /**
     * headerHidden
     */
    public boolean headerHidden;
    /**
     * popupHeight
     */
    public String popupHeight = null;
    /**
     * popupWidth
     */
    public String popupWidth = null;
    /**
     * sheet
     */
    public String sheet;
}
