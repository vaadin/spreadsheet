package com.vaadin.addon.spreadsheet.test.fixtures;

/**
 * EagerFixtureFactory
 */
public class EagerFixtureFactory implements SpreadsheetFixtureFactory {

    private SpreadsheetFixture fixture;

    /**
     * @param fixture
     */
    public EagerFixtureFactory(SpreadsheetFixture fixture) {
        this.fixture = fixture;
    }

    @Override
    public SpreadsheetFixture create() {
        return fixture;
    }
}
