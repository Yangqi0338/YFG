/******************************************************************************
* Copyright (C) 2018 广州尚捷科技有限责任公司
* All Rights Reserved.
* 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
* 不得使用、复制、修改或发布本软件.
*****************************************************************************/
package com.base.sbc.module.basicsdatum.controller;

import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.*;
import com.base.sbc.module.basicsdatum.service.BasicsdatumCoefficientTemplateService;
import com.base.sbc.module.basicsdatum.service.BasicsdatumDimensionalityService;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumCoefficientTemplateVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
* 类描述：基础资料-纬度系数模板 Controller类
* @address com.base.sbc.module.basicsdatum.web.BasicsdatumCoefficientTemplateController
* @author your name
* @email your email
* @date 创建时间：2024-1-15 14:34:41
* @version 1.0
*/
@RestController
@Api(tags = "基础资料-纬度系数模板相关接口")
@RequestMapping(value = BaseController.SAAS_URL + "/coefficientTemplate", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class BasicsdatumCoefficientTemplateController{

	@Autowired
	private BasicsdatumCoefficientTemplateService basicsdatumCoefficientTemplateService;

	@Autowired
	private BasicsdatumDimensionalityService basicsdatumDimensionalityService;

	@ApiOperation(value = "分页查询")
	@GetMapping("/getCoefficientTemplateList")
	public PageInfo getCoefficientTemplateList(BasicsdatumCoefficientTemplateDto dto) {
		return basicsdatumCoefficientTemplateService.getCoefficientTemplateList(dto);
	}


	@ApiOperation(value = "删除-通过id查询,多个逗号分开")
	@DeleteMapping("/{id}")
	public Boolean removeById(@PathVariable("id") String id) {
		List<String> ids = StringUtils.convertList(id);
		return basicsdatumCoefficientTemplateService.removeByIds(ids);
	}

	@ApiOperation(value = "新增修改")
	@PostMapping("/addUpdateCoefficientTemplate")
	public Boolean addUpdateCoefficientTemplate(@RequestBody AddUpdateCoefficientTemplateDto dto) {
		return basicsdatumCoefficientTemplateService.addUpdateCoefficientTemplate(dto);
	}

	@ApiOperation(value = "批量启用/停用", notes = "ids:, status:0启用1停用")
	@PostMapping("/startStopCoefficientTemplate")
	public Boolean startStopCoefficientTemplate(@Valid @RequestBody StartStopDto startStopDto) {
		return basicsdatumCoefficientTemplateService.startStopCoefficientTemplate(startStopDto);
	}

	@ApiOperation(value = "复制模板")
	@PostMapping("/copyTemplate")
	public Boolean copyTemplate(@Valid @RequestBody BasicsdatumCoefficientTemplateDto dto) {
		return basicsdatumCoefficientTemplateService.copyTemplate(dto);
	}


	@ApiOperation(value = "查看模板详情")
	@GetMapping("/getTemplateDetails")
	public BasicsdatumCoefficientTemplateVo getTemplateDetails(@Valid IdDto dto) {
		return basicsdatumCoefficientTemplateService.getTemplateDetails(dto);
	}

	@ApiOperation(value = "获取围度系数数据")
	@GetMapping("/getDimensionality")
	public Map getDimensionality(BasicsdatumDimensionalityDto dto) {
		return basicsdatumDimensionalityService.getDimensionality(dto);
	}


	@ApiOperation(value = "保存/编辑维度标签")
	@PostMapping("/batchSaveDimensionality")
	public boolean batchSaveDimensionality(@Valid @RequestBody List<BasicsdatumDimensionalityDto> dtoList) {
		return basicsdatumDimensionalityService.batchSaveDimensionality(dtoList);
	}


}































