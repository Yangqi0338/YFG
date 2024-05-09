/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.service.impl;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.base.sbc.client.amc.enums.DataPermissionsBusinessTypeEnum;
import com.base.sbc.client.amc.service.DataPermissionsService;
import com.base.sbc.client.message.utils.MessageUtils;
import com.base.sbc.config.annotation.DuplicationCheck;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.ureport.minio.MinioUtils;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.config.utils.ExcelUtils;
import com.base.sbc.config.utils.FilesUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.common.dto.RemoveDto;
import com.base.sbc.module.common.service.AttachmentService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.common.utils.AttachmentTypeConstant;
import com.base.sbc.module.common.vo.AttachmentVo;
import com.base.sbc.module.sample.dto.*;
import com.base.sbc.module.sample.entity.FabricBasicInformation;
import com.base.sbc.module.sample.mapper.FabricBasicInformationMapper;
import com.base.sbc.module.sample.service.FabricBasicInformationService;
import com.base.sbc.module.sample.vo.FabricInformationVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 类描述：面料基本信息 service类
 * @address com.base.sbc.module.sample.service.FabricBasicInformationService
 * @author lxl
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-4-19 18:23:26
 * @version 1.0
 */
@Service
public class FabricBasicInformationServiceImpl extends BaseServiceImpl<FabricBasicInformationMapper, FabricBasicInformation> implements FabricBasicInformationService {

    @Autowired
    private BaseController baseController;

    @Autowired
    private DataPermissionsService dataPermissionsService;
    @Autowired
    private MessageUtils messageUtils;

    @Autowired
    private MinioUtils minioUtils;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private FilesUtils filesUtils;



    @Override
    public PageInfo getFabricInformationList(QueryFabricInformationDto queryFabricInformationDto) {
        queryFabricInformationDto.setOrderBy("create_date desc");
        BaseQueryWrapper queryWrapper = new BaseQueryWrapper<>();
        queryWrapper.eq("company_code", baseController.getUserCompany());
        queryWrapper.eq(StringUtils.isNotBlank(queryFabricInformationDto.getYearName()), "year_name", queryFabricInformationDto.getYearName());
        queryWrapper.eq(StringUtils.isNotBlank(queryFabricInformationDto.getSeasonName()), "season", queryFabricInformationDto.getSeasonName());
        queryWrapper.eq(StringUtils.isNotBlank(queryFabricInformationDto.getBrandName()), "brand", queryFabricInformationDto.getBrandName());
        queryWrapper.eq(StringUtils.isNotBlank(queryFabricInformationDto.getIsNewFabric()), "is_new_fabric", queryFabricInformationDto.getIsNewFabric());
        queryWrapper.like(StringUtils.isNotBlank(queryFabricInformationDto.getSupplierMaterialCode()), "supplier_material_code", queryFabricInformationDto.getSupplierMaterialCode());
        queryWrapper.like(StringUtils.isNotBlank(queryFabricInformationDto.getSupplierName()), "supplier_name", queryFabricInformationDto.getSupplierName());
        queryWrapper.like(StringUtils.isNotBlank(queryFabricInformationDto.getSupplierColor()), "supplier_color", queryFabricInformationDto.getSupplierColor());
        queryWrapper.like(StringUtils.isNotBlank(queryFabricInformationDto.getAtactiformStylist()), "atactiform_stylist_user_id", queryFabricInformationDto.getAtactiformStylist());
        if (StringUtils.isNotBlank(queryFabricInformationDto.getSearch())) {
            queryWrapper.andLike(queryFabricInformationDto.getSearch(), "supplier_material_code", "supplier_name");
          /*  queryWrapper.apply("and ( supplier_material_code like concat('%','" + queryFabricInformationDto.getSearch() + "','%') " +
                    " or  supplier_name like concat('%','" + queryFabricInformationDto.getSearch() + "','%')");*/
        }
        dataPermissionsService.getDataPermissionsForQw(queryWrapper, DataPermissionsBusinessTypeEnum.FabricInformation.getK(),"",new String[]{"brand:brand","create_dept_id:create_dept_id"},true);
        Page<FabricBasicInformation>  objects = PageHelper.startPage(queryFabricInformationDto);
        baseMapper.selectList(queryWrapper);
        PageInfo<FabricInformationVo> copy = CopyUtil.copy(objects.toPageInfo(), FabricInformationVo.class);
        List<FabricInformationVo> list = copy.getList();
        if(CollUtil.isNotEmpty(list)) {
            List<String> ids = list.stream().map(FabricInformationVo::getId).collect(Collectors.toList());
            queryWrapper.clear();
            queryWrapper.in("a.foreign_id", ids);
            queryWrapper.in("a.del_flag", BaseGlobal.NO);
            queryWrapper.eq("a.type", AttachmentTypeConstant.TRANSFER_MANAGE_IMAGE);
            List<AttachmentVo> attachmentVoList = attachmentService.findByQw(queryWrapper);
            Map<String, List<AttachmentVo>> listMap = attachmentVoList.stream().collect(Collectors.groupingBy(AttachmentVo::getForeignId));
            for (FabricInformationVo fabricInformationVo : list) {
                List<AttachmentVo> voList = listMap.get(fabricInformationVo.getId());
                if(CollUtil.isNotEmpty(voList)){
                    fabricInformationVo.setImageUrlList(voList.stream().map(AttachmentVo::getUrl).collect(Collectors.toList()));
                }
            }
        }
        return copy;
    }

    @Override
    @Transactional(readOnly = false)
    @DuplicationCheck
    public ApiResult saveUpdateFabricBasic(SaveUpdateFabricBasicInformationDto saveUpdateFabricBasicDto) {
        //FabricBasicInformation fabricBasicInformation = new FabricBasicInformation();
        if (StringUtils.isNotBlank(saveUpdateFabricBasicDto.getId()) && !StringUtils.equals(saveUpdateFabricBasicDto.getId(),BaseGlobal.STOCK_STATUS_REJECT) ) {
            /*调整*/
            //fabricBasicInformation=baseMapper.selectById(saveUpdateFabricBasicDto.getId());
            //BeanUtils.copyProperties(saveUpdateFabricBasicDto,fabricBasicInformation );
            //fabricBasicInformation.updateInit();
            //OperaLogEntity operaLogEntity = new OperaLogEntity();
            //operaLogEntity.setName("面料调样单");
            //operaLogEntity.setDocumentId(fabricBasicInformation.getId());
            this.updateById(saveUpdateFabricBasicDto,"面料调样单",null,saveUpdateFabricBasicDto.getCodeName());
        } else {
            /*新增*/
            //BeanUtils.copyProperties(saveUpdateFabricBasicDto,fabricBasicInformation );
            //fabricBasicInformation.setCompanyCode(baseController.getUserCompany());
            saveUpdateFabricBasicDto.setId(null);
            saveUpdateFabricBasicDto.setRegisterDate(new Date());
            saveUpdateFabricBasicDto.setCreateDeptId(getVirtualDetpIds());
            this.save(saveUpdateFabricBasicDto);
            FabricBasicInformation f = this.getById(saveUpdateFabricBasicDto.getId());
            this.saveOperaLog("新增","面料调样单",null,f.getCodeName(),saveUpdateFabricBasicDto,null);
        }
        /*保存图片*/
        List<SampleAttachmentDto> attachmentDtoList = new ArrayList<>();
        String imageUrl = saveUpdateFabricBasicDto.getImageUrl();
        if (StringUtils.isNotBlank(imageUrl)) {
            String[] imageUrls = imageUrl.split(",");
            for (int i = 0; i < imageUrls.length; i++) {
                SampleAttachmentDto sampleAttachmentDto = new SampleAttachmentDto();
                sampleAttachmentDto.setFileId(imageUrls[i]);
                attachmentDtoList.add(sampleAttachmentDto);
            }
            attachmentService.saveFiles(saveUpdateFabricBasicDto.getId(), attachmentDtoList, AttachmentTypeConstant.TRANSFER_MANAGE_IMAGE);
        }
        /*发送面料调样单消息给面辅料专员*/
        messageUtils.atactiformSendMessage("fabric","1",baseController.getUser());
        return ApiResult.success("操作成功");
    }

    @Override
    public ApiResult delFabric(RemoveDto removeDto) {
        this.removeByIds(removeDto);
        return ApiResult.success("操作成功");
    }

    @Override
    public ApiResult getById(QueryDetailFabricDto queryDetailFabricDto) {
        FabricBasicInformation fabricBasicInformation=   baseMapper.selectById(queryDetailFabricDto.getId());
        FabricInformationVo fabricInformationVo=new FabricInformationVo();
        BeanUtils.copyProperties(fabricBasicInformation,fabricInformationVo );
        List<AttachmentVo> attachmentVoList = attachmentService.findByforeignId(fabricBasicInformation.getId(), AttachmentTypeConstant.TRANSFER_MANAGE_IMAGE);
        if(CollUtil.isNotEmpty(attachmentVoList)){
            fabricInformationVo.setAttachmentVoList(attachmentVoList);
        }
        return ApiResult.success("查询成功",fabricInformationVo);
    }

    /**
     * 导出面料调样单
     *
     * @param response
     * @param queryFabricInformationDto
     */
    @Override
    public void fabricInformationDeriveExcel(HttpServletResponse response, QueryFabricInformationDto queryFabricInformationDto) throws IOException {
        PageInfo<FabricInformationVo> pageInfo =    getFabricInformationList(queryFabricInformationDto);
        List<FabricInformationVo>  informationVoList = pageInfo.getList();
        List<FabricInformationExcelDto> fabricInformationExcelDtoList = BeanUtil.copyToList(informationVoList, FabricInformationExcelDto.class);
        fabricInformationExcelDtoList.forEach(f ->{
            if(CollUtil.isNotEmpty( f.getImageUrlList())){
                for (int i = 0; i < f.getImageUrlList().size(); i++) {
                    BeanUtil.setProperty(f,"imageUrl"+(i+1),f.getImageUrlList().get(i));
                }
            }
        });
        ExcelUtils.exportExcel(fabricInformationExcelDtoList,  FabricInformationExcelDto.class, "面样调样单.xlsx",new ExportParams("面样调样单", "面样调样单", ExcelType.HSSF) ,response);
    }

    /**
     * 上传理化报告
     *
     * @param id
     * @param file
     * @param request
     * @return
     * @throws Throwable
     */
    @Override
    public Boolean uploadingReport(String id, MultipartFile file, HttpServletRequest request) throws Throwable {
        FabricBasicInformation fabricBasicInformation = baseMapper.selectById(id);
        Object o = filesUtils.uploadBigData(file, FilesUtils.PRODUCT, request).getData();
        String s = o.toString();
        fabricBasicInformation.setReportName(file.getOriginalFilename());
        fabricBasicInformation.setReportUrl(s);
        fabricBasicInformation.updateInit();
        baseMapper.updateById(fabricBasicInformation);
        return true;
    }

/** 自定义方法区 不替换的区域【other_start】 **/



/** 自定义方法区 不替换的区域【other_end】 **/

}
