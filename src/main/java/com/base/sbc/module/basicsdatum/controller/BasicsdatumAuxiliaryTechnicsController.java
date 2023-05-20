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
import com.base.sbc.module.basicsdatum.entity.BasicsdatumAuxiliaryTechnics;
import com.base.sbc.module.basicsdatum.service.BasicsdatumAuxiliaryTechnicsService;
import com.base.sbc.module.basicsdatum.dto.AddRevampBasicsdatumAuxiliaryTechnicsDto;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumAuxiliaryTechnicsVo;
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
* 类描述：基础资料-外辅工艺 Controller类
* @address com.base.sbc.module.basicsdatum.web.BasicsdatumAuxiliaryTechnicsController
* @author mengfanjiang
* @email 2915350015@qq.com
* @date 创建时间：2023-5-20 19:08:56
* @version 1.0
*/
@RestController
@Api(tags = "基础资料-外辅工艺")
@RequestMapping(value = BaseController.SAAS_URL + "/basicsdatumAuxiliaryTechnics", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class BasicsdatumAuxiliaryTechnicsController{

	@Autowired
	private BasicsdatumAuxiliaryTechnicsService basicsdatumAuxiliaryTechnicsService;

	@ApiOperation(value = "分页查询")
	@GetMapping("/getBasicsdatumAuxiliaryTechnicsList")
	public PageInfo<BasicsdatumAuxiliaryTechnicsVo> getBasicsdatumAuxiliaryTechnicsList(QueryDto queryDto) {
		return  basicsdatumAuxiliaryTechnicsService.getBasicsdatumAuxiliaryTechnicsList(queryDto);
	}

	@ApiOperation(value = "/导入")
	@PostMapping("/basicsdatumAuxiliaryTechnicsImportExcel")
	public Boolean basicsdatumAuxiliaryTechnicsImportExcel(@RequestParam("file") MultipartFile file) throws Exception {
	return basicsdatumAuxiliaryTechnicsService.basicsdatumAuxiliaryTechnicsImportExcel(file);
	}

	@ApiOperation(value = "/导出")
	@GetMapping("/basicsdatumAuxiliaryTechnicsDeriveExcel")
	public void basicsdatumAuxiliaryTechnicsDeriveExcel(HttpServletResponse response) throws Exception {
       basicsdatumAuxiliaryTechnicsService.basicsdatumAuxiliaryTechnicsDeriveExcel(response);
	}

	@ApiOperation(value = "批量启用/停用", notes = "ids:, status:0启用1停用")
	@PostMapping("/startStopBasicsdatumAuxiliaryTechnics")
	public Boolean startStopBasicsdatumAuxiliaryTechnics(@Valid @RequestBody StartStopDto startStopDto) {
	return basicsdatumAuxiliaryTechnicsService.startStopBasicsdatumAuxiliaryTechnics(startStopDto);
	}


	@ApiOperation(value = "新增修改基础资料-外辅工艺")
	@PostMapping("/addRevampBasicsdatumAuxiliaryTechnics")
	public Boolean addRevampBasicsdatumAuxiliaryTechnics(@Valid @RequestBody AddRevampBasicsdatumAuxiliaryTechnicsDto addRevampBasicsdatumAuxiliaryTechnicsDto) {
	return basicsdatumAuxiliaryTechnicsService.addRevampBasicsdatumAuxiliaryTechnics(addRevampBasicsdatumAuxiliaryTechnicsDto);
	}

	@ApiOperation(value = "删除基础资料-外辅工艺")
	@DeleteMapping("/delBasicsdatumAuxiliaryTechnics")
	public Boolean delBasicsdatumAuxiliaryTechnics(@Valid @NotBlank(message = "编号id不能为空") String id) {
	return basicsdatumAuxiliaryTechnicsService.delBasicsdatumAuxiliaryTechnics(id);
	}

	@ApiOperation(value = "明细-通过id查询")
	@GetMapping("/{id}")
	public BasicsdatumAuxiliaryTechnics getById(@PathVariable("id") String id) {
		return basicsdatumAuxiliaryTechnicsService.getById(id);
	}


}































