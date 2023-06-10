package com.sunseagear.common.mvc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sunseagear.common.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 树抽象实体基类
 */
public abstract class TreeEntity<T> extends DataEntity<T> {

    @TableField(value = "name")
    protected String name; // 资源名称
    @TableField(value = "parent_id")
    protected T parentId; // 父编号
    @TableField(value = "parent_ids")
    protected String parentIds; // 父编号列表

    @TableField(exist = false)
    List<TreeEntity> children = new ArrayList<>();


    public boolean isRoot() {
        if (getParentId() == null || getParentId().equals("0") || getParentId().equals("")) {
            return true;
        }
        return false;
    }


    public Long getLevel() {
        if (parentIds == null) {
            return (long) 0;
        }
        String[] parentIdArr = parentIds.split("/");
        List<String> idsList = new ArrayList<>();
        for (String id : parentIdArr) {
            if (!StringUtils.isEmpty(id)) {
                idsList.add(id);
            }
        }
        return (long) (idsList.size());
    }

    public Boolean isLeaf() {
        if (!this.children.isEmpty()) {
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

    public String makeSelfAsNewParentIds() {
        if (StringUtils.isEmpty(getParentIds())) {
            return getId() + getSeparator();
        }
        return getParentIds() + getId() + getSeparator();
    }

    public String getSeparator() {
        return "/";
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public T getParentId() {
        return parentId;
    }

    public void setParentId(T parentId) {
        this.parentId = parentId;
    }

    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    public List<TreeEntity> getChildren() {
        return children;
    }

    public void setChildren(List<TreeEntity> children) {
        this.children = children;
    }

}
