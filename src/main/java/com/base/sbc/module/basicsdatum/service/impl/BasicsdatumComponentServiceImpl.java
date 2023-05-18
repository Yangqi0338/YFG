/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.service.impl;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.FilesUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.AddRevampComponentDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumComponentExcelDto;
import com.base.sbc.module.basicsdatum.dto.QueryDto;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumComponent;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumComponentMapper;
import com.base.sbc.module.basicsdatum.service.BasicsdatumComponentService;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumComponentVo;
import com.base.sbc.module.common.service.impl.ServicePlusImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;

/**
 * 类描述：基础资料-部件 service类
 *
 * @author mengfanjiang
 * @version 1.0
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumComponentService
 * @email 2915350015@qq.com
 * @date 创建时间：2023-5-10 11:37:55
 */
@Service
public class BasicsdatumComponentServiceImpl extends ServicePlusImpl<BasicsdatumComponentMapper, BasicsdatumComponent> implements BasicsdatumComponentService {

    @Autowired
    private BaseController baseController;
    @Autowired
    private FilesUtils filesUtils;

    @Override
    public PageInfo<BasicsdatumComponentVo> getComponentList(QueryDto queryDto) {
        /*分页*/
        PageHelper.startPage(queryDto);
        QueryWrapper<BasicsdatumComponent> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("company_code", baseController.getUserCompany());
        queryWrapper.in(StrUtil.isNotEmpty(queryDto.getCoding()), "coding", queryDto.getCoding());
        /*查询部件数据*/
        List<BasicsdatumComponent> basicsdatumComponentList = baseMapper.selectList(queryWrapper);
        PageInfo<BasicsdatumComponent> pageInfo = new PageInfo<>(basicsdatumComponentList);
        /*转换vo*/
        List<BasicsdatumComponentVo> list = BeanUtil.copyToList(basicsdatumComponentList, BasicsdatumComponentVo.class);
        PageInfo<BasicsdatumComponentVo> pageInfo1 = new PageInfo<BasicsdatumComponentVo>();
        pageInfo1.setList(list);
        pageInfo1.setTotal(pageInfo.getTotal());
        pageInfo1.setPageNum(pageInfo.getPageNum());
        pageInfo1.setPageSize(pageInfo.getPageSize());
        return pageInfo1;

    }

    @Override
    public Boolean importExcel(MultipartFile file) throws Exception {
        ImportParams params = new ImportParams();
        params.setNeedSave(false);
        List<BasicsdatumComponentExcelDto> list = ExcelImportUtil.importExcel(file.getInputStream(), BasicsdatumComponentExcelDto.class, params);
        for (BasicsdatumComponentExcelDto basicsdatumComponentExcelDto : list) {
            //如果图片不为空
            if (StringUtils.isNotEmpty(basicsdatumComponentExcelDto.getImage())) {
                File file1 = new File(basicsdatumComponentExcelDto.getImage());
                String s=  filesUtils.upload(filesUtils.convertFileToMultipartFile(file1), FilesUtils.PRODUCT,baseController.getUserCompany());
                basicsdatumComponentExcelDto.setImage(s);
            }
            basicsdatumComponentExcelDto.setStatus(basicsdatumComponentExcelDto.getStatus().equals("true")?"0":"1");
        }
        List<BasicsdatumComponent> basicsdatumComponentList = BeanUtil.copyToList(list, BasicsdatumComponent.class);
        saveBatch(basicsdatumComponentList);
        return true;
    }

    @Override
    public Boolean addRevampComponent(AddRevampComponentDto addRevampComponentDto) {
        BasicsdatumComponent basicsdatumComponent = new BasicsdatumComponent();
        if (StringUtils.isEmpty(addRevampComponentDto.getId())) {
            QueryWrapper<BasicsdatumComponent> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("coding",addRevampComponentDto.getCoding());
            /*查询数据是否存在*/
            List<BasicsdatumComponent> list = baseMapper.selectList(queryWrapper);
            if(!CollectionUtils.isEmpty(list)){
                throw new OtherException(BaseErrorEnum.ERR_INSERT_DATA_REPEAT);
            }
            /*新增*/
            BeanUtils.copyProperties(addRevampComponentDto, basicsdatumComponent);
            basicsdatumComponent.setCompanyCode(baseController.getUserCompany());
            basicsdatumComponent.insertInit();
            baseMapper.insert(basicsdatumComponent);
        } else {
            /*修改*/
            basicsdatumComponent = baseMapper.selectById(addRevampComponentDto.getId());
            if (ObjectUtils.isEmpty(basicsdatumComponent)) {
                throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
            }
            BeanUtils.copyProperties(addRevampComponentDto, basicsdatumComponent);
            basicsdatumComponent.updateInit();
            baseMapper.updateById(basicsdatumComponent);
        }
        return true;
    }

    @Override
    public Boolean delComponent(String id) {
        List<String> ids = StringUtils.convertList(id);
        /*批量删除*/
        baseMapper.deleteBatchIds(ids);
        return true;
    }

    @Override
    public Boolean componentStartStop(StartStopDto startStopDto) {
        UpdateWrapper<BasicsdatumComponent> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id",StringUtils.convertList(startStopDto.getIds()));
        updateWrapper.set("status", startStopDto.getStatus());
        /*修改状态*/
        return baseMapper.update(null, updateWrapper) > 0;
    }

}
