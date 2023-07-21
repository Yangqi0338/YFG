/******************************************************************************
* Copyright (C) 2018 广州尚捷科技有限责任公司
* All Rights Reserved.
* 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
* 不得使用、复制、修改或发布本软件.
*****************************************************************************/
package com.base.sbc.module.basicsdatum.controller;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.basicsdatum.dto.AddRevampSizeDto;
import com.base.sbc.module.basicsdatum.dto.AddRevampSizeLabelDto;
import com.base.sbc.module.basicsdatum.dto.QueryDasicsdatumSizeDto;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.basicsdatum.service.BasicsdatumSizeLabelService;
import com.base.sbc.module.basicsdatum.service.BasicsdatumSizeService;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumSizeVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
* 类描述：基础资料-尺码表 Controller类
* @address com.base.sbc.module.basicsdatum.web.BasicsdatumSizeController
* @author mengfanjiang
* @email 2915350015@qq.com
* @date 创建时间：2023-5-17 14:01:34
* @version 1.0
*/
@RestController
@Api(tags = "基础资料-尺码表")
@RequestMapping(value = BaseController.SAAS_URL + "/basicsdatumSize", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class BasicsdatumSizeController{

	@Autowired
	private BasicsdatumSizeService basicsdatumSizeService;

	@Autowired
	private BasicsdatumSizeLabelService sizeLabelService;

	@ApiOperation(value = "分页查询尺码")
	@GetMapping("/getSizeList")
	public PageInfo<BasicsdatumSizeVo> getSizeList(QueryDasicsdatumSizeDto queryDasicsdatumSizeDto) {
		return basicsdatumSizeService.getSizeList(queryDasicsdatumSizeDto);
	}

	@ApiOperation(value = "/导入")
	@PostMapping("/importExcel")
	public ApiResult importExcel(@RequestParam("file") MultipartFile file) throws Exception {
		return ApiResult.success("操作成功",basicsdatumSizeService.importExcel(file)) ;
	}

	@ApiOperation(value = "/导出")
	@GetMapping("/deriveExcel")
	public void deriveExcel(QueryDasicsdatumSizeDto queryDasicsdatumSizeDto, HttpServletResponse response) throws Exception {
		basicsdatumSizeService.deriveExcel(queryDasicsdatumSizeDto,response);
	}

	@ApiOperation(value = "新增修改尺码")
	@PostMapping("/addRevampSize")
	public ApiResult addRevampSize(@Valid @RequestBody AddRevampSizeDto addRevampSizeDto) {
		return ApiResult.success("操作成功",basicsdatumSizeService.addRevampSize(addRevampSizeDto)) ;
	}

	@ApiOperation(value = "批量启用/停用", notes = "ids:, status:0启用1停用")
	@PostMapping("/sizeStartStop")
	public ApiResult sizeStartStop(@Valid @RequestBody StartStopDto startStopDto) {
		return ApiResult.success("操作成功",basicsdatumSizeService.sizeStartStop(startStopDto));
	}

	@ApiOperation(value = "删除尺码")
	@DeleteMapping("/delSize")
	public ApiResult delSize(@Valid @NotBlank(message = "编号id不能为空") String id) {
		return ApiResult.success("操作成功",basicsdatumSizeService.delSize(id));
	}


	/*标签相关接口*/
	@ApiOperation(value = "查询尺码标签")
	@GetMapping("/getSizeLabelList")
	public ApiResult getSizeLabelList(QueryDasicsdatumSizeDto queryDasicsdatumSizeDto) {
		return	ApiResult.success("操作成功",sizeLabelService.getSizeLabelList(queryDasicsdatumSizeDto)) ;
	}

	@ApiOperation(value = "新增修改尺码标签")
	@PostMapping("/addRevampSizeLabel")
	public ApiResult addRevampSizeLabel(@Valid @RequestBody AddRevampSizeLabelDto addRevampSizeLabelDto) {
		return ApiResult.success("操作成功",sizeLabelService.addRevampSizeLabel(addRevampSizeLabelDto)) ;
	}


	@ApiOperation(value = "删除尺码标签")
	@DeleteMapping("/delSizeLabel")
	public ApiResult delSizeLabel(@Valid @NotBlank(message = "编号id不能为空") String id) {
		return ApiResult.success("操作成功",sizeLabelService.delSizeLabel(id));
	}

	/*获取尺码*/
	@ApiOperation(value = "查询尺码标签")
	@GetMapping("/getSizeName")
	public ApiResult getSizeName(@Valid @NotBlank(message = "编号id不能为空") String sort) {
		return	ApiResult.success("操作成功",basicsdatumSizeService.getSizeName(sort)) ;
	}



}































