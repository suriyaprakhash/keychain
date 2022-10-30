package com.suriya.io;

import com.suriya.chain.exception.KeyChainException;
import com.suriya.data.KeyNode;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeyChainTest {

    String filePath = "src//test//resources//store";
    String fileName = "keyChain";

    @Test
    public void createKeyStore() throws KeyChainException {

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

        KeyChain keyChain = KeyChain.chain(keyNodeList).deploy( filePath,fileName, "2LnWTb$%fW$Poe2k");
        System.out.println("startsWith " + keyChain.startsWith());
        System.out.println("startsWithPassword " + keyChain.startsWithPassword());
        System.out.println("filePassword " + keyChain.filePassword());
    }
}
