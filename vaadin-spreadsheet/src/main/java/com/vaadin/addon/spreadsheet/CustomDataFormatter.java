package com.vaadin.addon.spreadsheet;

import java.util.regex.Pattern;

import org.apache.poi.ss.format.CellFormat;
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

    private String[] getCustomFormatParts(String format) {
        return format.split(";");
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

        String[] parts = dataFormatString.split(";", -1);

//        if (parts.length < 3) {
//            return formatter.formatCellValue(cell, evaluator);
//        }

        if (getCellType(cell, evaluator) == CellType.NUMERIC) {

            final double value = cell.getNumericCellValue();
            return formatDataUsingFormatPart(Math.abs(value),
                getNumericFormat(value, parts));


//            if (value > 0.0) {
//                return formatDataUsingFormatPart(value, parts[0]);
//            } else if (value < 0.0) {
//                return formatDataUsingFormatPart(value, parts[1]);
//            } else { // value == 0.0
//                return formatDataUsingFormatPart(value, parts[2]);
//            }
        }

        if (parts.length == 4 && getCellType(cell, evaluator) == CellType.STRING) {
            if (parts[3].isEmpty()) {
                return "";
            }

            return CellFormat.getInstance(dataFormatString).apply(cell).text;
        }

        return formatter.formatCellValue(cell, evaluator);

        //        CellType cellType = getCellType(cell, evaluator);
//
//        if (hasThreeParts(dataFormatString)) {
//            CellFormat cfmt = CellFormat.getInstance("# ##0 _€;[Red]-# ##0 _€");
//
//            System.out.println(dataFormatString);
//            System.out.println(cfmt.apply(cell).textColor);
//
//            return cfmt.apply(cell).text + cfmt.apply(cell).textColor;
//
////            String newFormatString = changeFormat(cell, evaluator);
////            //double numericCellValue = cell.getNumericCellValue();
////            //if it is negative remove the - sign
////            //numericCellValue = Math.abs(numericCellValue);
////            return formatter
////                .formatRawCellContents(numericCellValue, -1, newFormatString);
//        }
//
//        return formatter.formatCellValue(cell, evaluator);
    }

    private static final Pattern NUMBER_PATTERN = Pattern.compile("[0#]+");
    private String formatDataUsingFormatPart(double value, String format) {

        if (format.isEmpty()) {
            return "";
        }

        if (!NUMBER_PATTERN.matcher(format).find()) {
            format = format + ";" + format + ";" + format;
        }

        return formatter.formatRawCellContents(value, 0, format);
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

    private String getNumericFormat(double value, String[] formatParts) {
        // fall through intended
        switch (formatParts.length) {
        case 3:
        case 4:
            if (value == 0)
                return formatParts[2];
        case 2:
            if (value < 0)
                return formatParts[1];
        case 1:
        default:
            return formatParts[0];
        }
    }

    private int getFormatIndex(Cell cell, FormulaEvaluator evaluator) {

        CellType cellType = getCellType(cell, evaluator);

        if (cellType != CellType.NUMERIC)
            return TEXT_FORMAT_INDEX;
        double numericCellValue = cell.getNumericCellValue();
        if (numericCellValue > 0)
            return POSITIVE_FORMAT_INDEX;

        return numericCellValue < 0 ? NEGATIVE_FORMAT_INDEX : ZERO_FORMAT_INDEX;
    }

    private CellType getCellType(Cell cell, FormulaEvaluator evaluator) {
        CellType cellType = cell.getCellTypeEnum();
        if (cellType == CellType.FORMULA) {
            cellType = evaluator.evaluateFormulaCellEnum(cell);
        }
        return cellType;
    }

    private String getFormatPart(String threePartFormat, int i) {
        String[] parts = threePartFormat.split(";");
        return parts[i];
    }
}
