/*
 * Vaadin Spreadsheet Addon
 *
 * Copyright (C) 2013-2026 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.addon.spreadsheet.test.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.junit.Before;
import org.junit.Test;

import com.vaadin.addon.spreadsheet.Spreadsheet;
import com.vaadin.addon.spreadsheet.Spreadsheet.CellValueChangeEvent;
import com.vaadin.addon.spreadsheet.Spreadsheet.ProtectedEditEvent;
import com.vaadin.addon.spreadsheet.SpreadsheetHandlerImpl;

public class LockedCellTest {

    private Spreadsheet spreadsheet;
    private SpreadsheetHandlerImpl handler;

    @Before
    public void setUp() {
        spreadsheet = new Spreadsheet();
        spreadsheet.setLocale(Locale.US);
        new TestableUI(spreadsheet);
        handler = new SpreadsheetHandlerImpl(spreadsheet);
    }

    @Test
    public void lockSheet_receiveCellValueEditedEvent_preventsEdit() {
        Cell cell = spreadsheet.createCell(1, 1, "Initial value");
        lockSheet();

        AtomicReference<ProtectedEditEvent> protectedEditEvent = new AtomicReference<>();
        spreadsheet.addProtectedEditListener(protectedEditEvent::set);

        AtomicReference<CellValueChangeEvent> cellValueChangeEvent = new AtomicReference<>();
        spreadsheet.addCellValueChangeListener(cellValueChangeEvent::set);

        spreadsheet.setSelection("B2");
        fireCellValueEditedEvent(2, 2, "Updated value");

        assertNotNull(protectedEditEvent.get());
        assertNull(cellValueChangeEvent.get());
        assertEquals("Initial value", cell.getStringCellValue());
    }

    @Test
    public void lockSheet_unlockCell_receiveCellValueEditedEvent_allowsEdit() {
        Cell cell = spreadsheet.createCell(1, 1, "Initial value");
        lockSheet();
        unlockCell("B2");

        AtomicReference<ProtectedEditEvent> protectedEditEvent = new AtomicReference<>();
        spreadsheet.addProtectedEditListener(protectedEditEvent::set);

        AtomicReference<CellValueChangeEvent> cellValueChangeEvent = new AtomicReference<>();
        spreadsheet.addCellValueChangeListener(cellValueChangeEvent::set);

        fireCellValueEditedEvent(2, 2, "Updated value");

        assertNull(protectedEditEvent.get());
        assertNotNull(cellValueChangeEvent.get());
        assertEquals("Updated value", cell.getStringCellValue());
    }

    @Test
    public void lockSheet_receiveUpdateCellCommentEvent_preventsEdit() {
        Cell cell = spreadsheet.createCell(1, 1, "Initial value");
        addComment(cell, "Initial comment");
        lockSheet();

        AtomicReference<ProtectedEditEvent> protectedEditEvent = new AtomicReference<>();
        spreadsheet.addProtectedEditListener(protectedEditEvent::set);

        fireProtectedCellWriteAttempted();

        assertNotNull(protectedEditEvent.get());
        assertEquals("Initial comment", cell.getCellComment().getString().getString());
    }

    @Test
    public void lockSheet_receiveUpdateCellCommentEventForMissingCell_doesNotCreateCell() {
        lockSheet();

        AtomicReference<ProtectedEditEvent> protectedEditEvent = new AtomicReference<>();
        spreadsheet.addProtectedEditListener(protectedEditEvent::set);

        fireProtectedCellWriteAttempted();

        assertNotNull(protectedEditEvent.get());
        assertNull(spreadsheet.getActiveSheet().getRow(4));
    }

    @Test
    public void lockSheet_unlockCell_receiveUpdateCellCommentEvent_allowsEdit() {
        Cell cell = spreadsheet.createCell(1, 1, "Initial value");
        addComment(cell, "Initial comment");
        lockSheet();
        unlockCell("B2");

        AtomicReference<ProtectedEditEvent> protectedEditEvent = new AtomicReference<>();
        spreadsheet.addProtectedEditListener(protectedEditEvent::set);

        fireUpdateCellCommentEvent(2, 2, "Updated comment");

        assertNull(protectedEditEvent.get());
        assertNotNull(cell.getCellComment());
        assertEquals("Updated comment", cell.getCellComment().getString().getString());
    }

    private void fireCellValueEditedEvent(int row, int col, String value) {
        spreadsheet.setSelection("B2");
        handler.cellValueEdited(row, col, value);
    }

    private void fireUpdateCellCommentEvent(int row, int col, String text) {
        handler.updateCellComment(text, col, row);
    }

    private void fireProtectedCellWriteAttempted() {
        handler.protectedCellWriteAttempted();
    }

    private void lockSheet() {
        spreadsheet.getActiveSheet().protectSheet("password");
    }

    private void unlockCell(String cellAddress) {
        spreadsheet.createCell(0, 0, "");
        CellStyle cellStyle = spreadsheet.getActiveSheet().getWorkbook()
                .createCellStyle();
        cellStyle.setLocked(false);
        spreadsheet.getCell(cellAddress).setCellStyle(cellStyle);
    }

    private void addComment(Cell cell, String text) {
        CreationHelper factory = spreadsheet.getActiveSheet().getWorkbook()
                .getCreationHelper();
        Drawing<?> drawing = spreadsheet.getActiveSheet().createDrawingPatriarch();
        ClientAnchor anchor = factory.createClientAnchor();
        anchor.setCol1(cell.getColumnIndex());
        anchor.setCol2(cell.getColumnIndex() + 1);
        anchor.setRow1(cell.getRowIndex());
        anchor.setRow2(cell.getRowIndex() + 3);

        Comment comment = drawing.createCellComment(anchor);
        comment.setString(factory.createRichTextString(text));
        comment.setAuthor("Spreadsheet User");
        cell.setCellComment(comment);
    }
}
