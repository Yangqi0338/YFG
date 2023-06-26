/******************************************************************************
* Copyright (C) 2018 广州尚捷科技有限责任公司
* All Rights Reserved.
* 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
* 不得使用、复制、修改或发布本软件.
*****************************************************************************/
package com.base.sbc.module.basicsdatum.controller;

import java.util.List;

import javax.validation.Valid;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.module.basicsdatum.dto.AddRevampConfigPrintDto;
import com.base.sbc.module.basicsdatum.dto.QueryRevampConfigPrintDto;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.basicsdatum.entity.ConfigPrint;
import com.base.sbc.module.basicsdatum.service.ConfigPrintService;
import com.base.sbc.module.basicsdatum.vo.ConfigPrintVo;
import com.github.pagehelper.PageInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
* 类描述：打印配置 Controller类
* @address com.base.sbc.module.basicsdatum.web.ConfigPrintController
* @author shenzhixiong
* @email 731139982@qq.com
* @date 创建时间：2023-6-21 10:29:11
* @version 1.0
*/
@RestController
@Api(tags = "打印配置")
@RequestMapping(value = BaseController.SAAS_URL + "/configPrint", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class ConfigPrintController{

	@Autowired
	private ConfigPrintService configPrintService;


	@ApiOperation(value = "分页查询")
	@GetMapping("/getConfigPrintList")
	public PageInfo<ConfigPrintVo> getConfigPrintList(QueryRevampConfigPrintDto dto) {
		return configPrintService.getConfigPrintList(dto);
	}

	@ApiOperation(value = "批量启用/停用", notes = "ids:, status:0启用1停用")
	@PostMapping("/startStopConfigPrint")
	public Boolean startStopConfigPrint(@Valid @RequestBody StartStopDto dto) {
		return configPrintService.startStopConfigPrint(dto);
	}

	@ApiOperation(value = "保存打印配置")
	@PostMapping("/addRevampConfigPrint")
	public Boolean addRevampConfigPrint(@Valid @RequestBody AddRevampConfigPrintDto dto) {
		return configPrintService.addRevampConfigPrint(dto);
	}

	@ApiOperation(value = "删除打印配置")
	@DeleteMapping("/delConfigPrint")
	public Boolean delConfigPrint(@RequestParam(value = "id") @NotBlank(message = "编号id不能为空") String id) {
		return configPrintService.delConfigPrint(id);
	}

	@ApiOperation(value = "明细-通过id查询")
	@GetMapping("/{id}")
	public ConfigPrint getById(@PathVariable("id") String id) {
		return configPrintService.getById(id);
	}

	@ApiOperation(value = "列表-按单据类型查询")
	@GetMapping("/getConfigPrintListByBillType")
	public List<ConfigPrintVo> getConfigPrintListByBillType(
			@RequestParam(value = "billType") @NotBlank(message = "单据类型") String billType) {
		List<ConfigPrint> list = configPrintService.list(new QueryWrapper<ConfigPrint>()
				.select("id,sort,bill_type,code,name,file_name,print_type,data_json,select_flag,stop_flag,remarks")
				.eq("bill_type", billType).orderByAsc("sort"));
		return CopyUtil.copy(list, ConfigPrintVo.class);
	}

}































