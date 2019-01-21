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
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.dom.client.TextAreaElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.vaadin.client.ui.VLabel;
import com.vaadin.client.ui.VOverlay;

/**
 * CellComment
 */
public class CellComment extends VOverlay {

    /**
     * COMMENT_OVERLAY_LINE_CLASSNAME
     */
    protected static final String COMMENT_OVERLAY_LINE_CLASSNAME = "comment-overlay-line";
    private static final String COMMENT_OVERLAY_CLASSNAME = "v-spreadsheet-comment-overlay";
    private static final String COMMENT_OVERLAY_AUTHOR_CLASSNAME = "comment-overlay-author";
    private static final String COMMENT_OVERLAY_INVALIDFORMULA_CLASSNAME = "comment-overlay-invalidformula";
    private static final String COMMENT_OVERLAY_SEPARATOR_CLASSNAME = "comment-overlay-separator";
    private static final String COMMENT_OVERLAY_LABEL_CLASSNAME = "comment-overlay-label";
    private static final String COMMENT_OVERLAY_INPUT_CLASSNAME = "comment-overlay-input";
    private static final String COMMENT_OVERLAY_SHADOW_CLASSNAME = COMMENT_OVERLAY_CLASSNAME
            + "-shadow";

    private final FlowPanel root;
    private final VLabel author;
    private final VLabel label;
    private final VLabel invalidFormula;

    private Element paneElement;

    private final DivElement line;

    private Element cellElement;
    private String linePositionClassName;

    private TextAreaElement input = Document.get().createTextAreaElement();

    private int offsetWidth;
    private int offsetHeight;
    private int cellRow;
    private int cellCol;

    /**
     * @param owner
     * @param paneElement
     */
    public CellComment(final SheetWidget owner, Element paneElement) {
        this.paneElement = paneElement;

        root = new FlowPanel();
        add(root);
        line = Document.get().createDivElement();
        line.setClassName(COMMENT_OVERLAY_LINE_CLASSNAME);

        author = new VLabel();
        author.setVisible(false);
        author.setStyleName(COMMENT_OVERLAY_AUTHOR_CLASSNAME);

        label = new VLabel();
        label.setVisible(false);
        label.setStyleName(COMMENT_OVERLAY_LABEL_CLASSNAME);
        setStyleName(COMMENT_OVERLAY_CLASSNAME);
        addStyleName(COMMENT_OVERLAY_SHADOW_CLASSNAME);
        setOwner(owner);
        setAnimationEnabled(false);
        setVisible(false);
        setZIndex(30);

        invalidFormula = new VLabel();
        invalidFormula.setVisible(false);
        invalidFormula.setStyleName(COMMENT_OVERLAY_INVALIDFORMULA_CLASSNAME);

        root.add(invalidFormula);
        root.add(author);
        root.add(label);

        // Comment editor
        input.addClassName(COMMENT_OVERLAY_INPUT_CLASSNAME);
        input.getStyle().setDisplay(Display.NONE);
        getElement().appendChild(input);
        input.setRows(4);
        input.getStyle().setWidth(200, Unit.PX);

        ClickHandler clickHandler = new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                owner.onCellCommentFocus(CellComment.this);
            }
        };
        author.addClickHandler(clickHandler);
        label.addClickHandler(clickHandler);
    }

    /**
     * bringForward 
     */
    public void bringForward() {
        setZIndex(35);
    }

    /**
     * pushBack 
     */
    public void pushBack() {
        setZIndex(30);
    }

    @Override
    public void hide() {
        super.hide();
        line.removeFromParent();
    }

    /**
     * setCommentText
     * @param text 
     */
    public void setCommentText(String text) {
        label.setText(text);
        hideIfNoContent(label);
        showOrHideSeparator();
    }

    /**
     * getCol
     * @return int
     */
    public int getCol() {
        return cellCol;
    }

    /**
     * getRow
     * @return int
     */
    public int getRow() {
        return cellRow;
    }

    /**
     * show
     * @param cellElement
     * @param row
     * @param col 
     */
    public void show(Element cellElement, int row, int col) {
        this.cellElement = cellElement;
        cellRow = row;
        cellCol = col;
        setVisible(false);
        show();
        offsetHeight = getOffsetHeight();
        offsetWidth = getOffsetWidth();
        calculatePosition();
        setVisible(true);
    }

    /**
     * showDependingToCellRightCorner
     * @param cellElement
     * @param row
     * @param col
     */
    public void showDependingToCellRightCorner(Element cellElement, int row,
            int col) {
        this.cellElement = cellElement;
        cellRow = row;
        cellCol = col;
        setVisible(false);
        show();
        offsetHeight = getOffsetHeight();
        offsetWidth = getOffsetWidth();
        refreshPositionAccordingToCellRightCorner();
    }

    /**
     * refreshPositionAccordingToCellRightCorner 
     */
    public void refreshPositionAccordingToCellRightCorner() {
        // do not set overlay visible if the cell top-right corner is not
        // visible on sheet
        if (cellElement != null) {
            int cellRight = cellElement.getAbsoluteRight();
            int cellTop = cellElement.getAbsoluteTop();
            if (cellRight >= paneElement.getAbsoluteLeft()
                    && cellRight < paneElement.getAbsoluteRight()
                    && cellTop >= paneElement.getAbsoluteTop()
                    && cellTop <= paneElement.getAbsoluteBottom()) {
                calculatePosition();
                setVisible(true);
                if (!isShowing()) {
                    show();
                }
            } else {
                setVisible(false);
            }
        } else {
            setVisible(false);
            hide();
        }
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        line.getStyle().setVisibility(
                visible ? Visibility.VISIBLE : Visibility.HIDDEN);
    }

    private void calculatePosition() {
        // display the overlay on the right side of the
        // cell if possible, move it up/down if necessary. if there is not
        // enough space on the right side, move it to the left.
        int cellRight = cellElement.getAbsoluteRight();
        int cellOffsetLeft = cellElement.getOffsetLeft();
        int cellOffsetWidth = cellElement.getOffsetWidth();
        int cellOffsetTop = cellElement.getOffsetTop();
        int cellTop = cellElement.getAbsoluteTop();
        int popupLeft = cellRight + 15;
        if (popupLeft + offsetWidth > paneElement.getAbsoluteRight()) {
            // move to left side if it fits there
            int temp = cellElement.getAbsoluteLeft() - 15 - offsetWidth;
            if (paneElement.getAbsoluteLeft() < temp) {
                popupLeft = temp;
            }
        }
        int popupTop = cellTop - 15;
        int sheetBottom = paneElement.getAbsoluteBottom();
        if (popupTop + offsetHeight > sheetBottom) {
            // move upwards as much possible to make it fit
            popupTop -= (popupTop + offsetHeight - sheetBottom + 5);
            int sheetTop = paneElement.getAbsoluteTop();
            if (popupTop < sheetTop) {
                popupTop = sheetTop;
            }
        } else if (popupTop < paneElement.getAbsoluteTop()) {
            popupTop += (paneElement.getAbsoluteTop() - popupTop);
        }
        setPopupPosition(popupLeft, popupTop);

        if (linePositionClassName != null) {
            line.removeClassName(linePositionClassName);
        }
        linePositionClassName = "col" + cellCol + " row" + cellRow;
        // Increase the target position to account for rounded corner
        popupTop += 2;
        popupLeft += 2;
        int a;
        int b = cellTop - popupTop;
        double deg;
        if (popupLeft > cellRight) {
            // draw the line to overlay top-left corner
            a = popupLeft - cellRight;
            if (b > 0) {
                deg = -1 * Math.toDegrees(Math.atan((double) b / (double) a));
            } else {
                b = Math.abs(b);
                deg = 0;
            }
        } else {
            // Decrease the left offset of target position to account for
            // rounded corner
            popupLeft -= 2;
            // draw the line to overlay top-right corner
            a = cellRight - (popupLeft + offsetWidth);
            if (b > 0) {
                deg = -180 + Math.toDegrees(Math.atan((double) b / (double) a));
            } else {
                b = Math.abs(b);
                deg = -180;
            }
        }
        double c = Math.sqrt(a * a + b * b) + 1;
        line.getStyle().setWidth(c, Unit.PX);
        line.getStyle().setTop(cellOffsetTop, Unit.PX);
        line.getStyle().setLeft(cellOffsetLeft + cellOffsetWidth, Unit.PX);
        line.getStyle().setProperty("transform", "rotate(" + deg + "deg)");
        line.getStyle().setProperty("msTransform", "rotate(" + deg + "deg)");
        line.getStyle()
                .setProperty("webkitTransform", "rotate(" + deg + "deg)");

        line.addClassName(linePositionClassName);
        paneElement.appendChild(line);
    }

    /**
     * setSheetElement
     * @param paneElement
     */
    public void setSheetElement(Element paneElement) {
        this.paneElement = paneElement;
    }

    /**
     * setEditMode
     * @param editMode
     */
    public void setEditMode(boolean editMode) {
        if (editMode) {
            input.setValue(label.getText());
            label.setVisible(false);
            input.getStyle().setDisplay(Display.BLOCK);
            input.focus();
            input.select();
        } else {
            label.setText(input.getValue());
            label.setVisible(true);
            input.getStyle().setDisplay(Display.NONE);
            ((SheetWidget) getOwner()).commitComment(label.getText(), getCol(),
                    getRow());
        }
        showOrHideSeparator();
    }

    /**
     * setAuthor
     * @param authorName
     */
    public void setAuthor(String authorName) {
        author.setText(authorName);
        hideIfNoContent(author);
        showOrHideSeparator();
    }

    /**
     * setInvalidFormulaMessage
     * @param invalidFormulaMessage
     */
    public void setInvalidFormulaMessage(String invalidFormulaMessage) {
        invalidFormula.setText(invalidFormulaMessage);
        hideIfNoContent(invalidFormula);
        showOrHideSeparator();
    }

    private void hideIfNoContent(VLabel label) {
        if(label.getText().isEmpty()) {
            label.setVisible(false);
        } else {
            label.setVisible(true);
        }
    }

    private void showOrHideSeparator() {
        if(invalidFormula.isVisible() && (author.isVisible() || label.isVisible()
                        || Display.BLOCK.equals(input.getStyle().getDisplay()) )) {
            if(!invalidFormula.getStyleName().contains(COMMENT_OVERLAY_SEPARATOR_CLASSNAME)) {
                invalidFormula.addStyleName(COMMENT_OVERLAY_SEPARATOR_CLASSNAME);
            }
        } else {
            if(invalidFormula.getStyleName().contains(COMMENT_OVERLAY_SEPARATOR_CLASSNAME)) {
                invalidFormula.removeStyleName(COMMENT_OVERLAY_SEPARATOR_CLASSNAME);
            }
        }
    }
}