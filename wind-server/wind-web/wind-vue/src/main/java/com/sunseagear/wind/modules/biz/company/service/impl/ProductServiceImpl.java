package com.sunseagear.wind.modules.biz.company.service.impl;

import com.sunseagear.common.mvc.service.impl.CommonServiceImpl;
import com.sunseagear.wind.modules.biz.company.entity.Product;
import com.sunseagear.wind.modules.biz.company.mapper.ProductMapper;
import com.sunseagear.wind.modules.biz.company.service.IProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * All rights Reserved, Designed By www.sunseagear.com
 *
 * @version V1.0
 * @package biz.company
 * @title: 产品管理&mdash;&mdash;级联表子表（飞廉演示）控制器
 * @description: 产品管理&mdash;&mdash;级联表子表（飞廉演示）控制器
 * @author:
 * @date: 2021-10-21 07:59:27
 * @copyright: www.sunseagear.com Inc. All rights reserved.
 */
@Transactional
@Service("productService")
public class ProductServiceImpl extends CommonServiceImpl<ProductMapper, Product> implements IProductService {

}
