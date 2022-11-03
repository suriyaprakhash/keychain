package com.suriya.chain.resolve;

import com.suriya.chain.algorithm.Cryptography;
import com.suriya.chain.exception.KeyChainException;
import com.suriya.chain.parser.AttributeParser;
import com.suriya.chain.parser.ByteProcessor;
import com.suriya.io.ResolverKeyNode;

import java.security.KeyStore;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.suriya.io.KeyChainSettings.General.*;

/**
 * This class is the primary processor that resolves the node one at a time based on the inputs supplied. This method
 * is internal to the library.
 *
 * See {@link com.suriya.io.KeyChain} for getting started
 */
public final class NodeResolver {

    private KeyStore keyStore;

    public static NodeResolver initialize(KeyStore keyStore) {
        NodeResolver nodeResolver = new NodeResolver();
        nodeResolver.keyStore = keyStore;
        return nodeResolver;
    }

    /**
     * This method resolves the entry node one at a time
     *
     * @param entryName - alias name of the entry
     * @param entryPassword - password for accessing the entry
     * @param attributeMapKeySet - the set of input attributes that are required
     * @return
     * @throws KeyChainException
     */
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
