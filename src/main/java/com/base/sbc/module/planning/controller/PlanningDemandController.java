/******************************************************************************
* Copyright (C) 2018 广州尚捷科技有限责任公司
* All Rights Reserved.
* 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
* 不得使用、复制、修改或发布本软件.
*****************************************************************************/
package com.base.sbc.module.planning.controller;

import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.planning.dto.*;
import com.base.sbc.module.planning.service.PlanningCategoryItemService;
import com.base.sbc.module.planning.service.PlanningDemandProportionDataService;
import com.base.sbc.module.planning.service.PlanningDemandService;
import com.base.sbc.module.planning.service.PlanningDimensionalityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
* 类描述：企划-需求管理相关接口
* @address com.base.sbc.module.planning.web.PlanningDemandDimensionalityController
* @author lxl
* @email lxl.fml@gmail.com
* @date 创建时间：2023-4-26 17:42:18
* @version 1.0
*/
@RestController
@Api(tags = "企划-需求管理相关接口")
@RequestMapping(value = BaseController.SAAS_URL + "/planningDemand", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class PlanningDemandController {

	@Resource
	private PlanningDemandService planningDemandService;

	@Resource
	private PlanningDemandProportionDataService planningDemandProportionDataService;

	@Resource
	private PlanningDimensionalityService planningDimensionalityService;

	@Resource
	private	PlanningCategoryItemService planningCategoryItemService;


    @Resource
	private CcmFeignService ccmFeignService;

	/*品类id获取需求及维度*/
	@ApiOperation(value = "品类id获取需求列表")
	@GetMapping("/getDemandListById")
	public ApiResult getDemandListById(QueryDemandDto queryDemandDto) {
		return planningDemandService.getDemandListById(queryDemandDto);
	}

	/*获取穿梭框表添加需求占比数据*/
	@ApiOperation(value = "获取穿梭框表添加需求占比数据")
	@GetMapping("/getFormDemand")
	public ApiResult getFormDemand(QueryDemandDto queryDemandDto) {
		return planningDemandService.getFormDemand(queryDemandDto);
	}


   /*穿梭框新增删除需求占比数据*/
	@ApiOperation(value = "穿梭框新增删除需求占比数据")
	@PostMapping("saveDel")
	public ApiResult saveDel(@RequestBody List<SaveDelDemandDto> saveDelDemandDto) {
		return planningDemandService.saveDel(saveDelDemandDto);
	}


	/*新增修改需求占比数据表*/
	@ApiOperation(value = "新增修改需求占比数据表")
	@PostMapping("saveUpdate")
	public ApiResult saveUpdate(@Valid @RequestBody SaveUpdateDemandProportionDataDto saveUpdateDemandDimensionalityDto) {
		return planningDemandProportionDataService.saveUpdate(saveUpdateDemandDimensionalityDto);
	}

	/*删除需求维度数据*/
	@ApiOperation(value = "删除需求维度数据")
	@DeleteMapping("/del")
	public ApiResult delPlanningBand(@Valid @NotBlank(message = "编号id不能为空") String id) {
		return planningDemandProportionDataService.del(id);
	}


	/*维度相关接口*/


	/*获取维度列表*/
	@ApiOperation(value = "获取维度列表")
	@GetMapping("/getDimensionalityList")
	public ApiResult getDimensionalityList(QueryPlanningDimensionalityDto queryDemandDimensionalityDto) {
		return planningDimensionalityService.getDimensionalityList(queryDemandDimensionalityDto);
	}

	/*获取穿梭框表添加维度标签*/
	@ApiOperation(value = "获取穿梭框表维度标签")
	@GetMapping("/getFormDimensionality")
	public ApiResult getFormDimensionality(QueryPlanningDimensionalityDto queryDemandDimensionalityDto) {
		return planningDimensionalityService.getFormDimensionality(queryDemandDimensionalityDto);
	}

	/*新增删除维度标签*/
	@ApiOperation(value = "新增删除维度标签")
	@PostMapping("/saveBatchDimensionality")
	public ApiResult saveBatchDimensionality(@RequestBody List<SaveDelDimensionalityDto> list) {
		return planningDimensionalityService.saveBatchDimensionality(list);
	}

	@ApiOperation(value = "保存/编辑维度标签")
	@PostMapping("/saveDimensionality")
	public ApiResult saveDimensionality(@Valid @RequestBody UpdateDimensionalityDto updateDimensionalityDto) {
		return planningDimensionalityService.saveDimensionality(updateDimensionalityDto);
	}

	/*删除维度标签*/
	@ApiOperation(value = "删除维度标签")
	@DeleteMapping("/delDimensionality")
	public ApiResult delDimensionality(@Valid @NotBlank(message = "编号id不能为空") String id) {
		return planningDimensionalityService.delDimensionality(id);
	}


}































