package com.sunseagear.wind.config;

import com.sunseagear.common.utils.AscToBcdUtils;
import com.sunseagear.common.utils.RSACryptographyUtils;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.security.PublicKey;

@Component("desencrypt")
@Slf4j
public class DESEncrypt implements StringEncryptor {
//    public final Logger log = LoggerFactory.getLogger(getClass());
    @Override
    public String encrypt(String password) {
        try {
            PrivateKey privateKey = RSACryptographyUtils.getPrivateKey(RSACryptographyUtils.modulusString, RSACryptographyUtils.privateExponentString);
//            System.out.println("-----加密前 =="+ password);
            byte[] encrypt = RSACryptographyUtils.encrypt(password.getBytes(), privateKey);
            String enPassword = AscToBcdUtils.bcd2Str(encrypt);
//            System.out.println("-----加密后 =="+ enPassword);
            return enPassword;
        } catch (Exception e) {
            log.error("加密出错了！！！");
            throw new RuntimeException(e);
        }
    }

    @Override
    public String decrypt(String enPassword) {
        try {
            PublicKey publicKey = RSACryptographyUtils.getPublicKey(RSACryptographyUtils.modulusString, RSACryptographyUtils.publicExponentString);
//            System.out.println("-----解密前 =="+ enPassword);
            byte[] bytes = AscToBcdUtils.ASCII_To_BCD(enPassword.getBytes(), enPassword.getBytes().length);
            byte[] password = RSACryptographyUtils.decrypt(bytes, publicKey);
//            System.out.println("-----解密后 =="+ new String(password));
            return new String(password);
        } catch (Exception e) {
            log.error("解密出错了！！！");
            throw new RuntimeException(e);
        }
    }
}
