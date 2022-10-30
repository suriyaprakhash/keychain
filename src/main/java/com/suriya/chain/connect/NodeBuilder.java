package com.suriya.chain.connect;

import com.suriya.chain.algorithm.Hash;
import com.suriya.chain.algorithm.SymmetricKey;
import com.suriya.chain.parser.AttributeParser;
import com.suriya.data.KeyNode;
import com.suriya.io.KeyChainSettings;
import com.suriya.util.PasswordGenerator;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyStore;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.suriya.io.KeyChainSettings.General.*;

public class NodeBuilder {

    private List<ConnectorKeyNode> connectorKeyNodeList;
    private String starterNodeName;
    private String starterNodePassword;

    private NodeBuilder() {}

    public static NodeBuilder initialize(List<KeyNode> keyNodeList) {
        NodeBuilder nodeBuilder = new NodeBuilder();
        nodeBuilder.connectorKeyNodeList =  keyNodeList.stream().map(keyNode ->  new ConnectorKeyNode(keyNode))
                .collect(Collectors.toList());
        return nodeBuilder;
    }

    public String starterNodeName() {
        return starterNodeName;
    }

    public String startedNodePassword() {
        return starterNodePassword;
    }

    public Set<ConnectorKeyNode> build() {
        connectorKeyNodeList.stream().forEach(connectorKeyNode -> {
            // setting aliasEntryName
            connectorKeyNode.setEntryName(autoGenerateAliasNameIfMissing(connectorKeyNode.getEntryName()));
        });

        IntStream.range(0, connectorKeyNodeList.size()).forEach(index -> {
            ConnectorKeyNode connectorKeyNode = connectorKeyNodeList.get(index);

            // setting key
            Key key = SymmetricKey
                    .generateSecureRandomKey(KeyChainSettings.Algorithm.secureRandomKeyAlgorithm);
            connectorKeyNode.setSecureRandomKey(key);

            // setting attributeSet
            Set<KeyStore.Entry.Attribute> attributeSet = gatherAttributeSet(connectorKeyNode.getAttributeMap());
            connectorKeyNode.setAttributeSet(attributeSet);

            KeyStore.Entry.Attribute nextKeyNodeNameAttribute = null;
            KeyStore.Entry.Attribute nextKeyNodePasswordAttribute = null;

            String nextPasswordHash = null;

            // setting attribute - nextPasswordHash = key + input attributeSet
            if (index != connectorKeyNodeList.size() - 1) { // skip the last index
                nextPasswordHash = generateNextPasswordHash(key.getEncoded(), attributeSet.toString()
                        .getBytes(StandardCharsets.UTF_8));
                nextKeyNodeNameAttribute = AttributeParser.getAttributeFromKeyValue(nextKeyNodePasswordAttributeKey,
                        nextPasswordHash);
                attributeSet.add(nextKeyNodeNameAttribute);
            }

            // setting attribute - nextNode
            if (index != connectorKeyNodeList.size() - 1) { // skip the last index
                String nextNodeName = connectorKeyNodeList.get(index + 1).getEntryName();
                nextKeyNodePasswordAttribute = AttributeParser.getAttributeFromKeyValue(nextKeyNodeNameAttributeKey,
                        nextNodeName);
                attributeSet.add(nextKeyNodePasswordAttribute);
            }

            // setting generated hash password in the next node
            if (index != connectorKeyNodeList.size() - 1) {
                connectorKeyNodeList.get(index + 1).setPassword(nextPasswordHash);
            }

        });

        // set starter node
        starterNodeName = connectorKeyNodeList.get(0).getEntryName();

        // set starter node password
        this.starterNodePassword = new PasswordGenerator.Builder()
                .lower(2)
                .upper(2)
                .digits(1)
                .build().generate(keyStorePasswordLength);
        connectorKeyNodeList.get(0).setPassword(this.starterNodePassword);


        Set<ConnectorKeyNode> connectorKeyNodeSet = new HashSet<>(connectorKeyNodeList);
        return connectorKeyNodeSet;
    }

    private String generateNextPasswordHash(byte[] encodedKeyByteArray, byte[] attributeMapByteArray) {
        ByteBuffer buff = ByteBuffer.allocate(encodedKeyByteArray.length + attributeMapByteArray.length);
        buff.put(encodedKeyByteArray);
        buff.put(attributeMapByteArray);
        return Hash.getHexStringFromByteArray(Hash.generateMessageDigest(KeyChainSettings.Algorithm
                .messageDigestAlgorithm, buff.array()));
    }

    private String autoGenerateAliasNameIfMissing(String entryAliasName) {
        if (entryAliasName == null || (entryAliasName != null && entryAliasName.length() == 0)) {
            entryAliasName = new PasswordGenerator.Builder()
                    .lower()
                    .build().generate(entryAliasNameLength);
        }
        return entryAliasName;
    }

    private Set<KeyStore.Entry.Attribute> gatherAttributeSet(Map<String,String> attributeMap) {
        Set<KeyStore.Entry.Attribute> attributeSet = null;

        // if attributeMap exists
        if (attributeMap != null && attributeMap.size() > 0) {
            attributeSet = AttributeParser
                    .populateAttributeSetFromMap(attributeMap);
        }
        return attributeSet;
    }
}
