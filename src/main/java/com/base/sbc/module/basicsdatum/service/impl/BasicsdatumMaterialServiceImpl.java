/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialColorQueryDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialColorSaveDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialPriceQueryDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialPriceSaveDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialQueryDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialSaveDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialWidthQueryDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialWidthSaveDto;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterial;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterialColor;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterialPrice;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterialWidth;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumMaterialMapper;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialColorService;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialPriceService;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialService;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialWidthService;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumMaterialColorPageVo;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumMaterialColorSelectVo;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumMaterialPageVo;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumMaterialPricePageVo;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumMaterialVo;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumMaterialWidthPageVo;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumMaterialWidthSelectVo;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

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
public class BasicsdatumMaterialServiceImpl extends BaseServiceImpl<BasicsdatumMaterialMapper, BasicsdatumMaterial>
		implements BasicsdatumMaterialService {

	@Autowired
	private BasicsdatumMaterialColorService materialColorService;
	@Autowired
	private BasicsdatumMaterialWidthService materialWidthService;
	@Autowired
	private BasicsdatumMaterialPriceService materialPriceService;

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
		this.saveOrUpdate(entity);
		return CopyUtil.copy(entity, BasicsdatumMaterialVo.class);
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
		return this.update(null, uw);
	}

	@Override
	public Boolean delBasicsdatumMaterial(String id) {
		return this.removeBatchByIds(StringUtils.convertList(id));
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
	public Boolean saveBasicsdatumMaterialPrice(BasicsdatumMaterialPriceSaveDto dto) {
		BasicsdatumMaterialPrice entity = CopyUtil.copy(dto, BasicsdatumMaterialPrice.class);
		if ("-1".equals(entity.getId())) {
			entity.setId(null);
		}
		return this.materialPriceService.saveOrUpdate(entity);
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

}
