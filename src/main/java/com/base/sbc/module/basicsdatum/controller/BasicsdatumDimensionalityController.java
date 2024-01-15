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
import com.base.sbc.module.basicsdatum.dto.BasicsdatumDimensionalityDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumDimensionality;
import com.base.sbc.module.basicsdatum.service.BasicsdatumDimensionalityService;
import com.base.sbc.module.planning.dto.DimensionLabelsSearchDto;
import com.base.sbc.module.planning.dto.UpdateDimensionalityDto;
import com.base.sbc.module.planning.entity.PlanningDimensionality;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
* 类描述：基础资料-纬度系数表 Controller类
* @address com.base.sbc.module.basicsdatum.web.BasicsdatumDimensionalityController
* @author your name
* @email your email
* @date 创建时间：2024-1-15 14:34:41
* @version 1.0
*/
@RestController
@Api(tags = "基础资料-纬度系数表")
@RequestMapping(value = BaseController.SAAS_URL + "/dimensionality", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class BasicsdatumDimensionalityController{

	@Autowired
	private BasicsdatumDimensionalityService basicsdatumDimensionalityService;

	@ApiOperation(value = "获取围度系数数据")
	@GetMapping("/getDimensionality")
	public Map getDimensionality(BasicsdatumDimensionalityDto dto) {
		return basicsdatumDimensionalityService.getDimensionality(dto);
	}


	@ApiOperation(value = "保存/编辑维度标签")
	@PostMapping("/batchSaveDimensionality")
	public boolean batchSaveDimensionality(@Valid @RequestBody List<BasicsdatumDimensionalityDto> dtoList) {
		return basicsdatumDimensionalityService.batchSaveDimensionality(dtoList);
	}



}































