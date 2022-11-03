package com.suriya.chain.constructor;

import com.suriya.chain.exception.KeyChainException;
import com.suriya.io.KeyNode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;

public class DeployerTest {

    static NodeConstructor nodeConstructor;
    String filePath = "src//test//resources//store";
    String fileName = "keyChain";

    @BeforeAll
    public static void beforeAll() {
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

        nodeConstructor = NodeConstructor.initialize(keyNodeList);
    }

    @Test
    public void deployTest() throws KeyChainException {
        Set<ConstructorKeyNode> constructorKeyNodeSet = nodeConstructor.build();
        String firstNode = nodeConstructor.starterNodeName();
        String firstNodePassword = nodeConstructor.startedNodePassword();
        Deployer deployer = Deployer.initialize(constructorKeyNodeSet).deploy(filePath, fileName);
        String filePassword = deployer.filePassword();

        System.out.println("filePassword: " + filePassword);
        System.out.println("firstNode: " + firstNode);
        System.out.println("firstNodePassword: " + firstNodePassword);
        constructorKeyNodeSet.stream().forEach(connectorKeyNode -> {
            System.out.println(connectorKeyNode.getEntryName() + " ::: " + connectorKeyNode.getPassword());
        });
    }
}
