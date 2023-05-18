/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.AddRevampSizeLabelDto;
import com.base.sbc.module.basicsdatum.dto.QueryDasicsdatumSizeDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMeasurement;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumSize;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumSizeLabelVo;
import com.base.sbc.module.common.service.impl.ServicePlusImpl;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumSizeLabelMapper;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumSizeLabel;
import com.base.sbc.module.basicsdatum.service.BasicsdatumSizeLabelService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * 类描述：基础资料-尺码标签 service类
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumSizeLabelService
 * @author mengfanjiang
 * @email 2915350015@qq.com
 * @date 创建时间：2023-5-17 14:01:35
 * @version 1.0  
 */
@Service
public class BasicsdatumSizeLabelServiceImpl extends ServicePlusImpl<BasicsdatumSizeLabelMapper, BasicsdatumSizeLabel> implements BasicsdatumSizeLabelService {

    @Autowired
    private BaseController baseController;

    /**
     * @param queryDasicsdatumSizeDto
     * @return
     */
    @Override
    public  List<BasicsdatumSizeLabelVo> getSizeLabelList(QueryDasicsdatumSizeDto queryDasicsdatumSizeDto) {
        QueryWrapper<BasicsdatumSizeLabel> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("company_code",baseController.getUserCompany());
        if(!StringUtils.isEmpty(queryDasicsdatumSizeDto.getLabelName())){
            queryWrapper.eq("label_name",queryDasicsdatumSizeDto.getLabelName());
        }
        List<BasicsdatumSizeLabel> labelList= baseMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(labelList)){
            throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
        }
        List<BasicsdatumSizeLabelVo> voList= BeanUtil.copyToList(labelList, BasicsdatumSizeLabelVo.class);
        return voList;
    }

    /**
     * 新增修改 尺码标签
     *
     * @param addRevampSizeLabelDto
     * @return
     */
    @Override
    public Boolean addRevampSizeLabel(AddRevampSizeLabelDto addRevampSizeLabelDto) {

        BasicsdatumSizeLabel basicsdatumSizeLabel = new BasicsdatumSizeLabel();
        if (StringUtils.isEmpty(addRevampSizeLabelDto.getId())) {
            QueryWrapper<BasicsdatumSizeLabel> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("company_code",baseController.getUserCompany());
            queryWrapper.eq("label_name",addRevampSizeLabelDto.getLabelName());
//            查询数据是否存在
            List<BasicsdatumSizeLabel> list = baseMapper.selectList(queryWrapper);
            if(!CollectionUtils.isEmpty(list)){
                throw new OtherException(BaseErrorEnum.ERR_INSERT_DATA_REPEAT);
            }
            /*新增*/
            BeanUtils.copyProperties(addRevampSizeLabelDto, basicsdatumSizeLabel);
            basicsdatumSizeLabel.setCompanyCode(baseController.getUserCompany());
            basicsdatumSizeLabel.insertInit();
            baseMapper.insert(basicsdatumSizeLabel);
        } else {
            /*修改*/
            basicsdatumSizeLabel = baseMapper.selectById(addRevampSizeLabelDto.getId());
            if (ObjectUtils.isEmpty(basicsdatumSizeLabel)) {
                throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
            }
            BeanUtils.copyProperties(addRevampSizeLabelDto, basicsdatumSizeLabel);
            basicsdatumSizeLabel.updateInit();
            baseMapper.updateById(basicsdatumSizeLabel);
        }
        return true;
    }

    /**
     * 删除尺码标签
     *
     * @param id
     * @return
     */
    @Override
    public Boolean delSizeLabel(String id) {
        List<String> ids = StringUtils.convertList(id);
        /*批量删除*/
        baseMapper.deleteBatchIds(ids);
        return true;
    }

	
}
