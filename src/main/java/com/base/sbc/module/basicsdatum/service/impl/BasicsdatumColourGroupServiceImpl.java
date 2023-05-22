/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumColourLibrary;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumColourLibraryMapper;
import com.base.sbc.module.common.service.impl.ServicePlusImpl;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumColourGroupMapper;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumColourGroup;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumColourGroupVo;
import com.base.sbc.module.basicsdatum.service.BasicsdatumColourGroupService;
import com.base.sbc.module.basicsdatum.dto.AddRevampBasicsdatumColourGroupDto;
import org.springframework.beans.factory.annotation.Autowired;
import com.base.sbc.config.common.base.BaseController;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.PageHelper;
import cn.hutool.core.bean.BeanUtil;
import com.base.sbc.config.utils.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.beans.BeanUtils;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.basicsdatum.dto.QueryDto;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：基础资料-颜色组 service类
 *
 * @author mengfanjiang
 * @version 1.0
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumColourGroupService
 * @email 2915350015@qq.com
 * @date 创建时间：2023-5-20 20:23:03
 */
@Service
public class BasicsdatumColourGroupServiceImpl extends ServicePlusImpl<BasicsdatumColourGroupMapper, BasicsdatumColourGroup> implements BasicsdatumColourGroupService {

    @Autowired
    private BaseController baseController;

    @Autowired
    private BasicsdatumColourLibraryMapper basicsdatumColourLibraryMapper;

/** 自定义方法区 不替换的区域【other_start】 **/

    /**
     * 基础资料-颜色组分页查询
     *
     * @param queryDto
     * @return
     */
    @Override
    public List<BasicsdatumColourGroupVo> getBasicsdatumColourGroupList(QueryDto queryDto) {
        /*分页*/

        QueryWrapper<BasicsdatumColourGroup> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("company_code", baseController.getUserCompany());
        /*查询基础资料-颜色组数据*/
        List<BasicsdatumColourGroup> basicsdatumColourGroupList = baseMapper.selectList(queryWrapper);
        /*转换vo*/
        List<BasicsdatumColourGroupVo> list = new ArrayList<>();

        basicsdatumColourGroupList.forEach(basicsdatumColourGroup -> {
            BasicsdatumColourGroupVo basicsdatumColourGroupVo = new BasicsdatumColourGroupVo();
            basicsdatumColourGroupVo.setName(basicsdatumColourGroup.getColourName());
            basicsdatumColourGroupVo.setId(basicsdatumColourGroup.getId());
            list.add(basicsdatumColourGroupVo);
        });

        return list;
    }


    /**
     * 方法描述：新增修改基础资料-颜色组
     *
     * @param addRevampBasicsdatumColourGroupDto 基础资料-颜色组Dto类
     * @return boolean
     */
    @Override
    public Boolean addRevampBasicsdatumColourGroup(AddRevampBasicsdatumColourGroupDto addRevampBasicsdatumColourGroupDto) {
        BasicsdatumColourGroup basicsdatumColourGroup = new BasicsdatumColourGroup();
        if (StringUtils.isEmpty(addRevampBasicsdatumColourGroupDto.getId())) {
            QueryWrapper<BasicsdatumColourGroup> queryWrapper = new QueryWrapper<>();
            /*新增*/
            BeanUtils.copyProperties(addRevampBasicsdatumColourGroupDto, basicsdatumColourGroup);
            basicsdatumColourGroup.setCompanyCode(baseController.getUserCompany());
            basicsdatumColourGroup.insertInit();
            baseMapper.insert(basicsdatumColourGroup);
        } else {
            /*修改*/
            basicsdatumColourGroup = baseMapper.selectById(addRevampBasicsdatumColourGroupDto.getId());
            if (ObjectUtils.isEmpty(basicsdatumColourGroup)) {
                throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
            }
            BeanUtils.copyProperties(addRevampBasicsdatumColourGroupDto, basicsdatumColourGroup);
            basicsdatumColourGroup.updateInit();
            baseMapper.updateById(basicsdatumColourGroup);
        }
        return true;
    }


    /**
     * 方法描述：删除基础资料-颜色组
     *
     * @param id （多个用，）
     * @return boolean
     */
    @Override
    public Boolean delBasicsdatumColourGroup(String id) {
        List<String> ids = StringUtils.convertList(id);
        QueryWrapper<BasicsdatumColourLibrary> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("company_code", baseController.getUserCompany());
        queryWrapper.in("colour_group_id", ids);
        if (!CollectionUtils.isEmpty(basicsdatumColourLibraryMapper.selectList(queryWrapper))) {
            throw new OtherException(BaseErrorEnum.ERR_DELETE_ATTRIBUTE_NOT_REQUIREMENTS);
        }
        /*批量删除*/
        baseMapper.deleteBatchIds(ids);
        return true;
    }


    /**
     * 方法描述：启用停止
     *
     * @param startStopDto 启用停止Dto类
     * @return boolean
     */
    @Override
    public Boolean startStopBasicsdatumColourGroup(StartStopDto startStopDto) {
        UpdateWrapper<BasicsdatumColourGroup> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", StringUtils.convertList(startStopDto.getIds()));
        updateWrapper.set("status", startStopDto.getStatus());
        /*修改状态*/
        return baseMapper.update(null, updateWrapper) > 0;
    }

    /** 自定义方法区 不替换的区域【other_end】 **/

}
