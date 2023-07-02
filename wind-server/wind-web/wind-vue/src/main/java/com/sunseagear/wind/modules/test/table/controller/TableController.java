package com.sunseagear.wind.modules.test.table.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunseagear.common.http.Response;
import com.sunseagear.common.mvc.controller.BaseBeanController;
import com.sunseagear.common.utils.DateUtils;
import com.sunseagear.common.utils.StringUtils;
import com.sunseagear.wind.aspectj.annotation.Log;
import com.sunseagear.wind.aspectj.enums.LogType;
import com.sunseagear.wind.modules.test.table.entity.Table;
import com.sunseagear.wind.modules.test.table.service.ITableService;
import com.sunseagear.wind.utils.excel.ExportExcel;
import com.sunseagear.wind.utils.excel.ImportExcel;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 * All rights Reserved, Designed By www.sunseagear.com
 *
 * @version V1.0
 * @package com.sunseagear.wind.modules.sys.controller
 * @title: 操作日志控制器
 * @description: 操作日志控制器 * @date: 2018-09-30 15:53:25
 * @copyright: 2018 www.sunseagear.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("test/table/table")
@PreAuthorize("hasAuthority('test:table:table')")
@Log(title = "综合表格")
public class TableController extends BaseBeanController<Table> {

    @Autowired
    private ITableService tableService;

    /**
     * 根据页码和每页记录数，以及查询条件动态加载数据
     *
     * @param request
     * @throws IOException
     */
    @PostMapping(value = "list")
    @Log(logType = LogType.SELECT)
    @PreAuthorize("hasAuthority('test:table:table:list')")
    public String list(HttpServletRequest request) throws IOException {
        //加入条件
        QueryWrapper<Table> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_date");

        String title = request.getParameter("title");
        if (!StringUtils.isEmpty(title)) {
            queryWrapper.eq("title", title);
        }
        String level = request.getParameter("level");
        if (!StringUtils.isEmpty(title)) {
            queryWrapper.eq("level", level);
        }
        String type = request.getParameter("type");
        if (!StringUtils.isEmpty(type)) {
            queryWrapper.eq("type", type);
        }
        // 预处理
        Page pageBean = tableService.selectPage(getPage(), queryWrapper);
        return Response.successPageJson(pageBean, "content", false);
    }

    @PostMapping("add")
    @Log(logType = LogType.INSERT)
    @PreAuthorize("hasAuthority('test:table:table:add')")
    public String add(@Valid Table entity, BindingResult result) {
        // 验证错误
        this.checkError(entity, result);
        tableService.insert(entity);
        return Response.ok("添加成功");
    }

    @PostMapping("update")
    @Log(logType = LogType.UPDATE)
    @PreAuthorize("hasAuthority('test:table:table:update')")
    public String update(@Valid Table entity, BindingResult result) {
        // 验证错误
        this.checkError(entity, result);
        tableService.insertOrUpdate(entity);
        return Response.ok("更新成功");
    }

    @GetMapping("detail/{id}")
    @Log(logType = LogType.SELECT)
    @PreAuthorize("hasAuthority('test:table:table:detail')")
    public String detail(@PathVariable("id") Long id) {
        Table tableEntity = tableService.selectById(id);
        tableEntity.setContent(StringEscapeUtils.unescapeHtml4(tableEntity.getContent()));
        return Response.successJson(tableEntity);
    }

    @PostMapping("delete")
    @Log(logType = LogType.DELETE)
    @PreAuthorize("hasAuthority('test:table:table:delete')")
    public String batchDelete(@RequestParam("ids") Long[] ids) {
        List<Serializable> idList = Arrays.asList(ids);
        tableService.deleteBatchIds(idList);
        return Response.ok("删除成功");
    }

    @GetMapping("template")
    @Log(logType = LogType.IMPORT)
    @PreAuthorize("hasAuthority('test:table:table:import')")
    public String template() {
        try {
            List<Table> list = new ArrayList<>();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            new ExportExcel("综合表格", Table.class, ExportExcel.TEMPLATE).setDataList(list).write(bos);
            byte[] bytes = bos.toByteArray();
            String bytesRes = StringUtils.bytesToHexString2(bytes);
            String fileName = "综合表格数据模板" + DateUtils.getDate("yyyyMMddHHmmss");
            HashMap<String, Object> result = new HashMap<>();
            result.put("bytes", bytesRes);
            result.put("title", fileName);
            return Response.successJson(result);
        } catch (Exception e) {
            logger.error("Template Exception", e);
            return Response.failJson("导出模板失败！失败信息：" + e.getMessage());
        }
    }

    @PostMapping("import")
    @Log(logType = LogType.IMPORT)
    @PreAuthorize("hasAuthority('test:table:table:import')")
    public String export(MultipartFile file) {
        try {
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<Table> list = ei.getDataList(Table.class);
            tableService.insertBatch(list);
            return Response.successJson("导入成功");
        } catch (Exception e) {
            logger.error("Import Exception", e);
            return Response.failJson("导出模板失败！失败信息：" + e.getMessage());
        }
    }

    @PostMapping("export")
    @Log(logType = LogType.EXPORT)
    @PreAuthorize("hasAuthority('test:table:table:export')")
    public String export(HttpServletRequest request) {
        try {
            QueryWrapper<Table> queryWrapper = new QueryWrapper<>();
            queryWrapper.orderByDesc("create_date");

            String title = request.getParameter("title");
            if (!StringUtils.isEmpty(title)) {
                queryWrapper.eq("title", title);
            }
            String level = request.getParameter("level");
            if (!StringUtils.isEmpty(title)) {
                queryWrapper.eq("level", level);
            }
            String type = request.getParameter("type");
            if (!StringUtils.isEmpty(type)) {
                queryWrapper.eq("type", type);
            }
            List<Table> list = tableService.selectList(queryWrapper);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            new ExportExcel("综合表格", Table.class).setDataList(list).write(bos);
            byte[] bytes = bos.toByteArray();
            String bytesRes = StringUtils.bytesToHexString2(bytes);
            String fileName = "综合表格" + DateUtils.getDate("yyyyMMddHHmmss");
            HashMap<String, Object> result = new HashMap<>();
            result.put("bytes", bytesRes);
            result.put("title", fileName);

            return Response.successJson(result);
        } catch (Exception e) {
            logger.error("Export Exception", e);
            return Response.failJson("导出记录失败！失败信息：" + e.getMessage());
        }

    }
}
