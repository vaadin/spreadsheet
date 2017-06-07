package com.vaadin.addon.spreadsheet;

/*
 * #%L
 * Vaadin Spreadsheet
 * %%
 * Copyright (C) 2013 - 2015 Vaadin Ltd
 * %%
 * This program is available under Commercial Vaadin Add-On License 3.0
 * (CVALv3).
 *
 * See the file license.html distributed with this software for more
 * information about licensing.
 *
 * You should have received a copy of the CVALv3 along with this program.
 * If not, see <http://vaadin.com/license/cval-3>.
 * #L%
 */

import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedString;

import org.apache.poi.hssf.converter.AbstractExcelUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;

/**
 * RowsAutofitUtil is an utility of the Spreadsheet component used
=======
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.w3c.dom.Element;

/**
 * RowsAutofitUtil is an utility class of the Spreadsheet component used
>>>>>>> 6e1287744e7295402dedc4a2ad881facb82b3ed1
 * to calculate proper height for autofitting rows.
 *
 * @author Vaadin Ltd.
 */
public class RowsAutofitUtil {
    // Sample text reaching the maximum possible row height
    public static final String EXAMPLE_TEXT = "0g";
    
    private final float securityMarginPercentage;
    private final float cssLineHeightPercentage;
    private final float letterSpacingInPoints;

    /**
     * Creates a new instance
     *
     * @param securityMarginPercentage
     *     a margin in terms of percentage
     *     of the full cell width/height that must be taken into account
     *     when autofitting.<br>E.g. percentage=0.15f
     *     means that autofit both:
     *     <ul>
     *     <li>is applied on an effective width which is 15% less than the real cell width,</li>
     *     <li>add a 15% extra to the autofitted height.</li>
     *     </ul>
     * @param cssLineHeightPercentage
     *     the value of css line-height property for cells
     * @param letterSpacingInPoints
     *     The value of css letter-spacing property
     *     expressed in points for cells
     */
    public RowsAutofitUtil(float securityMarginPercentage,
        float cssLineHeightPercentage, float letterSpacingInPoints) {
        this.securityMarginPercentage = securityMarginPercentage;
        this.cssLineHeightPercentage = cssLineHeightPercentage;
        this.letterSpacingInPoints = letterSpacingInPoints;
    }

    // The <row> attribute to set custom Height
    public static final String CUSTOM_HEIGHT = "customHeight";

    // The value for customHeight attribute that Excel actually uses for "true"
    public static final String EXCEL_TRUE = "1";

    // Since calculation of wrapped text is not so accurate
    // this amount of additional rows is taken into account to calculate
    // cell height
    private static final FontRenderContext fontRenderContext = new FontRenderContext(
        null, true, true);

    public void autoSizeRow(Sheet sheet, int row) {
        Row sheetRow = sheet.getRow(row);
        if (sheetRow == null) {
            return;
        }

        if (sheetRow.getFirstCellNum() == -1) {
            // When no font information is available,
            // "autofitting" a row means set its height to the default
            sheetRow.setHeightInPoints(sheet.getDefaultRowHeightInPoints());
            return;
        }

        float height = sheet.getDefaultRowHeightInPoints();
        for (Cell cell : sheetRow) {
            Font font = getCellFont(cell);
            if (font != null) {
                float requiredLineHeight = getRequiredLineHeight(font, EXAMPLE_TEXT);
                float lineSpaceHeight = requiredLineHeight * (cssLineHeightPercentage - 1);
                int lineCount = getLineCount(cell);
                
                float requiredCellHeight = lineCount * requiredLineHeight
                    + (lineCount - 1) * lineSpaceHeight;
                
                requiredCellHeight += requiredCellHeight * securityMarginPercentage;
                
                height = Math.max(height, requiredCellHeight);
            }
        }

        sheetRow.setHeightInPoints(height);
    }

    public static void setCustomHeight(Sheet sheet, int row,
        boolean customHeight) {
        Row sheetRow = sheet.getRow(row);
        if (sheetRow == null) {
            sheetRow = sheet.createRow(row);
        }

        // autofit works only with XSSF
        if (!(sheetRow instanceof XSSFRow)) {
            return;
        }

        ((XSSFRow) sheetRow).getCTRow().setCustomHeight(customHeight);
    }

    public static boolean isCustomHeight(Sheet sheet, int row) {
        Row sheetRow = sheet.getRow(row);

        // autofit works only with XSSF
        if (!(sheetRow instanceof XSSFRow)) {
            return false;
        }

        return ((XSSFRow) sheetRow).getCTRow().getCustomHeight();
    }

    private int getLineCount(Cell cell) {
        if (!isWrapText(cell)) {
            return 1;
        }

        // TODO Suppport formula strings
        String cellValue;
        try {
            cellValue = cell.getStringCellValue();
        } catch (Exception e) {
            return 1;
        }
        
        if (cellValue.isEmpty()) {
            return 1;
        }

        AttributedString str = new AttributedString(cellValue);
        XSSFFont font = ((XSSFCellStyle) cell.getCellStyle()).getFont();
        copyAttributes(font, str, 0, cellValue.length() - 1);
        LineBreakMeasurer measurer = new LineBreakMeasurer(str.getIterator(),
            fontRenderContext);

        int nextPos = 0;
        int lineCnt = 0;

        // TODO Support Merged columns
        int columnWidth = AbstractExcelUtils.getColumnWidthInPx(
            cell.getSheet().getColumnWidth(cell.getColumnIndex()));
        columnWidth -= columnWidth * securityMarginPercentage;
        
        while (measurer.getPosition() < cellValue.length()) {
            nextPos = measurer.nextOffset(columnWidth);
            lineCnt++;
            measurer.setPosition(nextPos);
        }
        return lineCnt;
    }

    private static boolean isWrapText(Cell cell) {
        CellStyle cellStyle = cell.getCellStyle();
        if (cellStyle == null) {
            return false;
        }
        return cellStyle.getWrapText();
    }

    private float getRequiredLineHeight(Font font, String text) {
        AttributedString str = new AttributedString(text);
        copyAttributes(font, str, 0, 1);
        TextLayout layout = new TextLayout(str.getIterator(),
            fontRenderContext);
        return layout.getAscent() + layout.getDescent();
    }

    /**
     * Copy text attributes from the supplied Font to Java2D AttributedString
     */
    private void copyAttributes(Font font, AttributedString str,
        int startIdx, int endIdx) {
        str.addAttribute(TextAttribute.FAMILY, font.getFontName(), startIdx,
            endIdx);
        str.addAttribute(TextAttribute.TRACKING, letterSpacingInPoints);
        str.addAttribute(TextAttribute.SIZE,
            (float) font.getFontHeightInPoints());
        if (font.getBold())
            str.addAttribute(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD,
                startIdx, endIdx);
        if (font.getItalic())
            str.addAttribute(TextAttribute.POSTURE,
                TextAttribute.POSTURE_OBLIQUE, startIdx, endIdx);
        if (font.getUnderline() == Font.U_SINGLE)
            str.addAttribute(TextAttribute.UNDERLINE,
                TextAttribute.UNDERLINE_ON, startIdx, endIdx);
    }

    private static Font getCellFont(Cell cell) {
        CellStyle cellStyle = cell.getCellStyle();
        if (cellStyle == null) {
            return null;
        }

        return cell.getSheet().getWorkbook()
            .getFontAt(cellStyle.getFontIndex());
    }
}
