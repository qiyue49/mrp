package com.sunseagear.wind.modules.biz.company.service.impl;

import com.sunseagear.common.mvc.service.impl.CommonServiceImpl;
import com.sunseagear.wind.modules.biz.company.service.ICompanyService;
import com.sunseagear.wind.modules.biz.company.entity.Company;
import com.sunseagear.wind.modules.biz.company.mapper.CompanyMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * All rights Reserved, Designed By www.sunseagear.com
 *
 * @version V1.0
 * @package biz.company
 * @title: 供货厂家&mdash;&mdash;级联表主表（飞廉演示）控制器
 * @description: 供货厂家&mdash;&mdash;级联表主表（飞廉演示）控制器
 * @author: 
 * @date: 2021-10-21 07:59:27
 * @copyright: www.sunseagear.com Inc. All rights reserved.
 */
@Transactional
@Service("companyService")
public class CompanyServiceImpl  extends CommonServiceImpl<CompanyMapper, Company> implements  ICompanyService {

}