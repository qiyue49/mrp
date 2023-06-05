package com.sunseagear.wind.common.helper;

import com.sunseagear.common.mvc.entity.TreeEntity;
import com.sunseagear.common.utils.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * 树排序
 */
public class VueTreeHelper {

    public static VueTreeHelper create() {
        return new VueTreeHelper();
    }

    public <T extends TreeEntity<M>, M extends Serializable> List<T> sort(List<T> treeNodes) {
        List<T> treeNodeList = new ArrayList<>();
        Map<M, T> treeEntityHashMap = treeNodes.stream().collect(Collectors.toMap(TreeEntity<M>::getId, Function.identity()));
        treeNodes.forEach(item -> {
            treeEntityHashMap.put(item.getId(), item);
        });
        treeNodes.forEach(item -> {
            if (item.getParentId() != null
                    && item.getParentId() != null
                    && treeEntityHashMap.containsKey(item.getParentId())) {
                T treeEntity = treeEntityHashMap.get(item.getParentId());
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


