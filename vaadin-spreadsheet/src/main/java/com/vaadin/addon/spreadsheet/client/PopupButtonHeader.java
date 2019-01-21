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
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ui.VWindow;

/**
 * PopupButtonHeader
 */
public class PopupButtonHeader extends Widget {

    /**
     * CLASSNAME
     */
    protected final static String CLASSNAME = PopupButtonWidget.POPUP_OVERLAY_CLASSNAME
            + "-header";

    private DivElement root = Document.get().createDivElement();
    private DivElement close = Document.get().createDivElement();
    private DivElement caption = Document.get().createDivElement();
    private PopupPanel popup;
    private SheetWidget sheetWidget;

    /**
     * constructor
     */
    public PopupButtonHeader() {
        root.setClassName(CLASSNAME);
        close.setClassName(VWindow.CLASSNAME + "-closebox");
        close.setAttribute("role", "button");
        caption.setClassName("header-caption");
        root.appendChild(close);
        root.appendChild(caption);
        Event.sinkEvents(close, Event.ONCLICK);
        Event.setEventListener(close, this);

        setElement(root);
    }

    /**
     * setCaption
     * @param caption
     */
    public void setCaption(String caption) {
        this.caption.setInnerText(caption);
    }

    /**
     * setPopup
     * @param popup
     */
    public void setPopup(PopupPanel popup) {
        this.popup = popup;
    }

    /**
     * setSheet
     * @param sheetWidget
     */
    public void setSheet(SheetWidget sheetWidget) {
        this.sheetWidget = sheetWidget;
    }

    @Override
    public void onBrowserEvent(Event event) {
        if (event.getEventTarget().equals(close)) {
            popup.hide();
            sheetWidget.focusSheet();
        } else {
            super.onBrowserEvent(event);
        }
    }

    /**
     * setHidden
     * @param headerHidden
     */
    public void setHidden(boolean headerHidden) {
        getElement().getStyle().setDisplay(
                headerHidden ? Display.NONE : Display.BLOCK);
    }

    /**
     * isHidden
     * @return boolean
     */
    public boolean isHidden() {
        return Display.NONE.getCssName().equals(
                getElement().getStyle().getDisplay());
    }
}
