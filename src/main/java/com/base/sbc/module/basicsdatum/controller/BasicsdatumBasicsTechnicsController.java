/******************************************************************************
* Copyright (C) 2018 广州尚捷科技有限责任公司
* All Rights Reserved.
* 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
* 不得使用、复制、修改或发布本软件.
*****************************************************************************/
package com.base.sbc.module.basicsdatum.controller;

import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.basicsdatum.dto.AddRevampBasicsdatumBasicsTechnicsDto;
import com.base.sbc.module.basicsdatum.dto.QueryDto;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumBasicsTechnics;
import com.base.sbc.module.basicsdatum.service.BasicsdatumBasicsTechnicsService;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumBasicsTechnicsVo;
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
* 类描述：基础资料-基础工艺 Controller类
* @address com.base.sbc.module.basicsdatum.web.BasicsdatumBasicsTechnicsController
* @author mengfanjiang
* @email 2915350015@qq.com
* @date 创建时间：2023-5-19 19:15:00
* @version 1.0
*/
@RestController
@Api(tags = "基础资料-基础工艺")
@RequestMapping(value = BaseController.SAAS_URL + "/basicsdatumBasicsTechnics", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class BasicsdatumBasicsTechnicsController{

	@Autowired
	private BasicsdatumBasicsTechnicsService basicsdatumBasicsTechnicsService;

	@ApiOperation(value = "分页查询")
	@GetMapping("/getBasicsdatumBasicsTechnicsList")
	public PageInfo<BasicsdatumBasicsTechnicsVo> getBasicsdatumBasicsTechnicsList(QueryDto queryDto) {
		return  basicsdatumBasicsTechnicsService.getBasicsdatumBasicsTechnicsList(queryDto);
	}

	@ApiOperation(value = "/导入")
	@PostMapping("/basicsdatumBasicsTechnicsImportExcel")
	public Boolean basicsdatumBasicsTechnicsImportExcel(@RequestParam("file") MultipartFile file) throws Exception {
	return basicsdatumBasicsTechnicsService.basicsdatumBasicsTechnicsImportExcel(file);
	}

	@ApiOperation(value = "/导出")
	@GetMapping("/basicsdatumBasicsTechnicsDeriveExcel")
	public void basicsdatumBasicsTechnicsDeriveExcel(HttpServletResponse response) throws Exception {
       basicsdatumBasicsTechnicsService.basicsdatumBasicsTechnicsDeriveExcel(response);
	}

	@ApiOperation(value = "批量启用/停用", notes = "ids:, status:0启用1停用")
	@PostMapping("/startStopBasicsdatumBasicsTechnics")
	public Boolean startStopBasicsdatumBasicsTechnics(@Valid @RequestBody StartStopDto startStopDto) {
	return basicsdatumBasicsTechnicsService.startStopBasicsdatumBasicsTechnics(startStopDto);
	}


	@ApiOperation(value = "新增修改基础资料-基础工艺")
	@PostMapping("/addRevampBasicsdatumBasicsTechnics")
	public Boolean addRevampBasicsdatumBasicsTechnics(@Valid @RequestBody AddRevampBasicsdatumBasicsTechnicsDto addRevampBasicsdatumBasicsTechnicsDto) {
	return basicsdatumBasicsTechnicsService.addRevampBasicsdatumBasicsTechnics(addRevampBasicsdatumBasicsTechnicsDto);
	}

	@ApiOperation(value = "删除基础资料-基础工艺")
	@DeleteMapping("/delBasicsdatumBasicsTechnics")
	public Boolean delBasicsdatumBasicsTechnics(@Valid @NotBlank(message = "编号id不能为空") String id) {
	return basicsdatumBasicsTechnicsService.delBasicsdatumBasicsTechnics(id);
	}

	@ApiOperation(value = "明细-通过id查询")
	@GetMapping("/{id}")
	public BasicsdatumBasicsTechnics getById(@PathVariable("id") String id) {
		return basicsdatumBasicsTechnicsService.getById(id);
	}


}































