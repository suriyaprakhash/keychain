package com.suriya.chain.constructor;

import com.suriya.chain.exception.KeyChainException;
import com.suriya.chain.parser.ByteProcessor;
import com.suriya.chain.parser.FileProcessor;
import com.suriya.util.RandomStringGenerator;

import java.security.KeyStore;
import java.util.Set;

import static com.suriya.io.KeyChainSettings.General.keyStorePasswordLength;
import static com.suriya.io.KeyChainSettings.Algorithm.keyStoreAlgorithm;

public final class Deployer {

    private Set<ConstructorKeyNode> constructorKeyNodeSet;
    private String fileName;
    private String filePath;
    private String filePassword;

    private Deployer() {}

    public static Deployer initialize(Set<ConstructorKeyNode> constructorKeyNodeSet) {
        Deployer deployer = new Deployer();
        deployer.constructorKeyNodeSet = constructorKeyNodeSet;
        return deployer;
    }

    public String filePassword() {
        return filePassword;
    }

    public Deployer deploy(String filePath, String fileName) throws KeyChainException {
        return deploy(filePath, fileName, null);
    }

    public Deployer deploy(String filePath, String fileName, String filePassword) throws KeyChainException {
        this.fileName = fileName;
        this.filePath = filePath;
        this.filePassword = filePassword;
        KeyStore keyStore = addSecretsToKeyStore();
        FileProcessor.writeFile(keyStore, this.filePath, this.fileName,  this.filePassword);
        return this;
    }

    public Deployer update(KeyStore keyStore) throws KeyChainException {
        addSecretsToKeyStore(keyStore);
        return this;
    }

    private KeyStore addSecretsToKeyStore() throws KeyChainException {
        byte[] keyStoreByteArray = null;

        if (filePassword != null) {
            try {
                keyStoreByteArray = FileProcessor.readFromFile(filePath, fileName);
            } catch (KeyChainException e) {
                // file error consumed
                e.printStackTrace();
            }
        }

        // handle new file
        if (filePassword == null || keyStoreByteArray == null) {
            filePassword = new RandomStringGenerator.Builder()
                    .lower(2)
                    .upper(2)
                    .digits(1)
                    .punctuation(1)
                    .build().generate(keyStorePasswordLength);
            keyStoreByteArray = null;
        }

        // read keystore from existing or init keystore
        KeyStore keyStore = ByteProcessor.keyStoreFromKeyStoreByteArray(keyStoreByteArray, keyStoreAlgorithm,
                filePassword);
        addSecretsToKeyStore(keyStore);
        return keyStore;
    }

    private void addSecretsToKeyStore(KeyStore keyStore) throws KeyChainException {
        constructorKeyNodeSet.stream().forEach(connectorKeyNode -> {
            try {
                ByteProcessor.storeSecretKeyInKeyStore(keyStore, connectorKeyNode.getSecureRandomKey(),
                        connectorKeyNode.getEntryName(), connectorKeyNode.getPassword(),
                        connectorKeyNode.getAttributeSet());
            } catch (KeyChainException e) {
                e.printStackTrace();
            }
        });
    }


}
