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
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.flowable.entity.AnswerDto;
import com.base.sbc.client.flowable.service.FlowableService;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.enums.BasicNumber;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.common.service.AttachmentService;
import com.base.sbc.module.operaLog.entity.OperaLogEntity;
import com.base.sbc.module.operaLog.service.OperaLogService;
import com.base.sbc.module.pack.dto.PackBomVersionDto;
import com.base.sbc.module.pack.dto.PackCommonPageSearchDto;
import com.base.sbc.module.pack.dto.PackCommonSearchDto;
import com.base.sbc.module.pack.dto.PackInfoSearchPageDto;
import com.base.sbc.module.pack.entity.PackBomVersion;
import com.base.sbc.module.pack.entity.PackInfo;
import com.base.sbc.module.pack.entity.PackInfoStatus;
import com.base.sbc.module.pack.mapper.PackInfoMapper;
import com.base.sbc.module.pack.service.*;
import com.base.sbc.module.pack.utils.PackUtils;
import com.base.sbc.module.pack.vo.BigGoodsPackInfoListVo;
import com.base.sbc.module.pack.vo.PackBomVersionVo;
import com.base.sbc.module.pack.vo.PackInfoListVo;
import com.base.sbc.module.pack.vo.SampleDesignPackInfoListVo;
import com.base.sbc.module.sample.entity.SampleDesign;
import com.base.sbc.module.sample.service.SampleDesignService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 类描述：资料包 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.pack.service.PackInfoService
 * @email your email
 * @date 创建时间：2023-7-6 17:13:01
 */
@Service
public class PackInfoServiceImpl extends PackBaseServiceImpl<PackInfoMapper, PackInfo> implements PackInfoService {


// 自定义方法区 不替换的区域【other_start】

    @Resource
    private SampleDesignService sampleDesignService;
    @Resource
    private AttachmentService attachmentService;
    @Resource
    private OperaLogService operaLogService;
    @Resource
    private PackBomVersionService packBomVersionService;
    @Resource
    private PackBomService packBomService;
    @Resource
    private PackBomSizeService packBomSizeService;
    @Resource
    private PackSizeService packSizeService;
    @Resource
    private PackProcessPriceService packProcessPriceService;
    @Resource
    private PackPricingService packPricingService;

    @Resource
    private PackPricingProcessCostsService packPricingProcessCostsService;

    @Resource
    private PackPricingCraftCostsService packPricingCraftCostsService;
    @Resource
    private PackPricingOtherCostsService packPricingOtherCostsService;
    @Resource
    private PackTechSpecService packTechSpecService;
    @Resource
    private PackSampleReviewService packSampleReviewService;
    @Resource
    private PackBusinessOpinionService packBusinessOpinionService;

    @Resource
    private PackInfoStatusService packInfoStatusService;
    @Resource
    private FlowableService flowableService;

    @Override
    public PageInfo<SampleDesignPackInfoListVo> pageBySampleDesign(PackInfoSearchPageDto pageDto) {

        // 查询样衣设计数据
        BaseQueryWrapper<SampleDesign> sdQw = new BaseQueryWrapper<>();
        sdQw.in("status", "1", "2");
        sdQw.notEmptyEq("prod_category1st", pageDto.getProdCategory1st());
        sdQw.notEmptyEq("prod_category", pageDto.getProdCategory());
        sdQw.notEmptyEq("prod_category2nd", pageDto.getProdCategory2nd());
        sdQw.notEmptyEq("prod_category3rd", pageDto.getProdCategory3rd());
        sdQw.notEmptyEq("planning_season_id", pageDto.getPlanningSeasonId());
        sdQw.andLike(pageDto.getSearch(), "design_no", "style_no", "style_name");
        sdQw.notEmptyEq("devt_type", pageDto.getDevtType());
        Page<SampleDesign> page = PageHelper.startPage(pageDto);
        sampleDesignService.list(sdQw);
        PageInfo<SampleDesignPackInfoListVo> pageInfo = CopyUtil.copy(page.toPageInfo(), SampleDesignPackInfoListVo.class);
        //查询bom列表
        List<SampleDesignPackInfoListVo> sdpList = pageInfo.getList();
        if (CollUtil.isNotEmpty(sdpList)) {
            //图片
            attachmentService.setListStylePic(sdpList, "stylePic");
            List<String> sdIds = sdpList.stream().map(SampleDesignPackInfoListVo::getId).collect(Collectors.toList());
            Map<String, List<PackInfoListVo>> piMaps = queryListToMapGroupByForeignId(sdIds, PackUtils.PACK_TYPE_DESIGN);
            for (SampleDesignPackInfoListVo sd : sdpList) {
                List<PackInfoListVo> packInfoListVos = piMaps.get(sd.getId());
                sd.setPackInfoList(packInfoListVos);
                if (CollUtil.isNotEmpty(packInfoListVos)) {
                    for (PackInfoListVo packInfoListVo : packInfoListVos) {
                        packInfoListVo.setStylePic(sd.getStylePic());
                    }
                }
            }
        }
        return pageInfo;
    }

    @Override
    public PackInfoListVo createBySampleDesign(String id) {
        SampleDesign sampleDesign = sampleDesignService.getById(id);
        if (sampleDesign == null) {
            throw new OtherException(BaseErrorEnum.ERR_INSERT_DATA_REPEAT);
        }
        PackInfo packInfo = BeanUtil.copyProperties(sampleDesign, PackInfo.class, "id", "status");
        CommonUtils.resetCreateUpdate(packInfo);
        String newId = IdUtil.getSnowflake().nextIdStr();
        packInfo.setId(newId);
        packInfo.setForeignId(id);

        //设置编码
        QueryWrapper codeQw = new QueryWrapper();
        codeQw.eq("foreign_id", id);
        long count = count(codeQw);
        packInfo.setCode(sampleDesign.getDesignNo() + StrUtil.DASHED + (count + 1));
        save(packInfo);
        //新建bom版本
        PackBomVersionDto versionDto = BeanUtil.copyProperties(packInfo, PackBomVersionDto.class, "id");
        versionDto.setPackType(PackUtils.PACK_TYPE_DESIGN);
        versionDto.setForeignId(newId);
        // 新建资料包状态表
        packInfoStatusService.newStatus(newId, PackUtils.PACK_TYPE_DESIGN);
        PackBomVersionVo packBomVersionVo = packBomVersionService.saveVersion(versionDto);
        packBomVersionService.enable(BeanUtil.copyProperties(packBomVersionVo, PackBomVersion.class));
        return BeanUtil.copyProperties(getById(packInfo.getId()), PackInfoListVo.class);
    }

    @Override
    public Map<String, List<PackInfoListVo>> queryListToMapGroupByForeignId(List<String> foreignIds, String packType) {
        QueryWrapper<PackInfo> qw = new QueryWrapper<>();
        qw.in("foreign_id", foreignIds);
        qw.eq("pack_type", packType);
        List<PackInfoListVo> list = getBaseMapper().queryByQw(qw);
        return Opt.ofNullable(list).map(l -> l.stream().collect(Collectors.groupingBy(PackInfoListVo::getForeignId))).orElse(MapUtil.empty());
    }

    @Override
    public PageInfo<OperaLogEntity> operationLog(PackCommonPageSearchDto pageDto) {
        QueryWrapper<OperaLogEntity> qw = new QueryWrapper<>();
        qw.eq("parent_id", pageDto.getForeignId());
        qw.likeRight("path", CollUtil.join(CollUtil.newArrayList("资料包", pageDto.getPackType(), pageDto.getForeignId()), StrUtil.DASHED));
        qw.orderByDesc("id");
        Page<OperaLogEntity> objects = PageHelper.startPage(pageDto);
        operaLogService.list(qw);
        return objects.toPageInfo();
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean toBigGoods(PackCommonSearchDto dto) {
        //查看资料包信息是否存在
        PackInfo packInfo = this.getById(dto.getForeignId());
        if (packInfo == null) {
            throw new OtherException("资料包信息不存在");
        }
        //查看版本是否锁定
        PackBomVersion version = packBomVersionService.getEnableVersion(dto);
        if (version == null) {
            throw new OtherException("无物料清单版本");
        }
        if (!StrUtil.equals(version.getLockFlag(), BaseGlobal.YES)) {
            throw new OtherException("物料清单版本未锁定");
        }
        //无配色信息
        if (StringUtils.isAnyBlank(packInfo.getStyleNo(), packInfo.getColor(), packInfo.getSampleStyleColorId())) {
            throw new OtherException("没有配色信息");
        }
        copyPack(dto.getForeignId(), dto.getPackType(), dto.getForeignId(), PackUtils.PACK_TYPE_BIG_GOODS);
        PackInfoStatus packInfoStatus = packInfoStatusService.get(dto.getForeignId(), PackUtils.PACK_TYPE_BIG_GOODS);
        //设置为已转大货
        packInfoStatus.setBomStatus(BasicNumber.ONE.getNumber());
        packInfoStatusService.updateById(packInfoStatus);
        //updateById(packInfo);
        return true;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean copyPack(String sourceForeignId, String sourcePackType, String targetForeignId, String targetPackType) {
        //图样附件、物料清单、尺寸表、工序工价、核价信息、工艺说明、样衣评审、业务意见、吊牌洗唛

        //图样附件
        attachmentService.copy(sourceForeignId, sourcePackType, targetForeignId, targetPackType);
        //物料清单
        packBomVersionService.copy(sourceForeignId, sourcePackType, targetForeignId, targetPackType);
        //尺寸表
        packSizeService.copy(sourceForeignId, sourcePackType, targetForeignId, targetPackType);
        //工序工价
        packProcessPriceService.copy(sourceForeignId, sourcePackType, targetForeignId, targetPackType);
        //核价信息
        // 基础
        packPricingService.copy(sourceForeignId, sourcePackType, targetForeignId, targetPackType);
        // 二次加工费
        packPricingCraftCostsService.copy(sourceForeignId, sourcePackType, targetForeignId, targetPackType);
        //加工费
        packPricingProcessCostsService.copy(sourceForeignId, sourcePackType, targetForeignId, targetPackType);
        // 其他费用
        packPricingOtherCostsService.copy(sourceForeignId, sourcePackType, targetForeignId, targetPackType);
        //工艺说明
        packTechSpecService.copy(sourceForeignId, sourcePackType, targetForeignId, targetPackType);
        //样衣评审
        packSampleReviewService.copy(sourceForeignId, sourcePackType, targetForeignId, targetPackType);
        //业务意见
        packBusinessOpinionService.copy(sourceForeignId, sourcePackType, targetForeignId, targetPackType);

        return true;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean startReverseApproval(String id) {
        PackInfo pack = getById(id);
        if (pack == null) {
            throw new OtherException("资料包数据不存在,请先保存");
        }
        Map<String, Object> variables = BeanUtil.beanToMap(pack);
        boolean flg = flowableService.start(FlowableService.big_goods_reverse + "[" + pack.getCode() + "]",
                FlowableService.big_goods_reverse, id,
                "/pdm/api/saas/packInfo/approval",
                "/pdm/api/saas/packInfo/approval",
                StrUtil.format("/styleManagement/dataPackage?id={}&sampleDesignId={}&style={}", pack.getId(), pack.getForeignId(), pack.getDesignNo()),
                variables);
        if (flg) {
            PackInfoStatus packInfoStatus = packInfoStatusService.get(id, PackUtils.PACK_TYPE_BIG_GOODS);
            packInfoStatus.setReverseConfirmStatus(BaseGlobal.STOCK_STATUS_WAIT_CHECK);
            packInfoStatusService.updateById(packInfoStatus);
        }
        return true;
    }

    @Override

    @Transactional(rollbackFor = {Exception.class})
    public boolean reverseApproval(AnswerDto dto) {
        PackInfo packInfo = getById(dto.getBusinessKey());
        if (packInfo != null) {
            PackInfoStatus packInfoStatus = packInfoStatusService.get(dto.getBusinessKey(), PackUtils.PACK_TYPE_BIG_GOODS);
            //通过
            if (StrUtil.equals(dto.getApprovalType(), BaseConstant.APPROVAL_PASS)) {
                copy(dto.getBusinessKey(), PackUtils.PACK_TYPE_BIG_GOODS, dto.getBusinessKey(), PackUtils.PACK_TYPE_DESIGN);
                packInfoStatus.setReverseConfirmStatus(BaseGlobal.STOCK_STATUS_CHECKED);
                packInfoStatus.setScmSendFlag(BaseGlobal.NO);
            }
            //驳回
            else if (StrUtil.equals(dto.getApprovalType(), BaseConstant.APPROVAL_REJECT)) {
                packInfoStatus.setReverseConfirmStatus(BaseGlobal.STOCK_STATUS_REJECT);
            }
            packInfoStatusService.updateById(packInfoStatus);
        }
        return true;
    }

    @Override
    public List<PackInfoListVo> queryByQw(QueryWrapper queryWrapper) {
        return getBaseMapper().queryByQw(queryWrapper);
    }

    @Override
    public PageInfo<BigGoodsPackInfoListVo> pageByBigGoods(PackInfoSearchPageDto pageDto) {
        BaseQueryWrapper<PackInfo> sdQw = new BaseQueryWrapper<>();
        sdQw.eq("bom_status", BasicNumber.ONE.getNumber());
        sdQw.eq("pack_type", PackUtils.PACK_TYPE_BIG_GOODS);
        sdQw.notEmptyEq("prod_category1st", pageDto.getProdCategory1st());
        sdQw.notEmptyEq("prod_category", pageDto.getProdCategory());
        sdQw.notEmptyEq("prod_category2nd", pageDto.getProdCategory2nd());
        sdQw.notEmptyEq("prod_category3rd", pageDto.getProdCategory3rd());
        sdQw.notEmptyEq("planning_season_id", pageDto.getPlanningSeasonId());
        sdQw.andLike(pageDto.getSearch(), "design_no", "style_no", "style_name");
        sdQw.notEmptyEq("devt_type", pageDto.getDevtType());
        Page<PackInfoListVo> page = PageHelper.startPage(pageDto);
//        list(sdQw);
        queryByQw(sdQw);
        PageInfo<BigGoodsPackInfoListVo> pageInfo = CopyUtil.copy(page.toPageInfo(), BigGoodsPackInfoListVo.class);
        return pageInfo;
    }

    @Override
    public PackInfo get(String foreignId, String packType) {
        QueryWrapper<PackInfo> qw = new QueryWrapper<>();
        qw.in("id", foreignId);
        qw.last("limit 1");
        return getOne(qw);
    }

    @Override
    public PackInfoListVo getDetail(String id, String packType) {
        QueryWrapper<PackInfo> qw = new QueryWrapper<>();
        qw.in("id", id);
        qw.eq("pack_type", packType);
        qw.last("limit 1");
        List<PackInfoListVo> packInfoListVos = getBaseMapper().queryByQw(qw);
        return CollUtil.get(packInfoListVos, 0);
    }

    @Override
    String getModeName() {
        return "资料包明细";
    }


// 自定义方法区 不替换的区域【other_end】

}
