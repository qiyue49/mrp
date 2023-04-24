package com.sunseagear.common.mvc.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sunseagear.common.mvc.entity.tree.TreeNode;
import com.sunseagear.common.utils.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 树抽象实体基类
 */
public abstract class TreeEntity<T> extends AbstractEntity<T> implements TreeNode<T>, java.io.Serializable {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    protected T id; // 编号
    @TableField(value = "name")
    protected String name; // 资源名称
    @TableField(value = "parent_id")
    protected T parentId; // 父编号
    @TableField(value = "parent_ids")
    protected String parentIds; // 父编号列表

    @TableField(value = "remarks")
    protected String remarks;
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    protected String createBy; // 创建者
    @TableField(value = "create_date", fill = FieldFill.INSERT)
    protected Date createDate; // 创建日期
    @TableField(value = "update_by", fill = FieldFill.UPDATE)
    protected String updateBy; // 更新者
    @TableField(value = "update_date", fill = FieldFill.UPDATE)
    protected Date updateDate; // 更新日期
    @TableField(value = "del_flag", fill = FieldFill.INSERT)
    protected String delFlag = "0"; // 删除标记（0：正常；1：删除 ）


    @TableField(exist = false)
    protected Boolean expanded = Boolean.FALSE;
    @TableField(exist = false)
    protected Boolean loaded = Boolean.TRUE;

    @TableField(exist = false)
    List<TreeEntity> children = new ArrayList<>();

    /**
     * 是否有叶子节点
     */
    @TableField(exist = false)
    protected boolean hasChildren;


    @Override
    public boolean isRoot() {
        if (getParentId() == null || getParentId().equals("0") || getParentId().equals("")) {
            return true;
        }
        return false;
    }


    @Override
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

    @Override
    public Boolean isLeaf() {
        if (isHasChildren()) {
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

    @Override
    public String makeSelfAsNewParentIds() {
        if (StringUtils.isEmpty(getParentIds())) {
            return getId() + getSeparator();
        }
        return getParentIds() + getId() + getSeparator();
    }

    @Override
    public String getSeparator() {
        return "/";
    }

    @Override
    public String[] makeTags() {
        return new String[0];
    }

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public Boolean getExpanded() {
        return expanded;
    }

    public void setExpanded(Boolean expanded) {
        this.expanded = expanded;
    }

    public Boolean getLoaded() {
        return loaded;
    }

    public void setLoaded(Boolean loaded) {
        this.loaded = loaded;
    }

    public List<TreeEntity> getChildren() {
        return children;
    }

    public void setChildren(List<TreeEntity> children) {
        this.children = children;
    }

    public boolean isHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }
}
