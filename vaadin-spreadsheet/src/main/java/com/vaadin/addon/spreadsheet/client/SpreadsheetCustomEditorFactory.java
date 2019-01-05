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

import com.google.gwt.user.client.ui.Widget;

/**
 * SpreadsheetCustomEditorFactory
 */
public interface SpreadsheetCustomEditorFactory {

    /**
     * hasCustomEditor
     * @param selectedCellKey
     * @return boolean
     */
    boolean hasCustomEditor(String selectedCellKey);

    /**
     * getCustomEditor
     * @param selectedCellKey
     * @return Widget
     */
    Widget getCustomEditor(String selectedCellKey);

}
