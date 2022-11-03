package com.suriya.chain.resolve;

import com.suriya.chain.algorithm.Cryptography;
import com.suriya.chain.exception.KeyChainException;
import com.suriya.chain.parser.AttributeParser;
import com.suriya.chain.parser.ByteProcessor;

import java.security.KeyStore;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.suriya.io.KeyChainSettings.General.*;

public class NodeResolver {

    private KeyStore keyStore;

    public static NodeResolver initialize(KeyStore keyStore) {
        NodeResolver nodeResolver = new NodeResolver();
        nodeResolver.keyStore = keyStore;
        return nodeResolver;
    }

    public ResolverKeyNode resolve(String entryName, String entryPassword, Set<String> attributeMapKeySet)
            throws KeyChainException {

        // read entry
        KeyStore.Entry entry = ByteProcessor.readKeyStoreEntryOfSecretKeyFromKeyStore(this.keyStore, entryName,
                entryPassword);

        // get attributes
        Set<KeyStore.Entry.Attribute> attributeSet = entry.getAttributes();

        // gather next key name and password
        Set<String> nextNodeAttributeSet = new HashSet<>();
        nextNodeAttributeSet.add(nextKeyNodeNameAttributeKey);
        nextNodeAttributeSet.add(nextKeyNodePasswordAttributeKey);
        Map<String, String> nextNodeMap = AttributeParser.populateAttributeMapFromSet(attributeSet,
                nextNodeAttributeSet).entrySet().stream().collect(Collectors.toMap(entryMap -> entryMap.getKey(),
                entryMap -> Cryptography.AES.decrypt(entryMap.getValue(), entryMap.getKey())));

        Map<String, String> attributeMap = AttributeParser.populateAttributeMapFromSet(attributeSet,
                attributeMapKeySet).entrySet().stream().collect(Collectors.toMap(entryMap -> entryMap.getKey(),
                entryMap -> Cryptography.AES.decrypt(entryMap.getValue(), entryMap.getKey())));

        // create resolver node
        ResolverKeyNode resolverKeyNode = new ResolverKeyNode(attributeMap, entryName, nextNodeMap
                .get(nextKeyNodeNameAttributeKey), nextNodeMap.get(nextKeyNodePasswordAttributeKey));

        return resolverKeyNode;
    }
}
