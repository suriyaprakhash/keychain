package com.suriya.io;

import com.suriya.chain.algorithm.AsymmetricKey;
import com.suriya.chain.constructor.ConstructorKeyNode;
import com.suriya.chain.constructor.Deployer;
import com.suriya.chain.constructor.NodeConstructor;
import com.suriya.chain.exception.KeyChainException;
import com.suriya.chain.resolve.NodeResolver;
import com.suriya.chain.resolve.Reader;
import com.suriya.chain.resolve.ResolverKeyNode;
import com.suriya.data.KeyNode;

import java.security.PrivateKey;
import java.util.List;
import java.util.Set;

public class KeyChain {

    public static class Constructor {
        private String startsWith;
        private String startsWithPassword;
        private String filePassword;
        private Set<ConstructorKeyNode> constructorKeyNodeSet;
        private PrivateKey privateKey;

        private Constructor(String startsWith, String startsWithPassword, Set<ConstructorKeyNode> constructorKeyNodeSet,
                            PrivateKey privateKey) {
            this.startsWith = startsWith;
            this.startsWithPassword = startsWithPassword;
            this.constructorKeyNodeSet = constructorKeyNodeSet;
            this.privateKey = privateKey;
        }

        public static Constructor construct(List<KeyNode> keyNodeList) {
            NodeConstructor nodeConstructor = NodeConstructor.initialize(keyNodeList, null);
            Set<ConstructorKeyNode> constructorKeyNodeSet = nodeConstructor.build();
            Constructor keyChain = new Constructor(nodeConstructor.starterNodeName(), nodeConstructor.startedNodePassword(),
                    constructorKeyNodeSet, null);
            return keyChain;
        }

        public static Constructor construct(List<KeyNode> keyNodeList, PrivateKey privateKey) {
            NodeConstructor nodeConstructor = NodeConstructor.initialize(keyNodeList, privateKey);
            Set<ConstructorKeyNode> constructorKeyNodeSet = nodeConstructor.build();
            Constructor keyChain = new Constructor(nodeConstructor.starterNodeName(), nodeConstructor.startedNodePassword(),
                    constructorKeyNodeSet, privateKey);
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

        public Constructor deploy(String filePath, String fileName) throws KeyChainException {
            Deployer deployer = Deployer.initialize(constructorKeyNodeSet);
            deployer.deploy(filePath, fileName);
            this.filePassword = deployer.filePassword();
            return this;
        }

        public Constructor deploy(String filePath, String fileName, String filePassword) throws KeyChainException{
            Deployer deployer = Deployer.initialize(constructorKeyNodeSet);
            deployer.deploy(filePath, fileName, filePassword);
            this.filePassword = deployer.filePassword();
            return this;
        }
    }

    public static class Resolver {

        private NodeResolver nodeResolver;

        private Resolver(NodeResolver nodeResolver) {
            this.nodeResolver = nodeResolver;
        }

        public static Resolver initialize(String filePath, String fileName, String filePassword) throws KeyChainException {
            NodeResolver nodeResolver = NodeResolver.initialize(Reader.read(filePath, fileName, filePassword));
            Resolver resolver = new Resolver(nodeResolver);
            return resolver;
        }

        public ResolverKeyNode resolve(String entryName, String entryPassword, Set<String> attributeMapKeySet) {
            return this.nodeResolver.resolve(entryName, entryPassword, attributeMapKeySet);
        }
    }

}
