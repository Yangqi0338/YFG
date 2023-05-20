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
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.ExcelUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.AddRevampBasicsdatumPressingPackingDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumPressingPackingExcelDto;
import com.base.sbc.module.basicsdatum.dto.QueryDto;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumPressingPacking;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumPressingPackingMapper;
import com.base.sbc.module.basicsdatum.service.BasicsdatumPressingPackingService;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumPressingPackingVo;
import com.base.sbc.module.common.service.impl.ServicePlusImpl;
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
 * 类描述：基础资料-洗涤图标与温馨提示 service类
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumPressingPackingService
 * @author mengfanjiang
 * @email 2915350015@qq.com
 * @date 创建时间：2023-5-19 19:15:00
 * @version 1.0  
 */
@Service
public class BasicsdatumPressingPackingServiceImpl extends ServicePlusImpl<BasicsdatumPressingPackingMapper, BasicsdatumPressingPacking> implements BasicsdatumPressingPackingService {

        @Autowired
        private BaseController baseController;

/** 自定义方法区 不替换的区域【other_start】 **/

        /**
        * 基础资料-洗涤图标与温馨提示分页查询
        *
        * @param queryDto
        * @return
        */
        @Override
        public PageInfo<BasicsdatumPressingPackingVo> getBasicsdatumPressingPackingList(QueryDto queryDto) {
            /*分页*/
            PageHelper.startPage(queryDto);
            QueryWrapper<BasicsdatumPressingPacking> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("company_code", baseController.getUserCompany());
            /*查询基础资料-洗涤图标与温馨提示数据*/
            List<BasicsdatumPressingPacking> basicsdatumPressingPackingList = baseMapper.selectList(queryWrapper);
            PageInfo<BasicsdatumPressingPacking> pageInfo = new PageInfo<>(basicsdatumPressingPackingList);
            /*转换vo*/
            List<BasicsdatumPressingPackingVo> list = BeanUtil.copyToList(basicsdatumPressingPackingList, BasicsdatumPressingPackingVo.class);
            PageInfo<BasicsdatumPressingPackingVo> pageInfo1 = new PageInfo<>();
            pageInfo1.setList(list);
            pageInfo1.setTotal(pageInfo.getTotal());
            pageInfo1.setPageNum(pageInfo.getPageNum());
            pageInfo1.setPageSize(pageInfo.getPageSize());
            return pageInfo1;
        }



       /**
       * 基础资料-洗涤图标与温馨提示导入
       *
       * @param file
       * @return
       */
       @Override
       public Boolean basicsdatumPressingPackingImportExcel(MultipartFile file) throws Exception {
            ImportParams params = new ImportParams();
            params.setNeedSave(false);
            List<BasicsdatumPressingPackingExcelDto> list = ExcelImportUtil.importExcel(file.getInputStream(), BasicsdatumPressingPackingExcelDto.class, params);
            List<BasicsdatumPressingPacking> basicsdatumPressingPackingList = BeanUtil.copyToList(list, BasicsdatumPressingPacking.class);
            saveBatch( basicsdatumPressingPackingList);
            return true;
       }

        /**
        * 基础资料-洗涤图标与温馨提示导出
        *
        * @param
        * @return
        */
        @Override
        public void basicsdatumPressingPackingDeriveExcel(HttpServletResponse response) throws Exception {
        QueryWrapper<BasicsdatumPressingPacking> queryWrapper=new QueryWrapper<>();
        List<BasicsdatumPressingPackingExcelDto> list = BeanUtil.copyToList( baseMapper.selectList(queryWrapper), BasicsdatumPressingPackingExcelDto.class);
        ExcelUtils.exportExcel(list, "基础资料-洗涤图标与温馨提示", "基础资料-洗涤图标与温馨提示", BasicsdatumPressingPackingExcelDto.class, "基础资料-洗涤图标与温馨提示.xlsx", response);
        }



        /**
        * 方法描述：新增修改基础资料-洗涤图标与温馨提示
        *
        * @param addRevampBasicsdatumPressingPackingDto 基础资料-洗涤图标与温馨提示Dto类
        * @return boolean
        */
        @Override
        public Boolean addRevampBasicsdatumPressingPacking(AddRevampBasicsdatumPressingPackingDto addRevampBasicsdatumPressingPackingDto) {
                BasicsdatumPressingPacking basicsdatumPressingPacking = new BasicsdatumPressingPacking();
            if (StringUtils.isEmpty(addRevampBasicsdatumPressingPackingDto.getId())) {
                QueryWrapper<BasicsdatumPressingPacking> queryWrapper=new QueryWrapper<>();
                /*新增*/
                BeanUtils.copyProperties(addRevampBasicsdatumPressingPackingDto, basicsdatumPressingPacking);
                basicsdatumPressingPacking.setCompanyCode(baseController.getUserCompany());
                basicsdatumPressingPacking.insertInit();
                baseMapper.insert(basicsdatumPressingPacking);
           } else {
                /*修改*/
                basicsdatumPressingPacking = baseMapper.selectById(addRevampBasicsdatumPressingPackingDto.getId());
                if (ObjectUtils.isEmpty(basicsdatumPressingPacking)) {
                throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
                }
                BeanUtils.copyProperties(addRevampBasicsdatumPressingPackingDto, basicsdatumPressingPacking);
                basicsdatumPressingPacking.updateInit();
                baseMapper.updateById(basicsdatumPressingPacking);
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
         public Boolean delBasicsdatumPressingPacking(String id) {
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
          public Boolean startStopBasicsdatumPressingPacking(StartStopDto startStopDto) {
            UpdateWrapper<BasicsdatumPressingPacking> updateWrapper = new UpdateWrapper<>();
            updateWrapper.in("id",StringUtils.convertList(startStopDto.getIds()));
            updateWrapper.set("status", startStopDto.getStatus());
            /*修改状态*/
             return baseMapper.update(null, updateWrapper) > 0;
          }

      /** 自定义方法区 不替换的区域【other_end】 **/

}
