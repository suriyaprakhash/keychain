package com.suriya.io;

public class KeyChainSettings {

    public static class KeyStore {
        public String filePath;
        public String fileName;
        public String password;
    }

    public static class General {
        public static boolean shuffleKeyList = true;
    }

    public static class Algorithm {
        public static String secureRandomKeyAlgorithm = "HmacSHA256";
        public static String messageDigestAlgorithm ="SHA-256";
    }

}
