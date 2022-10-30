package com.suriya.chain.parser;

import com.suriya.chain.exception.KeyChainException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileProcessor {

    public static byte[] readFromFile(String filePath, String fileName) throws KeyChainException {
        byte[] fileByteArray = null;
        try {
            fileByteArray = Files.readAllBytes(Paths.get(filePath+ "\\" + fileName));
        } catch (IOException exception) {
            throw new KeyChainException("Exception while reading the file " + fileName, exception);
        }
        return fileByteArray;
    }

    public static void writeFile(byte[] fileByteArray, String filePath, String fileName) throws KeyChainException {
        try {
            Files.write(Paths.get(filePath+ "\\" + fileName), fileByteArray);
        } catch (IOException exception) {
            throw new KeyChainException("Exception while writing the file " + fileName, exception);
        }
    }

}
