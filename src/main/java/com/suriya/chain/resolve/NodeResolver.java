package com.suriya.chain.resolve;

import com.suriya.chain.algorithm.Cryptography;
import com.suriya.chain.parser.AttributeParser;
import com.suriya.chain.parser.ByteProcessor;

import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.PublicKey;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.suriya.io.KeyChainSettings.General.*;
import static com.suriya.io.KeyChainSettings.Algorithm.*;

public class NodeResolver {

    private KeyStore keyStore;

    public static NodeResolver initialize(KeyStore keyStore) {
        NodeResolver nodeResolver = new NodeResolver();
        nodeResolver.keyStore = keyStore;
        return nodeResolver;
    }

    public ResolverKeyNode resolve(String entryName, String entryPassword, Set<String> attributeMapKeySet) {
        return resolve(entryName, entryPassword, attributeMapKeySet, null);
    }

    public ResolverKeyNode resolve(String entryName, String entryPassword, Set<String> attributeMapKeySet, PublicKey
                                   publicKey) {

        // read entry
        KeyStore.Entry entry = ByteProcessor.readKeyStoreEntryOfSecretKeyFromKeyStore(this.keyStore, entryName,
                entryPassword);
        // get attributes
        Set<KeyStore.Entry.Attribute> attributeSet = entry.getAttributes();


        Map<String, String> attributeMap = null;
        // setting attribute map
        if (publicKey != null) { // if expecting encrypted value
            attributeMap = AttributeParser.populateAttributeMapFromSet(attributeSet,
                    attributeMapKeySet).entrySet().stream().collect(Collectors.toMap(mapEntry -> mapEntry.getKey(),
                    mapEntry -> new String(Cryptography.decrypt(cipherAlgorithm, publicKey, mapEntry.getValue()
                            .getBytes(StandardCharsets.UTF_8)))));
        } else { // if expecting encrypted value
            attributeMap = AttributeParser.populateAttributeMapFromSet(attributeSet,
                    attributeMapKeySet);
        }

        // gather next key name and password
        Set<String> nextNodeAttributeSet = new HashSet<>();
        nextNodeAttributeSet.add(nextKeyNodeNameAttributeKey);
        nextNodeAttributeSet.add(nextKeyNodePasswordAttributeKey);

        Map<String, String> nextNodeMap = null;
        if (publicKey != null) { // if expecting encrypted value
            nextNodeMap = AttributeParser.populateAttributeMapFromSet(attributeSet,
                    nextNodeAttributeSet).entrySet().stream().collect(Collectors.toMap(mapEntry -> mapEntry.getKey(),
                    mapEntry -> new String(Cryptography.decrypt(cipherAlgorithm, publicKey, mapEntry.getValue()
                            .getBytes(StandardCharsets.UTF_8)))));
        } else {// if not expecting encrypted value
            nextNodeMap = AttributeParser.populateAttributeMapFromSet(attributeSet,
                    nextNodeAttributeSet);
        }

        // create resolver node
        ResolverKeyNode resolverKeyNode = new ResolverKeyNode(attributeMap, entryName, nextNodeMap.get(nextKeyNodeNameAttributeKey),
                nextNodeMap.get(nextKeyNodePasswordAttributeKey));

        return resolverKeyNode;
    }
}
