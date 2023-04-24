package com.sunseagear.common.oss.client;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import com.sunseagear.common.oss.config.OssConfig;
import com.sunseagear.common.oss.config.TencentConfig;
import com.sunseagear.common.oss.exception.OSSException;
import com.sunseagear.common.utils.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

@Slf4j
public class TencentCOS extends AbstractOSSClient {
    public static final String DEFAULT_CONFIG_FILE = "tencent.oss.properties";
    //腾讯云客户端
    private String secretId = "";

    //API密钥secretKey
    private String secretKey = "";

    //存储桶所属地域
    private String region = "";

    //存储桶空间名称
    private String bucketName = "";

    //存储桶访问域名
    private String domain;

    private COSClient client;
    //上传文件前缀路径(eg:/images/) 设置自己的主目录
    private String prefix;

    @Override
    public void init() {
        init(DEFAULT_CONFIG_FILE);
    }

    @Override
    public void init(String propertiesName) {
        PropertiesUtil p = new PropertiesUtil(propertiesName);
        secretId = p.getString("tencent.oss.secret-id");
        secretKey = p.getString("tencent.oss.secret-key");
        region = p.getString("tencent.oss.region");
        bucketName = p.getString("tencent.oss.bucket-name");
        domain = p.getString("tencent.oss.domain");
        prefix = p.getString("tencent.oss.prefix");
        //初始化用户身份信息(secretId,secretKey)
        COSCredentials cosCredentials = new BasicCOSCredentials(secretId, secretKey);
        //设置bucket的区域
        ClientConfig clientConfig = new ClientConfig(new Region(region));
        //生成cos客户端
        client = new COSClient(cosCredentials, clientConfig);
    }

    public void init(OssConfig config) {
        TencentConfig tencent = config.getTencent();
        secretId = tencent.getSecretId();
        secretKey = tencent.getSecretKey();
        region = tencent.getRegion();
        bucketName = tencent.getBucketName();
        domain = tencent.getDomain();
        prefix = tencent.getPrefix();
        COSCredentials cosCredentials = new BasicCOSCredentials(secretId, secretKey);
        ClientConfig clientConfig = new ClientConfig(new Region(region));
        client = new COSClient(cosCredentials, clientConfig);
    }

    /**
     * 一般不推荐使用
     *
     * @return
     */
    public COSClient getClient() {
        return this.client;
    }

    /**
     * 文件上传
     *
     * @param inputStream
     * @param path
     * @return
     */
    @Override
    public String upload(InputStream inputStream, String path) {
        try {
            String fileType = path.substring(path.lastIndexOf("."));
            String fileName = new Date().getTime() + fileType;
            String key = prefix + fileName;
            // 创建上传Object的Metadata
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(inputStream.available());
            objectMetadata.setCacheControl("no-cache");
            objectMetadata.setHeader("Pragma", "no-cache");
            objectMetadata.setContentType(getcontentType(fileType));
            objectMetadata.setContentDisposition("inline;filename=" + fileName);
            //创建存储对象的请求
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, inputStream, objectMetadata);
            //执行上传并返回结果信息
            PutObjectResult putObjectResult = client.putObject(putObjectRequest);
            return domain + "/" + key;
        } catch (IOException e) {
            log.error("上传腾讯云COS服务器异常." + e.getMessage(), e);
            throw new OSSException("上传文件失败", e);
        }
    }

    @Override
    public void delete(String filename) {

        try {
            filename = filename.replace(domain + "/", "");
            client.deleteObject(bucketName, filename);
            log.info("删除" + bucketName + "下的文件" + filename + "成功");
        } catch (Exception e) {
            throw new OSSException("删除文件失败", e);
        }
    }

    /**
     * Description: 判断Cos服务文件上传时文件的contentType
     *
     * @param filenameExtension 文件后缀
     * @return String
     */
    public static String getcontentType(String filenameExtension) {
        if (filenameExtension.equalsIgnoreCase("bmp")) {
            return "image/bmp";
        }
        if (filenameExtension.equalsIgnoreCase("gif")) {
            return "image/gif";
        }
        if (filenameExtension.equalsIgnoreCase("jpeg") || filenameExtension.equalsIgnoreCase("jpg")
                || filenameExtension.equalsIgnoreCase("png")) {
            return "image/jpeg";
        }
        if (filenameExtension.equalsIgnoreCase("html")) {
            return "text/html";
        }
        if (filenameExtension.equalsIgnoreCase("txt")) {
            return "text/plain";
        }
        if (filenameExtension.equalsIgnoreCase("vsd")) {
            return "application/vnd.visio";
        }
        if (filenameExtension.equalsIgnoreCase("pptx") || filenameExtension.equalsIgnoreCase("ppt")) {
            return "application/vnd.ms-powerpoint";
        }
        if (filenameExtension.equalsIgnoreCase("docx") || filenameExtension.equalsIgnoreCase("doc")) {
            return "application/msword";
        }
        if (filenameExtension.equalsIgnoreCase("xml")) {
            return "text/xml";
        }
        return "image/jpeg";
    }
}
