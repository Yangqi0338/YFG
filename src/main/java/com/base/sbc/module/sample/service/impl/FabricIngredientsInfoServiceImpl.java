/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.client.amc.enums.DataPermissionsBusinessTypeEnum;
import com.base.sbc.client.amc.service.DataPermissionsService;
import com.base.sbc.client.message.utils.MessageUtils;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.ureport.minio.MinioUtils;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.config.utils.ExcelUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.common.dto.RemoveDto;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.sample.dto.AddRevampFabricIngredientsInfoDto;
import com.base.sbc.module.sample.dto.FabricIngredientsInfoExcelDto;
import com.base.sbc.module.sample.dto.QueryFabricIngredientsInfoDto;
import com.base.sbc.module.sample.entity.FabricIngredientsInfo;
import com.base.sbc.module.sample.entity.FabricIngredientsSpecification;
import com.base.sbc.module.sample.mapper.FabricIngredientsInfoMapper;
import com.base.sbc.module.sample.service.FabricIngredientsInfoService;
import com.base.sbc.module.sample.service.FabricIngredientsSpecificationService;
import com.base.sbc.module.sample.vo.FabricIngredientsInfoVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Autowired
    private FabricIngredientsSpecificationService fabricIngredientsSpecificationService;
    @Autowired
    private MinioUtils minioUtils;

/** 自定义方法区 不替换的区域【other_start】 **/

    /**
     * 调样-辅料信息分页查询
     *
     * @param queryFabricIngredientsInfoDto
     * @return
     */
    @Override
    public PageInfo getFabricIngredientsInfoList(QueryFabricIngredientsInfoDto queryFabricIngredientsInfoDto) {
        queryFabricIngredientsInfoDto.setOrderBy("create_date desc");
        /*分页*/
        BaseQueryWrapper<FabricIngredientsInfo> queryWrapper = new BaseQueryWrapper<>();
        queryWrapper.eq("tfii.company_code", baseController.getUserCompany());
        queryWrapper.eq("tfii.del_flag", BaseGlobal.NO);

        queryWrapper.eq(StringUtils.isNotBlank(queryFabricIngredientsInfoDto.getCategoryId()),"tfii.category_id",queryFabricIngredientsInfoDto.getCategoryId());
        queryWrapper.eq(StringUtils.isNotBlank(queryFabricIngredientsInfoDto.getCategoryName()),"tfii.category_name",queryFabricIngredientsInfoDto.getCategoryName());
        queryWrapper.eq(StringUtils.isNotBlank(queryFabricIngredientsInfoDto.getDevTypeName()),"tfii.dev_type_name",queryFabricIngredientsInfoDto.getDevTypeName());
        queryWrapper.like(StringUtils.isNotBlank(queryFabricIngredientsInfoDto.getManufacturerNumber()),"tfii.manufacturer_number",queryFabricIngredientsInfoDto.getManufacturerNumber());
        queryWrapper.like(StringUtils.isNotBlank(queryFabricIngredientsInfoDto.getManufacturer()),"tfii.manufacturer",queryFabricIngredientsInfoDto.getManufacturer());
        queryWrapper.in(StringUtils.isNotBlank(queryFabricIngredientsInfoDto.getAtactiformStylist()),"tfii.atactiform_stylist_user_id",StringUtils.convertList(queryFabricIngredientsInfoDto.getAtactiformStylist()));
        queryWrapper.like(StringUtils.isNotBlank(queryFabricIngredientsInfoDto.getCreateName()),"tfii.create_name",queryFabricIngredientsInfoDto.getCreateName());
        queryWrapper.eq(StringUtils.isNotBlank(queryFabricIngredientsInfoDto.getCompletionStatus()),"tfii.completion_status",queryFabricIngredientsInfoDto.getCompletionStatus());
        queryWrapper.between("tfii.create_date",StringUtils.split(queryFabricIngredientsInfoDto.getCreateDate(),","));
        if (!StringUtils.isEmpty(queryFabricIngredientsInfoDto.getPracticalAtactiformDate())){
            queryWrapper.between("tfii.practical_atactiform_date",queryFabricIngredientsInfoDto.getPracticalAtactiformDate().split(","));
        }
        queryWrapper.groupBy("tfii.id");
        dataPermissionsService.getDataPermissionsForQw(queryWrapper, DataPermissionsBusinessTypeEnum.FabricInformation.getK(),"tfii.",new String[]{"category_id:category_id","create_dept_id:create_dept_id"},true);
        /*查询调样-辅料信息数据*/
        Page<FabricIngredientsInfoVo> objects = PageHelper.startPage(queryFabricIngredientsInfoDto);
        baseMapper.getSelectList(queryWrapper);
        PageInfo<FabricIngredientsInfoVo> copy = CopyUtil.copy(objects.toPageInfo(), FabricIngredientsInfoVo.class);
        List<FabricIngredientsInfoVo> list = copy.getList();
        /*导出时不查询*/
        if (StringUtils.isBlank(queryFabricIngredientsInfoDto.getDeriveFlag())) {
            if(CollUtil.isNotEmpty(list)){
                List<String> stringList = list.stream().map(FabricIngredientsInfoVo::getId).collect(Collectors.toList());
                List<FabricIngredientsSpecification> ingredientsSpecificationList = fabricIngredientsSpecificationService.listByField("ingredients_info_id", stringList);
                Map<String, List<FabricIngredientsSpecification>> map = ingredientsSpecificationList.stream().collect(Collectors.groupingBy(p -> p.getIngredientsInfoId()));
                for (FabricIngredientsInfoVo fabricIngredientsInfoVo : list) {
                    List<FabricIngredientsSpecification> list1 = map.get(fabricIngredientsInfoVo.getId());
                    fabricIngredientsInfoVo.setIngredientsSpecificationList(list1);
                }
            }
        }else {
            return copy;
        }

        /*转换vo*/
        minioUtils.setObjectUrlToList(list,"imageUrl");
        return copy;
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
        List<FabricIngredientsSpecification>  list =    addRevampFabricIngredientsInfoDto.getIngredientsSpecificationList();
        if (StringUtils.isEmpty(addRevampFabricIngredientsInfoDto.getId())) {
            /*新增*/
            BeanUtils.copyProperties(addRevampFabricIngredientsInfoDto, fabricIngredientsInfo);
      /*      fabricIngredientsInfo.setAtactiformStylistUserId(baseController.getUserId());
            fabricIngredientsInfo.setAtactiformStylist(baseController.getUser().getName());*/
            fabricIngredientsInfo.setCompanyCode(baseController.getUserCompany());
            fabricIngredientsInfo.insertInit();
            CommonUtils.removeQuery(fabricIngredientsInfo,"imageUrl");
            fabricIngredientsInfo.setCreateDeptId(getVirtualDetpIds());
            this.save(fabricIngredientsInfo);
            if(CollUtil.isNotEmpty(list)){
                list.forEach(l -> {
                    l.setId(null);
                    l.setIngredientsInfoId(fabricIngredientsInfo.getId());
                });
                fabricIngredientsSpecificationService.saveBatch(list);
            }
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
            /*先删除之前得数据*/
            QueryWrapper<FabricIngredientsSpecification> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("ingredients_info_id", addRevampFabricIngredientsInfoDto.getId());
            fabricIngredientsSpecificationService.remove(queryWrapper);
            if(CollUtil.isNotEmpty(list)){
                list.forEach(l -> {
                    l.setId(null);
                    l.setIngredientsInfoId(addRevampFabricIngredientsInfoDto.getId());
                });
                fabricIngredientsSpecificationService.saveBatch(list);
            }
            CommonUtils.removeQuery(addRevampFabricIngredientsInfoDto,"imageUrl");
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

    /**
     * 辅料导出
     *
     * @param response
     * @param queryFabricIngredientsInfoDto
     */
    @Override
    public void fabricIngredientsInfoDeriveExcel(HttpServletResponse response, QueryFabricIngredientsInfoDto queryFabricIngredientsInfoDto) throws IOException {
        queryFabricIngredientsInfoDto.setDeriveFlag(BaseGlobal.NO);
        List<FabricIngredientsInfoVo> ingredientsInfoVoList = getFabricIngredientsInfoList(queryFabricIngredientsInfoDto).getList();
        List<FabricIngredientsInfoExcelDto> list = BeanUtil.copyToList(ingredientsInfoVoList, FabricIngredientsInfoExcelDto.class);
        if(StrUtil.equals(queryFabricIngredientsInfoDto.getImgFlag(),BaseGlobal.YES)){
            minioUtils.setObjectUrlToList(list,"imageUrl");
        }
        /*使用线程导出*/
        ExcelUtils.executorExportExcel(list, FabricIngredientsInfoExcelDto.class,"辅料调样单",queryFabricIngredientsInfoDto.getImgFlag(),2000,response,"imageUrl");
//        ExcelUtils.exportExcel(ingredientsInfoVoList,  FabricIngredientsInfoExcelDto.class, "辅料调样单.xlsx",new ExportParams("辅料调样单", "辅料调样单", ExcelType.HSSF) ,response);


    }

    /**
     * 复制辅料
     *
     * @param id
     * @return
     */
    @Override
    public Boolean copyIngredients(String id) {

        FabricIngredientsInfo fabricIngredientsInfo = baseMapper.selectById(id);
        fabricIngredientsInfo.setId(null);
        fabricIngredientsInfo.setCode(null);
        fabricIngredientsInfo.insertInit();
        /*先保存辅料单*/
        baseMapper.insert(fabricIngredientsInfo);
        /*辅料规格信息*/
        List<FabricIngredientsSpecification> ingredientsSpecificationList = fabricIngredientsSpecificationService.listByField("ingredients_info_id", StringUtils.convertList(id));
        ingredientsSpecificationList.forEach(i -> {
            i.setIngredientsInfoId(fabricIngredientsInfo.getId());
            i.setId(null);
        });
        /*保存复制辅料*/
        fabricIngredientsSpecificationService.saveBatch(ingredientsSpecificationList);
        this.saveOperaLog("复制","辅料调样单",null,fabricIngredientsInfo.getCodeName(),fabricIngredientsInfo,null);
        return true;
    }

    /** 自定义方法区 不替换的区域【other_end】 **/

}
