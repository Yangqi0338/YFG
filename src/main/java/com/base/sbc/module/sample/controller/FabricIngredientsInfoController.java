/******************************************************************************
* Copyright (C) 2018 广州尚捷科技有限责任公司
* All Rights Reserved.
* 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
* 不得使用、复制、修改或发布本软件.
*****************************************************************************/
package com.base.sbc.module.sample.controller;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.Page;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.sample.dto.QueryFabricIngredientsInfoDto;
import com.base.sbc.module.sample.entity.FabricIngredientsInfo;
import com.base.sbc.module.sample.service.FabricIngredientsInfoService;
import com.base.sbc.module.sample.dto.AddRevampFabricIngredientsInfoDto;
import com.base.sbc.module.sample.vo.FabricIngredientsInfoVo;
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
* 类描述：调样-辅料信息 Controller类
* @address com.base.sbc.module.sample.web.FabricIngredientsInfoController
* @author mengfanjiang
* @email XX.com
* @date 创建时间：2023-7-14 17:32:38
* @version 1.0
*/
@RestController
@Api(tags = "调样-辅料信息")
@RequestMapping(value = BaseController.SAAS_URL + "/fabricIngredientsInfo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class FabricIngredientsInfoController{

	@Autowired
	private FabricIngredientsInfoService fabricIngredientsInfoService;

	@ApiOperation(value = "分页查询")
	@GetMapping("/getFabricIngredientsInfoList")
	public PageInfo<FabricIngredientsInfoVo> getFabricIngredientsInfoList(QueryFabricIngredientsInfoDto queryFabricIngredientsInfoDto) {
		return  fabricIngredientsInfoService.getFabricIngredientsInfoList(queryFabricIngredientsInfoDto);
	}



	@ApiOperation(value = "批量启用/停用", notes = "ids:, status:0启用1停用")
	@PostMapping("/startStopFabricIngredientsInfo")
	public Boolean startStopFabricIngredientsInfo(@Valid @RequestBody StartStopDto startStopDto) {
	return fabricIngredientsInfoService.startStopFabricIngredientsInfo(startStopDto);
	}


	@ApiOperation(value = "新增修改调样-辅料信息")
	@PostMapping("/addRevampFabricIngredientsInfo")
	public Boolean addRevampFabricIngredientsInfo(@Valid @RequestBody AddRevampFabricIngredientsInfoDto addRevampFabricIngredientsInfoDto) {
	return fabricIngredientsInfoService.addRevampFabricIngredientsInfo(addRevampFabricIngredientsInfoDto);
	}

	@ApiOperation(value = "删除调样-辅料信息")
	@DeleteMapping("/delFabricIngredientsInfo")
	public Boolean delFabricIngredientsInfo(@Valid @NotBlank(message = "编号id不能为空") String id) {
	return fabricIngredientsInfoService.delFabricIngredientsInfo(id);
	}

	@ApiOperation(value = "明细-通过id查询")
	@GetMapping("/{id}")
	public FabricIngredientsInfo getById(@PathVariable("id") String id) {
		return fabricIngredientsInfoService.getById(id);
	}


}































