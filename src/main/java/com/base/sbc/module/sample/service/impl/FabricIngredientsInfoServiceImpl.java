/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.client.amc.enums.DataPermissionsBusinessTypeEnum;
import com.base.sbc.client.amc.service.AmcFeignService;
import com.base.sbc.client.amc.service.DataPermissionsService;
import com.base.sbc.client.amc.vo.FieldDataPermissionVO;
import com.base.sbc.client.message.utils.MessageUtils;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.QueryDto;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.common.dto.RemoveDto;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.sample.dto.AddRevampFabricIngredientsInfoDto;
import com.base.sbc.module.sample.dto.QueryFabricIngredientsInfoDto;
import com.base.sbc.module.sample.entity.FabricIngredientsInfo;
import com.base.sbc.module.sample.mapper.FabricIngredientsInfoMapper;
import com.base.sbc.module.sample.service.FabricIngredientsInfoService;
import com.base.sbc.module.sample.vo.FabricIngredientsInfoVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * 类描述：调样-辅料信息 service类
 * @address com.base.sbc.module.sample.service.FabricIngredientsInfoService
 * @author mengfanjiang
 * @email XX.com
 * @date 创建时间：2023-7-14 17:32:38
 * @version 1.0
 */
@Service
public class FabricIngredientsInfoServiceImpl extends BaseServiceImpl<FabricIngredientsInfoMapper, FabricIngredientsInfo> implements FabricIngredientsInfoService {

        @Autowired
        private BaseController baseController;
        @Autowired
        private DataPermissionsService dataPermissionsService;
        @Autowired
        private MessageUtils messageUtils;

/** 自定义方法区 不替换的区域【other_start】 **/

        /**
        * 调样-辅料信息分页查询
        *
        * @param queryFabricIngredientsInfoDto
        * @return
        */
        @Override
        public PageInfo<FabricIngredientsInfoVo> getFabricIngredientsInfoList(QueryFabricIngredientsInfoDto queryFabricIngredientsInfoDto) {

            /*分页*/
            PageHelper.startPage(queryFabricIngredientsInfoDto);
            QueryWrapper<FabricIngredientsInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("company_code", baseController.getUserCompany());
            queryWrapper.eq(StringUtils.isNotBlank(queryFabricIngredientsInfoDto.getCategoryId()),"category_id",queryFabricIngredientsInfoDto.getCategoryId());
            queryWrapper.eq(StringUtils.isNotBlank(queryFabricIngredientsInfoDto.getCategoryName()),"category_name",queryFabricIngredientsInfoDto.getCategoryName());
            queryWrapper.eq(StringUtils.isNotBlank(queryFabricIngredientsInfoDto.getDevTypeName()),"dev_type_name",queryFabricIngredientsInfoDto.getDevTypeName());

            if(StringUtils.isNotBlank(queryFabricIngredientsInfoDto.getOriginate())){
                if("0".equals(queryFabricIngredientsInfoDto.getOriginate())){
                    queryWrapper.eq("create_id", baseController.getUserId());
                }
            }
            dataPermissionsService.getDataPermissionsForQw(queryWrapper, DataPermissionsBusinessTypeEnum.FabricInformation.getK(),"",new String[]{"category_id"},true);
            /*查询调样-辅料信息数据*/
            List<FabricIngredientsInfo> fabricIngredientsInfoList = baseMapper.selectList(queryWrapper);
            PageInfo<FabricIngredientsInfo> pageInfo = new PageInfo<>(fabricIngredientsInfoList);
            /*转换vo*/
            List<FabricIngredientsInfoVo> list = BeanUtil.copyToList(fabricIngredientsInfoList, FabricIngredientsInfoVo.class);
            PageInfo<FabricIngredientsInfoVo> pageInfo1 = new PageInfo<>();
            pageInfo1.setList(list);
            pageInfo1.setTotal(pageInfo.getTotal());
            pageInfo1.setPageNum(pageInfo.getPageNum());
            pageInfo1.setPageSize(pageInfo.getPageSize());
            return pageInfo1;
        }





        /**
        * 方法描述：新增修改调样-辅料信息
        *
        * @param addRevampFabricIngredientsInfoDto 调样-辅料信息Dto类
        * @return boolean
        */
        @Override
        public Boolean addRevampFabricIngredientsInfo(AddRevampFabricIngredientsInfoDto addRevampFabricIngredientsInfoDto) {
                FabricIngredientsInfo fabricIngredientsInfo = new FabricIngredientsInfo();
            if (StringUtils.isEmpty(addRevampFabricIngredientsInfoDto.getId())) {
                QueryWrapper<FabricIngredientsInfo> queryWrapper=new QueryWrapper<>();
                /*新增*/
                BeanUtils.copyProperties(addRevampFabricIngredientsInfoDto, fabricIngredientsInfo);
                fabricIngredientsInfo.setAtactiformStylistUserId(baseController.getUserId());
                fabricIngredientsInfo.setAtactiformStylist(baseController.getUser().getName());
                fabricIngredientsInfo.setCompanyCode(baseController.getUserCompany());
                fabricIngredientsInfo.insertInit();
                this.save(fabricIngredientsInfo);
                FabricIngredientsInfo f = this.getById(fabricIngredientsInfo.getId());
                this.saveOperaLog("新增","辅料调样单",null,f.getCodeName(),fabricIngredientsInfo,null);
           } else {
                /*修改*/
                //fabricIngredientsInfo = baseMapper.selectById(addRevampFabricIngredientsInfoDto.getId());
                //if (ObjectUtils.isEmpty(fabricIngredientsInfo)) {
                //throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
                //}
                //BeanUtils.copyProperties(addRevampFabricIngredientsInfoDto, fabricIngredientsInfo);
                //fabricIngredientsInfo.updateInit();
                this.updateById(addRevampFabricIngredientsInfoDto,"辅料调样单",null,addRevampFabricIngredientsInfoDto.getCodeName());
                }
                /*发送辅料消息给面辅料专员*/
                messageUtils.atactiformSendMessage("ingredients",addRevampFabricIngredientsInfoDto.getSubmitFlag(),baseController.getUser());
                return true;
         }


         /**
         * 方法描述：删除调样-辅料信息
         *

         * @return boolean
         */
         @Override
         public Boolean delFabricIngredientsInfo(RemoveDto removeDto) {
         //List<String> ids = StringUtils.convertList(id);
           /*批量删除*/
          this.removeByIds(removeDto);
           return true;
         }


         /**
         * 方法描述：启用停止
         *
         * @param startStopDto 启用停止Dto类
         * @return boolean
         */
          @Override
          public Boolean startStopFabricIngredientsInfo(StartStopDto startStopDto) {
            UpdateWrapper<FabricIngredientsInfo> updateWrapper = new UpdateWrapper<>();
            updateWrapper.in("id",StringUtils.convertList(startStopDto.getIds()));
            updateWrapper.set("status", startStopDto.getStatus());
            /*修改状态*/
             return baseMapper.update(null, updateWrapper) > 0;
          }

      /** 自定义方法区 不替换的区域【other_end】 **/

}
