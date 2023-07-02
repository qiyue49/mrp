package com.sunseagear.wind.modules.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunseagear.common.http.Response;
import com.sunseagear.common.mvc.controller.BaseBeanController;
import com.sunseagear.common.utils.StringUtils;
import com.sunseagear.wind.aspectj.annotation.Log;
import com.sunseagear.wind.aspectj.enums.LogType;
import com.sunseagear.wind.modules.sys.entity.Dict;
import com.sunseagear.wind.modules.sys.service.IDictService;
import com.sunseagear.wind.utils.DictUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * All rights Reserved, Designed By www.sunseagear.com
 *
 * @version V1.0
 * @package com.sunseagear.wind.modules.sys.controller
 * @title: 消息模版控制器
 * @description: 消息模版控制器 * @date: 2018-09-03 15:10:10
 * @copyright: 2018 www.sunseagear.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("/sys/dict")
@PreAuthorize("hasAuthority('sys:dict')")
@Log(title = "字典管理")
public class DictController extends BaseBeanController<Dict> {

    @Autowired
    private IDictService dictService;

    /**
     * 字典
     *
     * @return
     */
    @GetMapping(value = "")
    public String get() {
        try {
            //放入数据字典
            return Response.successJson(DictUtils.getDict());
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("获取失败");
        }
    }

    /**
     * 根据页码和每页记录数，以及查询条件动态加载数据
     *
     * @param request
     * @throws IOException
     */
    @GetMapping(value = "list")
    @Log(logType = LogType.SELECT)
    @PreAuthorize("hasAuthority('sys:dict:list')")
    public String list(HttpServletRequest request) throws IOException {
        //加入条件
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort");
        String keyword = request.getParameter("keyword");
        String gid = request.getParameter("gid");
        if (!StringUtils.isEmpty(gid) && !StringUtils.isEmpty(keyword)) {
            queryWrapper.eq("gid", gid).and(i -> i.like("label", keyword).or().like("value", keyword));
        } else if (!StringUtils.isEmpty(gid)) {
            queryWrapper.eq("gid", gid);
        }

        // 预处理
        Page pageBean = dictService.selectPage(getPage(), queryWrapper);
        return Response.successPageJson(pageBean);
    }

    @PostMapping("add")
    @Log(logType = LogType.INSERT)
    @PreAuthorize("hasAuthority('sys:dict:add')")
    public String add(@Valid Dict entity, BindingResult result) {
        // 验证错误
        this.checkError(entity, result);
        dictService.insert(entity);
        DictUtils.initDict();
        return Response.ok("添加成功");
    }

    @PostMapping("update")
    @Log(logType = LogType.UPDATE)
    @PreAuthorize("hasAuthority('sys:dict:add')")
    public String update(@Valid Dict entity, BindingResult result) {
        // 验证错误
        this.checkError(entity, result);
        dictService.insertOrUpdate(entity);
        DictUtils.initDict();
        return Response.ok("更新成功");
    }

    @PostMapping("delete")
    @Log(logType = LogType.DELETE)
    @PreAuthorize("hasAuthority('sys:dict:delete')")
    public String batchDelete(@RequestParam("ids") Long[] ids) {
        List<Serializable> idList = Arrays.asList(ids);
        dictService.deleteBatchIds(idList);
        DictUtils.initDict();
        return Response.ok("删除成功");
    }

}
