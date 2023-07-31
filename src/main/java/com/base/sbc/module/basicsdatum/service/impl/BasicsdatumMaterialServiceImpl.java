/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.config.utils.ExcelUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialColorQueryDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialColorSaveDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialPriceQueryDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialPriceSaveDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialQueryDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialSaveDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialWidthGroupSaveDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialWidthQueryDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialWidthSaveDto;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterial;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterialColor;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterialPrice;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterialPriceDetail;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterialWidth;
import com.base.sbc.module.basicsdatum.entity.Specification;
import com.base.sbc.module.basicsdatum.entity.SpecificationGroup;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumMaterialMapper;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialColorService;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialPriceDetailService;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialPriceService;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialService;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialWidthService;
import com.base.sbc.module.basicsdatum.service.SpecificationGroupService;
import com.base.sbc.module.basicsdatum.service.SpecificationService;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumMaterialColorPageVo;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumMaterialColorSelectVo;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumMaterialExcelVo;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumMaterialPageVo;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumMaterialPricePageVo;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumMaterialVo;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumMaterialWidthPageVo;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumMaterialWidthSelectVo;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.pack.vo.BomSelMaterialVo;
import com.base.sbc.module.smp.SmpService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;

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

	private final BasicsdatumMaterialColorService materialColorService;
	private final BasicsdatumMaterialWidthService materialWidthService;
	private final BasicsdatumMaterialPriceService materialPriceService;
	private final SpecificationService specificationService;
	private final SpecificationGroupService specificationGroupService;
	private final BasicsdatumMaterialPriceDetailService basicsdatumMaterialPriceDetailService;

	/**
	 * 解决循环依赖报错的问题
	 */
	@Lazy
	@Resource
	private SmpService smpService;

	@Override
	public PageInfo<BasicsdatumMaterialPageVo> getBasicsdatumMaterialList(BasicsdatumMaterialQueryDto dto) {
		if (dto.getPageNum() != 0 && dto.getPageSize() != 0) {
			PageHelper.startPage(dto);
		}
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
		List<BasicsdatumMaterial> list = this.list(qc);
		return CopyUtil.copy(new PageInfo<>(list), BasicsdatumMaterialPageVo.class);
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
		//if ("fabric".equals(entity.getMaterialType())) {
			//this.saveFabricWidth(entity.getMaterialCode(), BigDecimalUtil.convertString(entity.getTranslate()));
		//}
		if (entity.getId()!=null && "1".equals(entity.getDistribute())){
			smpService.materials(entity.getId().split(","));
		}
		this.saveOrUpdate(entity);


		return CopyUtil.copy(entity, BasicsdatumMaterialVo.class);
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

		QueryWrapper<BasicsdatumMaterial> queryWrapper =new QueryWrapper<>();
		queryWrapper.in("id",dto.getIds());
		for (BasicsdatumMaterial basicsdatumMaterial : this.list(queryWrapper)) {
			if ("1".equals(basicsdatumMaterial.getDistribute())){
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
		qw.andLike(dto.getCategoryId(), "category_id", "category_ids");
		Page<BomSelMaterialVo> page = PageHelper.startPage(dto);
		List<BomSelMaterialVo> list = getBaseMapper().getBomSelMaterialList(qw);
		if (CollUtil.isNotEmpty(list)) {
			list.forEach(i -> {
				i.setId(IdUtil.randomUUID());
			});
		}
		return page.toPageInfo();
	}

	@Override
	public BasicsdatumMaterialVo getBasicsdatumMaterial(String id) {
		BasicsdatumMaterial material = this.getById(id);
		List<BasicsdatumMaterialColorSelectVo> select = this.baseMapper
				.getBasicsdatumMaterialColorSelect(this.getCompanyCode(), material.getMaterialCode());
		List<BasicsdatumMaterialWidthSelectVo> select2 = this.baseMapper
				.getBasicsdatumMaterialWidthSelect(this.getCompanyCode(), material.getMaterialCode());
		BasicsdatumMaterialVo copy = CopyUtil.copy(material, BasicsdatumMaterialVo.class);
		copy.setColorList(select);
		copy.setWidthList(select2);
		return copy;
	}

	@Override
	public PageInfo<BasicsdatumMaterialWidthPageVo> getBasicsdatumMaterialWidthList(
			BasicsdatumMaterialWidthQueryDto dto) {
		if (dto.getPageNum() != 0 && dto.getPageSize() != 0) {
			PageHelper.startPage(dto);
		}
		List<BasicsdatumMaterialWidthPageVo> list = this.baseMapper
				.getBasicsdatumMaterialWidthList(this.getCompanyCode(), dto.getMaterialCode());
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
				.getBasicsdatumMaterialColorList(this.getCompanyCode(), dto.getMaterialCode());
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
			this.materialPriceService
					.update(new UpdateWrapper<BasicsdatumMaterialPrice>().eq(COMPANY_CODE, getCompanyCode())
							.eq("material_code", entity.getMaterialCode()).ne("id", entity.getId())
							.set("select_flag", "0"));

			this.update(new UpdateWrapper<BasicsdatumMaterial>().eq(COMPANY_CODE, getCompanyCode())
					.eq("material_code", entity.getMaterialCode()).set("supplier_id", dto.getSupplierId())
					.set("supplier_name", dto.getSupplierName()));
		}

		basicsdatumMaterialPriceDetailService.remove(new QueryWrapper<BasicsdatumMaterialPriceDetail>().eq("price_id", entity.getId()));
		List<BasicsdatumMaterialPriceDetail> basicsdatumMaterialPriceDetails =new ArrayList<>();

		String[] colorCodes = entity.getColor().split(",");
		String[] colorNames = entity.getColorName().split(",");
		String[] widthCodes = entity.getWidth().split(",");
		String[] widthNames = entity.getWidthName().split(",");
		for (int i = 0; i < colorNames.length; i++) {
			for (int j = 0; j < widthNames.length; j++) {
				BasicsdatumMaterialPriceDetail basicsdatumMaterialPriceDetai =new BasicsdatumMaterialPriceDetail();
				BeanUtil.copyProperties(entity,basicsdatumMaterialPriceDetai);
				basicsdatumMaterialPriceDetai.setId(null);
				basicsdatumMaterialPriceDetai.setPriceId(entity.getId());

				basicsdatumMaterialPriceDetai.setColorName(colorNames[i]);
				basicsdatumMaterialPriceDetai.setWidthName(widthNames[j]);
				try {
					basicsdatumMaterialPriceDetai.setColor(colorCodes[i]);
				}catch (Exception e ){
					//e.printStackTrace();
				}
				try {
					basicsdatumMaterialPriceDetai.setWidth(widthCodes[j]);
				}catch (Exception e ){

					//e.printStackTrace();
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

	@Transactional
	@Override
	public Boolean saveBasicsdatumMaterialWidthGroup(BasicsdatumMaterialWidthGroupSaveDto dto) {

		// 1、 获取规格组的规格集合
		List<Specification> specifications = null;
		SpecificationGroup specificationGroup = specificationGroupService
				.getOne(new QueryWrapper<SpecificationGroup>().eq(COMPANY_CODE, getCompanyCode()).eq("code",
						dto.getWidthGroupCode()));
		BaseQueryWrapper<Specification> queryWrapper = new BaseQueryWrapper<>();
		if (specificationGroup != null && StringUtils.isNotBlank(specificationGroup.getSpecificationIds())) {
			String specificationIds = specificationGroup.getSpecificationIds();
			String[] ids = specificationIds.split(",");
			queryWrapper.in("id", Arrays.asList(ids));
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
}
