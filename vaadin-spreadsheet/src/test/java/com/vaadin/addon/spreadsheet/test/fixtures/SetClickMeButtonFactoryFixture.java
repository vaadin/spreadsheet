package com.vaadin.addon.spreadsheet.test.fixtures;

import com.vaadin.addon.spreadsheet.Spreadsheet;
import com.vaadin.addon.spreadsheet.SpreadsheetComponentFactory;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;

public class SetClickMeButtonFactoryFixture implements SpreadsheetFixture {

    @Override
    public void loadFixture(Spreadsheet spreadsheet) {
        spreadsheet.setSpreadsheetComponentFactory(new ClickMeButtonFactory());
    }

    public class ClickMeButtonFactory implements SpreadsheetComponentFactory {
        @Override
        public Component getCustomComponentForCell(Cell cell, final int rowIndex, final int columnIndex,
                                                   Spreadsheet spreadsheet, Sheet sheet) {
            if (cell != null && cell.getCellTypeEnum() == CellType.STRING
                    && cell.getStringCellValue().equals("Click Me")) {

                Button button = new Button("Click Me");
                button.addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent e) {
                        Notification.show(String.format(
                                "Clicked the button at row %1$d column %2$d", rowIndex, columnIndex));
                    }
                });
                button.setWidth("100%");
                return button;
            } else {
                return null;
            }
        }

        @Override
        public Component getCustomEditorForCell(Cell cell, int rowIndex,
                                                int columnIndex, Spreadsheet spreadsheet, Sheet sheet) {
            return null;
        }

        @Override
        public void onCustomEditorDisplayed(Cell cell, int rowIndex,
                                            int columnIndex, Spreadsheet spreadsheet, Sheet sheet,
                                            Component customEditor) {
        }
    }
}
