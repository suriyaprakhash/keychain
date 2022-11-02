package com.suriya.algorithm;

import com.suriya.chain.algorithm.AsymmetricKey;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class AsymmetricKeyTest {

    @Test
    public void generateAsymmetricKey_Test_RSA() {
        KeyPair keyPair = AsymmetricKey.generateAsymmetricKey("DSA", 2048); //DSA RSA
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();
    }
}
