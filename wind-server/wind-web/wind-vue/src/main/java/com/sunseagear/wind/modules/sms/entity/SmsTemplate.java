package com.sunseagear.wind.modules.sms.entity;

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
 * @package com.sunseagear.wind.modules.sms.entity
 * @title: 短信模板实体
 * @description: 短信模板实体 * @date: 2018-09-14 09:47:35
 * @copyright: 2018 www.sunseagear.com Inc. All rights reserved.
 */
@Data
@TableName("sms_template")
@SuppressWarnings("serial")
public class SmsTemplate extends DataEntity<Long> {

    /**
     * id
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
     * 业务类型
     */
    @TableField(value = "business_type")
    private String businessType;
    /**
     * 模版内容
     */
    @TableField(value = "template_content")
    private String templateContent;

}
