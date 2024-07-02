/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabricsummary.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.client.amc.enums.DataPermissionsBusinessTypeEnum;
import com.base.sbc.client.amc.service.DataPermissionsService;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.fabricsummary.entity.FabricSummaryGroup;
import com.base.sbc.module.fabricsummary.mapper.FabricSummaryGroupMapper;
import com.base.sbc.module.fabricsummary.service.FabricSummaryGroupService;
import com.base.sbc.module.sample.dto.FabricSummaryStyleMaterialDto;
import com.base.sbc.module.sample.vo.FabricSummaryGroupVo;
import com.beust.jcommander.internal.Lists;
import com.github.pagehelper.PageInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

import cn.hutool.core.bean.BeanUtil;

/**
 * 类描述：款式管理-面料汇总-组管理 service类
 * @address com.base.sbc.module.fabricsummary.service.FabricSummaryGroupService
 * @author your name
 * @email your email
 * @date 创建时间：2024-4-26 15:06:06
 * @version 1.0  
 */
@Service
public class FabricSummaryGroupServiceImpl extends BaseServiceImpl<FabricSummaryGroupMapper, FabricSummaryGroup> implements FabricSummaryGroupService {

    @Autowired
    private DataPermissionsService dataPermissionsService;

    public static void main(String[] args) {
        IdGen idGen = new IdGen();
        System.out.println(idGen.nextIdStr());
    }

    @Override
    public PageInfo<FabricSummaryGroupVo> getGroupList(FabricSummaryStyleMaterialDto dto) {
        BaseQueryWrapper<FabricSummaryGroup> qw = new BaseQueryWrapper<>();
        dataPermissionsService.getDataPermissionsForQw(qw, DataPermissionsBusinessTypeEnum.fabricSummaryList.getK(), "tps.", new String[]{"brand"}, true);
        qw.eq("tfsg.del_flag","0");
        if (StringUtils.isEmpty(dto.getMaterialCode()) && StringUtils.isEmpty(dto.getStyleNo()) && StringUtils.isEmpty(dto.getDesignNo())){
            if (StringUtils.isNotBlank(dto.getPlanningSeasonId())){
                qw.eq("tfsg.planning_season_id",dto.getPlanningSeasonId());
            }
        }else {
            BaseQueryWrapper queryWrapper = new BaseQueryWrapper<>();
            queryWrapper.eq(StringUtils.isNotEmpty(dto.getPlanningSeasonId()),"tfsg.planning_season_id",dto.getPlanningSeasonId());
            queryWrapper.eq(StringUtils.isNotEmpty(dto.getDesignNo()),"tfss.design_no",dto.getDesignNo());
            queryWrapper.eq(StringUtils.isNotEmpty(dto.getStyleNo()),"tfsg.style_no",dto.getStyleNo());
            queryWrapper.eq(StringUtils.isNotEmpty(dto.getMaterialCode()),"tfs.material_code",dto.getMaterialCode());
            List<String> idList = baseMapper.getGroupIds(queryWrapper);
            if (CollectionUtils.isEmpty(idList)){
                PageInfo<FabricSummaryGroupVo> pageInfo = new PageInfo<>();
                pageInfo.setList(Lists.newArrayList());
                BeanUtil.copyProperties(dto,pageInfo);
                return pageInfo;
            }
            qw.in(!CollectionUtils.isEmpty(idList),"tfsg.id",idList);
        }
        List<FabricSummaryGroup> list = baseMapper.getFabricSummaryGroupList(qw);
        PageInfo<FabricSummaryGroupVo> pageInfo = new PageInfo<>();
        List<FabricSummaryGroupVo> fabricSummaryGroupVos = BeanUtil.copyToList(list, FabricSummaryGroupVo.class);
        pageInfo.setList(fabricSummaryGroupVos);
        //TODO 靳帅 补充所属部门
        return pageInfo;
    }

    @Override
    public boolean deleteByIds(List<String> ids) {
        if (CollectionUtils.isEmpty(ids)){
            return true;
        }
        UpdateWrapper<FabricSummaryGroup> qw = new UpdateWrapper<>();
        qw.lambda().in(FabricSummaryGroup::getId,ids);
        qw.lambda().set(FabricSummaryGroup::getDelFlag,"1");
        return update(qw);
    }

// 自定义方法区 不替换的区域【other_start】



// 自定义方法区 不替换的区域【other_end】

}
