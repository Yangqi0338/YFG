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
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.enums.BasicNumber;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.pack.dto.*;
import com.base.sbc.module.pack.entity.*;
import com.base.sbc.module.pack.mapper.PackBomVersionMapper;
import com.base.sbc.module.pack.service.*;
import com.base.sbc.module.pack.utils.PackUtils;
import com.base.sbc.module.pack.vo.PackBomColorVo;
import com.base.sbc.module.pack.vo.PackBomSizeVo;
import com.base.sbc.module.pack.vo.PackBomVersionVo;
import com.base.sbc.module.pack.vo.PackBomVo;
import com.base.sbc.module.purchase.service.PurchaseDemandService;
import com.base.sbc.module.style.service.StyleInfoColorService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;
import java.util.stream.Collectors;

import static com.base.sbc.client.ccm.enums.CcmBaseSettingEnum.DESIGN_BOM_TO_BIG_GOODS_CHECK_SWITCH;

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

    protected static Logger logger = LoggerFactory.getLogger(PackBomVersionServiceImpl.class);

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
    @Resource
    private StyleInfoColorService styleInfoColorService;
    @Resource
    private PackBomColorService packBomColorService;

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
        return packBomService.list(foreignId, packType, packBomVersion.getId());
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
        queryWrapper.in("scm_send_flag", StringUtils.convertList("0,2"));
        List<PackBom> packBomList = packBomService.list(queryWrapper);
        if (CollUtil.isNotEmpty(packBomList)) {
            throw new OtherException("物料清单存在未下发数据");
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
    public boolean copy(String sourceForeignId, String sourcePackType, String targetForeignId, String targetPackType, String flg) {
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
        List<PackBomSizeVo> bomSizeList = null;
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
                for (PackBomVo packBomVo : bomList) {
                    bomIds.add(packBomVo.getId());
                    packBomVo.setScmSendFlag(BaseGlobal.NO);
                    packBomVo.setStageFlag(PackUtils.PACK_TYPE_DESIGN);
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
        //转大货
        else if (StrUtil.equals(BasicNumber.ONE.getNumber(), flg)) {

            //1获取源启用版本
            PackBomVersion enableVersion = getEnableVersion(sourceForeignId, sourcePackType);
            //获取源版本数据
            bomList = packBomService.list(sourceForeignId, sourcePackType, enableVersion.getId());
            List<String> bomIds = Opt.ofNullable(bomList).map(bl -> bl.stream().map(PackBomVo::getId).collect(Collectors.toList())).orElse(CollUtil.newArrayList());
            bomSizeList = packBomSizeService.getByBomIds(bomIds);
            for (PackBomVo packBomVo : bomList) {
                packBomVo.setLossRate(null);
                /*转大货修改变成未下发*/
                packBomVo.setScmSendFlag("0");
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
            PackBomVersion targetEnableVersion = getEnableVersion(targetForeignId, targetPackType);
            //获取目标数据
            List<PackBom> targetBomList = packBomService.list(new QueryWrapper<PackBom>()
                    .eq("bom_version_id", targetEnableVersion.getId())
                    .eq("foreign_id", targetForeignId)
                    .eq("pack_type", targetPackType)
                    .eq("stage_flag", PackUtils.PACK_TYPE_DESIGN));
            List<String> bomIds = Opt.ofNullable(targetBomList).map(bl -> bl.stream().map(PackBom::getId).collect(Collectors.toList())).orElse(CollUtil.newArrayList());
            List<PackBomSizeVo> targetBomSizeList = packBomSizeService.getByBomIds(bomIds);
            List<PackBomColorVo> targetPackBomColorList = packBomColorService.getByBomIds(bomIds);
            // bom 数据为设计阶段数据源数据 + 在大货阶段添加的数据
            //获取在大货阶段添加的数据
            PackBomVersion bigEnableVersion = getEnableVersion(sourceForeignId, sourcePackType);
            List<PackBom> bigBomList = packBomService.list(new QueryWrapper<PackBom>()
                    .eq("bom_version_id", bigEnableVersion.getId())
                    .eq("foreign_id", sourceForeignId)
                    .eq("pack_type", sourcePackType)
                    .eq("stage_flag", PackUtils.PACK_TYPE_BIG_GOODS));
            List<String> bigBimIds = Opt.ofNullable(bigBomList).map(bl -> bl.stream().map(PackBom::getId).collect(Collectors.toList())).orElse(CollUtil.newArrayList());
            List<PackBomSizeVo> bigBomSizeList = packBomSizeService.getByBomIds(bigBimIds);
            List<PackBomColorVo> bigPackBomColorList = packBomColorService.getByBomIds(bigBimIds);
            CollUtil.newArrayList(targetBomList);
            bomList = CollUtil.unionAll(BeanUtil.copyToList(targetBomList, PackBomVo.class), BeanUtil.copyToList(bigBomList, PackBomVo.class));
            bomSizeList = CollUtil.unionAll(targetBomSizeList, bigBomSizeList);
            packBomColorList = CollUtil.unionAll(BeanUtil.copyToList(targetPackBomColorList, PackBomColor.class),
                    BeanUtil.copyToList(bigPackBomColorList, PackBomColor.class));
            // 创建目标启用版本 并启用
            PackBomVersionDto versionDto = new PackBomVersionDto();
            versionDto.setForeignId(targetForeignId);
            versionDto.setPackType(targetPackType);
            newVersion = saveVersion(versionDto);
            //启动版本
            enable(newVersion);
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
                bom.setCode(newVersion.getVersion() + StrUtil.DASHED + (i + 1));
                bom.setForeignId(targetForeignId);
                newIdMaps.put(bom.getId(), newId);
                bom.setId(newId);
                bom.setBomVersionId(newVersion.getId());
            }
            packBomService.saveBatch(BeanUtil.copyToList(bomList, PackBom.class));

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


        return true;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean copy(String sourceForeignId, String sourcePackType, String targetForeignId, String targetPackType) {
        return copy(sourceForeignId, sourcePackType, targetForeignId, targetPackType, BasicNumber.ZERO.getNumber());
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
            throw new OtherException("未填写:" + CollUtil.join(errorMessage, StrUtil.COMMA));
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
    String getModeName() {
        return "物料清单版本";
    }

// 自定义方法区 不替换的区域【other_end】

}
