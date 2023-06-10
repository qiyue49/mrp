package com.sunseagear.wind.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sunseagear.common.mvc.entity.AbstractEntity;
import lombok.Data;

@Data
@TableName("sys_user_role")
public class UserRole extends AbstractEntity<Long> {

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 用户编号
     */
    private Long userId;
    /**
     * 角色编号
     */
    private Long roleId;

}
