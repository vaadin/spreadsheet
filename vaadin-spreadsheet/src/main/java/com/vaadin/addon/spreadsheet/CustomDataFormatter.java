package com.vaadin.addon.spreadsheet;

import java.util.regex.Pattern;

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
    private static final Pattern NUMBER_PATTERN = Pattern.compile("[0#]+");

    private final DataFormatter formatter;

    public CustomDataFormatter(DataFormatter formatter) {
        this.formatter = formatter;
    }

    /*
     * returns true if the formatting contains 3 or more parts
     */
    private boolean hasThreeParts(String dataFormatString) {
        return dataFormatString.contains(";")
            && dataFormatString.indexOf(';') != dataFormatString
            .lastIndexOf(';');
    }

    public String formatCellValue(Cell cell, FormulaEvaluator evaluator) {

        String dataFormatString = cell.getCellStyle().getDataFormatString();
        CellType cellType = getCellType(cell, evaluator);
        if (hasThreeParts(dataFormatString) && cellType == CellType.NUMERIC) {
            String newFormatString = changeFormat(cell, evaluator);
                double numericCellValue = cell.getNumericCellValue();
                //if it is negative remove the - sign
                numericCellValue = numericCellValue < 0 ?
                    numericCellValue * -1 :
                    numericCellValue;
                return formatter.formatRawCellContents(numericCellValue, -1,
                    newFormatString);
        }

        return formatter.formatCellValue(cell, evaluator);
    }

    /*
     *Given a Cell with a custom format having 3 (or in some cases 4) parts,
     *  returns only one part (according to the value of the cell)
     */
    private String changeFormat(Cell cell, FormulaEvaluator evalueator) {
        int index = getFormatIndex(cell, evalueator);
        String oldFormatString = cell.getCellStyle().getDataFormatString();
        String newFormatString = getFormatPart(oldFormatString, index);

        //POI doesn't format string literals having 1 part, repeat it three times
        if(!NUMBER_PATTERN.matcher(newFormatString).find()){
            newFormatString +=  ";" +newFormatString + ";" + newFormatString;
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
        //in case if there is no format part for text live it as it is
        if (i >= parts.length)
            return "@";

        return parts[i];
    }
}
