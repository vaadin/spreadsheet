package com.vaadin.addon.spreadsheet.test;

/**
 * SheetClicker
 */
public interface SheetClicker {

    /**
     * clickCell
     * @param cell
     */
    public void clickCell(String cell);

    /**
     * clickRow
     * @param row
     */
    public void clickRow(int row);

    /**
     * clickColumn
     * @param column
     */
    public void clickColumn(String column);
}
