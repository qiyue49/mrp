package com.sunseagear.wind.modules.email.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sunseagear.common.mvc.entity.DataEntity;
import lombok.Data;

/**
 * All rights Reserved, Designed By www.sunseagear.com
 *
 * @version V1.0
 * @package com.sunseagear.wind.modules.email.entity
 * @title: 邮件模板实体
 * @description: 邮件模板实体 * @date: 2018-09-12 10:59:18
 * @copyright: 2018 www.sunseagear.com Inc. All rights reserved.
 */

@Data
@TableName("email_template")
@SuppressWarnings("serial")
public class EmailTemplate extends DataEntity<Long> {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 模版名称
     */
    @TableField(value = "name")
    private String name;
    /**
     * 模版编码
     */
    @TableField(value = "code")
    private String code;
    /**
     * 模版主题
     */
    @TableField(value = "template_subject")
    private String templateSubject;
    /**
     * 模版内容
     */
    @TableField(value = "template_content")
    private String templateContent;

}
