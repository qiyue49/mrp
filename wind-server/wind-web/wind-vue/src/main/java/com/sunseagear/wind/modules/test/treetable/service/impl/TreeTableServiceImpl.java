package com.sunseagear.wind.modules.test.treetable.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sunseagear.common.mvc.service.impl.TreeCommonServiceImpl;
import com.sunseagear.common.utils.StringUtils;
import com.sunseagear.wind.modules.test.treetable.entity.TreeTable;
import com.sunseagear.wind.modules.test.treetable.mapper.TreeTableMapper;
import com.sunseagear.wind.modules.test.treetable.service.ITreeTableService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * All rights Reserved, Designed By www.sunseagear.com
 *
 * @version V1.0
 * @package test.treetable
 * @title: 树形结构表控制器
 * @description: 树形结构表控制器
 * @author: admin
 * @date: 2019-11-13 21:38:34
 * @copyright: www.sunseagear.com Inc. All rights reserved.
 */
@Transactional
@Service("treeTableService")
public class TreeTableServiceImpl extends TreeCommonServiceImpl<TreeTableMapper, TreeTable, Long> implements ITreeTableService {


    @Override
    public List<TreeTable> selectList(Wrapper<TreeTable> wrapper) {
        List<TreeTable> list = super.selectList(wrapper);
        return getTreeTables(list);
    }

    private List<TreeTable> getTreeTables(List<TreeTable> treeNodeList) {
        List<TreeTable> TreeTableListAll = list(new QueryWrapper());
        HashMap<String, TreeTable> TreeTableHashMapAll = new HashMap<>();
        TreeTableListAll.forEach(TreeTable -> {
            TreeTableHashMapAll.put(TreeTable.getId().toString(), TreeTable);
        });
        HashMap<String, TreeTable> TreeTableHashMap = new HashMap<>();
        treeNodeList.forEach(treeNode -> {
            String parentIds = treeNode.getParentIds();
            if (!StringUtils.isEmpty(parentIds)) {
                Arrays.asList(parentIds.split("/")).forEach(id -> {
                    TreeTable TreeTable = TreeTableHashMapAll.get(id);
                    TreeTableHashMap.put(id, TreeTable);
                });
            }
            TreeTableHashMap.put(treeNode.getId().toString(), TreeTableHashMapAll.get(treeNode.getId().toString()));

        });
        List<TreeTable> TreeTableList = new ArrayList<>();
        TreeTableHashMap.values().forEach(item -> {
            TreeTableList.add(item);
        });
        TreeTableList.sort(new Comparator<TreeTable>() {
            @Override
            public int compare(TreeTable o1, TreeTable o2) {
                return o1.getCreateDate().compareTo(o2.getCreateDate());
            }
        });
        return TreeTableList;
    }

}
