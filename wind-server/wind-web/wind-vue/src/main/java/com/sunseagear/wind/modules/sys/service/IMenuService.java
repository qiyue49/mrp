package com.sunseagear.wind.modules.sys.service;

import com.sunseagear.common.mvc.service.ITreeCommonService;
import com.sunseagear.wind.modules.sys.entity.Menu;

import java.util.List;

/**
 * @author jwcg
 * @version V1.0
 * @Title:
 * @Description:
 * @date 2014-12-20 21:33:32
 */
public interface IMenuService extends ITreeCommonService<Menu, Long> {

    List<Menu> getCurrentUserMenus();

    List<Menu> findMenuAndPermissionByUserId(Long userId);

    /**
     * 通过用户ID查找菜单
     *
     * @return
     */
    List<Menu> findMenuByUserId(Long userId);

    /**
     * 通过角色查找菜单
     *
     * @return
     */
    List<Menu> findMenuByRoleId(Long roleId);

    /**
     * 通过用户ID查找菜单
     *
     * @return
     */
    List<Menu> findPermissionByUserId(Long userId);

    /**
     * 通过用户ID查找菜单
     *
     * @return
     */
    List<Menu> findPermissionByRoleId(Long roleId);

    void changeSort(Long menuId, Integer sort);

    void generateButton(Long menuId,
                        String parentPermission,
                        String[] permissions,
                        String[] permissionTitles, Boolean additional);
}
