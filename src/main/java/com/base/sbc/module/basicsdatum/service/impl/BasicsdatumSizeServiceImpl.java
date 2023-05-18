/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.BaseEntity;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.ExcelUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.*;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumSizeLabel;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumSizeLabelMapper;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumSizeVo;
import com.base.sbc.module.common.service.impl.ServicePlusImpl;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumSizeMapper;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumSize;
import com.base.sbc.module.basicsdatum.service.BasicsdatumSizeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：基础资料-尺码表 service类
 *
 * @author mengfanjiang
 * @version 1.0
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumSizeService
 * @email 2915350015@qq.com
 * @date 创建时间：2023-5-17 14:01:34
 */
@Service
public class BasicsdatumSizeServiceImpl extends ServicePlusImpl<BasicsdatumSizeMapper, BasicsdatumSize> implements BasicsdatumSizeService {

    @Autowired
    private BaseController baseController;

    @Autowired
    private BasicsdatumSizeLabelMapper basicsdatumSizeLabelMapper;

    /**
     * 查询尺码列表
     *
     * @param queryDasicsdatumSizeDto
     * @return
     */
    @Override
    public PageInfo<BasicsdatumSizeVo> getSizeList(QueryDasicsdatumSizeDto queryDasicsdatumSizeDto) {
        if (StringUtils.isEmpty(queryDasicsdatumSizeDto.getSizeLabelId())) {
            throw new OtherException("标签id为空");
        }
        /*分页*/
        PageHelper.startPage(queryDasicsdatumSizeDto);
        QueryWrapper<BasicsdatumSize> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("company_code", baseController.getUserCompany());
        queryWrapper.eq("size_label_id", queryDasicsdatumSizeDto.getSizeLabelId());
        if (!StringUtils.isEmpty(queryDasicsdatumSizeDto.getSearch())) {
            queryWrapper.like("hangtags", queryDasicsdatumSizeDto.getSearch()).or().like("model", queryDasicsdatumSizeDto.getSearch());
        }
        /*查询部件数据*/
        List<BasicsdatumSize> basicsdatumSizeList = baseMapper.selectList(queryWrapper);
        PageInfo<BasicsdatumSize> pageInfo = new PageInfo(basicsdatumSizeList);
        /*转换vo*/
        List<BasicsdatumSizeVo> list = BeanUtil.copyToList(basicsdatumSizeList, BasicsdatumSizeVo.class);
        PageInfo<BasicsdatumSizeVo> vo = new PageInfo<>();
        vo.setList(list);
        vo.setTotal(pageInfo.getTotal());
        vo.setPageNum(pageInfo.getPageNum());
        vo.setPageSize(pageInfo.getPageSize());
        return vo;
    }

    /**
     * 尺码导入
     *
     * @param file
     * @return
     */
    @Override
    public Boolean importExcel(MultipartFile file) throws Exception {
        ImportParams params = new ImportParams();
        params.setNeedSave(false);
        /*需要新增的尺码标签*/
        List<BasicsdatumSizeLabel> labelList = new ArrayList<>();
        List<BasicsdatumSizeExcelDto> list = ExcelImportUtil.importExcel(file.getInputStream(), BasicsdatumSizeExcelDto.class, params);
        for (BasicsdatumSizeExcelDto basicsdatumSizeExcelDto : list) {
            if (StringUtils.isEmpty(basicsdatumSizeExcelDto.getLabelName())) {
                basicsdatumSizeExcelDto.setLabelName("其他");
            }

            /*查询在尺码标签是否存在*/
            QueryWrapper<BasicsdatumSizeLabel> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("label_name", basicsdatumSizeExcelDto.getLabelName());
            /*校验数据*/
            List<BasicsdatumSizeLabel> verifyList = basicsdatumSizeLabelMapper.selectList(queryWrapper);
            BasicsdatumSizeLabel basicsdatumSizeLabel = new BasicsdatumSizeLabel();
            if (CollectionUtils.isEmpty(verifyList)) {
                basicsdatumSizeLabel.setLabelName(basicsdatumSizeExcelDto.getLabelName());
                basicsdatumSizeLabel.insertInit();
                basicsdatumSizeLabelMapper.insert(basicsdatumSizeLabel);
                basicsdatumSizeExcelDto.setSizeLabelId(basicsdatumSizeLabel.getId());
            } else {
                basicsdatumSizeExcelDto.setSizeLabelId(verifyList.get(0).getId());
            }
            basicsdatumSizeExcelDto.setStatus(basicsdatumSizeExcelDto.getStatus().equals("true")?"0":"1");
            basicsdatumSizeExcelDto.setShowSizeStatus(basicsdatumSizeExcelDto.getShowSizeStatus().equals("true")?"0":"1");

        }
        List<BasicsdatumSize> basicsdatumSizeList = BeanUtil.copyToList(list, BasicsdatumSize.class);

        /*每次添加500条数据*/
        int batchSize = 500;
        for (int i = 0; i < basicsdatumSizeList.size(); i += batchSize) {
            int endIndex = Math.min(i + batchSize, basicsdatumSizeList.size());
            List<BasicsdatumSize> list1 = basicsdatumSizeList.subList(i, endIndex);
            saveBatch(list1);
        }
        return true;
    }

    /**
     * 导出
     *
     * @param queryDasicsdatumSizeDto isDerive 1导出，0模板导出,sizeLabelId 为空导出所有数据
     * @param response
     */
    @Override
    public void deriveExcel(QueryDasicsdatumSizeDto queryDasicsdatumSizeDto, HttpServletResponse response) throws IOException {
        if(StringUtils.isEmpty( queryDasicsdatumSizeDto.getIsDerive())){
            throw new OtherException("失败");
        }

        String fileName="";

        List<BasicsdatumSizeExcelDto> list =new ArrayList<>();

        if(queryDasicsdatumSizeDto.getIsDerive().equals(BaseGlobal.STATUS_NORMAL)){
            /*模板导出*/
            fileName="尺码模板";
        }else {
            fileName="尺码";
        }
        QueryWrapper<BasicsdatumSizeExcelDto> queryWrapper=new QueryWrapper<>();
        list = baseMapper.selectSize(queryWrapper);
        ExcelUtils.exportExcel(list, "人员名单", "名单", BasicsdatumSizeExcelDto.class, "人员名单.xlsx", response);


    }

    /**
     * 新增修改尺码表
     *
     * @param addRevampSizeDto
     * @return
     */
    @Override
    public Boolean addRevampSize(AddRevampSizeDto addRevampSizeDto) {

        BasicsdatumSize basicsdatumSize = new BasicsdatumSize();
        if (StringUtils.isEmpty(addRevampSizeDto.getId())) {
            if(StringUtils.isEmpty(addRevampSizeDto.getSizeLabelId())){
                throw new OtherException("尺码标签为空");
            }
 /*           QueryWrapper<BasicsdatumSize> queryWrapper=new QueryWrapper<>();
//            queryWrapper.eq("",addRevampSizeDto.getCoding());
              queryWrapper.eq("",addRevampSizeDto.getCoding());
            *//*查询数据是否存在*//*
            List<BasicsdatumSize> list = baseMapper.selectList(queryWrapper);
            if(!CollectionUtils.isEmpty(list)){
                throw new OtherException(BaseErrorEnum.ERR_INSERT_DATA_REPEAT);
            }*/
            /*新增*/
            BeanUtils.copyProperties(addRevampSizeDto, basicsdatumSize);
            basicsdatumSize.setCompanyCode(baseController.getUserCompany());
            basicsdatumSize.insertInit();
            baseMapper.insert(basicsdatumSize);
        } else {
            /*修改*/
            basicsdatumSize = baseMapper.selectById(addRevampSizeDto.getId());
            if (ObjectUtils.isEmpty(basicsdatumSize)) {
                throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
            }
            BeanUtils.copyProperties(addRevampSizeDto, basicsdatumSize);
            basicsdatumSize.updateInit();
            baseMapper.updateById(basicsdatumSize);
        }
        return true;
    }

    /**
     * 启动停止尺码
     *
     * @param startStopDto
     * @return
     */
    @Override
    public Boolean sizeStartStop(StartStopDto startStopDto) {
        UpdateWrapper<BasicsdatumSize> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", StringUtils.convertList(startStopDto.getIds()));
        updateWrapper.set("status", startStopDto.getStatus());
        /*修改状态*/
        return baseMapper.update(null, updateWrapper) > 0;
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @Override
    public Boolean delSize(String id) {
        List<String> ids = StringUtils.convertList(id);
        /*批量删除*/
        baseMapper.deleteBatchIds(ids);
        return true;
    }


}
