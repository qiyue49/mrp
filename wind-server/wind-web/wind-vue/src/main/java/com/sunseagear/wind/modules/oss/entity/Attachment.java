package com.sunseagear.wind.modules.oss.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sunseagear.common.mvc.entity.DataEntity;
import lombok.Data;

import java.util.Date;

/**
 * All rights Reserved, Designed By www.sunseagear.com
 *
 * @version V1.0
 * @package com.sunseagear.oss.entity
 * @title: 附件实体
 * @description: 附件实体 * @date: 2018-04-25 15:55:59
 * @copyright: 2018 www.sunseagear.com Inc. All rights reserved.
 */

@Data
@TableName("oss_attachment")
@SuppressWarnings("serial")
public class Attachment extends DataEntity<Long> {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 文件名称
     */
    @TableField(value = "file_name")
    private String fileName;
    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    private Long userId;
    /**
     * 上传时间
     */
    @TableField(value = "upload_time")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date uploadTime;
    /**
     * 上传的ID
     */
    @TableField(value = "upload_ip")
    private String uploadIp;
    /**
     * 文件扩展名
     */
    @TableField(value = "file_extension")
    private String fileExtension;
    /**
     * 文件路径
     */
    @TableField(value = "file_path")
    private String filePath;
    /**
     * 文件大小
     */
    @TableField(value = "file_size")
    private Long fileSize;
    /**
     * content_type
     */
    @TableField(value = "content_type")
    private String contentType;
    /**
     * 状态
     */
    @TableField(value = "status")
    private String status;
    /**
     * oss的根路径
     */
    @TableField(value = "base_path")
    private String basePath;

}
