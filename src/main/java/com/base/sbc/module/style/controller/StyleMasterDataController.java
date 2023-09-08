/******************************************************************************
* Copyright (C) 2018 广州尚捷科技有限责任公司
* All Rights Reserved.
* 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
* 不得使用、复制、修改或发布本软件.
*****************************************************************************/
package com.base.sbc.module.style.controller;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.Page;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.style.dto.StyleMasterDataSaveDto;
import com.base.sbc.module.style.dto.StyleSaveDto;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.style.entity.StyleMasterData;
import com.base.sbc.module.style.service.StyleMasterDataService;
import com.base.sbc.module.style.vo.StyleMasterDataVo;
import com.base.sbc.module.style.vo.StyleVo;
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
* 类描述：款式主数据 Controller类
* @address com.base.sbc.module.style.web.StyleMasterDataController
* @author mengfanjiang
* @email XX.com
* @date 创建时间：2023-9-7 13:57:43
* @version 1.0
*/
@RestController
@Api(tags = "款式主数据")
@RequestMapping(value = BaseController.SAAS_URL + "/styleMasterData", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class StyleMasterDataController{

	@Autowired
	private StyleMasterDataService styleMasterDataService;




	@ApiOperation(value = "明细信息")
	@GetMapping("/{id}")
	public StyleMasterDataVo getDetail(@PathVariable("id") String id) {
		return styleMasterDataService.getDetail(id);
	}


	@ApiOperation(value = "保存")
	@PostMapping
	public StyleMasterDataVo save(@RequestBody StyleMasterDataSaveDto dto) {
		return styleMasterDataService.saveStyle(dto);
	}




}































