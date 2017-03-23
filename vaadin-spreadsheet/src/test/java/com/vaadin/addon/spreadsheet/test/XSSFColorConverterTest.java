package com.vaadin.addon.spreadsheet.test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Before;
import org.junit.Test;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColors;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRgbColor;

import com.vaadin.addon.spreadsheet.test.pageobjects.SpreadsheetPage;

public class XSSFColorConverterTest extends AbstractSpreadsheetTestCase {

    private static final String BACKGROUND_COLOR = "background-color";
    private XSSFWorkbook workbook;
    private SpreadsheetPage spreadsheetPage;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        InputStream is = getClass()
            .getResourceAsStream("/test_sheets/wrong_color.xlsx");

        workbook = new XSSFWorkbook(is);
        spreadsheetPage = headerPage.loadFile("wrong_color.xlsx", this);
    }

    @Test
    public void customIndexedColor_compareForegroundColor_consistentColors() throws IOException {
        XSSFCell cell = workbook.getSheetAt(1).getRow(0).getCell(0);
        XSSFColor color = cell.getCellStyle().getFillForegroundColorColor();
        String hexColor = getIndexedForegroundColor(color);

        assertNotNull(hexColor);

        String cssValue = spreadsheetPage.getCellAt(1, 1)
            .getCssValue(BACKGROUND_COLOR);

        assertNotNull(cssValue);

        assertEquals(cssValue, hexARGBtoRGBA(hexColor));

    }

    private String getIndexedForegroundColor(XSSFColor color) {
        if (color.isIndexed() && hasCustomIndexedColors()) {
            StylesTable styleSource = workbook.getStylesSource();

            CTRgbColor ctRgbColor = styleSource.getCTStylesheet().getColors()
                .getIndexedColors().getRgbColorList().get(color.getIndexed());

            String hexArgb = ctRgbColor.getDomNode().getAttributes()
                .getNamedItem("rgb").getNodeValue();
            return hexArgb;
        }

        return color.getARGBHex();
    }

    private boolean hasCustomIndexedColors() {
        StylesTable stylesSource = workbook.getStylesSource();
        CTColors ctColors = stylesSource.getCTStylesheet().getColors();
        if (ctColors == null) {
            return false;
        }
        if (ctColors.getIndexedColors() == null) {
            return false;
        }
        return true;
    }

    private String hexARGBtoRGBA(String hexARGB) {
        StringBuilder sb = new StringBuilder("rgba(");
        int rgba[] = new int[3];

        rgba[0] = Integer.parseInt(hexARGB.substring(2, 4), 16);
        rgba[1] = Integer.parseInt(hexARGB.substring(4, 6), 16);
        rgba[2] = Integer.parseInt(hexARGB.substring(6), 16);
        int alpha = Integer.parseInt(hexARGB.substring(0, 2), 16);

        if (alpha == 0.0) {
            alpha = 1;
        }
        sb.append(rgba[0]);
        sb.append(", ");
        sb.append(rgba[1]);
        sb.append(", ");
        sb.append(rgba[2]);
        sb.append(", ");
        sb.append(alpha);
        sb.append(")");

        return sb.toString();
    }

}