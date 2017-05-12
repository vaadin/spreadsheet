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
    public void assertCorrectValuesAfterLoading() {
        setLocale(Locale.US);

        assertNumbersColumn();
        assertLiteralsColumn();
        assertEmptyColumn();
    }

    private void assertNumbersColumn() {
        // four part format
        assertCellValue("C1", "5.0");
        assertCellValue("C2", "(5.0)");
        assertCellValue("C3", "-");
        assertCellValue("C4", "\"formatted text\"");

        // three part format
        assertCellValue("C6", "5.0");
        assertCellValue("C7", "(5.0)");
        assertCellValue("C8", "-");
        assertCellValue("C9", "text");

        // two part format
        assertCellValue("C11", "5.0");
        assertCellValue("C12", "(5.0)");
        assertCellValue("C13", "0.0");
        assertCellValue("C14", "text");

        // one part format
        assertCellValue("C16", "5.0");
        assertCellValue("C17", "-5.0");
        assertCellValue("C18", "0.0");
        assertCellValue("C19", "text");
    }

    private void assertLiteralsColumn() {
        final String literal = "literal";

        // four part format
        assertCellValue("E1", literal);
        assertCellValue("E2", literal);
        assertCellValue("E3", literal);
        assertCellValue("E4", literal);

        // three part format
        assertCellValue("E6", literal);
        assertCellValue("E7", literal);
        assertCellValue("E8", literal);
        assertCellValue("E9", "text");

        // two part format
        assertCellValue("E11", literal);
        assertCellValue("E12", literal);
        assertCellValue("E13", literal);
        assertCellValue("E14", "text");

        // one part format
        assertCellValue("E16", literal);
        assertCellValue("E17", "-" + literal);
        assertCellValue("E18", literal);
        assertCellValue("E19", "text");
    }

    private void assertEmptyColumn() {
        final String empty = "";

        // four part format
        assertCellValue("G1", empty);
        assertCellValue("G2", empty);
        assertCellValue("G3", empty);
        assertCellValue("G4", empty);

        // three part format
        assertCellValue("G6", empty);
        assertCellValue("G7", empty);
        assertCellValue("G8", empty);
        assertCellValue("G9", "text");

        // two part format
        assertCellValue("G11", empty);
        assertCellValue("G12", empty);
        assertCellValue("G13", empty);
        assertCellValue("G14", "text");

        // one part format
        assertCellValue("G16", empty);
        assertCellValue("G17", "-" + empty);
        assertCellValue("G18", empty);
        assertCellValue("G19", "text");
    }

//    @Test
//    public void customFormatCellWithFourParts_enterZero_getsFormatted()
//        throws Exception {
//        // changes into minus, Excel also adds space on both sides and fills the
//        // left side with whitespace
//        assertCellFormat("C3", "0", "-", Locale.US);
//    }
//
//    @Test
//    public void customFormatCellWithFourParts_enterText_noChange()
//        throws Exception {
//        //adds a double quote on text values
//        assertCellFormat("C4", "text", "\"text\"", Locale.US);
//    }
//
//    @Test
//    public void customFormatCellWithFourParts_enterNumberWithITlocale_getsFormatted()
//        throws Exception {
//        //number formatted in European format
//        assertCellFormat("C1", "=5555555.5", "5.555.555,5", Locale.ITALY);
//    }
//
//    @Test
//    public void customFormatCellWithThreeParts_enterPositiveNumber_becomesNumberType()
//        throws Exception {
//        // numbers are right-aligned
//        assertAlignment("right", "C6");
//        assertCellFormat("C6", "5", "5.0", Locale.US);
//    }
//
//    @Test
//    public void customFormatCellWithThreeParts_enterNegativeNumber_getsFormatted()
//        throws Exception {
//        // drops minus and gains parentheses
//        assertCellFormat("C6", "-5", "(5.0)", Locale.US);
//    }
//
//    @Test
//    public void customFormatCellWithThreeParts_enterZero_getsFormatted()
//        throws Exception {
//
//        // changes into minus, Excel also adds space on both sides and fills the
//        // left side with whitespace
//        assertCellFormat("C6", "0", "-", Locale.US);
//    }
//
//    @Test
//    public void customFormatCellWithThreeParts_enterText_noChange()
//        throws Exception {
//
//        assertCellFormat("C6", "text", "text", Locale.US);
//    }
//
//    @Test
//    public void customFormatCellWithThreeParts_enterNumberWithITlocale_getsFormatted()
//        throws Exception {
//        assertCellFormat("C6", "=5555555.5", "5.555.555,5", Locale.ITALY);
//    }
//
//    @Test
//    public void customFormatCellWithTwoParts_enterPositiveNumber_becomesNumberType()
//        throws Exception {
//        // numbers are right-aligned
//        assertAlignment("right", "C11");
//        assertCellFormat("C11", "5", "5.0", Locale.US);
//    }
//
//    @Test
//    public void customFormatCellWithTwoParts_enterNegativeNumber_getsFormatted()
//        throws Exception {
//        // drops minus and gains parentheses
//        assertCellFormat("C11", "-5", "(5.0)", Locale.US);
//    }
//
//    @Test
//    public void customFormatCellWithTwoParts_enterZero_getsFormatted()
//        throws Exception {
//        assertCellFormat("C11", "0", "0.0", Locale.US);
//    }
//
//    @Test
//    public void customFormatCellWithTwoParts_enterText_noChange()
//        throws Exception {
//
//        assertCellFormat("C11", "text", "text", Locale.US);
//    }
//
//    @Test
//    public void customFormatCellWithTwoParts_enterNumberWithITlocale_getsFormatted()
//        throws Exception {
//        assertCellFormat("C11", "=5555555.5", "5.555.555,5", Locale.ITALY);
//    }
//
//    @Test
//    public void customFormatCellWithOnePart_enterPositiveNumber_becomesNumberType()
//        throws Exception {
//        // numbers are right-aligned
//        assertAlignment("right", "C11");
//        assertCellFormat("C16", "5", "5.0", Locale.US);
//    }
//
//    @Test
//    public void customFormatCellWithOnePart_enterNegativeNumber_getsFormatted()
//        throws Exception {
//        assertCellFormat("C16", "-5", "-5.0", Locale.US);
//    }
//
//    @Test
//    public void customFormatCellWithOnePart_enterZero_getsFormatted()
//        throws Exception {
//        assertCellFormat("C16", "0", "0.0", Locale.US);
//    }
//
//    @Test
//    public void customFormatCellWithOnePart_enterText_noChange()
//        throws Exception {
//
//        assertCellFormat("C16", "text", "text", Locale.US);
//    }
//
//    @Test
//    public void customFormatCellWithOnePart_enterNumberWithITlocale_getsFormatted()
//        throws Exception {
//        assertCellFormat("C16", "=5555555.5", "5.555.555,5", Locale.ITALY);
//    }
//
//    private void assertCellFormat(String cellID, String value, String expected,
//        Locale locale) {
//        SheetCellElement formatCell = spreadSheet.getCellAt(cellID);
//        formatCell.setValue(value);
//        setLocale(locale);
//        assertEquals(expected, formatCell.getValue());
//    }

    private void assertCellValue(String cell, String value) {
        assertEquals(value, spreadSheet.getCellAt(cell).getValue());
    }

    private void assertAlignment(String expectedAlignment, String cellId) {
        SheetCellElement formatCell = spreadSheet.getCellAt(cellId);
        assertEquals(expectedAlignment, formatCell.getCssValue("text-align"));
    }
}
