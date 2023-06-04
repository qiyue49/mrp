package com.sunseagear.wind.common.helper;

import com.sunseagear.common.mvc.entity.TreeEntity;
import com.sunseagear.common.utils.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 树排序
 */
public class VueTreeHelper implements Serializable {

    public static VueTreeHelper create() {
        return new VueTreeHelper();
    }

    public <T extends TreeEntity> List<T> sort(List<T> treeNodes) {
        List<T> treeNodeList = new ArrayList<>();
        HashMap<String, TreeEntity> treeEntityHashMap = new HashMap<>();
        treeNodes.forEach(item -> {
            treeEntityHashMap.put(String.valueOf(item.getId()), item);
        });
        treeNodes.forEach(item -> {
            if (item.getParentId() != null
                    && !StringUtils.isEmpty(String.valueOf(item.getParentId()))
                    && treeEntityHashMap.containsKey(item.getParentId().toString())) {
                TreeEntity treeEntity = treeEntityHashMap.get(item.getParentId().toString());
                if (treeEntity.getChildren() == null) {
                    treeEntity.setChildren(new ArrayList<>());
                }
                treeEntity.getChildren().add(item);

            } else {
                treeNodeList.add(item);
            }
        });
        return treeNodeList;
    }
}


