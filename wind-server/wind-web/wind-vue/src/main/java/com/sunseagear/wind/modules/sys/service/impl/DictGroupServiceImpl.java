package com.sunseagear.wind.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sunseagear.common.mvc.service.impl.CommonServiceImpl;
import com.sunseagear.wind.modules.sys.entity.Dict;
import com.sunseagear.wind.modules.sys.entity.DictGroup;
import com.sunseagear.wind.modules.sys.mapper.DictGroupMapper;
import com.sunseagear.wind.modules.sys.service.IDictGroupService;
import com.sunseagear.wind.modules.sys.service.IDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

@Transactional
@Service("dictGroupService")
public class DictGroupServiceImpl extends CommonServiceImpl<DictGroupMapper, DictGroup> implements IDictGroupService {
    @Autowired
    private IDictService dictService;

    @Override
    public boolean deleteById(Serializable id) {
        dictService.delete(new QueryWrapper<Dict>().eq("gid", id));
        return super.deleteById(id);
    }
    @Override
    public void deleteBatchIds(List<Serializable> ids) {
        dictService.delete(new QueryWrapper<Dict>().in("gid", ids));
        super.deleteBatchIds(ids);
    }
}
