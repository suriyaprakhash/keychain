package com.suriya.io;

import com.suriya.chain.algorithm.AsymmetricKey;
import com.suriya.chain.exception.KeyChainException;
import com.suriya.chain.resolve.ResolverKeyNode;
import com.suriya.data.KeyNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.util.*;

import static com.suriya.io.KeyChainSettings.Algorithm.*;

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


    @Test
    public void resolveKeyStoreTest_Encrypted() throws KeyChainException {
        KeyPair keyPair = AsymmetricKey.generateAsymmetricKey(keyPairAlgorithm, keyPairKeySize);

        List<KeyNode> keyNodeList1 = new ArrayList<>();
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

        keyNodeList1.add(kn1);
        keyNodeList1.add(kn2);
        keyNodeList1.add(kn3);

        KeyChain.Constructor keyChainConstructor = KeyChain.Constructor.construct(keyNodeList1, keyPair.getPrivate())
                .deploy( filePath,fileName, null);
        System.out.println("startsWith " + keyChainConstructor.startsWith());
        System.out.println("startsWithPassword " + keyChainConstructor.startsWithPassword());
        System.out.println("filePassword " + keyChainConstructor.filePassword());

        // node1
        Set<String> node1AttributeKeySet = new HashSet<>();
        node1AttributeKeySet.add("1");
        node1AttributeKeySet.add("2");
        ResolverKeyNode resolverKeyNode1 = KeyChain.Resolver
                .initialize(filePath, fileName, keyChainConstructor.filePassword())
                .resolve(keyChainConstructor.startsWith(), keyChainConstructor.startsWithPassword(),
                        node1AttributeKeySet, keyPair.getPublic());
        Assertions.assertEquals(attributeMap1.get("1"), resolverKeyNode1.getAttributeMap().get("1"));
        Assertions.assertEquals(attributeMap1.get("2"), resolverKeyNode1.getAttributeMap().get("2"));
    }


    @Test
    public void resolveKeyStoreTest_Existing() throws KeyChainException {

        List<KeyNode> keyNodeList1 = new ArrayList<>();
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

        keyNodeList1.add(kn1);
        keyNodeList1.add(kn2);
        keyNodeList1.add(kn3);

        KeyChain.Constructor keyChainConstructor = KeyChain.Constructor.construct(keyNodeList1)
                .deploy( filePath,fileName, null);
        System.out.println("startsWith " + keyChainConstructor.startsWith());
        System.out.println("startsWithPassword " + keyChainConstructor.startsWithPassword());
        System.out.println("filePassword " + keyChainConstructor.filePassword());

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

        // actual test
        ////// iteration 2

        List<KeyNode> keyNodeList2 = new ArrayList<>();
        Map<String, String> attributeMap21 = new HashMap<>();
        attributeMap21.put("decimal", "26");
        attributeMap21.put("hex", "15AE");
        KeyNode kn21 = new KeyNode(attributeMap21);

        Map<String, String> attributeMap22 = new HashMap<>();
        attributeMap22.put("single", "timeline");
        attributeMap22.put("paired", "hello world");
        KeyNode kn22 = new KeyNode(attributeMap22);

        keyNodeList2.add(kn21);
        keyNodeList2.add(kn22);

        // using existing one
        KeyChain.Constructor keyChainConstructor2 = KeyChain.Constructor.construct(keyNodeList2)
                .deploy(filePath,fileName, keyChainConstructor.filePassword());
        System.out.println("startsWith " + keyChainConstructor2.startsWith());
        System.out.println("startsWithPassword " + keyChainConstructor2.startsWithPassword());
        System.out.println("filePassword " + keyChainConstructor2.filePassword());

        // node1
        Set<String> node21AttributeKeySet = new HashSet<>();
        node21AttributeKeySet.add("decimal");
        node21AttributeKeySet.add("hex");
        ResolverKeyNode resolverKeyNode21 = KeyChain.Resolver
                .initialize(filePath, fileName, keyChainConstructor.filePassword())
                .resolve(keyChainConstructor2.startsWith(), keyChainConstructor2.startsWithPassword(),
                        node21AttributeKeySet);
        Assertions.assertEquals(attributeMap21.get("decimal"), resolverKeyNode21.getAttributeMap().get("decimal"));
        Assertions.assertEquals(attributeMap21.get("hex"), resolverKeyNode21.getAttributeMap().get("hex"));

        // node2
        Set<String> node22AttributeKeySet = new HashSet<>();
        node22AttributeKeySet.add("single");
        node22AttributeKeySet.add("paired");
        ResolverKeyNode resolverKeyNode22 = KeyChain.Resolver
                .initialize(filePath, fileName, keyChainConstructor.filePassword())
                .resolve(resolverKeyNode21.getNextNodeName(), resolverKeyNode21.getNextNodePassword(),
                        node22AttributeKeySet);
        Assertions.assertEquals(attributeMap22.get("single"), resolverKeyNode22.getAttributeMap().get("single"));
        Assertions.assertEquals(attributeMap22.get("paired"), resolverKeyNode22.getAttributeMap().get("paired"));

    }
}
