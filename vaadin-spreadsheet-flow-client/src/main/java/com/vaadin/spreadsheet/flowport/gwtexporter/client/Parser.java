package com.vaadin.spreadsheet.flowport.gwtexporter.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dev.util.collect.Lists;
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

    public static Map<String, PopupButtonState> parseListOfPopupButtons(String raw) {
        if ("null".equals(raw)) return null;
        Map<String, PopupButtonState> l = new HashMap<>();
        List<String> tokens = parse(raw);
        for (String token : tokens) {
            String[] ts = token.split("#");
            PopupButtonState s;
            l.put(ts[0], s = new PopupButtonState());
            s.active = Boolean.parseBoolean(ts[1]);
            s.col = Integer.parseInt(ts[2]);
            s.row = Integer.parseInt(ts[3]);
            s.active = Boolean.parseBoolean(ts[4]);
            s.sheet = ts[5];
            s.popupWidth = ts[6];
            s.popupHeight = ts[7];
        }
        return l;
    }

    public static List<GroupingData> parseListOfGroupingData(String raw) {
        if ("null".equals(raw)) return null;
        List<GroupingData> l = new ArrayList<>();
        List<String> tokens = parse(raw);
        for (String token : tokens) {
            String[] ts = token.split("#");
            l.add(new GroupingData(
                    Integer.parseInt(ts[0])
                    , Integer.parseInt(ts[1])
                    , Integer.parseInt(ts[2])
                    , Integer.parseInt(ts[3])
                    , Boolean.parseBoolean(ts[4])
            ));
        }
        return l;
    }

    public static String[] parseArrayOfStrings(String raw) {
        if ("null".equals(raw)) return null;
        List<String> tokens = parse(raw);
        String[] l = new String[tokens.size()];
        for (int i = 0; i < tokens.size(); i++) {
            l[i] = tokens.get(i);
        }
        return l;
    }

    public static HashMap<Integer, String> parseMapIntegerString(String raw) {
        if ("null".equals(raw)) return null;
        List<String> tokens = parse(raw);
        //consoleLog("received: " + raw);
        //tokens.forEach(s -> consoleLog("-->" + s));
        HashMap<Integer, String> l = new HashMap<>();
        for (String token : tokens) {
            List<String> ts = parse(token, '@');
            String v = ts.get(1);
            if (v == null || "null".equals(v)) v = null;
            else if (v.startsWith("\"")) v = ts.get(1).substring(1, ts.get(1).length() - 1);
            l.put(Integer.parseInt(ts.get(0)), v);
        }
        return l;
    }

    public static HashMap<Integer, Integer> parseMapIntegerInteger(String raw) {
        if ("null".equals(raw)) return null;
        List<String> tokens = parse(raw);
        HashMap<Integer, Integer> l = new HashMap<>();
        for (String token : tokens) {
            String[] ts = token.split("@");
            l.put(Integer.parseInt(ts[0]), Integer.parseInt(ts[1]));
        }
        return l;
    }

    public static Set<Integer> parseSetIntegerJs(String json) {
        return parseSet(json, val -> (int)val.asNumber());
    }

    public static ArrayList<String> parseArraylistStringJs(String json) {
        return parseArray(json, JsonValue::asString);
    }

    public static ArrayList<Integer> parseArraylistIntegerJs(String json) {
        return parseArray(json, a -> (int) a.asNumber());
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
        return parseMap(json, JsonValue::asString);
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

    private native static void copyJsToJava(JsonValue j, Object o) /*-{
      Object.assign(o, j);
    }-*/;

    private static <T> ArrayList<T> parseArrayJstype(String json, Supplier<T> jsTypeNew) {
        return parseArray(json, jsBean -> {
            T javaBean = jsTypeNew.get();
            copyJsToJava(jsBean, javaBean);
            return javaBean;
        });
    }

    private static <T> Set<T> parseSet(String json, Function<JsonValue, T> jsToJava) {
        List<T> ret = parseArray(json, jsToJava);
        return ret == null ? null : new HashSet<T>(ret);
    }

    private static <T> ArrayList<T> parseArray(String json, Function<JsonValue, T> jsToJava) {
        ArrayList<T> javaArr = new ArrayList<>();
        if (json == null || json.isEmpty() || "null".equals(json)) {
            return javaArr;
        }
        JsonArray jsArr = JsonUtil.parse(json);
        for (int i = 0; i < jsArr.length(); i++) {
            JsonValue val = jsArr.getObject(i);
            javaArr.add(jsToJava.apply(val));
        }
        return javaArr;
    }

    private static <T> HashMap<String, T> parseMapStringJstype(String json, Supplier<T> jsTypeNew) {
        return parseMap(json, jsBean -> {
            T javaBean = jsTypeNew.get();
            copyJsToJava(jsBean, javaBean);
            return javaBean;
        });
    }

    private static <T> HashMap<String, T> parseMap(String json, Function<JsonValue, T> jsToJava) {
        if (json == null || json.isEmpty() || "null".equals(json)) {
            return null;
        }
        JsonObject jsObj = JsonUtil.parse(json);
        HashMap<String, T> hash = new HashMap<>();
        for (int i = 0; i < jsObj.keys().length; i++) {
            String key = jsObj.keys()[i];
            JsonValue val = jsObj.getObject(key);
            hash.put(key, jsToJava.apply(val));
        }
        return hash;
    }

    private static ArrayList<String> parse(String payload) {
        return parse(payload, ',');
    }

    private static ArrayList<String> parse(String payload, char separator) {
        ArrayList<String> tokens = new ArrayList<>();
        if (payload != null) {
            int pos = 0;
            int start = 0;
            boolean hasNonString = false;
            boolean insideString = false;
            boolean escaped = false;
            while (pos < payload.length()) {
                if (!escaped && separator == payload.charAt(pos) && !insideString) {
                    if (pos > start) tokens.add(payload.substring(hasNonString?start:start + 1, hasNonString?pos:pos - 1).replaceAll("\\\\", ""));
                    else tokens.add("");
                    start = pos + 1;
                    hasNonString = false;
                } else if ('"' == payload.charAt(pos)) {
                    if (!escaped) {
                        if (insideString) { // end of string
                            insideString = false;
                        } else { // start of string
                            insideString = true;
                        }
                    } else {
                        escaped = false;
                    }
                } else if ('\\' == payload.charAt(pos)) {
                    escaped = true;
                } else {
                    if (escaped) escaped = false;
                    if (!insideString) hasNonString = true;
                }
                pos++;
            }
            if (pos > start) tokens.add(payload.substring(hasNonString?start:start + 1, hasNonString?pos:pos - 1).replaceAll("\\\\", ""));
        }
        return tokens;
    }

}
