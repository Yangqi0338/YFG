/******************************************************************************
* Copyright (C) 2018 广州尚捷科技有限责任公司
* All Rights Reserved.
* 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
* 不得使用、复制、修改或发布本软件.
*****************************************************************************/
package com.base.sbc.module.basicsdatum.controller;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.basicsdatum.dto.AddRevampComponentDto;
import com.base.sbc.module.basicsdatum.dto.QueryComponentDto;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.basicsdatum.service.BasicsdatumComponentService;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumComponentVo;
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

/**
* 类描述：基础资料-部件 Controller类
* @address com.base.sbc.module.basicsdatum.web.BasicsdatumComponentController
* @author mengfanjiang
* @email 2915350015@qq.com
* @date 创建时间：2023-5-10 11:37:55
* @version 1.0
*/
@RestController
@Api(tags = "基础资料-部件")
@RequestMapping(value = BaseController.SAAS_URL + "/basicsdatumComponent", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class BasicsdatumComponentController{

	@Autowired
	private BasicsdatumComponentService basicsdatumComponentService;


	@ApiOperation(value = "分页查询")
	@GetMapping("/getComponentList")
	public PageInfo<BasicsdatumComponentVo> getComponentList(@Valid QueryComponentDto queryComponentDto) {
		return basicsdatumComponentService.getComponentList(queryComponentDto);
	}


	@ApiOperation(value = "/导入")
	@PostMapping("/importExcel")
	public ApiResult importExcel( @RequestParam("file") MultipartFile file) throws Exception {
		 return ApiResult.success("操作成功",basicsdatumComponentService.importExcel(file)) ;
	}


	@ApiOperation(value = "新增修改部件")
	@PostMapping("/addRevampComponent")
	public ApiResult addRevampComponent(@Valid @RequestBody AddRevampComponentDto addRevampComponentDto) {
		return ApiResult.success("操作成功",basicsdatumComponentService.addRevampComponent(addRevampComponentDto)) ;
	}


	@ApiOperation(value = "删除部件数据")
	@DeleteMapping("/delComponent")
	public ApiResult delComponent(@Valid @NotBlank(message = "编号id不能为空") String id) {
		return ApiResult.success("操作成功",basicsdatumComponentService.delComponent(id));
	}



	@ApiOperation(value = "批量启用/停用", notes = "ids:, status:0启用1停用")
	@PostMapping("/componentStartStop")
	public ApiResult componentStartStop(@Valid @RequestBody StartStopDto startStopDto) {
		return ApiResult.success("操作成功",basicsdatumComponentService.componentStartStop(startStopDto));
	}


}































