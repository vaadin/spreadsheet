/*
 * Vaadin Spreadsheet Addon
 *
 * Copyright (C) 2013-2024 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.addon.spreadsheet.test.demoapps;

import com.vaadin.addon.spreadsheet.Spreadsheet;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Viewport;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

@Viewport("width=device-width, initial-scale=1")
@Theme("demo")
@Widgetset("com.vaadin.addon.spreadsheet.Widgetset")
public class TouchUI extends UI {

    @Override
    protected void init(VaadinRequest request) {
        Spreadsheet ss = new Spreadsheet();
        ss.reset();

        setContent(ss);
        ss.setSizeFull();
        setSizeFull();
    }

}
