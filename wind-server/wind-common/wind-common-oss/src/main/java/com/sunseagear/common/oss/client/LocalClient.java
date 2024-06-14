package com.sunseagear.common.oss.client;

import com.sunseagear.common.oss.config.LocalConfig;
import com.sunseagear.common.oss.config.OssConfig;
import com.sunseagear.common.oss.exception.OSSException;
import com.sunseagear.common.utils.FileUtils;
import com.sunseagear.common.utils.PropertiesUtil;

import java.io.File;
import java.io.InputStream;

/**
 * All rights Reserved, Designed By kjt.gzst.gov.cn
 *
 * @version V1.0
 * @title: IOSSClient.java
 * @description: 这里为本地操作 * @date: 2017年8月11日 下午9:55:00
 * @copyright: 2017 kjt.gzst.gov.cn Inc. All rights reserved.
 */
public class LocalClient extends AbstractOSSClient {

    public static final String DEFAULT_CONFIG_FILE = "local.properties";
    private String domain;//本地域名配置
    private String uploadFilePath;//上传路径

    @Override
    public void init() {
        init(DEFAULT_CONFIG_FILE);
    }

    @Override
    public void init(String propertiesName) {
        PropertiesUtil p = new PropertiesUtil(propertiesName);
        domain = p.getString("local.domain");
        uploadFilePath = p.getString("local.upload-file-path");

    }

    @Override
    public void init(OssConfig config) {
        LocalConfig localConfig = config.getLocal();
        domain = localConfig.getDomain();
        uploadFilePath = localConfig.getUploadFilePath();
    }


    /**
     * 文件上传
     */
    @Override
    public String upload(InputStream inputStream, String path) {
        String fullPath = uploadFilePath + File.separator + path;
        try {
            FileUtils.saveFile(inputStream.readAllBytes(), fullPath);
        } catch (Exception e) {
            throw new OSSException("上传文件失败", e);
        }
        return domain + "/" + path;
    }

    @Override
    public void delete(String filename) {
        filename = filename.replace(domain, "");
        FileUtils.delFile(uploadFilePath + filename);
    }

}
