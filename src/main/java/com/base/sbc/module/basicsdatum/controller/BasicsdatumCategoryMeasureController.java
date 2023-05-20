/******************************************************************************
* Copyright (C) 2018 广州尚捷科技有限责任公司
* All Rights Reserved.
* 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
* 不得使用、复制、修改或发布本软件.
*****************************************************************************/
package com.base.sbc.module.basicsdatum.controller;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.Page;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumCategoryMeasure;
import com.base.sbc.module.basicsdatum.service.BasicsdatumCategoryMeasureService;
import com.base.sbc.module.basicsdatum.dto.AddRevampBasicsdatumCategoryMeasureDto;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumCategoryMeasureVo;
import org.hibernate.validator.constraints.NotBlank;
import com.base.sbc.module.basicsdatum.dto.QueryDto;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.util.List;

/**
* 类描述：基础资料-品类测量组 Controller类
* @address com.base.sbc.module.basicsdatum.web.BasicsdatumCategoryMeasureController
* @author mengfanjiang
* @email 2915350015@qq.com
* @date 创建时间：2023-5-20 19:08:55
* @version 1.0
*/
@RestController
@Api(tags = "基础资料-品类测量组")
@RequestMapping(value = BaseController.SAAS_URL + "/basicsdatumCategoryMeasure", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class BasicsdatumCategoryMeasureController{

	@Autowired
	private BasicsdatumCategoryMeasureService basicsdatumCategoryMeasureService;

	@ApiOperation(value = "分页查询")
	@GetMapping("/getBasicsdatumCategoryMeasureList")
	public PageInfo<BasicsdatumCategoryMeasureVo> getBasicsdatumCategoryMeasureList(QueryDto queryDto) {
		return  basicsdatumCategoryMeasureService.getBasicsdatumCategoryMeasureList(queryDto);
	}

	@ApiOperation(value = "/导入")
	@PostMapping("/basicsdatumCategoryMeasureImportExcel")
	public Boolean basicsdatumCategoryMeasureImportExcel(@RequestParam("file") MultipartFile file) throws Exception {
	return basicsdatumCategoryMeasureService.basicsdatumCategoryMeasureImportExcel(file);
	}

	@ApiOperation(value = "/导出")
	@GetMapping("/basicsdatumCategoryMeasureDeriveExcel")
	public void basicsdatumCategoryMeasureDeriveExcel(HttpServletResponse response) throws Exception {
       basicsdatumCategoryMeasureService.basicsdatumCategoryMeasureDeriveExcel(response);
	}

	@ApiOperation(value = "批量启用/停用", notes = "ids:, status:0启用1停用")
	@PostMapping("/startStopBasicsdatumCategoryMeasure")
	public Boolean startStopBasicsdatumCategoryMeasure(@Valid @RequestBody StartStopDto startStopDto) {
	return basicsdatumCategoryMeasureService.startStopBasicsdatumCategoryMeasure(startStopDto);
	}


	@ApiOperation(value = "新增修改基础资料-品类测量组")
	@PostMapping("/addRevampBasicsdatumCategoryMeasure")
	public Boolean addRevampBasicsdatumCategoryMeasure(@Valid @RequestBody AddRevampBasicsdatumCategoryMeasureDto addRevampBasicsdatumCategoryMeasureDto) {
	return basicsdatumCategoryMeasureService.addRevampBasicsdatumCategoryMeasure(addRevampBasicsdatumCategoryMeasureDto);
	}

	@ApiOperation(value = "删除基础资料-品类测量组")
	@DeleteMapping("/delBasicsdatumCategoryMeasure")
	public Boolean delBasicsdatumCategoryMeasure(@Valid @NotBlank(message = "编号id不能为空") String id) {
	return basicsdatumCategoryMeasureService.delBasicsdatumCategoryMeasure(id);
	}

	@ApiOperation(value = "明细-通过id查询")
	@GetMapping("/{id}")
	public BasicsdatumCategoryMeasure getById(@PathVariable("id") String id) {
		return basicsdatumCategoryMeasureService.getById(id);
	}


}































