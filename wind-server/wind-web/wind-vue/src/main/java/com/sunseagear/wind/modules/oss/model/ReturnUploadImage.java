package com.sunseagear.wind.modules.oss.model;

import lombok.Getter;

/**
 * 文件上传成功回掉
 */
@Getter
public class ReturnUploadImage {
    private String state;//上传状态SUCCESS 一定要大写
    private String url;//上传地址
    private String title;//图片名称demo.jpg
    private String original;//图片名称demo.jpg

    public void setState(String state) {
        this.state = state;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOriginal(String original) {
        this.original = original;
    }
}
