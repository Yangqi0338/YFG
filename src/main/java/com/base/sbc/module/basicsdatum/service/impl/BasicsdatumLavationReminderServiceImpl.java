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
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.minio.MinioUtils;
import com.base.sbc.config.utils.ExcelUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.AddRevampBasicsdatumLavationReminderDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumLavationReminderExcelDto;
import com.base.sbc.module.basicsdatum.dto.QueryDto;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumLavationReminder;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterialsIngredient;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumLavationReminderMapper;
import com.base.sbc.module.basicsdatum.service.BasicsdatumLavationReminderService;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumLavationReminderVo;
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
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 类描述：基础资料-洗涤图标与温馨提示 service类
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumLavationReminderService
 * @author mengfanjiang
 * @email 2915350015@qq.com
 * @date 创建时间：2023-5-19 19:15:00
 * @version 1.0
 */
@Service
public class BasicsdatumLavationReminderServiceImpl extends BaseServiceImpl<BasicsdatumLavationReminderMapper, BasicsdatumLavationReminder> implements BasicsdatumLavationReminderService {

        @Autowired
        private BaseController baseController;

        @Autowired
        private UploadFileService uploadFileService;

        @Autowired
        private MinioUtils minioUtils;

        @Autowired
        private CcmFeignService ccmFeignService;

/** 自定义方法区 不替换的区域【other_start】 **/

        /**
        * 基础资料-洗涤图标与温馨提示分页查询
        *
        * @param queryDto
        * @return
        */
        @Override
        public PageInfo<BasicsdatumLavationReminderVo> getBasicsdatumLavationReminderList(QueryDto queryDto) {
            /*分页*/
            PageHelper.startPage(queryDto);
            QueryWrapper<BasicsdatumLavationReminder> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("lr.company_code", baseController.getUserCompany());
            queryWrapper.eq("lr.del_flag", BaseGlobal.DEL_FLAG_NORMAL);
            queryWrapper.eq(StringUtils.isNotEmpty(queryDto.getStatus()), "lr.status", queryDto.getStatus());
            queryWrapper.like(StringUtils.isNotEmpty(queryDto.getCode()), "lr.code", queryDto.getCode());
            queryWrapper.like(StringUtils.isNotEmpty(queryDto.getCareLabel()),"lr.care_label",queryDto.getCareLabel());
            /*查询基础资料-洗涤图标与温馨提示数据*/
            List<BasicsdatumLavationReminderVo> basicsdatumLavationReminderList = baseMapper.getLavationReminderList(queryWrapper);
            PageInfo<BasicsdatumLavationReminderVo> pageInfo = new PageInfo<>(basicsdatumLavationReminderList);
            return pageInfo;
        }



       /**
       * 基础资料-洗涤图标与温馨提示导入
       *
       * @param file
       * @return
       */
       @Override
       public Boolean basicsdatumLavationReminderImportExcel(MultipartFile file) throws Exception {
            ImportParams params = new ImportParams();
            params.setNeedSave(false);
            List<BasicsdatumLavationReminderExcelDto> list = ExcelImportUtil.importExcel(file.getInputStream(), BasicsdatumLavationReminderExcelDto.class, params);
           list =  list.stream().filter(p -> StringUtils.isNotBlank(p.getCode())).collect(Collectors.toList());
           /*获取字典值*/
           Map<String, Map<String, String>> dictInfoToMap = ccmFeignService.getDictInfoToMap("wxts");
           Map<String, String> map =   dictInfoToMap.get("wxts");
           for (BasicsdatumLavationReminderExcelDto basicsdatumLavationReminderExcelDto : list) {
               if(!StringUtils.isEmpty(basicsdatumLavationReminderExcelDto.getPicture())){
                   if (StringUtils.isNotEmpty(basicsdatumLavationReminderExcelDto.getPicture())) {
                       File file1 = new File(basicsdatumLavationReminderExcelDto.getPicture());
                       /*上传图*/
                       AttachmentVo attachmentVo = uploadFileService.uploadToMinio(minioUtils.convertFileToMultipartFile(file1));
                       basicsdatumLavationReminderExcelDto.setPicture(attachmentVo.getUrl());
                   }
               }
               if(StringUtils.isNotBlank(basicsdatumLavationReminderExcelDto.getReminderName())){
                   for(Map.Entry<String, String> entry : map.entrySet()){
                       String key = entry.getKey();
                       String value = entry.getValue();
                       if (value.equals(basicsdatumLavationReminderExcelDto.getReminderName())){
                           basicsdatumLavationReminderExcelDto.setReminderCode(key);
                           basicsdatumLavationReminderExcelDto.setReminderName(value);
                           break;
                       }
                   }
               }
           }

            List<BasicsdatumLavationReminder> basicsdatumLavationReminderList = BeanUtil.copyToList(list, BasicsdatumLavationReminder.class);
           for (BasicsdatumLavationReminder basicsdatumLavationReminder : basicsdatumLavationReminderList) {
               QueryWrapper<BasicsdatumLavationReminder> queryWrapper =new BaseQueryWrapper<>();
               queryWrapper.eq("code",basicsdatumLavationReminder.getCode());
               this.saveOrUpdate(basicsdatumLavationReminder,queryWrapper);
           }
            return true;
       }

        /**
        * 基础资料-洗涤图标与温馨提示导出
        *
        * @param
        * @return
        */
        @Override
        public void basicsdatumLavationReminderDeriveExcel(HttpServletResponse response) throws Exception {
        QueryWrapper<BasicsdatumLavationReminder> queryWrapper=new QueryWrapper<>();
        List<BasicsdatumLavationReminderExcelDto> list = BeanUtil.copyToList( baseMapper.selectList(queryWrapper), BasicsdatumLavationReminderExcelDto.class);
        ExcelUtils.exportExcel(list,  BasicsdatumLavationReminderExcelDto.class, "基础资料-洗涤图标与温馨提示.xlsx",new ExportParams() ,response);
        }



        /**
        * 方法描述：新增修改基础资料-洗涤图标与温馨提示
        *
        * @param addRevampBasicsdatumLavationReminderDto 基础资料-洗涤图标与温馨提示Dto类
        * @return boolean
        */
        @Override
        public Boolean addRevampBasicsdatumLavationReminder(AddRevampBasicsdatumLavationReminderDto addRevampBasicsdatumLavationReminderDto) {
            BasicsdatumLavationReminder basicsdatumLavationReminder = new BasicsdatumLavationReminder();
            QueryWrapper<BasicsdatumLavationReminder> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("code",addRevampBasicsdatumLavationReminderDto.getCode());
            /*查询数据是否存在*/
            List<BasicsdatumLavationReminder> list = baseMapper.selectList(queryWrapper);
            if (StringUtils.isEmpty(addRevampBasicsdatumLavationReminderDto.getId())) {
                if(!CollectionUtils.isEmpty(list)){
                    throw new OtherException(BaseErrorEnum.ERR_INSERT_DATA_REPEAT);
                }
                /*新增*/
                BeanUtils.copyProperties(addRevampBasicsdatumLavationReminderDto, basicsdatumLavationReminder);
                basicsdatumLavationReminder.setCompanyCode(baseController.getUserCompany());
                basicsdatumLavationReminder.insertInit();
                baseMapper.insert(basicsdatumLavationReminder);
           } else {
                /*修改*/
                basicsdatumLavationReminder = baseMapper.selectById(addRevampBasicsdatumLavationReminderDto.getId());
                if (ObjectUtils.isEmpty(basicsdatumLavationReminder)) {
                throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
                }
                if(!addRevampBasicsdatumLavationReminderDto.getCode().equals(addRevampBasicsdatumLavationReminderDto.getCode()) && !CollectionUtils.isEmpty(list)){
                    throw new OtherException(BaseErrorEnum.ERR_INSERT_DATA_REPEAT);
                }
                BeanUtils.copyProperties(addRevampBasicsdatumLavationReminderDto, basicsdatumLavationReminder);
                basicsdatumLavationReminder.updateInit();
                baseMapper.updateById(basicsdatumLavationReminder);
                }
                return true;
         }


         /**
         * 方法描述：删除基础资料-洗涤图标与温馨提示
         *
         * @param id （多个用，）
         * @return boolean
         */
         @Override
         public Boolean delBasicsdatumLavationReminder(String id) {
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
          public Boolean startStopBasicsdatumLavationReminder(StartStopDto startStopDto) {
            UpdateWrapper<BasicsdatumLavationReminder> updateWrapper = new UpdateWrapper<>();
            updateWrapper.in("id",StringUtils.convertList(startStopDto.getIds()));
            updateWrapper.set("status", startStopDto.getStatus());
            /*修改状态*/
             return baseMapper.update(null, updateWrapper) > 0;
          }

      /** 自定义方法区 不替换的区域【other_end】 **/

}
