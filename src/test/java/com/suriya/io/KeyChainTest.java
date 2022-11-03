package com.suriya.io;

import com.suriya.chain.exception.KeyChainException;
import com.suriya.chain.parser.ByteProcessor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.KeyStore;
import java.util.*;

public class KeyChainTest {

    String filePath = "src//test//resources//store";
    String fileName = "keyChain";

    @Test
    public void createKeyStoreTest_usingFile() throws KeyChainException {

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
    public void createKeyStoreTest_usingKeyStore() throws KeyChainException {
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

        KeyStore keyStore = ByteProcessor.keyStoreFromKeyStoreByteArray(null, "PKCS12", "suriya");
        KeyChain.Constructor keyChainConstructor = KeyChain.Constructor.construct(keyNodeList)
                .update(keyStore);
        String entryStartsWith = keyChainConstructor.startsWith();
        String entryStartsWithPassword = keyChainConstructor.startsWithPassword();

        Assertions.assertTrue(entryStartsWith != null);
        Assertions.assertTrue(entryStartsWithPassword != null);

        KeyChain.Resolver resolver = KeyChain.Resolver.initialize(keyStore);
        
        // node1
        ResolverKeyNode resolverKeyNode1 = resolver.resolve(entryStartsWith,
                entryStartsWithPassword, attributeMap1.keySet());
        Assertions.assertTrue(resolverKeyNode1.getAttributeMap().get("1").equals("one"));
        Assertions.assertTrue(resolverKeyNode1.getAttributeMap().get("2").equals("two"));
        
        // node2
        ResolverKeyNode resolverKeyNode2 = resolver.resolve(resolverKeyNode1.getNextNodeName(),
                resolverKeyNode1.getNextNodePassword(), attributeMap2.keySet());
        Assertions.assertTrue(resolverKeyNode2.getAttributeMap().get("1").equals("onnnu"));
        Assertions.assertTrue(resolverKeyNode2.getAttributeMap().get("2").equals("rendu"));

        // node3
        ResolverKeyNode resolverKeyNode3 = resolver.resolve(resolverKeyNode2.getNextNodeName(),
                resolverKeyNode2.getNextNodePassword(), attributeMap3.keySet());
        Assertions.assertTrue(resolverKeyNode3.getAttributeMap().get("1").equals("elc1"));
        Assertions.assertTrue(resolverKeyNode3.getAttributeMap().get("2").equals("elc2"));
        Assertions.assertTrue(resolverKeyNode3.getAttributeMap().get("3").equals("elc3"));

    }


}
