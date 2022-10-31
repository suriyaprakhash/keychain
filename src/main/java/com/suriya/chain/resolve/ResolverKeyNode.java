package com.suriya.chain.resolve;

import com.suriya.data.KeyNode;

import java.util.Map;

public class ResolverKeyNode extends KeyNode {

    private String nextNodeName;
    private String nextNodePassword;

    public ResolverKeyNode(Map<String, String> attributeMap, String entryName, String nextNodeName,
                           String nextNodePassword) {
        super(attributeMap, entryName);
        this.nextNodeName = nextNodeName;
        this.nextNodePassword = nextNodePassword;
    }

    public String getNextNodeName() {
        return nextNodeName;
    }

    public String getNextNodePassword() {
        return nextNodePassword;
    }
}
