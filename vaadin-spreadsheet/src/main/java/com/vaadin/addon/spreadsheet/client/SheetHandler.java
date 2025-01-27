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
package com.vaadin.addon.spreadsheet.client;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.NativeEvent;
import com.vaadin.addon.spreadsheet.client.GroupingWidget.GroupingHandler;

public interface SheetHandler extends GroupingHandler {

    void onCellClick(int column, int row, String value, boolean shiftPressed,
            boolean metaOrCtrlPressed, boolean updateToActionHandler);

    void onLinkCellClick(int column, int row);

    void onCellDoubleClick(int column, int row, String value);

    void onRowHeaderClick(int row, boolean shiftPressed,
            boolean metaOrCrtlPressed);

    void onColumnHeaderClick(int row, boolean shiftPressed,
            boolean metaOrCrtlPressed);

    void onScrollViewChanged(int firstRow, int lastRow, int firstColumn,
            int lastColumn);

    void onSelectionIncreasePainted(int c1, int c2, int r1, int r2);

    void onSelectionDecreasePainted(int colEdgeIndex, int rowEdgeIndex);

    void onFinishedSelectingCellsWithDrag(int col1, int col2, int row1,
            int row2);

    void onSelectingCellsWithDrag(int parsedCol, int parsedRow);

    void onCellInputBlur(String value);

    void onCellInputFocus();

    void onCellInputCancel();

    void onCellInputEnter(String value, boolean shift);

    void onCellInputTab(String value, boolean shift);

    void onCellInputValueChange(String value);

    void onSheetKeyPress(NativeEvent nativeEvent, String enteredCharacter);

    int getRowBufferSize();

    int getColumnBufferSize();

    /**
     * Returns the default row height in points (pt).
     *
     * @return height
     */
    float getDefaultRowHeight();

    /**
     * Returns the number of defined rows in the spreadsheet.
     *
     * @return row count
     */
    int getDefinedRows();

    /**
     * Returns the widths of the columns.
     *
     * @return width array
     */
    int[] getColWidths();

    /**
     * Height of a row in points (pt) including bottom border. Rows are indexed
     * from 1 to getRows(). Returns 0 for hidden rows.
     *
     * @param row
     *            row index, 1-based
     * @return height
     */
    float getRowHeight(int row);

    /**
     * Width of a column in pixels including right border. Columns are indexed
     * from 1 to getColumns(). Returns 0 for hidden columns.
     *
     * @param col
     *            column index, 1-based
     * @return width
     */
    int getColWidth(int col);

    /**
     * Returns 0 for hidden columns, otherwise same as
     * {@link #getColWidth(int)}.
     *
     * @param col
     *            column index, 1-based
     * @return width (px)
     */
    int getColWidthActual(int col);

    /**
     * Get header of a column as HTML. Columns are indexed from 1 to
     * getColumns().
     *
     * @param col
     *            column index, 1-based
     * @return header HTML
     */
    String getColHeader(int col);

    /**
     * Get header of a row as HTML. Rows are indexed from 1 to getRows().
     *
     * @param row
     *            row index, 1-based
     * @return header HTML
     */
    String getRowHeader(int row);

    /**
     * The maximum amount of columns that are visible.
     *
     * @return count
     */
    int getMaxColumns();

    /**
     * The maximum amount of rows that are visible.
     *
     * @return count
     */
    int getMaxRows();

    int[] getRowHeightsPX();

    Map<Integer, String> getCellStyleToCSSStyle();

    Map<Integer, Integer> getRowIndexToStyleIndex();

    Map<Integer, Integer> getColumnIndexToStyleIndex();

    Map<Integer, String> getConditionalFormattingStyles();

    /**
     * Returns whether the given column is hidden.
     *
     * @param col
     *            column index, 1-based
     * @return {@code true} if the column is hidden, {@code false} otherwise
     */
    boolean isColumnHidden(int col);

    /**
     * Returns whether the given row is hidden.
     *
     * @param row
     *            row index, 1-based
     * @return {@code true} if the row is hidden, {@code false} otherwise
     */
    boolean isRowHidden(int row);

    /**
     * Called on right mouse button click on top of some cell.
     *
     * @param nativeEvent
     *            triggered event
     * @param column
     *            index, 1-based
     * @param row
     *            index, 1-based
     */
    void onCellRightClick(NativeEvent nativeEvent, int column, int row);

    /**
     * Called on right mouse button click on top of a row header.
     *
     * @param nativeEvent
     *            triggered event
     * @param rowIndex
     *            1-based
     */
    void onRowHeaderRightClick(NativeEvent nativeEvent, int rowIndex);

    /**
     * Called on right mouse button click on top of a column header.
     *
     * @param nativeEvent
     *            triggered event
     * @param columnIndex
     *            1-based
     */
    void onColumnHeaderRightClick(NativeEvent nativeEvent, int columnIndex);

    boolean hasCustomContextMenu();

    boolean canResizeColumn();

    boolean canResizeRow();

    /**
     * Triggered after row resize.
     *
     * @param newSizes
     *            map containing 1-based row indexes and new sizes as pt
     */
    void onRowsResized(Map<Integer, Float> newSizes);

    /**
     * Triggered after column resize.
     *
     * @param newSizes
     *            map containing 1-based column indexes and new sizes as pt
     */
    void onColumnsResized(Map<Integer, Integer> newSizes);

    /**
     * Triggered when a row header receives a double-click.
     *
     * @param rowIndex
     *            1-based
     */
    void onRowHeaderDoubleClick(int rowIndex);

    /**
     * Triggered when a column header receives a double-click.
     *
     * @param columnIndex
     *            1-based
     */
    void onColumnHeaderResizeDoubleClick(int columnIndex);

    /**
     * Returns the merged region that this cell belongs to.
     *
     * @param col
     *            column index, 1-based
     * @param row
     *            row index, 1-based
     * @return merged region or {@code null} if not found
     */
    MergedRegion getMergedRegion(int col, int row);

    /**
     * Returns the merged region starting at the given coordinates.
     *
     * @param col
     *            starting column index of merged cell, 1-based
     * @param row
     *            starting row index of merged cell, 1-based
     * @return merged region or {@code null} if not found
     */
    MergedRegion getMergedRegionStartingFrom(int col, int row);

    void onRedoPress();

    void onUndoPress();

    void setCellStyleWidthRatios(
            HashMap<Integer, Float> cellStyleWidthRatioMap);

    /**
     * Called when user pastes something inside the sheet.
     *
     * @param text
     *            the pasted content
     */
    void onSheetPaste(String text);

    /**
     * Called after successful cut operation; currently selected cells should be
     * cleared
     */
    void clearSelectedCellsOnCut();

    FormulaBarWidget getFormulaBarWidget();

    void updateCellComment(String text, int col, int row);

    void selectAll();

    boolean isSheetProtected();

    boolean isColProtected(int col);

    boolean isRowProtected(int row);

}
