/******************************************************************************
* Copyright (C) 2018 广州尚捷科技有限责任公司
* All Rights Reserved.
* 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
* 不得使用、复制、修改或发布本软件.
*****************************************************************************/
package com.base.sbc.module.basicsdatum.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.Page;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumBrandSeasonDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumBrandSeason;
import com.base.sbc.module.basicsdatum.service.BasicsdatumBrandSeasonService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
* 类描述：品牌-季度表 Controller类
* @address com.base.sbc.module.basicsdatum.web.BasicsdatumBrandSeasonController
* @author 谭博文
* @email your email
* @date 创建时间：2024-4-9 9:42:49
* @version 1.0
*/
@RestController
@Api(tags = "品牌-季度表")
@RequestMapping(value = BaseController.SAAS_URL + "/basicsdatumBrandSeason", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class BasicsdatumBrandSeasonController extends BaseController {

	@Autowired
	private BasicsdatumBrandSeasonService basicsdatumBrandSeasonService;

	@ApiOperation(value = "分页查询")
	@GetMapping
	public PageInfo<BasicsdatumBrandSeason> page(BasicsdatumBrandSeasonDto basicsdatumBrandSeasonDto) {
		if (null == basicsdatumBrandSeasonDto) {
			return null;
		}
		QueryWrapper<BasicsdatumBrandSeason> queryWrapper = new QueryWrapper<>();
		if (StringUtils.isNotBlank(basicsdatumBrandSeasonDto.getBrand())) {
			queryWrapper.eq("brand", basicsdatumBrandSeasonDto.getBrand());
		}
		if (StringUtils.isNotBlank(basicsdatumBrandSeasonDto.getSeason())) {
			queryWrapper.eq("season", basicsdatumBrandSeasonDto.getSeason());
		}
		if (StringUtils.isNotBlank(basicsdatumBrandSeasonDto.getMonth())) {
			queryWrapper.eq("month", basicsdatumBrandSeasonDto.getMonth());
		}
		List<BasicsdatumBrandSeason> list = basicsdatumBrandSeasonService.list();
		return new PageInfo<>(list);
	}

	@ApiOperation(value = "明细-通过id查询")
	@GetMapping("/{id}")
	public BasicsdatumBrandSeason getById(@PathVariable("id") String id) {
		return basicsdatumBrandSeasonService.getById(id);
	}

/*	@ApiOperation(value = "条件查询")
	@GetMapping("/getBrandSeason")
	public ApiResult getBrandSeason(@RequestBody BasicsdatumBrandSeason basicsdatumBrandSeason) {
		if (null == basicsdatumBrandSeason) {
			return ApiResult.error("查询条件为空", 500);
		}
		QueryWrapper<BasicsdatumBrandSeason> queryWrapper = new QueryWrapper<>();
		if (StringUtils.isNotBlank(basicsdatumBrandSeason.getBrand())) {
			queryWrapper.eq("brand", basicsdatumBrandSeason.getBrand());
		}
		if (StringUtils.isNotBlank(basicsdatumBrandSeason.getSeason())) {
			queryWrapper.eq("season", basicsdatumBrandSeason.getSeason());
		}
		return selectSuccess(basicsdatumBrandSeasonService.list(queryWrapper));
	}*/

	@ApiOperation(value = "删除-通过id查询,多个逗号分开")
	@DeleteMapping("/{id}")
	public Boolean removeById(@PathVariable("id") String id) {
		List<String> ids = StringUtils.convertList(id);
		return basicsdatumBrandSeasonService.removeByIds(ids);
	}

	@ApiOperation(value = "保存")
	@PostMapping("/add")
	public ApiResult save(@RequestBody BasicsdatumBrandSeason basicsdatumBrandSeason) {
		return basicsdatumBrandSeasonService.addAndUpdateBasicsdatumBrandSeason(basicsdatumBrandSeason);
	}

	@ApiOperation(value = "修改")
	@PostMapping("/update")
	public ApiResult update(@RequestBody BasicsdatumBrandSeason basicsdatumBrandSeason) {
		return basicsdatumBrandSeasonService.addAndUpdateBasicsdatumBrandSeason(basicsdatumBrandSeason);
	}

}































