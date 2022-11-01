package com.suriya.chain.algorithm;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
import java.security.spec.*;

public final class AsymmetricKey {

    public static KeyPair generateAsymmetricKey(String algorithm, int keySize) {
        KeyPair keyPair = null;
        PrivateKey privateKey = null;
        PublicKey publicKey = null;
        try {
            //Creating KeyPair generator object
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(algorithm); //DSA RSA
            //Initializing the KeyPairGenerator
            keyPairGen.initialize(keySize); //2048

            //Generating the pair of keys
            keyPair = keyPairGen.generateKeyPair();

            //Getting the private key from the key pair
            privateKey = keyPair.getPrivate();

            //Getting the public key from the key pair
            publicKey = keyPair.getPublic();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return keyPair;
    }


//    public PublicKey getPublicKeyFromPrivateKey(String algorithm, PrivateKey privateKey);
////        PublicKey publicKey = null;
////        try {
////            KeyFactory kf = KeyFactory.getInstance(algorithm);
////            switch (kf.getAlgorithm()) {
////                case "RSA":
////                    RSAPrivateKeySpec priv = kf.getKeySpec(privateKey, RSAPrivateKeySpec.class);
////                    RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(priv.getModulus(), BigInteger.valueOf(65537));
////                    publicKey = kf.generatePublic(rsaPublicKeySpec);
////                    break;
////                case "DSA":
////                    DSAPrivateKeySpec dsaPrivateKeySpec = kf.getKeySpec(privateKey, DSAPrivateKeySpec.class);
////                    DSAPrivateKey dsaPrivateKey = (DSAPrivateKey) privateKey;
////                    DSAPublicKeySpec dsaPublicKeySpec = new DSAPublicKeySpec(new BigInteger("test".getBytes(StandardCharsets.UTF_8)), dsaPrivateKey.getParams().getP(), dsaPrivateKey.getParams().getQ(), dsaPrivateKey.getParams().getG());
////                    publicKey = kf.generatePublic(dsaPublicKeySpec);
////                    break;
////                default:
////                    break;
////            }
////        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
////            e.printStackTrace();
////        }
//        return publicKey;
//    }
}
