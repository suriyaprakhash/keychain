package com.suriya.chain.constructor;

import com.suriya.data.KeyNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.KeyStore;
import java.util.*;

public class NodeConstructorTest {

    @Test
    void nodeBuilderTest() {
        String filePath = "src//test//resources//store";
        String fileName = "keyChain";

        List<KeyNode> keyNodeList = new ArrayList<>();
        Map<String, String> attributeMap1 = new HashMap<>();
        attributeMap1.put("1", "one");
        attributeMap1.put("2", "two");
        KeyNode kn1 = new KeyNode(attributeMap1);

        Map<String, String> attributeMap2 = new HashMap<>();
        attributeMap2.put("1", "onnnu");
        attributeMap2.put("2", "rendu");
        KeyNode kn2 = new KeyNode(attributeMap2);


        Map<String, String> attributeMap3 = new HashMap<>();
        attributeMap3.put("1", "elc1");
        attributeMap3.put("2", "elc2");
        attributeMap3.put("3", "elc3");
        KeyNode kn3 = new KeyNode(attributeMap3);

        keyNodeList.add(kn1);
        keyNodeList.add(kn2);
        keyNodeList.add(kn3);

        NodeConstructor nodeConstructor = NodeConstructor.initialize(keyNodeList);
        Set<ConstructorKeyNode> constructorKeyNodeSet = nodeConstructor.build();
        String firstNode = nodeConstructor.starterNodeName();

        // checking value
        Set<KeyStore.Entry.Attribute> attributeSet = constructorKeyNodeSet.stream().filter(keyNode -> keyNode
                .getEntryName().equals(firstNode)).findAny().get().getAttributeSet();
        Assertions.assertTrue(attributeSet.stream().anyMatch(attribute -> attribute.getValue().equals("one")));
        Assertions.assertTrue(attributeSet.stream().anyMatch(attribute -> attribute.getValue().equals("two")));
    }
}
