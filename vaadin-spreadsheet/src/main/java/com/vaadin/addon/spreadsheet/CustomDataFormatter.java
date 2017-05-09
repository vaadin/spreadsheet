package com.vaadin.addon.spreadsheet;

import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

/**
 * TODO: to be removed when the bug (https://bz.apache.org/bugzilla/show_bug.cgi?id=60040) is resolved
 *
 * {@Link org.apache.poi.ss.usermodel.DataFormatter} doesn't consider
 * Locale while formatting three part custom format (eg. #.##0,00#;(#.##0,00);"-").
 *
 * However, a custom format that has one or two parts is treated correctly
 * with regards to Locale.
 *
 * This class is used as workaround for 3-4 part custom formats, which is done
 * only one option based on the cell content and treating it as a one-part
 * formatting.
 */
class CustomDataFormatter extends DataFormatter {
    private static final Pattern NUMBER_PATTERN = Pattern.compile("[0#]+");

    // In a custom format the first part represents a format for positive numbers,
    // the second for negative numbers, the third for zero and the fourth a plain text
    private final int POSITIVE_FORMAT_INDEX = 0;
    private final int NEGATIVE_FORMAT_INDEX = 1;
    private final int ZERO_FORMAT_INDEX = 2;
    private final int TEXT_FORMAT_INDEX = 3;

    private final DataFormatter formatter;

    public CustomDataFormatter(DataFormatter formatter) {
        this.formatter = formatter;
    }

    /**
     * Returns true if the formatting contains 3 or more parts.
     */
    private boolean hasThreeParts(String dataFormatString) {
        return dataFormatString.contains(";")
            && dataFormatString.indexOf(';') != dataFormatString
            .lastIndexOf(';');
    }

    /**
     * If a cell has a custom format with three or more parts
     * and it contains a numeric value,
     * then this method formats it as if it had only one part by
     * choosing the format based on the value (i.e. positive, negative or 0).
     *
     * Otherwise use <code>DataFormatter#formatCellValue</code>
     **/
    @Override
    public String formatCellValue(Cell cell, FormulaEvaluator evaluator) {

        String dataFormatString = cell.getCellStyle().getDataFormatString();
        CellType cellType = getCellType(cell, evaluator);
        if (hasThreeParts(dataFormatString) && cellType == CellType.NUMERIC) {
            String newFormatString = changeFormat(cell, evaluator);
            double numericCellValue = cell.getNumericCellValue();
            //if it is negative remove the - sign
            numericCellValue = Math.abs(numericCellValue);
            return formatter
                .formatRawCellContents(numericCellValue, -1, newFormatString);
        }

        return formatter.formatCellValue(cell, evaluator);
    }

    /**
     * Given a Cell with a custom format having 3 (or in some cases 4) parts,
     * returns only one part (according to the value of the cell)
     */
    private String changeFormat(Cell cell, FormulaEvaluator evalueator) {
        int index = getFormatIndex(cell, evalueator);
        String oldFormatString = cell.getCellStyle().getDataFormatString();
        String newFormatString = getFormatPart(oldFormatString, index);

        //POI doesn't format string literals having 1 part, repeat it three times
        if (!NUMBER_PATTERN.matcher(newFormatString).find()) {
            newFormatString += ";" + newFormatString + ";" + newFormatString;
        }

        return newFormatString;
    }

    private int getFormatIndex(Cell cell, FormulaEvaluator evalueator) {

        CellType cellType = getCellType(cell, evalueator);

        if (cellType != CellType.NUMERIC)
            return TEXT_FORMAT_INDEX;
        double numericCellValue = cell.getNumericCellValue();
        if (numericCellValue > 0)
            return POSITIVE_FORMAT_INDEX;
        return numericCellValue < 0 ? NEGATIVE_FORMAT_INDEX : ZERO_FORMAT_INDEX;
    }

    private CellType getCellType(Cell cell, FormulaEvaluator evalueator) {
        CellType cellType = cell.getCellTypeEnum();
        if (cellType == CellType.FORMULA) {
            cellType = evalueator.evaluateFormulaCellEnum(cell);
        }
        return cellType;
    }

    private String getFormatPart(String threePartFormat, int i) {
        String[] parts = threePartFormat.split(";");
        return parts[i];
    }
}
