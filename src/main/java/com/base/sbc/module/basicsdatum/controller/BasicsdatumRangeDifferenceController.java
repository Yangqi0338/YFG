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
import com.base.sbc.module.basicsdatum.entity.BasicsdatumRangeDifference;
import com.base.sbc.module.basicsdatum.service.BasicsdatumRangeDifferenceService;
import com.base.sbc.module.basicsdatum.dto.AddRevampBasicsdatumRangeDifferenceDto;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumRangeDifferenceVo;
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
* 类描述：基础资料-档差 Controller类
* @address com.base.sbc.module.basicsdatum.web.BasicsdatumRangeDifferenceController
* @author mengfanjiang
* @email 2915350015@qq.com
* @date 创建时间：2023-5-18 19:42:16
* @version 1.0
*/
@RestController
@Api(tags = "基础资料-档差")
@RequestMapping(value = BaseController.SAAS_URL + "/basicsdatumRangeDifference", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class BasicsdatumRangeDifferenceController{

	@Autowired
	private BasicsdatumRangeDifferenceService basicsdatumRangeDifferenceService;

	@ApiOperation(value = "分页查询")
	@GetMapping("/getList")
	public PageInfo<BasicsdatumRangeDifferenceVo> getList(QueryDto queryDto) {
		return  basicsdatumRangeDifferenceService.getList(queryDto);
	}

	@ApiOperation(value = "/导入")
	@PostMapping("/importExcel")
	public Boolean importExcel(@RequestParam("file") MultipartFile file) throws Exception {
	return basicsdatumRangeDifferenceService.importExcel(file);
	}

	@ApiOperation(value = "/导出")
	@GetMapping("/deriveExcel")
	public void deriveExcel(HttpServletResponse response) throws Exception {
       basicsdatumRangeDifferenceService.deriveExcel(response);
	}

	@ApiOperation(value = "批量启用/停用", notes = "ids:, status:0启用1停用")
	@PostMapping("/startStop")
	public Boolean startStop(@Valid @RequestBody StartStopDto startStopDto) {
	return basicsdatumRangeDifferenceService.startStop(startStopDto);
	}


	@ApiOperation(value = "新增修改基础资料-档差")
	@PostMapping("/addRevamp")
	public Boolean addRevamp(@Valid @RequestBody AddRevampBasicsdatumRangeDifferenceDto addRevampBasicsdatumRangeDifferenceDto) {
	return basicsdatumRangeDifferenceService.addRevamp(addRevampBasicsdatumRangeDifferenceDto);
	}

	@ApiOperation(value = "删除基础资料-档差")
	@DeleteMapping("/del")
	public Boolean del(@Valid @NotBlank(message = "编号id不能为空") String id) {
	return basicsdatumRangeDifferenceService.del(id);
	}

	@ApiOperation(value = "明细-通过id查询")
	@GetMapping("/{id}")
	public BasicsdatumRangeDifference getById(@PathVariable("id") String id) {
		return basicsdatumRangeDifferenceService.getById(id);
	}


}































