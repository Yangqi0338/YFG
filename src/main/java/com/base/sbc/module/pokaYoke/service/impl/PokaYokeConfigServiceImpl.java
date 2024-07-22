/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pokaYoke.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterial;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.pack.service.PackInfoService;
import com.base.sbc.module.pokaYoke.enums.PokBusinessTypeEnum;
import com.base.sbc.module.pokaYoke.mapper.PokaYokeConfigMapper;
import com.base.sbc.module.pokaYoke.entity.PokaYokeConfig;
import com.base.sbc.module.pokaYoke.dto.PokaYokeConfigQueryDto;
import com.base.sbc.module.pokaYoke.vo.PokaYokeConfigVo;
import com.base.sbc.module.pokaYoke.service.PokaYokeConfigService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Objects;

/**
 * 类描述：防呆防错配置 service类
 * @address com.base.sbc.module.pokaYoke.service.PokaYokeConfigService
 * @author your name
 * @email your email
 * @date 创建时间：2024-7-17 14:23:30
 * @version 1.0
 */
@Service
public class PokaYokeConfigServiceImpl extends BaseServiceImpl<PokaYokeConfigMapper, PokaYokeConfig> implements PokaYokeConfigService {

    @Autowired
    private PackInfoService packInfoService;

    @Autowired
    private BasicsdatumMaterialService basicsdatumMaterialService;


    @Override
    public PageInfo<PokaYokeConfigVo> queryPage(PokaYokeConfigQueryDto dto) {
        QueryWrapper<PokaYokeConfig> qw = getByDtoQueryWrapper(dto);
        Page<PokaYokeConfigVo> page = PageHelper.startPage(dto);
        list(qw);
        return page.toPageInfo();
    }

    @Override
    public Boolean savePokaYokeConfig(PokaYokeConfigVo vo) {
        if (PokBusinessTypeEnum.BOM_UNIT_USE == vo.getType()){
            checkSaveOrUpdate(vo, false);
        }
        PokaYokeConfig pokaYokeConfig = new PokaYokeConfig();
        BeanUtil.copyProperties(vo,pokaYokeConfig);
        pokaYokeConfig.insertInit();
        return save(pokaYokeConfig);
    }

    @Override
    public Boolean updatePokaYokeConfig(PokaYokeConfigVo vo) {
        if (PokBusinessTypeEnum.BOM_UNIT_USE == vo.getType()){
            checkSaveOrUpdate(vo,true);
        }
        PokaYokeConfig pokaYokeConfig = new PokaYokeConfig();
        BeanUtil.copyProperties(vo,pokaYokeConfig);
        return updateById(pokaYokeConfig);
    }

    @Override
    public Boolean del(String id) {
        if (StringUtils.isEmpty(id)){
            return true;
        }
        UpdateWrapper<PokaYokeConfig> uw = new UpdateWrapper<>();
        uw.lambda().in(PokaYokeConfig::getId,StringUtils.convertList(id));
        uw.lambda().eq(PokaYokeConfig::getDelFlag,"0");
        uw.lambda().set(PokaYokeConfig::getDelFlag,"1");
        return update(uw);
    }

    @Override
    public PokaYokeConfigVo queryCondition(PokaYokeConfigQueryDto dto) {
        if (PokBusinessTypeEnum.BOM_UNIT_USE == dto.getType()){
            PokaYokeConfig pokaYokeConfig = queryConditionBom(dto);
            PokaYokeConfigVo pokaYokeConfigVo = new PokaYokeConfigVo();
            BeanUtil.copyProperties(pokaYokeConfig,pokaYokeConfigVo);
            return pokaYokeConfigVo;
        }
        return null;
    }


    private QueryWrapper<PokaYokeConfig> getByDtoQueryWrapper(PokaYokeConfigQueryDto dto) {
        QueryWrapper<PokaYokeConfig> qw = new QueryWrapper<>();
        qw.lambda().eq(PokaYokeConfig::getDelFlag,"0");
        qw.lambda().eq(null != dto.getType(),PokaYokeConfig::getType,dto.getType());
        qw.lambda().like(StringUtils.isNotBlank(dto.getBrandName()),PokaYokeConfig::getBrandName,dto.getBrandName());
        qw.lambda().eq(StringUtils.isNotBlank(dto.getCategory1Code()),PokaYokeConfig::getCategory1Code,dto.getCategory1Code());
        qw.lambda().like(StringUtils.isNotBlank(dto.getCategory1Name()),PokaYokeConfig::getCategory1Name,dto.getCategory1Name());
        qw.lambda().eq(StringUtils.isNotBlank(dto.getCategory2Code()),PokaYokeConfig::getCategory2Code,dto.getCategory2Code());
        qw.lambda().like(StringUtils.isNotBlank(dto.getCategory2Name()),PokaYokeConfig::getCategory2Name,dto.getCategory2Name());
        qw.lambda().eq(StringUtils.isNotBlank(dto.getCategory3Code()),PokaYokeConfig::getCategory3Code,dto.getCategory3Code());
        qw.lambda().like(StringUtils.isNotBlank(dto.getCategory3Name()),PokaYokeConfig::getCategory3Name,dto.getCategory3Name());
        qw.lambda().like(StringUtils.isNotBlank(dto.getMaterialCode()),PokaYokeConfig::getMaterialCode,dto.getMaterialCode());
        qw.lambda().eq(null != dto.getExecuteLevel(),PokaYokeConfig::getExecuteLevel,dto.getExecuteLevel());
        return qw;
    }

    private void checkSaveOrUpdate(PokaYokeConfigVo vo, boolean isUpdate) {
        //检查物料号
        checkMaterialCode(vo);

        QueryWrapper<PokaYokeConfig> qw = new QueryWrapper<>();
        if (isUpdate){
            qw.lambda().ne(PokaYokeConfig::getId,vo.getId());
        }
        qw.lambda().eq(PokaYokeConfig::getDelFlag,"0");
        qw.lambda().eq(PokaYokeConfig::getType,vo.getType());
        qw.lambda().eq(StringUtils.isNotBlank(vo.getCategory1Code()),PokaYokeConfig::getCategory1Code,vo.getCategory1Code());

        if (StringUtils.isNotBlank(vo.getCategory3Code())){
            qw.lambda().eq(PokaYokeConfig::getCategory2Code,vo.getCategory2Code());
        }else {
            qw.lambda().eq(PokaYokeConfig::getCategory2Code,"");
        }

        if (StringUtils.isNotBlank(vo.getCategory3Code())){
            qw.lambda().eq(PokaYokeConfig::getCategory3Code,vo.getCategory3Code());
        }else {
            qw.lambda().eq(PokaYokeConfig::getCategory3Code,"");
        }

        if (StringUtils.isNotBlank(vo.getMaterialCode())){
            qw.lambda().like(PokaYokeConfig::getMaterialCode,vo.getMaterialCode());
        }else {
            qw.lambda().eq(PokaYokeConfig::getMaterialCode,"");
        }

        if (StringUtils.isNotBlank(vo.getBrandName())){
            qw.lambda().last("and " + lastBrand(vo.getBrandName(),"brand_name"));
        }
        List<PokaYokeConfig> list = list(qw);
        if (CollUtil.isNotEmpty(list)){
            throw new OtherException("已存在相同配置的数据, 请检查！");
        }
    }

    private void checkMaterialCode(PokaYokeConfigVo vo) {
        if (StringUtils.isEmpty(vo.getMaterialCode())){
            return;
        }
        QueryWrapper<PokaYokeConfig> qw = new QueryWrapper<>();
        qw.lambda().like(PokaYokeConfig::getMaterialCode,vo.getMaterialCode());
        if (StringUtils.isNotBlank(vo.getId())){
            qw.lambda().ne(PokaYokeConfig::getId,vo.getId());
        }
        if (StringUtils.isNotBlank(vo.getBrandName())){
            qw.lambda().last("and " + lastBrand(vo.getBrandName(),"brand_name"));
        }
        List<PokaYokeConfig> list = list(qw);
        if (CollUtil.isNotEmpty(list)){
            throw new OtherException("已存在该物料号的配置, 请检查！");
        }

        BasicsdatumMaterial materialByCode = basicsdatumMaterialService.getMaterialByCode(vo.getMaterialCode());
        if (null == materialByCode){
            throw new OtherException("物料号不存在!");
        }
        if (!vo.getCategory1Code().equals(materialByCode.getCategory1Code())){
            throw new OtherException("该物料号不在此大类中!");
        }
        if (StringUtils.isNotBlank(vo.getCategory2Code()) && !vo.getCategory2Code().equals(materialByCode.getCategory2Code())){
            throw new OtherException("该物料号不在此中类中!");
        }
        if (StringUtils.isNotBlank(vo.getCategory3Code()) && !vo.getCategory3Code().equals(materialByCode.getCategory3Code())){
            throw new OtherException("该物料号不在此小类中!");
        }

    }

    private String lastBrand(String data, String column){
        StringBuilder buffer = new StringBuilder();
        buffer.append("(");
        List<String> list = StringUtils.convertList(data);

        for (int i = 0; i < list.size(); i++) {
            buffer.append("FIND_IN_SET").append("(").append("'").append(list.get(i)).append("'").append(",").append(column).append(")");
            if (i != (list.size() -1)){
                buffer.append(" or ");
            }
        }
        buffer.append(")");
        return buffer.toString();
    }


    private PokaYokeConfig queryConditionBom(PokaYokeConfigQueryDto dto) {
            if (StringUtils.isEmpty(dto.getMaterialCode()) || StringUtils.isEmpty(dto.getForeignId())){
            return null;
        }
        //获取品牌
        String brandName = packInfoService.getByIdBrandName(dto.getForeignId());
        if (StringUtils.isEmpty(brandName)){
            return null;
        }
        QueryWrapper<PokaYokeConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(PokaYokeConfig::getDelFlag,"0");
        queryWrapper.lambda().eq(PokaYokeConfig::getMaterialCode,dto.getMaterialCode());
        queryWrapper.lambda().last("and "+ lastBrand(brandName,"brand_name"));
        List<PokaYokeConfig> list = list(queryWrapper);
        if (CollUtil.isNotEmpty(list)){
            return list.get(0);
        }
        BasicsdatumMaterial materialByCode = basicsdatumMaterialService.getMaterialByCode(dto.getMaterialCode());
        if (Objects.isNull(materialByCode)){
            return null;
        }
        QueryWrapper<PokaYokeConfig> q1 = getByBrandName(brandName);
        PokaYokeConfig controlCondition = null;
        q1.lambda().eq(PokaYokeConfig::getMaterialCode,"");
        q1.lambda().eq(PokaYokeConfig::getCategory1Code,materialByCode.getCategory1Code());
        q1.lambda().eq(PokaYokeConfig::getCategory2Code,materialByCode.getCategory2Code());
        q1.lambda().eq(PokaYokeConfig::getCategory3Code,materialByCode.getCategory3Code());
        controlCondition = getControlCondition(q1);
        if (!Objects.isNull(controlCondition)){
            return controlCondition;
        }
        QueryWrapper<PokaYokeConfig> q2 = getByBrandName(brandName);
        q2.lambda().eq(PokaYokeConfig::getMaterialCode,"");
        q2.lambda().eq(PokaYokeConfig::getCategory1Code,materialByCode.getCategory1Code());
        q2.lambda().eq(PokaYokeConfig::getCategory2Code,materialByCode.getCategory2Code());
        q2.lambda().eq(PokaYokeConfig::getCategory3Code,"");
        controlCondition = getControlCondition(q2);
        if (!Objects.isNull(controlCondition)){
            return controlCondition;
        }
        QueryWrapper<PokaYokeConfig> q3 = getByBrandName(brandName);
        q3.lambda().eq(PokaYokeConfig::getMaterialCode,"");
        q3.lambda().eq(PokaYokeConfig::getCategory1Code,materialByCode.getCategory1Code());
        q3.lambda().eq(PokaYokeConfig::getCategory2Code,"");
        q3.lambda().eq(PokaYokeConfig::getCategory3Code,"");
        return getControlCondition(q3);
    }

    private QueryWrapper<PokaYokeConfig> getByBrandName(String brandName){
        QueryWrapper<PokaYokeConfig> qw = new QueryWrapper<>();
        qw.lambda().last("and "+ lastBrand(brandName,"brand_name"));
        return qw;
    }


    private PokaYokeConfig getControlCondition(QueryWrapper<PokaYokeConfig> qw){
        List<PokaYokeConfig> list = list(qw);
        if (CollUtil.isEmpty(list)){
            return null;
        }
        return list.get(0);
    }

// 自定义方法区 不替换的区域【other_start】


// 自定义方法区 不替换的区域【other_end】

}
