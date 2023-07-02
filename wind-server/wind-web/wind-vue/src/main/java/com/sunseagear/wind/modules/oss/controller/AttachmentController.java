package com.sunseagear.wind.modules.oss.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunseagear.common.http.Response;
import com.sunseagear.common.mvc.controller.BaseBeanController;
import com.sunseagear.common.oss.exception.FileNameLengthLimitExceededException;
import com.sunseagear.common.oss.exception.InvalidExtensionException;
import com.sunseagear.common.utils.MessageUtils;
import com.sunseagear.common.utils.StringUtils;
import com.sunseagear.wind.aspectj.annotation.Log;
import com.sunseagear.wind.aspectj.enums.LogType;
import com.sunseagear.wind.common.helper.AttachmentHelper;
import com.sunseagear.wind.common.response.ResponseError;
import com.sunseagear.wind.modules.oss.entity.Attachment;
import com.sunseagear.wind.modules.oss.service.IAttachmentService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileUploadBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * All rights Reserved, Designed By www.sunseagear.com
 *
 * @version V1.0
 * @package com.sunseagear.oss.controller
 * @title: 附件管理控制器
 * @description: 附件管理控制器 * @date: 2018-04-25 14:25:55
 * @copyright: 2018 www.sunseagear.com Inc. All rights reserved.
 */
@RestController
@RequestMapping("oss/attachment")
@PreAuthorize("hasAuthority('oss:attachment')")
@Log(title = "附件日志")
public class AttachmentController extends BaseBeanController<Attachment> {

    @Autowired
    private IAttachmentService attachmentService;

    @Autowired
    private AttachmentHelper attachmentHelper;

    /**
     * 根据页码和每页记录数，以及查询条件动态加载数据
     *
     * @param request
     * @throws IOException
     */
    @GetMapping(value = "list")
    @Log(logType = LogType.SELECT)
    @PreAuthorize("hasAuthority('oss:attachment:list')")
    public String list(HttpServletRequest request) throws IOException {
        //加入条件
        QueryWrapper<Attachment> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("upload_time");
        String fileName = request.getParameter("fileName");
        if (!StringUtils.isEmpty(fileName)) {
            queryWrapper.like("file_name", fileName);
        }
        // 预处理
        Page pageBean = attachmentService.selectPage(getPage(), queryWrapper);
        return Response.successPageJson(pageBean);
    }

    @PostMapping("delete")
    @Log(logType = LogType.DELETE)
    @PreAuthorize("hasAuthority('oss:attachment:delete')")
    public String batchDelete(@RequestParam(value = "ids", required = false) String[] ids) {
        List<Serializable> idList = java.util.Arrays.asList(ids);
        attachmentService.deleteBatchIds(idList);
        return Response.ok("删除成功");
    }

    /**
     * @param request,file,directory
     * @return
     * @title: ajaxUpload
     * @description: 文件上传
     * @return: AjaxUploadResponse
     */
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public String upload(HttpServletRequest request, MultipartFile[] file, @RequestParam(required = false, defaultValue = "") String dir) {
        try {
            return Response.successJson((Object) attachmentHelper.upload(request, file, dir));
        } catch (IOException e) {
            return Response.error(ResponseError.NORMAL_ERROR, MessageUtils.getMessage("upload.server.error"));
        } catch (InvalidExtensionException e) {
            return Response.error(ResponseError.NORMAL_ERROR, MessageUtils.getMessage("upload.server.error"));
        } catch (FileUploadBase.FileSizeLimitExceededException e) {
            return Response.error(ResponseError.NORMAL_ERROR, MessageUtils.getMessage("upload.server.error"));
        } catch (FileNameLengthLimitExceededException e) {
            return Response.error(ResponseError.NORMAL_ERROR, MessageUtils.getMessage("upload.server.error"));
        }
    }


}
