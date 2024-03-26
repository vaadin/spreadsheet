/*
 * Vaadin Spreadsheet Addon
 *
 * Copyright (C) 2012-2024 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.addon.spreadsheet.test.fixtures;

import com.vaadin.addon.spreadsheet.PopupButton;
import com.vaadin.addon.spreadsheet.Spreadsheet;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;

@SuppressWarnings("serial")
public class TabsheetPopupButtonFixture implements SpreadsheetFixture {

    @Override
    public void loadFixture(final Spreadsheet spreadsheet) {
        spreadsheet
                .addSelectionChangeListener(new Spreadsheet.SelectionChangeListener() {
                    @Override
                    public void onSelectionChange(
                            Spreadsheet.SelectionChangeEvent selectionChangeEvent) {

                        PopupButton popupButton = new PopupButton();
                        TabSheet tabSheet = new TabSheet();
                        tabSheet.setId("tabsheet");
                        tabSheet.setHeight("150px");
                        tabSheet.setWidth("200px");
                        popupButton.setContent(tabSheet);
                        TabSheet.Tab tab1 = tabSheet.addTab(new Label("A"));
                        tab1.setCaption("TAB1");
                        TabSheet.Tab tab2 = tabSheet.addTab(new Label("B"));
                        tab2.setCaption("TAB2");
                        spreadsheet.setPopup(
                                spreadsheet.getSelectedCellReference(),
                                popupButton);
                    }
                });
    }
}
