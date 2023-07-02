package com.sunseagear.wind.modules.test.twotable.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunseagear.common.http.Response;
import com.sunseagear.common.mvc.controller.BaseBeanController;
import com.sunseagear.common.utils.StringUtils;
import com.sunseagear.wind.aspectj.annotation.Log;
import com.sunseagear.wind.aspectj.enums.LogType;
import com.sunseagear.wind.modules.test.twotable.entity.CarModel;
import com.sunseagear.wind.modules.test.twotable.service.ICarModelService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.Serializable;
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
@RequestMapping("/test/twotable/carmodel")
@PreAuthorize("hasAuthority('test:twotable:car')")
@Log(title = "车辆型号管理")
public class CarModelController extends BaseBeanController<CarModel> {

    @Autowired
    private ICarModelService carModelService;


    /**
     * 根据页码和每页记录数，以及查询条件动态加载数据
     *
     * @param request
     * @throws IOException
     */
    @PostMapping(value = "list")
    @Log(logType = LogType.SELECT)
    @PreAuthorize("hasAuthority('test:twotable:car:list')")
    public String list(HttpServletRequest request) throws IOException {
        //加入条件
        QueryWrapper<CarModel> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort");
        String keyword = request.getParameter("keyword");
        String carId = request.getParameter("carId");
        if (!StringUtils.isEmpty(carId) && !StringUtils.isEmpty(keyword)) {
            queryWrapper.eq("car_id", carId).and(i -> i.like("label", keyword).or().like("value", keyword));
        } else if (!StringUtils.isEmpty(carId)) {
            queryWrapper.eq("car_id", carId);
        }

        // 预处理
        Page pageBean = carModelService.selectPage(getPage(), queryWrapper);
        return Response.successPageJson(pageBean);
    }

    @GetMapping("detail/{id}")
    @Log(logType = LogType.SELECT)
    @PreAuthorize("hasAuthority('test:twotable:car:detail')")
    public String detail(@PathVariable("id") Long id) {
        CarModel carModel = carModelService.selectById(id);
        return Response.successJson(carModel);
    }

    @PostMapping("add")
    @Log(logType = LogType.UPDATE)
    @PreAuthorize("hasAuthority('add')")
    public String add(@Valid CarModel entity, BindingResult result) {
        // 验证错误
        this.checkError(entity, result);
        carModelService.insert(entity);
        return Response.ok("添加成功");
    }

    @PostMapping("update")
    @Log(logType = LogType.UPDATE)
    @PreAuthorize("hasAuthority('test:twotable:car:update')")
    public String update(@Valid CarModel entity, BindingResult result) {
        // 验证错误
        this.checkError(entity, result);
        carModelService.insertOrUpdate(entity);
        return Response.ok("更新成功");
    }

    @PostMapping("delete")
    @Log(logType = LogType.DELETE)
    @PreAuthorize("hasAuthority('test:twotable:car:delete')")
    public String batchDelete(@RequestParam("ids") Long[] ids) {
        List<Serializable> idList = java.util.Arrays.asList(ids);
        carModelService.deleteBatchIds(idList);
        return Response.ok("删除成功");
    }

}
