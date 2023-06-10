package com.sunseagear.wind.modules.test.twotable.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sunseagear.common.mvc.entity.DataEntity;
import lombok.Data;

@Data
@TableName("test_car_model")
@SuppressWarnings("serial")
public class CarModel extends DataEntity<Long> {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    @TableField(value = "car_id")
    private Long carId;
    @TableField(value = "name")
    private String name;
    @TableField(value = "value")
    private String value;

    @TableField(value = "sort")
    private Integer sort;
    @TableField(exist = false)
    private String code;


}
