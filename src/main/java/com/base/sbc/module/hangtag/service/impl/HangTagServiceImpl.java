/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.hangtag.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.StrJoiner;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.base.sbc.client.amc.enums.DataPermissionsBusinessTypeEnum;
import com.base.sbc.client.amc.service.DataPermissionsService;
import com.base.sbc.client.flowable.service.FlowableService;
import com.base.sbc.client.flowable.vo.FlowRecordVo;
import com.base.sbc.config.common.BaseLambdaQueryWrapper;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.constant.MoreLanguageProperties;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.enums.business.*;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.redis.RedisKeyBuilder;
import com.base.sbc.config.redis.RedisKeyConstant;
import com.base.sbc.config.redis.RedisStaticFunUtils;
import com.base.sbc.config.ureport.minio.MinioUtils;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.config.utils.ExcelUtils;
import com.base.sbc.config.utils.StylePicUtils;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumModelType;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumSize;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialService;
import com.base.sbc.module.basicsdatum.service.BasicsdatumModelTypeService;
import com.base.sbc.module.basicsdatum.service.BasicsdatumSizeService;
import com.base.sbc.module.common.service.UploadFileService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.hangtag.dto.*;
import com.base.sbc.module.hangtag.entity.HangTag;
import com.base.sbc.module.hangtag.entity.HangTagIngredient;
import com.base.sbc.module.hangtag.entity.HangTagInspectCompany;
import com.base.sbc.module.hangtag.entity.HangTagLog;
import com.base.sbc.module.hangtag.enums.HangTagDeliverySCMStatusEnum;
import com.base.sbc.module.hangtag.enums.OperationDescriptionEnum;
import com.base.sbc.module.hangtag.mapper.HangTagMapper;
import com.base.sbc.module.hangtag.service.HangTagIngredientService;
import com.base.sbc.module.hangtag.service.HangTagInspectCompanyService;
import com.base.sbc.module.hangtag.service.HangTagLogService;
import com.base.sbc.module.hangtag.service.HangTagService;
import com.base.sbc.module.hangtag.vo.*;
import com.base.sbc.module.hangtag.vo.MoreLanguageHangTagVO.HangTagMoreLanguageGroup;
import com.base.sbc.module.hangtag.vo.MoreLanguageHangTagVO.MoreLanguageCodeMapping;
import com.base.sbc.module.moreLanguage.dto.CountryLanguageDto;
import com.base.sbc.module.moreLanguage.dto.CountryQueryDto;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageStatusCheckDetailDTO;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageStatusCheckDetailOldDTO;
import com.base.sbc.module.moreLanguage.entity.CountryLanguage;
import com.base.sbc.module.moreLanguage.entity.StandardColumnCountryTranslate;
import com.base.sbc.module.moreLanguage.entity.StyleCountryStatus;
import com.base.sbc.module.moreLanguage.mapper.StyleCountryStatusMapper;
import com.base.sbc.module.moreLanguage.service.CountryLanguageService;
import com.base.sbc.module.moreLanguage.service.StandardColumnCountryRelationService;
import com.base.sbc.module.moreLanguage.service.StandardColumnCountryTranslateService;
import com.base.sbc.module.moreLanguage.service.StyleCountryStatusService;
import com.base.sbc.module.pack.entity.PackBom;
import com.base.sbc.module.pack.entity.PackInfo;
import com.base.sbc.module.pack.service.PackBomService;
import com.base.sbc.module.pack.service.PackInfoService;
import com.base.sbc.module.pack.service.PackInfoStatusService;
import com.base.sbc.module.pack.utils.PackUtils;
import com.base.sbc.module.pricing.service.StylePricingService;
import com.base.sbc.module.pricing.vo.StylePricingVO;
import com.base.sbc.module.smp.SmpService;
import com.base.sbc.module.smp.entity.TagPrinting;
import com.base.sbc.module.standard.entity.StandardColumn;
import com.base.sbc.module.standard.service.StandardColumnService;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.style.entity.StyleColor;
import com.base.sbc.module.style.entity.StyleMainAccessories;
import com.base.sbc.module.style.mapper.StyleColorMapper;
import com.base.sbc.module.style.service.StyleColorService;
import com.base.sbc.module.style.service.StyleMainAccessoriesService;
import com.base.sbc.module.style.service.StyleService;
import com.base.sbc.open.dto.MoreLanguageTagPrinting;
import com.base.sbc.open.dto.MoreLanguageTagPrintingList;
import com.base.sbc.open.dto.TagPrintingSupportVO.CodeMapping;
import com.base.sbc.open.entity.EscmMaterialCompnentInspectCompanyDto;
import com.base.sbc.open.service.EscmMaterialCompnentInspectCompanyService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import springfox.documentation.spring.web.json.Json;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.base.sbc.config.constant.Constants.COMMA;
import static com.base.sbc.config.constant.MoreLanguageProperties.MoreLanguageMsgEnum.HAVEN_T_COUNTRY_LANGUAGE;
import static com.base.sbc.config.constant.MoreLanguageProperties.MoreLanguageMsgEnum.HAVEN_T_TAG;
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
	private StandardColumnCountryTranslateService standardColumnCountryTranslateService;

	@Autowired
	private EscmMaterialCompnentInspectCompanyService escmMaterialCompnentInspectCompanyService;

	@Autowired
	private HangTagInspectCompanyService hangTagInspectCompanyService;

	@Autowired
	private StyleCountryStatusMapper styleCountryStatusMapper;

	@Resource
	@Lazy
	private StyleCountryStatusService styleCountryStatusService;

	@Autowired
	private StandardColumnCountryRelationService standardColumnCountryRelationService;

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
			hangTagDTO.setDesignNos(hangTagDTO.getDesignNo().split(","));
		}

		if(StrUtil.isNotBlank(hangTagDTO.getProductCode())){
			hangTagDTO.setProductCodes(hangTagDTO.getProductCode().split(","));
		}

		if(StrUtil.isNotBlank(hangTagDTO.getProdCategory())){
			hangTagDTO.setProdCategorys(hangTagDTO.getProdCategory().split(","));
		}

		if(StrUtil.isNotBlank(hangTagDTO.getBandName())){
			hangTagDTO.setBandNames(hangTagDTO.getBandName().split(","));
		}
		List<HangTagListVO> hangTagListVOS = hangTagMapper.queryList(hangTagDTO, authSql);
		if(StrUtil.equals(hangTagDTO.getImgFlag(),BaseGlobal.YES)){
			if(hangTagListVOS.size() > 2000){
				throw new OtherException("带图片导出2000条数据");
			}
			minioUtils.setObjectUrlToList(hangTagListVOS, "washingLabel");
		}
		if(!StrUtil.equals(hangTagDTO.getDeriveFlag(),BaseGlobal.YES)){
			minioUtils.setObjectUrlToList(hangTagListVOS, "washingLabel");
		}
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
						// 状态：0.未填写，1.未提交，2.待工艺员确认，3.待技术员确认，4.待品控确认，5.待翻译确认,6.不通过, 7.已确认

						if (HangTagStatusEnum.SUSPEND != e.getStatus()) {
							switch (flowRecordVo.getName()) {
							case "大货工艺员确认":
								e.setStatus(HangTagStatusEnum.NOT_COMMIT);
								break;
							case "后技术确认":
								e.setStatus(HangTagStatusEnum.TECH_CHECK);
								break;
							case "品控确认":
								e.setStatus(HangTagStatusEnum.QC_CHECK);
								break;
							case "翻译确认":
								e.setStatus(HangTagStatusEnum.TRANSLATE_CHECK);
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
/*		List<PackInfo> packInfos = packInfoService
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
		}*/
		// 如果之前完成了,但是新加了一个国家，就要改回到部分翻译
		if (hangTagListVOS.stream().anyMatch(it-> it.getStatus() == HangTagStatusEnum.FINISH)) {
			long size = countryLanguageService.getAllCountrySize();
			hangTagListVOS.stream().filter(it-> it.getStatus() == HangTagStatusEnum.FINISH).forEach(hangTag-> {
				if (styleCountryStatusService.count(new BaseLambdaQueryWrapper<StyleCountryStatus>()
						.eq(StyleCountryStatus::getBulkStyleNo, hangTag.getBulkStyleNo())
						.ne(StyleCountryStatus::getStatus, StyleCountryStatusEnum.UNCHECK)) < size) {
					hangTag.setStatus(HangTagStatusEnum.PART_TRANSLATE_CHECK);
				}
			});
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
		hangTagSearchDTO.setDeriveFlag(BaseGlobal.YES);
		List<HangTagListVO> list = queryPageInfo(hangTagSearchDTO, userCompany).getList();
		List<HangTagVoExcel> hangTagVoExcels = BeanUtil.copyToList(list, HangTagVoExcel.class);
		ExcelUtils.executorExportExcel(hangTagVoExcels, HangTagVoExcel.class,"吊牌",hangTagSearchDTO.getImgFlag(),2000,response,"washingLabel");
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
			hangTagVO.setStatus(HangTagStatusEnum.NOT_COMMIT);
		}


		if(StrUtil.isNotBlank(hangTagVO.getId())){
			// 查询检测报告
			List<HangTagInspectCompany> hangTagInspectCompanyList = hangTagInspectCompanyService.listByField("hang_tag_id", com.base.sbc.config.utils.StringUtils.convertList(hangTagVO.getId()));
			if (CollUtil.isNotEmpty(hangTagInspectCompanyList)) {
				List<EscmMaterialCompnentInspectCompanyDto> list = escmMaterialCompnentInspectCompanyService.listByIds(hangTagInspectCompanyList.stream().map(HangTagInspectCompany::getInspectCompanyId).collect(Collectors.toList()));
				hangTagVO.setCompnentInspectCompanyDtoList(list);
				return hangTagVO;
			}
		}

		PackInfo packInfo = packInfoService
				.getOne(new QueryWrapper<PackInfo>().eq("style_no", hangTagVO.getBulkStyleNo()));
		if (packInfo != null) {
			QueryWrapper<PackBom> queryWrapper = new QueryWrapper<PackBom>().eq("foreign_id", packInfo.getId());
			queryWrapper.eq("unusable_flag",BaseGlobal.NO);
			queryWrapper.eq("pack_type",StrUtil.equals(hangTagVO.getBomStatus(),BaseGlobal.YES)? PackUtils.PACK_TYPE_BIG_GOODS :PackUtils.PACK_TYPE_DESIGN);
			List<PackBom> packBomList = packBomService.list(queryWrapper);
			if (!packBomList.isEmpty()) {
				List<String> codes = packBomList.stream().map(PackBom::getMaterialCode).collect(Collectors.toList());
				if (!codes.isEmpty()) {
					/*查询物料*/
					List<EscmMaterialCompnentInspectCompanyDto> list =	escmMaterialCompnentInspectCompanyService.getListByMaterialsNo(new QueryWrapper<EscmMaterialCompnentInspectCompanyDto>().in("materials_no",codes));
/*					List<BasicsdatumMaterial> list = basicsdatumMaterialService
							.list(new QueryWrapper<BasicsdatumMaterial>().in("material_code", codes));*/
					hangTagVO.setCompnentInspectCompanyDtoList(list);
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

		/*boolean flag = false;
		if(StringUtils.isEmpty(hangTagDTO.getId())){
			flag = true;
		}else {
			HangTag hangTag1 = this.getById(hangTagDTO.getId());
			if(!hangTag1.getProductName().equals(hangTag.getProductName())){
				flag = true;
			}
		}*/


		super.saveOrUpdate(hangTag, "吊牌管理");
		String id = hangTag.getId();

		// 成分检查
		if (hangTagDTO.getStatus() == HangTagStatusEnum.DESIGN_CHECK) {
			strictCheckIngredientPercentage(Collections.singletonList(id));
		}
		/*检测报告*/
		List<HangTagInspectCompany> hangTagInspectCompanyList = hangTagDTO.getHangTagInspectCompanyList();
		if(CollUtil.isNotEmpty(hangTagInspectCompanyList)){
			/*先删除之前的数据*/
				hangTagInspectCompanyService.remove(new QueryWrapper<HangTagInspectCompany>().eq("hang_tag_id",hangTag.getId()));
			for (HangTagInspectCompany hangTagInspectCompany : hangTagInspectCompanyList) {
				//前端传值，  保存操作：inspectCompanyId 有值，保存并添加：id 有值
				hangTagInspectCompany.setInspectCompanyId(StrUtil.isNotEmpty(hangTagInspectCompany.getInspectCompanyId()) ? hangTagInspectCompany.getInspectCompanyId() : hangTagInspectCompany.getId());
				hangTagInspectCompany.setId(IdUtil.getSnowflakeNextIdStr());
				hangTagInspectCompany.setHangTagId(hangTag.getId());
				hangTagInspectCompany.insertInit();
			}
			hangTagInspectCompanyService.saveOrUpdateBatch(hangTagInspectCompanyList);
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


		if (HangTagStatusEnum.DESIGN_CHECK == hangTag.getStatus() && HangTagStatusCheckEnum.TRANSLATE_CHECK == hangTagDTO.getCheckType()) {
			hangTag = this.getById(hangTag.getId());
			// 发起审批
			flowableService.start(FlowableService.HANGING_TAG_REVIEW + hangTag.getBulkStyleNo(),
					FlowableService.HANGING_TAG_REVIEW, hangTag.getBulkStyleNo(), "/pdm/api/saas/hangTag/toExamine",
					"/pdm/api/saas/hangTag/toExamine", "/pdm/api/saas/hangTag/toExamine", null,
					BeanUtil.beanToMap(hangTag));
		}
		// 如果提交审核默认通过第一个审核
		if (HangTagStatusEnum.DESIGN_CHECK == hangTag.getStatus() && HangTagStatusCheckEnum.QC_CHECK != hangTagDTO.getCheckType()) {
			hangTag.setStatus(HangTagStatusEnum.TECH_CHECK);
			this.updateById(hangTag);
		}
		try {
			//下发成分
			smpService.sendTageComposition(Collections.singletonList(id));

			//region 2023-12-06 吊牌保存需要修改工艺员确认状态
			smpService.tagConfirmDates(Collections.singletonList(id), HangTagDeliverySCMStatusEnum.TECHNOLOGIST_CONFIRM, 1);
			//endregion

		}catch (Exception ignored){
		}
        return id;
	}

	@Transactional
	@Override
	public void updateSecondPackagingFormById(HangTagDTO hangTagDTO) {
		if (StrUtil.isNotEmpty(hangTagDTO.getId())) {
			HangTag hangTag = this.getById(hangTagDTO.getId());
			if (hangTag != null) {
				String code = hangTag.getSecondPackagingFormCode();
				if (StrUtil.isBlank(code)) {
					code = "-1";
				}

				hangTag.setSecondPackagingForm(hangTagDTO.getSecondPackagingForm());
				hangTag.setSecondPackagingFormCode(hangTagDTO.getSecondPackagingFormCode());
				this.updateById(hangTag);

				StyleColor styleColor = styleColorService.getByOne("style_no", hangTag.getBulkStyleNo());
				if (styleColor != null) {
					String secondPackagingFormCode = hangTagDTO.getSecondPackagingFormCode();
					if (StrUtil.isBlank(code)) {
						secondPackagingFormCode = "-1";
					}
					if (!code.equals(secondPackagingFormCode)) {
						smpService.goods(styleColor.getId().split(","));
					}
				}
			}
		}
	}

	private void strictCheckIngredientPercentage(List<String> hangTagIdList){
		if (CollectionUtil.isEmpty(hangTagIdList)) return;;
		List<HangTagIngredient> hangTagIngredientList = hangTagIngredientService.list(new BaseLambdaQueryWrapper<HangTagIngredient>()
				.in(HangTagIngredient::getHangTagId, hangTagIdList)
				.eq(HangTagIngredient::getStrictCheck, YesOrNoEnum.YES.getValueStr())
		);
		if (CollectionUtil.isEmpty(hangTagIngredientList)) return;

		hangTagIngredientList.stream().filter(it-> !it.checkPercentageRequired() && !it.checkDescriptionRemarks()).forEach(it-> {
			throw new OtherException("开启成分信息校验后,(材料类型-百分比-成分名称)和(成分说明)必须二选一进行填写");
		});

		hangTagIngredientList.stream().filter(HangTagIngredient::checkPercentageRequired)
				.collect(Collectors.groupingBy(HangTagIngredient::getHangTagId)).forEach((hangTagId, sameHangTagIdList)-> {
					sameHangTagIdList.stream().collect(Collectors.groupingBy(it-> it.getTypeCode() + "-" + Opt.ofNullable(it.getIngredientSecondCode()).orElse(""))).forEach((code, list)-> {
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
	public void updateStatus(HangTagUpdateStatusDTO hangTagUpdateStatusDTO, boolean repeatUpdate, List<HangTag> hangTags) {
		String userCompany = hangTagUpdateStatusDTO.getUserCompany();
		logger.info("HangTagService#updateStatus 更新状态 hangTagUpdateStatusDTO:{}, userCompany:{}",
				JSON.toJSONString(hangTagUpdateStatusDTO), userCompany);
		if (CollectionUtils.isEmpty(hangTags)) {
			LambdaQueryWrapper<HangTag> queryWrapper = new QueryWrapper<HangTag>().lambda().in(HangTag::getId,
					hangTagUpdateStatusDTO.getIds());
			// .eq(HangTag::getCompanyCode, userCompany);
			hangTags.addAll(super.list(queryWrapper));
			// 检查
			strictCheckIngredientPercentage(hangTags.stream().map(HangTag::getId).collect(Collectors.toList()));
		}
		if (CollectionUtils.isEmpty(hangTags)) {
			throw new OtherException("存在未填写数据，请先填写");
		}
		HangTagStatusEnum updateStatus = hangTagUpdateStatusDTO.getStatus();

		// 如果当前是部分确认,且部分确认,仅当部分确认
		if (HangTagStatusEnum.PART_TRANSLATE_CHECK == updateStatus) {
			List<HangTag> partCheckTranslateList = hangTags.stream()
					.filter(it -> Arrays.asList(HangTagStatusEnum.FINISH, HangTagStatusEnum.TRANSLATE_CHECK, HangTagStatusEnum.PART_TRANSLATE_CHECK).contains(it.getStatus())).collect(Collectors.toList());
			if (CollectionUtil.isNotEmpty(partCheckTranslateList)) {
//				String countryCode = hangTagUpdateStatusDTO.getCountryCode();
//				if (StrUtil.isBlank(countryCode)) throw new OtherException("未选择需要翻译的国家");

				partCheckTranslateList.stream().collect(Collectors.groupingBy(it-> it.getStatus() != HangTagStatusEnum.TRANSLATE_CHECK)).forEach((multiCheck, multiCheckList)-> {
					styleCountryStatusService.updateStatus(partCheckTranslateList.stream().map(hangTag-> {
						StyleCountryStatus status = new StyleCountryStatus();
						status.setBulkStyleNo(hangTag.getBulkStyleNo());
						status.setCountryCode(hangTagUpdateStatusDTO.getCountryCode());
						status.setStatus(StyleCountryStatusEnum.CHECK);
						return status;
					}).collect(Collectors.toList()), multiCheckList, multiCheck);
					if (multiCheck) {
						hangTags.removeAll(multiCheckList);
					}
				});
			}
		}

		if (CollectionUtil.isEmpty(hangTags)) return;

		ArrayList<HangTag> updateHangTags = Lists.newArrayList();
		hangTags.forEach(e -> {
			if (HangTagStatusEnum.NOT_COMMIT != updateStatus) {
				try {
					if (HangTagStatusEnum.FINISH == e.getStatus()) {
						throw new OtherException("存在已通过审核数据，请反审");
					}
					if (HangTagStatusEnum.NOT_COMMIT == e.getStatus()
							&&
							HangTagStatusEnum.DESIGN_CHECK != updateStatus
					) {
						throw new OtherException("存在待提交数据，请先提交");
					}
					if (HangTagStatusEnum.DESIGN_CHECK == e.getStatus()
							&&
							HangTagStatusEnum.TECH_CHECK != updateStatus
					) {
						throw new OtherException("存在待工艺员确认数据，请先待工艺员确认");
					}
					if (HangTagStatusEnum.TECH_CHECK == e.getStatus()
							&&
							HangTagStatusEnum.QC_CHECK != updateStatus) {
						throw new OtherException("存在待技术员确认数据，请先技术员确认");
					}
					if (HangTagStatusEnum.QC_CHECK == e.getStatus()
							&&
							HangTagStatusEnum.TRANSLATE_CHECK != updateStatus) {
						throw new OtherException("存在待品控确认数据，请先品控确认");
					}
					if (HangTagStatusEnum.TRANSLATE_CHECK == e.getStatus()
							&&
							HangTagStatusEnum.PART_TRANSLATE_CHECK != updateStatus) {
						throw new OtherException("存在待翻译确认数据，请先翻译确认");
					}
					if (HangTagStatusEnum.PART_TRANSLATE_CHECK == e.getStatus()
							&&
							HangTagStatusEnum.FINISH != updateStatus) {
						throw new OtherException("存在部分翻译数据，请先全部翻译确认");
					}
				}catch (OtherException ex) {
					if (!repeatUpdate || !e.getStatus().equals(updateStatus)) {
						throw ex;
					}
				}
			}
			HangTag hangTag = new HangTag();
			hangTag.setId(e.getId());
			hangTag.updateInit();
			hangTag.setStatus(updateStatus);
			if (updateStatus.lessThan(HangTagStatusEnum.TRANSLATE_CHECK)) {
				hangTag.setConfirmDate(null);
			}else if (updateStatus == HangTagStatusEnum.TRANSLATE_CHECK) {
				hangTag.setConfirmDate(new Date());
			}else {
				hangTag.setTranslateConfirmDate(new Date());
			}
			updateHangTags.add(hangTag);
		});
		super.updateBatchById(updateHangTags);
		hangTagLogService.saveBatch(hangTagUpdateStatusDTO.getIds(), updateStatus.getText(), userCompany);

		HangTagDeliverySCMStatusEnum type;
		switch (updateStatus) {
			case TECH_CHECK:
				type = HangTagDeliverySCMStatusEnum.TECHNOLOGIST_CONFIRM;
				break;
			case QC_CHECK:
				type = HangTagDeliverySCMStatusEnum.TECHNICAL_CONFIRM;
				break;
			case TRANSLATE_CHECK:
				type = HangTagDeliverySCMStatusEnum.QUALITY_CONTROL_CONFIRM;
				break;
            default:
				type = HangTagDeliverySCMStatusEnum.TRANSLATE_CONFIRM;
		}

		smpService.tagConfirmDates(hangTagUpdateStatusDTO.getIds(),type,1);
		// 貌似无用, checkType前端不传
		if (HangTagStatusCheckEnum.QC_CHECK == hangTagUpdateStatusDTO.getCheckType()) {
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
	@Transactional(rollbackFor = Exception.class)
	public List<TagPrinting> hangTagPrinting(String styleNo, boolean likeQueryFlag) {
		BaseQueryWrapper<HangTag> baseQueryWrapper = new BaseQueryWrapper<>();

		if (likeQueryFlag) {
			baseQueryWrapper.notEmptyEq("bulk_style_no", styleNo);
		} else {
			baseQueryWrapper.notEmptyLike("bulk_style_no", styleNo);
		}
		// 吊牌只查询非历史迁移数据
		//baseQueryWrapper.ne("historical_data", "1");

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
					// 根据款号查询 styleColor
					String materialCodeNames = null;
					if ("1".equals(styleColor.getBomStatus())) {
						materialCodeNames = "";
						// 如果已经转大货了 才需要查询物料信息并返回
						// 根据款式编码获取资料包信息
						PackInfo packInfo = packInfoService.getOne(
								new LambdaQueryWrapper<PackInfo>()
										.eq(PackInfo::getStyleNo, styleColor.getStyleNo())
						);
                        if (ObjectUtil.isNotEmpty(packInfo)) {
                            // 根据资料包的 id 查询物料信息
                            List<PackBom> packBomList = packBomService.list(
                                    new LambdaQueryWrapper<PackBom>()
                                            .eq(PackBom::getForeignId, packInfo.getId())
                                            .eq(PackBom::getPackType,"packBigGoods")
                            );
                            if (ObjectUtil.isNotEmpty(packBomList)) {
								materialCodeNames = CollUtil.join(
										packBomList
												.stream()
												.map(PackBom::getMaterialCodeName)
												.filter(item -> item.contains("特殊吊牌"))
												.collect(Collectors.toList()),
										",");

							}
                        }
					}
					tagPrinting.setMaterialCodeNames(materialCodeNames);
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
				tagPrinting.setApproved(hangTag.getStatus().greatThan(HangTagStatusEnum.QC_CHECK) && HangTagStatusEnum.SUSPEND != hangTag.getStatus());
				// 翻译确认
				tagPrinting.setTranslateApproved(hangTag.getStatus() == HangTagStatusEnum.FINISH);
				// 温馨提示
				tagPrinting.setAttention(hangTag.getWarmTips());
				// 后技术确认
				tagPrinting.setTechApproved(hangTag.getStatus().greatThan(HangTagStatusEnum.DESIGN_CHECK) && HangTagStatusEnum.SUSPEND != hangTag.getStatus());
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
			hangTag.setStatus(HangTagStatusEnum.NOT_COMMIT);
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
	public Object getMoreLanguageDetailsByBulkStyleNo(HangTagMoreLanguageDTO hangTagMoreLanguageDTO) {
		// 多国家语言 多款号
		Boolean likeQueryFlag = hangTagMoreLanguageDTO.getLikeQueryFlag();
		// 模糊查询
		List<String> bulkStyleNoList;
		if (Boolean.TRUE.equals(likeQueryFlag)) {
			// 模糊查询 TODO
			bulkStyleNoList = new ArrayList<>();
		}else {
			bulkStyleNoList = Arrays.asList(hangTagMoreLanguageDTO.getBulkStyleNo().split(COMMA));
		}
		SystemSource source = hangTagMoreLanguageDTO.getSource();

		// 查询国家语言
		CountryQueryDto countryQueryDto = HANG_TAG_CV.copy2CountryQuery(hangTagMoreLanguageDTO);
		List<CountryLanguageDto> countryLanguageList = countryLanguageService.listQuery(countryQueryDto);
		if (CollectionUtil.isEmpty(countryLanguageList)) throw new OtherException(MoreLanguageProperties.getMsg(HAVEN_T_COUNTRY_LANGUAGE));

		// 获取所有的吊牌
		List<MoreLanguageHangTagVO> hangTagVOList = HANG_TAG_CV.copyList2MoreLanguage(hangTagMapper.getDetailsByBulkStyleNo(
				bulkStyleNoList, hangTagMoreLanguageDTO.getUserCompany(), hangTagMoreLanguageDTO.getSelectType()
		));

		// 获取吊牌成分
		List<HangTagIngredient> allIngredientList = hangTagIngredientService.list(new BaseLambdaQueryWrapper<HangTagIngredient>()
				.notEmptyIn(HangTagIngredient::getHangTagId, hangTagVOList.stream().map(HangTagVO::getId).collect(Collectors.toList()))
				.select(HangTagIngredient::getIngredientCode, HangTagIngredient::getIngredientName, HangTagIngredient::getIngredientDescriptionCode, HangTagIngredient::getIngredientDescription,
						HangTagIngredient::getTypeCode, HangTagIngredient::getType, HangTagIngredient::getIngredientSecondCode, HangTagIngredient::getIngredientSecondName, HangTagIngredient::getHangTagId)
		);

		// 获取吊牌的modelType
		List<String> sizeCodeList  = hangTagVOList.stream().map(HangTagVO::getModelType).filter(StrUtil::isNotBlank).distinct().collect(Collectors.toList());
		List<BasicsdatumModelType> modelTypeList = basicsdatumModelTypeService.list(new BaseLambdaQueryWrapper<BasicsdatumModelType>()
				.notEmptyIn(BasicsdatumModelType::getCode,sizeCodeList)
				.select(BasicsdatumModelType::getCode, BasicsdatumModelType::getSize, BasicsdatumModelType::getSizeCode)
		);
		hangTagVOList.forEach(hangTagVO-> {
			// 设置当前款号的额外数据
			hangTagVO.setIngredientList(allIngredientList.stream().filter(it -> it.getHangTagId().equals(hangTagVO.getId())).collect(Collectors.toList()));
			hangTagVO.setSizeList(modelTypeList.stream().filter(it-> it.getCode().equals(hangTagVO.getModelType())).collect(Collectors.toList()));
		});

		// 根据国家分组
		// 获取需要实时翻译
		hangTagMoreLanguageDTO.setDecorate(source != SystemSource.PRINT);
//		hangTagMoreLanguageDTO.setDocumentStatusLimit(source.greatThan(SystemSource.INTERNAL_LINE) ? StyleCountryStatusEnum.CHECK: null);
		List<HangTagMoreLanguageBaseVO> resultList = buildResultList(hangTagMoreLanguageDTO, countryLanguageList, bulkStyleNoList, hangTagVOList);

		resultList.sort(Comparator.comparing(HangTagMoreLanguageBaseVO::getStandardColumnId, Comparator.nullsFirst(String::compareTo)));
//				 存入redis, 若审核确认了,减少
		countryLanguageList.forEach(countryLanguage-> {
			String code = countryLanguage.getCode();
			bulkStyleNoList.forEach(bulkStyleNo-> {
				RedisStaticFunUtils.set(RedisKeyConstant.HANG_TAG_COUNTRY.addEnd(true, code, bulkStyleNo),
						resultList.stream().filter(it-> it.getCode().equals(code) && it.getBulkStyleNo().equals(bulkStyleNo)).collect(Collectors.toList())
				);
			});
		});
		return decorateResultList(source, resultList, hangTagVOList);
	}

	private List<HangTagMoreLanguageBaseVO> buildResultList(HangTagMoreLanguageDTO hangTagMoreLanguageDTO,
															List<CountryLanguageDto> countryLanguageList,
															List<String> bulkStyleNoList,
															List<MoreLanguageHangTagVO> hangTagVOList
	) {
		List<HangTagMoreLanguageBaseVO> resultList = new ArrayList<>();
		Set<String> codeList = CollUtil.set(false, hangTagMoreLanguageDTO.getCode().split(COMMA));

		List<String> languageCodeList = countryLanguageList.stream().map(CountryLanguageDto::getLanguageCode).distinct().collect(Collectors.toList());
		List<CountryLanguageType> typeList = countryLanguageList.stream().map(CountryLanguageDto::getType).distinct().collect(Collectors.toList());

		// 获得要翻译的标准列码集合
		Map<String, List<StandardColumn>> standardColumnMap = new HashMap<>(codeList.size());
		codeList.forEach(code-> {
			List<StandardColumn> standardColumnList = standardColumnMap.getOrDefault(code, new ArrayList<>());
			typeList.forEach(type-> {
				standardColumnList.addAll(countryLanguageService.findStandardColumnList(code, type, false));
			});
			standardColumnMap.put(code, standardColumnList);
		});

		// 获取吊牌状态
		List<StyleCountryStatus> styleCountryStatusList = styleCountryStatusMapper.selectList(new BaseLambdaQueryWrapper<StyleCountryStatus>()
				.notNullEq(StyleCountryStatus::getStatus, hangTagMoreLanguageDTO.getDocumentStatusLimit())
				.notEmptyEq(StyleCountryStatus::getCountryCode, hangTagMoreLanguageDTO.getCode())
				.notEmptyEq(StyleCountryStatus::getType, hangTagMoreLanguageDTO.getType())
				.notEmptyIn(StyleCountryStatus::getBulkStyleNo, bulkStyleNoList));

		// 查询单语言
		CountryQueryDto languageDto = new CountryQueryDto();
		languageDto.setLanguageCode(String.join(COMMA, languageCodeList));
		languageDto.setSingleLanguageFlag(YesOrNoEnum.YES);
		List<CountryLanguageDto> singleLanguageDtoList = countryLanguageService.listQuery(languageDto);

		countryLanguageList.stream().collect(CommonUtils.groupingBy(CountryLanguage::getCode)).forEach((code, sameCodeList)-> {
			// 获取当前国家对应的标准列数据
			List<StandardColumn> standardColumnList = standardColumnMap.getOrDefault(code, new ArrayList<>());

			// 循环款号
			buildBulkResultList(hangTagMoreLanguageDTO, hangTagVOList, sameCodeList, bulkStyleNoList, standardColumnList, styleCountryStatusList, singleLanguageDtoList, resultList);
		});
		return resultList;
	}

	private void buildBulkResultList(HangTagMoreLanguageDTO hangTagMoreLanguageDTO,
									 List<MoreLanguageHangTagVO> hangTagVOList,
									 List<CountryLanguageDto> sameCodeList,
									 List<String> countryMappingBulkStyleNoList,
									 List<StandardColumn> standardColumnList,
									 List<StyleCountryStatus> styleCountryStatusList,
									 List<CountryLanguageDto> singleLanguageDtoList,
									 List<HangTagMoreLanguageBaseVO> resultList
	) {
		// 封装基础VO
		HangTagMoreLanguageBaseVO moreLanguageBaseVO = HANG_TAG_CV.copy2MoreLanguageBaseVO(sameCodeList.get(0));
		String code = moreLanguageBaseVO.getCode();

		List<HangTagMoreLanguageCheckDTO> codeMappingList = Opt.ofNullable(hangTagMoreLanguageDTO.getHangTagMoreLanguageCheckDTOList()).orElse(new ArrayList<>());
		// 根据传入的检查Bean, 确定这个国家需要检查的吊牌 和 语言
		List<HangTagMoreLanguageCheckDTO> mappingList = codeMappingList.stream().filter(it -> it.getCode().equals(code)).collect(Collectors.toList());
		if (!mappingList.isEmpty()) {
			countryMappingBulkStyleNoList = mappingList.stream().flatMap(it-> Arrays.stream(it.getBulkStyleNo().split(COMMA))).collect(Collectors.toList());
		}
		sameCodeList.removeIf(countryLanguageDto-> !mappingList.isEmpty() && mappingList.stream().noneMatch(it-> it.match(countryLanguageDto)));

		for (String bulkStyleNo : countryMappingBulkStyleNoList) {
			moreLanguageBaseVO.setBulkStyleNo(bulkStyleNo);

			Optional<MoreLanguageHangTagVO> hangTagVoOpt = hangTagVOList.stream().filter(it -> it.getBulkStyleNo().equals(bulkStyleNo)).findFirst();
			if (!hangTagVoOpt.isPresent()) {
				// 如果不需要合并错误信息,直接报错
				if (Boolean.TRUE.equals(hangTagMoreLanguageDTO.getMergeWarnMsg())) {
					resultList.add(HANG_TAG_CV.copyMyself(moreLanguageBaseVO));
					continue;
				}else {
					throw new OtherException(MoreLanguageProperties.getMsg(HAVEN_T_TAG, bulkStyleNo));
				}
			}
			MoreLanguageHangTagVO hangTagVO = hangTagVoOpt.get();

			// 获取款号绑定的codeMapping 和 groupMapping
			Map<String, MoreLanguageCodeMapping<?>> codeMap = hangTagVO.getBaseCodeMapping();

			// 安全标题没选择安全技术类别,删除
			if (sameCodeList.stream().noneMatch(it-> it.getType() == CountryLanguageType.TAG) && "10".equals(hangTagVO.getSaftyTitleCode())) {
				codeMap.remove(MoreLanguageProperties.saftyStandardCode);
			}

			// 循环配置在CodeMapping的标准列数据
			standardColumnList.stream().filter(it -> codeMap.containsKey(it.getCode())).forEach(standardColumn -> {
				String standardColumnCode = standardColumn.getCode();
				// 获取与标准类相同类型的国家语言列表
				CountryLanguageType countryLanguageType = CountryLanguageType.findByStandardColumnType(standardColumn.getType());
				List<CountryLanguageDto> countryLanguageDtoList = sameCodeList.stream()
						.filter(it -> countryLanguageType == it.getType()).collect(Collectors.toList());

				String checkDetailJson = styleCountryStatusList.stream()
						.filter(it -> bulkStyleNo.equals(it.getBulkStyleNo())
								&& code.equals(it.getCountryCode())
								&& countryLanguageType == it.getType()
						).findFirst().map(StyleCountryStatus::getCheckDetailJson).orElse("[]");

				// 从codeMapping 获取 处理Func
				MoreLanguageCodeMapping<Object> codeFunc = (MoreLanguageCodeMapping<Object>) codeMap.get(standardColumnCode);

				String parentStandardCode = standardColumnList.stream().filter(it -> countryLanguageType.getStandardColumnType() == it.getType())
						.map(StandardColumn::getCode).findFirst().orElseThrow(() -> new OtherException("未定位到维护内容数据"));
				moreLanguageBaseVO.setTitleCode(parentStandardCode);

				buildStandardResultList(hangTagMoreLanguageDTO, countryLanguageDtoList, checkDetailJson, standardColumn,
						singleLanguageDtoList, codeFunc, hangTagVO, moreLanguageBaseVO, resultList);
			});
		}
	}

	private void buildStandardResultList(HangTagMoreLanguageDTO hangTagMoreLanguageDTO,
										 List<CountryLanguageDto> countryLanguageDtoList,
										 String checkDetailJson,
										 StandardColumn standardColumn,
										 List<CountryLanguageDto> singleLanguageDtoList,
										 MoreLanguageCodeMapping<Object> codeFunc,
										 MoreLanguageHangTagVO hangTagVO,
										 HangTagMoreLanguageBaseVO moreLanguageBaseVO,
										 List<HangTagMoreLanguageBaseVO> resultList
	) {
		String standardColumnCode = standardColumn.getCode();
		CountryLanguageType countryLanguageType = CountryLanguageType.findByStandardColumnType(standardColumn.getType());

		List<MoreLanguageStatusCheckDetailDTO> statusCheckDetailList = JSONUtil.toList(checkDetailJson, MoreLanguageStatusCheckDetailDTO.class);

		// 遍历语言列表, 封装languageList
		for (Object data : codeFunc.getListFunc().apply(hangTagVO)) {
			// 拷贝基础数据
			HangTagMoreLanguageBaseVO hangTagMoreLanguageBaseVO = HANG_TAG_CV.copyMyself(moreLanguageBaseVO);
			HANG_TAG_CV.standardColumn2MoreLanguageBaseVO(standardColumn, hangTagMoreLanguageBaseVO);
			List<HangTagMoreLanguageVO> languageList = new ArrayList<>();
			hangTagMoreLanguageBaseVO.setLanguageList(languageList);
			hangTagMoreLanguageBaseVO.setCountryLanguageType(countryLanguageType);

			// 获取翻译的code和name
			String propertiesCode = Opt.ofNullable(codeFunc.getKey().apply(data)).orElse("");
			hangTagMoreLanguageBaseVO.setPropertiesCode(propertiesCode);
			String propertiesName = codeFunc.getValue().apply(data);
			hangTagMoreLanguageBaseVO.setPropertiesName(propertiesName);
			if (StrUtil.isAllBlank(propertiesName, propertiesCode)) continue;

			// 封装多值的Code
			String[] keySplit = propertiesCode.split(MoreLanguageProperties.multiSeparator);
			List<String> propertiesCodeList;
			if (keySplit.length <= 1) {
				propertiesCodeList = Arrays.asList(propertiesCode.split(COMMA));
			}else {
				propertiesCodeList = Arrays.asList(keySplit);
			}

			if (CollectionUtil.isNotEmpty(countryLanguageDtoList)) {
				List<CountryLanguageDto> singleLanguageTypeList = singleLanguageDtoList.stream()
						.filter(it -> countryLanguageType == it.getType()).collect(Collectors.toList());
				// 查询具体翻译
				List<StandardColumnCountryTranslate> translateList = new ArrayList<>();
				if (hangTagMoreLanguageDTO.getDecorate()) {
					String titleCode = StrUtil.isNotBlank(codeFunc.getSearchStandardColumnCode())
							? codeFunc.getSearchStandardColumnCode() : standardColumnCode;
					translateList.addAll(standardColumnCountryTranslateService.list(new LambdaQueryWrapper<StandardColumnCountryTranslate>()
							.in(StandardColumnCountryTranslate::getTitleCode, Arrays.asList(titleCode, hangTagMoreLanguageBaseVO.getTitleCode()))
							.in(StandardColumnCountryTranslate::getPropertiesCode, Stream.of(propertiesCodeList, Collections.singletonList(standardColumnCode))
									.flatMap(Collection::stream).collect(Collectors.toList()))
							.in(StandardColumnCountryTranslate::getCountryLanguageId, Stream.of(countryLanguageDtoList, singleLanguageTypeList)
									.flatMap(it -> it.stream().map(CountryLanguage::getId)).collect(Collectors.toList()))
					));
				}
				countryLanguageDtoList.forEach(countryLanguageDto -> {
					List<String> countryLanguageIdList = singleLanguageTypeList.stream().filter(it -> it.getLanguageCode().equals(countryLanguageDto.getLanguageCode()))
							.map(CountryLanguageDto::getId).collect(Collectors.toList());
					countryLanguageIdList.add(countryLanguageDto.getId());

					List<StandardColumnCountryTranslate> countryTranslateList = translateList.stream()
							.filter(it -> countryLanguageIdList.contains(it.getCountryLanguageId()) && StrUtil.isNotBlank(it.getContent()))
							.collect(Collectors.toList());
					languageList.add(buildTranslateResultList(countryTranslateList, standardColumnCode, propertiesCodeList,
							countryLanguageDto, hangTagMoreLanguageBaseVO, statusCheckDetailList));
				});
			}
			else { hangTagMoreLanguageBaseVO.setHasLanguage(false); }
			resultList.add(hangTagMoreLanguageBaseVO);
		}
	}

	private HangTagMoreLanguageVO buildTranslateResultList(List<StandardColumnCountryTranslate> translateList,
														   String standardColumnCode,
														   List<String> propertiesCodeList,
														   CountryLanguageDto countryLanguageDto,
														   HangTagMoreLanguageBaseVO hangTagMoreLanguageBaseVO,
														   List<MoreLanguageStatusCheckDetailDTO> statusCheckDetailList
	) {
		int propertiesCodeSize = propertiesCodeList.size();
		// 检查是否要\n合并
		boolean needFeed = propertiesCodeSize > 1;

		String languageCode = countryLanguageDto.getLanguageCode();
		// 复制基础 languageVo
		HangTagMoreLanguageVO languageVO = HANG_TAG_CV.copy2MoreLanguageVO(countryLanguageDto);
		languageVO.setPropertiesCode(hangTagMoreLanguageBaseVO.getPropertiesCode());
		languageVO.setModel(hangTagMoreLanguageBaseVO.getModel());
		// 设置标准列模式用于判断
		if (!languageVO.forceFindContent()) {
			languageVO.setPropertiesContent(hangTagMoreLanguageBaseVO.getPropertiesName());
		}
		// 已审核包含这个标准类, 修正审核状态
		statusCheckDetailList.stream().filter(it -> languageCode.equals(it.getLanguageCode()))
				.findFirst().ifPresent(checkDetailDTO ->
						checkDetailDTO.getAuditList().forEach(it -> {
							if (standardColumnCode.equals(it.getSource())) {
								// 找到 设置翻译 以及 flag
								languageVO.setCannotFindStandardColumnContent(false);
								languageVO.setStandardColumnContent(it.getContent());
								languageVO.setTitleAuditStatus(StyleCountryStatusEnum.findByCode(it.getStatus()));
							}
							if (standardColumnCode.equals(it.getStandardColumnCode()) && propertiesCodeList.contains(it.getSource())) {
								// 找到 设置翻译 以及 flag
								languageVO.setCannotFindPropertiesContent(false);
								languageVO.setPropertiesContent(it.getContent());
								languageVO.setContentAuditStatus(StyleCountryStatusEnum.findByCode(it.getStatus()));
							}
						})
				);
		if (languageVO.getCannotFindStandardColumnContent()) {
			translateList.stream().filter(it -> it.getPropertiesCode().equals(standardColumnCode)).findFirst().ifPresent(titleTranslate -> {
				// 找到 设置翻译 以及 flag
				languageVO.setCannotFindStandardColumnContent(false);
				languageVO.setStandardColumnContent(titleTranslate.getContent());
			});
		}
		if (languageVO.getCannotFindPropertiesContent()) {
			List<StandardColumnCountryTranslate> countryTranslateList = translateList.stream()
					.filter(it -> it.getTitleCode().equals(standardColumnCode))
					.collect(Collectors.toList());

			countryTranslateList.stream().findFirst().ifPresent(translate -> {
				//  找到 设置翻译 以及 flag
				languageVO.setCannotFindPropertiesContent(false);
				// 再复制, 主要是翻译时间
				HANG_TAG_CV.countryTranslate2MoreLanguageVO(translate, languageVO);
				String content;
				// 需要合并 就多个合并
				if (needFeed) {
					List<String> propertiesCountryTranslateList = countryTranslateList.stream()
							.filter(CommonUtils.distinctByKey(StandardColumnCountryTranslate::getPropertiesCode))
							.sorted(Comparator.comparing(it -> propertiesCodeList.indexOf(it.getPropertiesCode())))
							.map(StandardColumnCountryTranslate::getContent).collect(Collectors.toList());
					if (propertiesCountryTranslateList.size() < propertiesCodeSize) {
						languageVO.setCannotFindPropertiesContent(true);
					}
					content = String.join(MoreLanguageProperties.multiSeparator, propertiesCountryTranslateList);
				} else {
					content = translate.getContent();
				}
				languageVO.setPropertiesContent(content);
			});
		}

		return languageVO;
	}

	private Object decorateResultList(SystemSource source, List<HangTagMoreLanguageBaseVO> resultList, List<MoreLanguageHangTagVO> hangTagVOList) {
		switch (source) {
			case SYSTEM:
				return resultList;
			case PDM:
				resultList.removeIf(it-> it.getShowFlag() == YesOrNoEnum.NO);
				List<HangTagMoreLanguageWebBaseVO> webBaseList = HANG_TAG_CV.copyList2Web(resultList);
				decorateWebList(hangTagVOList, webBaseList);
				webBaseList.forEach(webBaseVO-> webBaseVO.getLanguageList().removeIf(it-> MoreLanguageProperties.isInternalLanguageCode(it.getLanguageCode())));
				return webBaseList.stream().collect(Collectors.groupingBy(HangTagMoreLanguageWebBaseVO::getType));
			case BCS:
			case ESCM:
				resultList.forEach(result-> result.setStatus(hangTagVOList.stream().filter(it->
						it.getBulkStyleNo().equals(result.getBulkStyleNo())
				).findFirst().map(MoreLanguageHangTagVO::getStatus).orElse(HangTagStatusEnum.FINISH)));
				resultList.removeIf(it-> it.getShowFlag() == YesOrNoEnum.NO);
				List<HangTagMoreLanguageBCSVO> sourceResultList = new ArrayList<>();
				HANG_TAG_CV.copyList2Bcs(resultList).stream().collect(Collectors.groupingBy(HangTagMoreLanguageBaseVO::getCode))
						.forEach((code, sameBulkStyleNoList)-> {
							sourceResultList.add(new HangTagMoreLanguageBCSVO(sameBulkStyleNoList));
						});
				return sourceResultList;
			case PRINT:
				// 假定为只传一个款
				TagPrinting tagPrinting = hangTagPrinting(hangTagVOList).get(0);
				tagPrinting.setC8_APPBOM_StorageReq(null);

				String bulkStyleNo = tagPrinting.getStyleCode();
//				List<HangTagMoreLanguagePrinterBaseVO> hangTagMoreLanguagePrinterBaseVOS = HANG_TAG_CV.copyList2Print(resultList);

				// 多国家
				List<MoreLanguageTagPrintingList> tagPrintingResultList = new ArrayList<>();
				// 假定单国家
				List<String> countryCodeList = resultList.stream().map(HangTagMoreLanguageBaseVO::getCode).collect(Collectors.toList());
//				List<StyleCountryStatus> countryStatusList = styleCountryStatusService.list(new BaseLambdaQueryWrapper<StyleCountryStatus>()
//						.notEmptyIn(StyleCountryStatus::getCountryCode, countryCodeList)
//						.eq(StyleCountryStatus::getBulkStyleNo, bulkStyleNo)
//						.ne(StyleCountryStatus::getStatus, StyleCountryStatusEnum.UNCHECK)
//				);
				resultList.stream().collect(Collectors.groupingBy(HangTagMoreLanguageBaseVO::getCode)).forEach((code, sameCodeList)-> {
					List<MoreLanguageTagPrinting> tagPrintingList = new ArrayList<>();
					// 获取所有的语言
					sameCodeList.stream().flatMap(it-> it.getLanguageList().stream().map(HangTagMoreLanguageVO::getLanguageCode)).distinct().forEach(languageCode-> {
						MoreLanguageTagPrinting printing = HANG_TAG_CV.copy2MoreLanguage(tagPrinting);
						Map<String, CodeMapping<?>> codeMap = printing.getCodeMap();

						for (HangTagMoreLanguageBaseVO result : sameCodeList) {
							String standardColumnCode = result.getStandardColumnCode();

							Optional<HangTagMoreLanguageVO> languageVoOpt = result.getLanguageList().stream().filter(it -> it.getLanguageCode().equals(languageCode)).findFirst();
							if (!languageVoOpt.isPresent()) continue;
							HangTagMoreLanguageVO languageVO = languageVoOpt.get();
							if (!codeMap.containsKey(standardColumnCode)) continue;

							CodeMapping<?> codeMapping = codeMap.get(standardColumnCode);
							printing.getTitleMap().put(codeMapping.getTitleCode(), codeMapping.getTitleName());
							if (!MoreLanguageProperties.internalLanguageCode.equals(languageCode)) {

								Function<MoreLanguageTagPrinting, ? extends List<?>> listFunc = codeMapping.getListFunc();
								if (listFunc == null) listFunc = MoreLanguageTagPrinting::getMySelfList;

								String titleContent = Opt.ofBlankAble(languageVO.getStandardColumnContent()).orElse(MoreLanguageProperties.isInternalLanguageCode(languageCode) ? result.getStandardColumnName() : "");
								printing.getTitleMap().put(codeMapping.getTitleCode(), titleContent);

								if (codeMapping.getMapping() != null) {
									Function<Object, String> codeFunc = (Function<Object, String>) codeMapping.getMapping().getKey();
									BiConsumer<Object, String> valueFunc = (BiConsumer<Object, String>) codeMapping.getMapping().getValue();
									List<?> list = listFunc.apply(printing);
									for (Object dataObj : list) {
										String sourceStr = codeFunc.apply(dataObj);
										String str = StrUtil.replace(sourceStr, result.getPropertiesName(), languageVO.getPropertiesContent());
										if (!sourceStr.equals(str)) {
											valueFunc.accept(dataObj, str);
										}
									}
								}
							}
							printing.setLanguageName(languageVO.getLanguageName());
						}

						// 全部审核完才为true，所以直接判断吊牌状态即可
//						printing.setTranslateApproved(countryStatusList.stream().anyMatch(it->
//								it.getBulkStyleNo().equals(bulkStyleNo) && it.getCountryCode().equals(code))
//						);
						tagPrintingList.add(printing);
					});
					tagPrintingResultList.add(new MoreLanguageTagPrintingList(tagPrintingList));
				});
				return tagPrintingResultList;
			default: throw new IllegalStateException("Unexpected value: " + source);
		}
	}

	@Override
	public boolean counterReview(HangTag reviewHangTag) {
//		HangTagStatusEnum status = reviewHangTag.getStatus();

		HangTag hangTag = this.getById(reviewHangTag.getId());
//		if (Arrays.asList(HangTagStatusEnum.TECH_CHECK, HangTagStatusEnum.SUSPEND, HangTagStatusEnum.QC_CHECK).contains(status)) {
//			hangTag.setStatus(HangTagStatusEnum.DESIGN_CHECK);
			hangTag.setStatus(HangTagStatusEnum.NOT_COMMIT);
//		}
//		if (HangTagStatusEnum.TRANSLATE_CHECK == status) {
//			hangTag.setStatus(HangTagStatusEnum.QC_CHECK);
//		}else {
//			hangTag.setStatus(HangTagStatusEnum.DESIGN_CHECK);
//		}

		smpService.tagConfirmDates(Arrays.asList(hangTag.getId()), HangTagDeliverySCMStatusEnum.TAG_LIST_CANCEL, 0);
		boolean update = this.updateById(hangTag);

		// 删除审核状态
		styleCountryStatusMapper.update(null, new LambdaUpdateWrapper<StyleCountryStatus>()
				.set(StyleCountryStatus::getStatus, StyleCountryStatusEnum.UNCHECK)
				.eq(StyleCountryStatus::getBulkStyleNo, hangTag.getBulkStyleNo())
				.ne(StyleCountryStatus::getStatus, StyleCountryStatusEnum.UNCHECK)
		);
		return update;
	}


	/**
	 * 通过物料编码获取检查报告
	 * @param dto
	 * @return
	 */
	@Override
	public List<EscmMaterialCompnentInspectCompanyDto> getInspectReport(InspectCompanyDto dto) {
		if(StrUtil.isEmpty(dto.getMaterialsNo())){
			throw new OtherException("物料编码不能为空");
		}
		QueryWrapper<EscmMaterialCompnentInspectCompanyDto> queryWrapper = new QueryWrapper<>();
		queryWrapper.in("materials_no", com.base.sbc.config.utils.StringUtils.convertList(dto.getMaterialsNo()));
		queryWrapper.eq(StrUtil.isNotBlank(dto.getYear()),"year",dto.getYear());
		queryWrapper.eq(StrUtil.isNotBlank(dto.getMaterialsName()),"materials_name", dto.getMaterialsName());
		/*查询基础资料-品类测量组数据*/

		List<EscmMaterialCompnentInspectCompanyDto> list = escmMaterialCompnentInspectCompanyService.getListByMaterialsNo(queryWrapper);
		return list;
	}

	private void decorateWebList(List<MoreLanguageHangTagVO> hangTagVOList, List<HangTagMoreLanguageWebBaseVO> webBaseList){
		// 设置分组
		Map<String, HangTagMoreLanguageGroup> groupMap = MapUtil.ofEntries(
				MapUtil.entry("DP16", new HangTagMoreLanguageGroup("DP09,DP11,DP10,DP13", "成分信息",MoreLanguageHangTagVO::getIngredient)),
				MapUtil.entry("DP06", new HangTagMoreLanguageGroup(null)),
				MapUtil.entry("DP12", new HangTagMoreLanguageGroup(MoreLanguageHangTagVO::getDownContent))
		);
		List<HangTagMoreLanguageWebBaseVO> groupList = new ArrayList<>();
		// 根据标准列编码+款号分组
		webBaseList.stream().collect(CommonUtils.groupingBy(it-> it.getCode() + "——————" + it.getBulkStyleNo())).forEach((code, sameCodeList)-> {
			String[] codeSplit = code.split("——————");
			String bulkStyleNo = codeSplit[1];
			// 遍历分组设置

			groupMap.forEach((groupName, group)-> {
				String standColumnCode = group.getStandColumnCode();
				// 获取数据中存在与分组名相同或分组标准列包含的列表
				List<HangTagMoreLanguageWebBaseVO> list = sameCodeList.stream()
						.filter(it -> groupName.equals(it.getStandardColumnCode())).collect(Collectors.toList());
				boolean notChoose = CollectionUtil.isEmpty(list);
				if (StrUtil.isNotBlank(standColumnCode)) {
					list.addAll(Arrays.stream(standColumnCode.split(","))
							.flatMap(it-> sameCodeList.stream()
									.filter(webBaseVO -> it.equals(webBaseVO.getStandardColumnCode()))
							).collect(Collectors.toList())
					);
				}
				if (CollectionUtil.isNotEmpty(list)) {
					// 源数据先移除
					webBaseList.removeAll(list);

					// 再次获取吊牌数据
					MoreLanguageHangTagVO hangTagVO = hangTagVOList.stream().filter(it -> it.getBulkStyleNo().equals(bulkStyleNo)).findFirst().get();
					HangTagMoreLanguageWebBaseVO webBaseVO = list.get(0);

					// 深拷贝 + 基础设置
					HangTagMoreLanguageWebBaseVO groupVO = HANG_TAG_CV.copyMyself(webBaseVO);
					groupVO.setIsGroup(true);
					groupVO.setStandardColumnCode(groupName);
					if (notChoose) {
						// 如果是分组名没对上,分组标准列码对上了,进行设置基础的名字和翻译
						groupVO.setStandardColumnName(group.getStandColumnName());
						groupVO.getLanguageList().forEach(languageVo-> {
							languageVo.setStandardColumnContent(group.getStandColumnName());
						});
					}
					// 一一设置对应的属性码属性名
					groupVO.setPropertiesCode(list.stream().map(HangTagMoreLanguageWebBaseVO::getPropertiesCode).distinct().collect(Collectors.joining(COMMA)));

					String separator = group.getSeparator();
					Function<MoreLanguageHangTagVO, String> content = group.getContent();
					String propertiesName;
					if (content != null) {
						propertiesName = content.apply(hangTagVO);
					}else {
						propertiesName = list.stream().map(HangTagMoreLanguageWebBaseVO::getPropertiesName).distinct().collect(Collectors.joining(separator));
					}
					groupVO.getLanguageList().forEach(languageVo-> {
						languageVo.setPropertiesContent(propertiesName);
						languageVo.setCannotFindPropertiesContent(false);
						languageVo.setIsGroup(true);
					});
					groupVO.getLanguageList().forEach(languageVo-> {
						list.stream().collect(Collectors.groupingBy((it)-> StrUtil.isNotBlank(it.getPropertiesName()) ? it.getPropertiesName() : ""))
								.forEach((key, sameNameLanguageList)-> {
									// 获取源数据中对应原本的翻译
									String value = sameNameLanguageList.get(0).getLanguageList().stream()
											.filter(it -> it.getLanguageCode().equals(languageVo.getLanguageCode()))
											.map(HangTagMoreLanguageVO::getPropertiesContent)
											.filter(StrUtil::isNotBlank).findFirst().orElse(" ");
									// 进行组合
									String s = languageVo.getPropertiesContent();
									String s1 = StrUtil.replace(s, key, value);
									if (StrUtil.isBlank(value) || s.equals(s1)) {
										languageVo.setCannotFindPropertiesContent(true);
									}
									languageVo.setPropertiesContent(s1);
									if (notChoose) {
										languageVo.setStandardColumnContent(StrUtil.replace(languageVo.getStandardColumnContent(), key, value));
									}
								});
						if (languageVo.getStandardColumnContent().equals(group.getStandColumnName())) {
							languageVo.setCannotFindStandardColumnContent(true);
						}
						// 若还是和之前一样，那就是没找到翻译
						String fillSeparator = MoreLanguageProperties.showInfoLanguageSeparator + MoreLanguageProperties.multiSeparator;
						String groupContent = StrUtil.replace(languageVo.propertiesContent, MoreLanguageProperties.multiSeparator, fillSeparator);
						languageVo.setPropertiesContent(groupContent);
					});
					groupVO.setPropertiesName(propertiesName);

					groupList.add(groupVO);
				}
			});
		});
		webBaseList.addAll(groupList);
	}

// 自定义方法区 不替换的区域【other_end】

}
