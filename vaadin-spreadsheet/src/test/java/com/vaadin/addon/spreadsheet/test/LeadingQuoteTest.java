package com.vaadin.addon.spreadsheet.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.vaadin.addon.spreadsheet.elements.SpreadsheetElement;
import com.vaadin.addon.spreadsheet.test.pageobjects.SpreadsheetPage;
import com.vaadin.addon.spreadsheet.test.testutil.SheetController;

public class LeadingQuoteTest extends AbstractSpreadsheetTestCase {

    private static final String B3 = "B3";
    private static final String B4 = "B4";
    private static final String B5 = "B5";
    private static final String B6 = "B6";
    private static final String C6 = "C6";

    private SheetController sheetController;
    private SpreadsheetPage spreadsheetPage;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        sheetController = new SheetController(driver, testBench(driver),
            getDesiredCapabilities());
        spreadsheetPage = headerPage.loadFile("leading_quotes.xlsx", this);
        waitForElementPresent(By.className("v-spreadsheet"));
    }

    @Test
    public void leadingQuote_exsistingStringRepresentingANumber_formulaBarAndInlineEditorShowALeadingQuote()
        throws Exception {

        // Cell must show the string value WITHOUT the trailing quote
        SpreadsheetElement cell = $(SpreadsheetElement.class).first();
        assertEquals("01", cell.getCellAt(B3).getValue());

        // Formula bar must show the string value WITH the trailing quote
        sheetController.selectCell(B3);
        assertEquals("'01", spreadsheetPage.getFormulaFieldValue());

        // Inline editor must show the string value WITH the trailing quote
        WebElement cellEditor = sheetController.getInlineEditor(B3);
        assertEquals("'01", spreadsheetPage.getFormulaFieldValue());
    }

    @Test
    public void leadingQuote_exsistingStringRepresentingANumberWithDot_formulaBarAndInlineEditorShowALeadingQuote()
        throws Exception {

        // Cell must show the string value WITHOUT the trailing quote
        SpreadsheetElement cell = $(SpreadsheetElement.class).first();
        assertEquals("123.456", cell.getCellAt(B4).getValue());

        // Formula bar must show the string value WITH the trailing quote
        sheetController.selectCell(B4);
        assertEquals("'123.456", spreadsheetPage.getFormulaFieldValue());

        // Inline editor must show the string value WITH the trailing quote
        WebElement cellEditor = sheetController.getInlineEditor(B4);
        assertEquals("'123.456", spreadsheetPage.getFormulaFieldValue());
    }

    @Test
    public void leadingQuote_exsistingStringRepresentingANumberWithComma_formulaBarAndInlineEditorShowALeadingQuote()
        throws Exception {

        // Cell must show the string value WITHOUT the trailing quote
        SpreadsheetElement cell = $(SpreadsheetElement.class).first();
        assertEquals("123,456", cell.getCellAt(B5).getValue());

        // Formula bar must show the string value WITH the trailing quote
        sheetController.selectCell(B5);
        assertEquals("'123,456", spreadsheetPage.getFormulaFieldValue());

        // Inline editor must show the string value WITH the trailing quote
        WebElement cellEditor = sheetController.getInlineEditor(B5);
        assertEquals("'123,456", spreadsheetPage.getFormulaFieldValue());
    }

    @Test
    public void leadingQuote_typeNumberWithLeadingQuote_quoteNotShownInCellAndInPOIModel()
        throws Exception {

        // Typing '12345 in B6
        SpreadsheetElement cell = $(SpreadsheetElement.class).first();
        sheetController.putCellContent(B6, "'12345");

        // Cell must show the string value WITHOUT the trailing quote
        assertEquals("12345", cell.getCellAt(B6).getValue());

        // POI model must be WITHOUT the trailing quote 
        // In cell C6 an excel formula prints the first two characters of B6
        // (that must not be a quote)
        assertEquals("12", cell.getCellAt(C6).getValue());

        // Formula bar must show the string value WITH the trailing quote
        sheetController.selectCell(B6);
        assertEquals("'12345", spreadsheetPage.getFormulaFieldValue());
    }

    @Test
    public void leadingQuote_typeNonNumberWithLeadingQuote_quoteRemovedEveryWhere()
        throws Exception {

        // Typing 'abcd in B6
        SpreadsheetElement cell = $(SpreadsheetElement.class).first();
        sheetController.putCellContent(B6, "'abcd");

        // Cell must show the string value WITHOUT the trailing quote
        assertEquals("abcd", cell.getCellAt(B6).getValue());

        // POI model must be WITHOUT the trailing quote 
        // In cell C6 an excel formula prints the first two characters of B6
        // (that must not be a quote)
        assertEquals("ab", cell.getCellAt(C6).getValue());

        // Formula bar must show the string value WITHOUT the trailing quote
        // (different behaviour than Excel but not that important)
        sheetController.selectCell(B6);
        assertEquals("abcd", spreadsheetPage.getFormulaFieldValue());
    }

    @Test
    public void leadingQuote_typeNumberWithTwoLeadingQuotes_justOneQuoteShownInCellAndInPOIModel()
        throws Exception {

        // Typing ''567 in B6
        SpreadsheetElement cell = $(SpreadsheetElement.class).first();
        sheetController.putCellContent(B6, "''567");

        // Cell must show the string value WITHOUT the first trailing quote
        assertEquals("'567", cell.getCellAt(B6).getValue());

        // POI model must be WITHOUT the first trailing quote 
        // In cell C6 an excel formula prints the first character of B6
        // (that must not be a quote)
        assertEquals("'5", cell.getCellAt(C6).getValue());

        // Formula bar must show the string value WITH two trailing quote
        sheetController.selectCell(B6);
        assertEquals("''567", spreadsheetPage.getFormulaFieldValue());
    }
}
