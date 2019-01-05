package com.vaadin.addon.spreadsheet.shared;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.vaadin.addon.spreadsheet.client.MergedRegion;
import com.vaadin.addon.spreadsheet.client.OverlayInfo;
import com.vaadin.shared.annotations.DelegateToWidget;
import com.vaadin.shared.ui.TabIndexState;

/**
 * SpreadsheetState
 */
@SuppressWarnings("serial")
public class SpreadsheetState extends TabIndexState {

    /**
     * rowBufferSize
     */
    @DelegateToWidget
    public int rowBufferSize = 200;

    /**
     * columnBufferSize
     */
    @DelegateToWidget
    public int columnBufferSize = 200;

    /**
     * rows
     */
    @DelegateToWidget
    public int rows;

    /**
     * cols
     */
    @DelegateToWidget
    public int cols;

    /**
     * colGroupingData
     */
    @DelegateToWidget
    public List<GroupingData> colGroupingData;
    /**
     * rowGroupingData
     */
    @DelegateToWidget
    public List<GroupingData> rowGroupingData;

    /**
     * colGroupingMax
     */
    @DelegateToWidget
    public int colGroupingMax;
    /**
     * rowGroupingMax
     */
    @DelegateToWidget
    public int rowGroupingMax;

    /**
     * colGroupingInversed
     */
    @DelegateToWidget
    public boolean colGroupingInversed;
    /**
     * rowGroupingInversed
     */
    @DelegateToWidget
    public boolean rowGroupingInversed;

    /**
     * defRowH
     */
    @DelegateToWidget
    public float defRowH;
    /**
     * defColW
     */
    @DelegateToWidget
    public int defColW;

    /**
     * rowH
     */
    @DelegateToWidget
    public float[] rowH;
    /**
     * colW
     */
    @DelegateToWidget
    public int[] colW;

    /** should the sheet be reloaded on client side */
    public boolean reload;

    /** 1-based */
    public int sheetIndex = 1;

    /**
     * sheetNames
     */
    public String[] sheetNames = null;

    /**
     * cellStyleToCSSStyle
     */
    @DelegateToWidget
    public HashMap<Integer, String> cellStyleToCSSStyle = null;
    /**
     * rowIndexToStyleIndex
     */
    @DelegateToWidget
    public HashMap<Integer, Integer> rowIndexToStyleIndex = null;
    /**
     * columnIndexToStyleIndex
     */
    @DelegateToWidget
    public HashMap<Integer, Integer> columnIndexToStyleIndex = null;
    /**
     * lockedColumnIndexes
     */
    @DelegateToWidget
    public Set<Integer> lockedColumnIndexes = null;
    /**
     * lockedRowIndexes
     */
    @DelegateToWidget
    public Set<Integer> lockedRowIndexes = null;

    /**
     * shiftedCellBorderStyles
     */
    @DelegateToWidget
    public ArrayList<String> shiftedCellBorderStyles = null;

    /**
     * All conditional formatting styles for this sheet.
     */
    @DelegateToWidget
    public HashMap<Integer, String> conditionalFormattingStyles = null;

    /** 1-based */
    @DelegateToWidget
    public ArrayList<Integer> hiddenColumnIndexes = null;

    /** 1-based */
    @DelegateToWidget
    public ArrayList<Integer> hiddenRowIndexes = null;

    /**
     * verticalScrollPositions
     */
    @DelegateToWidget
    public int[] verticalScrollPositions;

    /**
     * horizontalScrollPositions
     */
    @DelegateToWidget
    public int[] horizontalScrollPositions;

    /**
     * sheetProtected
     */
    public boolean sheetProtected;

    /**
     * workbookProtected
     */
    @DelegateToWidget
    public boolean workbookProtected;

    /**
     * cellKeysToEditorIdMap
     */
    public HashMap<String, String> cellKeysToEditorIdMap;

    /**
     * componentIDtoCellKeysMap
     */
    public HashMap<String, String> componentIDtoCellKeysMap;

    // Cell CSS key to link tooltip (usually same as address)
    /**
     * hyperlinksTooltips
     */
    @DelegateToWidget
    public HashMap<String, String> hyperlinksTooltips;

    /**
     * cellComments
     */
    public HashMap<String, String> cellComments;
    /**
     * cellCommentAuthors
     */
    public HashMap<String, String> cellCommentAuthors;

    /**
     * visibleCellComments
     */
    public ArrayList<String> visibleCellComments;

    /**
     * invalidFormulaCells
     */
    public Set<String> invalidFormulaCells;

    /**
     * hasActions
     */
    public boolean hasActions;

    /**
     * overlays
     */
    public HashMap<String, OverlayInfo> overlays;

    /**
     * mergedRegions
     */
    public ArrayList<MergedRegion> mergedRegions;

    /**
     * displayGridlines
     */
    @DelegateToWidget
    public boolean displayGridlines = true;

    /**
     * displayRowColHeadings
     */
    @DelegateToWidget
    public boolean displayRowColHeadings = true;

    /**
     * verticalSplitPosition
     */
    @DelegateToWidget
    public int verticalSplitPosition = 0;
    /**
     * horizontalSplitPosition
     */
    @DelegateToWidget
    public int horizontalSplitPosition = 0;

    /**
     * infoLabelValue
     */
    @DelegateToWidget
    public String infoLabelValue;

    /**
     * workbookChangeToggle
     */
    public boolean workbookChangeToggle;

    /**
     * invalidFormulaErrorMessage
     */
    @DelegateToWidget
    public String invalidFormulaErrorMessage = "Invalid formula";

    /**
     * lockFormatColumns
     */
    @DelegateToWidget
    public boolean lockFormatColumns = true;

    /**
     * lockFormatRows
     */
    @DelegateToWidget
    public boolean lockFormatRows = true;

    /**
     * namedRanges
     */
    @DelegateToWidget
    public List<String> namedRanges;
}
