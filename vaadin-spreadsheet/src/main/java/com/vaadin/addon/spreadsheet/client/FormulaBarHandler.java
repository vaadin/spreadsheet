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

/**
 * FormulaBarHandler
 */
public interface FormulaBarHandler {

    /**
     * onAddressEntered
     * @param value
     */
    void onAddressEntered(String value);

    /**
     * onAddressFieldEsc
     */
    void onAddressFieldEsc();

    /** Swap the cell data to the value if it is a formula. */
    /**
     * onFormulaFieldFocus
     * @param value
     */
    void onFormulaFieldFocus(String value);

    /**
     * onFormulaFieldBlur
     * @param value
     */
    void onFormulaFieldBlur(String value);

    /**
     * onFormulaEnter
     * @param value
     */
    void onFormulaEnter(String value);

    /**
     * onFormulaTab
     * @param value
     * @param focusSheet
     */
    void onFormulaTab(String value, boolean focusSheet);

    /**
     * onFormulaEsc
     */
    void onFormulaEsc();

    /**
     * onFormulaValueChange
     * @param value
     */
    void onFormulaValueChange(String value);

    /**
     * isTouchMode
     * @return boolean
     */
    boolean isTouchMode();

    /**
     * setSheetFocused
     * @param focused
     */
    void setSheetFocused(boolean focused);

    /**
     * createCellAddress
     * @param column
     * @param row
     * @return String
     */
    String createCellAddress(int column, int row);

    /**
     * getSheetNames
     * @return String[]
     */
    String[] getSheetNames();

    /**
     * getActiveSheetName
     * @return String
     */
    String getActiveSheetName();

}
