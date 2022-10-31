package com.suriya.algorithm;

import com.suriya.chain.algorithm.Hash;
import com.suriya.util.ConversionUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class HashTest {

    /**
     * Test for {@link Hash#generateMessageDigest(String, byte[])} with SHA256
     */
    @Test
    public void generateMessageDigest_SHA256() {
        System.out.println("Message digest with SHA-256 :");
        byte[] iteration1 = Hash.generateMessageDigest("SHA-256", "Suriya".getBytes(StandardCharsets.UTF_8));
        byte[] iteration2 = Hash.generateMessageDigest("SHA-256", "Suriya".getBytes(StandardCharsets.UTF_8));
        // check byte array matches
        Assertions.assertTrue(Arrays.equals(iteration1, iteration2));
        // with hex conversion
        Assertions.assertEquals(ConversionUtil.bytesToHex(iteration1), ConversionUtil.bytesToHex(iteration2));
    }
}
