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

public class Cell {

    public static final String CELL_COMMENT_TRIANGLE_CLASSNAME = "cell-comment-triangle";
    public static final String CELL_INVALID_FORMULA_CLASSNAME = "cell-invalidformula-triangle";
    private static final int ZINDEXVALUE = 2;
    private final DivElement element;
    private DivElement cellCommentTriangle;
    private DivElement invalidFormulaTriangle;
    /**
     * 1-based
     */
    private int col;
    /**
     * 1-based
     */
    private int row;
    private Element popupButtonElement;
    private String value;

    private String cellStyle = "cs0";
    private boolean needsMeasure;
    private SheetWidget sheetWidget;

    public Cell(SheetWidget sheetWidget, int col, int row) {
        this.sheetWidget = sheetWidget;
        this.col = col;
        this.row = row;

        element = Document.get().createDivElement();
        updateCellValues();
    }

    public Cell(SheetWidget sheetWidget, int col, int row, CellData cellData) {
        this.sheetWidget = sheetWidget;
        this.col = col;
        this.row = row;
        element = Document.get().createDivElement();
        if (cellData == null) {
            value = null;
        } else {
            needsMeasure = cellData.needsMeasure;
            value = cellData.value;
            cellStyle = cellData.cellStyle;
        }
        updateCellValues();
        updateInnerText();
    }

    public DivElement getElement() {
        return element;
    }

    public void update(int col, int row, CellData cellData) {
        this.col = col;
        this.row = row;
        cellStyle = cellData == null ? "cs0" : cellData.cellStyle;
        value = cellData == null ? null : cellData.value;
        needsMeasure = cellData == null ? false : cellData.needsMeasure;

        updateInnerText();
        updateCellValues();
    }

    private void updateInnerText() {
        if (value == null || value.isEmpty()) {
            element.setInnerText("");
            element.getStyle().clearZIndex();
        } else {
            element.getStyle().setZIndex(ZINDEXVALUE);
            if (needsMeasure
                    && sheetWidget.measureValueWidth(cellStyle, value) > getElement()
                            .getClientWidth()) {
                element.setInnerText("###");
            } else {
                element.setInnerText(value);
            }
        }
    }

    protected void updateCellValues() {
        removeCellCommentMark();
        removePopupButton();
        updateClassName();
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
        this.needsMeasure = needsMeasure;
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
}
