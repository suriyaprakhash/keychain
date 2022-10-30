package com.suriya.io;

import com.suriya.chain.connect.ConnectorKeyNode;
import com.suriya.chain.connect.Deployer;
import com.suriya.chain.connect.NodeBuilder;
import com.suriya.chain.exception.KeyChainException;
import com.suriya.data.KeyNode;

import java.util.List;
import java.util.Set;

public class KeyChain {

    private String startsWith;
    private Set<ConnectorKeyNode> connectorKeyNodeSet;

    private KeyChain(String startsWith, Set<ConnectorKeyNode> connectorKeyNodeSet) {
        this.startsWith = startsWith;
        this.connectorKeyNodeSet = connectorKeyNodeSet;
    }

    public static KeyChain chain(List<KeyNode> keyNodeList) {
      NodeBuilder nodeBuilder = NodeBuilder.initialize(keyNodeList);
      KeyChain keyChain = new KeyChain(nodeBuilder.starterNodeName(), nodeBuilder.build());
      return keyChain;
    }

    public String first() {
        return startsWith;
    }

    public KeyChain deploy(String filePath, String fileName) throws KeyChainException {
        Deployer deployer = Deployer.initialize(connectorKeyNodeSet);
        deployer.deploy(filePath, fileName);
        return this;
    }

    public KeyChain deploy(String filePath, String fileName, String filePassword) throws KeyChainException{
        Deployer deployer = Deployer.initialize(connectorKeyNodeSet);
        deployer.deploy(filePath, fileName, filePassword);
        return this;
    }

}
