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

    public abstract ID getId();

    public abstract void setId(ID id);
}
