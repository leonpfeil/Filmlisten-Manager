package com.sep.client.extras;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encryption {
    public static String encryptString(String input) throws NoSuchAlgorithmException {
        MessageDigest md= MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(input.getBytes());
        BigInteger bigInt = new BigInteger(1, messageDigest);

        return bigInt.toString(16);
    }
}
