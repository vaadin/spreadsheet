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

import org.apache.poi.hssf.converter.ExcelToHtmlUtils;

import com.vaadin.addon.spreadsheet.Spreadsheet;

/**
 * Command for changing the width of column(s).
 *
 * @author Vaadin Ltd.
 * @since 1.0
 */
public class ColumnSizeChangeCommand extends SizeChangeCommand {
    public ColumnSizeChangeCommand(Spreadsheet spreadsheet) {
        super(spreadsheet, Type.COLUMN);
    }

    @Override
    protected Object updateValue(int index, Object value) {
        Object columnWidth = getCurrentValue(index);
        spreadsheet.setColumnWidth(index, (Integer) value);
        return columnWidth;
    }

    @Override
    protected Object getCurrentValue(int index) {
        if (getSheet().isColumnHidden(index)) {
            return 0;
        }
        return ExcelToHtmlUtils
            .getColumnWidthInPx(getSheet().getColumnWidth(index));
    }
}
