package com.vaadin.addon.spreadsheet.test;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.Keys;

import com.vaadin.addon.spreadsheet.elements.SpreadsheetElement;
import com.vaadin.addon.spreadsheet.test.testutil.ModifierController;
import com.vaadin.addon.spreadsheet.test.testutil.SheetController;
import com.vaadin.testbench.parallel.Browser;

/**
 * SelectionTest
 */
public class SelectionTest extends AbstractSpreadsheetTestCase {

    /**
     * ctrl
     */
    protected SheetController ctrl;
    /**
     * shift
     */
    protected ModifierController shift;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        headerPage.createNewSpreadsheet();
        ctrl = new ModifierController(driver, Keys.CONTROL,
                testBench(getDriver()), getDesiredCapabilities());
        shift = new ModifierController(driver, Keys.SHIFT,
                testBench(getDriver()), getDesiredCapabilities());
    }
    /**
     * testSelectionSingleCell
     */
    @Test
    public void testSelectionSingleCell() {
        skipBrowser("Shift/Ctrl select fails with Firefox and PhantomJS", Browser.FIREFOX, Browser.PHANTOMJS);
        sheetController.selectCell("B2");
        ctrl.selectCell("C3");
        ctrl.selectCell("D4");

        assertCellSelected("B2");
        assertCellSelected("C3");
        assertCellSelected("D4");
    }

    /**
     * testMultipleRectSelection
     */
    @Test
    public void testMultipleRectSelection() {
        skipBrowser("Shift/Ctrl select fails with Firefox and PhantomJS", Browser.FIREFOX, Browser.PHANTOMJS);
        sheetController.selectCell("B1");
        shift.selectCell("B3");
        ctrl.selectCell("D3");
        shift.selectCell("E3");

        assertCellSelected("B1");
        assertCellSelected("B2");
        assertCellSelected("B3");
        assertCellSelected("D3");
        assertCellSelected("E3");
    }

    /**
     * testComplicatedCellSelection
     */
    @Test
    public void testComplicatedCellSelection() {
        skipBrowser("Shift/Ctrl select fails with Firefox and PhantomJS", Browser.FIREFOX, Browser.PHANTOMJS);
        sheetController.selectCell("B2");
        ctrl.clickCell("E2");
        ctrl.clickCell("C3");
        shift.clickCell("D4");
        ctrl.clickCell("E5");
        ctrl.clickCell("B5");

        assertCellSelected("B2");
        assertCellSelected("B5");
        assertCellSelected("C3");
        assertCellSelected("C4");
        assertCellSelected("D3");
        assertCellSelected("D4");
        assertCellSelected("E2");
        assertCellSelected("E5");
    }

    /**
     * testRowSelection
     */
    @Test
    public void testRowSelection() {
        skipBrowser("Shift/Ctrl select fails with Firefox and PhantomJS", Browser.FIREFOX, Browser.PHANTOMJS);
        sheetController.clickRow(3);
        ctrl.clickRow(5);
        shift.clickRow(7);

        assertRowSelected("3");
        assertRowSelected("5");
        assertRowSelected("6");
        assertRowSelected("7");
    }

    /**
     * testColumnSelection
     */
    @Test
    public void testColumnSelection() {
        skipBrowser("Shift/Ctrl select fails with Firefox and PhantomJS", Browser.FIREFOX, Browser.PHANTOMJS);
        sheetController.clickColumn("B");
        ctrl.clickColumn("D");
        shift.clickColumn("F");

        assertColumnSelected("B");
        assertColumnSelected("D");
        assertColumnSelected("E");
        assertColumnSelected("F");
    }

    /**
     * testRowColumnMixed
     */
    @Test
    public void testRowColumnMixed() {
        skipBrowser("Shift/Ctrl select fails with Firefox and PhantomJS", Browser.FIREFOX, Browser.PHANTOMJS);
        sheetController.clickColumn("C");
        ctrl.clickColumn("E");
        ctrl.clickRow(3);
        shift.clickRow(5);
        ctrl.clickColumn("G");

        assertColumnSelected("C");
        assertColumnSelected("E");
        assertColumnSelected("G");
        assertRowSelected("3");
        assertRowSelected("4");
        assertRowSelected("5");
    }

    /**
     * testColumnRowWithPagination
     */
    @Test
    @Ignore("Navigating to cell clears the selection, so this can't work -> Figure out another way to scroll")
    public void testColumnRowWithPagination() {
        skipBrowser("Shift/Ctrl select fails with Firefox and PhantomJS", Browser.FIREFOX, Browser.PHANTOMJS);
        sheetController.clickColumn("C");
        ctrl.clickColumn("E");
        ctrl.clickRow(3);

        sheetController.navigateToCell("C195");
        assertCellSelected("C190");
        assertCellSelected("C200");
        assertCellSelected("E190");
        assertCellSelected("E200");

        sheetController.navigateToCell("AV3");
        assertCellSelected("AS3");
        assertCellSelected("AZ3");
    }

    /**
     * testShiftClick
     */
    @Test
    public void testShiftClick() {
        skipBrowser("Shift/Ctrl select fails with Firefox and PhantomJS", Browser.FIREFOX, Browser.PHANTOMJS);
        sheetController.clickCell("B2");
        shift.clickCell("C3");
        assertCellSelected("B2");
        assertCellSelected("B3");
        assertCellSelected("C2");
        assertCellSelected("C3");

        sheetController.clickCell("B5");
        shift.clickCell("B7");
        assertCellSelected("B5");
        assertCellSelected("B6");
        assertCellSelected("B7");

        sheetController.clickCell("E2");
        shift.clickCell("G2");
        assertCellSelected("E2");
        assertCellSelected("F2");
        assertCellSelected("G2");
    }

    /**
     * testWithMouse
     */
    @Test
    public void testWithMouse() {
        sheetController.selectRegion("B2", "C3");

        assertCellSelected("B2");
        assertCellSelected("B3");
        assertCellSelected("C2");
        assertCellSelected("C3");

        sheetController.selectRegion("B5", "B6");

        assertCellSelected("B5");
        assertCellSelected("B6");

        sheetController.selectRegion("E2", "F2");

        assertCellSelected("E2");
        assertCellSelected("F2");
    }

    /**
     * mouseSelection_cellWithStringValue_cellIsSelected
     */
    @Test
    public void mouseSelection_cellWithStringValue_cellIsSelected() {
        sheetController.clickCell("B2");
        assertCellSelected("B2");
        sheetController.setCellVallue("B2", "value");

        sheetController.clickCell("C5");
        assertCellSelected("C5");

        sheetController.clickCell("B2");
        assertCellSelected("B2");
    }

    private void assertCellSelected(String cell) {
        assertTrue($(SpreadsheetElement.class).first().getCellAt(cell)
                .isCellSelected());
    }

    private void assertRowSelected(String row) {
        for (int i = 0; i < 10; i++) {
            assertCellSelected((char) ('A' + i) + row);
        }
    }

    private void assertColumnSelected(String column) {
        for (int i = 1; i < 10; i++) {
            assertCellSelected(column + i);
        }
    }

}
