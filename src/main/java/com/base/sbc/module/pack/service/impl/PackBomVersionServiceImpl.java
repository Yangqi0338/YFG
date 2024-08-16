/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.client.flowable.entity.AnswerDto;
import com.base.sbc.client.flowable.service.FlowableService;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.enums.BasicNumber;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumSupplier;
import com.base.sbc.module.basicsdatum.service.BasicsdatumSupplierService;
import com.base.sbc.module.pack.dto.PackBomBigGoodsEmptyCheckDto;
import com.base.sbc.module.pack.dto.PackBomDesignEmptyCheckDto;
import com.base.sbc.module.pack.dto.PackBomEmptyCheckDto;
import com.base.sbc.module.pack.dto.PackBomSizeEmptyCheckDto;
import com.base.sbc.module.pack.dto.PackBomVersionDto;
import com.base.sbc.module.pack.dto.PackCommonPageSearchDto;
import com.base.sbc.module.pack.dto.PackCommonSearchDto;
import com.base.sbc.module.pack.dto.PackInfoDto;
import com.base.sbc.module.pack.entity.PackBom;
import com.base.sbc.module.pack.entity.PackBomColor;
import com.base.sbc.module.pack.entity.PackBomSize;
import com.base.sbc.module.pack.entity.PackBomVersion;
import com.base.sbc.module.pack.entity.PackInfo;
import com.base.sbc.module.pack.entity.PackInfoStatus;
import com.base.sbc.module.pack.mapper.PackBomVersionMapper;
import com.base.sbc.module.pack.service.*;
import com.base.sbc.module.pack.utils.PackUtils;
import com.base.sbc.module.pack.vo.PackBomSizeVo;
import com.base.sbc.module.pack.vo.PackBomVersionVo;
import com.base.sbc.module.pack.vo.PackBomVo;
import com.base.sbc.module.smp.SmpService;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.style.service.StyleInfoColorService;
import com.base.sbc.module.style.service.StyleService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.base.sbc.client.ccm.enums.CcmBaseSettingEnum.DESIGN_BOM_TO_BIG_GOODS_CHECK_SWITCH;
import static com.base.sbc.client.ccm.enums.CcmBaseSettingEnum.STYLE_MANY_COLOR;

/**
 * 类描述：资料包-物料清单-物料版本 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.pack.service.PackBomVersionService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-7-1 16:37:20
 */
@Service
public class PackBomVersionServiceImpl extends AbstractPackBaseServiceImpl<PackBomVersionMapper, PackBomVersion> implements PackBomVersionService {

    protected static Logger logger = LoggerFactory.getLogger(PackBomVersionServiceImpl.class);

// 自定义方法区 不替换的区域【other_start】

    @Resource
    private PackBomVersionService packBomVersionService;
    @Resource
    private PackBomService packBomService;
    @Resource
    private PackBomSizeService packBomSizeService;
    @Resource
    private PackInfoService packInfoService;
    @Resource
    private PackInfoStatusService packInfoStatusService;
    @Resource
    private FlowableService flowableService;
    @Resource
    private CcmFeignService ccmFeignService;
    @Resource
    private StyleInfoColorService styleInfoColorService;
    @Resource
    private PackBomColorService packBomColorService;
    @Resource
    private BasicsdatumSupplierService basicsdatumSupplierService;
    @Resource
    @Lazy
    private SmpService smpService;
    @Resource
    private StyleService styleService;

    @Resource
    private PackPricingBomService packPricingBomService;

    @Override
    public PageInfo<PackBomVersionVo> pageInfo(PackCommonPageSearchDto dto) {
        QueryWrapper<PackBomVersion> qw = new QueryWrapper<>();
        dto.setOrderBy("version desc");
        PackUtils.commonQw(qw, dto);
        Page<PackBomVersionVo> page = PageHelper.startPage(dto);
        list(qw);
        PageInfo<PackBomVersionVo> pageInfo = page.toPageInfo();
        PageInfo<PackBomVersionVo> voPageInfo = CopyUtil.copy(pageInfo, PackBomVersionVo.class);
        return voPageInfo;
    }


    @Override
    @Transactional(rollbackFor = {Exception.class})
    public PackBomVersionVo saveVersion(PackBomVersionDto dto) {
        //新增
        if (StrUtil.isBlank(dto.getId()) || StrUtil.contains(dto.getId(), StrUtil.DASHED)) {
            PackBomVersionVo pageData = BeanUtil.copyProperties(dto, PackBomVersionVo.class);
            pageData.setId(null);
            QueryWrapper<PackBomVersion> qw = new QueryWrapper<>();
            PackUtils.commonQw(qw, dto);
            long count = count(qw);
            pageData.setVersion("BOM " + (count + 1));
            pageData.setStatus(BaseGlobal.NO);
            save(pageData);
            return BeanUtil.copyProperties(pageData, PackBomVersionVo.class);
        }
        //修改
        else {
            PackBomVersion dbData = getById(dto.getId());
            if (dbData == null) {
                throw new OtherException(BaseErrorEnum.ERR_UPDATE_DATA_NOT_FOUND);
            }
            BeanUtil.copyProperties(dto, dbData);
            boolean b = updateById(dbData);
            return BeanUtil.copyProperties(dbData, PackBomVersionVo.class);
        }
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean changeVersionStatus(String id) {
        PackBomVersion version = getById(id);
        if (version == null) {
            throw new OtherException("未找到版本信息");
        }
        String status = version.getStatus();

        //停用操作
        if (StrUtil.equals(status, BaseGlobal.YES)) {
            QueryWrapper<PackBomVersion> qw = new QueryWrapper<>();
            PackUtils.commonQw(qw, version);
            qw.ne("id", id);
            qw.eq("status", BaseGlobal.YES);
            long count = count(qw);
            if (count == 0) {
                throw new OtherException("必须有一个是启用的");
            }
            version.setStatus(BaseGlobal.NO);
            updateById(version);
            log(id, "停用");
        }
        //启用操作
        else {
            // 1.将当前启用的停用 2.启用当前的
            enable(version);
            log(id, "启用");
            packBomService.update(new LambdaUpdateWrapper<PackBom>()
                    .eq(PackBom::getForeignId, version.getForeignId())
                    .ne(PackBom::getBomVersionId, id)
                    .set(PackBom::getStatus, BaseGlobal.NO)
            );
        }

        // 将所有packBom的状态改为version状态,方便调用
        packBomService.update(new LambdaUpdateWrapper<PackBom>()
                .eq(PackBom::getBomVersionId, id)
                .set(PackBom::getStatus, version.getStatus())
        );
        return true;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean enable(PackBomVersion version) {
        UpdateWrapper<PackBomVersion> qw = new UpdateWrapper<>();
        PackUtils.commonQw(qw, version);
        qw.eq("status", BaseGlobal.YES);
        qw.set("status", BaseGlobal.NO);
        setUpdateInfo(qw);
        update(qw);
        version.setStatus(BaseGlobal.YES);
        return updateById(version);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean lockChange(String userCompany, String id, String lockFlag) {
        UpdateWrapper<PackBomVersion> uw = new UpdateWrapper<>();
        uw.in("id", StrUtil.split(id, CharUtil.COMMA));
        uw.set("lock_flag", lockFlag);
        PackBomVersion packBomVersion = baseMapper.selectById(id);
        /*解锁取消大货制单员确认*/
        PackInfoStatus packInfoStatus = packInfoStatusService.get(packBomVersion.getForeignId(),packBomVersion.getPackType());
        packInfoStatus.setBulkOrderClerkConfirm(BaseGlobal.NO);
        packInfoStatusService.updateById(packInfoStatus);
        setUpdateInfo(uw);
        log(id, StrUtil.equals(lockFlag, BaseGlobal.YES) ? "版本锁定" : "版本解锁");
        return update(uw);
    }


    @Override
    public PackBomVersion getEnableVersion(PackCommonSearchDto dto) {
        QueryWrapper<PackBomVersion> qw = new QueryWrapper<>();
        PackUtils.commonQw(qw, dto);
        qw.eq("status", BaseGlobal.YES);
        qw.last("limit 1");
        PackBomVersion one = getOne(qw);
        return one;
    }

    @Override
    public List<PackBomVo> getEnableVersionBomList(String foreignId, String packType) {
        PackBomVersion packBomVersion = getEnableVersion(foreignId, packType);
        List<PackBomVo> list = packBomService.list(foreignId, packType, packBomVersion.getId());
        List<String> ids = list.stream().map(PackBomVo::getSupplierId).collect(Collectors.toList());
        List<BasicsdatumSupplier> basicsdatumSuppliers = basicsdatumSupplierService.listByField("supplier_code", ids);
        basicsdatumSuppliers.forEach(b -> list.forEach(l -> {
            if (StrUtil.equals(b.getSupplierCode(), l.getSupplierId())) {
                l.setSupplierAbbreviation(b.getSupplierAbbreviation());
            }
        }));
        return list;
    }

    @Override
    public PackBomVersion getEnableVersion(String foreignId, String packType) {
        PackCommonSearchDto dto = new PackCommonSearchDto();
        dto.setPackType(packType);
        dto.setForeignId(foreignId);
        return getEnableVersion(dto);
    }

    @Override
    public boolean startApproval(String id) {
        PackBomVersion bomVersion = getById(id);
        if (bomVersion == null) {
            throw new OtherException("版本不存在,请先保存");
        }
        if(StrUtil.equals(bomVersion.getConfirmStatus(),BaseGlobal.STOCK_STATUS_WAIT_CHECK)){
            throw new OtherException("已提交待审核");
        }

        String foreignId = bomVersion.getForeignId();
        PackInfo packInfo = packInfoService.getById(foreignId);
        if (packInfo == null) {
            throw new OtherException("资料包不存在");
        }
        /*校验物料清单是否全部下发*/
        bomVersion.getForeignId();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("pack_type", "packBigGoods");
        queryWrapper.eq("bom_version_id", bomVersion.getId());
        queryWrapper.eq("foreign_id", bomVersion.getForeignId());
        queryWrapper.in("scm_send_flag", StringUtils.convertList("0,2,3"));
        List<PackBom> packBomList = packBomService.list(queryWrapper);
        if (CollUtil.isNotEmpty(packBomList)) {
            throw new OtherException("物料清单存在未下发数据");
        }
        Map<String, Object> variables = BeanUtil.beanToMap(bomVersion);
        boolean flg = flowableService.start(FlowableService.DESIGN_BOM_PDN + "[" + bomVersion.getVersion() + "]",
                FlowableService.DESIGN_BOM_PDN, id, "/pdm/api/saas/packBom/version/approval",
                "/pdm/api/saas/packBom/version/approval", "/pdm/api/saas/packBom/version/approval", StrUtil.format("/styleManagement/dataPackage?id={}&styleId={}&style={}", packInfo.getId(), packInfo.getForeignId(), packInfo.getDesignNo()), variables);
        if (flg) {
            bomVersion.setConfirmStatus(BaseGlobal.STOCK_STATUS_WAIT_CHECK);
            updateById(bomVersion);
            //下发配色
            smpService.goods(new String[]{packInfo.getStyleColorId()});

        }
        return true;

    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean approval(AnswerDto dto) {
        PackBomVersion version = getById(dto.getBusinessKey());
        PackInfoStatus packInfoStatus = packInfoStatusService.get(version.getForeignId(), version.getPackType());
        if (version != null) {
            //通过
            if (StrUtil.equals(dto.getApprovalType(), BaseConstant.APPROVAL_PASS)) {
                version.setConfirmStatus(BaseGlobal.STOCK_STATUS_CHECKED);
                packInfoStatus.setBulkOrderClerkConfirm(BaseGlobal.YES);
                packInfoStatus.setBulkOrderClerkConfirmDate(new Date());
                /*审核通过锁定状态*/
                version.setLockFlag(BaseGlobal.STATUS_CLOSE);
            }
            //驳回
            else if (StrUtil.equals(dto.getApprovalType(), BaseConstant.APPROVAL_REJECT)) {
                version.setConfirmStatus(BaseGlobal.STOCK_STATUS_REJECT);
                packInfoStatus.setBulkOrderClerkConfirm(BaseGlobal.NO);
            } else {
                version.setConfirmStatus(BaseGlobal.STOCK_STATUS_DRAFT);
            }
            packInfoStatusService.updateById(packInfoStatus);
            updateById(version);
        }
        return true;
    }

    @Override
    public PackBomVersion checkVersion(String id) {
        PackBomVersion byId = getById(id);
        if (byId == null) {
            throw new OtherException("版本不存在");
        }
        if (StrUtil.equals(byId.getLockFlag(), BaseGlobal.YES)) {
            throw new OtherException("物料清单已锁定，请解锁");
        }
        return byId;
    }

    /**
     * 复制
     *
     * @param sourceForeignId
     * @param sourcePackType
     * @param targetForeignId
     * @param targetPackType
     * @param flg             0 正常拷贝,  1 转大货 ,2 反审
     * @return
     */
    @Override
    public boolean copy(String sourceForeignId, String sourcePackType, String targetForeignId, String targetPackType, String overlayFlag, String flg,String flag) {
        if (StrUtil.equals(sourceForeignId, targetForeignId) && StrUtil.equals(sourcePackType, targetPackType)) {
            return true;
        }
        /**
         * eg
         *设计A（物料版本)——大货A（物料版本），大货A（物料版本）反审——设计生成B版本，并启用。修改完成后，下发设计B（物料版本）——大货也变成B版本
         */
        Snowflake snowflake = IdUtil.getSnowflake();
        Map<String, String> newIdMaps = new HashMap<>(16);
        //物料
        List<PackBomVo> bomList = null;
        //配码
        List<PackBomSizeVo> bomSizeList = new ArrayList<>();
        //配色
        List<PackBomColor> packBomColorList = null;
        //处理版本
        //新版本
        PackBomVersionVo newVersion = null;
        //正常拷贝 保存版本
        if (StrUtil.equals(BasicNumber.ZERO.getNumber(), flg)) {
            //1获取源启用版本
            PackBomVersion enableVersion = getEnableVersion(sourceForeignId, sourcePackType);
            //获取源版本数据
            if (enableVersion != null) {
                bomList = packBomService.list(sourceForeignId, sourcePackType, enableVersion.getId());
            }
            List<String> bomIds = new ArrayList<>();
            if (CollUtil.isNotEmpty(bomList)) {
                if (bomList != null) {
                    for (PackBomVo packBomVo : bomList) {
                        bomIds.add(packBomVo.getId());
                        packBomVo.setScmSendFlag(BaseGlobal.NO);
                        if(!StrUtil.equals(flag,BaseGlobal.YES)){
                            packBomVo.setStageFlag(targetPackType);
                        }else {
                            /*大货添加的数据不改变状态用于报次款转大货时要下发的数据*/
                            if(StrUtil.equals(packBomVo.getStageFlag(),PackUtils.PACK_TYPE_BIG_GOODS)){
                                packBomVo.setScmSendFlag(BaseGlobal.IN_READY);
                            }
                        }
                        /*如果数据源是大货阶段的数据当设计单件用量空时取大货当前用量*/
                        if(StrUtil.equals(PackUtils.PACK_TYPE_BIG_GOODS,sourcePackType) && packBomVo.getDesignUnitUse() == null){
                            packBomVo.setDesignUnitUse(packBomVo.getBulkUnitUse());
                        }
                    }
                }
            }
            /*判断号型类型是否相等*/
            PackInfo packInfo = packInfoService.getById(sourceForeignId);
            /*查询原资料报*/
            PackInfo packInfo1 = packInfoService.getById(targetForeignId);

            Style style = styleService.getById(packInfo.getForeignId());

            Style style1 = styleService.getById(packInfo1.getForeignId());

            /*判断两个资料包的号型类型是否相同*/
            if(StrUtil.equals(style.getSizeRange(),style1.getSizeRange())){
                bomSizeList = packBomSizeService.getByBomIds(bomIds);
            }else {
                /*获取款式信息中选中的尺码添加的packbomzies表中*/
                List<String> productSizes = StringUtils.convertList(style1.getProductSizes());
                List<String> sizeIds = StringUtils.convertList(style1.getSizeIds());
                /*组装bom下面的配吗*/
                for (PackBomVo packBomVo : bomList) {
                    for (int i = 0; i < sizeIds.size(); i++) {
                        PackBomSizeVo packBomSize = new PackBomSizeVo();
                        packBomSize.setSizeId(sizeIds.get(i));
                        packBomSize.setSize(StringUtils.replaceBlank(productSizes.get(i)));
                        packBomSize.setWidth(packBomVo.getTranslate());
                        packBomSize.setWidthCode(packBomVo.getTranslateCode());
                        packBomSize.setQuantity(StrUtil.equals(packBomVo.getStageFlag(), PackUtils.PACK_TYPE_DESIGN) ? packBomVo.getDesignUnitUse() : packBomVo.getBulkUnitUse());
                        packBomSize.setBomId(packBomVo.getId());
                        bomSizeList.add(packBomSize);
                    }
                }
            }
            packBomColorList = BeanUtil.copyToList(packBomColorService.getByBomIds(bomIds), PackBomColor.class);
            // 1获取目标启用版本
            newVersion = BeanUtil.copyProperties(getEnableVersion(targetForeignId, targetPackType), PackBomVersionVo.class);
            if (newVersion == null) {
                // 创建目标启用版本 并启用
                PackBomVersionDto versionDto = new PackBomVersionDto();
                versionDto.setForeignId(targetForeignId);
                versionDto.setPackType(targetPackType);
                newVersion = saveVersion(versionDto);
                //启动版本
                enable(newVersion);
            } else {
                //覆盖 删除目标数据
                if (StrUtil.equals(overlayFlag, BaseGlobal.YES)) {
                    QueryWrapper delQw = new QueryWrapper();
                    delQw.eq("bom_version_id", newVersion.getId());
                    packBomService.remove(delQw);
                    packBomSizeService.remove(delQw);
                    packBomColorService.remove(delQw);
                }
            }
        }
        //转大货
        else if (StrUtil.equals(BasicNumber.ONE.getNumber(), flg)) {

            //1获取源启用版本
            PackBomVersion enableVersion = getEnableVersion(sourceForeignId, sourcePackType);
            //获取源版本数据
            bomList = packBomService.list(sourceForeignId, sourcePackType, enableVersion.getId());
            List<String> bomIds = Opt.ofNullable(bomList).map(bl -> bl.stream().map(PackBomVo::getId).collect(Collectors.toList())).orElse(CollUtil.newArrayList());
            bomSizeList = packBomSizeService.getByBomIds(bomIds);
            for (PackBomVo packBomVo : bomList) {
//                packBomVo.setPlanningLoossRate(0);
                if(packBomVo.getBulkUnitUse()==null ||packBomVo.getBulkUnitUse().compareTo(BigDecimal.ZERO) == 0 ){
                    //转大货 大货单件用量=设计单件用量
                    packBomVo.setBulkUnitUse(packBomVo.getDesignUnitUse());
                }
                /*转大货修改变成可编辑*/
                packBomVo.setScmSendFlag(BaseGlobal.IN_READY);
            }
            packBomColorList = BeanUtil.copyToList(packBomColorService.getByBomIds(bomIds), PackBomColor.class);

            //转大货 非空校验
            if (ccmFeignService.getSwitchByCode(DESIGN_BOM_TO_BIG_GOODS_CHECK_SWITCH.getKeyCode())) {
                /*过滤调不是这个阶段新增的数据*/
                List<PackBomVo> collect = bomList.stream().filter(b -> StrUtil.equals(b.getStageFlag(), b.getPackType())).collect(Collectors.toList());
                List<String> stringList = collect.stream().map(PackBomVo::getId).collect(Collectors.toList());
                List<PackBomSizeVo> sizeVoList = bomSizeList.stream().filter(s -> stringList.contains(s.getBomId())).collect(Collectors.toList());
                checkBomDataEmptyThrowException(collect, sizeVoList);
            }
            // 创建目标启用版本 并启用
            PackBomVersionDto versionDto = new PackBomVersionDto();
            versionDto.setForeignId(targetForeignId);
            versionDto.setPackType(targetPackType);
            newVersion = saveVersion(versionDto);
            //启动版本
            enable(newVersion);
        }
        //反审
        else if (StrUtil.equals(BasicNumber.TWO.getNumber(), flg)) {
            //1获取目标启用版本
            PackBomVersion enableVersion = getEnableVersion(sourceForeignId, sourcePackType);
            //获取源版本数据
            if (enableVersion != null) {
                bomList = packBomService.list(sourceForeignId, sourcePackType, enableVersion.getId());
            }
            List<String> bomIds = new ArrayList<>();
            if (CollUtil.isNotEmpty(bomList)) {
                for (PackBomVo packBomVo : bomList) {
                    bomIds.add(packBomVo.getId());
                    /*已下发的数据反审修改成可修改*/
                    if(StringUtils.equals(packBomVo.getScmSendFlag(),BaseGlobal.YES)){
                        packBomVo.setScmSendFlag(BaseGlobal.IN_READY);
                    }

                }
            }
            bomSizeList = packBomSizeService.getByBomIds(bomIds);
            packBomColorList = BeanUtil.copyToList(packBomColorService.getByBomIds(bomIds), PackBomColor.class);
            // 创建目标启用版本 并启用
            PackBomVersionDto versionDto = new PackBomVersionDto();
            versionDto.setForeignId(targetForeignId);
            versionDto.setPackType(targetPackType);
            newVersion = saveVersion(versionDto);
            //启动版本
            enable(newVersion);
        }

        // 查询版本最后一个数据的排序值
        PackBomVersion packBomVersion1 = packBomVersionService.getEnableVersion(targetForeignId, targetPackType);
        int targetSort = 0;
        PackBom packBom = packBomService.getOne(new LambdaQueryWrapper<PackBom>()
                .eq(PackBom::getForeignId, targetForeignId)
                .eq(PackBom::getPackType, targetPackType)
                .eq(PackBom::getBomVersionId, packBomVersion1.getId())
                .orderByDesc(PackBom::getSort)
                .last("limit 1")
        );
        if (ObjectUtil.isNotEmpty(packBom)) {
            targetSort = packBom.getSort();
        }

        //保存物料清单
        if (CollUtil.isNotEmpty(bomList)) {
            for (int i = 0; i < bomList.size(); i++) {
                PackBom bom = bomList.get(i);
                // 查询物料清单-单款多色数据
                QueryWrapper<PackBomColor> bomColorQueryWrapper = new QueryWrapper<>();
                bomColorQueryWrapper.eq("foreign_id", sourceForeignId);
                bomColorQueryWrapper.eq("pack_type", sourcePackType);
                bomColorQueryWrapper.eq("bom_id", bom.getId());
                String newId = snowflake.nextIdStr();
                bom.setPackType(targetPackType);
                //bom.setCode(newVersion.getVersion() + StrUtil.DASHED + (i + 1));
                bom.setForeignId(targetForeignId);
                newIdMaps.put(bom.getId(), newId);
                bom.setId(newId);
                if (StrUtil.equals(BasicNumber.ZERO.getNumber(), flg)) {
                    bom.setSort(targetSort + i + 1);
                }
                bom.setBomVersionId(newVersion.getId());
                bom.setHistoricalData(BaseGlobal.NO);
                bom.insertInit();
            }
            packBomService.saveBatch(BeanUtil.copyToList(bomList, PackBom.class));
            if(StrUtil.equals(BasicNumber.ONE.getNumber(), flg)){
                packPricingBomService.copy(sourceForeignId, sourcePackType, targetForeignId, targetPackType,newIdMaps);
            }
        }
        // 复制物料清单-单款多色数据到大货资料包
        if (CollUtil.isNotEmpty(packBomColorList)) {
            for (PackBomColor packBomColor : packBomColorList) {
                packBomColor.setId(null);
                packBomColor.insertInit();
                packBomColor.setBomVersionId(newVersion.getId());
                packBomColor.setBomId(newIdMaps.get(packBomColor.getBomId()));
                packBomColor.setPackType(targetPackType);
                packBomColor.setForeignId(targetForeignId);
            }
            packBomColorService.saveBatch(packBomColorList);
        }
        //保存尺码
        if (CollUtil.isNotEmpty(bomSizeList)) {
            for (PackBomSize bomSize : bomSizeList) {
                bomSize.setPackType(targetPackType);
                bomSize.setForeignId(targetForeignId);
                bomSize.setId(null);
                bomSize.setBomVersionId(newVersion.getId());
                bomSize.setBomId(newIdMaps.get(bomSize.getBomId()));
            }
            packBomSizeService.saveBatch(BeanUtil.copyToList(bomSizeList, PackBomSize.class));
        }
        Boolean styleManyColorSwitch = ccmFeignService.getSwitchByCode(STYLE_MANY_COLOR.getKeyCode());
        if (styleManyColorSwitch) {
            try {
                // 保存款式设计详情颜色
                PackInfoDto packInfoDto = new PackInfoDto();
                packInfoDto.setId(sourceForeignId);
                packInfoDto.setSourcePackType(sourcePackType);
                packInfoDto.setTargetPackType(targetPackType);
                packInfoDto.setTargetForeignId(targetForeignId);
                styleInfoColorService.insertStyleInfoColorList(packInfoDto);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("保存款式设计详情颜色错误信息如下：{}", e);
            }
        }


        return true;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean copy(String sourceForeignId, String sourcePackType, String targetForeignId, String targetPackType, String overlayFlag) {
        return copy(sourceForeignId, sourcePackType, targetForeignId, targetPackType, overlayFlag, BasicNumber.ZERO.getNumber(),null);
    }

    @Override
    public void checkBomDataEmptyThrowException(Collection<? extends PackBom> bomList, Collection<? extends PackBomSize> bomSizeList) {
        if (CollUtil.isEmpty(bomList)) {
            throw new OtherException("物料信息为空");
        }
        if (CollUtil.isEmpty(bomSizeList)) {
            throw new OtherException("物料尺码信息为空");
        }
        Map<String, PackBom> bomMap = new HashMap<>(16);
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        PackBomBigGoodsEmptyCheckDto pe = null;
        PackBomDesignEmptyCheckDto pd = null;
        List<String> errorMessage = new ArrayList<>(16);
        Map<String, List<String>> errorMsg = new HashMap<>(16);
        for (PackBom packBom : bomList) {
            errorMsg.put(packBom.getId(), CollUtil.newArrayList());
            bomMap.put(packBom.getId(), packBom);
            if (StrUtil.equals(packBom.getPackType(), "packDesign")) {
                pd = BeanUtil.copyProperties(packBom, PackBomDesignEmptyCheckDto.class);
                Set<ConstraintViolation<PackBomDesignEmptyCheckDto>> validate = validator.validate(pd);
                if (CollUtil.isNotEmpty(validate)) {
                    errorMsg.get(packBom.getId()).addAll(validate.stream().map(item -> item.getMessage()).collect(Collectors.toList()));
                }
            } else {
                pe = BeanUtil.copyProperties(packBom, PackBomBigGoodsEmptyCheckDto.class);
                Set<ConstraintViolation<PackBomBigGoodsEmptyCheckDto>> validate = validator.validate(pe);
                if (CollUtil.isNotEmpty(validate)) {
                    errorMsg.get(packBom.getId()).addAll(validate.stream().map(item -> item.getMessage()).collect(Collectors.toList()));
                }
            }
        }
        PackBomSizeEmptyCheckDto cs = null;
        for (PackBomSize packBomSize : bomSizeList) {
            cs = BeanUtil.copyProperties(packBomSize, PackBomSizeEmptyCheckDto.class);
            Set<ConstraintViolation<PackBomSizeEmptyCheckDto>> validate = validator.validate(cs);
            if (CollUtil.isNotEmpty(validate) && bomMap.containsKey(packBomSize.getBomId())) {
                errorMsg.get(packBomSize.getBomId()).addAll(validate.stream().map(item -> packBomSize.getSize() + item.getMessage()).collect(Collectors.toList()));
            }
        }
        for (Map.Entry<String, List<String>> em : errorMsg.entrySet()) {
            List<String> value = em.getValue();
            String key = em.getKey();
            if (CollUtil.isEmpty(value)) {
                continue;
            }
            PackBom packBom = bomMap.get(key);
            if (packBom == null) {
                continue;
            }
            errorMessage.add(StrUtil.format("物料【{}】{}不能为空", packBom.getMaterialCodeName(), CollUtil.join(value, StrUtil.COMMA)));

        }
        if (CollUtil.isNotEmpty(errorMessage)) {
            throw new OtherException(CollUtil.join(errorMessage, StrUtil.LF));
        }
    }

    @Override
    public void checkBomDataEmptyThrowException(PackBom bom) {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        PackBomEmptyCheckDto c = BeanUtil.copyProperties(bom, PackBomEmptyCheckDto.class);
        Set<String> errorMessage = new HashSet<>(16);
        Set<ConstraintViolation<PackBomEmptyCheckDto>> validate = validator.validate(c);
        if (CollUtil.isNotEmpty(validate)) {
            validate.forEach(item -> {
                errorMessage.add(item.getMessage());
            });
            throw new OtherException(c.getMaterialCodeName()+"未填写:" + CollUtil.join(errorMessage, StrUtil.COMMA));
        }

    }

    @Override
    public void checkBomSizeDataEmptyThrowException(PackBomSize bomSize) {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        PackBomSizeEmptyCheckDto c = BeanUtil.copyProperties(bomSize, PackBomSizeEmptyCheckDto.class);
        Set<String> errorMessage = new HashSet<>(16);
        Set<ConstraintViolation<PackBomSizeEmptyCheckDto>> validate = validator.validate(c);
        if (CollUtil.isNotEmpty(validate)) {
            validate.forEach(item -> {
                errorMessage.add(item.getMessage());
            });
            throw new OtherException("未填写:" + CollUtil.join(errorMessage, StrUtil.COMMA));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean reverseApproval(String id) {
        PackBomVersion version = getById(id);
        PackInfoStatus packInfoStatus = packInfoStatusService.get(version.getForeignId(), version.getPackType());
        version.setConfirmStatus("0");
        packInfoStatus.setBulkOrderClerkConfirm(BaseGlobal.NO);
        packInfoStatus.setBulkOrderClerkConfirmDate(null);
        /*审核通过锁定状态*/
        version.setLockFlag(BaseGlobal.STATUS_NORMAL);
        packInfoStatusService.updateById(packInfoStatus);
        updateById(version);
        return true;
    }


    @Override
    String getModeName() {
        return "物料清单版本";
    }

// 自定义方法区 不替换的区域【other_end】

}
