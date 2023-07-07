/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.controller;

import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.common.dto.IdDto;
import com.base.sbc.module.common.dto.IdsDto;
import com.base.sbc.module.pack.dto.PackInfoSearchPageDto;
import com.base.sbc.module.pack.service.PackInfoService;
import com.base.sbc.module.pack.vo.PackInfoListVo;
import com.base.sbc.module.pack.vo.SampleDesignPackInfoListVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 类描述：资料包 Controller类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.pack.web.PackInfoController
 * @email your email
 * @date 创建时间：2023-7-6 17:13:01
 */
@RestController
@Api(tags = "资料包-基础信息")
@RequestMapping(value = BaseController.SAAS_URL + "/packInfo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class PackInfoController {

	@Autowired
	private PackInfoService packInfoService;

	@ApiOperation(value = "分页查询")
	@GetMapping
	public PageInfo<SampleDesignPackInfoListVo> pageBySampleDesign(@Valid PackInfoSearchPageDto pageDto) {
		return packInfoService.pageBySampleDesign(pageDto);
	}

	@ApiOperation(value = "新建BOM(通过样衣设计)")
	@GetMapping("/createBySampleDesign")

	public PackInfoListVo createBySampleDesign(@Valid IdDto idDto) {
		return packInfoService.createBySampleDesign(idDto.getId());
	}

	@ApiOperation(value = "删除-通过id查询,多个逗号分开")
	@DeleteMapping()
	public Boolean removeById(@Valid IdsDto ids) {
		return packInfoService.removeByIds(StringUtils.convertList(ids.getId()));
	}
}































