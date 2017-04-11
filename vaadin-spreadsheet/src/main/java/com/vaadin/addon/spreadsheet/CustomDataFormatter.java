package com.vaadin.addon.spreadsheet;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

/**
 *TODO: to be removed when the bug (https://bz.apache.org/bugzilla/show_bug.cgi?id=60040) is resolved
 */
public class CustomDataFormatter {
    private final int POSITIVE_FORMAT_INDEX = 0;
    private final int NEGATIVE_FORMAT_INDEX = 1;
    private final int ZERO_FORMAT_INDEX = 2;
    private final int TEXT_FORMAT_INDEX = 3;

    private final DataFormatter formatter;

    public CustomDataFormatter(DataFormatter formatter) {
        this.formatter = formatter;
    }

    /*
     * returns true if the formatting contains 3 or more parts
     */
    public static boolean hasThreeParts(String dataFormatString) {
        return dataFormatString.contains(";")
            && dataFormatString.indexOf(';') != dataFormatString
            .lastIndexOf(';');
    }

    public String formatCellValue(Cell cell, FormulaEvaluator evaluator) {
        String result = null;

        String dataFormatString = cell.getCellStyle().getDataFormatString();
        if (hasThreeParts(dataFormatString)) {
            String oldFormat = changeFormat(cell, evaluator);
            result = formatter.formatCellValue(cell, evaluator);
            setCellFormat(cell, oldFormat);
            //remove - sign from negetive numbers unless it is specified in the format
            if (result.startsWith("-") && !dataFormatString.startsWith("-")) {
                result = result.substring(1);
            }

            return result;
        }

        return formatter.formatCellValue(cell, evaluator);
    }

    /*
     *Given a Cell with a custom format having 3 (or in some cases 4) parts,
     * This method chooses only one part (according to the value of the cell)
     */
    private String changeFormat(Cell cell, FormulaEvaluator evalueator) {
        int index = getFormatIndex(cell, evalueator);
        String oldFormatString = cell.getCellStyle().getDataFormatString();
        String newFormatString = getFormatPart(oldFormatString, index);
        setCellFormat(cell, newFormatString);
        return oldFormatString;
    }

    private void setCellFormat(Cell cell, String dataFormatString) {
        DataFormat dataFormat = cell.getSheet().getWorkbook()
            .createDataFormat();
        CellStyle cellStyle = cell.getCellStyle();

        cellStyle.setDataFormat(dataFormat.getFormat(dataFormatString));
    }

    private int getFormatIndex(Cell cell, FormulaEvaluator evalueator) {

        CellType cellType = cell.getCellTypeEnum();
        if (cellType == CellType.FORMULA) {
            cellType = evalueator.evaluateFormulaCellEnum(cell);
        }

        if (cellType != CellType.NUMERIC)
            return TEXT_FORMAT_INDEX;
        double numericCellValue = cell.getNumericCellValue();
        if (numericCellValue > 0)
            return POSITIVE_FORMAT_INDEX;
        return numericCellValue < 0 ? NEGATIVE_FORMAT_INDEX : ZERO_FORMAT_INDEX;
    }

    private String getFormatPart(String threePartFormat, int i) {
        String[] parts = threePartFormat.split(";");
        //in case if there is no format part for text live it as it is
        if (i >= parts.length)
            return "@";

        return parts[i];
    }
}
