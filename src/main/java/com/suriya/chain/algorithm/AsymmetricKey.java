package com.suriya.chain.algorithm;

import java.security.*;

public final class AsymmetricKey {

    public static KeyPair generateAsymmetricKey(String algorithm, int keySize) {
        KeyPair keyPair = null;
        try {
            //Creating KeyPair generator object
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(algorithm); //DSA RSA
            //Initializing the KeyPairGenerator
            keyPairGen.initialize(keySize); //2048

            //Generating the pair of keys
            keyPair = keyPairGen.generateKeyPair();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return keyPair;
    }
}
