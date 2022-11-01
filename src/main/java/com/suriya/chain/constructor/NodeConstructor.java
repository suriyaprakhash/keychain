package com.suriya.chain.constructor;

import com.suriya.chain.algorithm.Cryptography;
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
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.suriya.io.KeyChainSettings.General.*;
import static com.suriya.io.KeyChainSettings.Algorithm.*;

public class NodeConstructor {

    private List<ConstructorKeyNode> constructorKeyNodeList;
    private String starterNodeName;
    private String starterNodePassword;
    private PrivateKey privateKey;

    private NodeConstructor(PrivateKey privateKey) {}

    public static NodeConstructor initialize(List<KeyNode> keyNodeList) {
        NodeConstructor nodeConstructor = new NodeConstructor(null);
        nodeConstructor.constructorKeyNodeList =  keyNodeList.stream().map(keyNode ->  new ConstructorKeyNode(keyNode))
                .collect(Collectors.toList());
        return nodeConstructor;
    }

    public static NodeConstructor initialize(List<KeyNode> keyNodeList, PrivateKey privateKey) {
        NodeConstructor nodeConstructor = new NodeConstructor(privateKey);
        nodeConstructor.constructorKeyNodeList =  keyNodeList.stream().map(keyNode ->  new ConstructorKeyNode(keyNode))
                .collect(Collectors.toList());
        return nodeConstructor;
    }

    public String starterNodeName() {
        return starterNodeName;
    }

    public String startedNodePassword() {
        return starterNodePassword;
    }

    public Set<ConstructorKeyNode> build() {
        constructorKeyNodeList.stream().forEach(connectorKeyNode -> {
            // setting aliasEntryName
            connectorKeyNode.setEntryName(autoGenerateAliasNameIfMissing(connectorKeyNode.getEntryName()));
        });

        IntStream.range(0, constructorKeyNodeList.size()).forEach(index -> {
            ConstructorKeyNode constructorKeyNode = constructorKeyNodeList.get(index);

            // setting key
            Key key = SymmetricKey
                    .generateSecureRandomKey(KeyChainSettings.Algorithm.secureRandomKeyAlgorithm);
            constructorKeyNode.setSecureRandomKey(key);

            // setting attributeSet
            Set<KeyStore.Entry.Attribute> attributeSet = null;
            if (privateKey != null) { // encrypt if privateKey passed
                Map<String, String> encryptedAttributeMap = new HashMap<>();
                constructorKeyNode.getAttributeMap().forEach((mapKey, mapValue) -> {
                    encryptedAttributeMap.put(mapKey, new String(Cryptography.encrypt(cipherAlgorithm, this.privateKey,
                            mapValue.getBytes(StandardCharsets.UTF_8))));

                });
                attributeSet = gatherAttributeSet(encryptedAttributeMap);
            } else { // don't encrypt if privateKey not passed
                attributeSet = gatherAttributeSet(constructorKeyNodeList.get(index).getAttributeMap());
            }
            constructorKeyNode.setAttributeSet(attributeSet);


            KeyStore.Entry.Attribute nextKeyNodeNameAttribute = null;
            KeyStore.Entry.Attribute nextKeyNodePasswordAttribute = null;

            String nextPasswordHash = null;

            // setting attribute - nextPasswordHash = key + input attributeSet
            if (index != constructorKeyNodeList.size() - 1) { // skip the last index
                nextPasswordHash = generateNextPasswordHash(key.getEncoded(), attributeSet.toString()
                        .getBytes(StandardCharsets.UTF_8));
                if (privateKey != null) { // encrypt if privateKey passed
                    String encryptedNextPasswordHash = new String(Cryptography.encrypt(cipherAlgorithm, this.privateKey,
                            nextPasswordHash.getBytes(StandardCharsets.UTF_8)));
                    nextKeyNodeNameAttribute = AttributeParser.getAttributeFromKeyValue(nextKeyNodePasswordAttributeKey,
                            encryptedNextPasswordHash);
                } else { // don't encrypt if privateKey not passed
                    nextKeyNodeNameAttribute = AttributeParser.getAttributeFromKeyValue(nextKeyNodePasswordAttributeKey,
                            nextPasswordHash);
                }
                attributeSet.add(nextKeyNodeNameAttribute);
            }

            // setting attribute - nextNode
            if (index != constructorKeyNodeList.size() - 1) { // skip the last index
                String nextNodeName = constructorKeyNodeList.get(index + 1).getEntryName();
                if (privateKey != null) { // encrypt if privateKey passed
                    String encryptedNextNodeName = new String(Cryptography.encrypt(cipherAlgorithm, this.privateKey,
                            nextNodeName.getBytes(StandardCharsets.UTF_8)));
                    nextKeyNodePasswordAttribute = AttributeParser.getAttributeFromKeyValue(nextKeyNodeNameAttributeKey,
                            encryptedNextNodeName);
                } else {
                    nextKeyNodePasswordAttribute = AttributeParser.getAttributeFromKeyValue(nextKeyNodeNameAttributeKey,
                            nextNodeName);
                }
                attributeSet.add(nextKeyNodePasswordAttribute);
            }

            // setting generated hash password in the next node
            if (index != constructorKeyNodeList.size() - 1) {
                constructorKeyNodeList.get(index + 1).setPassword(nextPasswordHash);
            }

        });

        // set starter node
        starterNodeName = constructorKeyNodeList.get(0).getEntryName();

        // set starter node password
        this.starterNodePassword = new PasswordGenerator.Builder()
                .lower(2)
                .upper(2)
                .digits(1)
                .build().generate(keyStorePasswordLength);
        constructorKeyNodeList.get(0).setPassword(this.starterNodePassword);


        Set<ConstructorKeyNode> constructorKeyNodeSet = new HashSet<>(constructorKeyNodeList);
        return constructorKeyNodeSet;
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
