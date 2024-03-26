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
package com.vaadin.addon.spreadsheet.client;

import com.vaadin.shared.communication.ServerRpc;

public interface PopupButtonServerRpc extends ServerRpc {

    /**
     * Called when the button has been clicked, and the pop-up has been opened.
     */
    public void onPopupButtonClick();

    /**
     * Called after the pop-up has been closed.
     */
    public void onPopupClose();
}
