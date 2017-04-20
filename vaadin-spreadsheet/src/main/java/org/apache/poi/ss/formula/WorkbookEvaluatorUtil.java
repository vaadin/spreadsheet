package org.apache.poi.ss.formula;

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

import org.apache.poi.ss.formula.eval.ValueEval;
import org.apache.poi.ss.formula.ptg.Ptg;
import org.apache.poi.ss.formula.ptg.RefPtgBase;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.BaseXSSFEvaluationWorkbook;
import org.apache.poi.xssf.usermodel.BaseXSSFFormulaEvaluator;

import com.vaadin.addon.spreadsheet.Spreadsheet;

/**
 * Helper to evaluate POI values that are package scoped, like formula Ptg[] arrays.
 */
public class WorkbookEvaluatorUtil {

    /**
     * Evaluate formula Ptg[] tokens
     */
    public static ValueEval evaluate(Spreadsheet spreadsheet, Ptg[] ptgs,
        Cell cell) {
        // allow for reuse of evaluation caches for performance - see POI #57840
        // for an example
        final WorkbookEvaluator workbookEvaluator = ((BaseXSSFFormulaEvaluator) spreadsheet.getFormulaEvaluator())._getWorkbookEvaluator();
        final OperationEvaluationContext ec = new OperationEvaluationContext(
            workbookEvaluator, workbookEvaluator.getWorkbook(),
            getSheetIndex(cell), cell.getRowIndex(), cell.getColumnIndex(),
            new EvaluationTracker(new EvaluationCache(null)));
        return workbookEvaluator.evaluateFormula(ec, ptgs);
    }

    public static BaseXSSFEvaluationWorkbook getEvaluationWorkbook(Spreadsheet spreadsheet) {
        return (BaseXSSFEvaluationWorkbook) ((BaseXSSFFormulaEvaluator) spreadsheet.getFormulaEvaluator())._getWorkbookEvaluator().getWorkbook();
    }

    private static int getSheetIndex(Cell cell) {
        Sheet sheet = cell.getSheet();
        return sheet.getWorkbook().getSheetIndex(sheet);
    }

    public static ValueEval evaluate(Spreadsheet spreadsheet, Cell cell,
        String formula, int deltaRow, int deltaColumn){
        // Parse formula and use deltas to get relative cell references to work
        // (#18702)
        Ptg[] ptgs = FormulaParser.parse(formula, WorkbookEvaluatorUtil.getEvaluationWorkbook(spreadsheet),
            FormulaType.CELL, spreadsheet.getActiveSheetIndex());

        for (Ptg ptg : ptgs) {
            // base class for cell reference "things"
            if (ptg instanceof RefPtgBase) {
                RefPtgBase ref = (RefPtgBase) ptg;
                // re-calculate cell references
                if (ref.isColRelative()) {
                    ref.setColumn(ref.getColumn() + deltaColumn);
                }
                if (ref.isRowRelative()) {
                    ref.setRow(ref.getRow() + deltaRow);
                }
            }
        }
        return evaluate(spreadsheet, ptgs, cell);
    }
}
