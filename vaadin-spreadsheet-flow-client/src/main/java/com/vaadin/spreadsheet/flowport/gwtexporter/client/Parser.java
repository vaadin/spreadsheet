package com.vaadin.spreadsheet.flowport.gwtexporter.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import com.vaadin.addon.spreadsheet.client.CellData;
import com.vaadin.addon.spreadsheet.client.MergedRegion;
import com.vaadin.addon.spreadsheet.client.OverlayInfo;
import com.vaadin.addon.spreadsheet.client.PopupButtonState;
import com.vaadin.addon.spreadsheet.client.SpreadsheetActionDetails;
import com.vaadin.addon.spreadsheet.shared.GroupingData;

import elemental.json.JsonArray;
import elemental.json.JsonObject;
import elemental.json.JsonValue;
import elemental.json.impl.JsonUtil;

public class Parser {

    public static List<PopupButtonState> parseListOfPopupButtonsJs(String json) {
        return parseArrayJstype(json, PopupButtonState::new);
    }

    public static List<GroupingData> parseListOfGroupingDataJs(String json){
        return parseArrayJstype(json, GroupingData::new);
    }

    public static String[] parseArrayOfStringsJs(String json) {
        return parseArray(json, JsonValue::asString).toArray(new String[0]);
    }

    public static HashMap<Integer, String> parseMapIntegerStringJs(String json) {
        return parseMap(json, Integer::valueOf, JsonValue::asString);
    }

    public static HashMap<Integer, Integer> parseMapIntegerIntegerJs(String json) {
        return parseMap(json, Integer::valueOf, v -> (int)v.asNumber());
    }

    public static Set<Integer> parseSetIntegerJs(String json) {
        return parseSet(json, v -> (int)v.asNumber());
    }

    public static ArrayList<String> parseArraylistStringJs(String json) {
        return parseArray(json, JsonValue::asString);
    }

    public static ArrayList<Integer> parseArraylistIntegerJs(String json) {
        return parseArray(json, v -> (int) v.asNumber());
    }

    public static float[] parseArrayFloatJs(String json) {
        double[] arr = parseArrayDoubleJs(json);
        float [] ret = new float[arr.length];
        for (int i = 0; i < arr.length; i++) {
            ret[i] = (float)arr[i];
        }
        return ret;
    }

    public static int[] parseArrayIntJs(String json) {
        return parseArray(json, JsonValue::asNumber).stream().mapToInt(Double::intValue).toArray();
    }

    public static double[] parseArrayDoubleJs(String json) {
        return parseArray(json, JsonValue::asNumber).stream().mapToDouble(Double::doubleValue).toArray();
    }

    public static HashMap<String, String> parseMapStringStringJs(String json) {
        return parseMap(json, String::valueOf, JsonValue::asString);
    }

    public static Set<String> parseSetStringJs(String json) {
        return parseSet(json, JsonValue::asString);
    }

    public static HashMap<String, OverlayInfo> parseMapStringOverlayInfoJs(String json) {
        return parseMapStringJstype(json, OverlayInfo::new);
    }

    public static ArrayList<MergedRegion> parseArrayMergedRegionJs(String json) {
        return parseArrayJstype(json, MergedRegion::new);
    }

    public static ArrayList<CellData> parseArraylistOfCellDataJs(String json) {
        return parseArrayJstype(json, CellData::new);
    }

    public static ArrayList<SpreadsheetActionDetails> parseArraylistSpreadsheetActionDetailsJs(String json) {
        return parseArrayJstype(json, SpreadsheetActionDetails::new);
    }

    private static <T> Set<T> parseSet(String json, Function<JsonValue, T> jsToJava) {
        List<T> ret = parseArray(json, jsToJava);
        return ret == null ? null : new HashSet<T>(ret);
    }

    private static <T> ArrayList<T> parseArrayJstype(String json, Supplier<T> constructor) {
        return parseArray(json, jsBean -> {
            T javaBean = constructor.get();
            copyJsToJava(jsBean, javaBean);
            return javaBean;
        });
    }

    private static <T> ArrayList<T> parseArray(String json, Function<JsonValue, T> jsToJava) {
        ArrayList<T> javaArr = new ArrayList<>();
        if (json == null || json.isEmpty() || "null".equals(json)) {
            return javaArr;
        }
        JsonArray jsArr = JsonUtil.parse(json);
        for (int i = 0; i < jsArr.length(); i++) {
            JsonValue val = jsArr.get(i);
            javaArr.add(jsToJava.apply(val));
        }
        return javaArr;
    }

    private static <T> HashMap<String, T> parseMapStringJstype(String json, Supplier<T> constructor) {
        return parseMap(json, String::valueOf, jsBean -> {
            T javaBean = constructor.get();
            copyJsToJava(jsBean, javaBean);
            return javaBean;
        });
    }

    private static <I, T> HashMap<I, T> parseMap(String json, Function<String, I> strToKey, Function<JsonValue, T> jsToJava) {
        if (json == null || json.isEmpty() || "null".equals(json)) {
            return null;
        }
        JsonObject jsObj = JsonUtil.parse(json);
        HashMap<I, T> hash = new HashMap<>();
        for (int i = 0; i < jsObj.keys().length; i++) {
            String key = jsObj.keys()[i];
            JsonValue val = jsObj.get(key);
            hash.put(strToKey.apply(key), jsToJava.apply(val));
        }
        return hash;
    }

    private native static void copyJsToJava(JsonValue j, Object o) /*-{
      Object.assign(o, j);
    }-*/;
}
