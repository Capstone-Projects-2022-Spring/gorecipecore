package com.cis.gorecipe.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class PasswordUtil {

    public static String hash(String password) throws NoSuchAlgorithmException {

        SecureRandom random = new SecureRandom(new byte[]{1, 2, 3, 4});
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(salt);

        return new String(md.digest(password.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
    }
}
