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
package com.vaadin.addon.spreadsheet.test.fixtures;

public class EagerFixtureFactory implements SpreadsheetFixtureFactory {

    private SpreadsheetFixture fixture;

    public EagerFixtureFactory(SpreadsheetFixture fixture) {
        this.fixture = fixture;
    }

    @Override
    public SpreadsheetFixture create() {
        return fixture;
    }
}
