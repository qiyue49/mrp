package com.sunseagear.common.mvc.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.sunseagear.common.mvc.constant.DataBaseConstant;

import java.util.Date;

/**
 * 数据Entity类
 *
 * @param <ID> 主键类型
 * @version 2016-12-03
 */
public abstract class DataEntity<ID> extends AbstractEntity<ID> {

    private static final long serialVersionUID = 1L;

    @TableField(value = "remarks")
    protected String remarks; // 备注

    @TableField(value = "create_by", fill = FieldFill.INSERT)
    protected Long createBy; // 创建者

    @TableField(value = "create_date", fill = FieldFill.INSERT)
    protected Date createDate; // 创建日期

    @TableField(value = "update_by", fill = FieldFill.UPDATE)
    protected Long updateBy; // 更新者

    @TableField(value = "update_date", fill = FieldFill.UPDATE)
    protected Date updateDate; // 更新日期

    @TableField(value = "del_flag", fill = FieldFill.INSERT)
    protected int delFlag = 0; // 删除标记（0：正常；1：删除 ）

    public DataEntity() {
        super();
        this.delFlag = DataBaseConstant.DEL_FLAG_NORMAL;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public int getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(int delFlag) {
        this.delFlag = delFlag;
    }
}
