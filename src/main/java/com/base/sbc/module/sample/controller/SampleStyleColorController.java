/******************************************************************************
* Copyright (C) 2018 广州尚捷科技有限责任公司
* All Rights Reserved.
* 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
* 不得使用、复制、修改或发布本软件.
*****************************************************************************/
package com.base.sbc.module.sample.controller;

import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.basicsdatum.dto.QueryDto;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.sample.dto.AddRevampSampleStyleColorDto;
import com.base.sbc.module.sample.entity.SampleStyleColor;
import com.base.sbc.module.sample.service.SampleStyleColorService;
import com.base.sbc.module.sample.vo.SampleStyleColorVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
* 类描述：样衣-款式配色 Controller类
* @address com.base.sbc.module.sample.web.SampleStyleColorController
* @author mengfanjiang
* @email XX.com
* @date 创建时间：2023-6-28 15:02:46
* @version 1.0
*/
@RestController
@Api(tags = "样衣-款式配色")
@RequestMapping(value = BaseController.SAAS_URL + "/sampleStyleColor", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class SampleStyleColorController{

	@Autowired
	private SampleStyleColorService sampleStyleColorService;

	@ApiOperation(value = "分页查询")
	@GetMapping("/getSampleStyleColorList")
	public PageInfo<SampleStyleColorVo> getSampleStyleColorList(QueryDto queryDto) {
		return  sampleStyleColorService.getSampleStyleColorList(queryDto);
	}



	@ApiOperation(value = "批量启用/停用", notes = "ids:, status:0启用1停用")
	@PostMapping("/startStopSampleStyleColor")
	public Boolean startStopSampleStyleColor(@Valid @RequestBody StartStopDto startStopDto) {
	return sampleStyleColorService.startStopSampleStyleColor(startStopDto);
	}


	@ApiOperation(value = "新增修改样衣-款式配色")
	@PostMapping("/addRevampSampleStyleColor")
	public Boolean addRevampSampleStyleColor(@Valid @RequestBody AddRevampSampleStyleColorDto addRevampSampleStyleColorDto) {
	return sampleStyleColorService.addRevampSampleStyleColor(addRevampSampleStyleColorDto);
	}

	@ApiOperation(value = "删除样衣-款式配色")
	@DeleteMapping("/delSampleStyleColor")
	public Boolean delSampleStyleColor(@Valid @NotBlank(message = "编号id不能为空") String id) {
	return sampleStyleColorService.delSampleStyleColor(id);
	}

	@ApiOperation(value = "明细-通过id查询")
	@GetMapping("/{id}")
	public SampleStyleColor getById(@PathVariable("id") String id) {
		return sampleStyleColorService.getById(id);
	}


}































