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
import com.base.sbc.module.basicsdatum.entity.BasicsdatumPressingPacking;
import com.base.sbc.module.basicsdatum.service.BasicsdatumPressingPackingService;
import com.base.sbc.module.basicsdatum.dto.AddRevampBasicsdatumPressingPackingDto;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumPressingPackingVo;
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
* 类描述：基础资料-洗涤图标与温馨提示 Controller类
* @address com.base.sbc.module.basicsdatum.web.BasicsdatumPressingPackingController
* @author mengfanjiang
* @email 2915350015@qq.com
* @date 创建时间：2023-5-19 19:15:00
* @version 1.0
*/
@RestController
@Api(tags = "基础资料-洗涤图标与温馨提示")
@RequestMapping(value = BaseController.SAAS_URL + "/basicsdatumPressingPacking", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class BasicsdatumPressingPackingController{

	@Autowired
	private BasicsdatumPressingPackingService basicsdatumPressingPackingService;

	@ApiOperation(value = "分页查询")
	@GetMapping("/getBasicsdatumPressingPackingList")
	public PageInfo<BasicsdatumPressingPackingVo> getBasicsdatumPressingPackingList(QueryDto queryDto) {
		return  basicsdatumPressingPackingService.getBasicsdatumPressingPackingList(queryDto);
	}

	@ApiOperation(value = "/导入")
	@PostMapping("/basicsdatumPressingPackingImportExcel")
	public ApiResult basicsdatumPressingPackingImportExcel(@RequestParam("file") MultipartFile file) throws Exception {
	return ApiResult.success("操作成功",basicsdatumPressingPackingService.basicsdatumPressingPackingImportExcel(file)) ;
	}

	@ApiOperation(value = "/导出")
	@GetMapping("/basicsdatumPressingPackingDeriveExcel")
	public void basicsdatumPressingPackingDeriveExcel(HttpServletResponse response) throws Exception {
       basicsdatumPressingPackingService.basicsdatumPressingPackingDeriveExcel(response);
	}

	@ApiOperation(value = "批量启用/停用", notes = "ids:, status:0启用1停用")
	@PostMapping("/startStopBasicsdatumPressingPacking")
	public Boolean startStopBasicsdatumPressingPacking(@Valid @RequestBody StartStopDto startStopDto) {
	return basicsdatumPressingPackingService.startStopBasicsdatumPressingPacking(startStopDto);
	}


	@ApiOperation(value = "新增修改基础资料-洗涤图标与温馨提示")
	@PostMapping("/addRevampBasicsdatumPressingPacking")
	public Boolean addRevampBasicsdatumPressingPacking(@Valid @RequestBody AddRevampBasicsdatumPressingPackingDto addRevampBasicsdatumPressingPackingDto) {
	return basicsdatumPressingPackingService.addRevampBasicsdatumPressingPacking(addRevampBasicsdatumPressingPackingDto);
	}

	@ApiOperation(value = "删除基础资料-洗涤图标与温馨提示")
	@DeleteMapping("/delBasicsdatumPressingPacking")
	public Boolean delBasicsdatumPressingPacking(@Valid @NotBlank(message = "编号id不能为空") String id) {
	return basicsdatumPressingPackingService.delBasicsdatumPressingPacking(id);
	}

	@ApiOperation(value = "明细-通过id查询")
	@GetMapping("/{id}")
	public BasicsdatumPressingPacking getById(@PathVariable("id") String id) {
		return basicsdatumPressingPackingService.getById(id);
	}


}































