package com.sunseagear.common.sms.utils.cloopen;

import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptUtil {

    public String md5Digest(String src) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] b = md.digest(src.getBytes(StandardCharsets.UTF_8));
        return byte2HexStr(b);
    }

    public String base64Encoder(String src) {
        return Base64.encodeBase64String(src.getBytes(StandardCharsets.UTF_8));
    }

    public String base64Decoder(String dest) {
        return new String(Base64.decodeBase64(dest.getBytes(StandardCharsets.UTF_8)));
    }

    private String byte2HexStr(byte[] b) {
        StringBuilder sb = new StringBuilder();
        for (byte value : b) {
            String s = Integer.toHexString(value & 0xFF);
            if (s.length() == 1) {
                sb.append("0");
            }

            sb.append(s.toUpperCase());
        }
        return sb.toString();
    }
}
