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
package com.vaadin.addon.spreadsheet.test.demoapps;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

import com.vaadin.server.VaadinServlet;

@WebServlet(value = "/*", asyncSupported = true, initParams = {
        @WebInitParam(name = "heartbeatInterval", value = "10"),
        @WebInitParam(name = "UIProvider", value = "com.vaadin.addon.spreadsheet.test.demoapps.TestUIProviderImpl") })
public class TestServlet extends VaadinServlet {
}
