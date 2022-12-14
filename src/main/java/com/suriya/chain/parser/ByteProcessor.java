package com.suriya.chain.parser;

import com.suriya.chain.exception.KeyChainException;

import javax.crypto.SecretKey;
import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

// PARSER
public class ByteProcessor {

    private static final List<byte[]> LICENSE_LIST = new ArrayList<>(3);


    public static KeyStore keyStoreFromKeyStoreByteArray(byte[] keyStoreByteArray, String keyStoreAlgorithm,
                                                         String keyStorePass) throws KeyChainException {
        KeyStore keyStore = null;
        try {
            //Creating the KeyStore object
            keyStore = KeyStore.getInstance(keyStoreAlgorithm);  //JCEKS PKCS12

            //Loading the KeyStore object
            char[] keyStorePasswordCharArray = keyStorePass.toCharArray(); // changeit

            //Check if key store already exists
            if (keyStoreByteArray == null) {
                keyStore.load(null, keyStorePasswordCharArray);
            } else {
                InputStream myInputStream = new ByteArrayInputStream(keyStoreByteArray);
                keyStore.load(myInputStream, keyStorePass.toCharArray());
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new KeyChainException("if there is an I/O or format problem with the keystore data, if a " +
                    "password is required but not given, or if the given password was incorrect. If the error is " +
                    "due to a wrong password, the cause of the IOException should be an UnrecoverableKeyException", e);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new KeyChainException("The algorithm used to check the integrity of the keystore cannot be found", e);
        } catch (KeyStoreException e) {
            e.printStackTrace();
            throw new KeyChainException("No Provider supports a KeyStoreSpi implementation for the specified type", e);
        } catch (CertificateException e) {
            e.printStackTrace();
            throw new KeyChainException("Certificates in the keystore could not be loaded", e);
        }
        return keyStore;
    }

    public static void storeSecretKeyInKeyStore(KeyStore keyStore, String keyStoreAlgorithm, String keyStorePass, Key key,
                                                String secretKeyEntryAliasName, String secretPassword,
                                                Set<KeyStore.Entry.Attribute> attributeSet) throws KeyChainException {
        try {
            //Creating the KeyStore.ProtectionParameter object
            KeyStore.ProtectionParameter protectionParam = new KeyStore.PasswordProtection(secretPassword.toCharArray());

            //Creating SecretKeyEntry object
            KeyStore.SecretKeyEntry secretKeyEntry = null;

            if (attributeSet != null) {
                secretKeyEntry = new KeyStore.SecretKeyEntry((SecretKey) key, attributeSet);
            } else {
                secretKeyEntry = new KeyStore.SecretKeyEntry((SecretKey) key);
            }
            keyStore.setEntry(secretKeyEntryAliasName, secretKeyEntry, protectionParam); //"secretKeyAlias"

        } catch (KeyStoreException e) {
            e.printStackTrace();
            throw new KeyChainException("No Provider supports a KeyStoreSpi implementation for the specified type", e);
        }
    }

    /*

    public static KeyStore storeSecretKeyInKeyStore(byte[] keyStoreByteArray, String keyStoreAlgorithm, String keyStorePass, Key key,
                                                  String secretKeyEntryAliasName, String secretPassword,
                                                  Set<KeyStore.Entry.Attribute> attributeSet) {

        KeyStore keyStore = null;
        try {
            //Creating the KeyStore object
            keyStore = KeyStore.getInstance(keyStoreAlgorithm);  //JCEKS PKCS12

            //Loading the KeyStore object
            char[] keyStorePasswordCharArray = keyStorePass.toCharArray(); // changeit
            char[] secretPasswordCharArray = secretPassword.toCharArray();

            //Check if key store already exists
            if (keyStoreByteArray == null) {
                keyStore.load(null, keyStorePasswordCharArray);
            } else {
                InputStream myInputStream = new ByteArrayInputStream(keyStoreByteArray);
                keyStore.load(myInputStream, keyStorePass.toCharArray());
            }

            //Creating the KeyStore.ProtectionParameter object
            KeyStore.ProtectionParameter protectionParam = new KeyStore.PasswordProtection(secretPasswordCharArray);

            //Creating SecretKeyEntry object
            KeyStore.SecretKeyEntry secretKeyEntry = null;
            if (attributeSet != null) {
                secretKeyEntry = new KeyStore.SecretKeyEntry((SecretKey) key, attributeSet);
            } else {
                secretKeyEntry = new KeyStore.SecretKeyEntry((SecretKey) key);
            }
            keyStore.setEntry(secretKeyEntryAliasName, secretKeyEntry, protectionParam); //"secretKeyAlias"

//            //Storing the KeyStore object
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            keyStore.store(out, keyStorePasswordCharArray);
//            updatedKeyStoreByteArray = new byte[out.size()];
//            System.out.println(out.size());
//            updatedKeyStoreByteArray = out.toByteArray();
//
//            out.close();

        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return keyStore;
    }

     */

    public static byte[] writeKeyStoreIntoByteArray(KeyStore keyStore, String keyStorePassword) {
        byte[] keyStoreByteArray = null;
        try{
            //Storing the KeyStore object
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            keyStore.store(out, keyStorePassword.toCharArray());
            keyStoreByteArray = new byte[out.size()];
            keyStoreByteArray = out.toByteArray();
            out.close();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return keyStoreByteArray;
    }

    public static KeyStore readKeyStoreFromByteArray(byte[] keyStoreByteArray, String keyStoreAlgorithm,
                                                     String keyStorePass) {
        KeyStore keyStore = null;
        try {
            keyStore = KeyStore.getInstance(keyStoreAlgorithm);

            InputStream myInputStream = new ByteArrayInputStream(keyStoreByteArray);

            keyStore.load(myInputStream, keyStorePass.toCharArray());
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return keyStore;
    }

    public static SecretKey readSecretKeyFromKeyStore(KeyStore keyStore, String secretKeyEntryAliasName,
                                                      String secretKeyPassword) {
        SecretKey secretKey = null;
        try {
            secretKey = (SecretKey) keyStore.getKey(secretKeyEntryAliasName, secretKeyPassword.toCharArray());
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return secretKey;
    }

    public static KeyStore.Entry readKeyStoreEntryOfSecretKeyFromKeyStore(KeyStore keyStore,
                                       String secretKeyEntryAliasName, String secretKeyPassword) {
        KeyStore.Entry keyStoreEntry = null;
        try {

            KeyStore.ProtectionParameter protectionParam = new KeyStore
                    .PasswordProtection(secretKeyPassword.toCharArray());
            keyStoreEntry = keyStore.getEntry(secretKeyEntryAliasName,protectionParam);
        } catch (UnrecoverableEntryException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return keyStoreEntry;
    }

//    public static SecretKey readSecretKeyFromKeyStore(byte[] keyStoreByteArray, String keyStoreAlgorithm, String keyStorePass,
//                                                      String secretKeyEntryAliasName, String secretKeyPassword) {
//        SecretKey secretKey = null;
//        try {
//            KeyStore ks = KeyStore.getInstance(keyStoreAlgorithm);
//
//            InputStream myInputStream = new ByteArrayInputStream(keyStoreByteArray);
//
//            ks.load(myInputStream, keyStorePass.toCharArray());
//
//            secretKey = (SecretKey) ks.getKey(secretKeyEntryAliasName, secretKeyPassword.toCharArray());
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (KeyStoreException e) {
//            e.printStackTrace();
//        } catch (UnrecoverableKeyException e) {
//            e.printStackTrace();
//        } catch (UnrecoverableEntryException e) {
//            e.printStackTrace();
//        } catch (CertificateException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        return secretKey;
//    }
//
//    public static KeyStore.Entry readSecretKeyEntryFromKeyStore(byte[] keyStoreByteArray, String keyStoreAlgorithm, String keyStorePass,
//                                                                String secretKeyEntryAliasName, String secretKeyPassword) {
//        KeyStore.Entry keyStoreEntry = null;
//        try {
//            KeyStore ks = KeyStore.getInstance(keyStoreAlgorithm);
//
//            InputStream myInputStream = new ByteArrayInputStream(keyStoreByteArray);
//
//            ks.load(myInputStream, keyStorePass.toCharArray());
//
//            SecretKey secretKey = (SecretKey) ks.getKey(secretKeyEntryAliasName, secretKeyPassword.toCharArray());
//
//            KeyStore.ProtectionParameter protectionParam = new KeyStore
//                    .PasswordProtection(secretKeyPassword.toCharArray());
//            keyStoreEntry = ks.getEntry(secretKeyEntryAliasName,protectionParam);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (KeyStoreException e) {
//            e.printStackTrace();
//        } catch (UnrecoverableKeyException e) {
//            e.printStackTrace();
//        } catch (UnrecoverableEntryException e) {
//            e.printStackTrace();
//        } catch (CertificateException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        return keyStoreEntry;
//    }

}
