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

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.formula.BaseFormulaEvaluator;
import org.apache.poi.ss.formula.ConditionalFormattingEvaluator;
import org.apache.poi.ss.formula.EvaluationConditionalFormatRule;
import org.apache.poi.ss.formula.eval.NotImplementedException;
import org.apache.poi.ss.usermodel.BorderFormatting;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * ConditionalFormatter is an utility class of Spreadsheet, which handles all
 * processing regarding Conditional Formatting rules.
 * <p>
 * Rules are parsed into CSS rules with individual class names. Class names for
 * each cell can then be fetched from this class.
 * <p>
 * For now, only XSSF formatting rules are supported because of bugs in POI.
 *
 * @author Thomas Mattsson / Vaadin Ltd.
 */
@SuppressWarnings("serial")
public class ConditionalFormatter implements Serializable {

    @SuppressWarnings("unused")
	private static final Logger LOGGER = Logger
            .getLogger(ConditionalFormatter.class.getName());

    /*
     * Slight hack. This style is used when a CF rule defines 'no border', in
     * which case the border should be empty. However, since we use cell DIV
     * borders for the grid structure, empty borders are in fact grey. So, if
     * one rule says red, and the next says no border, then we need to know what
     * 'no border' means in CSS. Of course, if the default CSS changes, this
     * needs to change too.
     */
    private static String BORDER_STYLE_DEFAULT = "1pt solid #d6d6d6;";

	/**
	 * starting index for conditional formatting CSS styles.  
	 * Should be high enough to avoid conflicts with other types. 
	 */
	public static final int BASE_CONDITIONAL_FORMAT_CSS_INDEX = 9000000;

	private Spreadsheet spreadsheet;


	/**
	 * cache cell CSS style ID lists (1:N), style ID# determines CSS priority order
	 */
	private Map<CellReference, Set<Integer>> cellToCssIndex = new HashMap<CellReference, Set<Integer>>();

    /**
     * Excel colors to CSS color definitions
     */
    protected ColorConverter colorConverter;

	/**
	 * Evaluator to cache formats and conditional evaluations
	 */
	private ConditionalFormattingEvaluator cfEvaluator;

	/**
	 * Stored here as a convenience, and because we need to notice changes to this
	 * from reloading a spreadsheet or any other path that could change it out from
	 * under the cfEvaluator.
	 */
	private FormulaEvaluator formulaEvaluator;
	
	/**
	 * common class for converting Excel formatting to CSS formatting.
	 * Used by both conditional formatting and table style formatting code.
	 */
	private IncrementalStyleBuilder styleBuilder;

    /**
     * Constructs a new ConditionalFormatter targeting the given Spreadsheet.
     *
     * @param spreadsheet
     *            Target spreadsheet
     */
    public ConditionalFormatter(Spreadsheet spreadsheet) {
        this.spreadsheet = spreadsheet;

        final Workbook workbook = spreadsheet.getWorkbook();
        if (workbook instanceof HSSFWorkbook) {
            colorConverter = new HSSFColorConverter((HSSFWorkbook) workbook);
        } else {
            colorConverter = new XSSFColorConverter((XSSFWorkbook) workbook);
        }

        // need to re-evaluate all styles when any value changes
        // - no way to predict dependencies
		spreadsheet.addCellValueChangeListener(e -> { if (cfEvaluator != null) cfEvaluator.clearAllCachedValues(); });

    }

	/**
	 * this may be called after the component workbook has been swapped out from under us.
	 * When that happens, the formula evaluator is recreated, so we can check for object equivalence.
	 * 
	 * @return true if the formula evaluator changed, and we have to reinitialize everything
	 */
	protected boolean init() {
		if (formulaEvaluator != spreadsheet.getFormulaEvaluator()) {
			formulaEvaluator = spreadsheet.getFormulaEvaluator();
			cfEvaluator = new ConditionalFormattingEvaluator(spreadsheet.getWorkbook(), (BaseFormulaEvaluator) formulaEvaluator);
			styleBuilder = new IncrementalStyleBuilder(spreadsheet, BORDER_STYLE_DEFAULT);
			return true;
		}
		return false;
	}
	
	/**
	 * @return the cfEvaluator
	 */
	public ConditionalFormattingEvaluator getConditionalFormattingEvaluator() {
		return cfEvaluator;
	}
	
	/**
	 * @return the styleBuilder
	 */
	public IncrementalStyleBuilder getStyleBuilder() {
		return styleBuilder;
	}

	/**
	 * @return the spreadsheet
	 */
	public Spreadsheet getSpreadsheet() {
		return spreadsheet;
	}
	
	/**
	 * this may be called after the component workbook has been swapped out from under us.
	 * When that happens, the formula evaluator is recreated, so we can check for object equivalence.
	 * <p>
	 * Conditional formats have multiple rules, with an order and possible "stop-if-true" logic.
	 * A single workbook can have multiple conditional formats.
	 * Everything is keyed by array index # in the Excel file formats, so we use those indexes
	 * as the basis for unique CSS ID values via a simple deterministic algorithm.  This way
	 * we don't have to track which CSS goes with which format and rule, we can recreate that
	 * if needed from the ID.
	 */
	public void createConditionalFormatterRules() {
        // make sure old styles are cleared
        if (cellToCssIndex != null) {
            for (CellReference key : cellToCssIndex.keySet()) {
                int col = key.getCol();
                int row = key.getRow();
                
                // note: this is for the active sheet!  If that has changed, it's the right coordinates
                // for the UI, but not the cell specified by the key!
                Cell cell = spreadsheet.getCell(row, col);
                if (cell != null) {
                    spreadsheet.markCellAsUpdated(cell, true);
                }
            }
        }

        cellToCssIndex.clear();
        
		if (! init()) {
			// rules already in place, but data may have changed, re-evaluate cells
			cfEvaluator.clearAllCachedValues();
			return;
		}
		
		// build rules properly for all sheets, but don't evaluate cells yet
		
		// remove previous styles, if any(should not be needed now that we do all sheets at once)
        spreadsheet.getState().conditionalFormattingStyles = new HashMap<Integer, String>();

        for (int s=0; s < spreadsheet.getWorkbook().getNumberOfSheets(); s++) {
        	buildStylesForSheet(spreadsheet.getWorkbook().getSheetAt(s));
        }
	}
	
	/**
	 * define the set of CSS rule indexes that apply to this cell
	 * @param cell 
	 * @return set of CSS rule IDs for applicable conditional formatting
	 */
	public Set<Integer> getCellFormattingIndex(Cell cell) {
		/*
		 * Why use Integer CSS IDs?  Looking at uses, there is no reason they can't be String instead.
		 * Or even an array of ints, so we can use sheet/row/column/border index arrays and return Set<int[]>
		 */
		if (cell == null) return Collections.emptySet();
		
		// calculate for cells to the right and below first, so this can have the proper border IDs if needed
		if (cell.getRowIndex() < cell.getSheet().getLastRowNum() && cell.getRowIndex() < SpreadsheetVersion.EXCEL2007.getLastRowIndex() -1) {
			getCellFormattingIndexInternal(new CellReference(cell.getSheet().getSheetName(), cell.getRowIndex() + 1, cell.getColumnIndex(), false, false));
		}
		if (cell.getColumnIndex() < SpreadsheetVersion.EXCEL2007.getLastColumnIndex() -1) {
			getCellFormattingIndexInternal(new CellReference(cell.getSheet().getSheetName(), cell.getRowIndex(), cell.getColumnIndex() + 1, false, false));
		}
		
		return getCellFormattingIndexInternal(new CellReference(cell.getSheet().getSheetName(), cell.getRowIndex(), cell.getColumnIndex(), false, false));
	}

	private Set<Integer> getCellFormattingIndexInternal(CellReference ref) {
		Set<Integer> styles = cellToCssIndex.get(ref);

		if (styles == null) try {
			// get the empty set in there now, so if there is an unimplemented function somewhere we don't have to hit it every time
			styles = new TreeSet<>();
			cellToCssIndex.put(ref, styles);
			
			List<EvaluationConditionalFormatRule> rules = cfEvaluator.getConditionalFormattingForCell(ref);
			if (rules == null) rules = Collections.emptyList();
			for (EvaluationConditionalFormatRule rule : rules) {
				styles.add(Integer.valueOf(getCssIndex(rule, IncrementalStyleBuilder.StyleType.BASE)));
				styles.add(Integer.valueOf(getCssIndex(rule, IncrementalStyleBuilder.StyleType.BOTTOM)));
				styles.add(Integer.valueOf(getCssIndex(rule, IncrementalStyleBuilder.StyleType.RIGHT)));
				
				// LEFT border CSS goes on the cell to the left!
				final BorderFormatting borderFormatting = rule.getRule().getBorderFormatting();
				if (borderFormatting != null) {
					if (borderFormatting.getBorderLeftEnum() != BorderStyle.NONE) {
						addBorderStyleId(getCssIndex(rule, IncrementalStyleBuilder.StyleType.LEFT), ref.getRow(), ref.getCol() - 1);
					}
					// TOP border CSS goes on the cell above!
					if (borderFormatting.getBorderTopEnum() != BorderStyle.NONE) {
						addBorderStyleId(getCssIndex(rule, IncrementalStyleBuilder.StyleType.TOP), ref.getRow() - 1, ref.getCol());
					}
				}
			}
		} catch (NotImplementedException e) {
			// treat formulas we can't evaluate as non-matches
			// TODO: should probably log this
			
		}
		
		return styles;
	}

	/**
	 * Adds the proper border style ID to the given cell.
	 * @param ruleCSSIndex
	 * @param row
	 * @param col
	 */
	protected void addBorderStyleId(int ruleCSSIndex, int row, int col) {
		if (row < 0 || col < 0) return; // out of bounds
		Set<Integer> styles = getCellFormattingIndexInternal(new CellReference(spreadsheet.getActiveSheet().getSheetName(), row, col, false, false));
		styles.add(new Integer(ruleCSSIndex));
	}
	
	/**
	 * @param sheet
	 */
	protected void buildStylesForSheet(Sheet sheet) {
		for (EvaluationConditionalFormatRule rule : cfEvaluator.getFormatRulesForSheet(sheet)) {
			styleBuilder.addStyleForRule(rule.getRule(), getCssIndex(rule, IncrementalStyleBuilder.StyleType.BASE));
		}
	}

	/**
	 * CSS index value is deterministic, so just calculate it as needed rather than keeping extra maps around
	 * @param rule
	 * @param type
	 * @return index for the given formatting, rule, and type
	 */
	protected int getCssIndex(EvaluationConditionalFormatRule rule, IncrementalStyleBuilder.StyleType type) {
		// each rule has 3 possible styles (StyleType).  Start at 9M just in case.
		return BASE_CONDITIONAL_FORMAT_CSS_INDEX
				+ spreadsheet.getWorkbook().getSheetIndex(rule.getSheet().getSheetName()) * 100000 // 899 sheets
				+ rule.getFormattingIndex() * 10000 // room for 99 formatting indexes per sheet (mostly 1:1 with regions)
				+ rule.getRuleIndex() * 10 // room for 999 rules per formatting (HSSF max 3, XSSF unlimited)
				+ type.ordinal(); // 0-2
	}
	
}
