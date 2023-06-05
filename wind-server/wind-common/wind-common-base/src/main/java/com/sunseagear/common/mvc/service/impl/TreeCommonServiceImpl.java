package com.sunseagear.common.mvc.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.sunseagear.common.mvc.entity.TreeEntity;
import com.sunseagear.common.mvc.mapper.BaseTreeMapper;
import com.sunseagear.common.mvc.service.ITreeCommonService;
import com.sunseagear.common.utils.ObjectUtils;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

@Transactional
public class TreeCommonServiceImpl<M extends BaseTreeMapper<T>, T extends TreeEntity<ID> & Serializable, ID extends Serializable>
        extends CommonServiceImpl<M, T> implements ITreeCommonService<T, ID> {

    @Override
    public T selectById(Serializable id) {
        return baseMapper.selectByTreeId(id);
    }

    @Override
    public List<T> selectTreeList(Wrapper<T> wrapper) {
        return baseMapper.selectTreeList(wrapper);
    }


    @Override
    public boolean insert(T entity) {
        if (isDemo) {
            return true;
        }

        if (!ObjectUtils.isNullOrEmpty(entity.getParentId())) {
            T parent = selectById(entity.getParentId());
            entity.setParentId(parent.getId());
            entity.setParentIds(parent.makeSelfAsNewParentIds());
        } else {
            entity.setParentId(null);
        }
        return super.save(entity);
    }

    @Override
    public boolean insertOrUpdate(T entity) {
        if (isDemo) {
            return true;
        }

        if (!ObjectUtils.isNullOrEmpty(entity.getParentId())) {
            T parent = selectById(entity.getParentId());
            updateSelftAndChild(entity, parent.getId(), parent.makeSelfAsNewParentIds());
        } else {
            entity.setParentId(null);
            updateSelftAndChild(entity, null, null);
        }
        return true;
    }


    private void updateSelftAndChild(T entity, ID newParentId, String newParentIds) {
        if (isDemo) {
            return;
        }

        T oldEntity = selectById(entity.getId());
        String oldChildrenParentIds = oldEntity.makeSelfAsNewParentIds();
        entity.setParentId(newParentId);
        entity.setParentIds(newParentIds);
        super.insertOrUpdate(entity);
        String newChildrenParentIds = entity.makeSelfAsNewParentIds();
        baseMapper.updateSunTreeParentIds(newChildrenParentIds, oldChildrenParentIds);
    }


    @Override
    public boolean deleteById(Serializable id) {
        if (isDemo) {
            return true;
        }
        T entity = selectById(id);
        baseMapper.deleteSunTree(entity.makeSelfAsNewParentIds());
        return super.deleteById(id);
    }

    @Override
    public void deleteBatchIds(List<Serializable> idList) {
        if (isDemo) {
            return;
        }
        for (Serializable treeId : idList) {
            deleteById(treeId);
        }
    }


}
