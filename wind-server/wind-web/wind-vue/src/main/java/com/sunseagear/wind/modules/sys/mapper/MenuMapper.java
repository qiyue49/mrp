package com.sunseagear.wind.modules.sys.mapper;

import com.sunseagear.common.mvc.mapper.BaseTreeMapper;
import com.sunseagear.wind.modules.sys.entity.Menu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MenuMapper extends BaseTreeMapper<Menu> {

    List<Menu> findMenuAndPermissionByUserId(Long userId);

    List<Menu> findMenuByUserId(Long userId);

    /**
     * @param roleId
     * @return
     * @title: findMenuByRoleId
     * @description: 通过角色查找菜单
     * @return: List<Menu>
     */
    List<Menu> findMenuByRoleId(Long roleId);


    /**
     * @param userId
     * @return
     * @title: findMenuByUserId
     * @description: 通过用户查找菜单
     * @return: List<Menu>
     */
    List<Menu> findPermissionByUserId(Long userId);

    /**
     * @param roleId
     * @return
     * @title: findMenuByRoleId
     * @description: 通过角色查找菜单
     * @return: List<Menu>
     */
    List<Menu> findPermissionByRoleId(Long roleId);

    void deleteRoleMenu(Long roleId);

    void deleteRolePermission(Long roleId);

}
