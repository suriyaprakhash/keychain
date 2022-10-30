package com.suriya.io;

public class KeyChainSettings {

    public static class General {
        public static int entryAliasNameLength = 10;
        public static int keyStorePasswordLength = 16;
        public static String nextKeyNodeNameAttributeKey = "KC_NEXT_NODE_NAME";
        public static String nextKeyNodePasswordAttributeKey = "KC_NEXT_NODE_PASSWORD";
    }

    public static class Algorithm {
        public static String secureRandomKeyAlgorithm = "HmacSHA256";
        public static String messageDigestAlgorithm ="SHA-256";
        public static String keyStoreAlgorithm = "PKCS12";
    }

}
