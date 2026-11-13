package com.sakalti.sakaplus.tinco.client;

import java.util.HashMap;
import java.util.Map;

public class ToolColorGenerator {

    private static final Map<String, Integer> COLOR_MAP = new HashMap<>();
    static {
        COLOR_MAP.put("hachilite", 0xFFB3E5FC);   // 淡い水色
        COLOR_MAP.put("iron", 0xFFAAAAAA);        // グレー
        COLOR_MAP.put("diamond", 0xFF55FFFF);     // 水色
        COLOR_MAP.put("netherite", 0xFF554433);   // 濃茶色
    }

    public static int getColor(String partName) {
        return COLOR_MAP.getOrDefault(partName.toLowerCase(), 0xFFFFFFFF);
    }
}
