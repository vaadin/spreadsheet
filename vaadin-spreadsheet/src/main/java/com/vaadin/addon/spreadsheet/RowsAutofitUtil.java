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

/**
 * RowsAutofitUtil is an utility class of the Spreadsheet component used
 * to calculate proper height for autofitting rows.
 *
 * @author Vaadin Ltd.
 */
class RowsAutofitUtil {
    // Sample text reaching the maximum possible row height
    public static final String EXAMPLE_TEXT = "0g";
    // Since calculation of wrapped text is not so accurate
    // this amount of additional rows is taken into account to calculate
    // cell height
    public static final int SAFETY_ADDITIONAL_ROWS = 1;
    private static final FontRenderContext fontRenderContext = new FontRenderContext(
        null, true, true);
    // Minimum height (in points) of rows refitted by Excel
    private static final float MINIMUM_HEIGHT = 15.0f;

    public static void autoSizeRow(Sheet sheet, int row) {
        Row sheetRow = sheet.getRow(row);
        if (sheetRow == null) {
            return;
        }

        if (sheetRow.getFirstCellNum() == -1) {
            return;
        }

        float height = MINIMUM_HEIGHT;
        for (Cell cell : sheetRow) {
            Font font = getCellFont(cell);
            if (font != null) {
                height = Math.max(height,
                    getRequiredHeight(font, EXAMPLE_TEXT) * getLineCount(cell));
            }
        }

        sheetRow.setHeightInPoints(height);
    }

    private static int getLineCount(Cell cell) {
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

        AttributedString str = new AttributedString(cellValue);
        LineBreakMeasurer measurer = new LineBreakMeasurer(str.getIterator(),
            fontRenderContext);

        int nextPos = 0;
        int lineCnt = 0;

        // TODO Support Merged columns
        int columnWidth = AbstractExcelUtils.getColumnWidthInPx(
            cell.getSheet().getColumnWidth(cell.getColumnIndex()));
        while (measurer.getPosition() < cellValue.length()) {
            nextPos = measurer.nextOffset(columnWidth);
            lineCnt++;
            measurer.setPosition(nextPos);
        }
        return lineCnt + SAFETY_ADDITIONAL_ROWS;
    }

    private static boolean isWrapText(Cell cell) {
        CellStyle cellStyle = cell.getCellStyle();
        if (cellStyle == null) {
            return false;
        }
        return cellStyle.getWrapText();
    }

    private static float getRequiredHeight(Font font, String text) {
        AttributedString str = new AttributedString(text);
        copyAttributes(font, str, 0, 1);
        TextLayout layout = new TextLayout(str.getIterator(),
            fontRenderContext);
        return layout.getAscent() + layout.getDescent();
    }

    /**
     * Copy text attributes from the supplied Font to Java2D AttributedString
     */
    private static void copyAttributes(Font font, AttributedString str,
        int startIdx, int endIdx) {
        str.addAttribute(TextAttribute.FAMILY, font.getFontName(), startIdx,
            endIdx);
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
