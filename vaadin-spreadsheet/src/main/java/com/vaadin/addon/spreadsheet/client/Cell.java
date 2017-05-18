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

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Overflow;

class Cell {

    public static final String CELL_COMMENT_TRIANGLE_CLASSNAME = "cell-comment-triangle";
    public static final String CELL_INVALID_FORMULA_CLASSNAME = "cell-invalidformula-triangle";
    private static final int Z_INDEX_VALUE = 2;

    /**
     * 1-based
     */
    private int col;
    /**
     * 1-based
     */
    private int row;
    private String value;

    private String cellStyle = "cs0";
    /**
     * Numeric values never overflow or wrap lines, they turn to ### if not fit
     */
    private boolean isNumeric;

    private boolean overflowDirty = true;

    private final DivElement element;
    private final SheetWidget sheetWidget;
    private DivElement cellCommentTriangle;
    private DivElement invalidFormulaTriangle;
    private Element popupButtonElement;

    public Cell(SheetWidget sheetWidget, int col, int row) {
        this.sheetWidget = sheetWidget;
        this.col = col;
        this.row = row;

        element = Document.get().createDivElement();

        updateClassName();
    }

    public Cell(SheetWidget sheetWidget, int col, int row, CellData cellData) {
        this(sheetWidget, col, row);

        updateCellData(col, row, cellData);
    }

    public DivElement getElement() {
        return element;
    }

    public void updateCellData(int col, int row, CellData cellData) {
        resetCellValues();

        // cellData could be null, so we can't use cellData.col/row
        this.col = col;
        this.row = row;

        if (cellData != null) {
            cellStyle = cellData.cellStyle;
            value = cellData.value;
            isNumeric = cellData.needsMeasure;
        }

        updateClassName();

        updateInnerText();
    }

    private void updateInnerText() {
        element.getStyle().setOverflow(Overflow.VISIBLE);

        if (value == null || value.isEmpty()) {
            element.setInnerText("");
            element.getStyle().clearZIndex();
        } else {
            element.getStyle().setZIndex(Z_INDEX_VALUE);

            if (value.startsWith("'")) {
                element.setInnerText(value.substring(1, value.length()));
            } else {
                element.setInnerText(value);
            }
        }
    }

    void updateOverflow() {

        boolean rightAligned = element.getAttribute("class").contains(" r ")
                || element.getAttribute("class").endsWith(" r");

        if (rightAligned)
            return;

        int overflowPx = measureScrollWidth() - getColumnWidth();

        // Increase overflow by cell left padding (2px)
        overflowPx += 2;

        if (overflowPx > 0) {
            if (isNumeric) {
                setElementText("###");
            } else {
                int width = getOverflowingDivWidth(overflowPx);
                createOverflowingDiv(width);
            }
        }

        // FIXME: is this whole thing necessary here?
//        if (sheetWidget.isMergedCell(SheetWidget.toKey(col, row))
//                && !(this instanceof MergedCell)) {
//            element.getStyle().setOverflow(Overflow.HIDDEN);
//        } else {
//            element.getStyle().setOverflow(Overflow.VISIBLE);
//        }

        overflowDirty = false;
    }

    private void createOverflowingDiv(int width) {
        // create element to contain the text, so we can apply overflow
        // rules
        DivElement overflowDiv = Document.get().createDivElement();
        overflowDiv.getStyle().setProperty("pointerEvents", "none");
        overflowDiv.getStyle().setWidth(width, Style.Unit.PX);
        overflowDiv.getStyle().setOverflow(Overflow.HIDDEN);
        overflowDiv.getStyle().setTextOverflow(Style.TextOverflow.ELLIPSIS);
        overflowDiv.setInnerText(element.getInnerText());
        element.setInnerText(null);
        element.appendChild(overflowDiv);
    }

    private int getColumnWidth() {
        return sheetWidget.actionHandler.getColWidth(col);
    }

    private int getOverflowingDivWidth(int overflowPx) {
        int colIndex = col;
        int width = 0;
        int[] colW = sheetWidget.actionHandler.getColWidths();
        boolean inFreezePane = col <= sheetWidget.verticalSplitPosition;

        while (colIndex < colW.length && width < overflowPx) {
            if (inFreezePane
                    && colIndex >= sheetWidget.verticalSplitPosition) {
                break;
            }
            Cell nextCell = sheetWidget.getCell(colIndex + 1, row);
            if (nextCell != null && nextCell.hasValue()) {
                break;
            }
            width += colW[colIndex];
            colIndex++;
        }
        // columnWidth is added after calculating the overflowing width
        width += getColumnWidth();
        return width;
    }

    int measureScrollWidth() {
        Integer scrollW = sheetWidget.scrollWidthCache.get(getUniqueKey());
        if (scrollW == null) {
            scrollW = element.getScrollWidth();
            sheetWidget.scrollWidthCache.put(getUniqueKey(), scrollW);
        }
        return scrollW;
    }

    private void resetCellValues() {
        row = -1;
        col = -1;

        cellStyle = "cs0";
        value = null;
        isNumeric = false;
        overflowDirty = true;

        removeCellCommentMark();
        removePopupButton();
        removeInvalidFormulaIndicator();
    }

    protected void updateClassName() {
        element.setClassName(SheetWidget.toKey(col, row) + " cell " + cellStyle);
    }

    public String getCellStyle() {
        return cellStyle;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value, String cellStyle, boolean needsMeasure) {
        if (!this.cellStyle.equals(cellStyle)) {
            this.cellStyle = cellStyle;
            updateClassName();
        }
        this.isNumeric = needsMeasure;
        setValue(value);
    }

    public void setValue(String value) {
        this.value = value;
        updateInnerText();

        if (cellCommentTriangle != null) {
            element.appendChild(cellCommentTriangle);
        }
        if (invalidFormulaTriangle != null) {
            element.appendChild(invalidFormulaTriangle);
        }
        if (popupButtonElement != null) {
            element.appendChild(popupButtonElement);
        }

        markAsOverflowDirty();
    }

    public void showPopupButton(Element popupButtonElement) {
        this.popupButtonElement = popupButtonElement;
        element.appendChild(popupButtonElement);
    }

    public void removePopupButton() {
        if (popupButtonElement != null) {
            popupButtonElement.removeFromParent();
            popupButtonElement = null;
        }
    }

    public void showCellCommentMark() {
        if (cellCommentTriangle == null) {
            cellCommentTriangle = Document.get().createDivElement();
            cellCommentTriangle.setClassName(CELL_COMMENT_TRIANGLE_CLASSNAME);
            element.appendChild(cellCommentTriangle);
        }
    }

    public void removeCellCommentMark() {
        if (cellCommentTriangle != null) {
            cellCommentTriangle.removeFromParent();
            cellCommentTriangle = null;
        }
    }

    public void showInvalidFormulaIndicator() {
        if (invalidFormulaTriangle == null) {
            invalidFormulaTriangle = Document.get().createDivElement();
            invalidFormulaTriangle.setClassName(CELL_INVALID_FORMULA_CLASSNAME);
            element.appendChild(invalidFormulaTriangle);
        }
    }

    public void removeInvalidFormulaIndicator() {
        if (invalidFormulaTriangle != null) {
            invalidFormulaTriangle.removeFromParent();
            invalidFormulaTriangle = null;
        }
    }

    public boolean isOverflowDirty() {
        return value != null && !value.isEmpty() && overflowDirty;
    }

    public void markAsOverflowDirty() {
        overflowDirty = true;
    }

    public boolean hasValue() {
        return value != null && !value.isEmpty();
    }

    private int getUniqueKey() {
        return 31 * (value.hashCode() + cellStyle.hashCode());
    }

    private void setElementText(String elementText) {
        // FIXME: make all set inner text calls use this method and add overlays
        element.setInnerText(elementText);
    }
}
