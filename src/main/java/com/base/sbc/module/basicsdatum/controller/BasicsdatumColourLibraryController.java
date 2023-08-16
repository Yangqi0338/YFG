/******************************************************************************
* Copyright (C) 2018 广州尚捷科技有限责任公司
* All Rights Reserved.
* 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
* 不得使用、复制、修改或发布本软件.
*****************************************************************************/
package com.base.sbc.module.basicsdatum.controller;

import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.basicsdatum.dto.AddRevampBasicsdatumColourLibraryDto;
import com.base.sbc.module.basicsdatum.dto.QueryBasicsdatumColourLibraryDto;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumColourLibrary;
import com.base.sbc.module.basicsdatum.service.BasicsdatumColourLibraryService;
import com.base.sbc.module.common.vo.SelectOptionsVo;
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
import java.util.List;

/**
* 类描述：基础资料-颜色库 Controller类
* @address com.base.sbc.module.basicsdatum.web.BasicsdatumColourLibraryController
* @author mengfanjiang
* @email 2915350015@qq.com
* @date 创建时间：2023-5-20 20:23:02
* @version 1.0
*/
@RestController
@Api(tags = "基础资料-颜色库")
@RequestMapping(value = BaseController.SAAS_URL + "/basicsdatumColourLibrary", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class BasicsdatumColourLibraryController{

	@Autowired
	private BasicsdatumColourLibraryService basicsdatumColourLibraryService;


	@ApiOperation(value = "分页查询")
	@GetMapping("/getBasicsdatumColourLibraryList")
	public PageInfo getBasicsdatumColourLibraryList(QueryBasicsdatumColourLibraryDto queryBasicsdatumColourLibraryDto) {
		return  basicsdatumColourLibraryService.getBasicsdatumColourLibraryList(queryBasicsdatumColourLibraryDto);
	}

	@ApiOperation(value = "/导入")
	@PostMapping("/basicsdatumColourLibraryImportExcel")
	public Boolean basicsdatumColourLibraryImportExcel(@RequestParam("file") MultipartFile file) throws Exception {
	return basicsdatumColourLibraryService.basicsdatumColourLibraryImportExcel(file);
	}

	@ApiOperation(value = "/导出")
	@GetMapping("/basicsdatumColourLibraryDeriveExcel")
	public void basicsdatumColourLibraryDeriveExcel(HttpServletResponse response) throws Exception {
       basicsdatumColourLibraryService.basicsdatumColourLibraryDeriveExcel(response);
	}

	@ApiOperation(value = "批量启用/停用", notes = "ids:, status:0启用1停用")
	@PostMapping("/startStopBasicsdatumColourLibrary")
	public Boolean startStopBasicsdatumColourLibrary(@Valid @RequestBody StartStopDto startStopDto) {
	return basicsdatumColourLibraryService.startStopBasicsdatumColourLibrary(startStopDto);
	}


	@ApiOperation(value = "新增修改基础资料-颜色库")
	@PostMapping("/addRevampBasicsdatumColourLibrary")
	public Boolean addRevampBasicsdatumColourLibrary(@Valid @RequestBody AddRevampBasicsdatumColourLibraryDto addRevampBasicsdatumColourLibraryDto) {
	return basicsdatumColourLibraryService.addRevampBasicsdatumColourLibrary(addRevampBasicsdatumColourLibraryDto);
	}

	@ApiOperation(value = "删除基础资料-颜色库")
	@DeleteMapping("/delBasicsdatumColourLibrary")
	public Boolean delBasicsdatumColourLibrary(@Valid @NotBlank(message = "编号id不能为空") String id) {
	return basicsdatumColourLibraryService.delBasicsdatumColourLibrary(id);
	}

	@ApiOperation(value = "明细-通过id查询")
	@GetMapping("/{id}")
	public BasicsdatumColourLibrary getById(@PathVariable("id") String id) {
		return basicsdatumColourLibraryService.getById(id);
	}


	@ApiOperation(value = "获取所有颜色规格(下拉选择)")
	@GetMapping("/getAllColourSpecification")
	public List<SelectOptionsVo> getAllColourSpecification(String status,String isStyle,String isMaterials) {
		return basicsdatumColourLibraryService.getAllColourSpecification(status,isStyle,isMaterials);
	}


}































