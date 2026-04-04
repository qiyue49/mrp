package com.sparkit.mrp.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

@Data
public class OrderDTO {
    private String orderId;
    @NotBlank(message = "订单编码不能为空")
    private String orderCode;
    @NotBlank(message = "客户ID不能为空")
    private String customerId;
    @NotBlank(message = "设计ID不能为空")
    private String designId;
    @NotNull(message = "数量不能为空")
    private Integer quantity;
    @NotBlank(message = "交付日期不能为空")
    private String deliveryDate;
    private String status;
    private Map<String, Object> extJson;
}
