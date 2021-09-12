package com.sunseagear.common.utils.entity;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;


public class PageResult<T extends Page> extends JsonResult<T> {

    private long total;
    private long current;


    public PageResult(T data) {
        super(data);
        this.total = data.getTotal();
        this.current = data.getCurrent();
    }

    public PageResult(boolean success, String message, T data) {
        super(success, message, data);
        this.total = data.getTotal();
        this.current = data.getCurrent();

    }

    public PageResult(boolean success, int statusCode, String message, T data) {
        super(success, statusCode, message, data);
        this.total = data.getTotal();
        this.current = data.getCurrent();

    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getCurrent() {
        return current;
    }

    public void setCurrent(long current) {
        this.current = current;
    }
}
