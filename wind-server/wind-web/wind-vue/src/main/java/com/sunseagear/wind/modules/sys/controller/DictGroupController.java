package com.sunseagear.wind.modules.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunseagear.common.http.Response;
import com.sunseagear.common.mvc.controller.BaseBeanController;
import com.sunseagear.common.utils.StringUtils;
import com.sunseagear.wind.aspectj.annotation.Log;
import com.sunseagear.wind.aspectj.enums.LogType;
import com.sunseagear.wind.modules.sys.entity.DictGroup;
import com.sunseagear.wind.modules.sys.service.IDictGroupService;
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

@RestController
@RequestMapping("/sys/dict/group")
@PreAuthorize("hasAuthority('sys:dict')")
@Log(title = "字典分组")
public class DictGroupController extends BaseBeanController<DictGroup> {

    @Autowired
    private IDictGroupService dictGroupService;


    /**
     * 根据页码和每页记录数，以及查询条件动态加载数据
     *
     * @param request
     * @throws IOException
     */
    @GetMapping(value = "list")
    @Log(logType = LogType.SELECT)
    @PreAuthorize("hasAuthority('sys:dict:group:list')")
    public String list(HttpServletRequest request) throws IOException {
        //加入条件
        QueryWrapper<DictGroup> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_date");
        String keyword = request.getParameter("keyword");
        if (!StringUtils.isEmpty(keyword)) {
            queryWrapper.like("name", keyword).or().like("code", keyword);
        }
        // 预处理
        Page pageBean = dictGroupService.selectPage(getPage(), queryWrapper);
        return Response.successPageJson(pageBean);
    }

    @PostMapping("add")
    @Log(logType = LogType.INSERT)
    @PreAuthorize("hasAuthority('sys:dict:group:add')")
    public String add(@Valid DictGroup entity, BindingResult result) {
        // 验证错误
        this.checkError(entity, result);
        dictGroupService.insert(entity);
        DictUtils.initDict();
        return Response.ok("添加成功");
    }

    @PostMapping("update")
    @Log(logType = LogType.UPDATE)
    @PreAuthorize("hasAuthority('sys:dict:group:update')")
    public String update(@Valid DictGroup entity, BindingResult result) {
        // 验证错误
        this.checkError(entity, result);
        dictGroupService.insertOrUpdate(entity);
        DictUtils.initDict();
        return Response.ok("更新成功");
    }

    @PostMapping("delete")
    @Log(logType = LogType.DELETE)
    @PreAuthorize("hasAuthority('sys:dict:group:delete')")
    public String batchDelete(@RequestParam("ids") Long[] ids) {
        List<Serializable> idList = Arrays.asList(ids);
        dictGroupService.deleteBatchIds(idList);
        DictUtils.initDict();
        return Response.ok("删除成功");
    }

}
