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
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.ExcelUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.AddRevampBasicsdatumIngredientDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumIngredientDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumIngredientExcelDto;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumIngredient;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumIngredientMapper;
import com.base.sbc.module.basicsdatum.service.BasicsdatumIngredientService;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumIngredientVo;
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
import java.util.List;

/**
 * 类描述：基础资料-材料成分 service类
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumIngredientService
 * @author mengfanjiang
 * @email 2915350015@qq.com
 * @date 创建时间：2023-5-19 19:15:00
 * @version 1.0
 */
@Service
public class BasicsdatumIngredientServiceImpl extends BaseServiceImpl<BasicsdatumIngredientMapper, BasicsdatumIngredient> implements BasicsdatumIngredientService {

        @Autowired
        private BaseController baseController;

/** 自定义方法区 不替换的区域【other_start】 **/

        /**
        * 基础资料-材料成分分页查询
        *
        * @param queryDto
        * @return
        */
        @Override
        public PageInfo<BasicsdatumIngredientVo> getBasicsdatumIngredientList(BasicsdatumIngredientDto queryDto) {
            /*分页*/
            PageHelper.startPage(queryDto);
            BaseQueryWrapper<BasicsdatumIngredient> queryWrapper = new BaseQueryWrapper<>();
            queryWrapper.eq("company_code", baseController.getUserCompany());
            queryWrapper.notEmptyLike("material",queryDto.getMaterial());
            queryWrapper.notEmptyLike("code",queryDto.getCode());
            queryWrapper.notEmptyLike("ingredient",queryDto.getIngredient());
            queryWrapper.eq(StringUtils.isNotEmpty(queryDto.getCreateName()),"create_name",queryDto.getCreateName());
            queryWrapper.eq(StringUtils.isNotEmpty(queryDto.getStatus()),"status",queryDto.getStatus());
            queryWrapper.between("create_date",queryDto.getCreateDate());
            queryWrapper.orderByDesc("create_date");

            /*查询基础资料-材料成分数据*/
            List<BasicsdatumIngredient> basicsdatumIngredientList = baseMapper.selectList(queryWrapper);
            PageInfo<BasicsdatumIngredient> pageInfo = new PageInfo<>(basicsdatumIngredientList);
            /*转换vo*/
            List<BasicsdatumIngredientVo> list = BeanUtil.copyToList(basicsdatumIngredientList, BasicsdatumIngredientVo.class);
            PageInfo<BasicsdatumIngredientVo> pageInfo1 = new PageInfo<>();
            pageInfo1.setList(list);
            pageInfo1.setTotal(pageInfo.getTotal());
            pageInfo1.setPageNum(pageInfo.getPageNum());
            pageInfo1.setPageSize(pageInfo.getPageSize());
            return pageInfo1;
        }



       /**
       * 基础资料-材料成分导入
       *
       * @param file
       * @return
       */
       @Override
       public Boolean basicsdatumIngredientImportExcel(MultipartFile file) throws Exception {
            ImportParams params = new ImportParams();
            params.setNeedSave(false);
            List<BasicsdatumIngredientExcelDto> list = ExcelImportUtil.importExcel(file.getInputStream(), BasicsdatumIngredientExcelDto.class, params);
            List<BasicsdatumIngredient> basicsdatumIngredientList = BeanUtil.copyToList(list, BasicsdatumIngredient.class);
           for (BasicsdatumIngredient basicsdatumIngredient : basicsdatumIngredientList) {
               if(StringUtils.isNotBlank(basicsdatumIngredient.getCode())) {
                   QueryWrapper<BasicsdatumIngredient> queryWrapper = new QueryWrapper<>();
                   queryWrapper.eq("code", basicsdatumIngredient.getCode());
                   this.saveOrUpdate(basicsdatumIngredient, queryWrapper);
               }
           }
            return true;
       }

        /**
        * 基础资料-材料成分导出
        *
        * @param
        * @return
        */
        @Override
        public void basicsdatumIngredientDeriveExcel(HttpServletResponse response) throws Exception {
        QueryWrapper<BasicsdatumIngredient> queryWrapper=new QueryWrapper<>();
        List<BasicsdatumIngredientExcelDto> list = BeanUtil.copyToList( baseMapper.selectList(queryWrapper), BasicsdatumIngredientExcelDto.class);
        ExcelUtils.exportExcel(list,  BasicsdatumIngredientExcelDto.class, "基础资料-材料成分.xlsx",new ExportParams() ,response);
        }



        /**
        * 方法描述：新增修改基础资料-材料成分
        *
        * @param addRevampBasicsdatumIngredientDto 基础资料-材料成分Dto类
        * @return boolean
        */
        @Override
        public Boolean addRevampBasicsdatumIngredient(AddRevampBasicsdatumIngredientDto addRevampBasicsdatumIngredientDto) {
            BasicsdatumIngredient basicsdatumIngredient = new BasicsdatumIngredient();
            QueryWrapper<BasicsdatumIngredient> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("code",addRevampBasicsdatumIngredientDto.getCode());
            /*查询数据是否存在*/
            List<BasicsdatumIngredient> list = baseMapper.selectList(queryWrapper);
            if (StringUtils.isEmpty(addRevampBasicsdatumIngredientDto.getId())) {
                /*新增*/
                if(!CollectionUtils.isEmpty(list)){
                    throw new OtherException(BaseErrorEnum.ERR_INSERT_DATA_REPEAT);
                }
                BeanUtils.copyProperties(addRevampBasicsdatumIngredientDto, basicsdatumIngredient);
                basicsdatumIngredient.setCompanyCode(baseController.getUserCompany());
                basicsdatumIngredient.insertInit();
                baseMapper.insert(basicsdatumIngredient);
           } else {
                /*修改*/
                basicsdatumIngredient = baseMapper.selectById(addRevampBasicsdatumIngredientDto.getId());
                if (ObjectUtils.isEmpty(basicsdatumIngredient)) {
                throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
                }
                if(!basicsdatumIngredient.getCode().equals(addRevampBasicsdatumIngredientDto.getCode()) && !CollectionUtils.isEmpty(list)){
                    throw new OtherException(BaseErrorEnum.ERR_INSERT_DATA_REPEAT);
                }
                BeanUtils.copyProperties(addRevampBasicsdatumIngredientDto, basicsdatumIngredient);
                basicsdatumIngredient.updateInit();
                baseMapper.updateById(basicsdatumIngredient);
                }
                return true;
         }


         /**
         * 方法描述：删除基础资料-材料成分
         *
         * @param id （多个用，）
         * @return boolean
         */
         @Override
         public Boolean delBasicsdatumIngredient(String id) {
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
          public Boolean startStopBasicsdatumIngredient(StartStopDto startStopDto) {
            UpdateWrapper<BasicsdatumIngredient> updateWrapper = new UpdateWrapper<>();
            updateWrapper.in("id",StringUtils.convertList(startStopDto.getIds()));
            updateWrapper.set("status", startStopDto.getStatus());
            /*修改状态*/
             return baseMapper.update(null, updateWrapper) > 0;
          }

      /** 自定义方法区 不替换的区域【other_end】 **/

}
