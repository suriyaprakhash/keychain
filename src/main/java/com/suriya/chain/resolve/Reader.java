package com.suriya.chain.resolve;

import com.suriya.chain.exception.KeyChainException;
import com.suriya.chain.parser.ByteProcessor;
import com.suriya.chain.parser.FileProcessor;

import java.security.KeyStore;

import static com.suriya.io.KeyChainSettings.Algorithm.*;

public final class Reader {

    public static KeyStore read(String filePath, String fileName, String filePassword) throws KeyChainException {
        byte[] keyStoreFileByteArray = FileProcessor.readFromFile(filePath, fileName);
        return ByteProcessor.keyStoreFromKeyStoreByteArray(keyStoreFileByteArray, keyStoreAlgorithm, filePassword);
    }

}
