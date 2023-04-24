package com.sunseagear.common.oss.client;

import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.sunseagear.common.oss.config.OssConfig;
import com.sunseagear.common.oss.config.QiniuConfig;
import com.sunseagear.common.oss.exception.OSSException;
import com.sunseagear.common.utils.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

@Slf4j
public class QiniuOSSClient extends AbstractOSSClient {
    public static final String DEFAULT_CONFIG_FILE = "qiniu.oss.properties";
    private UploadManager uploadManager;
    private BucketManager bucketManager;
    private String token;
    //访问域名
    private String domain;

    private String prefix;

    private String bucketName;


    @Override
    public void init() {
        init(DEFAULT_CONFIG_FILE);
    }

    @Override
    public void init(String propertiesName) {
        PropertiesUtil p = new PropertiesUtil(propertiesName);
        String accessKey = p.getString("qiniu.oss.access-key");
        String secretKey = p.getString("qiniu.oss.secret-key");
        bucketName = p.getString("qiniu.oss.bucket-name");
        domain = p.getString("qiniu.oss.domain");
        prefix = p.getString("qiniu.oss.prefix");
        Auth auth = Auth.create(accessKey, secretKey);
        token = auth.uploadToken(bucketName);
        Configuration configuration = new Configuration();
        configuration.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;
        uploadManager = new UploadManager(configuration);
        bucketManager = new BucketManager(auth, configuration);
    }


    @Override
    public void init(OssConfig config) {
        QiniuConfig qiniu = config.getQiniu();
        Auth auth = Auth.create(qiniu.getAccessKey(), qiniu.getSecretKey());
        token = auth.uploadToken(qiniu.getBucketName());
        bucketName = qiniu.getBucketName();
        domain = qiniu.getDomain();
        prefix = qiniu.getPrefix();
        Configuration configuration = new Configuration();
        configuration.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;
        uploadManager = new UploadManager(configuration);
        bucketManager = new BucketManager(auth, configuration);
    }

    @Override
    public String upload(InputStream inputStream, String path) {
        try {
            byte[] data = IOUtils.toByteArray(inputStream);
            return this.upload(data, path);
        } catch (IOException e) {
            throw new OSSException("上传文件失败", e);
        }
    }

    private String upload(byte[] data, String path) {
        try {
            String fileType = path.substring(path.lastIndexOf("."));
            String fileName = new Date().getTime() + "" + fileType;
            String filepath = prefix + fileName;
            Response res = uploadManager.put(data, filepath, token);
            if (!res.isOK()) {
                throw new RuntimeException("上传七牛出错：" + res.toString());
            }
            return domain + "/" + filepath;
        } catch (Exception e) {
            throw new OSSException("上传文件失败，请核对七牛配置信息");
        }
    }

    @Override
    public void delete(String filename) {
        try {
            filename = filename.replace(domain + "/", "");
            Response response = bucketManager.delete(bucketName, filename);
            log.info("删除" + bucketName + "下的文件" + filename + "成功");
        } catch (Exception e) {
            throw new OSSException("删除文件失败", e);
        }
    }
}
