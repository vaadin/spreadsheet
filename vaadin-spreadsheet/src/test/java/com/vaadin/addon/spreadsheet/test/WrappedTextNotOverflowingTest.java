package com.vaadin.addon.spreadsheet.test;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertThat;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import com.vaadin.addon.spreadsheet.elements.SheetCellElement;
import com.vaadin.addon.spreadsheet.test.pageobjects.SpreadsheetPage;
import com.vaadin.testbench.annotations.RunLocally;
import com.vaadin.testbench.parallel.Browser;

/**
 * Text should not overflow when word-wrapped
 */
@RunLocally(Browser.PHANTOMJS)
public class WrappedTextNotOverflowingTest extends AbstractSpreadsheetTestCase {

    private SpreadsheetPage spreadsheetPage;

    private static void assertNoOverflow(SheetCellElement cell) {
        WebElement element = cell.getWrappedElement();
        try {
            WebElement subDiv = element.findElement(By.tagName("div"));

            // SubDiv has been found: if it's wider than cell width then text overflows
            assertThat(subDiv.getSize().getWidth(),
                lessThanOrEqualTo(cell.getSize().getWidth()));

        } catch (NoSuchElementException e) {
            // No Sub-DIVs nested into cell are found so text will fit
            // the cell div and cannot overflow 
        }
    }

    private static void assertOverflows(SheetCellElement cell) {
        WebElement element = cell.getWrappedElement();

        try {
            WebElement subDiv = element.findElement(By.tagName("div"));

            // SubDiv has been found: if it's wider than cell width then text overflows
            assertThat("Text div was expected to be wider than cell div",
                subDiv.getSize().getWidth(),
                greaterThanOrEqualTo(cell.getSize().getWidth()));

        } catch (NoSuchElementException e) {
            Assert.fail(
                "A sub-DIV with overflowing text was expected inside the cell DIV");
        }
    }

    @Test
    public void sheetWithOverflowingText_wordWrapOnFirstColumn_textShouldNotOverflow()
        throws Exception {
        loadFile();

        // Cell A2 contains wrapped long text
        // For some reason it works fine even if bug is not fixed
        SheetCellElement cell = spreadsheetPage.getCellAt(1, 2);

        assertNoOverflow(cell);
    }

    @Test
    public void sheetWithOverflowingText_wordWrapOnThirdColumn_textShouldNotOverflow()
        throws Exception {
        loadFile();

        // Cell C2 contains wrapped long text 
        // For some reason it works, the bug shows up only if there is another
        // wrapped cell on the left (A2)
        SheetCellElement cell = spreadsheetPage.getCellAt(3, 2);

        assertNoOverflow(cell);
    }

    @Test
    public void sheetWithOverflowingText_noWordWrapAndLongText_textShouldOverflow()
        throws Exception {
        loadFile();

        // Cell A5 contains unwrapped long text 
        SheetCellElement cell = spreadsheetPage.getCellAt(1, 5);

        assertOverflows(cell);
    }

    @Test
    public void sheetWithOverflowingText_mergedCellsWordWrapAndLongText_textShouldNotOverflowTheMergedCell()
        throws Exception {
        loadFile();

        // Cell G2 contains unwrapped long text and is merged with H2 
        SheetCellElement cell = spreadsheetPage.getCellAt(7, 2);

        assertNoOverflow(cell);
    }

    private void loadFile() {
        spreadsheetPage = headerPage
            .loadFile("wrapped_text_not_overflowing.xlsx", this);
        spreadsheetPage.selectSheetAt(0);
    }
}
