/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.service.impl;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.base.sbc.client.ccm.entity.BasicBaseDict;
import com.base.sbc.client.ccm.entity.BasicStructureTreeVo;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.ExcelUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.style.dto.PrincipalDesignerManageQueryDto;
import com.base.sbc.module.style.entity.PrincipalDesignerManage;
import com.base.sbc.module.style.mapper.PrincipalDesignerManageMapper;
import com.base.sbc.module.style.service.PrincipalDesignerManageService;
import com.base.sbc.module.style.vo.PrincipalDesignerManageExcel;
import com.base.sbc.module.style.vo.PrincipalDesignerManageVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 类描述：负责设计师配置表 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.style.service.PrincipalDesignerManageService
 * @email your email
 * @date 创建时间：2024-3-18 16:27:53
 */
@Service
public class PrincipalDesignerManageServiceImpl extends BaseServiceImpl<PrincipalDesignerManageMapper, PrincipalDesignerManage> implements PrincipalDesignerManageService {

// 自定义方法区 不替换的区域【other_start】

    @Autowired
    private CcmFeignService ccmFeignService;

    @Override
    public PageInfo<PrincipalDesignerManageVo> findPage(PrincipalDesignerManageQueryDto dto) {
        Page<Object> objects = PageHelper.startPage(dto);
        BaseQueryWrapper<PrincipalDesignerManage> qw = new BaseQueryWrapper<>();
        List<PrincipalDesignerManage> list = list(qw);
        return new PageInfo<>(BeanUtil.copyToList(list, PrincipalDesignerManageVo.class));
    }

    @Override
    public ApiResult importExcel(List<PrincipalDesignerManageExcel> list) {
        List<String> prodCategoryNameList = new ArrayList<>();
        List<String> designerList = new ArrayList<>();

        for (PrincipalDesignerManageExcel excel : list) {
            prodCategoryNameList.add(excel.getProdCategoryName());
            designerList.add(excel.getDesigner());
        }
        //品牌
        List<BasicBaseDict> dictInfoToList = ccmFeignService.getDictInfoToList("C8_Brand");
        Map<String, List<BasicBaseDict>> brandNameMap = dictInfoToList.stream().collect(Collectors.groupingBy(BasicBaseDict::getName));
        //品类
        List<BasicStructureTreeVo> structureTreeByCodes = ccmFeignService.findStructureTreeByCodes(String.join(",", prodCategoryNameList));
        Map<String, String> prodCategoryMap = new HashMap<>();
        structureTreeByCodes.forEach(o -> o.getChildren().forEach(s -> prodCategoryMap.put(o.getName() + "_" + s.getName(), o.getId() + "_" + s.getId())));

        for (PrincipalDesignerManageExcel excel : list) {
            //品牌
            if (brandNameMap.containsKey(excel.getBrandName())) {
                String name = brandNameMap.get(excel.getBrandName()).get(0).getValue();
                excel.setBrand(name);
            } else {
                throw new OtherException("品牌不存在：" + excel.getBrandName());
            }
            //品类+中类
            if (prodCategoryMap.containsKey(excel.getProdCategoryName() + "_" + excel.getProdCategory2ndName())) {
                String[] ids = prodCategoryMap.get(excel.getProdCategoryName() + "_" + excel.getProdCategory2ndName()).split("_");
                excel.setProdCategory(ids[0]);
                excel.setProdCategory2nd(ids[1]);
            } else {
                throw new OtherException("品类+中类 不存在：" + excel.getProdCategoryName() + "_" + excel.getProdCategory2ndName());
            }
            //设计师  TODO


        }
        List<PrincipalDesignerManage> list1 = BeanUtil.copyToList(list, PrincipalDesignerManage.class);
        for (PrincipalDesignerManage principalDesignerManage : list1) {
            LambdaUpdateWrapper<PrincipalDesignerManage> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(PrincipalDesignerManage::getBrand,principalDesignerManage.getBrand());
            updateWrapper.eq(PrincipalDesignerManage::getProdCategory,principalDesignerManage.getProdCategory());
            updateWrapper.eq(PrincipalDesignerManage::getProdCategory2nd,principalDesignerManage.getProdCategory2nd());
            saveOrUpdate(principalDesignerManage,updateWrapper);
        }

        return ApiResult.success("导入成功");
    }

    @Override
    public void exportExcel(HttpServletResponse response, PrincipalDesignerManageQueryDto dto) throws IOException {
        List<PrincipalDesignerManageVo> list = findPage(dto).getList();
        List<PrincipalDesignerManageExcel> principalDesignerManageExcels = BeanUtil.copyToList(list, PrincipalDesignerManageExcel.class);
        ExcelUtils.exportExcel(principalDesignerManageExcels, PrincipalDesignerManageExcel.class, "负责设计师配置.xlsx", new ExportParams("负责设计师配置", "负责设计师配置", ExcelType.HSSF), response);
    }

// 自定义方法区 不替换的区域【other_end】

}
