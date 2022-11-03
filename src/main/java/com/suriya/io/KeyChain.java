package com.suriya.io;

import com.suriya.chain.constructor.ConstructorKeyNode;
import com.suriya.chain.constructor.Deployer;
import com.suriya.chain.constructor.NodeConstructor;
import com.suriya.chain.exception.KeyChainException;
import com.suriya.chain.resolve.NodeResolver;
import com.suriya.chain.resolve.Reader;
import com.suriya.chain.resolve.ResolverKeyNode;
import com.suriya.data.KeyNode;

import java.security.KeyStore;
import java.util.List;
import java.util.Set;

/**
 * The IO class used for building and resolving the keychain. The main pieces include {@link Constructor} and
 * {@link Resolver} used for construction of the entry nodes and resolving the entry nodes.
 *<p>
 * <b>KeyChain Construction Usage:</b>
 * <code>
 *     KeyChain.construct(nodeList).deploy(filePath,fileName)
 * </code>
 *</p>
 * <p>See {@link KeyNode} for more info on the required input.</p>
 *<p>
 * <b>KeyChain Construction Usage:</b>
 * <code>
 *   KeyChain.Resolver resolver = KeyChain.initialize(filePath,fileName,filePassword).deploy(filePath,fileName)
 *   resolver.resolve(entryName, entryPassword, attributeMapKeySet)
 * </code>
 *</p>
 * <p>See {@link ResolverKeyNode} for reading the output from the resolved node.</p>
 */
public class KeyChain {

    public static class Constructor {
        private String startsWith;
        private String startsWithPassword;
        private String filePassword;
        private Set<ConstructorKeyNode> constructorKeyNodeSet;

        private Constructor(String startsWith, String startsWithPassword, Set<ConstructorKeyNode>
                constructorKeyNodeSet) {
            this.startsWith = startsWith;
            this.startsWithPassword = startsWithPassword;
            this.constructorKeyNodeSet = constructorKeyNodeSet;
        }

        /**
         * Initialize the {@link Constructor} class and get everything to construct the KeyChain. Then call
         * {@link this#deploy(String, String)} to deploy the KeyStore file into your filesystem.
         *
         * @param keyNodeList - See {@link KeyNode} for more info
         * @return
         */
        public static Constructor construct(List<KeyNode> keyNodeList) {
            NodeConstructor nodeConstructor = NodeConstructor.initialize(keyNodeList);
            Set<ConstructorKeyNode> constructorKeyNodeSet = nodeConstructor.build();
            Constructor keyChain = new Constructor(nodeConstructor.starterNodeName(), nodeConstructor
                    .startedNodePassword(), constructorKeyNodeSet);
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

        /**
         * This method deploys a new keyStore. Use {@link this#filePassword} to get the keystore file password.
         *
         * <p>For overwriting existing keyStore file use {@link this#deploy(String, String, String)}</p>
         *
         * @param filePath - File system path where to store the keyStore file
         * @param fileName - Name of the keystore file
         * @return
         * @throws KeyChainException
         */
        public Constructor deploy(String filePath, String fileName) throws KeyChainException {
            Deployer deployer = Deployer.initialize(constructorKeyNodeSet);
            deployer.deploy(filePath, fileName);
            this.filePassword = deployer.filePassword();
            return this;
        }

        /**
         * This method deploys a new keyStore.
         *
         * <p>For new keyStore file use {@link this#deploy(String, String)}</p>
         *
         * @param filePath - File system path where find the existing keyStore file
         * @param fileName - Name of the existing keystore file
         * @param filePassword - Password of the existing file
         * @return
         * @throws KeyChainException
         */
        public Constructor deploy(String filePath, String fileName, String filePassword) throws KeyChainException {
            Deployer deployer = Deployer.initialize(constructorKeyNodeSet);
            deployer.deploy(filePath, fileName, filePassword);
            this.filePassword = deployer.filePassword();
            return this;
        }

        public Constructor chain(KeyStore keyStore) throws KeyChainException {
            Deployer deployer = Deployer.initialize(constructorKeyNodeSet);
            deployer.chain(keyStore);
            return this;
        }

    }

    public static class Resolver {

        private NodeResolver nodeResolver;

        private Resolver(NodeResolver nodeResolver) {
            this.nodeResolver = nodeResolver;
        }

        /**
         * This initializes the {@link Resolver} for resolving the entry nodes within the KeyChain. Then call
         * {@link this#resolve(String, String, Set)}
         *
         * @param filePath - The path of the KeyStore file to resolve
         * @param fileName - The name of the KeyStore file
         * @param filePassword - The password of the KeyStore file
         * @return
         * @throws KeyChainException
         */
        public static Resolver initialize(String filePath, String fileName, String filePassword)
                throws KeyChainException {
            NodeResolver nodeResolver = NodeResolver.initialize(Reader.read(filePath, fileName, filePassword));
            Resolver resolver = new Resolver(nodeResolver);
            return resolver;
        }

        public static Resolver initialize(KeyStore keyStore) {
            NodeResolver nodeResolver = NodeResolver.initialize(keyStore);
            Resolver resolver = new Resolver(nodeResolver);
            return resolver;
        }

        /**
         * This method resolves the current entry node from the KeyStore file.
         *
         * @param entryName - name of the entry in the KeyStore file
         * @param entryPassword - password of the entry in the KeyStore file
         * @param attributeMapKeySet - set of key for which the values are required
         * @return
         * @throws KeyChainException
         */
        public ResolverKeyNode resolve(String entryName, String entryPassword, Set<String> attributeMapKeySet)
                throws KeyChainException {
            return this.nodeResolver.resolve(entryName, entryPassword, attributeMapKeySet);
        }
    }

}
