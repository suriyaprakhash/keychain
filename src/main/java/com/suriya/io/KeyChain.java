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
    private String startsWithPassword;
    private String filePassword;
    private Set<ConnectorKeyNode> connectorKeyNodeSet;

    private KeyChain(String startsWith, String startsWithPassword, Set<ConnectorKeyNode> connectorKeyNodeSet) {
        this.startsWith = startsWith;
        this.startsWithPassword = startsWithPassword;
        this.connectorKeyNodeSet = connectorKeyNodeSet;
    }

    public static KeyChain chain(List<KeyNode> keyNodeList) {
      NodeBuilder nodeBuilder = NodeBuilder.initialize(keyNodeList);
      nodeBuilder.build();
      KeyChain keyChain = new KeyChain(nodeBuilder.starterNodeName(), nodeBuilder.startedNodePassword(),
              nodeBuilder.build());
      return keyChain;
    }

    public String startsWith() {
        return startsWith;
    }

    public String startsWithPassword() {
        return startsWithPassword;
    }

    public String filePassword() {
        return filePassword;
    }

    public KeyChain deploy(String filePath, String fileName) throws KeyChainException {
        Deployer deployer = Deployer.initialize(connectorKeyNodeSet);
        deployer.deploy(filePath, fileName);
        this.filePassword = deployer.filePassword();
        return this;
    }

    public KeyChain deploy(String filePath, String fileName, String filePassword) throws KeyChainException{
        Deployer deployer = Deployer.initialize(connectorKeyNodeSet);
        deployer.deploy(filePath, fileName, filePassword);
        this.filePassword = deployer.filePassword();
        return this;
    }

}
