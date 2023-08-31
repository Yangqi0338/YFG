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
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.client.flowable.entity.AnswerDto;
import com.base.sbc.client.flowable.service.FlowableService;
import com.base.sbc.config.annotation.OperaLog;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.enums.OperationType;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.module.pack.dto.*;
import com.base.sbc.module.pack.entity.*;
import com.base.sbc.module.pack.mapper.PackBomVersionMapper;
import com.base.sbc.module.pack.service.*;
import com.base.sbc.module.pack.utils.PackUtils;
import com.base.sbc.module.pack.vo.PackBomVersionVo;
import com.base.sbc.module.purchase.service.PurchaseDemandService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;
import java.util.stream.Collectors;

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
public class PackBomVersionServiceImpl extends PackBaseServiceImpl<PackBomVersionMapper, PackBomVersion> implements PackBomVersionService {


// 自定义方法区 不替换的区域【other_start】

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
    private PurchaseDemandService purchaseDemandService;
    @Resource
    private CcmFeignService ccmFeignService;

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
    @OperaLog(value = "物料清单版本", operationType = OperationType.INSERT_UPDATE, pathSpEL = PackUtils.pathSqEL, parentIdSpEl = "#p0.foreignId")
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
        }
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
        setUpdateInfo(uw);
        log(id, StrUtil.equals(lockFlag, BaseGlobal.YES) ? "版本锁定" : "版本解锁");
        if(ccmFeignService.getSwitchByCode("LOCK_GENERATE_PURCHASE_DEMAND") && StrUtil.equals(lockFlag, BaseGlobal.YES)){
            //版本锁定后，生成采购需求单数据
            purchaseDemandService.generatePurchaseDemand(userCompany, id);
        }
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
        String foreignId = bomVersion.getForeignId();
        PackInfo packInfo = packInfoService.getById(foreignId);
        if (packInfo == null) {
            throw new OtherException("资料包不存在");
        }
        Map<String, Object> variables = BeanUtil.beanToMap(bomVersion);
        boolean flg = flowableService.start(FlowableService.design_bom_pdn + "[" + bomVersion.getVersion() + "]",
                FlowableService.design_bom_pdn, id, "/pdm/api/saas/packBom/version/approval",
                "/pdm/api/saas/packBom/version/approval", "/pdm/api/saas/packBom/version/approval", StrUtil.format("/styleManagement/dataPackage?id={}&styleId={}&style={}", packInfo.getId(), packInfo.getForeignId(), packInfo.getDesignNo()), variables);
        if (flg) {
            bomVersion.setConfirmStatus(BaseGlobal.STOCK_STATUS_WAIT_CHECK);
            updateById(bomVersion);
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
            throw new OtherException("已锁定");
        }
        return byId;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean copy(String sourceForeignId, String sourcePackType, String targetForeignId, String targetPackType) {
        /**
         * eg
         *设计A（物料版本)——大货A（物料版本），大货A（物料版本）反审——设计生成B版本，并启用。修改完成后，下发设计B（物料版本）——大货也变成B版本
         */
        this.del(targetForeignId, targetPackType);
        packBomService.del(targetForeignId, targetPackType);
        packBomSizeService.del(targetForeignId, targetPackType);
        Snowflake snowflake = IdUtil.getSnowflake();
        Map<String, String> newIdMaps = new HashMap<>(16);
        // 获取源数据
        // 1.获取版本
        QueryWrapper query = new QueryWrapper();
        query.eq("foreign_id", sourceForeignId);
        query.eq("pack_type", sourcePackType);
        List<PackBomVersion> versionsList = list(query);
        //2.获取物料清单
        List<PackBom> bomList = packBomService.list(query);
        // 3获取物料尺码
        List<PackBomSize> bomSizeList = packBomSizeService.list(query);

        //转大货 非空校验
        if (StrUtil.equals(targetPackType, PackUtils.PACK_TYPE_BIG_GOODS)) {
            //获取启动版本
            PackBomVersion version = CollUtil.findOne(versionsList, (a) -> StrUtil.equals(a.getStatus(), BaseGlobal.YES));
            //获取启动版本的BOM数据
            Collection<PackBom> packBoms = CollUtil.filterNew(bomList, bom -> StrUtil.equals(version.getId(), bom.getBomVersionId()));
            Collection<PackBomSize> packBomSizes = CollUtil.filterNew(bomSizeList, bom -> StrUtil.equals(version.getId(), bom.getBomVersionId()));
            checkBomDataEmptyThrowException(packBoms, packBomSizes);
        }

        //保存版本
        if (CollUtil.isNotEmpty(versionsList)) {
            for (PackBomVersion version : versionsList) {
                version.setPackType(targetPackType);
                version.setForeignId(targetForeignId);
                String newId = snowflake.nextIdStr();
                newIdMaps.put(version.getId(), newId);
                version.setId(newId);
            }
            saveBatch(versionsList);
        }
        //保存物料清单
        if (CollUtil.isNotEmpty(bomList)) {
            for (PackBom bom : bomList) {
                String newId = snowflake.nextIdStr();
                bom.setPackType(targetPackType);
                bom.setForeignId(targetForeignId);
                newIdMaps.put(bom.getId(), newId);
                bom.setId(newId);
                bom.setBomVersionId(newIdMaps.get(bom.getBomVersionId()));
            }
            packBomService.saveBatch(bomList);
        }
        //保存尺码
        if (CollUtil.isNotEmpty(bomSizeList)) {
            for (PackBomSize bomSize : bomSizeList) {
                String newId = snowflake.nextIdStr();
                bomSize.setPackType(targetPackType);
                bomSize.setForeignId(targetForeignId);
                bomSize.setId(newId);
                bomSize.setBomVersionId(newIdMaps.get(bomSize.getBomVersionId()));
                bomSize.setBomId(newIdMaps.get(bomSize.getBomId()));
            }
            packBomSizeService.saveBatch(bomSizeList);
        }
        //大货 到设计 表示反审，则新建一个bom版本
        if (StrUtil.equals(targetPackType, PackUtils.PACK_TYPE_DESIGN) && StrUtil.equals(sourcePackType, PackUtils.PACK_TYPE_BIG_GOODS)) {
            //查找启动版本
            PackBomVersion one = CollUtil.findOne(versionsList, (a) -> StrUtil.equals(a.getStatus(), BaseGlobal.YES));

            //创建一个新版本
            PackBomVersionDto versionDto = new PackBomVersionDto();
            versionDto.setForeignId(targetForeignId);
            versionDto.setPackType(targetPackType);
            PackBomVersionVo newVersion = saveVersion(versionDto);
            //启动版本
            enable(newVersion);

            if (one != null) {

                List<PackBom> newBomList = Opt.ofEmptyAble(bomList).orElse(new ArrayList<>())
                        .stream().filter((b) -> StrUtil.equals(b.getBomVersionId(), one.getId()))
                        .collect(Collectors.toList());
                if (CollUtil.isNotEmpty(newBomList)) {
                    List<String> bomIds = new ArrayList<>(16);
                    for (PackBom bom : newBomList) {
                        String newId = snowflake.nextIdStr();
                        bom.setPackType(targetPackType);
                        newIdMaps.put(bom.getId(), newId);
                        bomIds.add(bom.getId());
                        bom.setId(newId);
                        bom.setBomVersionId(newVersion.getId());
                    }
                    packBomService.saveBatch(newBomList);
                    //保存尺码
                    List<PackBomSize> newBomSizeList = Opt.ofEmptyAble(bomSizeList).orElse(new ArrayList<>()).stream().filter((c) -> bomIds.contains(c.getBomId())).collect(Collectors.toList());
                    if (CollUtil.isNotEmpty(newBomSizeList)) {
                        for (PackBomSize bomSize : newBomSizeList) {
                            String newId = snowflake.nextIdStr();
                            bomSize.setId(newId);
                            bomSize.setBomVersionId(newVersion.getId());
                            bomSize.setBomId(newIdMaps.get(bomSize.getBomId()));
                        }
                        packBomSizeService.saveBatch(newBomSizeList);
                    }

                }

            }
        }
        return true;
    }

    @Override
    public void checkBomDataEmptyThrowException(Collection<PackBom> bomList, Collection<PackBomSize> bomSizeList) {
        if (CollUtil.isEmpty(bomList)) {
            throw new OtherException("物料信息为空");
        }
        if (CollUtil.isEmpty(bomSizeList)) {
            throw new OtherException("物料尺码信息为空");
        }
        Map<String, PackBom> bomMap = new HashMap<>(16);
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        PackBomEmptyCheckDto c = null;
        List<String> errorMessage = new ArrayList<>(16);
        Map<String, List<String>> errorMsg = new HashMap<>(16);
        for (PackBom packBom : bomList) {
            errorMsg.put(packBom.getId(), CollUtil.newArrayList());
            bomMap.put(packBom.getId(), packBom);
            c = BeanUtil.copyProperties(packBom, PackBomEmptyCheckDto.class);
            Set<ConstraintViolation<PackBomEmptyCheckDto>> validate = validator.validate(c);
            if (CollUtil.isNotEmpty(validate)) {
                errorMsg.get(packBom.getId()).addAll(validate.stream().map(item -> item.getMessage()).collect(Collectors.toList()));
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
            throw new OtherException(CollUtil.join(errorMessage, StrUtil.COMMA));
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
            throw new OtherException(CollUtil.join(errorMessage, StrUtil.COMMA));
        }
    }

    @Override
    String getModeName() {
        return "物料清单版本";
    }

// 自定义方法区 不替换的区域【other_end】

}
