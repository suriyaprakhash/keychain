package com.suriya.util;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ConversionUtil {

    public static String bytesToHex(byte[] inputBytes) {
        StringBuffer result = new StringBuffer();
        for (byte b : inputBytes) result.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        return result.toString();
    }

    public static String stringToHex(String inputString) {
        byte[] byteString = inputString.getBytes(Charset.forName("UTF-8"));
        BigInteger biStr = new BigInteger(byteString);
        return biStr.toString(16);
    }

    public static String stringToBinary(String inputString) {
        byte[] byteString = inputString.getBytes(Charset.forName("UTF-8"));
        BigInteger biStr = new BigInteger(byteString);
        return biStr.toString(2);
    }

    public static String stringToDecimal(String inputString) {
        byte[] byteString = inputString.getBytes(Charset.forName("UTF-8"));
        BigInteger biStr = new BigInteger(byteString);
        return biStr.toString(10);
    }

}
