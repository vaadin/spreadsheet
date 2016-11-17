package com.vaadin.addon.spreadsheet.test.junit;

import java.util.Properties;

import com.vaadin.server.DefaultDeploymentConfiguration;
import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletService;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;

public class TestableUI extends UI {

    private DeploymentConfiguration deploymentConfiguration;
    private VaadinServlet servlet;
    private VaadinService service;

    public TestableUI(Component content) {
        servlet = new VaadinServlet();
        deploymentConfiguration = new DefaultDeploymentConfiguration(
                TestableUI.class, new Properties());
        try {
            service = new VaadinServletService(servlet,
                    deploymentConfiguration);
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
}