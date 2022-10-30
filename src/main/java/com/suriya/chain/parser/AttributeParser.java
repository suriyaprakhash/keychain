package com.suriya.chain.parser;

import com.suriya.chain.algorithm.Hash;

import java.security.KeyStore;
import java.security.PKCS12Attribute;
import java.util.*;
import java.util.stream.Collectors;

import static com.suriya.io.KeyChainSettings.Algorithm.*;

public class AttributeParser {

    public static Set<KeyStore.Entry.Attribute> populateAttributeSetFromMap(Map<String, String> attributeMap) {
        Set<KeyStore.Entry.Attribute> attributeSet =
                attributeMap.entrySet().stream().map(entry -> {
                    String attributeKeyHash = Hash.getHexStringFromByteArray(
                            Hash.generateMessageDigest(messageDigestAlgorithm , entry.getKey()));
                          return  new PKCS12Attribute(attributeKeyHash,
                                    entry.getValue());
                }).collect(Collectors.toSet());
        return attributeSet;
    }

    public static Map<String,String> populateAttributeMapFromSet(Set<KeyStore.Entry.Attribute> attributeSet,
                                                                 Set<String> attributeMapKeySet) {
        return attributeMapKeySet.stream().collect(Collectors.toMap(
                attributeKeyString -> attributeKeyString , attributeKeyString -> {
                    KeyStore.Entry.Attribute attribute = getAttributeForTheGivenKey(attributeSet, attributeKeyString);
                    if (attribute == null) {
                        return "";
                    }
                    return attribute.getValue();
                }));
    }


    private static KeyStore.Entry.Attribute getAttributeForTheGivenKey(Set<KeyStore.Entry.Attribute> attributeSet,
                                                                       String attributeKey) {
        Optional<KeyStore.Entry.Attribute> optionalAttribute = null;
        String attributeKeyHash = Hash.getHexStringFromByteArray(
                Hash.generateMessageDigest(messageDigestAlgorithm , attributeKey));
        optionalAttribute = attributeSet.stream().filter(tempAttr -> tempAttr.getName().equals(attributeKeyHash))
                .findAny();
        if (optionalAttribute.isPresent()) {
            return optionalAttribute.get();
        }
        return null;
    }

}
