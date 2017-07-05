package com.vaadin.addon.spreadsheet.test.fixtures;

/**
 * Test fixtures for server-side Spreadsheet manipulation
 *
 */
public enum TestFixtures {
    FirstColumnWidth(FirstColumnWidthFixture.class), PopupButton(
            PopupButtonFixture.class), TabsheetPopupButton(
            TabsheetPopupButtonFixture.class), TablePopupButton(
            TablePopupButtonFixture.class), SpreadsheetTable(
            SpreadsheetTableFixture.class), Comments(CommentFixture.class), AddOrRemoveComment(
            AddOrRemoveCommentFixture.class), Formats(FormatsFixture.class), DisableChartOverlays(
            DisableChartsFixture.class), StyleMergeReigions(
            StyleMergeReigions.class), RemoveFixture(RemoveFixture.class), DefaultStyleUnlocked(
            DefaultStyleUnlockedFixture.class), HideSecondRow(
            HideSecondRowFixture.class), LargeSpreadsheet(
            LargeSpreadsheetFixture.class);

    public final SpreadsheetFixtureFactory factory;

    TestFixtures(SpreadsheetFixtureFactory factory) {
        this.factory = factory;
    }

    TestFixtures(Class<? extends SpreadsheetFixture> fixtureClass) {
        this(new ClassFixtureFactory(fixtureClass));
    }
}
