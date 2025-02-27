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
package com.vaadin.addon.spreadsheet.test.junit;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.vaadin.server.ConnectorIdGenerator;
import com.vaadin.server.DefaultDeploymentConfiguration;
import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.RequestHandler;
import com.vaadin.server.ServiceException;
import com.vaadin.server.ServiceInitEvent;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletService;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;

/**
 * UI with a VaadinSession which is always locked.
 * <p>
 * Useful for writing unit tests where you attach components to the UI and need
 * the whole chain with connector ids, etc to work.
 */

public class TestableUI extends UI {

    private DeploymentConfiguration deploymentConfiguration;
    private VaadinServlet servlet;
    private VaadinService service;

    public TestableUI(Component content) {
        servlet = new VaadinServlet();
        deploymentConfiguration = new DefaultDeploymentConfiguration(
                TestableUI.class, new Properties());
        try {
            service = new MockServletService(servlet, deploymentConfiguration);
        } catch (ServiceException e) {
            throw new RuntimeException("Failed to create service", e);
        }
        setSession(new VaadinSession(service) {
            @Override
            public boolean hasLock() {
                return true;
            }
        });

        setContent(content);
    }

    @Override
    protected void init(VaadinRequest request) {

    }

    class MockServletService extends VaadinServletService {
        public MockServletService(VaadinServlet servlet,
                DeploymentConfiguration deploymentConfiguration)
                throws ServiceException {
            super(servlet, deploymentConfiguration);
            ServiceInitEvent event = new ServiceInitEvent(this);
            event.addConnectorIdGenerator(connectorIdGenerationEvent -> {
                return ConnectorIdGenerator
                        .generateDefaultConnectorId(connectorIdGenerationEvent);
            });
            init();
        }

        @Override
        protected List<RequestHandler> createRequestHandlers()
                throws ServiceException {
            return new ArrayList<>();
        }
    }
}
