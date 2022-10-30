package com.suriya.chain.connect;

import com.suriya.chain.exception.KeyChainException;
import com.suriya.chain.parser.ByteProcessor;
import com.suriya.chain.parser.FileProcessor;
import com.suriya.data.KeyNode;
import com.suriya.util.PasswordGenerator;

import java.security.KeyStore;
import java.util.Set;

import static com.suriya.io.KeyChainSettings.General.keyStorePasswordLength;
import static com.suriya.io.KeyChainSettings.Algorithm.keyStoreAlgorithm;

public class Deployer {

    private Set<ConnectorKeyNode> connectorKeyNodeSet;

    private Deployer() {}

    public static Deployer initialize(Set<ConnectorKeyNode> connectorKeyNodeSet) {
        Deployer deployer = new Deployer();
        deployer.connectorKeyNodeSet = connectorKeyNodeSet;
        return deployer;
    }

//
//    public static Deployer initialize(Set<KeyNode> keyNodeSet, String filePath, String fileName) {
//        Deployer deployer = new Deployer();
//        deployer.keyNodeSet = keyNodeSet;
//        deployer.fileName = fileName;
//        deployer.filePath = filePath;
//        return deployer;
//    }

    public void deploy(String filePath, String fileName) throws KeyChainException {
        deploy(filePath, fileName, null);
    }

    public void deploy(String filePath, String fileName, String filePassword) throws KeyChainException {

        byte[] keyStoreByteArray = null;

        try {
            keyStoreByteArray = FileProcessor.readFromFile(filePath, fileName);
        } catch (KeyChainException e) {
            // file error consumed
            e.printStackTrace();
        }

        // handle new file
        if (filePassword == null || keyStoreByteArray == null) {
            filePassword = new PasswordGenerator.Builder()
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
        String finalFilePassword = filePassword;
        connectorKeyNodeSet.stream().forEach(connectorKeyNode -> {
            try {
                ByteProcessor.storeSecretKeyInKeyStore(keyStore, keyStoreAlgorithm,
                        finalFilePassword, connectorKeyNode.getSecureRandomKey(), connectorKeyNode.getEntryName(),
                        null, connectorKeyNode.getAttributeSet());
            } catch (KeyChainException e) {
                e.printStackTrace();
            }
        });
    }


}
