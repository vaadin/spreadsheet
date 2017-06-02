package com.vaadin.addon.spreadsheet;

import java.io.Serializable;
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

class NamedRangeUtils implements Serializable {

    private Spreadsheet spreadsheet;

    public NamedRangeUtils(Spreadsheet spreadsheet) {
        this.spreadsheet = spreadsheet;
    }
    
    private CellSelectionManager getSelectionManager() {
        return spreadsheet.getCellSelectionManager();
    }

    public String getNameForFormulaIfExists(CellRangeAddress cra) {
        final String sheetName = spreadsheet.getActiveSheet().getSheetName();
        final String formula = cra.formatAsString(sheetName, true);

        for (Name name : spreadsheet.getWorkbook().getAllNames()) {
            final boolean globalName = name.getSheetIndex() == -1;
            final boolean nameRefersToThisSheet =
                name.getSheetIndex() == spreadsheet.getActiveSheetIndex();

            if (globalName || nameRefersToThisSheet) {
                if (formula.equals(name.getRefersToFormula())) {
                    return name.getNameName();
                }
            }
        }

        return null;
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
     * Get cell reference type
     *
     * @param value
     *     New value of the address field
     * @return NameType of cell
     */
    private CellReference.NameType getCellReferenceType(String value) {
        SpreadsheetVersion spreadsheetVersion = getSpreadsheetVersion();
        return CellReference.classifyCellReference(value, spreadsheetVersion);
    }

    private SpreadsheetVersion getSpreadsheetVersion() {
        return spreadsheet.getWorkbook().getSpreadsheetVersion();
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
        if (getSelectionManager().getCellRangeAddresses().isEmpty()) {
            return getIndividualCellFormula();
        } else {
            return getRangeCellFormula();
        }
    }

    private String getIndividualCellFormula() {
        Sheet activeSheet = spreadsheet.getActiveSheet();
        String sheetName = activeSheet.getSheetName();
        String selectedCell = getSelectionManager().getSelectedCellRange()
            .formatAsString();
        return sheetName + "!" + selectedCell;
    }

    private String getRangeCellFormula() {
        StringBuilder rangeFormula = new StringBuilder();
        Sheet activeSheet = spreadsheet.getActiveSheet();
        String sheetName = activeSheet.getSheetName();
        for (CellRangeAddress cellRangeAddress : getSelectionManager()
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
        String formulaSheet = name.getSheetName();
        switchSheet(formulaSheet, rangeFormula);
        for (AreaReference aref : getAreaReferences(rangeFormula)) {
            if (aref.isSingleCell()) { // bug?
                selectSingleRange(aref, name.getNameName());
            } else {
                selectMultipleRanges(aref, name.getNameName());
            }
        }
    }

    private void switchSheet(String formulaSheet, String range) {
        if (!spreadsheet.getActiveSheet().getSheetName()
            .equals(formulaSheet)) {
            int sheetIndex = spreadsheet.getWorkbook().getSheetIndex(formulaSheet);
            spreadsheet.setActiveSheetIndex(sheetIndex);
            spreadsheet.initialSheetSelection = range;
        }
    }

    private void selectMultipleRanges(AreaReference aref, String name) {
        String areaString = aref.formatAsString();
        CellRangeAddress cra = spreadsheet
            .createCorrectCellRangeAddress(areaString);
        
        getSelectionManager().handleCellRangeSelection(cra, name);
    }

    private void selectSingleRange(AreaReference aref, String name) {
        CellReference cell = aref.getFirstCell();
        getSelectionManager()
            .handleCellAddressChange(cell.getRow() + 1, cell.getCol() + 1,
                false, name);
    }

    private AreaReference[] getAreaReferences(String rangeFormula) {
        if (!AreaReference.isContiguous(rangeFormula)) {
            return AreaReference.generateContiguous(rangeFormula);
        } else {
            return new AreaReference[] { new AreaReference(rangeFormula,
                getSpreadsheetVersion()) };
        }
    }
}
