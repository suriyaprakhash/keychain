package com.suriya.algorithm;

import com.suriya.chain.algorithm.AsymmetricKey;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.*;

public class AsymmetricKeyTest {

    @Test
    public void generateAsymmetricKey_Test_DSA() {
        KeyPair keyPair1 = AsymmetricKey.generateAsymmetricKey("RSA", 2048); //DSA RSA
        PrivateKey privateKey1 = keyPair1.getPrivate();
        PublicKey publicKey1 = keyPair1.getPublic();

        KeyPair keyPair2 = AsymmetricKey.generateAsymmetricKey("RSA", 2048); //DSA RSA
        PrivateKey privateKey2 = keyPair2.getPrivate();
        PublicKey publicKey2 = keyPair2.getPublic();

        Assertions.assertNotEquals(keyPair1.getPublic(), keyPair2.getPublic());


    }

//    @Test
//    public void getPublicKeyFromPrivateKey_Test_RSA() {
//        KeyPair keyPair = AsymmetricKey.generateAsymmetricKey("DSA", 2048);
//        PublicKey publicKey1 = keyPair.getPublic();
//        PrivateKey privateKey = keyPair.getPrivate();
//        PublicKey publicKey2 = AsymmetricKey.getPublicKeyFromPrivateKey("DSA", keyPair.getPrivate());
//        Assertions.assertEquals(publicKey1, publicKey2);
//    }
}
