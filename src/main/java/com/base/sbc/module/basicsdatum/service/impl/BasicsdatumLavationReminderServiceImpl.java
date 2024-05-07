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
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.StrJoiner;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.client.ccm.entity.BasicBaseDict;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.client.ccm.service.CcmService;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.constant.MoreLanguageProperties;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.ureport.minio.MinioUtils;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.config.utils.ExcelUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.AddRevampBasicsdatumLavationReminderDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumLavationReminderExcelDto;
import com.base.sbc.module.basicsdatum.dto.QueryDto;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumLavationReminder;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumWashIcon;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumLavationReminderMapper;
import com.base.sbc.module.basicsdatum.service.BasicsdatumLavationReminderService;
import com.base.sbc.module.basicsdatum.service.BasicsdatumWashIconService;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumLavationReminderVo;
import com.base.sbc.module.common.service.UploadFileService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.base.sbc.config.constant.Constants.COMMA;

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
        private MinioUtils minioUtils;

        @Autowired
        private CcmFeignService ccmFeignService;

        @Autowired
        private CcmService ccmService;

        @Autowired
        private BasicsdatumWashIconService basicsdatumWashIconService;

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
            queryWrapper.like(StringUtils.isNotBlank(queryDto.getName()),"wi.name", queryDto.getName());
            queryWrapper.eq(StringUtils.isNotEmpty(queryDto.getStatus()), "lr.status", queryDto.getStatus());
            queryWrapper.like(StringUtils.isNotEmpty(queryDto.getCode()), "lr.code", queryDto.getCode());
            queryWrapper.like(StringUtils.isNotEmpty(queryDto.getCareLabel()),"lr.care_label",queryDto.getCareLabel());
            queryWrapper.like(StringUtils.isNotEmpty(queryDto.getDescription()),"lr.description",queryDto.getDescription());

            /*查询基础资料-洗涤图标与温馨提示数据*/
            List<BasicsdatumLavationReminderVo> basicsdatumLavationReminderList = baseMapper.getLavationReminderList(queryWrapper);
            minioUtils.setObjectUrlToList(basicsdatumLavationReminderList, "url");
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
           list =  list.stream().filter(p -> StringUtils.isNotBlank(p.getCode()) || StrUtil.isNotBlank(p.getWashIconCode())).collect(Collectors.toList());
           List<String> washIconCodeList = list.stream().map(BasicsdatumLavationReminderExcelDto::getWashIconCode).collect(Collectors.toList());
           // 获取洗标名称
           List<BasicsdatumWashIcon> washIconList = basicsdatumWashIconService.list(new LambdaQueryWrapper<BasicsdatumWashIcon>().and(it -> it
                   .in(BasicsdatumWashIcon::getName, washIconCodeList).or()
                   .in(BasicsdatumWashIcon::getCode, washIconCodeList)));
           /*获取字典值*/
           List<BasicBaseDict> dictInfoList = ccmFeignService.getDictInfoToList("wxts");
           String maxValue = StrUtil.replace(
                   dictInfoList.stream().max(Comparator.comparing(BasicBaseDict::getValue)).map(BasicBaseDict::getValue).orElse(""),
                   "WX", "");
           // 最长匹配原则
           dictInfoList.sort(Comparator.comparing(BasicBaseDict::getNameLength).reversed());
           Map<String,BasicBaseDict> newDictInfoMap = new HashMap<>();
           AtomicInteger up = new AtomicInteger(0);
           for (BasicsdatumLavationReminderExcelDto basicsdatumLavationReminderExcelDto : list) {
               StrJoiner reminderNameJoiner = StrJoiner.of("\n");
               StrJoiner reminderCodeJoiner = StrJoiner.of(COMMA);
               if(StringUtils.isNotBlank(basicsdatumLavationReminderExcelDto.getReminderName())){
                   List<String> reminderList = StrUtil.split(basicsdatumLavationReminderExcelDto.getReminderName(), "\n");
                   reminderList.forEach(reminderName-> {
                       Optional<BasicBaseDict> dictOpt = dictInfoList.stream().filter(it -> reminderName.equals(it.getName())).findFirst();
                       if (dictOpt.isPresent()) {
                           reminderNameJoiner.append(reminderName);
                           reminderCodeJoiner.append(dictOpt.get().getValue());
                       }else if (NumberUtil.isNumber(maxValue)) {
                           reminderNameJoiner.append(reminderName);
                           String newCodeStr;
                           if (newDictInfoMap.containsKey(reminderName)) {
                               newCodeStr = newDictInfoMap.get(reminderName).getValue();
                           }else {
                               int newCode = Integer.parseInt(maxValue) + up.incrementAndGet();
                               newCodeStr = "WX" + MoreLanguageProperties.calculateZeroFill(newCode, 10) + newCode;
                               BasicBaseDict baseDict = BeanUtil.copyProperties(dictInfoList.stream().findFirst().get(),BasicBaseDict.class);
                               baseDict.setValue(newCodeStr);
                               baseDict.setSort(BigDecimal.valueOf(newCode));
                               baseDict.setName(reminderName);
                               newDictInfoMap.put(reminderName,baseDict);
                           }
                           reminderCodeJoiner.append(newCodeStr);
                       } else {
                           throw new OtherException("wxts字典中缺少"+ reminderName + "的字典项");
                       }
                   });
               }
               basicsdatumLavationReminderExcelDto.setReminderCode(reminderCodeJoiner.toString());
               basicsdatumLavationReminderExcelDto.setReminderName(reminderNameJoiner.toString());

               String washIconCode = basicsdatumLavationReminderExcelDto.getWashIconCode();
               BasicsdatumWashIcon basicsdatumWashIcon = washIconList.stream().filter(it -> washIconCode.equals(it.getName())).findFirst().orElse(
                       washIconList.stream().filter(it -> washIconCode.equals(it.getCode())).findFirst().orElseThrow(() -> new OtherException("不存在" + washIconCode + "洗标"))
               );
               basicsdatumLavationReminderExcelDto.setWashIconCode(basicsdatumWashIcon.getCode());
               basicsdatumLavationReminderExcelDto.setUrl(basicsdatumWashIcon.getUrl());
           }
           ccmService.batchInsert(new ArrayList<>(newDictInfoMap.values()));

           List<BasicsdatumLavationReminder> basicsdatumLavationReminderList = BeanUtil.copyToList(list, BasicsdatumLavationReminder.class);
           for (BasicsdatumLavationReminder basicsdatumLavationReminder : basicsdatumLavationReminderList) {
               basicsdatumLavationReminder.setStatus("0");
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
            List<BasicsdatumLavationReminderExcelDto> list = BeanUtil.copyToList( baseMapper.getLavationReminderList(queryWrapper), BasicsdatumLavationReminderExcelDto.class);
            list.forEach(it-> StrUtil.replace(it.getReminderName(), ",","\n"));
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
