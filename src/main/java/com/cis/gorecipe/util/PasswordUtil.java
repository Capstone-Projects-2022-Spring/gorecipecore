package com.cis.gorecipe.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtil {

    private static final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public static String hash(String password) {

        return bCryptPasswordEncoder.encode(password);

    }
}
