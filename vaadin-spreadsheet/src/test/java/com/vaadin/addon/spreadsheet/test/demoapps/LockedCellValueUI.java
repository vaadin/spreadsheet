/*
 * Vaadin Spreadsheet Addon
 *
 * Copyright (C) 2013-2025 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.addon.spreadsheet.test.demoapps;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

import com.vaadin.addon.spreadsheet.Spreadsheet;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("demo")
@Widgetset("com.vaadin.addon.spreadsheet.Widgetset")
public class LockedCellValueUI extends UI {

    private Spreadsheet spreadsheet = null;

    @Override
    protected void init(VaadinRequest request) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setWidth("1000px");
        setContent(layout);

        Label label = new Label("Protected sheet. Without toggling (button), "
                + "it shouldn't be possible to write or copy-paste anything "
                + "anywhere. After toggling, it should be possible to write "
                + "and paste things into B2 and E5 only. Note that B2, C3, "
                + "and E5 (null) are the only explicitly initialized cells. "
                + "Manual testing should include other cells as well, such as "
                + "A1, D4, and F6. Before the #448 fix it was possible to "
                + "paste but not write to a cell on a row that had no "
                + "existing initialized cells before the attempt.");
        label.setWidthFull();
        label.setHeightUndefined();
        layout.addComponent(label);

        spreadsheet = new Spreadsheet();

        // 0-based index
        Cell cellB2 = spreadsheet.createCell(1, 1, "B2 value");
        spreadsheet.createCell(2, 2, "C3 value");
        Cell cellE5 = spreadsheet.createCell(4, 4, null);
        spreadsheet.setActiveSheetIndex(0);
        spreadsheet.setActiveSheetProtected("protected");
        spreadsheet.reload();

        Workbook workbook = spreadsheet.getWorkbook();
        CellStyle unLockedCellStyle = workbook.createCellStyle();
        unLockedCellStyle.setLocked(false);
        CellStyle lockedCellStyle = workbook.createCellStyle();
        lockedCellStyle.setLocked(true);

        Button setProtected = new Button("Toggle B2 and E5 protection",
                event -> {
                    if (unLockedCellStyle.equals(cellB2.getCellStyle())) {
                        cellB2.setCellStyle(lockedCellStyle);
                        cellE5.setCellStyle(lockedCellStyle);
                    } else {
                        cellB2.setCellStyle(unLockedCellStyle);
                        cellE5.setCellStyle(unLockedCellStyle);
                    }
                    spreadsheet.reload();
                });

        layout.addComponent(spreadsheet);
        layout.addComponent(setProtected);
    }
}
