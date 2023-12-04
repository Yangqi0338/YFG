/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.hangtag.service.impl;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.StrUtil;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumModelType;
import com.base.sbc.module.basicsdatum.service.BasicsdatumModelTypeService;
import com.base.sbc.module.smp.SmpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.amc.enums.DataPermissionsBusinessTypeEnum;
import com.base.sbc.client.amc.service.DataPermissionsService;
import com.base.sbc.client.flowable.service.FlowableService;
import com.base.sbc.client.flowable.vo.FlowRecordVo;
import com.base.sbc.config.CustomStylePicUpload;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.ureport.minio.MinioUtils;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.config.utils.ExcelUtils;
import com.base.sbc.config.utils.StylePicUtils;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterial;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumSize;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialService;
import com.base.sbc.module.basicsdatum.service.BasicsdatumSizeService;
import com.base.sbc.module.common.service.UploadFileService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.hangtag.dto.HangTagDTO;
import com.base.sbc.module.hangtag.dto.HangTagSearchDTO;
import com.base.sbc.module.hangtag.dto.HangTagUpdateStatusDTO;
import com.base.sbc.module.hangtag.entity.HangTag;
import com.base.sbc.module.hangtag.entity.HangTagIngredient;
import com.base.sbc.module.hangtag.entity.HangTagLog;
import com.base.sbc.module.hangtag.enums.HangTagStatusEnum;
import com.base.sbc.module.hangtag.enums.OperationDescriptionEnum;
import com.base.sbc.module.hangtag.mapper.HangTagMapper;
import com.base.sbc.module.hangtag.service.HangTagIngredientService;
import com.base.sbc.module.hangtag.service.HangTagLogService;
import com.base.sbc.module.hangtag.service.HangTagService;
import com.base.sbc.module.hangtag.vo.HangTagListVO;
import com.base.sbc.module.hangtag.vo.HangTagVO;
import com.base.sbc.module.hangtag.vo.HangTagVoExcel;
import com.base.sbc.module.pack.entity.PackBom;
import com.base.sbc.module.pack.entity.PackInfo;
import com.base.sbc.module.pack.entity.PackInfoStatus;
import com.base.sbc.module.pack.service.PackBomService;
import com.base.sbc.module.pack.service.PackInfoService;
import com.base.sbc.module.pack.service.PackInfoStatusService;
import com.base.sbc.module.pricing.service.StylePricingService;
import com.base.sbc.module.pricing.vo.StylePricingVO;
import com.base.sbc.module.smp.entity.TagPrinting;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.style.entity.StyleColor;
import com.base.sbc.module.style.mapper.StyleColorMapper;
import com.base.sbc.module.style.service.StyleColorService;
import com.base.sbc.module.style.service.StyleService;
import com.base.sbc.open.service.EscmMaterialCompnentInspectCompanyService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;

/**
 * 类描述：吊牌表 service类
 *
 * @author xhj
 * @version 1.0
 * @address com.base.sbc.module.hangTag.service.HangTagService
 * @email ch.183.g1114@gmail.com
 * @date 创建时间：2023-6-26 17:15:53
 */
@Service
@RequiredArgsConstructor
public class HangTagServiceImpl extends BaseServiceImpl<HangTagMapper, HangTag> implements HangTagService {
	// 自定义方法区 不替换的区域【other_start】
	private static final Logger logger = LoggerFactory.getLogger(HangTagService.class);

	@Autowired
	private HangTagMapper hangTagMapper;
	@Autowired
	private StylePicUtils stylePicUtils;
	@Autowired
	private HangTagLogService hangTagLogService;
	@Autowired
	private PackInfoStatusService packInfoStatusService;
	@Autowired
	private UploadFileService uploadFileService;
	@Autowired
	private StyleColorMapper styleColorMapper;
	@Autowired
	private FlowableService flowableService;
	@Autowired
	@Lazy
	private StyleColorService styleColorService;

	@Autowired
	@Lazy
	private PackInfoService packInfoService;
	private final StylePricingService stylePricingService;
	private final StyleService styleService;
	private final BasicsdatumSizeService basicsdatumSizeService;

	private final BasicsdatumMaterialService basicsdatumMaterialService;
	private final PackBomService packBomService;
	@Autowired
	@Lazy
	private DataPermissionsService dataPermissionsService;
	private final BasicsdatumModelTypeService basicsdatumModelTypeService;


	@Autowired
	@Lazy
	private SmpService smpService;
	@Autowired
	private HangTagIngredientService hangTagIngredientService;

	private final MinioUtils minioUtils;

	@Override
	public PageInfo<HangTagListVO> queryPageInfo(HangTagSearchDTO hangTagDTO, String userCompany) {
		hangTagDTO.setCompanyCode(userCompany);
		if (hangTagDTO.getPageNum() != 0 && hangTagDTO.getPageSize() != 0) {
			PageHelper.startPage(hangTagDTO.getPageNum(), hangTagDTO.getPageSize());
		}
		String authSql = dataPermissionsService
				.getDataPermissionsSql(DataPermissionsBusinessTypeEnum.hangTagList.getK(), "tsd.", null, false);
		if (!StringUtils.isEmpty(hangTagDTO.getBulkStyleNo())) {
			hangTagDTO.setBulkStyleNos(hangTagDTO.getBulkStyleNo().split(","));
		}
		if(StrUtil.isNotBlank(hangTagDTO.getDesignNo())){
			hangTagDTO.setDesignNos(StringUtils.split(hangTagDTO.getDesignNo(),","));
		}
		List<HangTagListVO> hangTagListVOS = hangTagMapper.queryList(hangTagDTO, authSql);
		minioUtils.setObjectUrlToList(hangTagListVOS, "washingLabel");
		if (hangTagListVOS.isEmpty()) {
			return new PageInfo<>(hangTagListVOS);
		}
		/* 获取大货款号 */
		List<String> stringList = hangTagListVOS.stream().filter(h -> !StringUtils.isEmpty(h.getBulkStyleNo()))
				.map(HangTagListVO::getBulkStyleNo).distinct().collect(Collectors.toList());
		/* 查询流程审批的结果 */
		Map<String, FlowRecordVo> flowRecordVoMap;
		if ("2".equals(hangTagDTO.getCheckType())) {
			flowRecordVoMap = flowableService.getFlowRecordMapBybusinessKey(stringList);

		} else {
			flowRecordVoMap = null;
		}
		// 1A7290012
		// IdGen idGen = new IdGen();
		List<String> bulkStyleNos = new ArrayList<>();
		hangTagListVOS.forEach(e -> {
			if (flowRecordVoMap != null) {
				FlowRecordVo flowRecordVo = flowRecordVoMap.get(e.getBulkStyleNo());
				if (!ObjectUtils.isEmpty(flowRecordVo)) {
//                判断流程是否完成
					e.setExamineUserNema(flowRecordVo.getUserName());
					e.setExamineUserId(flowRecordVo.getUserId());
					if (BaseGlobal.YES.equals(flowRecordVo.getEndFlag())) {
//                    e.setConfirmDate(flowRecordVo.getEndTime());
						// e.setStatus("5"); 不需要设置为通过,通过或者不通过会在回调页面设置
					} else {
						// 状态：0.未填写，1.未提交，2.待工艺员确认，3.待技术员确认，4.待品控确认，5.已确认

						if (!"6".equals(e.getStatus())) {
							switch (flowRecordVo.getName()) {
							case "大货工艺员确认":
								e.setStatus("2");
								break;
							case "后技术确认":
								e.setStatus("3");
								break;
							case "品控确认":
								e.setStatus("4");
								break;
							default:
								break;
							}
						}

					}
				}
			}

			bulkStyleNos.add(e.getBulkStyleNo());

		});
		List<PackInfo> packInfos = packInfoService
				.list(new QueryWrapper<PackInfo>().in("style_no", bulkStyleNos).select("id", "style_no"));
		List<String> packInfoIds = packInfos.stream().map(PackInfo::getId).collect(Collectors.toList());
		if (!packInfoIds.isEmpty()) {
			List<PackInfoStatus> packInfoStatus = packInfoStatusService.list(new QueryWrapper<PackInfoStatus>()
					.in("foreign_id", packInfoIds).select("foreign_id", "bom_status"));
			Map<String, String> hashMap = new HashMap<>();
			for (PackInfo packInfo : packInfos) {
				for (PackInfoStatus infoStatus : packInfoStatus) {
					if (packInfo.getId().equals(infoStatus.getForeignId())) {
						hashMap.put(packInfo.getStyleNo(), infoStatus.getBomStatus());
					}
				}
			}
			for (HangTagListVO hangTagListVO : hangTagListVOS) {
				hangTagListVO.setBomStatus(hashMap.get(hangTagListVO.getBulkStyleNo()));
			}
		}

		return new PageInfo<>(hangTagListVOS);
	}

	/**
	 * 吊牌导出
	 *
	 * @param response
	 * @param hangTagSearchDTO
	 */
	@Override
	public void deriveExcel(HttpServletResponse response, HangTagSearchDTO hangTagSearchDTO, String userCompany)
			throws IOException {
		/* 查询吊牌数据 */
		List<HangTagListVO> list = queryPageInfo(hangTagSearchDTO, userCompany).getList();
		List<HangTagVoExcel> hangTagVoExcels = BeanUtil.copyToList(list, HangTagVoExcel.class);
		ExcelUtils.exportExcel(hangTagVoExcels, HangTagVoExcel.class, "吊牌.xlsx", new ExportParams(), response);
	}

	@Override
	public HangTagVO getDetailsByBulkStyleNo(String bulkStyleNo, String userCompany, String selectType) {
		HangTagVO hangTagVO = hangTagMapper.getDetailsByBulkStyleNo(bulkStyleNo, userCompany, selectType);
		if (hangTagVO == null) {
			throw new OtherException("大货款号:" + bulkStyleNo + " 不存在");
		}
		hangTagVO.setStylePic(stylePicUtils.getStyleUrl(hangTagVO.getStylePic()));
		hangTagVO.setStyleColorPic(stylePicUtils.getStyleUrl(hangTagVO.getStyleColorPic()));
		minioUtils.setObjectUrlToObject(hangTagVO, "washingLabel");
		if (StringUtils.isEmpty(hangTagVO.getStatus())) {
			hangTagVO.setStatus(HangTagStatusEnum.NOT_SUBMIT.getK());
		}
		// 查询检测报告
		PackInfo packInfo = packInfoService
				.getOne(new QueryWrapper<PackInfo>().eq("style_no", hangTagVO.getBulkStyleNo()));
		if (packInfo != null) {
			List<PackBom> packBomList = packBomService.list(
					new QueryWrapper<PackBom>().eq("foreign_id", packInfo.getId()));
			if (!packBomList.isEmpty()) {
				List<String> codes = packBomList.stream().map(PackBom::getMaterialCode).collect(Collectors.toList());
				if (!codes.isEmpty()) {
					List<BasicsdatumMaterial> list = basicsdatumMaterialService
							.list(new QueryWrapper<BasicsdatumMaterial>().in("material_code", codes));
					hangTagVO.setBasicsdatumMaterials(list);
				}
			}
		}
		return hangTagVO;
	}

	@Override
	@Transactional(rollbackFor = { Exception.class })
	public String save(HangTagDTO hangTagDTO, String userCompany) {
		//判断大货款号是否存在
		if (StringUtils.isEmpty(hangTagDTO.getId())) {
			long count = this.count(new QueryWrapper<HangTag>().eq("bulk_style_no", hangTagDTO.getBulkStyleNo()).eq("company_code", userCompany));
			if (count > 0) {
				throw new OtherException("大货款号已存在,请勿重复添加");
			}
		}


		logger.info("HangTagService#save 保存吊牌 hangTagDTO:{}, userCompany:{}", JSON.toJSONString(hangTagDTO),
				userCompany);
		HangTag hangTag = new HangTag();
		CommonUtils.removeQuery(hangTagDTO, "washingLabel");

		BeanUtil.copyProperties(hangTagDTO, hangTag);
		//如果品名修改则下发scm
		StyleColor styleColor = styleColorService.getOne(new QueryWrapper<StyleColor>().eq
				("style_no", hangTag.getBulkStyleNo()).eq("company_code", userCompany).select("id"));


		boolean flag = false;
		if(StringUtils.isEmpty(hangTagDTO.getId())){
			flag = true;
		}else {
			HangTag hangTag1 = this.getById(hangTagDTO.getId());
			if(!hangTag1.getProductName().equals(hangTag.getProductName())){
				flag = true;
			}
		}

		super.saveOrUpdate(hangTag, "吊牌管理");

		if (flag){
			smpService.goods(styleColor.getId().split(","));
		}

		String id = hangTag.getId();






		// List<BasicsdatumMaterialIngredient> materialIngredientList =
		// basicsdatumMaterialController.formatToList(hangTagDTO.getIngredient(), "0",
		// "");

		// List<HangTagIngredientDTO> hangTagIngredients = new ArrayList<>();
		// for (BasicsdatumMaterialIngredient basicsdatumMaterialIngredient :
		// materialIngredientList) {
		// HangTagIngredientDTO hangTagIngredient = new HangTagIngredientDTO();
		// hangTagIngredient.setPercentage(basicsdatumMaterialIngredient.getRatio());
		// hangTagIngredient.setType(basicsdatumMaterialIngredient.getName());
		// hangTagIngredient.setTypeCode(basicsdatumMaterialIngredient.getType());
		// hangTagIngredient.setDescriptionRemarks(basicsdatumMaterialIngredient.getSay());
		// hangTagIngredients.add(hangTagIngredient);
		// }
		// hangTagIngredientService.remove(new
		// QueryWrapper<HangTagIngredient>().eq("hang_tag_id", id));
		// hangTagIngredientService.save(hangTagIngredients, id, userCompany);
		hangTagLogService.save(id, OperationDescriptionEnum.SAVE.getV(), userCompany);

		/**
		 * 当存在品名时同步到配色
		 */
		if (!StringUtils.isEmpty(hangTag.getProductCode()) && !StringUtils.isEmpty(hangTag.getProductName())) {
			/* 同步配色品名 */
			if (!ObjectUtils.isEmpty(styleColor)) {
				styleColor.setProductCode(hangTag.getProductCode());
				styleColor.setProductName(hangTag.getProductName());
				styleColorMapper.updateById(styleColor);
			}
		}

		if ("2".equals(hangTag.getStatus()) && "2".equals(hangTagDTO.getCheckType())) {
			hangTag = this.getById(hangTag.getId());
			// 发起审批
			flowableService.start(FlowableService.HANGING_TAG_REVIEW + hangTag.getBulkStyleNo(),
					FlowableService.HANGING_TAG_REVIEW, hangTag.getBulkStyleNo(), "/pdm/api/saas/hangTag/toExamine",
					"/pdm/api/saas/hangTag/toExamine", "/pdm/api/saas/hangTag/toExamine", null,
					BeanUtil.beanToMap(hangTag));
		}
		// 如果提交审核默认通过第一个审核
		if ("2".equals(hangTag.getStatus()) && !"2".equals(hangTagDTO.getCheckType())) {
			hangTag.setStatus("3");
			this.updateById(hangTag);
		}
		try {
			//下发成分
			smpService.sendTageComposition(Collections.singletonList(id));
		}catch (Exception ignored){
		}
        return id;
	}

	@Override
	public void updateStatus(HangTagUpdateStatusDTO hangTagUpdateStatusDTO, String userCompany) {
		logger.info("HangTagService#updateStatus 更新状态 hangTagUpdateStatusDTO:{}, userCompany:{}",
				JSON.toJSONString(hangTagUpdateStatusDTO), userCompany);
		LambdaQueryWrapper<HangTag> queryWrapper = new QueryWrapper<HangTag>().lambda().in(HangTag::getId,
				hangTagUpdateStatusDTO.getIds());
		// .eq(HangTag::getCompanyCode, userCompany);
		List<HangTag> hangTags = super.list(queryWrapper);
		if (CollectionUtils.isEmpty(hangTags)) {
			throw new OtherException("存在未填写数据，请先填写");
		}
		ArrayList<HangTag> updateHangTags = Lists.newArrayList();
		hangTags.forEach(e -> {
			if (!HangTagStatusEnum.NOT_SUBMIT.getK().equals(hangTagUpdateStatusDTO.getStatus())) {
				if (HangTagStatusEnum.CONFIRMED.getK().equals(e.getStatus())) {
					throw new OtherException("存在已通过审核数据，请反审");
				}
				if (HangTagStatusEnum.NOT_SUBMIT.getK().equals(e.getStatus())
						&& !HangTagStatusEnum.TO_TECHNICIANS_CONFIRMED.getK()
								.equals(hangTagUpdateStatusDTO.getStatus())) {
					throw new OtherException("存在待提交数据，请先提交");
				}

				if (HangTagStatusEnum.TO_TECHNICIANS_CONFIRMED.getK().equals(e.getStatus())
						&& !HangTagStatusEnum.TO_TECHNOLOGIST_CONFIRMED.getK()
								.equals(hangTagUpdateStatusDTO.getStatus())) {
					throw new OtherException("存在待工艺员确认数据，请先待工艺员确认");
				}
				// if (HangTagStatusEnum.TO_TECHNICIANS_CONFIRMED.getK().equals(e.getStatus())
				// &&
				// !HangTagStatusEnum.TO_TECHNOLOGIST_CONFIRMED.getK().equals(hangTagUpdateStatusDTO.getStatus()))
				// {
				// throw new OtherException("已提交审核,请等待");
				// }

				if (HangTagStatusEnum.TO_TECHNOLOGIST_CONFIRMED.getK().equals(e.getStatus())
						&& !HangTagStatusEnum.TO_QUALITY_CONTROL_CONFIRMED.getK()
								.equals(hangTagUpdateStatusDTO.getStatus())) {
					throw new OtherException("存在待技术员确认数据，请先技术员确认");
				}
				if (HangTagStatusEnum.TO_QUALITY_CONTROL_CONFIRMED.getK().equals(e.getStatus())
						&& !HangTagStatusEnum.CONFIRMED.getK().equals(hangTagUpdateStatusDTO.getStatus())) {
					throw new OtherException("存在待品控确认数据，请先品控确认");
				}
			}
			HangTag hangTag = new HangTag();
			hangTag.setId(e.getId());
			hangTag.updateInit();
			hangTag.setStatus(hangTagUpdateStatusDTO.getStatus());
			if (HangTagStatusEnum.CONFIRMED.getK().equals(hangTagUpdateStatusDTO.getStatus())) {
				hangTag.setConfirmDate(new Date());
			}
			if (HangTagStatusEnum.TO_TECHNICIANS_CONFIRMED.getK().equals(hangTagUpdateStatusDTO.getStatus())) {
				hangTag.setConfirmDate(null);
			}
			updateHangTags.add(hangTag);

		});
		super.updateBatchById(updateHangTags);
		hangTagLogService.saveBatch(hangTagUpdateStatusDTO.getIds(),
				OperationDescriptionEnum.getV(hangTagUpdateStatusDTO.getStatus()), userCompany);

		if ("2".equals(hangTagUpdateStatusDTO.getCheckType())) {
			// 发送审批
			List<HangTag> hangTags1 = this.listByIds(hangTagUpdateStatusDTO.getIds());
			for (HangTag tag : hangTags1) {
				flowableService.start(FlowableService.HANGING_TAG_REVIEW + tag.getBulkStyleNo(),
						FlowableService.HANGING_TAG_REVIEW, tag.getBulkStyleNo(), "/pdm/api/saas/hangTag/toExamine",
						"/pdm/api/saas/hangTag/toExamine", "/pdm/api/saas/hangTag/toExamine", null,
						BeanUtil.beanToMap(tag));

			}
		}

	}

	@Override
	public List<TagPrinting> hangTagPrinting(String styleNo, boolean likeQueryFlag) {
		BaseQueryWrapper<HangTag> baseQueryWrapper = new BaseQueryWrapper<>();
		List<TagPrinting> tagPrintings = new ArrayList<>();
		if (likeQueryFlag) {
			baseQueryWrapper.notEmptyEq("bulk_style_no", styleNo);
		} else {
			baseQueryWrapper.notEmptyLike("bulk_style_no", styleNo);
		}
		// 吊牌只查询非历史迁移数据
		baseQueryWrapper.ne("historical_data", "1");

		List<HangTag> list = this.list(baseQueryWrapper);
		if (!list.isEmpty()) {
			for (HangTag hangTag : list) {
				// 配色
				StyleColor styleColor = styleColorService
						.getOne(new QueryWrapper<StyleColor>().eq("style_no", hangTag.getBulkStyleNo()));
				// TODO: styleColor可能为null 这里进行容错判断
				Style style = styleService.getById(styleColor != null ? styleColor.getStyleId() : " ");

				TagPrinting tagPrinting = new TagPrinting();
				// 是否赠品
				tagPrinting.setIsGift(null);
				// 批次
				tagPrinting.setC8_Colorway_BatchNo(null);
				if (styleColor != null) {
					// 唯一码
					tagPrinting.setC8_Colorway_WareCode(styleColor.getWareCode());
					// 颜色名称
					tagPrinting.setColorDescription(styleColor.getColorName());
					// 颜色编码
					tagPrinting.setColorCode(styleColor.getColorCode());
				}

				// 大货款号
				tagPrinting.setStyleCode(hangTag.getBulkStyleNo());

				PackInfo packInfo = packInfoService.getOne(
						new QueryWrapper<PackInfo>().eq("style_no",
								styleColor != null ? styleColor.getStyleNo() : " "));
				if (packInfo != null) {
					// 款式定价
					StylePricingVO stylePricingVO = stylePricingService.getByPackId(packInfo.getId(),
							styleColor.getCompanyCode());
					if (stylePricingVO != null) {
						// 商品吊牌价确认
						tagPrinting.setMerchApproved("1".equals(stylePricingVO.getProductTagPriceConfirm()));
						// 系列
						tagPrinting.setC8_Colorway_Series(stylePricingVO.getSeries());

					}
				}
				if (styleColor != null) {

					// 配饰款号
					tagPrinting.setSecCode(styleColor.getAccessoryNo());
					// 主款款号
					tagPrinting.setMainCode(styleColor.getPrincipalStyleNo());
					// 吊牌价
					tagPrinting.setC8_Colorway_SalesPrice(styleColor.getTagPrice());
					// 是否内配饰
					tagPrinting.setIsAccessories(!StringUtils.isEmpty(styleColor.getAccessoryNo()));
					// 大货款号是否激活
					tagPrinting.setActive("0".equals(styleColor.getStatus()));
					// 销售类型
					tagPrinting.setC8_Colorway_SaleType(styleColor.getSalesType());
				}
				if (style != null) {

					// 品牌_描述
					tagPrinting.setC8_Season_Brand(style.getBrandName());
					// 品类id
					tagPrinting.setC8_Collection_ProdCategory(style.getProdCategory());
					// 主题
					tagPrinting.setTheme(style.getSubject());
					// 小类编码
					tagPrinting.setC8_Style_3rdCategory(style.getProdCategory3rd());
					// 中类编码
					tagPrinting.setC8_Style_2ndCategory(style.getProdCategory2nd());
					// 尺码号型编码
					tagPrinting.setSizeRangeCode(style.getSizeRange());
					// 尺码号型名称
					tagPrinting.setSizeRangeName(style.getSizeRangeName());
					// 款式分类
					tagPrinting.setProductType(style.getStyleName());
					// 大类
					tagPrinting.setC8_1stProdCategory(style.getProdCategory1stName());
					// 尺码号型:分类
					BaseQueryWrapper<BasicsdatumModelType> queryWrapper = new BaseQueryWrapper<>();
					queryWrapper.eq("code", style.getSizeRange());
					BasicsdatumModelType basicsdatumModelType = basicsdatumModelTypeService.getOne(queryWrapper);
					if ( basicsdatumModelType != null) {
                        switch (basicsdatumModelType.getDimensionType()) {
                            case "规格":
								tagPrinting.setSizeRangeDimensionType("Spec");
								break;
							case "特殊规格":
								tagPrinting.setSizeRangeDimensionType("SpecialSpec");
								break;
							case "门幅":
								tagPrinting.setSizeRangeDimensionType("Width");
								break;
							case "无规格":
								tagPrinting.setSizeRangeDimensionType("NonSpec");
								break;
							case "其他":
								tagPrinting.setSizeRangeDimensionType("Other");
								break;
                            case "号型":
                            default:
								tagPrinting.setSizeRangeDimensionType("Size");
								break;
                        }
					}
				}


				// 成分
				tagPrinting.setComposition(hangTag.getIngredient());
				// 洗标
//				tagPrinting.setCareSymbols(hangTag.getWashingLabelName());
				tagPrinting.setCareSymbols(hangTag.getWashingCode());
				// 质量等级
				tagPrinting.setQualityClass(hangTag.getQualityGrade());
				// 品名
				tagPrinting.setProductName(hangTag.getProductName());
				// 安全类别
				tagPrinting.setSaftyType(hangTag.getSaftyType());
				// 执行标准
				tagPrinting.setOPStandard(hangTag.getExecuteStandard());
				// 品控部确认
				tagPrinting.setApproved("5".equals(hangTag.getStatus()));
				// 温馨提示
				tagPrinting.setAttention(hangTag.getWarmTips());
				// 后技术确认
				tagPrinting
						.setTechApproved(Integer.parseInt(hangTag.getStatus()) > 2 && !"6".equals(hangTag.getStatus()));
				// 安全标题
				tagPrinting.setSaftyTitle(hangTag.getSaftyTitle());
				// 洗唛材质备注
				tagPrinting.setC8_APPBOM_Comment(hangTag.getWashingMaterialRemarksName());
				// 贮藏要求
				tagPrinting.setC8_APPBOM_StorageReq(hangTag.getStorageDemandName());
				// 产地
				tagPrinting.setC8_APPBOM_MadeIn(hangTag.getProducer());
				// 入库时间
				tagPrinting.setC8_APPBOM_StorageTime(null);
				// 英文成分
				tagPrinting.setCompsitionMix(null);
				// 英文温馨提示
				tagPrinting.setWarmPointEN(null);
				// 英文贮藏要求
				tagPrinting.setStorageReqEN(null);
				List<TagPrinting.Size> size = new ArrayList<>();

				String sizeIds = style.getSizeIds();
				if (!StringUtils.isEmpty(sizeIds)) {
					for (BasicsdatumSize basicsdatumSize : basicsdatumSizeService
							.listByIds(Arrays.asList(sizeIds.split(",")))) {
						TagPrinting.Size size1 = new TagPrinting.Size();
						size1.setSIZECODE(basicsdatumSize.getInternalSize());
						size1.setSORTCODE(basicsdatumSize.getSort());
						size1.setSIZENAME(basicsdatumSize.getModel());
						size1.setSizeID(basicsdatumSize.getCode());
						size1.setEXTSIZECODE(basicsdatumSize.getExtSizeCode());
						size1.setShowIntSize("1".equals(basicsdatumSize.getHangTagShowSizeStatus()));
						size1.setEuropeCode(basicsdatumSize.getEuropeanSize());
						String downContent = hangTag.getDownContent();
						if (!StringUtils.isEmpty(downContent)) {
							for (String s : downContent.split("\n")) {
								if (!StringUtils.isEmpty(s)) {
									String[] split = s.split(":");
									if (split.length > 1) {
										if (split[0].equals(size1.getSIZENAME()+"("+size1.getSIZECODE()+")")) {
											size1.setSKUFiller(split[1]);
										}
									}
								}
							}

						}
						String specialSpec = hangTag.getSpecialSpec();
						if (!StringUtils.isEmpty(specialSpec)) {
							for (String s : specialSpec.split("\n")) {
								if (!StringUtils.isEmpty(s)) {
									String[] split = s.split(":");
									if (split.length > 1) {
										if (split[0].equals(size1.getSIZENAME()+"("+size1.getSIZECODE()+")")) {
											size1.setSpecialSpec(split[1]);
										}
									}
								}
							}

						}
						size.add(size1);
					}

				}
				// 款式尺码明细
				tagPrinting.setSize(size);

				tagPrintings.add(tagPrinting);
			}
		}

		// 改变吊牌打印状态
		list.forEach(e -> {
			e.setPrintOrNot("1");
		});
		this.updateBatchById(list);

		return tagPrintings;
	}

	@Override
	public String getTechSpecFileByStyleNo(String styleNo) {
		String techSpecFileId = packInfoStatusService.getTechSpecFileIdByStyleNo(styleNo);
		if (StringUtils.isEmpty(techSpecFileId)) {
			return null;
		}
		String url = uploadFileService.getUrlById(techSpecFileId);
		return minioUtils.getObjectUrl(url);
	}

	/**
	 * 复制吊牌
	 *
	 * @param styleNo
	 * @param newStyleNo
	 * @return
	 */
	@Override
	public Boolean copyPack(String styleNo, String newStyleNo) {
		QueryWrapper queryWrapper = new QueryWrapper();
		queryWrapper.eq("bulk_style_no", styleNo);
		HangTag hangTag = baseMapper.selectOne(queryWrapper);
		if (!ObjectUtils.isEmpty(hangTag)) {
			/* 存在吊牌时 复制吊牌 */
			hangTag.setId(null);
			hangTag.setBulkStyleNo(newStyleNo);
			hangTag.setStatus(BaseGlobal.STATUS_CLOSE);
			save(hangTag);
			/* 查询成分 */
			queryWrapper.clear();
			queryWrapper.eq("hang_tag_id", hangTag.getId());
			List<HangTagIngredient> hangTagIngredientList = hangTagIngredientService.list(queryWrapper);
			/* 复制成分 */
			hangTagIngredientList.forEach(i -> {
				i.setId(null);
				i.setHangTagId(hangTag.getId());
			});
			hangTagIngredientService.saveBatch(hangTagIngredientList);
			HangTagLog hangTagLog = new HangTagLog();
			hangTagLog.setHangTagId(hangTag.getId());
			hangTagLog.setOperationDescription("报错款复制");
			hangTagLogService.save(hangTagLog);
		}
		return true;
	}

// 自定义方法区 不替换的区域【other_end】

}
