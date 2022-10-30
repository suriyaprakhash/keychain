package com.suriya.chain.connect;

import com.suriya.chain.exception.KeyChainException;
import com.suriya.chain.parser.ByteProcessor;
import com.suriya.chain.parser.FileProcessor;
import com.suriya.util.PasswordGenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Set;

import static com.suriya.io.KeyChainSettings.General.keyStorePasswordLength;
import static com.suriya.io.KeyChainSettings.Algorithm.keyStoreAlgorithm;

public class Deployer {

    private Set<ConnectorKeyNode> connectorKeyNodeSet;
    private String fileName;
    private String filePath;
    private String filePassword;

    private Deployer() {}

    public static Deployer initialize(Set<ConnectorKeyNode> connectorKeyNodeSet) {
        Deployer deployer = new Deployer();
        deployer.connectorKeyNodeSet = connectorKeyNodeSet;
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
        connectorKeyNodeSet.stream().forEach(connectorKeyNode -> {
            try {
                ByteProcessor.storeSecretKeyInKeyStore(keyStore, keyStoreAlgorithm,
                        filePassword, connectorKeyNode.getSecureRandomKey(), connectorKeyNode.getEntryName(),
                        connectorKeyNode.getPassword(), connectorKeyNode.getAttributeSet());
            } catch (KeyChainException e) {
                e.printStackTrace();
            }
        });

        return keyStore;
    }


}
