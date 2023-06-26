/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.service.impl;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.minio.MinioUtils;
import com.base.sbc.config.utils.ExcelUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.*;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMattersAttention;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumMattersAttentionMapper;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMattersAttentionService;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumMattersAttentionVo;
import com.base.sbc.module.common.service.UploadFileService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.common.vo.AttachmentVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;

/** 
 * 类描述：基础资料-注意事项 service类
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumMattersAttentionService
 * @author mengfanjiang
 * @email 2915350015@qq.com
 * @date 创建时间：2023-5-19 19:15:01
 * @version 1.0  
 */
@Service
public class BasicsdatumMattersAttentionBaseServiceImpl extends BaseServiceImpl<BasicsdatumMattersAttentionMapper, BasicsdatumMattersAttention> implements BasicsdatumMattersAttentionService {

        @Autowired
        private BaseController baseController;

        @Autowired
        private UploadFileService uploadFileService;

        @Autowired
        private MinioUtils minioUtils;

/** 自定义方法区 不替换的区域【other_start】 **/

        /**
        * 基础资料-注意事项分页查询
        *
        * @param queryDto
        * @return
        */
        @Override
        public PageInfo<BasicsdatumMattersAttentionVo> getBasicsdatumMattersAttentionList(QueryDto queryDto) {
            /*分页*/
            PageHelper.startPage(queryDto);
            QueryWrapper<BasicsdatumMattersAttention> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("company_code", baseController.getUserCompany());
            /*查询基础资料-注意事项数据*/
            List<BasicsdatumMattersAttention> basicsdatumMattersAttentionList = baseMapper.selectList(queryWrapper);
            PageInfo<BasicsdatumMattersAttention> pageInfo = new PageInfo<>(basicsdatumMattersAttentionList);
            /*转换vo*/
            List<BasicsdatumMattersAttentionVo> list = BeanUtil.copyToList(basicsdatumMattersAttentionList, BasicsdatumMattersAttentionVo.class);
            PageInfo<BasicsdatumMattersAttentionVo> pageInfo1 = new PageInfo<>();
            pageInfo1.setList(list);
            pageInfo1.setTotal(pageInfo.getTotal());
            pageInfo1.setPageNum(pageInfo.getPageNum());
            pageInfo1.setPageSize(pageInfo.getPageSize());
            return pageInfo1;
        }



       /**
       * 基础资料-注意事项导入
       *
       * @param file
       * @return
       */
       @Override
       public Boolean basicsdatumMattersAttentionImportExcel(MultipartFile file) throws Exception {
           ImportParams params = new ImportParams();
           params.setNeedSave(false);
           List<BasicsdatumMattersAttentionExcelDto> list = ExcelImportUtil.importExcel(file.getInputStream(), BasicsdatumMattersAttentionExcelDto.class, params);
           for (BasicsdatumMattersAttentionExcelDto basicsdatumMattersAttentionExcelDto : list) {
               if (StringUtils.isNotEmpty(basicsdatumMattersAttentionExcelDto.getPicture())) {
                   File file1 = new File(basicsdatumMattersAttentionExcelDto.getPicture());
                   /*上传图*/
                   AttachmentVo attachmentVo = uploadFileService.uploadToMinio(minioUtils.convertFileToMultipartFile(file1));
                   basicsdatumMattersAttentionExcelDto.setPicture(attachmentVo.getUrl());
               }
           }
           List<BasicsdatumMattersAttention> basicsdatumMattersAttentionList = BeanUtil.copyToList(list, BasicsdatumMattersAttention.class);
           saveOrUpdateBatch(basicsdatumMattersAttentionList);
           return true;
       }

        /**
        * 基础资料-注意事项导出
        *
        * @param
        * @return
        */
        @Override
        public void basicsdatumMattersAttentionDeriveExcel(HttpServletResponse response) throws Exception {
        QueryWrapper<BasicsdatumMattersAttention> queryWrapper=new QueryWrapper<>();
        List<BasicsdatumMattersAttentionExcelDto> list = BeanUtil.copyToList( baseMapper.selectList(queryWrapper), BasicsdatumMattersAttentionExcelDto.class);
        ExcelUtils.exportExcel(list,  BasicsdatumMattersAttentionExcelDto.class, "基础资料-注意事项.xlsx",new ExportParams() ,response);
        }



        /**
        * 方法描述：新增修改基础资料-注意事项
        *
        * @param addRevampBasicsdatumMattersAttentionDto 基础资料-注意事项Dto类
        * @return boolean
        */
        @Override
        public Boolean addRevampBasicsdatumMattersAttention(AddRevampBasicsdatumMattersAttentionDto addRevampBasicsdatumMattersAttentionDto) {
                BasicsdatumMattersAttention basicsdatumMattersAttention = new BasicsdatumMattersAttention();
            if (StringUtils.isEmpty(addRevampBasicsdatumMattersAttentionDto.getId())) {
                QueryWrapper<BasicsdatumMattersAttention> queryWrapper=new QueryWrapper<>();
                queryWrapper.eq("coding",addRevampBasicsdatumMattersAttentionDto.getCoding());
                queryWrapper.eq("company_code",baseController.getUserCompany());
                if(!CollectionUtils.isEmpty(baseMapper.selectList(queryWrapper))){
                 throw new OtherException(BaseErrorEnum.ERR_INSERT_DATA_REPEAT);
                }
                /*新增*/
                BeanUtils.copyProperties(addRevampBasicsdatumMattersAttentionDto, basicsdatumMattersAttention);
                basicsdatumMattersAttention.setCompanyCode(baseController.getUserCompany());
                basicsdatumMattersAttention.insertInit();
                baseMapper.insert(basicsdatumMattersAttention);
           } else {
                /*修改*/
                basicsdatumMattersAttention = baseMapper.selectById(addRevampBasicsdatumMattersAttentionDto.getId());
                if (ObjectUtils.isEmpty(basicsdatumMattersAttention)) {
                throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
                }
                BeanUtils.copyProperties(addRevampBasicsdatumMattersAttentionDto, basicsdatumMattersAttention);
                basicsdatumMattersAttention.updateInit();
                baseMapper.updateById(basicsdatumMattersAttention);
                }
                return true;
         }


         /**
         * 方法描述：删除基础资料-注意事项
         *
         * @param id （多个用，）
         * @return boolean
         */
         @Override
         public Boolean delBasicsdatumMattersAttention(String id) {
         List<String> ids = StringUtils.convertList(id);
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
          public Boolean startStopBasicsdatumMattersAttention(StartStopDto startStopDto) {
            UpdateWrapper<BasicsdatumMattersAttention> updateWrapper = new UpdateWrapper<>();
            updateWrapper.in("id",StringUtils.convertList(startStopDto.getIds()));
            updateWrapper.set("status", startStopDto.getStatus());
            /*修改状态*/
             return baseMapper.update(null, updateWrapper) > 0;
          }

      /** 自定义方法区 不替换的区域【other_end】 **/

}
