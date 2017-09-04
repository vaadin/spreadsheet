package com.vaadin.addon.spreadsheet.test;

import com.vaadin.addon.spreadsheet.elements.SheetCellElement;
import com.vaadin.addon.spreadsheet.elements.SpreadsheetElement;
import org.junit.Test;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Tests for issue#604.
 */
public class ReplaceFormulaCellTest extends AbstractSpreadsheetTestCase {

    private SpreadsheetElement spreadSheet;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        headerPage.createNewSpreadsheet();
        spreadSheet = $(SpreadsheetElement.class).first();
    }

    @Test
    public void replace_FormulaValueWithStringValue_valueChanged() {
        final SheetCellElement a1 = spreadSheet.getCellAt("A1");
        a1.setValue("1");

        final SheetCellElement b1 = spreadSheet.getCellAt("B1");
        b1.setValue("=A1 + 1");

        assertThat(b1.getValue(), equalTo("2"));

        sheetController.selectCell("B1");
        new Actions(getDriver()).sendKeys(Keys.F2).build().perform(); //edit mode
        b1.setValue("value");

        assertThat(b1.getValue(), equalTo("value"));
    }

    @Test
    public void replace_FormulaValueWithNumberValue_valueChanged() {
        final SheetCellElement a1 = spreadSheet.getCellAt("A1");
        a1.setValue("1");

        final SheetCellElement b1 = spreadSheet.getCellAt("B1");
        b1.setValue("=A1 + 1");

        assertThat(b1.getValue(), equalTo("2"));

        sheetController.selectCell("B1");
        new Actions(getDriver()).sendKeys(Keys.F2).build().perform(); //edit mode
        b1.setValue("123");

        assertThat(b1.getValue(), equalTo("123"));
    }
}
