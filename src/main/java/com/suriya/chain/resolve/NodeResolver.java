package com.suriya.chain.resolve;

import com.suriya.chain.parser.AttributeParser;
import com.suriya.chain.parser.ByteProcessor;

import java.security.KeyStore;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.suriya.io.KeyChainSettings.General.*;

public class NodeResolver {

    private KeyStore keyStore;

    public static NodeResolver initialize(KeyStore keyStore) {
        NodeResolver nodeResolver = new NodeResolver();
        nodeResolver.keyStore = keyStore;
        return nodeResolver;
    }

    public ResolverKeyNode resolve(String entryName, String entryPassword, Set<String> attributeMapKeySet) {

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
                nextNodeAttributeSet);

        // create resolver node
        ResolverKeyNode resolverKeyNode = new ResolverKeyNode(AttributeParser.populateAttributeMapFromSet(attributeSet,
                attributeMapKeySet), entryName, nextNodeMap.get(nextKeyNodeNameAttributeKey),
                nextNodeMap.get(nextKeyNodePasswordAttributeKey));

        return resolverKeyNode;
    }
}
