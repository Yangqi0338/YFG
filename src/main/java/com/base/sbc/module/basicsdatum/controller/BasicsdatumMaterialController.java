/******************************************************************************
* Copyright (C) 2018 广州尚捷科技有限责任公司
* All Rights Reserved.
* 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
* 不得使用、复制、修改或发布本软件.
*****************************************************************************/
package com.base.sbc.module.basicsdatum.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialColorQueryDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialColorSaveDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialPriceQueryDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialPriceSaveDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialQueryDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialSaveDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialWidthGroupSaveDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialWidthQueryDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialWidthSaveDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialWidthsSaveDto;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterial;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialService;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumMaterialColorPageVo;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumMaterialPageVo;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumMaterialPricePageVo;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumMaterialSelectVo;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumMaterialVo;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumMaterialWidthPageVo;
import com.base.sbc.module.pack.vo.BomSelMaterialVo;
import com.github.pagehelper.PageInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 类描述：基础资料-物料档案
 * 
 * @date 创建时间：2023-6-26 22:45:23
 * @author xiong
 * @version 1.0
 */
@RestController
@Api(tags = "基础资料-物料档案")
@RequestMapping(value = BaseController.SAAS_URL
		+ "/basicsdatumMaterial", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class BasicsdatumMaterialController {

	@Autowired
	private BasicsdatumMaterialService basicsdatumMaterialService;

	@Autowired
	private BaseController baseController;

	@ApiOperation(value = "主物料:查询下拉,search:按编码或名称检索,status状态默认全部")
	@GetMapping("/getBasicsdatumMaterialSelect")
	public List<BasicsdatumMaterialSelectVo> getBasicsdatumMaterialSelect(
			@RequestParam(value = "search", required = false) String search,
			@RequestParam(value = "status", required = false) String status) {
		List<BasicsdatumMaterial> list = basicsdatumMaterialService.list(new BaseQueryWrapper<BasicsdatumMaterial>()
				.select("id,material_name,material_code").eq("company_code", baseController.getUserCompany())
				.like(StringUtils.isNotBlank(search), "material_code_name", search)
				.eq(StringUtils.isNotBlank(status), "status", status).last(" limit 0,50 "));
		return CopyUtil.copy(list, BasicsdatumMaterialSelectVo.class);
	}

	@ApiOperation(value = "主物料:查询列表")
	@GetMapping("/getBasicsdatumMaterialList")
	public PageInfo<BasicsdatumMaterialPageVo> getBasicsdatumMaterialList(BasicsdatumMaterialQueryDto dto) {
		return basicsdatumMaterialService.getBasicsdatumMaterialList(dto);
	}

	@ApiOperation(value = "物料清单-选择物料档案列表")
	@GetMapping("/getBomSelMaterialList")
	public PageInfo<BomSelMaterialVo> getBomSelMaterialList(BasicsdatumMaterialQueryDto dto) {
		return basicsdatumMaterialService.getBomSelMaterialList(dto);
	}

	@ApiOperation(value = "主物料:保存主信息")
	@PostMapping("/saveBasicsdatumMaterial")
	public BasicsdatumMaterialVo saveBasicsdatumMaterial(@Valid @RequestBody BasicsdatumMaterialSaveDto dto) {
		return basicsdatumMaterialService.saveBasicsdatumMaterial(dto);
	}

	@ApiOperation(value = "主物料:停用/启用物料")
	@PostMapping("/startStopBasicsdatumMaterial")
	public Boolean startStopBasicsdatumMaterial(@Valid @RequestBody StartStopDto dto) {
		return basicsdatumMaterialService.startStopBasicsdatumMaterial(dto);
	}

	@ApiOperation(value = "主物料:删除物料及详情")
	@DeleteMapping("/delBasicsdatumMaterial")
	public Boolean delBasicsdatumMaterial(@RequestParam(value = "id") @NotBlank(message = "id不能为空") String id) {
		return basicsdatumMaterialService.delBasicsdatumMaterial(id);
	}

	@ApiOperation(value = "主物料：按筛选条件导出")
	@GetMapping("/exportBasicsdatumMaterial")
	public void exportBasicsdatumMaterial(HttpServletResponse response, BasicsdatumMaterialQueryDto dto)
			throws Exception {
		basicsdatumMaterialService.exportBasicsdatumMaterial(response, dto);
	}

	@ApiOperation(value = "主物料:查询物料详情(不包括详情)")
	@GetMapping("/getBasicsdatumMaterial")
	public BasicsdatumMaterialVo getBasicsdatumMaterial(
			@RequestParam(value = "id") @NotBlank(message = "编号id不能为空") String id) {
		return basicsdatumMaterialService.getBasicsdatumMaterial(id);
	}

	@ApiOperation(value = "主物料：下拉选择规格后保存到规格组")
	@PostMapping("/saveBasicsdatumMaterialWidths")
	public Boolean saveBasicsdatumMaterialWidths(@Valid @RequestBody BasicsdatumMaterialWidthsSaveDto dto) {
		return basicsdatumMaterialService.saveBasicsdatumMaterialWidths(dto);
	}

	@ApiOperation(value = "物料规格:查询规格列表")
	@GetMapping("/getBasicsdatumMaterialWidthList")
	public PageInfo<BasicsdatumMaterialWidthPageVo> getBasicsdatumMaterialWidthList(
			BasicsdatumMaterialWidthQueryDto dto) {
		return basicsdatumMaterialService.getBasicsdatumMaterialWidthList(dto);
	}

	@ApiOperation(value = "物料规格:新增规格")
	@PostMapping("/saveBasicsdatumMaterialWidth")
	public Boolean saveBasicsdatumMaterialWidth(@Valid @RequestBody BasicsdatumMaterialWidthSaveDto dto) {
		return basicsdatumMaterialService.saveBasicsdatumMaterialWidth(dto);
	}

	@ApiOperation(value = "物料规格:按规格组清理并批量导入新增规格")
	@PostMapping("/saveBasicsdatumMaterialWidthGroup")
	public Boolean saveBasicsdatumMaterialWidthGroup(@Valid @RequestBody BasicsdatumMaterialWidthGroupSaveDto dto) {
		return basicsdatumMaterialService.saveBasicsdatumMaterialWidthGroup(dto);
	}

	@ApiOperation(value = "物料规格:停用/启用规格")
	@PostMapping("/startStopBasicsdatumMaterialWidth")
	public Boolean startStopBasicsdatumMaterialWidth(@Valid @RequestBody StartStopDto dto) {
		return basicsdatumMaterialService.startStopBasicsdatumMaterialWidth(dto);
	}

	@ApiOperation(value = "物料规格:删除规格")
	@DeleteMapping("/delBasicsdatumMaterialWidth")
	public Boolean delBasicsdatumMaterialWidth(@RequestParam(value = "id") @NotBlank(message = "id不能为空") String id) {
		return basicsdatumMaterialService.delBasicsdatumMaterialWidth(id);
	}

	@ApiOperation(value = "物料颜色:查询颜色列表")
	@GetMapping("/getBasicsdatumMaterialColorList")
	public PageInfo<BasicsdatumMaterialColorPageVo> getBasicsdatumMaterialColorList(
			BasicsdatumMaterialColorQueryDto dto) {
		return basicsdatumMaterialService.getBasicsdatumMaterialColorList(dto);
	}

	@ApiOperation(value = "物料颜色:保存颜色")
	@PostMapping("/saveBasicsdatumMaterialColor")
	public Boolean saveBasicsdatumMaterialColor(@Valid @RequestBody BasicsdatumMaterialColorSaveDto dto) {
		return basicsdatumMaterialService.saveBasicsdatumMaterialColor(dto);
	}

	@ApiOperation(value = "物料颜色:停用/启用颜色")
	@PostMapping("/startStopBasicsdatumMaterialColor")
	public Boolean startStopBasicsdatumMaterialColor(@Valid @RequestBody StartStopDto dto) {
		return basicsdatumMaterialService.startStopBasicsdatumMaterialColor(dto);
	}

	@ApiOperation(value = " 物料颜色:删除颜色")
	@DeleteMapping("/delBasicsdatumMaterialColor")
	public Boolean delBasicsdatumMaterialColor(@RequestParam(value = "id") @NotBlank(message = "id不能为空") String id) {
		return basicsdatumMaterialService.delBasicsdatumMaterialColor(id);
	}

	@ApiOperation(value = "物料报价:查询报价列表")
	@GetMapping("/getBasicsdatumMaterialPriceList")
	public PageInfo<BasicsdatumMaterialPricePageVo> getBasicsdatumMaterialPriceList(
			BasicsdatumMaterialPriceQueryDto dto) {
		return basicsdatumMaterialService.getBasicsdatumMaterialPriceList(dto);
	}

	@ApiOperation(value = "物料报价:查询报价列表的颜色尺码选择下拉")
	@GetMapping("/getBasicsdatumMaterialPriceColorWidthSelect")
	public Map<String, Object> getBasicsdatumMaterialPriceColorWidthSelect(
			@RequestParam(value = "materialCode") @NotBlank(message = "物料编码不能为空") String materialCode) {
		return basicsdatumMaterialService.getBasicsdatumMaterialPriceColorWidthSelect(materialCode);
	}

	@ApiOperation(value = "物料报价:保存报价")
	@PostMapping("/saveBasicsdatumMaterialPrice")
	public Boolean saveBasicsdatumMaterialPrice(@Valid @RequestBody BasicsdatumMaterialPriceSaveDto dto) {
		return basicsdatumMaterialService.saveBasicsdatumMaterialPrice(dto);
	}

	@ApiOperation(value = "物料报价:停用/启用报价")
	@PostMapping("/startStopBasicsdatumMaterialPrice")
	public Boolean startStopBasicsdatumMaterialPrice(@Valid @RequestBody StartStopDto dto) {
		return basicsdatumMaterialService.startStopBasicsdatumMaterialPrice(dto);
	}

	@ApiOperation(value = "物料报价:删除报价")
	@DeleteMapping("/delBasicsdatumMaterialPrice")
	public Boolean delBasicsdatumMaterialPrice(@RequestParam(value = "id") @NotBlank(message = "id不能为空") String id) {
		return basicsdatumMaterialService.delBasicsdatumMaterialPrice(id);
	}

}