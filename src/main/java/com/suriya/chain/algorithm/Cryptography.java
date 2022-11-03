package com.suriya.chain.algorithm;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Arrays;
import java.util.Base64;

import static com.suriya.io.KeyChainSettings.Algorithm.*;

public class Cryptography {

    public static byte[] encrypt(String cipherAlgorithm, PrivateKey privateKey, byte[] actualData) {
        byte[] encryptedData = null;
        try {
            Cipher cipher = Cipher.getInstance(cipherAlgorithm); //
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            cipher.update(actualData);
            encryptedData = cipher.doFinal();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return encryptedData;
    }

    public static  byte[] decrypt(String cipherAlgorithm, PublicKey publicKey, byte[] encryptedData) {
        byte[] decryptedData = null;
        try {
            Cipher cipher = Cipher.getInstance(cipherAlgorithm); //
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            cipher.update(encryptedData);
            decryptedData = cipher.doFinal();

        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return decryptedData;
    }


    public static class AES {

        private static String secretKeySpecAlgorithm = "AES";
//        private static String aesCipherAlgorithm = "AES/ECB/PKCS5Padding";
        private static String messageDigestAlgorithm = "SHA-1";

        private static SecretKeySpec setKey(final String myKey) {
            MessageDigest sha = null;
            SecretKeySpec secretKey = null;
            byte[] key = null;
            try {
                key = myKey.getBytes(StandardCharsets.UTF_8);
                sha = MessageDigest.getInstance(messageDigestAlgorithm);
                key = sha.digest(key);
                key = Arrays.copyOf(key, 16);
                secretKey = new SecretKeySpec(key, secretKeySpecAlgorithm);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return secretKey;
        }

        public static String encrypt(final String strToEncrypt, final String secret) {
            try {
                Cipher cipher = Cipher.getInstance(aesCipherAlgorithm);
                cipher.init(Cipher.ENCRYPT_MODE, setKey(secret));
                return Base64.getEncoder()
                        .encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
            } catch (Exception e) {
                System.out.println("Error while encrypting: " + e.toString());
            }
            return null;
        }

        public static String decrypt(final String strToDecrypt, final String secret) {
            try {
                Cipher cipher = Cipher.getInstance(aesCipherAlgorithm);
                cipher.init(Cipher.DECRYPT_MODE, setKey(secret));
                return new String(cipher.doFinal(Base64.getDecoder()
                        .decode(strToDecrypt)));
            } catch (Exception e) {
                System.out.println("Error while decrypting: " + e.toString());
            }
            return null;
        }
    }

}
