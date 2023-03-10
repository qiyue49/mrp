package com.sunseagear.wind.modules.biz.newsType.json;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sunseagear.common.mvc.controller.BaseBeanController;
import com.sunseagear.common.utils.JsonUtils;
import com.sunseagear.wind.aspectj.annotation.Log;
import com.sunseagear.wind.aspectj.enums.LogType;
import com.sunseagear.wind.modules.biz.newsType.entity.NewsType;
import com.sunseagear.wind.modules.biz.newsType.service.INewsTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;


/**
 * All rights Reserved, Designed By www.sunseagear.com
 *
 * @version V1.0
 * @package biz.newsType
 * @title: 新闻类型控制器
 * @description: 新闻类型控制器
 * @author:
 * @date: 2020-11-09 04:00:26
 * @copyright: www.sunseagear.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("json/newsType")
@Log(title = "新闻类型")
public class NewsTypeJsonController extends BaseBeanController<NewsType> {

    @Autowired
    private INewsTypeService newsTypeService;

    /**
     * 根据页码和每页记录数，以及查询条件动态加载数据
     *
     * @param request
     * @throws IOException
     */
    @PostMapping(value = "list")
    @Log(logType = LogType.SELECT)
    public String list(HttpServletRequest request) throws IOException {
        //加入条件
        QueryWrapper<NewsType> entityWrapper = new QueryWrapper<>();
        // 预处理
        List<NewsType> newsTypes = newsTypeService.selectList(entityWrapper);
        return JsonUtils.successMessage(newsTypes);
    }

    @PostMapping("add")
    @Log(logType = LogType.INSERT)
    public String add(NewsType entity, BindingResult result,
                      HttpServletRequest request, HttpServletResponse response) {
        // 验证错误
        this.checkError(entity, result);
        newsTypeService.insert(entity);
        return JsonUtils.successMessage("添加成功");
    }

    @PostMapping("update")
    @Log(logType = LogType.UPDATE)
    public String update(NewsType entity, BindingResult result,
                         HttpServletRequest request, HttpServletResponse response) {
        // 验证错误
        this.checkError(entity, result);
        newsTypeService.insertOrUpdate(entity);
        return JsonUtils.successMessage("更新成功");
    }

    @GetMapping("detail/{id}")
    @Log(logType = LogType.SELECT)
    public String detail(Model model, @PathVariable("id") String id) {
        NewsType newsType = newsTypeService.selectById(id);
        return JsonUtils.successMessage(newsType);
    }

    @PostMapping("delete")
    @Log(logType = LogType.DELETE)
    public String batchDelete(@RequestParam("ids") String[] ids) {
        List<Serializable> idList = java.util.Arrays.asList(ids);
        newsTypeService.deleteBatchIds(idList);
        return JsonUtils.successMessage("删除成功");
    }
}
