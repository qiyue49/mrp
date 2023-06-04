package com.sunseagear.wind.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sunseagear.common.mvc.entity.AbstractEntity;
import lombok.Data;

@Data
@TableName("sys_user_role")
public class UserRole extends AbstractEntity<Long> {

    private Long userId; //用户编号
    private Long roleId; //角色编号

}
