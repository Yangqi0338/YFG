/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.module.common.service.impl.ServicePlusImpl;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumModelTypeMapper;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumModelType;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumModelTypeVo;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumModelType;
import com.base.sbc.module.basicsdatum.service.BasicsdatumModelTypeService;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumModelTypeExcelDto;
import com.base.sbc.module.basicsdatum.dto.AddRevampBasicsdatumModelTypeDto;
import org.springframework.beans.factory.annotation.Autowired;
import com.base.sbc.config.common.base.BaseController;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.PageHelper;
import cn.hutool.core.bean.BeanUtil;
import org.springframework.web.multipart.MultipartFile;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import javax.servlet.http.HttpServletResponse;
import com.base.sbc.config.utils.StringUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.beans.BeanUtils;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.basicsdatum.dto.QueryDto;
import com.base.sbc.config.utils.ExcelUtils;

import java.util.ArrayList;
import java.util.List;

/** 
 * 类描述：基础资料-号型类型 service类
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumModelTypeService
 * @author mengfanjiang
 * @email 2915350015@qq.com
 * @date 创建时间：2023-5-20 9:31:14
 * @version 1.0  
 */
@Service
public class BasicsdatumModelTypeServiceImpl extends ServicePlusImpl<BasicsdatumModelTypeMapper, BasicsdatumModelType> implements BasicsdatumModelTypeService {

        @Autowired
        private BaseController baseController;

/** 自定义方法区 不替换的区域【other_start】 **/

        /**
        * 基础资料-号型类型分页查询
        *
        * @param queryDto
        * @return
        */
        @Override
        public PageInfo<BasicsdatumModelTypeVo> getBasicsdatumModelTypeList(QueryDto queryDto) {
            /*分页*/
            if(queryDto.getPageNum()!=0 && queryDto.getPageSize()!=0){
                PageHelper.startPage(queryDto);
            }

            QueryWrapper<BasicsdatumModelType> queryWrapper = new QueryWrapper<>();
            if(!StringUtils.isEmpty(queryDto.getModelType())){
                queryWrapper.eq("model_type",queryDto.getModelType());
            }
            queryWrapper.eq("company_code", baseController.getUserCompany());
            /*查询基础资料-号型类型数据*/
            List<BasicsdatumModelType> basicsdatumModelTypeList = baseMapper.selectList(queryWrapper);
            PageInfo<BasicsdatumModelType> pageInfo = new PageInfo<>(basicsdatumModelTypeList);
            /*转换vo*/
            List<BasicsdatumModelTypeVo> list = BeanUtil.copyToList(basicsdatumModelTypeList, BasicsdatumModelTypeVo.class);
            PageInfo<BasicsdatumModelTypeVo> pageInfo1 = new PageInfo<>();
            pageInfo1.setList(list);
            pageInfo1.setTotal(pageInfo.getTotal());
            pageInfo1.setPageNum(pageInfo.getPageNum());
            pageInfo1.setPageSize(pageInfo.getPageSize());
            return pageInfo1;
        }



       /**
       * 基础资料-号型类型导入
       *
       * @param file
       * @return
       */
       @Override
       public Boolean basicsdatumModelTypeImportExcel(MultipartFile file) throws Exception {
            ImportParams params = new ImportParams();
            params.setNeedSave(false);
            List<BasicsdatumModelTypeExcelDto> list = ExcelImportUtil.importExcel(file.getInputStream(), BasicsdatumModelTypeExcelDto.class, params);
            List<BasicsdatumModelType> basicsdatumModelTypeList = BeanUtil.copyToList(list, BasicsdatumModelType.class);
            saveOrUpdateBatch( basicsdatumModelTypeList);
            return true;
       }

        /**
        * 基础资料-号型类型导出
        *
        * @param
        * @return
        */
        @Override
        public void basicsdatumModelTypeDeriveExcel(HttpServletResponse response) throws Exception {
        QueryWrapper<BasicsdatumModelType> queryWrapper=new QueryWrapper<>();
        List<BasicsdatumModelTypeExcelDto> list = BeanUtil.copyToList( baseMapper.selectList(queryWrapper), BasicsdatumModelTypeExcelDto.class);
        ExcelUtils.exportExcel(list, "基础资料-号型类型", "基础资料-号型类型", BasicsdatumModelTypeExcelDto.class, "基础资料-号型类型.xlsx", response);
        }



        /**
        * 方法描述：新增修改基础资料-号型类型
        *
        * @param addRevampBasicsdatumModelTypeDto 基础资料-号型类型Dto类
        * @return boolean
        */
        @Override
        public Boolean addRevampBasicsdatumModelType(AddRevampBasicsdatumModelTypeDto addRevampBasicsdatumModelTypeDto) {
                BasicsdatumModelType basicsdatumModelType = new BasicsdatumModelType();
            if (StringUtils.isEmpty(addRevampBasicsdatumModelTypeDto.getId())) {
                QueryWrapper<BasicsdatumModelType> queryWrapper=new QueryWrapper<>();
                /*新增*/
                BeanUtils.copyProperties(addRevampBasicsdatumModelTypeDto, basicsdatumModelType);
                basicsdatumModelType.setCompanyCode(baseController.getUserCompany());
                basicsdatumModelType.insertInit();
                baseMapper.insert(basicsdatumModelType);
           } else {
                /*修改*/
                basicsdatumModelType = baseMapper.selectById(addRevampBasicsdatumModelTypeDto.getId());
                if (ObjectUtils.isEmpty(basicsdatumModelType)) {
                throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
                }
                BeanUtils.copyProperties(addRevampBasicsdatumModelTypeDto, basicsdatumModelType);
                basicsdatumModelType.updateInit();
                baseMapper.updateById(basicsdatumModelType);
                }
                return true;
         }


         /**
         * 方法描述：删除基础资料-号型类型
         *
         * @param id （多个用，）
         * @return boolean
         */
         @Override
         public Boolean delBasicsdatumModelType(String id) {
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
          public Boolean startStopBasicsdatumModelType(StartStopDto startStopDto) {
            UpdateWrapper<BasicsdatumModelType> updateWrapper = new UpdateWrapper<>();
            updateWrapper.in("id",StringUtils.convertList(startStopDto.getIds()));
            updateWrapper.set("status", startStopDto.getStatus());
            /*修改状态*/
             return baseMapper.update(null, updateWrapper) > 0;
          }

      /** 自定义方法区 不替换的区域【other_end】 **/

}
