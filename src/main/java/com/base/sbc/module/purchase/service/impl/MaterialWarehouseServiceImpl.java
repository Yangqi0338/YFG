/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.purchase.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.purchase.mapper.MaterialWarehouseMapper;
import com.base.sbc.module.purchase.entity.MaterialWarehouse;
import com.base.sbc.module.purchase.service.MaterialWarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 类描述：物料仓库 service类
 * @address com.base.sbc.module.purchase.service.MaterialWarehouseService
 * @author tzy
 * @email 974849633@qq.com
 * @date 创建时间：2023-8-4 14:46:11
 * @version 1.0  
 */
@Service
public class MaterialWarehouseServiceImpl extends BaseServiceImpl<MaterialWarehouseMapper, MaterialWarehouse> implements MaterialWarehouseService {

    @Autowired
    private MaterialWarehouseMapper warehouseMapper;

    /**
     * 新增仓库
     * @param userCompany 用户信息
     * @param companyCode 企业编码
     * @param materialWarehouse 仓库实体
     * */
    public ApiResult addWarehouse(UserCompany userCompany, String companyCode, MaterialWarehouse materialWarehouse){
        IdGen idGen = new IdGen();

        QueryWrapper<MaterialWarehouse> qw = new QueryWrapper<>();
        qw.eq("company_code", companyCode);
        qw.eq("warehouse_name", materialWarehouse.getWarehouseName());
        List<MaterialWarehouse> nameList = list(qw);
        if(CollectionUtil.isNotEmpty(nameList)){
            return ApiResult.error("已经有相同名称的仓库，请更换！", 500);
        }

        qw.clear();
        qw.eq("company_code", companyCode);
        qw.eq("code", materialWarehouse.getCode());
        List<MaterialWarehouse> codeList = list(qw);
        if(CollectionUtil.isNotEmpty(codeList)){
            return ApiResult.error("仓库编码重复，请更换！", 500);
        }

        String id = idGen.nextIdStr();
        materialWarehouse.insertInit(userCompany);
        materialWarehouse.setId(id);
        materialWarehouse.setCompanyCode(companyCode);
        materialWarehouse.setStatus("1");

        boolean result = save(materialWarehouse);
        if(result){
            if(StringUtils.equals(materialWarehouse.getDefaultSetting(), "1")){
                //默认仓库，将其他仓库设定为否
                warehouseMapper.updateAllDefault(id);
            }
            return ApiResult.success("新增成功！", materialWarehouse);
        }
        return ApiResult.error("新增失败！", 500);
    }

    /**
     * 修改仓库
     * @param userCompany 用户信息
     * @param companyCode 企业编码
     * @param materialWarehouse 仓库实体
     * */
    @Override
    public ApiResult updateWarehouse(UserCompany userCompany, String companyCode, MaterialWarehouse materialWarehouse) {
        QueryWrapper<MaterialWarehouse> qw = new QueryWrapper<>();
        qw.eq("company_code", companyCode);
        qw.eq("warehouse_name", materialWarehouse.getWarehouseName());
        qw.ne("id", materialWarehouse.getId());
        List<MaterialWarehouse> list = list(qw);
        if(CollectionUtil.isNotEmpty(list)){
            return ApiResult.error("已经有相同名称的仓库，请更换！", 500);
        }

        qw.clear();
        qw.eq("company_code", companyCode);
        qw.eq("code", materialWarehouse.getCode());
        qw.ne("id", materialWarehouse.getId());
        List<MaterialWarehouse> codeList = list(qw);
        if(CollectionUtil.isNotEmpty(codeList)){
            return ApiResult.error("仓库编码重复，请更换！", 500);
        }

        materialWarehouse.updateInit(userCompany);
        boolean b = updateById(materialWarehouse);
        if (b) {
            if(StringUtils.equals(materialWarehouse.getDefaultSetting(), "1")){
                warehouseMapper.updateAllDefault(materialWarehouse.getId());
            }
            return ApiResult.success("修改成功！",materialWarehouse);
        }
        return ApiResult.error("找不到数据！", 500);
    }

}

