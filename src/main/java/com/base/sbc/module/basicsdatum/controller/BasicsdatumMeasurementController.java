/******************************************************************************
* Copyright (C) 2018 广州尚捷科技有限责任公司
* All Rights Reserved.
* 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
* 不得使用、复制、修改或发布本软件.
*****************************************************************************/
package com.base.sbc.module.basicsdatum.controller;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.AddRevampComponentDto;
import com.base.sbc.module.basicsdatum.dto.AddRevampMeasurementDto;
import com.base.sbc.module.basicsdatum.dto.QueryDto;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMeasurement;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMeasurementService;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumComponentVo;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumMeasurementVo;
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
* 类描述：基础资料-测量点 Controller类
* @address com.base.sbc.module.basicsdatum.web.BasicsdatumMeasurementController
* @author mengfanjiang
* @email 2915350015@qq.com
* @date 创建时间：2023-5-17 9:35:14
* @version 1.0
*/
@RestController
@Api(tags = "基础资料-测量点")
@RequestMapping(value = BaseController.SAAS_URL + "/measurement", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class BasicsdatumMeasurementController{

	@Autowired
	private BasicsdatumMeasurementService basicsdatumMeasurementService;

	@ApiOperation(value = "分页查询")
	@GetMapping("/getMeasurementList")
	public PageInfo<BasicsdatumMeasurementVo> getMeasurement(@Valid QueryDto queryDto) {
		return basicsdatumMeasurementService.getMeasurement(queryDto);
	}


	@ApiOperation(value = "/导入")
	@PostMapping("/importExcel")
	public ApiResult importExcel(@RequestParam("file") MultipartFile file) throws Exception {
		return ApiResult.success("操作成功",basicsdatumMeasurementService.importExcel(file)) ;
	}


	@ApiOperation(value = "新增修改部件")
	@PostMapping("/addRevampMeasurement")
	public ApiResult addRevampComponent(@Valid @RequestBody AddRevampMeasurementDto addRevampMeasurementDto) {
		return ApiResult.success("操作成功",basicsdatumMeasurementService.addRevampMeasurement(addRevampMeasurementDto)) ;
	}


	@ApiOperation(value = "删除部件数据")
	@DeleteMapping("/delMeasurement")
	public ApiResult delMeasurement(@Valid @NotBlank(message = "编号id不能为空") String id) {
		return ApiResult.success("操作成功",basicsdatumMeasurementService.delMeasurement(id));
	}



	@ApiOperation(value = "批量启用/停用", notes = "ids:, status:0启用1停用")
	@PostMapping("/measurementStartStop")
	public ApiResult measurementStartStop(@Valid @RequestBody StartStopDto startStopDto) {
		return ApiResult.success("操作成功",basicsdatumMeasurementService.measurementStartStop(startStopDto));
	}

}































