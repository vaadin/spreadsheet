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

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.NativeEvent;
import com.vaadin.addon.spreadsheet.client.GroupingWidget.GroupingHandler;

/**
 * Sheet handler
 */
public interface SheetHandler extends GroupingHandler {

    /**
     * @param column
     * @param row
     * @param value
     * @param shiftPressed
     * @param metaOrCtrlPressed
     * @param updateToActionHandler
     */
    void onCellClick(int column, int row, String value, boolean shiftPressed,
            boolean metaOrCtrlPressed, boolean updateToActionHandler);

    /**
     * @param column
     * @param row
     */
    void onLinkCellClick(int column, int row);

    /**
     * @param column
     * @param row
     * @param value
     */
    void onCellDoubleClick(int column, int row, String value);

    /**
     * @param row
     * @param shiftPressed
     * @param metaOrCrtlPressed
     */
    void onRowHeaderClick(int row, boolean shiftPressed,
            boolean metaOrCrtlPressed);

    /**
     * @param row
     * @param shiftPressed
     * @param metaOrCrtlPressed
     */
    void onColumnHeaderClick(int row, boolean shiftPressed,
            boolean metaOrCrtlPressed);

    /**
     * @param firstRow
     * @param lastRow
     * @param firstColumn
     * @param lastColumn
     */
    void onScrollViewChanged(int firstRow, int lastRow, int firstColumn,
            int lastColumn);

    /**
     * @param c1
     * @param c2
     * @param r1
     * @param r2
     */
    void onSelectionIncreasePainted(int c1, int c2, int r1, int r2);

    /**
     * @param colEdgeIndex
     * @param rowEdgeIndex
     */
    void onSelectionDecreasePainted(int colEdgeIndex, int rowEdgeIndex);

    /**
     * @param col1
     * @param col2
     * @param row1
     * @param row2
     */
    void onFinishedSelectingCellsWithDrag(int col1, int col2, int row1, int row2);

    /**
     * @param parsedCol
     * @param parsedRow
     */
    void onSelectingCellsWithDrag(int parsedCol, int parsedRow);

    /**
     * @param value
     */
    void onCellInputBlur(String value);

    /**
     * handle event
     */
    void onCellInputFocus();

    /**
     * handle event
     */
    void onCellInputCancel();

    /**
     * @param value
     * @param shift
     */
    void onCellInputEnter(String value, boolean shift);

    /**
     * @param value
     * @param shift
     */
    void onCellInputTab(String value, boolean shift);

    /**
     * @param value
     */
    void onCellInputValueChange(String value);

    /**
     * @param nativeEvent
     * @param enteredCharacter
     */
    void onSheetKeyPress(NativeEvent nativeEvent, String enteredCharacter);

    /**
     * @return row buffer size
     */
    int getRowBufferSize();

    /**
     * @return column buffer size
     */
    int getColumnBufferSize();

    /**
     * default row height in points (?)
     *
     * @return
     */
    /**
     * @return default row height
     */
    float getDefaultRowHeight();

    /** Number of defined rows in the spreadsheet */
    /**
     * @return defined rows
     */
    int getDefinedRows();

    /**
     * @return column widths
     */
    int[] getColWidths();

    /**
     * Height of a row in points (pt) including bottom border. Rows are indexed
     * from 1 to getRows(). Returns 0 for hidden rows.
     * @param row
     * @return row height
     */
    float getRowHeight(int row);

    /**
     * Width of a column in pixels including right border. Columns are indexed
     * from 1 to getColumns(). Returns 0 for hidden columns.
     * @param col
     * @return width
     */
    int getColWidth(int col);

    /**
     * Returns 0 for hidden columns, otherwise same as {@link #getColWidth(int)}
     * .
     *
     * @param col
     *            1-based
     * @return width (px)
     */
    int getColWidthActual(int col);

    /**
     * Get header of a column as HTML. Columns are indexed from 1 to
     * getColumns().
     * @param col 
     * @return header HTML 
     */
    String getColHeader(int col);

    /** Get header of a row as HTML. Rows are indexed from 1 to getRows(). 
     * @param row 
     * @return row header HTML 
     **/
    String getRowHeader(int row);

    /**
     * The maximum amount of columns that are visible
     *
     * @return max columns
     */
    int getMaxColumns();

    /**
     * The maximum amount of rows that are visible
     *
     * @return max rows
     */
    int getMaxRows();

    /**
     * @return array of row heights in pixels
     */
    int[] getRowHeightsPX();

    /**
     * @return map of cell style indexes to CSS
     */
    Map<Integer, String> getCellStyleToCSSStyle();

    /**
     * @return map of row indexes to row style indexes
     */
    Map<Integer, Integer> getRowIndexToStyleIndex();

    /**
     * @return map of column indexes to column style indexes
     */
    Map<Integer, Integer> getColumnIndexToStyleIndex();

    /**
     * @return map of conditional style indexes to CSS
     */
    Map<Integer, String> getConditionalFormattingStyles();

    /**
     *
     * @param i
     *            1-based
     * @return true if the column is hidden
     */
    boolean isColumnHidden(int i);

    /**
     *
     * @param i
     *            1-based
     * @return true if the row is hidden
     */
    boolean isRowHidden(int i);

    /**
     * Called on right mouse button click on top of some cell.
     *
     * @param nativeEvent
     * @param column
     *            1-based
     * @param row
     *            1-based
     */
    void onCellRightClick(NativeEvent nativeEvent, int column, int row);

    /**
     * Called on right mouse button click on top of a row header
     *
     * @param nativeEvent
     * @param rowIndex
     *            1-based
     */
    void onRowHeaderRightClick(NativeEvent nativeEvent, int rowIndex);

    /**
     * Called on right mouse button click on top of a column header
     *
     * @param nativeEvent
     * @param columnIndex
     *            1-based
     */
    void onColumnHeaderRightClick(NativeEvent nativeEvent, int columnIndex);

    /**
     * @return true if there is a custom context menu
     */
    boolean hasCustomContextMenu();

    /**
     * @return true if columns can be resized
     */
    boolean canResizeColumn();

    /**
     * @return true if rows can be resized
     */
    boolean canResizeRow();

    /** Map containing 1-based row indexes and new sizes as pt
     * @param newSizes
     */
    void onRowsResized(Map<Integer, Float> newSizes);

    /** Map containing 1-based column indexes and new sizes as pt 
     * @param newSizes 
     **/
    void onColumnsResized(Map<Integer, Integer> newSizes);

    /**
     * @param rowIndex
     *     1-based
     */
    void onRowHeaderDoubleClick(int rowIndex);

    /**
     *
     * @param columnIndex
     *            1-based
     */
    void onColumnHeaderResizeDoubleClick(int columnIndex);

    /**
     * Returns the merged region that this cell belongs to.
     *
     * @param col
     * @param row
     * @return merged region for cell
     */
    MergedRegion getMergedRegion(int col, int row);

    /**
     * Params 1-based
     *
     * @param col
     *            starting column of merged cell
     * @param row
     *            starting row of merged cell
     * @return merged region starting from cell
     */
    MergedRegion getMergedRegionStartingFrom(int col, int row);

    /**
     * handle event
     */
    void onRedoPress();

    /**
     * handle event
     */
    void onUndoPress();

    /**
     * @param cellStyleWidthRatioMap
     */
    void setCellStyleWidthRatios(HashMap<Integer, Float> cellStyleWidthRatioMap);

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

    /**
     * @return widget
     */
    FormulaBarWidget getFormulaBarWidget();

    /**
     * @param text
     * @param col
     * @param row
     */
    void updateCellComment(String text, int col, int row);

    /**
     * select all cells
     */
    void selectAll();

    /**
     * @return true if protected
     */
    boolean isSheetProtected();

    /**
     * @param col
     * @return true if protected
     */
    boolean isColProtected(int col);

    /**
     * @param row
     * @return true if protected
     */
    boolean isRowProtected(int row);

}
