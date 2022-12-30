package com.sunseagear.wind.common.helper;

import com.sunseagear.common.oss.OSSUploadHelper;
import com.sunseagear.common.oss.config.OssConfig;
import com.sunseagear.common.oss.exception.FileNameLengthLimitExceededException;
import com.sunseagear.common.oss.exception.InvalidExtensionException;
import com.sunseagear.common.utils.ArrayUtils;
import com.sunseagear.common.utils.IpUtils;
import com.sunseagear.common.utils.StringUtils;
import com.sunseagear.common.utils.UserUtils;
import com.sunseagear.common.utils.entity.Principal;
import com.sunseagear.wind.modules.oss.entity.Attachment;
import com.sunseagear.wind.modules.oss.service.IAttachmentService;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * All rights Reserved, Designed By www.sunseagear.com
 *
 * @version V1.0
 * @package com.sunseagear.oss.controller
 * @title: 附件上传助手
 * @description: 附件上传助手 * @date: 2018-04-25 14:25:55
 * @copyright: 2018 www.sunseagear.com Inc. All rights reserved.
 */
@Component("attachmentHelper")
@EnableConfigurationProperties({OssConfig.class})
public class AttachmentHelper {

    @Autowired
    private IAttachmentService attachmentService;
    @Autowired
    private OssConfig ossConfig;

    private OSSUploadHelper uploadHelper;

    @PostConstruct
    public void initHelper() {
        uploadHelper = new OSSUploadHelper();
        uploadHelper.init(ossConfig);
    }


    public String upload(HttpServletRequest request, MultipartFile[] file, String dir) throws InvalidExtensionException, FileUploadBase.FileSizeLimitExceededException, FileNameLengthLimitExceededException, IOException {
        List<String> attachmentList = new ArrayList<>();

        for (MultipartFile item : file) {
            Attachment attachment = upload(request, item, dir);
            attachmentList.add(attachment.getFilePath());
        }

        return ArrayUtils.join(attachmentList, ",");
    }


    public Attachment upload(HttpServletRequest request, MultipartFile file) throws FileSizeLimitExceededException,
            IOException, FileNameLengthLimitExceededException, InvalidExtensionException {
        String basePath = request.getParameter("directory");
        return upload(request, file, basePath);
    }

    public Attachment upload(HttpServletRequest request, MultipartFile file, String directory) throws FileSizeLimitExceededException,
            IOException, FileNameLengthLimitExceededException, InvalidExtensionException {
        String url = uploadHelper.upload(request, file, directory);
        String filename = file.getOriginalFilename();
        long size = file.getSize();
        //file.getContentType();
        String realFileName = StringUtils.getFileNameNoEx(filename);
        String fileExtension = StringUtils.getExtensionName(filename);
        // 保存上传的信息
        Attachment attachment = new Attachment();
        attachment.setFileExtension(fileExtension);
        attachment.setFileName(realFileName);
        attachment.setContentType(file.getContentType());
        attachment.setFilePath(url);
        attachment.setFileSize(size);
        attachment.setStatus("1");
        attachment.setUploadIp(IpUtils.getIpAddr(request));
        attachment.setUploadTime(new Date());
        Principal principal = UserUtils.getPrincipal();
        attachment.setUserId(principal == null ? null : principal.getId());
        attachment.setBasePath(directory);
        attachmentService.insert(attachment);
        return attachment;
    }

}
