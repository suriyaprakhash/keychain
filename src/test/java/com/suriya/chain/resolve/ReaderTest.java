package com.suriya.chain.resolve;

import com.suriya.chain.exception.KeyChainException;
import com.suriya.data.KeyNode;
import com.suriya.io.KeyChain;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.security.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReaderTest {

    static String filePath = "src//test//resources//store";
    static String fileName = "keyChain";

    static Map<String, String> map = new HashMap<>();

    @BeforeAll
    public static void beforeAll() throws KeyChainException {
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
                .deploy(filePath, fileName);
        System.out.println("startsWith " + keyChainConstructor.startsWith());
        System.out.println("startsWithPassword " + keyChainConstructor.startsWithPassword());
        System.out.println("filePassword " + keyChainConstructor.filePassword());


        map.put("startsWith", keyChainConstructor.startsWith());
        map.put("startsWithPassword", keyChainConstructor.startsWithPassword());
        map.put("filePassword", keyChainConstructor.filePassword());
    }

    @Test
    void readTest() throws KeyChainException, UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException {
        KeyStore keyStore = Reader.read(filePath, fileName, map.get("filePassword"));
        Assertions.assertTrue(keyStore != null);
    }
}
