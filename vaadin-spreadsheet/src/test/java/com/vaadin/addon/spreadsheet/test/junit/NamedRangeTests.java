package com.vaadin.addon.spreadsheet.test.junit;

import java.io.File;
import java.net.URL;
import java.util.Set;

import org.apache.poi.ss.util.CellReference;
import org.junit.Test;

import com.vaadin.addon.spreadsheet.Spreadsheet;

public class NamedRangeTests {

    private File getSpreadsheetTestFile(String filename) throws Exception {
        URL testSheetResource = this.getClass().getClassLoader()
            .getResource("test_sheets/" + filename);
        return new File(testSheetResource.toURI());
    }

    @Test
    public void namedRangeSelect_enterValidNamedRange_namedRangeSelected()
        throws Exception {

        Spreadsheet ss = new Spreadsheet(
            getSpreadsheetTestFile("named_ranges.xlsx"));

//        new TestableUI(ss);

        ss.setSelection("john");

//        ss.getCellSelectionManager().on

        Set<CellReference> selectedCellReferences = ss
            .getSelectedCellReferences();

        System.out.println(selectedCellReferences);
    }

}
