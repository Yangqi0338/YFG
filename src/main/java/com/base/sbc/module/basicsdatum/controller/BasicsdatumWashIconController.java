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
import com.base.sbc.module.basicsdatum.entity.BasicsdatumWashIcon;
import com.base.sbc.module.basicsdatum.service.BasicsdatumWashIconService;
import com.base.sbc.module.basicsdatum.dto.AddRevampBasicsdatumWashIconDto;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumWashIconVo;
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
* 类描述：基础资料-洗涤图标 Controller类
* @address com.base.sbc.module.basicsdatum.web.BasicsdatumWashIconController
* @author mengfanjiang
* @email XX.com
* @date 创建时间：2023-7-27 17:27:54
* @version 1.0
*/
@RestController
@Api(tags = "基础资料-洗涤图标")
@RequestMapping(value = BaseController.SAAS_URL + "/basicsdatumWashIcon", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class BasicsdatumWashIconController{

	@Autowired
	private BasicsdatumWashIconService basicsdatumWashIconService;

	@ApiOperation(value = "分页查询")
	@GetMapping("/getBasicsdatumWashIconList")
	public PageInfo<BasicsdatumWashIconVo> getBasicsdatumWashIconList(QueryDto queryDto) {
		return  basicsdatumWashIconService.getBasicsdatumWashIconList(queryDto);
	}



	@ApiOperation(value = "批量启用/停用", notes = "ids:, status:0启用1停用")
	@PostMapping("/startStopBasicsdatumWashIcon")
	public Boolean startStopBasicsdatumWashIcon(@Valid @RequestBody StartStopDto startStopDto) {
	return basicsdatumWashIconService.startStopBasicsdatumWashIcon(startStopDto);
	}


	@ApiOperation(value = "新增修改基础资料-洗涤图标")
	@PostMapping("/addRevampBasicsdatumWashIcon")
	public Boolean addRevampBasicsdatumWashIcon(@Valid @RequestBody AddRevampBasicsdatumWashIconDto addRevampBasicsdatumWashIconDto) {
	return basicsdatumWashIconService.addRevampBasicsdatumWashIcon(addRevampBasicsdatumWashIconDto);
	}

	@ApiOperation(value = "删除基础资料-洗涤图标")
	@DeleteMapping("/delBasicsdatumWashIcon")
	public Boolean delBasicsdatumWashIcon(@Valid @NotBlank(message = "编号id不能为空") String id) {
	return basicsdatumWashIconService.delBasicsdatumWashIcon(id);
	}

	@ApiOperation(value = "明细-通过id查询")
	@GetMapping("/{id}")
	public BasicsdatumWashIcon getById(@PathVariable("id") String id) {
		return basicsdatumWashIconService.getById(id);
	}


}































