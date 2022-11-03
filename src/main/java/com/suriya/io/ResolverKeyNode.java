package com.suriya.io;

import com.suriya.io.KeyNode;

import java.util.Map;

/**
 * See {@link KeyNode} for the parent class information.
 *
 * This class provides additional properties to used during resolving the Key Chain
 */
public final class ResolverKeyNode extends KeyNode {

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
