/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.client.flowable.entity.AnswerDto;
import com.base.sbc.client.flowable.service.FlowableService;
import com.base.sbc.client.oauth.entity.GroupUser;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.enums.BasicNumber;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.config.utils.StyleNoImgUtils;
import com.base.sbc.module.common.eumns.UreportDownEnum;
import com.base.sbc.module.common.service.AttachmentService;
import com.base.sbc.module.common.service.UploadFileService;
import com.base.sbc.module.common.service.UreportService;
import com.base.sbc.module.common.vo.AttachmentVo;
import com.base.sbc.module.hangTag.service.HangTagService;
import com.base.sbc.module.hangTag.vo.HangTagVO;
import com.base.sbc.module.operaLog.entity.OperaLogEntity;
import com.base.sbc.module.operaLog.service.OperaLogService;
import com.base.sbc.module.pack.dto.*;
import com.base.sbc.module.pack.entity.PackBomVersion;
import com.base.sbc.module.pack.entity.PackInfo;
import com.base.sbc.module.pack.entity.PackInfoStatus;
import com.base.sbc.module.pack.entity.PackSize;
import com.base.sbc.module.pack.mapper.PackInfoMapper;
import com.base.sbc.module.pack.service.*;
import com.base.sbc.module.pack.utils.GenTechSpecPdfFile;
import com.base.sbc.module.pack.utils.PackUtils;
import com.base.sbc.module.pack.vo.*;
import com.base.sbc.module.pricing.vo.PricingVO;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.style.entity.StyleColor;
import com.base.sbc.module.style.mapper.StyleColorMapper;
import com.base.sbc.module.style.service.StyleService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.HashMap;
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
    private StyleService styleService;
    @Resource
    private AttachmentService attachmentService;
    @Resource
    private UploadFileService uploadFileService;
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
    private PackTechPackagingService packTechPackagingService;
    @Resource
    private PackSampleReviewService packSampleReviewService;
    @Resource
    private PackBusinessOpinionService packBusinessOpinionService;

    @Resource
    private PackInfoStatusService packInfoStatusService;
    @Resource
    private FlowableService flowableService;
    @Resource
    private UreportService ureportService;

    @Resource
    private StyleColorMapper styleColorMapper;


    @Resource
    private HangTagService hangTagService;

    @Override
    public PageInfo<StylePackInfoListVo> pageBySampleDesign(PackInfoSearchPageDto pageDto) {

        // 查询款式设计数据
        BaseQueryWrapper<Style> sdQw = new BaseQueryWrapper<>();
        sdQw.in("status", "1", "2");
        sdQw.notEmptyEq("prod_category1st", pageDto.getProdCategory1st());
        sdQw.notEmptyEq("prod_category", pageDto.getProdCategory());
        sdQw.notEmptyEq("prod_category2nd", pageDto.getProdCategory2nd());
        sdQw.notEmptyEq("prod_category3rd", pageDto.getProdCategory3rd());
        sdQw.notEmptyEq("planning_season_id", pageDto.getPlanningSeasonId());
        sdQw.andLike(pageDto.getSearch(), "design_no", "style_no", "style_name");
        sdQw.notEmptyEq("devt_type", pageDto.getDevtType());
        Page<Style> page = PageHelper.startPage(pageDto);
        styleService.list(sdQw);
        PageInfo<StylePackInfoListVo> pageInfo = CopyUtil.copy(page.toPageInfo(), StylePackInfoListVo.class);
        //查询bom列表
        List<StylePackInfoListVo> sdpList = pageInfo.getList();
        if (CollUtil.isNotEmpty(sdpList)) {
            //图片
            attachmentService.setListStylePic(sdpList, "stylePic");
            List<String> sdIds = sdpList.stream().map(StylePackInfoListVo::getId).collect(Collectors.toList());

            Map<String, List<PackInfoListVo>> piMaps = queryListToMapGroupByForeignId(sdIds, PackUtils.PACK_TYPE_DESIGN);
            for (StylePackInfoListVo sd : sdpList) {
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
    public PageInfo<PackInfoListVo> pageInfo(PackInfoSearchPageDto pageDto) {
        BaseQueryWrapper<PackInfo> qw = new BaseQueryWrapper<>();
        qw.notEmptyEq("foreign_id", pageDto.getStyleId());
        qw.notEmptyEq("pack_type", PackUtils.PACK_TYPE_DESIGN);
        qw.orderByDesc("id");
        Page<PackInfoListVo> objects = PageHelper.startPage(pageDto);
        List<PackInfoListVo> list = getBaseMapper().queryByQw(qw);
        attachmentService.setListStylePic(list, "stylePic");
        return objects.toPageInfo();
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean setPatternNo(PackInfoSetPatternNoDto dto) {
        PackInfo packInfo = new PackInfo();
        packInfo.setPatternNo(dto.getPatternNo());
        packInfo.setPatternMakingId(dto.getPatternMakingId());
        UpdateWrapper<PackInfo> uw = new UpdateWrapper<>();
        uw.lambda().eq(PackInfo::getId, dto.getPackId());
        return update(packInfo, uw);
    }


    @Override
    public PackInfoListVo createByStyle(CreatePackInfoByStyleDto dto) {
        Style style = styleService.getById(dto.getId());
        if (style == null) {
            throw new OtherException(BaseErrorEnum.ERR_INSERT_DATA_REPEAT);
        }
        PackInfo packInfo = BeanUtil.copyProperties(style, PackInfo.class, "id", "status");
        CommonUtils.resetCreateUpdate(packInfo);
        String newId = IdUtil.getSnowflake().nextIdStr();
        packInfo.setId(newId);
        packInfo.setForeignId(dto.getId());

        //设置编码
        QueryWrapper codeQw = new QueryWrapper();
        codeQw.eq("foreign_id", dto.getId());
        long count = getBaseMapper().countByQw(codeQw);
        packInfo.setCode(style.getDesignNo() + StrUtil.DASHED + (count + 1));
        packInfo.setName(Opt.ofBlankAble(dto.getName()).orElse(packInfo.getCode()));
        packInfo.setPatternNo(dto.getPatternNo());
        packInfo.setPatternMakingId(dto.getPatternMakingId());
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
        if (StringUtils.isAnyBlank(packInfo.getStyleNo(), packInfo.getColor(), packInfo.getStyleColorId())) {
            throw new OtherException("没有配色信息");
        }


        copyPack(dto.getForeignId(), dto.getPackType(), dto.getForeignId(), PackUtils.PACK_TYPE_BIG_GOODS);
        PackInfoStatus packDesignStatus = packInfoStatusService.get(dto.getForeignId(), PackUtils.PACK_TYPE_DESIGN);
        //设置为已转大货
        packDesignStatus.setBomStatus(BasicNumber.ONE.getNumber());
        packInfoStatusService.updateById(packDesignStatus);
        PackInfoStatus packInfoStatus = packInfoStatusService.get(dto.getForeignId(), PackUtils.PACK_TYPE_BIG_GOODS);
        //设置为已转大货
        packInfoStatus.setBomStatus(BasicNumber.ONE.getNumber());
        packInfoStatus.setDesignTechConfirm(BasicNumber.ONE.getNumber());
        packInfoStatusService.updateById(packInfoStatus);
        //updateById(packInfo);
        //设置bom 状态
        changeBomStatus(packInfo.getId(), BasicNumber.ONE.getNumber());
        return true;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void changeBomStatus(String packInfoId, String bomStatus) {
        PackInfo byId = getById(packInfoId);
        //修改配色的bom 状态
        if (byId == null) {
            return;
        }
        String styleColorId = byId.getStyleColorId();
        if (StrUtil.isBlank(styleColorId)) {
            return;
        }
        StyleColor styleColor = new StyleColor();
        styleColor.setBomStatus(bomStatus);
        UpdateWrapper<StyleColor> uw = new UpdateWrapper<>();
        uw.eq("id", styleColorId);
        styleColorMapper.update(styleColor, uw);
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
        // 工艺说明包装方式
        packTechPackagingService.copy(sourceForeignId, sourcePackType, targetForeignId, targetPackType);
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
        PackInfoStatus packInfoStatus = packInfoStatusService.get(id, PackUtils.PACK_TYPE_BIG_GOODS);
        packInfoStatus.setReverseConfirmStatus(BaseGlobal.STOCK_STATUS_WAIT_CHECK);
        packInfoStatusService.updateById(packInfoStatus);
        Map<String, Object> variables = BeanUtil.beanToMap(pack);
        boolean flg = flowableService.start(FlowableService.big_goods_reverse + "[" + pack.getCode() + "]",
                FlowableService.big_goods_reverse, id,
                "/pdm/api/saas/packInfo/reverseApproval",
                "/pdm/api/saas/packInfo/reverseApproval",
                "/pdm/api/saas/packInfo/reverseApproval",
                StrUtil.format("/styleManagement/dataPackage?id={}&styleId={}&style={}", pack.getId(), pack.getForeignId(), pack.getDesignNo()),
                variables);
        return true;
    }

    @Override
    public boolean startApproval(String id) {
        PackInfo pack = getById(id);
        if (pack == null) {
            throw new OtherException("资料包数据不存在,请先保存");
        }
        PackInfoStatus packInfoStatus = packInfoStatusService.get(id, PackUtils.PACK_TYPE_BIG_GOODS);
        packInfoStatus.setConfirmStatus(BaseGlobal.STOCK_STATUS_WAIT_CHECK);
        packInfoStatusService.updateById(packInfoStatus);
        Map<String, Object> variables = BeanUtil.beanToMap(pack);
        boolean flg = flowableService.start(FlowableService.big_goods_reverse + "[" + pack.getCode() + "]",
                FlowableService.big_goods_reverse, id,
                "/pdm/api/saas/packInfo/approval",
                "/pdm/api/saas/packInfo/approval",
                "/pdm/api/saas/packInfo/approval",
                StrUtil.format("/styleManagement/dataPackage?id={}&styleId={}&style={}", pack.getId(), pack.getForeignId(), pack.getDesignNo()),
                variables);

        return true;
    }

    @Override
    public boolean approval(AnswerDto dto) {
        PackInfo packInfo = getById(dto.getBusinessKey());
        if (packInfo != null) {
            PackInfoStatus packInfoStatus = packInfoStatusService.get(dto.getBusinessKey(), PackUtils.PACK_TYPE_BIG_GOODS);
            //通过
            if (StrUtil.equals(dto.getApprovalType(), BaseConstant.APPROVAL_PASS)) {
                packInfoStatus.setConfirmStatus(BaseGlobal.STOCK_STATUS_CHECKED);
                packInfoStatus.setPostTechConfirm(BaseGlobal.YES);
                packInfoStatus.setPostTechConfirmDate(new Date());
            }
            //驳回
            else if (StrUtil.equals(dto.getApprovalType(), BaseConstant.APPROVAL_REJECT)) {
                packInfoStatus.setConfirmStatus(BaseGlobal.STOCK_STATUS_REJECT);
                packInfoStatus.setPostTechConfirm(BaseGlobal.NO);
            } else {
                packInfoStatus.setConfirmStatus(BaseGlobal.STOCK_STATUS_DRAFT);
            }
            packInfoStatusService.updateById(packInfoStatus);
        }
        return true;
    }

    @Override
    public PageInfo<PricingSelectListVO> pricingSelectList(PricingSelectSearchDTO pricingSelectSearchDTO) {
        Page<PricingSelectListVO> voPage = PageHelper.startPage(pricingSelectSearchDTO);
        super.getBaseMapper().pricingSelectList(pricingSelectSearchDTO);
        return CopyUtil.copy(voPage.toPageInfo(), PricingSelectListVO.class);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public AttachmentVo genTechSpecFile(PackCommonSearchDto dto) {
        //获取款式设计id
        PackInfo byId = this.getById(dto.getForeignId());
        Map<String, String> params = new HashMap<>(12);
        params.put("styleId", byId.getForeignId());
        params.put("foreignId", dto.getForeignId());
        params.put("packType", dto.getPackType());
        // 下载文件并上传到minio
        AttachmentVo attachmentVo = ureportService.downFileAndUploadMinio(UreportDownEnum.PDF, "process", "工艺单", byId.getCode() + ".pdf", params);
        // 将文件id保存到状态表
        PackInfoStatus packInfoStatus = packInfoStatusService.get(dto.getForeignId(), dto.getPackType());
        packInfoStatus.setTechSpecFileId(attachmentVo.getFileId());
        packInfoStatusService.updateById(packInfoStatus);
        return attachmentVo;
    }

    @Override
    public AttachmentVo genTechSpecFile2(GroupUser groupUser, PackCommonSearchDto dto) {
        //获取款式信息
        PackInfoListVo detail = getDetail(dto.getForeignId(), dto.getPackType());

        if (detail == null) {
            throw new OtherException("获取资料包数据失败");
        }
        //获取款式信息
        Style style = styleService.getById(detail.getForeignId());
        if (style == null) {
            throw new OtherException("获取款式信息失败");
        }
        GenTechSpecPdfFile vo = new GenTechSpecPdfFile();
        // 获取吊牌信息
        if (StrUtil.isNotBlank(detail.getStyleNo())) {
            HangTagVO tag = hangTagService.getDetailsByBulkStyleNo(detail.getStyleNo(), getCompanyCode());
            if (tag != null) {
                BeanUtil.copyProperties(tag, vo);
            }
        }
        if (StrUtil.isNotBlank(vo.getStylePic())) {
            vo.setStylePic(uploadFileService.getUrlById(vo.getStylePic()));
        }

        //图片
        if (StrUtil.isNotBlank(detail.getStyleColorId())) {
            StyleColor styleColor = styleColorMapper.selectById(detail.getStyleColorId());
            String styleNoImgUrl = StyleNoImgUtils.getStyleNoImgUrl(groupUser, styleColor.getStyleColorPic());
            vo.setStylePic(styleNoImgUrl);
        }
        vo.setCompanyName("意丰歌集团有限公司");
        vo.setBrandName(style.getBrandName());
        vo.setDesignNo(style.getDesignNo());
        vo.setStyleNo(detail.getStyleNo());
        vo.setDesigner(style.getDesigner());
        vo.setProductSizes(style.getProductSizes());
        vo.setPatternDesignName(style.getPatternDesignName());
        vo.setSizeRangeName(style.getSizeRangeName());
        vo.setProdCategoryName(style.getProdCategoryName());
        PackTechSpecSearchDto techSearch = BeanUtil.copyProperties(dto, PackTechSpecSearchDto.class);
        //工艺信息
        vo.setTechSpecVoList(packTechSpecService.list(techSearch));
        //图片
        vo.setPicList(packTechSpecService.picList(techSearch));
        //尺寸表
        List<PackSize> sizeList = packSizeService.list(dto.getForeignId(), dto.getPackType());
        vo.setSizeList(BeanUtil.copyToList(sizeList, PackSizeVo.class));
        ByteArrayOutputStream gen = vo.gen();
        String fileName = Opt.ofBlankAble(detail.getStyleNo()).orElse(detail.getCode()) + ".pdf";

        try {
            MockMultipartFile mockMultipartFile = new MockMultipartFile(fileName, fileName, FileUtil.getMimeType(fileName), new ByteArrayInputStream(gen.toByteArray()));
            AttachmentVo attachmentVo = uploadFileService.uploadToMinio(mockMultipartFile);
            // 将文件id保存到状态表
            PackInfoStatus packInfoStatus = packInfoStatusService.get(dto.getForeignId(), dto.getPackType());
            packInfoStatus.setTechSpecFileId(attachmentVo.getFileId());
            packInfoStatusService.updateById(packInfoStatus);
            return attachmentVo;
        } catch (Exception e) {
            e.printStackTrace();
            throw new OtherException("生成工艺文件失败");
        }

    }

    @Override
    public boolean delTechSpecFile(PackCommonSearchDto dto) {
        UpdateWrapper qw = new UpdateWrapper();
        PackUtils.commonQw(qw, dto);
        qw.set("tech_spec_file_id", null);
        packInfoStatusService.update(qw);
        return true;
    }

    @Override
    public PricingVO getPricingVoById(String id) {
        return this.getBaseMapper().getPricingVoById(id);
    }

    /**
     * 样衣id查询bom
     *
     * @param designNo
     * @return
     */
    @Override
    public PageInfo<PackInfoListVo> getInfoListByDesignNo(String designNo) {
        if (StringUtils.isBlank(designNo)) {
            throw new OtherException("缺少设计款号");
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("design_no", designNo);
        List<PackInfoListVo> basicsdatumModelTypeList = BeanUtil.copyToList(baseMapper.selectList(queryWrapper), PackInfoListVo.class);
        List<Style> style = styleService.list(queryWrapper);
        if (!CollectionUtils.isEmpty(basicsdatumModelTypeList) && !CollectionUtils.isEmpty(style)) {
            basicsdatumModelTypeList.forEach(b -> {
                b.setStylePic(uploadFileService.getUrlById(style.get(0).getStylePic()));
            });
        }
        PageInfo<PackInfoListVo> pageInfo = new PageInfo<>(basicsdatumModelTypeList);
        return pageInfo;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean association(PackInfoAssociationDto dto) {
        PackInfo packInfo = getById(dto.getPackId());
        if (packInfo == null) {
            throw new OtherException("资料包数据为空");
        }
        StyleColor color = styleColorMapper.selectById(dto.getStyleColorId());
        if (color == null) {
            throw new OtherException("配色数据为空");
        }
        packInfo.setColor(color.getColorName());
        packInfo.setColorCode(color.getColorCode());
        packInfo.setStyleNo(color.getStyleNo());
        packInfo.setStyleColorId(dto.getStyleColorId());
        updateById(packInfo);

        color.setBom(packInfo.getCode());
        styleColorMapper.updateById(color);
        return true;
    }


    @Override

    @Transactional(rollbackFor = {Exception.class})
    public boolean reverseApproval(AnswerDto dto) {
        PackInfo packInfo = getById(dto.getBusinessKey());
        if (packInfo != null) {
            PackInfoStatus bigGoodsPs = packInfoStatusService.get(dto.getBusinessKey(), PackUtils.PACK_TYPE_BIG_GOODS);
            PackInfoStatus designPs = packInfoStatusService.get(dto.getBusinessKey(), PackUtils.PACK_TYPE_DESIGN);
            //通过
            if (StrUtil.equals(dto.getApprovalType(), BaseConstant.APPROVAL_PASS)) {
                copyPack(dto.getBusinessKey(), PackUtils.PACK_TYPE_BIG_GOODS, dto.getBusinessKey(), PackUtils.PACK_TYPE_DESIGN);
                bigGoodsPs.setReverseConfirmStatus(BaseGlobal.STOCK_STATUS_CHECKED);
                bigGoodsPs.setScmSendFlag(BaseGlobal.NO);
                // bom阶段设置为样衣阶段
                bigGoodsPs.setBomStatus(BasicNumber.ZERO.getNumber());
                designPs.setBomStatus(BasicNumber.ZERO.getNumber());
                //设置bom 状态
                changeBomStatus(packInfo.getId(), BasicNumber.ZERO.getNumber());
            }
            //驳回
            else if (StrUtil.equals(dto.getApprovalType(), BaseConstant.APPROVAL_REJECT)) {
                bigGoodsPs.setReverseConfirmStatus(BaseGlobal.STOCK_STATUS_REJECT);
            } else {
                bigGoodsPs.setConfirmStatus(BaseGlobal.STOCK_STATUS_DRAFT);
            }
            packInfoStatusService.updateById(bigGoodsPs);
            packInfoStatusService.updateById(designPs);
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
        sdQw.notEmptyEq("bom_status", pageDto.getBomStatus());
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
        List<PackInfoListVo> packInfoListVos = queryByQw(sdQw);
        attachmentService.setListStylePic(packInfoListVos, "stylePic");
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
        PackInfoListVo packInfoListVo = CollUtil.get(packInfoListVos, 0);
        if (packInfoListVo != null && StrUtil.isNotBlank(packInfoListVo.getTechSpecFileId())) {
            packInfoListVo.setTechSpecFile(attachmentService.getAttachmentByFileId(packInfoListVo.getTechSpecFileId()));
        }
        return packInfoListVo;
    }

    @Override
    String getModeName() {
        return "资料包明细";
    }


// 自定义方法区 不替换的区域【other_end】

}
