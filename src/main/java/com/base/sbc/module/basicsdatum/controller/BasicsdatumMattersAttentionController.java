/******************************************************************************
* Copyright (C) 2018 广州尚捷科技有限责任公司
* All Rights Reserved.
* 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
* 不得使用、复制、修改或发布本软件.
*****************************************************************************/
package com.base.sbc.module.basicsdatum.controller;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.basicsdatum.dto.AddRevampBasicsdatumMattersAttentionDto;
import com.base.sbc.module.basicsdatum.dto.QueryDto;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMattersAttention;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMattersAttentionService;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumMattersAttentionVo;
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
* 类描述：基础资料-注意事项 Controller类
* @address com.base.sbc.module.basicsdatum.web.BasicsdatumMattersAttentionController
* @author mengfanjiang
* @email 2915350015@qq.com
* @date 创建时间：2023-5-19 19:15:01
* @version 1.0
*/
@RestController
@Api(tags = "基础资料-注意事项")
@RequestMapping(value = BaseController.SAAS_URL + "/basicsdatumMattersAttention", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class BasicsdatumMattersAttentionController{

	@Autowired
	private BasicsdatumMattersAttentionService basicsdatumMattersAttentionService;

	@ApiOperation(value = "分页查询")
	@GetMapping("/getBasicsdatumMattersAttentionList")
	public PageInfo<BasicsdatumMattersAttentionVo> getBasicsdatumMattersAttentionList(QueryDto queryDto) {
		return  basicsdatumMattersAttentionService.getBasicsdatumMattersAttentionList(queryDto);
	}

	@ApiOperation(value = "/导入")
	@PostMapping("/basicsdatumMattersAttentionImportExcel")
	public ApiResult basicsdatumMattersAttentionImportExcel(@RequestParam("file") MultipartFile file) throws Exception {
	return ApiResult.success("操作成功",basicsdatumMattersAttentionService.basicsdatumMattersAttentionImportExcel(file)) ;
	}

	@ApiOperation(value = "/导出")
	@GetMapping("/basicsdatumMattersAttentionDeriveExcel")
	public void basicsdatumMattersAttentionDeriveExcel(HttpServletResponse response) throws Exception {
       basicsdatumMattersAttentionService.basicsdatumMattersAttentionDeriveExcel(response);
	}

	@ApiOperation(value = "批量启用/停用", notes = "ids:, status:0启用1停用")
	@PostMapping("/startStopBasicsdatumMattersAttention")
	public Boolean startStopBasicsdatumMattersAttention(@Valid @RequestBody StartStopDto startStopDto) {
	return basicsdatumMattersAttentionService.startStopBasicsdatumMattersAttention(startStopDto);
	}


	@ApiOperation(value = "新增修改基础资料-注意事项")
	@PostMapping("/addRevampBasicsdatumMattersAttention")
	public Boolean addRevampBasicsdatumMattersAttention(@Valid @RequestBody AddRevampBasicsdatumMattersAttentionDto addRevampBasicsdatumMattersAttentionDto) {
	return basicsdatumMattersAttentionService.addRevampBasicsdatumMattersAttention(addRevampBasicsdatumMattersAttentionDto);
	}

	@ApiOperation(value = "删除基础资料-注意事项")
	@DeleteMapping("/delBasicsdatumMattersAttention")
	public Boolean delBasicsdatumMattersAttention(@Valid @NotBlank(message = "编号id不能为空") String id) {
	return basicsdatumMattersAttentionService.delBasicsdatumMattersAttention(id);
	}

	@ApiOperation(value = "明细-通过id查询")
	@GetMapping("/{id}")
	public BasicsdatumMattersAttention getById(@PathVariable("id") String id) {
		return basicsdatumMattersAttentionService.getById(id);
	}


}































