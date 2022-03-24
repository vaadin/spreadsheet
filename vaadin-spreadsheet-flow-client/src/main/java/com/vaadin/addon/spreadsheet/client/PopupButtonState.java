package com.vaadin.addon.spreadsheet.client;

/*
 * #%L
 * Vaadin Spreadsheet
 * %%
 * Copyright (C) 2013 - 2022 Vaadin Ltd
 * %%
 * This program is available under Commercial Vaadin Developer License
 * 4.0 (CVDLv4).
 * 
 * See the file license.html distributed with this software for more
 * information about licensing.
 * 
 * You should have received a copy of the CVALv3 along with this program.
 * If not, see <https://vaadin.com/license/cvdl-4.0>.
 * #L%
 */

import com.vaadin.shared.AbstractComponentState;

@SuppressWarnings("serial")
public class PopupButtonState extends AbstractComponentState {

    /** 1-based */
    public int col;
    /** 1-based */
    public int row;
    public boolean active;
    public boolean headerHidden;
    public String popupHeight = null;
    public String popupWidth = null;
    public String sheet;
}
