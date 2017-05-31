package com.vaadin.addon.spreadsheet;

import java.util.Arrays;
import java.util.List;

import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.ss.util.CellReference.NameType;

class CellReferenceUtils {

    private Spreadsheet spreadsheet;
    private CellSelectionManager cellSelectionManager;

    public CellReferenceUtils(Spreadsheet spreadsheet) {
        this.spreadsheet = spreadsheet;
        cellSelectionManager = spreadsheet.getCellSelectionManager();

    }

    /**
     * Get cell reference type
     *
     * @param value
     *     New value of the address field
     * @return NameType of cell
     */
    private CellReference.NameType getCellReferenceType(String value) {
        SpreadsheetVersion spreadsheetVersion = spreadsheet
            .getSpreadsheetVersion();
        return CellReference.classifyCellReference(value, spreadsheetVersion);
    }

    /**
     * Check if entered range is cell reference
     *
     * @param value
     *     New value of the address field
     * @return
     */
    public boolean isCellReference(String value) {
        CellReference.NameType nameType = getCellReferenceType(value);
        List<CellReference.NameType> cellColRowTypes = Arrays
            .asList(NameType.CELL, NameType.COLUMN, NameType.ROW);
        if (cellColRowTypes.contains(nameType)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Check if entered range is cell reference
     *
     * @param value
     *     New value of the address field
     * @return
     */
    public boolean isNamedRange(String value) {
        CellReference.NameType nameType = getCellReferenceType(value);
        if (NameType.NAMED_RANGE.equals(nameType)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Run when address field contains named range This creates new range or
     * selects already existing one.
     *
     * @param value
     *     Address field value
     */
    public void onNamedRange(String value) {
        Workbook workbook = spreadsheet.getWorkbook();
        Name name = workbook.getName(value);
        if (name == null) {
            createNewNamedRange(value);
        } else {
            selectExistingNameRange(name);
        }
    }

    /**
     * Create new named range
     *
     * @param value
     *     Name of value range
     */
    private void createNewNamedRange(String value) {
        Workbook workbook = spreadsheet.getWorkbook();

        Name name = workbook.createName();
        name.setNameName(value);
        name.setRefersToFormula(getSelectedRangeFormula());

        SpreadsheetFactory.loadNamedRanges(spreadsheet);
    }

    /**
     * Get formula for currently selected range(s)
     *
     * @return
     */
    private String getSelectedRangeFormula() {
        if (cellSelectionManager.getCellRangeAddresses().isEmpty()) {
            return getIndividualCellFormula();
        } else {
            return getRangeCellFormula();
        }
    }

    private String getIndividualCellFormula() {
        Sheet activeSheet = spreadsheet.getActiveSheet();
        String sheetName = activeSheet.getSheetName();
        String selectedCell = cellSelectionManager.getSelectedCellRange()
            .formatAsString();
        return sheetName + "!" + selectedCell;
    }

    private String getRangeCellFormula() {
        StringBuilder rangeFormula = new StringBuilder();
        Sheet activeSheet = spreadsheet.getActiveSheet();
        String sheetName = activeSheet.getSheetName();
        for (CellRangeAddress cellRangeAddress : cellSelectionManager
            .getCellRangeAddresses()) {
            
            if (rangeFormula.length() != 0) {
                rangeFormula.append(","); // TODO
            }
            
            rangeFormula
                .append(cellRangeAddress.formatAsString(sheetName, false));
        }
        return rangeFormula.toString();
    }

    private void selectExistingNameRange(Name name) {
        String rangeFormula = name.getRefersToFormula();
        String formulasSheet = name.getSheetName();
        switchSheet(formulasSheet, rangeFormula);
        for (AreaReference aref : getAreaReferences(rangeFormula)) {
            if (aref.isSingleCell()) { // bug?
                selectSingleRange(aref);
            } else {
                selectMultipleRanges(aref, name.getNameName());
            }
        }
    }

    private void switchSheet(String formulasSheet, String range) {
        if (!spreadsheet.getActiveSheet().getSheetName()
            .equals(formulasSheet)) {
            spreadsheet.switchSheet(formulasSheet); // named range defines sheet
            spreadsheet.initialSheetSelection = range;
        }
    }

    private void selectMultipleRanges(AreaReference aref, String name) {
        String areaString = aref.formatAsString();
        CellRangeAddress cra = spreadsheet
            .createCorrectCellRangeAddress(areaString);
        
        cellSelectionManager.handleCellRangeSelection(cra, name);
    }

    private void selectSingleRange(AreaReference aref) {
        CellReference cell = aref.getFirstCell();
        cellSelectionManager
            .handleCellAddressChange(cell.getRow() + 1, cell.getCol() + 1,
                false);
    }

    private AreaReference[] getAreaReferences(String rangeFormula) {
        if (!AreaReference.isContiguous(rangeFormula)) {
            return AreaReference.generateContiguous(rangeFormula);
        } else {
            return new AreaReference[] { new AreaReference(rangeFormula,
                spreadsheet.getSpreadsheetVersion()) };
        }
    }

}
