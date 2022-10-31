package com.suriya.io;

import com.suriya.chain.exception.KeyChainException;
import com.suriya.chain.resolve.ResolverKeyNode;
import com.suriya.data.KeyNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class KeyChainTest {

    String filePath = "src//test//resources//store";
    String fileName = "keyChain";

    @Test
    public void createKeyStoreTest() throws KeyChainException {

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

        KeyChain.Constructor keyChainConstructor = KeyChain.Constructor.construct(keyNodeList)
                .deploy( filePath,fileName, null);
        System.out.println("startsWith " + keyChainConstructor.startsWith());
        System.out.println("startsWithPassword " + keyChainConstructor.startsWithPassword());
        System.out.println("filePassword " + keyChainConstructor.filePassword());

//        Map<String, String> map = new HashMap<>();
//        map.put("startsWith", keyChainConstructor.startsWith());
//        map.put("startsWithPassword", keyChainConstructor.startsWithPassword());
//        map.put("filePassword", keyChainConstructor.filePassword());
    }

    @Test
    public void resolveKeyStoreTest() throws KeyChainException {

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

        KeyChain.Constructor keyChainConstructor = KeyChain.Constructor.construct(keyNodeList)
                .deploy( filePath,fileName, null);
        System.out.println("startsWith " + keyChainConstructor.startsWith());
        System.out.println("startsWithPassword " + keyChainConstructor.startsWithPassword());
        System.out.println("filePassword " + keyChainConstructor.filePassword());

//        Map<String, String> map = new HashMap<>();
//        map.put("startsWith", keyChainConstructor.startsWith());
//        map.put("startsWithPassword", keyChainConstructor.startsWithPassword());
//        map.put("filePassword", keyChainConstructor.filePassword());

        // actual test
        // node1
        Set<String> node1AttributeKeySet = new HashSet<>();
        node1AttributeKeySet.add("1");
        node1AttributeKeySet.add("2");
        ResolverKeyNode resolverKeyNode1 = KeyChain.Resolver
                .initialize(filePath, fileName, keyChainConstructor.filePassword())
                .resolve(keyChainConstructor.startsWith(), keyChainConstructor.startsWithPassword(),
                        node1AttributeKeySet);
        Assertions.assertEquals(attributeMap1.get("1"), resolverKeyNode1.getAttributeMap().get("1"));
        Assertions.assertEquals(attributeMap1.get("2"), resolverKeyNode1.getAttributeMap().get("2"));

        // node2
        Set<String> node2AttributeKeySet = new HashSet<>();
        node2AttributeKeySet.add("1");
        node2AttributeKeySet.add("2");
        ResolverKeyNode resolverKeyNode2 = KeyChain.Resolver
                .initialize(filePath, fileName, keyChainConstructor.filePassword())
                .resolve(resolverKeyNode1.getNextNodeName(), resolverKeyNode1.getNextNodePassword(),
                        node2AttributeKeySet);
        Assertions.assertEquals(attributeMap2.get("1"), resolverKeyNode2.getAttributeMap().get("1"));
        Assertions.assertEquals(attributeMap2.get("2"), resolverKeyNode2.getAttributeMap().get("2"));

        // node3
        Set<String> node3AttributeKeySet = new HashSet<>();
        node3AttributeKeySet.add("1");
        node3AttributeKeySet.add("2");
        node3AttributeKeySet.add("3");
        ResolverKeyNode resolverKeyNode3 = KeyChain.Resolver
                .initialize(filePath, fileName, keyChainConstructor.filePassword())
                .resolve(resolverKeyNode2.getNextNodeName(), resolverKeyNode2.getNextNodePassword(),
                        node3AttributeKeySet);
        Assertions.assertEquals(attributeMap3.get("1"), resolverKeyNode3.getAttributeMap().get("1"));
        Assertions.assertEquals(attributeMap3.get("2"), resolverKeyNode3.getAttributeMap().get("2"));
        Assertions.assertEquals(attributeMap3.get("3"), resolverKeyNode3.getAttributeMap().get("3"));

    }
}
