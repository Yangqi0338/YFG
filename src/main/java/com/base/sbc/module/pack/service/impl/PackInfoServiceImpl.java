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
import com.base.sbc.module.common.service.AttachmentService;
import com.base.sbc.module.operaLog.entity.OperaLogEntity;
import com.base.sbc.module.operaLog.service.OperaLogService;
import com.base.sbc.module.pack.dto.PackBomVersionDto;
import com.base.sbc.module.pack.dto.PackCommonPageSearchDto;
import com.base.sbc.module.pack.dto.PackCommonSearchDto;
import com.base.sbc.module.pack.dto.PackInfoSearchPageDto;
import com.base.sbc.module.pack.entity.PackBomVersion;
import com.base.sbc.module.pack.entity.PackInfo;
import com.base.sbc.module.pack.mapper.PackInfoMapper;
import com.base.sbc.module.pack.service.*;
import com.base.sbc.module.pack.utils.PackUtils;
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
            Map<String, List<PackInfoListVo>> piMaps = queryListToMapGroupByForeignId(sdIds);
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
        packInfo.setBomStatus(BasicNumber.ZERO.getNumber());
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
        PackBomVersionVo packBomVersionVo = packBomVersionService.saveVersion(versionDto);
        packBomVersionService.enable(BeanUtil.copyProperties(packBomVersionVo, PackBomVersion.class));
        return BeanUtil.copyProperties(getById(packInfo.getId()), PackInfoListVo.class);
    }

    @Override
    public Map<String, List<PackInfoListVo>> queryListToMapGroupByForeignId(List<String> foreignIds) {
        QueryWrapper<PackInfo> qw = new QueryWrapper<>();
        qw.in("foreign_id", foreignIds);
        List<PackInfoListVo> list = BeanUtil.copyToList(list(qw), PackInfoListVo.class);
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
        copyPack(dto.getForeignId(), dto.getPackType(), dto.getForeignId(), PackUtils.PACK_TYPE_BIG_GOODS);
        //设置为已转大货
        packInfo.setBomStatus(BasicNumber.ONE.getNumber());
        updateById(packInfo);
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
        // TODO 吊牌洗唛
        return true;
    }

    @Override
    public boolean startApproval(String id) {
        PackInfo pack = getById(id);
        if (pack == null) {
            throw new OtherException("样衣数据不存在,请先保存");
        }
        Map<String, Object> variables = BeanUtil.beanToMap(pack);
        boolean flg = flowableService.start(FlowableService.big_goods_reverse + "[" + pack.getCode() + "]", FlowableService.big_goods_reverse, id, "/pdm/api/saas/packInfo/approval", "/pdm/api/saas/packInfo/approval", "/sampleClothesDesign/sampleDesign/" + id, variables);
        if (flg) {
            pack.setConfirmStatus(BaseGlobal.STOCK_STATUS_WAIT_CHECK);
            updateById(pack);
        }
        return true;
    }

    @Override
    public boolean approval(AnswerDto dto) {
        PackInfo packInfo = getById(dto.getBusinessKey());
        if (packInfo != null) {
            //通过
            if (StrUtil.equals(dto.getApprovalType(), BaseConstant.APPROVAL_PASS)) {
                copy(dto.getBusinessKey(), PackUtils.PACK_TYPE_BIG_GOODS, dto.getBusinessKey(), PackUtils.PACK_TYPE_DESIGN);
                packInfo.setConfirmStatus(BaseGlobal.STOCK_STATUS_CHECKED);
            }
            //驳回
            else if (StrUtil.equals(dto.getApprovalType(), BaseConstant.APPROVAL_REJECT)) {
                packInfo.setConfirmStatus(BaseGlobal.STOCK_STATUS_REJECT);
            }
            updateById(packInfo);
        }
        return true;
    }

    @Override
    String getModeName() {
        return "资料包明细";
    }


// 自定义方法区 不替换的区域【other_end】

}
