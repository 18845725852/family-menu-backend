package com.example.familymenu.auth.api;

import java.security.SecureRandom;

public final class DefaultNicknameGenerator {
    private static final char[] LETTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final char[] DIGITS = "0123456789".toCharArray();
    private static final char[] ALL = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
    private static final SecureRandom RANDOM = new SecureRandom();

    private DefaultNicknameGenerator() {
    }

    public static String generate() {
        char[] suffix = new char[6];
        suffix[0] = LETTERS[RANDOM.nextInt(LETTERS.length)];
        suffix[1] = DIGITS[RANDOM.nextInt(DIGITS.length)];
        for (int i = 2; i < suffix.length; i++) {
            suffix[i] = ALL[RANDOM.nextInt(ALL.length)];
        }
        for (int i = suffix.length - 1; i > 0; i--) {
            int target = RANDOM.nextInt(i + 1);
            char value = suffix[i];
            suffix[i] = suffix[target];
            suffix[target] = value;
        }
        return "微信用户" + new String(suffix);
    }
}
