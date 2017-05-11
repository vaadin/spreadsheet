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
 * POI library has two classes {@Link org.apache.poi.ss.format.CellFormat} and
 * {@Link org.apache.poi.ss.usermodel.DataFormatter} to deal with custom formatting.
 * The implementation is very buggy!
 *
 * This class work around the following bugs:
 *
 * 1) {@Link org.apache.poi.ss.format.CellFormat} does not use the Locale info.
 * Therefore cells having three or four part custom format
 * (eg. #.##0,00#;(#.##0,00);"-") are not correctly formatted.
 *
 * 2) If a custom format has only one part and this part is literal (e.g. does
 * not refer to the number being entered), the formatting is not done correctly.
 *
 * 3) Custom formats that have empty parts (i.e. they render a certain value as
 * empty) are not rendered correctly.
 *
 * CellFormat does okay job for text formatting and literals, but for numbers it
 * fails to consider the locale.
 *
 * DataFormatter can correctly format numbers using the locale, but cannot format
 * text or literals.
 *
 * This class tries to work around the most use cases by delegating a certain case to
 * one parser or another and changing the format string to be compatible with
 * the parser.
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
     * If a cell has a custom format with three or more parts
     * and it contains a numeric value,
     * then this method formats it as if it had only one part by
     * choosing the format based on the value (i.e. positive, negative or 0).
     *
     * Otherwise use <code>DataFormatter#formatCellValue</code>
     **/
    @Override
    public String formatCellValue(Cell cell, FormulaEvaluator evaluator) {

        final String dataFormatString = cell.getCellStyle().getDataFormatString();

        final String[] parts = dataFormatString.split(";", -1);

        final CellType cellType = getCellType(cell, evaluator);

        if (cellType == CellType.NUMERIC) {
            final double value = cell.getNumericCellValue();

            return formatNumericValueUsingFormatPart(cell, Math.abs(value),
                getNumericFormat(value, parts));
        }

        if (parts.length == 4 && cellType == CellType.STRING) {
            return formatStringCellValue(cell, dataFormatString, parts);
        }

        return formatter.formatCellValue(cell, evaluator);
    }

    private CellType getCellType(Cell cell, FormulaEvaluator evaluator) {

        CellType cellType = cell.getCellTypeEnum();
        if (cellType == CellType.FORMULA) {
            cellType = evaluator.evaluateFormulaCellEnum(cell);
        }
        return cellType;
    }

    private String formatNumericValueUsingFormatPart(Cell cell, double value, String format) {

        if (format.isEmpty()) {
            return "";
        }

        if (isOnlyLiteralFormat(format)) {
            // CellFormat can format literals
            return CellFormat.getInstance(format).apply(cell).text;
        } else {
            // DataFormatter can format numbers correctly
            return formatter.formatRawCellContents(value, 0, format);
        }
    }

    /**
     * Best attempt to check if the format contains numbers that
     * we are formatting or is purely literal.
     * Known issue that is does not consider possible escaped/inside string
     * characters, but it's a very rare case.
     */
    private boolean isOnlyLiteralFormat(String format) {
        final Pattern NUMBER_PATTERN = Pattern.compile("[0#]+");

        return !NUMBER_PATTERN.matcher(format).find();
    }

    private String getNumericFormat(double value, String[] formatParts) {
        // fall through intended
        switch (formatParts.length) {
        case 3:
        case 4:
            if (value == 0)
                return formatParts[ZERO_FORMAT_INDEX];
        case 2:
            if (value < 0)
                return formatParts[NEGATIVE_FORMAT_INDEX];
        case 1:
        default:
            return formatParts[POSITIVE_FORMAT_INDEX];
        }
    }

    /**
     * DataFormatter cannot format strings, but CellFormat can.
     */
    private String formatStringCellValue(Cell cell, String formatString, String[] parts) {
        if (parts[TEXT_FORMAT_INDEX].isEmpty()) {
            return "";
        }

        return CellFormat.getInstance(formatString).apply(cell).text;
    }
}
