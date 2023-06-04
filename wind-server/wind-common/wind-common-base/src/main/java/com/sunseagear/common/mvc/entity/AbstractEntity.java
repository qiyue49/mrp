package com.sunseagear.common.mvc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.google.gson.annotations.JsonAdapter;
import com.sunseagear.common.http.Response;

import java.io.Serializable;

/**
 * 抽象实体基类
 */
public abstract class AbstractEntity<ID> implements Serializable {

    @JsonAdapter(Response.IdAdapter.class)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    protected ID id; // 编号


    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }
}
