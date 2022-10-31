package com.suriya.algorithm;

import com.suriya.chain.algorithm.SymmetricKey;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.Key;

public class SymmetricKeyTest {

    /**
     *  Test for {@link SymmetricKey#generateSecureRandomKey(String)}  with DES
     */
    @Test
    public void generateSecureRandomKeyTest_DES() {
        System.out.println("Key with DES :");
        System.out.println("MAC : " + SymmetricKey.generateSecureRandomKey("DES"));
    }

    @Test
    public void generateSecureRandomKeyTest_DES_NotSameOnDifferentIterations() {
        System.out.println("Key with DES :");
        System.out.println("MAC : " + SymmetricKey.generateSecureRandomKey("DES"));
        Key iteration1 = SymmetricKey.generateSecureRandomKey("DES");
        Key iteration2 = SymmetricKey.generateSecureRandomKey("DES");
        Assertions.assertTrue(!iteration1.equals(iteration2));
    }


    /**
     *  Test for {@link SymmetricKey#generateSecretKeyFromPassword(String, String)}  with DER
     */
    @Test
    public void generateKeyFromPasswordTest_DER_SameForSamePassword() {
        System.out.println("Key with DER :");
        System.out.println("MAC : " + SymmetricKey.generateSecretKeyFromPassword("DER", "Suriya"));
        Key iteration1 = SymmetricKey.generateSecretKeyFromPassword("DER", "Suriya");
        Key iteration2 = SymmetricKey.generateSecretKeyFromPassword("DER", "Suriya");
        Assertions.assertTrue(iteration1.equals(iteration2));
    }


}
