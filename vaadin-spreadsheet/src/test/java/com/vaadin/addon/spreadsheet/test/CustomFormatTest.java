package com.vaadin.addon.spreadsheet.test;

import static org.junit.Assert.assertEquals;
import java.util.Locale;

import org.junit.Test;

import com.vaadin.addon.spreadsheet.elements.SheetCellElement;
import com.vaadin.addon.spreadsheet.elements.SpreadsheetElement;
import com.vaadin.testbench.annotations.RunLocally;
import com.vaadin.testbench.parallel.Browser;

@RunLocally(Browser.PHANTOMJS)
public class CustomFormatTest extends AbstractSpreadsheetTestCase {

    private SpreadsheetElement spreadSheet;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        headerPage.loadFile("custom_format.xlsx", this);

        spreadSheet = $(SpreadsheetElement.class).first();
    }

    @Test
    public void customFormatCellWithFourParts_enterPositiveNumber_becomesNumberType()
            throws Exception {
        // numbers are right-aligned
        assertAlignment("right", "C1");
        assertCellFormat("C1", "5", "5.0", Locale.US);
    }

    @Test
    public void customFormatCellWithFourParts_enterNegativeNumber_getsFormatted()
            throws Exception {
        // drops minus and gains parentheses
        assertCellFormat("C1", "-5", "(5.0)", Locale.US);
    }

    @Test
    public void customFormatCellWithFourParts_enterZero_getsFormatted() throws Exception {
        // changes into minus, Excel also adds space on both sides and fills the
        // left side with whitespace
        assertCellFormat("C1", "0", "-", Locale.US);
    }

    @Test
    public void customFormatCellWithFourParts_enterText_noChange() throws Exception {
        //adds a double quote on text values
        assertCellFormat("C4", "text", "\"text\"", Locale.US);
    }

    @Test
    public void customFormatCellWithFourParts_enterNumberWithITlocale_getsFormatted()
        throws Exception {
        //number formatted in European format
        assertCellFormat("C1", "=5555555.5", "5.555.555,5", Locale.ITALY);
    }

    @Test
    public void customFormatCellWithThreeParts_enterPositiveNumber_becomesNumberType()
        throws Exception {
        // numbers are right-aligned
        assertAlignment("right", "C6");
        assertCellFormat("C6", "5", "5.0", Locale.US);
    }


    @Test
    public void customFormatCellWithThreeParts_enterNegativeNumber_getsFormatted()
        throws Exception {
        // drops minus and gains parentheses
        assertCellFormat("C6","-5","(5.0)",Locale.US);
    }

    @Test
    public void customFormatCellWithThreeParts_enterZero_getsFormatted() throws Exception {

        // changes into minus, Excel also adds space on both sides and fills the
        // left side with whitespace
        assertCellFormat("C6","0","-",Locale.US);
    }

    @Test
    public void customFormatCellWithThreeParts_enterText_noChange() throws Exception {

        assertCellFormat("C6","text","text",Locale.US);
    }

    @Test
    public void customFormatCellWithThreeParts_enterNumberWithITlocale_getsFormatted()
        throws Exception {
        assertCellFormat("C6","=5555555.5","5.555.555,5",Locale.ITALY);
    }

    @Test
    public void customFormatCellWithTwoParts_enterPositiveNumber_becomesNumberType()
        throws Exception {
        // numbers are right-aligned
        assertAlignment("right", "C11");
        assertCellFormat("C11", "5", "5.0", Locale.US);
    }


    @Test
    public void customFormatCellWithTwoParts_enterNegativeNumber_getsFormatted()
        throws Exception {
        // drops minus and gains parentheses
        assertCellFormat("C11","-5","(5.0)",Locale.US);
    }

    @Test
    public void customFormatCellWithTwoParts_enterZero_getsFormatted() throws Exception {
        assertCellFormat("C11","0","0.0",Locale.US);
    }

    @Test
    public void customFormatCellWithTwoParts_enterText_noChange() throws Exception {

        assertCellFormat("C11","text","text",Locale.US);
    }

    @Test
    public void customFormatCellWithTwoParts_enterNumberWithITlocale_getsFormatted()
        throws Exception {
        assertCellFormat("C11","=5555555.5","5.555.555,5",Locale.ITALY);
    }


    @Test
    public void customFormatCellWithOnePart_enterPositiveNumber_becomesNumberType()
        throws Exception {
        // numbers are right-aligned
        assertAlignment("right", "C11");
        assertCellFormat("C16", "5", "5.0", Locale.US);
    }


    @Test
    public void customFormatCellWithOnePart_enterNegativeNumber_getsFormatted()
        throws Exception {
        assertCellFormat("C16","-5","-5.0",Locale.US);
    }

    @Test
    public void customFormatCellWithOnePart_enterZero_getsFormatted() throws Exception {
        assertCellFormat("C16","0","0.0",Locale.US);
    }

    @Test
    public void customFormatCellWithOnePart_enterText_noChange() throws Exception {

        assertCellFormat("C16","text","text",Locale.US);
    }

    @Test
    public void customFormatCellWithOnePart_enterNumberWithITlocale_getsFormatted()
        throws Exception {
        assertCellFormat("C16","=5555555.5","5.555.555,5",Locale.ITALY);
    }

    private void assertCellFormat(String cellID, String value,
        String expected,Locale locale) {
        SheetCellElement formatCell = spreadSheet.getCellAt(cellID);
        formatCell.setValue(value);
        setLocale(locale);
        assertEquals(expected, formatCell.getValue());
    }

    private void assertAlignment(String expectedAlignment, String cellId) {
        SheetCellElement formatCell = spreadSheet.getCellAt(cellId);
        assertEquals(expectedAlignment,formatCell.getCssValue("text-align"));
    }
}
