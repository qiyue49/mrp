package com.sunseagear.wind.modules.test.twotable.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunseagear.common.http.Response;
import com.sunseagear.common.mvc.controller.BaseBeanController;
import com.sunseagear.common.utils.StringUtils;
import com.sunseagear.wind.aspectj.annotation.Log;
import com.sunseagear.wind.aspectj.enums.LogType;
import com.sunseagear.wind.modules.test.twotable.entity.Car;
import com.sunseagear.wind.modules.test.twotable.service.ICarService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

@RestController
@RequestMapping("/test/twotable/car")
@PreAuthorize("hasAuthority('test:twotable:car')")
@Log(title = "车辆品牌分组")
public class CarController extends BaseBeanController<Car> {

    @Autowired
    private ICarService carService;


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
        QueryWrapper<Car> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_date");
        String keyword = request.getParameter("keyword");
        if (!StringUtils.isEmpty(keyword)) {
            queryWrapper.like("name", keyword).or().like("code", keyword);
        }
        // 预处理
        Page pageBean = carService.selectPage(getPage(), queryWrapper);
        return Response.successPageJson(pageBean);
    }

    @GetMapping("detail/{id}")
    @Log(logType = LogType.SELECT)
    @PreAuthorize("hasAuthority('test:twotable:car:detail')")
    public String detail(@PathVariable("id") Long id) {
        Car car = carService.selectById(id);
        return Response.successJson(car);
    }

    @PostMapping("add")
    @Log(logType = LogType.INSERT)
    @PreAuthorize("hasAuthority('add')")
    public String add(@Valid Car entity, BindingResult result) {
        // 验证错误
        this.checkError(entity, result);
        carService.insert(entity);
        return Response.ok("添加成功");
    }

    @PostMapping("update")
    @Log(logType = LogType.UPDATE)
    @PreAuthorize("hasAuthority('test:twotable:car:update')")
    public String update(@Valid Car entity, BindingResult result) {
        // 验证错误
        this.checkError(entity, result);
        carService.insertOrUpdate(entity);
        return Response.ok("更新成功");
    }

    @PostMapping("delete/{id}")
    @PreAuthorize("hasAuthority('test:twotable:car:delete')")
    public String delete(@RequestParam("ids") Long[] ids) {
        List<Serializable> idList = java.util.Arrays.asList(ids);
        carService.deleteBatchIds(idList);
        return Response.ok("删除成功");
    }

}
