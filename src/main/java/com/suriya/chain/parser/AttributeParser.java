package com.suriya.chain.parser;

import com.suriya.util.ConversionUtil;

import java.security.KeyStore;
import java.security.PKCS12Attribute;
import java.util.*;
import java.util.stream.Collectors;

public class AttributeParser {

    public static Set<KeyStore.Entry.Attribute> populateAttributeSetFromMap(Map<String, String> attributeMap) {
        Set<KeyStore.Entry.Attribute> attributeSet = attributeMap.entrySet().stream()
                .map(entry -> AttributeParser.getAttributeFromKeyValue(entry.getKey(), entry.getValue()))
                .collect(Collectors.toSet());
        return attributeSet;
    }

    public static PKCS12Attribute getAttributeFromKeyValue(String attributeKey, String attributeValue) {
        String oidHash = ConversionUtil.stringToASN1(attributeKey).toString();
        return new PKCS12Attribute(oidHash, attributeValue);
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

    private static KeyStore.Entry.Attribute getAttributeForTheGivenKey(Set<KeyStore.Entry.Attribute> attributeSet, String key) {
        Optional<KeyStore.Entry.Attribute> optionalAttribute = null;
        String oid = ConversionUtil.stringToASN1(key).toString();
        optionalAttribute = attributeSet.stream().filter(tempAttr -> tempAttr.getName().equals(oid)).findAny();
        if (optionalAttribute.isPresent()) {
            return optionalAttribute.get();
        }
        return null;
    }

}
