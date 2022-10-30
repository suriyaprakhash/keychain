package com.suriya.data;

import java.util.Map;

public class KeyNode {
    private final Map<String, String> attributeMap;
    protected String entryName;

    public KeyNode(Map<String, String> attributeMap, String entryName) {
        this.attributeMap = attributeMap;
        this.entryName = entryName;
    }

    public Map<String, String> getAttributeMap() {
        return attributeMap;
    }

    public String getEntryName() {
        return entryName;
    }

}
