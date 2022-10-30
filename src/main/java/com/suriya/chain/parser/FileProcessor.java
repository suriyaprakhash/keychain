package com.suriya.chain.parser;

import com.suriya.chain.exception.KeyChainException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

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

    public static void writeFile(KeyStore keyStore, String filePath, String fileName, String filePassword)
            throws KeyChainException {
        //Storing the KeyStore object
        File file = new File(filePath + "//" + fileName);
        java.io.FileOutputStream fos = null;
        try {
            fos = new java.io.FileOutputStream(file);
            keyStore.store(fos, filePassword.toCharArray());
        } catch (FileNotFoundException e) {
            throw new KeyChainException("Exception while writing the file " + fileName, e);
        } catch (CertificateException e) {
            throw new KeyChainException("Exception while writing the file " + fileName, e);
        } catch (KeyStoreException e) {
            throw new KeyChainException("Exception while writing the file " + fileName, e);
        } catch (IOException exception) {
            throw new KeyChainException("Exception while writing the file " + fileName, exception);
        } catch (NoSuchAlgorithmException e) {
            throw new KeyChainException("Exception while writing the file " + fileName, e);
        }
    }

}
