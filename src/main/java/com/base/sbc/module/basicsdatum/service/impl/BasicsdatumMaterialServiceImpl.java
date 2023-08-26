/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.service.impl;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.client.flowable.entity.AnswerDto;
import com.base.sbc.client.flowable.service.FlowableService;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.*;
import com.base.sbc.module.basicsdatum.controller.BasicsdatumMaterialController;
import com.base.sbc.module.basicsdatum.dto.*;
import com.base.sbc.module.basicsdatum.entity.*;
import com.base.sbc.module.basicsdatum.enums.BasicsdatumMaterialBizTypeEnum;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumMaterialMapper;
import com.base.sbc.module.basicsdatum.service.*;
import com.base.sbc.module.basicsdatum.vo.*;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.fabric.service.BasicFabricLibraryService;
import com.base.sbc.module.pack.vo.BomSelMaterialVo;
import com.base.sbc.module.smp.SmpService;
import com.base.sbc.open.entity.EscmMaterialCompnentInspectCompanyDto;
import com.base.sbc.open.service.EscmMaterialCompnentInspectCompanyService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.base.sbc.config.adviceAdapter.ResponseControllerAdvice.companyUserInfo;

/**
 * 类描述：基础资料-物料档案 service类
 *
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialService
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 创建时间：2023-6-26 17:57:17
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class BasicsdatumMaterialServiceImpl extends BaseServiceImpl<BasicsdatumMaterialMapper, BasicsdatumMaterial>
		implements BasicsdatumMaterialService {

	private final SpecificationService specificationService;
	private final SpecificationGroupService specificationGroupService;
	private final BasicsdatumMaterialOldService materialOldService;
	private final BasicsdatumMaterialWidthService materialWidthService;
	private final BasicsdatumMaterialColorService materialColorService;
	private final BasicsdatumMaterialPriceService materialPriceService;
	private final BasicsdatumMaterialIngredientService materialIngredientService;
	private final BasicsdatumMaterialPriceDetailService basicsdatumMaterialPriceDetailService;
	private final EscmMaterialCompnentInspectCompanyService escmMaterialCompnentInspectCompanyService;
	private final FlowableService flowableService;

	@Autowired
	private BasicFabricLibraryService basicFabricLibraryService;

	/**
	 * 解决循环依赖报错的问题
	 */
	@Lazy
	@Resource
	private SmpService smpService;

	@ApiOperation(value = "主物料成分转换")
	@GetMapping("/formatIngredient")
	public List<BasicsdatumMaterialIngredient> formatIngredient(
			@RequestParam(value = "value", required = false) String value) {
		String str = IngredientUtils.format(value);
		return formatToList(str);
	}

	/**
	 * 转换为对象集合
	 *
	 * @param str
	 * @return
	 */
	private List<BasicsdatumMaterialIngredient> formatToList(String str) {
		String[] strs = str.split(",");
		List<BasicsdatumMaterialIngredient> list = new ArrayList<>();
		for (int i = 0; i < strs.length; i++) {
			String ingredients = strs[i];
			BasicsdatumMaterialIngredient in = new BasicsdatumMaterialIngredient();
			in.setRatio(BigDecimalUtil.valueOf(ingredients.split("%")[0]));
			String nameSay = ingredients.split("%")[1];
			int kidx = nameSay.indexOf("(");
			String name = nameSay;
			String say = "";
			if (kidx != -1) {
				name = nameSay.substring(0, kidx);
				say = nameSay.substring(kidx + 1, nameSay.length() - 1);
			}
			in.setName(name);
			in.setSay(say);
			list.add(in);
		}
		return list;
	}

	@Override
	public PageInfo<BasicsdatumMaterialPageVo> getBasicsdatumMaterialList(BasicsdatumMaterialQueryDto dto) {
		if (dto.getPageNum() != 0 && dto.getPageSize() != 0) {
			PageHelper.startPage(dto);
		}
		BaseQueryWrapper<BasicsdatumMaterial> qc = new BaseQueryWrapper<>();
		qc.eq("company_code", this.getCompanyCode());
		qc.andLike(dto.getSearch(), "material_code", "material_name");
		qc.notEmptyEq("status", dto.getStatus());
		qc.notEmptyLike("material_code_name", dto.getMaterialCodeName());
		qc.notEmptyLike("supplier_name", dto.getSupplierName());
		qc.notEmptyLike("material_code", dto.getMaterialCode());
		qc.notEmptyLike("material_name", dto.getMaterialName());
		if (StringUtils.isNotEmpty(dto.getCategoryId())) {
			qc.and(Wrapper -> Wrapper.eq("category_id", dto.getCategoryId()).or()
					.eq("category1_code ", dto.getCategoryId()).or().eq("category2_code", dto.getCategoryId()).or()
					.eq("category3_code", dto.getCategoryId()));
		}
		qc.eq("biz_type", BasicsdatumMaterialBizTypeEnum.MATERIAL.getK());
		if (StringUtils.isNotEmpty(dto.getConfirmStatus())) {
			List<String> confirmStatus = Arrays.stream(dto.getConfirmStatus().split(",")).collect(Collectors.toList());
			qc.in("confirm_status", confirmStatus);
		} else {
			qc.eq("confirm_status", "2");
		}
		List<BasicsdatumMaterial> list = this.list(qc);
		PageInfo<BasicsdatumMaterialPageVo> copy = CopyUtil.copy(new PageInfo<>(list), BasicsdatumMaterialPageVo.class);

		for (BasicsdatumMaterialPageVo basicsdatumMaterialPageVo : copy.getList()) {
			String materialCode = basicsdatumMaterialPageVo.getMaterialCode();
			EscmMaterialCompnentInspectCompanyDto escmMaterialCompnentInspectCompanyDto = escmMaterialCompnentInspectCompanyService.getOne(new QueryWrapper<EscmMaterialCompnentInspectCompanyDto>().eq("materials_no", materialCode));
			if (escmMaterialCompnentInspectCompanyDto!=null){
				basicsdatumMaterialPageVo.setFabricEvaluation(escmMaterialCompnentInspectCompanyDto.getRemark());
				basicsdatumMaterialPageVo.setCheckCompanyName(escmMaterialCompnentInspectCompanyDto.getCompanyFullName());
				basicsdatumMaterialPageVo.setCheckDate(escmMaterialCompnentInspectCompanyDto.getArriveDate());
				basicsdatumMaterialPageVo.setCheckValidDate(String.valueOf(escmMaterialCompnentInspectCompanyDto.getValidityTime()));
				basicsdatumMaterialPageVo.setCheckItems(escmMaterialCompnentInspectCompanyDto.getSendInspectContent());
				basicsdatumMaterialPageVo.setCheckOrderUserName(escmMaterialCompnentInspectCompanyDto.getMakerByName());
				basicsdatumMaterialPageVo.setCheckFileUrl(escmMaterialCompnentInspectCompanyDto.getFileUrl());
			}

		}
		return copy;
	}

	@Override
	public BasicsdatumMaterialVo saveBasicsdatumMaterial(BasicsdatumMaterialSaveDto dto) {
		BasicsdatumMaterial entity = CopyUtil.copy(dto, BasicsdatumMaterial.class);
		if ("-1".equals(entity.getId())) {
			entity.setId(null);
			entity.setStatus("0");
			String categoryCode = entity.getMaterialCode();
			// 获取并放入最大code
			entity.setMaterialCode(getMaxCode(categoryCode));
		}
		entity.setMaterialCodeName(entity.getMaterialCode() + entity.getMaterialName());

		// 特殊逻辑： 如果是面料的时候，需要增加门幅幅宽的数据 给到物料规格
		// if ("fabric".equals(entity.getMaterialType())) {
		// this.saveFabricWidth(entity.getMaterialCode(),
		// BigDecimalUtil.convertString(entity.getTranslate()));
		// }
		// 如果成分不为空,则清理替换成分信息
		this.saveIngredient(dto);

		if (entity.getId() != null && "1".equals(entity.getDistribute())) {
			smpService.materials(entity.getId().split(","));
		}
		// 保存主信息
		this.saveOrUpdate(entity);
		return getBasicsdatumMaterial(entity.getId());
	}

	/**
	 * 保存成分数据
	 *
	 * @param dto
	 */
	private void saveIngredient(BasicsdatumMaterialSaveDto dto) {
		UserCompany userCompany = companyUserInfo.get();
		String userName = userCompany.getAliasUserName();
		String userId = userCompany.getUserId();
		if (dto.getIngredientList() != null) {
			materialIngredientService.remove(new QueryWrapper<BasicsdatumMaterialIngredient>()
					.eq(COMPANY_CODE, getCompanyCode()).eq("material_code", dto.getMaterialCode()).eq("type", "0"));
			for (BasicsdatumMaterialIngredient item : dto.getIngredientList()) {
				item.setCompanyCode(this.getCompanyCode());
				item.setCreateId(userId);
				item.setCreateName(userName);

			}
			materialIngredientService.saveBatch(dto.getIngredientList());
		}
		if (dto.getFactoryCompositionList() != null) {
			materialIngredientService.remove(new QueryWrapper<BasicsdatumMaterialIngredient>()
					.eq(COMPANY_CODE, getCompanyCode()).eq("material_code", dto.getMaterialCode()).eq("type", "1"));
			for (BasicsdatumMaterialIngredient item : dto.getFactoryCompositionList()) {
				item.setCompanyCode(this.getCompanyCode());
				item.setCreateId(userId);
				item.setCreateName(userName);
			}
			materialIngredientService.saveBatch(dto.getFactoryCompositionList());
		}

	}

	/**
	 * 如果是面料的时候，需要增加门幅幅宽的数据 给到物料规格并保持一个规格
	 *
	 * @param materialCode
	 * @param widthCode
	 */
	private void saveFabricWidth(String materialCode, String widthCode) {
		List<BasicsdatumMaterialWidth> list = this.materialWidthService
				.list(new QueryWrapper<BasicsdatumMaterialWidth>().eq("company_code", this.getCompanyCode())
						.eq("material_code", materialCode));
		if (list != null && list.size() > 0) {
			// 如果存在多个，第一个如果不同则修改
			BasicsdatumMaterialWidth width = list.get(0);
			if (!width.getWidthCode().equals(widthCode)) {
				width.setWidthCode(widthCode);
				this.materialWidthService.updateById(width);
			}
			// 如果还有其他的进行移除
			if (list.size() > 1) {
				List<String> ids = new ArrayList<>();
				for (int i = 1; i < list.size(); i++) {
					ids.add(list.get(i).getId());
				}
				this.materialWidthService.removeBatchByIds(ids);
			}

		} else {
			BasicsdatumMaterialWidth one = new BasicsdatumMaterialWidth();
			one.setCompanyCode(this.getCompanyCode());
			one.setWidthCode(widthCode);
			one.setName(widthCode);
			one.setMaterialCode(materialCode);
			this.materialWidthService.save(one);
		}
	}

	private String getMaxCode(String categoryCode) {
		BaseQueryWrapper<BasicsdatumMaterial> qc = new BaseQueryWrapper<>();
		qc.select("material_code");
		qc.eq("company_code", this.getCompanyCode());
		qc.eq("del_flag", "0").or().eq("del_flag", "1");
//		qc.eq(" length(material_code)", categoryCode.length() + 5);
//		qc.likeRight("material_code", categoryCode);
		qc.orderByDesc(" create_date ");
		qc.last(" limit 1 ");
		BasicsdatumMaterial one = this.baseMapper.selectOne(qc);
		if (one != null) {
			String code = one.getMaterialCode();// code.replace(categoryCode, "")
			Integer replace = Integer.parseInt(code.substring(code.length() - 5));
			return categoryCode + String.format("%05d", replace + 1);
		} else {
			return categoryCode + "00001";
		}
	}

	@Override
	public Boolean startStopBasicsdatumMaterial(StartStopDto dto) {
		UpdateWrapper<BasicsdatumMaterial> uw = new UpdateWrapper<>();
		uw.in("id", StringUtils.convertList(dto.getIds()));
		uw.set("status", dto.getStatus());

		QueryWrapper<BasicsdatumMaterial> queryWrapper = new QueryWrapper<>();
		queryWrapper.in("id", dto.getIds());
		for (BasicsdatumMaterial basicsdatumMaterial : this.list(queryWrapper)) {
			if ("1".equals(basicsdatumMaterial.getDistribute())) {
				smpService.materials(basicsdatumMaterial.getId().split(","));
			}
		}

		return this.update(null, uw);
	}

	@Override
	public Boolean delBasicsdatumMaterial(String id) {
		List<String> list = StringUtils.convertList(id);
		BaseQueryWrapper<BasicsdatumMaterial> qc = new BaseQueryWrapper<>();
		qc.select("material_code");
		qc.eq("company_code", this.getCompanyCode());
		List<String> list2 = this.list(qc).stream().map(BasicsdatumMaterial::getMaterialCode)
				.collect(Collectors.toList());
		// 删除主表
		this.removeBatchByIds(list);
		// 删除子表颜色和规格及报价
		this.materialWidthService.remove(new QueryWrapper<BasicsdatumMaterialWidth>()
				.eq("company_code", this.getCompanyCode()).in("material_code", list2));
		this.materialColorService.remove(new QueryWrapper<BasicsdatumMaterialColor>()
				.eq("company_code", this.getCompanyCode()).in("material_code", list2));
		this.materialPriceService.remove(new QueryWrapper<BasicsdatumMaterialPrice>()
				.eq("company_code", this.getCompanyCode()).in("material_code", list2));
		return true;
	}

	@Override
	public void exportBasicsdatumMaterial(HttpServletResponse response, BasicsdatumMaterialQueryDto dto)
			throws IOException {
		BaseQueryWrapper<BasicsdatumMaterial> qc = new BaseQueryWrapper<>();
		qc.eq("company_code", this.getCompanyCode());
		qc.notEmptyEq("status", dto.getStatus());
		qc.notEmptyLike("material_code_name", dto.getMaterialCodeName());
		qc.notEmptyLike("supplier_name", dto.getSupplierName());
		qc.notEmptyLike("material_code", dto.getMaterialCode());
		qc.notEmptyLike("material_name", dto.getMaterialName());
		if (StringUtils.isNotEmpty(dto.getCategoryId())) {
			qc.and(Wrapper -> Wrapper.eq("category_id", dto.getCategoryId()).or().like("category_ids ",
					dto.getCategoryId()));
		}
		List<BasicsdatumMaterialExcelVo> list = CopyUtil.copy(this.list(qc), BasicsdatumMaterialExcelVo.class);
		ExcelUtils.exportExcel(list, BasicsdatumMaterialExcelVo.class, "物料档案.xls", new ExportParams(), response);
	}

	@Override
	public PageInfo<BomSelMaterialVo> getBomSelMaterialList(BasicsdatumMaterialQueryDto dto) {
		BaseQueryWrapper<BasicsdatumMaterial> qw = new BaseQueryWrapper<>();
		qw.andLike(dto.getSearch(), "material_code", "material_name");
		qw.eq("company_code", this.getCompanyCode());
		qw.notEmptyLike("material_code_name", dto.getMaterialCodeName());
		qw.notEmptyLike("material_code", dto.getMaterialCode());
		qw.notEmptyLike("material_name", dto.getMaterialName());
		qw.notEmptyIn("status", dto.getStatus());
		qw.eq("del_flag", BaseGlobal.NO);
		qw.andLike(dto.getCategoryId(), "category1_code", "category2_code", "category3_code");
		qw.eq("biz_type", BasicsdatumMaterialBizTypeEnum.MATERIAL.getK());
		qw.eq("confirm_status", "2");
		Page<BomSelMaterialVo> page = PageHelper.startPage(dto);
		List<BomSelMaterialVo> list = getBaseMapper().getBomSelMaterialList(qw);

		if (CollUtil.isNotEmpty(list)) {
			//查询默认供应商
			List<String> materialCodeList = list.stream().map(BomSelMaterialVo::getMaterialCode).filter(StrUtil::isNotBlank).collect(Collectors.toList());
			List<BomSelMaterialVo> priceList = materialPriceService.findDefaultToBomSel(materialCodeList);
			List<BomSelMaterialVo> widthList = materialWidthService.findDefaultToBomSel(materialCodeList);
			Map<String, BomSelMaterialVo> priceMap = Opt.ofEmptyAble(priceList)
					.map(item -> item.stream().collect(Collectors.toMap(k -> k.getMaterialCode(), v -> v, (a, b) -> a)))
					.orElse(MapUtil.empty());
			Map<String, BomSelMaterialVo> widthMap = Opt.ofEmptyAble(widthList)
					.map(item -> item.stream().collect(Collectors.toMap(k -> k.getMaterialCode(), v -> v, (a, b) -> a)))
					.orElse(MapUtil.empty());
			list.forEach(i -> {
				BomSelMaterialVo priceInfo = priceMap.get(i.getMaterialCode());
				BomSelMaterialVo widthInfo = widthMap.get(i.getMaterialCode());
				BeanUtil.copyProperties(priceInfo, i, CopyOptions.create().ignoreNullValue());
				BeanUtil.copyProperties(widthInfo, i, CopyOptions.create().ignoreNullValue());
				i.setId(IdUtil.randomUUID());
			});
		}
		return page.toPageInfo();
	}

	/**
	 * 查询详情
	 */
	@Override
	public BasicsdatumMaterialVo getBasicsdatumMaterial(String id) {
		BasicsdatumMaterial material = this.getById(id);
		List<BasicsdatumMaterialColorSelectVo> select = this.baseMapper
				.getBasicsdatumMaterialColorSelect(this.getCompanyCode(), material.getMaterialCode());
		List<BasicsdatumMaterialWidthSelectVo> select2 = this.baseMapper
				.getBasicsdatumMaterialWidthSelect(this.getCompanyCode(), material.getMaterialCode());
		BasicsdatumMaterialVo copy = CopyUtil.copy(material, BasicsdatumMaterialVo.class);
		// 填充规格规格组
		if (select2 != null && select2.size() > 0) {
			StringBuffer width = new StringBuffer();
			StringBuffer widthName = new StringBuffer();
			select2.forEach(item -> {
				BasicsdatumMaterialWidthSelectVo s = (item);
				width.append(",").append(s.getCode());
				widthName.append(",").append(s.getName());
			});
			copy.setWidth(width.substring(1));
			copy.setWidthName(widthName.substring(1));
		}
		copy.setColorList(select);
		copy.setWidthList(select2);
		// 获取成分子表的数据
		List<BasicsdatumMaterialIngredient> list = materialIngredientService
				.list(new QueryWrapper<BasicsdatumMaterialIngredient>().eq("company_code", this.getCompanyCode())
						.eq("material_code", material.getMaterialCode()));
		if (list != null && list.size() > 0) {
			copy.setIngredientList(
					list.stream().filter(item -> "0".equals(item.getType())).collect(Collectors.toList()));
			copy.setFactoryCompositionList(
					list.stream().filter(item -> "1".equals(item.getType())).collect(Collectors.toList()));
		}

		return copy;
	}

	@Override
	public PageInfo<BasicsdatumMaterialWidthPageVo> getBasicsdatumMaterialWidthList(
			BasicsdatumMaterialWidthQueryDto dto) {
		if (dto.getPageNum() != 0 && dto.getPageSize() != 0) {
			PageHelper.startPage(dto);
		}
		List<BasicsdatumMaterialWidthPageVo> list = this.baseMapper
				.getBasicsdatumMaterialWidthList(this.getCompanyCode(), dto.getMaterialCode(), dto.getStatus());
		return new PageInfo<>(list);
	}

	@Override
	public Boolean saveBasicsdatumMaterialWidth(BasicsdatumMaterialWidthSaveDto dto) {
		long count = this.materialWidthService.count(new QueryWrapper<BasicsdatumMaterialWidth>().ne("id", dto.getId())
				.eq("company_code", this.getCompanyCode()).eq("Material_Code", dto.getMaterialCode())
				.eq("Width_Code", dto.getWidthCode()));
		if (count > 0) {
			throw new OtherException("当前规格已存在");
		}
		BasicsdatumMaterialWidth entity = CopyUtil.copy(dto, BasicsdatumMaterialWidth.class);
		if ("-1".equals(entity.getId())) {
			entity.setId(null);
		}
		return this.materialWidthService.saveOrUpdate(entity);
	}

	@Override
	public Boolean startStopBasicsdatumMaterialWidth(StartStopDto dto) {
		UpdateWrapper<BasicsdatumMaterialWidth> uw = new UpdateWrapper<>();
		uw.in("id", StringUtils.convertList(dto.getIds()));
		uw.set("status", dto.getStatus());

		//停用需要校验下游系统是否引用
		if ("1".equals(dto.getStatus())){
			QueryWrapper<BasicsdatumMaterialWidth> queryWrapper= new BaseQueryWrapper<>();
			queryWrapper.in("id",StringUtils.convertList(dto.getIds()));
			List<BasicsdatumMaterialWidth> list = materialWidthService.list(queryWrapper);
			for (BasicsdatumMaterialWidth basicsdatumMaterialWidth : list) {
				Boolean b = smpService.checkSizeAndColor(basicsdatumMaterialWidth.getMaterialCode(), "1", basicsdatumMaterialWidth.getWidthCode());
				if (!b){
					throw new OtherException("\""+basicsdatumMaterialWidth.getName()+"\"下游系统以引用,不允许停用");
				}
			}
		}

		return this.materialWidthService.update(null, uw);
	}

	@Override
	public Boolean delBasicsdatumMaterialWidth(String id) {
		return this.materialWidthService.removeBatchByIds(StringUtils.convertList(id));
	}

	@Override
	public PageInfo<BasicsdatumMaterialColorPageVo> getBasicsdatumMaterialColorList(
			BasicsdatumMaterialColorQueryDto dto) {
		if (dto.getPageNum() != 0 && dto.getPageSize() != 0) {
			PageHelper.startPage(dto);
		}
		List<BasicsdatumMaterialColorPageVo> list = this.baseMapper
				.getBasicsdatumMaterialColorList(this.getCompanyCode(), dto.getMaterialCode(), dto.getStatus());
		return new PageInfo<>(list);
	}

	@Override
	public Boolean saveBasicsdatumMaterialColor(BasicsdatumMaterialColorSaveDto dto) {
		long count = this.materialColorService.count(new QueryWrapper<BasicsdatumMaterialColor>().ne("id", dto.getId())
				.eq("company_code", this.getCompanyCode()).eq("Material_Code", dto.getMaterialCode())
				.eq("color_Code", dto.getColorCode()));
		if (count > 0) {
			throw new OtherException("当前颜色已存在");
		}
		BasicsdatumMaterialColor entity = CopyUtil.copy(dto, BasicsdatumMaterialColor.class);
		if ("-1".equals(entity.getId())) {
			entity.setId(null);
		}
		return this.materialColorService.saveOrUpdate(entity);
	}

	@Override
	public Boolean delBasicsdatumMaterialColor(String id) {
		return this.materialColorService.removeBatchByIds(StringUtils.convertList(id));
	}

	@Override
	public Boolean startStopBasicsdatumMaterialColor(StartStopDto dto) {
		UpdateWrapper<BasicsdatumMaterialColor> uw = new UpdateWrapper<>();
		uw.in("id", StringUtils.convertList(dto.getIds()));
		uw.set("status", dto.getStatus());

		//停用需要校验下游系统是否引用
		if ("1".equals(dto.getStatus())){
			QueryWrapper<BasicsdatumMaterialColor> queryWrapper= new BaseQueryWrapper<>();
			queryWrapper.in("id",StringUtils.convertList(dto.getIds()));
			List<BasicsdatumMaterialColor> list = materialColorService.list(queryWrapper);
			for (BasicsdatumMaterialColor basicsdatumMaterialColor : list) {
				Boolean b = smpService.checkSizeAndColor(basicsdatumMaterialColor.getMaterialCode(), "2", basicsdatumMaterialColor.getColorCode());
				if (!b){
					throw new OtherException("\""+basicsdatumMaterialColor.getColorName()+"\"下游系统以引用,不允许停用");
				}
			}
		}

		return this.materialColorService.update(null, uw);
	}

	@Override
	public PageInfo<BasicsdatumMaterialPricePageVo> getBasicsdatumMaterialPriceList(
			BasicsdatumMaterialPriceQueryDto dto) {
		if (dto.getPageNum() != 0 && dto.getPageSize() != 0) {
			PageHelper.startPage(dto);
		}

		BaseQueryWrapper<BasicsdatumMaterialPrice> qc = new BaseQueryWrapper<>();
		qc.eq("company_code", this.getCompanyCode());
		qc.notEmptyEq("material_code", dto.getMaterialCode());
		qc.notEmptyEq("status", dto.getStatus());
		qc.orderByDesc("select_flag");
		List<BasicsdatumMaterialPrice> list = this.materialPriceService.list(qc);
		return CopyUtil.copy(new PageInfo<>(list), BasicsdatumMaterialPricePageVo.class);
	}

	@Override
	public Map<String, Object> getBasicsdatumMaterialPriceColorWidthSelect(String materialCode) {
		Map<String, Object> map = new HashMap<>();
		List<BasicsdatumMaterialColorSelectVo> select = this.baseMapper
				.getBasicsdatumMaterialColorSelect(this.getCompanyCode(), materialCode);
		List<BasicsdatumMaterialWidthSelectVo> select2 = this.baseMapper
				.getBasicsdatumMaterialWidthSelect(this.getCompanyCode(), materialCode);
		map.put("colorSelect", select);
		map.put("widthSelect", select2);
		return map;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean saveBasicsdatumMaterialPrice(BasicsdatumMaterialPriceSaveDto dto) {
		BasicsdatumMaterialPrice entity = CopyUtil.copy(dto, BasicsdatumMaterialPrice.class);
		if ("-1".equals(entity.getId())) {
			entity.setId(null);
		}
		this.materialPriceService.saveOrUpdate(entity);
		// 如果当前是默认：批量修改其他物料未非默认，同时修改主信息的默认物料
		if (entity != null && entity.getSelectFlag() != null && entity.getSelectFlag() == true) {
			this.materialPriceService.update(new UpdateWrapper<BasicsdatumMaterialPrice>()
					.eq(COMPANY_CODE, getCompanyCode()).eq("material_code", entity.getMaterialCode())
					.ne("id", entity.getId()).set("select_flag", "0"));

			this.update(new UpdateWrapper<BasicsdatumMaterial>().eq(COMPANY_CODE, getCompanyCode())
					.eq("material_code", entity.getMaterialCode()).set("supplier_id", dto.getSupplierId())
					.set("supplier_name", dto.getSupplierName())
					.set("supplier_fabric_code", entity.getSupplierMaterialCode())
					.set("supplier_quotation_price", entity.getQuotationPrice()));
		}

		basicsdatumMaterialPriceDetailService
				.remove(new QueryWrapper<BasicsdatumMaterialPriceDetail>().eq("price_id", entity.getId()));
		List<BasicsdatumMaterialPriceDetail> basicsdatumMaterialPriceDetails = new ArrayList<>();

		String[] colorCodes = entity.getColor().split(",");
		String[] colorNames = entity.getColorName().split(",");
		String[] widthCodes = entity.getWidth().split(",");
		String[] widthNames = entity.getWidthName().split(",");
		for (int i = 0; i < colorNames.length; i++) {
			for (int j = 0; j < widthNames.length; j++) {
				BasicsdatumMaterialPriceDetail basicsdatumMaterialPriceDetai = new BasicsdatumMaterialPriceDetail();
				BeanUtil.copyProperties(entity, basicsdatumMaterialPriceDetai);
				basicsdatumMaterialPriceDetai.setId(null);
				basicsdatumMaterialPriceDetai.setPriceId(entity.getId());

				basicsdatumMaterialPriceDetai.setColorName(colorNames[i]);
				basicsdatumMaterialPriceDetai.setWidthName(widthNames[j]);
				try {
					basicsdatumMaterialPriceDetai.setColor(colorCodes[i]);
				} catch (Exception e) {
					// e.printStackTrace();
				}
				try {
					basicsdatumMaterialPriceDetai.setWidth(widthCodes[j]);
				} catch (Exception e) {

					// e.printStackTrace();
				}
				basicsdatumMaterialPriceDetails.add(basicsdatumMaterialPriceDetai);
			}
		}
		basicsdatumMaterialPriceDetailService.saveBatch(basicsdatumMaterialPriceDetails);
		return true;
	}

	@Override
	public Boolean startStopBasicsdatumMaterialPrice(StartStopDto dto) {
		UpdateWrapper<BasicsdatumMaterialPrice> uw = new UpdateWrapper<>();
		uw.in("id", StringUtils.convertList(dto.getIds()));
		uw.set("status", dto.getStatus());
		return this.materialPriceService.update(null, uw);
	}

	@Override
	public Boolean delBasicsdatumMaterialPrice(String id) {
		return this.materialPriceService.removeBatchByIds(StringUtils.convertList(id));
	}

	/**
	 * 根据规格组绑定规格
	 */
	@Transactional
	@Override
	public Boolean saveBasicsdatumMaterialWidthGroup(BasicsdatumMaterialWidthGroupSaveDto dto) {

		// 1、 获取规格组的规格集合
		List<Specification> specifications = null;
		SpecificationGroup specificationGroup = specificationGroupService.getOne(new QueryWrapper<SpecificationGroup>()
				.eq(COMPANY_CODE, getCompanyCode()).eq("code", dto.getWidthGroupCode()));
		BaseQueryWrapper<Specification> queryWrapper = new BaseQueryWrapper<>();
		if (specificationGroup != null && StringUtils.isNotBlank(specificationGroup.getSpecificationIds())) {
			String specificationIds = specificationGroup.getSpecificationIds();
			String[] ids = specificationIds.split(",");
			queryWrapper.in("code", Arrays.asList(ids));
			specifications = specificationService.list(queryWrapper);
		}
		// 2、清理现有物料规格
		this.materialWidthService.remove(new QueryWrapper<BasicsdatumMaterialWidth>().eq(COMPANY_CODE, getCompanyCode())
				.eq("material_code", dto.getMaterialCode()));
		// 3、添加规格组的规格
		if (specifications != null && specifications.size() > 0) {
			List<BasicsdatumMaterialWidth> wList = new ArrayList<>();
			BasicsdatumMaterialWidth width;
			for (Specification specification : specifications) {
				width = new BasicsdatumMaterialWidth();
				width.setCompanyCode(getCompanyCode());
				width.setStatus("0");
				width.setMaterialCode(dto.getMaterialCode());
				width.setWidthCode(specification.getCode());
				width.setName(specification.getName());
				wList.add(width);
			}
			this.materialWidthService.saveBatch(wList);
		}
		return true;
	}

	/**
	 * 根据规格下拉 绑定规格
	 */
	@Override
	@Transactional
	public Boolean saveBasicsdatumMaterialWidths(BasicsdatumMaterialWidthsSaveDto dto) {
		// 1、清理现有物料规格
		this.materialWidthService.remove(new QueryWrapper<BasicsdatumMaterialWidth>().eq(COMPANY_CODE, getCompanyCode())
				.eq("material_code", dto.getMaterialCode()));
		String[] codes = dto.getWidth().split(",");
		String[] names = dto.getWidthName().split(",");
		// 2、添加规格组的规格
		if (codes != null && codes.length > 0) {
			List<BasicsdatumMaterialWidth> wList = new ArrayList<>();
			BasicsdatumMaterialWidth width;
			int i = 0;
			for (String code : codes) {
				width = new BasicsdatumMaterialWidth();
				width.setCompanyCode(getCompanyCode());
				width.setStatus("0");
				width.setMaterialCode(dto.getMaterialCode());
				width.setWidthCode(code);
				width.setName(names[i]);
				i++;
				wList.add(width);
			}
			this.materialWidthService.saveBatch(wList);
		}
		return true;
	}

	/**
	 * 查询旧料号列表
	 */
	@Override
	public PageInfo<BasicsdatumMaterialOldPageVo> getBasicsdatumMaterialOldList(BasicsdatumMaterialOldQueryDto dto) {
		if (dto.getPageNum() != 0 && dto.getPageSize() != 0) {
			PageHelper.startPage(dto);
		}
		List<BasicsdatumMaterialOldPageVo> list = baseMapper.getBasicsdatumMaterialOldPage(this.getCompanyCode(),
				dto.getMaterialCode());
		return new PageInfo<>(list);
	}

	@Override
	@Transactional
	public Boolean saveBasicsdatumMaterialOld(BasicsdatumMaterialOldSaveDto dto) {
		// 清理
//		this.materialOldService.remove(new QueryWrapper<BasicsdatumMaterialOld>().eq(COMPANY_CODE, getCompanyCode())
//				.eq("material_code", dto.getMaterialCode()));
		long count = materialOldService.count(new QueryWrapper<BasicsdatumMaterialOld>()
				.eq(COMPANY_CODE, getCompanyCode()).eq("material_code", dto.getMaterialCode())
				.in("old_Material_code", StringUtils.convertList(dto.getOldMaterialCode())));
		if (count > 0) {
			throw new OtherException("已添加对应的旧料号");
		}
		String[] codes = dto.getOldMaterialCode().split(",");

		List<BasicsdatumMaterialOld> list = new ArrayList<>();
		BasicsdatumMaterialOld old = null;
		for (String code : codes) {
			old = new BasicsdatumMaterialOld();
			old.setCompanyCode(getCompanyCode());
			old.setMaterialCode(dto.getMaterialCode());
			old.setOldMaterialCode(code);
			list.add(old);
		}
		this.materialOldService.saveBatch(list);
		return true;
	}

	@Override
	public Boolean delBasicsdatumMaterialOld(String id) {
		return this.materialOldService.removeBatchByIds(StringUtils.convertList(id));
	}

	@Override
	public PageInfo<WarehouseMaterialVo> getPurchaseMaterialList(BasicsdatumMaterialQueryDto dto) {
		Page<WarehouseMaterialVo> page = null;
		if (dto.getPageNum() != 0 && dto.getPageSize() != 0) {
			page = PageHelper.startPage(dto);
		}
		BaseQueryWrapper<BasicsdatumMaterial> qc = new BaseQueryWrapper<>();
		qc.eq("m.company_code", this.getCompanyCode());
		qc.eq("m.status", "0");
		qc.notEmptyLike("m.supplier_id", dto.getSupplierId());
		qc.notEmptyEq("c.color_name", dto.getMaterialColor());
		if (StringUtils.isNotEmpty(dto.getSearch())) {
			qc.and(Wrapper -> Wrapper.eq("m.material_code", dto.getSearch()).or().eq("m.material_name ",
					dto.getSearch()));
		}
		qc.eq("m.biz_type", BasicsdatumMaterialBizTypeEnum.MATERIAL.getK());
		qc.eq("m.confirm_status", "2");

		List<WarehouseMaterialVo> list = getBaseMapper().getPurchaseMaterialList(qc);
		for (WarehouseMaterialVo item : list) {
			item.setId(IdUtil.randomUUID());
		}
		return page.toPageInfo();
	}

	public Boolean updateInquiryNumberDeliveryName(BasicsdatumMaterialSaveDto dto){
			BasicsdatumMaterial basicsdatumMaterial = new BasicsdatumMaterial();
			basicsdatumMaterial.setId(dto.getId());
			basicsdatumMaterial.setInquiryNumber(dto.getInquiryNumber());
			basicsdatumMaterial.setDeliveryName(dto.getDeliveryName());
			int countNum = this.baseMapper.updateById(basicsdatumMaterial);
			return countNum > 0 ? true : false;
	}

	@Override
	public void saveSubmit(BasicsdatumMaterialSaveDto dto) {
		dto.setConfirmStatus("1");
		this.saveBasicsdatumMaterial(dto);
			flowableService.start(FlowableService.BASICSDATUM_MATERIAL,
					FlowableService.BASICSDATUM_MATERIAL, dto.getId(),
					"/pdm/api/saas/basicsdatumMaterial/approval",
					"/pdm/api/saas/basicsdatumMaterial/approval",
					"/pdm/api/saas/basicsdatumMaterial/approval",
					"pdm/api/saas/basicsdatumMaterial/getBasicsdatumMaterial?id=" + dto.getId(), BeanUtil.beanToMap(dto));
	}

	@Override
	@Transactional(rollbackFor = {OtherException.class, Exception.class})
	public boolean approval(AnswerDto dto) {
		BasicsdatumMaterialVo basicsdatumMaterialVo = this.getBasicsdatumMaterial(dto.getBusinessKey());
		if (Objects.isNull(basicsdatumMaterialVo)) {
			throw new OtherException("数据不存在");
		}
		if (StrUtil.equals(dto.getApprovalType(), BaseConstant.APPROVAL_PASS)) {
			BasicsdatumMaterial basicsdatumMaterial = new BasicsdatumMaterial();
			basicsdatumMaterial.setId(basicsdatumMaterialVo.getId());
			basicsdatumMaterial.updateInit();
			basicsdatumMaterial.setConfirmStatus("2");
			basicsdatumMaterial.setConfirmId(super.getUserId());
			basicsdatumMaterial.setConfirmName(super.getUserName());
			super.updateById(basicsdatumMaterial);
		} else {
			super.getBaseMapper().deleteById(basicsdatumMaterialVo.getId());
		}
		basicFabricLibraryService.materialApproveProcessing(basicsdatumMaterialVo.getId(), dto.getApprovalType());
		return true;
	}
}
