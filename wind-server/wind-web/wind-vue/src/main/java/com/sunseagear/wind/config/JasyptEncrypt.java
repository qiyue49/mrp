package com.sunseagear.wind.config;

import com.sunseagear.common.utils.AscToBcdUtils;
import com.sunseagear.common.utils.RSACryptographyUtils;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.security.PublicKey;

@Component("jasyptEncrypt")
@Slf4j
public class JasyptEncrypt implements StringEncryptor {

    /*
     * 生成数据加密字符串的方法
     * 1、Util模块RSACryptographyUtils类中的data就是要加密的内容。
     * 2、运行main函数，会出现公钥，公钥模，公钥指数，私钥，私钥模，私钥指数，加密前，加密后，解密后等等打印信息。
     * 3、如果加密前和解密后内容一样，则说明加密成功，将公钥模，公钥指数 ，私钥指数记录下来保存到下面变量中
     * 4、将加密后的字段复制到yml数据库的密码位置。并且用ENC(包含)
     * */


    //为了保障数据安全，以下参数请自己利用RSACryptographyUtils类生成
    //公钥模
    private static final String MODULUS_STRING = "110164495849272711202946616459372319079286499719250901440671340574963041290097354511674847858255163623581724687248801829049425284007804858435579993841022410424572621928291217650718515503750189445424238314989884966610984102758500224037057166856928439759722922436560678668443479105219017648052800023516140485813";
    //公钥指数
    private static final String PUBLIC_EXPONENT_STRING = "65537";
    //私钥指数
    private static final String PRIVATE_EXPONENT_STRING = "60961374589464335329448433595001243934409634234376812970495854819286962409427814192294429595915006240334693799041229338119023717134245555884839770765193981465801650715324507756935907255074388128225545291549661142722809494073282145759411807472927750931464002229391265490987569890138799235618523643438558669377";


    @Override
    public String encrypt(String password) {
        try {
            PrivateKey privateKey = RSACryptographyUtils.getPrivateKey(MODULUS_STRING, PRIVATE_EXPONENT_STRING);
            byte[] encrypt = RSACryptographyUtils.encrypt(password.getBytes(), privateKey);
            return AscToBcdUtils.bcd2Str(encrypt);
        } catch (Exception e) {
            log.error("加密出错了！！！");
            throw new RuntimeException(e);
        }
    }

    @Override
    public String decrypt(String enPassword) {
        try {
            PublicKey publicKey = RSACryptographyUtils.getPublicKey(MODULUS_STRING, PUBLIC_EXPONENT_STRING);
            byte[] bytes = AscToBcdUtils.ASCII_To_BCD(enPassword.getBytes(), enPassword.getBytes().length);
            byte[] password = RSACryptographyUtils.decrypt(bytes, publicKey);
            return new String(password);
        } catch (Exception e) {
            log.error("解密出错了！！！");
            throw new RuntimeException(e);
        }
    }
}
