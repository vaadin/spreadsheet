package com.vaadin.addon.spreadsheet.client;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.event.dom.client.ContextMenuEvent;

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

import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.event.dom.client.DragEndEvent;
import com.google.gwt.event.dom.client.DragEnterEvent;
import com.google.gwt.event.dom.client.DragEvent;
import com.google.gwt.event.dom.client.DragLeaveEvent;
import com.google.gwt.event.dom.client.DragOverEvent;
import com.google.gwt.event.dom.client.DragStartEvent;
import com.google.gwt.event.dom.client.GestureChangeEvent;
import com.google.gwt.event.dom.client.GestureEndEvent;
import com.google.gwt.event.dom.client.GestureStartEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.event.dom.client.TouchCancelEvent;
import com.google.gwt.event.dom.client.TouchCancelHandler;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchEndHandler;
import com.google.gwt.event.dom.client.TouchMoveEvent;
import com.google.gwt.event.dom.client.TouchMoveHandler;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.vaadin.client.BrowserInfo;

/**
 * SheetContextHandler
 */
public class SheetContextHandler
        implements ContextMenuHandler, TouchStartHandler, TouchCancelHandler,
        TouchEndHandler, TouchMoveHandler, ScrollHandler {

    private SheetWidget sheetWidget;
    private Timer timer;

    /**
     * @param widget 
     */
    public SheetContextHandler(SheetWidget widget) {
        this.sheetWidget = widget;

        // everything but iOS implements ContextMenuEvent
        sheetWidget.addDomHandler(this,  ContextMenuEvent.getType());
        
        // don't bother registering touch events if not iOS
        if (sheetWidget.isTouchMode() && BrowserInfo.get().isIOS()) {
            sheetWidget.addDomHandler(this, TouchStartEvent.getType());
            sheetWidget.addDomHandler(this, TouchEndEvent.getType());
            sheetWidget.addDomHandler(this, TouchMoveEvent.getType());
            sheetWidget.addDomHandler(this, TouchCancelEvent.getType());
            sheetWidget.addDomHandler(this, ScrollEvent.getType());
        }
    }
    
    /**
     * @return SheetWidget
     */
    public SheetWidget getSheetWidget() {
        return sheetWidget;
    }
    
    /**
     * Cancel any iOS context long-press timer
     * 
     * Needed for canceling from a scroll event,
     * which doesn't bubble due to other handlers.
     */
    public void cancelTimer() {
        if (timer != null) timer.cancel();
    }

    /**
     * @see com.google.gwt.event.dom.client.ScrollHandler#onScroll(com.google.gwt.event.dom.client.ScrollEvent)
     */
    @Override
    public void onScroll(ScrollEvent event) {
        cancelTimer();
    }

    /**
     * @see com.google.gwt.event.dom.client.TouchMoveHandler#onTouchMove(com.google.gwt.event.dom.client.TouchMoveEvent)
     */
    @Override
    public void onTouchMove(TouchMoveEvent event) {
        cancelTimer();
    }

    /**
     * @see com.google.gwt.event.dom.client.TouchEndHandler#onTouchEnd(com.google.gwt.event.dom.client.TouchEndEvent)
     */
    @Override
    public void onTouchEnd(TouchEndEvent event) {
        cancelTimer();
    }

    /**
     * @see com.google.gwt.event.dom.client.TouchCancelHandler#onTouchCancel(com.google.gwt.event.dom.client.TouchCancelEvent)
     */
    @Override
    public void onTouchCancel(TouchCancelEvent event) {
        cancelTimer();
    }

    /**
     * @see com.google.gwt.event.dom.client.TouchStartHandler#onTouchStart(com.google.gwt.event.dom.client.TouchStartEvent)
     */
    @Override
    public void onTouchStart(TouchStartEvent event) {
        cancelTimer();
        
        NativeEvent ne = event.getNativeEvent();
        Element target = ne.getEventTarget().cast();

        if (target == null) target = getSheetWidget().getElement();
        
        final Element finalTarget = target;
        
        JsArray<Touch> targetTouches = ne.getTargetTouches();
        Touch touch = null;
        
        // ignore non-touch and multi-touch
        if (targetTouches == null 
                || targetTouches.length() == 0 
                || targetTouches.length() > 1) return; 

        touch = targetTouches.get(0);
        
        final int screenX = 
                touch == null ? ne.getScreenX() : touch.getScreenX();
        final int screenY = 
                touch == null ? ne.getScreenY() : touch.getScreenY();
        final int clientX = 
                touch == null ? ne.getClientX() : touch.getClientX();
        final int clientY = 
                touch == null ? ne.getClientY() : touch.getClientY();
        final EventTarget relatedEventTarget = 
                touch == null ? ne.getEventTarget() : touch.getTarget();
        final Element relatedTarget = 
                relatedEventTarget == null ? null : Element.as(relatedEventTarget);
        
        timer = new Timer() {
                @Override
                public void run() {
                        NativeEvent evt = Document.get().createMouseEvent(
                                        BrowserEvents.CONTEXTMENU,
                                        true, 
                                        true, 
                                        0, 
                                        screenX, 
                                        screenY, 
                                        clientX, 
                                        clientY, 
                                        false, 
                                        false, 
                                        false, 
                                        false, 
                                        NativeEvent.BUTTON_RIGHT, 
                                        relatedTarget
                                );
                        finalTarget.dispatchEvent(evt);
                }
        };
        
        timer.schedule(750);
    }

    /**
     * @see com.google.gwt.event.dom.client.ContextMenuHandler#onContextMenu(com.google.gwt.event.dom.client.ContextMenuEvent)
     */
    @Override
    public void onContextMenu(ContextMenuEvent event) {
        onContextMenuEvent(Event.as(event.getNativeEvent()));        
    }
    /**
     * Take the event we are given, find the cell coordinates, 
     * and call the context menu handler.
     * 
     * Some code duplicated from {@link SheetWidget#onSheetMouseDown(Event)}
     * @param event
     */
    public void onContextMenuEvent(Event event) {
        if (! getSheetWidget().getSheetHandler().hasCustomContextMenu()) {
            return;
        }
        
        Element target = event.getEventTarget().cast();

        String className = target.getAttribute("class");

        // click target is the inner div because IE10 and 9 are not compatible
        // with 'pointer-events: none'
        if ((BrowserInfo.get().isIE9() || BrowserInfo.get().isIE10())
                && (className == null || className.isEmpty())) {
            String parentClassName = target.getParentElement().getAttribute(
                    "class");
            if (parentClassName.contains("cell")) {
                className = parentClassName;
            }
        }

        SheetJsniUtil jsniUtil = getSheetWidget().getSheetJsniUtil();

        int i = jsniUtil.isHeader(className);
        if (i == 1 || i == 2) {
            int index = jsniUtil.parseHeaderIndex(className);
            if (i == 1) {
                getSheetWidget().actionHandler.onRowHeaderRightClick(
                        event, index);
            } else {
                getSheetWidget().actionHandler.onColumnHeaderRightClick(
                        event, index);
            }
            event.stopPropagation();
            event.preventDefault();
        } else {
            
            if (className.contains("sheet") || target.getTagName().equals("input")
                    || className.equals("floater")) {
                return; // event target is one of the panes or input
            }
            
            if (getSheetWidget().isEventInCustomEditorCell(event)) {
                // allow sheet context menu on top of custom editors
                getSheetWidget().getSheetHandler().onCellRightClick(event, 
                        getSheetWidget().getSelectedCellColumn(),
                        getSheetWidget().getSelectedCellRow());
            } else if (className.contains("cell")) {
                if (className.equals("cell-comment-triangle")) {
                    getSheetWidget().jsniUtil.parseColRow(
                            target.getParentElement().getAttribute(
                                    "class"));
                } else {
                    getSheetWidget().jsniUtil.parseColRow(className);
                }
                int targetCol = getSheetWidget().jsniUtil.getParsedCol();
                int targetRow = getSheetWidget().jsniUtil.getParsedRow();
                
                event.stopPropagation();
                event.preventDefault();
                getSheetWidget().getSheetHandler()
                .onCellRightClick(event, targetCol, targetRow);
            }
        }
    }
}
