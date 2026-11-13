package com.sakalti.sakaplus.tinco.client;

import net.minecraft.item.ItemStack;

public class AssembledToolRenderer {

    public void debugPrint(ItemStack stack) {
        var tag = stack.getOrCreateTag();
        String head = tag.getString("head");
        String handle = tag.getString("handle");
        String extra = tag.getString("extra");

        String headMat = parseMaterial(head);
        String handleMat = parseMaterial(handle);
        String extraMat = parseMaterial(extra);

        int headColor = ToolColorGenerator.getColor(headMat);
        int handleColor = ToolColorGenerator.getColor(handleMat);
        int extraColor = ToolColorGenerator.getColor(extraMat);

        System.out.println("Head: " + headMat + " Color: " + Integer.toHexString(headColor));
        System.out.println("Handle: " + handleMat + " Color: " + Integer.toHexString(handleColor));
        System.out.println("Extra: " + extraMat + " Color: " + Integer.toHexString(extraColor));
    }

    private String parseMaterial(String partStr) {
        if (partStr == null) return "";
        String lower = partStr.toLowerCase();
        if (lower.endsWith("_handle")) return lower.substring(0, lower.length() - 7);
        if (lower.endsWith("_head")) return lower.substring(0, lower.length() - 5);
        if (lower.endsWith("_extra")) return lower.substring(0, lower.length() - 6);
        return lower;
    }
}
