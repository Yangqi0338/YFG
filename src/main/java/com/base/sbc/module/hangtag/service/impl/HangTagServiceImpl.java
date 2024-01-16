/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.hangtag.service.impl;
import java.util.Date;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.map.MapUtil;
import com.base.sbc.config.common.BaseLambdaQueryWrapper;
import com.base.sbc.config.enums.business.CountryLanguageType;

import java.io.IOException;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.enums.business.StandardColumnModel;
import com.base.sbc.config.enums.business.SystemSource;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumModelType;
import com.base.sbc.module.basicsdatum.service.BasicsdatumModelTypeService;
import com.base.sbc.module.hangtag.enums.HangTagDeliverySCMStatusEnum;
import com.base.sbc.module.basicsdatum.service.SizeBulkStyleService;
import com.base.sbc.module.hangtag.dto.HangTagMoreLanguageCheckDTO;
import com.base.sbc.module.hangtag.dto.HangTagMoreLanguageDTO;
import com.base.sbc.module.hangtag.vo.HangTagMoreLanguageBCSVO;
import com.base.sbc.module.hangtag.vo.HangTagMoreLanguageBaseVO;
import com.base.sbc.module.hangtag.vo.HangTagMoreLanguageVO;
import com.base.sbc.module.hangtag.vo.HangTagMoreLanguageWebBaseVO;
import com.base.sbc.module.hangtag.vo.MoreLanguageHangTagVO;
import com.base.sbc.module.hangtag.vo.MoreLanguageHangTagVO.HangTagMoreLanguageGroup;
import com.base.sbc.module.hangtag.vo.MoreLanguageHangTagVO.MoreLanguageCodeMapping;
import com.base.sbc.module.moreLanguage.dto.CountryLanguageDto;
import com.base.sbc.module.moreLanguage.dto.CountryQueryDto;
import com.base.sbc.module.moreLanguage.entity.CountryLanguage;
import com.base.sbc.module.moreLanguage.entity.StandardColumnCountryTranslate;
import com.base.sbc.module.moreLanguage.service.CountryLanguageService;
import com.base.sbc.module.moreLanguage.service.StandardColumnCountryRelationService;
import com.base.sbc.module.moreLanguage.service.StandardColumnCountryTranslateService;
import com.base.sbc.module.smp.SmpService;
import com.base.sbc.module.smp.entity.TagPrinting;
import com.base.sbc.module.style.entity.StyleMainAccessories;
import com.base.sbc.module.style.service.StyleMainAccessoriesService;
import com.base.sbc.module.standard.entity.StandardColumn;
import com.base.sbc.module.standard.service.StandardColumnService;
import com.base.sbc.open.dto.MoreLanguageTagPrinting;
import com.base.sbc.open.dto.MoreLanguageTagPrintingList;
import com.base.sbc.open.dto.TagPrintingSupportVO;
import com.base.sbc.open.dto.TagPrintingSupportVO.CodeMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.method.P;
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
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.style.entity.StyleColor;
import com.base.sbc.module.style.mapper.StyleColorMapper;
import com.base.sbc.module.style.service.StyleColorService;
import com.base.sbc.module.style.service.StyleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;

import static com.base.sbc.config.constant.Constants.COMMA;
import static com.base.sbc.module.common.convert.ConvertContext.HANG_TAG_CV;

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

	private final StyleMainAccessoriesService styleMainAccessoriesService;

	@Autowired
	private CountryLanguageService countryLanguageService;

	@Autowired
	private StandardColumnService standardColumnService;

	@Autowired
	private StandardColumnCountryRelationService standardColumnCountryRelationService;

	@Autowired
	private StandardColumnCountryTranslateService standardColumnCountryTranslateService;

	@Autowired
	private SizeBulkStyleService sizeBulkStyleService;

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

		if(StrUtil.isNotBlank(hangTagDTO.getProductCode())){
			hangTagDTO.setProductCodes(StringUtils.split(hangTagDTO.getProductCode(),","));
		}

		if(StrUtil.isNotBlank(hangTagDTO.getProdCategory())){
			hangTagDTO.setProdCategorys(StringUtils.split(hangTagDTO.getProdCategory(),","));
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
		HangTagVO hangTagVO = hangTagMapper.getDetailsByBulkStyleNo(Collections.singletonList(bulkStyleNo), userCompany, selectType).stream().findFirst().orElse(null);
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
	/*	StyleColor styleColor = styleColorService.getOne(new QueryWrapper<StyleColor>().eq
				("style_no", hangTag.getBulkStyleNo()).eq("company_code", userCompany).select("id"));
*/

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
		String id = hangTag.getId();

		// 成分检查
		if (!"0".equals(hangTagDTO.getStatus()) && !"1".equals(hangTagDTO.getStatus())) {
			strictCheckIngredientPercentage(Collections.singletonList(id));
		}

	/*	if (flag){
			smpService.goods(styleColor.getId().split(","));
		}*/

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
			StyleColor styleColor = styleColorService.getByOne("style_no",hangTag.getBulkStyleNo());
			/* 同步配色品名 */
			if (!ObjectUtils.isEmpty(styleColor)) {
				styleColor.setProductCode(hangTag.getProductCode());
				styleColor.setProductName(hangTag.getProductName());
				styleColorMapper.updateById(styleColor);
				/*修改品名是下发配色前提配色已下发*/
				if(StrUtil.equals(styleColor.getScmSendFlag(),BaseGlobal.YES) || StrUtil.equals(styleColor.getScmSendFlag(),BaseGlobal.IN_READY)){
					smpService.goods(styleColor.getId().split(","));
				}
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

			//region 2023-12-06 吊牌保存需要修改工艺员确认状态
			smpService.tagConfirmDates(Collections.singletonList(id), HangTagDeliverySCMStatusEnum.TECHNOLOGIST_CONFIRM.getCode(), 1);
			//endregion

		}catch (Exception ignored){
		}
        return id;
	}

	private void strictCheckIngredientPercentage(List<String> hangTagIdList){
		List<HangTagIngredient> hangTagIngredientList = hangTagIngredientService.list(new LambdaQueryWrapper<HangTagIngredient>()
				.in(HangTagIngredient::getHangTagId, hangTagIdList)
				.eq(HangTagIngredient::getStrictCheck, YesOrNoEnum.YES.getValueStr())
		);
		if (CollectionUtil.isEmpty(hangTagIngredientList)) return;

		hangTagIngredientList.stream().filter(it-> !it.checkPercentageRequired() && !it.checkDescriptionRemarks()).forEach(it-> {
			throw new OtherException("开启成分信息校验后,(材料类型-百分比-成分名称)和(成分说明)必须二选一进行填写");
		});

		hangTagIngredientList.stream().filter(HangTagIngredient::checkPercentageRequired)
				.collect(Collectors.groupingBy(HangTagIngredient::getHangTagId)).forEach((hangTagId, sameHangTagIdList)-> {
					sameHangTagIdList.stream().collect(Collectors.groupingBy(it-> it.getTypeCode() + "-" + it.getIngredientSecondCode())).forEach((code, list)-> {
						StringJoiner stringJoiner = new StringJoiner("-");
						String type = list.get(0).getType();
						stringJoiner.add(type);
						String ingredientSecondName = list.get(0).getIngredientSecondName();
						if (StrUtil.isNotBlank(ingredientSecondName)) {
							stringJoiner.add(ingredientSecondName);
						}
						if (list.stream().mapToDouble(it-> it.getPercentage().doubleValue()).sum() != 100.0) {
                            throw new OtherException(stringJoiner +"百分比相加不是100,未通过校验");
                        }
					});
		});
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
		// 检查
		strictCheckIngredientPercentage(hangTags.stream().map(HangTag::getId).collect(Collectors.toList()));

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

		String status = hangTagUpdateStatusDTO.getStatus();
		int type;
		switch (status) {
			case "3":
				type = 1;
				break;
			case "4":
				type = 2;
				break;
			case "5":
				type = 3;
				break;
			default:
				type = 3;
		}

		smpService.tagConfirmDates(hangTagUpdateStatusDTO.getIds(),type,1);
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

		if (likeQueryFlag) {
			baseQueryWrapper.notEmptyEq("bulk_style_no", styleNo);
		} else {
			baseQueryWrapper.notEmptyLike("bulk_style_no", styleNo);
		}
		// 吊牌只查询非历史迁移数据
		baseQueryWrapper.ne("historical_data", "1");

		List<HangTag> list = this.list(baseQueryWrapper);

		List<TagPrinting> tagPrintings = hangTagPrinting(list);

		// 改变吊牌打印状态
		list.forEach(e -> {
			e.setPrintOrNot("1");
		});
		this.updateBatchById(list);

		return tagPrintings;
	}

	private <T extends HangTag> List<TagPrinting> hangTagPrinting(List<T> list) {
		List<TagPrinting> tagPrintings = new ArrayList<>();
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

// 是否内配饰
					tagPrinting.setIsAccessories("1".equals(styleColor.getIsTrim()));
					List<StyleMainAccessories> styleMainAccessories = styleMainAccessoriesService.list(new QueryWrapper<StyleMainAccessories>().eq("style_color_id", styleColor.getId()));
					if (!styleMainAccessories.isEmpty()){
						List<String> collect = styleMainAccessories.stream().map(StyleMainAccessories::getStyleNo).collect(Collectors.toList());
						if ("1".equals(styleColor.getIsTrim())){
							// 主款款号
							tagPrinting.setMainCode(String.join(",",collect));
						}else {
							// 配饰款号
							tagPrinting.setSecCode(String.join(",",collect));
						}
					}

					// 吊牌价
					tagPrinting.setC8_Colorway_SalesPrice(styleColor.getTagPrice());

					// 大货款号是否激活
					tagPrinting.setActive("0".equals(styleColor.getStatus()));
					// 销售类型
					tagPrinting.setC8_Colorway_SaleType(styleColor.getSalesTypeName());
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
					tagPrinting.setProductType(style.getStyleTypeName());
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
				tagPrinting.setC8_APPBOM_StorageTime(hangTag.getProduceDate());
				// 英文成分
				tagPrinting.setCompsitionMix(null);
				// 英文温馨提示
				tagPrinting.setWarmPointEN(null);
				// 英文贮藏要求
				tagPrinting.setStorageReqEN(null);
				List<TagPrinting.Size> size = new ArrayList<>();
				if (style!= null) {
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
										String[] split = s.split(":",2);
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
										String[] split = s.split(":",2);
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
		this.updateBatchById(BeanUtil.copyToList(list, HangTag.class));

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

	@Override
	public Object getMoreLanguageDetailsByBulkStyleNo(HangTagMoreLanguageDTO hangTagMoreLanguageDTO, boolean needHandle, boolean mergeWarnMsg) {
		List<String> codeList = Arrays.asList(hangTagMoreLanguageDTO.getCode().split(","));

		// 多国家语言 多款号
		Boolean likeQueryFlag = hangTagMoreLanguageDTO.getLikeQueryFlag();
		// 模糊查询
		List<String> bulkStyleNoList;
		if (Boolean.TRUE.equals(likeQueryFlag)) {
			// 模糊查询 TODO
			bulkStyleNoList = new ArrayList<>();
		}else {
			bulkStyleNoList = Arrays.asList(hangTagMoreLanguageDTO.getBulkStyleNo().split(","));
		}
		SystemSource source = hangTagMoreLanguageDTO.getSource();

		// 查询国家语言
		CountryQueryDto countryQueryDto = HANG_TAG_CV.copy2CountryQuery(hangTagMoreLanguageDTO);
		List<CountryLanguageDto> countryLanguageList = countryLanguageService.listQuery(countryQueryDto);
		if (CollectionUtil.isEmpty(countryLanguageList)) throw new OtherException("未查询到国家语言");

		// 获取全局 吊牌类型、国家语言Id、语言编码
		List<CountryLanguageType> typeList = countryLanguageList.stream().map(CountryLanguageDto::getType).distinct().collect(Collectors.toList());
		List<String> countryLanguageIdList = countryLanguageList.stream().map(CountryLanguageDto::getId).collect(Collectors.toList());
		List<String> languageCodeList = countryLanguageList.stream().map(CountryLanguageDto::getLanguageCode).distinct().collect(Collectors.toList());

		// 获取所有的吊牌
		List<MoreLanguageHangTagVO> hangTagVOList = HANG_TAG_CV.copyList2MoreLanguage(hangTagMapper.getDetailsByBulkStyleNo(
				bulkStyleNoList, hangTagMoreLanguageDTO.getUserCompany(), hangTagMoreLanguageDTO.getSelectType()
		));

		// 获取吊牌成分
		List<HangTagIngredient> allIngredientList = hangTagIngredientService.list(new BaseLambdaQueryWrapper<HangTagIngredient>()
				.notEmptyIn(HangTagIngredient::getHangTagId, hangTagVOList.stream().map(HangTagVO::getId).collect(Collectors.toList())));

		// 获取吊牌的modelType
		List<String> sizeCodeList  = hangTagVOList.stream().map(HangTagVO::getModelType).filter(StrUtil::isNotBlank).distinct().collect(Collectors.toList());
		List<BasicsdatumModelType> modelTypeList = basicsdatumModelTypeService.list(new BaseLambdaQueryWrapper<BasicsdatumModelType>().in(BasicsdatumModelType::getCode,sizeCodeList));

		// 获得要翻译的标准列码集合
		Map<String, List<StandardColumn>> standardColumnMap = new HashMap<>(codeList.size());
		codeList.forEach(code-> {
			List<StandardColumn> standardColumnList = standardColumnMap.getOrDefault(code, new ArrayList<>());
			typeList.forEach(type-> {
				standardColumnList.addAll(countryLanguageService.findStandardColumnList(code, type, false));
			});
			standardColumnMap.put(code, standardColumnList);
		});

		List<String> standardColumnCodeList = standardColumnMap.values().stream().flatMap(it -> it.stream().map(StandardColumn::getCode)).distinct().collect(Collectors.toList());

		// 先查询表头 可以去查另一个表 StandardColumnTranslate
		List<StandardColumnCountryTranslate> titleTranslateList = standardColumnCountryTranslateService.list(new LambdaQueryWrapper<StandardColumnCountryTranslate>()
				.in(StandardColumnCountryTranslate::getCountryLanguageId, countryLanguageIdList)
				.in(StandardColumnCountryTranslate::getPropertiesCode,standardColumnCodeList)
		);

		CountryQueryDto languageDto = new CountryQueryDto();
		languageDto.setLanguageCode(String.join(COMMA, languageCodeList));
		languageDto.setSingleLanguageFlag(YesOrNoEnum.YES);
		List<CountryLanguageDto> singleLanguageDtoList = countryLanguageService.listQuery(languageDto);
		// 如果为空, 查找单语言翻译
		if(CollectionUtil.isEmpty(titleTranslateList)) {
			titleTranslateList.addAll(standardColumnCountryTranslateService.list(new LambdaQueryWrapper<StandardColumnCountryTranslate>()
					.in(StandardColumnCountryTranslate::getCountryLanguageId, singleLanguageDtoList.stream().map(CountryLanguageDto::getId).collect(Collectors.toList()))
					.in(StandardColumnCountryTranslate::getPropertiesCode,standardColumnCodeList)
			));
		}

		List<HangTagMoreLanguageBaseVO> resultList = new ArrayList<>();
		List<String> mergeWarnMsgList = new ArrayList<>();

		// 根据国家分组
		countryLanguageList.stream().collect(Collectors.groupingBy(CountryLanguage::getCode)).forEach((code, sameCodeList)-> {
			// 封装基础VO
			HangTagMoreLanguageBaseVO baseCountryLanguageVO = HANG_TAG_CV.copy2MoreLanguageBaseVO(sameCodeList.get(0));

			// 根据传入的检查Bean, 确定这个国家需要检查的吊牌 和 语言
			List<String> countryMappingBulkStyleNoList = bulkStyleNoList;
			List<HangTagMoreLanguageCheckDTO> codeMappingList = hangTagMoreLanguageDTO.getHangTagMoreLanguageCheckDTOList();
			if (CollectionUtil.isNotEmpty(codeMappingList)) {
				List<HangTagMoreLanguageCheckDTO> mappingList = codeMappingList.stream().filter(it -> it.getCode().equals(code)).collect(Collectors.toList());
				countryMappingBulkStyleNoList = mappingList.stream().flatMap(it-> Arrays.stream(it.getBulkStyleNo().split(","))).collect(Collectors.toList());
				sameCodeList = sameCodeList.stream().filter(it-> mappingList.stream().anyMatch(codeMapping->
						it.getType().equals(codeMapping.getType()) &&
								(StrUtil.isBlank(codeMapping.getLanguageCode()) || it.getLanguageCode().equals(codeMapping.getLanguageCode())))).collect(Collectors.toList());
			}
			// 如果没有任何一个语种,报错返回 (缺少语言名字)TODO
			if (CollectionUtil.isEmpty(sameCodeList)) throw new OtherException(baseCountryLanguageVO.getCountryName()+"不存在该语种");

			// 获取当前国家对应的标准列数据
			List<StandardColumn> standardColumnList = standardColumnMap.getOrDefault(code, new ArrayList<>());

			// 循环款号
			for (String bulkStyleNo : countryMappingBulkStyleNoList) {
				Optional<MoreLanguageHangTagVO> hangTagVoOpt = hangTagVOList.stream().filter(it -> it.getBulkStyleNo().equals(bulkStyleNo)).findFirst();
				if (!hangTagVoOpt.isPresent()) {
					String msg = "大货款号:" + bulkStyleNo + " 不存在吊牌信息";
					// 如果不需要合并错误信息,直接报错
					if (!mergeWarnMsg) {
						throw new OtherException(msg);
					}else {
						mergeWarnMsgList.add(msg);
						continue;
					}
				}
				MoreLanguageHangTagVO hangTagVO = hangTagVoOpt.get();

				// 获取款号绑定的codeMapping 和 groupMapping
				Map<String, MoreLanguageCodeMapping<?>> codeMap = hangTagVO.getBaseCodeMapping();

				// 安全标题没选择安全技术类别,删除
				if (sameCodeList.stream().noneMatch(it-> it.getType() == CountryLanguageType.TAG) && "10".equals(hangTagVO.getSaftyTitleCode())) {
					codeMap.remove("DP02");
				}

				// 设置当前款号的额外数据
				hangTagVO.setIngredientList(allIngredientList.stream()
						.filter(it -> it.getHangTagId().equals(hangTagVO.getId()))
						.collect(Collectors.toList()));
				hangTagVO.setSizeList(modelTypeList.stream().filter(it-> it.getCode().equals(hangTagVO.getModelType())).collect(Collectors.toList()));

				// 循环配置在CodeMapping的标准列数据
				List<CountryLanguageDto> finalSameCodeList = sameCodeList;
				List<HangTagMoreLanguageBaseVO> result = standardColumnList.stream().filter(it -> codeMap.containsKey(it.getCode())).flatMap(standardColumn -> {
					// 获取与标准类相同类型的国家语言列表
					List<CountryLanguageDto> countryLanguageDtoList = finalSameCodeList.stream()
							.filter(it -> CountryLanguageType.findByStandardColumnType(standardColumn.getType()) == it.getType()).collect(Collectors.toList());
					if (CollectionUtil.isEmpty(countryLanguageDtoList)) return null;

					String standardColumnCode = standardColumn.getCode();
					// 从codeMapping 获取 处理Func
					MoreLanguageCodeMapping<Object> codeFunc = (MoreLanguageCodeMapping<Object>) codeMap.get(standardColumnCode);

					// 遍历语言列表, 封装languageList
					return codeFunc.getListFunc().apply(hangTagVO).stream().map(data-> {
						// 拷贝基础数据
						HangTagMoreLanguageBaseVO hangTagMoreLanguageBaseVO = HANG_TAG_CV.copyMyself(baseCountryLanguageVO);
						hangTagMoreLanguageBaseVO.setBulkStyleNo(bulkStyleNo);
						HANG_TAG_CV.standardColumn2MoreLanguageBaseVO(standardColumn, hangTagMoreLanguageBaseVO);

						List<HangTagMoreLanguageVO> languageList = new ArrayList<>();
						hangTagMoreLanguageBaseVO.setLanguageList(languageList);

						// 获取翻译的code和name
						String propertiesCode = codeFunc.getKey().apply(data);
						hangTagMoreLanguageBaseVO.setPropertiesCode(propertiesCode);
						String propertiesName = codeFunc.getValue().apply(data);
						hangTagMoreLanguageBaseVO.setPropertiesName(propertiesName);

						if (StrUtil.isBlank(propertiesCode)) return hangTagMoreLanguageBaseVO;

						// 封装多值的Code
						List<String> propertiesCodeList = Arrays.asList(propertiesCode.split("\n"));
						if (propertiesCodeList.size() <= 1) {
							propertiesCodeList = Arrays.asList(propertiesCode.split(","));
						}
						// 检查是否要\n合并
						boolean needFeed = propertiesCodeList.size() > 1;

						// 查询具体翻译
						LambdaQueryWrapper<StandardColumnCountryTranslate> translateQueryWrapper = new LambdaQueryWrapper<StandardColumnCountryTranslate>()
								.eq(StandardColumnCountryTranslate::getTitleCode, standardColumnCode)
								.in(StandardColumnCountryTranslate::getPropertiesCode, propertiesCodeList);

						List<StandardColumnCountryTranslate> translateList = standardColumnCountryTranslateService.list(translateQueryWrapper.clone()
								.in(StandardColumnCountryTranslate::getCountryLanguageId, countryLanguageDtoList.stream().map(CountryLanguage::getId).collect(Collectors.toList()))
						);

						// 如果没有翻译,查找单语言翻译
						List<CountryLanguageDto> singleLanguageTypeList = singleLanguageDtoList.stream()
								.filter(it -> CountryLanguageType.findByStandardColumnType(standardColumn.getType()) == it.getType()).collect(Collectors.toList());
						if (CollectionUtil.isEmpty(translateList)) {
							translateList.addAll(standardColumnCountryTranslateService.list(translateQueryWrapper.clone()
									.in(StandardColumnCountryTranslate::getCountryLanguageId, singleLanguageTypeList.stream().map(CountryLanguage::getId).collect(Collectors.toList()))
							));
						}

						countryLanguageDtoList.forEach(countryLanguageDto -> {
							String countryLanguageId = countryLanguageDto.getId();
							// 获取单语言的id
							String singleLanguageId = singleLanguageTypeList.stream().filter(it -> it.getLanguageCode().equals(countryLanguageDto.getLanguageCode()))
									.findFirst().map(CountryLanguage::getId).orElse("");
							List<String> languageIdList = Arrays.asList(countryLanguageId, singleLanguageId);
							// 复制基础 languageVo
							HangTagMoreLanguageVO languageVO = HANG_TAG_CV.copy2MoreLanguageVO(countryLanguageDto);
							// 设置标准列模式用于判断
							languageVO.setModel(standardColumn.getModel());
							// 获取标题名翻译
							titleTranslateList.stream().filter(it->
									languageIdList.contains(it.getCountryLanguageId())
											&&
											it.getPropertiesCode().equals(standardColumnCode)
											&&
											StrUtil.isNotBlank(it.getContent())
							).findFirst().ifPresent(titleTranslate -> {
								// 找到 设置翻译 以及 flag
								languageVO.setCannotFindStandardColumnContent(false);
								languageVO.setStandardColumnContent(titleTranslate.getContent());
							});

							List<StandardColumnCountryTranslate> countryTranslateList = translateList.stream()
									.filter(it -> languageIdList.contains(it.getCountryLanguageId()) && StrUtil.isNotBlank(it.getContent()))
									.collect(Collectors.toList());

							countryTranslateList.stream().findFirst().ifPresent(translate-> {
								//  找到 设置翻译 以及 flag
								languageVO.setCannotFindPropertiesContent(false);
								// 再复制, 主要是翻译时间
								HANG_TAG_CV.countryTranslate2MoreLanguageVO(translate, languageVO);
								String content;
								// 需要合并 就多个合并
								if (needFeed) {
									content = countryTranslateList.stream().map(StandardColumnCountryTranslate::getContent).collect(Collectors.joining("\n"));
								} else {
									content = translate.getContent();
								}
								languageVO.setPropertiesContent(content);
							});
							languageList.add(languageVO);
						});
						return hangTagMoreLanguageBaseVO;
					});
				}).filter(Objects::nonNull).collect(Collectors.toList());

//					// 成分信息专属
//					String ingredient = hangTagVO.getIngredient();
//
//					if (StrUtil.isNotBlank(ingredient)) {
//						HangTagMoreLanguageBaseVO ingredientMoreLanguageVO = BeanUtil.copyProperties(baseCountryLanguageVO, HangTagMoreLanguageBaseVO.class);
//						ingredientMoreLanguageVO.setBulkStyleNo(bulkStyleNo);
//						ingredientMoreLanguageVO.setStandardColumnId("1730154821870800898");
//						ingredientMoreLanguageVO.setStandardColumnCode("DP09,DP11,DP10");
//						ingredientMoreLanguageVO.setStandardColumnName("成分信息");
//						ingredientMoreLanguageVO.setIsGroup(true);
//						ingredientMoreLanguageVO.setModel(StandardColumnModel.RADIO);
//						result.add(ingredientMoreLanguageVO);
//
//
//						BaseLambdaQueryWrapper<HangTagIngredient> queryWrapper = new BaseLambdaQueryWrapper<>();
//						queryWrapper.eq(HangTagIngredient::getHangTagId, hangTagVO.getId());
//						List<HangTagIngredient> list = hangTagIngredientService.list(queryWrapper);
//
//						List<StandardColumnCountryTranslate> ingredientContentList = new ArrayList<>();
//						List<String> ingredientCodeList = list.stream().flatMap(it -> Stream.of(it.getIngredientCode(), it.getIngredientDescriptionCode(), it.getTypeCode(), it.getIngredientSecondCode()))
//								.filter(StrUtil::isNotBlank).collect(Collectors.toList());
//
//						if (CollectionUtil.isNotEmpty(ingredientCodeList)) {
//							// 需要做个严格模式, 任意条件不满足直接返回空, 而不是condition删减(或者通过严格模式指定queryWrapper是emptyWhere就返回默认值) TODO
//							ingredientContentList.addAll(standardColumnCountryTranslateService.list(new LambdaQueryWrapper<StandardColumnCountryTranslate>()
//											.eq(StandardColumnCountryTranslate::getCountryLanguageId, countryLanguageId)
//											.in(StandardColumnCountryTranslate::getTitleCode, Arrays.asList(ingredientMoreLanguageVO.getStandardColumnCode().split(",")))
//											.in(StandardColumnCountryTranslate::getPropertiesCode, ingredientCodeList))
//									.stream().filter(Objects::nonNull)
//									.sorted(Comparator.nullsLast(Comparator.comparing(StandardColumnCountryTranslate::getUpdateDate))).collect(Collectors.toList()));
//						}
//
//						for (StandardColumnCountryTranslate content : ingredientContentList) {
//							ingredient = ingredient.replaceAll(content.getPropertiesName(), content.getContent());
//							BeanUtil.copyProperties(content, ingredientMoreLanguageVO);
//						}
//						ingredientMoreLanguageVO.setPropertiesCode(null);
//						ingredientMoreLanguageVO.setPropertiesName(hangTagVO.getIngredient());
//						ingredientMoreLanguageVO.setPropertiesContent(ingredient);
//						ingredientMoreLanguageVO.setCannotFindPropertiesContent(ingredientCodeList.size() > ingredientContentList.size());
//					}
//
//					// 充绒量
//					HangTagMoreLanguageBaseVO woolFillMoreLanguageVO = result.stream().filter(it -> "DP12".equals(it.getStandardColumnCode())).findFirst().orElse(null);
//
//					if (woolFillMoreLanguageVO != null) {
//						String downContent = woolFillMoreLanguageVO.getPropertiesContent();
//
//						if (StrUtil.isNotBlank(downContent)) {
//
//
//							List<String> woolFillSizeCodeList = Arrays.stream(downContent.split("\n")).map(it -> it.split(":")[0]).collect(Collectors.toList());
//							List<StandardColumnCountryTranslate> woolFillContentList = woolFillSizeCodeList.stream().map(it -> {
//								int sizeIndex = sizeNameList.indexOf(it);
//								String sizeCode = sizeCodeList.get(sizeIndex);
//								return standardColumnCountryTranslateService.findOne(new LambdaQueryWrapper<StandardColumnCountryTranslate>()
//										.eq(StandardColumnCountryTranslate::getCountryLanguageId, countryLanguageId)
//										.eq(StandardColumnCountryTranslate::getTitleCode, "DP06")
//										.eq(StandardColumnCountryTranslate::getPropertiesCode, sizeCode + "-" + modelType.getCode()));
//							}).filter(Objects::nonNull).sorted(Comparator.nullsLast(Comparator.comparing(StandardColumnCountryTranslate::getUpdateDate))).collect(Collectors.toList());
//
//							for (StandardColumnCountryTranslate content : woolFillContentList) {
//								downContent = downContent.replaceAll(content.getPropertiesName(), content.getContent());
//								BeanUtil.copyProperties(content, woolFillMoreLanguageVO);
//							}
//
//							woolFillMoreLanguageVO.setModel(StandardColumnModel.RADIO);
//							woolFillMoreLanguageVO.setPropertiesCode(null);
//							woolFillMoreLanguageVO.setPropertiesName(hangTagVO.getDownContent());
//							woolFillMoreLanguageVO.setPropertiesContent(downContent);
//							woolFillMoreLanguageVO.setCannotFindPropertiesContent(woolFillSizeCodeList.size() > woolFillContentList.size());
//						}
//					}

				resultList.addAll(result);
			}
		});

		if (!mergeWarnMsgList.isEmpty()) {
			StringJoiner mergeWarnMsgJoiner = new StringJoiner(" / ");
			mergeWarnMsgList.stream().distinct().forEach(mergeWarnMsgJoiner::add);
			throw new OtherException(mergeWarnMsgJoiner.toString());
		}

		resultList.sort(Comparator.comparing(HangTagMoreLanguageBaseVO::getStandardColumnId));
		List<HangTagMoreLanguageWebBaseVO> webBaseList = HANG_TAG_CV.copyList2Web(resultList);
		decorateWebList(hangTagVOList, webBaseList);
		switch (source) {
			case PDM:
                return webBaseList.stream().collect(Collectors.groupingBy(HangTagMoreLanguageWebBaseVO::getType));
			case BCS:
				List<HangTagMoreLanguageBCSVO> sourceResultList = new ArrayList<>();
				HANG_TAG_CV.copyList2Bcs(resultList).stream().collect(Collectors.groupingBy(HangTagMoreLanguageBaseVO::getCode))
						.forEach((code, sameBulkStyleNoList)-> {
							sourceResultList.add(new HangTagMoreLanguageBCSVO(sameBulkStyleNoList));
						});
				return sourceResultList;
			case PRINT:
				// 假定为只传一个款
				TagPrinting tagPrinting = hangTagPrinting(hangTagVOList).get(0);
//				List<HangTagMoreLanguagePrinterBaseVO> hangTagMoreLanguagePrinterBaseVOS = HANG_TAG_CV.copyList2Print(resultList);

				// 多国家
				List<MoreLanguageTagPrintingList> tagPrintingResultList = new ArrayList<>();
				// 假定单国家
				resultList.stream().collect(Collectors.groupingBy(HangTagMoreLanguageBaseVO::getCode)).forEach((code, sameCodeList)-> {
					List<MoreLanguageTagPrinting> tagPrintingList = new ArrayList<>();
					// 获取所有的语言
					sameCodeList.stream().flatMap(it-> it.getLanguageList().stream().map(HangTagMoreLanguageVO::getLanguageCode)).distinct().forEach(languageCode-> {
						MoreLanguageTagPrinting printing = HANG_TAG_CV.copy2MoreLanguage(tagPrinting);
						Map<String, CodeMapping<?>> codeMap = printing.getCodeMap();
						codeMap.forEach((key,value)-> {
							printing.getTitleMap().put(value.getTitleCode(), value.getTitleName());
						});
						for (HangTagMoreLanguageBaseVO result : sameCodeList) {
							String standardColumnCode = result.getStandardColumnCode();

							Optional<HangTagMoreLanguageVO> languageVoOpt = result.getLanguageList().stream().filter(it -> it.getLanguageCode().equals(languageCode)).findFirst();
							if (!languageVoOpt.isPresent()) continue;
							HangTagMoreLanguageVO languageVO = languageVoOpt.get();
							if (!codeMap.containsKey(standardColumnCode)) continue;

							CodeMapping<?> codeMapping = codeMap.get(standardColumnCode);

							Function<MoreLanguageTagPrinting, ? extends List<?>> listFunc = codeMapping.getListFunc();
							if (listFunc == null) listFunc = MoreLanguageTagPrinting::getMySelfList;

							String titleContent = Opt.ofBlankAble(languageVO.getStandardColumnContent()).orElse(result.getStandardColumnName());
							printing.getTitleMap().put(codeMapping.getTitleCode(), titleContent);

							if (codeMapping.getMapping() != null) {
								Function<Object, String> title = (Function<Object, String>) codeMapping.getMapping().getKey();
								BiConsumer<Object, String> value = (BiConsumer<Object, String>) codeMapping.getMapping().getValue();
								listFunc.apply(printing).forEach(dataObj-> {
									value.accept(dataObj, Opt.ofBlankAble(languageVO.getPropertiesContent()).orElse(result.getPropertiesName()));
								});
							}

							printing.setLanguageName(languageVO.getLanguageName());
						}
						tagPrintingList.add(printing);
					});
					tagPrintingResultList.add(new MoreLanguageTagPrintingList(tagPrintingList));
				});
				return tagPrintingResultList;
			default: throw new IllegalStateException("Unexpected value: " + source);
		}
	}

	private void decorateWebList(List<MoreLanguageHangTagVO> hangTagVOList, List<HangTagMoreLanguageWebBaseVO> webBaseList){
		Map<String, HangTagMoreLanguageGroup> groupMap = MapUtil.ofEntries(
				MapUtil.entry("成分信息", new HangTagMoreLanguageGroup("DP09,DP11,DP10,DP13", MoreLanguageHangTagVO::getIngredient)),
				MapUtil.entry("DP06", new HangTagMoreLanguageGroup("DP06", null)),
				MapUtil.entry("DP12", new HangTagMoreLanguageGroup("DP12", MoreLanguageHangTagVO::getDownContent))
		);
		List<HangTagMoreLanguageWebBaseVO> groupList = new ArrayList<>();
		webBaseList.stream().collect(Collectors.groupingBy(HangTagMoreLanguageWebBaseVO::getCode)).forEach((code, sameCodeList)-> {
			groupMap.forEach((groupName, group)-> {
				String standColumnCode = group.getStandColumnCode();
				List<HangTagMoreLanguageWebBaseVO> sameStandardColumnCodeList = webBaseList.stream()
						.filter(it -> standColumnCode.contains(it.getStandardColumnCode())).collect(Collectors.toList());

				if (CollectionUtil.isNotEmpty(sameStandardColumnCodeList)) {
					webBaseList.removeAll(sameStandardColumnCodeList);
					sameStandardColumnCodeList.stream().collect(Collectors.groupingBy(HangTagMoreLanguageWebBaseVO::getBulkStyleNo)).forEach((bulkStyleNo, sameBulkList)-> {
						MoreLanguageHangTagVO hangTagVO = hangTagVOList.stream().filter(it -> it.getBulkStyleNo().equals(bulkStyleNo)).findFirst().get();
						HangTagMoreLanguageWebBaseVO webBaseVO = sameBulkList.get(0);

						HangTagMoreLanguageWebBaseVO groupVO = HANG_TAG_CV.copyMyself(webBaseVO);
						groupVO.setStandardColumnCode(standColumnCode);
						groupVO.setStandardColumnName(groupName);
						if (groupName.equals(standColumnCode)) {
							groupVO.setStandardColumnName(webBaseVO.getStandardColumnName());
						}
						groupVO.setPropertiesCode(sameBulkList.stream().map(HangTagMoreLanguageWebBaseVO::getPropertiesCode).distinct().collect(Collectors.joining(",")));

						String separator = group.getSeparator();
						Function<MoreLanguageHangTagVO, String> content = group.getContent();
						String propertiesName;
						if (content != null) {
							propertiesName = content.apply(hangTagVO);
						}else {
							propertiesName = sameBulkList.stream().map(HangTagMoreLanguageWebBaseVO::getPropertiesName).distinct().collect(Collectors.joining(separator));
						}

						groupVO.getLanguageList().forEach(languageVo-> {
							languageVo.setPropertiesContent(propertiesName);
							List<Map<String, String>> rightLanguageMap = sameBulkList.stream().map(source-> {
								String sourcePropertiesName = source.getPropertiesName();
								return MapUtil.of(sourcePropertiesName, source.getLanguageList().stream()
										.filter(it -> it.getLanguageCode().equals(languageVo.getLanguageCode()))
										.findFirst().map(HangTagMoreLanguageVO::getPropertiesContent).orElse(""));
							}).collect(Collectors.toList());

							rightLanguageMap.forEach(map-> {
								map.forEach((key,value)-> languageVo.setPropertiesContent(languageVo.getPropertiesContent().replaceAll(key,value)));
							});
						});
						groupVO.setPropertiesName(propertiesName);

						groupList.add(groupVO);
					});
				}
			});
		});
		webBaseList.addAll(groupList);
	}

// 自定义方法区 不替换的区域【other_end】

}
