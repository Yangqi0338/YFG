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
import com.base.sbc.module.basicsdatum.dto.*;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterialsIngredient;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumMaterialsIngredientMapper;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialsIngredientService;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumMaterialsIngredientVo;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 类描述：基础资料-材料成分 service类
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialsIngredientService
 * @author mengfanjiang
 * @email 2915350015@qq.com
 * @date 创建时间：2023-5-19 19:15:00
 * @version 1.0
 */
@Service
public class BasicsdatumMaterialsIngredientBaseServiceImpl extends BaseServiceImpl<BasicsdatumMaterialsIngredientMapper, BasicsdatumMaterialsIngredient> implements BasicsdatumMaterialsIngredientService {

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
        public PageInfo<BasicsdatumMaterialsIngredientVo> getBasicsdatumMaterialsIngredientList(BasicsdatumMaterialsIngredientDto queryDto) {
            /*分页*/
            PageHelper.startPage(queryDto);
            BaseQueryWrapper<BasicsdatumMaterialsIngredient> queryWrapper = new BaseQueryWrapper<>();
            queryWrapper.eq("company_code", baseController.getUserCompany());
            queryWrapper.notEmptyLike("material",queryDto.getMaterial());
            queryWrapper.eq(StringUtils.isNotEmpty(queryDto.getCreateName()),"create_name",queryDto.getCreateName());
            queryWrapper.between("create_date",queryDto.getCreateDate());
            queryWrapper.orderByDesc("create_date");

            /*查询基础资料-材料成分数据*/
            List<BasicsdatumMaterialsIngredient> basicsdatumMaterialsIngredientList = baseMapper.selectList(queryWrapper);
            PageInfo<BasicsdatumMaterialsIngredient> pageInfo = new PageInfo<>(basicsdatumMaterialsIngredientList);
            /*转换vo*/
            List<BasicsdatumMaterialsIngredientVo> list = BeanUtil.copyToList(basicsdatumMaterialsIngredientList, BasicsdatumMaterialsIngredientVo.class);
            PageInfo<BasicsdatumMaterialsIngredientVo> pageInfo1 = new PageInfo<>();
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
       public Boolean basicsdatumMaterialsIngredientImportExcel(MultipartFile file) throws Exception {
            ImportParams params = new ImportParams();
            params.setNeedSave(false);
            List<BasicsdatumMaterialsIngredientExcelDto> list = ExcelImportUtil.importExcel(file.getInputStream(), BasicsdatumMaterialsIngredientExcelDto.class, params);
            List<BasicsdatumMaterialsIngredient> basicsdatumMaterialsIngredientList = BeanUtil.copyToList(list, BasicsdatumMaterialsIngredient.class);
           for (BasicsdatumMaterialsIngredient basicsdatumMaterialsIngredient : basicsdatumMaterialsIngredientList) {
               QueryWrapper<BasicsdatumMaterialsIngredient> queryWrapper =new QueryWrapper<>();
               queryWrapper.eq("ingredient",basicsdatumMaterialsIngredient.getIngredient());
               this.saveOrUpdate(basicsdatumMaterialsIngredient,queryWrapper);
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
        public void basicsdatumMaterialsIngredientDeriveExcel(HttpServletResponse response) throws Exception {
        QueryWrapper<BasicsdatumMaterialsIngredient> queryWrapper=new QueryWrapper<>();
        List<BasicsdatumMaterialsIngredientExcelDto> list = BeanUtil.copyToList( baseMapper.selectList(queryWrapper), BasicsdatumMaterialsIngredientExcelDto.class);
        ExcelUtils.exportExcel(list,  BasicsdatumMaterialsIngredientExcelDto.class, "基础资料-材料成分.xlsx",new ExportParams() ,response);
        }



        /**
        * 方法描述：新增修改基础资料-材料成分
        *
        * @param addRevampBasicsdatumMaterialsIngredientDto 基础资料-材料成分Dto类
        * @return boolean
        */
        @Override
        public Boolean addRevampBasicsdatumMaterialsIngredient(AddRevampBasicsdatumMaterialsIngredientDto addRevampBasicsdatumMaterialsIngredientDto) {
                BasicsdatumMaterialsIngredient basicsdatumMaterialsIngredient = new BasicsdatumMaterialsIngredient();
            if (StringUtils.isEmpty(addRevampBasicsdatumMaterialsIngredientDto.getId())) {
                QueryWrapper<BasicsdatumMaterialsIngredient> queryWrapper=new QueryWrapper<>();
                /*新增*/
                BeanUtils.copyProperties(addRevampBasicsdatumMaterialsIngredientDto, basicsdatumMaterialsIngredient);
                basicsdatumMaterialsIngredient.setCompanyCode(baseController.getUserCompany());
                basicsdatumMaterialsIngredient.insertInit();
                baseMapper.insert(basicsdatumMaterialsIngredient);
           } else {
                /*修改*/
                basicsdatumMaterialsIngredient = baseMapper.selectById(addRevampBasicsdatumMaterialsIngredientDto.getId());
                if (ObjectUtils.isEmpty(basicsdatumMaterialsIngredient)) {
                throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
                }
                BeanUtils.copyProperties(addRevampBasicsdatumMaterialsIngredientDto, basicsdatumMaterialsIngredient);
                basicsdatumMaterialsIngredient.updateInit();
                baseMapper.updateById(basicsdatumMaterialsIngredient);
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
         public Boolean delBasicsdatumMaterialsIngredient(String id) {
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
          public Boolean startStopBasicsdatumMaterialsIngredient(StartStopDto startStopDto) {
            UpdateWrapper<BasicsdatumMaterialsIngredient> updateWrapper = new UpdateWrapper<>();
            updateWrapper.in("id",StringUtils.convertList(startStopDto.getIds()));
            updateWrapper.set("status", startStopDto.getStatus());
            /*修改状态*/
             return baseMapper.update(null, updateWrapper) > 0;
          }

      /** 自定义方法区 不替换的区域【other_end】 **/

}
