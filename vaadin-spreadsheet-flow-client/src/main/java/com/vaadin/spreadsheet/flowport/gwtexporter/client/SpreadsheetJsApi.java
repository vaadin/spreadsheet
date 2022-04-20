package com.vaadin.spreadsheet.flowport.gwtexporter.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.RootPanel;
import com.vaadin.addon.spreadsheet.client.MUtil;
import com.vaadin.addon.spreadsheet.client.PopupButtonConnector;
import com.vaadin.addon.spreadsheet.client.PopupButtonState;
import com.vaadin.addon.spreadsheet.client.PopupButtonWidget;
import com.vaadin.addon.spreadsheet.client.SpreadsheetClientRpc;
import com.vaadin.addon.spreadsheet.client.SpreadsheetConnector;
import com.vaadin.addon.spreadsheet.client.SpreadsheetServerRpc;
import com.vaadin.addon.spreadsheet.client.SpreadsheetWidget;
import com.vaadin.addon.spreadsheet.shared.SpreadsheetState;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.metadata.ConnectorBundleLoader;
import com.vaadin.client.metadata.TypeDataStore;

import elemental.json.Json;
import elemental.json.JsonObject;
import jsinterop.annotations.JsType;

/**
 *
 * this is the public api which we will export to js
 *
 */
@JsType
public class SpreadsheetJsApi {

    public SpreadsheetWidget spreadsheetWidget;
    protected SpreadsheetConnector spreadsheetConnector;
    Map<String, PopupButtonWidget> popupButtonWidgets = new HashMap<>();
    Map<String, PopupButtonConnector> popupButtonConnectors = new HashMap<>();
    Map<String, PopupButtonState> popupButtonStates = new HashMap<>();
    protected ApplicationConnection applicationConnection;
    private String originalStyles;

    native void consoleLog(String message) /*-{
      console.log("spreadsheetapi", message );
    }-*/;

    native void debugger() /*-{
      debugger;
    }-*/;

    /**
     * receives the element where the widget mut be embedded into, and publishes
     * the methods which can be used from js
     *
     * @param element
     */
    public SpreadsheetJsApi(Element element) {
        if (element != null) {
           init(element);
        }
    }

    private void init(Element element) {
        // Only support eager connectors for now
        ConnectorBundleLoader.get()
                .loadBundle(ConnectorBundleLoader.EAGER_BUNDLE_NAME, null);

        applicationConnection = new ApplicationConnection();
        spreadsheetConnector = new SpreadsheetConnector();
        spreadsheetConnector.doInit("1", new ApplicationConnection());
        spreadsheetWidget = spreadsheetConnector.getWidget();

        // esto es para evitar el bundle
        TypeDataStore.get().setClass(spreadsheetConnector.getClass().getName(), SpreadsheetConnector.class);

        RootPanel.getForElement(element).add(spreadsheetWidget);
    }

    public void disconnected() {
        if (spreadsheetConnector != null) {
            spreadsheetConnector.onUnregister();
        }
    }

    private void delegateToWidget(SpreadsheetConnector connector, StateChangeEvent sce) {
        for (String propertyName : new String[] {
        "rowBufferSize",
        "columnBufferSize",
        "rows",
        "cols",
        "colGroupingData",
        "rowGroupingData",
        "colGroupingMax",
        "rowGroupingMax",
        "colGroupingInversed",
        "rowGroupingInversed",
        "defRowH",
        "defColW",
        "rowH",
        "colW",
        "cellStyleToCSSStyle",
        "rowIndexToStyleIndex",
        "columnIndexToStyleIndex",
        "lockedColumnIndexes",
        "lockedRowIndexes",
        "shiftedCellBorderStyles",
        "conditionalFormattingStyles",
        "hiddenColumnIndexes",
        "hiddenRowIndexes",
        "verticalScrollPositions",
        "horizontalScrollPositions",
        "workbookProtected",
        "hyperlinksTooltips",
        "displayGridlines",
        "displayRowColHeadings",
        "verticalSplitPosition",
        "horizontalSplitPosition",
        "infoLabelValue",
        "invalidFormulaErrorMessage",
        "lockFormatColumns",
        "lockFormatRows",
                "namedRanges",

            "height",
                "width",
                "description",
                "descriptionContentMode",
                "caption",
                "styles",
                "id",
                "primaryStyleName",
                "errorMessage",
                "captionAsHtml",
                "tabIndex",
                "enabled"
        }) {
            if (sce.isInitialStateChange() || sce.hasPropertyChanged(propertyName)) {
                SpreadsheetWidget w = connector.getWidget();
                SpreadsheetState s = getState();
                if ("rowBufferSize".equals(propertyName)) w.setRowBufferSize(s.rowBufferSize);
                if ("columnBufferSize".equals(propertyName)) w.setColumnBufferSize(s.columnBufferSize);
                if ("rows".equals(propertyName)) w.setRows(s.rows);
                if ("cols".equals(propertyName)) w.setCols(s.cols);
                if ("colGroupingData".equals(propertyName)) w.setColGroupingData(s.colGroupingData);
                if ("rowGroupingData".equals(propertyName)) w.setRowGroupingData(s.rowGroupingData);
                if ("colGroupingMax".equals(propertyName)) w.setColGroupingMax(s.colGroupingMax);
                if ("rowGroupingMax".equals(propertyName)) w.setRowGroupingMax(s.rowGroupingMax);
                if ("colGroupingInversed".equals(propertyName)) w.setColGroupingInversed(s.colGroupingInversed);
                if ("rowGroupingInversed".equals(propertyName)) w.setRowGroupingInversed(s.rowGroupingInversed);
                if ("defRowH".equals(propertyName)) w.setDefRowH(s.defRowH);
                if ("defColW".equals(propertyName)) w.setDefColW(s.defColW);
                if ("rowH".equals(propertyName)) w.setRowH(s.rowH);
                if ("colW".equals(propertyName)) w.setColW(s.colW);
                if ("cellStyleToCSSStyle".equals(propertyName)) w.setCellStyleToCSSStyle(s.cellStyleToCSSStyle);
                if ("rowIndexToStyleIndex".equals(propertyName)) w.setRowIndexToStyleIndex(s.rowIndexToStyleIndex);
                if ("columnIndexToStyleIndex".equals(propertyName)) w.setColumnIndexToStyleIndex(s.columnIndexToStyleIndex);
                if ("lockedColumnIndexes".equals(propertyName)) w.setLockedColumnIndexes(s.lockedColumnIndexes);
                if ("lockedRowIndexes".equals(propertyName)) w.setLockedRowIndexes(s.lockedRowIndexes);
                if ("shiftedCellBorderStyles".equals(propertyName)) w.setShiftedCellBorderStyles(s.shiftedCellBorderStyles);
                if ("conditionalFormattingStyles".equals(propertyName)) w.setConditionalFormattingStyles(s.conditionalFormattingStyles);
                if ("hiddenColumnIndexes".equals(propertyName)) w.setHiddenColumnIndexes(s.hiddenColumnIndexes);
                if ("hiddenRowIndexes".equals(propertyName)) w.setHiddenRowIndexes(s.hiddenRowIndexes);
                if ("verticalScrollPositions".equals(propertyName)) w.setVerticalScrollPositions(s.verticalScrollPositions);
                if ("horizontalScrollPositions".equals(propertyName)) w.setHorizontalScrollPositions(s.horizontalScrollPositions);
                if ("workbookProtected".equals(propertyName)) w.setWorkbookProtected(s.workbookProtected);
                if ("hyperlinksTooltips".equals(propertyName)) w.setHyperlinksTooltips(s.hyperlinksTooltips);
                if ("displayGridlines".equals(propertyName)) w.setDisplayGridlines(s.displayGridlines);
                if ("displayRowColHeadings".equals(propertyName)) w.setDisplayRowColHeadings(s.displayRowColHeadings);
                if ("verticalSplitPosition".equals(propertyName)) w.setVerticalSplitPosition(s.verticalSplitPosition);
                if ("horizontalSplitPosition".equals(propertyName)) w.setHorizontalSplitPosition(s.horizontalSplitPosition);
                if ("infoLabelValue".equals(propertyName)) w.setInfoLabelValue(s.infoLabelValue);
                if ("invalidFormulaErrorMessage".equals(propertyName)) w.setInvalidFormulaErrorMessage(s.invalidFormulaErrorMessage);
                if ("lockFormatColumns".equals(propertyName)) w.setLockFormatColumns(s.lockFormatColumns);
                if ("lockFormatRows".equals(propertyName)) w.setLockFormatRows(s.lockFormatRows);
                if ("namedRanges".equals(propertyName)) w.setNamedRanges(s.namedRanges);

                if ("height".equals(propertyName)) w.setHeight(s.height);
                if ("width".equals(propertyName)) w.setWidth(s.width);
                if ("id".equals(propertyName)) w.setId(s.id);
            }
        }
    }

    public void layout() {
        spreadsheetConnector.getLayoutManager().layoutNow();
        spreadsheetWidget.relayoutSheet();
        spreadsheetConnector.postLayout();
    }

    public void resize() {
        spreadsheetWidget.widgetSizeChanged();
    }

    public void relayout() {
        Scheduler.get().scheduleDeferred(() -> {
            //spreadsheetWidget.getSheetWidget().ensureCustomStyleTagsAreInTheRightShadowRoot();
            spreadsheetWidget.relayoutSheet();
        });
    }

    private SpreadsheetServerRpcImpl getServerRpcInstance() {
        return (SpreadsheetServerRpcImpl) spreadsheetConnector.getProtectedRpcProxy(SpreadsheetServerRpc.class);
    }

    private SpreadsheetClientRpc getClientRpcInstance() {
        String rpcInterfaceId = SpreadsheetClientRpc.class.getName().replaceAll("\\$", ".");
        return (SpreadsheetClientRpc) spreadsheetConnector.getRpcImplementations(rpcInterfaceId).iterator().next();
    }

    /*
    SHARED STATE
     */
    protected SpreadsheetState getState() {
        return spreadsheetConnector.getState();
    }

    public void setRowBufferSize(int rowBufferSize) {
        getState().rowBufferSize = rowBufferSize;
    }

    public void setColumnBufferSize(int columnBufferSize) {
        getState().columnBufferSize = columnBufferSize;
    }

    public void setRows(int rows) {
        getState().rows = rows;
    }

    public void setCols(int cols) {
        getState().cols = cols;
    }

    public void setColGroupingData(String colGroupingData) {
        getState().colGroupingData = Parser.parseListOfGroupingData(colGroupingData);
    }

    public void setRowGroupingData(String rowGroupingData) {
        getState().rowGroupingData = Parser.parseListOfGroupingData(rowGroupingData);
    }

    public void setColGroupingMax(int colGroupingMax) {
        getState().colGroupingMax = colGroupingMax;
    }

    public void setRowGroupingMax(int rowGroupingMax) {
        getState().rowGroupingMax = rowGroupingMax;
    }

    public void setColGroupingInversed(boolean colGroupingInversed) {
        getState().colGroupingInversed = colGroupingInversed;
    }

    public void setRowGroupingInversed(boolean rowGroupingInversed) {
        getState().rowGroupingInversed = rowGroupingInversed;
    }

    public void setDefRowH(float defRowH) {
        getState().defRowH = defRowH;
    }

    public void setDefColW(int defColW) {
        getState().defColW = defColW;
    }

    public void setRowH(String rowHJson) {
        getState().rowH = Parser.parseArrayFloatJs(rowHJson);
    }

    public void setColW(String colWJson) {
        getState().colW = Parser.parseArrayIntJs(colWJson);
    }

    public void setReload(boolean reload) {
        getState().reload = true;
    }

    public void setSheetIndex(int sheetIndex) {
        getState().sheetIndex = sheetIndex;
    }

    public void setSheetNames(String sheetNames) {
        getState().sheetNames = Parser.parseArrayOfStrings(sheetNames);
    }

    public void setCellStyleToCSSStyle(String cellStyleToCSSStyle) {
        getState().cellStyleToCSSStyle = Parser.parseMapIntegerString(cellStyleToCSSStyle);
    }

    public void setRowIndexToStyleIndex(String rowIndexToStyleIndex) {
        getState().rowIndexToStyleIndex = Parser.parseMapIntegerInteger(rowIndexToStyleIndex);
    }

    public void setColumnIndexToStyleIndex(String columnIndexToStyleIndex) {
        getState().columnIndexToStyleIndex = Parser.parseMapIntegerInteger(columnIndexToStyleIndex);
    }

    public void setLockedColumnIndexes(String lockedColumnIndexesJson) {
        getState().lockedColumnIndexes = Parser.parseSetIntegerJs(lockedColumnIndexesJson);
    }

    public void setLockedRowIndexes(String lockedRowIndexesJson) {
        getState().lockedRowIndexes = Parser.parseSetIntegerJs(lockedRowIndexesJson);
    }

    public void setShiftedCellBorderStyles(String shiftedCellBorderStylesJson) {
        getState().shiftedCellBorderStyles = Parser.parseArraylistStringJs(shiftedCellBorderStylesJson);
    }

    public void setConditionalFormattingStyles(String conditionalFormattingStyles) {
        getState().conditionalFormattingStyles = Parser.parseMapIntegerString(conditionalFormattingStyles);
    }

    public void setHiddenColumnIndexes(String hiddenColumnIndexesJson) {
        getState().hiddenColumnIndexes = Parser.parseArraylistIntegerJs(hiddenColumnIndexesJson);
    }

    public void setHiddenRowIndexes(String hiddenRowIndexesJson) {
        getState().hiddenRowIndexes = Parser.parseArraylistIntegerJs(hiddenRowIndexesJson);
    }

    public void setVerticalScrollPositions(String verticalScrollPositionsJson) {
        getState().verticalScrollPositions = Parser.parseArrayIntJs(verticalScrollPositionsJson);
    }

    public void setHorizontalScrollPositions(String horizontalScrollPositionsJson) {
        getState().horizontalScrollPositions = Parser.parseArrayIntJs(horizontalScrollPositionsJson);
    }

    public void setSheetProtected(boolean sheetProtected) {
        getState().sheetProtected = sheetProtected;
    }

    public void setWorkbookProtected(boolean workbookProtected) {
        getState().workbookProtected = workbookProtected;
    }

    public void setCellKeysToEditorIdMap(String cellKeysToEditorIdMapJson) {
        getState().cellKeysToEditorIdMap = Parser.parseMapStringStringJs(cellKeysToEditorIdMapJson);
    }

    public void setComponentIDtoCellKeysMap(String componentIDtoCellKeysMapJson) {
        getState().componentIDtoCellKeysMap = Parser.parseMapStringStringJs(componentIDtoCellKeysMapJson);
    }

    public void setHyperlinksTooltips(String hyperlinksTooltipsJson) {
        getState().hyperlinksTooltips = Parser.parseMapStringStringJs(hyperlinksTooltipsJson);
    }

    public void setCellComments(String cellCommentsJsonJson) {
        getState().cellComments = Parser.parseMapStringStringJs(cellCommentsJsonJson);
    }

    public void setCellCommentAuthors(String cellCommentAuthorsJson) {
        getState().cellCommentAuthors = Parser.parseMapStringStringJs(cellCommentAuthorsJson);
    }

    public void setVisibleCellComments(String visibleCellCommentsJson) {
        getState().visibleCellComments = Parser.parseArraylistStringJs(visibleCellCommentsJson);
    }

    public void setInvalidFormulaCells(String invalidFormulaCells) {
        getState().invalidFormulaCells = Parser.parseSetStringJs(invalidFormulaCells);
    }

    public void setHasActions(boolean hasActions) {
        getState().hasActions = hasActions;
    }

    public void setOverlays(String overlaysJson) {
        getState().overlays = Parser.parseMapStringOverlayInfoJs(overlaysJson);
    }

    public void setMergedRegions(String mergedRegionsJson) {
        getState().mergedRegions = Parser.parseArrayMergedRegionJs(mergedRegionsJson);
    }

    public void setDisplayGridlines(boolean displayGridlines) {
        getState().displayGridlines = displayGridlines;
    }

    public void setDisplayRowColHeadings(boolean displayRowColHeadings) {
        getState().displayRowColHeadings = displayRowColHeadings;
    }

    public void setVerticalSplitPosition(int verticalSplitPosition) {
        getState().verticalSplitPosition = verticalSplitPosition;
    }

    public void setHorizontalSplitPosition(int horizontalSplitPosition) {
        getState().horizontalSplitPosition = horizontalSplitPosition;
    }

    public void setInfoLabelValue(String infoLabelValue) {
        getState().infoLabelValue = infoLabelValue;
    }

    public void setWorkbookChangeToggle(boolean workbookChangeToggle) {
        getState().workbookChangeToggle = workbookChangeToggle;
    }

    public void setInvalidFormulaErrorMessage(String invalidFormulaErrorMessage) {
        getState().invalidFormulaErrorMessage = invalidFormulaErrorMessage;
    }

    public void setLockFormatColumns(boolean lockFormatColumns) {
        getState().lockFormatColumns = lockFormatColumns;
    }

    public void setLockFormatRows(boolean lockFormatRows) {
        getState().lockFormatRows = lockFormatRows;
    }

    public void setNamedRanges(String namedRangesJson) {
        getState().namedRanges = Parser.parseArraylistStringJs(namedRangesJson);
    }

    public void setHeight(String height) {
        getState().height = height;
    }

    public void setWidth(String width) {
        getState().width = width;
    }

    public void setId(String id) {
        getState().id = id;
    }

    private String[] serverClasses = new String[0];

    public void setClass(String classNames) {
        // Server does not remove styles, it updates the entire attribute
        // with the classes that it manages.
        // Because the client also uses classes for certain features like
        // in DisplayGridlines, we store a cache with classes sets by server
        // for removing it in next iteration.

        // Remove all classes set by server in previous request
        for (String c : serverClasses) {
            spreadsheetWidget.removeStyleName(c);
        }
        // Cache classes for next time
        serverClasses = classNames.split(" ");
        // Set the new classes
        for (String c : serverClasses) {
            spreadsheetWidget.addStyleName(c);
        }
    }

    public void setPopups(String raw) {
        Map<String, PopupButtonState> l = Parser.parseListOfPopupButtons(raw);
        l.keySet().forEach(k -> {
            if (popupButtonWidgets.containsKey(k)) {
                consoleLog("popup already exists");
            } else {
                consoleLog("adding popup widget");
                PopupButtonWidget w;
                popupButtonWidgets.put(k, w = new PopupButtonWidget());
                PopupButtonConnector c;
                popupButtonConnectors.put(k, c = new PopupButtonConnector());
                PopupButtonState s;
                popupButtonStates.put(k, s = l.get(k));
                w.setCol(s.col);
                w.setRow(s.row);
                w.setPopupHeaderHidden(s.headerHidden);
                w.setSheetWidget(spreadsheetWidget.getSheetWidget(), DivElement.as(spreadsheetWidget.getSheetWidget().getElement()));
                w.setPopupWidth(s.popupWidth);
                w.setPopupHeight(s.popupHeight);
                spreadsheetWidget.addPopupButton(w);
            }
        });
    }

    public void setResources(Element element, String resources) {
        ArrayList<String> l = Parser.parseArraylistStringJs(resources);
        l.forEach(k -> spreadsheetConnector.getConnection().setResource(k, element.getAttribute("resource-" + k)));
    }

    public void notifyStateChanges(String[] propNames, boolean initial) {
        MUtil.console("NOTIFY Changes");
        JsonObject stateJson = Json.createObject();
        for (String propName : propNames) stateJson.put(propName, "");
        StateChangeEvent event = new StateChangeEvent(spreadsheetConnector, stateJson, initial);
        delegateToWidget(spreadsheetConnector, event);
        spreadsheetConnector.onStateChanged(event);
    }

    /* CLIENT RPC METHODS */

    public void updateBottomRightCellValues(String cellDataJson) {
        getClientRpcInstance().updateBottomRightCellValues(Parser.parseArraylistOfCellDataJs(cellDataJson));
    }

    public void updateTopLeftCellValues(String cellDataJson) {
        getClientRpcInstance().updateTopLeftCellValues(Parser.parseArraylistOfCellDataJs(cellDataJson));
    }

    public void updateTopRightCellValues(String cellDataJson) {
        getClientRpcInstance().updateTopRightCellValues(Parser.parseArraylistOfCellDataJs(cellDataJson));
    }

    public void updateBottomLeftCellValues(String cellDataJson) {
        getClientRpcInstance().updateBottomLeftCellValues(Parser.parseArraylistOfCellDataJs(cellDataJson));
    }

    public void updateFormulaBar(String possibleName, int col, int row) {
        getClientRpcInstance().updateFormulaBar(possibleName, col, row);
    }

    public void invalidCellAddress() {
        getClientRpcInstance().invalidCellAddress();
    }

    public void showSelectedCell(String name, int col, int row, String cellValue, boolean formula, boolean locked, boolean initialSelection) {
        getClientRpcInstance().showSelectedCell(name, col, row, cellValue, formula, locked, initialSelection);
    }

    public void showActions(String actionDetailsJson) {
        getClientRpcInstance().showActions(Parser.parseArraylistSpreadsheetActionDetailsJs(actionDetailsJson));
    }

    public void setSelectedCellAndRange(String name, int col, int row, int c1, int c2, int r1, int r2, boolean scroll) {
        getClientRpcInstance().setSelectedCellAndRange(name, col, row, c1, c2, r1, r2, scroll);
    }

    public void cellsUpdated(String cellDataJson) {
        getClientRpcInstance().cellsUpdated(Parser.parseArraylistOfCellDataJs(cellDataJson));
    }

    public void refreshCellStyles() {
        Scheduler.get().scheduleDeferred(() -> {
            getClientRpcInstance().refreshCellStyles();
        });
    }

    public void editCellComment(int col, int row) {
        // On a new comment, server creates the comment setting the author but
        // properties are updated after actions, thus, executing this
        // asynchronously fixes it (#790)
        Scheduler.get().scheduleDeferred(() -> {
            getClientRpcInstance().editCellComment(col, row);
        });
    }

    /*
    SERVER RPC METHOD CALLBACKS
     */
    public void setGroupingCollapsedCallback(Consumer<String> callback) {
        getServerRpcInstance().setGroupingCollapsedCallback(callback);
    }

    public void setLevelHeaderClickedCallback(Consumer<String> callback) {
        getServerRpcInstance().setLevelHeaderClickedCallback(callback);
    }

    public void setOnSheetScrollCallback(Consumer<String> callback) {
        getServerRpcInstance().setOnSheetScrollCallback(callback);
    }

    public void setSheetAddressChangedCallback(Consumer<String> callback) {
        getServerRpcInstance().setSheetAddressChangedCallback(callback);
    }

    public void setCellSelectedCallback(Consumer<String> callback) {
        getServerRpcInstance().setCellSelectedCallback(callback);
    }

    public void setCellRangeSelectedCallback(Consumer<String> callback) {
        getServerRpcInstance().setCellRangeSelectedCallback(callback);
    }

    public void setCellAddedToSelectionAndSelectedCallback(Consumer<String> callback) {
        getServerRpcInstance().setCellAddedToSelectionAndSelectedCallback(callback);
    }

    public void setCellsAddedToRangeSelectionCallback(Consumer<String> callback) {
        getServerRpcInstance().setCellsAddedToRangeSelectionCallback(callback);
    }

    public void setRowSelectedCallback(Consumer<String> callback) {
        getServerRpcInstance().setRowSelectedCallback(callback);
    }

    public void setRowAddedToRangeSelectionCallback(Consumer<String> callback) {
        getServerRpcInstance().setRowAddedToRangeSelectionCallback(callback);
    }

    public void setColumnSelectedCallback(Consumer<String> callback) {
        getServerRpcInstance().setColumnSelectedCallback(callback);
    }

    public void setColumnAddedToSelectionCallback(Consumer<String> callback) {
        getServerRpcInstance().setColumnAddedToSelectionCallback(callback);
    }

    public void setSelectionIncreasePaintedCallback(Consumer<String> callback) {
        getServerRpcInstance().setSelectionIncreasePaintedCallback(callback);
    }


    public void setSelectionDecreasePaintedCallback(Consumer<String> callback) {
        getServerRpcInstance().setSelectionDecreasePaintedCallback(callback);
    }

    public void setCellValueEditedCallback(Consumer<String> callback) {
        getServerRpcInstance().setCellValueEditedCallback(callback);
    }

    public void setSheetSelectedCallback(Consumer<String> callback) {
        getServerRpcInstance().setSheetSelectedCallback(callback);
    }

    public void setSheetRenamedCallback(Consumer<String> callback) {
        getServerRpcInstance().setSheetRenamedCallback(callback);
    }

    public void setSheetCreatedCallback(Consumer<String> callback) {
        getServerRpcInstance().setSheetCreatedCallback(callback);
    }

    public void setCellRangePaintedCallback(Consumer<String> callback) {
        getServerRpcInstance().setCellRangePaintedCallback(callback);
    }

    public void setDeleteSelectedCellsCallback(Consumer<String> callback) {
        getServerRpcInstance().setDeleteSelectedCellsCallback(callback);
    }

    public void setLinkCellClickedCallback(Consumer<String> callback) {
        getServerRpcInstance().setLinkCellClickedCallback(callback);
    }

    public void setRowsResizedCallback(Consumer<String> callback) {
        getServerRpcInstance().setRowsResizedCallback(callback);
    }

    public void setColumnResizedCallback(Consumer<String> callback) {
        getServerRpcInstance().setColumnResizedCallback(callback);
    }

    public void setOnRowAutofitCallback(Consumer<Integer> callback) {
        getServerRpcInstance().setOnRowAutofitCallback(callback);
    }

    public void setOnColumnAutofitCallback(Consumer<Integer> callback) {
        getServerRpcInstance().setOnColumnAutofitCallback(callback);
    }

    public void setOnUndoCallback(Runnable callback) {
        getServerRpcInstance().setOnUndoCallback(callback);
    }

    public void setOnRedoCallback(Runnable callback) {
        getServerRpcInstance().setOnRedoCallback(callback);
    }

    public void setSetCellStyleWidthRatiosCallback(Consumer<String> callback) {
        getServerRpcInstance().setSetCellStyleWidthRatiosCallback(callback);
    }

    public void setProtectedCellWriteAttemptedCallback(Runnable callback) {
        getServerRpcInstance().setProtectedCellWriteAttemptedCallback(callback);
    }

    public void setOnPasteCallback(Consumer<String> callback) {
        getServerRpcInstance().setOnPasteCallback(callback);
    }

    public void setClearSelectedCellsOnCutCallback(Runnable callback) {
        getServerRpcInstance().setClearSelectedCellsOnCutCallback(callback);
    }

    public void setUpdateCellCommentCallback(Consumer<String> callback) {
        getServerRpcInstance().setUpdateCellCommentCallback(callback);
    }

    public void setOnConnectorInitCallback(Runnable callback) {
        getServerRpcInstance().setOnConnectorInitCallback(callback);
    }

    public void setContextMenuOpenOnSelectionCallback(Consumer<String> callback) {
        getServerRpcInstance().setContextMenuOpenOnSelectionCallback(callback);
    }

    public void setActionOnCurrentSelectionCallback(Consumer<String> callback) {
        getServerRpcInstance().setActionOnCurrentSelectionCallback(callback);
    }

    public void setRowHeaderContextMenuOpenCallback(Consumer<Integer> callback) {
        getServerRpcInstance().setRowHeaderContextMenuOpenCallback(callback);
    }

    public void setActionOnRowHeaderCallback(Consumer<String> callback) {
        getServerRpcInstance().setActionOnRowHeaderCallback(callback);
    }

    public void setColumnHeaderContextMenuOpenCallback(Consumer<Integer> callback) {
        getServerRpcInstance().setColumnHeaderContextMenuOpenCallback(callback);
    }

    public void setActionOnColumnHeaderCallback(Consumer<String> callback) {
        getServerRpcInstance().setActionOnColumnHeaderCallback(callback);
    }


    public void load() {
        spreadsheetWidget.load();
    }

    public void relayoutSheet() {
        spreadsheetWidget.relayoutSheet();
    }

    public void updateCellsAndRefreshCellStyles() {
    }

}
