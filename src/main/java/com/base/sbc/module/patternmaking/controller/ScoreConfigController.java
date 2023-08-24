/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.patternmaking.controller;

import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.common.dto.IdsDto;
import com.base.sbc.module.patternmaking.dto.ScoreConfigSearchDto;
import com.base.sbc.module.patternmaking.entity.ScoreConfig;
import com.base.sbc.module.patternmaking.service.ScoreConfigService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 类描述：默认评分配置 Controller类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.patternmaking.web.ScoreConfigController
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-8-24 13:37:10
 */
@RestController
@Api(tags = "默认评分配置")
@RequestMapping(value = BaseController.SAAS_URL + "/scoreConfig", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class ScoreConfigController {

	@Autowired
	private ScoreConfigService scoreConfigService;

	@ApiOperation(value = "分页查询")
	@GetMapping
	public PageInfo<ScoreConfig> page(ScoreConfigSearchDto dto) {
		return scoreConfigService.pageByDto(dto);
	}

	@ApiOperation(value = "明细-通过id查询")
	@GetMapping("/{id}")
	public ScoreConfig getById(@PathVariable("id") String id) {
		return scoreConfigService.getById(id);
	}

	@ApiOperation(value = "查询默认分数")
	@GetMapping("/findOne")
	public ScoreConfig findOne(ScoreConfigSearchDto dto) {
		return scoreConfigService.findOne(dto);
	}

	@ApiOperation(value = "删除-通过id查询,多个逗号分开")
	@DeleteMapping()
	public Boolean removeById(@Validated IdsDto dto) {
		List<String> ids = StringUtils.convertList(dto.getId());
		return scoreConfigService.removeByIds(ids);
	}

	@ApiOperation(value = "保存")
	@PostMapping
	public ScoreConfig save(@RequestBody ScoreConfig scoreConfig) {
		scoreConfigService.saveBy(scoreConfig);
		return scoreConfig;
	}

	@ApiOperation(value = "修改分数")
	@PutMapping
	public boolean updateScore(@RequestBody ScoreConfig scoreConfig) {
		return scoreConfigService.updateScore(scoreConfig);
	}

}































