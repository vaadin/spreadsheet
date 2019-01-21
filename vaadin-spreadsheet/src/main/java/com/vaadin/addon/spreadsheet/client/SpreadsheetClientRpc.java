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

import java.util.ArrayList;

import com.vaadin.shared.communication.ClientRpc;

/**
 * SpreadsheetClientRpc
 */
public interface SpreadsheetClientRpc extends ClientRpc {

    /**
     * updateBottomRightCellValues
     * @param cellData
     */
    void updateBottomRightCellValues(ArrayList<CellData> cellData);

    /**
     * updateTopLeftCellValues
     * @param cellData
     */
    void updateTopLeftCellValues(ArrayList<CellData> cellData);

    /**
     * updateTopRightCellValues
     * @param cellData
     */
    void updateTopRightCellValues(ArrayList<CellData> cellData);

    /**
     * updateBottomLeftCellValues
     * @param cellData
     */
    void updateBottomLeftCellValues(ArrayList<CellData> cellData);

    /**
     * @param col
     *            Selected cell's column. 1-based
     * @param row
     *            Selected cell's row. 1-based
     */
    /**
     * updateFormulaBar
     * @param possibleName
     * @param col
     * @param row
     */
    void updateFormulaBar(String possibleName, int col, int row);

    /**
     * invalidCellAddress
     */
    void invalidCellAddress();

    /**
     * showSelectedCell
     * @param name
     * @param col
     * @param row
     * @param cellValue
     * @param function
     * @param locked
     * @param initialSelection
     */
    void showSelectedCell(String name, int col, int row, String cellValue, boolean function,
            boolean locked, boolean initialSelection);

    /**
     * The String arrays contain the caption and the icon resource key.
     * 
     * @param actionDetails
     */
    void showActions(ArrayList<SpreadsheetActionDetails> actionDetails);

    /**
     * Updates the selected cell and painted range. Displays the selected cell
     * value. Indexes 1-based.
     * @param name 
     * @param col 
     * @param row 
     * @param c1 
     * @param c2 
     * @param r1 
     * @param r2 
     * @param scroll 
     */
    void setSelectedCellAndRange(String name, int col, int row, int c1, int c2, int r1,
            int r2, boolean scroll);

    /**
     * cellsUpdated
     * @param updatedCellData
     */
    void cellsUpdated(ArrayList<CellData> updatedCellData);

    /**
     * refreshCellStyles
     */
    void refreshCellStyles();

    /**
     * editCellComment
     * @param col
     * @param row
     */
    void editCellComment(int col, int row);
}