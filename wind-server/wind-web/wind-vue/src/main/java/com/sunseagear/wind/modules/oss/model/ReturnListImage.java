package com.sunseagear.wind.modules.oss.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 列表回掉
 */
@Getter
public class ReturnListImage {
    private String state;//上传状态SUCCESS 一定要大写
    private Integer start;
    private Integer total;
    private List<Map<String, String>> list = new ArrayList<>();


    public void setState(String state) {
        this.state = state;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public void setList(List<Map<String, String>> list) {
        this.list = list;
    }

    public void putUrl(String url) {
        Map<String, String> urlMap = new HashMap<>();
        urlMap.put("url", url);
        list.add(urlMap);
    }
}
