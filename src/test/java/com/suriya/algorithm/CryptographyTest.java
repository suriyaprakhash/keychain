package com.suriya.algorithm;

import com.suriya.chain.algorithm.AsymmetricKey;
import com.suriya.chain.algorithm.Cryptography;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class CryptographyTest {
    @Test
    void asymmetricCipherTest() {
        KeyPair keyPair1 = AsymmetricKey.generateAsymmetricKey("RSA", 2048); //DSA RSA
        PrivateKey privateKey1 = keyPair1.getPrivate();
        PublicKey publicKey1 = keyPair1.getPublic();

        KeyPair keyPair2 = AsymmetricKey.generateAsymmetricKey("RSA", 2048); //DSA RSA
        PublicKey publicKey2 = keyPair2.getPublic();

//        RSASupport rsaSupport = new RSASupport();
//        KeyPair rsaKeyPair = rsaSupport.generateKeyPair();
//
//        PublicKey publicKey2 = rsaKeyPair.getPublic();

        String actualString = "hello world";
        byte[] encryptedByteArray = Cryptography.encrypt("RSA/ECB/PKCS1Padding", privateKey1, actualString.getBytes(StandardCharsets.UTF_8));
        String decryptedString = new String(Cryptography.decrypt("RSA/ECB/PKCS1Padding", publicKey1, encryptedByteArray));
        Assertions.assertEquals(actualString, decryptedString);
    }

    @Test
    void symmetricCipherPasswordTest() {
        String actualString = "Java support many secure encryption algorithms but some of them " +
                "are weak to be used in security-intensive applications. " +
                "For example, the Data Encryption Standard (DES) encryption algorithm is considered highly insecure; " +
                "messages encrypted using DES have been decrypted by brute force within a single day " +
                "by machines such as the Electronic Frontier Foundation’s (EFF) Deep Crack. A more secure encryption " +
                "algorithm is AES – Advanced Encryption Standard which is" +
                " a symmetric encryption algorithm. AES encryption is used by the U.S. for securing sensitive but " +
                "unclassified material, so we can say it is enough secure.";
        String encryptedString = Cryptography.AES.encrypt( actualString, "suriya");
        String decryptedString = Cryptography.AES.decrypt( encryptedString, "suriya");
        Assertions.assertEquals(actualString, decryptedString);
    }
}
