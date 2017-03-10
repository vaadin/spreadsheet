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

import com.vaadin.addon.spreadsheet.Spreadsheet;

/**
 * Command for changing the height of row(s).
 *
 * @author Vaadin Ltd.
 * @since 1.0
 */
public class RowSizeChangeCommand extends SizeChangeCommand {
    public RowSizeChangeCommand(Spreadsheet spreadsheet) {
        super(spreadsheet, Type.ROW);
    }
}
