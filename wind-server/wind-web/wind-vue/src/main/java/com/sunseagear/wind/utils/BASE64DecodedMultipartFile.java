package com.sunseagear.wind.utils;

import org.apache.commons.codec.binary.Base64;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.security.SecureRandom;

/**
 * base64转MultipartFile
 */
public class BASE64DecodedMultipartFile implements MultipartFile {

    private final byte[] imgContent;
    private final String header;
    private SecureRandom random = new SecureRandom();

    public BASE64DecodedMultipartFile(byte[] imgContent, String header) {
        this.imgContent = imgContent;
        this.header = header.split(";")[0];
    }

    @Override
    public String getName() {
        return System.currentTimeMillis() + "" + random.nextInt(10) + "." + header.split("/")[1];
    }

    @Override
    public String getOriginalFilename() {
        return System.currentTimeMillis() + "" + random.nextInt(10) + "." + header.split("/")[1];
    }

    @Override
    public String getContentType() {
        return header.split(":")[1];
    }

    @Override
    public boolean isEmpty() {
        return imgContent == null || imgContent.length == 0;
    }

    @Override
    public long getSize() {
        return imgContent.length;
    }

    @Override
    public byte[] getBytes() {
        return imgContent;
    }

    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(imgContent);
    }

    @Override
    public void transferTo(File dest) throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream(dest)) {
            outputStream.write(imgContent);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Invalid file destination", e);
        }
    }

    /**
     * base64转MultipartFile文件
     */
    public static MultipartFile base64ToMultipart(String base64) {
        String[] baseStrs = base64.split(",");

        byte[] b = Base64.decodeBase64(baseStrs[1]);

        for (int i = 0; i < b.length; ++i) {
            if (b[i] < 0) {
                b[i] += (byte) 256;
            }
        }

        return new BASE64DecodedMultipartFile(b, baseStrs[0]);
    }
}
