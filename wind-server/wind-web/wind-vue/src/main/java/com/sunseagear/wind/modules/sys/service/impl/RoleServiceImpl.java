package com.sunseagear.wind.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sunseagear.common.mvc.service.impl.CommonServiceImpl;
import com.sunseagear.wind.modules.sys.entity.Role;
import com.sunseagear.wind.modules.sys.entity.RoleDataRule;
import com.sunseagear.wind.modules.sys.entity.RoleMenu;
import com.sunseagear.wind.modules.sys.entity.UserRole;
import com.sunseagear.wind.modules.sys.mapper.RoleDataRuleMapper;
import com.sunseagear.wind.modules.sys.mapper.RoleMapper;
import com.sunseagear.wind.modules.sys.mapper.RoleMenuMapper;
import com.sunseagear.wind.modules.sys.mapper.UserRoleMapper;
import com.sunseagear.wind.modules.sys.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Transactional
@Service("roleService")
public class RoleServiceImpl extends CommonServiceImpl<RoleMapper, Role> implements IRoleService {

    @Autowired
    private RoleDataRuleMapper roleDataRuleMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Override
    public List<Role> findListByUserId(Long userid) {
        return baseMapper.findRoleByUserId(userid);
    }

    @Override
    public void deleteBatchIds(Collection<? extends Serializable> idList) {
        roleDataRuleMapper.delete(new QueryWrapper<RoleDataRule>().in("role_id", idList));
        roleMenuMapper.delete(new QueryWrapper<RoleMenu>().in("role_id", idList));
        userRoleMapper.delete(new QueryWrapper<UserRole>().in("role_id", idList));
        super.deleteBatchIds(idList);
    }

    @Override
    public boolean deleteById(Serializable id) {
        roleDataRuleMapper.delete(new QueryWrapper<RoleDataRule>().eq("role_id", id));
        roleMenuMapper.delete(new QueryWrapper<RoleMenu>().eq("role_id", id));
        userRoleMapper.delete(new QueryWrapper<UserRole>().eq("role_id", id));
        return super.deleteById(id);
    }
}
