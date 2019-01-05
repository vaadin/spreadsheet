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
import java.util.List;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.HasCloseHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ui.VOverlay;

/**
 * PopupButtonWidget overlay
 */
public class PopupButtonWidget extends FocusWidget implements ClickHandler,
        HasCloseHandlers<PopupPanel> {

    /**
     * popup style class
     */
    protected static final String BUTTON_CLASSNAME = "popupbutton";
    /**
     * overlay style class
     */
    protected static final String POPUP_OVERLAY_CLASSNAME = "v-spreadsheet-"
            + BUTTON_CLASSNAME + "-overlay";
    /**
     * layout style class
     */
    protected static final String POPUP_OVERLAY_LAYOUT_CLASSNAME = "overlay-layout";
    /**
     * active style class
     */
    protected static final String BUTTON_ACTIVE_CLASSNAME = "active";

    final DivElement root = Document.get().createDivElement();

    private PositionCallback callback = new PositionCallback() {

        @Override
        public void setPosition(int offsetWidth, int offsetHeight) {
            Element parentElement = root.getParentElement();
            final int absoluteBottom = parentElement.getAbsoluteBottom();
            final int absoluteRight = parentElement.getAbsoluteRight();
            int left = absoluteRight - offsetWidth;
            if (left < sheet.getAbsoluteLeft()) {
                left = absoluteRight;
            }
            int top = absoluteBottom;
            if ((top + offsetHeight) > sheet.getAbsoluteBottom()) {
                top = parentElement.getAbsoluteTop() - offsetHeight;
            }
            if (top < sheet.getAbsoluteTop()) {
                top = sheet.getAbsoluteTop();
            }
            popup.setPopupPosition(left, top);
        }
    };

    final VOverlay popup;
    private final PopupButtonHeader popupHeader;
    private final VerticalPanel popupLayout;

    private final List<Widget> popupChildrenWidgets = new ArrayList<Widget>();
    DivElement sheet;
    private int col;
    private int row;
    private SheetWidget owner;

    /**
     * default constructor
     */
    public PopupButtonWidget() {
        root.setClassName(BUTTON_CLASSNAME);
        root.setAttribute("role", "button");

        popup = new VOverlay(true, false);
        popup.setStyleName(POPUP_OVERLAY_CLASSNAME);
        popupLayout = new VerticalPanel();
        popupLayout.setStylePrimaryName(POPUP_OVERLAY_LAYOUT_CLASSNAME);
        popupHeader = new PopupButtonHeader();
        popupHeader.setPopup(popup);
        popupLayout.add(popupHeader);
        popup.add(popupLayout);

        setElement(root);

        addClickHandler(this);
    }

    /**
     * @param owner
     * @param sheet
     */
    public void setSheetWidget(SheetWidget owner, DivElement sheet) {
        this.sheet = sheet;
        this.owner = owner;
        popup.setOwner(owner);
        popupHeader.setSheet(owner);
    }

    /**
     * 1-based
     * 
     * @return the col
     */
    public int getCol() {
        return col;
    }

    /**
     * 1-based
     * 
     * @param col
     *            the col to set
     */
    public void setCol(int col) {
        this.col = col;
    }

    /**
     * 1-based
     * 
     * @return the row
     */
    public int getRow() {
        return row;
    }

    /**
     * 1-based
     * 
     * @param row
     *            the row to set
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * 1-based
     * 
     * @param row
     * @param col
     */
    public void setRowCol(int row, int col) {
        Widget owner = popup.getOwner();
        if (owner != null && owner instanceof SheetWidget) {
            ((SheetWidget) owner).updatePopupButtonPosition(this, this.row,
                    this.col, row, col);
        }
        getElement().removeClassName("c" + this.col + "r" + this.row);
        this.col = col;
        this.row = row;
        getElement().addClassName("c" + this.col + "r" + this.row);
    }

    @Override
    public void onClick(ClickEvent event) {
        openPopupOverlay();
        event.stopPropagation();
    }

    /**
     * open popup
     */
    protected void openPopupOverlay() {
        Element parentElement = root.getParentElement();
        if (parentElement != null) {
            popup.setPopupPositionAndShow(callback);
        } else {
            Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                @Override
                public void execute() {
                    popup.setPopupPositionAndShow(callback);
                }
            });
        }
        if (owner != null) {
            popupHeader.setCaption(owner.getCellValue(col, row));
        }
    }

    /**
     * @param active
     */
    public void markActive(boolean active) {
        if (active) {
            root.addClassName(BUTTON_ACTIVE_CLASSNAME);
        } else {
            root.removeClassName(BUTTON_ACTIVE_CLASSNAME);
        }
    }

    /**
     * @param widget
     */
    public void addPopupComponent(Widget widget) {
        popupChildrenWidgets.add(widget);
        popupLayout.add(widget);
    }

    /**
     * @param widget
     */
    public void removePopupComponent(Widget widget) {
        popupChildrenWidgets.remove(widget);
        popupLayout.remove(widget);
    }

    /**
     * @param popupHeight
     */
    public void setPopupHeight(String popupHeight) {
        if (popupHeight != null) {
            ((PopupPanel) popup).setHeight(popupHeight);
        } else {
            ((PopupPanel) popup).setHeight("");
        }
    }

    /**
     * @param popupWidth
     */
    public void setPopupWidth(String popupWidth) {
        if (popupWidth != null) {
            ((PopupPanel) popup).setWidth(popupWidth);
        } else {
            ((PopupPanel) popup).setWidth("");
        }
    }

    /**
     * Override the position callback method for the button's popup.
     * 
     * @param positionCallback
     *            not null
     */
    public void setPopupPositionCallback(PositionCallback positionCallback) {
        if (positionCallback != null) {
            callback = positionCallback;
        }
    }

    /**
     * Returns the position callback method used for the button's popup.
     * 
     * @return callback
     */
    public PositionCallback getPositionCallback() {
        return callback;
    }

    /**
     * @param headerHidden
     */
    public void setPopupHeaderHidden(boolean headerHidden) {
        popupHeader.setHidden(headerHidden);
    }

    /**
     * @return true if popup header is hidden
     */
    public boolean isPopupHeaderHidden() {
        return popupHeader.isHidden();
    }

    @Override
    public void setStyleName(String style, boolean add) {
        super.setStyleName(style, add);
        popup.setStyleName(style, add);
    }

    @Override
    public HandlerRegistration addCloseHandler(CloseHandler<PopupPanel> handler) {
        return popup.addCloseHandler(handler);
    }

    /**
     * @return true if popup is open
     */
    public boolean isPopupOpen() {
        return popup.isShowing();
    }

    /**
     * close the popup
     */
    public void closePopup() {
        popup.hide();
    }

    /**
     * open the popup
     */
    public void openPopup() {
        if (owner != null && owner.isCellRendered(col, row)) {
            openPopupOverlay();
        }
    }

    @Override
    protected void onDetach() {
        closePopup();
        super.onDetach();
    }
}
