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
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * Class that represents a single column grouping indicator.
 *
 * @author Thomas Mattsson / Vaadin LTD
 *
 */
public abstract class GroupingWidget extends FlowPanel {

    /**
     * SINGLE_ITEM_SIZE_HEIGHT
     */
    protected static final int SINGLE_ITEM_SIZE_HEIGHT = 18;
    /**
     * SINGLE_ITEM_SIZE_WIDTH
     */
    protected static final int SINGLE_ITEM_SIZE_WIDTH = 15;
    /**
     * EXPAND_CHAR
     */
    public static final String EXPAND_CHAR = "+";
    /**
     * CONTRACT_SIGN
     */
    public static final String CONTRACT_SIGN = "&#x2212;";

    /**
     * GroupingHandler
     */
    public interface GroupingHandler {
        /**
         * setGroupingCollapsed
         * @param cols
         * @param colIndex
         * @param collapsed
         */
        void setGroupingCollapsed(boolean cols, int colIndex, boolean collapsed);

        /**
         * levelHeaderClicked
         * @param cols
         * @param level
         */
        void levelHeaderClicked(boolean cols, int level);
    }

    private DivElement btn = Document.get().createDivElement();

    private boolean collapsed = false;
    private boolean inversed = false;

    private int index;

    /**
     * handler
     */
    protected GroupingHandler handler;

    /**
     * top
     */
    protected int top = -1;
    /**
     * left
     */
    protected int left = -1;
    /**
     * marginLeft
     */
    protected double marginLeft = -1;
    /**
     * marginTop
     */
    protected double marginTop = -1;
    /**
     * width
     */
    protected double width = -1;
    /**
     * height
     */
    protected double height = -1;

    /**
     * @param index
     *            Unique index for the group, 0-based. This index is used on
     *            server side when collapsing/expanding the group.
     * @param handler
     *            The gateway to the server side
     */
    public GroupingWidget(final int index, final GroupingHandler handler) {

        this.index = index;
        this.handler = handler;

        setStyleName("grouping");
        addStyleName("minus");

        btn.setInnerHTML(CONTRACT_SIGN);
        btn.setClassName("expand");
        getElement().appendChild(btn);

        Event.sinkEvents(getElement(), Event.ONCLICK | Event.ONCONTEXTMENU);
    }

    /**
     * setWidthPX
     * @param w
     */
    public void setWidthPX(double w) {
        if (isCollapsed()) {
            setSize(0);
            if (!isInversed()) {
                setMargin(w);
            }
        } else {
            setSize(w);
        }
    }

    /**
     * setSize
     * @param size
     */
    protected abstract void setSize(double size);

    /**
     * setMargin
     * @param size
     */
    protected abstract void setMargin(double size);

    /**
     * setCollapsed
     * @param collapsed
     */
    public void setCollapsed(boolean collapsed) {

        if (this.collapsed == collapsed) {
            return;
        }

        if (collapsed) {
            btn.setInnerHTML(EXPAND_CHAR);
            removeStyleName("minus");
            addStyleName("plus");
        } else {
            btn.setInnerHTML(CONTRACT_SIGN);
            removeStyleName("plus");
            addStyleName("minus");
        }
        this.collapsed = collapsed;
    }

    /**
     * isCollapsed
     * @return boolean
     */
    public boolean isCollapsed() {
        return collapsed;
    }

    @Override
    public void onBrowserEvent(Event event) {

        event.preventDefault();
        event.stopPropagation();

        if (event.getButton() == NativeEvent.BUTTON_LEFT) {
            handler.setGroupingCollapsed(isCols(), index, !collapsed);
            setCollapsed(!collapsed);
        }
    }

    /**
     * isCols
     * @return boolean
     */
    protected abstract boolean isCols();

    /**
     * Where this marker should be positioned.
     *
     * @param offset
     *            The number of pixels from 0 (top or left) this group should be
     *            positioned.
     * @param level
     *            The level of the group, 0-based.
     */
    public abstract void setPos(int offset, int level);

    /**
     * @return The total height of a panel with the given amount of groups
     */
    /**
     * getTotalHeight
     * @param maxGrouping
     * @return int
     */
    public static int getTotalHeight(int maxGrouping) {
        return 3 + maxGrouping * SINGLE_ITEM_SIZE_HEIGHT;
    }

    /**
     * @return The total width of a panel with the given amount of groups
     */
    /**
     * getTotalWidth
     * @param maxGrouping
     * @return int
     */
    public static int getTotalWidth(int maxGrouping) {
        return 1 + maxGrouping * SINGLE_ITEM_SIZE_WIDTH;
    }

    /**
     * setIndex
     * @param i
     */
    public void setIndex(int i) {
        index = i;
    }

    /**
     * getIndex
     * @return int
     */
    public int getIndex() {
        return index;
    }

    /**
     * isInversed
     * @return boolean
     */
    public boolean isInversed() {
        return inversed;
    }

    /**
     * setInversed
     * @param inversed
     */
    public void setInversed(boolean inversed) {
        this.inversed = inversed;
        if (inversed) {
            addStyleName("inversed");
        } else {
            removeStyleName("inversed");
        }
    }

    /**
     * cloneWidget
     * @return GroupingWidget
     */
    protected abstract GroupingWidget cloneWidget();

    /**
     * copy fields
     * @param newWidget
     */
    protected void copyfields(GroupingWidget newWidget) {
        newWidget.collapsed = collapsed;
        newWidget.index = index;
        newWidget.inversed = inversed;

        newWidget.btn.setInnerText(btn.getInnerText());

        Style style = newWidget.getElement().getStyle();

        newWidget.setStyleName(getStyleName());

        if (marginLeft > -1) {
            style.setMarginLeft(marginLeft, Unit.PX);
        }
        if (marginTop > -1) {
            style.setMarginTop(marginTop, Unit.PX);
        }
        if (height > -1) {
            style.setHeight(height, Unit.PX);
        }
        if (width > -1) {
            style.setWidth(width, Unit.PX);
        }
        if (top > -1) {
            style.setTop(top, Unit.PX);
        }
        if (left > -1) {
            style.setLeft(left, Unit.PX);
        }
    }

}
