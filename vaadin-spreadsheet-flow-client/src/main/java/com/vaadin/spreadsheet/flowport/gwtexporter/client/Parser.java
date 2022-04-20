package com.vaadin.spreadsheet.flowport.gwtexporter.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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

    public static Set<Integer> parseSetInteger(String raw) {
        if ("null".equals(raw)) return null;
        List<String> tokens = parse(raw);
        Set<Integer> l = new HashSet<>();
        for (String token : tokens) {
            l.add(Integer.parseInt(token));
        }
        return l;
    }

    public static ArrayList<String> parseArraylistString(String raw) {
        if ("null".equals(raw)) return null;
        ArrayList<String> tokens = parse(raw);
        return tokens;
    }

    public static ArrayList<Integer> parseArraylistInteger(String raw) {
        if ("null".equals(raw)) return null;
        List<String> tokens = parse(raw);
        ArrayList<Integer> l = new ArrayList<>();
        for (String token : tokens) {
            l.add(Integer.parseInt(token));
        }
        return l;
    }

    public static int[] parseArrayInt(String raw) {
        if ("null".equals(raw)) return null;
        List<String> tokens = parse(raw);
        int[] l = new int[tokens.size()];
        for (int i = 0; i < tokens.size(); i++) {
            l[i] = Integer.parseInt(tokens.get(i));
        }
        return l;
    }

    public static float[] parseArrayFloat(String raw) {
        if ("null".equals(raw)) return null;
        List<String> tokens = parse(raw);
        float[] l = new float[tokens.size()];
        for (int i = 0; i < tokens.size(); i++) {
            l[i] = Float.parseFloat(tokens.get(i));
        }
        return l;
    }

    public static HashMap<String, String> parseMapStringString(String raw) {
        if ("null".equals(raw)) return null;
        List<String> tokens = parse(raw);
        HashMap<String, String> l = new HashMap<>();
        for (String token : tokens) {
            List<String> ts = parse(token, '@');
            l.put(ts.get(0), ts.get(1));
        }
        return l;
    }
    
    public static Set<String> parseSetString(String raw) {
        if ("null".equals(raw)) return null;
        List<String> tokens = parse(raw);
        Set<String> l = new HashSet<>();
        for (String token : tokens) {
            l.add(token);
        }
        return l;
    }
    
    public static Set<String> parseSetStringJs(String json) {
        return parseSet(json, String::new);
    }

    public static HashMap<String, OverlayInfo> parseMapStringOverlayInfoJs(String json) {
        return parseBeanMap(json, OverlayInfo::new);
    }

    public static ArrayList<MergedRegion> parseArrayMergedRegionJs(String json) {
        return parseBeanArray(json, MergedRegion::new);
    }
    
    public static ArrayList<CellData> parseArraylistOfCellDataJs(String json) {
        return parseBeanArray(json, CellData::new);
    }
    
    public static ArrayList<SpreadsheetActionDetails> parseArraylistSpreadsheetActionDetailsJs(String json) {
        return parseBeanArray(json, SpreadsheetActionDetails::new);
    }    

    native static void consoleLog(String message) /*-{
      console.log("parser", message );
    }-*/;
    
    private native static void copyJsToJava(JsonObject j, Object o) /*-{
      Object.assign(o, j);
    }-*/;
    
    private static <T> Set<T> parseSet(String json, Function<String, T> function) {
        return new HashSet<T>(parseArray(json, function));
    }
        
    private static <T> List<T> parseArray(String json, Function<String, T> function) {
        ArrayList<T> javaArr = new ArrayList<>();
        if (json == null || json.isEmpty() || "null".equals(json)) {
            return javaArr;
        }        
        JsonArray jsArr = JsonUtil.parse(json);
        for (int i = 0; i < jsArr.length(); i++) {
            String val = jsArr.getObject(i).asString();
            T javaObj = function.apply(val);
            javaArr.add(javaObj);
        }
        return javaArr;
    }
    
    private static <T> ArrayList<T> parseBeanArray(String json, Supplier<T> javaSupplier) {
        if (json == null || json.isEmpty() || "null".equals(json)) {
            return null;
        }
        JsonArray jsArr = JsonUtil.parse(json);
        ArrayList<T> javaArr = new ArrayList<>();
        for (int i = 0; i < jsArr.length(); i++) {
            JsonObject jsObj = jsArr.getObject(i);
            T javaObj = javaSupplier.get();
            copyJsToJava(jsObj, javaObj);
            javaArr.add(javaObj);
        }
        return javaArr;
    }

    private static <T> HashMap<String, T> parseBeanMap(String json, Supplier<T> javaSupplier) {
        if (json == null || json.isEmpty() || "null".equals(json)) {
            return null;
        }
        JsonObject jsObj = JsonUtil.parse(json);
        HashMap<String, T> hash = new HashMap<>();
        for (int i = 0; i < jsObj.keys().length; i++) {
            String key = jsObj.keys()[i];
            JsonObject jsBean = jsObj.getObject(key);
            T javaBean = javaSupplier.get();
            copyJsToJava(jsBean, javaBean);
            hash.put(key, javaBean);
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
