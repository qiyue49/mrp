package com.sunseagear.wind.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sunseagear.common.datarule.handler.DataRuleHandler;
import com.sunseagear.common.mvc.service.impl.TreeCommonServiceImpl;
import com.sunseagear.common.utils.StringUtils;
import com.sunseagear.wind.modules.sys.entity.Organization;
import com.sunseagear.wind.modules.sys.mapper.OrganizationMapper;
import com.sunseagear.wind.modules.sys.service.IOrganizationService;
import com.sunseagear.wind.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;

@Transactional
@Service("organizationService")
public class OrganizationServiceImpl extends TreeCommonServiceImpl<OrganizationMapper, Organization, Long>
        implements IOrganizationService {

    //所有数据更新都需要刷新数据权限
    @Autowired
    private DataRuleHandler dataRuleHandler;

    @Override
    public List<Organization> selectList(Wrapper<Organization> wrapper) {
        List<Organization> list = super.selectList(wrapper);
        return getTreeTables(list);
    }

    private List<Organization> getTreeTables(List<Organization> treeNodeList) {
        List<Organization> TreeTableListAll = list(new QueryWrapper<Organization>().eq("tenant_id", UserUtils.getTenantId()));
        HashMap<String, Organization> TreeTableHashMapAll = new HashMap<>();
        TreeTableListAll.forEach(Organization -> {
            TreeTableHashMapAll.put(Organization.getId().toString(), Organization);
        });
        HashMap<String, Organization> TreeTableHashMap = new HashMap<>();
        treeNodeList.forEach(treeNode -> {
            String parentIds = treeNode.getParentIds();
            if (!StringUtils.isEmpty(parentIds)) {
                Arrays.asList(parentIds.split("/")).forEach(id -> {
                    Organization Organization = TreeTableHashMapAll.get(id);
                    TreeTableHashMap.put(id, Organization);
                });
            }
            TreeTableHashMap.put(treeNode.getId().toString(), TreeTableHashMapAll.get(treeNode.getId().toString()));

        });
        List<Organization> TreeTableList = new ArrayList<>(TreeTableHashMap.values());
        TreeTableList.sort(new Comparator<Organization>() {
            @Override
            public int compare(Organization o1, Organization o2) {
                return o1.getCreateDate().compareTo(o2.getCreateDate());
            }
        });
        return TreeTableList;
    }


    @Override
    public List<Organization> findListByUserId(Long userId) {
        return baseMapper.findListByUserId(userId);
    }

    @Override
    public boolean save(Organization entity) {
        boolean save = super.save(entity);
        dataRuleHandler.refreshTreeEntity("sys_organization");
        return save;
    }

    @Override
    public boolean saveOrUpdate(Organization entity) {
        boolean result = super.saveOrUpdate(entity);
        dataRuleHandler.refreshTreeEntity("sys_organization");
        return result;
    }

    @Override
    public boolean removeById(Serializable id) {
        boolean result = super.removeById(id);
        dataRuleHandler.refreshTreeEntity("sys_organization");
        return result;
    }

    @Override
    public boolean updateById(Organization entity) {
        boolean result = super.updateById(entity);
        dataRuleHandler.refreshTreeEntity("sys_organization");
        return result;
    }

    @Override
    public boolean removeByIds(Collection<?> idList) {
        boolean result = super.removeByIds(idList);
        dataRuleHandler.refreshTreeEntity("sys_organization");
        return result;
    }
}
