package com.suriya.io;

import java.security.KeyStore;

/**
 * This class acts as a settings for this library. Configure to your needs. Make sure both the construction and
 * resolver settings should be the same.
 */
public class KeyChainSettings {

    /**
     * General settings for building and resolving the keychain
     */
    public static class General {
        /**
         * Configuration for entry alias name length in the keystore entry
         */
        public static int entryAliasNameLength = 10;
        /**
         * Configuration for entry password length in the keystore entry
         */
        public static int keyStorePasswordLength = 16;
        /**
         * Attribute name {@link KeyStore.Entry.Attribute} for the Next entry's name in the keystore
         */
        public static String nextKeyNodeNameAttributeKey = "KC_NEXT_NODE_NAME";
        /**
         * Attribute name {@link KeyStore.Entry.Attribute} for the Next entry's password in the keystore
         */
        public static String nextKeyNodePasswordAttributeKey = "KC_NEXT_NODE_PASSWORD";
    }

    /**
     * Algorithm settings used in the library.
     */
    public static class Algorithm {
        /**
         * This is the algorithm that is used for chain's secretKey generation
         */
        public static String secureRandomKeyAlgorithm = "HmacSHA256";
        /**
         * This is the algorithm that is used for hashing next key node password.
         */
        public static String messageDigestAlgorithm ="SHA-256";
        /**
         * This specifies the keystore type. It only support PKCS12 because it uses PKCS12 attributeSet where Name is
         * the Oid and Value is the encrypted string.
         */
        public final static String keyStoreAlgorithm = "PKCS12";
        /**
         * Specify any AES cipher algorithm, it is used for encrypting the attribute type.
         * See {@link com.suriya.chain.algorithm.Cryptography.AES} for more info
         */
        public static String aesCipherAlgorithm = "AES/ECB/PKCS5Padding";
    }

}
