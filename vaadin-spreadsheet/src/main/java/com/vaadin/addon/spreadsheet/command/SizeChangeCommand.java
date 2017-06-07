package com.vaadin.addon.spreadsheet.command;

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

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;

import com.vaadin.addon.spreadsheet.Spreadsheet;

/**
 * Abstract class for changing the height of row(s) or the width of column(s).
 * 
 * @author Vaadin Ltd.
 * @since 1.0
 */
abstract class SizeChangeCommand extends SpreadsheetCommand {

    private Object[] values;
    protected Integer[] indexes;

    SizeChangeCommand(Spreadsheet spreadsheet) {
        super(spreadsheet);
    }

    /**
     * Captures the current row heights or column widths (depending on the type
     * set to this command) for the row/column indexes given.
     *
     * @param indexes
     *            Row /column indexes, 1-based
     */
    public void captureValues(Integer[] indexes) {
        this.indexes = indexes;
        values = new Object[indexes.length];
        for (int i = 0; i < indexes.length; i++) {
            values[i] = getCurrentValue(indexes[i] - 1);
        }
    }

    @Override
    public void execute() {
        for (int i = 0; i < indexes.length; i++) {
            values[i] = updateValue(indexes[i] - 1, values[i]);
        }
    }

    @Override
    public CellReference getSelectedCellReference() {
        return null;
    }

    @Override
    public CellRangeAddress getPaintedCellRange() {
        return null;
    }

    /**
     * Sets the height/width of the target row/column (found by the given index)
     * to the given value.
     *
     * @param index
     *            row/column index, 0-based
     * @param value
     *            new height/width
     * @return Previous height/width of the row/column
     */
    protected abstract Object updateValue(int index, Object value);

    /**
     * Returns the current height/width of the target row/column.
     *
     * @param index
     *            row/column index, 0-based
     * @return current height for row OR width for column
     */
    protected abstract Object getCurrentValue(int index);

}
