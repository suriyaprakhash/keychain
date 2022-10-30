package com.suriya.chain.connect;

import com.suriya.chain.algorithm.SymmetricKey;
import com.suriya.chain.parser.AttributeParser;
import com.suriya.data.KeyNode;
import com.suriya.io.KeyChainSettings;
import com.suriya.util.PasswordGenerator;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class NodeBuilder {

    private int ENTRY_ALIAS_NAME_LENGTH = 10;

    private List<ConnectorKeyNode> connectorKeyNodeList;
    private List<String> keyNodeEntryOrderList;

    public List<String> getKeyNodeEntryOrderList() {
        return keyNodeEntryOrderList;
    }

    private NodeBuilder() {}

    public NodeBuilder initialize(List<KeyNode> keyNodeList) {
        NodeBuilder nodeBuilder = new NodeBuilder();
        nodeBuilder.connectorKeyNodeList =  keyNodeList.stream().map(keyNode -> {
            return (ConnectorKeyNode) keyNode;
        }).collect(Collectors.toList());
        return nodeBuilder;
    }

    public Set<KeyNode> build() {
        Set<KeyNode> keyNodeSet = new HashSet<>();
        keyNodeEntryOrderList = new ArrayList<>();

        connectorKeyNodeList.stream().forEach(keyNode -> {

            String entryAliasName = keyNode.getEntryName();
            if (entryAliasName == null || (entryAliasName != null && entryAliasName.length() == 0)) {
                entryAliasName = new PasswordGenerator.Builder()
                        .lower()
                        .build().generate(ENTRY_ALIAS_NAME_LENGTH);
            }

            keyNodeEntryOrderList.add(entryAliasName);
        });
        return null;
    }

    private void generateKey(KeyNode keyNode) {
        Key key = SymmetricKey.generateSecureRandomKey(KeyChainSettings.Algorithm.secureRandomKeyAlgorithm);
        byte[] attributeSetByteArray = null;

        // if attributeMap exists
        if (keyNode.getAttributeMap() != null && keyNode.getAttributeMap().size() > 0) {
            Set<KeyStore.Entry.Attribute> attributeSet = AttributeParser
                    .populateAttributeSetFromMap(keyNode.getAttributeMap());
            attributeSetByteArray = attributeSet.toString().getBytes(StandardCharsets.UTF_8);
        }

    }
}
