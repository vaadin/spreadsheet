/*
 * Vaadin Spreadsheet Addon
 *
 * Copyright (C) 2012-2024 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.addon.spreadsheet.test.testutil;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.util.CellRangeAddress;

import com.vaadin.addon.spreadsheet.Spreadsheet;
import com.vaadin.addon.spreadsheet.Spreadsheet.SelectionChangeEvent;
import com.vaadin.event.Action;

public class SpreadsheetActionHandler implements Action.Handler {

    private static final long serialVersionUID = 2L;

    private List<Cell> cellHandlers = new LinkedList<SpreadsheetActionHandler.Cell>();
    private List<Column> columnHandlers = new LinkedList<SpreadsheetActionHandler.Column>();
    private List<Row> rowHandlers = new LinkedList<SpreadsheetActionHandler.Row>();

    private Map<Integer, Cell> cellActionOwnership;
    private Map<Integer, Column> columnActionOwnership;
    private Map<Integer, Row> rowActionOwnership;

    public void addCellHandler(Cell cellHandler) {
        cellHandlers.add(cellHandler);
    }

    public void addColumnHandler(Column columnHandler) {
        columnHandlers.add(columnHandler);
    }

    public void addRowHandler(Row rowHandler) {
        rowHandlers.add(rowHandler);
    }

    @Override
    public Action[] getActions(Object target, Object sender) {
        if (!(sender instanceof Spreadsheet)) {
            throw new RuntimeException(
                    "The event was not generated by a spreadsheet object");
        }

        Spreadsheet spreadsheet = (Spreadsheet) sender;

        if (target instanceof SelectionChangeEvent) {
            LinkedList<Action> actions = new LinkedList<Action>();
            cellActionOwnership = new HashMap<Integer, SpreadsheetActionHandler.Cell>();
            for (Cell cellHandler : cellHandlers) {
                Action[] currentCellActions = cellHandler
                        .getActions((SelectionChangeEvent) target, spreadsheet);
                for (Action action : currentCellActions) {
                    cellActionOwnership.put(action.hashCode(), cellHandler);
                    actions.add(action);
                }
            }
            return actions.toArray(new Action[0]);
        }

        if (target instanceof CellRangeAddress) {
            CellRangeAddress cra = (CellRangeAddress) target;
            if (cra.isFullColumnRange()) {
                LinkedList<Action> actions = new LinkedList<Action>();
                columnActionOwnership = new HashMap<Integer, SpreadsheetActionHandler.Column>();
                for (Column columnHandler : columnHandlers) {
                    Action[] currentColumnActions = columnHandler
                            .getActions(cra, spreadsheet);
                    for (Action action : currentColumnActions) {
                        columnActionOwnership.put(action.hashCode(),
                                columnHandler);
                        actions.add(action);
                    }
                }
                return actions.toArray(new Action[0]);
            }

            if (cra.isFullRowRange()) {
                LinkedList<Action> actions = new LinkedList<Action>();
                rowActionOwnership = new HashMap<Integer, SpreadsheetActionHandler.Row>();
                for (Row rowHandler : rowHandlers) {
                    Action[] currentRowActions = rowHandler.getActions(cra,
                            spreadsheet);
                    for (Action action : currentRowActions) {
                        rowActionOwnership.put(action.hashCode(), rowHandler);
                        actions.add(action);
                    }
                }
                return actions.toArray(new Action[0]);
            }

            throw new RuntimeException("Header interaction not managed");
        }

        throw new RuntimeException("Action not handled");
    }

    @Override
    public void handleAction(Action action, Object sender, Object target) {
        if (!(sender instanceof Spreadsheet)) { // XXX - Wait for Pekka
            throw new RuntimeException(
                    "The event was not generated by a spreadsheet object");
        }
        Spreadsheet spreadsheet = (Spreadsheet) sender;

        if (target instanceof SelectionChangeEvent) {
            if (cellActionOwnership.containsKey(action.hashCode())) {
                cellActionOwnership.get(action.hashCode()).handleAction(action,
                        (SelectionChangeEvent) target, spreadsheet);
            }
            return;
        }

        if (target instanceof CellRangeAddress) {
            CellRangeAddress cra = (CellRangeAddress) target;
            if (cra.isFullColumnRange()) {
                if (columnActionOwnership.containsKey(action.hashCode())) {
                    columnActionOwnership.get(action.hashCode())
                            .handleAction(action, cra, spreadsheet);
                }
                return;
            }
            if (cra.isFullRowRange()) {
                if (rowActionOwnership.containsKey(action.hashCode())) {
                    rowActionOwnership.get(action.hashCode())
                            .handleAction(action, cra, spreadsheet);
                }
                return;
            }
            throw new RuntimeException("Header interaction not managed");
        }

        throw new RuntimeException("Action not handled");
    }

    public interface Cell {

        public Action[] getActions(SelectionChangeEvent target,
                Spreadsheet sender);

        public void handleAction(Action action, SelectionChangeEvent sender,
                Spreadsheet target);
    }

    public interface Column {

        public Action[] getActions(CellRangeAddress target, Spreadsheet sender);

        public void handleAction(Action action, CellRangeAddress sender,
                Spreadsheet target);
    }

    public interface Row {

        public Action[] getActions(CellRangeAddress target, Spreadsheet sender);

        public void handleAction(Action action, CellRangeAddress sender,
                Spreadsheet target);
    }

}
