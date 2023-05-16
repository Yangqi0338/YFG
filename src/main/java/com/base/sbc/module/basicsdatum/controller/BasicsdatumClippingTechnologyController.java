/******************************************************************************
* Copyright (C) 2018 广州尚捷科技有限责任公司
* All Rights Reserved.
* 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
* 不得使用、复制、修改或发布本软件.
*****************************************************************************/
package com.base.sbc.module.basicsdatum.controller;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.Page;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.AddRevampComponentDto;
import com.base.sbc.module.basicsdatum.dto.AddRevampTechnologyDto;
import com.base.sbc.module.basicsdatum.dto.QueryComponentDto;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumClippingTechnology;
import com.base.sbc.module.basicsdatum.service.BasicsdatumClippingTechnologyService;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumTechnologyVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

/**
* 类描述：基础资料-裁剪工艺 Controller类
* @address com.base.sbc.module.basicsdatum.web.BasicsdatumClippingTechnologyController
* @author mengfanjiang
* @email 2915350015@qq.com
* @date 创建时间：2023-5-15 17:31:31
* @version 1.0
*/
@RestController
@Api(tags = "基础资料-裁剪工艺")
@RequestMapping(value = BaseController.SAAS_URL + "/clippingTechnology", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class BasicsdatumClippingTechnologyController{

	@Autowired
	private BasicsdatumClippingTechnologyService basicsdatumClippingTechnologyService;

	@ApiOperation(value = "分页查询")
	@GetMapping("/getTechnologyList")
	public PageInfo<BasicsdatumTechnologyVo> getTechnologyList(QueryComponentDto queryComponentDto) {
		return  basicsdatumClippingTechnologyService.getTechnologyList(queryComponentDto);
	}


	@ApiOperation(value = "/导入")
	@PostMapping("/importExcel")
	public ApiResult importExcel( @RequestParam("file") MultipartFile file) throws Exception {
		return ApiResult.success("操作成功",basicsdatumClippingTechnologyService.importExcel(file)) ;
	}


	@ApiOperation(value = "新增修改裁剪工艺")
	@PostMapping("/addRevampTechnology")
	public ApiResult addRevampTechnology(@Valid @RequestBody AddRevampTechnologyDto addRevampComponentDto) {
		return ApiResult.success("操作成功",basicsdatumClippingTechnologyService.addRevampTechnology(addRevampComponentDto)) ;
	}

	@ApiOperation(value = "删除部件数据裁剪工艺")
	@DeleteMapping("/delTechnology")
	public ApiResult delTechnology(@Valid @NotBlank(message = "编号id不能为空") String id) {
		return ApiResult.success("操作成功",basicsdatumClippingTechnologyService.delTechnology(id));
	}



	@ApiOperation(value = "批量启用/停用", notes = "ids:, status:0启用1停用")
	@PostMapping("/technologyStartStop")
	public ApiResult technologyStartStop(@Valid @RequestBody StartStopDto startStopDto) {
		return ApiResult.success("操作成功",basicsdatumClippingTechnologyService.technologyStartStop(startStopDto));
	}



}































