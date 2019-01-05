package com.vaadin.addon.spreadsheet.test.fixtures;

/**
 * Test fixtures for server-side Spreadsheet manipulation
 *
 */
public enum TestFixtures {
    /**
     * FirstColumnWidth
     */
	FirstColumnWidth(FirstColumnWidthFixture.class), 
	/**
	 * PopupButton
	 */
	PopupButton(PopupButtonFixture.class), 
	/**
	 * TabsheetPopupButton
	 */
	TabsheetPopupButton(TabsheetPopupButtonFixture.class), 
	/**
	 * TablePopupButton
	 */
	TablePopupButton(TablePopupButtonFixture.class), 
	/**
	 * SpreadsheetTable
	 */
	SpreadsheetTable(SpreadsheetTableFixture.class), 
	/**
	 * Comments
	 */
	Comments(CommentFixture.class), 
	/**
	 * AddOrRemoveComment
	 */
	AddOrRemoveComment(AddOrRemoveCommentFixture.class), 
	/**
	 * Formats
	 */
	Formats(FormatsFixture.class), 
	/**
	 * DisableChartOverlays
	 */
	DisableChartOverlays(DisableChartsFixture.class), 
	/**
	 * StyleMergeReigions
	 */
	StyleMergeReigions(StyleMergeReigions.class), 
	/**
	 * RemoveFixture
	 */
	RemoveFixture(RemoveFixture.class), 
	/**
	 * DefaultStyleUnlocked
	 */
	DefaultStyleUnlocked(DefaultStyleUnlockedFixture.class), 
	/**
	 * HideSecondRow
	 */
	HideSecondRow(HideSecondRowFixture.class),
	/**
	 * LargeSpreadsheet
	 */
	LargeSpreadsheet(LargeSpreadsheetFixture.class),
    /**
     * ColumnToggle
     */
    ColumnToggle(ColumnToggleFixture.class),
    /**
     * RowToggle
     */
    RowToggle(RowToggleFixture.class),
    /**
     * DeletionHandler
     */
    DeletionHandler(DeletionHandlerFixture.class),
    /**
     * Selection
     */
    Selection(SelectionFixture.class),
    /**
     * MergeCells
     */
    MergeCells(CellMergeFixture.class),
    /**
     * ValueChangeHandler
     */
    ValueChangeHandler(ValueHandlerFixture.class),
    /**
     * Rename
     */
    Rename(RenameFixture.class),
    /**
     * CreateSheet
     */
    CreateSheet(SheetsFixture.class),
    /**
     * CustomEditor
     */
    CustomEditor(SimpleCustomEditorFixture.class),
    /**
     * Styles
     */
    Styles(StylesFixture.class),
    /**
     * LockCell
     */
    LockCell(LockCellFixture.class),
    /**
     * CustomComponent
     */
    CustomComponent(CustomComponentFixture.class),
    /**
     * Action
     */
    Action(ActionFixture.class),
    /**
     * InsertRow
     */
    InsertRow(InsertRowFixture.class),
    /**
     * DeleteRow
     */
    DeleteRow(DeleteRowFixture.class), 
    /**
     * RowHeaderDoubleClick
     */
    RowHeaderDoubleClick(RowHeaderDoubleClickFixture.class)
    ;

    /**
     * factory
     */
    public final SpreadsheetFixtureFactory factory;

    TestFixtures(SpreadsheetFixtureFactory factory) {
        this.factory = factory;
    }

    TestFixtures(Class<? extends SpreadsheetFixture> fixtureClass) {
        this(new ClassFixtureFactory(fixtureClass));
    }
}
