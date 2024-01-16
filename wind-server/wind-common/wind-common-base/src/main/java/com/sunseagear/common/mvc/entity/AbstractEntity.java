package com.sunseagear.common.mvc.entity;

import java.io.Serializable;

/**
 * 抽象实体基类
 */
public abstract class AbstractEntity<ID> implements Serializable {

    public static final int DEL_FLAG_NORMAL = 0;
    public static final int DEL_FLAG_DELETE = 1;
    public abstract ID getId();

    public abstract void setId(ID id);
}
