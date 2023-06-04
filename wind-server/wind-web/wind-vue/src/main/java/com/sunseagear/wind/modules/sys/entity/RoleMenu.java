package com.sunseagear.wind.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @version V1.0
 * @Title:
 * @Description:
 * @date 2017-02-21 12:54:43
 */
@Data
@TableName("sys_role_menu")
@SuppressWarnings("serial")
public class RoleMenu implements java.io.Serializable {

    /**
     * 菜单编号
     */
    @TableField(value = "menu_id")
    private String menuId;
    /**
     * 角色编号
     */
    @TableField(value = "role_id")
    private Long roleId;
}
