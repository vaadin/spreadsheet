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

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.vaadin.addon.spreadsheet.RowsAutofitUtil;
import com.vaadin.addon.spreadsheet.Spreadsheet;

/**
 * Command for changing the height of row(s).
 *
 * @author Vaadin Ltd.
 * @since 1.0
 */
public class RowSizeChangeCommand extends SizeChangeCommand {

    private boolean[] customHeightRows;

    public RowSizeChangeCommand(Spreadsheet spreadsheet) {
        super(spreadsheet);
    }

    @Override
    public void captureValues(Integer[] indexes) {
        super.captureValues(indexes);

        // Also stores the previous customHeight attribute
        Sheet sheet = spreadsheet.getActiveSheet();
        customHeightRows = new boolean[indexes.length];
        for (int i = 0; i < indexes.length; i++) {
            customHeightRows[i] = RowsAutofitUtil
                .isCustomHeight(sheet, indexes[i] - 1);
        }
    }

    @Override
    public void execute() {
        super.execute();

        // Also update the customHeight attribute
        for (int i = 0; i < indexes.length; i++) {
            customHeightRows[i] = updateCustomHeight(indexes[i] - 1,
                customHeightRows[i]);
        }
    }

    @Override
    protected Object updateValue(int index, Object value) {
        Row row = spreadsheet.getActiveSheet().getRow(index);
        // null rows use default row height
        // null height marks default height
        Object oldHeight = getCurrentValue(index);

        if (value == null && row != null) {
            spreadsheet.setRowHeight(index, spreadsheet.getDefaultRowHeight());
        } else if (value != null) {
            spreadsheet.setRowHeight(index, (Float) value);

        } // if both are null, then default is applied already (shouldn't)
        return oldHeight;
    }

    private boolean updateCustomHeight(int index, boolean customHeight) {
        Sheet sheet = spreadsheet.getActiveSheet();
        boolean oldCustomHeight = RowsAutofitUtil.isCustomHeight(sheet, index);
        RowsAutofitUtil.setCustomHeight(sheet, index, customHeight);
        return oldCustomHeight;
    }

    @Override
    protected Object getCurrentValue(int index) {
        Row row = getSheet().getRow(index);
        // null rows use default row height
        // null height marks default height
        return row == null ?
            null :
            row.getZeroHeight() ? 0.0F : row.getHeightInPoints();
    }
}
