package com.sunseagear.common.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;


public class EncyOrDecyUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(EncyOrDecyUtils.class);
    private static final int SALT_SIZE = 32;
    private static final int ITERATION_COUNT = 50000;
    private static final char[] DEFAULT_KEY = "Tr7WE0J2z3uJod4p".toCharArray();

    private static final Integer KEY_LENGTH = 16;

    public static String MD5(byte[] data) {
        return DigestUtils.md5Hex(data);
    }

    public static String MD5(String data) {
        return DigestUtils.md5Hex(data);
    }

    public static String generateKey(char[] password, int keyLength) {
        try {
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            byte[] salt = new byte[SALT_SIZE];
            random.nextBytes(salt);
            KeySpec keyspec = new PBEKeySpec(password, salt, ITERATION_COUNT, keyLength * 2 * 3);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            SecretKey secretkey = factory.generateSecret(keyspec);
            return Base64.getEncoder().encodeToString(secretkey.getEncoded());
        } catch (Exception e) {
            LOGGER.error("generate Key error", e);
        }
        return null;
    }

    public static String aesEncrypt(String content) {
        return aesEncrypt(content, DEFAULT_KEY);
    }

    public static String aesDecrypt(String content) {
        return aesDecrypt(content, DEFAULT_KEY);
    }

    public static String aesEncrypt(String sSrc, char[] sKey) {
        try {
            if (sKey == null || sKey.length != 16) {
                LOGGER.warn("Key为空或长度不是16位");
                return null;
            }

            SecureRandom random = new SecureRandom();
            byte[] iv = new byte[16];
            random.nextBytes(iv);

            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128);
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] raw = secretKey.getEncoded();

            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivParameterSpec);
            byte[] encrypted = cipher.doFinal(sSrc.getBytes(StandardCharsets.UTF_8));

            // Combine the IV and the encrypted data for storage or transmission
            byte[] combined = new byte[iv.length + encrypted.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);

            return Base64.getEncoder().encodeToString(combined);
        } catch (Exception e) {
            LOGGER.error("aes encrypt string error", e);
        }
        return null;
    }

    public static String aesDecrypt(String sSrc, char[] sKey) {
        try {
            if (sKey == null || sKey.length != 16) {
                LOGGER.warn("Key为空或长度不是16位");
                return null;
            }

            byte[] combined = Base64.getDecoder().decode(sSrc);
            byte[] iv = new byte[16];
            byte[] encrypted = new byte[combined.length - iv.length];

            System.arraycopy(combined, 0, iv, 0, iv.length);
            System.arraycopy(combined, iv.length, encrypted, 0, encrypted.length);

            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128);
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] raw = secretKey.getEncoded();

            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

            cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivParameterSpec);

            try {
                byte[] original = cipher.doFinal(encrypted);
                return new String(original, StandardCharsets.UTF_8);
            } catch (Exception e) {
                LOGGER.error("aes decrypt string error", e);
                return null;
            }
        } catch (Exception e) {
            LOGGER.error("aes decrypt string error", e);
        }
        return null;
    }
}
