package com.vaadin.flow.component.spreadsheet.rpc;

/*
 * #%L
 * Vaadin Spreadsheet
 * %%
 * Copyright (C) 2013 - 2022 Vaadin Ltd
 * %%
 * This program is available under Commercial Vaadin Developer License
 * 4.0 (CVDLv4).
 * 
 * You should have received a copy of the CVALv3 along with this program.
 * If not, see <https://vaadin.com/license/cvdl-4.0>.
 * #L%
 */

public interface PopupButtonClientRpc {

    /**
     * Opens the popup if the button is rendered.
     */
    public void openPopup();

    /**
     * Closes the popup if it is open.
     */
    public void closePopup();
}
